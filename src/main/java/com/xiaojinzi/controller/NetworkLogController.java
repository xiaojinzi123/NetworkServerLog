package com.xiaojinzi.controller;


import com.google.gson.Gson;
import com.xiaojinzi.NetworkLog;
import com.xiaojinzi.bean.MessageBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("network")
public class NetworkLogController {

    private static Gson g = new Gson();

    /**
     * 接受请求发送到浏览器, 但是仅用于测试
     */
    @PostMapping("log")
    @ResponseBody
    public String log(@RequestParam("tag") String tag,
                      @RequestParam("data") String data) {
        if (tag == null || tag.length() == 0) {
            throw new NullPointerException("data is null");
        }
        if (data == null || data.length() == 0) {
            throw new NullPointerException("data is null");
        }
        MessageBean messageBean = g.fromJson(data, MessageBean.class);
        String deviceName = NetworkLog.getInstance().getCombineDeviceName(tag);
        if (deviceName == null) {
            return "fail";
        } else {
            messageBean.setSelfTag(deviceName);
            NetworkLog.getInstance().sendNetworkLog(messageBean);
            return "success";
        }
    }

}
