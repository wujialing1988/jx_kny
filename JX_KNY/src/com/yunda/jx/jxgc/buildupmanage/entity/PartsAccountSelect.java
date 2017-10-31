package com.yunda.jx.jxgc.buildupmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TODO 此视图已删除，待确定无用后删除此类
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配置管理中配件选择视图实体类
 * <li>创建人：程锐
 * <li>创建日期：2013-2-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="V_JXGC_PARTSACCOUNT_SELECT")
public class PartsAccountSelect {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;   
    /* idx主键 */
    @Id
    private String idx;
    /* 配件型号表主键 */
    @Column(name="Parts_TYPE_IDX")
    private String partsTypeIDX;
    /* 配件名称 */
    @Column(name="Parts_Name")
    private String partsName;
    /* 规格型号 */
    @Column(name="Specification_Model")
    private String specificationModel;
    /* 配件铭牌号 */
    @Column(name="Nameplate_No")
    private String nameplateNo;
    /* 配件编号 */
    @Column(name="Parts_NO")
    private String partsNo;
    /* 固定资产编号 */
    @Column(name="FIXED_ASSETS_NO")
    private String fixedAssetsNo;
    /* 固定资产名称 */
    @Column(name="FIXED_ASSETS_NAME")
    private String fixedAssetsName;
    /* 固定资产卡片号 */
    @Column(name="FIXED_ASSETS_CARDNO")
    private String fixedAssetsCardno;
    /* 资产状态，其值来源于配件状态表 */
    @Column(name="Assets_Staus")
    private Integer assetsStaus;
    /* 上配件状态，其值来源于配件状态表 */
    @Column(name="Install_Parts_Status")
    private Integer installPartsStatus;
    /* 上车状态，其值来源于配件状态表 */
    @Column(name="Install_Train_Status")
    private Integer installTrainStatus;
    /* 库存状态，10 11 12 20  */
    @Column(name="WareHouse_Status")
    private Integer warehouseStatus;
    /* 健康状态，其值来源配件状态表 */
    @Column(name="Health_Status")
    private Integer healthStatus;
    /* 周转状态，其值来源于配件状态表 */
    @Column(name="Turnover_Status")
    private Integer turnoverStatus;
    /* 所属单位 */
    @Column(name="Owner_Unit")
    private Long ownerUnit;
    /* 所属单位名称 */
    @Column(name="Owner_Unit_Name")
    private String ownerUnitName;
    /* 使用单位 */
    @Column(name="Use_Unit")
    private Long useUnit;
    /* 使用单位名称 */
    @Column(name="Use_Unit_Name")
    private String useUnitName;
    /* 出厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Leave_Date")
    private java.util.Date leaveDate;
    /* 交付使用日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Use_Date")
    private java.util.Date useDate;
    /* 价格 */
    private Double price;
    /* 组成型号主键*/
    @Column(name="BUILDUP_TYPE_IDX")
    private String buildupTypeIdx;
    /* 组成型号编码 */
    @Column(name="BUILDUP_TYPE_CODE")
    private String buildupTypeCode;
    /* 组成型号名称 */
    @Column(name="BUILDUP_TYPE_NAME")
    private String buildupTypeName;
    /* 登记人主键 */
    private Long empid;
    /* 登记人名称 */
    private String empname;
    /* 登记时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Register_Time")
    private java.util.Date registerTime;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    
    /* 已走行公里 */
    @Column(name="Achieve_KM")
    private Double achieveKM;
    /* 已使用年限 */
    @Column(name="Achieve_YEAR")
    private Double achieveYEAR;
    /* 备注 */
    private String remarks;
   
    /* 制造厂主键 */
    @Column(name="MADE_FACTORY_IDX")
    private String madeFactoryIdx;
    /* 制造厂名 */
    @Column(name="MADE_FACTORY_NAME")
    private String madeFactoryName;
    /* 专业类型表主键*/
    @Column(name="PROFESSIONAL_TYPE_IDX")
    private String professionalTypeIdx;
    /* 专业类型名称*/
    @Column(name="PROFESSIONAL_TYPE_NAME")
    private String professionalTypeName;
    
