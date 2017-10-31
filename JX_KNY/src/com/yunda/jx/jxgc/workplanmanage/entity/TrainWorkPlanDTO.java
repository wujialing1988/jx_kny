package com.yunda.jx.jxgc.workplanmanage.entity;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修生产日报查询字段
 * <li>创建人：林欢
 * <li>创建日期：2016-6-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
public class TrainWorkPlanDTO {
    
    private String trainTypeAndNo;// 车型车号
    
    private String repairClassNameAndTime;// 修程修次
    
    private String deserveName;// 配属段名称
    
    private String delegateDName;// 委修段名称
    
    private String inTime;// 入段时间
    
    private String beginTime;// 检修开始时间
    
    private String nodeName;// 今日计划
    
    private String workStationName;// 当前工位
    
    private String tomorrowNodeName;// 明日计划
    
    private String planStateTime;// 计划停时
    
    private String realStateTime;// 实际停时
    
    private String idx;// 机车作业计划主键idx
    
    private String trainTypeShortName;// 车型拼音码
    
    private String trainNo;// 车号
    
    private String repairClassName;// 修程名称
    
    private String repairtimeName;// 修次名称
    
    private String planBeginTime;// 计划开始时间
    
    private String planEndTime;// 计划完成时间
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getPlanBeginTime() {
        return planBeginTime;
    }
    
    public void setPlanBeginTime(String planBeginTime) {
        this.planBeginTime = planBeginTime;
    }
    
    public String getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
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
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    public String getDelegateDName() {
        return delegateDName;
    }
    
    public void setDelegateDName(String delegateDName) {
        this.delegateDName = delegateDName;
    }
    
    public String getRepairClassNameAndTime() {
        return repairClassNameAndTime;
    }
    
    public void setRepairClassNameAndTime(String repairClassNameAndTime) {
        this.repairClassNameAndTime = repairClassNameAndTime;
    }
    
    public String getTomorrowNodeName() {
        return tomorrowNodeName;
    }
    
    public void setTomorrowNodeName(String tomorrowNodeName) {
        this.tomorrowNodeName = tomorrowNodeName;
    }
    
    public String getBeginTime() {
        return beginTime;
    }
    
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
    
    public String getDeserveName() {
        return deserveName;
    }
    
    public void setDeserveName(String deserveName) {
        this.deserveName = deserveName;
    }
    
    public String getInTime() {
        return inTime;
    }
    
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    public String getPlanStateTime() {
        return planStateTime;
    }
    
    public void setPlanStateTime(String planStateTime) {
        this.planStateTime = planStateTime;
    }
    
    public String getRealStateTime() {
        return realStateTime;
    }
    
    public void setRealStateTime(String realStateTime) {
        this.realStateTime = realStateTime;
    }
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }
    
    public String getWorkStationName() {
        return workStationName;
    }
    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
}
