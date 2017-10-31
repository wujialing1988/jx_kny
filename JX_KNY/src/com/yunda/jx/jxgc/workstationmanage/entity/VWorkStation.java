package com.yunda.jx.jxgc.workstationmanage.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.jx.jxgc.workplanmanage.entity.VJobProcessNode;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车检修台位占用计划查询实体
 * <li>创建人：何涛
 * <li>创建日期：2015-4-29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class VWorkStation implements Serializable {
    
    /** default */
    private static final long serialVersionUID = 1L;
    
    /* 工位主键 */
    @Id
    private String idx;
    
    /* 工位名称 */
    @Column(name = "WORK_STATION_CODE")
    private String workStationCode;
    
    /* 工位名称 */
    @Column(name = "WORK_STATION_NAME")
    private String workStationName;
    
    /* 流水线主键 */
    @Column(name = "REPAIR_LINE_IDX")
    private String repaireLineIDX;
    
    /* 流水线名称 */
    @Column(name = "REPAIR_LINE_NAME")
    private String repaireLineName;
    
    /* 顺序号 */
    @Column(name = "SEQ_NO")
    private Integer seqNo;
    
    /* 占用台位的机车检修作业计划 - 流程节点 */
    @Transient
    private List<VJobProcessNode> jobProcessNodes;
    
    /* 机车检修作业流程计划状态 */
    @Transient
    private String workPlanStatus;
    
    /* 工位组主键 */
    @Transient
    private String wsGroupIDX;
    
    /* 机车检修作业计划主键 */
    @Transient
    private String trainWorkPlanIDX;
    
    /**
     * @return 获取工位idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @param idx 设置工位idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @return 获取流水线主键
     */
    public String getRepaireLineIDX() {
        return repaireLineIDX;
    }
    
    /**
     * @param repaireLineIDX 设置流水线主键
     */
    public void setRepaireLineIDX(String repaireLineIDX) {
        this.repaireLineIDX = repaireLineIDX;
    }
    
    /**
     * @return 获取流水线名称
     */
    public String getRepaireLineName() {
        return repaireLineName;
    }
    
    /**
     * @param repaireLineName 设置流水线名称
     */
    public void setRepaireLineName(String repaireLineName) {
        this.repaireLineName = repaireLineName;
    }
    
    /**
     * @return 获取顺序号
     */
    public Integer getSeqNo() {
        return seqNo;
    }

    /**
     * @param seqNo 设置顺序号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    /**
     * @return 获取工位编码
     */
    public String getWorkStationCode() {
        return workStationCode;
    }
    
    /**
     * @param workStationCode 设置工位编码
     */
    public void setWorkStationCode(String workStationCode) {
        this.workStationCode = workStationCode;
    }
    
    /**
     * @return 获取工位名称
     */
    public String getWorkStationName() {
        return workStationName;
    }
    
    /**
     * @param workStationName 设置工位名称
     */
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    /**
     * @return 获取占用台位的机车检修作业计划 - 流程节点
     */
    public List<VJobProcessNode> getJobProcessNodes() {
        return jobProcessNodes;
    }
    
    /**
     * @param jobProcessNodes 设置占用台位的机车检修作业计划 - 流程节点
     */
    public void setJobProcessNodes(List<VJobProcessNode> jobProcessNodes) {
        this.jobProcessNodes = jobProcessNodes;
    }
    
    /**
     * @return 获取机车检修作业流程计划状态
     */
    public String getWorkPlanStatus() {
        return workPlanStatus;
    }
    
    /**
     * @param workPlanStatus 设置机车检修作业流程计划状态
     */
    public void setWorkPlanStatus(String workPlanStatus) {
        this.workPlanStatus = workPlanStatus;
    }
    
    /**
     * @return 获取工位组主键
     */
    public String getWsGroupIDX() {
        return wsGroupIDX;
    }
    
    /**
     * @param wsGroupIDX 设置工位组主键
     */
    public void setWsGroupIDX(String wsGroupIDX) {
        this.wsGroupIDX = wsGroupIDX;
    }
    
    /**
     * @return 获取机车检修作业计划主键
     */
    public String getTrainWorkPlanIDX() {
        return trainWorkPlanIDX;
    }
    
    /**
     * @param trainWorkPlanIDX 设置机车检修作业计划主键
     */
    public void setTrainWorkPlanIDX(String trainWorkPlanIDX) {
        this.trainWorkPlanIDX = trainWorkPlanIDX;
    }
    
}
