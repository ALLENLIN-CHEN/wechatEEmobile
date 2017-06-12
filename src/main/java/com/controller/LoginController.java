package com.controller;

import com.github.sd4324530.fastweixin.api.enums.OauthScope;
import com.github.sd4324530.fastweixin.company.api.QYOauthAPI;
import com.github.sd4324530.fastweixin.company.api.QYUserAPI;
import com.github.sd4324530.fastweixin.company.api.config.QYAPIConfig;
import com.github.sd4324530.fastweixin.company.api.entity.QYUser;
import com.github.sd4324530.fastweixin.company.api.response.GetOauthUserInfoResponse;
import com.github.sd4324530.fastweixin.company.api.response.GetQYUserInfoResponse;
import com.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private QYAPIConfig qyapiConfig = new QYAPIConfig("wxa50ffd87271d6e9e", "PP6oO5miSKsQsRTDrZaaHLxvZx2hQT9PnWXtESRPP6anCMxaIgfITTIo-riM1Pnu");
    private QYOauthAPI qyOauthAPI = new QYOauthAPI(qyapiConfig);
    private QYUserAPI qyUserAPI = new QYUserAPI(qyapiConfig);


    @RequestMapping(value = "getOauth", method = RequestMethod.GET)
    public void getOauth(@RequestParam("state") String state, HttpServletResponse response) throws IOException {
        response.sendRedirect(qyOauthAPI.getOauthPageUrl("www.chenlinallen.com/api/login/getUserId", OauthScope.SNSAPI_USERINFO, state));
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
        // 根据授权获取的code获取信息
        GetOauthUserInfoResponse oauthUserInfoResponse = qyUserAPI.getOauthUserInfo(code);
        // 得到用户信息
        GetQYUserInfoResponse getQYUserInfoResponse = qyUserAPI.get(oauthUserInfoResponse.getUserid());
        // 企业号添加用户
        QYUser qyUser = qyUserAPI.get(oauthUserInfoResponse.getUserid()).getUser();
        qyUserAPI.create(qyUser);
        // 添加用户
        userService.creatUser(getQYUserInfoResponse.getUser());
        // 重定向到前端
        response.sendRedirect(state + "?openId=" + oauthUserInfoResponse.getUserid());
    }
}


