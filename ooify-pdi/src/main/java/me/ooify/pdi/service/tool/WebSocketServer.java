package me.ooify.pdi.service.tool;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/pdi/{userId}")
public class WebSocketServer {

    private static final ConcurrentHashMap<Long, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        SESSION_MAP.put(userId, session);
        System.out.println("WebSocket 连接成功：" + userId);
    }

    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        SESSION_MAP.remove(userId);
        System.out.println("WebSocket 断开：" + userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket 错误：" + error.getMessage());
    }

    public void sendToUser(Long userId, String message) {
        Session session = SESSION_MAP.get(userId);
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }
}