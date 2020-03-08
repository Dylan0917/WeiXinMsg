package com.yu.timeQu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yu.weixin.GetAccessToken;
import com.yu.weixin.SendService;
import com.yu.weixin.WeatherService;
import com.yu.weixin.WeiXinConst;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/3/1 0:15
 */
public class LoveMsgJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JSONArray array = WeatherService.getLove();
        try {
            for (int i = 0; i < array.size(); i++) {
                String o = array.getString(i);
                SendService.sendMsg(WeiXinConst.SEND_MSG_URL_01+"access_token="+ GetAccessToken.getToken(),
                        o,"","1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
