package com.service;

import com.dao.impl.*;
import com.entity.*;
import com.entity.newT.UserT;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zengqin on 2017/3/24.
 */
@Service
public class ProjectService {
    @Autowired
    private ProjectDao projectDao;
    private TeamDao teamDao;
    @Autowired
    private ScheduleDao scheduleDao;
    @Autowired
    private SubprojectDao subprojectDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ScheduleMemberDao scheduleMemberDao;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ProjectMemberService projectMemberService;
    @Autowired
    private TeamUserDao teamUserDao;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 分页显示项目列表
     * 按项目阶段查询
     * 按项目名字搜索
     *
     * @param
     * @param pagerModel
     * @return
     */
    public Pager findByStatus(String teamStatus, String project, Pager pagerModel, String openId) {

        String hql = "select p.projectId,sub.subprojectId,sub.subproject, p.project, sub.teamStatus,p.team.teamId,p.team.teamName"
                + " from Subproject sub left join sub.project p  where p.team.teamId in(select t.team.teamId from TeamUser t where t.user.openId=:openId and t.role!=0) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        if (teamStatus != null) {
            params.put("teamStatus", teamStatus);
            hql += "and sub.teamStatus=:teamStatus";
        }
        if (project != null) {
            params.put("project", "%" + project + "%");
            hql += " and p.project like :project ";
        }
        return projectDao.findByPage(hql, pagerModel, params);
    }

    /**
     * 分页显示项目列表
     * 按项目阶段查询
     * 按项目名字搜索
     *
     * @param
     * @param pagerModel
     * @return
     */
    public Pager findByMutiStatus(List<String> teamStatus, Pager pagerModel, String openId) {

        String hql = "select p.projectId,sub.subprojectId,sub.subproject, p.project, sub.teamStatus,p.team.teamId,p.team.teamName"
                + " from Subproject sub left join sub.project p  where p.team.teamId in(select t.team.teamId from TeamUser t where t.user.openId=:openId and t.role!=0 ) and sub.teamStatus in (:teamStatus)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("openId", openId);
        params.put("teamStatus", teamStatus);

        return projectDao.findByPage(hql, pagerModel, params);
    }


    public Project findById(int projectId) {
        return projectDao.findById(projectId);
    }

    /**
     * 查询项目负责人
     */
    public List<UserT> findLeader(int subprojectId) {
        String hql = "select new com.entity.newT.UserT(m.user.openId,m.user.userName) from ProjectMember m " +
                " where m.subproject.subprojectId=:subprojectId and m.roleType=:roleType";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subprojectId", subprojectId);
        params.put("roleType", 'a');
        return projectDao.findById(params, hql);
    }

    /**
     * 查询项目其他人员
     */
    public List<UserT> findNoLeaders(int subprojectId) {
        String hql = "select new com.entity.newT.UserT(m.user.openId,m.user.userName) from ProjectMember m " +
                " where m.subproject.subprojectId=:subprojectId and m.roleType in ('b','c','d')";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subprojectId", subprojectId);
        return projectDao.findById(params, hql);
    }

    /**
     * 查询所有团队
     */
    public List findAllTeam() {
        String hql = "select new com.entity.newT.TeamT(t.teamId,t.teamName) from Team t";
        return projectDao.findAllTeam(hql);

    }

    /**
     * 按teamId查询团队
     */
    public List findByTeamId(int teamId) {
        String hql = "select t.teamId,t.teamName from Team t "
                + " where t.teamId=:teamId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teamId", teamId);
        return projectDao.findById(params, hql);
    }

    /**
     * 获取团队所有人员
     */
    public List findAllMember(int teamId) {
        String hql = "select distinct new com.entity.newT.UserT(u.openId,u.userName) " +
                "from TeamUser tu left join tu.user u left join tu.team t" +
                " where t.teamId=:teamId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teamId", teamId);
        return projectDao.findAllMember(params, hql);
    }

    /**
     * 创建新项目
     */
    @Transactional
    public boolean createNewProject(Project project) {
        return projectDao.create_project(project);
    }
    /**
     * 创建新项目
     */
    @Transactional
    public Project createNewPCProject(Project project) {
         projectDao.create_project(project);
         return project;
    }
    /**
     * 创建新子项目
     */
    @Transactional
    public Subproject createNewPCSubProject(Subproject subproject) {
        subprojectDao.saveSubproject(subproject);
        Set<ProjectMember> projectMemberSet = subproject.getProjectMembers();
        for (ProjectMember projectMember:projectMemberSet) {
            projectMember.setSubproject(subproject);
            projectMemberService.saveProjectMember(projectMember);
        }
        return subproject;
    }

    @Transactional
    public void updateMainProject(Project project) {
        projectDao.update(project);
    }

    @Transactional
    public void updateMainProjectWithoutSubProjectAndTeam(Project project) {
        String hql = "update Project set " +
                "project=:project, type=:type, area=:area, contractStage=:contractStage, contractAmount=:contractAmount, " +
                "completedAmount=:completedAmount, receivedAmount=:receivedAmount, contractBalance=:contractBalance " +
                "where projectId=:projectId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", project.getProjectId());
        params.put("project", project.getProject());
        params.put("type", project.getType());
        params.put("area", project.getArea());
        params.put("contractStage", project.getContractStage());
        params.put("contractAmount", project.getContractAmount());
        params.put("completedAmount", project.getCompletedAmount());
        params.put("receivedAmount", project.getReceivedAmount());
        params.put("contractBalance", project.getContractBalance());
        projectDao.updateBy(hql,params,null);
    }


    /**
     * 根据人员职位查询人员
     */
    public List findByPosition(int teamId, String position) {
        String hql = "select new com.entity.newT.UserT(u.openId,u.userName) " +
                "from TeamUser tu left join tu.user u left join tu.team t" +
                " where t.teamId=:teamId and u.position like :position";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teamId", teamId);
        params.put("position", "%" + position + "%");
        return projectDao.findByPosition(hql, params, null);
    }

    /**
     * 显示项目下的任务列表
     */
    public Pager findSchedules(int projectId, int subprojectId, String taskContent, Character taskType, Pager pagerModel,List<Character> status) {
        String hql = "select new com.entity.newT.ScheduleT(task.taskContent, task.taskReply, task.taskType, task.scheduleId,p.projectId," +
                " p.project, sub.subprojectId, sub.subproject,task.taskTime, task.taskStatus, task.user.openId)"
                + "from Subproject sub left join sub.project p left join sub.schedules task "
                + "where sub.subprojectId=:subprojectId and p.projectId=:projectId and task.taskStatus in :status";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("subprojectId", subprojectId);
        params.put("status", status);
        return projectDao.findByPage(hql, pagerModel, params);
    }

    /**
     * 显示项目下的任务列表
     */
    public Pager findSchedules(int projectId, int subprojectId, String taskContent, Character taskType, Pager pagerModel) {
        String hql = "select new com.entity.newT.ScheduleT(task.taskContent, task.taskReply, task.taskType, task.scheduleId,p.projectId, p.project, sub.subprojectId, sub.subproject,task.taskTime)"
                + "from Subproject sub left join sub.project p left join sub.schedules task "
                + "where sub.subprojectId=:subprojectId and p.projectId=:projectId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("subprojectId", subprojectId);
        if (taskContent != null) {
            hql += " and task.taskContent like :taskContent ";
            params.put("taskContent", "%" + taskContent + "%");
        }
        if (taskType != null) {
            hql += " and task.taskType=:taskType ";
            params.put("taskType", taskType);
        }
        return projectDao.findByPage(hql, pagerModel, params);

    }

    /**
     * 修改任务完成的最后时间
     */
    @Transactional //解决 No Session found for current thread
    public boolean doExtension(String scheduleId, String date) {

        try {
            Schedule s = scheduleDao.load(Schedule.class, Integer.parseInt(scheduleId));
            Date date1 = simpleDateFormat.parse(date);
            s.setTaskTime(date1);
            scheduleDao.save(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    ;

    /**
     * 标记已完成
     */
    @Transactional
    public boolean markAsDone(String scheduleId) {
        try {
            Schedule s = scheduleDao.load(Schedule.class, Integer.parseInt(scheduleId));
            s.setTaskStatus('a');
            scheduleDao.save(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 标记未完成
     */
    @Transactional
    public boolean markAsUndone(String scheduleId) {

        try {
            Schedule s = scheduleDao.load(Schedule.class, Integer.parseInt(scheduleId));
            s.setTaskStatus('b');
            scheduleDao.save(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 标记超期
     */
    @Transactional
    public boolean markAsOverdue(String scheduleId) {

        try {
            Schedule s = scheduleDao.load(Schedule.class, Integer.parseInt(scheduleId));
            s.setTaskStatus('c');
            scheduleDao.save(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获取任务现有人员
     */
    @Transactional
    public List findScheduleMember(String scheduleId) {
        String hql = "select new com.entity.newT.ScheduleMemberT(sm.user.openId,sm.user.userName) from ScheduleMember sm where sm.schedule.scheduleId=" + scheduleId;
        List list = projectDao.findByHql(hql, null, null);
        return list;
    }

    /**
     * 查询团队中不在项目内的成员
     */
    @Transactional
    public List findOtherMember(String teamId, String projectId) {
        String hql = "select new com.entity.newT.TeamUserT(tu.user.openId,tu.user.userName) from TeamUser tu where tu.team.teamId=" + teamId + " and tu.user.openId not in(select p.user.openId from ProjectMember p where p.subproject.project.projectId=" + projectId + ")";
        List list = projectDao.findByHql(hql, null, null);
        return list;
    }

    /**
     * 添加任务成员
     */
    @Transactional
    public boolean addScheduleMember(String scheduleId, Set<ScheduleMember> scheduleMemberSet) {
        try {
            Schedule schedule = scheduleService.findById(Integer.parseInt(scheduleId));
            //  Set<ScheduleMember> scheduleMembers=schedule.getScheduleMembers();
            for (ScheduleMember member : scheduleMemberSet) {
                member.setSchedule(schedule);
                scheduleMemberDao.save(member);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 删除任务成员
     */
    @Transactional
    public boolean deleteScheduleMember(String scheduleId, String openId) {
        try {
            String hql = "from ScheduleMember where scheduleId=:scheduleId ";
            Map<String, Object> params = new HashedMap();
            params.put("scheduleId", Integer.parseInt(scheduleId));
            List<ScheduleMember> list = scheduleMemberDao.findByHql(hql, params, null);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUser().getOpenId().equals(openId)) {
                    scheduleMemberDao.delete(list.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建新的任务
     */
    @Transactional
    public boolean addNewSchedulepublic(String subprojectId, String taskContent,
                                        Character taskType, String taskTime,
                                        Set<ScheduleMember> scheduleMemberSet, String taskReply, String openid) {
        try {
            Subproject subproject = subprojectDao.load(Subproject.class, Integer.parseInt(subprojectId));
            Date taskTime1 = simpleDateFormat.parse(taskTime);
            Schedule schedule = new Schedule();
            schedule.setSubproject(subproject);
            schedule.setTaskContent(taskContent);
            schedule.setTaskTime(taskTime1);
            schedule.setTaskReply(taskReply);
            schedule.setTaskType(taskType);
            if (openid != null) {
                String hql = "from User where openId=:openId";
                Map<String, Object> params = new HashedMap();
                params.put("openId", openid);
                List<User> users = userDao.findByHql(hql, params, null);
                User user = users.get(0);
                schedule.setUser(user);
            }
            scheduleDao.save(schedule);
            Set<ScheduleMember> scheduleMembers = new HashSet<ScheduleMember>();
            for (ScheduleMember member : scheduleMemberSet) {
                member.setSchedule(schedule);
                scheduleMemberDao.save(member);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 更新某人对某一任务的最新回复
     */
    @Transactional
    public boolean updateReply(String task_reply, String scheduleId) {
        Schedule s = null;
        try {
            s = scheduleDao.load(Schedule.class, Integer.parseInt(scheduleId));
            String oldReply = s.getTaskReply();
            if (oldReply == null) {
                oldReply = "";
            }
            s.setTaskReply(oldReply + "</br>" + task_reply);
            scheduleDao.save(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 根据teamId查找属于该团队的所有项目
     */
    public ArrayList findTeamProjectListByTeamId(Integer teamId) {
        ArrayList arrayList = new ArrayList();
        List list = projectDao.findTeamProjectListByTeamId(teamId);
        for (int i = 0; i < list.size(); i++) {
            Object[] row = (Object[]) list.get(i);
            Map<String, Object> projectMap = new HashedMap();
            projectMap.put("projectId", row[0]);
            projectMap.put("projectName", row[1]);
            arrayList.add(projectMap);
        }
        return arrayList;
    }


    public RecordEntity findByRecordId(int recordId) {
        String hql = "from RecordEntity where recordId=:recordId";
        Map<String, Object> params = new HashedMap();
        params.put("recordId", recordId);
        List<RecordEntity> list = projectDao.findById(params, hql);
        return list.get(0);
    }

    /**
     * 删除任务(任务转移中使用)
     */
    @Transactional
    public void deleteSchedule(int scheduleId) {
        String hql = "delete Schedule as s where s.scheduleId=" + scheduleId;
        projectDao.deleteByHql(hql, null, null);
    }

    /**
     * 删除项目
     */
    public boolean deleteProject(int projectId) {
        String hql = "delete from Project where projectId=:projectId";
        Map<String, Object> params = new HashMap<>();
        params.put("projectId", projectId);
        return projectDao.deleteByHql(hql, params, null);
    }

    /**
     * 根据openId查询与该人员相关的项目
     */
    public List findProject(String openId) {
        String hql = "select p.projectId,p.project from Project p  where " +
                " p.team.teamId in(select t.team.teamId from TeamUser t where t.user.openId=:openId)";
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        List list = projectDao.findByHql(hql, params, null);
        return list;
    }

    /**
     * 根据项目Id查询其所有子项目
     */
    public List findSubproject(int projectId) {
        String hql = "select sub.subprojectId,sub.subproject from Subproject sub left join sub.project p " +
                " where p.projectId=:projectId ";
        Map<String, Object> params = new HashMap<>();
        params.put("projectId", projectId);
        List list = projectDao.findByHql(hql, params, null);
        return list;
    }

    /**
     * 根据openId查询与该人员相关的项目及子项目
     */
    public List findRelatedProjects(String openId) {
        String hql = "select p.projectId,p.project from Project p  where " +
                " p.team.teamId in(select t.team.teamId from TeamUser t where t.user.openId=:openId)";
        Map<String, Object> params = new HashMap<>();
        params.put("openId", openId);
        List list = projectDao.findByHql(hql, params, null);
        return list;
    }

}