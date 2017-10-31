package com.yunda.jx.jxgc.tpmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultQCResult实体类, 数据表：提票质量检查
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Entity
@Table(name = "JXGC_Fault_QC_Result")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultQCResult implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 质量检验项状态——未开放 */
    public static final int STATUS_WKF = 0;

    /** 质量检验项状态——待处理 */
    public static final int STATUS_DCL = 1;

    /** 质量检验项状态——已处理 */
    public static final int STATUS_YCL = 2;

    /** 质量检验项状态——已终止 */
    public static final int STATUS_YZZ = 3;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 提票单主键 */
    @Column(name = "Relation_IDX")
    private String relationIDX;
    
    /* 检验项编码 */
    @Column(name = "Check_Item_Code")
    private String checkItemCode;
    
    /* 检验项名称 */
    @Column(name = "Check_Item_Name")
    private String checkItemName;
    
    /* 抽检/必检（1：抽检；2：必检；默认为1） */
    @Column(name = "Check_Way")
    private Integer checkWay;
    
    /* 是否指派（0：否；1：是；默认为0） */
    @Column(name = "Is_Assign")
    private Integer isAssign;
    
    /* 顺序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 检验人员 */
    @Column(name = "QC_EmpID")
    private Long qCEmpID;
    
    /* 检验人员名称 */
    @Column(name = "QC_EmpName")
    private String qCEmpName;
    
    /* 检验时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "QC_Time")
    private java.util.Date qCTime;
    
    /* 备注 */
    private String remarks;
    
    /* 检验状态（0:未开放;1:待处理;2:已处理;3已终止） */
    private Integer status;
    
    /**
     * @return String 获取提票单主键
     */
    public String getRelationIDX() {
        return relationIDX;
    }
    
    /**
     * @param relationIDX 设置提票单主键
     */
    public void setRelationIDX(String relationIDX) {
        this.relationIDX = relationIDX;
    }
    
    /**
     * @return String 获取检验项编码
     */
    public String getCheckItemCode() {
        return checkItemCode;
    }
    
    /**
     * @param checkItemCode 设置检验项编码
     */
    public void setCheckItemCode(String checkItemCode) {
        this.checkItemCode = checkItemCode;
    }
    
    /**
     * @return String 获取检验项名称
     */
    public String getCheckItemName() {
        return checkItemName;
    }
    
    /**
     * @param checkItemName 设置检验项名称
     */
    public void setCheckItemName(String checkItemName) {
        this.checkItemName = checkItemName;
    }
    
    /**
     * @return Integer 获取抽检/必检
     */
    public Integer getCheckWay() {
        return checkWay;
    }
    
    /**
     * @param checkWay 设置抽检/必检
     */
    public void setCheckWay(Integer checkWay) {
        this.checkWay = checkWay;
    }
    
    /**
     * @return Integer 获取是否指派
     */
    public Integer getIsAssign() {
        return isAssign;
    }
    
    /**
     * @param isAssign 设置是否指派
     */
    public void setIsAssign(Integer isAssign) {
        this.isAssign = isAssign;
    }
    
    /**
     * @return Integer 获取顺序号
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
     * @return Long 获取检验人员
     */
    public Long getQCEmpID() {
        return qCEmpID;
    }
    
    /**
     * @param qCEmpID 设置检验人员
     */
    public void setQCEmpID(Long qCEmpID) {
        this.qCEmpID = qCEmpID;
    }
    
    /**
     * @return String 获取检验人员名称
     */
    public String getQCEmpName() {
        return qCEmpName;
    }
    
    /**
     * @param qCEmpName 设置检验人员名称
     */
    public void setQCEmpName(String qCEmpName) {
        this.qCEmpName = qCEmpName;
    }
    
    /**
     * @return java.util.Date 获取检验时间
     */
    public java.util.Date getQCTime() {
        return qCTime;
    }
    
    /**
     * @param qCTime 设置检验时间
     */
    public void setQCTime(java.util.Date qCTime) {
        this.qCTime = qCTime;
    }
    
    /**
     * @return String 获取备注
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * @param remarks 设置备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @return Integer 获取检验状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * @param status 设置检验状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
}
