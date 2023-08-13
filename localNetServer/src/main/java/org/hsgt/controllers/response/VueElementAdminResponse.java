package org.hsgt.controllers.response;

import lombok.Data;

@Data
public class VueElementAdminResponse {
    public static int LOGIN_SUCCESS = 20000;
    public static int PW_INCORRECT = 60204;
    public static int LOGIN_FAIL = 50008;

    int code;
    String message;
    Object data;

    public VueElementAdminResponse() {
    }

    public VueElementAdminResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static VueElementAdminResponse ok() {
        VueElementAdminResponse ans = new VueElementAdminResponse();
        ans.setCode(LOGIN_SUCCESS);
        ans.setMessage("success");
        return ans;
    }

}
