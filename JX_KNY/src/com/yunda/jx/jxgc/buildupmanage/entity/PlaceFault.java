package com.yunda.jx.jxgc.buildupmanage.entity;

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
 * <li>说明：PlaceFault实体类, 数据表：故障现象
 * <li>创建人：程锐
 * <li>创建日期：2013-01-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Place_Fault")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PlaceFault implements java.io.Serializable{
    /**故障主键（其它：-111）*/
    public static final String OTHERID = "-111";
    /**自定义故障现象主键（-1111）*/
    public static final String CUSTOMID = "-1111";
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 组成位置主键 */
	@Column(name="BuildUp_Place_Idx")
	private String buildUpPlaceIdx;
    /* 组成型号主键 */
    @Column(name="BuildUp_Type_Idx")
    private String buildUpTypeIdx;
	/* 故障编号 */
	@Column(name="Fault_Id")
	private String faultId;
	/* 故障名称 */
	@Column(name="Fault_Name")
	private String faultName;
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

	/**
	 * @return String 获取组成位置主键
	 */
	public String getBuildUpPlaceIdx(){
		return buildUpPlaceIdx;
	}
	/**
	 * @param 设置组成位置主键
	 */
	public void setBuildUpPlaceIdx(String buildUpPlaceIdx) {
		this.buildUpPlaceIdx = buildUpPlaceIdx;
	}
    /**
     * @return String 获取组成型号主键
     */
    public String getBuildUpTypeIdx(){
        return buildUpTypeIdx;
    }
    /**
     * @param 设置组成型号主键
     */
    public void setBuildUpTypeIdx(String buildUpTypeIdx) {
        this.buildUpTypeIdx = buildUpTypeIdx;
    }
	/**
	 * @return String 获取故障编号
	 */
	public String getFaultId(){
		return faultId;
	}
	/**
	 * @param 设置故障编号
	 */
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}
	/**
	 * @return String 获取故障名称
	 */
	public String getFaultName(){
		return faultName;
	}
	/**
	 * @param 设置故障名称
	 */
	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录状态
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
	 * @param 设置站点标识
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
	 * @param 设置创建人
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
	 * @param 设置创建时间
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
	 * @param 设置修改人
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
	 * @param 设置修改时间
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
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}