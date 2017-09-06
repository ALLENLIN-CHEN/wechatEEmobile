package com.entity;

// Generated 2016-4-16 15:43:05 by Hibernate Tools 3.4.0.CR1

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Subproject generated by hbm2java
 */
@Entity
@Table(name = "subproject", catalog = "projectdatabase")
public class Subproject implements Serializable
{

	private Integer subprojectId;
	private Project project;
	private Character projectStatus;
	private String subproject;
	private String teamStatus;//String 子项目标签(子项目当前阶段)
	private String contractStatus;
	private String paymentStatus;
	private String allocationStatus;
	@JsonIgnore
	private Set<ProjectMember> projectMembers = new HashSet<ProjectMember>(0);
	@JsonIgnore
	private Set<Schedule> schedules = new HashSet<Schedule>(0);

	public Subproject()
	{
	}

	public Subproject(Project project, Character projectStatus,
			String subproject, String teamStatus, String contractStatus,
			String paymentStatus, String allocationStatus,
			Set<ProjectMember> projectMembers, Set<Schedule> schedules)
	{
		this.project = project;
		this.projectStatus = projectStatus;
		this.subproject = subproject;
		this.teamStatus = teamStatus;
		this.contractStatus = contractStatus;
		this.paymentStatus = paymentStatus;
		this.allocationStatus = allocationStatus;
		this.projectMembers = projectMembers;
		this.schedules = schedules;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "subprojectId", unique = true, nullable = false)
	public Integer getSubprojectId()
	{
		return this.subprojectId;
	}

	public void setSubprojectId(Integer subprojectId)
	{
		this.subprojectId = subprojectId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projectId")
	public Project getProject()
	{
		return this.project;
	}

	public void setProject(Project project)
	{
		this.project = project;
	}

	@Column(name = "project_status", length = 1)
	public Character getProjectStatus()
	{
		return this.projectStatus;
	}

	public void setProjectStatus(Character projectStatus)
	{
		this.projectStatus = projectStatus;
	}

	@Column(name = "subproject", length = 200)
	public String getSubproject()
	{
		return this.subproject;
	}

	public void setSubproject(String subproject)
	{
		this.subproject = subproject;
	}

	@Column(name = "team_status", length = 200)
	public String getTeamStatus()
	{
		return this.teamStatus;
	}

	public void setTeamStatus(String teamStatus)
	{
		this.teamStatus = teamStatus;
	}

	@Column(name = "contract_status", length = 200)
	public String getContractStatus()
	{
		return this.contractStatus;
	}

	public void setContractStatus(String contractStatus)
	{
		this.contractStatus = contractStatus;
	}

	@Column(name = "payment_status", length = 200)
	public String getPaymentStatus()
	{
		return this.paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus)
	{
		this.paymentStatus = paymentStatus;
	}

	@Column(name = "allocation_status", length = 200)
	public String getAllocationStatus()
	{
		return this.allocationStatus;
	}

	public void setAllocationStatus(String allocationStatus)
	{
		this.allocationStatus = allocationStatus;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subproject")
	public Set<ProjectMember> getProjectMembers()
	{
		return this.projectMembers;
	}

	public void setProjectMembers(Set<ProjectMember> projectMembers)
	{
		this.projectMembers = projectMembers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subproject")
	public Set<Schedule> getSchedules()
	{
		return this.schedules;
	}

	public void setSchedules(Set<Schedule> schedules)
	{
		this.schedules = schedules;
	}

}
