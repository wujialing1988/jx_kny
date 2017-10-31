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
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 下车配件清单实体类
 * <li>创建人：张迪
 * <li>创建日期：2016-7-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="jxgc_jobProcess_off_parts")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JobProcessPartsOffList implements java.io.Serializable{

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 流程idx */
	@Column(name="Process_idx")
	private String processIdx;
	
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型简称 */
	@Column(name="Train_Type_Short_Name")
	private String trainTypeShortName;
	/* 位置代码    */
	@Column(name="WZDM")
	private String wzdm;
	/* 位置名称 */
	@Column(name=" WZMC")
	private String  wzmc;
     /* 上车节点id*/
    @Column(name = "on_node_idx")
    private String onNodeIdx;
    /* 上车节点名称 */
    @Column(name = "on_node_name")
    private String onNodeName;
    /* 下车节点id*/
    @Column(name = "off_node_idx")
    private String offNodeIdx;
    /* 下车节点名称 */
    @Column(name = "off_node_name")
    private String offNodeName;
    /*  配件id */
    @Column(name = "parts_idx")
    private String partsId;
    /* 配件名称 */
    @Column(name = "parts_name")
    private String partsName;
    
    /* 记录状态 */
    @Column(name = "Record_Status")
    private Integer recordStatus;

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
    
    /* 排序 */
    @Column(name = "seq_no")
    private Integer seqNo;
   
	
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	/**
	 * @param trainTypeIDX 设置车型主键
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}

	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	/**
	 * @param trainTypeShortName 设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
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
    
    public String getOffNodeIdx() {
        return offNodeIdx;
    }
    
    public void setOffNodeIdx(String offNodeIdx) {
        this.offNodeIdx = offNodeIdx;
    }
    
    public String getOffNodeName() {
        return offNodeName;
    }
    
    public void setOffNodeName(String offNodeName) {
        this.offNodeName = offNodeName;
    }
    
    public String getOnNodeIdx() {
        return onNodeIdx;
    }
    
    public void setOnNodeIdx(String onNodeIdx) {
        this.onNodeIdx = onNodeIdx;
    }
    
    public String getOnNodeName() {
        return onNodeName;
    }
    
    public void setOnNodeName(String onNodeName) {
        this.onNodeName = onNodeName;
    }

    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getProcessIdx() {
        return processIdx;
    }
    
    public void setProcessIdx(String processIdx) {
        this.processIdx = processIdx;
    }
    
    public String getWzmc() {
        return wzmc;
    }
    
    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }
    
    public String getPartsId() {
        return partsId;
    }
    
    public void setPartsId(String partsId) {
        this.partsId = partsId;
    }
    
    public String getWzdm() {
        return wzdm;
    }
    
    public void setWzdm(String wzdm) {
        this.wzdm = wzdm;
    }
    
    public Integer getSeqNo() {
        return seqNo;
    }
    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    
  
}