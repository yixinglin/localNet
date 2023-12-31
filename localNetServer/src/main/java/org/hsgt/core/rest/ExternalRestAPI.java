package org.hsgt.core.rest;

import lombok.Data;
import org.net.HttpRequest;

@Data
public class ExternalRestAPI extends HttpRequest {
    String id;
    String secret_key;
    String client_key;
    String account_name;
    public ExternalRestAPI(ApiKey apiKey) {
        this.secret_key = apiKey.getSecretKey();
        this.client_key = apiKey.getClientKey();
        this.account_name = apiKey.getAccountName();
        this.id = apiKey.getId();
    }

}
