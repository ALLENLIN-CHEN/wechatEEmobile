package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by congzihan on 17/9/19.
 */
@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public User getUserId(@RequestParam String openId,
                          @RequestParam String userName,
                          @RequestParam String phoneNum,
                          @RequestParam String qqNum,
                          @RequestParam String email,
                          @RequestParam List<Integer> tags,
                          @RequestParam String wechatNum,
                          @RequestParam String id){
        User user = new User();
        user.setOpenId(openId);
        user.setUserName(userName);
        user.setPhoneNum(phoneNum);
        user.setQqNum(qqNum);
        user.setEmail(email);
        user.setWechatNum(wechatNum);
        userService.creatUser(user,tags,id);
        return user;

    }
}
