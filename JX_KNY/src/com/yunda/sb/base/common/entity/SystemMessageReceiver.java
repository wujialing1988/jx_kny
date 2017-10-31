package com.yunda.sb.base.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：T_SYSTEM_MESSAGE_RECEIVER，数据表：系统消息接收者
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "T_SYSTEM_MESSAGE_RECEIVER")
public class SystemMessageReceiver implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 接收状态 - 忽略 */
	public static final int RECEIVE_STATE_HL = 0;

	/** 接收状态 - 确认（查看） */
	public static final int RECEIVE_STATE_QR = 1;

	/** 接收状态 - 未接受 */
	public static final int RECEIVE_STATE_NO_RECEIVE = -1;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 系统消息idx主键 */
	@Column(name = "SYSTEM_MESSAGE_IDX")
	private String systemMessageIdx;

	/** 接收人 */
	@Column(name = "EMP_NAME")
	private String empName;

	/** 接收人id */
	@Column(name = "EMP_ID")
	private Long empId;

	/** 接收班组 */
	@Column(name = "ORG_NAME")
	private String orgName;

	/** 接收班组id */
	@Column(name = "ORG_ID")
	private Long orgId;

	/** 接收状态，0：忽略，1：确认（查看）*/
	@Column(name = "RECEIVE_STATE")
	private Integer receiveState;

	/** 记录状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/** 消息创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/**
	 * <li>说明：设置系统消息idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param systemMessageIdx 系统消息idx主键
	 * @return 系统消息接收者实体对象
	 */
	public SystemMessageReceiver systemMessageIdx(String systemMessageIdx) {
		this.systemMessageIdx = systemMessageIdx;
		return this;
	}

	/**
	 * <li>说明：设置接收人
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param empName 接收人
	 * @return 系统消息接收者实体对象
	 */
	public SystemMessageReceiver empName(String empName) {
		this.empName = empName;
		return this;
	}

	/**
	 * <li>说明：设置接收人id
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param empId 接收人id
	 * @return 系统消息接收者实体对象
	 */
	public SystemMessageReceiver empId(Long empId) {
		this.empId = empId;
		return this;
	}

	/**
	 * <li>说明：设置接收班组
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param orgName 接收班组
	 * @return 系统消息接收者实体对象
	 */
	public SystemMessageReceiver orgName(String orgName) {
		this.orgName = orgName;
		return this;
	}

	/**
	 * <li>说明：设置接收班组id
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param orgId 接收班组id
	 * @return 系统消息接收者实体对象
	 */
	public SystemMessageReceiver orgId(Long orgId) {
		this.orgId = orgId;
		return this;
	}

	/**
	 * <li>说明：设置接收状态
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param receiveState 接收状态，0：忽略，1：确认（查看）
	 * @return 系统消息接收者实体对象
	 */
	public SystemMessageReceiver receiveState(int receiveState) {
		this.receiveState = receiveState;
		return this;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getSystemMessageIdx() {
		return systemMessageIdx;
	}

	public void setSystemMessageIdx(String systemMessageIdx) {
		this.systemMessageIdx = systemMessageIdx;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getReceiveState() {
		return receiveState;
	}

	public void setReceiveState(Integer receiveState) {
		this.receiveState = receiveState;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
