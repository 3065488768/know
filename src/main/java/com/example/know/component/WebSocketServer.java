package com.example.know.component;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.know.util.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket的处理类
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {

    /**
     * 用来记录当前在线连接数，应该设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全set,用来存放每个客户端的websocket连接
     */
    private static ConcurrentHashMap<String, WebSocketServer> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private String userId;

    /**
     * 连接建立成功的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (concurrentHashMap.containsKey(userId)) {
            concurrentHashMap.remove(userId);
        } else {
            addOnlineCount();
        }
        concurrentHashMap.put(userId, this);
        log.info("userId为{}的用户连接,当前在线人数为：{}", userId, onlineCount);
//        sendMessage(JSON.toJSONString(AjaxResult.success("连接成功")));
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() throws ClosedChannelException {
        if (concurrentHashMap.containsKey(userId)) {
            concurrentHashMap.remove(userId);
            subOnlineCount();
        }
        log.info("userId为{}的用户退出,当前在线人数为：{}", userId, onlineCount);
    }

    /**
     * 收到客户端消息后调用
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("userId为{}的用户发送消息，内容是{}", userId, message);
//         消息处理
        if (StringUtils.isNotBlank(message)) {
            try {
                JSONObject jsonObject = JSON.parseObject(message);
                jsonObject.put("fromUserId", this.userId);
                String toUserId = jsonObject.getString(" toUserId");
                if (StringUtils.isNotBlank(toUserId) && concurrentHashMap.containsKey(toUserId)) {
                    concurrentHashMap.get(toUserId).sendMessage(message);
                } else {
                    log.error("请求的toUserId:{}不在服务器上", toUserId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送自定义消息
     **/
    public static void sendInfo(String message, Object data, String userId) {
        log.info("发送消息到:" + userId + ",报文:" + message);
        if (StringUtils.isNotBlank(userId) && concurrentHashMap.containsKey(userId)) {
            if (ObjectUtil.isNotNull(data)) {
                concurrentHashMap.get(userId).sendMessage(JSON.toJSONString(AjaxResult.success(message, data)));
            } else {
                concurrentHashMap.get(userId).sendMessage(JSON.toJSONString(AjaxResult.success(message)));
            }
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

//    public static void sendInfo(String message,String userId) {
//        log.info("发送消息到:"+userId+",报文:"+ message);
//        if(StringUtils.isNotBlank(userId) && concurrentHashMap.containsKey(userId)){
//            concurrentHashMap.get(userId).sendMessage(JSON.toJSONString(AjaxResult.success(message)));
//        }else{
//            log.error("用户"+userId+",不在线！");
//        }
//    }

    /**
     * 获得此时的
     * 在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人
     * 数加1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 在线人
     * 数减1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

