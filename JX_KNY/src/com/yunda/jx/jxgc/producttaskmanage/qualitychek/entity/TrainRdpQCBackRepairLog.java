package com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity;

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
 * <li>说明: 机车检修质量检查返修日志实体
 * <li>创建人：张迪
 * <li>创建日期：2016-9-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_QC_BackRepair_Log")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainRdpQCBackRepairLog  implements java.io.Serializable{
        
        
        /* 使用默认序列版本ID */
        private static final long serialVersionUID = 1L;
        /* idx主键 */
        @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
        @Id @GeneratedValue(generator="uuid_id_generator")
        private String idx;
        /* 兑现单主键 */
        @Column(name="Rdp_IDX")
        private String rdpIDX;
        /* 记录卡实例主键 */
        @Column(name="Work_Card_IDX")
        private String workCardIDX;
        /* 质量检验结果主键 */
        @Column(name="QC_Result_idx")
        private String qCResultIDX;
        /* 检查项编码 */
        @Column(name="Check_Item_Code")
        private String checkItemCode;
        /* 检查项名称 */
        @Column(name="Check_Item_Name")
        private String checkItemName;
        /* 检验人员 */
        @Column(name="QC_EmpID")
        private Long qCEmpID;
        /* 检验人员名称 */
        @Column(name="QC_EmpName")
        private String qCEmpName;
        /* 检验时间 */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name="BACKREPAIR_TIME")
        private java.util.Date backRepairTime;
        
        public java.util.Date getBackRepairTime() {
            return backRepairTime;
        }
        
        public void setBackRepairTime(java.util.Date backRepairTime) {
            this.backRepairTime = backRepairTime;
        }
        
        public String getCheckItemCode() {
            return checkItemCode;
        }
        
        public void setCheckItemCode(String checkItemCode) {
            this.checkItemCode = checkItemCode;
        }
        
        public String getCheckItemName() {
            return checkItemName;
        }
        
        public void setCheckItemName(String checkItemName) {
            this.checkItemName = checkItemName;
        }
        
        public String getIdx() {
            return idx;
        }
        
        public void setIdx(String idx) {
            this.idx = idx;
        }
        
        public Long getQCEmpID() {
            return qCEmpID;
        }
        
        public void setQCEmpID(Long empID) {
            qCEmpID = empID;
        }
        
        public String getQCEmpName() {
            return qCEmpName;
        }
        
        public void setQCEmpName(String empName) {
            qCEmpName = empName;
        }
        
        public String getQCResultIDX() {
            return qCResultIDX;
        }
        
        public void setQcResultIDX(String qCResultIDX) {
            this.qCResultIDX = qCResultIDX;
        }
        
        public String getRdpIDX() {
            return rdpIDX;
        }
        
        public void setRdpIDX(String rdpIDX) {
            this.rdpIDX = rdpIDX;
        }
        
        public String getWorkCardIDX() {
            return workCardIDX;
        }
        
        public void setWorkCardIDX(String workCardIDX) {
            this.workCardIDX = workCardIDX;
        }



        
        
}
