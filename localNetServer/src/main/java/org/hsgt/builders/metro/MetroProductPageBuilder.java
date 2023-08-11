package org.hsgt.builders.metro;

import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.common.ShippingGroup;
import org.hsgt.entities.pricing.Competitor;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class MetroProductPageBuilder {

    public static final float POSITIVE_INFINITY = 99999;
    private ProductPage productPage;

    public MetroProductPageBuilder(String myShopName) {
        this.productPage = new ProductPage();
        Competitor self = new Competitor();
        self.setShopName(myShopName);
        this.productPage.setSelf(self);
    }

    public MetroProductPageBuilder pageInfo(JSONObject page) {
        JSONObject item = page.getJSONObject("item");
        productPage.setId(item.getString("idItem"));
        productPage.setCode(item.getString("mid"));
        productPage.setProductName(item.getString("name"));
        // this.setEnabled(true);
        if (item.isNull("manufacturer")) {
            productPage.setManufacturer("Unknown");
        } else {
            productPage.setManufacturer(item.getJSONObject("manufacturer").getString("name"));
        }

        return this;
    }

    public MetroProductPageBuilder sellers(JSONObject page) {
        JSONArray offers =page.getJSONArray("offers");
        String mid = page.getJSONObject("item").getString("mid");
        List<Competitor> competitorList = new ArrayList<>();
        for (int i = 0; i < offers.length(); i++) {
            JSONObject offer = offers.getJSONObject(i);
            Competitor competitor = new Competitor();
            competitor.setQuantity(offer.getInt("quantity"));
            competitor.setId(offer.getString("id"));
            competitor.setShopName(offer.getJSONObject("organization").getString("shopName"));
            competitor.setPrice2(offer.getJSONObject("originRegionInfo").getJSONObject("price").getFloat("net"));
            competitor.setProductId(mid);
            /* Set the price on sale */
            JSONArray volumePrices = offer.getJSONObject("originRegionInfo").getJSONArray("volumePrices");
            if (volumePrices.length() > 0) {
                competitor.setLabel("volume");
                float price1 = volumePrices.getJSONObject(volumePrices.length()-1).getJSONObject("price").getFloat("net");
                competitor.setPrice1(price1);
            } else {
                competitor.setPrice1(competitor.getPrice2());
            }

            /* Set the shipping group */
            ShippingGroup shippingGroup = new ShippingGroup();
            shippingGroup.setOwner(competitor.getShopName());
            shippingGroup.setPlatform("metro");
            if (!offer.isNull("shippingGroup")) {
                JSONObject sc = offer.getJSONObject("shippingGroup").getJSONObject("shippingCost");
                shippingGroup.setId(offer.getJSONObject("shippingGroup").getString("id"));
                shippingGroup.setGroupName(offer.getJSONObject("shippingGroup").getString("name"));
                shippingGroup.setUnitCost(sc.getJSONObject("unitCost").getFloat("net"));
                shippingGroup.setExtraUnitCost(sc.getJSONObject("extraUnitCost").getFloat("net"));
                shippingGroup.setPlatform("metro");
                if (sc.getBoolean("thresholdEnabled") ) {
                    shippingGroup.setMaxShippingCost(sc.getJSONObject("thresholdAmount").getFloat("net"));
                }
                shippingGroup.setFreeFrom(POSITIVE_INFINITY);
                shippingGroup.setDestCountry(sc.getJSONObject("destinationRegion").getString("value"));
            }
            competitor.setShippingGroup(shippingGroup);
            /* Set the label for competitors */
            if (!offer.isNull("labels")) {
                competitor.setLabel(offer.getJSONArray("labels").getString(0));
            } else {
                if (competitor.getLabel() == null) {
                    competitor.setLabel("");
                }
            }
            competitorList.add(competitor);
            if (competitor.getShopName().equals(this.productPage.getSelf().getShopName())) {
                this.productPage.setSelf(competitor);
            }
        }

        productPage.setCompetitors(this.sortCompetitors(competitorList));
        return this;
    }

    private List<Competitor> sortCompetitors(List<Competitor> competitors) {
        int i=0;
        // List<Competitor> sortedCompetitors = competitors.stream().sorted(Comparator.comparing(Competitor::getPrice2)).collect(Collectors.toList());
        competitors.sort(Comparator.comparing(o -> o.getPrice2() + o.getShippingGroup().getUnitCost()));
        List<Competitor> sortedCompetitors = competitors;

        Iterator<Competitor> iter = sortedCompetitors.iterator();
        while(iter.hasNext()) {
            iter.next().setRank(i++);
        }
        return sortedCompetitors;
    }

    public ProductPage build() {
        ProductPage ans = this.productPage;
        this.productPage = new ProductPage();
        return ans;
    }
}
