package com.dao.impl;

import com.controller.LoginController;
import com.dao.BaseDao;
import com.entity.User;
import com.github.sd4324530.fastweixin.company.api.entity.QYUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by  xionglian on 2017/3/25.
 */
@Repository("UserDao")
public class UserDao extends BaseDao<User> {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    public List findBy(String hql, Map<String, Object> params) {
        return this.findByHql(hql, params, null);
    }

    /**
     * @author rthtr 2017/4/17
     */
    public User getUserByOpenId(String openId) {
        //   return  this.get(User.class,openId);
        String hql = "from User u where u.openId='" + openId + "'";
        List<User> list = this.findByHql(hql, null, null);
        return list.get(0);
    }

    public List getList() {
        //   return  this.get(User.class,openId);
        String hql = "from User u";
        List<User> list = this.findByHql(hql, null, null);
        return list;
    }

    public User saveUser(QYUser qyUser) {
        //   return  this.get(User.class,openId);
        if (qyUser == null || qyUser.getUserId() == null)
            return null;
        User user = new User(qyUser.getUserId(), qyUser.getName(), qyUser.getMobile(), qyUser.getEmail(), qyUser.getGender(),
                qyUser.getMobile(), qyUser.getPosition(), null, qyUser.getWeixinid(), null, true, null, null, null);
        this.saveOrUpdate(user);
        log.info("增加了用户"+user);
        return user;
    }

}
