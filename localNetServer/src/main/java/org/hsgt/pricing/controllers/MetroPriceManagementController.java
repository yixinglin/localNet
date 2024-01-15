package org.hsgt.pricing.controllers;

import io.swagger.annotations.Api;
import org.hsgt.core.controllers.VueElementAdminLoginController;
import org.hsgt.core.domain.ResponseResult;
import org.hsgt.core.domain.User;
import org.hsgt.core.mapper.UserMapper;
import org.hsgt.pricing.BO.Offer;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.mapper.CompetitorMapper;
import org.hsgt.pricing.mapper.ConfigureMapper;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.OfferService;
import org.hsgt.pricing.services.PriceManagementService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "Price Management" )
@RestController
@RequestMapping("/pricing/metro")
public class MetroPriceManagementController {

    @Autowired
    PriceManagementService priceManagementService;

    @Qualifier("metroOfferService")
    @Autowired
    OfferService offerService;

    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    CompetitorMapper competitorMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    VueElementAdminLoginController loginController;

    @Autowired
    private MetroPricingConfig pricingConfig;

    // private List<String> filterKeywords = Global.pricing_filterKeywords;
    private SellerApi api;
    public MetroPriceManagementController() {
        // api = pricingConfig.getApi();
    }

    // Suggest price based on the data from database.
//    @GetMapping("/suggest")
//    public ResponseResult<SuggestedPrice> suggestPriceToUpdate(String productId) {
//        SuggestedPrice suggestedPrice = priceManagementService.suggestPriceUpdate(productId);
//        ResponseResult<SuggestedPrice> resp = ResponseResult.success().setData(suggestedPrice);
//        return resp;
//    }

//    @GetMapping("/conf")
//    public ResponseResult<List<ConfigureResponse>> getConfiguration() {
//        List<Configure> configureList = priceManagementService.queryAllConfigurations();
//        configureList = configureList.stream().filter(c -> !excluded(c)).collect(Collectors.toList());
//        List<ConfigureResponse> configureResponses = configureList.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());
//        ResponseResult<List<ConfigureResponse>> resp = ResponseResult.success().setData(configureResponses).setLength(configureResponses.size());
//        return resp;
//    }

//    @PostMapping("/conf")
//    public ResponseResult updateConfiguration(@RequestBody List<ConfigureResponse> payload) {
//        List<Configure> conf = payload.stream().map(o -> ConfigureResponse.build(o)).collect(Collectors.toList());
//        // Update t_configure
//        priceManagementService.updateConfiguration(conf);
//        // Update t_offer
//        for (Configure c: conf) {
//            offerService.updateLowestPriceAndNote(c.getOffer());
//        }
//        ResponseResult resp = ResponseResult.success().setLength(conf.size());
//        return resp;
//    }

    @PostMapping("/edit")
    public ResponseResult pricing(@RequestBody NewOffer newOffer, Object offerList, HttpServletRequest httpRequest) {
        String ip = httpRequest != null? httpRequest.getRemoteAddr(): "127.0.0.1";
        Object ans = this.loginController.userInfo(newOffer.getToken()).getData();
        User user = null;
        if (ip.equals("127.0.0.1")) {
           user = userMapper.selectByName("admin");
        }
        if (ans instanceof User) {
            user = (User) ans;
        }

        if (user == null) {
            throw new RuntimeException("Rejected new offer. " + ip + newOffer.toString());
        }
        JSONArray ol = offerList instanceof JSONArray? (JSONArray) offerList: null;
        NewOffer resp = priceManagementService.pricing(newOffer, ol, ip);
        ResponseResult<Offer> cr = ResponseResult.success().setData(resp);
        return cr;
    }

//    public boolean excluded(Configure configure) {
//        List<String> filterKeywords = pricingConfig.getFilterKeywords();
//        return filterKeywords.stream().filter(s -> configure.getOffer().getProductName().toLowerCase().contains(s)).findFirst().isPresent();
//    }
}
