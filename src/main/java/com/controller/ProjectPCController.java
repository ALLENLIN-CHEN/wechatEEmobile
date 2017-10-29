package com.controller;

import com.dao.impl.SubprojectDao;
import com.dao.impl.TeamUserDao;
import com.entity.*;
import com.entity.newT.ProjectT;
import com.entity.newT.ScheduleT;
import com.entity.newT.UserT;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "projectPC")
public class ProjectPCController {
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
    TeamService teamService;
    @Autowired
    TeamUserDao teamUserDao;
    @Autowired
    UserService userService;
    @Autowired
    MemoForPersonService memoForPersonService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleMemberService scheduleMemberService;
    @Autowired
    MemoForSubprojectService memoForSubprojectService;
    @Autowired
    SubprojectDao subprojectDao;



    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listAllProject(
            @RequestParam String openId,
            @RequestParam List<String> teamStatus,
            @RequestParam int currentPageNumber,
            @RequestParam int pageSize
            ) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {

            Pager pagerModel = new Pager(currentPageNumber, pageSize);
            pagerModel = projectService.findByMutiStatus(teamStatus, pagerModel, openId);

            List projects = pagerModel.getDataList();
            List<ProjectT> projectTs = null;
            if (projects != null && projects.size() > 0) {
                projectTs = new ArrayList<ProjectT>();
                for (int i = 0; i < projects.size(); i++) {
                    Object[] obj = (Object[]) projects.get(i);
                    ProjectT projectT = new ProjectT();
                    if (obj[0] != null) {
                        projectT.setProjectId(Integer.parseInt(obj[0].toString()));
                    }
                    if (obj[1] != null) {
                        projectT.setSubprjectId(Integer.parseInt(obj[1].toString()));
                        List<UserT> userTs = projectService.findLeader(Integer.parseInt(obj[1].toString()));
                        projectT.setUserTs(userTs);
                        List<ScheduleT> scheduleTS =
                                scheduleService.findTaskStatusSubproject(Integer.parseInt(obj[1].toString()));
                        projectT.setSchedules(scheduleTS);
                    }
                    if (obj[2] != null) {
                        projectT.setSubproject(obj[2].toString());
                    }
                    if (obj[3] != null) {
                        projectT.setProject(obj[3].toString());
                    }
                    if (obj[4] != null) {
                        projectT.setTeamStatus(obj[4].toString());
                    }
                    if (obj[5] != null) {
                        projectT.setTeamId(Integer.parseInt(obj[5].toString()));
                    }
                    if (obj[6] != null) {
                        projectT.setTeamName(obj[6].toString());
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
     * 查看项目
     */
    @RequestMapping(value = "showProject",method = RequestMethod.GET)
    @ResponseBody
    public Project showProject(HttpServletRequest request) {
        return projectService.findById(Integer.parseInt(request.getParameter("projectId")));
    }

    /**
     * 查看子项目
     */
    @RequestMapping(value = "showSubProject",method = RequestMethod.GET)
    @ResponseBody
    public Subproject showSubProject(HttpServletRequest request) {
        return subprojectService.findById(Integer.parseInt(request.getParameter("subprojectId")));
    }

    /**
     * 更新项目
     */
    @RequestMapping(value = "updateProject",method = RequestMethod.POST)
    @ResponseBody
    public Project updateProject(@RequestBody Project project) {
        projectService.updateMainProjectWithoutSubProjectAndTeam(project);
        return project;
    }

    /**
     * 更新项目
     */
    @RequestMapping(value = "updateProjectName",method = RequestMethod.POST)
    @ResponseBody
    public Project updateProjectName(@RequestParam int projectId,@RequestParam String projectName) {
        Project project = projectService.findById(projectId);
        if(project == null)
            return null;
        project.setProject(projectName);
        projectService.updateMainProject(project);
        return project;
    }

    /**
     * 删除项目
     */
    @RequestMapping(value = "deleteProject",method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteProject(@RequestParam int projectId) {
        return projectService.deleteProject(projectId);
    }

    /**
     * 创建项目
     */
    @RequestMapping(value = "createProject",method = RequestMethod.POST)
    @JsonIgnoreProperties(value={"team"})
    @ResponseBody
    public Project createProject(@RequestBody Project project,@RequestParam int teamid) {
        project.setTeam(teamService.findTeam(teamid));
        project.setProjectTime(new Date());
        return projectService.createNewPCProject(project);
    }

    /**
     * 创建子项目
     */
    @Deprecated
    @RequestMapping(value = "createSubProject",method = RequestMethod.POST)
    @ResponseBody
    public Subproject createSubProject(@RequestBody Subproject subproject) {
        return projectService.createNewPCSubProject(subproject);
    }


    /**
     * 删除子项目
     */
    @Deprecated
    @RequestMapping(value = "removeSubProject",method = RequestMethod.DELETE)
    @ResponseBody
    public Subproject removeSubProject(@RequestParam int subProjectId) {
        return projectService.removeSubProject(subProjectId);
    }

    /**
     * 根据teamId获取项目id/名称列表
     */
    @RequestMapping(value = "listProjectByTeam",method = RequestMethod.GET)
    @ResponseBody
    public  List listProjectsIdAndNameByTeamId(@RequestParam int teamid) {
        return projectService.findTeamProjectListByTeamId(teamid);
    }

    /**
     * 根据teamId所属团队人员(包括标签）
     */
    @RequestMapping(value = "listTeamMembersAndTags",method = RequestMethod.GET)
    @ResponseBody
    public  List listTeamMeber(@RequestParam String teamid) {
        return projectMemberService.teamMembers(teamid);
    }

    /**
     * 根据项目id获取子项目id/名称列表
     */
    @RequestMapping(value = "listSubProjectByTeam",method = RequestMethod.GET)
    @ResponseBody
    public  List listSubProjectByTeam(@RequestParam int projectid) {
        return subprojectService.findSubProjectListByProjectId(projectid);
    }


    /**
     * 显示项目下的任务列表
     */
    @RequestMapping(value="findSchedules")
    @ResponseBody
    public Map<String,Object> findSchedules(HttpServletRequest request,
                                            @RequestParam List<Character> status){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try{
            int currentPageNumber = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber")):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize")):5;
            int projectId=Integer.parseInt(request.getParameter("projectId"));
            int subprojectId=Integer.parseInt(request.getParameter("subprojectId"));


            Pager pagerModel = new Pager(currentPageNumber, pageSize);

                pagerModel=projectService.findSchedules(projectId,subprojectId,null,null,pagerModel,status);

            List<ScheduleT> schedules = pagerModel.getDataList();
            // 查询负责任人
            List<UserT> leaders = projectService.findLeader(subprojectId);

            // 查询非负责人
            List<UserT> notLeaders = projectService.findNoLeaders(subprojectId);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("schedules",schedules);
            dataMap.put("totalSize",totalSize);
            dataMap.put("leader",leaders);
            dataMap.put("members",notLeaders);
            System.out.println(dataMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;
    }
}


