package com.service;

import com.dao.impl.MemoForPersonDao;
import com.entity.MemoForPerson;
import com.entity.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rthtr on 2017/4/15.
 */
@Service
public class MemoForPersonService {
    @Autowired
    MemoForPersonDao memoForPersonDao;

    public ArrayList findPersonMemo(Pager pager, String openId, String searchString) {
        ArrayList<Map> arrayList = new ArrayList<Map>();
        Pager subprojectList = memoForPersonDao.findPersonMemo(pager, openId, searchString);
        for (int i = 0; i < subprojectList.getDataList().size(); i++) {
            Object[] row = (Object[]) subprojectList.getDataList().get(i);

            Map<String, Object> mapForOneMemo = new HashMap<String, Object>();
            mapForOneMemo.put("subprojectName", row[0]);
            mapForOneMemo.put("projectName", row[1]);
            mapForOneMemo.put("content", row[2]);
            mapForOneMemo.put("date", row[3]);
            mapForOneMemo.put("memoId",row[4]);
            mapForOneMemo.put("hasRead",row[5]);

            if (Integer.parseInt(row[5].toString())==0){
                MemoForPerson memoForPerson=memoForPersonDao.getMemoForPersoById(Integer.parseInt(row[4].toString()));
                memoForPerson.setHasRead(1);
                memoForPersonDao.update(memoForPerson);
            }
            arrayList.add(mapForOneMemo);
        }
        return arrayList;
    }

    public int getCountForPersonMemos(String openId){
        int count = memoForPersonDao.getCountForPersonMemos(openId);
        return count;
    }

}
