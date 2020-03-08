package com.yu.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yu.utils.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/2/29 19:52
 */
public class WeatherService {
    public static String getWeather(){
        JSONObject curDayWeather = getForcast();
        String[] rqs = getDate().split("-");
        String btzk = curDayWeather.getString("cond_txt_d");
        String yjzk = curDayWeather.getString("cond_txt_n");
        String tqzk = btzk.equals(yjzk) ? btzk : btzk + "转" + yjzk;
        String msg = SendService.genWeatherMsg(rqs[0] + "年" + rqs[1] + "月" + rqs[2] + "日",
                getWeekDay(),
                tqzk,
                curDayWeather.getString("wind_dir"),
                curDayWeather.getString("wind_sc"),
                curDayWeather.getString("tmp_min"),
                curDayWeather.getString("tmp_max"),getAir().getString("qlty"));
        return msg;
    }
    public static String getWeekDay(){
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    public static String getDate(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String createTime = dateFormat.format(now);//格式化然后放入字符串中
        return createTime;
    }
    public static JSONObject getForcast(){
        String requestUrl = WeiXinConst.FORCAST_URL + "location=" + WeiXinConst.LOCATION +"&key=" + WeiXinConst.WEATHER_KEY;
        System.out.println(requestUrl);
        String ret = HttpUtil.httpRequest(requestUrl,"GET",null);
        JSONObject jsonObject = JSON.parseObject(ret);
        JSONArray array1 = (JSONArray) jsonObject.get("HeWeather6");
        JSONObject obj1 = array1.getJSONObject(0);
        JSONArray dayForcast = (JSONArray) obj1.get("daily_forecast");
        String curDate = getDate();
        JSONObject curDayWeather = null;
        for (int i = 0; i < dayForcast.size(); i++) {
            JSONObject object = dayForcast.getJSONObject(i);
            if (object.get("date").toString().equals(curDate)){
                curDayWeather = object;
                break;
            }
        }
        return curDayWeather;
    }
    public static JSONObject getAir(){
        String requestUrl = WeiXinConst.AIR_NOW_URL + "location=taian" +"&key=" + WeiXinConst.WEATHER_KEY;
        System.out.println(requestUrl);
        String ret = HttpUtil.httpRequest(requestUrl,"GET",null);
        JSONObject jsonObject = JSON.parseObject(ret);
        JSONArray array1 = (JSONArray) jsonObject.get("HeWeather6");
        JSONObject obj1 = array1.getJSONObject(0);
        JSONArray dayForcast = (JSONArray) obj1.get("air_now_station");
        JSONObject curDayWeather = null;
        for (int i = 0; i < dayForcast.size(); i++) {
            JSONObject object = dayForcast.getJSONObject(i);
            if (object.get("asid").toString().equals("CNA1656")){
                curDayWeather = object;
                break;
            }
        }
//        System.out.println(ret);
        return curDayWeather;
    }
    public static JSONArray getLove(){
        String ret = HttpUtil.httpRequest(WeiXinConst.LOVE_URL,"GET",null);
        JSONObject jsonObject = JSON.parseObject(ret);
        JSONArray array = jsonObject.getJSONArray("returnObj");
//        System.out.println(ret);
        return array;
    }
}
