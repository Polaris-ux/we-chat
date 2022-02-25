package com.mjy.chatroom.mapper;

import com.mjy.chatroom.entity.Users;
import com.mjy.chatroom.entity.vo.FriendRequestVo;
import com.mjy.chatroom.entity.vo.UsersVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersMapper {
    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Users getUserByName(String username);

    int updateNickname(UsersVo usersVo);

    List<FriendRequestVo> getUsersByAcceptId(String userId);
}