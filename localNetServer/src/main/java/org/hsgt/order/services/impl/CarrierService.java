package org.hsgt.order.services.impl;

import com.google.common.collect.ImmutableMap;
import org.hsgt.order.config.GlsConfig;
import org.hsgt.order.BO.ParcelLabel;
import org.hsgt.order.BO.ShippingAddress;
import org.hsgt.order.rest.carriers.CarrierApi;
import org.hsgt.order.rest.carriers.GlsApi;
import org.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utils.IoUtils;
import org.utils.JSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class CarrierService {

    @Autowired
    GlsConfig glsConfig;
    @Autowired
    CarrierApi glsApi;
    // Make GLS label from ParcelLabel class.
    public ParcelLabel makeGlsParcelLabel(ParcelLabel parcelLabel) {
        GLSParcelLabel glabel = new GLSParcelLabel(parcelLabel);
        // Post request to GLS api.
        // GlsApi api = (GlsApi) glsConfig.getApiInstance();
        GlsApi api = (GlsApi) glsApi;
        String cachePath = glsConfig.getCachePath();
        String postBody = glabel.getPostBody(api.getShippingId());
        HttpResponse resp = api.createParcelLabel(postBody);
        String s = resp.getContent();
        JSON jresp = new JSON(s);

        List<String> b64Labels = jresp.readArray("$.labels");
        parcelLabel.setB64Pdf(b64Labels);
        Iterator<String> iterParcelNumbers = jresp.readArray("$.parcels..parcelNumber").iterator();
        Iterator<String> iterTrackId = jresp.readArray("$.parcels..trackId").iterator();
        parcelLabel.getParcels().stream().forEach(p -> {
            p.setParcelNumber(iterParcelNumbers.next());
            p.setTrackId(iterTrackId.next());
        });
        Path saveTo = Paths.get(cachePath, String.format("%s.json", parcelLabel.getReference()));
        IoUtils.writeFile(saveTo.toFile(), s);
        return parcelLabel;
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
    public String getPostBody(String shipperId) {
        ParcelLabel lab = parcelLabel;
        ShippingAddress addr = lab.getShippingAddress();
        JSON json = new JSON(this.jsonTemplate)
                .set("$.shipperId", shipperId)
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

