package com.service;

import com.dao.impl.*;
import com.entity.Pager;
import com.entity.ProjectMember;
import com.entity.Subproject;
import com.entity.User;
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
    @Autowired
    ProjectDao projectDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ProjectMemberDao projectMemberDao;

    @Transactional
    public boolean saveSubproject(Subproject subproject){
        return subprojectDao.saveSubproject(subproject);
    }
    @Transactional
    public void update(Subproject subproject){
         subprojectDao.update(subproject);
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
    /**
     * 查询子项目现有人员
     */
    @Transactional
    public List subprojectMembers(int subprojectId){
        String hql="select p.user.openId,p.user.userName from ProjectMember p left join p.subproject sub where sub.subprojectId=:subprojectId ";
        Map<String,Object>params=new HashMap<>();
        params.put("subprojectId",subprojectId);
        List list=subprojectDao.findByHql(hql,params,null);
        return list;
    }
    /**
     *查询项目现有人员
     */
    @Transactional
    public List projectMembers(int projectId){
        String hql="select p.user.openId,p.user.userName from ProjectMember p left join p.subproject sub where sub.project.projectId=:projectId";
        Map<String,Object>params=new HashMap<>();
        params.put("projectId",projectId);
        List list=projectDao.findByHql(hql,params,null);
        return  list;
    }
    /**
     * 删除子项目现有人员
     */
    @Transactional
    public void deleteSubprojectMember(int subprojectId,String openId){
        String hql="delete ProjectMember as p where p.subproject.subprojectId=:subprojectId and p.user.openId=:openId";
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("subprojectId",subprojectId);
        params.put("openId",openId);
        subprojectDao.deleteByHql(hql,params,null);
    }
    /**
     * 添加子项目人员
     */
    @Transactional
    public void addSubprojectMember(int subprojectId,String openId,char roleType){
        String hql="from User where openId=:openId";
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("openId",openId);
        List<User> list=userDao.findByHql(hql,params,null);
        User user=list.get(0);
        Subproject subproject=subprojectDao.get(Subproject.class,subprojectId);
        ProjectMember projectMember=new ProjectMember();
        projectMember.setRoleType(roleType);
        projectMember.setSubproject(subproject);
        projectMember.setUser(user);
        projectMemberDao.saveProjectMember(projectMember);

    }
}
