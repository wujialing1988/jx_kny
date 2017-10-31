package com.yunda.jx.webservice.stationTerminal.base.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于工序延误实体包装
 * <li>说明: 用于getWorkSeqDelay接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-26
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-2
 * <li>修改内容：增加字段realBeginTimeStr
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WorkSeqDelayBean implements java.io.Serializable {
    
    private String idx; // 主键
    
    private String trainTypeShortName; // 车型
    
    private String trainNo; // 车号
    
    private String repairClassName; // 修程
    
    private String repairTimeName; // 修次
    
    private String planBeginTimeStr; // 计划开始时间
    
    private String planEndTimeStr; // 计划结束时间
    
    private String nodeName; // 节点名称
    
    private String delayTime; // 延误时间
    
    private String realBeginTimeStr; // 实际开工时间
    
    private String tempDelayTime;// 计算的延误时间
    
    public String getDelayTime() {
        return delayTime;
    }
    
    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getPlanBeginTimeStr() {
        return planBeginTimeStr;
    }
    
    public void setPlanBeginTimeStr(String planBeginTimeStr) {
        this.planBeginTimeStr = planBeginTimeStr;
    }
    
    public String getPlanEndTimeStr() {
        return planEndTimeStr;
    }
    
    public void setPlanEndTimeStr(String planEndTimeStr) {
        this.planEndTimeStr = planEndTimeStr;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    public String getRepairTimeName() {
        return repairTimeName;
    }
    
    public void setRepairTimeName(String repairTimeName) {
        this.repairTimeName = repairTimeName;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getRealBeginTimeStr() {
        return realBeginTimeStr;
    }
    
    public void setRealBeginTimeStr(String realBeginTimeStr) {
        this.realBeginTimeStr = realBeginTimeStr;
    }
    
    public String getTempDelayTime() {
        return tempDelayTime;
    }
    
    public void setTempDelayTime(String tempDelayTime) {
        this.tempDelayTime = tempDelayTime;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
}
