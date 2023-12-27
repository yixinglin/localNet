package org.hsgt.core.rest;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import org.net.HttpRequest;
import org.utils.Logger;

import java.util.*;

public class ExternalRestAPIMetro extends HttpRequest {

    protected Logger logger = Logger.loggerBuilder(ExternalRestAPIMetro.class);
    String secret_key;
    String client_key;
    String account_name;
    public ExternalRestAPIMetro(ApiKey apiKey) {
        this.secret_key = apiKey.getSecretKey();
        this.client_key = apiKey.getClientKey();
        this.account_name = apiKey.getAccountName();
    }

     private String signRequest(String method, String uri, String body, long timestamp)  {
         List<String> elem = Arrays.asList(method, uri, body, Long.toString(timestamp));
         String plainText = String.join("\n", elem);
         String sign = Hashing.hmacSha256(this.secret_key.getBytes()).hashBytes(plainText.getBytes()).toString();
         return sign;
     }

     public Map getHttpHeaders(String method, String uri, String body) {
         long timestamp = new Date().getTime()/1000;
         String sign = this.signRequest(method, uri, body, timestamp);
         Map<String, String> headers = ImmutableMap.of("Accept", "application/json",
                 "X-Client-Id", this.client_key, "X-Timestamp", Long.toString(timestamp),
                 "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36",
                 "X-Signature", sign);
         return new HashMap(headers);
     }

    public String accountName() {
        return this.account_name;
    }


}


