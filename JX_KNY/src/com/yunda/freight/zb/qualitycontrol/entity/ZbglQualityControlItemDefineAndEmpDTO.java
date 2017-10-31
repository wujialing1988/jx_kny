package com.yunda.freight.zb.qualitycontrol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglQualityControlItemDefineAndEmpDTO模型类,质量检查和人员查询对象
 * <li>创建人：林欢
 * <li>创建日期：2016-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 

@Entity
public class ZbglQualityControlItemDefineAndEmpDTO implements Serializable{

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* idx主键 */
	@Id
	private String idx;
	
	/* 检查项编码 */
	@Column(name="qc_item_no")
	private String qcItemNo;
	
	/* 检查项名称 */
	@Column(name="qc_item_name")
	private String qcItemName;
	
	/* 检查人员 */
	@Column(name="check_empid")
	private Long checkEmpID;
	
	/* 检查人员名称 */
	@Column(name="check_empname")
	private String checkEmpName;

	public Long getCheckEmpID() {
		return checkEmpID;
	}

	public void setCheckEmpID(Long checkEmpID) {
		this.checkEmpID = checkEmpID;
	}

	public String getCheckEmpName() {
		return checkEmpName;
	}

	public void setCheckEmpName(String checkEmpName) {
		this.checkEmpName = checkEmpName;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getQcItemName() {
		return qcItemName;
	}

	public void setQcItemName(String qcItemName) {
		this.qcItemName = qcItemName;
	}

	public String getQcItemNo() {
		return qcItemNo;
	}

	public void setQcItemNo(String qcItemNo) {
		this.qcItemNo = qcItemNo;
	}
	
}
