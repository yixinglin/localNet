package org.hsgt.api;


import org.net.HttpResponse;
import org.utils.IoUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CachedSellerApi implements SellerApi {

    private final SellerApi api;
    private Path orderDirectory = Paths.get("src/main/resources/hsgt/.cache/metro/orders");
    private Path offerDirectory = Paths.get("src/main/resources/hsgt/.cache/metro/offers");
    private Path shippingGroupDirectory = Paths.get("src/main/resources/hsgt/.cache/metro/shipping");

    public CachedSellerApi(SellerApi api) {
        this.api = api;
        IoUtils.makedir(orderDirectory);
        IoUtils.makedir(offerDirectory);
        IoUtils.makedir(shippingGroupDirectory);

    }

    @Override
    public HttpResponse selectAllOrders() {
        return this.api.selectAllOrders();
    }

    @Override
    public HttpResponse selectOrders(int limit, int offset) {
        return this.api.selectOrders(limit, offset);
    }

    @Override
    public HttpResponse selectAllOffers() {
        return this.api.selectAllOffers();
    }

    @Override
    public HttpResponse selectOffers(int limit, int offset) {
        return this.api.selectOffers(limit, offset);
    }

    @Override
    public HttpResponse selectAllShippingGroups() {
        return this.api.selectAllShippingGroups();
    }

    @Override
    public HttpResponse selectShippingGroups(int limit, int offset) {
        return this.api.selectShippingGroups(limit, offset);
    }

    @Override
    public HttpResponse selectDocById(String id) {
        return this.api.selectDocById(id);
    }

    @Override
    public HttpResponse selectOrderById(String id) {
        Path cachedOrders = Paths.get(this.orderDirectory.toString(), id + ".json");
        if (cachedOrders.toFile().exists()) {
            return new HttpResponse(200, IoUtils.readFile(cachedOrders.toString()));
        } else {
            HttpResponse resp = this.api.selectOrderById(id);
            IoUtils.writeFile(cachedOrders.toString(), resp.getContent());
            return resp;
        }
    }

    @Override
    public HttpResponse selectOfferById(String id) {
        return this.api.selectOfferById(id);
    }

    @Override
    public HttpResponse selectShippingGroupById(String id) {
        return this.api.selectShippingGroupById(id);
//        Path cacheShipping = Paths.get(this.shippingGroupDirectory.toString(), id+".json");
//        if (cacheShipping.toFile().exists()) {
//            return new HttpResponse(200, IoUtils.readFile(cacheShipping.toString()));
//        } else {
//            HttpResponse resp = this.api.selectShippingGroupById(id);
//            IoUtils.writeFile(cacheShipping.toString(), resp.getContent());
//            return resp;
//        }
    }


    @Override
    public HttpResponse selectProductPageById(String id) {
        return this.api.selectProductPageById(id);
    }

    @Override
    public HttpResponse updateOfferById(Object payload, Object default_, boolean actualUpdate) {
        return this.api.updateOfferById(payload, default_, actualUpdate);
    }

    @Override
    public String accountName() {
        return this.api.accountName();
    }
}

