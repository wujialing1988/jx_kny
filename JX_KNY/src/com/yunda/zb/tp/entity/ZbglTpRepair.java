package com.yunda.zb.tp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpRepair实体类, 数据表：JT6提票
 * <li>创建人：刘国栋
 * <li>创建日期：2016-08-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_JT6_REPAIR")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpRepair {
    //返修完成
    public static Integer STATUS_COMPLETE = 1 ;
    //正在返修
    public static Integer STATUS_ONGOING = 0 ;

	//返修单主键idx
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
	
	//故障位置
	@Column(name = "Fault_Fix_Full_Name")
    private String faultFixFullName;
	
	//故障原因
	@Column(name = "Fault_Reason")
    private String faultReason;
    
    //ʩ�޷施修方法
    @Column(name = "Method_Desc")
    private String methodDesc;
    
    //专业类型
    @Column(name = "Professional_Type_Name")
    private String professionalTypeName;
    
    //处理结果描述
    @Column(name = "Repair_Desc")
    private String repairDesc;
    
    //检查人idx
    @Column(name = "Check_Person_IDX")
    private String checkPersonIDX;
    
    //检查人
    @Column(name = "Check_Person")
    private String checkPerson;
    
    //检查时间
    @Column(name = "Check_Time")
    private String checkTime;
    
    //返修原因
    @Column(name = "Repair_Reason")
    private String repairReason;
    
    //jt6提票主键idx
    @Column(name = "Jt6_IDX")
    private String jt6IDX;
    
    //返修内容
    @Column(name = "Repair_Content")
    private String repairContent;
    
    //返修人员idx
    @Column(name = "Repair_Person_IDX")
    private String repairPersonIDX; 
    
    //返修人员姓名
    @Column(name = "Repair_Person_Name")
    private String repairPersonName;
    
    //返修状态
    @Column(name = "status")
    private Integer status;
    
    //第几次返修
    @Column(name = "Repair_Times")
    private Integer repairTimes;

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public String getCheckPersonIDX() {
		return checkPersonIDX;
	}

	public void setCheckPersonIDX(String checkPersonIDX) {
		this.checkPersonIDX = checkPersonIDX;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getFaultFixFullName() {
		return faultFixFullName;
	}

	public void setFaultFixFullName(String faultFixFullName) {
		this.faultFixFullName = faultFixFullName;
	}

	public String getFaultReason() {
		return faultReason;
	}

	public void setFaultReason(String faultReason) {
		this.faultReason = faultReason;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getJt6IDX() {
		return jt6IDX;
	}

	public void setJt6IDX(String jt6IDX) {
		this.jt6IDX = jt6IDX;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}

	public String getProfessionalTypeName() {
		return professionalTypeName;
	}

	public void setProfessionalTypeName(String professionalTypeName) {
		this.professionalTypeName = professionalTypeName;
	}

	public String getRepairContent() {
		return repairContent;
	}

	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}

	public String getRepairDesc() {
		return repairDesc;
	}

	public void setRepairDesc(String repairDesc) {
		this.repairDesc = repairDesc;
	}

	public String getRepairPersonIDX() {
		return repairPersonIDX;
	}

	public void setRepairPersonIDX(String repairPersonIDX) {
		this.repairPersonIDX = repairPersonIDX;
	}

	public String getRepairPersonName() {
		return repairPersonName;
	}

	public void setRepairPersonName(String repairPersonName) {
		this.repairPersonName = repairPersonName;
	}

	public String getRepairReason() {
		return repairReason;
	}

	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}

	public Integer getRepairTimes() {
		return repairTimes;
	}

	public void setRepairTimes(Integer repairTimes) {
		this.repairTimes = repairTimes;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
    
    
    
    
}
