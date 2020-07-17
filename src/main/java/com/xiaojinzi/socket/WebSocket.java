package com.xiaojinzi.socket;


import com.xiaojinzi.bean.MessageBean;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
public class WebSocket {

    public WebSocket() {

        try {
            ServerLog.getLog();
        } catch (Exception e) {
        }

        System.out.println("init WebSocket");

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        try {

            JSONObject jb = new JSONObject(message);
            String targetTag = jb.getString(MessageBean.TARRGET_FLAG);
            String selfTag = String.valueOf(session.hashCode());
            // 添加上web客户端自身的标志
            jb.put(MessageBean.SELF_FLAG, selfTag);

            if (MessageBean.TARRGET_SERVER.equals(targetTag)) { // 如果是和服务器交互的

            } else { // 其他的都是和客户端交互的
                ServerLog.getLog().sendMessageToApp(selfTag,targetTag, jb.toString());
            }

        } catch (Exception ignore) {

        }
    }

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private static Vector<Session> sessions = new Vector<>();

    private Session mSession;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        mSession = session;
        sessions.add(session);
        webSocketSet.add(this);
        ServerLog.getLog().sendDeviceListToClient();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public synchronized void onClose() {
        if (mSession != null) {
            sessions.remove(mSession);
        }
        webSocketSet.remove(this);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param target
     * @param message
     * @throws IOException
     */
    public static synchronized void sendMessage(String target, String message) {

        try {

            for (int i = sessions.size() - 1; i >= 0; i--) {
                Session session = sessions.get(i);
                boolean isSend = false;
                if (target == null || "".equals(target.trim())) {
                    isSend = true;
                } else if (String.valueOf(session.hashCode()).equals(target)) {
                    isSend = true;
                }

                if (!isSend) {
                    continue;
                }

                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    sessions.remove(session);
                }

            }

        } catch (Exception ignore) {
        }

    }

}
