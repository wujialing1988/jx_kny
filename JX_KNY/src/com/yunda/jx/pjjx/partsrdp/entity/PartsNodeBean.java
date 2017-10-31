package com.yunda.jx.pjjx.partsrdp.entity;

import java.io.Serializable;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件流程节点封装实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-11-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@SuppressWarnings("serial")
public class PartsNodeBean implements Serializable {
    
    /* idx主键 */
    private String idx;
    
    /* 额定工期(单位：分钟) */
    private Double ratedPeriod;
    
    /* 计划开工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date planStartTime;
    
    /* 计划完工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date planEndTime;
    
    /* 父级节点 */
    private String parentIDX;
    
    /* 是否叶子节点,0:否；1：是 */
    private Integer isLeaf;
    
    /* 日历 无此字段，需重构 */
    private String workCalendarIDX;
    
    /* 计划模式 AUTO:自动，MANUAL：手动 无此字段 */
    // private String planMode;
    // 后置节点idx数组
    private String[] nextNodeIDXS;
    
    // 下级第一顺序子节点idx数组
    private String[] childNodeIDXS;
    
    // 下级节点idx数组
    private String[] allChildNodeIDXS;
    
    // 前置节点idx数组
    private String[] preNodeIDXS;
    
    // 是否修改
    private boolean isChanged;
    
    /* 配件检修作业计划主键 */
    private String rdpIDX;
    
    /* 节点名称 */
    private String wpNodeName;
    
    /* 是否已计算过此节点 */
    private boolean hasCal;
    
    /* 记录是修改的这个节点，默认为false未修改，true为修改 */
    private boolean isThisChange;
    
    public String[] getAllChildNodeIDXS() {
        return allChildNodeIDXS;
    }
    
    public void setAllChildNodeIDXS(String[] allChildNodeIDXS) {
        this.allChildNodeIDXS = allChildNodeIDXS;
    }
    
    public String[] getChildNodeIDXS() {
        return childNodeIDXS;
    }
    
    public void setChildNodeIDXS(String[] childNodeIDXS) {
        this.childNodeIDXS = childNodeIDXS;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Integer getIsLeaf() {
        return isLeaf;
    }
    
    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }
    
    public String getParentIDX() {
        return parentIDX;
    }
    
    public void setParentIDX(String parentIDX) {
        this.parentIDX = parentIDX;
    }
    
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    public String getWorkCalendarIDX() {
        return workCalendarIDX;
    }
    
    public void setWorkCalendarIDX(String workCalendarIDX) {
        this.workCalendarIDX = workCalendarIDX;
    }
    
    public boolean isChanged() {
        return isChanged;
    }
    
    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }
    
    public String[] getNextNodeIDXS() {
        return nextNodeIDXS;
    }
    
    public void setNextNodeIDXS(String[] nextNodeIDXS) {
        this.nextNodeIDXS = nextNodeIDXS;
    }
    
    public String[] getPreNodeIDXS() {
        return preNodeIDXS;
    }
    
    public void setPreNodeIDXS(String[] preNodeIDXS) {
        this.preNodeIDXS = preNodeIDXS;
    }
    
    public boolean isHasCal() {
        return hasCal;
    }
    
    public void setHasCal(boolean hasCal) {
        this.hasCal = hasCal;
    }
    
    public boolean isThisChange() {
        return isThisChange;
    }
    
    public void setThisChange(boolean isThisChange) {
        this.isThisChange = isThisChange;
    }
    
    public java.util.Date getPlanStartTime() {
        return planStartTime;
    }
    
    public void setPlanStartTime(java.util.Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    
    public Double getRatedPeriod() {
        return ratedPeriod;
    }
    
    public void setRatedPeriod(Double ratedPeriod) {
        this.ratedPeriod = ratedPeriod;
    }
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public String getWpNodeName() {
        return wpNodeName;
    }
    
    public void setWpNodeName(String wpNodeName) {
        this.wpNodeName = wpNodeName;
    }
    
}
