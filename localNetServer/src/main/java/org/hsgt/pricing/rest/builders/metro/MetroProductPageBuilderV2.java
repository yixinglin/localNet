package org.hsgt.pricing.rest.builders.metro;

import org.apache.commons.lang3.StringUtils;
import org.hsgt.pricing.BO.Competitor;
import org.hsgt.pricing.BO.ProductPage;
import org.hsgt.pricing.BO.ShippingGroup;
import org.hsgt.pricing.domain.CompetitionDO;
import org.utils.JSON;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetroProductPageBuilderV2 {

    public static final float POSITIVE_INFINITY = 99999;

    private ProductPage productPage;

    public MetroProductPageBuilderV2(String myShopName) {
        this.productPage = new ProductPage();
        CompetitionDO self = new CompetitionDO();
        self.setShopName(myShopName);
        this.productPage.setSelf(Competitor.convertToCompetitor(self));
    }

    public MetroProductPageBuilderV2 pageInfo(String pageJson) {
        JSON jp = new JSON(pageJson);
        productPage.setId(jp.read("$.item.idItem"));
        productPage.setCode(jp.read("$.item.mid"));
        productPage.setProductName(jp.read("$.item.name"));
        String manufacturer = jp.read("$.item.manufacturer.name");
        if (StringUtils.isEmpty(manufacturer)) {
            productPage.setManufacturer("Unknown");
        } else {
            productPage.setManufacturer(manufacturer);
        }
        return this;
    }

    public MetroProductPageBuilderV2 sellers(String page) {
        JSON jp = new JSON(page);
        String mid = jp.read("$.item.mid");
        List<Map> listOffer = jp.read("$.offers", List.class);

        List<Competitor> competitorList = listOffer.stream()
                .map(js -> {
                    Competitor c = convertJsonToCompetitor(js);
                    c.setProductId(mid);
                    return c;
                }).collect(Collectors.toList());
        productPage.setCompetitors(competitorList);
        return this;
    }

    private Competitor convertJsonToCompetitor(Map map) {
        Competitor competitor = new Competitor();
        JSON jp = new JSON(map);
        competitor.setId(jp.read("$.id"));
        competitor.setQuantity(jp.read("$.quantity", Integer.class));
        competitor.setShopName(jp.read("$.organization.shopName"));
        competitor.setPrice2(jp.read("$.originRegionInfo.price.net", Float.class));
        List<Map> volumePrices = jp.read("$.originRegionInfo.volumePrices", List.class);
        if (volumePrices.size() > 0) {
            competitor.setLabel("volume");
            competitor.setPrice1(jp.read("$.originRegionInfo.volumePrices[-1].price.net", Float.class));
        } else {
            competitor.setPrice1(competitor.getPrice2());
        }


        ShippingGroup shippingGroup = ShippingGroup.free();
        Map sgmap = jp.read("$.shippingGroup", Map.class);
        if (sgmap != null) {
            shippingGroup.setId(jp.read("$.shippingGroup.id"));
            shippingGroup.setGroupName(jp.read("$.shippingGroup.name"));
            shippingGroup.setUnitCost(jp.read("$.shippingGroup.shippingCost.unitCost.net", Float.class));
            shippingGroup.setExtraUnitCost(jp.read("$.shippingGroup.shippingCost.extraUnitCost.net", Float.class));
            shippingGroup.setPlatform("metro");
            shippingGroup.setFreeFrom(POSITIVE_INFINITY);
            shippingGroup.setDestCountry(jp.read("$.shippingGroup.shippingCost.destinationRegion.value"));
            Boolean isThresholdEnabled = jp.read("$.shippingGroup.shippingCost.thresholdEnabled", Boolean.class);
            if (isThresholdEnabled) {
                shippingGroup.setMaxShippingCost(jp.read("$.shippingGroup.shippingCost.thresholdAmount.net", Float.class));
            }
            shippingGroup.setOwner(competitor.getShopName());
        } else {

        }

        competitor.setShippingGroup(shippingGroup);

        List<Map> labels = jp.read("$.labels", List.class);
        if (labels != null) {
            competitor.setLabel(jp.read("$.labels[0]"));
        } else if (competitor.getLabel() == null) {
            competitor.setLabel("");
        }

        if (competitor.getShopName().equals(this.productPage.getSelf().getShopName())) {
            this.productPage.setSelf(competitor);
        }

        return competitor;
    }

    public ProductPage build() {
        ProductPage ans = this.productPage;
        this.productPage = new ProductPage();
        return ans;
    }


}
