package com.yunda.jx.wlgl.outwh.entity;

import java.util.Date;

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
 * <li>说明：MatOutWH实体类, 数据表：出库单
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
@Table(name="WLGL_Mat_Out_WH")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatOutWH implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 出库类型 - 出库到机车*/
    public static final String TYPE_CKDJC = "出库到机车";
    /** 出库类型 - 其他原因*/
    public static final String TYPE_QTYY = "其他原因出库";
    /** 出库类型 - 移库出库 */
    public static final String TYPE_YK = "移库出库";
    /** 出库类型 - 借出 */
    public static final String TYPE_JC = "借出";
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
	/* 单价 */
	private Double price;
    /* 单位 */
    private String unit;
	/* 出库人主键 */
	@Column(name="EX_WH_Emp_ID")
	private Long exWhEmpID;
	/* 出库人名称 */
	@Column(name="EX_WH_Emp")
	private String exWhEmp;
	/* 领用人主键 */
	@Column(name="Get_Emp_ID")
	private Long getEmpID;
	/* 领用人名称 */
	@Column(name="Get_Emp")
	private String getEmp;
	/* 领用机构ID */
	@Column(name="Get_Org_ID")
	private Long getOrgID;
	/* 领用机构名称 */
	@Column(name="Get_Org")
	private String getOrg;
	/* 领用机构序列 */
	@Column(name="Get_Org_Seq")
	private String getOrgSeq;
	/* 出库日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="WH_Date")
	private java.util.Date whDate;
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 数量 */
	private Integer qty;
	/* 出库类型（出库到机车、其他原因出库） */
	@Column(name="WH_Type")
	private String wHType;
	/* 物料类型（技改件、消耗件） */
	@Column(name="Mat_Type")
	private String matType;
	/* 出库原因 */
	@Column(name="EX_WH_Reason")
	private String exWhReason;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
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
    /* 库位id */
    @Transient
    private String locationIdx ;
    /* 库位状态*/
    @Transient
    private String status ;
    /* 接收库房主键 */
    @Transient
    private String getWhIDX;
    /* 接收库房名称 */
    @Transient
    private String getWhName;   
    /* 移库单id */
    @Transient
    private String moveIdx;   
    
    /* 物料移入的状态 */
    @Transient
    private String moveStatus;
    public MatOutWH(){ super();}
    public MatOutWH(String moveIdx,String getWhIDX,String getWhName, String matType, String matCode, String matDesc, String unit, Integer qty, String whIDX, 
                    String whName, String locationName, Date whDate, String exWhEmp, String moveStatus){
        this.moveIdx = moveIdx;
        this.getWhIDX = getWhIDX;
        this.getWhName = getWhName;
        this.matType = matType;
        this.matCode = matCode;
        this.matDesc = matDesc;
        this.unit = unit;
        this.qty = qty;
        this.whIDX = whIDX;
        this.whName = whName;
        this.locationName = locationName;
        this.whDate = whDate;
        this.exWhEmp = exWhEmp;
        this.moveStatus = moveStatus;     
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
	 * @return Long 获取出库人主键
	 */
	public Long getExWhEmpID(){
		return exWhEmpID;
	}
	/**
	 * @param exWhEmpID 设置出库人主键
	 */
	public void setExWhEmpID(Long exWhEmpID) {
		this.exWhEmpID = exWhEmpID;
	}
	/**
	 * @return String 获取出库人名称
	 */
	public String getExWhEmp(){
		return exWhEmp;
	}
	/**
	 * @param exWhEmp 设置出库人名称
	 */
	public void setExWhEmp(String exWhEmp) {
		this.exWhEmp = exWhEmp;
	}
	/**
	 * @return Long 获取领用人主键
	 */
	public Long getGetEmpID(){
		return getEmpID;
	}
	/**
	 * @param getEmpID 设置领用人主键
	 */
	public void setGetEmpID(Long getEmpID) {
		this.getEmpID = getEmpID;
	}
	/**
	 * @return String 获取领用人名称
	 */
	public String getGetEmp(){
		return getEmp;
	}
	/**
	 * @param getEmp 设置领用人名称
	 */
	public void setGetEmp(String getEmp) {
		this.getEmp = getEmp;
	}
	/**
	 * @return Long 获取领用机构ID
	 */
	public Long getGetOrgID(){
		return getOrgID;
	}
	/**
	 * @param getOrgID 设置领用机构ID
	 */
	public void setGetOrgID(Long getOrgID) {
		this.getOrgID = getOrgID;
	}
	/**
	 * @return String 获取领用机构名称
	 */
	public String getGetOrg(){
		return getOrg;
	}
	/**
	 * @param getOrg 设置领用机构名称
	 */
	public void setGetOrg(String getOrg) {
		this.getOrg = getOrg;
	}
	/**
	 * @return String 获取领用机构序列
	 */
	public String getGetOrgSeq(){
		return getOrgSeq;
	}
	/**
	 * @param getOrgSeq 设置领用机构序列
	 */
	public void setGetOrgSeq(String getOrgSeq) {
		this.getOrgSeq = getOrgSeq;
	}
	/**
	 * @return java.util.Date 获取出库日期
	 */
	public java.util.Date getWhDate(){
		return whDate;
	}
	/**
	 * @param whDate 设置出库日期
	 */
	public void setWhDate(java.util.Date whDate) {
		this.whDate = whDate;
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
	 * @return String 获取出库类型
	 */
	public String getWHType(){
		return wHType;
	}
	/**
	 * @param wHType 设置出库类型
	 */
	public void setWHType(String wHType) {
		this.wHType = wHType;
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
	 * @return String 获取出库原因
	 */
	public String getExWhReason(){
		return exWhReason;
	}
	/**
	 * @param exWhReason 设置出库原因
	 */
	public void setExWhReason(String exWhReason) {
		this.exWhReason = exWhReason;
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

    
    public String getRtID() {
        return rtID;
    }

    
    public void setRtID(String rtID) {
        this.rtID = rtID;
    }

    
    public String getRtName() {
        return rtName;
    }

    
    public void setRtName(String rtName) {
        this.rtName = rtName;
    }

    
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }

    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }

    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }

    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }

    
    public String getXcID() {
        return xcID;
    }

    
    public void setXcID(String xcID) {
        this.xcID = xcID;
    }

    
    public String getXcName() {
        return xcName;
    }

    
    public void setXcName(String xcName) {
        this.xcName = xcName;
    }

    
    public String getGetWhIDX() {
        return getWhIDX;
    }

    
    public void setGetWhIDX(String getWhIDX) {
        this.getWhIDX = getWhIDX;
    }

    
    public String getGetWhName() {
        return getWhName;
    }

    
    public void setGetWhName(String getWhName) {
        this.getWhName = getWhName;
    }

    
    public String getUnit() {
        return unit;
    }

    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getMoveStatus() {
        return moveStatus;
    }
    
    public void setMoveStatus(String moveStatus) {
        this.moveStatus = moveStatus;
    }
    
    public String getMoveIdx() {
        return moveIdx;
    }
    
    public void setMoveIdx(String moveIdx) {
        this.moveIdx = moveIdx;
    }
    
  
}