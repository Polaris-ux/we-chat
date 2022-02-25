package com.mjy.chatroom.netty.interact;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @description
 * @create 2021-11-20 21:34
 */
public class UserAndChannel {
    private static Map<String, Channel> channelMap=new HashMap<>();

    public static void put(String userId,Channel channel){
        channelMap.put(userId, channel);
    }

    public static Channel getChannel(String userId){
        return channelMap.get(userId);
    }
}
