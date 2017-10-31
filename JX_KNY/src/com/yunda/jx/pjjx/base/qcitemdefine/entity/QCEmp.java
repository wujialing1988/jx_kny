package com.yunda.jx.pjjx.base.qcitemdefine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCEmp实体 数据表：质量检查人员
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:39:01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_QC_Emp")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class QCEmp implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 质量检查项主键 */
	@Column(name = "QC_Item_IDX")
	private String qCItemIDX;

	/** 检查人员 */
	@Column(name = "Check_EmpID")
	private Long checkEmpID;

	/** 检查人员名称 */
	@Column(name = "Check_EmpName")
	private String checkEmpName;

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
    
    /** 检查班组的组合字段字面值（多个班组之间用逗号分隔） */
    @Transient
    private String checkOrg;
	
	/**
	 * Default Constructor
	 */
	public QCEmp() {
		super();
	}

	public Long getCheckEmpID() {
		return checkEmpID;
	}

	public void setCheckEmpID(Long checkEmpID) {
		this.checkEmpID = checkEmpID;
	}

	public String getCheckEmpName() {
		return checkEmpName;
	}

	public void setCheckEmpName(String checkEmpName) {
		this.checkEmpName = checkEmpName;
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

	public String getQCItemIDX() {
		return qCItemIDX;
	}

	public void setQCItemIDX(String itemIDX) {
		qCItemIDX = itemIDX;
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

    
    public String getCheckOrg() {
        return checkOrg;
    }
    
    public void setCheckOrg(String checkOrg) {
        this.checkOrg = checkOrg;
    }
	
}