package com.yunda.jx.jxgc.processdef.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobNodeStationDef实体 数据表：关联作业工位
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_JobNode_Station_Def")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JobNodeStationDef implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 节点主键 */
	@Column(name="Node_IDX")
	private String nodeIDX;
	/* 工位主键 */
	@Column(name="Work_Station_IDX")
	private String workStationIDX;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    /* 站点标识 */
    @Column(updatable=false)
    private String siteID;
    /* 创建者 */
    @Column(updatable=false)
    private Long creator;
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Create_Time",updatable=false)
    private java.util.Date createTime;
    /* 更新者*/
    private Long updator;
    /* 更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private java.util.Date updateTime;

	/**
	 * @return String 获取节点主键
	 */
	public String getNodeIDX(){
		return nodeIDX;
	}
	/**
	 * @param nodeIDX 设置节点主键
	 */
	public void setNodeIDX(String nodeIDX) {
		this.nodeIDX = nodeIDX;
	}
	/**
	 * @return String 获取工位主键
	 */
	public String getWorkStationIDX(){
		return workStationIDX;
	}
	/**
	 * @param workStationIDX 设置工位主键
	 */
	public void setWorkStationIDX(String workStationIDX) {
		this.workStationIDX = workStationIDX;
	}
    /**
     * @return Integer 获取记录的状态
     */
    public Integer getRecordStatus(){
        return recordStatus;
    }
    /**
     * @param recordStatus 设置记录的状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    /**
     * @return String 获取站点标识
     */
    public String getSiteID(){
        return siteID;
    }
    /**
     * @param siteID 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    /**
     * @return Long 获取创建者
     */
    public Long getCreator(){
        return creator;
    }
    /**
     * @param creator 设置创建者
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime(){
        return createTime;
    }
    /**
     * @param createTime 设置创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator(){
        return updator;
    }
    /**
     * @param updator 设置修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    /**
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime(){
        return updateTime;
    }
    /**
     * @param updateTime 设置修改时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
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