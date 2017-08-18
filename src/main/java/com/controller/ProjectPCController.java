package com.controller;

import com.dao.impl.SubprojectDao;
import com.dao.impl.TeamUserDao;
import com.entity.Pager;
import com.entity.Project;
import com.entity.newT.ProjectT;
import com.entity.newT.UserT;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * 更新项目
     */
    @RequestMapping(value = "updateProject",method = RequestMethod.POST)
    @ResponseBody
    public Project updateProject(@RequestBody Project project) {
        projectService.updateMainProjectWithoutSubProjectAndTeam(project);
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
    @ResponseBody
    public Project createProject(@RequestBody Project project) {
        project.setProjectTime(new Date());
        return projectService.createNewPCProject(project);
    }


}


