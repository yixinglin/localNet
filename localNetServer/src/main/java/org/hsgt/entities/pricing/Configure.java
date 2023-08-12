package org.hsgt.entities.pricing;

import lombok.Data;
import org.hsgt.strategy.Strategy;

import java.io.Serializable;

@Data
public class Configure implements Serializable {

    private Offer offer;
    private boolean enabled;
    private Strategy strategy;
}
