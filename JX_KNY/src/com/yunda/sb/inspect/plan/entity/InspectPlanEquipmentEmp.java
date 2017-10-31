package com.yunda.sb.inspect.plan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: InspectPlanEquipmentEmp实体类，数据表：巡检设备人员
 * <li>创建人：何涛
 * <li>创建日期：2016年8月16
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_INSPECT_PLAN_EQUIPMENT_EMP")
public class InspectPlanEquipmentEmp implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备主键 */
	@Column(name = "PLAN_EQUIPMENT_IDX")
	private String planEquipmentIdx;

	/** 巡检班组id，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "INSPECT_ORGID")
	private String inspectOrgid;

	/** 巡检班组名称，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "INSPECT_ORGNAME")
	private String inspectOrgname;

	/** 巡检人，人员id，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "INSPECT_EMPID")
	private String inspectEmpid;

	/** 巡检人名称，人员名称，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "INSPECT_EMP")
	private String inspectEmp;

	/** 委托巡检班组id，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "ENTRUST_INSPECT_ORGID")
	private String entrustInspectOrgid;

	/** 委托巡检班组名称，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "ENTRUST_INSPECT_ORGNAME")
	private String entrustInspectOrgname;

	/** 委托巡检人，人员id，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "ENTRUST_INSPECT_EMPID")
	private String entrustInspectEmpid;

	/** 委托巡检人名称，人员名称，多个人员以英文逗号“,”进行分隔 */
	@Column(name = "ENTRUST_INSPECT_EMP")
	private String entrustInspectEmp;

	/** 检修类型（1：机械、2：电气、3：其它） */
	@Column(name = "REPAIR_TYPE")
	private Integer repairType;

	/** 巡检结果（已巡检、未巡检） */
	@Column(name = "CHECK_RESULT")
	private String checkResult;

	/** 巡检情况描述 */
	@Column(name = "CHECK_RESULT_DESC")
	private String checkResultDesc;

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

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getPlanEquipmentIdx() {
		return planEquipmentIdx;
	}

	public void setPlanEquipmentIdx(String planEquipmentIdx) {
		this.planEquipmentIdx = planEquipmentIdx;
	}

	public String getInspectOrgid() {
		return inspectOrgid;
	}

	public void setInspectOrgid(String inspectOrgid) {
		this.inspectOrgid = inspectOrgid;
	}

	public String getInspectOrgname() {
		return inspectOrgname;
	}

	public void setInspectOrgname(String inspectOrgname) {
		this.inspectOrgname = inspectOrgname;
	}

	public String getInspectEmpid() {
		return inspectEmpid;
	}

	public void setInspectEmpid(String inspectEmpid) {
		this.inspectEmpid = inspectEmpid;
	}

	public String getInspectEmp() {
		return inspectEmp;
	}

	public void setInspectEmp(String inspectEmp) {
		this.inspectEmp = inspectEmp;
	}

	public String getEntrustInspectOrgid() {
		return entrustInspectOrgid;
	}

	public void setEntrustInspectOrgid(String entrustInspectOrgid) {
		this.entrustInspectOrgid = entrustInspectOrgid;
	}

	public String getEntrustInspectOrgname() {
		return entrustInspectOrgname;
	}

	public void setEntrustInspectOrgname(String entrustInspectOrgname) {
		this.entrustInspectOrgname = entrustInspectOrgname;
	}

	public String getEntrustInspectEmpid() {
		return entrustInspectEmpid;
	}

	public void setEntrustInspectEmpid(String entrustInspectEmpid) {
		this.entrustInspectEmpid = entrustInspectEmpid;
	}

	public String getEntrustInspectEmp() {
		return entrustInspectEmp;
	}

	public void setEntrustInspectEmp(String entrustInspectEmp) {
		this.entrustInspectEmp = entrustInspectEmp;
	}

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getCheckResultDesc() {
		return checkResultDesc;
	}

	public void setCheckResultDesc(String checkResultDesc) {
		this.checkResultDesc = checkResultDesc;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkResult == null) ? 0 : checkResult.hashCode());
		result = prime * result + ((checkResultDesc == null) ? 0 : checkResultDesc.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((entrustInspectEmp == null) ? 0 : entrustInspectEmp.hashCode());
		result = prime * result + ((entrustInspectEmpid == null) ? 0 : entrustInspectEmpid.hashCode());
		result = prime * result + ((entrustInspectOrgid == null) ? 0 : entrustInspectOrgid.hashCode());
		result = prime * result + ((entrustInspectOrgname == null) ? 0 : entrustInspectOrgname.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((inspectEmp == null) ? 0 : inspectEmp.hashCode());
		result = prime * result + ((inspectEmpid == null) ? 0 : inspectEmpid.hashCode());
		result = prime * result + ((inspectOrgid == null) ? 0 : inspectOrgid.hashCode());
		result = prime * result + ((inspectOrgname == null) ? 0 : inspectOrgname.hashCode());
		result = prime * result + ((planEquipmentIdx == null) ? 0 : planEquipmentIdx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((repairType == null) ? 0 : repairType.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InspectPlanEquipmentEmp other = (InspectPlanEquipmentEmp) obj;
		if (checkResult == null) {
			if (other.checkResult != null)
				return false;
		} else if (!checkResult.equals(other.checkResult))
			return false;
		if (checkResultDesc == null) {
			if (other.checkResultDesc != null)
				return false;
		} else if (!checkResultDesc.equals(other.checkResultDesc))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (entrustInspectEmp == null) {
			if (other.entrustInspectEmp != null)
				return false;
		} else if (!entrustInspectEmp.equals(other.entrustInspectEmp))
			return false;
		if (entrustInspectEmpid == null) {
			if (other.entrustInspectEmpid != null)
				return false;
		} else if (!entrustInspectEmpid.equals(other.entrustInspectEmpid))
			return false;
		if (entrustInspectOrgid == null) {
			if (other.entrustInspectOrgid != null)
				return false;
		} else if (!entrustInspectOrgid.equals(other.entrustInspectOrgid))
			return false;
		if (entrustInspectOrgname == null) {
			if (other.entrustInspectOrgname != null)
				return false;
		} else if (!entrustInspectOrgname.equals(other.entrustInspectOrgname))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (inspectEmp == null) {
			if (other.inspectEmp != null)
				return false;
		} else if (!inspectEmp.equals(other.inspectEmp))
			return false;
		if (inspectEmpid == null) {
			if (other.inspectEmpid != null)
				return false;
		} else if (!inspectEmpid.equals(other.inspectEmpid))
			return false;
		if (inspectOrgid == null) {
			if (other.inspectOrgid != null)
				return false;
		} else if (!inspectOrgid.equals(other.inspectOrgid))
			return false;
		if (inspectOrgname == null) {
			if (other.inspectOrgname != null)
				return false;
		} else if (!inspectOrgname.equals(other.inspectOrgname))
			return false;
		if (planEquipmentIdx == null) {
			if (other.planEquipmentIdx != null)
				return false;
		} else if (!planEquipmentIdx.equals(other.planEquipmentIdx))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (repairType == null) {
			if (other.repairType != null)
				return false;
		} else if (!repairType.equals(other.repairType))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (updator == null) {
			if (other.updator != null)
				return false;
		} else if (!updator.equals(other.updator))
			return false;
		return true;
	}

}
