package com.controller;

import com.entity.Pager;
import com.entity.ProjectMember;
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
    ProjectMemberService projectMemberService;
    @Autowired
    MemoForPersonService memoForPersonService;

    private Map<String, Object> dataMap = new HashMap<String, Object>();
    private Pager pagerModel = new Pager(1, 5);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 团队模块中的通用方法
     * 获取teamId的初始值
     */
    public Integer initTeamId(String openId){
        List teamUsers=teamUserService.findTeamUsersByOpenId(openId);
        Integer teamIdTemp=0;
        if(teamUsers.size()!=0){
            Object [] row=(Object[]) teamUsers.get(0);
            teamIdTemp=Integer.parseInt(row[0].toString());
        }
        return teamIdTemp;
    }

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
     *2.1 团队-人员
     * 团队模块中对人员的任务强度统计(团队id已知)
     * 按团队人员名称、时间范围查询人员已超期、未完成的任务的个数
     */
    @RequestMapping(value = "findTaskIntensityForPerson")
    @ResponseBody
    public Map<String, Object> findTaskIntensityForPerson(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId = (String) request.getParameter("openId");
            Integer teamIdTemp=initTeamId(openId);
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):teamIdTemp;
            String memberName = (String) request.getParameter("memberName");
            String startTime=(String) request.getParameter("startTime");
            String endTime=(String) request.getParameter("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }
            ArrayList<Map> data=scheduleMemberService.findTaskIntensityForPerson(pagerModel,teamId,memberName,startTime,endTime);

            int []total=new int[data.size()];
            for (int i=0;i<data.size();i++){
                total[i]=Integer.parseInt(data.get(i).get("taskUnfinished").toString())+Integer.parseInt(data.get(i).get("taskOverTime").toString());
            }
            sort(total);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("max",total[0]);
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
     *2.1.5 团队-人员-统计
     * 团队模块中对人员的任务强度中的任务进行详细统计(列表)
     * 根据团队中成员微信号（memberOpenId）获取该成员的超期和未完成任务列表
     * 需要给出登录者的角色，以便确认是否有新建任务的权限
     */
    @RequestMapping(value = "taskIntensityStatisticsForPerson")
    @ResponseBody
    public Map<String, Object> taskIntensityStatisticsForPerson(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=(String) request.getParameter("openId");//登录者微信号
            String memberOpenId=(String) request.getParameter("memberOpenId");//被查看团队成员微信号
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):0;
//            Integer teamId = Integer.parseInt(json.get("teamId").toString());
            String startTime=(String) request.getParameter("startTime");
            String endTime=(String) request.getParameter("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }

            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
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
     * 2.1.6 一键询问
     * openId：询问人（登录者），memberOpenId：被询问人，subprojectId：任务Id
     */
    @RequestMapping(value = "inquiry")
    @ResponseBody
    public  Map<String,Object> inquiry(HttpServletRequest request){
        dataMap.clear();
        try {
            String openId=(String) request.getParameter("openId");//登录者微信号
            String memberOpenId=(String) request.getParameter("memberOpenId");//被查看团队成员微信号
            Integer scheduleId = request.getParameter("scheduleId")!=null?Integer.parseInt(request.getParameter("scheduleId").toString()):0;
            memoForPersonService.inquiry(openId,memberOpenId,scheduleId);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
        }catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        System.out.println(dataMap);
        return dataMap;
    }

    /**
     *2.1.7 团队-人员-分析
     * 团队模块中对人员的任务强度中的任务进行分析（折线图）
     * 根据团队中成员微信号（memberOpenId）获取该成员超期、未完成、已完成任务的三条折线
     * 三个时间范围：近两周（weekly） 近一个月（monthly） 近半年（yearly）
     */
    @RequestMapping(value = "taskIntensityAnalyzeForPerson")
    @ResponseBody
    public Map<String, Object> taskIntensityAnalyzeForPerson(HttpServletRequest request) {
        dataMap.clear();
        try {
            String openId=(String) request.getParameter("openId");//登录者微信号
            String memberOpenId=(String) request.getParameter("memberOpenId");//被查看团队成员微信号
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):0;
            String period=request.getParameter("period")!=null?request.getParameter("period").toString():"weekly";//时间范围

            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
            Map data=scheduleMemberService.findTaskIntensityAnalyzeForPerson(teamId,memberOpenId,period);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
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
     *2.1.9 团队-项目-分析
     * 团队模块中对子项目的任务强度中的任务进行分析（折线图）
     * 根据团队中子项目Id（subprojectId）获取该子项目超期、未完成、已完成任务三条折线
     * 三个时间范围：近两周（weekly） 近一个月（monthly） 近半年（yearly）
     */
    @RequestMapping(value = "taskIntensityAnalyzeForSubproject")
    @ResponseBody
    public Map<String, Object> taskIntensityAnalyzeForSubproject(HttpServletRequest request) {
        dataMap.clear();
        try {
            String openId=(String) request.getParameter("openId");//登录者微信号
            Integer subprojectId= Integer.parseInt(request.getParameter("subprojectId").toString());//被查看子项目id
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):0;
            String period=request.getParameter("period")!=null?request.getParameter("period").toString():"weekly";//时间范围

            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
            Map data=scheduleService.findTaskIntensityAnalyzeForSubproject(teamId,subprojectId,period);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
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
     * 2.1.8 团队-项目-新建任务选择项目（根据teamId返回在该团队下的所有项目）
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
     * 2.1.8 团队-项目-新建任务选择子任务
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
     * 2.1.1 & 2.1.2 团队-项目
     *团队模块中对子项目的任务强度统计(团队id已知)[子项目列表]
     * 按团队子项目名称、子项目状态标签、时间范围、查询项目中已超期、未完成的任务的个数
     */
    @RequestMapping(value = "findTaskIntensityForProject")
    @ResponseBody
    public Map<String, Object> findTaskIntensityForProject(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
//            Integer teamId = Integer.parseInt(json.get("teamId").toString());
            String openId = (String) request.getParameter("openId");
            Integer teamIdTemp=initTeamId(openId);
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):teamIdTemp;
            String subprojectName = (String) request.getParameter("subprojectNames");
            String subprojectStatus=(String) request.getParameter("subprojectStatus");//子项目状态
            String startTime=(String) request.getParameter("startTime");
            String endTime=(String) request.getParameter("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }
            ArrayList<Map> data=subprojectService.findTaskIntensityForSubproject(pagerModel,teamId,subprojectName,subprojectStatus,startTime,endTime);

            int []total=new int[data.size()];
            for (int i=0;i<data.size();i++){
                total[i]=Integer.parseInt(data.get(i).get("taskUnfinished").toString())+Integer.parseInt(data.get(i).get("taskOverTime").toString());
            }
            sort(total);

            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("max",total[0]);
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
     * 2.1.3 & 2.1.4 团队-项目-统计
     *团队模块中对项目的任务强度中的任务进行详细统计(任务列表)
     * 根据团队中成员微信号（subproject）获取该子项目的超期和未完成任务列表
     * 需要给出登录者的角色，以便确认是否有新建任务的权限
     */
    @RequestMapping(value = "taskIntensityStatisticsForSubproject")
    @ResponseBody
    public Map<String, Object> taskIntensityStatisticsForSubproject(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=(String) request.getParameter("openId");//登录者微信号
//            Integer teamId = Integer.parseInt(json.get("teamId").toString());// 登录者
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):0;
            Integer subprojectId = Integer.parseInt(request.getParameter("subprojectId").toString());// 被查看的子项目id
            String scheduleType=(String) request.getParameter("scheduleType");
            String startTime=(String) request.getParameter("startTime");
            String endTime=(String) request.getParameter("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }
//            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
            List<TeamUser> list=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId);
            int canModify=0;
            for(TeamUser teamUser:list){
                if(teamUser.getRole()==1){
                    canModify=1;
                }
            }
            ProjectMember projectMember=teamUserService.findP(openId,subprojectId);
            if(projectMember.getRoleType()!='d'){
                canModify=1;
            }
            ArrayList data=scheduleService.findTaskIntensityStatisticsForSubproject(pagerModel,subprojectId, scheduleType,startTime,endTime);

            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
            dataMap.put("canModify",canModify);
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
     *2.2、团队-人力分布
     * 按团队、子项目名称、子项目状态标签查询子项目中的人数
     */
    @RequestMapping(value = "manpowerDistributionForTeam")
    @ResponseBody
    public Map<String, Object> manpowerDistributionForTeam(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):0;
            String subprojectName = (String) request.getParameter("subprojectNames");
            String subprojectStatus=(String) request.getParameter("subprojectStatus");//子项目状态

            ArrayList data=projectMemberService.findManpowerDistributionForTeam(pagerModel,teamId,subprojectName,subprojectStatus);
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
     *@author rthtr 2017/4/10
     *2.3、团队-计划安排-会议(scheduleType="f")/团队-计划安排-出图(scheduleType="e")
     * 根据选择的团队、搜索的任务名称、任务截止日期范围查询详细的计划安排（任务）列表
     */
    @RequestMapping(value = "scheduleStatisticsForSubproject")
    @ResponseBody
    public Map<String, Object> scheduleStatisticsForSubproject(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=(String) request.getParameter("openId");//登录者微信号
            Integer teamIdTemp=initTeamId(openId);
            Integer teamId = request.getParameter("teamId")!=null?Integer.parseInt(request.getParameter("teamId").toString()):teamIdTemp;
            String searchingForSheduleName=request.getParameter("searchingForSheduleName");
            String startTime=(String) request.getParameter("startTime");
            String endTime=(String) request.getParameter("endTime");
            if(startTime==null||endTime==null||startTime.equals("")||endTime.equals("")){
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, - 1);
                startTime=simpleDateFormat.format(c.getTime());
                endTime=simpleDateFormat.format(new Date());
            }
            String scheduleType=(String) request.getParameter("scheduleType");//登录者微信号

            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
            ArrayList data=scheduleService.findStatisticsForSubproject(pagerModel,teamId, scheduleType,searchingForSheduleName,startTime,endTime);
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
     * 对项目的修改权限判断
     * @param request
     * @return
     */
    @RequestMapping(value = "canModify")
    @ResponseBody
    public Map<String, Object> modify(@RequestBody String request){
        dataMap.clear();
        try {
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String openId=(String)json.get("openId");
            int teamId=Integer.parseInt((String)json.get("teamId"));
            int subprojectId=Integer.parseInt((String)json.get("subprojectId"));
            List<TeamUser> list=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId);
            int canModify=0;
            for(TeamUser teamUser:list){
                if(teamUser.getRole()==1){
                    canModify=1;
                }
            }
            ProjectMember projectMember=teamUserService.findP(openId,subprojectId);
            if(projectMember.getRoleType()!='d'){
                canModify=1;
            }
            dataMap.put("result","success");
            dataMap.put("resultTip", "");
            dataMap.put("canModify",canModify);
        }catch (Exception e){
            e.getStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return dataMap;

    }
    //teamController中用于排序(寻找最大值)的通用方法
    public void sort(int[] a)
    {
        int temp = 0;
        for (int i = a.length - 1; i > 0; --i)
        {
            for (int j = 0; j < i; ++j)
            {
                if (a[j + 1]>a[j])
                {
                    temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

}


