package com.yunda.jx.pjjx.partsrdp.qcinst.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件检修质检实体包装类
 * <li>创建人：程锐
 * <li>创建日期：2015-10-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Entity
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsQRBean implements java.io.Serializable {
    
    /** 默认序列号 */
    private static final long serialVersionUID = 1L;
    @Id    
    private String idx;
    
    /* 记录卡实例主键 */
    private String rdpRecordCardIDX;
    
    /* 检查项编码 */
    private String qcItemNo;
    
    /* 检查项名称 */
    private String qcItemName;
    
    /* 作业主键 */
    private String rdpIDX;
    
    /* 记录卡描述 */
    private String recordCardDesc;
    
    /* 记录卡编号 */
    private String recordCardNo;
    
    /* 作业人名称 */
    private String workEmpName;
    
    /* 作业结束时间 */
    private String workEndTime;
    
    /* 作业开始时间 */
    private String workStartTime;
    
    /* 质检人员 */
    @Transient
    private String qcEmpNames;
    
    /* 质检状态*/
    @Transient
    private String status;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public String getRdpRecordCardIDX() {
        return rdpRecordCardIDX;
    }
    
    public void setRdpRecordCardIDX(String rdpRecordCardIDX) {
        this.rdpRecordCardIDX = rdpRecordCardIDX;
    }
    
    public String getRecordCardDesc() {
        return recordCardDesc;
    }
    
    public void setRecordCardDesc(String recordCardDesc) {
        this.recordCardDesc = recordCardDesc;
    }
    
    public String getRecordCardNo() {
        return recordCardNo;
    }
    
    public void setRecordCardNo(String recordCardNo) {
        this.recordCardNo = recordCardNo;
    }
    
    public String getWorkEmpName() {
        return workEmpName;
    }
    
    public void setWorkEmpName(String workEmpName) {
        this.workEmpName = workEmpName;
    }  
    
    
    public String getQcItemName() {
		return qcItemName;
	}

	public void setQcItemName(String qcItemName) {
		this.qcItemName = qcItemName;
	}

	public String getQcItemNo() {
		return qcItemNo;
	}

	public void setQcItemNo(String qcItemNo) {
		this.qcItemNo = qcItemNo;
	}

	public String getWorkEndTime() {
        return workEndTime;
    }
    
    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }
    
    public String getWorkStartTime() {
        return workStartTime;
    }
    
    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime;
    }

    
    public String getQcEmpNames() {
        return qcEmpNames;
    }

    
    public void setQcEmpNames(String qcEmpNames) {
        this.qcEmpNames = qcEmpNames;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }    
}
