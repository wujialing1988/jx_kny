package com.yunda.zb.mobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pda整备单返回对象
 * <li>创建人：林欢
 * <li>创建日期：2016-07-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpMobileDTO {
    
    /* 机车整备单idx */
    @Id
    @Column(name = "ZBGL_RDP_IDX")
    private String zbglRdpIDX;
    
    /* 车型主键 */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIDX;
    
    /* 车型简称 */
    @Column(name = "TRAIN_TYPE_SHORTNAME")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo;
    
    /* 入段时间 */
    @Column(name = "IN_TIME")
    private String inTime;
    
    /* 待领活数量 */
    @Column(name = "WAIT_TODO_COUNT")
    private String waitToDoCount;
    
    /* 待销活数量 */
    @Column(name = "WAIT_ONGOING_COUNT")
    private String waitOnGoingCount;
    
    /* 已完成数量 */
    @Column(name = "COMPLET_COUNT")
    private String completCount;
    
    public String getCompletCount() {
        return completCount;
    }
    
    public void setCompletCount(String completCount) {
        this.completCount = completCount;
    }
    
    public String getInTime() {
        return inTime;
    }
    
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    public String getWaitToDoCount() {
        return waitToDoCount;
    }
    
    public void setWaitToDoCount(String waitToDoCount) {
        this.waitToDoCount = waitToDoCount;
    }
    
    public String getZbglRdpIDX() {
        return zbglRdpIDX;
    }
    
    public void setZbglRdpIDX(String zbglRdpIDX) {
        this.zbglRdpIDX = zbglRdpIDX;
    }

	public String getWaitOnGoingCount() {
		return waitOnGoingCount;
	}

	public void setWaitOnGoingCount(String waitOnGoingCount) {
		this.waitOnGoingCount = waitOnGoingCount;
	}
    
}
