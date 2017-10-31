package com.yunda.jx.pjjx.partsrdp.wpinst.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修作业节点实体包装类
 * <li>创建人：程锐
 * <li>创建日期：2015-9-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Entity
public class PartsRdpNodeBean implements java.io.Serializable {
    
    /** 默认序列号 */
    private static final long serialVersionUID = -1856930317769171057L;

	/* idx主键 */
    @Id
    private String idx;

    /* 配件新增台账主键 */
    @Column(name="PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    @Column(name="IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 规格型号 */
    @Column(name="SPECIFICATION_MODEL")
    private String specificationModel;
    
    /* 配件名称 */
    @Column(name="PARTS_NAME")
    private String partsName;
    
    /* 配件编号 */
    @Column(name="PARTS_NO")
    private String partsNo;
    
    /* 下车车型 */
    @Column(name="UNLOAD_TRAINTYPE")
    private String unloadTrainType;
    
    /* 下车车号 */
    @Column(name="UNLOAD_TRAINNO")
    private String unloadTrainNo;
    
    /* 下车修程 */
    @Column(name="UNLOAD_REPAIR_CLASS")
    private String unloadRepairClass;
    
    /* 下车修次 */
    @Column(name="UNLOAD_REPAIR_TIME")
    private String unloadRepairTime;
    
    /* 承修班组名称 */
    @Column(name="REPAIR_ORGNAME")
    private String repairOrgName;
    
    /* 作业主键 */
    @Column(name="RDP_IDX")
    private String rdpIDX;
    
    /* 节点名称 */
    @Column(name="WP_NODE_NAME")
    private String wpNodeName;
    
    /* 计划开始时间 */
    @Column(name="PLAN_STARTTIME")
    private String planStartTime;
    
    /* 计划结束时间 */
    @Column(name="PLAN_ENDTIME")
    private String planEndTime;
    
    /* 实际开始时间 */
    @Column(name="REAL_STARTTIME")
    private String realStartTime;
    
    /* 实际结束时间 */
    @Column(name="REAL_ENDTIME")
    private String realEndTime;
    
    /* 状态 */
    private String status;
    
    /* 作业工位 */
    @Column(name="WORK_STATION_NAME")
    private String workStationName;
    
    /* 作业工位主键 */
    @Column(name="WORK_STATION_IDX")
    private String workStationIDX;
    
    /* 下车（下配件）位置*/
    @Column(name="UNLOAD_PLACE")
    private String unloadPlace;
    
    /** 配件检修作业节点下未处理的工单数，包含检修记录工单、作业工单、 回修提票 */
    @Column(name="TASK_COUNTS")
    private Integer taskCounts;
    
    /** 配件检修作业节点下返工的记录工单数 */
    @Column(name="BACK_COUNTS")
    private Integer backCounts;
    
    public Integer getTaskCounts() {
        return taskCounts;
    }

    public void setTaskCounts(Integer taskCounts) {
        this.taskCounts = taskCounts;
    }

    public String getUnloadPlace() {
		return unloadPlace;
	}
    
    public Integer getBackCounts() {
        return backCounts;
    }

    public void setBackCounts(Integer backCounts) {
        this.backCounts = backCounts;
    }

	public void setUnloadPlace(String unloadPlace) {
		this.unloadPlace = unloadPlace;
	}

	public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
//    public String getMatCode() {
//        return matCode;
//    }
//    
//    public void setMatCode(String matCode) {
//        this.matCode = matCode;
//    }
    
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }
    
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getPartsNo() {
        return partsNo;
    }
    
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    
//    public String getPartsTypeIDX() {
//        return partsTypeIDX;
//    }
//    
//    public void setPartsTypeIDX(String partsTypeIDX) {
//        this.partsTypeIDX = partsTypeIDX;
//    }
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
//    public Long getRepairOrgID() {
//        return repairOrgID;
//    }
//    
//    public void setRepairOrgID(Long repairOrgID) {
//        this.repairOrgID = repairOrgID;
//    }
    
    public String getRepairOrgName() {
        return repairOrgName;
    }
    
    public void setRepairOrgName(String repairOrgName) {
        this.repairOrgName = repairOrgName;
    }
    
//    public String getRepairOrgSeq() {
//        return repairOrgSeq;
//    }
//    
//    public void setRepairOrgSeq(String repairOrgSeq) {
//        this.repairOrgSeq = repairOrgSeq;
//    }
//    
//    public Integer getSeqNo() {
//        return seqNo;
//    }
//    
//    public void setSeqNo(Integer seqNo) {
//        this.seqNo = seqNo;
//    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getUnloadRepairClass() {
        return unloadRepairClass;
    }
    
    public void setUnloadRepairClass(String unloadRepairClass) {
        this.unloadRepairClass = unloadRepairClass;
    }
    
//    public String getUnloadRepairClassIdx() {
//        return unloadRepairClassIdx;
//    }
//    
//    public void setUnloadRepairClassIdx(String unloadRepairClassIdx) {
//        this.unloadRepairClassIdx = unloadRepairClassIdx;
//    }
    
    public String getUnloadRepairTime() {
        return unloadRepairTime;
    }
    
    public void setUnloadRepairTime(String unloadRepairTime) {
        this.unloadRepairTime = unloadRepairTime;
    }
    
//    public String getUnloadRepairTimeIdx() {
//        return unloadRepairTimeIdx;
//    }
//    
//    public void setUnloadRepairTimeIdx(String unloadRepairTimeIdx) {
//        this.unloadRepairTimeIdx = unloadRepairTimeIdx;
//    }
    
    public String getUnloadTrainNo() {
        return unloadTrainNo;
    }
    
    public void setUnloadTrainNo(String unloadTrainNo) {
        this.unloadTrainNo = unloadTrainNo;
    }
    
    public String getUnloadTrainType() {
        return unloadTrainType;
    }
    
    public void setUnloadTrainType(String unloadTrainType) {
        this.unloadTrainType = unloadTrainType;
    }
    
//    public String getUnloadTrainTypeIdx() {
//        return unloadTrainTypeIdx;
//    }
//    
//    public void setUnloadTrainTypeIdx(String unloadTrainTypeIdx) {
//        this.unloadTrainTypeIdx = unloadTrainTypeIdx;
//    }
    
    public String getWorkStationName() {
        return workStationName;
    }
    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
//    public String getWpNodeDesc() {
//        return wpNodeDesc;
//    }
//    
//    public void setWpNodeDesc(String wpNodeDesc) {
//        this.wpNodeDesc = wpNodeDesc;
//    }
//    
//    public String getWpNodeIDX() {
//        return wpNodeIDX;
//    }
//    
//    public void setWpNodeIDX(String wpNodeIDX) {
//        this.wpNodeIDX = wpNodeIDX;
//    }
    
    public String getWpNodeName() {
        return wpNodeName;
    }
    
    public void setWpNodeName(String wpNodeName) {
        this.wpNodeName = wpNodeName;
    }
    
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    public String getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    public String getPlanStartTime() {
        return planStartTime;
    }
    
    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }
    
    public String getRealEndTime() {
        return realEndTime;
    }
    
    public void setRealEndTime(String realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    public String getRealStartTime() {
        return realStartTime;
    }
    
    public void setRealStartTime(String realStartTime) {
        this.realStartTime = realStartTime;
    }
}
