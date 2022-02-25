package com.mjy.chatroom.entity.vo;

import java.io.Serializable;

/**
 * @author
 * @description
 * @create 2021-11-15 13:57
 */
public class MyFriendsVo implements Serializable {
    private String friendId;
    private String friendUsername;
    private String friendNickname;
    private String friendFaceImage;

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public String getFriendFaceImage() {
        return friendFaceImage;
    }

    public void setFriendFaceImage(String friendFaceImage) {
        this.friendFaceImage = friendFaceImage;
    }

    @Override
    public String toString() {
        return "MyFriendsVo{" +
                "friendId='" + friendId + '\'' +
                ", friendUsername='" + friendUsername + '\'' +
                ", friendNickname='" + friendNickname + '\'' +
                ", friendFaceImage='" + friendFaceImage + '\'' +
                '}';
    }
}
