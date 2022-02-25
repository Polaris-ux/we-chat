package com.mjy.chatroom.entity.vo;

import java.io.Serializable;

/**
 * @author
 * @description
 * @create 2021-11-13 21:50
 */
public class FriendRequestVo implements Serializable {
    private String sendId;
    private String sendUsername;
    private String sendFaceImage;
    private String sendNickname;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    public String getSendFaceImage() {
        return sendFaceImage;
    }

    public void setSendFaceImage(String sendFaceImage) {
        this.sendFaceImage = sendFaceImage;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }

    @Override
    public String toString() {
        return "FriendRequestVo{" +
                "sendId='" + sendId + '\'' +
                ", sendUsername='" + sendUsername + '\'' +
                ", sendFaceImage='" + sendFaceImage + '\'' +
                ", sendNickname='" + sendNickname + '\'' +
                '}';
    }
}
