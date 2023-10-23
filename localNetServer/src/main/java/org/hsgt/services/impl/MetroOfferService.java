package org.hsgt.services.impl;

import org.hsgt.api.SellerApi;
import org.hsgt.builders.metro.MetroOfferBuilder;
import org.hsgt.config.Global;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Configure;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.ConfigureMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.hsgt.mappers.SqlService;
import org.hsgt.services.OfferService;
import org.hsgt.strategy.TotalPriceStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.Logger;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MetroOfferService implements OfferService {

    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private ShippingGroupMapper shippingGroupMapper;
    @Autowired
    private ConfigureMapper configureMapper;
    private SellerApi api;

    public MetroOfferService() {
        this.api = Global.getMetroApiInstance();
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
        String s = this.api.selectAllOffers().getContent();
        MetroOfferBuilder builder = new MetroOfferBuilder();
        JSONArray jOfferList = new JSONObject(s).getJSONArray("items");
        List<Offer> offers =  new ArrayList<>();
        // Offers without concrete shipping group details
        for (int i = 0; i < jOfferList.length(); i++) {
            // Convert JSON to Offer object
            Offer o = builder.offer(jOfferList.getJSONObject(i)).build();
            // Update offer to database
            this.offerToDataBase(o);
            o.setShippingGroup(null);
            if (o.getQuantity() > 0)
                offers.add(o);
        }
        Set idSet = offers.stream().map(o -> o.getId()).collect(Collectors.toSet());
        // Offer data from database
        List<Offer>  offersFromDB = this.offerMapper.selectList(null);
        // Sort
        offersFromDB = offersFromDB.stream()
                .filter(o -> idSet.contains(o.getId()))
                .sorted(Comparator.comparing(Offer::getNote).thenComparing(Offer::getPrice))
                .collect(Collectors.toList());

        assert offersFromDB.size() == offers.size(): "The size of acquired offers must be the same as acquired from API";
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
        // Insert to t_shippinggroup
        ShippingGroup sg = offer.getShippingGroup();
        sg.setOwner(this.api.accountName());
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
