package com.yunda.twt.sensor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TWTSensor实体类, 数据表：传感器注册
 * <li>创建人：程梅
 * <li>创建日期：2015-05-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="TWT_sensor")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TWTSensor implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 集线盒编号 */
    @Column(name = "Box_Code")
    private String boxCode;
    
    /* 传感器编号 */
    @Column(name = "Sensor_Code")
    private String sensorCode;
    
    /* 最小感应门限 */
    @Column(name = "Min_Limit")
    private Long minLimit;
    
    /* 最大感应门限 */
    @Column(name = "Max_Limit")
    private Long maxLimit;
    
    /* 检测周期 */
    @Column(name = "Check_Cycle")
    private Long checkCycle;
    
    /* 安装位置 */
    private String location;
    
    /* 绑定序列号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 绑定台位编码 */
    @Column(name = "Station_Code")
    private String stationCode;
    
    /* 绑定台位名称 */
    @Column(name = "Station_Name")
    private String stationName;
    
    /* 站场 */
    private String siteId;
    
    /**
     * @return String 获取集线盒编号
     */
    public String getBoxCode() {
        return boxCode;
    }
    
    /**
     * @param boxCode 设置集线盒编号
     */
    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }
    
    /**
     * @return String 获取传感器编号
     */
    public String getSensorCode() {
        return sensorCode;
    }
    
    /**
     * @param sensorCode 设置传感器编号
     */
    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }
    
    /**
     * @return Long 获取最小感应门限
     */
    public Long getMinLimit() {
        return minLimit;
    }
    
    /**
     * @param minLimit 设置最小感应门限
     */
    public void setMinLimit(Long minLimit) {
        this.minLimit = minLimit;
    }
    
    /**
     * @return Long 获取最高感应门限
     */
    public Long getMaxLimit() {
        return maxLimit;
    }
    
    /**
     * @param maxLimit 设置最高感应门限
     */
    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }
    
    /**
     * @return Long 获取检测周期
     */
    public Long getCheckCycle() {
        return checkCycle;
    }
    
    /**
     * @param checkCycle 设置检测周期
     */
    public void setCheckCycle(Long checkCycle) {
        this.checkCycle = checkCycle;
    }
    
    /**
     * @return String 获取安装位置
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * @param location 设置安装位置
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * @return Integer 获取绑定序列号
     */
    public Integer getSeqNo() {
        return seqNo;
    }
    
    /**
     * @param seqNo 设置绑定序列号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    /**
     * @return String 获取绑定台位号
     */
    public String getStationCode() {
        return stationCode;
    }
    
    /**
     * @param stationCode 设置绑定台位号
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    
    /**
     * @return String 获取绑定台位名称
     */
    public String getStationName() {
        return stationName;
    }
    
    /**
     * @param stationName 设置绑定台位名称
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
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
    
    public String getSiteId() {
        return siteId;
    }
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}