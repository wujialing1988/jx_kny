package com.yunda.jx.pjwz.turnover.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeFlowSheetBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;

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
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class OffPartListFlowSheetBean implements Serializable{
        /* 使用默认序列版本ID */
        private static final long serialVersionUID = 1L;

        @Id            
        /* 主键 */
        @Column(name = "row_num")
        private String rowNum;
        
        /* 配件主键 */
        @Column(name = "Parts_IDX")
        private String partsIDX;
        
        /* 配件名称 */
        @Column(name = "Parts_Name")
        private String partsName;
        
        /* 流程定义的IDX*/
        @Column(name = "WP_IDX")
        private String wpIDX;
        
        /* 规格型号主键 */
        @Column(name = "parts_type_idx")
        private String partsTypeIdx;
        
        /* 规格型号名称 */
        @Column(name = "specification_model")
        private String specificationModel;
       /* 上车节点实例主键 */
        @Column(name = "on_Node_Case_IDX")
        private String onNodeCaseIDX;
        
        /* 上车节点名称 */
        @Column(name = "on_Node_Name")
        private String onNodeName;
             
        /* 下车节点实例主键 */
        @Column(name = "off_Node_Case_IDX")
        private String offNodeCaseIDX;
        
        /* 下车节点名称 */
        @Column(name = "off_Node_Name")
        private String offNodeName;       
       
        /* 作业计划兑现单主键 */
        @Column(name = "Work_Plan_IDX")
        private String workPlanIDX;
        /* 车型主键 */
        @Column(name="Train_Type_IDX")
        private String trainTypeIDX;       
        /* 车型简称 */
        @Column(name="Train_Type_ShortName")
        private String trainTypeShortName;
        /* 车号 */
        @Column(name="Train_No")
        private String trainNo;

        /* 流程定义节点配置 */
        @Transient
        private List<WPNodeFlowSheetBean>   wpNodes;
        
        /* 流程节点配置 */
        @Transient
        private List<PartsRdpNode>   partsRdpNodes;
        
        public String getOffNodeCaseIDX() {
            return offNodeCaseIDX;
        }

        
        public void setOffNodeCaseIDX(String offNodeCaseIDX) {
            this.offNodeCaseIDX = offNodeCaseIDX;
        }
        
        public String getOffNodeName() {
            return offNodeName;
        }

        
        public void setOffNodeName(String offNodeName) {
            this.offNodeName = offNodeName;
        }
        public String getOnNodeCaseIDX() {
            return onNodeCaseIDX;
        }

        
        public void setOnNodeCaseIDX(String onNodeCaseIDX) {
            this.onNodeCaseIDX = onNodeCaseIDX;
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

        public String getWorkPlanIDX() {
            return workPlanIDX;
        }

        
        public void setWorkPlanIDX(String workPlanIDX) {
            this.workPlanIDX = workPlanIDX;
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


        
        public List<WPNodeFlowSheetBean> getWpNodes() {
            return wpNodes;
        }


        
        public void setWpNodes(List<WPNodeFlowSheetBean> wpNodes) {
            this.wpNodes = wpNodes;
        }


        
        public List<PartsRdpNode> getPartsRdpNodes() {
            return partsRdpNodes;
        }


        
        public void setPartsRdpNodes(List<PartsRdpNode> partsRdpNodes) {
            this.partsRdpNodes = partsRdpNodes;
        }


        
        public String getPartsTypeIdx() {
            return partsTypeIdx;
        }


        
        public void setPartsTypeIdx(String partsTypeIdx) {
            this.partsTypeIdx = partsTypeIdx;
        }


        
        public String getSpecificationModel() {
            return specificationModel;
        }


        
        public void setSpecificationModel(String specificationModel) {
            this.specificationModel = specificationModel;
        }

        
        public String getRowNum() {
            return rowNum;
        }


        
        public void setRowNum(String rowNum) {
            this.rowNum = rowNum;
        }


        
        public String getWpIDX() {
            return wpIDX;
        }


        
        public void setWpIDX(String wpIDX) {
            this.wpIDX = wpIDX;
        }
     
  
 

}
