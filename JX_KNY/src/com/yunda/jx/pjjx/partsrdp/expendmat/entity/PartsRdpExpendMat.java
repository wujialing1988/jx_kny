package com.yunda.jx.pjjx.partsrdp.expendmat.entity;

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
 * <li>说明：PartsRdpExpendMat实体类, 数据表：物料消耗记录
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_Parts_Rdp_Expend_Mat")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpExpendMat implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 节点主键 */
	@Column(name="Rdp_Node_IDX")
	private String rdpNodeIDX;
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
	private String unit;
	/* 单价 */
	private Double price;
	/* 额定数量 */
	@Column(name="number_rated")
	private Double numberRated;
	/* 消耗数量 */
	private Double qty;
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
	/* 消耗人 */
	@Column(name="HANDLE_EMPID")
	private Long handleEmpId;
	/* 消耗人名称 */
	@Column(name="HANDLE_EMPNAME")
	private String handleEmpName;
	/* 消耗班组 */
	@Column(name="HANDLE_ORGID")
	private Long handleOrgId;
	/* 消耗班组名称 */
	@Column(name="HANDLE_ORGNAME")
	private String handleOrgName;
	/* 消耗班组序列 */
	@Column(name="HANDLE_ORGSEQ")
	private String handleOrgSeq;

	/**
	 * @return 获取消耗人
	 */
	public Long getHandleEmpId() {
		return handleEmpId;
	}
	/**
	 * @param handleEmpId 设置消耗人
	 */
	public void setHandleEmpId(Long handleEmpId) {
		this.handleEmpId = handleEmpId;
	}
	/**
	 * @return 获取消耗人名称
	 */
	public String getHandleEmpName() {
		return handleEmpName;
	}
	/**
	 * @param handleEmpName 设置消耗人名称
	 */
	public void setHandleEmpName(String handleEmpName) {
		this.handleEmpName = handleEmpName;
	}
	/**
	 * @return 获取消耗班组
	 */
	public Long getHandleOrgId() {
		return handleOrgId;
	}
	/**
	 * @param handleOrgId 设置消耗班组
	 */
	public void setHandleOrgId(Long handleOrgId) {
		this.handleOrgId = handleOrgId;
	}
	/**
	 * @return 获取消耗班组名称
	 */
	public String getHandleOrgName() {
		return handleOrgName;
	}
	/**
	 * @param handleOrgName 设置消耗班组名称
	 */
	public void setHandleOrgName(String handleOrgName) {
		this.handleOrgName = handleOrgName;
	}
	/**
	 * @return 获取消耗班组序列
	 */
	public String getHandleOrgSeq() {
		return handleOrgSeq;
	}
	/**
	 * @param handleOrgSeq 设置消耗班组序列
	 */
	public void setHandleOrgSeq(String handleOrgSeq) {
		this.handleOrgSeq = handleOrgSeq;
	}
	/**
	 * @return String 获取作业主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param rdpIDX 设置作业主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	/**
	 * @return String 获取工艺工单主键
	 */
	public String getRdpNodeIDX(){
		return rdpNodeIDX;
	}
	/**
	 * @param rdpNodeIDX 设置工艺工单主键
	 */
	public void setRdpNodeIDX(String rdpNodeIDX) {
		this.rdpNodeIDX = rdpNodeIDX;
	}
	/**
	 * @return String 获取物料编码
	 */
	public String getMatCode(){
		return matCode;
	}
	/**
	 * @param matCode 设置物料编码
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	/**
	 * @return String 获取物料描述
	 */
	public String getMatDesc(){
		return matDesc;
	}
	/**
	 * @param matDesc 设置物料描述
	 */
	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}
	/**
	 * @return String 获取单位
	 */
	public String getUnit(){
		return unit;
	}
	/**
	 * @param unit 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return Double 获取单价
	 */
	public Double getPrice(){
		return price;
	}
	/**
	 * @param price 设置单价
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return Double 获取消耗数量
	 */
	public Double getQty(){
		return qty;
	}
	/**
	 * @param qty 设置消耗数量
	 */
	public void setQty(Double qty) {
		this.qty = qty;
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
	/**
	 * @return String 获取额定数量
	 */
	public Double getNumberRated() {
		return numberRated;
	}
	/**
	 * @param numberRated 设置额定数量
	 */
	public void setNumberRated(Double numberRated) {
		this.numberRated = numberRated;
	}
}