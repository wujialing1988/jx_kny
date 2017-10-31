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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 提票确认类型配置
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_FAULT_TICKET_RULE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicketCheckRule implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 是否 - 否 */
    public static final int CONST_INT_IS_N = 0;
    /** 是否 - 是 */
    public static final int CONST_INT_IS_Y = 1;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id 
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车提票类型 */
    @Column(name = "FAULT_TICKET_TYPE")
    private String faultTicketType;
    
    /* 是否做确认 1：是，0：否*/
    @Column(name = "IS_AFFIRM")
    private Integer isAffirm;
    
    /* 是否做验收 1：是，0：否*/
    @Column(name = "IS_CHECK")
    private Integer isCheck;
    
    /* 已处理卡控 1：是，0：否*/
    @Column(name = "IS_DONE_CONTROL")
    private Integer isCheckControl;
    
    /* 排序号 */
    @Column(name = "SEQ_NO")
    private Integer seqNo;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;

    
    public java.util.Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    
    public Long getCreator() {
        return creator;
    }

    
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    
    public String getFaultTicketType() {
        return faultTicketType;
    }

    
    public void setFaultTicketType(String faultTicketType) {
        this.faultTicketType = faultTicketType;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Integer getIsAffirm() {
        return isAffirm;
    }

    
    public void setIsAffirm(Integer isAffirm) {
        this.isAffirm = isAffirm;
    }

    
    public Integer getIsCheck() {
        return isCheck;
    }

    
    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    
    public Integer getIsCheckControl() {
        return isCheckControl;
    }

    
    public void setIsCheckControl(Integer isCheckControl) {
        this.isCheckControl = isCheckControl;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public Long getUpdator() {
        return updator;
    }

    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    
    
    
}
