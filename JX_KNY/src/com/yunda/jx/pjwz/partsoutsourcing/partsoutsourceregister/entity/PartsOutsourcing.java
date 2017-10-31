package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件委外实体类
 * <li>创建人：王斌
 * <li>创建日期：2014-5-21
 * <li>修改人: 何涛
 * <li>修改日期：2014-08-28
 * <li>修改内容：新增“扩展编号json”属性
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_OUTSOURCING")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsOutsourcing implements Serializable {
    
    /** 配件状态 - 已返回(back) */
    public static final String PARTS_STATUS_YFH = "back";
    
    /** 配件状态 - 委外(out) */
    public static final String PARTS_STATUS_WW = "out";
    
    public static final int TAKE_OVER_TYPE_ORG = 1;// 接收部门类型：机构
    
    public static final int TAKE_OVER_TYPE_WH = 2;// 接收部门类型：库房
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 检修任务单主键 */
    @Column(name="RDP_IDX")
    private String rdpIdx;
    
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;// 配件信息主键
    
    @Column(name = "PARTS_TYPE_IDX")
    private String partsTypeIdx;// 配件型号表主键
    
    @Column(name = "PARTS_NAME")
    private String partsName;// 配件名称
    
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;// 规格规格型号
    
    @Column(name = "UNIT")
    private String unit;// 计量单位
    
    @Column(name = "PARTS_OUT_NO")
    private String partsOutNo;// 配件委外编号
    
    @Column(name = "PARTS_BACK_NO")
    private String partsBackNo;// 配件返回编号
    
    /* 配件铭牌号 */
    @Column(name = "nameplate_No")
    private String nameplateNo;
    
    @Column(name = "OUTSOURCING_FACTORY_ID")
    private String outsourcingFactoryId;// 委外厂家编码
    
    @Column(name = "OUTSOURCING_FACTORY")
    private String outsourcingFactory;// 委外厂家
    
    @Column(name = "OUTSOURCING_REASION")
    private String outsourcingReasion;// 委外检修原因
    
    @Column(name = "CAR_NO")
    private String carNo;// 车牌号
    
    @Column(name = "REPAIR_CONTENT")
    private String repairContent;// 修理内容
    
    @Temporal(TemporalType.DATE)
    @Column(name = "OUTSOURCING_DATE")
    private Date outsourcingDate;// 委外日期
    
    @Column(name = "OUT_ORG_ID")
    private Integer outOrgId;// 经办部门主键
    
    @Column(name = "OUT_ORG")
    private String outOrg;// 经办部门
    
    @Column(name = "OUT_EMP_ID")
    private Integer outEmpId;// 经办人主键
    
    @Column(name = "OUT_EMP")
    private String outEmp;// 经办人
    
    @Temporal(TemporalType.DATE)
    @Column(name = "BACK_DATE")
    private Date backDate;// 返回日期
    
    @Column(name = "TAKE_OVER_EMP_ID")
    private Integer takeOverEmpId;// 接收人主键
    
    @Column(name = "TAKE_OVER_EMP")
    private String takeOverEmp;// 接收人
    
    @Column(name = "TAKE_OVER_ORG_ID")
    private String takeOverOrgId;// 接收部门主键
    
    @Column(name = "TAKE_OVER_ORG")
    private String takeOverOrg;// 接收部门
    
    @Column(name = "REMARKS")
    private String remarks;// 备注
    
    @Column(name = "STATUS")
    private String status;// 配件状态：out（已委外）back（已返回）
    
    @Column(name = "CREATOR_NAME")
    private String creatorName;// 制单人
    
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
    
    @Transient
    private String configDetail;// 详细配置
    
    /** 扩展编号json */
    @Column(name = "ExtendNoJson")
    private String extendNoJson;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 配件返回识别码 */
    @Column(name = "IDENTIFICATION_CODE_back")
    private String identificationCodeBack;
    
    /* 接收部门类型：1：机构；2：库房 */
    @Column(name = "TAKE_OVER_Type")
    private Integer takeOverType;
    
    /* 接收部门序列(存储组织机构的orgseq字段) */
    @Column(name = "TAKE_OVER_ORGSEQ")
    private String takeOverOrgSeq;
    
    /* 回段配件状态编码 */
    @Column(name = "BACK_Parts_Status")
    private String backPartsStatus;
    
    /* 回段配件状态名称 */
    @Column(name = "BACK_Parts_Status_NAME")
    private String backPartsStatusName;
    
    /* 是否超范围登记 */
    @Column(name = "IS_IN_RANGE")
    private String isInRange;
    
    
    public String getBackPartsStatus() {
        return backPartsStatus;
    }

    
    public void setBackPartsStatus(String backPartsStatus) {
        this.backPartsStatus = backPartsStatus;
    }

    
    public String getBackPartsStatusName() {
        return backPartsStatusName;
    }

    
    public void setBackPartsStatusName(String backPartsStatusName) {
        this.backPartsStatusName = backPartsStatusName;
    }

    public String getTakeOverOrgSeq() {
        return takeOverOrgSeq;
    }
    
    public void setTakeOverOrgSeq(String takeOverOrgSeq) {
        this.takeOverOrgSeq = takeOverOrgSeq;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    /**
     * @return 获取扩展编号json
     */
    public String getExtendNoJson() {
        return extendNoJson;
    }
    
    /**
     * @param extendNoJson 设置扩展编号json
     */
    public void setExtendNoJson(String extendNoJson) {
        this.extendNoJson = extendNoJson;
    }
    
    /**
     * <li>说明：构造方法
     * <li>创建人：程梅
     * <li>创建日期：2015-12-11
     * <li>修改人：
     * <li>修改日期：
     */
    public PartsOutsourcing() {
        
    }
    
    /**
     * <li>说明：构造方法
     * <li>创建人：程梅
     * <li>创建日期：2015-12-11
     * <li>修改人：
     * <li>修改日期：
     * @param partsAccountIDX 配件台账id
     * @param partsTypeIdx 型号id
     * @param partsName 配件名称
     * @param specificationModel 规格型号
     * @param partsOutNo 委外编号
     * @param extendNoJson 扩展编号
     * @param status 状态
     * @param configDetail 详细配置
     * @param outsourcingFactoryId 委外厂家编码
     * @param outsourcingFactory 委外厂家
     * @param outsourcingDate 委外日期
     */
    public PartsOutsourcing(String partsAccountIDX, String partsTypeIdx, String partsName, String specificationModel, String partsOutNo,
        String extendNoJson, String status, String configDetail, String outsourcingFactoryId, String outsourcingFactory, Date outsourcingDate) {
        this.partsAccountIDX = partsAccountIDX;
        this.partsTypeIdx = partsTypeIdx;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
        this.partsOutNo = partsOutNo;
        this.extendNoJson = extendNoJson;
        this.status = status;
        this.configDetail = configDetail;
        this.outsourcingFactoryId = outsourcingFactoryId;
        this.outsourcingFactory = outsourcingFactory;
        this.outsourcingDate = outsourcingDate;
    }
    
    /**
     * <li>说明：查询状态为委外的配件委外登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人：
     * <li>修改日期：
     * @param outEmpId 经办人
     * @param outEmp 经办人
     * @param outsourcingDate 委外日期
     * @param outsourcingFactoryId 委外厂家主键
     * @param outsourcingFactory 委外厂家名称
     * @param partsOutNo 委外编号
     * @param nameplateNo 铭牌号
     * @param identificationCode 识别码
     * @param partsName 配件名称
     * @param specificationModel 规格型号
     * @param outsourcingReasion 委外原因
     * @param repairContent 修理内容
     */
    public PartsOutsourcing(Integer outEmpId, String outEmp, Date outsourcingDate, String outsourcingFactoryId, String outsourcingFactory,
        String partsOutNo, String nameplateNo, String identificationCode, String partsName, String specificationModel, String outsourcingReasion,
        String repairContent) {
        this.outEmpId = outEmpId;
        this.outEmp = outEmp;
        this.nameplateNo = nameplateNo;
        this.identificationCode = identificationCode;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
        this.partsOutNo = partsOutNo;
        this.outsourcingReasion = outsourcingReasion;
        this.repairContent = repairContent;
        this.outsourcingFactoryId = outsourcingFactoryId;
        this.outsourcingFactory = outsourcingFactory;
        this.outsourcingDate = outsourcingDate;
    }
    
    /**
     * <li>说明：查询状态为已返回的配件委外登记记录
     * <li>创建人：程梅
     * <li>创建日期：2016-6-21
     * <li>修改人：
     * <li>修改日期：
     * @param takeOverEmpId 经办人
     * @param takeOverEmp 经办人
     * @param outsourcingDate 委外日期
     * @param outsourcingFactoryId 委外厂家主键
     * @param outsourcingFactory 委外厂家名称
     * @param partsOutNo 委外编号
     * @param nameplateNo 铭牌号
     * @param identificationCode 识别码
     * @param partsName 配件名称
     * @param specificationModel 规格型号
     * @param outsourcingReasion 委外原因
     * @param repairContent 修理内容
     * @param backDate 返回日期
     * @param partsBackNo 返回编号
     * @param identificationCodeBack 返回识别码
     * @param takeOverOrgId 接收部门主键
     * @param takeOverOrg 接收部门名称
     * @param takeOverOrgSeq 接收部门序列
     */
    public PartsOutsourcing(Integer takeOverEmpId, String takeOverEmp, Date outsourcingDate, Date backDate, String outsourcingFactoryId,
        String outsourcingFactory, String partsOutNo, String partsBackNo, String nameplateNo, String identificationCode,
        String identificationCodeBack, String partsName, String specificationModel, String outsourcingReasion, String repairContent,
        String takeOverOrgId, String takeOverOrg, String takeOverOrgSeq) {
        this.takeOverEmpId = takeOverEmpId;
        this.takeOverEmp = takeOverEmp;
        this.nameplateNo = nameplateNo;
        this.identificationCode = identificationCode;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
        this.partsOutNo = partsOutNo;
        this.outsourcingReasion = outsourcingReasion;
        this.repairContent = repairContent;
        this.outsourcingFactoryId = outsourcingFactoryId;
        this.outsourcingFactory = outsourcingFactory;
        this.outsourcingDate = outsourcingDate;
        this.backDate = backDate;
        this.partsBackNo = partsBackNo;
        this.identificationCodeBack = identificationCodeBack;
        this.takeOverOrgId = takeOverOrgId;
        this.takeOverOrg = takeOverOrg;
        this.takeOverOrgSeq = takeOverOrgSeq;
    }
    
    public Date getBackDate() {
        return backDate;
    }
    
    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }
    
    public String getCarNo() {
        return carNo;
    }
    
    public void setCarNo(String carNo) {
        this.carNo = carNo;
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
    
    public String getOutEmp() {
        return outEmp;
    }
    
    public void setOutEmp(String outEmp) {
        this.outEmp = outEmp;
    }
    
    public Integer getOutEmpId() {
        return outEmpId;
    }
    
    public void setOutEmpId(Integer outEmpId) {
        this.outEmpId = outEmpId;
    }
    
    public String getOutOrg() {
        return outOrg;
    }
    
    public void setOutOrg(String outOrg) {
        this.outOrg = outOrg;
    }
    
    public Integer getOutOrgId() {
        return outOrgId;
    }
    
    public void setOutOrgId(Integer outOrgId) {
        this.outOrgId = outOrgId;
    }
    
    public Date getOutsourcingDate() {
        return outsourcingDate;
    }
    
    public void setOutsourcingDate(Date outsourcingDate) {
        this.outsourcingDate = outsourcingDate;
    }
    
    public String getOutsourcingFactoryId() {
        return outsourcingFactoryId;
    }
    
    public void setOutsourcingFactoryId(String outsourcingFactoryId) {
        this.outsourcingFactoryId = outsourcingFactoryId;
    }
    
    public String getOutsourcingReasion() {
        return outsourcingReasion;
    }
    
    public void setOutsourcingReasion(String outsourcingReasion) {
        this.outsourcingReasion = outsourcingReasion;
    }
    
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }
    
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
    
    public String getPartsBackNo() {
        return partsBackNo;
    }
    
    public void setPartsBackNo(String partsBackNo) {
        this.partsBackNo = partsBackNo;
    }
    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getPartsOutNo() {
        return partsOutNo;
    }
    
    public void setPartsOutNo(String partsOutNo) {
        this.partsOutNo = partsOutNo;
    }
    
    public String getPartsTypeIdx() {
        return partsTypeIdx;
    }
    
    public void setPartsTypeIdx(String partsTypeIdx) {
        this.partsTypeIdx = partsTypeIdx;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getRepairContent() {
        return repairContent;
    }
    
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
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
    
    public String getTakeOverOrg() {
        return takeOverOrg;
    }
    
    public void setTakeOverOrg(String takeOverOrg) {
        this.takeOverOrg = takeOverOrg;
    }
    
    public String getTakeOverOrgId() {
        return takeOverOrgId;
    }
    
    public void setTakeOverOrgId(String takeOverOrgId) {
        this.takeOverOrgId = takeOverOrgId;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
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
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getOutsourcingFactory() {
        return outsourcingFactory;
    }
    
    public void setOutsourcingFactory(String outsourcingFactory) {
        this.outsourcingFactory = outsourcingFactory;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getConfigDetail() {
        return configDetail;
    }
    
    public void setConfigDetail(String configDetail) {
        this.configDetail = configDetail;
    }
    
    public String getIdentificationCodeBack() {
        return identificationCodeBack;
    }
    
    public void setIdentificationCodeBack(String identificationCodeBack) {
        this.identificationCodeBack = identificationCodeBack;
    }
    
    public Integer getTakeOverType() {
        return takeOverType;
    }
    
    public void setTakeOverType(Integer takeOverType) {
        this.takeOverType = takeOverType;
    }
    
    public String getNameplateNo() {
        return nameplateNo;
    }
    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }

    
    public String getRdpIdx() {
        return rdpIdx;
    }
    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public String getIsInRange() {
        return isInRange;
    }


    
    public void setIsInRange(String isInRange) {
        this.isInRange = isInRange;
    }
    
    
    
}
