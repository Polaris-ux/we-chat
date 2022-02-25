package com.mjy.chatroom.mapper;

import com.mjy.chatroom.entity.MyFriends;
import com.mjy.chatroom.entity.vo.MyFriendsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyFriendsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);

    MyFriends getFriendRecord(@Param("userId")String userId, @Param("friendsId")String friendsId);

    List<MyFriendsVo> selectFriendsById(String userId);
}