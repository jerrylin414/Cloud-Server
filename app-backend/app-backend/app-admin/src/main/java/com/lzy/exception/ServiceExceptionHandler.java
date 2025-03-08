package com.lzy.exception;

import com.lzy.common.CommonResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auther jerry
 * @date 11/7/2024 2:18 PM
 */
@RestControllerAdvice
public class ServiceExceptionHandler {
    private static Logger logger = LogManager.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public CommonResult handleServiceException(ServiceException e) {
        logger.error(e.getMessage(),e);
        return CommonResult.failed(e.getMessage());
    }
}
