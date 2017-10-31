package com.yunda.jx.pjjx.base.recorddefine.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 检测项编码（又可视化系统获取）
 * <li>创建人：林欢
 * <li>创建日期：2016-6-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name = "PJJX_CHECK_ITEM_DEFAULT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsCheckItemDefult implements Serializable{
    
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;

    /** 检测项编号 */
    @Id
    @Column(name = "check_ID")
    private String checkID;

    /** 检测值 */
    @Column(name = "name")
    private String name;

    /** 检测项描述 */
    @Column(name = "check_Desc")
    private String desc;
    
    /** 检测项单位 */
    @Column(name = "unit")
    private String unit;
    
    

    
    public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}

    public String getCheckID() {
        return checkID;
    }
    
    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }

}

