package com.yunda.twt.twtinfo.entity;

import java.util.List;

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
 * <li>说明：SiteVideoNvr实体类, 数据表：视频监控网络硬盘录像机
 * <li>创建人：程锐
 * <li>创建日期：2015-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TWT_Site_Video_Nvr")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class SiteVideoNvr implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 网络录像机名称 */
    @Column(name = "Nvr_Name")
    private String nvrName;
    
    /* IP地址 */
    @Column(name = "Nvr_IP")
    private String nvrIP;
    
    /* 端口号 */
    @Column(name = "Nvr_Port")
    private Integer nvrPort;
    
    /* 用户名 */
    private String username;
    
    /* 密码 */
    private String password;
    
    /* 站场 */
	@Column(updatable = false)
    private String siteID;
    
	/* NVR下属通道号 */
	@Transient
    private List<SiteVideoNvrChanel> chanelList;
    
    /**
     * @return 获取NVR下属通道号
     */
    public List<SiteVideoNvrChanel> getChanelList() {
        return chanelList;
    }

    /**
     * @param chanelList 设置NVR下属通道号
     */
    public void setChanelList(List<SiteVideoNvrChanel> chanelList) {
        this.chanelList = chanelList;
    }

    /**
     * @return 获取idx主键
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
     * @return 获取密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password 设置密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return 获取IP地址
     */
    
    public String getNvrIP() {
        return nvrIP;
    }
    
    /**
     * @param nvrIP 设置IP地址
     */
    public void setNvrIP(String nvrIP) {
        this.nvrIP = nvrIP;
    }
    
    /**
     * @return 获取网络录像机名称
     */
    public String getNvrName() {
        return nvrName;
    }
    
    /**
     * @param nvrName 设置网络录像机名称
     */
    public void setNvrName(String nvrName) {
        this.nvrName = nvrName;
    }
    
    /**
     * @return 获取端口号
     */
    public Integer getNvrPort() {
        return nvrPort;
    }
    
    /**
     * @param nvrPort 设置端口号
     */
    public void setNvrPort(Integer nvrPort) {
        this.nvrPort = nvrPort;
    }
    
    /**
     * @return 获取用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * @param username 设置用户名
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @return hash值
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((nvrIP == null) ? 0 : nvrIP.hashCode());
        result = PRIME * result + ((nvrName == null) ? 0 : nvrName.hashCode());
        result = PRIME * result + ((nvrPort == null) ? 0 : nvrPort.hashCode());
        result = PRIME * result + ((password == null) ? 0 : password.hashCode());
        result = PRIME * result + ((siteID == null) ? 0 : siteID.hashCode());
        result = PRIME * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * @param obj 视频监控网络硬盘录像机对象
     * @return 布尔
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SiteVideoNvr other = (SiteVideoNvr) obj;
        if (nvrIP == null) {
            if (other.nvrIP != null)
                return false;
        } else if (!nvrIP.equals(other.nvrIP))
            return false;
        if (nvrName == null) {
            if (other.nvrName != null)
                return false;
        } else if (!nvrName.equals(other.nvrName))
            return false;
        if (nvrPort == null) {
            if (other.nvrPort != null)
                return false;
        } else if (!nvrPort.equals(other.nvrPort))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (siteID == null) {
            if (other.siteID != null)
                return false;
        } else if (!siteID.equals(other.siteID))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
}
