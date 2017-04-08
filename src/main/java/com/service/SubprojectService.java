package com.service;

import com.dao.impl.ScheduleDao;
import com.dao.impl.SubprojectDao;
import com.entity.Pager;
import com.entity.Subproject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/6.
 */
@Service
public class SubprojectService {
    @Autowired
    SubprojectDao subprojectDao;
    @Autowired
    ScheduleDao scheduleDao;

    @Transactional
    public boolean saveSubproject(Subproject subproject){
        return subprojectDao.saveSubproject(subproject);
    }
    public Subproject findById(int subprojectId){
        return subprojectDao.findById(subprojectId);
    }
    /**
     * 根据projectId查找属于项目（project）的所有子项目（subProject）
     */
    public ArrayList findSubProjectListByProjectId(Integer projectId){
        ArrayList arrayList=new ArrayList();
        List list=subprojectDao.findSubProjectListByProjectId(projectId);
        for(int i=0;i<list.size();i++){
            Object [] row=(Object [])list.get(i);
            Map<String,Object> projectMap=new HashedMap();
            projectMap.put("subprojectId",row[0]);
            projectMap.put("subprojectName",row[1]);
            arrayList.add(projectMap);
        }
        return arrayList;
    }

    /**
     * 团队模块中对子项目的任务强度统计[子项目列表]
     */
    public ArrayList findTaskIntensityForSubproject(Pager pager, Integer teamId, String subprojectName, String subprojectStatus, String startTime, String endTime){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager subprojectList=subprojectDao.findSubprojectListByTeamId(pager,teamId,subprojectName,subprojectStatus);
        for(int i=0;i<subprojectList.getDataList().size();i++){
            Object [] row=(Object[])subprojectList.getDataList().get(i);
            List dataForOverTime=scheduleDao.findTaskOverTimeForSubproject(Integer.parseInt(row[0].toString()),startTime,endTime);
            Long dataForOverTimeAmount = dataForOverTime.isEmpty() ? 0 : (Long) dataForOverTime.get(0);
            List dataUnfinished=scheduleDao.findUnfinishedForSubproject(Integer.parseInt(row[0].toString()),startTime,endTime);
            Long dataUnfinishedAmount = dataUnfinished.isEmpty() ? 0 : (Long) dataUnfinished.get(0);

            Map<String,Object> mapForOneSubproject=new HashMap<String, Object>();
            mapForOneSubproject.put("taskUnfinished",dataUnfinishedAmount);
            mapForOneSubproject.put("taskOverTime",dataForOverTimeAmount);
            mapForOneSubproject.put("subprojectId",row[0]);
            mapForOneSubproject.put("subprojectName",row[1]);
            mapForOneSubproject.put("projectId",row[2]);
            mapForOneSubproject.put("projectName",row[3]);
            arrayList.add(mapForOneSubproject);
        }
        return arrayList;
    }
}
