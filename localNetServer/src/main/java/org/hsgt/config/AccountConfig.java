package org.hsgt.config;

import org.hsgt.api.ApiKey;
import org.json.JSONObject;
import org.utils.IoUtils;

public class AccountConfig {

    public static final int METRO_KEY = 1;
    public static final int KAUFLAND_KEY = 2;

    public static ApiKey generateApiKey(int channel) {
        String s = IoUtils.readFile("../data/config.json");
        JSONObject conf = new JSONObject(s);
        ApiKey apiKey = new ApiKey();
        switch (channel) {
            case METRO_KEY:
                apiKey.setClientKey(conf.getJSONObject("metro").getString("client"));
                apiKey.setSecretKey(conf.getJSONObject("metro").getString("secret"));
                apiKey.setAccountName(conf.getJSONObject("metro").getString("account"));
                break;
            case KAUFLAND_KEY:
                apiKey.setClientKey(conf.getJSONObject("kaufland").getString("client"));
                apiKey.setSecretKey(conf.getJSONObject("kaufland").getString("secret"));
                apiKey.setAccountName(conf.getJSONObject("kaufland").getString("account"));
                break;
        }
        return apiKey;
    }
}
