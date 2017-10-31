package com.yunda.jx.jxgc.producttaskmanage.entity;

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
 * <li>说明：DisposeOpinion实体类, 数据表：处理意见
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * 
 */
@Entity
@Table(name="JXGC_Dispose_Opinion")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class DisposeOpinion implements java.io.Serializable{
	
	/** 签名类型-必检项签名*/
	public static final String SIGN_TYPE_MUST_CHECK = "MUST_CHECK";
	/** 签名类型-审核转临修 */
	//TODO 20140328汪东良：请明确此常量是否在使用，如果没有使用请清理掉 DisposeOpinionManager204行已注释代码引用，如该代码无效则可清除
	public static final String SING_TYPE_VERIFY_TOLX = "VERITY_TO_LX";
	/** 签名类型 - 提票开工签名 */
	public static final String SIGN_TYPE_FAULT_WORKING = "FAULT_WORKING";
	/** 签名类型 - 提票质检 */
	public static final String SIGN_TYPE_FAULT_QC = "FAULT_QUALITY_CHECK";
	
	
	/** 签名类型 - 开始上砂*/
	public static final String SIGN_TYPE_START_SANDING = "START_SANDING";
	
	/** 签名类型 - 结束上砂*/
	public static final String SIGN_TYPE_END_SANDING = "END_SANDING";
	

	/** 签名类型 - 驳回退票*/
	public static final String SIGN_TYPE_CANCEL_TP = "CANCEL_TP";
	
	/** 签名类型 - 同意退票*/
	public static final String SIGN_TYPE_APPLY_TP = "APPLY_TP";
	
	/** 签名类型 - 专修退票*/
	public static final String SIGN_TYPE_ZX_TP = "ZX_TP";
	
	/** 签名类型 - 专检退票*/
	public static final String SIGN_TYPE_ZJ_TP = "ZJ_TP";
	
	/** 签名类型 - 申请退票*/
	public static final String SIGN_TYPE_ASK_TP = "ASK_TP";
	
	/** 签名类型 - 工人申请退票*/
	public static final String SIGN_TYPE_WORKER_ASK_TP = "WORKER_ASK_TP";
	
	/** 签名类型 - 工人申请临修*/
	public static final String SIGN_TYPE_WORKER_ASK_LX = "WORKER_ASK_LX";
	
	/** 签名类型-整备工长申请转临修*/
	public static final String SIGN_TYPE_ASK_LX = "ASK_LX";
	
	/** 签名类型-整备同意转临修*/
	public static final String SIGN_TYPE_PASS_LX = "PASS_LX";
	
	/** 签名类型-整备取消转临修*/
	public static final String SIGN_TYPE_CANCEL_LX = "CANCEL_LX";	
	/** 签名类型-提票车间调度审核 程锐添加2013-12-30*/
	public static final String SIGN_TYPE_FAULT_VERTIFY = "FAULT_VERTIFY";
	/** 签名类型-提票技术科技术鉴定 程锐添加2013-12-30*/
	public static final String SIGN_TYPE_FAULT_TECVERTIFY = "FAULT_TECVERTIFY";
		
	/** 技术指令措施审核 */
	public static final String SIGN_TYPE_MEASURE_VERTIFY = "MEASURE_VERTIFY";
	
	/** 技术指令措施完工 */
	public static final String SIGN_TYPE_MEASURE_WG = "MEASURE_WG";
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 业务主键（作业卡、质量控制等业务主键） */
	@Column(name="Business_IDX")
	private String businessIDX;
	/* 签名类型*/
	@Column(name="SIGN_TYPE")
	private String signType;
	/* 流程实例ID*/
	@Column(name="PROCESSINSTID")
	private Long processInstID;
	/* 活动实例ID*/
	@Column(name="ACTIVITYINSTID")
	private Long activityInstID;
	/* 工作项ID*/
	@Column(name="WORKITEMID")
	private Long workItemID;
	/* 工作项名称 */
	@Column(name="Work_Item_Name")
	private String workItemName;
	/* 处理项编码 */
	@Column(name="Dispose_Item_Code")
	private String disposeItemCode;
	/* 处理人姓名 */
	@Column(name="Dispose_Person")
	private String disposePerson;
	/* 处理人operatorId*/
	@Column(name="DISPOSE_PERSON_ID")
	private String disposePersonID;
	/* 处理时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Dispose_Time")
	private java.util.Date disposeTime;
    
    @Column(name="Dispose_Idea")
    private String disposeIdea;
	/* 处理意见描述 */
	@Column(name="Dispose_Opinion")
	private String disposeOpinion;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private java.util.Date updateTime;
	/* 未签名人班组名称*/
	@Transient
	private String orgName;
	/* 标识来源*/
	@Transient
	private String fromMark;
	/* 搭载业务主键 */
	@Transient
	private String carryBizIdx;
	/* 处理业务主键 */
	@Transient
	private String bizIdx;
	/* 出段时间 */
	@Transient
	private Date outTime;
	/* 兑现单主键 */
	@Transient
	private String rdpIdx;
	/* 业务签名类型 */
	@Transient
	private String bizType;
	/* 业务条件 */
	@Transient
	private Integer bizCondition;
	/* 处理时间字符串 */
	@Transient
	private String disposeTimeStr;
	/*处理提票主键*/
	@Transient
	private String faultIdx;

	/**
	 * @return String 获取业务主键
	 */
	public String getBusinessIDX(){
		return businessIDX;
	}
	/**
	 * @param 设置业务主键
	 */
	public void setBusinessIDX(String businessIDX) {
		this.businessIDX = businessIDX;
	}
	/**
	 * @return String 获取工作项名称
	 */
	public String getWorkItemName(){
		return workItemName;
	}
	/**
	 * @param 设置工作项名称
	 */
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
	/**
	 * @return String 获取处理项编码
	 */
	public String getDisposeItemCode(){
		return disposeItemCode;
	}
	/**
	 * @param 设置处理项编码
	 */
	public void setDisposeItemCode(String disposeItemCode) {
		this.disposeItemCode = disposeItemCode;
	}
	/**
	 * @return String 获取处理人
	 */
	public String getDisposePerson(){
		return disposePerson;
	}
	/**
	 * @param 设置处理人
	 */
	public void setDisposePerson(String disposePerson) {
		this.disposePerson = disposePerson;
	}
	/**
	 * @return java.util.Date 获取处理时间
	 */
	public java.util.Date getDisposeTime(){
		return disposeTime;
	}
	/**
	 * @param 设置处理时间
	 */
	public void setDisposeTime(java.util.Date disposeTime) {
		this.disposeTime = disposeTime;
	}
	/**
	 * @return String 获取处理意见
	 */
	public String getDisposeOpinion(){
		return disposeOpinion;
	}
	/**
	 * @param 设置处理意见
	 */
	public void setDisposeOpinion(String disposeOpinion) {
		this.disposeOpinion = disposeOpinion;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	/**
	 * @param 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
		return creator;
	}
	/**
	 * @param 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime(){
		return createTime;
	}
	/**
	 * @param 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator(){
		return updator;
	}
	/**
	 * @param 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime(){
		return updateTime;
	}
	/**
	 * @param 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
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
	
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
		
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
    
    public Long getActivityInstID() {
        return activityInstID;
    }
    
    public void setActivityInstID(Long activityInstID) {
        this.activityInstID = activityInstID;
    }
    
    public Long getWorkItemID() {
        return workItemID;
    }
    
    public void setWorkItemID(Long workItemID) {
        this.workItemID = workItemID;
    }
    
    public String getDisposePersonID() {
        return disposePersonID;
    }
    
    public void setDisposePersonID(String disposePersonID) {
        this.disposePersonID = disposePersonID;
    }
    
    public Long getProcessInstID() {
        return processInstID;
    }
    
    public void setProcessInstID(Long processInstID) {
        this.processInstID = processInstID;
    }
	public String getFromMark() {
		return fromMark;
	}
	public void setFromMark(String fromMark) {
		this.fromMark = fromMark;
	}
    
    public String getCarryBizIdx() {
        return carryBizIdx;
    }
    
    public void setCarryBizIdx(String carryBizIdx) {
        this.carryBizIdx = carryBizIdx;
    }
    
    public String getBizIdx() {
        return bizIdx;
    }
    
    public void setBizIdx(String bizIdx) {
        this.bizIdx = bizIdx;
    }
    
    public Date getOutTime() {
        return outTime;
    }
    
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
    
    public String getRdpIdx() {
        return rdpIdx;
    }
    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public String getBizType() {
        return bizType;
    }
    
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
    public String getFaultIdx() {
		return faultIdx;
	}
	public void setFaultIdx(String faultIdx) {
		this.faultIdx = faultIdx;
	}
	/**
     * <li>方法说明：获取业务逻辑条件 
     * <li>方法名称：getBizCondition
     * <li>@return
     * <li>return: Integer
     * <li>创建人：张凡
     * <li>创建时间：2013-8-5 上午10:35:20
     * <li>修改人：
     * <li>修改内容：
     */
    public Integer getBizCondition() {
        return bizCondition;
    }
    
    public void setBizCondition(Integer bizCondition) {
        this.bizCondition = bizCondition;
    }
    
    public String getDisposeTimeStr() {
        return disposeTimeStr;
    }
    
    public void setDisposeTimeStr(String disposeTimeStr) {
        this.disposeTimeStr = disposeTimeStr;
    }
    
    public String getDisposeIdea() {
        return disposeIdea;
    }
    
    public void setDisposeIdea(String disposeIdea) {
        this.disposeIdea = disposeIdea;
    }
    public DisposeOpinion(){
    }
    public DisposeOpinion(String disposeIdea){
        this.disposeIdea = disposeIdea;
    }
}