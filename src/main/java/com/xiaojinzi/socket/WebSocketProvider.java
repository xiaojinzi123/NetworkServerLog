package com.xiaojinzi.socket;


import com.google.gson.Gson;
import com.xiaojinzi.NetworkLog;
import com.xiaojinzi.NetworkProvider;
import com.xiaojinzi.bean.MessageBean;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/networkProvide")
public class WebSocketProvider implements NetworkProvider {

    private static AtomicInteger counter = new AtomicInteger();

    // 不重复并且剪短
    private final String TAG = "device_" + counter.incrementAndGet();

    private Gson gson = new Gson();

    /**
     * 所有的回话
     */
    private static Vector<Session> sessions = new Vector<>();

    private String deviceName = "unKnow";

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            MessageBean messageBean = gson.fromJson(message, MessageBean.class);
            // 如果是设置名字
            if (MessageBean.DEVICE_NAME_FLAG.equals(messageBean.getAction())) {
                deviceName = messageBean.getData().toString();
                NetworkLog.getInstance().sendDevicesInfoToConsumer();
            } else {
                NetworkLog.getInstance().sendNetworkLog(messageBean);
            }
        } catch (Exception ignore) {
            // ignore
        }
    }

    /**
     * 当前的会话
     */
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
        MessageBean messageBean = MessageBean.tagBuild(TAG);
        try {
            session.getBasicRemote().sendText(gson.toJson(messageBean));
            NetworkLog.getInstance().addProvider(this);
        } catch (IOException e) {
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public synchronized void onClose() {
        if (mSession != null) {
            sessions.remove(mSession);
        }
        NetworkLog.getInstance().removeProvider(this);
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getCombineDeviceName() {
        return getTag() + "_" + getDeviceName();
    }

    @Override
    public void send(String data) {
        if (mSession == null) {
            return;
        }
        try {
            mSession.getBasicRemote().sendText(data);
        } catch (Exception e){
            destroy();
        }
    }

    private void destroy(){
        if (mSession != null) {
            sessions.remove(mSession);
        }
        NetworkLog.getInstance().removeProvider(this);
    }

}
