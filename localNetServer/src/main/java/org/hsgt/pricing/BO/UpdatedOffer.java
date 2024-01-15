package org.hsgt.pricing.BO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hsgt.pricing.controllers.response.NewOffer;
import org.hsgt.pricing.domain.PricingHistoryDo;


import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdatedOffer implements Serializable {
    private int id;
    private String ip;
    private LocalDateTime datetime;
    private NewOffer offer;
    private String note;
    private String username;

    public UpdatedOffer(PricingHistoryDo pricingHistoryDo) {
        this.id = pricingHistoryDo.getId();
        this.ip = pricingHistoryDo.getIp();
        this.datetime = pricingHistoryDo.getDatetime();
        this.note = pricingHistoryDo.getNote();
        this.username = pricingHistoryDo.getUsername();
    }
}
