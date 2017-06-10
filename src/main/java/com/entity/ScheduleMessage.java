package com.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by rthtr on 2017/4/4.
 */
@Entity
@Table(
        name = "scheduleMessage", catalog = "projectdatabase",
        uniqueConstraints= @UniqueConstraint(columnNames = {"createTime", "content" ,"openId","scheduleId"})
)
public class ScheduleMessage {
    private int id;
    private Date createTime;
    private String content;
    private User openId;
    private Schedule scheduleId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleMessage that = (ScheduleMessage) o;

        if (id != that.id) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "openId", referencedColumnName = "openId")
    public User getOpenId() {
        return openId;
    }

    public void setOpenId(User openId) {
        this.openId = openId;
    }

    @ManyToOne
    @JoinColumn(name = "scheduleId", referencedColumnName = "scheduleId")
    public Schedule getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Schedule scheduleId) {
        this.scheduleId = scheduleId;
    }
}
