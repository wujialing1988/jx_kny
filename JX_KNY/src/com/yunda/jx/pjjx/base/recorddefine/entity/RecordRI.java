package com.yunda.jx.pjjx.base.recorddefine.entity;

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
 * <li>说明：RecordRI实体类, 数据表：检修检测项
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_Record_RI")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class RecordRI implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 记录卡主键 */
	@Column(name = "Record_Card_IDX")
	private String recordCardIDX;

	/** 检修检测项编号 */
	@Column(name = "Repair_Item_No")
	private String repairItemNo;

	/** 检修检测项名称 */
	@Column(name = "Repair_Item_Name")
	private String repairItemName;

	/** 技术要求 */
	@Column(name = "Repair_Standard")
	private String repairStandard;

	/** 默认结果 */
	@Column(name = "Default_Result")
	private String defaultResult;

	/** 顺序号 */
	@Column(name = "Seq_No")
	private Integer seqNo;

	/** 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name = "Record_Status")
	private Integer recordStatus;

	/** 站点标识，为了同步数据而使用 */
	@Column(updatable = false)
	private String siteID;
	/** 检具 */
	@Column(name = "Checked_Tools")
	private String checkTools;

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
	 * @return String 获取记录卡主键
	 */
	public String getRecordCardIDX() {
		return recordCardIDX;
	}

	/**
	 * @param recordCardIDX 设置记录卡主键
	 */
	public void setRecordCardIDX(String recordCardIDX) {
		this.recordCardIDX = recordCardIDX;
	}

	/**
	 * @return String 获取检修检测项编号
	 */
	public String getRepairItemNo() {
		return repairItemNo;
	}

	/**
	 * @param repairItemNo 设置检修检测项编号
	 */
	public void setRepairItemNo(String repairItemNo) {
		this.repairItemNo = repairItemNo;
	}

	/**
	 * @return String 获取检修检测项名称
	 */
	public String getRepairItemName() {
		return repairItemName;
	}

	/**
	 * @param repairItemName 设置检修检测项名称
	 */
	public void setRepairItemName(String repairItemName) {
		this.repairItemName = repairItemName;
	}

	/**
	 * @return String 获取技术要求
	 */
	public String getRepairStandard() {
		return repairStandard;
	}

	/**
	 * @param repairStandard 设置技术要求
	 */
	public void setRepairStandard(String repairStandard) {
		this.repairStandard = repairStandard;
	}

	/**
	 * @return String 获取默认结果
	 */
	public String getDefaultResult() {
		return defaultResult;
	}

	/**
	 * @param defaultResult 设置默认结果
	 */
	public void setDefaultResult(String defaultResult) {
		this.defaultResult = defaultResult;
	}

	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo() {
		return seqNo;
	}

	/**
	 * @param seqNo 设置顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus 设置记录的状态
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
	 * @param siteID 设置站点标识
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
	 * @param creator 设置创建人
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
	 * @param createTime 设置创建时间
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
	 * @param updator 设置修改人
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
	 * @param updateTime 设置修改时间
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
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

    
    public String getCheckTools() {
        return checkTools;
    }

    
    public void setCheckTools(String checkTools) {
        this.checkTools = checkTools;
    }
}