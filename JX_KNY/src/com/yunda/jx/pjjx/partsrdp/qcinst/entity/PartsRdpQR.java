package com.yunda.jx.pjjx.partsrdp.qcinst.entity;

import java.io.Serializable;

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
 * <li>说明：PartsRdpQR实体类, 数据表：质量检查结果
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_Parts_Rdp_QR")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpQR implements java.io.Serializable{
	
	/** 状态 - 待处理 */
	public static final String CONST_STR_STATUS_DCL = "01";
	/** 状态 - 已处理 */
	public static final String CONST_STR_STATUS_YCL = "02";
    /** 状态 - 已终止 */
	public static final String CONST_STR_STATUS_YZZ = "03";
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 关联主键 */
	@Column(name="Rel_IDX")
	private String relIDX;
	/* 记录卡实例主键 */
	@Column(name="Rdp_Record_Card_IDX")
	private String rdpRecordCardIDX;
	/* 质量检查项主键 */
	@Column(name="QC_Item_IDX")
	private String qCItemIDX;
	/* 检查项编码 */
	@Column(name="QC_Item_No")
	private String qCItemNo;
	/* 检查项名称 */
	@Column(name="QC_Item_Name")
	private String qCItemName;
	/* 检验方式,抽检/必检（1：抽检；2：必检）默认为1 */
	@Column(name="Check_Way")
	private Integer checkWay;
	/* 是否指派 */
	@Column(name="Is_Assign")
	private Integer isAssign;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 检验人员 */
	@Column(name="QR_EmpID")
	private Long qREmpID;
	/* 检验人员名称 */
	@Column(name="QR_EmpName")
	private String qREmpName;
	/* 检验结果 */
	@Column(name="QR_Result")
	private String qRResult;
	/* 检验时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="QR_Time")
	private java.util.Date qRTime;
	/* 状态 */
	private String status;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
	
	@Transient
	private String recordCardDesc;
    
    /* （配件检修质检）是否可以进行提交 */
    @Transient
    private String isCanSubmit ;
	

	/**
	 * @return String 获取关联主键
	 */
	public String getRelIDX(){
		return relIDX;
	}
	/**
	 * @param relIDX 设置关联主键
	 */
	public void setRelIDX(String relIDX) {
		this.relIDX = relIDX;
	}
	/**
	 * @return String 获取记录卡实例主键
	 */
	public String getRdpRecordCardIDX(){
		return rdpRecordCardIDX;
	}
	/**
	 * @param rdpRecordCardIDX 设置记录卡实例主键
	 */
	public void setRdpRecordCardIDX(String rdpRecordCardIDX) {
		this.rdpRecordCardIDX = rdpRecordCardIDX;
	}
	/**
	 * @return String 获取质量检查项主键
	 */
	public String getQCItemIDX(){
		return qCItemIDX;
	}
	/**
	 * @param qCItemIDX 设置质量检查项主键
	 */
	public void setQCItemIDX(String qCItemIDX) {
		this.qCItemIDX = qCItemIDX;
	}
	/**
	 * @return String 获取检查项编码
	 */
	public String getQCItemNo(){
		return qCItemNo;
	}
	/**
	 * @param qCItemNo 设置检查项编码
	 */
	public void setQCItemNo(String qCItemNo) {
		this.qCItemNo = qCItemNo;
	}
	/**
	 * @return String 获取检查项名称
	 */
	public String getQCItemName(){
		return qCItemName;
	}
	/**
	 * @param qCItemName 设置检查项名称
	 */
	public void setQCItemName(String qCItemName) {
		this.qCItemName = qCItemName;
	}
	/**
	 * @return Integer 获取检验方式
	 */
	public Integer getCheckWay(){
		return checkWay;
	}
	/**
	 * @param checkWay 设置检验方式
	 */
	public void setCheckWay(Integer checkWay) {
		this.checkWay = checkWay;
	}
	/**
	 * @return Integer 获取是否指派
	 */
	public Integer getIsAssign(){
		return isAssign;
	}
	/**
	 * @param isAssign 设置是否指派
	 */
	public void setIsAssign(Integer isAssign) {
		this.isAssign = isAssign;
	}
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	/**
	 * @param seqNo 设置顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return Long 获取检验人员
	 */
	public Long getQREmpID(){
		return qREmpID;
	}
	/**
	 * @param qREmpID 设置检验人员
	 */
	public void setQREmpID(Long qREmpID) {
		this.qREmpID = qREmpID;
	}
	/**
	 * @return String 获取检验人员名称
	 */
	public String getQREmpName(){
		return qREmpName;
	}
	/**
	 * @param qREmpName 设置检验人员名称
	 */
	public void setQREmpName(String qREmpName) {
		this.qREmpName = qREmpName;
	}
	/**
	 * @return String 获取检验结果
	 */
	public String getQRResult(){
		return qRResult;
	}
	/**
	 * @param qRResult 设置检验结果
	 */
	public void setQRResult(String qRResult) {
		this.qRResult = qRResult;
	}
	/**
	 * @return java.util.Date 获取检验时间
	 */
	public java.util.Date getQRTime(){
		return qRTime;
	}
	/**
	 * @param qRTime 设置检验时间
	 */
	public void setQRTime(java.util.Date qRTime) {
		this.qRTime = qRTime;
	}
	/**
	 * @return String 获取状态
	 */
	public String getStatus(){
		return status;
	}
	/**
	 * @param status 设置状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录的状态
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
	 * @param siteID 设置站点标识
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
	 * @param creator 设置创建人
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
	 * @param createTime 设置创建时间
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
	 * @param updator 设置修改人
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
	 * @param updateTime 设置修改时间
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
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
	
	/**
	 * <li>标题：机车检修管理信息系统
	 * <li>说明：待检验工单分组查询实体
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-1-16 下午04:50:12
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * 
	 * @author 测控部检修系统项目组
	 * @version 1.0
	 */
	@Entity
	@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
	public static class PartsRdpSearcher implements Serializable {
		private static final long serialVersionUID = 1L;
		@Id
		private String rownum;
		/* 待检验工单数量 */
		private int qty;
		/* 作业主键 */
		@Column(name="RDP_IDX")
		private String rdpIDX;
		/* 配件编号 */
		@Column(name="PARTS_NO")
		private String partsNo;
		/* 配件名称 */
		@Column(name="PARTS_NAME")
		private String partsName;
		/* 规格型号 */
		@Column(name="SPECIFICATION_MODEL")
		private String specificationModel;
		/* 物料编码 */
		@Column(name="MAT_CODE")
		private String matCode;
		/* 承修班组 */
		@Column(name="Repair_OrgID")
		private Long repairOrgID;
		/* 承修班组名称 */
		@Column(name="Repair_OrgName")
		private String repairOrgName;
		/* 承修班组序列 */
		@Column(name="Repair_OrgSeq")
		private String repairOrgSeq;
		/* 检修负责人 */
		@Column(name="Duty_EmpID")
		private Long dutyEmpID;
		/* 检修负责人名称 */
		@Column(name="Duty_EmpName")
		private String dutyEmpName;
		/* 检查项编码 */
		@Column(name="QC_Item_No")
		private String qcItemNo;
		/* 检查项名称 */
		@Column(name="QC_Item_Name")
		private String qcItemName;
		/* 下车车型 */
		@Column(name="UNLOAD_TRAINTYPE")
		private String unloadTrainType;
		/* 下车车号 */
		@Column(name="UNLOAD_TRAINNO")
		private String unloadTrainNo;
		/* 下车修程 */
		@Column(name="UNLOAD_REPAIR_CLASS")
		private String unloadRepairClass;
		/* 下车修次 */
		@Column(name="UNLOAD_REPAIR_TIME")
		private String unloadRepairTime;
		/* 扩展编号 */
		private String extendNo;
		/* 计划开始时间 */
		@Column(name="Plan_StartTime")
		private String planStartTime;
		/* 计划结束时间 */
		@Column(name="Plan_EndTime")
		private String planEndTime;
        
        /* 配件识别码 */
        @Column(name="IDENTIFICATION_CODE")
        private String identificationCode;
        
        /* 下车位置*/
        @Column(name="UNLOAD_PLACE")
        private String unloadPlace;
		
        /* 质检类型 */
        @Column(name="CHECK_WAY")
        private String checkWay;
        
        /* 配件信息主键 */
		@Column(name="PARTS_ACCOUNT_IDX")
		private String partsAccountIDX;
        
		public String getPartsAccountIDX() {
			return partsAccountIDX;
		}
		public void setPartsAccountIDX(String partsAccountIDX) {
			this.partsAccountIDX = partsAccountIDX;
		}
		public Long getDutyEmpID() {
			return dutyEmpID;
		}
		public void setDutyEmpID(Long dutyEmpID) {
			this.dutyEmpID = dutyEmpID;
		}
		public String getDutyEmpName() {
			return dutyEmpName;
		}
		public void setDutyEmpName(String dutyEmpName) {
			this.dutyEmpName = dutyEmpName;
		}
		public String getExtendNo() {
			return extendNo;
		}
		public void setExtendNo(String extendNo) {
			this.extendNo = extendNo;
		}
		public String getMatCode() {
			return matCode;
		}
		public void setMatCode(String matCode) {
			this.matCode = matCode;
		}
		public String getPartsName() {
			return partsName;
		}
		public void setPartsName(String partsName) {
			this.partsName = partsName;
		}
		public String getRdpIDX() {
			return rdpIDX;
		}
		public void setRdpIDX(String rdpIDX) {
			this.rdpIDX = rdpIDX;
		}
		public String getPartsNo() {
			return partsNo;
		}
		public void setPartsNo(String partsNo) {
			this.partsNo = partsNo;
		}
		public String getPlanEndTime() {
			return planEndTime;
		}
		public void setPlanEndTime(String planEndTime) {
			this.planEndTime = planEndTime;
		}
		public String getPlanStartTime() {
			return planStartTime;
		}
		public void setPlanStartTime(String planStartTime) {
			this.planStartTime = planStartTime;
		}
		public String getQcItemName() {
			return qcItemName;
		}
		public void setQcItemName(String qcItemName) {
			this.qcItemName = qcItemName;
		}
		public String getQcItemNo() {
			return qcItemNo;
		}
		public void setQcItemNo(String qcItemNo) {
			this.qcItemNo = qcItemNo;
		}
		public int getQty() {
			return qty;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public Long getRepairOrgID() {
			return repairOrgID;
		}
		public void setRepairOrgID(Long repairOrgID) {
			this.repairOrgID = repairOrgID;
		}
		public String getRepairOrgName() {
			return repairOrgName;
		}
		public void setRepairOrgName(String repairOrgName) {
			this.repairOrgName = repairOrgName;
		}
		public String getRepairOrgSeq() {
			return repairOrgSeq;
		}
		public void setRepairOrgSeq(String repairOrgSeq) {
			this.repairOrgSeq = repairOrgSeq;
		}
		public String getRownum() {
			return rownum;
		}
		public void setRownum(String rownum) {
			this.rownum = rownum;
		}
		public String getSpecificationModel() {
			return specificationModel;
		}
		public void setSpecificationModel(String specificationModel) {
			this.specificationModel = specificationModel;
		}
		public String getUnloadRepairClass() {
			return unloadRepairClass;
		}
		public void setUnloadRepairClass(String unloadRepairClass) {
			this.unloadRepairClass = unloadRepairClass;
		}
		public String getUnloadRepairTime() {
			return unloadRepairTime;
		}
		public void setUnloadRepairTime(String unloadRepairTime) {
			this.unloadRepairTime = unloadRepairTime;
		}
		public String getUnloadTrainNo() {
			return unloadTrainNo;
		}
		public void setUnloadTrainNo(String unloadTrainNo) {
			this.unloadTrainNo = unloadTrainNo;
		}
		public String getUnloadTrainType() {
			return unloadTrainType;
		}
		public void setUnloadTrainType(String unloadTrainType) {
			this.unloadTrainType = unloadTrainType;
		}
        
        public String getIdentificationCode() {
            return identificationCode;
        }
        
        public void setIdentificationCode(String identificationCode) {
            this.identificationCode = identificationCode;
        }
        
		public String getCheckWay() {
			return checkWay;
		}
		public void setCheckWay(String checkWay) {
			this.checkWay = checkWay;
		}
		public String getUnloadPlace() {
			return unloadPlace;
		}
		public void setUnloadPlace(String unloadPlace) {
			this.unloadPlace = unloadPlace;
		}
        
	}

	public String getRecordCardDesc() {
		return recordCardDesc;
	}
	public void setRecordCardDesc(String recordCardDesc) {
		this.recordCardDesc = recordCardDesc;
	}
    
    public String getIsCanSubmit() {
        return isCanSubmit;
    }
    
    public void setIsCanSubmit(String isCanSubmit) {
        this.isCanSubmit = isCanSubmit;
    }
	
}