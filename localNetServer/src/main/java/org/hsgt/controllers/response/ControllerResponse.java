package org.hsgt.controllers.response;

import lombok.Data;

@Data
public class ControllerResponse<T> {
    public static int LOGIN_SUCCESS = 20000;
    public static int PW_INCORRECT = 60204;
    public static int LOGIN_FAIL = 50008;
    public static int ERR_EXCEPTION = 50009;

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

    public static ControllerResponse err(Exception e) {
        ControllerResponse ans  = new ControllerResponse();
        ans.setCode(ERR_EXCEPTION);
        ans.setMessage(e.getClass().toString());
        e.printStackTrace();
        StackTraceElement[] stackTrace = e.getStackTrace();
        ans.setData(String.format("%s\t Method: %s", e.getMessage(), stackTrace[0].getMethodName()));
        return ans;
    }

    public ControllerResponse setData(T data) {
        this.data = data;
        return this;
    }
}
