package com.dao.impl;

import com.dao.BaseDao;
import com.entity.TransferMemberEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by zengqin on 2017/4/7.
*/
@Repository("TransferMemberDao")
public class TransferMemberDao extends BaseDao<TransferMemberEntity> {
    /**
     * 保存指派人员
    */
    public boolean saveTransferMember(TransferMemberEntity transferMember){
        try {
            this.save(transferMember);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }
}

