package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：修次实体类, 数据表：Jcgy_rt
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="J_JCGY_RT")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class RT implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 修次编码 */
    @Id
	@Column(name="RT_ID")
	private String rtID;
	/* 修次名称 */
	@Column(name="RT_NAME")
	private String rtName;

	/**
	 * @return String 获取修次编码
	 */
	public String getRtID(){
		return rtID;
	}
	/**
	 * @param 设置修次编码
	 */
	public void setRtID(String rtID) {
		this.rtID = rtID;
	}
	/**
	 * @return String 获取修次名称
	 */
	public String getRtName(){
		return rtName;
	}
	/**
	 * @param 设置修次名称
	 */
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
}