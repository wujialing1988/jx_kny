
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
 * <li>说明：FixPlace实体类, 数据表：安装位置
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
@Table(name = "JXGC_FIX_PLACE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FixPlace implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 是否虚拟位置（1：是） */
    public static final int ISVIRTUAL = 1;
    
    /* 是否虚拟位置（0：否） */
    public static final int NOTVIRTUAL = 0;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 安装位置编码 */
    @Column(name = "FixPlace_Code")
    private String fixPlaceCode;
    
    /* 安装位置编码全名 */
    @Column(name = "FixPlace_FullCode")
    private String fixPlaceFullCode;
    
    /* 安装位置名称 */
    @Column(name = "FixPlace_Name")
    private String fixPlaceName;
    
    /* 安装位置全名 */
    @Column(name = "FixPlace_FullName")
    private String fixPlaceFullName;
    
    /* 安装位置序号 */
    @Column(name = "FixPlace_SEQ")
    private Integer fixPlaceSEQ;
    
    /* 安装位置简称 */
    @Column(name = "FIXPLACE_SHORTNAME")
    private String fixPlaceShortName;
    
    /* 图号 */
    @Column(name = "Chart_No")
    private String chartNo;
    
    /* 配件专业类型表主键 */
    @Column(name = "Professional_Type_IDX")
    private String professionalTypeIDX;
    
    /* 专业类型名称 */
    @Column(name = "Professional_Type_Name")
    private String professionalTypeName;
    
    /* 上级安装位置 */
    @Column(name = "Parent_Idx")
    private String parentIdx;
    
    /* 位置编码 */
    @Column(name = "Part_ID")
    private String partID;
    
    /* 位置名称 */
    @Column(name = "Part_Name")
    private String partName;
    
    /* 所属组成型号 */
    @Column(name = "Parts_BuildUp_Type_Idx")
    private String partsBuildUpTypeIdx;
    
    /* 是否虚拟位置（1：是；0：否） */
    @Column(name = "Is_Virtual")
    private Integer isVirtual;
    
    /* 备注 */
    private String remarks;
    
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
     * @return String 获取安装位置编码
     */
    public String getFixPlaceCode() {
        return fixPlaceCode;
    }
    
    /**
     * @param 设置安装位置编码
     */
    public void setFixPlaceCode(String fixPlaceCode) {
        this.fixPlaceCode = fixPlaceCode;
    }
    
    /**
     * @return String 获取安装位置编码全名
     */
    public String getFixPlaceFullCode() {
        return fixPlaceFullCode;
    }
    
    /**
     * @param 设置安装位置编码全名
     */
    public void setFixPlaceFullCode(String fixPlaceFullCode) {
        this.fixPlaceFullCode = fixPlaceFullCode;
    }
    
    /**
     * @return String 获取安装位置名称
     */
    public String getFixPlaceName() {
        return fixPlaceName;
    }
    
    /**
     * @param 设置安装位置名称
     */
    public void setFixPlaceName(String fixPlaceName) {
        this.fixPlaceName = fixPlaceName;
    }
    
    /**
     * @return String 获取安装位置名称全名
     */
    public String getFixPlaceFullName() {
        return fixPlaceFullName;
    }
    
    /**
     * @param 设置安装位置名称全名
     */
    public void setFixPlaceFullName(String fixPlaceFullName) {
        this.fixPlaceFullName = fixPlaceFullName;
    }
    
    /**
     * @return Integer 获取安装位置序号
     */
    public Integer getFixPlaceSEQ() {
        return fixPlaceSEQ;
    }
    
    /**
     * @param 设置安装位置序号
     */
    public void setFixPlaceSEQ(Integer fixPlaceSEQ) {
        this.fixPlaceSEQ = fixPlaceSEQ;
    }
    
    /**
     * @return String 获取图号
     */
    public String getChartNo() {
        return chartNo;
    }
    
    /**
     * @param 设置图号
     */
    public void setChartNo(String chartNo) {
        this.chartNo = chartNo;
    }
    
    /**
     * @return String 获取配件专业类型表主键
     */
    public String getProfessionalTypeIDX() {
        return professionalTypeIDX;
    }
    
    /**
     * @param 设置配件专业类型表主键
     */
    public void setProfessionalTypeIDX(String professionalTypeIDX) {
        this.professionalTypeIDX = professionalTypeIDX;
    }
    
    /**
     * @return String 获取专业类型名称
     */
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    /**
     * @param 设置专业类型名称
     */
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    /**
     * @return String 获取上级安装位置
     */
    public String getParentIdx() {
        return parentIdx;
    }
    
    /**
     * @param 设置上级安装位置
     */
    public void setParentIdx(String parentIdx) {
        this.parentIdx = parentIdx;
    }
    
    /**
     * @return String 获取位置编码
     */
    public String getPartID() {
        return partID;
    }
    
    /**
     * @param 设置位置编码
     */
    public void setPartID(String partID) {
        this.partID = partID;
    }
    
    /**
     * @return String 获取位置名称
     */
    public String getPartName() {
        return partName;
    }
    
    /**
     * @param 设置位置名称
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }
    
    /**
     * @return String 获取所属组成型号
     */
    public String getPartsBuildUpTypeIdx() {
        return partsBuildUpTypeIdx;
    }
    
    /**
     * @param 设置所属组成型号
     */
    public void setPartsBuildUpTypeIdx(String partsBuildUpTypeIdx) {
        this.partsBuildUpTypeIdx = partsBuildUpTypeIdx;
    }
    
    /**
     * @return Integer 获取是否虚拟位置
     */
    public Integer getIsVirtual() {
        return isVirtual;
    }
    
    /**
     * @param 设置是否虚拟位置
     */
    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }
    
    /**
     * @return String 获取备注
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * @param 设置备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * @return String 安装位置简称
     */
    public String getFixPlaceShortName() {
        return fixPlaceShortName;
    }
    
    /**
     * @param 设置安装位置简称
     */
    public void setFixPlaceShortName(String fixPlaceShortName) {
        this.fixPlaceShortName = fixPlaceShortName;
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
