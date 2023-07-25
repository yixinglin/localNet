package org.hsgt.entities.pricing;

import lombok.Data;
import org.hsgt.entities.pricing.Offer;

import java.io.Serializable;

@Data
public class UpdatedOffer implements Serializable {
    private int id;
    private String ip;
    private String dateTime;
    private Offer offer;
}
