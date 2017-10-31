package com.yunda.sb.repair.process.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: RepairWorkOrderBean，设备检修作业工单查询封装实体
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public class RepairWorkOrderBean implements java.io.Serializable {
	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 范围实例主键 */
	@Column(name = "scope_case_idx")
	private String scopeCaseIdx;

	/** 作业项定义主键 */
	@Column(name = "define_idx")
	private String defineIdx;

	/** 序号 */
	@Column(name = "sort_no")
	private Integer sortNo;

	/** 作业内容 */
	@Column(name = "work_content")
	private String workContent;

	/** 工艺标准 */
	@Column(name = "process_standard")
	private String processStandard;

	/** 施修人 */
	@Column(name = "worker_id")
	private Long workerId;

	/** 施修人名称 */
	@Column(name = "worker_name")
	private String workerName;

	/** 其他作业人员名称 */
	@Column(name = "other_worker_name")
	private String otherWorkerName;

	/** 处理时间 */
	@Column(name = "process_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processTime;

	/** 实修记录 */
	@Column(name = "repair_record")
	private String repairRecord;

	/** 备注 */
	private String remark;

	/** 工单状态，默认为未处理（代码：1） */
	@Column(name = "order_status")
	private Integer orderStatus;

	/** 附件（照片）数量 */
	@Column(name = "IMAGE_COUNT")
	private Integer imageCount;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getScopeCaseIdx() {
		return scopeCaseIdx;
	}

	public void setScopeCaseIdx(String scopeCaseIdx) {
		this.scopeCaseIdx = scopeCaseIdx;
	}

	public String getDefineIdx() {
		return defineIdx;
	}

	public void setDefineIdx(String defineIdx) {
		this.defineIdx = defineIdx;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public String getProcessStandard() {
		return processStandard;
	}

	public void setProcessStandard(String processStandard) {
		this.processStandard = processStandard;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getOtherWorkerName() {
		return otherWorkerName;
	}

	public void setOtherWorkerName(String otherWorkerName) {
		this.otherWorkerName = otherWorkerName;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public String getRepairRecord() {
		return repairRecord;
	}

	public void setRepairRecord(String repairRecord) {
		this.repairRecord = repairRecord;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

}
