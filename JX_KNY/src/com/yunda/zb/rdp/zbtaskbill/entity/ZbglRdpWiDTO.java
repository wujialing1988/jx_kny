package com.yunda.zb.rdp.zbtaskbill.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-8-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpWiDTO implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 机车号 */
    @Column(name = "trainTypeAndNo")
    private String trainTypeAndNo;
    
    /* 任务名称 */
    @Column(name = "wi_name")
    private String wiName;
    
    /* 任务描述 */
    @Column(name = "wi_desc")
    private String wiDesc;
    
    /* 入段时间 */
    @Column(name = "in_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inTime;
    
    /* 领活时间 */
    @Column(name = "fetch_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fetchTime;
    
    /* 车型主键 */
    @Column(name = "train_type_idx")
    private String trainTypeIDX;
    
    /* 车号 */
    @Column(name = "train_no")
    private String trainNo;
    
    /* 顺序号 */
    @Column(name = "seq_no")
    private Integer seqNo;
    
    /* 车型简称 */
    @Column(name = "train_type_shortname")
    private String trainTypeShortName;
    
    /* 配属段编码 */
    @Column(name = "d_id")
    private String dID;
    
    /* 配属段名称 */
    @Column(name = "d_name")
    private String dName;
    
    /* 入段时间 */
    @Transient
    private String inTimeStr;
    
    /* 领活时间 */
    @Transient
    private String fetchTimeStr;
    
    /* 任务单类型,1：正常整备范围；3：预警 */
    @Column(name = "wi_class")
    private String wiClass;
    
    /* 其他作业人员 */
    @Column(name = "worker")
    private String worker;
    
    /* 是否合格 */
    @Column(name = "IS_HG")
    private String isHg;
    

    /* 任务单类型,正常整备范围；预警 */
    @Transient
    private String wiClassName;
    
    public String getWiClass() {
        return wiClass;
    }
    
    public void setWiClass(String wiClass) {
        this.wiClass = wiClass;
    }
    
    public String getFetchTimeStr() {
        return fetchTimeStr;
    }
    
    public void setFetchTimeStr(String fetchTimeStr) {
        this.fetchTimeStr = fetchTimeStr;
    }
    
    public String getInTimeStr() {
        return inTimeStr;
    }
    
    public void setInTimeStr(String inTimeStr) {
        this.inTimeStr = inTimeStr;
    }
    
    public String getDID() {
        return dID;
    }
    
    public void setDID(String did) {
        dID = did;
    }
    
    public String getDName() {
        return dName;
    }
    
    public void setDName(String name) {
        dName = name;
    }
    
    public Date getFetchTime() {
        return fetchTime;
    }
    
    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.util.Date getInTime() {
        return inTime;
    }
    
    public void setInTime(java.util.Date inTime) {
        this.inTime = inTime;
    }
    
    public Integer getSeqNo() {
        return seqNo;
    }
    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }
    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    public String getWiClassName() {
        return wiClassName;
    }
    
    public void setWiClassName(String wiClassName) {
        this.wiClassName = wiClassName;
    }
    
    public String getWiDesc() {
        return wiDesc;
    }
    
    public void setWiDesc(String wiDesc) {
        this.wiDesc = wiDesc;
    }
    
    public String getWiName() {
        return wiName;
    }
    
    public void setWiName(String wiName) {
        this.wiName = wiName;
    }

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getIsHg() {
		return isHg;
	}

	public void setIsHg(String isHg) {
		this.isHg = isHg;
	}
    
}
