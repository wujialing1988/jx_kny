package com.yunda.jx.webservice.stationTerminal.base.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取作业卡信息实体包装
 * <li>说明: 用于workCard接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-24
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-3
 * <li>修改内容：增加字段planTrainTimeStr、transinTimeStr、specificationModel
 * <li>修改人: 林欢
 * <li>修改日期：2016-6-7
 * <li>修改内容：删除字段，注释部分，检修流程改动
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WorkCardBean implements java.io.Serializable {
    
    private String nodeCaseName; // 工艺节点名称
    // private String partsName ; //零部件名称
    // private String partsNo ; //零部件编号
    // private String nameplateNo; //零部件编号
    
    private String safeAnnouncements; // 安全注意事项
    // private String ratedWorkHours ; //工时
    
    private String workSeqClass; // 检修类型
    // private String fixPlaceFullName ; //位置全名
    
    private String remarks; // 描述
    // private String fixPlaceIDX ; //零部件IDX
    
    private String planTrainTimeStr; // 计划交车时间
    
    private String transinTimeStr; // 转入时间
    // private String specificationModel; //规格型号
    
    private String workCardName;// 作业工单名称
    
    private String realBeginTimeStr;// 实际开始时间
    
    private String realEndTimeStr;// 实际结束时间
    
    private String worker;// 作业人员名称
    
    private String workerID;// 作业人员ID
    
    public String getPlanTrainTimeStr() {
        return planTrainTimeStr;
    }
    
    public void setPlanTrainTimeStr(String planTrainTimeStr) {
        this.planTrainTimeStr = planTrainTimeStr;
    }
    
    public String getWorkerID() {
        return workerID;
    }
    
    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }
    
    public String getNodeCaseName() {
        return nodeCaseName;
    }
    
    public void setNodeCaseName(String nodeCaseName) {
        this.nodeCaseName = nodeCaseName;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getSafeAnnouncements() {
        return safeAnnouncements;
    }
    
    public void setSafeAnnouncements(String safeAnnouncements) {
        this.safeAnnouncements = safeAnnouncements;
    }
    
    public String getWorkSeqClass() {
        return workSeqClass;
    }
    
    public void setWorkSeqClass(String workSeqClass) {
        this.workSeqClass = workSeqClass;
    }
    
    public String getTransinTimeStr() {
        return transinTimeStr;
    }
    
    public void setTransinTimeStr(String transinTimeStr) {
        this.transinTimeStr = transinTimeStr;
    }
    
    public String getWorkCardName() {
        return workCardName;
    }
    
    public void setWorkCardName(String workCardName) {
        this.workCardName = workCardName;
    }
    
    public String getRealBeginTimeStr() {
        return realBeginTimeStr;
    }
    
    public void setRealBeginTimeStr(String realBeginTimeStr) {
        this.realBeginTimeStr = realBeginTimeStr;
    }
    
    public String getRealEndTimeStr() {
        return realEndTimeStr;
    }
    
    public void setRealEndTimeStr(String realEndTimeStr) {
        this.realEndTimeStr = realEndTimeStr;
    }
    
    public String getWorker() {
        return worker;
    }
    
    public void setWorker(String worker) {
        this.worker = worker;
    }
}
