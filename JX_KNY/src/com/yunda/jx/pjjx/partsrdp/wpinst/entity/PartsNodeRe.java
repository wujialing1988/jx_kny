package com.yunda.jx.pjjx.partsrdp.wpinst.entity;

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
 * <li>说明: 返修节点
 * <li>创建人：程锐
 * <li>创建日期：2016-2-1
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Entity
@Table(name = "pjjx_rdp_node_re")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsNodeRe implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 检修作业流程节点主键 */
    @Column(name = "Rdp_Node_IDX")
    private String rdpNodeIDX;
    
    /* 提交人 */
    @Column(name = "SUBMIT_EMP_ID")
    private String submitEmpID;
    
    /* 提交人名称 */
    @Column(name = "SUBMIT_EMP_NAME")
    private String submitEmpName;
    
    /* 返修原因 */
    @Column(name = "REBACK_CAUSE")
    private String rebackCause;
    
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
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
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
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getRdpNodeIDX() {
        return rdpNodeIDX;
    }
    
    public void setRdpNodeIDX(String rdpNodeIDX) {
        this.rdpNodeIDX = rdpNodeIDX;
    }
    
    public String getRebackCause() {
        return rebackCause;
    }
    
    public void setRebackCause(String rebackCause) {
        this.rebackCause = rebackCause;
    }
    
    public String getSubmitEmpID() {
        return submitEmpID;
    }
    
    public void setSubmitEmpID(String submitEmpID) {
        this.submitEmpID = submitEmpID;
    }
    
    public String getSubmitEmpName() {
        return submitEmpName;
    }
    
    public void setSubmitEmpName(String submitEmpName) {
        this.submitEmpName = submitEmpName;
    }
    
}
