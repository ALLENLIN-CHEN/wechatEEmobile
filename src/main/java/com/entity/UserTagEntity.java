package com.entity;

import javax.persistence.*;

/**
 * Created by congzihan on 17/7/25.
 */
@Entity
@Table(name = "userTag", schema = "projectdatabase", catalog = "")
public class UserTagEntity {
    private String openid;
    private String tag;

    @Id
    @Column(name = "openid")
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Basic
    @Column(name = "tag")
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTagEntity that = (UserTagEntity) o;

        if (openid != null ? !openid.equals(that.openid) : that.openid != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = openid != null ? openid.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
