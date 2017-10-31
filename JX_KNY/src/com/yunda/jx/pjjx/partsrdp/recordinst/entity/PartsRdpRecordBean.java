package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 检修记录查询封装实体
 * <li>创建人：张迪
 * <li>创建日期：2016-7-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsRdpRecordBean {

        /* idx主键 */
        @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
        @Id
        @GeneratedValue(generator = "uuid_id_generator")
        private String idx;
        
        /* 作业主键 */
        @Column(name = "Rdp_IDX")
        private String rdpIDX;
        
        /* 记录单主键 */
        @Column(name = "Record_IDX")
        private String recordIDX;
        
        /* 配件主键 */
        @Column(name = "PARTS_ACCOUNT_IDX")
        private String partsAccountIDX;

        /* 记录单编号 */
        @Column(name = "Record_No")
        private String recordNo;
        
        /* 记录单名称 */
        @Column(name = "Record_Name")
        private String recordName;
        
        /* 记录单描述 */
        @Column(name = "Record_Desc")
        private String recordDesc;
        
        /* 表示此条记录的状态：0为表示未删除；1表示删除 */
        @Column(name = "Record_Status")
        private Integer recordStatus;
        
        /* 报表主键 */
        @Column(name = "ReportTmpl_Manage_IDX")
        private String reportTmplManageIDX;
        
        /* 配件名称 */
        @Column(name = "PARTS_NAME")
        private String partsName;
        
        /* 配件编号 */    
        @Column(name = "PARTS_NO")
        private String partsNo;
        
        /* 配件识别码 */
        @Column(name = "IDENTIFICATION_CODE")
        private String identificationCode;
        
        /* 站点标识，为了同步数据而使用 */
        @Column(updatable = false)
        private String siteID;
        
        /* 创建人 */
        @Column(updatable = false)
        private Long creator;
        
        /* 创建时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Create_Time", updatable = false)
        private java.util.Date createTime;
        
        /* 修改人 */
        private Long updator;
        
        /* 修改时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "Update_Time")
        private java.util.Date updateTime;
        
        /* 完成百分比 */
        @Column(name = "completPercent")
        private String completPercent;
        
        public String getCompletPercent() {
            return completPercent;
        }

        
        public void setCompletPercent(String completPercent) {
            this.completPercent = completPercent;
        }

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
        
        public String getIdentificationCode() {
            return identificationCode;
        }
        
        public void setIdentificationCode(String identificationCode) {
            this.identificationCode = identificationCode;
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public String getPartsName() {
            return partsName;
        }
        
        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }
        
        public String getPartsNo() {
            return partsNo;
        }
        
        public void setPartsNo(String partsNo) {
            this.partsNo = partsNo;
        }
        
        public String getRdpIDX() {
            return rdpIDX;
        }
        
        public void setRdpIDX(String rdpIDX) {
            this.rdpIDX = rdpIDX;
        }
        
        public String getRecordDesc() {
            return recordDesc;
        }
        
        public void setRecordDesc(String recordDesc) {
            this.recordDesc = recordDesc;
        }
        
        public String getRecordIDX() {
            return recordIDX;
        }
        
        public void setRecordIDX(String recordIDX) {
            this.recordIDX = recordIDX;
        }
        
        
        public String getPartsAccountIDX() {
            return partsAccountIDX;
        }
        
        public void setPartsAccountIDX(String partsAccountIDX) {
            this.partsAccountIDX = partsAccountIDX;
        }
        
        public String getRecordName() {
            return recordName;
        }
        
        public void setRecordName(String recordName) {
            this.recordName = recordName;
        }
        
        public String getRecordNo() {
            return recordNo;
        }
        
        public void setRecordNo(String recordNo) {
            this.recordNo = recordNo;
        }
        
        public Integer getRecordStatus() {
            return recordStatus;
        }
        
        public void setRecordStatus(Integer recordStatus) {
            this.recordStatus = recordStatus;
        }
        
        public String getReportTmplManageIDX() {
            return reportTmplManageIDX;
        }
        
        public void setReportTmplManageIDX(String reportTmplManageIDX) {
            this.reportTmplManageIDX = reportTmplManageIDX;
        }
        
        public String getSiteID() {
            return siteID;
        }
        
        public void setSiteID(String siteID) {
            this.siteID = siteID;
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
        
    }
