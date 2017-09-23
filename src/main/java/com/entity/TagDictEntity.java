package com.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by congzihan on 17/7/28.
 */
@Entity
@Table(name = "tagDict", schema = "projectdatabase", catalog = "")
public class TagDictEntity {
    private int tagId;
    private String tagName;
    private int teamId;
    private String tagType;
    private String tagContent;

    @Basic
    @Column(name = "teamId", nullable = true)
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId", unique = true, nullable = false)
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "tagName", nullable = false, length = 32)
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDictEntity that = (TagDictEntity) o;

        if (tagId != that.tagId) return false;
        if (tagName != null ? !tagName.equals(that.tagName) : that.tagName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tagId;
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "tagType", nullable = true, length = 32)
    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @Basic
    @Column(name = "tagContent", nullable = true, length = 100)
    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
}
