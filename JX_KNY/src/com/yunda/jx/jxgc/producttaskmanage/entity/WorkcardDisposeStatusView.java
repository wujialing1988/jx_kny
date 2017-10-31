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
 * <li>说明: 作业工单处理情况
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
@Table(name="v_jxgc_workcard_dispose_status")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkcardDisposeStatusView implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    public static final String IS_Y = "Y";
    public static final String IS_Y_ZN = "已派工";
    public static final String IS_N = "N";
    public static final String IS_N_ZN = "未派工";
    /* idx主键 */
    @Id
    private String idx;
    /* 车型 */
    @Column(name="Train_Type_Name")
    private String trainTypetName;
    /* 车号 */
    @Column(name="Train_No")
    private String trainNo;
    /* 修程+修次 */
	@Column(name="Repair_Class_Name")
	private String repairClassName;
    /* 作业卡名称 */
	@Column(name="Work_Card_Name")
	private String workCardName;
	/* 故障位置名称全名 */
    @Column(name="FixPlace_FullName")
    private String fixPlaceFullName;
    /* 工艺节点实例名称 */
	@Column(name="Node_Case_Name")
	private String nodeCaseName;
    /* 检修活动名称 */
	@Column(name="Activity_Name")
	private String activityName;
	/* 施修任务兑现单主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	 /* 工位所属班组名称 --- 作业班组 */
    @Column(name="work_station_belong_team_name")
    private String workStationBelongTeamName;
    /* 工位所属班组 */
	@Column(name="work_station_belong_team")
	private Long workStationBelongTeam;
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
    /* 已处理人员 */
    @Column(name="complete_worker")
    private String completeWorker;
    /* 状态（10：未处理、20：处理中、30：已处理） */
    private String status;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
	public String getWorkCardName() {
		return workCardName;
	}
	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getNodeCaseName() {
		return nodeCaseName;
	}
	public void setNodeCaseName(String nodeCaseName) {
		this.nodeCaseName = nodeCaseName;
	}
	public String getRepairClassName() {
		return repairClassName;
	}
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	public String getWorkStationBelongTeamName() {
		return workStationBelongTeamName;
	}
	public void setWorkStationBelongTeamName(String workStationBelongTeamName) {
		this.workStationBelongTeamName = workStationBelongTeamName;
	}
	public String getRdpIDX() {
		return rdpIDX;
	}
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	public Long getWorkStationBelongTeam() {
		return workStationBelongTeam;
	}
	public void setWorkStationBelongTeam(Long workStationBelongTeam) {
		this.workStationBelongTeam = workStationBelongTeam;
	}
    
    public String getCompleteWorker() {
        return completeWorker;
    }
    
    public void setCompleteWorker(String completeWorker) {
        this.completeWorker = completeWorker;
    }
}