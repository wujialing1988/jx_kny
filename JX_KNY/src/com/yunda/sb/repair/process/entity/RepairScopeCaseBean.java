package com.yunda.sb.repair.process.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScopeCase查询封装实体
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月4日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public class RepairScopeCaseBean implements java.io.Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	private String idx;

	/** 范围定义主键 */
	@Column(name = "scope_define_idx")
	private String scopeDefineIdx;

	/** 检修任务单主键 */
	@Column(name = "task_list_idx")
	private String taskListIdx;

	/** 序号 */
	@Column(name = "sort_no")
	private Integer sortNo;

	/** 检修类型，1：机型、2：电气、3：其它 */
	@Column(name = "repair_type")
	private Integer repairType;

	/** 检修范围名称 */
	@Column(name = "repair_item_name")
	private String repairItemName;

	/** 备注 */
	private String remark;

	/** 处理状态：(未处理、已处理)，默认为未处理 */
	private String state;

	/* 未处理作业工单数 */
	@Column(name = "WCL_COUNT")
	private Integer wclCount;

	/* 已处理作业工单数 */
	@Column(name = "YCL_COUNT")
	private Integer yclCount;

	/**
	 * <li>说明：设置范围定义主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeDefineIdx 范围定义主键
	 * @return 检修范围实例
	 */
	public RepairScopeCaseBean scopeDefineIdx(String scopeDefineIdx) {
		this.scopeDefineIdx = scopeDefineIdx;
		return this;
	}

	/**
	 * <li>说明：设置检修任务单主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单主键
	 * @return 检修范围实例
	 */
	public RepairScopeCaseBean taskListIdx(String taskListIdx) {
		this.taskListIdx = taskListIdx;
		return this;
	}

	/**
	 * <li>说明：设置序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param sortNo 序号
	 * @return 检修范围实例
	 */
	public RepairScopeCaseBean seqNo(Integer sortNo) {
		this.sortNo = sortNo;
		return this;
	}

	/**
	 * <li>说明：设置检修类型，1：机型、2：电气、3：其它
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairType 检修类型，1：机型、2：电气、3：其它
	 * @return 检修范围实例
	 */
	public RepairScopeCaseBean repairType(Integer repairType) {
		this.repairType = repairType;
		return this;
	}

	/**
	 * <li>说明：设置检修范围名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairItemName 检修范围名称
	 * @return 检修范围实例
	 */
	public RepairScopeCaseBean repairScopeName(String repairItemName) {
		this.repairItemName = repairItemName;
		return this;
	}

	/**
	 * <li>说明：设置备注
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param remark 备注
	 * @return 检修范围实例
	 */
	public RepairScopeCaseBean remark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getScopeDefineIdx() {
		return scopeDefineIdx;
	}

	public void setScopeDefineIdx(String scopeDefineIdx) {
		this.scopeDefineIdx = scopeDefineIdx;
	}

	public String getTaskListIdx() {
		return taskListIdx;
	}

	public void setTaskListIdx(String taskListIdx) {
		this.taskListIdx = taskListIdx;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getRepairItemName() {
		return repairItemName;
	}

	public void setRepairItemName(String repairItemName) {
		this.repairItemName = repairItemName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getWclCount() {
		return wclCount;
	}

	public void setWclCount(Integer wclCount) {
		this.wclCount = wclCount;
	}

	public Integer getYclCount() {
		return yclCount;
	}

	public void setYclCount(Integer yclCount) {
		this.yclCount = yclCount;
	}

}
