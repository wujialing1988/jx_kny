package com.yunda.twt.twtinfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：SiteWH实体类, 数据表：库
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
@Table(name = "TWT_Site_WH")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class SiteWH implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 站场 */
    private String siteID;
    
    /* 库编码 */
    @Column(name = "WH_Code")
    private String whCode;
    
    /* 库名称 */
    @Column(name = "WH_Name")
    private String whName;
    
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
     * @return String 获取库编码
     */
    public String getWhCode() {
        return whCode;
    }
    
    /**
     * @param whCode 设置库编码
     */
    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }
    
    /**
     * @return String 获取库名称
     */
    public String getWhName() {
        return whName;
    }
    
    /**
     * @param whName 设置库名称
     */
    public void setWhName(String whName) {
        this.whName = whName;
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
