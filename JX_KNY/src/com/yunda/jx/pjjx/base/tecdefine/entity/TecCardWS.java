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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: TecCardWS实体 数据表：配件检修工序
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:09:32
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_Tec_Card_WS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TecCardWS implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 工序编号 */
	@Column(name = "WS_No")
	private String wsNo;

	/** 工序名称 */
	@Column(name = "WS_Name")
	private String wsName;

	/** 工序描述 */
	@Column(name = "WS_Desc")
	private String wsDesc;

	/** 顺序号 */
	@Column(name = "Seq_No")
	private Integer seqNo;

	/** 上级工序idx主键 */
	@Column(name = "WS_Parent_IDX")
	private String wsParentIDX;

	/** 检修工艺卡idx主键 */
	@Column(name = "Tec_Card_IDX")
	private String tecCardIDX;

	/** 质量检验 */
	@Transient
	@JsonIgnore
	// @Column(name = "QC_Content")		
	// 新版设计取消了在工序里设置质量检查项，因此隐藏此部分表单（2014-11-12 by HeTao）
	private String qCContent;

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

	public String getQCContent() {
		return qCContent;
	}

	public void setQCContent(String content) {
		qCContent = content;
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

	public String getTecCardIDX() {
		return tecCardIDX;
	}

	public void setTecCardIDX(String tecCardIDX) {
		this.tecCardIDX = tecCardIDX;
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

	public String getWsDesc() {
		return wsDesc;
	}

	public void setWsDesc(String wsDesc) {
		this.wsDesc = wsDesc;
	}

	public String getWsName() {
		return wsName;
	}

	public void setWsName(String wsName) {
		this.wsName = wsName;
	}

	public String getWsNo() {
		return wsNo;
	}

	public void setWsNo(String wsNo) {
		this.wsNo = wsNo;
	}

	public String getWsParentIDX() {
		return wsParentIDX;
	}

	public void setWsParentIDX(String wsParentIDX) {
		this.wsParentIDX = wsParentIDX;
	}


}