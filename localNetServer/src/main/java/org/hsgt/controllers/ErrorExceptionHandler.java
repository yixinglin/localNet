package org.hsgt.controllers;

import org.hsgt.controllers.response.ControllerResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.utils.IoUtils;
import org.utils.Logger;

@ControllerAdvice
public class ErrorExceptionHandler {
    Logger logger = Logger.loggerBuilder(ErrorExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ControllerResponse<String> errorException(Exception e) {
        logger.error(IoUtils.getStackTrace(e));
        ControllerResponse<String> cr = ControllerResponse.err(e);
        return cr;
    }

}
