package com.xiaojinzi.socket;


import com.google.gson.Gson;
import com.xiaojinzi.bean.MessageBean;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by cxj on 21/12/2017.
 */
public class ServerLog {

    private static ServerLog log = new ServerLog();

    public static ServerLog getLog() {
        return log;
    }

    private Map<String, SocketMaintain> devices = Collections.synchronizedMap(new HashMap<String, SocketMaintain>());

    private synchronized List<String> getDevices() {
        List<String> devicesList = new ArrayList<>();
        Set<String> keySet = devices.keySet();
        for (String tag : keySet) {
            devicesList.add(tag);
        }
        return devicesList;
    }

    private synchronized void addDeviceTag(String deviceTag, SocketMaintain socketMaintain) {
        boolean isExist = devices.containsKey(deviceTag);
        if (isExist) {
            SocketMaintain value = devices.get(deviceTag);
            if (value == socketMaintain) {
                return;
            }
        }
        devices.put(deviceTag, socketMaintain);
        sendDeviceListToClient();
    }

    private synchronized void removeDeviceTag(String deviceTag) {
        devices.remove(deviceTag);
        sendDeviceListToClient();
    }

    public synchronized void sendDeviceListToClient() {

        MessageBean messageBean = new MessageBean();
        messageBean.setAction("deviceList");
        messageBean.setData(getDevices());
        String json = new Gson().toJson(messageBean);
        WebSocket.sendMessage(null, json);
        sendMessageToApp(null, MessageBean.TARRGET_ALL, json);

    }

    private ServerLog() {

        socketServer = new SocketServer(13579);

        socketServer.setSocketMessageAcceptListener(this);

        socketServer.setSocketServerListener(this);

        SocketLog.setLogListener(this);

    }


    @Override
    public void accept(SocketMaintain socket, String message) {

        try {

            JSONObject jb = new JSONObject(message);

            // 获取信息的目标
            String targetTag = jb.optString(MessageBean.TARRGET_FLAG);
            String selfTag = jb.optString(MessageBean.SELF_FLAG);

            if (MessageBean.TARRGET_SERVER.equals(targetTag)) { // 如果是和服务器交互的

                String action = jb.getString(MessageBean.ACTION_FLAG);

                if (MessageBean.ACTON_ADD_DEVICE.equals(action)) {
                    String deviceTag = jb.getString(MessageBean.DATA_FLAG);
                    addDeviceTag(deviceTag, socket);
                }


            } else {

                List<String> devices = getDevices();

                String targetDevice = null;

                for (String device : devices) {

                    if (device.equals(targetTag)) {

                        targetDevice = device;

                    }

                }

                if (targetDevice == null) {
                    WebSocket.sendMessage(targetTag, message);
                } else {
                    sendMessageToApp(selfTag, targetDevice, message);
                }


            }


        } catch (Exception ignore) {
        }

    }


    @Override
    public void onLog(String tag, String data) {
        System.out.println(tag + "---------\n" + data);
    }

    @Override
    public synchronized void onSocketConnected(SocketMaintain socket) {
        System.out.println("new socket");
    }

    @Override
    public synchronized void onSocketDisconnect(SocketMaintain socket) {

        String deviceTag = null;
        Set<Map.Entry<String, SocketMaintain>> entries = devices.entrySet();
        for (Map.Entry<String, SocketMaintain> entry : entries) {
            if (entry.getValue() == socket) {
                deviceTag = entry.getKey();
            }
        }
        if (deviceTag == null) {
            return;
        }

        removeDeviceTag(deviceTag);

    }

    public synchronized void sendMessageToApp(String selfTag, String targetTag, String message) {

        if (socketServer == null || devices == null) {
            return;
        }

        if (MessageBean.TARRGET_ALL.equals(targetTag)) {
            socketServer.send(message);
            return;
        }

        SocketMaintain socketMaintain = devices.get(targetTag);

        if (socketMaintain == null) {

            MessageBean messageBean = MessageBean.createMsgBoxBean("目标设备已经离线!");
            String json = new Gson().toJson(messageBean);
            WebSocket.sendMessage(selfTag, json);

            return;
        } else {

            socketMaintain.send(message);

        }

    }

}
