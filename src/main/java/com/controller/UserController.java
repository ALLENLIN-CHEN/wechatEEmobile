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
                          @RequestParam (required = false) String qqNum,
                          @RequestParam (required = false) String email,
                          @RequestParam (required = false) List<Integer> tags,
                          @RequestParam String wechatNum,
                          @RequestParam String id){
        User user = new User();
        if (userService.findUser(openId) != null)
            return null;
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
