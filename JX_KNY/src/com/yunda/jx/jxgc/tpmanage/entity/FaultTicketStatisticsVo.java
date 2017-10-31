package com.yunda.jx.jxgc.tpmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicketStatisticsVo implements java.io.Serializable {

    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /* 主键 */
    @Id
    @Column(name = "RN")
    private Integer idx ;
    
    /* 机车检修作业计划主键 */
    @Column(name = "WORK_PLAN_IDX")
    private String workPlanIDX;
    
    /* 提票类型（数据字典项：如JT6，JT28等） */
    @Column(name = "TYPE")
    private String faultTicketType;
    
    /* 排序 */
    @Column(name = "SORTNO")
    private String sortno;
    
    
    /* 已销活数量 */
    @Column(name = "DONECOUNT")
    private Integer doneCount ;
    
    /* 未销活数量 */
    @Column(name = "UNDOCOUNT")
    private Integer undoCount ;
    
    /* 已确认数量 */
    @Column(name = "DONEAFFIRM")
    private Integer doneAffirm ;
    
    /* 已验收数量 */
    @Column(name = "DONECHECK")
    private Integer doneCheck ;
    
    /* 是否做确认 true 需要做确认 */
    @Column(name = "FILTER1")
    private String filter1 ;
    
    /* 已验收验收 true 需要做验收 */
    @Column(name = "FILTER2")
    private String filter2 ;

    
    public String getFaultTicketType() {
        return faultTicketType;
    }

    
    public void setFaultTicketType(String faultTicketType) {
        this.faultTicketType = faultTicketType;
    }

    
    
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }

    
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }


    
    public Integer getDoneCount() {
        return doneCount;
    }


    
    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
    }


    
    public Integer getUndoCount() {
        return undoCount;
    }


    
    public void setUndoCount(Integer undoCount) {
        this.undoCount = undoCount;
    }


    
    public Integer getDoneAffirm() {
        return doneAffirm;
    }


    
    public void setDoneAffirm(Integer doneAffirm) {
        this.doneAffirm = doneAffirm;
    }


    
    public Integer getDoneCheck() {
        return doneCheck;
    }


    
    public void setDoneCheck(Integer doneCheck) {
        this.doneCheck = doneCheck;
    }


    
    public String getSortno() {
        return sortno;
    }

    
    public void setSortno(String sortno) {
        this.sortno = sortno;
    }

    
    public Integer getIdx() {
        return idx;
    }


    public void setIdx(Integer idx) {
        this.idx = idx;
    }


    
    public String getFilter1() {
        return filter1;
    }


    
    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }


    
    public String getFilter2() {
        return filter2;
    }


    
    public void setFilter2(String filter2) {
        this.filter2 = filter2;
    }
    
    
    
}
