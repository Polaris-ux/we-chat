package com.mjy.chatroom.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author
 * @description
 * @create 2021-11-20 19:51
 */
public class WebServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        // http编解码器
        pipeline.addLast(new HttpServerCodec());

        // 是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());

        //http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // 加入心跳机制监听用户是否已经断开,自定义的空闲状态检测的handler
        pipeline.addLast(new IdleStateHandler(30,40,60));
        pipeline.addLast(new HeartBeatHandler());

        /*
           对应webSocket，它的数据是以帧进行传递的
           浏览器请求时：ws://localhost:9000/hello 表示请求的url
           WebSocketServerProtocolHandler 核心功能是将http请求升级为ws请求，保持长连接
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));

        // 自定义handler
        pipeline.addLast(new ChatHandler());


    }
}
