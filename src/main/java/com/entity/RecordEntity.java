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
    private String openId;

    public RecordEntity() {
    }

    public RecordEntity(String content, String openId) {
        this.content = content;
        this.openId = openId;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
