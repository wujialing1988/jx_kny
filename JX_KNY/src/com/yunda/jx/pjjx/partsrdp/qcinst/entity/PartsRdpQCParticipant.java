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
 * <li>说明：PartsRdpQCParticipant实体类, 数据表：质量可检查人员
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
@Table(name="PJJX_Parts_Rdp_QC_Participant")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpQCParticipant implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 记录卡实例主键 */
	@Column(name="Rdp_Record_Card_IDX")
	private String rdpRecordCardIDX;
	/* 检查项编码 */
	@Column(name="QC_Item_No")
	private String qCItemNo;
	/* 检验人员 */
	@Column(name="QC_EmpID")
	private Long qCEmpID;
	/* 检验人员名称 */
	@Column(name="QC_EmpName")
	private String qCEmpName;
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
    /* 检查项名称 */
    private String qCItemName;
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-12-8
     * <li>修改人： 
     * <li>修改日期：
     */
    public PartsRdpQCParticipant() {
        super();
    }
    
    /**
     * <li>说明：构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-12-8
     * <li>修改人： 
     * <li>修改日期：
     * @param idx idx主键
     * @param rdpIDX 作业主键
     * @param rdpRecordCardIDX 记录卡实例主键
     * @param qCItemNo 检查项编码
     * @param qCEmpID 检验人员
     * @param qCEmpName 检验人员名称
     * @param qCItemName 检查项名称
     */
	public PartsRdpQCParticipant(String idx, String rdpIDX, String rdpRecordCardIDX, String qCItemNo, Long qCEmpID, String qCEmpName, String qCItemName) {
        super();
        this.idx = idx;
        this.rdpIDX = rdpIDX;
        this.rdpRecordCardIDX = rdpRecordCardIDX;
        this.qCItemNo = qCItemNo;
        this.qCEmpID = qCEmpID;
        this.qCEmpName = qCEmpName;
        this.qCItemName = qCItemName;
    }
    /**
     * @return 获取检查项名称
	 */
    public String getQCItemName() {
        return qCItemName;
    }
    /**
     * @param itemName 设置检查项名称
     */
    public void setQCItemName(String itemName) {
        qCItemName = itemName;
    }
    /**
	 * @return String 获取作业主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param rdpIDX 设置作业主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
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
	 * @return Long 获取检验人员
	 */
	public Long getQCEmpID(){
		return qCEmpID;
	}
	/**
	 * @param qCEmpID 设置检验人员
	 */
	public void setQCEmpID(Long qCEmpID) {
		this.qCEmpID = qCEmpID;
	}
	/**
	 * @return String 获取检验人员名称
	 */
	public String getQCEmpName(){
		return qCEmpName;
	}
	/**
	 * @param qCEmpName 设置检验人员名称
	 */
	public void setQCEmpName(String qCEmpName) {
		this.qCEmpName = qCEmpName;
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
	 * <li>说明：通过指派方式指定的质量检查人员对象
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-1-19 下午03:51:15
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * 
	 * @author 测控部检修系统项目组
	 * @version 1.0
	 */

	@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
	public static final class QCEmp implements Serializable{
		
		/** default */
		private static final long serialVersionUID = 1L;

		/** 人员名称 */
		private String empName;
		
		/** 人员ID */
		private Long empId;
		
		/** 质量检查项编码 */
		private String qcItemNo;
		
		/** 质量检查项名称 */
		private String qcItemName;
        
        /** 状态 */
        private String status ;
		
		/**
		 * @return 获取人员ID
		 */
		public Long getEmpId() {
			return empId;
		}
		/**
		 * @param empId 设置人员ID
		 */
		public void setEmpId(Long empId) {
			this.empId = empId;
		}
		/**
		 * @return 获取人员名称
		 */
		public String getEmpName() {
			return empName;
		}
		/**
		 * @param empName 设置人员名称
		 */
		public void setEmpName(String empName) {
			this.empName = empName;
		}
		/**
		 * @return 获取质量检查项编码
		 */
		public String getQcItemNo() {
			return qcItemNo;
		}
		/**
		 * @param qcItemNo 设置质量检查项编码
		 */
		public void setQcItemNo(String qcItemNo) {
			this.qcItemNo = qcItemNo;
		}
		/**
		 * @return 获取质量检查项名称
		 */
		public String getQcItemName() {
			return qcItemName;
		}
		/**
		 * @param qcItemName 设置质量检查项名称
		 */
		public void setQcItemName(String qcItemName) {
			this.qcItemName = qcItemName;
		}
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
		
	}
	
}