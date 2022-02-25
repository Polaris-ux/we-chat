package com.mjy.chatroom.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author
 * @description
 * @create 2021-11-20 23:52
 */

/**
 * 在IOC容器的启动过程中，当所有的bean都已经处理完成之后，spring ioc容器会有一个发布事件的动作，
 * 让我们的bean实现ApplicationListener接口，这样当发布事件时，[Spring]的ioc容器就会以容器的实例对象作为
 * 事件源类并从中找到事件的监听者，此时ApplicationListener接口实例中的onApplicationEvent(E event)方法就会被调用
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private WebServer webServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(contextRefreshedEvent.getApplicationContext().getParent()==null){
            try {
                webServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
