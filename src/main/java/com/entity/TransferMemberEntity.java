package com.entity;

import javax.persistence.*;

/**
 * Created by zengqin on 2017/4/7.
*/

@Entity
@Table(name = "transferMember", catalog = "projectdatabase")
public class TransferMemberEntity implements java.io.Serializable{
    private int transferMemberId;
    private User user;
    private TransferEntity transferEntity;

    public TransferMemberEntity() {
    }

    public TransferMemberEntity(User user, TransferEntity transferEntity) {
        this.user = user;
        this.transferEntity = transferEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transferMemberId")
    public int getTransferMemberId() {
        return transferMemberId;
    }

    public void setTransferMemberId(int transferMemberId) {
        this.transferMemberId = transferMemberId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "openId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transferId")
    public TransferEntity getTransferEntity() {
        return transferEntity;
    }

    public void setTransferEntity(TransferEntity transferEntity) {
        this.transferEntity = transferEntity;
    }
}

