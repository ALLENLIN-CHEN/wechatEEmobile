package com.controller;

import com.entity.Pager;
import com.sun.deploy.net.HttpUtils;
import com.util.AccessTokenUtil;
import com.util.HttpSender;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rthtr on 2017/4/22.
 */
@Controller
@RequestMapping(value = "login")
public class LoginController {
    @Autowired
    private HttpSender httpSender;
    @Autowired
    private AccessTokenUtil accessTokenUtil;

    private Map<String, Object> dataMap = new HashMap<String, Object>();

    /**
     * 微信授权时，用来获取微信企业号授权的用户userId（做微信授权时的测试）
     * 访问特殊url后，拿到code
     * @param request
     * @return
     */
    @RequestMapping(value="getUserId", method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserId(HttpServletRequest request, @RequestParam("code")String code) {
        dataMap.clear();
        //accessToken是企业号的唯一全局票据
        /**
         * 这个链接的作用是根据code获取用户信息
         * {"UserId":"USERID.","DeviceId":"DEVICEID"}
         */
        try{
            String jsonString = httpSender.sendGet("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo",
                    "access_token=" + accessTokenUtil.getAccessToken().getToken()
                            + "&code=" + code, 5);
            JSONObject jsonObj = JSONObject.fromObject(jsonString);
            //若key不存在时，optString会返回一个空字符串或者默认设置的值
            //getString方法则会报错
            String openId = jsonObj.optString("UserId");
            request.setAttribute("openId", openId);
            dataMap.put("result", "success");
            dataMap.put("openId", openId);
        } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        dataMap.put("result", "fail");
        dataMap.put("resultTip", e.getMessage());
    }
        return dataMap;
    }
}
