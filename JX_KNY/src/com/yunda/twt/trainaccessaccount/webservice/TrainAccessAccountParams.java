package com.yunda.twt.trainaccessaccount.webservice;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车信息bean 台位图接口传递参数实体类
 * <li>创建人：程锐
 * <li>创建日期：2015-2-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class TrainAccessAccountParams {
    
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
    
    /* 车型拼音码 */
    private String trainTypeShortName;
    
    /* 车号 */
    private String trainNo;
    
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
