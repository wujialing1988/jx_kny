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
 * <li>说明：RecordDI实体类, 数据表：检测项
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
@Table(name = "PJJX_Record_DI")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class RecordDI implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 是否必填 - 是 */
	public static final int CONST_INT_IS_BLANK_YES = 1;
	/** 是否必填 - 否 */
	public static final int CONST_INT_IS_BLANK_NO = 0;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 检修检测项主键 */
	@Column(name = "RI_IDX")
	private String rIIDX;

	/** 检测项编号 */
	@Column(name = "Data_Item_No")
	private String dataItemNo;

	/** 检测项名称 */
	@Column(name = "Data_Item_Name")
	private String dataItemName;

	/** 是否必填 */
	@Column(name = "Is_Blank")
	private Integer isBlank;

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
    
    /* 检修项编码（又可视化系统获取而来） */
    @Column(name="CHECK_ID")
    private String checkID;  
    
    /* 最小范围值 */
    private Double minResult;
    /* 最大范围值 */
    private Double maxResult;
    
	/** 检具 */
	@Column(name = "Checked_Tools")
	private String checkTools;
	/**
	 * @return String 获取检修检测项主键
	 */
	public String getRIIDX() {
		return rIIDX;
	}

	/**
	 * @param rIIDX 设置检修检测项主键
	 */
	public void setRIIDX(String rIIDX) {
		this.rIIDX = rIIDX;
	}

	/**
	 * @return String 获取检测项编号
	 */
	public String getDataItemNo() {
		return dataItemNo;
	}

	/**
	 * @param dataItemNo 设置检测项编号
	 */
	public void setDataItemNo(String dataItemNo) {
		this.dataItemNo = dataItemNo;
	}

	/**
	 * @return String 获取检测项名称
	 */
	public String getDataItemName() {
		return dataItemName;
	}

	/**
	 * @param dataItemName 设置检测项名称
	 */
	public void setDataItemName(String dataItemName) {
		this.dataItemName = dataItemName;
	}

	/**
	 * @return Integer 获取是否必填
	 */
	public Integer getIsBlank() {
		return isBlank;
	}

	/**
	 * @param isBlank 设置是否必填
	 */
	public void setIsBlank(Integer isBlank) {
		this.isBlank = isBlank;
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

    
    public Double getMaxResult() {
        return maxResult;
    }

    
    public void setMaxResult(Double maxResult) {
        this.maxResult = maxResult;
    }

    
    public Double getMinResult() {
        return minResult;
    }

    
    public void setMinResult(Double minResult) {
        this.minResult = minResult;
    }
    
    public String getCheckID() {
        return checkID;
    }

    
    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }

	public String getCheckTools() {
		return checkTools;
	}

	public void setCheckTools(String checkTools) {
		this.checkTools = checkTools;
	}
}