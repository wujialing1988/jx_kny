package com.yunda.zb.zbfw.entity;

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
 * <li>说明：ZbFwcx实体类, 数据表：整备范围适用车型
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBFW_CX")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbFwcx implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 整备范围主键 */
	@Column(name="ZBFW_IDX")
	private String zbfwIDX;
	/* 车型编码 */
	@Column(name="CX_BM")
	private String cXBM;
	/* 车型拼音码 */
	@Column(name="CX_PYM")
	private String cXPYM;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;
	
	/**
	 * 
	 * <li>说明：默认构造方法
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public ZbFwcx(){}
	/**
	 * 
	 * <li>说明：整备范围车型列表实体构造方法
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param idx 主键
	 * @param zbfwIDX 范围主键
	 * @param cXPYM 车型简称
	 * @param cXBM 车型主键
	 */
	public ZbFwcx(String idx,String zbfwIDX,String cXBM,String cXPYM){
		super();
		this.idx = idx;
		this.zbfwIDX = zbfwIDX;
		this.cXBM = cXBM;
		this.cXPYM = cXPYM;
	}
	
	/**
	 * @return String 获取整备范围主键
	 */
	public String getZbfwIDX(){
		return zbfwIDX;
	}
	public void setZbfwIDX(String zbfwIDX) {
		this.zbfwIDX = zbfwIDX;
	}
	/**
	 * @return String 获取车型编码
	 */
	public String getCXBM(){
		return cXBM;
	}
	public void setCXBM(String cXBM) {
		this.cXBM = cXBM;
	}
	/**
	 * @return String 获取车型拼音码
	 */
	public String getCXPYM(){
		return cXPYM;
	}
	public void setCXPYM(String cXPYM) {
		this.cXPYM = cXPYM;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
}