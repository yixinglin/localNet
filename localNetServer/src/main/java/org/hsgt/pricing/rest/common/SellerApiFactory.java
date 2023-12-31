package org.hsgt.pricing.rest.common;

//public class SellerApiFactory {
//
//    public static SellerApi createSellerApi(ChannelType channelType, ApiKey apiKey, boolean isCached) {
//        SellerApi api;
//        switch (channelType) {
//            case METRO:
//                api = new MetroSellerApi(apiKey);
//                break;
//            case METRO_MOCKED:
//                api = new MetroMockSellerApi(apiKey);
//                break;
//            case KAUFLAND:
//                api = new KauflandSellerApi();
//                break;
//            default:
//                api = null;
//        }
//        if (isCached) {
//            api = new CachedSellerApi(api);
//        }
//        return api;
//    }
//
//}
