package com.yunda.jx.wlgl.partsBase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WhMatQuota实体类, 数据表：库房保有量
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="WLGL_WH_MAT_Quota")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WhMatQuota implements java.io.Serializable{
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
    
    /* 工作地点标识码 */ 
    @Column(name = "WORKPLACE_CODE")
    private java.lang.String workplaceCode;            
            
    /* 工作地点名称 */ 
    @Column(name = "WORKPLACE_NAME")
    private java.lang.String workplaceName; 
    
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
	private String unit;
	/* 最低保有量 */
	@Column(name="Min_Qty")
	private Integer minQty;
	/* 最大量 */
	@Column(name="Max_Qty")
	private Integer maxQty;
    /* 当前量 */
    @Column(name="CURRENT_QTY")
    private Integer currentQty;
	/* 使用频率 */
	@Column(name="Use_Rate")
	private String useRate;
	/* 备注 */
	private String remarks;
	/* 维护人 */
	@Column(name="Maintain_Emp")
	private String maintainEmp;
	/* 维护日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Maintain_Date")
	private java.util.Date maintainDate;
    /* 物料类型（技改件、消耗件） */
    @Column(name="Mat_Type")
    private String matType;
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
	 * @return Integer 获取最低保有量
	 */
	public Integer getMinQty(){
		return minQty;
	}
	/**
	 * @param 设置最低保有量
	 */
	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}
	/**
	 * @return Integer 获取最大量
	 */
	public Integer getMaxQty(){
		return maxQty;
	}
	/**
	 * @param 设置最大量
	 */
	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}
	/**
	 * @return String 获取使用频率
	 */
	public String getUseRate(){
		return useRate;
	}
	/**
	 * @param 设置使用频率
	 */
	public void setUseRate(String useRate) {
		this.useRate = useRate;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return String 获取维护人
	 */
	public String getMaintainEmp(){
		return maintainEmp;
	}
	/**
	 * @param 设置维护人
	 */
	public void setMaintainEmp(String maintainEmp) {
		this.maintainEmp = maintainEmp;
	}
	/**
	 * @return java.util.Date 获取维护日期
	 */
	public java.util.Date getMaintainDate(){
		return maintainDate;
	}
	/**
	 * @param 设置维护日期
	 */
	public void setMaintainDate(java.util.Date maintainDate) {
		this.maintainDate = maintainDate;
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
    
    public Integer getCurrentQty() {
        return currentQty;
    }
    
    public void setCurrentQty(Integer currentQty) {
        this.currentQty = currentQty;
    }
    
    public java.lang.String getWorkplaceCode() {
        return workplaceCode;
    }
    
    public void setWorkplaceCode(java.lang.String workplaceCode) {
        this.workplaceCode = workplaceCode;
    }
    
    public java.lang.String getWorkplaceName() {
        return workplaceName;
    }
    
    public void setWorkplaceName(java.lang.String workplaceName) {
        this.workplaceName = workplaceName;
    }
    
    
}