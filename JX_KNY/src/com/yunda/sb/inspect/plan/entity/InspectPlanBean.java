package com.yunda.sb.inspect.plan.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: InspectPlan查询实体类，数据表：巡检计划
 * <li>创建人：何涛
 * <li>创建日期：2016年6月16日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public class InspectPlanBean implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	private String idx;

	/** 巡检线路idx主键 */
	@Column(name = "ROUTE_IDX")
	private String routeIdx;

	/** 巡检线路名称 */
	@Column(name = "ROUTE_NAME")
	private String routeName;

	/** 计划编制人 */
	@Column(name = "PARTROL_WORKER")
	private String partrolWorker;

	/** 计划编制人ID */
	@Column(name = "PARTROL_WORKER_ID")
	private Long partrolWorkerId;

	/** 巡检周期（周检，半月检，月检，季检，临时） */
	@Column(name = "PERIOD_TYPE")
	private String periodType;

	/** 计划开始日期 */
	@Column(name = "PLAN_START_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planStartDate;

	/** 计划结束日期 */
	@Column(name = "PLAN_END_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planEndDate;

	/** 处理状态：(未处理、已处理) */
	private String state;

	/** 未巡检完成的设备数 */
	@Column(name = "WXJ_COUNT")
	private Integer wxjCount;

	/** 已巡检完成的设备数 */
	@Column(name = "YXJ_COUNT")
	private Integer yxjCount;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getRouteIdx() {
		return routeIdx;
	}

	public void setRouteIdx(String routeIdx) {
		this.routeIdx = routeIdx;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getPartrolWorker() {
		return partrolWorker;
	}

	public void setPartrolWorker(String partrolWorker) {
		this.partrolWorker = partrolWorker;
	}

	public Long getPartrolWorkerId() {
		return partrolWorkerId;
	}

	public void setPartrolWorkerId(Long partrolWorkerId) {
		this.partrolWorkerId = partrolWorkerId;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public java.util.Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(java.util.Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public java.util.Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(java.util.Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getWxjCount() {
		return wxjCount;
	}

	public void setWxjCount(Integer wxjCount) {
		this.wxjCount = wxjCount;
	}

	public Integer getYxjCount() {
		return yxjCount;
	}

	public void setYxjCount(Integer yxjCount) {
		this.yxjCount = yxjCount;
	}

	/**
	 * <li>说明：转换设备巡检状态，区分未开始、处理中和已延期的巡检计划，用于web端页面显示
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 设备巡检状态[未开始、处理中、已延期]
	 */
	public String convertState() {
		if (InspectPlan.STATE_YCL.equals(state)) {
			return state;
		}
		Date now = Calendar.getInstance().getTime();
		if (now.before(planStartDate)) {
			return "未开始";
		}
		if (0 < this.getDelayDays()) {
			return "<span style=\"color:red;display:inline;font-weight:bold;\">已延期</span>";
		}
		return "处理中";
	}

	/**
	 * <li>说明：获取该巡检计划的延期天数
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 延期天数
	 */
	private int getDelayDays() {
		if (InspectPlan.STATE_YCL.equals(this.state)) {
			return 0;
		}
		Date now = Calendar.getInstance().getTime();
		Double delayDays = Math.floor((now.getTime() - planEndDate.getTime()) / (1000 * 60 * 60 * 24));
		return delayDays.intValue();
	}

	@Override
	public String toString() {
		// 正常进行的巡检计划已绿色背景显示进度
		String bgColor = "#00FF00";
		if (0 < this.getDelayDays()) {
			// 已延期的巡检计划已红色色背景显示进度
			bgColor = "#FF0000";
		}
		float total = Float.parseFloat((yxjCount + wxjCount) + "");
		float width0 = 213F;
		float width1 = width0;
		if (0 != total) {
			width1 *= Float.parseFloat(this.yxjCount + "") / total;
			width1 = width1 == 0 ? 1 : width1;
		}
		StringBuilder sb = new StringBuilder("<div style = \"display:inline-block;margin:2px 0 0 -15px;background:rgb(239, 239, 239);margin-left:-5px;height:15px;width:" + width0
				+ "px;border-radius:0px;border:1px solid rgb(192, 195, 213);border-left:0;\">");
		sb.append("<div style = \"background:" + bgColor + ";margin-top:1px;height:14px;line-height:14px;border-radius:0px;width:" + width1 + "px;\">");
		sb.append(routeName);
		sb.append("_");
		sb.append(periodType);
		sb.append("_");
		sb.append(partrolWorker);
		if (0 != yxjCount + wxjCount) {
			sb.append("_");
			sb.append(yxjCount + "/" + (yxjCount + wxjCount));
		}
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
}
