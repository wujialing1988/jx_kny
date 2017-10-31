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
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsExtendNo实体类, 数据表：配件扩展编号
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
@Table(name = "PJWZ_Parts_ExtendNo")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsExtendNo implements java.io.Serializable {
    
    /** 是否启用-已启用：是 */
    public static final String IS_USED = "是";
    
    /** 是否启用-已启用：否 */
    public static final String NO_USED = "否";
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /** 配件型号表主键 */
    @Column(name = "PARTS_TYPE_IDX")
    private String partsTypeIDX;
    
    /** 对应扩展编号字段 */
    @Column(name = "ExtendNo_Field")
    private String extendNoField;
    
    /** 扩展编号名称 */
    @Column(name = "ExtendNo_Name")
    private String extendNoName;
    
    /** 是否启用(是、否) 
     * <li> 是:<code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.IS_USED</code>
     * <li> 否:<code>com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType.NO_USED</code> 
     */
    private String isUsed;
    
    /** 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /** 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", updatable = false)
    private java.util.Date createTime;
    
    /** 修改人 */
    private Long updator;
    
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
    /**
     * @return
     * @return true 获取配件型号表主键
     */
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    /**
     * @param 设置配件型号表主键
     */
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    /**
     * @return String 获取对应扩展编号字段
     */
    public String getExtendNoField() {
        return extendNoField;
    }
    
    /**
     * @param 设置对应扩展编号字段
     */
    public void setExtendNoField(String extendNoField) {
        this.extendNoField = extendNoField;
    }
    
    /**
     * @return String 获取扩展编号名称
     */
    public String getExtendNoName() {
        return extendNoName;
    }
    
    /**
     * @param 设置扩展编号名称
     */
    public void setExtendNoName(String extendNoName) {
        this.extendNoName = extendNoName;
    }
    
    /**
     * @return String 获取是否启用
     */
    public String getIsUsed() {
        return isUsed;
    }
    
    /**
     * @param 设置是否启用
     */
    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
    
    /**
     * @return Integer 获取记录的状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param 设置记录的状态
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
     * @param 设置站点标识
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
     * @param 设置创建人
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
     * @param 设置创建时间
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
     * @param 设置修改人
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
     * @param 设置修改时间
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
     * @param 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
}
