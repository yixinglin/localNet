package org.hsgt.order.rest.carriers;

import org.net.HttpResponse;

public interface CarrierApi {
    HttpResponse createParcelLabel(Object params);
}
