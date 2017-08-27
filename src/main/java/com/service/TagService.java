package com.service;

import com.alibaba.fastjson.JSONObject;
import com.dao.impl.TagDictDao;
import com.dao.impl.TagRelationDao;
import com.entity.TagDictEntity;
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
    private TagRelationDao tagRelationDao;


    public List<TagDictEntity> allTagsByTeamId(int teamId, String tagType) {
        return tagDictDao.getAllTagByTeamId(teamId, tagType);
    }

    public JSONObject getAllTagMemberByTeamId(int teamId, String tagType) {
        List<HashMap<String,Object>> result =  tagDictDao.getAllTagMemberByTeamId(teamId, tagType);
        JSONObject jsonObject = new JSONObject();
        for (HashMap hashMap: result){
            String key = hashMap.get("tagName").toString();
            if (jsonObject.get(key) == null){
                ArrayList arrayList = new ArrayList();
                arrayList.add(hashMap);
                jsonObject.put(key,arrayList);
            }
            else {
                ArrayList arrayList = (ArrayList) jsonObject.get(key);
                arrayList.add(hashMap);
            }
        }
        return jsonObject;
    }


    public TagDictEntity getPeopleByTagList(List<String> tagNames) {
        String hql = "from TagDicEntity where tagNames=:tagNames inner join ";
        return null;
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
