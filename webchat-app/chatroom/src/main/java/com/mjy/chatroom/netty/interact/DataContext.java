package com.mjy.chatroom.netty.interact;

import java.io.Serializable;

/**
 * @author
 * @description
 * @create 2021-11-20 21:06
 */
public class DataContext implements Serializable {
    private Integer action;
    private ChatContext chatContext;
    // 扩展字段，用来传输消息id
    private String extend;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public ChatContext getChatContext() {
        return chatContext;
    }

    public void setChatContext(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "DataContext{" +
                "action=" + action +
                ", chatContext=" + chatContext +
                ", extend='" + extend + '\'' +
                '}';
    }
}
