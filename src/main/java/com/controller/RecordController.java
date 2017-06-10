package com.controller;

import com.entity.Pager;
import com.entity.RecordEntity;
import com.service.ProjectService;
import com.service.RecordService;
import com.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengqin on 2017/4/8.
*/
@Controller
@RequestMapping(value="record")
public class RecordController {
    @Autowired
    RecordService recordService;
    @Autowired
    ProjectService projectService;

    private Map<String,Object> dataMap=new HashMap<String,Object>();

    /**
     * 查看随手记/随手记警告
    */
    @RequestMapping(value = "recordList")
    @ResponseBody
    public Map<String,Object> findRecord(HttpServletRequest request){
        dataMap.clear();
        try{
            String openId=request.getParameter("openId");
            String currentPageNumber = request.getParameter("currentPageNumber")!=null?request.getParameter("currentPageNumber"):"1";
            String pageSize = request.getParameter("pageSize")!=null?request.getParameter("pageSize"):"5";
            Pager pagerModel=new Pager(Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
            pagerModel=recordService.findRecord(openId,pagerModel);
            List<RecordEntity> recordList=pagerModel.getDataList();
            dataMap.put("result","success");
            dataMap.put("totalSize",pagerModel.getTotalSize());
            dataMap.put("recordList",recordList);
        }catch (Exception e){
            e.getStackTrace();
            dataMap.put("result","fail");
            dataMap.put("resultTip",e.getMessage());
        }
        return dataMap;
    }
    /**
     * 随手记新建
     */
    @RequestMapping(value = "createRecord")
    @ResponseBody
    public Map<String,Object> createRecord(@RequestBody String request){
        dataMap.clear();
        try{
            Map<String, Object> json = JsonUtil.parseJSON2Map(request);
            String content=(String)json.get("content");
            String openId=(String)json.get("openId");
            recordService.createRecord(content,openId);
            dataMap.put("result","success");
            dataMap.put("resultTip","随手记新建成功");

        }catch (Exception e){
            e.getStackTrace();
            dataMap.put("result","fail");
            dataMap.put("resultTip",e.getMessage());
        }
        return  dataMap;
    }
    /**
     * 随手记-编辑
     */
    @RequestMapping(value = "updateRecord")
    @ResponseBody
    public Map<String,Object> updateRecord(@RequestBody String request){
        dataMap.clear();
        try {
            Map<String,Object> json=JsonUtil.parseJSON2Map(request);
            int recordId=Integer.parseInt((String)json.get("recordId"));
            String content=(String)json.get("content");
            recordService.updateRecord(content,recordId);
            dataMap.put("result","success");
            dataMap.put("resultTip","随手记编辑成功");
        }catch (Exception e){
            e.getStackTrace();
            dataMap.put("result","fail");
            dataMap.put("resultTip",e.getMessage());
        }
            return dataMap;
    }
    /**
     * 获取单个随手记转入任务
     */
    @RequestMapping(value = "recordToSchedule")
    @ResponseBody
    public Map<String,Object> recordToSchedule(HttpServletRequest request){
        dataMap.clear();
        try{
            int recordId=Integer.parseInt(request.getParameter("recordId"));
            //RecordEntity record=projectService.findByRecordId(recordId);
            RecordEntity record=recordService.findByRecordId(recordId);
            dataMap.put("result","success");
            dataMap.put("resultTip","");
            dataMap.put("record",record);
        }catch (Exception e){
            e.getStackTrace();
            dataMap.put("result","fial");
            dataMap.put("resultTip",e.getMessage());
        }

        return dataMap;
    }
    /**
     *随手记删除/删除随手记警告
     */
    @RequestMapping(value = "recordDelete")
    @ResponseBody
    public Map<String,Object> recordDelete(@RequestBody String request){
        dataMap.clear();
        try{
            Map<String,Object> json=JsonUtil.parseJSON2Map(request);
            int recordId=Integer.parseInt((String)json.get("recordId"));
            recordService.recordDelete(recordId);
            dataMap.put("result","success");
            dataMap.put("resultTip","随手记删除成功");
        }catch (Exception e){
            e.getStackTrace();
            dataMap.put("result","fial");
            dataMap.put("resultTip",e.getMessage());
        }
        return dataMap;
    }
}
