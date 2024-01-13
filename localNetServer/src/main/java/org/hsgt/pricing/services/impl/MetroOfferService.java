package org.hsgt.pricing.services.impl;

import org.hsgt.core.mapper.SqlService;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.domain.ShippingGroup;
import org.hsgt.pricing.domain.pricing.Configure;
import org.hsgt.pricing.mapper.ConfigureMapper;
import org.hsgt.pricing.mapper.OfferMapper;
import org.hsgt.pricing.mapper.ShippingGroupMapper;
import org.hsgt.pricing.rest.builders.metro.MetroOfferBuilder;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.schedule.Cache;
import org.hsgt.pricing.services.OfferService;
import org.hsgt.pricing.strategy.TotalPriceStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class MetroOfferService implements OfferService {

    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private ShippingGroupMapper shippingGroupMapper;
    @Autowired
    private ConfigureMapper configureMapper;

//    @Autowired
//    private MetroPricingConfig pricingConfig;

    @Autowired
    private SellerApi metroOfferSellerApi;

    Logger logger = Logger.loggerBuilder(MetroOfferService.class);
    public MetroOfferService() {
    }

    /*
     * @param :
      * @return List<Offer>
     * @author Lin
     * @description TODO [UTD] Get offer data from api. Hence, the data is up-to-date.
     *               After that the latest offer will be stored in the database.
     * @date 16.Aug.2023 016 01:58
     */
    public List<Offer> queryById(List<String> ids) {
        SellerApi api = metroOfferSellerApi;
        String s = api.selectAllOffers().getContent();
        Cache.currentMetroOfferList = s;
        this.logger.info("@@ Saved [currentMetroOfferList] to Cache");
        MetroOfferBuilder builder = new MetroOfferBuilder();
        JSONArray jOfferList = new JSONObject(s).getJSONArray("items");
        List<Offer> offers =  new ArrayList<>();
        // Offers without concrete shipping group details
        for (int i = 0; i < jOfferList.length(); i++) {
            // Convert JSON to Offer object
            Offer o = builder.offer(jOfferList.getJSONObject(i)).build();
            o.setActive(true);
            // Update offer to database
            this.offerToDataBase(o);
            o.setShippingGroup(null);
            if (o.getQuantity() > 0)
                offers.add(o);
        }
        Set idSet = offers.stream().map(o -> o.getId()).collect(Collectors.toSet());  // A set of activated product ID
        // Offer data from database
        List<Offer>  offersFromDB0 = this.offerMapper.selectList(null);
        // Sort
        List<Offer> offersFromDB = offersFromDB0.stream()
                .filter(o -> idSet.contains(o.getId())) // A set of product ID
                .sorted(Comparator.comparing(Offer::getNote).thenComparing(Offer::getPrice))
                .collect(Collectors.toList());

        assert offersFromDB.size() == offers.size(): "The size of acquired offers must be the same as acquired from API";

        // Set deactivated offer
        List<Offer> deactivatedOffer = offersFromDB0.stream().filter(o -> !idSet.contains(o.getId())).collect(Collectors.toList());
        if (deactivatedOffer.size() > 0) {
            for (Offer o: deactivatedOffer) {
                o.setActive(false);
                this.offerToDataBase(o);
            }
        }

        return offersFromDB;
    }

    /*
     * @param id:
     * @return Offer
     * @author Lin
     * @description TODO [UTD] Get offer data from api. Hence, the data is up-to-date.
     * @date 16.Aug.2023 016 02:31
     */
    @Override
    public Offer queryById(String id) {
        List<Offer> offers = this.queryById((List<String>) null);
        List<Offer> filtered = offers.stream().filter( o -> o.getId().equals(id)).collect(Collectors.toList());
        Offer ret;
        if (filtered != null) {
            ret = filtered.get(0);
        } else {
            throw new RuntimeException(String.format("The offer with id [%s] does not exist!", id));
        }
        return ret;
    }

    /*
     * @param offer: Offer to update
      * @return void
     * @author Lin
     * @description TODO Insert up-to-date offer to database,
     *               including Offer, Configuration, ShippingGroup ID.
     *               Note that the data of shipping groups is incomplete
     * @date 16.Aug.2023 016 02:01
     */
    private void offerToDataBase(Offer offer) {
        SellerApi api = metroOfferSellerApi;
        // Insert to t_shippinggroup
        ShippingGroup sg = offer.getShippingGroup();
        sg.setOwner(api.accountName());
        SqlService.sqlInsertOrSkip(sg, shippingGroupMapper);
        // Update or insert to t_offer
        offer.setLowestPrice(offer.getPrice() * 0.8f);
        SqlService.sqlInsertOrUpdate(offer, offerMapper);
        // Insert to t_configure
        Configure configure = new Configure();
        configure.setOffer(offer);
        configure.setStrategy(new TotalPriceStrategy(0.01f, 0.5f));
        configure.setEnabled(false);
        SqlService.sqlInsertOrSkip(configure, configureMapper);
    }

    /*
     * @param :
      * @return List<String>  A list of IDs
     * @author Lin
     * @description TODO Query offers from database. The acquired data could not be up-to-date.
     * @date 16.Aug.2023 016 02:04
     */
    public List<Offer> queryAll() {
        List<Offer> offers = offerMapper.selectList(null);
        return offers;
    }

    @Override
    public void updateLowestPriceAndNote(Offer offer) {
        assert offer != null;
        assert offer.getLowestPrice() >= 0;
        assert offer.getNote() != null;
        assert offer.getAmount() >= 0;
        Offer o = offerMapper.selectById(offer);
        if (o == null) {
            Logger.loggerBuilder(getClass()).error("Offer dose not exist!");
            throw new RuntimeException("Offer dose not exist!" + offer.getId());
        } else {
            offerMapper.updateLowestPriceAndNote(offer);
        }
    }
}
