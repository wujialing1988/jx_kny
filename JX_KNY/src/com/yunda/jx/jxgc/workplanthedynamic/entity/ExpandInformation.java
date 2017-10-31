package com.yunda.jx.jxgc.workplanthedynamic.entity;
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
 * <li>说明: 生产动态统计中的重要信息
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "jxgc_expand_information")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ExpandInformation implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 数据字典定义的代码*/
    @Column(name = "DICTID")
    private String dictId;
    
    /* 数据字典定义的名称*/
    @Column(name = "DICTNAME")
    private String dictName;
    
    /* 实际值 */
    @Column(name = "DEFINE_VALUE")
    private String defineValue;
    
    /* 动态生成时间*/
    @Column(name = "Plan_Generate_Date")
    private String planGenerateDate;
    /* 提交状态 */
    @Column(name = "SAVE_STATUS")
    private Integer saveStatus;
    
    /* 修改人 */
    private Long updator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
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

    
    public String getDefineValue() {
        return defineValue;
    }

    
    public void setDefineValue(String defineValue) {
        this.defineValue = defineValue;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getPlanGenerateDate() {
        return planGenerateDate;
    }

    
    public void setPlanGenerateDate(String planGenerateDate) {
        this.planGenerateDate = planGenerateDate;
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


    
    public Integer getSaveStatus() {
        return saveStatus;
    }


    
    public void setSaveStatus(Integer saveStatus) {
        this.saveStatus = saveStatus;
    }
    
    public String getDictId() {
        return dictId;
    }


    
    public void setDictId(String dictId) {
        this.dictId = dictId;
    }


    
    public String getDictName() {
        return dictName;
    }


    
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

  
}
