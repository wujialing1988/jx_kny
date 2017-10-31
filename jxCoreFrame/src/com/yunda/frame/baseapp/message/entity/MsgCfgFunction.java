package com.yunda.frame.baseapp.message.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MsgCfgFunction实体类, 数据表：消息服务配置-功能点定义
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="X_MsgCfgFunction")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MsgCfgFunction implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 功能点编码 */
	private String funCode;
	/* 功能点名称 */
	private String funName;

	/**
	 * @return String 获取功能点编码
	 */
	public String getFunCode(){
		return funCode;
	}
	/**
	 * @param 设置功能点编码
	 */
	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}
	/**
	 * @return String 获取功能点名称
	 */
	public String getFunName(){
		return funName;
	}
	/**
	 * @param 设置功能点名称
	 */
	public void setFunName(String funName) {
		this.funName = funName;
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