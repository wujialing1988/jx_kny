package com.yunda.jx.scdd.repairplan.entity;

import java.util.Comparator;

public class RepairStandardVo implements Comparator<RepairStandardVo> {

	/* 修程 */
	private String repairClass;
	/* 修程名称 */
	private String repairClassName;
	/* 修次 */
	private String repairOrder;
	/* 修次名称 */
	private String repairOrderName;
	
	
	public String getRepairClass() {
		return repairClass;
	}

	public void setRepairClass(String repairClass) {
		this.repairClass = repairClass;
	}

	public String getRepairClassName() {
		return repairClassName;
	}

	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}

	public String getRepairOrder() {
		return repairOrder;
	}

	public void setRepairOrder(String repairOrder) {
		this.repairOrder = repairOrder;
	}

	public String getRepairOrderName() {
		return repairOrderName;
	}

	public void setRepairOrderName(String repairOrderName) {
		this.repairOrderName = repairOrderName;
	}

	public int compare(RepairStandardVo o1, RepairStandardVo o2) {
		if(Integer.parseInt(o1.getRepairOrder()) > Integer.parseInt(o2.getRepairOrder())){
			return 1;
		}else{
			return 0;
		}
		
	}

}
