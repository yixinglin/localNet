package org.hsgt.core.config;

import org.hsgt.core.enu.ApiKeyType;
import org.hsgt.core.rest.ApiKey;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.utils.IoUtils;
import org.utils.JSON;


@Component
public class AccountConfig {

    private static JSONObject config = null;
    private AccountConfig() {}

    @Value("${app.account.config}")
    public void setConfig(String path) {
        String s = IoUtils.readFile(path);
        AccountConfig.config = new JSONObject(s);
    }

    public static JSONObject getConfigInstance() {
        return config;
    }

    public static ApiKey generateApiKey(ApiKeyType type) {
        JSONObject conf = getConfigInstance();
        ApiKey apiKey = new ApiKey();
        switch (type) {
            case METRO_KEY:
                apiKey.setClientKey(new JSON(conf).read("$.metro.client"));
                apiKey.setSecretKey(new JSON(conf).read("$.metro.secret"));
                apiKey.setAccountName(new JSON(conf).read("$.metro.account"));
                break;
            case KAUFLAND_KEY:
                apiKey.setClientKey(new JSON(conf).read("$.kaufland.client"));
                apiKey.setSecretKey(new JSON(conf).read("$.kaufland.secret"));
                apiKey.setAccountName(new JSON(conf).read("$.kaufland.account"));
                break;
            case GLS_KEY:
                apiKey.setId(new JSON(conf).read("$.gls.shipperId"));
                apiKey.setClientKey(new JSON(conf).read("$.gls.username"));
                apiKey.setSecretKey(new JSON(conf).read("$.gls.password"));
                apiKey.setAccountName(new JSON(conf).read("$.gls.account"));
                break;
            case GLS_TEST_KEY:
                apiKey.setId(new JSON(conf).read("$.gls-test.shipperId"));
                apiKey.setClientKey(new JSON(conf).read("$.gls-test.username"));
                apiKey.setSecretKey(new JSON(conf).read("$.gls-test.password"));
                apiKey.setAccountName(new JSON(conf).read("$.gls-test.account"));
                break;
        }
        return apiKey;
    }
}
