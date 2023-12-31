package org.hsgt.builders;

import org.hsgt.order.domain.Parcel;
import org.hsgt.order.domain.ParcelLabel;
import org.hsgt.order.domain.ShippingAddress;
import org.hsgt.order.services.impl.CarrierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class ParcelLabelBuilderTest {

    @Autowired
    CarrierService carrierService;
    @Test
    public void makeGlsParcelLabelTest() {
        ParcelLabel label = new ParcelLabel();
        ShippingAddress addr = new ShippingAddress();
        Parcel parcel = new Parcel();
        addr.setCompanyName("Company");
        addr.setFirstName("Lisa");
        addr.setLastName("Khali");
        addr.setSupplementField("OG");
        addr.setCity("Hamburg");
        addr.setCountryCode("DE");
        addr.setStreetName("Apple Str.");
        addr.setHouseNumber("12");
        addr.setZipCode("23232");
        parcel.setParcelNumber("3846298347");
        parcel.setComment("HG20er");
        parcel.setWeight(0.8f);
        parcel.setTrackId("MTS2343245");
        label.setId("112");
        label.setReference("O22-123456789");
        label.setShipperId("SHIPID");
        label.setPhone("123123213");
        label.setEmail("231@email.com");
        label.setShippingAddress(addr);
        label.setParcels(Arrays.asList(parcel));
        ParcelLabel body = carrierService.makeGlsParcelLabel(label);

        System.out.println(body);
    }
}
