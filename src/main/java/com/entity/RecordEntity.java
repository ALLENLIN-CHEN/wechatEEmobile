package com.entity;

import javax.persistence.*;

/**
 * Created by zengqin on 2017/4/7.
*/
@Entity
@Table(name = "record", catalog = "projectdatabase")
public class RecordEntity implements java.io.Serializable{
    private int recordId;
    private String content;
    private User user;

    public RecordEntity() {
    }

    public RecordEntity(String content, User user) {
        this.content = content;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordId")
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "openId")

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
