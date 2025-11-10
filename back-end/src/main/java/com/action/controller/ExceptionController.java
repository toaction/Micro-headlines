package com.action.controller;

import com.action.common.Code;
import com.action.common.Result;
import com.action.exception.BusinessException;
import com.action.exception.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public Result doException(Exception e) {
        e.printStackTrace();
        return Result.error(Code.SYSTEM_UNKNOWN_ERROR, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException e) {
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException e) {
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMessage());
    }
}
