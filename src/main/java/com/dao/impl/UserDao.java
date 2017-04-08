package com.dao.impl;

import com.dao.BaseDao;
import com.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by  xionglian on 2017/3/25.
 */
@Repository
public class UserDao extends BaseDao<User> {
    public List findBy(String hql, Map<String,Object>params){
        return this.findByHql(hql,params,null);
    }
}
