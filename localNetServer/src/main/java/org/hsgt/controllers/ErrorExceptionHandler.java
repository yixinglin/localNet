package org.hsgt.controllers;

import org.hsgt.controllers.response.ControllerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ControllerResponse<String> errorException(Exception e) {
        System.out.println(e);
        ControllerResponse<String> cr = ControllerResponse.err(e);
        return cr;
    }

}
