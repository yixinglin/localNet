package org.hsgt.order.BO;

import lombok.Data;
import org.hsgt.order.domain.AddressDO;

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
    String countryCode;
    String taxNumber;
    String companyVat;
    String email;
    String phone;
    String mobile;

    public static BillingAddress convertToBO(AddressDO item) {
        if (item == null) {
            return null;
        }
        BillingAddress result = new BillingAddress();
        result.setFirstName(item.getFirstName());
        result.setLastName(item.getLastName());
        result.setCompanyName(item.getCompanyName());
        result.setSupplementField(item.getSupplementField());
        result.setStreetName(item.getStreetName());
        result.setHouseNumber(item.getHouseNumber());
        result.setZipCode(item.getZipCode());
        result.setCity(item.getCity());
        result.setStateOrProvince(item.getStateOrProvince());
        result.setCountry(item.getCountry());
        result.setCountryCode(item.getCountryCode());
        result.setTaxNumber(item.getTaxNumber());
        result.setCompanyVat(item.getCompanyVat());
        result.setEmail(item.getEmail());
        result.setPhone(item.getPhone());
        result.setMobile(item.getMobile());
        return result;
    }
}
