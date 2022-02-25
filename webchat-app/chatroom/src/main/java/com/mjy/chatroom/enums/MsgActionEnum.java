package com.mjy.chatroom.enums;

/**
 * @author
 * @description
 * @create 2021-11-20 21:14
 */
public enum MsgActionEnum {
    CONNECT(1,"第一次(或重连)初始化连接"),
    CHAT(2,"聊天消息"),
    SIGNED(3,"消息签收"),
    KEEPALIVE(4,"客户端保持长连接"),
    PULL_FRIEND(5,"拉取好友");

    public Integer type;
    public String content;

    MsgActionEnum(Integer type,String content){
        this.type = type;
        this.content=content;
    }

    public Integer getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
