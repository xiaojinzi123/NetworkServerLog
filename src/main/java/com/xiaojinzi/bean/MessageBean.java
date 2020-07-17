package com.xiaojinzi.bean;

/**
 * app 和 web 通信的格式,服务器通过其中"targetAppTag"和"targetWebTag"找出目标设备,发送过去
 * time   : 2018/05/20
 *
 * @author : xiaojinzi 30212
 */
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
     * 目标的key
     */
    public static final String TARRGET_FLAG = "targetTag";

    /**
     * 自身的tag的key
     */
    public static final String SELF_FLAG = "selfTag";

    /**
     * 针对的是socket连接的客户端,不是指的是web端
     */
    public static final String TARRGET_ALL = "allTarget";
    public static final String TARRGET_SERVER = "serverTarget";

    // 表示弹出一个信息的确定框
    public static final String ACTON_MSGBOX = "msgbox";
    public static final String ACTON_ADD_DEVICE = "addDeviceTag";

    // 表示其他端发送给哪一端
    private String targetTag;
    // 表示自身的tag
    private String selfTag;

    // 表示需要支持的操作
    private String action;

    // 真正发送出去的数据,最终会转化成 json 数据传出去
    private T data;

    public MessageBean() {
    }

    public MessageBean(String targetTag, String selfTag, String action, T data) {
        this.targetTag = targetTag;
        this.selfTag = selfTag;
        this.action = action;
        this.data = data;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
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

    public static MessageBean createMsgBoxBean(String message) {
        MessageBean bean = new MessageBean();
        bean.setAction(MessageBean.ACTON_MSGBOX);
        bean.setData(message);
        return bean;
    }

}