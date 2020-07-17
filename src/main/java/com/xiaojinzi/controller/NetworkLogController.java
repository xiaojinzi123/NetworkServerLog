package com.xiaojinzi.controller;


import com.google.gson.Gson;
import com.xiaojinzi.bean.MessageBean;
import com.xiaojinzi.socket.NetworkWebSocket;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("network")
public class NetworkLogController {

    private static Gson g = new Gson();

    private static Set<String> tags = Collections.synchronizedSet(new HashSet<>());

    /**
     * 接受请求发送到浏览器
     *
     * @param data
     */
    @PostMapping("log")
    @ResponseBody
    public String log(String data) {
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        JSONObject jb = new JSONObject(data);
        // 获取自身的tag和目标的tag
        String selfTag = jb.optString(MessageBean.SELF_FLAG);
        String targetTag = jb.optString(MessageBean.TARRGET_FLAG);
        // 有去重的功能
        if (selfTag != null && !selfTag.isEmpty()) {
            tags.add(selfTag);
            sendTagToClient();
        }
        NetworkWebSocket.sendMessage(targetTag, jb.toString());
        return "hello ios";
    }

    public static void sendTagToClient() {
        MessageBean messageBean = new MessageBean();
        messageBean.setAction("deviceList");
        messageBean.setData(tags);
        String json = g.toJson(messageBean);
        NetworkWebSocket.sendMessage(null, json);
    }

    /**
     * 清空tags
     *
     * @return
     */
    @RequestMapping("clearTag")
    @ResponseBody
    public String clearTag() {

        tags.clear();
        sendTagToClient();

        return "hello ios";

    }

}
