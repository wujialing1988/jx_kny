package com.yunda.jx.jxgc.producttaskmanage.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业工单处理实体
 * <li>创建人：程锐
 * <li>创建日期：2015-9-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
public class WorkCardHandle implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    private String idx;
    
    private java.util.Date realBeginTime;
    
    private java.util.Date realEndTime;
    
    private String remarks;
    
    private String workerID;
    
//    private String partsNo;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
//    public String getPartsNo() {
//        return partsNo;
//    }
//    
//    public void setPartsNo(String partsNo) {
//        this.partsNo = partsNo;
//    }
    
    public java.util.Date getRealBeginTime() {
        return realBeginTime;
    }
    
    public void setRealBeginTime(java.util.Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }
    
    public java.util.Date getRealEndTime() {
        return realEndTime;
    }
    
    public void setRealEndTime(java.util.Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getWorkerID() {
        return workerID;
    }
    
    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }
    
}
