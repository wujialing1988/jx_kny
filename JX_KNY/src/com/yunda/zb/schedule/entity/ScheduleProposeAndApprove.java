package com.yunda.zb.schedule.entity;

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
 * <li>说明：调度命令单 申请-审批 简单流程
 * <li>创建人：黄杨
 * <li>创建日期：2017-6-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "SCHEDULE_PROPOSE_AND_APPROVE")
public class ScheduleProposeAndApprove implements Serializable {

	/**
	 * 默认版本号
	 */
	private static final long serialVersionUID = 1L;

	public static final String STATUS_XJ = "新建";

	public static final String STATUS_DSP = "待审批";

	public static final String STATUS_YSP = "已审批";

	@Id
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 主题 */
	@Column(name = "TOPIC")
	private String topic;

	/** 命令号 */
	@Column(name = "ORDER_NUM")
	private String orderNum;

	/** 内容 */
	@Column(name = "CONTENT")
	private String content;

	/** 申请人ID */
	@Column(name = "PROPOSER_ID")
	private String proposerId;

	/** 申请人姓名 */
	@Column(name = "PROPOSER_NAME")
	private String proposerName;

	/** 审批人ID */
	@Column(name = "APPROVER_ID")
	private String approverId;

	/** 审批人姓名 */
	@Column(name = "APPROVER_NAME")
	private String approverName;

	/** 申请时间 */
	@Column(name = "PROPOSE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date proposeTime;

	/** 审批时间 */
	@Column(name = "APPROVE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveTime;

	/** 审批意见 */
	@Column(name = "APPROVAL_OPINION")
	private String approvalOpinion;

	/** 命令单状态 */
	@Column(name = "STATUS")
	private String status;

	public String getApprovalOpinion() {
		return approvalOpinion;
	}

	public void setApprovalOpinion(String approvalOpinion) {
		this.approvalOpinion = approvalOpinion;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getProposerId() {
		return proposerId;
	}

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public Date getProposeTime() {
		return proposeTime;
	}

	public void setProposeTime(Date proposeTime) {
		this.proposeTime = proposeTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
