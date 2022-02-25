package com.mjy.chatroom.util;

/**
 * @author
 * @description
 * @create 2021-11-03 14:13
 */
public class ReturnResult<T> {
    // 状态码
    private Integer status;

    private String message;

    private T data;

    public ReturnResult(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ReturnResult(){

    }

    public static ReturnResult success(){
        ReturnResult res = new ReturnResult();
        res.setStatus(200);
        res.setMessage("ok");
        return res;
    }

    public static ReturnResult error(){
        ReturnResult res = new ReturnResult();
        res.setStatus(500);
        res.setMessage("error");
        return res;
    }

    public ReturnResult status(Integer status){
        setStatus(status);
        return this;
    }

    public ReturnResult message(String message){
        setMessage(message);
        return this;
    }

    public ReturnResult data(T data){
        setData(data);
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
