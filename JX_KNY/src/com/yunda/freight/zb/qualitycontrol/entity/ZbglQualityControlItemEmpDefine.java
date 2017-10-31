package com.yunda.freight.zb.qualitycontrol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglQualityControlItemEmpDefine实体类, 数据表：检查人员基础配置
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
@Table(name="k_qc_item_emep_define")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglQualityControlItemEmpDefine implements Serializable{

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	
	/* 检查项主键 */
	@Column(name="qc_item_idx")
	private String qcItemIDX;
	
	/* 检查人员 */
	@Column(name="check_empid")
	private Integer checkEmpID;
	
	/* 检查人员名称 */
	@Column(name="check_empname")
	private String checkEmpName;
	
	/* 站点标识 */
	@Column(name="siteid")
	private String siteID;

	public Integer getCheckEmpID() {
		return checkEmpID;
	}

	public void setCheckEmpID(Integer checkEmpID) {
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

	public String getQcItemIDX() {
		return qcItemIDX;
	}

	public void setQcItemIDX(String qcItemIDX) {
		this.qcItemIDX = qcItemIDX;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	
	
}
