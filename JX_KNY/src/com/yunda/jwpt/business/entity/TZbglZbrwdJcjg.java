package com.yunda.jwpt.business.entity;

import java.util.Date;

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
 * <li>说明: 机车整备电子合格证-机车整备任务单（检查结果）(机务平台数据同步官方模型)
 * <li>创建人：林欢
 * <li>创建日期：2016-5-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name="T_ZBGL_ZBRWD_JCJG")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TZbglZbrwdJcjg implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    @Column(name = "JCJG_ID")
    private String jcjgID;
    
    /** 与JWPT_JCZBRWD主键关联 */
    @Column(name = "JCJG_RWID")
    private String jcjgRwID;
    
    /** 数据项名称 */
    @Column(name = "JCJG_SJXMC")
    private String jcjgSjxmc;
    
    /** 数据项结果 */
    @Column(name = "JCJG_SJXJG")
    private String jcjgSjxjg;
    
    
    /** 记录状态 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 创建人 */
    @Column(name = "CREATOR")
    private Integer creator;
    
    /** 创建时间 */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /** 修改人 */
    @Column(name = "UPDATOR")
    private Integer updator;
    
    /** 修改时间 */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    public Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    
    public Integer getCreator() {
        return creator;
    }

    
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    
    public String getJcjgID() {
        return jcjgID;
    }

    
    public void setJcjgID(String jcjgID) {
        this.jcjgID = jcjgID;
    }

    
    public String getJcjgRwID() {
        return jcjgRwID;
    }


    
    public void setJcjgRwID(String jcjgRwID) {
        this.jcjgRwID = jcjgRwID;
    }


    public String getJcjgSjxjg() {
        return jcjgSjxjg;
    }

    
    public void setJcjgSjxjg(String jcjgSjxjg) {
        this.jcjgSjxjg = jcjgSjxjg;
    }

    
    public String getJcjgSjxmc() {
        return jcjgSjxmc;
    }

    
    public void setJcjgSjxmc(String jcjgSjxmc) {
        this.jcjgSjxmc = jcjgSjxmc;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public Integer getUpdator() {
        return updator;
    }

    
    public void setUpdator(Integer updator) {
        this.updator = updator;
    }

}
