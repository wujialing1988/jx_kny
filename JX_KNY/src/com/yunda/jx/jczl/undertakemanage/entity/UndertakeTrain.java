package com.yunda.jx.jczl.undertakemanage.entity;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：UndertakeTrain实体类, 数据表：Undertake_train（承修机车）
 * <li>创建人：程梅
 * <li>创建日期：2013-03-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JCZL_UNDERTAKE_TRAIN")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class UndertakeTrain implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
    /* 主键 */
	private String idx;
    /* 承修车型主键 */
    @Column(name="Undertake_Train_Type_IDX")
    private String undertakeTrainTypeIDX;
    /* 车型主键 */
    @Column(name="Train_Type_IDX")
    private String trainTypeIDX;
    /* 车号 */
    @Column(name="Train_No")
    private String trainNo;
    /* 车型英文简称 */
    @Column(name="Train_Type_ShortName")
    private String trainTypeShortName;
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
    @Column(name="Create_Time", updatable=false)
    private java.util.Date createTime;
    /* 修改人 */
    private Long updator;
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private java.util.Date updateTime;

    /* 使用别名称 */
    @Transient
    private String trainUseName;
    /* 配属单位名称 */
    @Transient
    private String holdOrgName;
    /* 制造厂家名 */
    @Transient
    private String makeFactoryName;
    /* 出厂日期 */
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date leaveDate;
    /* 所属局名称 */
    @Transient
    private String bName;
    /* 所属段名称 */
    @Transient
    private String dName;
    public String getBName() {
		return bName;
	}

	public void setBName(String name) {
		bName = name;
	}

	public String getDName() {
		return dName;
	}

	public void setDName(String name) {
		dName = name;
	}

	public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
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
    
    public String getUndertakeTrainTypeIDX() {
        return undertakeTrainTypeIDX;
    }
    
    public void setUndertakeTrainTypeIDX(String undertakeTrainTypeIDX) {
        this.undertakeTrainTypeIDX = undertakeTrainTypeIDX;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
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

    
    public String getHoldOrgName() {
        return holdOrgName;
    }

    
    public void setHoldOrgName(String holdOrgName) {
        this.holdOrgName = holdOrgName;
    }

    
    public java.util.Date getLeaveDate() {
        return leaveDate;
    }

    
    public void setLeaveDate(java.util.Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    
    public String getMakeFactoryName() {
        return makeFactoryName;
    }

    
    public void setMakeFactoryName(String makeFactoryName) {
        this.makeFactoryName = makeFactoryName;
    }

    
    public String getTrainUseName() {
        return trainUseName;
    }

    
    public void setTrainUseName(String trainUseName) {
        this.trainUseName = trainUseName;
    }
}