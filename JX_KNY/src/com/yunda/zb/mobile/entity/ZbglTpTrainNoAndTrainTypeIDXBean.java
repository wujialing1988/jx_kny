package com.yunda.zb.mobile.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.frame.util.DateUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglTpTrainNoAndTrainTypeIDXBean查询类
 * <li>创建人：林欢
 * <li>创建日期：2016-8-4
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */

@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpTrainNoAndTrainTypeIDXBean implements Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @Id
    private String idx;
    
    /** 车号 */
    @Column(name = "train_no")
    private String trainNo;
    
    /** 车型简称 */
    @Column(name = "train_type_shortname")
    private String trainTypeShortName;
    
    /** 待接活数量 */
    @Column(name = "toDoCount")
    private String toDoCount;
    
    /** 待销活数量 */
    @Column(name = "onGoingCount")
    private String onGoingCount;
    
    /** 入段时间 */
    @Transient
    private String inTime;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getInTime() {
        return inTime;
    }
    
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }
    
    public String getOnGoingCount() {
        return onGoingCount;
    }
    
    public void setOnGoingCount(String onGoingCount) {
        this.onGoingCount = onGoingCount;
    }
    
    public String getToDoCount() {
        return toDoCount;
    }
    
    public void setToDoCount(String toDoCount) {
        this.toDoCount = toDoCount;
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
