package com.yunda.zb.oilconsumption.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Jyzl实体类, 数据表：机油种类编码
 * <li>创建人：王利成
 * <li>创建日期：2015-01-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JCBM_JYZL")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class Jyzl implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 机油种类编码 */
	@Id	
	@Column(name="JYZLBM")
	private String jyCode;
	/* 机油种类名称 */
	@Column(name="JYZLMC")
	private String jyName;
	/* 计量单位 */
	@Column(name="DW")
	private String dw;
	
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getJyCode() {
		return jyCode;
	}
	public void setJyCode(String jyCode) {
		this.jyCode = jyCode;
	}
	public String getJyName() {
		return jyName;
	}
	public void setJyName(String jyName) {
		this.jyName = jyName;
	}
}