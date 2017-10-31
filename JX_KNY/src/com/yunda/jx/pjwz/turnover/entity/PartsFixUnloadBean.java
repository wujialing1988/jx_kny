package com.yunda.jx.pjwz.turnover.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图PJWZ_PARTS_FIX_UPLOAD_VIEW实体类, 数据表：配件上车下车信息查询视图
 * <li>创建人：张迪
 * <li>创建日期：2016年10月28日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_FIX_UPLOAD_VIEW")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsFixUnloadBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    @Id
    private String idx;
   
    /* 下车配件清单定义主键 */
    @Column(name = "JOBPROCESS_OFF_PARTS_IDX")
    private String offPartsIDx;
    
    /* 配件主键 */
    @Column(name = "Parts_IDX")
    private String partsIDX;
    
    /* 配件名称 */
    @Column(name = "Parts_Name")
    private String partsName;
    /* 位置代码    */
    @Column(name="WZDM")
    private String wzdm;
    /* 位置名称 */
    @Column(name="WZMC")
    private String  wzmc;
  
    
    /* 作业流程上车节点主键 */
    @Column(name = "on_Node_IDX")
    private String onNodeIDX;
    
    /* 上车节点实例主键 */
    @Column(name = "on_Node_Case_IDX")
    private String onNodeCaseIDX;
    
    /* 上车节点名称 */
    @Column(name = "on_Node_Name")
    private String onNodeName;
    
    /* 作业流程下车节点主键 */
    @Column(name = "off_Node_IDX")
    private String offNodeIDX;
    
    /* 下车节点实例主键 */
    @Column(name = "off_Node_Case_IDX")
    private String offNodeCaseIDX;
    
    /* 下车节点名称 */
    @Column(name = "off_Node_Name")
    private String offNodeName;
    
   
    /* 作业计划兑现单主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;
    /* 作业流程主键 */
    @Column(name = "process_IDX")
    private String processIDX;
    /* 车型主键 */
    @Column(name="Train_Type_IDX")
    private String trainTypeIDX;       
    /* 车型简称 */
    @Column(name="Train_Type_ShortName")
    private String trainTypeShortName;
    /* 车号 */
    @Column(name="Train_No")
    private String trainNo;
    
    /* 下车配件编号 */
    @Column(name="unload_parts_no")
    private String unloadPartsNo;
    /* 下车配件规格型号 */
    @Column(name="unload_specification_model")
    private String unloadSpecificationModel;
    /* 下车配件台账idx */
    @Column(name="unload_parts_account_idx")
    private String unloadPartsAccountIdx;
    
    /* 下车日期 */
    @Column(name="unload_date")
    private Date unloadDate;
    /* 上车配件编号 */
    @Column(name="aboard_parts_no")
    private String aboardPartsNo;
    /* 下车配件规格型号 */
    @Column(name="aboard_specification_model")
    private String aboardSpecificationModel;
    /* 上车配件台账idx */
    @Column(name="aboard_parts_account_idx")
    private String aboardPartsAccountIdx;
    /* 上车日期 */
    @Column(name="aboard_date")
    private Date aboardDate;
    
    /* 配件状态是否是新品*/
    @Column(name="parts_status")
    private String partsStatus;

    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    
    /**
     * @return String 获取配件名称
     */
    public String getPartsName() {
        return partsName;
    }
    
    /**
     * @param partsName 设置配件名称
     */
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    
    public Date getAboardDate() {
        return aboardDate;
    }

    
    public void setAboardDate(Date aboardDate) {
        this.aboardDate = aboardDate;
    }

    
    public String getAboardPartsAccountIdx() {
        return aboardPartsAccountIdx;
    }

    
    public void setAboardPartsAccountIdx(String aboardPartsAccountIdx) {
        this.aboardPartsAccountIdx = aboardPartsAccountIdx;
    }

    
    public String getAboardPartsNo() {
        return aboardPartsNo;
    }

    
    public void setAboardPartsNo(String aboardPartsNo) {
        this.aboardPartsNo = aboardPartsNo;
    }

    
    public java.util.Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    
    public Long getCreator() {
        return creator;
    }

    
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getOffNodeCaseIDX() {
        return offNodeCaseIDX;
    }

    
    public void setOffNodeCaseIDX(String offNodeCaseIDX) {
        this.offNodeCaseIDX = offNodeCaseIDX;
    }

    
    public String getOffNodeIDX() {
        return offNodeIDX;
    }

    
    public void setOffNodeIDX(String offNodeIDX) {
        this.offNodeIDX = offNodeIDX;
    }

    
    public String getOffNodeName() {
        return offNodeName;
    }

    
    public void setOffNodeName(String offNodeName) {
        this.offNodeName = offNodeName;
    }

    
    public String getOffPartsIDx() {
        return offPartsIDx;
    }

    
    public void setOffPartsIDx(String offPartsIDx) {
        this.offPartsIDx = offPartsIDx;
    }

    
    public String getOnNodeCaseIDX() {
        return onNodeCaseIDX;
    }

    
    public void setOnNodeCaseIDX(String onNodeCaseIDX) {
        this.onNodeCaseIDX = onNodeCaseIDX;
    }

    
    public String getOnNodeIDX() {
        return onNodeIDX;
    }

    
    public void setOnNodeIDX(String onNodeIDX) {
        this.onNodeIDX = onNodeIDX;
    }

    
    public String getOnNodeName() {
        return onNodeName;
    }

    
    public void setOnNodeName(String onNodeName) {
        this.onNodeName = onNodeName;
    }

    
    public String getPartsIDX() {
        return partsIDX;
    }

    
    public void setPartsIDX(String partsIDX) {
        this.partsIDX = partsIDX;
    }

    
    public String getProcessIDX() {
        return processIDX;
    }

    
    public void setProcessIDX(String processIDX) {
        this.processIDX = processIDX;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public String getSiteID() {
        return siteID;
    }

    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }

    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }

    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }

    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }

    
    public Date getUnloadDate() {
        return unloadDate;
    }

    
    public void setUnloadDate(Date unloadDate) {
        this.unloadDate = unloadDate;
    }

    
    public String getUnloadPartsAccountIdx() {
        return unloadPartsAccountIdx;
    }

    
    public void setUnloadPartsAccountIdx(String unloadPartsAccountIdx) {
        this.unloadPartsAccountIdx = unloadPartsAccountIdx;
    }

    
    public String getUnloadPartsNo() {
        return unloadPartsNo;
    }

    
    public void setUnloadPartsNo(String unloadPartsNo) {
        this.unloadPartsNo = unloadPartsNo;
    }

    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public Long getUpdator() {
        return updator;
    }

    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }

    
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }

    
    public String getWzdm() {
        return wzdm;
    }

    
    public void setWzdm(String wzdm) {
        this.wzdm = wzdm;
    }

    
    public String getWzmc() {
        return wzmc;
    }

    
    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }

    
    public String getPartsStatus() {
        return partsStatus;
    }

    
    public void setPartsStatus(String partsStatus) {
        this.partsStatus = partsStatus;
    }

    
    public String getAboardSpecificationModel() {
        return aboardSpecificationModel;
    }

    
    public void setAboardSpecificationModel(String aboardSpecificationModel) {
        this.aboardSpecificationModel = aboardSpecificationModel;
    }

    
    public String getUnloadSpecificationModel() {
        return unloadSpecificationModel;
    }

    
    public void setUnloadSpecificationModel(String unloadSpecificationModel) {
        this.unloadSpecificationModel = unloadSpecificationModel;
    }
    
}
