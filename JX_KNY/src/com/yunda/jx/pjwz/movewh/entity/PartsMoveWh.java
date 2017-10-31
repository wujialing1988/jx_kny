package com.yunda.jx.pjwz.movewh.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件移库
 * <li>创建人：程梅
 * <li>创建日期：2016年5月31日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_Parts_Move_Wh")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsMoveWh implements Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    @Column(name = "WH_IDX")
    private String whIdx;// 移出库房主键
    
    @Column(name = "WH_NAME")
    private String whName;// 移出库房
    
    private String location;// 移出位置
    
    @Column(name = "IN_WH_IDX")
    private String inWhIdx;// 移入库房主键
    
    @Column(name = "IN_WH_NAME")
    private String inWhName;// 移入库房
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "move_TIME")
    private Date moveTime;// 移库日期
    
    @Column(name = "move_EMP_ID")
    private Integer moveEmpId;// 移库人主键
    
    @Column(name = "move_EMP")
    private String moveEmp;// 移库人
    
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIdx;// 配件信息主键
    
    @Column(name = "PARTS_TYPE_IDX")
    private String partsTypeIDX;// 规格型号主键
    
    @Column(name = "Parts_Name")
    private String partsName;// 配件名称
    
    @Column(name = "Specification_Model")
    private String specificationModel;// 规格型号
    
    @Column(name = "Parts_No")
    private String partsNo;// 配件编号
    
    /* 配件铭牌号 */
    @Column(name = "nameplate_No")
    private String nameplateNo;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 单据状态 */
    private String status;
    
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 站点标示，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
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
    
    public String getPartsAccountIdx() {
        return partsAccountIdx;
    }
    
    public void setPartsAccountIdx(String partsAccountIdx) {
        this.partsAccountIdx = partsAccountIdx;
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
    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
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
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
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
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getNameplateNo() {
        return nameplateNo;
    }
    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
    
    public String getInWhIdx() {
        return inWhIdx;
    }
    
    public void setInWhIdx(String inWhIdx) {
        this.inWhIdx = inWhIdx;
    }
    
    public String getInWhName() {
        return inWhName;
    }
    
    public void setInWhName(String inWhName) {
        this.inWhName = inWhName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getMoveEmp() {
        return moveEmp;
    }
    
    public void setMoveEmp(String moveEmp) {
        this.moveEmp = moveEmp;
    }
    
    public Integer getMoveEmpId() {
        return moveEmpId;
    }
    
    public void setMoveEmpId(Integer moveEmpId) {
        this.moveEmpId = moveEmpId;
    }
    
    public Date getMoveTime() {
        return moveTime;
    }
    
    public void setMoveTime(Date moveTime) {
        this.moveTime = moveTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getWhIdx() {
        return whIdx;
    }
    
    public void setWhIdx(String whIdx) {
        this.whIdx = whIdx;
    }
    
    public String getWhName() {
        return whName;
    }
    
    public void setWhName(String whName) {
        this.whName = whName;
    }
}
