package com.yunda.jx.jxgc.workhis.partslist.entity;

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
 * <li>说明：视图PJWZ_PARTS_UNLOAD_REGISTER_HIS实体类, 数据表：配件下车信息历史查询视图
 * <li>创建人：程梅
 * <li>创建日期：2016年1月21日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_UNLOAD_REGISTER_HIS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsUnloadRegisterHis implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    /* 兑现单id */
    @Column(name="RDP_IDX")
    private String rdpIdx;
    /* 规格型号 */
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;
    
    /* 配件名称 */
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /* 配件编号 */
    @Column(name = "PARTS_NO")
    private String partsNo;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 下车位置 */
    @Column(name = "UNLOAD_Place")
    private String unloadPlace;
    
    /* 下车原因 */
    @Column(name = "UNLOAD_Reason")
    private String unloadReason;
    
    /* 下车日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UNLOAD_DATE")
    private java.util.Date unloadDate;
    
    /* 检修需求描述 */
    @Column(name = "WP_Desc")
    private String wpDesc;
    
    /* 配件状态名称 */
    @Column(name = "Parts_Status_Name")
    private String partsStatusName;
    
    /* 配件信息主键 */
    @Column(name="PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
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
    
    /* 配件兑现单IDX */
    @Column(name = "PARTSRDPIDX")
    private String partsRdpIDX;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
    /**
     * @return String 获取规格型号
     */
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    /**
     * @param specificationModel 设置规格型号
     */
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
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
    
    /**
     * @return String 获取配件编号
     */
    public String getPartsNo() {
        return partsNo;
    }
    
    /**
     * @param partsNo 设置配件编号
     */
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    
    /**
     * @return String 获取下车位置
     */
    public String getUnloadPlace() {
        return unloadPlace;
    }
    
    /**
     * @param unloadPlace 设置下车位置
     */
    public void setUnloadPlace(String unloadPlace) {
        this.unloadPlace = unloadPlace;
    }
    
    /**
     * @return String 获取下车原因
     */
    public String getUnloadReason() {
        return unloadReason;
    }
    
    /**
     * @param unloadReason 设置下车原因
     */
    public void setUnloadReason(String unloadReason) {
        this.unloadReason = unloadReason;
    }
    
    /**
     * @return java.util.Date 获取下车日期
     */
    public java.util.Date getUnloadDate() {
        return unloadDate;
    }
    
    /**
     * @param unloadDate 设置下车日期
     */
    public void setUnloadDate(java.util.Date unloadDate) {
        this.unloadDate = unloadDate;
    }
    
    /**
     * @return Integer 获取记录的状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录的状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    /**
     * @param creator 设置创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    /**
     * @param createTime 设置创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator() {
        return updator;
    }
    
    /**
     * @param updator 设置修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    /**
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置修改时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getWpDesc() {
        return wpDesc;
    }
    
    public void setWpDesc(String wpDesc) {
        this.wpDesc = wpDesc;
    }
    
    public String getPartsStatusName() {
        return partsStatusName;
    }
    
    public void setPartsStatusName(String partsStatusName) {
        this.partsStatusName = partsStatusName;
    }

    
    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }

	public String getPartsRdpIDX() {
		return partsRdpIDX;
	}

	public void setPartsRdpIDX(String partsRdpIDX) {
		this.partsRdpIDX = partsRdpIDX;
	}

    
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }

    
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
}
