package com.mjy.chatroom.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

/**
 * @author
 * @description
 * @create 2021-11-20 19:42
 */
@Component
public class WebServer {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap server;
    private ChannelFuture future;



    public WebServer(){
        boss=new NioEventLoopGroup();
        worker=new NioEventLoopGroup();
        server=new ServerBootstrap();
        server.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new WebServerInitializer());
    }

    public void start(){
        try {
            future=server.bind(9000);
            System.out.println("netty websocket server 启动完毕.....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
