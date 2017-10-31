package com.yunda.jx.component.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：EquipPart实体类, 数据表：位置编码
 * <li>创建人：程锐
 * <li>创建日期：2012-11-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="J_JCGY_EQUIP_PART")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class EquipPart implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 位置编码 */
    @Id
	@Column(name="PART_ID")
	private String partId;
	/* 位置名称 */
	@Column(name="PART_NAME")
	private String partName;

	/**
	 * @return String 获取位置编码
	 */
	public String getPartId(){
		return partId;
	}
	/**
	 * @param 设置位置编码
	 */
	public void setPartId(String partId) {
		this.partId = partId;
	}
	/**
	 * @return String 获取位置名称
	 */
	public String getPartName(){
		return partName;
	}
	/**
	 * @param 设置位置名称
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
}