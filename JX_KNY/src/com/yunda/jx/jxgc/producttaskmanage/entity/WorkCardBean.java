package com.yunda.jx.jxgc.producttaskmanage.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WorkCard实体查询类, 数据表：作业卡
 * <li>创建人：何涛
 * <li>创建日期：2016-5-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkCardBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    
    /** 工单名称 */
    @Column(name = "WORK_CARD_NAME")
    private String workCardName;
    
    /** 车型简称 */
    @Column(name = "TRAIN_TYPE_SHORTNAME")
    private String trainSortName;
    
    /** 车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo;
    
    /** 机车检修节点名称 */
    @Column(name = "NODE_NAME")
    private String nodeCaseName;
    
    /** 修程修次 */
    private String repairClassRepairTime;
    
    /** 机车检修作业计划 - 计划开始时间 */
    private String transinTimeStr;
    
    /** 机车检修作业计划 - 计划结束时间 */
    private String planTrainTimeStr;
    
    /** 作业工单时间开始时间字符串 */
    private String realBeginTimeStr;
    /** 作业计划主键 */
    @Column(name = "Rdp_IDX")
    private String rdpIdx;
    
    /** 作业人员 */
    @Transient
    private String worker;
    
    /* 修改人 */
    @Transient
    private Long updator;
    
    /* 修改人 */
    @Transient
    private String updatorName;
    
    // /** 配件编号 */
    // @Column(name = "PARTS_NO")
    // private String partsNo;
    
    /** 状态 */
    private String status;
    
    /** 备注 */
    private String remarks;
    
    /** 工单实际开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REAL_BEGIN_TIME")
    private Date realBeginTime;
    
    /** 工单实际结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REAL_END_TIME")
    private Date realEndTime;
    
    /** 作业任务信息List */
    @Transient
    private List<WorkTaskBean> workTaskBeanList;
    
    /** 机车检修质量检验结果 */
    @Transient
    private List<QCResult> qCResultList;
    @Transient
    private List workcardQcList;
    
    /* 车型车号 */   
    private String trainTypeTrainNo;
    // 无参构造函数
    public WorkCardBean(){super();}
    public WorkCardBean(String idx, String workCardName, String worker, String rdpIdx , Long updator ){
        this.idx = idx;
        this.workCardName = workCardName;
        this.worker = worker;
        this.rdpIdx = rdpIdx;
        this.updator = updator;
    }
    public String getTrainTypeTrainNo() {
        return trainTypeTrainNo;
    }

    
    public void setTrainTypeTrainNo(String trainTypeTrainNo) {
        this.trainTypeTrainNo = trainTypeTrainNo;
    }

    public String getUpdatorName() {
        return updatorName;
    }
    
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
    
    public List<QCResult> getQCResultList() {
        return qCResultList;
    }
    
    public void setQCResultList(List<QCResult> resultList) {
        qCResultList = resultList;
    }
    
    public List<WorkTaskBean> getWorkTaskBeanList() {
        return workTaskBeanList;
    }
    
    public void setWorkTaskBeanList(List<WorkTaskBean> workTaskBeanList) {
        this.workTaskBeanList = workTaskBeanList;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getNodeCaseName() {
        return nodeCaseName;
    }
    
    public void setNodeCaseName(String nodeCaseName) {
        this.nodeCaseName = nodeCaseName;
    }
    
    // public String getPartsNo() {
    // return partsNo;
    // }
    //    
    // public void setPartsNo(String partsNo) {
    // this.partsNo = partsNo;
    // }
    
    public String getPlanTrainTimeStr() {
        return planTrainTimeStr;
    }
    
    public void setPlanTrainTimeStr(String planTrainTimeStr) {
        this.planTrainTimeStr = planTrainTimeStr;
    }
    
    public Date getRealBeginTime() {
        return realBeginTime;
    }
    
    public void setRealBeginTime(Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }
    
    public String getRealBeginTimeStr() {
        return realBeginTimeStr;
    }
    
    public void setRealBeginTimeStr(String realBeginTimeStr) {
        this.realBeginTimeStr = realBeginTimeStr;
    }
    
    public Date getRealEndTime() {
        return realEndTime;
    }
    
    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getRepairClassRepairTime() {
        return repairClassRepairTime;
    }
    
    public void setRepairClassRepairTime(String repairClassRepairTime) {
        this.repairClassRepairTime = repairClassRepairTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainSortName() {
        return trainSortName;
    }
    
    public void setTrainSortName(String trainSortName) {
        this.trainSortName = trainSortName;
    }
    
    public String getTransinTimeStr() {
        return transinTimeStr;
    }
    
    public void setTransinTimeStr(String transinTimeStr) {
        this.transinTimeStr = transinTimeStr;
    }
    
    public String getWorkCardName() {
        return workCardName;
    }
    
    public void setWorkCardName(String workCardName) {
        this.workCardName = workCardName;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }


    
    public String getRdpIdx() {
        return rdpIdx;
    }


    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public String getWorker() {
        return worker;
    }
    
    public void setWorker(String worker) {
        this.worker = worker;
    }
    
    public List getWorkcardQcList() {
        return workcardQcList;
    }
    
    public void setWorkcardQcList(List workcardQcList) {
        this.workcardQcList = workcardQcList;
    }

}
