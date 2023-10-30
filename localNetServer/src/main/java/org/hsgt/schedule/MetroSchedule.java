package org.hsgt.schedule;

import org.hsgt.config.MetroPricingConfig;
import org.hsgt.controllers.MetroOfferController;
import org.hsgt.controllers.MetroPriceManagementController;
import org.hsgt.controllers.response.NewOffer;
import org.hsgt.controllers.response.SuggestedPrice;
import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.strategy.Strategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.utils.IoUtils;

import java.util.ArrayList;
import java.util.List;
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

    // public boolean enable_metro_dynamic_pricing = Global.enable_metro_dynamic_pricing;
    // public final long dynamic_time_delay_sec = metroPricingConfig.getDynamicTimeDelaySec(); // Global.dynamic_time_delay_sec;
    public int rounds = 1;

    public List<String> skipProductIds = new ArrayList<>();

    @Scheduled(fixedDelayString = "${pricing.metro.dynamic-task-delay-ms}")  // Time delay in milliseconds
    public void metroDynamicPricingTask() {
        boolean enableMetroDynamicPricing = metroPricingConfig.isEnableDynamicPricing();
        boolean isMocked = metroPricingConfig.isMocked();
        long httpDelay = metroPricingConfig.getDynamicHttpDelayMs();
        if (!enableMetroDynamicPricing )
            return;
        System.out.printf("[%s][#%d] %s\n", IoUtils.currentDateTime(), rounds, "Metro Dynamic Pricing Task Started. ");
        try {
            // Obtain up-to-date offer
            List<Offer> activeListOffer = metroOfferController.selectAll().getData();
            JSONArray currentOfferList = (new JSONObject(Cache.currentMetroOfferList)).getJSONArray("items");
            // Obtain up-to-date data from product page, and shipping groups will be updated.
            // The latest data will be stored in the local database.
            List<ProductPage> productListPage = new ArrayList<>();
            for (Offer o: activeListOffer) {
                try {
                    ProductPage p;
                    if (this.skipProductIds.contains(o.getId())) {
                        System.out.println("Skip " + o.getId());
                        continue;
                    }
                    p = metroOfferController.productPage(o.getId()).getData();
                    List<Competitor> competitors = p.getCompetitors();
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
                    if(!isMocked) IoUtils.delay(httpDelay, httpDelay+8000);
                } catch (Exception e) {
                    // Send  notification to email
                    e.printStackTrace();
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
            this.skipProductIds.addAll(lossSuggestedListPrice);
            System.out.println("Skip: " + String.join(" | ", this.skipProductIds));
            System.out.println("Suggested price approved...");
            for (SuggestedPrice suggested: suggestedListPrice) {
                Offer offer = activeListOffer.stream()
                        .filter(o -> o.getId().equals(suggested.getProductId())).findFirst().get();
                offer.setPrice(suggested.getPrice());
                NewOffer newOffer = NewOffer.from(offer);
                metroPriceManagementController.pricing(newOffer, currentOfferList, null);
                IoUtils.delay(200);
            }

        } catch (AssertionError e) {
            e.printStackTrace();
        } finally {
            rounds += 1;
            System.out.println("Metro Dynamic Pricing Task Finished.");
        }

    }

    @Scheduled(fixedDelay = 2* 3600 *1000)  // Time delay in milliseconds
    public void clearCache() {
        System.out.printf("[%s] %s\n", IoUtils.currentDateTime(), "Clear cache");
        this.skipProductIds = new ArrayList<>();
    }

}
