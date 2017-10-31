package com.yunda.twt.trainaccessaccount.webservice;

import java.io.Serializable;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: TrainAccessAccountBean实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-1-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TrainAccessAccountBean implements Serializable {
    
    /* 车型简称 */
    private String trainTypeShortName;
    
    /* 车号 */
    private String trainNo;
    
    /* 机车状态 */
    private String trainStatus;
    
    /* 所上设备编号 */
    private String equipNo;
    
    /* 所上设备类型 */
    private String equipClass;
    
    /* 所上设备名称 */
    private String equipName;
    
    /* 上设备时间 */
    private String onEquipTime;
    
    /* 入段站场ID */
    private String siteID;
    
    /* 股道自动化中机车别名 */
    private String trainAliasName;
    
    /* 台位图上机车GUID */
    private String trainGUID;
    
    /* 设备GUID */
    private String equipGUID;
    
    /* 设备顺序 */
    private String equipOrder;
    
    /* 入段时间 */
    private String inTime;
    
    /* 机车出入段台账IDX */
    private String idx;
    
    /* 修程名称 */
    private String repairClassName;
    
    /* 车头方向 */
    private String ctfx;
    
    /* 车型编码 */
    private String trainTypeIDX;
    
    /* 整备机车在段倒计时 */
    private String lastTime;
    
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
    
    public String getEquipClass() {
        return equipClass;
    }
    
    public void setEquipClass(String equipClass) {
        this.equipClass = equipClass;
    }
    
    public String getEquipGUID() {
        return equipGUID;
    }
    
    public void setEquipGUID(String equipGUID) {
        this.equipGUID = equipGUID;
    }
    
    public String getEquipName() {
        return equipName;
    }
    
    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }
    
    public String getEquipNo() {
        return equipNo;
    }
    
    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }
    
    public String getEquipOrder() {
        return equipOrder;
    }
    
    public void setEquipOrder(String equipOrder) {
        this.equipOrder = equipOrder;
    }
    
    public String getInTime() {
        return inTime;
    }
    
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }
    
    public String getOnEquipTime() {
        return onEquipTime;
    }
    
    public void setOnEquipTime(String onEquipTime) {
        this.onEquipTime = onEquipTime;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public String getTrainAliasName() {
        return trainAliasName;
    }
    
    public void setTrainAliasName(String trainAliasName) {
        this.trainAliasName = trainAliasName;
    }
    
    public String getTrainGUID() {
        return trainGUID;
    }
    
    public void setTrainGUID(String trainGUID) {
        this.trainGUID = trainGUID;
    }
    
    public String getTrainStatus() {
        return trainStatus;
    }
    
    public void setTrainStatus(String trainStatus) {
        this.trainStatus = trainStatus;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    public String getCtfx() {
        return ctfx;
    }
    
    public void setCtfx(String ctfx) {
        this.ctfx = ctfx;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }

    
    public String getLastTime() {
        return lastTime;
    }

    
    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
    
}
