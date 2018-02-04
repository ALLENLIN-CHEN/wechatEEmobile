package com.dao.impl;

import com.dao.BaseDao;
import com.entity.TagDictEntity;
import com.entity.TagRelationEntity;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by congzihan on 17/7/28.
 */
@Repository("TagDictDao")
public class TagDictDao extends BaseDao<TagDictEntity> {
    public List<TagDictEntity> getAllTagByTeamId(int teamId, String tagType) {
        String hql = "from TagDictEntity where teamId=:teamId and tagType=:tagType group by tagName";
        Map<String, Object> params = new HashedMap();
        params.put("teamId", teamId);
        params.put("tagType", tagType);
        List<TagDictEntity> users = this.findByHql(hql, params, null);
        return users;
    }

    public List getAllTagMemberByTeamId(int teamId, String tagType) {
        String hql = "SELECT tagId,wechatId,wechatName,tagName," +
                "user.phoneNum,user.email,user.qqNum,user.wechatNum FROM " +
                "tagRelation INNER JOIN user ON tagRelation.wechatId = user.openId " +
                "where teamId=:teamId and tagType=:tagType";
        Map<String, Object> params = new HashedMap();
        params.put("teamId", teamId);
        params.put("tagType", tagType);
        SQLQuery query = this.getCurrentSession().createSQLQuery(hql);
        //设定结果结果集中的每个对象为Map类型

        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        List<TagDictEntity> users = this.excuteBySQL(hql, params, query);
        return users;
    }

    public boolean batchDeleteTagByIds(List<Integer> ids) {
        String hql = "Delete from TagDictEntity where tagId in :ids";
        Map<String, Object> params = new HashedMap();
        params.put("ids", ids);
        return this.deleteByHql(hql, params, null);
    }

    public void batchSaveTag(List<TagDictEntity> tagDictEntities) {
        for (int i = 0; i < tagDictEntities.size(); i++) {
            this.save(tagDictEntities.get(i));
            if (i % 100 == 0) {             //以每100个数据作为一个处理单元
                this.getCurrentSession().flush();           //保持与数据库数据的同步
                this.getCurrentSession().clear();           //清楚Session级别的一级缓存的全部数据，及时释放占用的内存
            }
        }
    }


}
