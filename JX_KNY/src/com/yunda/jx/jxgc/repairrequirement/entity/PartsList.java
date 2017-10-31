package com.yunda.jx.jxgc.repairrequirement.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsList实体类, 数据表：配件清单
 * <li>创建人：程梅
 * <li>创建日期：2016-1-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Parts_List")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsList implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    //类型
    public static final String TYPE_UNLOAD = "下车配件" ;
    public static final String TYPE_ABOARD = "上车配件" ;
    public static final String TYPE_AC = "在车配件" ;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 关联主键 */
	@Column(name="Relation_IDX")
	private String relationIdx;
	/* 配件型号表主键 */
	@Column(name="PARTS_TYPE_IDX")
	private String partsTypeIdx;
	/* 位置 */
	private String place;
	/* 类型 */
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
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	
	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public String getPartsTypeIdx() {
        return partsTypeIdx;
    }
    
    public void setPartsTypeIdx(String partsTypeIdx) {
        this.partsTypeIdx = partsTypeIdx;
    }
    
    public String getPlace() {
        return place;
    }
    
    public void setPlace(String place) {
        this.place = place;
    }
    
    public String getRelationIdx() {
        return relationIdx;
    }
    
    public void setRelationIdx(String relationIdx) {
        this.relationIdx = relationIdx;
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