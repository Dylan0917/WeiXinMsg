package com.yu.timeQu;

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
 * @date 2020/2/29 22:11
 */
public class WeatherJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SendService.sendMsg(WeiXinConst.SEND_MSG_URL_01+"access_token="+ GetAccessToken.getToken(),
                    WeatherService.getWeather(),"","1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
