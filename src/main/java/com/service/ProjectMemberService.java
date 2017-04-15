package com.service;

import com.dao.impl.ProjectMemberDao;
import com.dao.impl.SubprojectDao;
import com.entity.Pager;
import com.entity.ProjectMember;
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
public class ProjectMemberService {
    @Autowired
    ProjectMemberDao projectMemberDao;
    @Autowired
    SubprojectDao subprojectDao;

    @Transactional
    public boolean saveProjectMember(ProjectMember projectMember){
        return projectMemberDao.saveProjectMember(projectMember);
    }

    /**
     * 团队模块中对子项目中人力分布统计
     */
    public ArrayList findManpowerDistributionForTeam(Pager pager, Integer teamId, String subprojectName, String subprojectStatus){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager subprojectList=subprojectDao.findSubprojectListByTeamId(pager,teamId,subprojectName,subprojectStatus);
        for(int i=0;i<subprojectList.getDataList().size();i++){
            Object [] row=(Object[])subprojectList.getDataList().get(i);
            List manpowerForSubproject = projectMemberDao.findManpowerForSubproject(Integer.parseInt(row[0].toString()));
            Long numberOfPeople = manpowerForSubproject.isEmpty() ? 0 : (Long) manpowerForSubproject.get(0);

            Map<String,Object> mapForOneSubproject=new HashMap<String, Object>();
            mapForOneSubproject.put("numberOfPeople",numberOfPeople);
            mapForOneSubproject.put("subprojectId",row[0]);
            mapForOneSubproject.put("subprojectName",row[1]);
            mapForOneSubproject.put("projectId",row[2]);
            mapForOneSubproject.put("projectName",row[3]);
            arrayList.add(mapForOneSubproject);
        }
        return arrayList;
    }

    /**
     * 备忘录模块中对与登录者相关的大事记子目录列表进行统计
     */
    public ArrayList findEventsForMemo(Pager pager,String openId,String searchString){
        ArrayList <Map> arrayList=new ArrayList<Map>();
        Pager subprojectList=projectMemberDao.findEventsForMemo(pager,openId,searchString);
        for(int i=0;i<subprojectList.getDataList().size();i++){
            Object [] row=(Object[])subprojectList.getDataList().get(i);
            List principalList = projectMemberDao.getPrincipalListBySubprojectId(Integer.parseInt(row[0].toString()));

            Map<String,Object> mapForOneSubproject=new HashMap<String, Object>();
            mapForOneSubproject.put("principalList",principalList);//负责人
            mapForOneSubproject.put("subprojectId",row[0]);
            mapForOneSubproject.put("subprojectName",row[1]);
            mapForOneSubproject.put("projectId",row[2]);
            mapForOneSubproject.put("projectName",row[3]);
            mapForOneSubproject.put("stage",row[4]);
            arrayList.add(mapForOneSubproject);
        }
        return arrayList;
}

}
