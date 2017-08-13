package com.entity;

import javax.persistence.*;

/**
 * Created by congzihan on 17/8/6.
 */
@Entity
@Table(name = "tagRelation", schema = "projectdatabase", catalog = "")
public class TagRelationEntity {
    private int tagRelationId;
    private String wechatId;
    private String tagType;
    private Integer tagId;

    public TagRelationEntity(String tagType, Integer tagId, Integer taskId) {
        this.tagType = tagType;
        this.tagId = tagId;
        this.taskId = taskId;
    }

    private Integer taskId;



    public TagRelationEntity(String tagType, Integer tagId, String wechatId) {
        this.wechatId = wechatId;
        this.tagType = tagType;
        this.tagId = tagId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagRelationId", nullable = false)
    public int getTagRelationId() {
        return tagRelationId;
    }

    public void setTagRelationId(int tagRelationId) {
        this.tagRelationId = tagRelationId;
    }

    @Basic
    @Column(name = "wechatId", nullable = true, length = 50)
    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    @Basic
    @Column(name = "tagType", nullable = true, length = 50)
    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @Basic
    @Column(name = "tagId", nullable = true)
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "taskId", nullable = true)
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagRelationEntity that = (TagRelationEntity) o;

        if (tagRelationId != that.tagRelationId) return false;
        if (wechatId != null ? !wechatId.equals(that.wechatId) : that.wechatId != null) return false;
        if (tagType != null ? !tagType.equals(that.tagType) : that.tagType != null) return false;
        if (tagId != null ? !tagId.equals(that.tagId) : that.tagId != null) return false;
        if (taskId != null ? !taskId.equals(that.taskId) : that.taskId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tagRelationId;
        result = 31 * result + (wechatId != null ? wechatId.hashCode() : 0);
        result = 31 * result + (tagType != null ? tagType.hashCode() : 0);
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        return result;
    }
}
