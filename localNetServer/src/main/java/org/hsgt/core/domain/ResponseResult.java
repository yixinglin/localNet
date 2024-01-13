package org.hsgt.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;
    int length;

    public ResponseResult(ResponseCode responseCode, T data) {
        this.code = responseCode.code;
        this.message = responseCode.message;
        this.data = data;
    }

    public ResponseResult(T data) {
        ResponseCode success = ResponseCode.SUCCESS;
        this.code = success.code;
        this.message = success.message;
        this.data = data;
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(ResponseCode responseCode) {
        this.code = responseCode.code;
        this.message = responseCode.message;
    }

    public ResponseResult(ResponseCode responseCode, String message) {
        this.code = responseCode.code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
    }


    public static <V> ResponseResult<V> success() {
        return new ResponseResult<>(ResponseCode.SUCCESS.code, ResponseCode.SUCCESS.message);
    }

    public static ResponseResult error(Exception e) {
        ResponseResult<String> rr = new ResponseResult<>(ResponseCode.SERVER_ERROR, e.getClass().toString());
        e.printStackTrace();
        StackTraceElement[] stackTrace = e.getStackTrace();
        rr.setData(String.format("%s\t Method: %s", e.getMessage(), stackTrace[0].getMethodName()));
        return rr;
    }

    public static <V> ResponseResult<V> error(ResponseCode errInfo) {
        return new ResponseResult<>(errInfo.code, errInfo.message);
    }

    public static <V> ResponseResult<V> error(Integer code, String message, V data) {
        return new ResponseResult<>(code, message, data);
    }

    public static <V> ResponseResult<V> error(ResponseCode responseCode, V data) {
        return new ResponseResult<>(responseCode.code, responseCode.message, data);
    }

    public static <V> ResponseResult<V> error(String str) {
        return new ResponseResult<>(ResponseCode.ERROR.code, str);
    }


    public ResponseResult setData(T data) {
        this.data = data;
        return this;
    }

    public ResponseResult setMessage(String message) {
        this.message = message;
        return  this;
    }

    public ResponseResult setLength(int length) {
        this.length = length;
        return this;
    }
}
