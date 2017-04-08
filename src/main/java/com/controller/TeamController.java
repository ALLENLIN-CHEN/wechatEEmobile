package com.controller;

import com.entity.Pager;
import com.entity.TeamUser;
import com.service.*;
import com.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rthtr on 2017/4/2.
 */
@Controller
@RequestMapping(value="team")
public class TeamController {
    @Autowired
    TeamService teamService;
    @Autowired
    TeamUserService teamUserService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleMemberService scheduleMemberService;
    @Autowired
    ProjectService projectService;
    @Autowired
    SubprojectService subprojectService;

    @Autowired
    private Map<String, Object> dataMap = new HashMap<String, Object>();
    private Pager pagerModel = new Pager(1, 5);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *进入团队页面，显示登录者可查看（所在）的团队列表
     */
    @RequestMapping(value = "teamList")
    @ResponseBody
    public Map<String, Object> teamList(HttpServletRequest request) {
        dataMap.clear();
        try {
            String openId = (String) request.getParameter("openId");
            List teamUsers=teamUserService.findTeamUsersByOpenId(openId);

            ArrayList <Map> arrayList=new ArrayList<Map>();
            for(int i=0;i<teamUsers.size();i++){
                Object []row = (Object [])teamUsers.get(i);
                Map<String,Object> team=new HashMap<String, Object>();
                team.put("teamId",row[0]);
                team.put("teamName",row[1]);
                arrayList.add(team);
            }

            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("teams", arrayList);
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
     *团队模块中对人员的任务强度统计(团队id已知)
     * 按团队人员名称、时间范围查询人员已超期、未完成的任务的个数
     */
    @RequestMapping(value = "findTaskIntensityForPerson")
    @ResponseBody
    public Map<String, Object> findTaskIntensityForPerson(@RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            int currentPage = json.get("currentPageNumber")!=null?Integer.parseInt(json.get("currentPageNumber").toString()):1;
            int pageSize = json.get("pageSize")!=null?Integer.parseInt(json.get("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            Integer teamId = Integer.parseInt(json.get("teamId").toString());
            String memberName = (String) json.get("memberName");
            String startTime=(String) json.get("startTime");
            String endTime=(String) json.get("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }
            ArrayList data=scheduleMemberService.findTaskIntensityForPerson(pagerModel,teamId,memberName,startTime,endTime);

            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("data",data);
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
     *团队模块中对人员的任务强度中的任务进行详细统计(列表)
     * 根据团队中成员微信号（memberOpenId）获取该成员的超期和未完成任务列表
     * 需要给出登录者的角色，以便确认是否有新建任务的权限
     */
    @RequestMapping(value = "taskIntensityStatisticsForPerson")
    @ResponseBody
    public Map<String, Object> taskIntensityStatisticsForPerson(@RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            int currentPage = json.get("currentPageNumber")!=null?Integer.parseInt(json.get("currentPageNumber").toString()):1;
            int pageSize = json.get("pageSize")!=null?Integer.parseInt(json.get("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=(String) json.get("openId");//登录者微信号
            String memberOpenId=(String) json.get("memberOpenId");//被查看团队成员微信号
            Integer teamId = Integer.parseInt(json.get("teamId").toString());
            String startTime=(String) json.get("startTime");
            String endTime=(String) json.get("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }

            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId);
            ArrayList data=scheduleMemberService.findTaskIntensityStatisticsForPerson(pagerModel,teamId,memberOpenId,startTime,endTime);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("role",teamUserForOpen.getRole());
            dataMap.put("data",data);

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
     *团队模块中创建新任务时的下拉框提供项目列表
     *根据teamId查找属于该团队的所有项目
     */
    @RequestMapping(value = "teamProjectListForSelect")
    @ResponseBody
    public Map<String, Object> teamProjectListForSelect(HttpServletRequest request) {
        dataMap.clear();
        try {
            Integer teamId = Integer.parseInt(request.getParameter("teamId").toString());//团队id
            ArrayList data=projectService.findTeamProjectListByTeamId(teamId);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("data",data);

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
     *创建新任务时的下拉框提供子项目列表（需在选定项目后才能提供子项目列表）
     *根据projectId查找属于项目（project）的所有子项目（subProject）
     */
    @RequestMapping(value = "subprojectListForSelect")
    @ResponseBody
    public Map<String, Object> subprojectListForSelect(HttpServletRequest request) {
        dataMap.clear();
        try {
            Integer projectId = Integer.parseInt(request.getParameter("projectId").toString());//团队id
            ArrayList data=subprojectService.findSubProjectListByProjectId(projectId);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("data",data);

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
     *团队模块中对子项目的任务强度统计(团队id已知)[子项目列表]
     * 按团队子项目名称、子项目状态标签、时间范围、查询项目中已超期、未完成的任务的个数
     */
    @RequestMapping(value = "findTaskIntensityForProject")
    @ResponseBody
    public Map<String, Object> findTaskIntensityForProject(@RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            int currentPage = json.get("currentPageNumber")!=null?Integer.parseInt(json.get("currentPageNumber").toString()):1;
            int pageSize = json.get("pageSize")!=null?Integer.parseInt(json.get("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            Integer teamId = Integer.parseInt(json.get("teamId").toString());
            String subprojectName = (String) json.get("subprojectNames");
            String subprojectStatus=(String) json.get("subprojectStatus");//子项目状态
            String startTime=(String) json.get("startTime");
            String endTime=(String) json.get("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }
            ArrayList data=subprojectService.findTaskIntensityForSubproject(pagerModel,teamId,subprojectName,subprojectStatus,startTime,endTime);

            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("data",data);
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
     *团队模块中对项目的任务强度中的任务进行详细统计(任务列表)
     * 根据团队中成员微信号（subproject）获取该子项目的超期和未完成任务列表
     * 需要给出登录者的角色，以便确认是否有新建任务的权限
     */
    @RequestMapping(value = "taskIntensityStatisticsForSubproject")
    @ResponseBody
    public Map<String, Object> taskIntensityStatisticsForSubproject(@RequestBody String request) {
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            int currentPage = json.get("currentPageNumber")!=null?Integer.parseInt(json.get("currentPageNumber").toString()):1;
            int pageSize = json.get("pageSize")!=null?Integer.parseInt(json.get("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=(String) json.get("openId");//登录者微信号
            Integer teamId = Integer.parseInt(json.get("teamId").toString());// 登录者
            Integer subprojectId = Integer.parseInt(json.get("subprojectId").toString());// 被查看的子项目id
            String scheduleType=(String) json.get("scheduleType");
            String startTime=(String) json.get("startTime");
            String endTime=(String) json.get("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }

            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId);
            ArrayList data=scheduleService.findTaskIntensityStatisticsForSubproject(pagerModel,subprojectId, scheduleType,startTime,endTime);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("role",teamUserForOpen.getRole());
            dataMap.put("data",data);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        System.out.println(dataMap);
        return dataMap;
    }


}
