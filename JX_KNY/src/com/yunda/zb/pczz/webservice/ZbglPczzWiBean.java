package com.yunda.zb.pczz.webservice;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车普查整治查询bean
 * <li>创建人：王利成
 * <li>创建日期：2015-3-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglPczzWiBean {
    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /**  idx主键  */
    @Id
    private String idx;
    
    /* 整备单主键 */
    @Column(name = "Rdp_Idx")
    private String rdpIdx;
    
    /* 车型主键 */
    @Column(name="Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型简称 */
    @Column(name="TRAIN_TYPE_SHORTNAME")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name="Train_No")
    private String trainNo;
    
    /*普查整治主键 */
    @Column(name = "Zbgl_Pczz_Idx")
    private String zbglPczzIdx;
    
    /* 更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private java.util.Date updateTime;
    
    /* 开始日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Start_Date")
    private java.util.Date startDate;
    /* 结束日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="End_Date")
    private java.util.Date endDate;
    
    /* 普查计划名称 */
    @Column(name="pczz_name")
    private String pczzName;
    
    /* 任务单状态 */
    @Column(name="WI_Status")
    private String wIStatus;

    /* 任务要求 */
    @Column(name="PCZZ_Req")
    private String pczzReq;

    
    /* 未完成 */
    @Column(name="notFinishCounts")
    private Integer notFinishCounts;
    
    /* 未检查 */
    @Column(name="notCheckCounts")
    private Integer notCheckCounts;
    
    
    public Integer getNotCheckCounts() {
		return notCheckCounts;
	}


	public void setNotCheckCounts(Integer notCheckCounts) {
		this.notCheckCounts = notCheckCounts;
	}


	public Integer getNotFinishCounts() {
		return notFinishCounts;
	}


	public void setNotFinishCounts(Integer notFinishCounts) {
		this.notFinishCounts = notFinishCounts;
	}


	public java.util.Date getEndDate() {
        return endDate;
    }

    
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getPczzName() {
        return pczzName;
    }

    
    public void setPczzName(String pczzName) {
        this.pczzName = pczzName;
    }

    
    public String getPczzReq() {
        return pczzReq;
    }

    
    public void setPczzReq(String pczzReq) {
        this.pczzReq = pczzReq;
    }

    
    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }

    
    public java.util.Date getStartDate() {
        return startDate;
    }

    
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
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

    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public String getWIStatus() {
        return wIStatus;
    }

    
    public void setWIStatus(String status) {
        wIStatus = status;
    }

    
    public String getZbglPczzIdx() {
        return zbglPczzIdx;
    }

    
    public void setZbglPczzIdx(String zbglPczzIdx) {
        this.zbglPczzIdx = zbglPczzIdx;
    }
    
    
}
