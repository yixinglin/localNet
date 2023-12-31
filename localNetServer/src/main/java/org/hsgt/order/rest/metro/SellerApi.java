package org.hsgt.order.rest.metro;

import org.net.HttpResponse;

public interface SellerApi {

    public HttpResponse selectAllOrders();
    public HttpResponse selectOrders(int limit, int offset);
    public HttpResponse selectOrderById(String id);

    public HttpResponse selectDocById(String id);
}
