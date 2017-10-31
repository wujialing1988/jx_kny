package com.yunda.jx.jxgc.base.tpqcitemdefine.entity;

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
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 提票质量检查范围基础配置实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-6-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_TPQC_ITEM_EMP_ORG_DEFINE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TPQCItemEmpOrgDefine implements java.io.Serializable {
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /** 质量检查人员idx主键 */
    @Column(name = "QC_Emp_IDX")
    private String qcEmpIDX;
    
    /** 检查机构ID */
    @Column(name = "Check_OrgID")
    private Long checkOrgID;
    
    /** 检查机构名称 */
    @Column(name = "Check_OrgName")
    private String checkOrgName;
    
    /** 检查机构序列 */
    @Column(name = "Check_OrgSeq")
    private String checkOrgSeq;
    
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
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-6-25
     * <li>修改人：
     * <li>修改日期：
     */
    public TPQCItemEmpOrgDefine() {
        super();
    }
    
    public Long getCheckOrgID() {
        return checkOrgID;
    }
    
    public void setCheckOrgID(Long checkOrgID) {
        this.checkOrgID = checkOrgID;
    }
    
    public String getCheckOrgName() {
        return checkOrgName;
    }
    
    public void setCheckOrgName(String checkOrgName) {
        this.checkOrgName = checkOrgName;
    }
    
    public String getCheckOrgSeq() {
        return checkOrgSeq;
    }
    
    public void setCheckOrgSeq(String checkOrgSeq) {
        this.checkOrgSeq = checkOrgSeq;
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
    
    public String getQcEmpIDX() {
        return qcEmpIDX;
    }
    
    public void setQcEmpIDX(String qcEmpIDX) {
        this.qcEmpIDX = qcEmpIDX;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
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
    
}
