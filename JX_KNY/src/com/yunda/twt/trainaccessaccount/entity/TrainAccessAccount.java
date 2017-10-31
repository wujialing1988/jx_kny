package com.yunda.twt.trainaccessaccount.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainAccessAccount实体类, 数据表：机车出入段台账
 * <li>创建人：程锐
 * <li>创建日期：2015-01-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TWT_Train_Access_Account")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainAccessAccount implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    // 当前机车状态
    public static final String TRAINSTATUS_DAIJIAN = "待检";
    
    public static final String TRAINSTATUS_ZHENGZAIJIANCHA = "正在检查（修）";
    
    public static final String TRAINSTATUS_LIANGHAO = "良好";
    
    public static final String TRAINSTATUS_FEIYUNYONG = "非运用";
    
    public static final String TRAINTOGO_ZB = "0101";
    
    public static final String TRAINTOGO_ZB_TYPE = "01";
    
    public static final String TRAINTOGO_JX_TYPE = "02";
    
    public static final String TRAINTOGO_BZB = "03";
    
    public static final String TRAINTOGO_JJL = "0102";
    
    public static final String TRAINTOGO_ZB_CH = "正常整备";
    
    public static final String TRAINTOGO_JJL_CH = "紧交路";
    
    public static final String TRAINTOGO_BZB_CH = "不整备";
    
    public static final String EQUIPCLASS_GD_CH = "轨道";// 所上设备类型-轨道
    
    public static final String EQUIPCLASS_TW_CH = "台位";// 所上设备类型-台位
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 车型编码 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型拼音码 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 配属段编码 */
    @Column(name = "D_ID")
    private String dID;
    
    /* 配属段名称 */
    @Column(name = "D_Name")
    private String dName;
    
    /* 到达车次 */
    @Column(name = "Arrive_Order")
    private String arriveOrder;
    
    /* 入段时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "In_Time")
    private java.util.Date inTime;
    
    /* 入段司机 */
    @Column(name = "In_Driver")
    private String inDriver;
    
    /* 入段去向 */
    @Column(name = "To_Go")
    private String toGo;
    
    /* 入段去向名称 */
    /** Added by hetao at 2015-08-21，用于【再修机车分类信息】统计界面，编辑出入段信息时，回显入段去向字段 */
    @Transient
    private String toGoName;
    
    /* 计划车次 */
    @Column(name = "Plan_Order")
    private String planOrder;
    
    /* 计划出段时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_Out_Time")
    private java.util.Date planOutTime;
    
    /* 出段时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Out_Time")
    private java.util.Date outTime;
    
    /* 出段司机 */
    @Column(name = "Out_Driver")
    private String outDriver;
    
    /* 出段车次 */
    @Column(name = "Out_Order")
    private String outOrder;
    
    /* 入段操作者编码 */
    @Column(name = "In_Handle_Person_ID")
    private Long inHandlePersonID;
    
    /* 入段操作者名称 */
    @Column(name = "In_Handle_Person_Name")
    private String inHandlePersonName;
    
    /* 机车状态 */
    @Column(name = "Train_Status")
    private String trainStatus;
    
    /* 状态超时时长 */
    @Column(name = "Status_OverTime")
    private Integer statusOverTime;
    
    /* 状态开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Time")
    private java.util.Date startTime;
    
    /* 是否预警，1：是；0：否 */
    @Column(name = "Is_Warning")
    private Integer isWarning;
    
    /* 所上设备编号 */
    @Column(name = "Equip_No")
    private String equipNo;
    
    /* 所上设备类型 */
    @Column(name = "Equip_Class")
    private String equipClass;
    
    /* 所上设备名称 */
    @Column(name = "Equip_Name")
    private String equipName;
    
    /* 上设备时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Time2")
    private java.util.Date onEquipTime;
    
    /* 出段操作者编码 */
    @Column(name = "Out_Handle_Person_ID")
    private Long outHandlePersonID;
    
    /* 出段操作者名称 */
    @Column(name = "Out_Handle_Person_Name")
    private String outHandlePersonName;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 入段站场ID */
    @Column(name = "SiteID")
    private String siteID;
    
    /* 入段站场名称 */
    @Column(name = "siteName")
    private String siteName;
    
    /* 股道自动化中机车别名 */
    private String trainAliasName;
    
    /* 台位图上机车GUID */
    private String trainGUID;
    
    /* 设备GUID */
    private String equipGUID;
    
    /* 设备顺序 */
    private String equipOrder;
    
    /* 机车出入段台账主键 */
    @Column(name = "Train_Position_HIS_IDX")
    private String trainPositionHISIDX;
    
    /* 修程编码 */
    @Column(name = "Repair_Class_IDX")
    private String repairClassIDX;
    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;   
    
    /* 车头方向 */
    private String ctfx;
    
    /* 机车出入段台账状态颜色 */
    @Transient
    private String color;
    
    /* 整备机车在段倒计时 */
    @Transient
    private String lastTime;
    
    
    public String getLastTime() {
        return lastTime;
    }

    
    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @return String 获取车型编码
     */
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    /**
     * @param trainTypeIDX 设置车型编码
     */
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    /**
     * @return String 获取车型拼音码
     */
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    /**
     * @param trainTypeShortName 设置车型拼音码
     */
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    /**
     * @return String 获取车号
     */
    public String getTrainNo() {
        return trainNo;
    }
    
    /**
     * @param trainNo 设置车号
     */
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    /**
     * @return String 获取配属段编码
     */
    public String getDID() {
        return dID;
    }
    
    /**
     * @param dID 设置配属段编码
     */
    public void setDID(String dID) {
        this.dID = dID;
    }
    
    /**
     * @return String 获取配属段名称
     */
    public String getDName() {
        return dName;
    }
    
    /**
     * @param dName 设置配属段名称
     */
    public void setDName(String dName) {
        this.dName = dName;
    }
    
    /**
     * @return String 获取到达车次
     */
    public String getArriveOrder() {
        return arriveOrder;
    }
    
    /**
     * @param arriveOrder 设置到达车次
     */
    public void setArriveOrder(String arriveOrder) {
        this.arriveOrder = arriveOrder;
    }
    
    /**
     * @return java.util.Date 获取入段时间
     */
    public java.util.Date getInTime() {
        return inTime;
    }
    
    /**
     * @param inTime 设置入段时间
     */
    public void setInTime(java.util.Date inTime) {
        this.inTime = inTime;
    }
    
    /**
     * @return String 获取入段司机
     */
    public String getInDriver() {
        return inDriver;
    }
    
    /**
     * @param inDriver 设置入段司机
     */
    public void setInDriver(String inDriver) {
        this.inDriver = inDriver;
    }
    
    /**
     * @return String 获取入段去向
     */
    public String getToGo() {
        return toGo;
    }
    
    /**
     * @param toGo 设置入段去向
     */
    public void setToGo(String toGo) {
        this.toGo = toGo;
    }
    
    /**
     * @return 入段去向名称
     */
    public String getToGoName() {
        return toGoName;
    }
    
    /**
     * @param toGoName 设置入段去向名称
     */
    public void setToGoName(String toGoName) {
        this.toGoName = toGoName;
    }
    
    /**
     * @return String 获取计划车次
     */
    public String getPlanOrder() {
        return planOrder;
    }
    
    /**
     * @param planOrder 设置计划车次
     */
    public void setPlanOrder(String planOrder) {
        this.planOrder = planOrder;
    }
    
    /**
     * @return java.util.Date 获取计划出段时间
     */
    public java.util.Date getPlanOutTime() {
        return planOutTime;
    }
    
    /**
     * @param planOutTime 设置计划出段时间
     */
    public void setPlanOutTime(java.util.Date planOutTime) {
        this.planOutTime = planOutTime;
    }
    
    /**
     * @return java.util.Date 获取出段时间
     */
    public java.util.Date getOutTime() {
        return outTime;
    }
    
    /**
     * @param outTime 设置出段时间
     */
    public void setOutTime(java.util.Date outTime) {
        this.outTime = outTime;
    }
    
    /**
     * @return String 获取出段司机
     */
    public String getOutDriver() {
        return outDriver;
    }
    
    /**
     * @param outDriver 设置出段司机
     */
    public void setOutDriver(String outDriver) {
        this.outDriver = outDriver;
    }
    
    /**
     * @return String 获取出段车次
     */
    public String getOutOrder() {
        return outOrder;
    }
    
    /**
     * @param outOrder 设置出段车次
     */
    public void setOutOrder(String outOrder) {
        this.outOrder = outOrder;
    }
    
    /**
     * @return String 获取入段站场名称
     */
    public String getSiteName() {
        return siteName;
    }
    
    /**
     * @param siteName 设置入段站场名称
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    /**
     * @return String 获取入段站场ID
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置入段站场ID
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return Long 获取入段操作者编码
     */
    public Long getInHandlePersonID() {
        return inHandlePersonID;
    }
    
    /**
     * @param inHandlePersonID 设置入段操作者编码
     */
    public void setInHandlePersonID(Long inHandlePersonID) {
        this.inHandlePersonID = inHandlePersonID;
    }
    
    /**
     * @return String 获取入段操作者名称
     */
    public String getInHandlePersonName() {
        return inHandlePersonName;
    }
    
    /**
     * @param inHandlePersonName 设置入段操作者名称
     */
    public void setInHandlePersonName(String inHandlePersonName) {
        this.inHandlePersonName = inHandlePersonName;
    }
    
    /**
     * @return String 获取机车当前状态
     */
    public String getTrainStatus() {
        return trainStatus;
    }
    
    /**
     * @param trainStatus 设置机车当前状态
     */
    public void setTrainStatus(String trainStatus) {
        this.trainStatus = trainStatus;
    }
    
    /**
     * @return Integer 获取状态超时时长
     */
    public Integer getStatusOverTime() {
        return statusOverTime;
    }
    
    /**
     * @param statusOverTime 设置状态超时时长
     */
    public void setStatusOverTime(Integer statusOverTime) {
        this.statusOverTime = statusOverTime;
    }
    
    /**
     * @return java.util.Date 获取当前状态开始时间
     */
    public java.util.Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param startTime 设置当前状态开始时间
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @return Integer 获取是否状态预警
     */
    public Integer getIsWarning() {
        return isWarning;
    }
    
    /**
     * @param isWarning 设置是否状态预警
     */
    public void setIsWarning(Integer isWarning) {
        this.isWarning = isWarning;
    }
    
    /**
     * @return String 获取所上设备编号
     */
    public String getEquipNo() {
        return equipNo;
    }
    
    /**
     * @param equipNo 设置所上设备编号
     */
    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }
    
    /**
     * @return String 获取所上设备类型
     */
    public String getEquipClass() {
        return equipClass;
    }
    
    /**
     * @param equipClass 设置所上设备类型
     */
    public void setEquipClass(String equipClass) {
        this.equipClass = equipClass;
    }
    
    /**
     * @return String 获取所上设备名称
     */
    public String getEquipName() {
        return equipName;
    }
    
    /**
     * @param equipName 设置所上设备名称
     */
    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }
    
    /**
     * @return java.util.Date 获取上设备时间
     */
    public java.util.Date getOnEquipTime() {
        return onEquipTime;
    }
    
    /**
     * @param onEquipTime 设置上设备时间
     */
    public void setOnEquipTime(java.util.Date onEquipTime) {
        this.onEquipTime = onEquipTime;
    }
    
    /**
     * @return Long 获取出段操作者编码
     */
    public Long getOutHandlePersonID() {
        return outHandlePersonID;
    }
    
    /**
     * @param outHandlePersonID 设置出段操作者编码
     */
    public void setOutHandlePersonID(Long outHandlePersonID) {
        this.outHandlePersonID = outHandlePersonID;
    }
    
    /**
     * @return String 获取出段操作者名称
     */
    public String getOutHandlePersonName() {
        return outHandlePersonName;
    }
    
    /**
     * @param outHandlePersonName 设置出段操作者名称
     */
    public void setOutHandlePersonName(String outHandlePersonName) {
        this.outHandlePersonName = outHandlePersonName;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return java.util.Date 获取最新更新时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置最新更新时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    /**
     * @return String 获取机车别名
     */
    public String getTrainAliasName() {
        return trainAliasName;
    }
    
    /**
     * @param trainAliasName 设置机车别名
     */
    public void setTrainAliasName(String trainAliasName) {
        this.trainAliasName = trainAliasName;
    }
    
    /**
     * @return String 获取机车GUID
     */
    public String getTrainGUID() {
        return trainGUID;
    }
    
    /**
     * @param trainGUID 设置机车GUID
     */
    public void setTrainGUID(String trainGUID) {
        this.trainGUID = trainGUID;
    }
    
    /**
     * @return String 获取设备GUID
     */
    public String getEquipGUID() {
        return equipGUID;
    }
    
    /**
     * @param equipGUID 设置设备GUID
     */
    public void setEquipGUID(String equipGUID) {
        this.equipGUID = equipGUID;
    }
    
    /**
     * @return String 获取设备顺序
     */
    public String getEquipOrder() {
        return equipOrder;
    }
    
    /**
     * @param equipOrder 设置设备顺序
     */
    public void setEquipOrder(String equipOrder) {
        this.equipOrder = equipOrder;
    }
    
    /**
     * @return String 获取位置历史信息主键
     */
    public String getTrainPositionHISIDX() {
        return trainPositionHISIDX;
    }
    
    /**
     * @param trainPositionHISIDX 设置位置历史信息主键
     */
    public void setTrainPositionHISIDX(String trainPositionHISIDX) {
        this.trainPositionHISIDX = trainPositionHISIDX;
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
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getCtfx() {
        return ctfx;
    }
    
    public void setCtfx(String ctfx) {
        this.ctfx = ctfx;
    }
    
    public String getRepairClassIDX() {
        return repairClassIDX;
    }
    
    public void setRepairClassIDX(String repairClassIDX) {
        this.repairClassIDX = repairClassIDX;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }

    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
}
