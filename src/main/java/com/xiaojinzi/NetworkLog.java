package com.xiaojinzi;

import com.google.gson.Gson;
import com.xiaojinzi.bean.MessageBean;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * 核心类
 */
public class NetworkLog {

    private static NetworkLog instance = new NetworkLog();

    private final Gson g = new Gson();

    private NetworkLog() {
    }

    /**
     * 获取实例对象
     */
    public static NetworkLog getInstance() {
        return instance;
    }

    private List<NetworkProvider> providers = Collections.synchronizedList(new LinkedList<>());
    private List<NetworkCustomer> consumers = Collections.synchronizedList(new LinkedList<>());

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    /**
     * 发送设备信息给消费者
     */
    public void sendDevicesInfoToConsumer() {
        executorService.submit(() -> {
            List<String> list = providers.stream()
                    .map(obj -> obj.getCombineDeviceName())
                    .collect(Collectors.toList());
            MessageBean messageBean = new MessageBean();
            messageBean.setAction(MessageBean.DEVICES_FLAG);
            messageBean.setData(list);
            sendNetworkLog(messageBean);
        });
    }

    public void addProvider(NetworkProvider networkProvider) {
        providers.add(networkProvider);
        sendDevicesInfoToConsumer();
    }

    public void removeProvider(NetworkProvider networkProvider) {
        providers.remove(networkProvider);
        sendDevicesInfoToConsumer();
    }

    public void addCustomer(NetworkCustomer networkCustomer) {
        consumers.add(networkCustomer);
        sendDevicesInfoToConsumer();
    }

    public void sendNetworkLog(MessageBean messageBean) {
        executorService.submit(() -> {
            for (NetworkCustomer networkCustomer : consumers) {
                networkCustomer.send(g.toJson(messageBean));
            }
        });
    }

    public void removeCustomer(NetworkCustomer networkCustomer) {
        consumers.remove(networkCustomer);
    }

    public String getCombineDeviceName(String tag) {
        if (tag == null || tag.length() == 0) {
            return null;
        }
        for (NetworkProvider provider : providers) {
            if (tag.equals(provider.getTag())) {
                return provider.getCombineDeviceName();
            }
        }
        return null;
    }

}
