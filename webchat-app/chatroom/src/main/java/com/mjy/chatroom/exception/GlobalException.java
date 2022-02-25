package com.mjy.chatroom.exception;

import com.mjy.chatroom.util.CodeMsg;

/**
 * @author
 * @description
 * @create 2021-11-03 14:38
 */
public class GlobalException extends RuntimeException{
    static final long serialVersionUID = 766939L;

    private CodeMsg codeMsg;

    public GlobalException(){
        super();
    }

    public GlobalException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg=codeMsg;
    }

    @Override
    public String toString() {
        return "GlobalException{" +
                "codeMsg=" + codeMsg +
                '}';
    }
}
