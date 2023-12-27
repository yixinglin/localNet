package org.hsgt.order.domain;

import lombok.Data;

import java.util.List;

@Data
public class ParcelLabel {
    String id;  // Label ID
    String shipperId;
    String reference;
    String phone;
    String email;
    String note;
    ShippingAddress shippingAddress;
    List<Parcel> parcels;
    String b64Pdf;
}
