package com.service;

import com.dao.impl.MemoForSubprojectDao;
import com.dao.impl.UserDao;
import com.entity.MemoForSubproject;
import com.entity.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rthtr on 2017/4/15.
 */
@Service
public class MemoForSubprojectService {
    @Autowired
    MemoForSubprojectDao memoForSubprojectDao;
    @Autowired
    UserDao userDao;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *根据subprojectId、模糊搜索查询子项目的大事记
     */
    public ArrayList<Map> findEventsExtendForMemo(Pager pager, Integer subprojectId, String searchString){
        ArrayList<Map> arrayList=new ArrayList<Map>();
        Pager subprojectList=memoForSubprojectDao.findEventsExtendForMemo(pager,subprojectId,searchString);
        for(int i=0;i<subprojectList.getDataList().size();i++){
            Object [] row=(Object[])subprojectList.getDataList().get(i);

            Map<String, Object> mapForOneMemo = new HashMap<String, Object>();
            mapForOneMemo.put("subprojectName", row[0]);
            mapForOneMemo.put("projectName", row[1]);
            mapForOneMemo.put("content", row[2]);
            mapForOneMemo.put("date", row[3]);
            mapForOneMemo.put("memoId",row[4]);
            mapForOneMemo.put("hasRead",row[5]);

            if (Integer.parseInt(row[5].toString())==0){
                MemoForSubproject memoForSubproject=memoForSubprojectDao.getMemoForSubprojectById(Integer.parseInt(row[4].toString()));
                memoForSubproject.setHasRead(1);
                memoForSubprojectDao.update(memoForSubproject);
            }

            arrayList.add(mapForOneMemo);
        }
        return arrayList;
    }
    /**
     * 保存大事记信息
     */
    @Transactional
    public void saveEvents(MemoForSubproject memoForSubproject){
        memoForSubprojectDao.save(memoForSubproject);
    }
}
