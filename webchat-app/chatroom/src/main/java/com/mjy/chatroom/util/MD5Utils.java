package com.mjy.chatroom.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author
 * @description
 * @create 2021-11-03 13:29
 */
public class MD5Utils {
    private static final String SALT="abcd1234";

    public static String md5(String msg){
        return DigestUtils.md5Hex(msg);
    }

    public static String encodeByMd5(String msg){
        String message=SALT.charAt(0)+SALT.charAt(1)+msg+SALT.charAt(7)+SALT.charAt(6);
        return md5(message);
    }
}
