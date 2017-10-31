package com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity;

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
 * <li>说明：QCResult实体类, 数据表：质量控制
 * <li>创建人：汪东良
 * <li>创建日期：2014-11-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_QC_Result")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class QCResult implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 质量检验项状态——未开放 */
	public static final int STATUS_WKF = 0;

	/** 质量检验项状态——待处理 */
	public static final int STATUS_DCL = 1;

	/** 质量检验项状态——已处理 */
	public static final int STATUS_YCL = 2;

	/** 质量检验项状态——已终止 */
	public static final int STATUS_YZZ = 3;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 关联主键（关联作业卡、作业任务主键，根据类型进行区分） */
	@Column(name = "Relation_IDX")
	private String relationIDX;

	/* 作业卡主键 */
	@Column(name = "Work_Card_IDX")
	private String workCardIDX;

	/* 检验项编码 */
	@Column(name = "Check_Item_Code")
	private String checkItemCode;

	/* 检验项名称 */
	@Column(name = "Check_Item_Name")
	private String checkItemName;

	/* 抽检/必检 */
	@Column(name = "Check_Way")
	private Integer checkWay;

	/* 是否指派 */
	@Column(name = "Is_Assign")
	private Integer isAssign;

	/* 顺序号 */
	@Column(name = "Seq_No")
	private Integer seqNo;

	/* 检验人员ID */
	@Column(name = "QC_EmpID")
	private Long qcEmpID;

	/* 检验人员名称 */
	@Column(name = "QC_EmpName")
	private String qcEmpName;

	/* 检验时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "QC_Time")
	private java.util.Date qcTime;

	/* 备注 */
	private String remarks;

	/* 检验状态（0:未开放;1:待处理;2:已处理;3已终止） */
	private Integer status;

	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 站点标识，为了同步数据而使用 */
	@Column(updatable = false)
	private String siteID;

	/* 创建人 */
	@Column(updatable = false)
	private Long creator;

	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	private java.util.Date createTime;

	/* 修改人 */
	private Long updator;

	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	private java.util.Date updateTime;
    
    /** 是否可提交 */
    @Transient
    private String isCanSubmit ;
	
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     */
	public QCResult() {
		super();
	}
	
    /**
     * <li>说明：带参构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-8-20
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 主键
     * @param relationIDX 关联主键
     * @param workCardIDX 作业工单主键
     * @param checkItemCode 检测项编码
     * @param checkItemName 检测项名称
     */
	public QCResult(String idx, String relationIDX, String workCardIDX, String checkItemCode, String checkItemName) {
		this.idx = idx;
		this.relationIDX = relationIDX;
		this.workCardIDX = workCardIDX;
		this.checkItemCode = checkItemCode;
		this.checkItemName = checkItemName;
	}
	/**
	 * <li>说明：带参构造方法
	 * <li>创建人：张迪
	 * <li>创建日期：2016-9-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param idx 主键
	 * @param checkItemCode 检测项编码
	 * @param checkItemName  检测项名称
	 * @param qcEmpName 检查人员名称
	 */
	public QCResult(String idx, String relationIDX, String workCardIDX, String checkItemCode, String checkItemName, Long qcEmpID, String qcEmpName, Integer status) {
        this.idx = idx;
        this.relationIDX = relationIDX;
        this.workCardIDX = workCardIDX;
        this.checkItemCode = checkItemCode;
        this.checkItemName = checkItemName;
        this.qcEmpID = qcEmpID;
        this.qcEmpName = qcEmpName;
        this.status = status;
	}

	public String getCheckItemCode() {
		return checkItemCode;
	}

	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}

	public String getCheckItemName() {
		return checkItemName;
	}

	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}

	public Integer getCheckWay() {
		return checkWay;
	}

	public void setCheckWay(Integer checkWay) {
		this.checkWay = checkWay;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Integer isAssign) {
		this.isAssign = isAssign;
	}

	public long getQcEmpID() {
		return qcEmpID;
	}

	public void setQcEmpID(long qcEmpID) {
		this.qcEmpID = qcEmpID;
	}

	

	public String getQcEmpName() {
		return qcEmpName;
	}

	public void setQcEmpName(String qcEmpName) {
		this.qcEmpName = qcEmpName;
	}

	public java.util.Date getQcTime() {
		return qcTime;
	}

	public void setQcTime(java.util.Date qcTime) {
		this.qcTime = qcTime;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getRelationIDX() {
		return relationIDX;
	}

	public void setRelationIDX(String relationIDX) {
		this.relationIDX = relationIDX;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public String getWorkCardIDX() {
		return workCardIDX;
	}

	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

    
    public String getIsCanSubmit() {
        return isCanSubmit;
    }

    
    public void setIsCanSubmit(String isCanSubmit) {
        this.isCanSubmit = isCanSubmit;
    }


}