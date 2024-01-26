package org.hsgt.pricing.BO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hsgt.pricing.controllers.response.NewOffer;

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

}
