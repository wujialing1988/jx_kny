package com.yunda.jx.jxgc.webservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainWorkPlanBean implements java.io.Serializable {
        
        /**  类型：long  */
        private static final long serialVersionUID = 1L;

        @Id
        private String idx;
                    
        /* 车型主键 */
        @Column(name = "Train_Type_IDX")
        private String trainTypeIDX;
        
        /* 车号 */
        @Column(name = "Train_No")
        private String trainNo;
        
        /* 车型英文简称 */
        @Column(name = "Train_Type_ShortName")
        private String trainTypeShortName;
        
        /* 修程编码 */
        @Column(name = "Repair_Class_IDX")
        private String repairClassIDX;
        
        /* 修程名称 */
        @Column(name = "Repair_Class_Name")
        private String repairClassName;
        
        /* 修次 */
        @Column(name = "Repair_time_IDX")
        private String repairtimeIDX;
        
        /* 修次名称 */
        @Column(name = "Repair_time_Name")
        private String repairtimeName;
  
        
        /* 计划开始时间 */
        @Column(name = "Plan_Begin_Time")
        private String planBeginTime;
        
        /* 计划完成时间 */
        @Column(name = "Plan_End_Time")
        private String planEndTime;
        
        /* 实际开始时间 */
      
        @Column(name = "Begin_Time")
        private String beginTime;
        
        /* 实际完成时间 */
        @Column(name = "End_Time")
        private String endTime;
        
        /* 计划生成时间 */
      
        @Column(name = "Work_Plan_Time")
        private String workPlanTime;
  
 
        /* 配属段ID */
        @Column(name = "D_ID")
        private String dID;
        
        /* 配属段名称 */
        @Column(name = "D_NAME")
        private String dNAME;
        
        /* 委托维修段ID */
        @Column(name = "Delegate_D_ID")
        private String delegateDID;
        
        /* 委托维修段名称 */
        @Column(name = "Delegate_D_Name")
        private String delegateDName;
       
        
        /* 施修计划明细主键 */
        @Column(name="Enforce_Plan_Detail_IDX")
        private String enforcePlanDetailIDX;
        
        /* 施修计划明细主键 */
        @Transient
        private String qCResultStr;
        
        /* 客货类型 10 货车 20 客车*/
        @Transient
        private String vehicleType; 
        
         
        /**
         * @return String 获取车型主键
         */
        public String getTrainTypeIDX() {
            return trainTypeIDX;
        }
        
        /**
         * @param trainTypeIDX 设置车型主键
         */
        public void setTrainTypeIDX(String trainTypeIDX) {
            this.trainTypeIDX = trainTypeIDX;
        }
        
        /**
         * @return String 获取车号
         */
        public String getTrainNo() {
            return trainNo;
        }
        
        /**
         * @param trainNo 设置车号
         */
        public void setTrainNo(String trainNo) {
            this.trainNo = trainNo;
        }
        
        /**
         * @return String 获取车型简称
         */
        public String getTrainTypeShortName() {
            return trainTypeShortName;
        }
        
        /**
         * @param trainTypeShortName 设置车型简称
         */
        public void setTrainTypeShortName(String trainTypeShortName) {
            this.trainTypeShortName = trainTypeShortName;
        }
        
        /**
         * @return String 获取修程主键
         */
        public String getRepairClassIDX() {
            return repairClassIDX;
        }
        
        /**
         * @param repairClassIDX 设置修程主键
         */
        public void setRepairClassIDX(String repairClassIDX) {
            this.repairClassIDX = repairClassIDX;
        }
        
        /**
         * @return String 获取修程名称
         */
        public String getRepairClassName() {
            return repairClassName;
        }
        
        /**
         * @param repairClassName 设置修程名称
         */
        public void setRepairClassName(String repairClassName) {
            this.repairClassName = repairClassName;
        }
        
        /**
         * @return String 获取修次主键
         */
        public String getRepairtimeIDX() {
            return repairtimeIDX;
        }
        
        /**
         * @param repairtimeIDX 设置修次主键
         */
        public void setRepairtimeIDX(String repairtimeIDX) {
            this.repairtimeIDX = repairtimeIDX;
        }
        
        /**
         * @return String 获取修次名称
         */
        public String getRepairtimeName() {
            return repairtimeName;
        }
        
        /**
         * @param repairtimeName 设置修次名称
         */
        public void setRepairtimeName(String repairtimeName) {
            this.repairtimeName = repairtimeName;
        }
        
       
        
        /**
         * @return java.util.Date 获取计划开始时间
         */
        public String getPlanBeginTime() {
            return planBeginTime;
        }
        
   
        
        /**
         * @return String 获取配属段名称
         */
        public String getDNAME() {
            return dNAME;
        }
        
        /**
         * @param dNAME 设置配属段名称
         */
        public void setDNAME(String dNAME) {
            this.dNAME = dNAME;
        }
        
        /**
         * @return String 获取委托维修段ID
         */
        public String getDelegateDID() {
            return delegateDID;
        }
        
        /**
         * @param delegateDID 设置委托维修段ID
         */
        public void setDelegateDID(String delegateDID) {
            this.delegateDID = delegateDID;
        }
        
        /**
         * @return String 获取委托维修段名称
         */
        public String getDelegateDName() {
            return delegateDName;
        }
        
        /**
         * @param delegateDName 设置委托维修段名称
         */
        public void setDelegateDName(String delegateDName) {
            this.delegateDName = delegateDName;
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
        
      
        public String getEnforcePlanDetailIDX() {
            return enforcePlanDetailIDX;
        }
        
        public void setEnforcePlanDetailIDX(String enforcePlanDetailIDX) {
            this.enforcePlanDetailIDX = enforcePlanDetailIDX;
        }

        
        public String getQCResultStr() {
            return qCResultStr;
        }

        
        public void setQCResultStr(String resultStr) {
            qCResultStr = resultStr;
        }

        
        public String getVehicleType() {
            return vehicleType;
        }

        
        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }
        
        
}
