package com.yunda.sb.inspect.record.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: InspectRecord实体类，数据表：设备巡检记录查询封装实体
 * <li>创建人：何涛
 * <li>创建日期：2016年8月18
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public class InspectRecordBean implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 巡检设备idx主键 */
	@Column(name = "PLAN_EQUIPMENT_IDX")
	private String planEquipmentIdx;

	/** 检修类型（1：机械、2：电气、3：其它） */
	@Column(name = "REPAIR_TYPE")
	private Integer repairType;

	/** 巡检结果（已巡检、未巡检） */
	@Column(name = "EMP_CHECK_RESULT")
	private String empCheckResult;

	/** 巡检情况描述 */
	@Column(name = "CHECK_RESULT_DESC")
	private String checkResultDesc;

	/** 设备类别编码 */
	@Column(name = "CLASS_CODE")
	private String classCode;

	/** 设备类别名称 */
	@Column(name = "CLASS_NAME")
	private String className;

	/** 检查项目 */
	@Column(name = "CHECK_ITEM")
	private String checkItem;

	/** 检查项目首拼（用于根据首字母进行快速检索） */
	@Column(name = "CHECK_ITEM_PY")
	private String checkItemPY;

	/** 检查标准 */
	@Column(name = "CHECK_STANDARD")
	private String checkStandard;

	/** 顺序号 */
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	/** 备注 */
	private String remarks;

	/** 检查结果（合格，不合格） */
	@Column(name = "CHECK_RESULT")
	private String checkResult;

	/** 巡检人 */
	@Column(name = "INSPECT_WORKER")
	private String inspectWorker;

	/** 巡检人ID */
	@Column(name = "INSPECT_WORKER_ID")
	private Long inspectWorkerId;

	/** 巡检人，人员id，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "INSPECT_EMPID")
	private String inspectEmpid;

	/** 巡检人名称，人员名称，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "INSPECT_EMP")
	private String inspectEmp;

	/** 委托巡检人，人员id，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "ENTRUST_INSPECT_EMPID")
	private String entrustInspectEmpid;

	/** 委托巡检人名称，人员名称，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "ENTRUST_INSPECT_EMP")
	private String entrustInspectEmp;

	/** 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/** 巡检时间 */
	@Column(name = "CHECK_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkTime;

	/** 附件（照片）数量 */
	@Column(name = "IMAGE_COUNT")
	private Integer imageCount;

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getEmpCheckResult() {
		return empCheckResult;
	}

	public void setEmpCheckResult(String empCheckResult) {
		this.empCheckResult = empCheckResult;
	}

	public String getCheckResultDesc() {
		return checkResultDesc;
	}

	public void setCheckResultDesc(String checkResultDesc) {
		this.checkResultDesc = checkResultDesc;
	}

	public String getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}

	public String getCheckItemPY() {
		return checkItemPY;
	}

	public void setCheckItemPY(String checkItemPY) {
		this.checkItemPY = checkItemPY;
	}

	public String getCheckStandard() {
		return checkStandard;
	}

	public void setCheckStandard(String checkStandard) {
		this.checkStandard = checkStandard;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getPlanEquipmentIdx() {
		return planEquipmentIdx;
	}

	public void setPlanEquipmentIdx(String planEquipmentIdx) {
		this.planEquipmentIdx = planEquipmentIdx;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getInspectWorker() {
		return inspectWorker;
	}

	public void setInspectWorker(String inspectWorker) {
		this.inspectWorker = inspectWorker;
	}

	public Long getInspectWorkerId() {
		return inspectWorkerId;
	}

	public void setInspectWorkerId(Long inspectWorkerId) {
		this.inspectWorkerId = inspectWorkerId;
	}

	public String getInspectEmpid() {
		return inspectEmpid;
	}

	public void setInspectEmpid(String inspectEmpid) {
		this.inspectEmpid = inspectEmpid;
	}

	public String getInspectEmp() {
		return inspectEmp;
	}

	public void setInspectEmp(String inspectEmp) {
		this.inspectEmp = inspectEmp;
	}

	public String getEntrustInspectEmpid() {
		return entrustInspectEmpid;
	}

	public void setEntrustInspectEmpid(String entrustInspectEmpid) {
		this.entrustInspectEmpid = entrustInspectEmpid;
	}

	public String getEntrustInspectEmp() {
		return entrustInspectEmp;
	}

	public void setEntrustInspectEmp(String entrustInspectEmp) {
		this.entrustInspectEmp = entrustInspectEmp;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

	/**
	 * <li>说明：设置检修类型
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 设备巡检记录
	 */
	public InspectRecordBean repairType(Integer repairType) {
		this.repairType = repairType;
		return this;
	}

	/**
	 * <li>说明：设置周期巡检设备idx主键 
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 周期巡检设备idx主键 
	 * @return 设备巡检记录
	 */
	public InspectRecordBean planEquipmentIdx(String planEquipmentIdx) {
		this.planEquipmentIdx = planEquipmentIdx;
		return this;
	}

	/**
	 * <li>说明：设置设备类别编码
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @return 设备巡检记录
	 */
	public InspectRecordBean classCode(String classCode) {
		this.classCode = classCode;
		return this;
	}

	/**
	 * <li>说明：设置设备类别名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param className 设备类别名称
	 * @return 设备巡检记录
	 */
	public InspectRecordBean className(String className) {
		this.className = className;
		return this;
	}

	/**
	 * <li>说明：设置检查项目
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param checkItem 检查项目
	 * @return 设备巡检记录
	 */
	public InspectRecordBean checkItem(String checkItem) {
		this.checkItem = checkItem;
		return this;
	}

	/**
	 * <li>说明：设置检查项目首拼（用于根据首字母进行快速检索）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param checkItemPY 检查项目首拼
	 * @return 设备巡检记录
	 */
	public InspectRecordBean checkItemPY(String checkItemPY) {
		this.checkItemPY = checkItemPY;
		return this;
	}

	/**
	 * <li>说明：设置检查标准
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param checkStandard 检查标准
	 * @return 设备巡检记录
	 */
	public InspectRecordBean checkStandard(String checkStandard) {
		this.checkStandard = checkStandard;
		return this;
	}

	/**
	 * <li>说明：设置顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param seqNo 顺序号
	 * @return 设备巡检记录
	 */
	public InspectRecordBean seqNo(Integer seqNo) {
		this.seqNo = seqNo;
		return this;
	}

	/**
	 * <li>说明：设置备注
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param remarks 备注
	 * @return 设备巡检记录
	 */
	public InspectRecordBean remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

}
