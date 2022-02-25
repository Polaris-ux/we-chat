package com.mjy.chatroom.netty.interact;

import java.io.Serializable;

/**
 * @author
 * @description
 * @create 2021-11-20 21:08
 */
public class ChatContext implements Serializable {
    // 发送者id
    private String senderId;
    // 接收者id
    private String receiverId;
    // 发送的消息
    private String msg;
    // 消息的id
    private String msgId;


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "ChatContext{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", msg='" + msg + '\'' +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
