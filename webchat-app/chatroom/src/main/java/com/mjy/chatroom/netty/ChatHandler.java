package com.mjy.chatroom.netty;

import com.mjy.chatroom.entity.ChatMsg;
import com.mjy.chatroom.enums.MsgActionEnum;
import com.mjy.chatroom.netty.interact.ChatContext;
import com.mjy.chatroom.netty.interact.DataContext;
import com.mjy.chatroom.netty.interact.UserAndChannel;
import com.mjy.chatroom.service.UserService;
import com.mjy.chatroom.util.JSONUtil;
import com.mjy.chatroom.util.SpringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @description
 * @create 2021-11-20 19:56
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup users;
    static{
        users=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame tw) throws Exception {
        // 获取客户端所传消息
        String text = tw.text();

        // 将得到的json字符串转化为实体类
        DataContext dataContext = JSONUtil.jsonToObject(text);

        // 判断消息类型，根据不同的类型来处理不同的业务
        Integer action=dataContext.getAction();
        ChatContext chatContext = dataContext.getChatContext();
        Channel channel = ctx.channel();
        UserService userService = SpringUtils.getBean("userServiceImpl",UserService.class);

        /**
         * 1、当WebSocket第一次open的时候，初始化channel,把用的channel和userId关联起来
         * 2、聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态【未签收】
         * 3、签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态【已签收】
         * 4、心跳机制
         */
        if(action== MsgActionEnum.CONNECT.type){
            // 1、当WebSocket第一次open的时候，初始化channel,把用的channel和userId关联起来
            String senderId = chatContext.getSenderId();
            if(senderId!=null&&!"".equals(senderId)){
                UserAndChannel.put(senderId,channel);
            }
        }else if(action==MsgActionEnum.CHAT.type){
            // 2、聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态【未签收】
            String senderId = chatContext.getSenderId();
            String receiverId = chatContext.getReceiverId();
            String msg=chatContext.getMsg();

            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setSendUserId(senderId);
            chatMsg.setAcceptUserId(receiverId);
            chatMsg.setMsg(msg);
            // 0表示未签收，1表示签收
            chatMsg.setSignFlag(0);
            chatMsg.setCreateTime(new Date());
            String msgId = userService.saveChatMsg(chatMsg);
            chatContext.setMsgId(msgId);

            DataContext newDataContext = new DataContext();
            newDataContext.setChatContext(chatContext);
            // 发送消息
            Channel receiverChannel = UserAndChannel.getChannel(receiverId);
            if(receiverChannel==null){
                // 离线用户
            }else{
                // 判断该用户是否在线
                Channel channel1 = users.find(receiverChannel.id());
                if(channel1!=null){
                    // 用户在线
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JSONUtil.ObjectToJSON(newDataContext)));
                }else{
                    // 用户离线
                }

            }

        }else if(action==MsgActionEnum.SIGNED.type){
            // 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态【已签收】
            // 多个消息id,以","分隔
            String extend = dataContext.getExtend();
            String[] split = extend.split(",");
            List<String> msgIds=new ArrayList<>();
            for (String id : split) {
                if(id!=null||!"".equals(id)){
                    msgIds.add(id);
                }
            }
            userService.doMsgSign(msgIds);

        }else if(action==MsgActionEnum.KEEPALIVE.type){
            // 防止用户断网，导致channel的资源白白被浪费
            System.out.println("接收到来自："+channel+"的心跳包");
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        users.remove(ctx.channel());
    }

    /**
     * 捕获异常
     * @param ctx ChannelHandlerContext
     * @param cause Throwable
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常后关闭连接，同时从channelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }


}
