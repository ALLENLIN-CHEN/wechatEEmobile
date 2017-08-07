package com.entity;

// Generated 2016-4-16 15:43:05 by Hibernate Tools 3.4.0.CR1

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.stream.events.Characters;

/**
 * ProjectMember generated by hbm2java
 */
@Entity
@Table(name = "projectMember", catalog = "projectdatabase")
public class ProjectMember implements java.io.Serializable
{

	private Integer projectMemberId;
	private Subproject subproject;
	private User user;
	private Character roleType;
	private Character canModify;

	public ProjectMember()
	{
	}

	public ProjectMember(Subproject subproject, User user, Character roleType)
	{
		this.subproject = subproject;
		this.user = user;
		this.roleType = roleType;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "projectMemberId", unique = true, nullable = false)
	public Integer getProjectMemberId()
	{
		return this.projectMemberId;
	}

	public void setProjectMemberId(Integer projectMemberId)
	{
		this.projectMemberId = projectMemberId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subprojectId")
	public Subproject getSubproject()
	{
		return this.subproject;
	}

	public void setSubproject(Subproject subproject)
	{
		this.subproject = subproject;
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

	@Column(name = "role_type", length = 1)
	public Character getRoleType()
	{
		return this.roleType;
	}
	public void setRoleType(Character roleType)
	{
		this.roleType = roleType;
	}

	@Column(name="can_modify", length = 1)
	public Character getCanModify(){
		if(this.canModify==null&&this.getRoleType()!='a'){
			this.canModify = '0';
		}
		if(this.canModify==null&&this.getRoleType()=='a'){
			this.canModify = '1';
		}
		return this.canModify;
	}
	public void setCanModify(Character canModify){this.canModify = canModify;}


}