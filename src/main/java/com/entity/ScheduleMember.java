package com.entity;

// Generated 2016-4-16 15:43:05 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ScheduleMember generated by hbm2java
 */
@Entity
@Table(name = "scheduleMember", catalog = "projectdatabase")
public class ScheduleMember implements java.io.Serializable
{

	private Integer scheduleMemberId;
	private Schedule schedule;
	private User user;

	public ScheduleMember()
	{
	}

	public ScheduleMember(Schedule schedule, User user)
	{
		this.schedule = schedule;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scheduleMemberId", unique = true, nullable = false)
	public Integer getScheduleMemberId()
	{
		return this.scheduleMemberId;
	}

	public void setScheduleMemberId(Integer scheduleMemberId)
	{
		this.scheduleMemberId = scheduleMemberId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scheduleId")
	public Schedule getSchedule()
	{
		return this.schedule;
	}

	public void setSchedule(Schedule schedule)
	{
		this.schedule = schedule;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "openId")
	public User getUser()
	{
		return this.user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}
