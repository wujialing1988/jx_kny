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
 * <li>说明: 局字典表
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
@Table(name="j_gyjc_bureau")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JgyjcBureau  implements java.io.Serializable{

    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 序号 */
    public Integer sort;
    /** 机构编码 */
    @Id 
    @Column(name="B_ID")
    public String bId;
    /** 机构名称 */
    @Column(name="B_NAME")
    public String bName;
    /** 机构简称 */
    public String shortName;
    /** 电报码 */
    public String code;
    /** 启用时间 */
    @Column(name="B_DATE")
    public Date bDate;
    /** 停止时间 */
    @Column(name="E_DATE")
    public Date eDate;
    
    public Date getBDate() {
        return bDate;
    }
    
    public void setBDate(Date date) {
        bDate = date;
    }
    
    public String getBId() {
        return bId;
    }
    
    public void setBId(String id) {
        bId = id;
    }
    
    public String getBName() {
        return bName;
    }
    
    public void setBName(String name) {
        bName = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Date getEDate() {
        return eDate;
    }
    
    public void setEDate(Date date) {
        eDate = date;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    } 
    
    
}
