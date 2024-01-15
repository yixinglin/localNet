package org.hsgt.pricing.BO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hsgt.pricing.domain.CompetitionDO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Competitor implements Serializable {
    private String id;        // seller id
    private String productId; //
    private Float price1;    // Price on sale
    private Float price2;    // Original price.
    private Integer quantity;
    private String label;
    private String shopName;
    private Integer rank;
    private LocalDateTime datetime;
    private ShippingGroup shippingGroup;

    public static Competitor convertToCompetitor(CompetitionDO item) {
        if (item == null) {
            return null;
        }
        Competitor result = new Competitor();
        // result.setId(item.getProductId());
        result.setProductId(item.getProductId());
        result.setPrice1(item.getPrice());
        result.setPrice2(item.getPrice());
        result.setQuantity(item.getQuantity());
        result.setLabel(item.getLabel());
        result.setShopName(item.getShopName());
        result.setRank(item.getRank_());
        result.setDatetime(item.getDatetime());
        // result.setShippingGroup(item.);
        return result;
    }

    public static CompetitionDO convertToCompetitionDO(Competitor item) {
        if (item == null) {
            return null;
        }
        CompetitionDO result = new CompetitionDO();
        result.setProductId(item.getProductId());
        result.setShopName(item.getShopName());
        result.setRank_(item.getRank());
        result.setPrice(item.getPrice2());
        result.setQuantity(item.getQuantity());
        result.setLabel(item.getLabel());
        result.setShippingGroupId(item.getShippingGroup().getId());
        result.setDatetime(item.getDatetime());
        return result;
    }
}
