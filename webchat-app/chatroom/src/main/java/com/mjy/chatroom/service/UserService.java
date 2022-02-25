package com.mjy.chatroom.service;

import com.mjy.chatroom.entity.ChatMsg;
import com.mjy.chatroom.entity.FriendsRequest;
import com.mjy.chatroom.entity.MyFriends;
import com.mjy.chatroom.entity.Users;
import com.mjy.chatroom.entity.vo.FriendRequestVo;
import com.mjy.chatroom.entity.vo.MyFriendsVo;
import com.mjy.chatroom.entity.vo.UsersVo;
import com.mjy.chatroom.util.ReturnResult;

import java.util.List;

/**
 * @author
 * @description
 * @create 2021-11-02 23:48
 */
public interface UserService {
    Users getUserById(String id);

    Users isUserExist(String username);

    Users saveUser(Users user);

    boolean editNickname(UsersVo usersVo);

    boolean updateImg(Users user);

    ReturnResult searchFriends(String userId,String username);

    boolean sendAddRequest(FriendsRequest friendRequest);

    List<FriendRequestVo> getSendReqUsersInfo(String userId);

    List<MyFriendsVo> addFriend(MyFriends mFriends);

    void ignoreFriend(MyFriends mFriends);

    int saveFriend(String userId,String friendId);

    List<MyFriendsVo> queryFriendsById(String userId);

    int getRequestNum(String userId);

    List<MyFriendsVo> getFriends(String userId);

    String saveChatMsg(ChatMsg chatMsg);

    void doMsgSign(List<String> msgId);

    List<ChatMsg> queryUnReadMsg(String accepterId);
}
