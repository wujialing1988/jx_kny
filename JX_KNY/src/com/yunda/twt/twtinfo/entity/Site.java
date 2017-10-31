package com.yunda.twt.twtinfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Site实体类, 数据表：站场
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
@Table(name = "TWT_Site")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Site implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 站场 */
    @Id
    private String siteID;
    
    /* 站场名称 */
    private String siteName;
    
    /* 主键 */
    @Column(name = "Map_Name")
    private String mapName;
    
    /* IP地址 */
    @Column(name = "IP_Address")
    private String ipAddress;
    
    /* Web地址 */
    @Column(name = "Web_Address")
    private String webAddress;
    
    /* 是否有股道自动化系统 */
    @Column(name = "Have_Track_System")
    private String haveTrackSystem;
    
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
     * @return String 获取站场名称
     */
    public String getSiteName() {
        return siteName;
    }
    
    /**
     * @param siteName 设置站场名称
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    /**
     * @return String 获取台位图名称
     */
    public String getMapName() {
        return mapName;
    }
    
    /**
     * @param mapName 设置台位图名称
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
    
    /**
     * @return String 获取IP地址
     */
    public String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * @param ipAddress 设置IP地址
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    /**
     * @return String 获取Web地址
     */
    public String getWebAddress() {
        return webAddress;
    }
    
    /**
     * @param webAddress 设置Web地址
     */
    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }
    
    /**
     * @return String 获取是否有股道自动化系统
     */
    public String getHaveTrackSystem() {
        return haveTrackSystem;
    }
    
    /**
     * @param haveTrackSystem 设置是否有股道自动化系统
     */
    public void setHaveTrackSystem(String haveTrackSystem) {
        this.haveTrackSystem = haveTrackSystem;
    }
}
