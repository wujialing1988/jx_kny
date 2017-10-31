package com.yunda.sb.fault.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：FaultOrder实体类，数据表：故障提票
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_FAULT_ORDER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaultOrder implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 提票单状态 - 新建 */
	public static final String STATE_XJ = "新建";

	/** 提票单状态 - 已派工（调度派工） */
	public static final String STATE_YPG = "已派工";

	/** 提票单状态 - 处理中（工长派工） */
	public static final String STATE_CLZ = "处理中";

	/** 提票单状态 - 已处理 */
	public static final String STATE_YCL = "已处理";

	/** 提票单状态 - 退回 */
	public static final String STATE_TH = "退回";

	/** 故障等级 - 一般 */
	public static final String FAULT_LEVEL_YB = "一般";

	/** 故障等级 - 重大 */
	public static final String FAULT_LEVEL_ZD = "重大";

	/** 故障等级 - 特大 */
	public static final String FAULT_LEVEL_TD = "特大";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备idx主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 设备名称 */
	@Column(name = "EQUIPMENT_NAME")
	private String equipmentName;

	/** 设备编码 */
	@Column(name = "EQUIPMENT_CODE")
	private String equipmentCode;

	/* 型号 */
	private String model;

	/* 规格 */
	private String specification;

	/** 提报人 */
	@Column(name = "SUBMIT_EMP")
	private String submitEmp;

	/** 提报人ID */
	@Column(name = "SUBMIT_EMP_ID")
	private Long submitEmpId;

	/** 提票单号 */
	@Column(name = "FAULT_ORDER_NO")
	private String faultOrderNo;

	/** 故障发现时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FAULT_OCCUR_TIME")
	private Date faultOccurTime;

	/** 故障部位及意见 */
	@Column(name = "FAULT_PLACE")
	private String faultPlace;

	/** 故障现象 */
	@Column(name = "FAULT_PHENOMENON")
	private String faultPhenomenon;

	/** 故障原因分析 */
	@Column(name = "CAUSE_ANALYSIS")
	private String causeAnalysis;

	/** 主修班组ID */
	@Column(name = "REPAIR_TEAM_ID")
	private Long repairTeamId;

	/** 主修班组 */
	@Column(name = "REPAIR_TEAM")
	private String repairTeam;

	/** 辅修班组，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "ASSIST_REPAIR_TEAM")
	private String assistRepairTeam;

	/** 辅修班组id，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "ASSIST_REPAIR_TEAM_ID")
	private String assistRepairTeamId;

	/** 修理费用 */
	@Column(name = "REPAIR_COST")
	private Float repairCost;

	/** 修理人 */
	@Column(name = "REPAIR_EMP")
	private String repairEmp;

	/** 修理人ID */
	@Column(name = "REPAIR_EMP_ID")
	private String repairEmpId;

	/** 辅修人员 */
	@Column(name = "ASSIST_REPAIR_EMPS")
	private String assistRepairEmps;

	/** 实际修理内容 */
	@Column(name = "REPAIR_CONTENT")
	private String repairContent;

	/** 故障恢复时间 */
	@Column(name = "FAULT_RECOVER_TIME")
	private Date faultRecoverTime;

	/** 使用人（保留） */
	@Column(name = "USE_WORKER")
	private String useWorker;

	/** 使用人ID（保留） */
	@Column(name = "USE_WORKER_ID")
	private Long useWorkerId;

	/** 故障等级（一般，重大，特大） */
	@Column(name = "FAULT_LEVEL")
	private String faultLevel;

	/** 提票状态（新建，已派工，处理中，已处理，退回） */
	private String state;

	/** 退回原因  */
	@Column(name = "BACK_REASON")
	private String backReason;

	/** 调度派工日期  */
	@Column(name = "DISPATCH_DATE_DD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dispatchDateDD;

	/** 工长派工日期  */
	@Column(name = "DISPATCH_DATE_GZ")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dispatchDateGZ;

	/** 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/** 创建人 */
	@Column(name = "CREATOR", updatable = false)
	private Long creator;

	/** 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;

	/** 修改人 */
	@Column(name = "UPDATOR")
	private Long updator;

	/** 修改时间 */
	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date updateTime;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getSubmitEmp() {
		return submitEmp;
	}

	public void setSubmitEmp(String submitEmp) {
		this.submitEmp = submitEmp;
	}

	public Long getSubmitEmpId() {
		return submitEmpId;
	}

	public void setSubmitEmpId(Long submitEmpId) {
		this.submitEmpId = submitEmpId;
	}

	public String getFaultOrderNo() {
		return faultOrderNo;
	}

	public void setFaultOrderNo(String faultOrderNo) {
		this.faultOrderNo = faultOrderNo;
	}

	public Date getFaultOccurTime() {
		return faultOccurTime;
	}

	public void setFaultOccurTime(Date faultOccurTime) {
		this.faultOccurTime = faultOccurTime;
	}

	public String getFaultPlace() {
		return faultPlace;
	}

	public void setFaultPlace(String faultPlace) {
		this.faultPlace = faultPlace;
	}

	public String getFaultPhenomenon() {
		return faultPhenomenon;
	}

	public void setFaultPhenomenon(String faultPhenomenon) {
		this.faultPhenomenon = faultPhenomenon;
	}

	public String getCauseAnalysis() {
		return causeAnalysis;
	}

	public void setCauseAnalysis(String causeAnalysis) {
		this.causeAnalysis = causeAnalysis;
	}

	public Long getRepairTeamId() {
		return repairTeamId;
	}

	public void setRepairTeamId(Long repairTeamId) {
		this.repairTeamId = repairTeamId;
	}

	public String getRepairTeam() {
		return repairTeam;
	}

	public void setRepairTeam(String repairTeam) {
		this.repairTeam = repairTeam;
	}

	public String getAssistRepairTeam() {
		return assistRepairTeam;
	}

	public void setAssistRepairTeam(String assistRepairTeam) {
		this.assistRepairTeam = assistRepairTeam;
	}

	public String getAssistRepairTeamId() {
		return assistRepairTeamId;
	}

	public void setAssistRepairTeamId(String assistRepairTeamId) {
		this.assistRepairTeamId = assistRepairTeamId;
	}

	public Float getRepairCost() {
		return repairCost;
	}

	public void setRepairCost(Float repairCost) {
		this.repairCost = repairCost;
	}

	public String getRepairEmp() {
		return repairEmp;
	}

	public void setRepairEmp(String repairEmp) {
		this.repairEmp = repairEmp;
	}

	public String getRepairEmpId() {
		return repairEmpId;
	}

	public void setRepairEmpId(String repairEmpId) {
		this.repairEmpId = repairEmpId;
	}

	public String getAssistRepairEmps() {
		return assistRepairEmps;
	}

	public void setAssistRepairEmps(String assistRepairEmps) {
		this.assistRepairEmps = assistRepairEmps;
	}

	public String getRepairContent() {
		return repairContent;
	}

	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}

	public Date getFaultRecoverTime() {
		return faultRecoverTime;
	}

	public void setFaultRecoverTime(Date faultRecoverTime) {
		this.faultRecoverTime = faultRecoverTime;
	}

	public String getUseWorker() {
		return useWorker;
	}

	public void setUseWorker(String useWorker) {
		this.useWorker = useWorker;
	}

	public Long getUseWorkerId() {
		return useWorkerId;
	}

	public void setUseWorkerId(Long useWorkerId) {
		this.useWorkerId = useWorkerId;
	}

	public String getFaultLevel() {
		return faultLevel;
	}

	public void setFaultLevel(String faultLevel) {
		this.faultLevel = faultLevel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

	public Date getDispatchDateDD() {
		return dispatchDateDD;
	}

	public void setDispatchDateDD(Date dispatchDateDD) {
		this.dispatchDateDD = dispatchDateDD;
	}

	public Date getDispatchDateGZ() {
		return dispatchDateGZ;
	}

	public void setDispatchDateGZ(Date dispatchDateGZ) {
		this.dispatchDateGZ = dispatchDateGZ;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

}
