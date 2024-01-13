package org.hsgt.order.rest.metro;

import org.net.HttpResponse;

public interface SellerApi {

    HttpResponse selectAllOrders();
    HttpResponse selectOrders(int limit, int offset);
    HttpResponse selectOrderById(String id);

    HttpResponse selectDocById(String id);
}
