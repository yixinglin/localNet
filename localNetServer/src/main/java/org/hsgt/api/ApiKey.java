package org.hsgt.api;

import lombok.Data;

@Data
public class ApiKey {
    private String clientKey;
    private String secretKey;
    private String accountName;
}
