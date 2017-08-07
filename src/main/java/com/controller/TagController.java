package com.controller;

import com.entity.TagDictEntity;
import com.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by congzihan on 17/7/28.
 */

@Controller
@RequestMapping(value = "tag")
public class TagController {
    @Autowired
    TagService tagService;

    // 根据teamId获取全部的标签
    @RequestMapping(value = "people/list", method = RequestMethod.GET)
    @ResponseBody
    public List<TagDictEntity> getAllTagsByTeamId(@RequestParam int teamId) {
        return tagService.allTagsByTeamId(teamId);
    }


    // 创建一个标签
    @RequestMapping(value = "people", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TagDictEntity saveTagDict(@RequestParam String tagName, @RequestParam int teamId) {
        return tagService.saveTag(tagName, teamId);
    }

    // 根据标签名称查询所属的人员
    @RequestMapping(value = "people/find", method = RequestMethod.GET)
    @ResponseBody
    public TagDictEntity getPeopleByTagList(@RequestParam List<String> tagNames) {
        return tagService.getPeopleByTagList(tagNames);
    }

    //对任务或者人员绑定标签
    @RequestMapping(value = "peopl/bind", method = RequestMethod.GET)
    @ResponseBody
    public List bindTag(@RequestParam String id,@RequestParam List<Integer> tags,@RequestParam String bindType) {
        return tagService.bindTag(id,tags,bindType);
    }

    @RequestMapping(value = "people", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePeopleTag(@RequestParam int tagId) {
        tagService.deleteTagById(tagId);
    }


}
