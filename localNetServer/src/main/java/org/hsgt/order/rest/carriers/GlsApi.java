package org.hsgt.order.rest.carriers;

import org.hsgt.core.enu.Env;
import org.hsgt.core.rest.ApiKey;
import org.hsgt.core.rest.ExternelRestAPIGls;

public class GlsApi extends ExternelRestAPIGls implements CarrierApi {
//    protected Logger logger = Logger.loggerBuilder(ExternelRestAPIGls.class);
//    final String baseUrl_prod = "https://api.gls-group.eu/public/v1";
//    final String baseUrl_test = "https://api-qs1.gls-group.eu/public/v1";
    // String baseUrl;
    public GlsApi(ApiKey apiKey, Env env) {
        super(apiKey);
        switch (env) {
            case prod:
                this.baseUrl = this.baseUrl_prod;
                break;
            case test:
                this.baseUrl = baseUrl_test;
                break;
            default:
                baseUrl = baseUrl_test;
        }
    }

//    public Map getHtpHeaders() {
//        String auth = Auth.basicAuth(super.getClient_key(), super.getSecret_key());
//        Map<String, String> headers = ImmutableMap.of(
//                "Host", "api.gls-group.eu",
//                "Accept-Language", "en",
//                "Accept-Encoding", "gzip,deflate",
//                "Accept", "application/json",
//                "Content-Type", "application/json",
//                "Authorization", auth
//        );
//        return headers;
//    }


//    @Override
//    public HttpResponse createParcelLabel(Object params) {
//        String body = (String) params;
//        Map headers = this.getHtpHeaders();
//        HttpResponse resp;
//        String url = String.format("%s/shipments", this.baseUrl);
//        try {
//            this.logger.info("Creating label " + body);
//            resp =  this.methodPostRequest(url, headers, body);
//        } catch (Exception e) {
//            this.logger.error(IoUtils.getStackTrace(e));
//            throw new RuntimeException(e);
//        }
//
//        if (resp.getStateCode() != 201) {
//            this.logger.error("Failed to create GLS parcel label (!201).\n"+body);
//            throw new CreateParcelError(resp.getContent());
//        }
//        return resp;
//    }

//    public String getShippingId() {
//        return super.getId();
//    }
//
//    public String getAccountName() {
//        return super.getAccount_name();
//    }
}
