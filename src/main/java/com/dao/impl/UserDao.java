package com.dao.impl;

import com.dao.BaseDao;
import com.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by  xionglian on 2017/3/25.
 */
@Repository("UserDao")
public class UserDao extends BaseDao<User> {
    public List findBy(String hql, Map<String,Object>params){
        return this.findByHql(hql,params,null);
    }

    /**
     * @author rthtr 2017/4/17
     */
    public User getUserByOpenId(String openId){
     //   return  this.get(User.class,openId);
        String hql="from User u where u.openId='"+openId+"'";
        List <User>list=this.findByHql(hql,null,null);
        return list.get(0);
    }

    public List getList(){
        //   return  this.get(User.class,openId);
        String hql="from User u";
        List <User>list=this.findByHql(hql,null,null);
        return list;
    }
}
