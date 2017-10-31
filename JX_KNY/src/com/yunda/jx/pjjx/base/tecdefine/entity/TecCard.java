package com.yunda.jx.pjjx.base.tecdefine.entity;

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
 * <li>说明: TecCard实体 数据表：配件检修工艺卡
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:06:07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_Tec_Card")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TecCard implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 工艺卡编号 */
	@Column(name = "Tec_Card_No")
	private String tecCardNo;

	/** 工艺卡描述 */
	@Column(name = "Tec_Card_Desc")
	private String tecCardDesc;

	/** 顺序号 */
	@Column(name = "Seq_No")
	private Integer seqNo;

	/** 检修工艺主键 */
	@Column(name = "Tec_IDX")
	private String tecIDX;

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
	
	/** 工艺编号 */
	@Transient
	private String tecNo;

	/** 工艺名称 */
	@Transient
	private String tecName;

	/** 工艺描述 */
	@Transient
	private String tecDesc;
	
	/**
	 * Default Constructor
	 */
	public TecCard() {
		super();
	}

	/**
	 * @param idx				idx主键
	 * @param tecCardNo			工艺卡编号
	 * @param tecCardDesc		工艺卡描述
	 * @param seqNo				顺序号
	 * @param tecNo				工艺编号
	 * @param tecName			工艺名称
	 * @param tecDesc			工艺描述
	 */
	public TecCard(String idx, String tecCardNo, String tecCardDesc, Integer seqNo, String tecNo, String tecName, String tecDesc) {
		this.idx = idx;
		this.tecCardNo = tecCardNo;
		this.tecCardDesc = tecCardDesc;
		this.seqNo = seqNo;
		this.tecNo = tecNo;
		this.tecName = tecName;
		this.tecDesc = tecDesc;
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

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getTecCardDesc() {
		return tecCardDesc;
	}

	public void setTecCardDesc(String tecCardDesc) {
		this.tecCardDesc = tecCardDesc;
	}

	public String getTecCardNo() {
		return tecCardNo;
	}

	public void setTecCardNo(String tecCardNo) {
		this.tecCardNo = tecCardNo;
	}

	public String getTecIDX() {
		return tecIDX;
	}

	public void setTecIDX(String tecIDX) {
		this.tecIDX = tecIDX;
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

	public String getTecDesc() {
		return tecDesc;
	}

	public void setTecDesc(String tecDesc) {
		this.tecDesc = tecDesc;
	}

	public String getTecName() {
		return tecName;
	}

	public void setTecName(String tecName) {
		this.tecName = tecName;
	}

	public String getTecNo() {
		return tecNo;
	}

	public void setTecNo(String tecNo) {
		this.tecNo = tecNo;
	}

}