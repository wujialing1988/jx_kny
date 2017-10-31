package com.yunda.jx.wlgl.inwh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatInWHNew实体类, 数据表：入库单
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
@Table(name="WLGL_Mat_In_WH_New")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatInWHNew implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 入库类型 - 新品入库*/
    public static final String TYPE_XPRK = "新品入库";
    /** 入库类型 - 机车退回*/
    public static final String TYPE_JCTH = "机车退回";
    /** 入库类型 - 其他原因*/
    public static final String TYPE_QTYY = "其他原因";
    /** 入库类型 - 移库 */
    public static final String TYPE_YK = "移库";
    /** 入库类型 - 移库退回*/
    public static final String TYPE_YKTH = "移库退回";
    /** 入库类型 - 归还*/
    public static final String TYPE_GH = "归还";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 库房主键 */
	@Column(name="WH_IDX")
	private String whIDX;
	/* 库房名称 */
	@Column(name="WH_Name")
	private String whName;
	/* 库位 */
	@Column(name="LOCATION_NAME")
	private String locationName;
	/* 入库人主键 */
	@Column(name="IN_WH_EMP_ID")
	private Long inWhEmpID;
	/* 入库人名称 */
	@Column(name="IN_WH_EMP")
	private String inWhEmp;
	/* 入库日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="IN_WH_DATE")
	private java.util.Date inWhDate;
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
	/* 单价 */
	private Double price;
	/* 数量 */
	private Integer qty;
	/* 交件班组主键 */
	@Column(name="HAND_OVER_ORG_ID")
	private Long handOverOrgID;
	/* 交件班组名称 */
	@Column(name="HAND_OVER_ORG")
	private String handOverOrg;
    /* 交件班组序列 */
    @Column(name = "HAND_OVER_ORG_Seq")
    private String handOverOrgSeq;
	/* 交件人主键 */
	@Column(name="HAND_OVER_EMP_ID")
	private Long handOverEmpID;
	/* 交件人名称 */
	@Column(name="HAND_OVER_EMP")
	private String handOverEmp;
	/* 车型编码 */
	@Column(name="TrainType_IDX")
	private String trainTypeIDX;
	/* 车型简称 */
	@Column(name="TrainType_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	private String trainNo;
	/* 修程编码 */
	@Column(name="XC_ID")
	private String xcID;
	/* 修程名称 */
	@Column(name="XC_Name")
	private String xcName;
	/* 修次编码 */
	@Column(name="RT_ID")
	private String rtID;
	/* 修次名称 */
	@Column(name="RT_Name")
	private String rtName;
	/* 入库原因 */
	@Column(name="IN_WH_Reason")
	private String inWhReason;
	/* 入库类型（机车退回、其他原因、移库、移库退回） */
	@Column(name="IN_WH_Type")
	private String inWhType;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 库位id */
    @Transient
    private String locationIdx ;
    /* 库位状态*/
    @Transient
    private String status ;
	/**
	 * @return String 获取库房主键
	 */
	public String getWhIDX(){
		return whIDX;
	}
	/**
	 * @param whIDX 设置库房主键
	 */
	public void setWhIDX(String whIDX) {
		this.whIDX = whIDX;
	}
	/**
	 * @return String 获取库房名称
	 */
	public String getWhName(){
		return whName;
	}
	/**
	 * @param whName 设置库房名称
	 */
	public void setWhName(String whName) {
		this.whName = whName;
	}
	/**
	 * @return String 获取库位
	 */
	public String getLocationName(){
		return locationName;
	}
	/**
	 * @param locationName 设置库位
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * @return Long 获取入库人主键
	 */
	public Long getInWhEmpID(){
		return inWhEmpID;
	}
	/**
	 * @param inWhEmpID 设置入库人主键
	 */
	public void setInWhEmpID(Long inWhEmpID) {
		this.inWhEmpID = inWhEmpID;
	}
	/**
	 * @return String 获取入库人名称
	 */
	public String getInWhEmp(){
		return inWhEmp;
	}
	/**
	 * @param inWhEmp 设置入库人名称
	 */
	public void setInWhEmp(String inWhEmp) {
		this.inWhEmp = inWhEmp;
	}
	/**
	 * @return java.util.Date 获取入库日期
	 */
	public java.util.Date getInWhDate(){
		return inWhDate;
	}
	/**
	 * @param inWhDate 设置入库日期
	 */
	public void setInWhDate(java.util.Date inWhDate) {
		this.inWhDate = inWhDate;
	}
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
	 * @return Double 获取单价
	 */
	public Double getPrice(){
		return price;
	}
	/**
	 * @param price 设置单价
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return Integer 获取数量
	 */
	public Integer getQty(){
		return qty;
	}
	/**
	 * @param qty 设置数量
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	/**
	 * @return Long 获取交件班组主键
	 */
	public Long getHandOverOrgID(){
		return handOverOrgID;
	}
	/**
	 * @param handOverOrgID 设置交件班组主键
	 */
	public void setHandOverOrgID(Long handOverOrgID) {
		this.handOverOrgID = handOverOrgID;
	}
	/**
	 * @return String 获取交件班组名称
	 */
	public String getHandOverOrg(){
		return handOverOrg;
	}
	/**
	 * @param handOverOrg 设置交件班组名称
	 */
	public void setHandOverOrg(String handOverOrg) {
		this.handOverOrg = handOverOrg;
	}
	/**
	 * @return Long 获取交件人主键
	 */
	public Long getHandOverEmpID(){
		return handOverEmpID;
	}
	/**
	 * @param handOverEmpID 设置交件人主键
	 */
	public void setHandOverEmpID(Long handOverEmpID) {
		this.handOverEmpID = handOverEmpID;
	}
	/**
	 * @return String 获取交件人名称
	 */
	public String getHandOverEmp(){
		return handOverEmp;
	}
	/**
	 * @param handOverEmp 设置交件人名称
	 */
	public void setHandOverEmp(String handOverEmp) {
		this.handOverEmp = handOverEmp;
	}
	/**
	 * @return String 获取车型编码
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	/**
	 * @param trainTypeIDX 设置车型编码
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	/**
	 * @param trainTypeShortName 设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}
	/**
	 * @param trainNo 设置车号
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/**
	 * @return String 获取修程编码
	 */
	public String getXcID(){
		return xcID;
	}
	/**
	 * @param xcID 设置修程编码
	 */
	public void setXcID(String xcID) {
		this.xcID = xcID;
	}
	/**
	 * @return String 获取修程名称
	 */
	public String getXcName(){
		return xcName;
	}
	/**
	 * @param xcName 设置修程名称
	 */
	public void setXcName(String xcName) {
		this.xcName = xcName;
	}
	/**
	 * @return String 获取修次编码
	 */
	public String getRtID(){
		return rtID;
	}
	/**
	 * @param rtID 设置修次编码
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
	 * @param rtName 设置修次名称
	 */
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
	/**
	 * @return String 获取入库原因
	 */
	public String getInWhReason(){
		return inWhReason;
	}
	/**
	 * @param inWhReason 设置入库原因
	 */
	public void setInWhReason(String inWhReason) {
		this.inWhReason = inWhReason;
	}
	/**
	 * @return String 获取入库类型
	 */
	public String getInWhType(){
		return inWhType;
	}
	/**
	 * @param inWhType 设置入库类型
	 */
	public void setInWhType(String inWhType) {
		this.inWhType = inWhType;
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
    
    public String getHandOverOrgSeq() {
        return handOverOrgSeq;
    }
    
    public void setHandOverOrgSeq(String handOverOrgSeq) {
        this.handOverOrgSeq = handOverOrgSeq;
    }
}