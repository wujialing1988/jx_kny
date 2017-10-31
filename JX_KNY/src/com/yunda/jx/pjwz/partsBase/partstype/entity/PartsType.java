package com.yunda.jx.pjwz.partsBase.partstype.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsType实体类, 数据表：互换配件型号表
 * <li>创建人：程锐
 * <li>创建日期：2014-08-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_Parts_Type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsType implements java.io.Serializable {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 高价互换配件 - 是: 1 */
    public static final int IS_HEIGHT = 1;
    /** 高价互换配件 - 不是： 0 */
    public static final int NO_HEIGHT = 0;
    
    /** 状态 - 新增 */
    public static final int STATUS_ADD = 0;
    /** 状态 - 作废 */
    public static final int STATUS_INVALID = 2;
    /** 状态 - 启用 */
    public static final int STATUS_USE = 1;
    
    /**
     * idx主键 
     */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator = "uuid_id_generator")
    private String idx;

    /** 图号 */
    @Column(name = "CHART_NO")
    private String chartNo;
    
    /** 配件分类名称 */
    @Column(name = "Class_Name")
    private String className;
    
    /** 是否带序列号 0:不是 1：是 */
    @Column(name = "IS_HAS_SEQ")
    private Integer isHasSeq;
    
    /**
     * 是否高价互换配件 
     * <li> 不是: 0 <code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.NO_HEIGHT</code>
     * <li> 是: 1 <code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.IS_HEIGHT</code>
     */
    @Column(name = "IS_HIGHTER_PRICED_PARTS")
    private Integer isHighterPricedParts;
    
    /**
     * 是否额外放行
     * 0 不是，默认，即需要按照正常的流程走，例如上车的配件必须先下车
     * 1 是，可以不按照正常流程，如不下车时也可进行上车
     */
    @Column(name = "IS_EXTRA")
    private Integer isExtra;
    
    /** 最大走行公里 */
    @Column(name = "LIMIT_KM")
    private String limitKm;
    
    /** 最大使用年限 */
    @Column(name = "LIMIT_YEARS")
    private String limitYears;
    
    /** 物料编码 */
    @Column(name = "Mat_Code")
    private String matCode;
    
    /** 配件分类表主键 */
    @Column(name = "Parts_Class_IDX")
    private String partsClassIdx;
    
    /** 配件名称 */
    @Column(name = "Parts_NAME")
    private String partsName;
    
    /** 专业类型表主键 */
    @Column(name = "Professional_Type_IDX")
    private String professionalTypeIdx;
    
    /** 专业类型名称 */
    @Column(name = "Professional_Type_Name")
    private String professionalTypeName;
    
    /** 规格型号 */
    @Column(name = "Specification_Model")
    private String specificationModel;

    /** 规格型号编码 */
    @Column(name = "Specification_Model_Code")
    private String specificationModelCode;
    
    /** 业务状态，0：新增；1：启用；2：作废 */
    @Column(name = "status")
    private Integer status;
    
    /** 最大库存期限 */
    @Column(name = "Time_Limit")
    private String timeLimit;
    
    /** 计量单位 */
    @Column(name = "UNIT")
    private String unit;

    /** 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="RECORD_STATUS")
    private Integer recordStatus;
    
    /** 站点标识，为了同步数据而使用 */
    @Column(updatable=false)
    private String siteID;
    
    /** 创建人 */
    @Column(updatable=false)
    private Long creator;
    
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_TIME",updatable=false)
    private java.util.Date createTime;
    
    /** 修改人 */
    private Long updator;
    
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATE_TIME")
    private java.util.Date updateTime;
    
    /** 机车零部件编码 **/
    @Column(name = "JCPJBM")
    private String jcpjbm;
    
    
    
    /**
     * @return 获取图号
     */
    public String getChartNo() {
        return chartNo;
    }
    
    /**
     * @return String 获取配件分类名称
     */
    public String getClassName() {
        return className;
    }
    
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @return 获取是否带序列号 0:不是 1：是 
     */
    public Integer getIsHasSeq() {
        return isHasSeq;
    }
    
    /**
     * @return 获取是否高价互换配件 
     * <li> 不是: 0 <code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.noHeight</code> 
     * <li> 是: 1 <code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.isHeight</code>
     */
    public Integer getIsHighterPricedParts() {
        return isHighterPricedParts;
    }
    
    /**
     * @return 获取最大走行公里
     */
    public String getLimitKm() {
        return limitKm;
    }
    
    /**
     * @return 获取最大使用年限
     */
    public String getLimitYears() {
        return limitYears;
    }
    
    /**
     * @return 获取物料编码
     */
    public String getMatCode() {
        return matCode;
    }
    
    /**
     * @return 获取配件分类表主键
     */
    public String getPartsClassIdx() {
        return partsClassIdx;
    }
    
    /**
     * @return 获取配件名称
     */
    public String getPartsName() {
        return partsName;
    }
    
    /**
     * @return 获取专业类型表主键
     */
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }
    
    /**
     * @return 获取专业类型名称
     */
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @return 获取规格型号
     */
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    /**
     * @return 获取规格型号编码
     */
    public String getSpecificationModelCode() {
        return specificationModelCode;
    }
    
    /**
     * @return Integer 获取状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * @return 获取最大库存期限
     */
    public String getTimeLimit() {
        return timeLimit;
    }
    
    /**
     * @return 获取计量单位
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator() {
        return updator;
    }
    
    /**
     * @param chartNo 设置图号
     */
    public void setChartNo(String chartNo) {
        this.chartNo = chartNo;
    }
    
    /**
     * @param className 设置配件分类名称
     */
    public void setClassName(String className) {
        this.className = className;
    }
    
    /**
     * @param createTime 设置创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @param creator 设置创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }

    /**
     * @param isHasSeq 设置是否带序列号 0:不是 1：是 
     */
    public void setIsHasSeq(Integer isHasSeq) {
        this.isHasSeq = isHasSeq;
    }
    
    /**
     * @param isHasSeq 设置是否高价互换配件
     * <li> 不是: 0 <code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.noHeight</code> 
     * <li> 是: 1 <code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.isHeight</code>
     */
    public void setIsHighterPricedParts(Integer isHighterPricedParts) {
        this.isHighterPricedParts = isHighterPricedParts;
    }
    
    /**
     * @param limitKm 设置最大走行公里
     */
    public void setLimitKm(String limitKm) {
        this.limitKm = limitKm;
    }
    
    /**
     * @param limitYears 设置最大使用年限
     */
    public void setLimitYears(String limitYears) {
        this.limitYears = limitYears;
    }
    
    /**
     * @param matCode 设置物料编码
     */
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    
    /**
     * @param partsClassIdx 设置配件分类表主键
     */
    public void setPartsClassIdx(String partsClassIdx) {
        this.partsClassIdx = partsClassIdx;
    }
    
    /**
     * @param partsName 设置配件名称
     */
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    /**
     * @param professionalTypeIdx 设置专业类型表主键
     */
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }
    
    /**
     * @param professionalTypeName 设置专业类型名称 
     */
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    /**
     * @param recordStatus 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @param siteID 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @param specificationModel 设置规格型号
     */
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    /**
     * @param specificationModelCode 设置规格型号编码
     */
    public void setSpecificationModelCode(String specificationModelCode) {
        this.specificationModelCode = specificationModelCode;
    }
    
    /**
     * @param status 设置状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @param timeLimit 设置最大库存期限
     */
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    /**
     * @param unit 设置计量单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    /**
     * @param updateTime 设置修改时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    /**
     * @param updator 设置修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

	public String getJcpjbm() {
		return jcpjbm;
	}

	public void setJcpjbm(String jcpjbm) {
		this.jcpjbm = jcpjbm;
	}

    
    public Integer getIsExtra() {
        return isExtra;
    }

    
    public void setIsExtra(Integer isExtra) {
        this.isExtra = isExtra;
    }
    
    
    
}
