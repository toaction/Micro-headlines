package com.action.headline.controller;


import com.action.headline.common.Code;
import com.action.headline.common.Result;
import com.action.headline.exception.BusinessException;
import com.action.headline.exception.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
