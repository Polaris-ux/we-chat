package com.mjy.chatroom.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author
 * @description 心跳机制
 * @create 2021-11-25 13:22
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 事件触发时会调用该函数
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event=(IdleStateEvent)evt;
            Channel channel=ctx.channel();
            // 处于读写空闲，自动断开连接，避免浪费资源
            if(event.state()== IdleState.ALL_IDLE){
                System.out.println(channel+":关闭");
                channel.close();
            }else if(event.state()==IdleState.READER_IDLE){
                System.out.println("读空闲......");
            }else if(event.state()==IdleState.WRITER_IDLE){
                System.out.println("写空闲");
            }
        }
    }
}
