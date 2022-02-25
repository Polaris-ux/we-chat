package com.mjy.chatroom.exception;

import com.mjy.chatroom.util.ReturnResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author
 * @description
 * @create 2021-11-03 14:56
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ReturnResult handleException(Exception e){
        if(e instanceof GlobalException){
            return ReturnResult.error().message(e.getMessage());
        }

        e.printStackTrace();
        return null;
    }
}
