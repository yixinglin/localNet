package org.hsgt.api;

public class SellerApiFactory {

    public static SellerApi createSellerApi(int channel, ApiKey apiKey) {
        SellerApi api;
        switch (channel) {
            case SellerApi.METRO:
                api = new MetroSellerApi(apiKey);
                break;
            case SellerApi.KAUFLAND:
                api = new KauflandSellerApi();
                break;
            case SellerApi.METRO_MOCKED:
                api = new MetroMockSellerApi();
                break;

            default:
                api = null;
        }
        return api;
    }
}
