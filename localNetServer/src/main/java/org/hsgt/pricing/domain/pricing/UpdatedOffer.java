package org.hsgt.pricing.domain.pricing;

import lombok.Data;
import org.hsgt.pricing.controllers.response.NewOffer;


import java.io.Serializable;

@Data
public class UpdatedOffer implements Serializable {
    private int id;
    private String ip;
    private String datetime;
    private NewOffer offer;
    private String note;
    private String username;
}
