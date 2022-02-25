package com.mjy.chatroom.util;

import com.alibaba.fastjson.JSON;
import com.mjy.chatroom.netty.interact.DataContext;

/**
 * @author
 * @description
 * @create 2021-11-20 21:24
 */
public class JSONUtil {


    /**
     * DataContext转换为json
     * @param dataContext DataContext
     * @return
     */
    public static String ObjectToJSON(DataContext dataContext){
        String str = JSON.toJSONString(dataContext);
        return str;
    }

    public static DataContext jsonToObject(String str){
        DataContext dataContext = JSON.parseObject(str, DataContext.class);
        return dataContext;
    }
}
