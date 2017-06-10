package com.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.entity.User;
import com.service.UserService;
import com.util.AccessTokenUtil;
import com.util.SendUtil;

/**
 * @author phan at 2016年3月2日
 */
@Component
public class SendTask extends TimerTask {

    @Autowired
    private UserService userService;

    @Override
    public void run() {
//        scheduleServiceImpl.autoChangeScheduleTaskStatus();
        System.out.println("send @ " + new Date().getTime());
        String accessToken = AccessTokenUtil.getAccessToken(null);
        List<User> userSet = userService.list();
        if (userSet == null)
            userSet = new ArrayList<User>();
        System.out.println("@@@@@ Next is Showing Userset");
        for (User touser : userSet) {
            String userId = touser.getOpenId();
//            System.out.println(touser.getUserName() + "  " + touser.getOpenId());
//            String content = userServiceImpl.showInfo(userId, null);
//            if (content != null && !"".equals(content)) {
//                content = "『每日通知』\n" + content + "回复【#数字】进行反馈\n";
//                SendUtil.send(accessToken, content, userId);
//            }
            SendUtil.sendWechatMobile(accessToken,userId);
            System.out.println("send end");
        }
        System.out.println("@@@@@ Over");
    }

}
