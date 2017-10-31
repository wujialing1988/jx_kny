package com.yunda.jx.jxgc.configmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车配置视图实体类
 * <li>创建人：程锐
 * <li>创建日期：2013-2-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * CRIF  2015-06-29  汪东良 高    将此代码跟机车信息管理“JczlTrain”相关代码结合，并考虑删除v_jxgc_trainconfigInfo相关代码
 */
@Entity
@Table(name="v_jxgc_trainconfigInfo")
public class TrainConfigInfo {
    /* idx主键 */
    @Id 
    private String idx;
    /* 车型主键 */
    @Column(name="TRAINTYPEIDX")
    private String trainTypeIDX;
    /* 车号 */
    @Column(name="TRAINNO")
    private String trainNo;
    /* 车型英文简称 */
    @Column(name="TRAINTYPESHORTNAME")
    private String trainTypeShortName;
    /* 资产状态(10使用中，20报废) */
    @Column(name="ASSETSTATE")
    private Integer assetState;
    /* 机车状态(10检修，20运用，30备用) */
    @Column(name="TRAINSTATE")
    private Integer trainState;
    /* 来源于机车公用基础码表：机车用途编码（J_JCGY_TRAIN_USER） */
    @Column(name="TRAINUSE")
    private String trainUse;
    /* 配属单位ID */
    @Column(name="HOLDORGID")
    private Long holdOrgId;
    /* 支配单位ID */
    @Column(name="USEDORGID")
    private Long usedOrgId;
    /* 原配属单位ID */
    @Column(name="OLDHOLDORGID")
    private Long oldHoldOrgId;
    /* 制造厂家主键，来源于表J_GYJC_FACTORY中的F_ID */
    @Column(name="MAKEFACTORYIDX")
    private String makeFactoryIDX;
    /* 制造厂家名，来源于表J_GYJC_FACTORY中的F_NAME */
    @Column(name="MAKEFACTORYNAME")
    private String makeFactoryName;
    /* 出厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LEAVEDATE")
    private java.util.Date leaveDate;
    /* 组成型号主键 */
    @Column(name="BUILDUPTYPEIDX")
    private String buildUpTypeIDX;
    /* 组成型号编码 */
    @Column(name="BUILDUPTYPECODE")
    private String buildUpTypeCode;
    /* 组成型号名称 */
    @Column(name="BUILDUPTYPENAME")
    private String buildUpTypeName;
    /* 备注 */
    private String remarks;
    /* 登记人 */
    @Column(name="REGISTERPERSON")
    private Long registerPerson;
    /* 登记人名称 */
    @Column(name="REGISTERPERSONNAME")
    private String registerPersonName;
    /* 登记时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REGISTERTIME")
    private java.util.Date registerTime;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    
    /* 是否有履历，用于演示的临时字段；1：是；0：否 */
    @Column(name="ISHAVERESUME")
    private Integer isHaveResume;
    
    /* 配属单位名称 */
    @Column(name="HOLDORGNAME")
    private String holdOrgName;
    /* 支配单位名称 */
    @Column(name="USEDORGNAME")
    private String usedOrgName;
    
    /* 原配属单位名称 */
    @Column(name="OLDHOLDORGNAME")
    private String oldHoldOrgName;
    
    /** 配属局ID */
    public String bId;
    /** 配属局名称 */
    public String bName;
    /** 配属局简称 */
    public String bShortName;
    /** 配属段ID */
    public String dId;
    /** 配属段名称 */
    public String dName;
    /** 配属段简称 */
    public String dShortName;
    
