package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.entity.TagDictEntity;
import com.entity.newT.TeamUserT2;
import com.service.TagService;

import com.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by congzihan on 17/7/28.
 */

@Controller
@RequestMapping(value = "tag")
public class TagController {
    @Autowired
    TagService tagService;


    // 创建多个标签
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public List<TagDictEntity> saveTagDict(@RequestBody List<TagDictEntity> tagDictEntities) {
        tagService.batchSaveTags(tagDictEntities);
        return tagDictEntities;
    }

    // 删除多个标签
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean batchDeleteTag(@RequestBody List<Integer> tagId) {
        boolean flag = tagService.batchDeleteTag(tagId);
        return flag;
    }


    // 根据teamId获取全部的标签（分组）
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public List<TagDictEntity> getAllTagsByTeamId(@RequestParam int teamId, @RequestParam String tagType) {
        return tagService.allTagsByTeamId(teamId, tagType);
    }


    // 根据teamId和标签类型获取标签的全部成员（分组）
    @RequestMapping(value = "listTagMember", method = RequestMethod.GET)
    @ResponseBody
    public List getAllTagMemberByTeamId(@RequestParam int teamId, @RequestParam List<Integer> tagids) {
        return tagService.getAllTagMemberByTeamId(teamId,tagids);
    }



    //对任务或者人员绑定标签
    @RequestMapping(value = "people/bind", method = RequestMethod.POST)
    @ResponseBody
    public List bindTag(@RequestParam String id, @RequestParam List<Integer> tags, @RequestParam String bindType) {
        return tagService.bindTag(id, tags, bindType);
    }


    // 根据标签名称查询已经绑定后的人员
    @RequestMapping(value = "people/binded/list", method = RequestMethod.GET)
    @ResponseBody
    public TagDictEntity getPeopleByTagList(@RequestParam List<String> tagNames) {
        return tagService.getPeopleByTagList(tagNames);
    }

}
