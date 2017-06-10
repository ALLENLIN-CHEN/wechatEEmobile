package com.controller;

import com.github.sd4324530.fastweixin.api.enums.OauthScope;
import com.github.sd4324530.fastweixin.company.api.QYOauthAPI;
import com.github.sd4324530.fastweixin.company.api.QYUserAPI;
import com.github.sd4324530.fastweixin.company.api.config.QYAPIConfig;
import com.github.sd4324530.fastweixin.company.api.response.GetOauthUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rthtr on 2017/4/22.
 */
@Controller
@RequestMapping(value = "login")
public class LoginController {


    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private QYAPIConfig qyapiConfig = new QYAPIConfig("wxa50ffd87271d6e9e", "PP6oO5miSKsQsRTDrZaaHLxvZx2hQT9PnWXtESRPP6anCMxaIgfITTIo-riM1Pnu");
    private QYOauthAPI qyOauthAPI = new QYOauthAPI(qyapiConfig);
    private QYUserAPI qyUserAPI = new QYUserAPI(qyapiConfig);


    @RequestMapping(value = "getOauth", method = RequestMethod.GET)
    public void getOauth(@RequestParam("state") String state, HttpServletResponse response) throws IOException {
        response.sendRedirect(qyOauthAPI.getOauthPageUrl("www.chenlinallen.com/login/getUserId", OauthScope.SNSAPI_USERINFO, state));
    }

    /**
     * 微信授权时，用来获取微信企业号授权的用户userId（做微信授权时的测试）
     * 访问特殊url后，拿到code
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getUserId", method = RequestMethod.GET)
    @ResponseBody
    public void getUserId(HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code, @RequestParam("state") String state) throws IOException {
        GetOauthUserInfoResponse oauthUserInfoResponse = qyUserAPI.getOauthUserInfo(code);

//        qyUserAPI.create(new QYUser());
        response.sendRedirect(state + "?openId=" + oauthUserInfoResponse.getUserid());
    }
}


