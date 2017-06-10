package com.controller;

import com.util.AccessTokenUtil;
import com.util.HttpSender;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
