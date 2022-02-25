package com.mjy.chatroom.mapper;

import com.mjy.chatroom.entity.ChatMsg;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMsgMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    ChatMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);

    int updateSignFlag(List<String> msgIds);

    List<ChatMsg> selectMsgByAccepterId(String accepterId);
}