package com.yunda.jx.pjjx.partsrdp.qcinst.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件检修质量检查结果-基于配件检修记录单
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-8-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsQRRecordResult implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* IDX主键 */
    @Id
    private String idx;
    
    /* 作业计划主键 */
    @Column(name = "rdp_idx")
    private String rdpIdx;
    
    /* 检修记录单主键 */
    @Column(name = "rdp_record_idx")
    private String rdpRecordIdx;
    
    /* 检测项主键 */
    @Column(name = "qc_item_idx")
    private String qcItemIdx;
    
    /* 检测项编码 */
    @Column(name = "qc_item_no")
    private String qcItemNo;
    
    /* 检测项名称 */
    @Column(name = "qc_item_name")
    private String qcItemName;
    
    /* 检测人员ID */
    @Column(name = "qc_empid")
    private String qcEmpid;
    
    /* 检测人员名称 */
    @Column(name = "qc_empname")
    private String qcEmpname;
    
    /* 待检检测项个数 */
    @Column(name = "qrCount")
    private Integer qrCount;

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getQcEmpid() {
        return qcEmpid;
    }

    
    public void setQcEmpid(String qcEmpid) {
        this.qcEmpid = qcEmpid;
    }

    
    public String getQcEmpname() {
        return qcEmpname;
    }

    
    public void setQcEmpname(String qcEmpname) {
        this.qcEmpname = qcEmpname;
    }

    
    public String getQcItemIdx() {
        return qcItemIdx;
    }

    
    public void setQcItemIdx(String qcItemIdx) {
        this.qcItemIdx = qcItemIdx;
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

    
    public Integer getQrCount() {
        return qrCount;
    }

    
    public void setQrCount(Integer qrCount) {
        this.qrCount = qrCount;
    }
    
    
    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public String getRdpRecordIdx() {
        return rdpRecordIdx;
    }
    
    public void setRdpRecordIdx(String rdpRecordIdx) {
        this.rdpRecordIdx = rdpRecordIdx;
    }

}
