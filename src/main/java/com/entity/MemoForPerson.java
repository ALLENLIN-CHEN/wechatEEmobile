package com.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rthtr on 2017/4/15.
 */
@Entity
@Table(name = "memoForPerson", catalog = "projectdatabase")
public class MemoForPerson {
    private int id;
    private String content;
    private Date operTime;
    private Integer hasRead;
    private User openId;
    private User operatorOpenId;
    private Subproject subprojectId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "operTime")
    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    @Basic
    @Column(name = "hasRead")
    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemoForPerson that = (MemoForPerson) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (operTime != null ? !operTime.equals(that.operTime) : that.operTime != null) return false;
        if (hasRead != null ? !hasRead.equals(that.hasRead) : that.hasRead != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (operTime != null ? operTime.hashCode() : 0);
        result = 31 * result + (hasRead != null ? hasRead.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "openId", referencedColumnName = "openId")
    public User getOpenId() {
        return openId;
    }

    public void setOpenId(User openId) {
        this.openId = openId;
    }

    @OneToOne
    @JoinColumn(name = "operatorOpenId", referencedColumnName = "openId")
    public User getOperatorOpenId() {
        return operatorOpenId;
    }

    public void setOperatorOpenId(User operatorOpenId) {
        this.operatorOpenId = operatorOpenId;
    }

    @OneToOne
    @JoinColumn(name = "subprojectId", referencedColumnName = "subprojectId")
    public Subproject getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(Subproject subprojectId) {
        this.subprojectId = subprojectId;
    }
}
