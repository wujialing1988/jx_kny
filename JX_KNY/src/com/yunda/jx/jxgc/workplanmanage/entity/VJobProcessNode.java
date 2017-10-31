package com.yunda.jx.jxgc.workplanmanage.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 用户封装用于界面vis显示的JobProcessNode实体类
 * <li>创建人：何涛
 * <li>创建日期：2015-5-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public final class VJobProcessNode {
    
    /** idx主键 */
    private String idx;
    
    /** 车型简称 */
    private String trainTypeShortName;
    
    /** 车号 */
    private String trainNo;
    
    /** 修程名称 */
    private String repairClassName;
    
    /** 修次名称 */
    private String repairtimeName;
    
    /** 机车检修作业计划(TrainWorkPlan)开始时间 */
    private Date beginTime;
    
    /** 机车检修作业计划(TrainWorkPlan)结束时间 */
    private Date endTime;
    
    /** 节点开始时间 */
    private Date planBeginTime;
    
    /** 节点结束时间 */
    private Date planEndTime;
    
    /** 工位主键 */
    private String workStationIDX;
    
    /** 工作计划主键 */
    private String workPlanIDX;
    
    /** 车型简称 */
    private String status;
    
    /** 延搁时间 */
    private Double delayTime;
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2015-5-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jNode JobProcessNode实体类, 数据表：机车检修作业计划-流程节点
     * @param trainWorkPlan TrainWorkPlan实体类, 数据表：机车检修作业计划
     */
    public void adaptFrom(JobProcessNode jNode, TrainWorkPlan trainWorkPlan) {
        idx = jNode.getIdx();
        trainTypeShortName = jNode.getTrainTypeShortName();
        trainNo = jNode.getTrainNo();
        planBeginTime = jNode.getPlanBeginTime();
        planEndTime = jNode.getPlanEndTime();
        workStationIDX = jNode.getWorkStationIDX();
        workPlanIDX = jNode.getWorkPlanIDX();
        status = jNode.getStatus();
        if (null != trainWorkPlan) {
            repairClassName = trainWorkPlan.getRepairClassName();
            repairtimeName = trainWorkPlan.getRepairtimeName();
            beginTime = trainWorkPlan.getPlanBeginTime();
            endTime = trainWorkPlan.getPlanEndTime();
        }
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Date getPlanBeginTime() {
        return planBeginTime;
    }
    
    public void setPlanBeginTime(Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }
    
    public Date getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    public Date getBeginTime() {
        return beginTime;
    }
    
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getDelayTime() {
        return delayTime;
    }
    
    public void setDelayTime(Double delayTime) {
        this.delayTime = delayTime;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    public String getRepairtimeName() {
        return repairtimeName;
    }
    
    public void setRepairtimeName(String repairtimeName) {
        this.repairtimeName = repairtimeName;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }
    
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }
    
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    /**
     * <li>说明：获取一个机车检修作业流程节点的延期时间
     * <li>创建人：何涛
     * <li>创建日期：2015-5-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param currentTime 当前时间
     * @return 延期时间(单位分钟)
     */
    public long getDelayTime(Date currentTime) {
        // 未开工的不计算延期时间
        long delayTime = -1;
        if (JobProcessNode.STATUS_UNSTART.equals(status)) {
            return delayTime;
        }
        // 未完工的按计划结束时间和当前时间做对比
        if (null != planEndTime && currentTime.after(planEndTime) && !JobProcessNode.STATUS_STOP.equals(status)) {
            delayTime = currentTime.getTime() - planEndTime.getTime();
        }
        return delayTime <= 0 ? delayTime : BigDecimal.valueOf(delayTime).divide(BigDecimal.valueOf(1000 * 60), 0, BigDecimal.ROUND_HALF_UP)
            .longValue();
    }
    
    /**
     * <li>说明：获取一个机车检修作业流程节点的延期时间
     * <li>创建人：何涛
     * <li>创建日期：2015-5-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return String
     */
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("时间范围:[").append(df.format(planBeginTime)).append("]");
        sb.append(" - ");
        sb.append("[").append(df.format(planEndTime)).append("]");
        return sb.toString();
    }
}
