package com.yunda.jx.wlgl.aboard.entity;

import java.util.Date;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatAboardTrainAccount实体类, 数据表：大型消耗配件上车记录
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-9 上午11:36:26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_Mat_Aboard_Train_Account")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatAboardTrainAccount implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id @GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 物料编号 */
	@Column(name = "Mat_No")
	private String matNo;

	/** 物料类型 */
	@Column(name = "Mat_Class")
	private String matClass;

	/** 位置 */
	private String position;

	/** 领用人主键 */
	@Column(name = "Get_Emp_ID")
	private Integer getEmpId;

	/** 领用人名称 */
	@Column(name = "Get_Emp")
	private String getEmp;

	/** 车型编码 */
	@Column(name = "TrainType_IDX")
	private String trainTypeIDX;

	/** 车型简称 */
	@Column(name = "TrainType_ShortName")
	private String trainTypeShortName;

	/** 车号 */
	private String trainNo;

	/** 修程编码 */
	@Column(name = "XC_ID")
	private String xcId;

	/** 修程名称 */
	@Column(name = "XC_Name")
	private String xcName;

	/** 修次编码 */
	@Column(name = "RT_ID")
	private String rtId;

	/** 修次名称 */
	@Column(name = "RT_Name")
	private String rtName;

	/** 上车日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Aboard_Date")
	private java.util.Date aboardDate;

	/** 备注 */
	private String remarks;

	/** 登帐人 */
	@Column(name = "Regist_Emp")
	private String registEmp;

	/** 登帐日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Regist_Date")
	private java.util.Date registDate;

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

	/** 查询字段 - 上车开始日期 */
	@Transient
	private Date startDate;

	/** 查询字段 - 上车结束日期 */
	@Transient
	private Date endDate;

	/**
	 * @return 获取上车日期
	 */
	public java.util.Date getAboardDate() {
		return aboardDate;
	}

	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator() {
		return creator;
	}

	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return 获取领用人名称
	 */
	public String getGetEmp() {
		return getEmp;
	}

	/**
	 * @return 获取领用人id
	 */
	public Integer getGetEmpId() {
		return getEmpId;
	}

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @return 获取物料类型
	 */
	public String getMatClass() {
		return matClass;
	}

	/**
	 * @return 设置物料编号
	 */
	public String getMatNo() {
		return matNo;
	}

	/**
	 * @return 获取位置
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @return java.util.Date 获取登帐日期
	 */
	public java.util.Date getRegistDate() {
		return registDate;
	}

	/**
	 * @return String 获取登帐人
	 */
	public String getRegistEmp() {
		return registEmp;
	}

	/**
	 * @return 获取备注
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @return 获取修改编码
	 */
	public String getRtId() {
		return rtId;
	}

	/**
	 * @return 获取修次名称
	 */
	public String getRtName() {
		return rtName;
	}

	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID() {
		return siteID;
	}

	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return 获取车号
	 */
	public String getTrainNo() {
		return trainNo;
	}

	/**
	 * @return 获取车型编码
	 */
	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}

	/**
	 * @return 获取车型简称
	 */
	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator() {
		return updator;
	}

	/**
	 * @return 获取修程编码
	 */
	public String getXcId() {
		return xcId;
	}

	/**
	 * @return 获取修程名称
	 */
	public String getXcName() {
		return xcName;
	}

	/**
	 * @param aboardDate 设置上车日期
	 */
	public void setAboardDate(java.util.Date aboardDate) {
		this.aboardDate = aboardDate;
	}

	/**
	 * @param 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @param 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param getEmp
	 *            设置领用人名称
	 */
	public void setGetEmp(String getEmp) {
		this.getEmp = getEmp;
	}

	/**
	 * @param getEmpId
	 *            设置领用人id
	 */
	public void setGetEmpId(Integer getEmpId) {
		this.getEmpId = getEmpId;
	}

	/**
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	/**
	 * @param matClass 设置物料类型
	 */
	public void setMatClass(String matClass) {
		this.matClass = matClass;
	}

	/**
	 * @param matNo 获取物料编号
	 */
	public void setMatNo(String matNo) {
		this.matNo = matNo;
	}

	/**
	 * @param position 设置位置
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @param 设置记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @param 设置登帐日期
	 */
	public void setRegistDate(java.util.Date registDate) {
		this.registDate = registDate;
	}

	/**
	 * @param 设置登帐人
	 */
	public void setRegistEmp(String registEmp) {
		this.registEmp = registEmp;
	}

	/**
	 * @param remarks 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @param rtId
	 *            设置修次编码
	 */
	public void setRtId(String rtId) {
		this.rtId = rtId;
	}

	/**
	 * @param rtName
	 *            设置修次名称
	 */
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}

	/**
	 * @param 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param trainNo
	 *            设置车号
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	/**
	 * @param trainTypeIDX
	 *            设置车型编码
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}

	/**
	 * @param trainTypeShortName
	 *            设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}

	/**
	 * @param 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @param 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @param xcId
	 *            设置修程编码
	 */
	public void setXcId(String xcId) {
		this.xcId = xcId;
	}

	/**
	 * @param xcName
	 *            设置修程名称
	 */
	public void setXcName(String xcName) {
		this.xcName = xcName;
	}

}