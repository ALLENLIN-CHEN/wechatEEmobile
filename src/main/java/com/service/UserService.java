package com.service;

import com.dao.impl.TagRelationDao;
import com.dao.impl.UserDao;
import com.entity.TagRelationEntity;
import com.entity.User;
import com.entity.newT.UserT;
import com.github.sd4324530.fastweixin.company.api.entity.QYUser;
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
    @Autowired
    TagRelationDao tagRelationDao;

    public UserT userToUserT(User user) {
        UserT userT = new UserT(user.getOpenId(), user.getUserName());
        return userT;

    }

    @Transactional
    public User findUser(String openId) {
        User user = userDao.get(User.class, openId);
        return user;
    }

    public List list() {
        List userList = userDao.getList();
        return userList;
    }

    public User creatUser(QYUser qyUser) {
        //在微信企业号中增加用户
        return userDao.saveUser(qyUser);

    }
    public void creatUser(User user,List<Integer> tags,String id) {
        //在微信企业号中增加用户
         userDao.save(user);
        tagRelationDao.bindTag(id,tags,"人员绑定");
    }

}

