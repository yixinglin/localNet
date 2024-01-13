package org.hsgt.core.controllers;

import org.hsgt.core.domain.ResponseResult;
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
    public ResponseResult<String> errorException(Exception e) {
        logger.error(IoUtils.getStackTrace(e));
        ResponseResult<String> cr = ResponseResult.error(e);
        return cr;
    }

}
