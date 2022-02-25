package com.mjy.chatroom.util;


import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * @author
 * @description
 * @create 2021-11-03 14:42
 */
public class CodeMsg {



    private String message;
    private Integer errorCode;

    // 登录模块
    public static final CodeMsg LOGIN_PARAM_NULL=new CodeMsg("用户登录的用户名或密码不能为空",10001);
    public static final CodeMsg Login_PARAM_ERROR=new CodeMsg("用户名或密码错误",10002);

    // 注册模块
    public static final CodeMsg INSERT_USER_ERROR =new CodeMsg("用户注册失败",20001) ;

    //修改头像
    public static final CodeMsg PHOTO_PARAM_NULL = new CodeMsg("修改头像的参数信息不能为空",30001);
    public static final CodeMsg ILLEGAL_PARAM_TYPE = new CodeMsg("不合法的图片编码格式",30002);

    // 生成二维码
    public static final CodeMsg CREATE_QRCODE_ERROR = new CodeMsg("生成二维码失败，请重试",30003);

    //
    public static final CodeMsg ADD_FRIEND_ERROR = new CodeMsg("向好友表插入记录失败",30004);
    public static final CodeMsg REMOVE_REQUEST_ERROR = new CodeMsg("删除好友请求失败",30005);
    public CodeMsg(){

    }

    public CodeMsg(String message,Integer errorCode){
        this.message=message;
        this.errorCode=errorCode;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "message='" + message + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }



}
