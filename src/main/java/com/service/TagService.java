package com.service;

import com.dao.impl.TagDictDao;
import com.dao.impl.TagRelationDao;
import com.entity.TagDictEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by congzihan on 17/7/28.
 */
@Service
public class TagService {


    @Autowired
    private TagDictDao tagDictDao;
    @Autowired
    private TagRelationDao tagRelationDao;


    public List<TagDictEntity> allTagsByTeamId(int teamId) {
        return tagDictDao.getAllTagByTeamId(teamId);
    }

    public TagDictEntity getPeopleByTagList(List<String> tagNames) {
        String hql = "from TagDicEntity where tagNames=:tagNames inner join ";
        return null;
    }

    public List bindTag(String id, List<Integer> tags, String bindType) {
        return tagRelationDao.bindTag(id, tags, bindType);
    }


    public TagDictEntity saveTag(String tagName, int teamId) {
        TagDictEntity tagDictEntity = new TagDictEntity();
        tagDictEntity.setTagName(tagName);
        tagDictEntity.setTeamId(teamId);
        tagDictDao.save(tagDictEntity);
        return tagDictEntity;
    }

    public TagDictEntity getTagById(int tagId) {
        return tagDictDao.get(TagDictEntity.class, tagId);
    }

    public void deleteTagById(int tagId) {
        TagDictEntity tagDictEntity = tagDictDao.load(TagDictEntity.class, tagId);
        tagDictDao.delete(tagDictEntity);
    }
}
