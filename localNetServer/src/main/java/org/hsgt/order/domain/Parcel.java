package org.hsgt.order.domain;

import lombok.Data;

@Data
public class Parcel {
    String trackId;
    String parcelNumber;
    String reference;
    float weight;
    String comment;
    String location;
}
