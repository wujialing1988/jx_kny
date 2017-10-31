package com.yunda.jx.jxgc.repairrequirement.entity;

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
 * <li>说明：QualityControl实体类, 数据表：质量控制
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Quality_Control")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class QualityControl implements java.io.Serializable{
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 关联主键（关联工序卡、工步主键，根据类型进行区分） */
	@Column(name="Relation_IDX")
	private String relationIDX;
	/* 检验项编码（存放数据字典ID） */
	@Column(name="Check_Item_Code")
	private String checkItemCode;
	/* 检验项名称（数据字典） */
	@Column(name="Check_Item_Name")
	private String checkItemName;
	/* 检验方式编码（存放数据字典ID） */
	@Column(name="Check_Way_Code")
	private String checkWayCode;
	/* 检验方式名称（数据字典） */
	@Column(name="Check_Way_Name")
	private String checkWayName;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private java.util.Date updateTime;

	/**
	 * @return String 获取关联主键
	 */
	public String getRelationIDX(){
		return relationIDX;
	}
	/**
	 * @param 设置关联主键
	 */
	public void setRelationIDX(String relationIDX) {
		this.relationIDX = relationIDX;
	}
	/**
	 * @return String 获取检验项编码
	 */
	public String getCheckItemCode(){
		return checkItemCode;
	}
	/**
	 * @param 设置检验项编码
	 */
	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}
	/**
	 * @return String 获取检验项名称
	 */
	public String getCheckItemName(){
		return checkItemName;
	}
	/**
	 * @param 设置检验项名称
	 */
	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}
	/**
	 * @return String 获取检验方式编码
	 */
	public String getCheckWayCode(){
		return checkWayCode;
	}
	/**
	 * @param 设置检验方式编码
	 */
	public void setCheckWayCode(String checkWayCode) {
		this.checkWayCode = checkWayCode;
	}
	/**
	 * @return String 获取检验方式名称
	 */
	public String getCheckWayName(){
		return checkWayName;
	}
	/**
	 * @param 设置检验方式名称
	 */
	public void setCheckWayName(String checkWayName) {
		this.checkWayName = checkWayName;
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