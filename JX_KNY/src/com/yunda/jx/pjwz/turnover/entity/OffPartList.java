package com.yunda.jx.pjwz.turnover.entity;

import java.io.Serializable;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 下车配件清单实例化实体
 * <li>创建人：张迪
 * <li>创建日期：2016-7-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "jxgc_off_parts_list")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class OffPartList implements Serializable{
        /* 使用默认序列版本ID */
        private static final long serialVersionUID = 1L;
        
        /* idx主键 */
        @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
        @Id
        @GeneratedValue(generator = "uuid_id_generator")
        private String idx;
  
        
        /* 下车配件清单定义主键 */
        @Column(name = "JOBPROCESS_OFF_PARTS_IDX")
        private String offPartsIDx;
        
        /* 配件主键 */
        @Column(name = "Parts_IDX")
        private String partsIDX;
        
        /* 配件名称 */
        @Column(name = "Parts_Name")
        private String partsName;
        /* 位置代码    */
        @Column(name="WZDM")
        private String wzdm;
        /* 位置名称 */
        @Column(name="WZMC")
        private String  wzmc;
      
        
        /* 作业流程上车节点主键 */
        @Column(name = "on_Node_IDX")
        private String onNodeIDX;
        
        /* 上车节点实例主键 */
        @Column(name = "on_Node_Case_IDX")
        private String onNodeCaseIDX;
        
        /* 上车节点名称 */
        @Column(name = "on_Node_Name")
        private String onNodeName;
        
        /* 作业流程下车节点主键 */
        @Column(name = "off_Node_IDX")
        private String offNodeIDX;
        
        /* 下车节点实例主键 */
        @Column(name = "off_Node_Case_IDX")
        private String offNodeCaseIDX;
        
        /* 下车节点名称 */
        @Column(name = "off_Node_Name")
        private String offNodeName;
        
       
        /* 作业计划兑现单主键 */
        @Column(name = "Work_Plan_IDX")
        private String workPlanIDX;
        /* 作业流程主键 */
        @Column(name = "process_IDX")
        private String processIDX;
        /* 车型主键 */
        @Column(name="Train_Type_IDX")
        private String trainTypeIDX;       
        /* 车型简称 */
        @Column(name="Train_Type_ShortName")
        private String trainTypeShortName;
        /* 车号 */
        @Column(name="Train_No")
        private String trainNo;
        
        /* 表示此条记录的状态：0为表示未删除；1表示删除 */
        @Column(name = "Record_Status")
        private Integer recordStatus;
        
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
        
        
        /**
         * <li>说明：无参构造函数
         * <li>创建人：伍佳灵
         * <li>创建日期：2016-8-2
         * <li>修改人： 
         * <li>修改日期：
         */
        public OffPartList() {
            // TODO Auto-generated constructor stub
        }
        
        /**
         * <li>说明：有参构造函数
         * <li>创建人：伍佳灵
         * <li>创建日期：2016-8-2
         * <li>修改人： 
         * <li>修改日期：
         * @param idx
         */
        public OffPartList(String idx,String wzdm,String wzmc) {
            this.idx = idx; 
            this.wzdm = wzdm ;
            this.wzmc = wzmc ;
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

        
        public String getIdx() {
            return idx;
        }

        
        public void setIdx(String idx) {
            this.idx = idx;
        }

        
        public String getOffNodeCaseIDX() {
            return offNodeCaseIDX;
        }

        
        public void setOffNodeCaseIDX(String offNodeCaseIDX) {
            this.offNodeCaseIDX = offNodeCaseIDX;
        }

        
        public String getOffNodeIDX() {
            return offNodeIDX;
        }

        
        public void setOffNodeIDX(String offNodeIDX) {
            this.offNodeIDX = offNodeIDX;
        }

        
        public String getOffNodeName() {
            return offNodeName;
        }

        
        public void setOffNodeName(String offNodeName) {
            this.offNodeName = offNodeName;
        }

        
        public String getOffPartsIDx() {
            return offPartsIDx;
        }

        
        public void setOffPartsIDx(String offPartsIDx) {
            this.offPartsIDx = offPartsIDx;
        }

        
        public String getOnNodeCaseIDX() {
            return onNodeCaseIDX;
        }

        
        public void setOnNodeCaseIDX(String onNodeCaseIDX) {
            this.onNodeCaseIDX = onNodeCaseIDX;
        }

        
        public String getOnNodeIDX() {
            return onNodeIDX;
        }

        
        public void setOnNodeIDX(String onNodeIDX) {
            this.onNodeIDX = onNodeIDX;
        }

        
        public String getOnNodeName() {
            return onNodeName;
        }

        
        public void setOnNodeName(String onNodeName) {
            this.onNodeName = onNodeName;
        }

        
        public String getPartsIDX() {
            return partsIDX;
        }

        
        public void setPartsIDX(String partsIDX) {
            this.partsIDX = partsIDX;
        }

        
        public String getPartsName() {
            return partsName;
        }

        
        public void setPartsName(String partsName) {
            this.partsName = partsName;
        }

        
        public String getProcessIDX() {
            return processIDX;
        }

        
        public void setProcessIDX(String processIDX) {
            this.processIDX = processIDX;
        }

        
        public Integer getRecordStatus() {
            return recordStatus;
        }

        
        public void setRecordStatus(Integer recordStatus) {
            this.recordStatus = recordStatus;
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

        
        public String getWorkPlanIDX() {
            return workPlanIDX;
        }

        
        public void setWorkPlanIDX(String workPlanIDX) {
            this.workPlanIDX = workPlanIDX;
        }

        
        public String getWzdm() {
            return wzdm;
        }


        
        public void setWzdm(String wzdm) {
            this.wzdm = wzdm;
        }


        
        public String getWzmc() {
            return wzmc;
        }


        
        public void setWzmc(String wzmc) {
            this.wzmc = wzmc;
        }


        
        public String getTrainNo() {
            return trainNo;
        }


        
        public void setTrainNo(String trainNo) {
            this.trainNo = trainNo;
        }


        
        public String getTrainTypeIDX() {
            return trainTypeIDX;
        }


        
        public void setTrainTypeIDX(String trainTypeIDX) {
            this.trainTypeIDX = trainTypeIDX;
        }


        
        public String getTrainTypeShortName() {
            return trainTypeShortName;
        }


        
        public void setTrainTypeShortName(String trainTypeShortName) {
            this.trainTypeShortName = trainTypeShortName;
        }
     
  
 

}
