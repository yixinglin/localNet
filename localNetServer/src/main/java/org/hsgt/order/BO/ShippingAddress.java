package org.hsgt.order.BO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShippingAddress implements Serializable {
    String firstName;
    String lastName;
    String companyName;
    String supplementField;
    String streetName;
    String houseNumber;
    String zipCode;
    String city;
    String country;
    String countryCode;
    String email;
    String phone;
    String mobile;
}
