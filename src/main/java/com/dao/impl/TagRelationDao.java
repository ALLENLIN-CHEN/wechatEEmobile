package com.dao.impl;

import com.dao.BaseDao;
import com.entity.TagRelationEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congzihan on 17/8/6.
 */
@Repository("TagRelationDao")
public class TagRelationDao extends BaseDao<TagRelationEntity> {
    public List<TagRelationEntity> bindTag(String id, List<Integer> tags,String typeName) {
        List<TagRelationEntity> relationEntities = new ArrayList<>();
        for(int tag : tags){
            TagRelationEntity tagRelationEntity = null;
            if(typeName.equals("人员绑定")) {
                 tagRelationEntity = new TagRelationEntity(typeName, tag, id);
            }
            if(typeName.equals("任务绑定")) {
                tagRelationEntity = new TagRelationEntity(typeName, tag, Integer.parseInt(id));
            }
            this.save(tagRelationEntity);
            relationEntities.add(tagRelationEntity);
        }
        return relationEntities;
    }


    public void canclePeopleTag(String wechatId, List<Integer> tags) {
        for(int tag : tags){
            this.save(new TagRelationEntity("人员绑定",tag,wechatId));
        }
    }
}
