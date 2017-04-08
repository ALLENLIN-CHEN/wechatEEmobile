package com.controller;

import com.entity.Pager;
import com.entity.newT.ScheduleT;
import com.service.MyService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/8.
 */
@Controller
@RequestMapping(value = "my")
public class MyController {
    @Autowired
    MyService myService;
    Map<String,Object> dataMap=new HashedMap();

    @RequestMapping(value = "myTransfer")
    @ResponseBody
    public Map<String ,Object> myTransfer(HttpServletRequest request){
        dataMap.clear();
        try{
            String openId=request.getParameter("openId");
            int currentPageNumber = request.getParameter("currentPageNumber")!=null?Integer.parseInt(request.getParameter("currentPageNumber")):1;
            int pageSize = request.getParameter("pageSize")!=null?Integer.parseInt(request.getParameter("pageSize")):5;
            Pager pagerModel = new Pager(currentPageNumber, pageSize);
            pagerModel=myService.findMyTransferSchedule(openId,pagerModel);
            List<ScheduleT> schedules = pagerModel.getDataList();
            int totalSize = pagerModel.getTotalSize();
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("schedules",schedules);
            dataMap.put("totalSize",totalSize);
        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("result", "fail");
            dataMap.put("resultTip", e.getMessage());
        }
        return  dataMap;
    }
}
