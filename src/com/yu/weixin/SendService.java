package com.yu.weixin;

import com.alibaba.fastjson.JSONObject;
import com.yu.utils.HttpUtil;

import java.io.IOException;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/2/29 15:29
 */
public class SendService {
    public static String sendMsg(String url,String msg,String userID,String partId) throws IOException {
        JSONObject jsonMsgObject = new JSONObject();
        jsonMsgObject.put("content",msg);
        JSONObject mainObj = new JSONObject();
        mainObj.put("touser",userID);
        mainObj.put("toparty",partId);
        mainObj.put("msgtype","text");
        mainObj.put("agentid","1000002");
        mainObj.put("text",jsonMsgObject);
        mainObj.put("safe","0");
        mainObj.put("enable_id_trans","0");
        mainObj.put("enable_duplicate_check","0");
        String ret = HttpUtil.send(url,mainObj,"utf-8");
        return ret;
    }
    public static String genWeatherMsg(String nyr,String xq,String tqzk,String fx,String fl,String dw,String gw,String kqzl){
        StringBuffer msg = new StringBuffer();
        msg.append("【泰安市泰山区】");
        msg.append(nyr);
        msg.append(xq).append(" ").append("天气# ");
        msg.append(tqzk).append(", ");
        msg.append(fx);
        msg.append(fl).append("级, ");
        msg.append("温度").append(dw).append("~").append(gw).append("℃").append(", ");
        msg.append("空气质量 ").append(kqzl);
        return msg.toString();
    }
}
