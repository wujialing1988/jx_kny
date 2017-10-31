package com.yunda.zb.rdp.zbbill.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车整备单实体bean
 * <li>创建人：程锐
 * <li>创建日期：2015-1-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
public class ZbglRdpBean {
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 车型简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 配属段名称 */
    @Column(name = "D_Name")
    private String dname;
    
    /* 整备站场名称 */
    private String siteName;
    
    /* 整备开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Start_Time")
    private java.util.Date rdpStartTime;
    
    /* 入段时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "In_Time")
    private java.util.Date inTime;
    
    /* 入段去向 */
    @Column(name = "To_Go")
    private String toGo;
    
    /* 检查任务完成情况 */
    private String rdpCount;
    
    /* 提票任务完成情况 */
    private String tpCount;
    
    /* 普查整治任务完成情况 */
    private String pczzCount;
    
    /* 转临修人名称 */
    private String handlePersonName;
    
    /* 转临修时间 */
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date handleTime;
    
    /* 转临修原因 */
    private String handleReason;
    
    public String getDname() {
        return dname;
    }
    
    public void setDName(String name) {
        dname = name;
    }
    
    public String getHandlePersonName() {
        return handlePersonName;
    }
    
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }
    
    public String getHandleReason() {
        return handleReason;
    }
    
    public void setHandleReason(String handleReason) {
        this.handleReason = handleReason;
    }
    
    public java.util.Date getHandleTime() {
        return handleTime;
    }
    
    public void setHandleTime(java.util.Date handleTime) {
        this.handleTime = handleTime;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.util.Date getInTime() {
        return inTime;
    }
    
    public void setInTime(java.util.Date inTime) {
        this.inTime = inTime;
    }
    
    public String getPczzCount() {
        return pczzCount;
    }
    
    public void setPczzCount(String pczzCount) {
        this.pczzCount = pczzCount;
    }
    
    public String getRdpCount() {
        return rdpCount;
    }
    
    public void setRdpCount(String rdpCount) {
        this.rdpCount = rdpCount;
    }
    
    public java.util.Date getRdpStartTime() {
        return rdpStartTime;
    }
    
    public void setRdpStartTime(java.util.Date rdpStartTime) {
        this.rdpStartTime = rdpStartTime;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public String getToGo() {
        return toGo;
    }
    
    public void setToGo(String toGo) {
        this.toGo = toGo;
    }
    
    public String getTpCount() {
        return tpCount;
    }
    
    public void setTpCount(String tpCount) {
        this.tpCount = tpCount;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
}
