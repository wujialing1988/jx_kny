package com.yunda.jx.jxgc.base.jcqcitemdefine.entity;

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
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: QCItem实体 数据表：质量检查项
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-12 下午01:39:28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_JCQC_ITEM_DEFINE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JCQCItemDefine implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 是否指派 - 否 */
	public static final int CONST_INT_IS_ASSIGN_N = 0;
	/** 是否指派 - 是 */
	public static final int CONST_INT_IS_ASSIGN_Y = 1;
	
	/** 抽检/必检 - （1：抽检） */
	public static final int CONST_INT_CHECK_WAY_CJ = 1;
	/** 抽检/必检 - （2：必检） */
	public static final int CONST_INT_CHECK_WAY_BJ = 2;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 检查项编码 */
	@Column(name = "QC_Item_No")
	private String qCItemNo;

	/** 检查项名称 */
	@Column(name = "QC_Item_Name")
	private String qCItemName;
	
	/** 抽检/必检（1：抽检；2：必检）默认为1 */
	@Column(name = "Check_Way")
	private Integer checkWay;

	/** 是否指派 */
	@Column(name = "Is_Assign")
	private Integer isAssign;

	/** 顺序号 */
	@Column(name = "Seq_No")
	private Integer seqNo;

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

	public Integer getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Integer isAssign) {
		this.isAssign = isAssign;
	}

	public Integer getCheckWay() {
		return checkWay;
	}

	public void setCheckWay(Integer checkWay) {
		this.checkWay = checkWay;
	}

	public String getQCItemName() {
		return qCItemName;
	}

	public void setQCItemName(String itemName) {
		qCItemName = itemName;
	}

	public String getQCItemNo() {
		return qCItemNo;
	}

	public void setQCItemNo(String itemNo) {
		qCItemNo = itemNo;
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