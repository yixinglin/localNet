package org.hsgt.entities;

import lombok.Data;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.strategy.Strategy;

import java.io.Serializable;

@Data
public class Configure implements Serializable {

    private Offer offer;
    private boolean enabled;
    private Strategy strategy;
}
