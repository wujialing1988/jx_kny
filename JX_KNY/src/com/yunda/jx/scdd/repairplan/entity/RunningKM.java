package com.yunda.jx.scdd.repairplan.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: RunningKM实体类, 数据表：走行公里
 * <li>创建人：张凡
 * <li>创建日期：2015-11-04
 * <li>修改人: 何涛
 * <li>修改日期：2016-04-30
 * <li>修改内容：对接北京博飞运安系统走行公里数据
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JCJX_RUNNING_KM")
public class RunningKM implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    public static final String TYPE_C1C6 = "C1C6";
    
    public static final String TYPE_FXDX = "辅修大修";
    
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    /* 主键 */
    @Column(name = "IDX")
    private String idx;
    
    /* 车型主键 */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIdx;
    
    /* 车型 */
    @Column(name = "TRAIN_TYPE")
    private String trainType;
    
    /* 车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo;
    
    /* 登记日期 */
    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date regDate;
    
    /* 开始日期 */
    @Column(name = "BEGIN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beginDate;
    
    /* 结束日期 */
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endDate;
    
    /* 新造后走行公里数 */
    @Column(name = "NEW_RUNNING_KM")
    private Float newRunningKm;
    
    /* 最近走行公里数 */
    @Column(name = "RECENTLY_RUNNING_KM")
    private Float recentlyRunningKm;
    
    /* 修程类型 */
    @Column(name = "REPAIR_TYPE")
    private String repairType;
    
    /* C1F */
    @Column(name = "C1")
    private Float c1;
    
    /* C2X */
    @Column(name = "C2")
    private Float c2;
    
    /* C3Z */
    @Column(name = "C3")
    private Float c3;
    
    /* C4D */
    @Column(name = "C4")
    private Float c4;
    
    /* C5 */
    @Column(name = "C5")
    private Float c5;
    
    /* C6 */
    @Column(name = "C6")
    private Float c6;
    
    /* 下一修程 */
    @Column(name = "REPAIR_CLASS")
    private String repairClass;
    
    /* 下一修程名称 */
    @Column(name = "REPAIR_CLASS_NAME")
    private String repairClassName;
    
    /* 下一修次 */
    @Column(name = "REPAIR_ORDER")
    private String repairOrder;
    
    /* 下一修次名称 */
    @Column(name = "REPAIR_ORDER_NAME")
    private String repairOrderName;
    
    /* 站点ID */
    @Column(name = "SITEID")
    private String siteid;
    
    /*
     * getter setter
     */
    /**
     * @return 获取主键
     */
    public String getIdx() {
        return this.idx;
    }
    
    /**
     * @param idx 设置主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @return 获取车型主键
     */
    public String getTrainTypeIdx() {
        return this.trainTypeIdx;
    }
    
    /**
     * @param trainTypeIdx 设置车型主键
     */
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }
    
    /**
     * @return 获取车型
     */
    public String getTrainType() {
        return this.trainType;
    }
    
    /**
     * @param trainType 设置车型
     */
    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }
    
    /**
     * @return 获取车号
     */
    public String getTrainNo() {
        return this.trainNo;
    }
    
    /**
     * @param trainNo 设置车号
     */
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    /**
     * @return 获取登记日期
     */
    public java.util.Date getRegDate() {
        return this.regDate;
    }
    
    /**
     * @param regDate 设置登记日期
     */
    public void setRegDate(java.util.Date regDate) {
        this.regDate = regDate;
    }
    
    /**
     * @return 获取开始日期
     */
    public java.util.Date getBeginDate() {
        return this.beginDate;
    }
    
    /**
     * @param beginDate 设置开始日期
     */
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    
    /**
     * @return 获取结束日期
     */
    public java.util.Date getEndDate() {
        return this.endDate;
    }
    
    /**
     * @param endDate 设置结束日期
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * @return 获取新造后走行公里数
     */
    public Float getNewRunningKm() {
        return this.newRunningKm;
    }
    
    /**
     * @param newRunningKm 设置新造后走行公里数
     */
    public void setNewRunningKm(Float newRunningKm) {
        this.newRunningKm = newRunningKm;
    }
    
    /**
     * @return 获取最近走行公里数
     */
    public Float getRecentlyRunningKm() {
        return this.recentlyRunningKm;
    }
    
    /**
     * @param recentlyRunningKm 设置最近走行公里数
     */
    public void setRecentlyRunningKm(Float recentlyRunningKm) {
        this.recentlyRunningKm = recentlyRunningKm;
    }
    
    /**
     * @return 获取修程类型
     */
    public String getRepairType() {
        return this.repairType;
    }
    
    /**
     * @param repairType 设置修程类型
     */
    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }
    
    /**
     * @return 获取C1F
     */
    public Float getC1() {
        return this.c1;
    }
    
    /**
     * @param c1 设置C1F
     */
    public void setC1(Float c1) {
        this.c1 = c1;
    }
    
    /**
     * @return 获取C2X
     */
    public Float getC2() {
        return this.c2;
    }
    
    /**
     * @param c2 设置C2X
     */
    public void setC2(Float c2) {
        this.c2 = c2;
    }
    
    /**
     * @return 获取C3Z
     */
    public Float getC3() {
        return this.c3;
    }
    
    /**
     * @param c3 设置C3Z
     */
    public void setC3(Float c3) {
        this.c3 = c3;
    }
    
    /**
     * @return 获取C4D
     */
    public Float getC4() {
        return this.c4;
    }
    
    /**
     * @param c4 设置C4D
     */
    public void setC4(Float c4) {
        this.c4 = c4;
    }
    
    /**
     * @return 获取C5
     */
    public Float getC5() {
        return this.c5;
    }
    
    /**
     * @param c5 设置C5
     */
    public void setC5(Float c5) {
        this.c5 = c5;
    }
    
    /**
     * @return 获取C6
     */
    public Float getC6() {
        return this.c6;
    }
    
    /**
     * @param c6 设置C6
     */
    public void setC6(Float c6) {
        this.c6 = c6;
    }
    
    /**
     * @return 获取下一修程
     */
    public String getRepairClass() {
        return this.repairClass;
    }
    
    /**
     * @param repairClass 设置下一修程
     */
    public void setRepairClass(String repairClass) {
        this.repairClass = repairClass;
    }
    
    /**
     * @return 获取下一修程名称
     */
    public String getRepairClassName() {
        return this.repairClassName;
    }
    
    /**
     * @param repairClassName 设置下一修程名称
     */
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    /**
     * @return 获取下一修次
     */
    public String getRepairOrder() {
        return this.repairOrder;
    }
    
    /**
     * @param repairOrder 设置下一修次
     */
    public void setRepairOrder(String repairOrder) {
        this.repairOrder = repairOrder;
    }
    
    /**
     * @return 获取下一修次名称
     */
    public String getRepairOrderName() {
        return this.repairOrderName;
    }
    
    /**
     * @param repairOrderName 设置下一修次名称
     */
    public void setRepairOrderName(String repairOrderName) {
        this.repairOrderName = repairOrderName;
    }
    
    /**
     * @return 获取站点ID
     */
    public String getSiteid() {
        return this.siteid;
    }
    
    /**
     * @param siteid 设置站点ID
     */
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    
    /**
     * <li>说明：设置车型
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainType 车型
     * @return 走行公里据对象
     */
    public RunningKM trainType(String trainType) {
        this.trainType = trainType;
        return this;
    }
    
    /**
     * <li>说明：设置车型主键
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型主键
     * @return 走行公里据对象
     */
    public RunningKM trainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
        return this;
    }
    
    /**
     * <li>说明：设置车号
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainNo 车号
     * @return 走行公里据对象
     */
    public RunningKM trainNo(String trainNo) {
        this.trainNo = trainNo;
        return this;
    }
    
    /**
     * <li>说明：设置辅修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param c1 辅修公里
     * @return 走行公里据对象
     */
    public RunningKM c1(Float c1) {
        this.c1 = c1;
        return this;
    }
    
    /**
     * <li>说明：设置车型
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param c2 小修公里
     * @return 走行公里据对象
     */
    public RunningKM c2(Float c2) {
        this.c2 = c2;
        return this;
    }
    
    /**
     * <li>说明：设置中修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param c3 中修公里
     * @return 走行公里据对象
     */
    public RunningKM c3(Float c3) {
        this.c3 = c3;
        return this;
    }
    
    /**
     * <li>说明：设置大修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param c4 大修公里
     * @return 走行公里据对象
     */
    public RunningKM c4(Float c4) {
        this.c4 = c4;
        return this;
    }
    
    /**
     * <li>说明：设置大修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param c4 大修公里
     * @return 走行公里据对象
     */
    public RunningKM c5(Float c5) {
        this.c5 = c5;
        return this;
    }
    
    /**
     * <li>说明：设置大修公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param c4 大修公里
     * @return 走行公里据对象
     */
    public RunningKM c6(Float c6) {
        this.c6 = c6;
        return this;
    }
    
    /**
     * <li>说明：设置累计公里
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param newRunningKm 累计公里
     * @return 走行公里据对象
     */
    public RunningKM newRunningKm(Float newRunningKm) {
        this.newRunningKm = newRunningKm;
        return this;
    }
    
    /**
     * <li>说明：设置登记日期
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param regDate 登记日期
     * @return 走行公里据对象
     */
    public RunningKM regDate(Date regDate) {
        this.regDate = regDate;
        return this;
    }
    
    /**
     * <li>说明：设置站点ID
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param siteid 站点ID
     * @return 走行公里据对象
     */
    public RunningKM siteid(String siteid) {
        this.siteid = siteid;
        return this;
    }
    
}
