package com.yunda.jx.pjjx.repairline.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkStationBinding实体类, 数据表：配件检修人员绑定工位
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_WORK_STATION_BINDING")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkStationBinding implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 工位主键 */
	@Column(name="Work_Station_IDX")
	private String workStationIdx;
	/* 人员主键 */
	private Long empId;
    
    /* 工位编码 */
    @Transient
    private String workStationCode;
    /* 工位名称 */
    @Transient
    private String workStationName;
    /* 流水线名称 */
    @Transient
    private String repairLineName;
    
    /**
     * 
     */
    public WorkStationBinding(){
        super();
    }
    /**
     * 
     * <li>说明：查询配件检修人员绑定工位列表时所用
     * <li>创建人：程梅
     * <li>创建日期：2015-10-17
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 主键
     * @param workStationCode 工位编码
     * @param workStationName 工位名称
     * @param repairLineName  流水线名称
     */
    public WorkStationBinding(String idx,String workStationCode,String workStationName,String repairLineName){
        this.idx = idx ;
        this.workStationCode = workStationCode ;
        this.workStationName = workStationName ;
        this.repairLineName = repairLineName ;
    }
    public String getRepairLineName() {
        return repairLineName;
    }
    
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
    }
    
    public String getWorkStationCode() {
        return workStationCode;
    }
    
    public void setWorkStationCode(String workStationCode) {
        this.workStationCode = workStationCode;
    }
    
    public String getWorkStationName() {
        return workStationName;
    }
    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    /**
	 * @return String 获取工位主键
	 */
	public String getWorkStationIdx(){
		return workStationIdx;
	}
	/**
	 * @param workStationIdx 设置工位主键
	 */
	public void setWorkStationIdx(String workStationIdx) {
		this.workStationIdx = workStationIdx;
	}
	/**
	 * @return Long 获取人员主键
	 */
	public Long getEmpId(){
		return empId;
	}
	/**
	 * @param empId 设置人员主键
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
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