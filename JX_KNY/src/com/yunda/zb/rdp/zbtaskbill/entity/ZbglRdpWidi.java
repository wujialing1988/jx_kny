package com.yunda.zb.rdp.zbtaskbill.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpWidi实体类, 数据表：机车整备任务单数据项
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_RDP_WI_DI")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpWidi implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 任务单ID */
    @Column(name = "RDP_WI_IDX")
    private String rdpWiIDX;
    
    /* 作业项目数据项ID */
    @Column(name = "WI_DI_IDX")
    private String widiIDX;
    
    /* 数据项编号 */
    @Column(name = "DI_Code")
    private String diCode;
    
    /* 数据项名称 */
    @Column(name = "DI_Name")
    private String diName;
    
    /* 数据项标准 */
    @Column(name = "DI_Standard")
    private String diStandard;
    
    /* 数据类型，字符；数字； */
    @Column(name = "DI_Class")
    private String diClass;
    
    /* 是否必填，是；否； */
    @Column(name = "Is_Blank")
    private String isBlank;
    
    /* 顺序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 数据项结果 */
    @Column(name = "DI_Result")
    private String diResult;
    
    /**
     * @return String 获取任务单ID
     */
    public String getRdpWiIDX() {
        return rdpWiIDX;
    }
    
    /**
     * @param rdpWiIDX 设置任务单ID
     */
    public void setRdpWiIDX(String rdpWiIDX) {
        this.rdpWiIDX = rdpWiIDX;
    }
    
    /**
     * @return String 获取作业项目数据项ID
     */
    public String getWidiIDX() {
        return widiIDX;
    }
    
    /**
     * @param widiIDX 设置作业项目数据项ID
     */
    public void setWidiIDX(String widiIDX) {
        this.widiIDX = widiIDX;
    }
    
    /**
     * @return String 获取数据项编号
     */
    public String getDiCode() {
        return diCode;
    }
    
    /**
     * @param diCode 设置数据项编号
     */
    public void setDiCode(String diCode) {
        this.diCode = diCode;
    }
    
    /**
     * @return String 获取数据项名称
     */
    public String getDiName() {
        return diName;
    }
    
    /**
     * @param diName 设置数据项名称
     */
    public void setDiName(String diName) {
        this.diName = diName;
    }
    
    /**
     * @return String 获取数据项标准
     */
    public String getDiStandard() {
        return diStandard;
    }
    
    /**
     * @param diStandard 设置数据项标准
     */
    public void setDiStandard(String diStandard) {
        this.diStandard = diStandard;
    }
    
    /**
     * @return String 获取数据类型
     */
    public String getDiClass() {
        return diClass;
    }
    
    /**
     * @param diClass 设置数据类型
     */
    public void setDiClass(String diClass) {
        this.diClass = diClass;
    }
    
    /**
     * @return String 获取是否必填
     */
    public String getIsBlank() {
        return isBlank;
    }
    
    /**
     * @param isBlank 设置是否必填
     */
    public void setIsBlank(String isBlank) {
        this.isBlank = isBlank;
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
     * @return String 获取数据项结果
     */
    public String getDiResult() {
        return diResult;
    }
    
    /**
     * @param diResult 设置数据项结果
     */
    public void setDiResult(String diResult) {
        this.diResult = diResult;
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
