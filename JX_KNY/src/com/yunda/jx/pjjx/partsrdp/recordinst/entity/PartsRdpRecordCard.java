package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpRecordCard实体类, 数据表：配件检修记录卡实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_Parts_Rdp_Record_Card")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartsRdpRecordCard implements java.io.Serializable {
    
    /** 检修记录卡实例状态 - 已终止 */
    public static final String STATUS_YZZ = "10";
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 回退次数 */
    @Column(name = "Back_Count")
    private Integer backCount;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 领活人 */
    @Column(name = "Handle_EmpID")
    private Long handleEmpID;
    
    /* 领活人名称 */
    @Column(name = "Handle_EmpName")
    private String handleEmpName;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 回退标识，0：正常，1：返工修 */
    @Column(name = "Is_Back")
    private Integer isBack;
    
    /* 质量检验 */
    @Column(name = "QC_Content")
    private String qcContent;
    
    /* 作业主键 */
    @Column(name = "Rdp_IDX")
    private String rdpIDX;
    
    /* 作业节点主键 */
    @Column(name = "Rdp_Node_IDX")
    private String rdpNodeIDX;
    
    /* 记录卡描述 */
    @Column(name = "Record_Card_Desc")
    private String recordCardDesc;
    
    /* 记录卡主键 */
    @Column(name = "Record_Card_IDX")
    private String recordCardIDX;
    
    /* 记录单主键 */
    @Column(name = "Rdp_Record_IDX")
    private String rdpRecordIDX;
    
    /* 记录卡编号 */
    @Column(name = "Record_Card_No")
    private String recordCardNo;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 检修情况描述 */
    private String remarks;
    
    /* 顺序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 状态 */
    private String status;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 作业人 */
    @Column(name = "Work_EmpID")
    private String workEmpID;
    
    /* 作业人名称 */
    @Column(name = "Work_EmpName")
    private String workEmpName;
    
    /* 作业结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Work_EndTime")
    private java.util.Date workEndTime;
    
    /* 作业开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Work_StartTime")
    private java.util.Date workStartTime;
    
    /* 检测检修项集合(用于查询) */
    @Transient
    private List<PartsRdpRecordRI> partsRdpRecordRiList ;

    /* 质量检查集合(用于查询) */
    @Transient
    private List<PartsRdpQR> partsRdpQRList ;
     
    /**
     * @return Integer 获取回退次数
     */
    public Integer getBackCount() {
        return backCount;
    }
    
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    /**
     * @return Long 获取领活人
     */
    public Long getHandleEmpID() {
        return handleEmpID;
    }
    
    /**
     * @return String 获取领活人名称
     */
    public String getHandleEmpName() {
        return handleEmpName;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @return Integer 获取回退标识
     */
    public Integer getIsBack() {
        return isBack;
    }
    
    /**
     * @return String 获取质量检验
     */
    public String getQcContent() {
        return qcContent;
    }
    
    /**
     * @return String 获取作业主键
     */
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    /**
     * @return String 获取作业节点主键
     */
    public String getRdpNodeIDX() {
        return rdpNodeIDX;
    }
    
    /**
     * @return String 获取记录卡描述
     */
    public String getRecordCardDesc() {
        return recordCardDesc;
    }
    
    /**
     * @return String 获取记录卡主键
     */
    public String getRecordCardIDX() {
        return recordCardIDX;
    }
    
    /**
     * @return 获取记录单主键
     */
    public String getRdpRecordIDX() {
        return rdpRecordIDX;
    }
    
    /**
     * @param rdpRecordIDX 设置记录单主键
     */
    public void setRdpRecordIDX(String rdpRecordIDX) {
        this.rdpRecordIDX = rdpRecordIDX;
    }
    
    /**
     * @return String 获取记录卡编号
     */
    public String getRecordCardNo() {
        return recordCardNo;
    }
    
    /**
     * @return Integer 获取记录的状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @return 获取检修情况描述
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * @return Integer 获取顺序号
     */
    public Integer getSeqNo() {
        return seqNo;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @return String 获取状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator() {
        return updator;
    }
    
    /**
     * @return String 获取作业人
     */
    public String getWorkEmpID() {
        return workEmpID;
    }
    
    /**
     * @return String 获取作业人名称
     */
    public String getWorkEmpName() {
        return workEmpName;
    }
    
    /**
     * @return java.util.Date 获取作业结束时间
     */
    public java.util.Date getWorkEndTime() {
        return workEndTime;
    }
    
    /**
     * @return java.util.Date 获取作业开始时间
     */
    public java.util.Date getWorkStartTime() {
        return workStartTime;
    }
    
    /**
     * @param backCount 设置回退次数
     */
    public void setBackCount(Integer backCount) {
        this.backCount = backCount;
    }
    
    /**
     * @param createTime 设置创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @param creator 设置创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    /**
     * @param handleEmpID 设置领活人
     */
    public void setHandleEmpID(Long handleEmpID) {
        this.handleEmpID = handleEmpID;
    }
    
    /**
     * @param handleEmpName 设置领活人名称
     */
    public void setHandleEmpName(String handleEmpName) {
        this.handleEmpName = handleEmpName;
    }
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @param isBack 设置回退标识
     */
    public void setIsBack(Integer isBack) {
        this.isBack = isBack;
    }
    
    /**
     * @param qCContent 设置质量检验
     */
    public void setQcContent(String qCContent) {
        this.qcContent = qCContent;
    }
    
    /**
     * @param rdpIDX 设置作业主键
     */
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    /**
     * @param rdpNodeIDX 设置作业节点主键
     */
    public void setRdpNodeIDX(String rdpNodeIDX) {
        this.rdpNodeIDX = rdpNodeIDX;
    }
    
    /**
     * @param recordCardDesc 设置记录卡描述
     */
    public void setRecordCardDesc(String recordCardDesc) {
        this.recordCardDesc = recordCardDesc;
    }
    
    /**
     * @param recordCardIDX 设置记录卡主键
     */
    public void setRecordCardIDX(String recordCardIDX) {
        this.recordCardIDX = recordCardIDX;
    }
    
    /**
     * @param recordCardNo 设置记录卡编号
     */
    public void setRecordCardNo(String recordCardNo) {
        this.recordCardNo = recordCardNo;
    }
    
    /**
     * @param recordStatus 设置记录的状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @param remarks 设置检修情况描述
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @param seqNo 设置顺序号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    /**
     * @param siteID 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @param status 设置状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @param updateTime 设置修改时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    /**
     * @param updator 设置修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    /**
     * @param workEmpID 设置作业人
     */
    public void setWorkEmpID(String workEmpID) {
        this.workEmpID = workEmpID;
    }
    
    /**
     * @param workEmpName 设置作业人名称
     */
    public void setWorkEmpName(String workEmpName) {
        this.workEmpName = workEmpName;
    }
    
    /**
     * @param workEndTime 设置作业结束时间
     */
    public void setWorkEndTime(java.util.Date workEndTime) {
        this.workEndTime = workEndTime;
    }
    
    /**
     * @param workStartTime 设置作业开始时间
     */
    public void setWorkStartTime(java.util.Date workStartTime) {
        this.workStartTime = workStartTime;
    }
    
    /**
     * <li>标题: 机车检修管理信息系统
     * <li>说明：PartsRdpRecordCardSearch实体类, PartsRdpRecordCard的查询实体类
     * <li>创建人： 何涛
     * <li>创建日期： 2014-12-17 上午11:53:32
     * <li>修改人:
     * <li>修改日期：
     * <li>修改内容：
     * <li>版权: Copyright (c) 2008 运达科技公司
     * @author 测控部检修系统项目组
     * @version 1.0
     */
    @Entity
    public static class PartsRdpRecordCardSearch implements Serializable {
        
        /** 使用默认序列版本ID */
        private static final long serialVersionUID = 1L;
        
        /** 相应记录工单下未处理的检修检测项记录数[riCounts] */
        private Integer riCounts;

        public Integer getRiCounts() {
            return riCounts;
        }

        public void setRiCounts(Integer riCounts) {
            this.riCounts = riCounts;
        }
        
        /* 回退次数 */
        @Column(name = "Back_Count")
        private Integer backCount;
        
        /* 创建时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Create_Time", updatable = false)
        private java.util.Date createTime;
        
        /* 创建人 */
        @Column(updatable = false)
        private Long creator;
        
        /* 领活人 */
        @Column(name = "Handle_EmpID")
        private Long handleEmpID;
        
        /* 领活人名称 */
        @Column(name = "Handle_EmpName")
        private String handleEmpName;
        
        /* idx主键 */
        @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
        @Id
        @GeneratedValue(generator = "uuid_id_generator")
        private String idx;
        
        /* 回退标识，0：正常，1：返工修 */
        @Column(name = "Is_Back")
        private Integer isBack;
        
        /* 质量检验 */
        @Column(name = "QC_Content")
        private String qcContent;
        
        /* 作业主键 */
        @Column(name = "Rdp_IDX")
        private String rdpIDX;
        
        /* 作业节点主键 */
        @Column(name = "Rdp_Node_IDX")
        private String rdpNodeIDX;
        
        /* 记录卡描述 */
        @Column(name = "Record_Card_Desc")
        private String recordCardDesc;
        
        /* 记录卡主键 */
        @Column(name = "Record_Card_IDX")
        private String recordCardIDX;
        
        /* 记录单主键 */
        @Column(name = "Rdp_Record_IDX")
        private String rdpRecordIDX;
        
        /* 记录卡编号 */
        @Column(name = "Record_Card_No")
        private String recordCardNo;
        
        /* 表示此条记录的状态：0为表示未删除；1表示删除 */
        @Column(name = "Record_Status")
        private Integer recordStatus;
        
        /* 检修情况描述 */
        private String remarks;
        
        /* 顺序号 */
        @Column(name = "Seq_No")
        private Integer seqNo;
        
        /* 站点标识，为了同步数据而使用 */
        @Column(updatable = false)
        private String siteID;
        
        /* 状态 */
        private String status;
        
        /* 修改时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Update_Time")
        private java.util.Date updateTime;
        
        /* 修改人 */
        private Long updator;
        
        /* 作业人 */
        @Column(name = "Work_EmpID")
        private String workEmpID;
        
        /* 作业人名称 */
        @Column(name = "Work_EmpName")
        private String workEmpName;
        
        /* 作业结束时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Work_EndTime")
        private java.util.Date workEndTime;
        
        /* 作业开始时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Work_StartTime")
        private java.util.Date workStartTime;
        
        /* 质量检查信息列表 */
        @Transient
        private List<PartsRdpQR> qrList;
        
        /**
         * @return 获取质量检查信息列表
         */
        public List<PartsRdpQR> getQrList() {
            return qrList;
        }
        
        /**
         * @param qrList 设置质量检查信息列表
         */
        public void setQrList(List<PartsRdpQR> qrList) {
            this.qrList = qrList;
        }

        /**
         * @return Integer 获取回退次数
         */
        public Integer getBackCount() {
            return backCount;
        }
        
        /**
         * @return java.util.Date 获取创建时间
         */
        public java.util.Date getCreateTime() {
            return createTime;
        }
        
        /**
         * @return Long 获取创建人
         */
        public Long getCreator() {
            return creator;
        }
        
        /**
         * @return Long 获取领活人
         */
        public Long getHandleEmpID() {
            return handleEmpID;
        }
        
        /**
         * @return String 获取领活人名称
         */
        public String getHandleEmpName() {
            return handleEmpName;
        }
        
        /**
         * @return String idx主键
         */
        public String getIdx() {
            return idx;
        }
        
        /**
         * @return Integer 获取回退标识
         */
        public Integer getIsBack() {
            return isBack;
        }
        
        /**
         * @return String 获取质量检验
         */
        public String getQcContent() {
            return qcContent;
        }
        
        /**
         * @return String 获取作业主键
         */
        public String getRdpIDX() {
            return rdpIDX;
        }
        
        /**
         * @return String 获取作业节点主键
         */
        public String getRdpNodeIDX() {
            return rdpNodeIDX;
        }
        
        /**
         * @return String 获取记录卡描述
         */
        public String getRecordCardDesc() {
            return recordCardDesc;
        }
        
        /**
         * @return String 获取记录卡主键
         */
        public String getRecordCardIDX() {
            return recordCardIDX;
        }
        
        /**
         * @return 获取记录单主键
         */
        public String getRdpRecordIDX() {
            return rdpRecordIDX;
        }
        
        /**
         * @param rdpRecordIDX 设置记录单主键
         */
        public void setRdpRecordIDX(String rdpRecordIDX) {
            this.rdpRecordIDX = rdpRecordIDX;
        }
        
        /**
         * @return String 获取记录卡编号
         */
        public String getRecordCardNo() {
            return recordCardNo;
        }
        
        /**
         * @return Integer 获取记录的状态
         */
        public Integer getRecordStatus() {
            return recordStatus;
        }
        
        /**
         * @return 获取检修情况描述
         */
        public String getRemarks() {
            return remarks;
        }
        
        /**
         * @return Integer 获取顺序号
         */
        public Integer getSeqNo() {
            return seqNo;
        }
        
        /**
         * @return String 获取站点标识
         */
        public String getSiteID() {
            return siteID;
        }
        
        /**
         * @return String 获取状态
         */
        public String getStatus() {
            return status;
        }
        
        /**
         * @return java.util.Date 获取修改时间
         */
        public java.util.Date getUpdateTime() {
            return updateTime;
        }
        
        /**
         * @return Long 获取修改人
         */
        public Long getUpdator() {
            return updator;
        }
        
        /**
         * @return String 获取作业人
         */
        public String getWorkEmpID() {
            return workEmpID;
        }
        
        /**
         * @return String 获取作业人名称
         */
        public String getWorkEmpName() {
            return workEmpName;
        }
        
        /**
         * @return java.util.Date 获取作业结束时间
         */
        public java.util.Date getWorkEndTime() {
            return workEndTime;
        }
        
        /**
         * @return java.util.Date 获取作业开始时间
         */
        public java.util.Date getWorkStartTime() {
            return workStartTime;
        }
        
        /**
         * @param backCount 设置回退次数
         */
        public void setBackCount(Integer backCount) {
            this.backCount = backCount;
        }
        
        /**
         * @param createTime 设置创建时间
         */
        public void setCreateTime(java.util.Date createTime) {
            this.createTime = createTime;
        }
        
        /**
         * @param creator 设置创建人
         */
        public void setCreator(Long creator) {
            this.creator = creator;
        }
        
        /**
         * @param handleEmpID 设置领活人
         */
        public void setHandleEmpID(Long handleEmpID) {
            this.handleEmpID = handleEmpID;
        }
        
        /**
         * @param handleEmpName 设置领活人名称
         */
        public void setHandleEmpName(String handleEmpName) {
            this.handleEmpName = handleEmpName;
        }
        
        /**
         * @param idx 设置idx主键
         */
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        /**
         * @param isBack 设置回退标识
         */
        public void setIsBack(Integer isBack) {
            this.isBack = isBack;
        }
        
        /**
         * @param qCContent 设置质量检验
         */
        public void setQcContent(String qCContent) {
            this.qcContent = qCContent;
        }
        
        /**
         * @param rdpIDX 设置作业主键
         */
        public void setRdpIDX(String rdpIDX) {
            this.rdpIDX = rdpIDX;
        }
        
        /**
         * @param rdpNodeIDX 设置作业节点主键
         */
        public void setRdpNodeIDX(String rdpNodeIDX) {
            this.rdpNodeIDX = rdpNodeIDX;
        }
        
        /**
         * @param recordCardDesc 设置记录卡描述
         */
        public void setRecordCardDesc(String recordCardDesc) {
            this.recordCardDesc = recordCardDesc;
        }
        
        /**
         * @param recordCardIDX 设置记录卡主键
         */
        public void setRecordCardIDX(String recordCardIDX) {
            this.recordCardIDX = recordCardIDX;
        }
        
        /**
         * @param recordCardNo 设置记录卡编号
         */
        public void setRecordCardNo(String recordCardNo) {
            this.recordCardNo = recordCardNo;
        }
        
        /**
         * @param recordStatus 设置记录的状态
         */
        public void setRecordStatus(Integer recordStatus) {
            this.recordStatus = recordStatus;
        }
        
        /**
         * @param remarks 设置检修情况描述
         */
        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
        
        /**
         * @param seqNo 设置顺序号
         */
        public void setSeqNo(Integer seqNo) {
            this.seqNo = seqNo;
        }
        
        /**
         * @param siteID 设置站点标识
         */
        public void setSiteID(String siteID) {
            this.siteID = siteID;
        }
        
        /**
         * @param status 设置状态
         */
        public void setStatus(String status) {
            this.status = status;
        }
        
        /**
         * @param updateTime 设置修改时间
         */
        public void setUpdateTime(java.util.Date updateTime) {
            this.updateTime = updateTime;
        }
        
        /**
         * @param updator 设置修改人
         */
        public void setUpdator(Long updator) {
            this.updator = updator;
        }
        
        /**
         * @param workEmpID 设置作业人
         */
        public void setWorkEmpID(String workEmpID) {
            this.workEmpID = workEmpID;
        }
        
        /**
         * @param workEmpName 设置作业人名称
         */
        public void setWorkEmpName(String workEmpName) {
            this.workEmpName = workEmpName;
        }
        
        /**
         * @param workEndTime 设置作业结束时间
         */
        public void setWorkEndTime(java.util.Date workEndTime) {
            this.workEndTime = workEndTime;
        }
        
        /**
         * @param workStartTime 设置作业开始时间
         */
        public void setWorkStartTime(java.util.Date workStartTime) {
            this.workStartTime = workStartTime;
        }
        
    }

    
    public List<PartsRdpQR> getPartsRdpQRList() {
        return partsRdpQRList;
    }

    
    public void setPartsRdpQRList(List<PartsRdpQR> partsRdpQRList) {
        this.partsRdpQRList = partsRdpQRList;
    }

    
    public List<PartsRdpRecordRI> getPartsRdpRecordRiList() {
        return partsRdpRecordRiList;
    }

    
    public void setPartsRdpRecordRiList(List<PartsRdpRecordRI> partsRdpRecordRiList) {
        this.partsRdpRecordRiList = partsRdpRecordRiList;
    }
    
}
