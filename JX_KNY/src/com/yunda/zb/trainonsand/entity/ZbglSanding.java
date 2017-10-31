package com.yunda.zb.trainonsand.entity;

import java.util.Date;

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
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglSanding实体类, 数据表：机车上砂记录
 * <li>创建人：王利成
 * <li>创建日期：2015-01-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_Sanding")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglSanding implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 机车出入段台账主键 */
	@Column(name="Train_Access_Account_IDX")
	private String trainAccessAccountIDX;
/*	 车型简称 
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	 车型主键 
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	 车号 
	@Column(name="Train_No")
	private String trainNo;*/
	/* 责任人 */
	@Column(name="Duty_PersonId")
	private Long dutyPersonId;
	/* 责任人名称 */
	@Column(name="Duty_PersonName")
	private String dutyPersonName;
	/* 上砂开始时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Start_Time")
	private java.util.Date startTime;
	/* 上砂结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="End_Time")
	private java.util.Date endTime;
	/* 上砂时间（StartTime-EndTime） */
	@Column(name="Sanding_Time")
	private Integer sandingTime;
	/* 标准上砂时间(配置项预设) */
	@Column(name="Standard_Sanding_Time")
	private Integer standardSandingTime;
	/* 机车停留时间(记录红外传感器上下台位时间) */
	@Column(name="Train_Stay_Time")
	private Integer trainStayTime;
	/* 是否超时，0：:否；1：是 */
	@Column(name="Is_OverTime")
	private Integer isOverTime;
    
    /*机车入段时间*/
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date  inTime;
    
    /* 车型简称 */
    @Transient
    private String trainTypeShortName;
    
    /* 车型主键 */
    @Transient
    private String trainTypeIDX;
    
    /* 车号 */
    @Transient
    private String trainNo;
	
	/* 配属段编码 */
	@Transient
	private String dID;
    
	/* 配属段名称 */
	@Transient
	private String dName;
	
	/*站点标识*/
    @Transient
	private String siteId;
	
	/*站点名称*/
    @Transient
	private String siteName;
    
    /*入段开始时间*/
    @Transient
    private String startDate;
    
    /*入段结束时间*/
    @Transient 
    private String overDate;
    
    /* 上砂量 */
    @Column(name="sand_num")
    private String sandNum;
    
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：王利成
     * <li>创建日期：2015-3-17
     * <li>修改人：
     * <li>修改日期：
     */
    public ZbglSanding(){}
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：王利成
     * <li>创建日期：2015-3-17
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 上砂主键
     * @param trainAccessAccountIDX  入库台账主键
     * @param trainTypeIDX   车型主键
     * @param trainTypeShortName  车型简称
     * @param trainNo 车号
     * @param startTime 开始上砂时间
     * @param endTime   结束上砂时间
     * @param sandingTime  上砂时间
     * @param standardSandingTime 标准上砂时间
     * @param dutyPersonId  处理人ID 
     * @param dutyPersonName 处理人姓名
     * @param isOverTime 是否超时
     * @param inTime 入段时间
     * @param dID  配置局ID
     * @param dName  配属局名称
     * @param siteId 站点ID
     * @param siteName  站点名称
     * @param sandNum  上砂量
     */
    public ZbglSanding(String idx, String trainAccessAccountIDX, String trainTypeIDX, String trainTypeShortName, String trainNo, Date startTime,
        Date endTime, Integer sandingTime, Integer standardSandingTime, Long dutyPersonId, String dutyPersonName, Integer isOverTime, Date inTime,
        String dID, String dName, String siteId, String siteName, String sandNum) {
        this.idx = idx;
        this.trainAccessAccountIDX = trainAccessAccountIDX;
        this.trainTypeIDX = trainTypeIDX;
        this.trainTypeShortName = trainTypeShortName;
        this.trainNo = trainNo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sandingTime = sandingTime;
        this.standardSandingTime = standardSandingTime;
        this.dutyPersonId = dutyPersonId;
        this.dutyPersonName = dutyPersonName;
        this.isOverTime = isOverTime;
        this.inTime = inTime;
        this.dID = dID;
        this.dName = dName;
        this.siteId = siteId;
        this.siteName = siteName;
        this.sandNum = sandNum;
    }
    
	/**
	 * @return String 获取机车出入段台账主键
	 */
	public String getTrainAccessAccountIDX(){
		return trainAccessAccountIDX;
	}
	public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
		this.trainAccessAccountIDX = trainAccessAccountIDX;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/* 配属段编码 */
	public String getDID() {
		return dID;
	}
	/* 配属段编码 */
	public void setDID(String did) {
		dID = did;
	}
	/* 配属段名称 */
	public String getDName() {
		return dName;
	}
	/* 配属段名称 */
	public void setDName(String name) {
		dName = name;
	}
	/**
	 * @return Long 获取责任人
	 */
	public Long getDutyPersonId(){
		return dutyPersonId;
	}
	public void setDutyPersonId(Long dutyPersonId) {
		this.dutyPersonId = dutyPersonId;
	}
	/**
	 * @return String 获取责任人名称
	 */
	public String getDutyPersonName(){
		return dutyPersonName;
	}
	public void setDutyPersonName(String dutyPersonName) {
		this.dutyPersonName = dutyPersonName;
	}
	public java.util.Date getStartTime(){
		return startTime;
	}
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return java.util.Date 获取上砂结束时间
	 */
	public java.util.Date getEndTime(){
		return endTime;
	}
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return Integer 获取上砂时间
	 */
	public Integer getSandingTime(){
		return sandingTime;
	}
	public void setSandingTime(Integer sandingTime) {
		this.sandingTime = sandingTime;
	}
	/**
	 * @return Integer 获取标准上砂时间
	 */
	public Integer getStandardSandingTime(){
		return standardSandingTime;
	}

	public void setStandardSandingTime(Integer standardSandingTime) {
		this.standardSandingTime = standardSandingTime;
	}
	/**
	 * @return Integer 获取机车停留时间
	 */
	public Integer getTrainStayTime(){
		return trainStayTime;
	}

	public void setTrainStayTime(Integer trainStayTime) {
		this.trainStayTime = trainStayTime;
	}
	/**
	 * @return Integer 获取是否超时
	 */
	public Integer getIsOverTime(){
		return isOverTime;
	}
	public void setIsOverTime(Integer isOverTime) {
		this.isOverTime = isOverTime;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    
    public String getOverDate() {
        return overDate;
    }

    
    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }

    
    public String getStartDate() {
        return startDate;
    }

    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getSandNum() {
        return sandNum;
    }
    
    public void setSandNum(String sandNum) {
        this.sandNum = sandNum;
    }
    
   /* *//**
     * <li>标题: 机车整备管理信息系统
     * <li>说明: 查询对象实体
     * <li>创建人：王利成
     * <li>创建日期：2015-2-3
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 信息系统事业部整备系统项目组
     * @version 1.0
     *//*
    @Entity
    @JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
    public static class ZbglSandingView implements Serializable {
        
        *//**  类型：long  *//*
        private static final long serialVersionUID = 1L;

        *//**  idx主键  *//*
        @Id
        private String idx;
        
         机车出入段台账主键 
        @Column(name = "Train_Access_Account_IDX")
        private String trainAccessAccountIDX;
        
         车型简称 
        @Column(name = "Train_Type_ShortName")
        private String trainTypeShortName;
        
         车型主键 
        @Column(name = "Train_Type_IDX")
        private String trainTypeIDX;
        
         车号 
        @Column(name = "Train_No")
        private String trainNo;
        
         责任人 
        @Column(name = "Duty_PersonId")
        private Long dutyPersonId;
        
         责任人名称 
        @Column(name = "Duty_PersonName")
        private String dutyPersonName;
        
         上砂开始时间 
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Start_Time")
        private java.util.Date startTime;
        
         上砂结束时间 
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "End_Time")
        private java.util.Date endTime;
        
         上砂时间（StartTime-EndTime） 
        @Column(name = "Sanding_Time")
        private Integer sandingTime;
        
         标准上砂时间(配置项预设) 
        @Column(name = "Standard_Sanding_Time")
        private Integer standardSandingTime;
        
         机车停留时间(记录红外传感器上下台位时间) 
        @Column(name = "Train_Stay_Time")
        private Integer trainStayTime;
        
         是否超时，0：:否；1：是 
        @Column(name = "Is_OverTime")
        private Integer isOverTime;
        
         入库时间 
        @Column(name = "in_time")
        @Temporal(TemporalType.TIMESTAMP)
        private Date inTime;
        
         配属段编码 
        @Column(name = "d_id")
        private String dID;
        
         配属段名称 
        @Column(name = "d_name")
        private String dName;
        
         站点标识 
        @Column(name = "siteid")
        private String siteId;
        
        站点名称
        @Column(name = "sitename")
        private String siteName;
        
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
        
        public Long getDutyPersonId() {
            return dutyPersonId;
        }
        
        public void setDutyPersonId(Long dutyPersonId) {
            this.dutyPersonId = dutyPersonId;
        }
        
        public String getDutyPersonName() {
            return dutyPersonName;
        }
        
        public void setDutyPersonName(String dutyPersonName) {
            this.dutyPersonName = dutyPersonName;
        }
        
        public java.util.Date getEndTime() {
            return endTime;
        }
        
        public void setEndTime(java.util.Date endTime) {
            this.endTime = endTime;
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public Date getInTime() {
            return inTime;
        }
        
        public void setInTime(Date inTime) {
            this.inTime = inTime;
        }
        
        public Integer getIsOverTime() {
            return isOverTime;
        }
        
        public void setIsOverTime(Integer isOverTime) {
            this.isOverTime = isOverTime;
        }
        
        public Integer getSandingTime() {
            return sandingTime;
        }
        
        public void setSandingTime(Integer sandingTime) {
            this.sandingTime = sandingTime;
        }
        
        public String getSiteId() {
            return siteId;
        }
        
        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }
        
        public String getSiteName() {
            return siteName;
        }
        
        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }
        
        public Integer getStandardSandingTime() {
            return standardSandingTime;
        }
        
        public void setStandardSandingTime(Integer standardSandingTime) {
            this.standardSandingTime = standardSandingTime;
        }
        
        public java.util.Date getStartTime() {
            return startTime;
        }
        
        public void setStartTime(java.util.Date startTime) {
            this.startTime = startTime;
        }
        
        public String getTrainAccessAccountIDX() {
            return trainAccessAccountIDX;
        }
        
        public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
            this.trainAccessAccountIDX = trainAccessAccountIDX;
        }
        
        public String getTrainNo() {
            return trainNo;
        }
        
        public void setTrainNo(String trainNo) {
            this.trainNo = trainNo;
        }
        
        public Integer getTrainStayTime() {
            return trainStayTime;
        }
        
        public void setTrainStayTime(Integer trainStayTime) {
            this.trainStayTime = trainStayTime;
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
        
    }
*/


	
}