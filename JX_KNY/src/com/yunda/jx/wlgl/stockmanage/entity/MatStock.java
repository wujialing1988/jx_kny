package com.yunda.jx.wlgl.stockmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MATSTOCK实体类, 数据表：物料库存台账
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="WLGL_MAT_STOCK")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatStock implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 库房主键 */
	@Column(name="WH_IDX")
	private String whIdx;
	/* 库房名称 */
	@Column(name="WH_Name")
	private String whName;
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
	private String unit;
	/* 数量 */
	private Integer qty;
    /* 物料类型（技改件、消耗件） */
    @Column(name="Mat_Type")
    private String matType;
    /* 库位 */
    @Column(name="LOCATION_NAME")
    private String locationName;

	/**
	 * @return true 获取库房主键
	 */
	public String getWhIdx(){
		return whIdx;
	}
	/**
	 * @param 设置库房主键
	 */
	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}
	/**
	 * @return String 获取库房名称
	 */
	public String getWhName(){
		return whName;
	}
	/**
	 * @param 设置库房名称
	 */
	public void setWhName(String whName) {
		this.whName = whName;
	}
	/**
	 * @return String 获取物料编码
	 */
	public String getMatCode(){
		return matCode;
	}
	/**
	 * @param 设置物料编码
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	/**
	 * @return String 获取物料描述
	 */
	public String getMatDesc(){
		return matDesc;
	}
	/**
	 * @param 设置物料描述
	 */
	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}
	/**
	 * @return String 获取单位
	 */
	public String getUnit(){
		return unit;
	}
	/**
	 * @param 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return Integer 获取数量
	 */
	public Integer getQty(){
		return qty;
	}
	/**
	 * @param 设置数量
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getMatType() {
        return matType;
    }
    
    public void setMatType(String matType) {
        this.matType = matType;
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