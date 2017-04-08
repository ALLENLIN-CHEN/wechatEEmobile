package com.service;

import com.dao.impl.ScheduleDao;
import com.dao.impl.TransferDao;
import com.dao.impl.UserDao;
import com.entity.Schedule;
import com.entity.TransferEntity;
import com.entity.User;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/7.
*/
@Service
public class TransferService {
    @Autowired
    ScheduleDao scheduleDao;
    @Autowired
    UserDao userDao;
    @Autowired
    TransferDao transferDao;
    @Autowired
    TransferMemberService transferMemberService;
    /**
     * 保存转移
    */
    @Transactional
    public boolean saveTransfer(int scheduleId, String openId,ArrayList<Map<String, Object>> transferMembers){
        try {
            Schedule schedule = scheduleDao.get(Schedule.class, scheduleId);
            String hql = "from User where openId=:openId";
            Map<String, Object> params = new HashedMap();
            params.put("openId", openId);
            List<User> list = userDao.findBy(hql, params);
            User user = list.get(0);
            TransferEntity transfer = new TransferEntity(schedule, user);
            transferDao.saveTransfer(transfer);
            transferMemberService.saveTransferMember(transferMembers,transfer);
        }catch (Exception e) {
            e.getStackTrace();
        }
        return  true;
    }
}

