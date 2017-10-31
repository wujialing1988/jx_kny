package com.yunda.jx.jxgc.producttaskmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 提票单处理情况
 * <li>创建人：程梅
 * <li>创建日期：2013-7-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="v_jxgc_notice_dispose_status")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class NoticeDisposeStatusView implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
    @Id
    private String idx;
    /* 车型主键 */
    @Column(name="Train_Type_IDX")
    private String trainTypeIDX;
    /* 车型英文简称 */
    @Column(name="Train_Type_Name")
    private String trainTypetName;
    /* 车号 */
    @Column(name="Train_No")
    private String trainNo;
    /* 提票单号 */
    @Column(name="notice_code")
    private String noticeCode;
    /* 提票类型 */
	@Column(name="notice_type")
	private String noticeType;
	/* 故障位置名称全名 */
    @Column(name="FixPlace_FullName")
    private String fixPlaceFullName;
    /* 故障描述 */
    @Column(name="Fault_Desc")
    private String faultDesc;
    /* 作业班组名称 */
    @Column(name="repair_team_name")
    private String repairTeamName;
    /* 作业班组 */
    @Column(name="repair_team")
    private Long repairTeam;
    /* 调度派工 */
    @Column(name="is_dispatcher")
    private String isDispatcher;
    /* 工长派工 */
    @Column(name="is_headman")
    private String isHeadman;
    /* 未开工人员 */
    @Column(name="notstart_worker")
    private String notstartWorker;
    /* 已开工人员 */
    @Column(name="start_worker")
    private String startWorker;
    /* 已完工人员 */
    @Column(name="complete_worker")
    private String completeWorker;
    
    /* 提票状态（10：未处理、20：处理中、30：已处理） */
    private Integer status;
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
	public String getFixPlaceFullName() {
		return fixPlaceFullName;
	}
	public void setFixPlaceFullName(String fixPlaceFullName) {
		this.fixPlaceFullName = fixPlaceFullName;
	}
	public String getIsDispatcher() {
		return isDispatcher;
	}
	public void setIsDispatcher(String isDispatcher) {
		this.isDispatcher = isDispatcher;
	}
	public String getIsHeadman() {
		return isHeadman;
	}
	public void setIsHeadman(String isHeadman) {
		this.isHeadman = isHeadman;
	}
	public String getNotstartWorker() {
		return notstartWorker;
	}
	public void setNotstartWorker(String notstartWorker) {
		this.notstartWorker = notstartWorker;
	}
	public String getStartWorker() {
		return startWorker;
	}
	public void setStartWorker(String startWorker) {
		this.startWorker = startWorker;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainTypetName() {
		return trainTypetName;
	}
	public void setTrainTypetName(String trainTypetName) {
		this.trainTypetName = trainTypetName;
	}
	public String getFaultDesc() {
		return faultDesc;
	}
	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}
	public String getNoticeCode() {
		return noticeCode;
	}
	public void setNoticeCode(String noticeCode) {
		this.noticeCode = noticeCode;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getRepairTeamName() {
		return repairTeamName;
	}
	public void setRepairTeamName(String repairTeamName) {
		this.repairTeamName = repairTeamName;
	}
	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	public Long getRepairTeam() {
		return repairTeam;
	}
	public void setRepairTeam(Long repairTeam) {
		this.repairTeam = repairTeam;
	}
	public String getCompleteWorker() {
		return completeWorker;
	}
	public void setCompleteWorker(String completeWorker) {
		this.completeWorker = completeWorker;
	}
    
}