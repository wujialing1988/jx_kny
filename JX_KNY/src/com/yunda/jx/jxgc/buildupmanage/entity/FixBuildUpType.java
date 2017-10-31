
package com.yunda.jx.jxgc.buildupmanage.entity;

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
 * <li>说明：FixBuildUpType实体类, 数据表：可安装组成型号
 * <li>创建人：程锐
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_FIX_BuildUp_Type")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FixBuildUpType implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    //是否缺省
    public static final int DEFAULT = 1;
    public static final int NODEFAULT = 0;
    public static final String DEFAULT_MEAN = "是";
    public static final String NODEFAULT_MEAN = "否";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 安装位置主键 */
    @Column(name = "Fix_Place_Idx")
    private String fixPlaceIdx;
    
    /* 可安装组成型号主键 */
    @Column(name = "BuildUp_Type_Idx")
    private String buildUpTypeIdx;
    
    /* 是否缺省（1：是；0：否） */
    @Column(name = "Is_Default")
    private Integer isDefault;
    
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
    
    /**
     * @return String 获取安装位置主键
     */
    public String getFixPlaceIdx() {
        return fixPlaceIdx;
    }
    
    /**
     * @param 设置安装位置主键
     */
    public void setFixPlaceIdx(String fixPlaceIdx) {
        this.fixPlaceIdx = fixPlaceIdx;
    }
    
    /**
     * @return String 获取可安装组成型号主键
     */
    public String getBuildUpTypeIdx() {
        return buildUpTypeIdx;
    }
    
    /**
     * @param 设置可安装组成型号主键
     */
    public void setBuildUpTypeIdx(String buildUpTypeIdx) {
        this.buildUpTypeIdx = buildUpTypeIdx;
    }
    
    /**
     * @return Integer 获取是否缺省
     */
    public Integer getIsDefault() {
        return isDefault;
    }
    
    /**
     * @param 设置是否缺省
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param 设置记录状态
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
