package org.hsgt.entities.orders;

import lombok.Data;

import java.io.Serializable;

@Data
public class BillingAddress implements Serializable {
    String firstName;
    String lastName;
    String companyName;
    String supplementField;
    String streetName;
    String houseNumber;
    String zipCode;
    String city;
    String stateOrProvince;
    String country;
    String taxNumber;
    String companyVat;
    String email;
    String phone;
}
