package com.yunda.jx.pjwz.check.entity;

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
 * <li>说明: 配件校验
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
@Table(name = "PJWZ_Parts_Check")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsCheck implements Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* 校验结果--合格*/
    public static final String CHECK_RESULT_YES = "合格" ;
    /* 校验结构--不合格*/
    public static final String CHECK_RESULT_NO = "不合格" ;
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    @Column(name = "check_EMP_ID")
    private Integer checkEmpId;// 校验人主键
    
    @Column(name = "check_EMP")
    private String handOverEmp;// 校验人
    
    @Column(name = "check_ORG_ID")
    private Integer checkOrgId;// 校验部门主键
    
    @Column(name = "check_ORG_SEQ")
    private String checkOrgSeq;// 校验部门序列
    
    @Column(name = "check_ORG")
    private String checkOrg;// 校验部门
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_TIME")
    private Date checkTime;// 校验日期
    
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
    
    /* 校验结果 */
    @Column(name = "check_result")
    private String checkResult;
    
    /* 备注 */
    private String remark;
    
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
    
    public String getHandOverEmp() {
        return handOverEmp;
    }
    
    public void setHandOverEmp(String handOverEmp) {
        this.handOverEmp = handOverEmp;
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
    
    public Integer getCheckEmpId() {
        return checkEmpId;
    }
    
    public void setCheckEmpId(Integer checkEmpId) {
        this.checkEmpId = checkEmpId;
    }
    
    public String getCheckOrg() {
        return checkOrg;
    }
    
    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg;
    }
    
    public Integer getCheckOrgId() {
        return checkOrgId;
    }
    
    public void setCheckOrgId(Integer checkOrgId) {
        this.checkOrgId = checkOrgId;
    }
    
    public String getCheckOrgSeq() {
        return checkOrgSeq;
    }
    
    public void setCheckOrgSeq(String checkOrgSeq) {
        this.checkOrgSeq = checkOrgSeq;
    }
    
    public String getCheckResult() {
        return checkResult;
    }
    
    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
    
    public Date getCheckTime() {
        return checkTime;
    }
    
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
