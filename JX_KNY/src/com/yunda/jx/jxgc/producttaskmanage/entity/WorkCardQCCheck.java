/**
 * 
 */
package com.yunda.jx.jxgc.producttaskmanage.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：质量检验查询对象的封装实体
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-26 下午01:48:17
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
public class WorkCardQCCheck {
	
	/** 联合主键 */
	@EmbeddedId
	private WorkCardQCCheckId id;
	
	/** 任务单主键 */
	@Column(name="RDP_IDX")
	private String rdpIdx;
	
	/** 车型 */
	@Column(name="TRAIN_TYPE_SHORTNAME")
	private String trainTypeName;
	
	/** 车号 */
	@Column(name="TRAIN_NO")
	private String trainNo;
	
	/** 修程名称 */
	@Column(name="REPAIR_NAME")
	private String repairName;
	
	/** 作业工单名称 */
	@Column(name="WORK_CARD_NAME")
	private String workCardName;
	
	/** 位置 */
	@Column(name="FIXPLACE_FULLNAME")
	private String fixPlaceFullName;
	
	/** 作业班组 */
	@Column(name="WORK_STATION_BELONG_TEAM_NAME")
	private String teamName;
	
	/** 检验项名称 */
	@Column(name="CHECK_ITEM_NAME")
	private String checkItemName;
	
	/** 抽检/必检 */
	@Column(name="CHECK_WAY")
	private String checkWay;
	
	/** 检验状态 */
	@Column(name="CHECK_PERSON_NAME")
	private String checkPersonName;
	
	/** 检验状态 */
    @Column(name="STATUS")
    private String status;

	public WorkCardQCCheck() {
		super();
	}

	public String getRdpIdx() {
		return rdpIdx;
	}

	public void setRdpIdx(String rdpIdx) {
		this.rdpIdx = rdpIdx;
	}

	public String getCheckItemName() {
		return checkItemName;
	}

	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}

	public String getCheckPersonName() {
		return checkPersonName;
	}

	public void setCheckPersonName(String checkPersonName) {
		this.checkPersonName = checkPersonName;
	}

	public String getCheckWay() {
		return checkWay;
	}

	public void setCheckWay(String checkWay) {
		this.checkWay = checkWay;
	}

	public String getFixPlaceFullName() {
		return fixPlaceFullName;
	}

	public void setFixPlaceFullName(String fixPlaceFullName) {
		this.fixPlaceFullName = fixPlaceFullName;
	}

	public WorkCardQCCheckId getId() {
		return id;
	}

	public void setId(WorkCardQCCheckId id) {
		this.id = id;
	}

	public String getRepairName() {
		return repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainTypeName() {
		return trainTypeName;
	}

	public void setTrainTypeName(String trainTypeName) {
		this.trainTypeName = trainTypeName;
	}

	public String getWorkCardName() {
		return workCardName;
	}

	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
    
	/**
	 * <li>标题：机车检修管理信息系统
	 * <li>说明：质量检验查询条件的封装实体
	 * <li>创建人： 何涛
	 * <li>创建日期： 2014-11-26 下午05:02:55
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * 
	 * @author 测控部检修系统项目组
	 * @version 1.0
	 */
	@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
	public static final class WorkCardQCCheckSearcher{
		
		/** 生产任务单，即：兑现单的idx主键 */
		private String rdpIDX;
		
		/** 作业工单名称 */
		private String workCardName;
		
		/** 质量检验人员名称 */
		private String checkPersonName;
		
		public WorkCardQCCheckSearcher() {
			super();
		}
		
		public String getCheckPersonName() {
			return checkPersonName;
		}
		
		public void setCheckPersonName(String checkPersonName) {
			this.checkPersonName = checkPersonName;
		}
		
		public String getRdpIDX() {
			return rdpIDX;
		}
		
		public void setRdpIDX(String rdpIDX) {
			this.rdpIDX = rdpIDX;
		}
		
		public String getWorkCardName() {
			return workCardName;
		}
		
		public void setWorkCardName(String workCardName) {
			this.workCardName = workCardName;
		}
		
	}
	
}
