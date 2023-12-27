package org.hsgt.order.services.impl;

import com.google.common.collect.ImmutableMap;
import org.hsgt.order.domain.ParcelLabel;
import org.hsgt.order.domain.ShippingAddress;
import org.springframework.stereotype.Service;
import org.utils.IoUtils;
import org.utils.JSON;

import java.util.Map;


@Service
public class CarrierService {

    // Make GLS label from ParcelLabel class.
    public Map makeGlsParcelLabel(ParcelLabel parcelLabel) {
        GLSParcelLabel glabel = new GLSParcelLabel(parcelLabel);
        String postBody = glabel.getPostBody();
        // Post request to GLS api.
        Map ans = null;
        return ans;
    }

    public Map makeDhlParcelLabel(ParcelLabel parcelLabel) {
        return null;
    }
    public Map makeUpsParcelLabel(ParcelLabel parcelLabel) {
        return null;
    }

}


class GLSParcelLabel {
    ParcelLabel parcelLabel;
    String jsonTemplate;
    public GLSParcelLabel(ParcelLabel parcelLabel) {
        this.parcelLabel = parcelLabel;
        this.jsonTemplate = IoUtils.readFile("res/templatePostBodyGlsParcelLabel.json");
    }
    public String getPostBody() {
        ParcelLabel lab = parcelLabel;
        ShippingAddress addr = lab.getShippingAddress();
        JSON json = new JSON(this.jsonTemplate)
                .set("$.shipperId", lab.getShipperId())
                .set("$.references[0]", lab.getReference())
                .set("$.addresses.delivery.name1", addr.getCompanyName())
                .set("$.addresses.delivery.name2", addr.getFirstName() + " " + addr.getLastName())
                .set("$.addresses.delivery.name3", addr.getSupplementField())
                .set("$.addresses.delivery.street1", addr.getStreetName() + " " + addr.getHouseNumber())
                .set("$.addresses.delivery.country", addr.getCountryCode())
                .set("$.addresses.delivery.zipCode", addr.getZipCode())
                .set("$.addresses.delivery.city", addr.getCity())
                .set("$.addresses.delivery.email", lab.getEmail())
                .set("$.addresses.delivery.phone", lab.getPhone());
        lab.getParcels().stream().forEach(p -> {
            Map m = ImmutableMap.of("weight", p.getWeight(),
                    "comment", p.getComment());
            json.addToArray("$.parcels", m);
        });
        json.delete("$.parcels[0]");
        return json.toString();
    }

}

