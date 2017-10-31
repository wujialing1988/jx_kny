package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordDIDTO查询类, 数据表：配件检修检测数据项
 * <li>创建人：林欢
 * <li>创建日期：2016-7-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
public class PartsRdpRecordDIDTO implements java.io.Serializable{
    
    /** 代表该条数据通过手工录入的方式 值：0*/
    public static final Integer HANDINPUT = 0;
    
    /** 代表该条数据通过可视化系统选择的方式 值：1*/
    public static final Integer VISUALINPUT = 1;
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@Id	
	private String idx;
	/* 检修检测项实例主键 */
	@Column(name="Rdp_Record_RI_IDX")
	private String rdpRecordRIIDX;
	/* 检测项主键 */
	@Column(name="Record_DI_IDX")
	private String recordDIIDX;
	/* 检测项编号 */
	@Column(name="Data_Item_No")
	private String dataItemNo;
	/* 检测项名称 */
	@Column(name="Data_Item_Name")
	private String dataItemName;
	/* 是否必填 */
	@Column(name="Is_Blank")
	private Integer isBlank;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 检测结果 */
	@Column(name="DATA_ITEM_RESULT")
	private String dataItemResult;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;	
    
    /* 检修项编码（又可视化系统获取而来） */
    @Column(name="CHECK_ID")
    private String checkID;  
    
    /* 数据来源（0=手工输入 1=可视化系统选择） 注意：如果该字段传递为nulll保存检测项的时候，默认为0*/
    @Column(name="data_Source")
    private Integer dataSource;  
    
    /* 最小范围值 */
    private Double minResult;
    /* 最大范围值 */
    private Double maxResult;
	
	/** 检具 */
	@Column(name = "Checked_Tools")
	private String checkTools;
	
	/* 检测值 */
	@Column(name = "check_Value")
	private String checkValue;

	/* 配件idx */
	@Column(name = "parts_ID")
	private String partID;

	/* 检测时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "check_Time")
	private Date checkTime;
	
	/* 检测结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "check_End_Time")
	private Date checkEndTime;
	
	
	public Date getCheckEndTime() {
		return checkEndTime;
	}
	public void setCheckEndTime(Date checkEndTime) {
		this.checkEndTime = checkEndTime;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckValue() {
		return checkValue;
	}
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	public String getPartID() {
		return partID;
	}
	public void setPartID(String partID) {
		this.partID = partID;
	}
	public String getCheckTools() {
		return checkTools;
	}
	public void setCheckTools(String checkTools) {
		this.checkTools = checkTools;
	}
	/**
	 * @return String 获取检修检测项实例主键
	 */
	public String getRdpRecordRIIDX(){
		return rdpRecordRIIDX;
	}
	/**
	 * @param rdpRecordRIIDX 设置检修检测项实例主键
	 */
	public void setRdpRecordRIIDX(String rdpRecordRIIDX) {
		this.rdpRecordRIIDX = rdpRecordRIIDX;
	}
	/**
	 * @return String 获取检测项主键
	 */
	public String getRecordDIIDX(){
		return recordDIIDX;
	}
	/**
	 * @param recordDIIDX 设置检测项主键
	 */
	public void setRecordDIIDX(String recordDIIDX) {
		this.recordDIIDX = recordDIIDX;
	}
	/**
	 * @return String 获取检测项编号
	 */
	public String getDataItemNo(){
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
	public String getDataItemName(){
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
	public Integer getIsBlank(){
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
	public Integer getSeqNo(){
		return seqNo;
	}
	/**
	 * @param seqNo 设置顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
	/**
	 * @return 获取检测结果
	 */
	public String getDataItemResult() {
		return dataItemResult;
	}
	/**
	 * @param dataItemResult 设置检测结果
	 */
	public void setDataItemResult(String dataItemResult) {
		this.dataItemResult = dataItemResult;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
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
	public String getSiteID(){
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
	public Long getCreator(){
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
	public java.util.Date getCreateTime(){
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
	public Long getUpdator(){
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
	public java.util.Date getUpdateTime(){
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
    
    /**
     * <li>说明：检验输入的数据项是否超出了预设的范围值
     * <li>创建人：何涛
     * <li>创建日期：2016-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return true：超出预设的范围值，false：没有超出预设的范围值
     */
    public boolean isOutOfRange() {
        if (null == this.dataItemResult) {
            return false;
        }
        if (null == this.minResult && null == this.maxResult) {
            return false;
        }
        Double result = null;
        try {
            // 输入结果非数字时的异常处理
            result = Double.valueOf(this.dataItemResult);
        } catch (Exception e) {
            return false;
        }
        if (null != this.minResult && result < this.minResult.doubleValue()) {
            return true;
        }
        if (null != this.maxResult && result > this.maxResult.doubleValue()) {
            return true;
        }
        return false;
    }
    
    public String getCheckID() {
        return checkID;
    }
    
    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }
    
    public Integer getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }    
}