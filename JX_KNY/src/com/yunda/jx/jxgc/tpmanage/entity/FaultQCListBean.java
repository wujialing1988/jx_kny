package com.yunda.jx.jxgc.tpmanage.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质检列表bean
 * <li>创建人：程锐
 * <li>创建日期：2015-7-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class FaultQCListBean implements java.io.Serializable {
    
    private String idx; // Idx
    
    private String checkItemCode; // 质检项编码
    
    private String checkItemName; // 质检项名称
    
    private String trainTypeShortName; // 车型
    
    private String trainNo; // 车号
    
    private String ticketCode; // 提票单编码
    
    private String ticketTime; // 提票时间
    
    private String faultName; // 故障现象
    
    private String faultDesc; // 故障描述
    
    private String fixPlaceFullName; // 故障位置
    
    private String type; // 提票类型
    
    private String ticketEmp; // 提票人
    
    private String tpIDX; // 提票单主键
    
    private String workPlanIDX; // 作业计划主键
    
    /* 故障发生日期 */
    private String faultOccurDate;
    
    /* 处理方法名称 */
    private String methodName;
    
    /* 处理方法描述 */
    private String methodDesc;
    
    /* 处理结果 */
    private String repairResult;
    
    /* 修竣时间 */
    private String completeTime;
    
    /* 销票人 */
    private String completeEmp;
    
    public String getCheckItemCode() {
        return checkItemCode;
    }
    
    public void setCheckItemCode(String checkItemCode) {
        this.checkItemCode = checkItemCode;
    }
    
    public String getCheckItemName() {
        return checkItemName;
    }
    
    public void setCheckItemName(String checkItemName) {
        this.checkItemName = checkItemName;
    }
    
    public String getFaultDesc() {
        return faultDesc;
    }
    
    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }
    
    public String getFaultName() {
        return faultName;
    }
    
    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }
    
    public String getFixPlaceFullName() {
        return fixPlaceFullName;
    }
    
    public void setFixPlaceFullName(String fixPlaceFullName) {
        this.fixPlaceFullName = fixPlaceFullName;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getTicketCode() {
        return ticketCode;
    }
    
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
    
    public String getTicketEmp() {
        return ticketEmp;
    }
    
    public void setTicketEmp(String ticketEmp) {
        this.ticketEmp = ticketEmp;
    }
    
    public String getTpIDX() {
        return tpIDX;
    }
    
    public void setTpIDX(String tpIDX) {
        this.tpIDX = tpIDX;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }
    
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }
    
    public String getCompleteEmp() {
        return completeEmp;
    }
    
    public void setCompleteEmp(String completeEmp) {
        this.completeEmp = completeEmp;
    }
    
    public String getCompleteTime() {
        return completeTime;
    }
    
    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }
    
    public String getFaultOccurDate() {
        return faultOccurDate;
    }
    
    public void setFaultOccurDate(String faultOccurDate) {
        this.faultOccurDate = faultOccurDate;
    }
    
    public String getTicketTime() {
        return ticketTime;
    }
    
    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }
    
    public String getMethodDesc() {
        return methodDesc;
    }
    
    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getRepairResult() {
        return repairResult;
    }
    
    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }
    
}
