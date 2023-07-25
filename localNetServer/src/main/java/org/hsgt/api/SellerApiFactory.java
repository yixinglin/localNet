package org.hsgt.api;

public class SellerApiFactory {

    public static SellerApi createSellerApi(int channel, String apiKey) {
        SellerApi api;
        switch (channel) {
            case SellerApi.METRO:
                api = new MetroSellerApi();
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
