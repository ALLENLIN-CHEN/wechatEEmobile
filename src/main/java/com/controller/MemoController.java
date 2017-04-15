package com.controller;

import com.entity.Pager;
import com.entity.TeamUser;
import com.service.MemoForPersonService;
import com.service.MemoForSubprojectService;
import com.service.ProjectMemberService;
import com.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rthtr on 2017/4/13.
 */
@Controller
@RequestMapping(value = "memo")
public class MemoController {
    @Autowired
    ProjectMemberService projectMemberService;
    @Autowired
    MemoForPersonService memoForPersonService;
    @Autowired
    MemoForSubprojectService memoForSubprojectService;


    private Map<String, Object> dataMap = new HashMap<String, Object>();
    private Pager pagerModel = new Pager(1, 5);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *4、备忘录-大事记
     * 根据openId、模糊搜索查询登录者参与的子项目
     */
    @RequestMapping(value = "eventsForMemo")
    @ResponseBody
    public Map<String, Object>  eventsForMemo(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=(String) request.getParameter("openId");//登录者微信号
            String searchString=(String) request.getParameter("searchString");//被查看团队成员微信号
//            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
            ArrayList data=projectMemberService.findEventsForMemo(pagerModel,openId,searchString);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
//            dataMap.put("role",teamUserForOpen.getRole());
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
     *4.1 备忘录-大事记-展开
     * 根据subprojectId、模糊搜索查询子项目大事记的备忘录
     */
    @RequestMapping(value = "eventsExtendForMemo")
    @ResponseBody
    public Map<String, Object>  eventsExtendForMemo(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            Integer subprojectId=Integer.parseInt(request.getParameter("subprojectId").toString()) ;//被查看的子项目Id
            String searchString=(String) request.getParameter("searchString");//被查看团队成员微信号
            ArrayList data=memoForSubprojectService.findEventsExtendForMemo(pagerModel,subprojectId,searchString);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
//            dataMap.put("role",teamUserForOpen.getRole());
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
     *4.2 备忘录-个人
     * 根据登录者openId、模糊搜索查询与登录者相关的备忘录
     */
    @RequestMapping(value = "personMemo")
    @ResponseBody
    public Map<String, Object>  personMemo(HttpServletRequest request) {
        dataMap.clear();
        try {
            int currentPage = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber").toString()):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize").toString()):5;
            pagerModel.setCurrentPageNumber(currentPage);
            pagerModel.setPageSize(pageSize);
            String openId=request.getParameter("openId").toString() ;//登录者的openId
            String searchString=(String) request.getParameter("searchString");//被查看团队成员微信号
//            TeamUser teamUserForOpen=teamUserService.findTeamUsersByOpenIdAndTeamId(openId, teamId).get(0);
            ArrayList data=memoForPersonService.findPersonMemo(pagerModel,openId,searchString);
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("totalSize",totalSize);
//            dataMap.put("role",teamUserForOpen.getRole());
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
     *统计备忘录中未读消息数（备忘录-个人）
     * 根据登录者openId查询与登录者相关的未读备忘录条数
     */
    @RequestMapping(value = "countForPersonMemos")
    @ResponseBody
    public Map<String, Object>  countForPersonMemos(HttpServletRequest request) {
        dataMap.clear();
        try {
            String openId=request.getParameter("openId").toString() ;//登录者的openId
            int count=memoForPersonService.getCountForPersonMemos(openId);
            dataMap.put("result", "success");
            dataMap.put("resultTip", "");
            dataMap.put("count",count);
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
