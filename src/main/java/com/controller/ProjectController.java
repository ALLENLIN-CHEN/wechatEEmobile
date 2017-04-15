package com.controller;

import com.dao.impl.SubprojectDao;
import com.dao.impl.TeamUserDao;
import com.entity.*;
import com.entity.newT.ProjectT;
import com.entity.newT.ScheduleT;
import com.entity.newT.TeamT;
import com.entity.newT.UserT;
import com.service.*;
import com.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping(value="project")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    TeamService T;
    @Autowired
    ProjectMemberService projectMemberService;
    @Autowired
    SubprojectService subprojectService;
    @Autowired
    TransferService transferService;
    @Autowired
    TransferMemberService transferMemberService;
    @Autowired
    TeamUserService teamUserService;
    @Autowired
    TeamUserDao teamUserDao;
    @Autowired
    SubprojectDao subprojectDao;

    private Map<String, Object> dataMap = new HashMap<String, Object>();
    private Pager pagerModel = new Pager(1, 5);

    /**
     * 分页显示项目列表 按子项目阶段查询
     * 按项目阶段查询
     * 按项目名字搜索
     */
    @RequestMapping(value = "projectList")
    @ResponseBody
    public Map<String, Object> findByStatus(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPageNumber = request.getParameter("currentPageNumber") != null ? Integer.parseInt(request.getParameter("currentPageNumber")) : 1;
            int pageSize = request.getParameter("pageSize") != null ? Integer.parseInt(request.getParameter("pageSize")) : 5;
            String teamStatus = request.getParameter("teamStatus");
            String project = request.getParameter("project");
            String openId=request.getParameter("openId");
            //int projectId=Integer.parseInt(request.getParameter("projectId"));
//            int projectId=Integer.parseInt(request.getParameter("projectId"));
            Pager pagerModel = new Pager(currentPageNumber, pageSize);
            if(teamStatus!=null&&project!=null) {
                pagerModel = projectService.findByStatus(teamStatus, project, pagerModel,openId);
            }
            if(teamStatus!=null&&project==null){
                pagerModel = projectService.findByStatus(teamStatus,null, pagerModel,openId);
            }
            if(teamStatus==null&&project==null){
                pagerModel = projectService.findByStatus(null,null, pagerModel,openId);
            }
            if(teamStatus==null&&project!=null){
                pagerModel = projectService.findByStatus(null,project, pagerModel,openId);
            }
            List projects = pagerModel.getDataList();
            List<ProjectT> projectTs=null;
            if(projects!=null&&projects.size()>0){
                projectTs=new ArrayList<ProjectT>();
                for(int i=0;i<projects.size();i++){
                    Object[] obj=(Object[])projects.get(i);
                    ProjectT projectT=new ProjectT();
                    if(obj[0]!=null){
                    projectT.setProjectId(Integer.parseInt(obj[0].toString()));
                    }
                    if(obj[1]!=null){
                        projectT.setSubprjectId(Integer.parseInt(obj[1].toString()));
                        List<UserT> userTs=projectService.findLeader(Integer.parseInt(obj[1].toString()));
                        projectT.setUserTs(userTs);
                    }
                    if(obj[2]!=null){
                        projectT.setSubproject(obj[2].toString());
                    }
                    if(obj[3]!=null){
                        projectT.setProject(obj[3].toString());
                    }
                    if(obj[4]!=null){
                        projectT.setTeamStatus(obj[4].toString());
                    }
                    if(obj[5]!=null) {
                        projectT.setTeamId(Integer.parseInt(obj[5].toString()));
                    }
                    projectTs.add(projectT);
                }
            }
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("projects", projectTs);
            dataMap.put("totalSize", totalSize);
            System.out.println(dataMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        System.out.println(dataMap);
        return dataMap;

    }
    /**
     * 查询所有团队
     */
    @RequestMapping(value="findAllTeam")
    @ResponseBody
    public Map<String, Object> findAllTeam(HttpServletRequest request){
        dataMap.clear();
        try {
            List<TeamT> teams = projectService.findAllTeam();
            for(TeamT t:teams){
                System.out.println(t.getTeamId());
            }
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("teams", teams);
            System.out.println(dataMap);
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;

    }
    /**
     * 获取团队所有人员
     */
    @RequestMapping(value="findAllMember")
    @ResponseBody
    public Map<String, Object>  findAllMember(HttpServletRequest request) {
        dataMap.clear();
        try {
            int teamId = Integer.parseInt(request.getParameter("teamId"));
            List<UserT> userT = projectService.findAllMember(teamId);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("users", userT);

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }
    /**
     * 根据人员职位查询人员
     */
    @RequestMapping(value = "findByPosition")
    @ResponseBody
    public Map<String, Object> findByPosition( HttpServletRequest request){
        dataMap.clear();
        try{
            String position=request.getParameter("position");
            int teamId = Integer.parseInt(request.getParameter("teamId"));
            List<UserT> userT = projectService.findByPosition(teamId,position);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("users", userT);

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;

    }

    /**
     * 创建新项目
     */
    @RequestMapping(value = "createNewProject")
    @ResponseBody
    public Map<String, Object> createNewProject(@RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String project = (String) json.get("project");
            String teamIdS = (String) json.get("teamId");
            int teamId=Integer.parseInt(teamIdS);
            Team team= T.findTeam(teamId);
            Project projectT = new Project();
            projectT.setTeam(team);
            projectT.setProject(project);
            projectService.createNewProject(projectT);
            ArrayList<Map<String, Object>> subprojectList = (ArrayList<Map<String, Object>>) json.get("subprojects");
            Set<ProjectMember> projectMembers = new HashSet<ProjectMember>();
            Set<Subproject> subprojects = new HashSet<Subproject>();
            for (Map<String, Object> map : subprojectList) {
                String subproject = (String) map.get("subproject");
                String teamStatus=(String)map.get("teamStatus");
                Subproject subproject1=new Subproject();
                subproject1.setSubproject(subproject);
                subproject1.setTeamStatus(teamStatus);
                subproject1.setProjectStatus('b');
                subproject1.setProject(projectT);
                subprojectService.saveSubproject(subproject1);
                ArrayList<Map<String, Object>> projectMemberList = (ArrayList<Map<String, Object>>) map.get("projectMembers");
                for (Map<String, Object> m : projectMemberList) {
                    String openId = (String) m.get("openId");
                    String roleTypeS = (String) m.get("roleType");
                    char roleType=roleTypeS.charAt(0);
                    User user = new User();
                    user.setOpenId(openId);
                    ProjectMember projectMember = new ProjectMember();
                    projectMember.setRoleType(roleType);
                    projectMember.setUser(user);
                    projectMember.setSubproject(subproject1);
                    projectMemberService.saveProjectMember(projectMember);
                    projectMembers.add(projectMember);
                }
                Subproject subprojectT = new Subproject();
                subprojectT.setSubproject(subproject);
                subprojectT.setProjectMembers(projectMembers);
                subprojects.add(subprojectT);
            }
            dataMap.put("result", "success");
            dataMap.put("resultTip", "项目创建成功");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }
    /**
     * 显示子项目现有人员
     */
    @RequestMapping(value = "subprojectMembers")
    @ResponseBody
    public Map<String,Object> subprojectMembers(HttpServletRequest request){
        dataMap.clear();
        try{
            int subprojectId= Integer.parseInt(request.getParameter("subprojectId"));
            List subprojectMembers=subprojectService.subprojectMembers(subprojectId);
            ArrayList<Map<String,Object>>data=new ArrayList<>();
            for(int i=0;i<subprojectMembers.size();i++){
                Map<String,Object> map=new HashMap<>();
                Object[] obj=(Object[])subprojectMembers.get(i);
                map.put("openId",obj[0].toString());
                map.put("userName",obj[1].toString());
                data.add(map);
            }
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("subprojectMembers",data);
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }

    /**
     * 显示项目现有人员
     */
    @RequestMapping(value = "projectMembers")
    @ResponseBody
    public Map<String,Object> projectMembers(HttpServletRequest request){
        dataMap.clear();
        try{
            int projectId= Integer.parseInt(request.getParameter("projectId"));
            List projectMembers=subprojectService.projectMembers(projectId);
            ArrayList<Map<String,Object>>data=new ArrayList<>();
            for(int i=0;i<projectMembers.size();i++){
                Map<String,Object> map=new HashMap<>();
                Object[] obj=(Object[])projectMembers.get(i);
                map.put("openId",obj[0].toString());
                map.put("userName",obj[1].toString());
                data.add(map);
            }
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("projectMembers",data);
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }

    /**
     * 删除子项目成员
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteProjecMember")
    @ResponseBody
    public Map<String,Object> deleteProjectMember(@RequestBody String request){
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            int subprojectId=Integer.parseInt((String)json.get("subprojectId"));
            ArrayList<Map<String, Object>> removeList = (ArrayList<Map<String, Object>>) json.get("removeList");
            for(Map<String,Object>map:removeList){
                String openId=(String)map.get("openId");
                subprojectService.deleteSubprojectMember(subprojectId,openId);
            }
            dataMap.put("result", "success");
            dataMap.put("resultTip", "人员删除成功");
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

         return dataMap;
    }

    /**
     * 添加子项目成员
     * @param request
     * @return
     */
    @RequestMapping(value = "addProjecMember")
    @ResponseBody
    public Map<String,Object> addProjectMember(@RequestBody String request){
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            int subprojectId=Integer.parseInt((String)json.get("subprojectId"));
            ArrayList<Map<String, Object>> addList = (ArrayList<Map<String, Object>>) json.get("addList");
            for(Map<String,Object>map:addList){
                String openId=(String)map.get("openId");
                String roleType1=(String)map.get("roleType");
                char roleType=roleType1.charAt(0);
                subprojectService.addSubprojectMember(subprojectId,openId,roleType);
            }
            dataMap.put("result", "success");
            dataMap.put("resultTip", "人员添加成功");
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return dataMap;
    }
    /**
     * 更新项目
     */
    @RequestMapping(value = "updateProject")
    @ResponseBody
    public Map<String, Object> updateProject(@RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String subproject=(String)json.get("subproject");
            ArrayList<Map<String,Object>> addList=(ArrayList<Map<String,Object>>)json.get("addList");
            ArrayList<Map<String,Object>>removeList=(ArrayList<Map<String,Object>>)json.get("removeList") ;
            String teamStatus=(String)json.get("teamStatus");
            int subprojectId=Integer.parseInt((String)json.get("subprojectId"));
            Subproject subproject1=subprojectService.findById(subprojectId);
            if(subproject!=null){
                subproject1.setSubproject(subproject);
            }
            if(teamStatus!=null){
                subproject1.setTeamStatus(teamStatus);
            }
            if(addList.size()>0) {
                for (Map<String, Object> map : addList){
                    String openId=(String)map.get("openId");
                    String roleType1=(String)map.get("roleType");
                    char roleType=roleType1.charAt(0);
                    subprojectService.addSubprojectMember(subprojectId,openId,roleType);
                }
            }
            if(removeList.size()>0){
                for(Map<String,Object>map1:removeList){
                    String openId=(String)map1.get("openId");
                    subprojectService.deleteSubprojectMember(subprojectId,openId);
                }
            }
                subprojectService.update(subproject1);
                dataMap.put("result", "success");
                dataMap.put("resultTip", "项目修改成功");
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }


    /**
     * 显示项目下的任务列表
     */
    @RequestMapping(value="findSchedules")
    @ResponseBody
    public Map<String,Object> findSchedules(HttpServletRequest request){
        dataMap.clear();
        try{
            int currentPageNumber = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber")):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize")):5;
            int projectId=Integer.parseInt(request.getParameter("projectId"));
            int subprojectId=Integer.parseInt(request.getParameter("subprojectId"));
            String taskContent=request.getParameter("taskContent");
            String taskType = request.getParameter("taskType");
            Pager pagerModel = new Pager(currentPageNumber, pageSize);
            if(taskContent==null&&taskType==null){
                pagerModel=projectService.findSchedules(projectId,subprojectId,null,null,pagerModel);
            }
            if(taskContent==null&&taskType!=null){
                pagerModel=projectService.findSchedules(projectId,subprojectId,null,taskType.charAt(0),pagerModel);
            }
            if(taskContent!=null&&taskType==null){
                pagerModel=projectService.findSchedules(projectId,subprojectId,taskContent,null,pagerModel);
            }
            if(taskContent!=null&&taskType!=null){
                pagerModel=projectService.findSchedules(projectId,subprojectId,taskContent,taskType.charAt(0),pagerModel);
            }
            List<ScheduleT> schedules = pagerModel.getDataList();
            for(ScheduleT T:schedules){
                System.out.println(T.getTaskContent());
            }
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("schedules",schedules);
            dataMap.put("totalSize",totalSize);
            System.out.println(dataMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }

    /**
     * 更新任务结束时间
     * @param request
     * @return
     */
    @RequestMapping(value="updateScheduleBydate",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> updateScheduleByDate(@RequestBody String request ){
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String id = (String)json.get("scheduleId");
            String date =(String) json.get("date");
            if(date==null||date.isEmpty()) {
                dataMap.put("result", "fail");
                dataMap.put("resultTip", "日期不能为空");
                return dataMap;
            }
            projectService.doExtension(id,date);
            dataMap.put("result","success");
            dataMap.put("resultTip",null);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 任务转移指派
    */
    @RequestMapping(value = "transferSchedule",method = RequestMethod.POST)
    @ResponseBody
    public Map<String ,Object> transferSchedule(@RequestBody String request){
        dataMap.clear();
        try{
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String scheduleId=(String)json.get("scheduleId");
            String openId=(String)json.get("openId");
            ArrayList<Map<String,Object>> transferMembers=(ArrayList<Map<String,Object>>)json.get("transferMembers");
            transferService.saveTransfer(Integer.parseInt(scheduleId),openId,transferMembers);
            projectService.deleteSchedule(Integer.parseInt(scheduleId));
            dataMap.put("result","success");
            dataMap.put("resultTip","转移成功");
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result","fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }


    /**
     * 更新任务状态
     * @param request
     * @return
     */
    @RequestMapping(value="updateScheduleByStatus",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> updateScheduleByStatus( @RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String id = (String)json.get("id");
            String status = (String)json.get("status");
            if(status==null||status.isEmpty()) {
                dataMap.put("result", "fail");
                dataMap.put("resultTip","");
                return dataMap;
            }
            if(status.equals("a"))
                projectService.markAsDone(id);
            else if(status.equals("c"))
                projectService.markAsOverdue(id);
            else if(status.equals("b"))
                projectService.markAsUndone(id);
            else{
                dataMap.put("result", "fail");
                dataMap.put("resultTip", "日期格式不正确");
                return dataMap;
            }

            dataMap.put("result","success");
            dataMap.put("resultTip",null);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 获取任务现有人员
     * @param request
     * @return
     */
    @RequestMapping(value="findScheduleMember",method = RequestMethod.GET)
    @ResponseBody
    public  Map<String, Object> findScheduleMember( HttpServletRequest request) {
        dataMap.clear();
        try {

            String currentPage = request.getParameter("currentPageNumber")!=null?request.getParameter("currentPageNumber"):"1";
            String pageSize = request.getParameter("pageSize")!=null?request.getParameter("pageSize"):"5";
            String scheduleId = request.getParameter("scheduleId");
            pagerModel.setCurrentPageNumber(Integer.parseInt(currentPage));
            pagerModel.setPageSize(Integer.parseInt(pageSize));
            if(scheduleId==null||scheduleId.isEmpty()) {
                dataMap.put("result", "fail");
                dataMap.put("resultTip", "任务Id不能为空");
                return dataMap;
            }
            pagerModel = projectService.findScheduleMember(scheduleId,pagerModel);
            dataMap.put("result","success");
            dataMap.put("totalSize",pagerModel.getTotalSize());
            dataMap.put("scheduleMember",pagerModel.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 查询团队中不在项目内的成员
     * @param request
     * @return
     */
    @RequestMapping(value="findOtherMember",method = RequestMethod.GET)
    @ResponseBody
    public  Map<String, Object> findOtherMember( HttpServletRequest request) {
        dataMap.clear();
        try {

            String currentPage = request.getParameter("currentPageNumber")!=null?request.getParameter("currentPageNumber"):"1";
            String pageSize = request.getParameter("pageSize")!=null?request.getParameter("pageSize"):"5";
            String teamId = request.getParameter("teamId");
            String projectId = request.getParameter("projectId");
            pagerModel.setCurrentPageNumber(Integer.parseInt(currentPage));
            pagerModel.setPageSize(Integer.parseInt(pageSize));
            pagerModel = projectService.findOtherMember(teamId,projectId,pagerModel);
            dataMap.put("result","success");
            dataMap.put("totalSize",pagerModel.getTotalSize());
            dataMap.put("teamUser",pagerModel.getDataList());
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 添加任务成员
     * @param request
     * @return
     */
    @RequestMapping(value="addScheduleMember",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> addScheduleMember( @RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String scheduleId = (String)json.get("scheduleId");
            ArrayList<Map<String, String>> scheduleMembers = (ArrayList<Map<String, String>>) json.get("scheduleMembers");
            Set<ScheduleMember> scheduleMemberSet=new HashSet<>();
            for(Map<String, String> map: scheduleMembers)
            {
                User user=new User();
                String openId=map.get("openId");
                user.setOpenId(openId);
                ScheduleMember scheduleMember=new ScheduleMember();
                scheduleMember.setUser(user);
                scheduleMemberSet.add(scheduleMember);
            }
            if(projectService.addScheduleMember(scheduleId,scheduleMemberSet)) {
                dataMap.put("result", "success");
                dataMap.put("resultTip", "人员添加成功");
            }else{
                dataMap.put("result", "fail");
                dataMap.put("resultTip", "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 删除任务成员
     * @param request
     * @return
     */
    @RequestMapping(value="deleteScheduleMember",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> deleteScheduleMember( @RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String scheduleId = (String) json.get("scheduleId");
            ArrayList<Map<String, String>> scheduleMembers = (ArrayList<Map<String, String>>) json.get("scheduleMembers");
            for(Map<String, String> map: scheduleMembers){
                String openId=map.get("openId");
                projectService.deleteScheduleMember(scheduleId,openId);
            }
            dataMap.put("result","success");
            dataMap.put("resultTip","删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 创建新的任务
     * @param request
     * @return
     */
    @RequestMapping(value="addNewSchedulepublic",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> addNewSchedulepublic( @RequestBody String request) {
        dataMap.clear();
        try {
                Map<String, Object> json = JsonUtil.parseJSON2Map(request);
                String subprojectId = (String) json.get("subprojectId");
                String taskContent =(String) json.get("taskContent");
                String taskType = (String)json.get("taskType");
                String taskTime = (String)json.get("taskTime");
                String taskReply =(String) json.get("taskReply");
                String openid=(String)json.get("openid");
                ArrayList<Map<String, String>> scheduleMembers = (ArrayList<Map<String, String>>) json.get("scheduleMembers");
            Set<ScheduleMember> scheduleMemberSet=new HashSet<>();
            for(Map<String, String> map: scheduleMembers)
            {
                User user=new User();
                String openId=map.get("openId");
                user.setOpenId(openId);
                ScheduleMember scheduleMember=new ScheduleMember();
                scheduleMember.setUser(user);
                scheduleMemberSet.add(scheduleMember);
            }
                projectService.addNewSchedulepublic(subprojectId,taskContent,taskType.charAt(0),taskTime,scheduleMemberSet,taskReply,openid);
                dataMap.put("result","success");
                dataMap.put("resultTip","任务创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }

    /**
     * 更新某人对某一任务的最新回复
     * @param request
     * @return
     */
    @RequestMapping(value="updateReply",method = RequestMethod.POST)
    @ResponseBody
    public  Map<String, Object> updateReply( @RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String task_reply = (String) json.get("task_reply");
            String scheduleId = (String)json.get("scheduleId");
            projectService.updateReply(task_reply,scheduleId);
            dataMap.put("result","success");
            dataMap.put("resultTip","");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }

        return  dataMap;
    }
    @RequestMapping(value = "deleteSchedule")
    @ResponseBody
    public Map<String, Object> delete(HttpServletRequest request){
        try {
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            projectService.deleteSchedule(scheduleId);
            dataMap.clear();
            dataMap.put("result", "success");
            return dataMap;
        }catch (Exception e){
            dataMap.put("resultTip",e.getMessage());
        }
        return dataMap;
    }

}


