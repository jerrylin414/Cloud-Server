package com.lzy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther jerry
 * @date 11/7/2024 2:20 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceException extends RuntimeException {
    private Integer code;


    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause, Integer code) {
        super(cause);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}