package com.yunda.jx.pjjx.partsrdp.qcinst.entity;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件质量检查返修日志实体
 * <li>创建人：张迪
 * <li>创建日期：2016-8-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_QR_BackRepair_Log")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpQRBackRepairLog implements java.io.Serializable{
	
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 兑现单主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 记录卡实例主键 */
	@Column(name="Rdp_Record_Card_IDX")
	private String rdpRecordCardIDX;

	/* 检查项编码 */
	@Column(name="QC_Item_No")
	private String qCItemNo;
	/* 检查项名称 */
	@Column(name="QC_Item_Name")
	private String qCItemName;
	/* 检验人员 */
	@Column(name="QR_EmpID")
	private Long qREmpID;
	/* 检验人员名称 */
	@Column(name="QR_EmpName")
	private String qREmpName;
	/* 检验时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BACKREPAIR_TIME")
	private java.util.Date backRepairTime;



	/**
	 * @return String 获取关联主键
	 */

	/**
	 * @return String 获取记录卡实例主键
	 */
	public String getRdpRecordCardIDX(){
		return rdpRecordCardIDX;
	}
	/**
	 * @param rdpRecordCardIDX 设置记录卡实例主键
	 */
	public void setRdpRecordCardIDX(String rdpRecordCardIDX) {
		this.rdpRecordCardIDX = rdpRecordCardIDX;
	}

	/**
	 * @return String 获取检查项编码
	 */
	public String getQCItemNo(){
		return qCItemNo;
	}
	/**
	 * @param qCItemNo 设置检查项编码
	 */
	public void setQCItemNo(String qCItemNo) {
		this.qCItemNo = qCItemNo;
	}
	/**
	 * @return String 获取检查项名称
	 */
	public String getQCItemName(){
		return qCItemName;
	}
	/**
	 * @param qCItemName 设置检查项名称
	 */
	public void setQCItemName(String qCItemName) {
		this.qCItemName = qCItemName;
	}
	
	/**
	 * @return Long 获取检验人员
	 */
	public Long getQREmpID(){
		return qREmpID;
	}
	/**
	 * @param qREmpID 设置检验人员
	 */
	public void setQREmpID(Long qREmpID) {
		this.qREmpID = qREmpID;
	}
	/**
	 * @return String 获取检验人员名称
	 */
	public String getQREmpName(){
		return qREmpName;
	}
	/**
	 * @param qREmpName 设置检验人员名称
	 */
	public void setQREmpName(String qREmpName) {
		this.qREmpName = qREmpName;
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
  
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public java.util.Date getBackRepairTime() {
        return backRepairTime;
    }
    
    public void setBackRepairTime(java.util.Date backRepairTime) {
        this.backRepairTime = backRepairTime;
    }
	
	

}