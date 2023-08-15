package org.hsgt.controllers.response;

import lombok.Data;

@Data
public class ControllerResponse<T> {
    public static int LOGIN_SUCCESS = 20000;
    public static int PW_INCORRECT = 60204;
    public static int LOGIN_FAIL = 50008;

    int code;
    String message;
    T data;

    public ControllerResponse() {
    }

    public ControllerResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ControllerResponse ok() {
        ControllerResponse ans = new ControllerResponse();
        ans.setCode(LOGIN_SUCCESS);
        ans.setMessage("success");
        return ans;
    }

    public ControllerResponse setData(T data) {
        this.data = data;
        return this;
    }
}
