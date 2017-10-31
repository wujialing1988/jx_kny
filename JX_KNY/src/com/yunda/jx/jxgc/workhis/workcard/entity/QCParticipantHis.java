package com.yunda.jx.jxgc.workhis.workcard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图JXGC_QC_PARTICIPANT_HIS实体类, 数据表：机车检修质量检验参与者历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_QC_PARTICIPANT_HIS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class QCParticipantHis implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@Id
	private String idx;

	/* 兑现单主键 */
	@Column(name = "Rdp_IDX")
	private String rdpIDX;

	/* 作业卡主键 */
	@Column(name = "Work_Card_IDX")
	private String workCardIDX;

	/* 检验项编码 */
	@Column(name = "Check_Item_Code")
	private String checkItemCode;

	/* 检验人主键 */
	@Column(name = "Check_Person_id")
	private String checkPersonID;

	/* 检验人名称 */
	@Column(name = "Check_Person_Name")
	private String checkPersonName;

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

	public String getCheckItemCode() {
		return checkItemCode;
	}

	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}

	public String getCheckPersonID() {
		return checkPersonID;
	}

	public void setCheckPersonID(String checkPersonID) {
		this.checkPersonID = checkPersonID;
	}

	public String getCheckPersonName() {
		return checkPersonName;
	}

	public void setCheckPersonName(String checkPersonName) {
		this.checkPersonName = checkPersonName;
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

	public String getRdpIDX() {
		return rdpIDX;
	}

	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
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

	public String getWorkCardIDX() {
		return workCardIDX;
	}

	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}
}