package com.dao.impl;

import com.dao.BaseDao;
import com.entity.TransferEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by zengqin on 2017/4/7.
*/
@Repository("TransferDao")
public class TransferDao extends BaseDao<TransferEntity> {
    /**
     * 保存转移
    */
    public boolean saveTransfer(TransferEntity transfer){
        try {
            this.save(transfer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }
}


