package com.yunda.jx.jxgc.workplanmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;



/**
 * <li>标题: 在修机车列表及工单数据实体
 * <li>说明: 类的功能描述
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-11-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainWorkPlanCount implements java.io.Serializable {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 作业卡主键idx */
    @Id
    private String idx;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;
    
    /* 修次名称 */
    @Column(name = "Repair_time_Name")
    private String repairtimeName;
    
    /* 范围活待修数量 */
    @Column(name = "fwhUndoCount")
    private Integer fwhUndoCount;
    
    /* 提票活待修数量 */
    @Column(name = "tphUndoCount")
    private Integer tphUndoCount;

    
    public Integer getFwhUndoCount() {
        return fwhUndoCount;
    }

    
    public void setFwhUndoCount(Integer fwhUndoCount) {
        this.fwhUndoCount = fwhUndoCount;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getRepairClassName() {
        return repairClassName;
    }

    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }

    
    public String getRepairtimeName() {
        return repairtimeName;
    }

    
    public void setRepairtimeName(String repairtimeName) {
        this.repairtimeName = repairtimeName;
    }

    
    public Integer getTphUndoCount() {
        return tphUndoCount;
    }

    
    public void setTphUndoCount(Integer tphUndoCount) {
        this.tphUndoCount = tphUndoCount;
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
