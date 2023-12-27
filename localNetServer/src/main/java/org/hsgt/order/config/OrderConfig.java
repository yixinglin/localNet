package org.hsgt.order.config;

import lombok.Data;
import org.hsgt.order.rest.metro.SellerApi;
import org.springframework.beans.factory.annotation.Value;


@Data
public abstract class OrderConfig {

    boolean mocked;
    @Value("${app.enable-email-notification}")
    boolean enableEmailNotification;
    @Value("${order.invoice.template}")
    String templateDocFile;
    @Value("${order.invoice.cache}")
    String cachePath;
    @Value("${order.invoice.pdf-out}")
    String pdfOutPath;

    SellerApi api;
    public abstract SellerApi getApiInstance();
}
