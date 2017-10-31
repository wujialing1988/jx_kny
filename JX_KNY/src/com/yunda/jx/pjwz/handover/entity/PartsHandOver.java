package com.yunda.jx.pjwz.handover.entity;

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
 * <li>说明: 配件交接
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
@Table(name = "PJWZ_Parts_Hand_over")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsHandOver implements Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    @Column(name = "HAND_OVER_EMP_ID")
    private Integer handOverEmpId;// 交件人主键
    
    @Column(name = "HAND_OVER_EMP")
    private String handOverEmp;// 交件人
    
    @Column(name = "HAND_OVER_ORG_ID")
    private Integer handOverOrgId;// 交件部门主键
    
    @Column(name = "HAND_OVER_ORG_SEQ")
    private String handOverOrgSeq;// 交件部门序列
    
    @Column(name = "HAND_OVER_ORG")
    private String handOverOrg;// 交件部门
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hand_over_TIME")
    private Date handOverTime;// 交件日期
    
    @Column(name = "TAKE_OVER_EMP_ID")
    private Integer takeOverEmpId;// 接收人主键
    
    @Column(name = "TAKE_OVER_EMP")
    private String takeOverEmp;// 接收人
    
    @Column(name = "TAKE_OVER_ORG_ID")
    private Integer takeOverOrgId;// 接收部门主键
    
    @Column(name = "TAKE_OVER_ORG_SEQ")
    private String takeOverOrgSeq;// 接收部门序列
    
    @Column(name = "TAKE_OVER_ORG")
    private String takeOverOrg;// 接收部门
    
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
    
    /* 配件状态编码 */
    @Column(name="Parts_Status")
    private String partsStatus;
    /* 配件状态名称 */
    @Column(name="Parts_Status_Name")
    private String partsStatusName;
    
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
    
    public String getHandOverEmp() {
        return handOverEmp;
    }
    
    public void setHandOverEmp(String handOverEmp) {
        this.handOverEmp = handOverEmp;
    }
    
    public Integer getHandOverEmpId() {
        return handOverEmpId;
    }
    
    public void setHandOverEmpId(Integer handOverEmpId) {
        this.handOverEmpId = handOverEmpId;
    }
    
    public String getHandOverOrg() {
        return handOverOrg;
    }
    
    public void setHandOverOrg(String handOverOrg) {
        this.handOverOrg = handOverOrg;
    }
    
    public Integer getHandOverOrgId() {
        return handOverOrgId;
    }
    
    public void setHandOverOrgId(Integer handOverOrgId) {
        this.handOverOrgId = handOverOrgId;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getTakeOverEmp() {
        return takeOverEmp;
    }
    
    public void setTakeOverEmp(String takeOverEmp) {
        this.takeOverEmp = takeOverEmp;
    }
    
    public Integer getTakeOverEmpId() {
        return takeOverEmpId;
    }
    
    public void setTakeOverEmpId(Integer takeOverEmpId) {
        this.takeOverEmpId = takeOverEmpId;
    }
    
    public String getHandOverOrgSeq() {
        return handOverOrgSeq;
    }
    
    public void setHandOverOrgSeq(String handOverOrgSeq) {
        this.handOverOrgSeq = handOverOrgSeq;
    }
    
    public Date getHandOverTime() {
        return handOverTime;
    }
    
    public void setHandOverTime(Date handOverTime) {
        this.handOverTime = handOverTime;
    }
    
    public String getTakeOverOrg() {
        return takeOverOrg;
    }
    
    public void setTakeOverOrg(String takeOverOrg) {
        this.takeOverOrg = takeOverOrg;
    }
    
    public Integer getTakeOverOrgId() {
        return takeOverOrgId;
    }
    
    public void setTakeOverOrgId(Integer takeOverOrgId) {
        this.takeOverOrgId = takeOverOrgId;
    }
    
    public String getTakeOverOrgSeq() {
        return takeOverOrgSeq;
    }
    
    public void setTakeOverOrgSeq(String takeOverOrgSeq) {
        this.takeOverOrgSeq = takeOverOrgSeq;
    }
    
    public String getNameplateNo() {
        return nameplateNo;
    }
    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }

    
    public String getPartsStatus() {
        return partsStatus;
    }

    
    public void setPartsStatus(String partsStatus) {
        this.partsStatus = partsStatus;
    }

    
    public String getPartsStatusName() {
        return partsStatusName;
    }

    
    public void setPartsStatusName(String partsStatusName) {
        this.partsStatusName = partsStatusName;
    }
}
