package com.yunda.jxpz.rcrtset.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="jxpz_rc_rt")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class RcRt implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 修程主键 */
	@Column(name="RC_IDX")
	private String rcIDX;
	/* 修次 */
	@Column(name="Repair_time_IDX")
	private String repairtimeIDX;
	/* 修次名称 */
	@Column(name="REPAIR_TIME_NAME")
	private String repairtimeName;
	/* 修次顺序 */
	@Column(name="Repair_time_Seq")
	private Integer repairtimeSeq;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
	/* 修程名称 */
	@Transient
	private String rcName;
	
	public RcRt() {
        super();
    }
    //定义查询修程修次构造方法
    public RcRt(String idx, String rcIDX, String rcName, String repairtimeIDX, String repairtimeName, Integer repairtimeSeq) {
        this.idx = idx;
        this.rcIDX = rcIDX;
        this.rcName = rcName;
        this.repairtimeIDX = repairtimeIDX;
        this.repairtimeName = repairtimeName;
        this.repairtimeSeq = repairtimeSeq;
    }
	/**
	 * @return String 获取修程主键
	 */
	public String getRcIDX(){
		return rcIDX;
	}
	/**
	 * @param 设置修程主键
	 */
	public void setRcIDX(String rcIDX) {
		this.rcIDX = rcIDX;
	}
	/**
	 * @return String 获取修次主键
	 */
	public String getRepairtimeIDX(){
		return repairtimeIDX;
	}
	/**
	 * @param 设置修次主键
	 */
	public void setRepairtimeIDX(String repairtimeIDX) {
		this.repairtimeIDX = repairtimeIDX;
	}
	/**
	 * @return Integer 获取修次顺序
	 */
	public Integer getRepairtimeSeq(){
		return repairtimeSeq;
	}
	/**
	 * @param 设置修次顺序
	 */
	public void setRepairtimeSeq(Integer repairtimeSeq) {
		this.repairtimeSeq = repairtimeSeq;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录状态
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
	 * @param 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
		return creator;
	}
	/**
	 * @param 设置创建人
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
	 * @param 设置创建时间
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
	 * @param 设置修改人
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
	 * @param 设置修改时间
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
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public String getRepairtimeName() {
        return repairtimeName;
    }
    
    public void setRepairtimeName(String repairtimeName) {
        this.repairtimeName = repairtimeName;
    }
	public String getRcName() {
		return rcName;
	}
	public void setRcName(String rcName) {
		this.rcName = rcName;
	}
    
}
