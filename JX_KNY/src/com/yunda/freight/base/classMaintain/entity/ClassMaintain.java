package com.yunda.freight.base.classMaintain.entity;

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
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次维护实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-13 16:30:29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_CLASS_MAINTAIN")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ClassMaintain implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 工作地点标识码 */ 
    @Column(name = "WORKPLACE_CODE")
    private java.lang.String workplaceCode;            
            
    /* 工作地点名称 */ 
    @Column(name = "WORKPLACE_NAME")
    private java.lang.String workplaceName;            
            
    /* 班次编码 */ 
    @Column(name = "CLASS_NO")
    private java.lang.String classNo;            
            
    /* 班次名称 */ 
    @Column(name = "CLASS_NAME")
    private java.lang.String className;            
            
    /* 排序号 */ 
    @Column(name = "SEQ_NO")
    private java.lang.Long seqNo;            
            
    /* 备注 */ 
    @Column(name = "REMARK")
    private java.lang.String remark;            
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
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
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getWorkplaceCode() {
        return this.workplaceCode;
    }
    
    public void setWorkplaceCode(java.lang.String value) {
        this.workplaceCode = value;
    }
    public java.lang.String getWorkplaceName() {
        return this.workplaceName;
    }
    
    public void setWorkplaceName(java.lang.String value) {
        this.workplaceName = value;
    }
    public java.lang.String getClassNo() {
        return this.classNo;
    }
    
    public void setClassNo(java.lang.String value) {
        this.classNo = value;
    }
    public java.lang.String getClassName() {
        return this.className;
    }
    
    public void setClassName(java.lang.String value) {
        this.className = value;
    }
    public java.lang.Long getSeqNo() {
        return this.seqNo;
    }
    
    public void setSeqNo(java.lang.Long value) {
        this.seqNo = value;
    }
    public java.lang.String getRemark() {
        return this.remark;
    }
    
    public void setRemark(java.lang.String value) {
        this.remark = value;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
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

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
    
}

