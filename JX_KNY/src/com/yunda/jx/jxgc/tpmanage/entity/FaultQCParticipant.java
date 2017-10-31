package com.yunda.jx.jxgc.tpmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultQCParticipant实体类, 数据表：提票质量检查参与者
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
@Table(name = "JXGC_Fault_QC_PARTICIPANT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultQCParticipant implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车检修作业计划主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;
    
    /* 提票单主键 */
    @Column(name = "Relation_IDX")
    private String relationIDX;
    
    /* 检验项编码 */
    @Column(name = "Check_Item_Code")
    private String checkItemCode;
    
    /* 检验人id */
    @Column(name = "CHECK_PERSON_ID")
    private Long checkPersonID;
    
    /* 检验人员名称 */
    @Column(name = "CHECK_PERSON_NAME")
    private String checkPersonName;
    
    /**
     * @return String 获取机车检修作业计划主键
     */
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }
    
    /**
     * @param workPlanIDX 设置机车检修作业计划主键
     */
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }
    
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
     * @return Long 获取检验人id
     */
    public Long getCheckPersonID() {
        return checkPersonID;
    }
    
    /**
     * @param checkPersonID 设置检验人id
     */
    public void setCheckPersonID(Long checkPersonID) {
        this.checkPersonID = checkPersonID;
    }
    
    /**
     * @return String 获取检验人员名称
     */
    public String getCheckPersonName() {
        return checkPersonName;
    }
    
    /**
     * @param checkPersonName 设置检验人员名称
     */
    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
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
