package com.service;

import com.dao.impl.TransferMemberDao;
import com.dao.impl.UserDao;
import com.entity.TransferEntity;
import com.entity.TransferMemberEntity;
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
public class TransferMemberService {
    @Autowired
    UserDao userDao;
    @Autowired
    TransferMemberDao transferMemberDao;

    /**
     * 保存指派人员
    */
    @Transactional
    public boolean saveTransferMember(ArrayList<Map<String, Object>> transferMembers, TransferEntity transfer) {
        try {
            for (Map<String, Object> map : transferMembers) {
                String openId = (String) map.get("openId");
                String hql = "from User where openId=:openId";
                Map<String, Object> params = new HashedMap();
                params.put("openId", openId);
                List<User> list = userDao.findBy(hql, params);
                User user = list.get(0);
                TransferMemberEntity transferMember = new TransferMemberEntity(user, transfer);
                transferMemberDao.saveTransferMember(transferMember);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return true;
    }
}

