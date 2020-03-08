package com.yu.bean;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/2/29 12:19
 */
public class WxMsg {
    private String id;
    private String tmsg;
    private String ttime;
    private String expiresin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTmsg() {
        return tmsg;
    }

    public void setTmsg(String tmsg) {
        this.tmsg = tmsg;
    }

    public String getTtime() {
        return ttime;
    }

    public void setTtime(String ttime) {
        this.ttime = ttime;
    }

    public String getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(String expiresin) {
        this.expiresin = expiresin;
    }
}
