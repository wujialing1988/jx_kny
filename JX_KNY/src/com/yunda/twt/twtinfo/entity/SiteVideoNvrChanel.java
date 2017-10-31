package com.yunda.twt.twtinfo.entity;

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
 * <li>标题：机车整备管理信息系统
 * <li>说明：SiteVideoNvrChanel实体类, 数据表：网络录像机通道
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TWT_Site_Video_Nvr_Chanel")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class SiteVideoNvrChanel implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 网络录像机idx主键 */
    @Column(name = "Video_Nvr_IDX")
    private String videoNvrIDX;
    
    /* 网络录像机名称 */
    @Transient
    private String videoNvrName;
    
    /* 通道号 */
    @Column(name = "Chanel_ID")
    private Integer chanelID;
    
    /* 通道名称 */
    @Column(name = "Chanel_Name")
    private String chanelName;
    
    /* 站场 */
	@Column(updatable = false)
    private String siteID;
    
    /* 视频点编码 */
    @Column(name = "Video_Code")
    private String videoCode;
    
    /* 视频点名称 */
    @Column(name = "Video_Name")
    private String videoName;
    
    @Transient
    /* 所属NVR实例对象 */
    private SiteVideoNvr siteVideoNvr;
    
    /**
     * default constructor
     */
    public SiteVideoNvrChanel() {
		super();
	}

	/**
	 * @param idx dx主键
	 * @param videoNvrIDX 网络录像机idx主键
	 * @param videoNvrName 网络录像机名称
	 * @param chanelID 通道号
	 * @param chanelName 通道名称
	 * @param siteID 站场ID
	 * @param videoCode 视频点编码
	 * @param videoName 视频点名称
	 */
	public SiteVideoNvrChanel(String idx, String videoNvrIDX, String videoNvrName, Integer chanelID, String chanelName, String siteID, String videoCode, String videoName) {
		super();
		this.idx = idx;
		this.videoNvrIDX = videoNvrIDX;
		this.videoNvrName = videoNvrName;
		this.chanelID = chanelID;
		this.chanelName = chanelName;
		this.siteID = siteID;
		this.videoCode = videoCode;
		this.videoName = videoName;
	}

	/**
     * @return 获取通道号
     */
    public Integer getChanelID() {
        return chanelID;
    }
    
    /**
     * @param chanelID 设置通道号
     */
    public void setChanelID(Integer chanelID) {
        this.chanelID = chanelID;
    }
    
    /**
     * @return 获取通道名称
     */
    public String getChanelName() {
        return chanelName;
    }
    
    /**
     * @param chanelName 设置通道名称
     */
    public void setChanelName(String chanelName) {
        this.chanelName = chanelName;
    }
    
    /**
     * @return 获取网络录像机idx主键
     */
    public String getVideoNvrIDX() {
        return videoNvrIDX;
    }
    
    /**
     * @param videoNvrIDX 设置网络录像机idx主键
     */
    public void setVideoNvrIDX(String videoNvrIDX) {
        this.videoNvrIDX = videoNvrIDX;
    }
    
    /**
     * @return 获取网络录像机名称
     */
    public String getVideoNvrName() {
		return videoNvrName;
	}

	/**
	 * @param videoNvrName 设置网络录像机名称
	 */
	public void setVideoNvrName(String videoNvrName) {
		this.videoNvrName = videoNvrName;
	}

	/**
     * @return String 获取站场
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置站场
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return String 获取视频点编码
     */
    public String getVideoCode() {
        return videoCode;
    }
    
    /**
     * @param videoCode 设置视频点编码
     */
    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }
    
    /**
     * @return String 获取视频点名称
     */
    public String getVideoName() {
        return videoName;
    }
    
    /**
     * @param videoName 设置视频点名称
     */
    public void setVideoName(String videoName) {
        this.videoName = videoName;
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

	/**
	 * @return 获取所属NVR实例对象
	 */
	public SiteVideoNvr getSiteVideoNvr() {
		return siteVideoNvr;
	}

	/**
	 * @param siteVideoNvr 设置所属NVR实例对象
	 */
	public void setSiteVideoNvr(SiteVideoNvr siteVideoNvr) {
		this.siteVideoNvr = siteVideoNvr;
	}
    
}
