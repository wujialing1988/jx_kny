package com.yunda.frame.baseapp.message.entity;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MsgCfgReceive实体类, 数据表：消息服务配置-接收方定义
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
@Table(name="X_MsgCfgReceive")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MsgCfgReceive implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 功能点定义主键 */
	private String funidx;
	/* 接收方ID */
	private String receiverID;
	/* 接收方名称 */
	private String receiverName;
	/* 接收方类型：1人员，2组织机构，3组，4岗位，5职务，6角色 */
	private Integer type;
	/* 规则标志 */
	private String ruleFlag;
	@Column(name="WORKPLACE_CODE")
	private String workplaceCode;
	/* 工作地点名称 */
	@Column(name="WORKPLACE_NAME")
	private String workplaceName;
	/**
	 * @return String 获取功能点定义主键
	 */
	public String getFunidx(){
		return funidx;
	}
	/**
	 * @param 设置功能点定义主键
	 */
	public void setFunidx(String funidx) {
		this.funidx = funidx;
	}
	/**
	 * @return String 获取接收方ID
	 */
	public String getReceiverID(){
		return receiverID;
	}
	/**
	 * @param 设置接收方ID
	 */
	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}
	/**
	 * @return String 获取接收方名称
	 */
	public String getReceiverName(){
		return receiverName;
	}
	/**
	 * @param 设置接收方名称
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * @return Integer 获取接收方类型
	 */
	public Integer getType(){
		return type;
	}
	/**
	 * @param 设置接收方类型
	 */
	public void setType(Integer type) {
		this.type = type;
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
	public String getWorkplaceCode() {
		return workplaceCode;
	}
	public void setWorkplaceCode(String workplaceCode) {
		this.workplaceCode = workplaceCode;
	}
	public String getWorkplaceName() {
		return workplaceName;
	}
	public void setWorkplaceName(String workplaceName) {
		this.workplaceName = workplaceName;
	}
	public String getRuleFlag() {
		return ruleFlag;
	}
	public void setRuleFlag(String ruleFlag) {
		this.ruleFlag = ruleFlag;
	}
}