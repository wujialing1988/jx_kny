package com.yunda.jx.jxgc.producttaskmanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 节点延误原因查询封装实体
 * <li>创建人：张迪
 * <li>创建日期：2016-7-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
public class NodeCaseDelayQuery {
    @Id
    private String idx;
    
    @Column(name = "node_case_Idx")
    private String nodeCaseIdx;
    
    @Column(name = "node_idx")
    private String nodeIDX;
    
    @Column(name = "Tec_Process_Case_IDX")
    private String tecProcessCaseIDX;
    
    @Column(name = "rdp_idx")
    private String rdpIDX;
    
    @Column(name = "delay_type")
    private String delayType;
   
    
    @Column(name = "delay_time")
    private Integer delayTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_begin_time")
    private Date planBeginTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_end_time")
    private Date planEndTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "real_begin_time")
    private Date realBeginTime;
    
    @Column(name = "delay_reason")
    private String delayReason;
    
    @Column(name = "node_name")
    private String nodeName;
  
    @Column(updatable = false) 
    private Long creator;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private Date createTime;
    
    private String orgname; //机构名称
    private String empname; //人员名称
    @Transient
    private String delayTypeName; //延期类型名称
    public Date getCreateTime() {
        return createTime;
    }


    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    
    public Long getCreator() {
        return creator;
    }


    
    public void setCreator(Long creator) {
        this.creator = creator;
    }


    public String getDelayReason() {
        return delayReason;
    }

    
    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    
    public Integer getDelayTime() {
        return delayTime;
    }

    
    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    
    public String getDelayType() {
        return delayType;
    }

    
    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }

    
    public String getDelayTypeName() {
        return delayTypeName;
    }

    
    public void setDelayTypeName(String delayTypeName) {
        this.delayTypeName = delayTypeName;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getNodeCaseIdx() {
        return nodeCaseIdx;
    }

    
    public void setNodeCaseIdx(String nodeCaseIdx) {
        this.nodeCaseIdx = nodeCaseIdx;
    }

    
    public String getNodeIDX() {
        return nodeIDX;
    }

    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }

    
    public Date getPlanBeginTime() {
        return planBeginTime;
    }

    
    public void setPlanBeginTime(Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }

    
    public Date getPlanEndTime() {
        return planEndTime;
    }

    
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    
    public String getRdpIDX() {
        return rdpIDX;
    }

    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }

    
    public Date getRealBeginTime() {
        return realBeginTime;
    }

    
    public void setRealBeginTime(Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }

    
    public String getTecProcessCaseIDX() {
        return tecProcessCaseIDX;
    }

    
    public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
        this.tecProcessCaseIDX = tecProcessCaseIDX;
    }
    
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }



    
    public String getOrgname() {
        return orgname;
    }



    
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }



    
    public String getEmpname() {
        return empname;
    }



    
    public void setEmpname(String empname) {
        this.empname = empname;
    }
    
}
