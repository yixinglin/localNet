package org.net;

import lombok.Data;

@Data
public class HttpResponse {
    private int stateCode;
    private String content;

    public HttpResponse(int stateCode, String content) {
        this.stateCode = stateCode;
        this.content = content;
    }
}
