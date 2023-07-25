package org.net;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    private boolean success;
    private int code;
    private String message;
    private Map<String, Object> data = new HashMap();

    public static Result ok() {
        Result r = new Result();
        r.setCode(ResultCode.SUCCESS);
        r.setSuccess(true);
        r.setMessage("成功");
        return r;
    }

    public static Result error() {
        Result r = new Result();
        r.setCode(ResultCode.ERROR);
        r.setSuccess(false);
        r.setMessage("失败");
        return r;
    }

    public Result date(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
