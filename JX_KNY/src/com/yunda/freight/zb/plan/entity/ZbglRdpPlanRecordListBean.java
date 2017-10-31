package com.yunda.freight.zb.plan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 库列检车辆实体（按车辆查询）
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-6-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpPlanRecordListBean implements java.io.Serializable{
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 车辆ID */
    @Id
    @Column(name = "IDX")
    private String idx; 
   
    /* 车辆编号 */
    @Column(name = "SEQ_NUM")
    private Integer seqNum ;
    
    /* 车辆车型ID */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIdx ;
    
    /* 车辆车型编码 */
    @Column(name = "TRAIN_TYPE_CODE")
    private String trainTypeCode ;
    
    /* 车辆车型名称 */
    @Column(name = "TRAIN_TYPE_NAME")
    private String trainTypeName ;
    
    /* 车辆车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo ;
    
    /* 作业实例ID */
    @Column(name = "rdpIdx")
    private String rdpIdx ;
    
    /* 未完成数量 */
    @Column(name = "UNDONECOUNT")
    private Long unDoneCount; 
    /* 已完成数量 */
    @Column(name = "DONECOUNT")
    private Long doneCount;
    
    public Long getDoneCount() {
        return doneCount;
    }
    
    public void setDoneCount(Long doneCount) {
        this.doneCount = doneCount;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getRdpIdx() {
        return rdpIdx;
    }
    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public Integer getSeqNum() {
        return seqNum;
    }
    
    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeCode() {
        return trainTypeCode;
    }
    
    public void setTrainTypeCode(String trainTypeCode) {
        this.trainTypeCode = trainTypeCode;
    }
    
    public String getTrainTypeIdx() {
        return trainTypeIdx;
    }
    
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }
    
    public String getTrainTypeName() {
        return trainTypeName;
    }
    
    public void setTrainTypeName(String trainTypeName) {
        this.trainTypeName = trainTypeName;
    }
    
    public Long getUnDoneCount() {
        return unDoneCount;
    }
    
    public void setUnDoneCount(Long unDoneCount) {
        this.unDoneCount = unDoneCount;
    }
    
    
    
}
