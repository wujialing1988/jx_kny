package com.yunda.jx.jxgc.workplanmanage.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;



/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修历史查询作业卡查询字段
 * <li>创建人：林欢
 * <li>创建日期：2016-6-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkPlanRepairActivityDTO implements java.io.Serializable {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 作业卡主键idx */
    @Id
    private String idx;
    
    /** 检修活动对应（检修记录单定义）主键 */
    @Column(name = "repairProjectIDX")
    private String repairProjectIDX;
    
    /** 检修活动编码 */
    @Column(name = "activityCode")
    private String activityCode;
    
    /** 检修活动名称 */
    @Column(name = "activityName")
    private String activityName;
    
    /** 完成情况 */
    @Column(name = "completPercent")
    private String completPercent;

    /** 质量检查情况列表 */
    @Transient
    private List rdpQCResultList;
    /** 质量检查项 */
    @Transient
    private String rdpQCResult;
    
    
    public List getRdpQCResultList() {
        return rdpQCResultList;
    }


    
    public void setRdpQCResultList(List rdpQCResultList) {
        this.rdpQCResultList = rdpQCResultList;
    }


    public String getActivityCode() {
        return activityCode;
    }

    
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    
    public String getActivityName() {
        return activityName;
    }

    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    
    public String getCompletPercent() {
        return completPercent;
    }

    
    public void setCompletPercent(String completPercent) {
        this.completPercent = completPercent;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }


    
    public String getRepairProjectIDX() {
        return repairProjectIDX;
    }


    
    public void setRepairProjectIDX(String repairProjectIDX) {
        this.repairProjectIDX = repairProjectIDX;
    }



    
    public String getRdpQCResult() {
        return rdpQCResult;
    }



    
    public void setRdpQCResult(String rdpQCResult) {
        this.rdpQCResult = rdpQCResult;
    }
    
    
}
