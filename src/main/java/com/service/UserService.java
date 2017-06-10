package com.service;

import com.dao.impl.UserDao;
import com.entity.User;
import com.entity.newT.UserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zengqin on 2017/3/25.
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public UserT userToUserT(User user){
        UserT userT=new UserT(user.getOpenId(),user.getUserName());
        return userT;

    }
    @Transactional
    public User findUser(String openId){
        User user=userDao.get(User.class,openId);
        return  user;
    }

    public List list(){
        List userList=userDao.getList();
        return  userList;
    }

}

