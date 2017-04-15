package com.service;

import com.dao.impl.*;
import com.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rthtr on 2017/4/15.
 */
@Service
@Transactional
public class MemoForPersonService {
    @Autowired
    MemoForPersonDao memoForPersonDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ProjectMemberDao projectMemberDao;
    @Autowired
    ScheduleDao scheduleDao;

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

    public void inquiry(String openId, String memberOpenId, Integer scheduleId){

        User user=userDao.getUserByOpenId(openId);
        User member=userDao.getUserByOpenId(memberOpenId);
        Schedule schedule=scheduleDao.findByScheduleId(scheduleId);
        ProjectMember projectMember=projectMemberDao.findByOpenIdAndSubprojectId(openId,schedule.getSubproject().getSubprojectId());
        String role="";
        if(projectMember.getRoleType().equals('a')){
            role="项目负责人";
        }else if(projectMember.getRoleType().equals('b')){
            role="秘书";
        }else if (projectMember.getRoleType().equals('c')){
            role="设计人员";
        }else if (projectMember.getRoleType().equals('d')){
            role="管理员";
        }
        String content=role+" <b>"+user.getUserName()+"</b> 询问您关于任务 "+"<b>"+schedule.getTaskContent()
                +"</b> 的进度，请您及时与他联系";
        MemoForPerson memoForPerson=new MemoForPerson();
        memoForPerson.setOpenId(member);
        memoForPerson.setSubprojectId(schedule.getSubproject());
        memoForPerson.setContent(content);
        memoForPerson.setOperatorOpenId(user);
        memoForPerson.setOperTime(new Date());
        memoForPerson.setHasRead(0);
        memoForPersonDao.save(memoForPerson);
    }

}
