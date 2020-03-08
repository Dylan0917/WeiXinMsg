package com.yu.test;

import com.yu.timeQu.CronTriggerRun;
import com.yu.weixin.WeatherService;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/2/29 11:42
 */
public class dbTest {
    public static void main(String[] args) {
        CronTriggerRun cronTrigger = new CronTriggerRun();
        try {
            cronTrigger.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
