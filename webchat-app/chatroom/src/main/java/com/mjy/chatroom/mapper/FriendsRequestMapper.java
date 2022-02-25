package com.mjy.chatroom.mapper;

import com.mjy.chatroom.entity.FriendsRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRequestMapper {
    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);

    int removeRecord(@Param("userId")String userId,@Param("friendId") String friendId);

    int selectRequests(String userId);
}