package com.yunda.jx.jcll.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历附件实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TRAIN_RECORD_ATTACHMENT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainRecordAttachment implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车履历主键 */
    @Column(name = "TRAIN_RECORD_IDX")
    private String trainRecordIdx;
    
    /* 机车履历实例主键 */
    @Column(name = "TRAIN_RECORD_INSTANCE_IDX")
    private String trainRecordInstanceIdx;
    
    
    /* 附件名称 */
    @Column(name = "ATTACHMENT_NAME")
    private String attachmentName;
    
    /* 排序号 */
    @Column(name = "SEQ_NO")
    private Integer seqNo;
    
    /* 记录状态 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /* 更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 修改人 */
    private Long updator;
    
    /* 附件大小 */
    @Transient
    private Long attachmentSize;
    
    /* 附件类型 */
    @Transient
    private String attachmentClass;
    
    /* 附件ID */
    @Transient
    private String attachmentIdx;
    
    
    
    /**
     * <li>说明：无参构造函数
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     */
    public TrainRecordAttachment() {
    }
    
    /**
     * <li>说明：有参构造函数
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 履历附件主键
     * @param trainRecordIdx 履历主键
     * @param trainRecordInstanceIdx 履历实例主键
     * @param attachmentName 附件名称
     * @param seqNo 排序号
     * @param createTime 创建时间
     * @param updateTime 修改时间
     * @param attachmentSize 附件大小
     * @param attachmentClass 附件类型
     * @param attachmentIdx 附件主键
     */
    public TrainRecordAttachment(String idx,String trainRecordIdx,String trainRecordInstanceIdx,String attachmentName,Integer seqNo,
                                Date createTime,Date updateTime,Long attachmentSize,String attachmentClass,String attachmentIdx) {
        this.idx = idx ;
        this.trainRecordIdx = trainRecordIdx ;
        this.trainRecordInstanceIdx = trainRecordInstanceIdx ;
        this.attachmentName = attachmentName ;
        this.seqNo = seqNo ;
        this.createTime = createTime ;
        this.updateTime = updateTime ;
        this.attachmentSize = attachmentSize ;
        this.attachmentClass = attachmentClass ;
        this.attachmentIdx = attachmentIdx ;
    }

    
    public String getAttachmentName() {
        return attachmentName;
    }

    
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    
    public java.util.Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public Integer getSeqNo() {
        return seqNo;
    }

    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    
    public String getTrainRecordIdx() {
        return trainRecordIdx;
    }

    
    public void setTrainRecordIdx(String trainRecordIdx) {
        this.trainRecordIdx = trainRecordIdx;
    }

    
    public String getTrainRecordInstanceIdx() {
        return trainRecordInstanceIdx;
    }

    
    public void setTrainRecordInstanceIdx(String trainRecordInstanceIdx) {
        this.trainRecordInstanceIdx = trainRecordInstanceIdx;
    }

    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }


    
    public Long getCreator() {
        return creator;
    }


    
    public void setCreator(Long creator) {
        this.creator = creator;
    }


    
    public Long getUpdator() {
        return updator;
    }


    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }


    
    public String getAttachmentClass() {
        return attachmentClass;
    }


    
    public void setAttachmentClass(String attachmentClass) {
        this.attachmentClass = attachmentClass;
    }


    
    public String getAttachmentIdx() {
        return attachmentIdx;
    }


    
    public void setAttachmentIdx(String attachmentIdx) {
        this.attachmentIdx = attachmentIdx;
    }


    
    public Long getAttachmentSize() {
        return attachmentSize;
    }


    
    public void setAttachmentSize(Long attachmentSize) {
        this.attachmentSize = attachmentSize;
    }
    
    
}
