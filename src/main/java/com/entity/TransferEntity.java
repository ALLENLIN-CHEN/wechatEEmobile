package com.entity;

import javax.persistence.*;

/**
*Created by zengqin on 2017/4/7.
*/
@Entity
@Table(name = "transfer", catalog = "projectdatabase")
public class TransferEntity implements java.io.Serializable{
    private int transferId;
    private Schedule schedule;
    private  User user;
   // private Set<TransferMemberEntity> transferMembers=new HashSet<>(0);

    public TransferEntity() {
    }

    public TransferEntity( Schedule schedule, User user) {
        this.schedule = schedule;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transferId")
    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheduleId")
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "openId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    /**
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transfer")
    public Set<TransferMemberEntity> getTransferMembers() {
        return transferMembers;
    }

    public void setTransferMembers(Set<TransferMemberEntity> transferMembers) {
        this.transferMembers = transferMembers;
    }
    */

}