    /**
     * @return String 获取配件型号表主键
     */
    public String getPartsTypeIDX(){
        return partsTypeIDX;
    }
    /**
     * @param 设置配件型号表主键
     */
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    /**
     * @return String 获取配件名称
     */
    public String getPartsName(){
        return partsName;
    }
    /**
     * @param 设置配件名称
     */
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    /**
     * @return String 获取规格型号
     */
    public String getSpecificationModel(){
        return specificationModel;
    }
    /**
     * @param 设置规格型号
     */
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    /**
     * @return String 获取配件铭牌号
     */
    public String getNameplateNo(){
        return nameplateNo;
    }
    /**
     * @param 设置配件铭牌号
     */
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
    /**
     * @return String 获取配件编号
     */
    public String getPartsNo(){
        return partsNo;
    }
    /**
     * @param 设置配件编号
     */
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    /**
     * @return String 获取固定资产编号
     */
    public String getFixedAssetsNo(){
        return fixedAssetsNo;
    }
    /**
     * @param 设置固定资产编号
     */
    public void setFixedAssetsNo(String fixedAssetsNo) {
        this.fixedAssetsNo = fixedAssetsNo;
    }
    /**
     * @return String 获取固定资产名称
     */
    public String getFixedAssetsName(){
        return fixedAssetsName;
    }
    /**
     * @param 设置固定资产名称
     */
    public void setFixedAssetsName(String fixedAssetsName) {
        this.fixedAssetsName = fixedAssetsName;
    }
    /**
     * @return String 获取固定资产卡片号
     */
    public String getFixedAssetsCardno(){
        return fixedAssetsCardno;
    }
    /**
     * @param 设置固定资产卡片号
     */
    public void setFixedAssetsCardno(String fixedAssetsCardno) {
        this.fixedAssetsCardno = fixedAssetsCardno;
    }
    /**
     * @return Integer 获取资产状态
     */
    public Integer getAssetsStaus(){
        return assetsStaus;
    }
    /**
     * @param 设置资产状态
     */
    public void setAssetsStaus(Integer assetsStaus) {
        this.assetsStaus = assetsStaus;
    }
    /**
     * @return Integer 获取上配件状态
     */
    public Integer getInstallPartsStatus(){
        return installPartsStatus;
    }
    /**
     * @param 设置上配件状态
     */
    public void setInstallPartsStatus(Integer installPartsStatus) {
        this.installPartsStatus = installPartsStatus;
    }
    /**
     * @return Integer 获取上车状态
     */
    public Integer getInstallTrainStatus(){
        return installTrainStatus;
    }
    /**
     * @param 设置上车状态
     */
    public void setInstallTrainStatus(Integer installTrainStatus) {
        this.installTrainStatus = installTrainStatus;
    }
    /**
     * @return Integer 获取库存状态
     */
    public Integer getWarehouseStatus(){
        return warehouseStatus;
    }
    /**
     * @param 设置库存状态
     */
    public void setWarehouseStatus(Integer warehouseStatus) {
        this.warehouseStatus = warehouseStatus;
    }
    /**
     * @return Integer 获取健康状态
     */
    public Integer getHealthStatus(){
        return healthStatus;
    }
    /**
     * @param 设置健康状态
     */
    public void setHealthStatus(Integer healthStatus) {
        this.healthStatus = healthStatus;
    }
    /**
     * @return Integer 获取周转状态
     */
    public Integer getTurnoverStatus(){
        return turnoverStatus;
    }
    /**
     * @param 设置周转状态
     */
    public void setTurnoverStatus(Integer turnoverStatus) {
        this.turnoverStatus = turnoverStatus;
    }
    /**
     * @return Long 获取所属单位
     */
    public Long getOwnerUnit(){
        return ownerUnit;
    }
    /**
     * @param 设置所属单位
     */
    public void setOwnerUnit(Long ownerUnit) {
        this.ownerUnit = ownerUnit;
    }
    /**
     * @return String 获取所属单位名称
     */
    public String getOwnerUnitName(){
        return ownerUnitName;
    }
    /**
     * @param 设置所属单位名称
     */
    public void setOwnerUnitName(String ownerUnitName) {
        this.ownerUnitName = ownerUnitName;
    }
    /**
     * @return Long 获取使用单位
     */
    public Long getUseUnit(){
        return useUnit;
    }
    /**
     * @param 设置使用单位
     */
    public void setUseUnit(Long useUnit) {
        this.useUnit = useUnit;
    }
    /**
     * @return String 获取使用单位名称
     */
    public String getUseUnitName(){
        return useUnitName;
    }
    /**
     * @param 设置使用单位名称
     */
    public void setUseUnitName(String useUnitName) {
        this.useUnitName = useUnitName;
    }
    
