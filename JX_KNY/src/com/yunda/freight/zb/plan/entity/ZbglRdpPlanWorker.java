package com.yunda.freight.zb.plan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检车辆作业人员
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_PLAN_WORKER")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpPlanWorker implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列检车辆计划 */
    @Column(name = "RDP_Record_IDX")
    private String rdpRecordIdx;
    
    /* 作业人员ID */
    @Column(name = "WORK_PERSON_IDX")
    private String workPersonIdx ;
    
    /* 作业人员姓名 */
    @Column(name = "WORK_PERSON_NAME")
    private String workPersonName ;
    
    /* 队列编码 */
    @Column(name = "QUEUE_NO")
    private String queueNo ;
    
    /* 队列名称 */
    @Column(name = "QUEUE_NAME")
    private String queueName ;
    
    /* 左右侧编码 */
    @Column(name = "POSITION_NO")
    private String positionNo ;
    
    /* 左右侧名称  */
    @Column(name = "POSITION_NAME")
    private String positionName ;
    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getRdpRecordIdx() {
        return rdpRecordIdx;
    }
    
    public void setRdpRecordIdx(String rdpRecordIdx) {
        this.rdpRecordIdx = rdpRecordIdx;
    }


    public String getWorkPersonIdx() {
        return workPersonIdx;
    }

    
    public void setWorkPersonIdx(String workPersonIdx) {
        this.workPersonIdx = workPersonIdx;
    }

    
    public String getWorkPersonName() {
        return workPersonName;
    }

    
    public void setWorkPersonName(String workPersonName) {
        this.workPersonName = workPersonName;
    }


	public String getQueueNo() {
		return queueNo;
	}


	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}


	public String getQueueName() {
		return queueName;
	}


	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}


	public String getPositionNo() {
		return positionNo;
	}


	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}


	public String getPositionName() {
		return positionName;
	}


	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
    
    
}
