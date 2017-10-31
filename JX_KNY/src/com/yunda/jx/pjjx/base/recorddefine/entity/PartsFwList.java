package com.yunda.jx.pjjx.base.recorddefine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件清单实体类
 * <li>创建人：程锐
 * <li>创建日期：2016-2-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "PJJX_Parts_List")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsFwList implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	public static final String TYPE_DISMANTLE = "拆卸";

	public static final String TYPE_INSTALL = "安装";

	public static final String TYPE_INSEPARAB = "不拆卸";

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 配件检修记录单IDX */
	@Column(name = "Relation_IDX")
	private String relationIDX;

	/** 配件规格型号IDX */
	@Column(name = "PARTS_TYPE_IDX")
	private String partsTypeIDX;

	/** 位置 */
	private String place;

	/** 类型：拆卸、安装、不拆卸 */
	private String type;

	/** 规格型号 */
	@Transient
	private String specificationModel;

	/** 计量单位 */
	@Transient
	private String unit;

	/** 配件名称 */
	@Transient
	private String partsName;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getPartsTypeIDX() {
		return partsTypeIDX;
	}

	public void setPartsTypeIDX(String partsTypeIDX) {
		this.partsTypeIDX = partsTypeIDX;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRelationIDX() {
		return relationIDX;
	}

	public void setRelationIDX(String relationIDX) {
		this.relationIDX = relationIDX;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPartsName() {
		return partsName;
	}

	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}

	public String getSpecificationModel() {
		return specificationModel;
	}

	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