    public Integer getAssetState() {
        return assetState;
    }

    
    public void setAssetState(Integer assetState) {
        this.assetState = assetState;
    }

    
    public String getBuildUpTypeCode() {
        return buildUpTypeCode;
    }

    
    public void setBuildUpTypeCode(String buildUpTypeCode) {
        this.buildUpTypeCode = buildUpTypeCode;
    }

    
    public String getBuildUpTypeIDX() {
        return buildUpTypeIDX;
    }

    
    public void setBuildUpTypeIDX(String buildUpTypeIDX) {
        this.buildUpTypeIDX = buildUpTypeIDX;
    }

    
    public String getBuildUpTypeName() {
        return buildUpTypeName;
    }

    
    public void setBuildUpTypeName(String buildUpTypeName) {
        this.buildUpTypeName = buildUpTypeName;
    }

    
    public Long getHoldOrgId() {
        return holdOrgId;
    }

    
    public void setHoldOrgId(Long holdOrgId) {
        this.holdOrgId = holdOrgId;
    }

    
    public String getHoldOrgName() {
        return holdOrgName;
    }

    
    public void setHoldOrgName(String holdOrgName) {
        this.holdOrgName = holdOrgName;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Integer getIsHaveResume() {
        return isHaveResume;
    }

    
    public void setIsHaveResume(Integer isHaveResume) {
        this.isHaveResume = isHaveResume;
    }

    
    public java.util.Date getLeaveDate() {
        return leaveDate;
    }

    
    public void setLeaveDate(java.util.Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    
    public String getMakeFactoryIDX() {
        return makeFactoryIDX;
    }

    
    public void setMakeFactoryIDX(String makeFactoryIDX) {
        this.makeFactoryIDX = makeFactoryIDX;
    }

    
    public String getMakeFactoryName() {
        return makeFactoryName;
    }

    
    public void setMakeFactoryName(String makeFactoryName) {
        this.makeFactoryName = makeFactoryName;
    }

    
    public Long getOldHoldOrgId() {
        return oldHoldOrgId;
    }

    
    public void setOldHoldOrgId(Long oldHoldOrgId) {
        this.oldHoldOrgId = oldHoldOrgId;
    }

    
    public String getOldHoldOrgName() {
        return oldHoldOrgName;
    }

    
    public void setOldHoldOrgName(String oldHoldOrgName) {
        this.oldHoldOrgName = oldHoldOrgName;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public Long getRegisterPerson() {
        return registerPerson;
    }

    
    public void setRegisterPerson(Long registerPerson) {
        this.registerPerson = registerPerson;
    }

    
    public String getRegisterPersonName() {
        return registerPersonName;
    }

    
    public void setRegisterPersonName(String registerPersonName) {
        this.registerPersonName = registerPersonName;
    }

    
    public java.util.Date getRegisterTime() {
        return registerTime;
    }

    
    public void setRegisterTime(java.util.Date registerTime) {
        this.registerTime = registerTime;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public Integer getTrainState() {
        return trainState;
    }

    
    public void setTrainState(Integer trainState) {
        this.trainState = trainState;
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

    
    public String getTrainUse() {
        return trainUse;
    }

    
    public void setTrainUse(String trainUse) {
        this.trainUse = trainUse;
    }

    
    public Long getUsedOrgId() {
        return usedOrgId;
    }

    
    public void setUsedOrgId(Long usedOrgId) {
        this.usedOrgId = usedOrgId;
    }

    
    public String getUsedOrgName() {
        return usedOrgName;
    }

    
    public void setUsedOrgName(String usedOrgName) {
        this.usedOrgName = usedOrgName;
    }


    
    public String getBId() {
        return bId;
    }


    
    public void setBId(String id) {
        bId = id;
    }


    
    public String getBName() {
        return bName;
    }


    
    public void setBName(String name) {
        bName = name;
    }


    
    public String getBShortName() {
        return bShortName;
    }


    
    public void setBShortName(String shortName) {
        bShortName = shortName;
    }


    
    public String getDId() {
        return dId;
    }


    
    public void setDId(String id) {
        dId = id;
    }


    
    public String getDName() {
        return dName;
    }


    
    public void setDName(String name) {
        dName = name;
    }


    
    public String getDShortName() {
        return dShortName;
    }


    
    public void setDShortName(String shortName) {
        dShortName = shortName;
    }
    
    
}
