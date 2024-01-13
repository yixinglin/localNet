package org.hsgt.pricing.schedule;

import org.hsgt.core.config.AccountConfig;
import org.hsgt.core.domain.Email;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.controllers.MetroOfferController;
import org.hsgt.pricing.controllers.MetroPriceManagementController;
import org.hsgt.pricing.controllers.response.ConfigureResponse;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.controllers.response.SuggestedPrice;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.domain.ProductPage;
import org.hsgt.pricing.domain.pricing.Competitor;
import org.hsgt.pricing.strategy.Strategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.utils.EmailClient;
import org.utils.IoUtils;
import org.utils.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class MetroSchedule {
    @Autowired
    MetroOfferController metroOfferController;
    @Autowired
    MetroPriceManagementController metroPriceManagementController;
    @Autowired
    MetroPricingConfig metroPricingConfig;

    public int rounds = 1;
    public List<String> skipProductIds = new ArrayList<>();
    Logger logger = Logger.loggerBuilder(MetroSchedule.class);

    @Scheduled(initialDelay = 10000, fixedDelayString = "${pricing.metro.dynamic-task-delay-ms}")  // Time delay in milliseconds
    public void metroDynamicPricingTask() {
        boolean enableMetroDynamicPricing = metroPricingConfig.isEnableDynamicPricing();
        boolean isMocked = metroPricingConfig.isMocked();
        long httpDelay = metroPricingConfig.getDynamicHttpDelayMs();
        long httpDelayMax = metroPricingConfig.getDynamicHttpDelayMax();
        if (!enableMetroDynamicPricing )
            return;
        System.out.printf("[%s][#%d] %s\n", IoUtils.currentDateTime(), rounds, "Metro Dynamic Pricing Task Started. ");
        try {
            // Obtain up-to-date offers
            List<Offer> activeListOffer = metroOfferController.selectAll().getData();
            JSONArray currentOfferList = (new JSONObject(Cache.currentMetroOfferList)).getJSONArray("items");
            List<ConfigureResponse> listConfigureResp = metroPriceManagementController.getConfiguration().getData();
            Map<String, List<ConfigureResponse>> configureRespMap = listConfigureResp.stream().collect(Collectors.groupingBy(ConfigureResponse::getProductId));
            // Exclude offers disabled in the configuration.
            activeListOffer = activeListOffer.stream()
                    .filter(o -> configureRespMap.get(o.getId()).get(0).isEnabled())
                    .collect(Collectors.toList());
            // Obtain up-to-date data from product page, and shipping groups will be updated.
            // The latest data will be stored in the local database.
            List<ProductPage> productListPage = new ArrayList<>();
            for (Offer o: activeListOffer) {
                try {
                    ProductPage p;
                    // Skip specified offers
                    if (this.skipProductIds.contains(o.getId())) {
                        System.out.println("Skip " + o.getId());
                        continue;
                    }
                    p = metroOfferController.productPage(o.getId()).getData();
                    List<Competitor> competitors = p.getCompetitors();
                    // Skip offers without competitors.
                    if (competitors == null  || competitors.size() <= 1) {
                        System.out.println("ADD ID to SkipProductIds " + o.getId());
                        this.skipProductIds.add(p.getCode());
                        continue;
                    }
                    Competitor top1 = competitors.stream().findFirst().get();
                    String pr = String.format("%s | %.2f | %s\n%-15s", p.getCode(), top1.getPrice1(), top1.getShopName(),
                            p.getProductName());
                    productListPage.add(p);
                    System.out.println(pr);
                    if(!isMocked) IoUtils.delay(httpDelay, httpDelay+httpDelayMax);
                } catch (Exception e) {
                    // Send  notification to email
                    this.notification("Fail to parse product page.", IoUtils.getStackTrace(e));
                }
            }
            System.out.println("Pricing Preprocessing...");
            List<SuggestedPrice> sp0 = productListPage.stream()
                    .map(p -> metroPriceManagementController.suggestPriceToUpdate(p.getCode()).getData())
                    .collect(Collectors.toList());
            List<SuggestedPrice> suggestedListPrice = sp0.stream().filter(p -> p.getStatus() == Strategy.APPROVED)
                    .collect(Collectors.toList());
            List<String> lossSuggestedListPrice = sp0.stream().filter(p -> p.getStatus() == Strategy.REJECTED_LOSS
                            || p.getStatus() == Strategy.REJECTED_RISK_LOSS
                            || p.getStatus() == Strategy.REJECTED_NO_COMPETITORS
                            || p.getStatus() == Strategy.REJECTED_INVALID_PRICE)
                    .map(p -> p.getProductId())
                    .collect(Collectors.toList());
            // Disable specified offers temporarily to avoid frequent API access.
            this.skipProductIds.addAll(lossSuggestedListPrice);
            this.logger.info("Skip: " + String.join(" | ", this.skipProductIds));
            System.out.println("Suggested price approved...");
            for (SuggestedPrice suggested: suggestedListPrice) {
                NewOffer newOffer = activeListOffer.stream()
                        .filter(o -> o.getId().equals(suggested.getProductId()))
                        .map(o -> NewOffer.from(o)).findFirst().get();
                newOffer.setPrice(suggested.getPrice());
                metroPriceManagementController.pricing(newOffer, currentOfferList, null);
                IoUtils.delay(200);
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            notification("Fail to parse product page.", IoUtils.getStackTrace(e));
        } finally {
            rounds += 1;
            System.out.println("Metro Dynamic Pricing Task Finished.");
        }
    }

    public void notification(String title, String message)  {
        if (this.metroPricingConfig.isEnableEmailNotification()) {
            JSONObject config = AccountConfig.getConfigInstance();
            JSONObject notification = config.getJSONObject("notification");
            JSONObject outgoing = notification.getJSONObject("outgoing");
            String receiver = notification.getJSONObject("receiver").getJSONArray("emails").getString(0);
            Email sender = (Email) IoUtils.jsonToObject(outgoing, Email.class);
            sender.setSubject(title);
            sender.setBody(message);
            EmailClient client = new EmailClient(sender);
            client.applySmtpSession();
            try {
                client.send(InternetAddress.parse(receiver));
            } catch (UnsupportedEncodingException | MessagingException e) {
                this.logger.error("Failed to send email. " + message);
            }
        } else {
            this.logger.error("Failed to send email. " + message);
        }
    }

    @Scheduled(fixedDelay = 2* 3600 *1000)  // Time delay in milliseconds
    public void clearCache() {
        System.out.printf("[%s] %s\n", IoUtils.currentDateTime(), "Clear cache");
        this.skipProductIds = new ArrayList<>();
    }

}
