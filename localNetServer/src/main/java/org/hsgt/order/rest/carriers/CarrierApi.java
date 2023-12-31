package org.hsgt.order.rest.carriers;

import org.net.HttpResponse;

public interface CarrierApi {
    public HttpResponse createParcelLabel(Object params);
}
