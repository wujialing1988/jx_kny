package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件委外登记bean
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-11-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsOutsourcingOutBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @Column(name = "row_num")
    private String rownum ;
    
    /* 配件委外登记主键 */
    @Column(name = "idx")
    private String idx;
    
    /* 检修任务单主键 */
    @Column(name = "RDP_IDX")
    private String rdpIdx;
    
    /* 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    /* 配件规格型号主键 */
    @Column(name = "PARTS_TYPE_IDX")
    private String partsTypeIdx;
    
    /* 规格型号 */
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;
    
    /* 配件名称 */
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /* 配件编号 */
    @Column(name = "PARTS_NO")
    private String partsOutNo;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 委外检修原因 */
    @Column(name = "OUTSOURCING_REASION")
    private String outsourcingReasion;
    
    /* 修理内容 */
    @Column(name = "REPAIR_CONTENT")
    private String repairContent;
    
    /* 委外厂家编码 */
    @Column(name = "OUTSOURCING_FACTORY_ID")
    private String outsourcingFactoryId; 
    
    /* 委外厂家 */
    @Column(name = "OUTSOURCING_FACTORY")
    private String outsourcingFactory;
    
    /* 委外日期 */
    @Temporal(TemporalType.DATE)
    @Column(name = "OUTSOURCING_DATE")
    private Date outsourcingDate;
    
    /* 状态 out 委外，back 已回段 */
    @Column(name = "STATUS")
    private String status;
    
    /* 是否超范围登记 */
    @Column(name = "IS_IN_RANGE")
    private String isInRange;

    
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

    
    public Date getOutsourcingDate() {
        return outsourcingDate;
    }

    
    public void setOutsourcingDate(Date outsourcingDate) {
        this.outsourcingDate = outsourcingDate;
    }

    
    public String getOutsourcingFactory() {
        return outsourcingFactory;
    }

    
    public void setOutsourcingFactory(String outsourcingFactory) {
        this.outsourcingFactory = outsourcingFactory;
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

    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }

    
    public String getRepairContent() {
        return repairContent;
    }

    
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    
    public String getRownum() {
        return rownum;
    }

    
    public void setRownum(String rownum) {
        this.rownum = rownum;
    }

    
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
    
    public String getIsInRange() {
        return isInRange;
    }
    
    public void setIsInRange(String isInRange) {
        this.isInRange = isInRange;
    }
    
    
    
}
