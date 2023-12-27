package org.hsgt.core.rest;

import lombok.Data;

@Data
public class ApiKey {
    private String clientKey;
    private String secretKey;
    private String accountName;
}
