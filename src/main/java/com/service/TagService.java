package com.service;

import com.alibaba.fastjson.JSONObject;
import com.dao.impl.TagDictDao;
import com.dao.impl.TagRelationDao;
import com.dao.impl.TeamUserDao;
import com.entity.TagDictEntity;
import com.entity.Team;
import com.entity.newT.TeamUserT2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by congzihan on 17/7/28.
 */
@Service
public class TagService {


    @Autowired
    private TagDictDao tagDictDao;
    @Autowired
    private TeamUserDao teamUserDao;
    @Autowired
    private TagRelationDao tagRelationDao;


    public List<TagDictEntity> allTagsByTeamId(int teamId, String tagType) {
        return tagDictDao.getAllTagByTeamId(teamId, tagType);
    }

    public List<TagDictEntity> getAllTagMemberByTeamId(int teamId, List<Integer> tagids) {
        List<TagDictEntity> tags = tagDictDao.getAllTagMemberByTeamId(teamId, tagids);

        return tags;
    }


    public TagDictEntity getPeopleByTagList(List<String> tagNames) {
        String hql = "from TagDicEntity where tagNames=:tagNames inner join ";
        return null;
    }

    public List<TeamUserT2> findTeamLeaderByTeamId(String teamId) {
        return teamUserDao.findTeamUsersLeaderByTeamId(teamId);
    }


    public List bindTag(String id, List<Integer> tags, String bindType) {
        return tagRelationDao.bindTag(id, tags, bindType);
    }


    public void batchSaveTags(List<TagDictEntity> tagDictEntities) {
        tagDictDao.batchSaveTag(tagDictEntities);
    }

    public TagDictEntity getTagById(int tagId) {
        return tagDictDao.get(TagDictEntity.class, tagId);
    }

    public boolean batchDeleteTag(List<Integer> tagId) {
        return tagDictDao.batchDeleteTagByIds(tagId);
    }
}
