package com.yunda.sb.repair.scope.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.base.IOrder;
import com.yunda.sb.repair.process.entity.RepairTaskList;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScope，数据表：设备检修范围
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_REPAIR_SCOPE")
public class RepairScope implements java.io.Serializable, IOrder, Comparable<RepairScope> {
	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 检修类型 - 机械  */
	public static final int REPAIR_TYPE_JX = 1;

	/** 检修类型 - 电气  */
	public static final int REPAIR_TYPE_DQ = 2;

	/** 检修类型 - 其它  */
	public static final int REPAIR_TYPE_QT = 3;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 顺序号 */
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	/** 设备类别编码 */
	@Column(name = "class_code")
	private String classCode;

	/** 设备类别名称 */
	@Column(name = "class_name")
	private String className;

	/** 设备类别名称首拼 */
	@Column(name = "class_name_py")
	private String classNamePY;

	/** 检修类型（1：机械、2：电气、3：其它） */
	@Column(name = "repair_type")
	private Integer repairType;

	/** 检修范围名称 */
	@Column(name = "repair_scope_name")
	private String repairScopeName;

	/** 小修，1：表示包含、0：不包含 */
	@Column(name = "repair_class_small")
	private Boolean repairClassSmall;

	/** 中修 ，1：表示包含、0：不包含*/
	@Column(name = "repair_class_medium")
	private Boolean repairClassMedium;

	/** 项修，1：表示包含、0：不包含 */
	@Column(name = "repair_class_subject")
	private Boolean repairClassSubject;

	/** 备注 */
	private String remark;

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

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassNamePY() {
		return classNamePY;
	}

	public void setClassNamePY(String classNamePY) {
		this.classNamePY = classNamePY;
	}

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getRepairScopeName() {
		return repairScopeName;
	}

	public void setRepairScopeName(String repairScopeName) {
		this.repairScopeName = repairScopeName;
	}

	public Boolean getRepairClassSmall() {
		return repairClassSmall;
	}

	public void setRepairClassSmall(Boolean repairClassSmall) {
		this.repairClassSmall = repairClassSmall;
	}

	public Boolean getRepairClassMedium() {
		return repairClassMedium;
	}

	public void setRepairClassMedium(Boolean repairClassMedium) {
		this.repairClassMedium = repairClassMedium;
	}

	public Boolean getRepairClassSubject() {
		return repairClassSubject;
	}

	public void setRepairClassSubject(Boolean repairClassSubject) {
		this.repairClassSubject = repairClassSubject;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Override
	public Integer getSeqNo() {
		return seqNo;
	}

	@Override
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * <li>说明：验证该检修范围是否适用于相应的修别（小、中、项）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：何涛
	 * <li>修改内容：修改字段的空指针异常
	 * <li>修改日期：2016年8月22日
	 * @param repairClassName 修别名称（小、中、项）
	 * @return true: 适用，false：不适用
	 */
	public boolean isValid(String repairClassName) {
		if (RepairTaskList.REPAIR_CLASS_NAME_SMALL.equals(repairClassName)) {
			return null == repairClassSmall ? false : this.repairClassSmall.booleanValue();
		}
		if (RepairTaskList.REPAIR_CLASS_NAME_MEDIUM.equals(repairClassName)) {
			return null == repairClassMedium ? false : this.repairClassMedium.booleanValue();
		}
		if (RepairTaskList.REPAIR_CLASS_NAME_SUBJECT.equals(repairClassName)) {
			return null == repairClassSubject ? false : this.repairClassSubject.booleanValue();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RepairScope o) {
		return this.seqNo.compareTo(o.getSeqNo());
	}

	/**
	 * <li>说明：单元测试代码
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月12日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param args
	 */
	public static void main(String[] args) {
		List<RepairScope> list = new ArrayList<RepairScope>();
		RepairScope rs0 = new RepairScope();
		rs0.setSeqNo(0);
		RepairScope rs1 = new RepairScope();
		rs1.setSeqNo(1);
		list.add(rs1);
		list.add(rs0);
		Collections.sort(list);
		for (RepairScope rs : list) {
			System.out.println(rs.getSeqNo());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((classNamePY == null) ? 0 : classNamePY.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((repairClassMedium == null) ? 0 : repairClassMedium.hashCode());
		result = prime * result + ((repairClassSmall == null) ? 0 : repairClassSmall.hashCode());
		result = prime * result + ((repairClassSubject == null) ? 0 : repairClassSubject.hashCode());
		result = prime * result + ((repairScopeName == null) ? 0 : repairScopeName.hashCode());
		result = prime * result + ((repairType == null) ? 0 : repairType.hashCode());
		result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
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
		RepairScope other = (RepairScope) obj;
		if (classCode == null) {
			if (other.classCode != null)
				return false;
		} else if (!classCode.equals(other.classCode))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (classNamePY == null) {
			if (other.classNamePY != null)
				return false;
		} else if (!classNamePY.equals(other.classNamePY))
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
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (repairClassMedium == null) {
			if (other.repairClassMedium != null)
				return false;
		} else if (!repairClassMedium.equals(other.repairClassMedium))
			return false;
		if (repairClassSmall == null) {
			if (other.repairClassSmall != null)
				return false;
		} else if (!repairClassSmall.equals(other.repairClassSmall))
			return false;
		if (repairClassSubject == null) {
			if (other.repairClassSubject != null)
				return false;
		} else if (!repairClassSubject.equals(other.repairClassSubject))
			return false;
		if (repairScopeName == null) {
			if (other.repairScopeName != null)
				return false;
		} else if (!repairScopeName.equals(other.repairScopeName))
			return false;
		if (repairType == null) {
			if (other.repairType != null)
				return false;
		} else if (!repairType.equals(other.repairType))
			return false;
		if (seqNo == null) {
			if (other.seqNo != null)
				return false;
		} else if (!seqNo.equals(other.seqNo))
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
