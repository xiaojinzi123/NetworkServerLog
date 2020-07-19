package com.xiaojinzi.bean;

public class MessageBean<T> {

    /**
     * 真正的数据的key
     */
    public static final String DATA_FLAG = "data";

    /**
     * action的key
     */
    public static final String ACTION_FLAG = "action";

    /**
     * 自身的tag的key
     */
    public static final String SELF_FLAG = "selfTag";

    /**
     * 设备列表 key
     */
    public static final String DEVICES_FLAG = "deviceList";

    /**
     * 设置设备名称的 key
     */
    public static final String DEVICE_NAME_FLAG = "deviceName";

    public static final String TAG_FLAG = "tag";

    // 表示自身的tag
    private String selfTag;

    // 表示需要支持的操作
    private String action;

    // 真正发送出去的数据,最终会转化成 json 数据传出去
    private T data;

    public MessageBean() {
    }

    public MessageBean(String selfTag, String action, T data) {
        this.selfTag = selfTag;
        this.action = action;
        this.data = data;
    }

    public static MessageBean tagBuild(String tag) {
        MessageBean result = new MessageBean();
        result.setAction(TAG_FLAG);
        result.setData(tag);
        return result;
    }

    public String getSelfTag() {
        return selfTag;
    }

    public void setSelfTag(String selfTag) {
        this.selfTag = selfTag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}