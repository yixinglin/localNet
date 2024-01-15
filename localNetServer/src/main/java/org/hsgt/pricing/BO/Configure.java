package org.hsgt.pricing.BO;

import lombok.Data;
import org.hsgt.pricing.strategy.Strategy;

import java.io.Serializable;

@Data
public class Configure implements Serializable {

    private Offer offer;
    private boolean enabled;
    private Strategy strategy;

}
