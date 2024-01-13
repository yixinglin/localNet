package org.hsgt.core.rest;

import com.google.common.collect.ImmutableMap;
import org.hsgt.core.exceptions.CreateParcelError;
import org.net.HttpResponse;
import org.utils.Auth;
import org.utils.IoUtils;
import org.utils.Logger;

import java.util.Map;

public class ExternelRestAPIGls extends ExternalRestAPI {
    protected Logger logger = Logger.loggerBuilder(ExternelRestAPIGls.class);

    protected final String baseUrl_prod = "https://api.gls-group.eu/public/v1";
    protected final String baseUrl_test = "https://api-qs1.gls-group.eu/public/v1";

    protected String baseUrl;
    public ExternelRestAPIGls(ApiKey apiKey) {
        super(apiKey);
    }

    public Map getHtpHeaders() {
        String auth = Auth.basicAuth(super.getClient_key(), super.getSecret_key());
        Map<String, String> headers = ImmutableMap.of(
                "Host", "api.gls-group.eu",
                "Accept-Language", "en",
                "Accept-Encoding", "gzip,deflate",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "Authorization", auth
        );
        return headers;
    }

    public HttpResponse createParcelLabel(Object params) {
        String body = (String) params;
        Map headers = this.getHtpHeaders();
        HttpResponse resp;
        String url = String.format("%s/shipments", this.baseUrl);
        try {
            this.logger.info("Creating label " + body);
            resp =  this.methodPostRequest(url, headers, body);
        } catch (Exception e) {
            this.logger.error(IoUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }

        if (resp.getStateCode() != 201) {
            this.logger.error("Failed to create GLS parcel label (!201).\n"+body);
            throw new CreateParcelError(resp.getContent());
        }
        return resp;
    }

    public String getShippingId() {
        return super.getId();
    }

    public String getAccountName() {
        return super.getAccount_name();
    }

}
