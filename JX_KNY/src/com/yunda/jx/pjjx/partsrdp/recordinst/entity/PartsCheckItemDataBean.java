package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 检测项编码（又可视化系统获取）查询类
 * <li>创建人：林欢
 * <li>创建日期：2016-6-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsCheckItemDataBean implements Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 检测项编码 */
    @Column(name = "check_ID")
    private String checkID;
    
    /* 检测值 */
    @Column(name = "check_Value")
    private String checkValue;
    
    /* 配件idx */
    @Column(name = "parts_ID")
    private String partsID;
    
    /* 检测时间 */
    @Column(name = "check_Time")
    private String checkTime;
    
    /* 配件检修计划实际开始时间 */
    @Transient
    private Date realStartTime;
    
    /* 配件检修计划实际结束时间 */
    @Transient
    private Date realEndTime;
    
    public Date getRealEndTime() {
        return realEndTime;
    }
    
    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    public Date getRealStartTime() {
        return realStartTime;
    }
    
    public void setRealStartTime(Date realStartTime) {
        this.realStartTime = realStartTime;
    }
    
    public String getCheckID() {
        return checkID;
    }
    
    public void setCheckID(String checkID) {
        this.checkID = checkID;
    }
    
    public String getCheckTime() {
        return checkTime;
    }
    
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
    
    public String getCheckValue() {
        return checkValue;
    }
    
    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getPartsID() {
        return partsID;
    }
    
    public void setPartsID(String partsID) {
        this.partsID = partsID;
    }
    
}