    public String getMadeFactoryIdx() {
        return madeFactoryIdx;
    }
    public void setMadeFactoryIdx(String madeFactoryIdx) {
        this.madeFactoryIdx = madeFactoryIdx;
    }
    public String getMadeFactoryName() {
        return madeFactoryName;
    }
    public void setMadeFactoryName(String madeFactoryName) {
        this.madeFactoryName = madeFactoryName;
    }
    /**
     * @return java.util.Date 获取出厂日期
     */
    public java.util.Date getLeaveDate(){
        return leaveDate;
    }
    /**
     * @param 设置出厂日期
     */
    public void setLeaveDate(java.util.Date leaveDate) {
        this.leaveDate = leaveDate;
    }
    /**
     * @return java.util.Date 获取交付使用日期
     */
    public java.util.Date getUseDate(){
        return useDate;
    }
    /**
     * @param 设置交付使用日期
     */
    public void setUseDate(java.util.Date useDate) {
        this.useDate = useDate;
    }
    /**
     * @return Double 获取价格
     */
    public Double getPrice(){
        return price;
    }
    /**
     * @param 设置价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getBuildupTypeCode() {
        return buildupTypeCode;
    }
    
    public void setBuildupTypeCode(String buildupTypeCode) {
        this.buildupTypeCode = buildupTypeCode;
    }
    
    public String getBuildupTypeIdx() {
        return buildupTypeIdx;
    }
    
    public void setBuildupTypeIdx(String buildupTypeIdx) {
        this.buildupTypeIdx = buildupTypeIdx;
    }
    
    public String getBuildupTypeName() {
        return buildupTypeName;
    }
    
    public void setBuildupTypeName(String buildupTypeName) {
        this.buildupTypeName = buildupTypeName;
    }
    /**
     * @return Long 获取登记人主键
     */
    public Long getEmpid(){
        return empid;
    }
    /**
     * @param 设置登记人主键
     */
    public void setEmpid(Long empid) {
        this.empid = empid;
    }
    /**
     * @return String 获取登记人名称
     */
    public String getEmpname(){
        return empname;
    }
    /**
     * @param 设置登记人名称
     */
    public void setEmpname(String empname) {
        this.empname = empname;
    }
    /**
     * @return java.util.Date 获取登记时间
     */
    public java.util.Date getRegisterTime(){
        return registerTime;
    }
    /**
     * @param 设置登记时间
     */
    public void setRegisterTime(java.util.Date registerTime) {
        this.registerTime = registerTime;
    }
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus(){
        return recordStatus;
    }
    /**
     * @param 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return Double 获取已走行公里
     */
    public Double getAchieveKM(){
        return achieveKM;
    }
    /**
     * @param 设置已走行公里
     */
    public void setAchieveKM(Double achieveKM) {
        this.achieveKM = achieveKM;
    }
    /**
     * @return Double 获取已使用年限
     */
    public Double getAchieveYEAR(){
        return achieveYEAR;
    }
    /**
     * @param 设置已使用年限
     */
    public void setAchieveYEAR(Double achieveYEAR) {
        this.achieveYEAR = achieveYEAR;
    }
    /**
     * @return String 获取备注
     */
    public String getRemarks(){
        return remarks;
    }
    /**
     * @param 设置备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    /**
     * @param 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
}
