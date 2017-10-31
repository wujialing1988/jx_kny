package com.yunda.frame.yhgl.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：UserData，数据表：用户基本信息查询封装实体
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
public class UserData implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	@Id
	/** 人员id */
	private Long empid;

	/** 人员姓名 */
	private String empname;

	/** 用户id */
	private String userid;

	/** 操作员id */
	private Long operatorid;

	/** 操作员名称 */
	private String operatorname;

	/** 所在班组id */
	private Long orgid;

	/** 所在班组名称 */
	private String orgname;

	/** 所在班组上级班组id */
	private Long parentorgid;

	/** 所在班组结构序列
	 * @see com.yunda.frame.yhgl.entity.OmOrganization#orgdegree
	 *  */
	private String orgseq;

	/** 所在班组结构等级 */
	private Long orglevel;

	/** 所在班组结构 级别*/
	private String orgdegree;

	public Long getEmpid() {
		return empid;
	}

	public void setEmpid(Long empid) {
		this.empid = empid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Long getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(Long operatorid) {
		this.operatorid = operatorid;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public Long getOrgid() {
		return orgid;
	}

	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public Long getParentorgid() {
		return parentorgid;
	}

	public void setParentorgid(Long parentorgid) {
		this.parentorgid = parentorgid;
	}

	public String getOrgseq() {
		return orgseq;
	}

	public void setOrgseq(String orgseq) {
		this.orgseq = orgseq;
	}

	public Long getOrglevel() {
		return orglevel;
	}

	public void setOrglevel(Long orglevel) {
		this.orglevel = orglevel;
	}

	public String getOrgdegree() {
		return orgdegree;
	}

	public void setOrgdegree(String orgdegree) {
		this.orgdegree = orgdegree;
	}

}