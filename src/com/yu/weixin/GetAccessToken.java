package com.yu.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yu.bean.WxMsg;
import com.yu.utils.HttpUtil;
import db.JDBCUtils;
import db.ResultUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yu.wenhua
 * @desc
 * @date 2020/2/29 11:49
 */
public class GetAccessToken {
    /**
     * 获取生效的token
     * @return
     */
    public static String getToken(){
        //1.查询数据库
        String sql = "SELECT * FROM WXMSG WHERE TTIME = (SELECT MAX(TTIME) FROM WXMSG)";
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultUtils ru = new ResultUtils();
        try {
            connection = JDBCUtils.getConnection();
            ru.setSql(sql);
            ps = ru.createPreparedStatement(connection);
            rs = ps.executeQuery();
            List list = ResultUtils.resultSetToListBean(rs, WxMsg.class);
            if (list.size() > 0){
                long curMills = System.currentTimeMillis();
                WxMsg msg = (WxMsg) list.get(0);
                long expireMills = Long.valueOf(msg.getExpiresin()) * 1000;
                long betweenMills = curMills - Long.valueOf(msg.getTtime());
                if (betweenMills < expireMills){
                    JDBCUtils.close(rs,connection,ps);
                    return msg.getTmsg();
                }
            }
        } catch (Exception e) {
            JDBCUtils.close(rs,connection,ps);
            e.printStackTrace();
        }
        //2.从网络获取
        String url = " https://qyapi.weixin.qq.com/cgi-bin/gettoken";
        String method = "GET";
        url = url + "?" + WeiXinConst.TOKEN_GET_PARAM;
        String retmsg = HttpUtil.httpsRequest(url,method,null);
        JSONObject jsonObject = JSON.parseObject(retmsg);
        int errorcode = Integer.parseInt(jsonObject.get("errcode").toString()) ;
        String accessToken = (String) jsonObject.get("access_token");
        String expiresIn = jsonObject.get("expires_in").toString();
        String id = UUID.randomUUID().toString().toUpperCase().replace("-","");
        String insertSql = "INSERT INTO WXMSG VALUES(?,?,?,?)";
        if (errorcode!= 0){
            return null;
        }
        ru.setSql(insertSql);
        try {
            ps = ru.createPreparedStatement(connection);
            ps.setString(1,id);
            ps.setString(2,accessToken);
            ps.setString(3,String.valueOf(System.currentTimeMillis()));
            ps.setString(4,expiresIn);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(rs,connection,ps);
        }
        return accessToken;
    }
}
