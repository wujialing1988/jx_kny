package com.yunda.jcgy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 段字典表
 * <li>创建人：程锐
 * <li>创建日期：2013-4-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="j_gyjc_deport")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JgyjcDeport  implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 机构编码 编码后两位01-39为机务段，41-69为供电段，71-99为水电段*/
    @Id 
    @Column(name="D_ID")
    public String dId;
    /** 机构名称 */
    @Column(name="D_NAME")
    public String dName;
    /** 机构简称 */
    public String shortName;
    /** 机构属性 1机务段 2供电段 3水电段*/
    public String attribute;
    /** 上级机构名称 */
    @Column(name="OWN_BUREAU")
    public String ownBureau;
    /** 启用时间 */
    @Column(name="B_DATE")
    public Date bDate;
    /** 停止时间 */
    @Column(name="E_DATE")
    public Date eDate;
    
    public String getAttribute() {
        return attribute;
    }
    
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    
    public Date getBDate() {
        return bDate;
    }
    
    public void setBDate(Date date) {
        bDate = date;
    }
    
    public String getDId() {
        return dId;
    }
    
    public void setDId(String id) {
        dId = id;
    }
    
    public String getDName() {
        return dName;
    }
    
    public void setDName(String name) {
        dName = name;
    }
    
    public Date getEDate() {
        return eDate;
    }
    
    public void setEDate(Date date) {
        eDate = date;
    }
    
    public String getOwnBureau() {
        return ownBureau;
    }
    
    public void setOwnBureau(String ownBureau) {
        this.ownBureau = ownBureau;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
}
