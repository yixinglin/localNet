package org.hsgt.order.rest.carriers;

import org.hsgt.core.rest.ApiKey;
import org.hsgt.order.config.GlsConfig;
import org.json.JSONObject;
import org.net.HttpRequest;
import org.net.HttpResponse;
import org.utils.IoUtils;

public class GlsMockApi extends HttpRequest implements CarrierApi {
    String cachePath;
    public GlsMockApi(GlsConfig glsConfig, ApiKey key) {
        // super(key, Env.test);
        this.cachePath = glsConfig.getCachePath();
    }
    @Override
    public HttpResponse createParcelLabel(Object params) {
        String s = IoUtils.readFile(cachePath + "/mock.json");
        System.out.println("Mock: " + new JSONObject(s));
        HttpResponse httpResponse = new HttpResponse(201, s);
        return httpResponse;
    }
}
