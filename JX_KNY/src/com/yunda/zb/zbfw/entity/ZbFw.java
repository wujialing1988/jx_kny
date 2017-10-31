package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFw实体类, 数据表：整备范围
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBFW")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbFw implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 整备作业流程编码规则 */
    public static final String CODE_RULE_JOB_PROCESS_CODE = "ZBGL_JOB_PROCESS_DEF_CODE";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 编号 */
	@Column(name="FW_Code")
	private String fwCode;
	/* 名称 */
	@Column(name="FW_Name")
	private String fwName;
	/* 描述 */
	@Column(name="FW_Desc")
	private String fwDesc;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站场 */
	@Column(updatable=false)
	private String siteID;
    /* 车型主键 */
    @Column(name="Train_Type_IDX")
    private String trainTypeIDX;
    /* 车型名称 */
    @Column(name="Train_Type_Name")
    private String trainTypeName;
    
    /* 车型简称 */
    @Column(name="Train_Type_Short_Name")
    private String trainTypeShortName;
    
    /** 列检适用车型及适用作业性质 **/
    /* 适用车型名称 */
    @Column(name="Train_Vehicle_Name")
    private String trainVehicleName;
    
    /* 适用车型编码 */
    @Column(name="Train_Vehicle_Code")
    private String trainVehicleCode;
    
    /* 适用作业性质 编码 */
    @Column(name = "WORK_NATURE_CODE")
    private String workNatureCode;
    
    /* 适用作业性质 到达作业、始发作业、中转作业、通过作业 */
    @Column(name = "WORK_NATURE")
    private String workNature;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
	
	/**
	 * 
	 * <li>说明：默认构造方法
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public ZbFw(){}
	/**
	 * 
	 * <li>说明：整备范围列表实体构造方法
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param idx 主键
	 * @param fwCode 编号
	 * @param fwName 范围名称
	 * @param fwDesc 范围描述
	 * @param trainTypeShortName 车型简称
	 * @param trainTypeIDX 车型主键
	 * @param siteID 站场标识
	 * 
	 */
	public ZbFw(String idx,String fwCode,String fwName,String fwDesc,String trainTypeShortName,String trainTypeIDX,String siteID){
		super();
		this.idx = idx;
		this.fwCode = fwCode;
		this.fwName = fwName;
		this.fwDesc = fwDesc;
		this.trainTypeShortName = trainTypeShortName;
		this.trainTypeIDX = trainTypeIDX;
		this.siteID = siteID;
	}
   
	/**
	 * @return String 获取编号
	 */
	public String getFwCode(){
		return fwCode;
	}
	public void setFwCode(String fwCode) {
		this.fwCode = fwCode;
	}
	/**
	 * @return String 获取名称
	 */
	public String getFwName(){
		return fwName;
	}
	public void setFwName(String fwName) {
		this.fwName = fwName;
	}
	/**
	 * @return String 获取描述
	 */
	public String getFwDesc(){
		return fwDesc;
	}
	public void setFwDesc(String fwDesc) {
		this.fwDesc = fwDesc;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String 获取站场
	 */
	public String getSiteID(){
		return siteID;
	}
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	
    public String getTrainTypeName() {
        return trainTypeName;
    }
    
    public void setTrainTypeName(String trainTypeName) {
        this.trainTypeName = trainTypeName;
    }
    /**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
    
    public String getTrainVehicleCode() {
        return trainVehicleCode;
    }
    
    public void setTrainVehicleCode(String trainVehicleCode) {
        this.trainVehicleCode = trainVehicleCode;
    }
    
    public String getTrainVehicleName() {
        return trainVehicleName;
    }
    
    public void setTrainVehicleName(String trainVehicleName) {
        this.trainVehicleName = trainVehicleName;
    }
    
    public String getWorkNature() {
        return workNature;
    }
    
    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }
    
    public String getWorkNatureCode() {
        return workNatureCode;
    }
    
    public void setWorkNatureCode(String workNatureCode) {
        this.workNatureCode = workNatureCode;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
    
	
}