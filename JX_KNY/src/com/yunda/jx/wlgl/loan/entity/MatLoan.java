package com.yunda.jx.wlgl.loan.entity;

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
 * <li>说明：MatLoan实体类, 数据表：借出归还单
 * <li>创建人：程梅
 * <li>创建日期：2016-05-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="WLGL_Mat_LOAN")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatLoan implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料类型（技改件、消耗件） */
	@Column(name="Mat_Type")
	private String matType;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
	private String unit;
    /* 借出单位 */
    @Column(name="loan_org")
    private String loanOrg;
    /* 借出数量 */
    @Column(name="loan_qty")
    private Integer loanQty;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
    
    /* 库房主键 */
    @Transient
    private String whIDX;
    /* 库房名称 */
    @Transient
    private String whName;
    /* 库位id */
    @Transient
    private String locationIdx;
    /* 库位 */
    @Transient
    private String locationName;
    /* 库位状态 */
    @Transient
    private String status;

 
	/**
	 * @return String 获取物料编码
	 */
	public String getMatCode(){
		return matCode;
	}
	/**
	 * @param matCode 设置物料编码
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	/**
	 * @return String 获取物料类型
	 */
	public String getMatType(){
		return matType;
	}
	/**
	 * @param matType 设置物料类型
	 */
	public void setMatType(String matType) {
		this.matType = matType;
	}
	/**
	 * @return String 获取物料描述
	 */
	public String getMatDesc(){
		return matDesc;
	}
	/**
	 * @param matDesc 设置物料描述
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
	 * @param unit 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	/**
	 * @param siteID 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public String getLoanOrg() {
        return loanOrg;
    }
    
    public void setLoanOrg(String loanOrg) {
        this.loanOrg = loanOrg;
    }
    
    public Integer getLoanQty() {
        return loanQty;
    }
    
    public void setLoanQty(Integer loanQty) {
        this.loanQty = loanQty;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getWhIDX() {
        return whIDX;
    }
    
    public void setWhIDX(String whIDX) {
        this.whIDX = whIDX;
    }
    
    public String getWhName() {
        return whName;
    }
    
    public void setWhName(String whName) {
        this.whName = whName;
    }
    
    public String getLocationIdx() {
        return locationIdx;
    }
    
    public void setLocationIdx(String locationIdx) {
        this.locationIdx = locationIdx;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}