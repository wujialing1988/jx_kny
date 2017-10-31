package com.yunda.jx.jxgc.workhis.workcard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图JXGC_WORKER_HIS实体类, 数据表：作业人员历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_WORKER_HIS")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkerHis implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@Id
	private String idx;
	/* 作业卡主键 */
	@Column(name="Work_Card_IDX")
	private String workCardIDX;
	/* 工作人员ID */
	@Column(name="Worker_ID")
	private Long workerID;
	/* 工作人员名称 */
	@Column(name="Worker_Name")
	private String workerName;
	/* 工作人员编码 */
	@Column(name="Worker_Code")
	private String workerCode;
	/* 人员所在班组主键 */
	@Column(name="Worker_Tream_IDX")
	private Long workerTreamIDX;
	/* 人员所在班组名称 */
	@Column(name="Worker_Tream_Name")
	private String workerTreamName;
	/* 人员所在班组序列 */
	@Column(name="Worker_Tream_Seq")
	private String workerTreamSeq;
	/* 工时 */
//	@Column(name="Work_Time")
//	private Integer workTime;
	/* 作业人员工种编号 */
//	@Column(name="Work_Type")
//	private String workType;
//	/* 作业人员工种名称 */
//	@Column(name="Work_TypeName")
//	private String workTypeName;
//	/* 开工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="Begin_Work_Time")
//	private java.util.Date beginWorkTime;
//	/* 完工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="End_Work_Time")
//	private java.util.Date endWorkTime;
//	/* 系统记录开工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="System_Beigin_Time")
//	private java.util.Date systemBeiginTime;
//	/* 系统记录完工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="System_End_Time")
//	private java.util.Date systemEndTime;
	/* 状态:10待领取、20待处理、30处理中、40已处理、50终止 */
//	private String status;
//	/* 备注 */
//	private String remarks;
//	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
//	@Column(name="RECORD_STATUS")
//	private Integer recordStatus;
//	/* 站点标识，为了同步数据而使用 */
//	@Column(updatable=false)
//	private String siteID;
//	/* 创建人 */
//	@Column(updatable=false)
//	private Long creator;
//	/* 创建时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="CREATE_TIME",updatable=false)
//	private java.util.Date createTime;
//	/* 修改人 */
//	private Long updator;
//	/* 修改时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="UPDATE_TIME")
//	private java.util.Date updateTime;
     /* 是否加班完成，1：是；0：否*/
//    @Column(name="IS_OVERTIME")
//    private Integer isOvertime;
//    /* 加班时间 */
//    @Column(name="OVERTIME")
//    private Long overtime;

	/* 开工时间 【字符型，供工位终端使用】*/
//	@Transient
//	private String beginWorkTimeStr;
//	/* 完工时间   【字符型，供工位终端使用】*/
//	@Transient
//	private String endWorkTimeStr;
	/**
     * 构造方法
	 */
	public WorkerHis(){
		super();
	}
	/**
	 * @return String 获取作业卡主键
	 */
	public String getWorkCardIDX(){
		return workCardIDX;
	}
	/**
	 * @param workCardIDX 设置作业卡主键
	 */
	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}
	/**
	 * @return Long 获取作业人员ID
	 */
	public Long getWorkerID(){
		return workerID;
	}
	/**
	 * @param workerID 设置作业人员ID
	 */
	public void setWorkerID(Long workerID) {
		this.workerID = workerID;
	}
	/**
	 * @return String 获取作业人员名称
	 */
	public String getWorkerName(){
		return workerName;
	}
	/**
	 * @param workerName 设置作业人员名称
	 */
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	/**
	 * @return String 获取作业人员编码
	 */
	public String getWorkerCode(){
		return workerCode;
	}
	/**
	 * @param workerCode 设置作业人员编码
	 */
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}
	/**
	 * @return Long 获取人员所在班组
	 */
	public Long getWorkerTreamIDX(){
		return workerTreamIDX;
	}
	/**
	 * @param workerTreamIDX 设置人员所在班组
	 */
	public void setWorkerTreamIDX(Long workerTreamIDX) {
		this.workerTreamIDX = workerTreamIDX;
	}
	/**
	 * @return String 获取人员所在班组名称
	 */
	public String getWorkerTreamName(){
		return workerTreamName;
	}
	/**
	 * @param workerTreamName 设置人员所在班组名称
	 */
	public void setWorkerTreamName(String workerTreamName) {
		this.workerTreamName = workerTreamName;
	}
	/**
	 * @return String 获取人员所在班组序列
	 */
	public String getWorkerTreamSeq(){
		return workerTreamSeq;
	}
	/**
	 * @param workerTreamSeq 设置人员所在班组序列
	 */
	public void setWorkerTreamSeq(String workerTreamSeq) {
		this.workerTreamSeq = workerTreamSeq;
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
}