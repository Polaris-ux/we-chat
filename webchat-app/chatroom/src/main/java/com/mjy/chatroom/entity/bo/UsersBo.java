package com.mjy.chatroom.entity.bo;

import java.io.Serializable;

/**
 * @author mjy
 * @description 头像上传接收参数的工具类
 * @create 2021-11-10 17:29
 */
public class UsersBo implements Serializable {
    private String userId;
    private String faceData;

    public UsersBo(String userId, String faceData) {
        this.userId = userId;
        this.faceData = faceData;
    }

    public UsersBo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFaceData() {
        return faceData;
    }

    public void setFaceData(String faceData) {
        this.faceData = faceData;
    }

    @Override
    public String toString() {
        return "UsersBo{" +
                "userId='" + userId + '\'' +
                ", faceData='" + faceData + '\'' +
                '}';
    }
}
