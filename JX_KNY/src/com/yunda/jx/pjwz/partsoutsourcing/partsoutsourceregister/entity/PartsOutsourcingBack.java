package com.yunda.jx.pjwz.partsoutsourcing.partsoutsourceregister.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 已返回委外记录
 * <li>创建人：程梅
 * <li>创建日期：2016-6-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PartsOutsourcingBack implements Serializable {
    
    private String partsName;// 配件名称
    
    private String specificationModel;// 规格规格型号
    
    private String partsOutNo;// 配件委外编号
    
    private String partsBackNo;// 配件返回编号
    
    /* 配件铭牌号 */
    private String nameplateNo;
    
    private String outsourcingFactoryId;// 委外厂家编码
    
    private String outsourcingFactory;// 委外厂家
    
    private String outsourcingReasion;// 委外检修原因
    
    private String repairContent;// 修理内容
    
    @Temporal(TemporalType.DATE)
    private Date outsourcingDate;// 委外日期
    
    @Temporal(TemporalType.DATE)
    private Date backDate;// 返回日期
    
    private Integer takeOverEmpId;// 接收人主键
    
    private String takeOverEmp;// 接收人
    
    private String takeOverOrgId;// 接收部门主键
    
    private String takeOverOrg;// 接收部门
    
    /* 配件识别码 */
    private String identificationCode;
    
    /* 配件返回识别码 */
    private String identificationCodeBack;
    
    /* 接收部门序列(存储组织机构的orgseq字段) */
    private String takeOverOrgSeq;
    public PartsOutsourcingBack(){
        super();
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
    
    public Date getBackDate() {
        return backDate;
    }
    
    public void setBackDate(Date backDate) {
        this.backDate = backDate;
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
    
    public String getRepairContent() {
        return repairContent;
    }
    
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
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
    
    public String getOutsourcingFactory() {
        return outsourcingFactory;
    }
    
    public void setOutsourcingFactory(String outsourcingFactory) {
        this.outsourcingFactory = outsourcingFactory;
    }
    
    public String getIdentificationCodeBack() {
        return identificationCodeBack;
    }
    
    public void setIdentificationCodeBack(String identificationCodeBack) {
        this.identificationCodeBack = identificationCodeBack;
    }
    
    public String getNameplateNo() {
        return nameplateNo;
    }
    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
    
}
