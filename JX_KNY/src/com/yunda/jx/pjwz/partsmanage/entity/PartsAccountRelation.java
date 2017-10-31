package com.yunda.jx.pjwz.partsmanage.entity;

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
 * <li>说明: 配件信息关系
 * <li>创建人：何涛
 * <li>创建日期：2015-3-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_ACCOUNT_RELATION")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsAccountRelation implements java.io.Serializable {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /** 配件信息主键 */
    @Column(name="PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    /** 上级配件信息主键 */
    @Column(name="PARENT_PARTS_ACCOUNT_IDX")
    private String parentPartsAccountIDX;
    
    /** 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /** 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /** 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /** 修改人 */
    private Long updator;
    
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /**
     * <li>说明：构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * @param partsAccountIDX 配件信息主键
     * @param parentPartsAccountIDX 上级配件信息主键
     */
    public PartsAccountRelation(String partsAccountIDX, String parentPartsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
        this.parentPartsAccountIDX = parentPartsAccountIDX;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-3-2
     * <li>修改人： 
     * <li>修改日期：
     */
    public PartsAccountRelation() {
        super();
    }

    /**
     * @return 获取上级配件信息主键
     */
    public String getParentPartsAccountIDX() {
        return parentPartsAccountIDX;
    }
    
    /**
     * @param parentPartsAccountIDX 设置上级配件信息主键
     */
    public void setParentPartsAccountIDX(String parentPartsAccountIDX) {
        this.parentPartsAccountIDX = parentPartsAccountIDX;
    }
    
    /**
     * @return 获取配件信息主键
     */
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }
    
    /**
     * @param partsAccountIDX 设置配件信息主键
     */
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
    
    /**
     * @return 获取创建时间
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
     * @return 获取创建人
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
     * @return 获取主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @param idx 设置主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @return 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return 获取站点ID
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置站点ID
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return 获取修改时间
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
     * @return 获取修改人
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
    
}
