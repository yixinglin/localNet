package org.hsgt.entities.pricing;

import lombok.Data;
import org.hsgt.controllers.response.NewOffer;


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
