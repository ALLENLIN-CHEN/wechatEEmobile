package com.dao.impl;

import com.dao.BaseDao;
import com.entity.TagDictEntity;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by congzihan on 17/7/28.
 */
@Repository("TagDictDao")
public class TagDictDao extends BaseDao<TagDictEntity> {
    public List<TagDictEntity> getAllTagByTeamId(int teamId,String tagType){
        String hql = "from TagDictEntity where teamId=:teamId and tagType=:tagType group by tagName";
        Map<String, Object> params = new HashedMap();
        params.put("teamId", teamId);
        params.put("tagType", tagType);
        List<TagDictEntity> users = this.findByHql(hql, params, null);
        return users;
    }


}
