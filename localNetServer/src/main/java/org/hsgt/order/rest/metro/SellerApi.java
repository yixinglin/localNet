package org.hsgt.order.rest.metro;

import org.net.HttpResponse;

public interface SellerApi {

    public static final int METRO = 1;
    public static final int METRO_MOCKED = 3;

    public static final int KAUFLAND = 4;

    public HttpResponse selectAllOrders();
    public HttpResponse selectOrders(int limit, int offset);
    public HttpResponse selectOrderById(String id);

    public HttpResponse selectDocById(String id);
}
