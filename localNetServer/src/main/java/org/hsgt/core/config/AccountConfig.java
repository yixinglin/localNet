package org.hsgt.core.config;

import org.hsgt.core.rest.ApiKey;
import org.json.JSONObject;
import org.utils.IoUtils;

public class AccountConfig {

    public static final int METRO_KEY = 1;
    public static final int KAUFLAND_KEY = 2;

    private static JSONObject config = null;

    private AccountConfig() {}

    public static JSONObject getConfigInstance() {
        if (config == null) {
            String s = IoUtils.readFile("../data/config.json");
            config = new JSONObject(s);
        }
        return config;
    }

    public static ApiKey generateApiKey(int channel) {
        JSONObject conf = getConfigInstance();
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
