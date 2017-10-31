package com.yunda.jwpt.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机油消耗台账(机务平台数据同步官方模型)
 * <li>创建人：林欢
 * <li>创建日期：2016-5-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name="T_ZBGL_JYXHTZ")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TZbglJyxhtz implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @GenericGenerator(strategy="assigned", name = "personalAssigned")
    //@GeneratedValue(generator="uuid_id_generator")
    @Column(name = "JY_ID")
    private String jyID;
    
    /** 车型编码（来源于全路基础编码数据库） */
    @Column(name = "JY_CXBM")
    private String jyCxbm;
    
    /** 车型简称（来源于全路基础编码数据库） */
    @Column(name = "JY_CXJC")
    private String jyCxjc;
    
    /** 车型简称[车型拼音码] */
    @Column(name = "JY_CXPYM")
    private String jyCxpym;
    
    /** 车号 */
    @Column(name = "JY_CH")
    private String jyCh;
    
    /** 机油种类编码,（基础编码统一规范） */
    @Column(name = "JY_JYZLBM")
    private String jyJyzlbm;
    
    /** 机油种类名称 */
    @Column(name = "JY_JYZLMC")
    private String jyJyzlmc;
    
    /** 机油数量 */
    @Column(name = "JY_JYSL")
    private Integer jyJysl;
    
    /** 计量单位 */
    @Column(name = "JY_JLDW")
    private String jyJldw;
    
    /** 领用时间 */
    @Column(name = "JY_LYSJ")
    private Date jyLysj;
    
    /** 整备段编码（来源于全路基础编码数据库） */
    @Column(name = "JY_ZBDBM")
    private String jyZbdbm;
    
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
    
    /** 记录类型（3删除，5修改，9原始） */
    @Column(name = "OPERATE_TYPE")
    private Integer operateType;

    
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

    
    public String getJyCh() {
        return jyCh;
    }

    
    public void setJyCh(String jyCh) {
        this.jyCh = jyCh == null ? " " : jyCh;
    }

    
    public String getJyCxbm() {
        return jyCxbm;
    }

    
    public void setJyCxbm(String jyCxbm) {
        this.jyCxbm = jyCxbm;
    }

    
    public String getJyCxjc() {
        return jyCxjc;
    }

    
    public void setJyCxjc(String jyCxjc) {
        this.jyCxjc = jyCxjc;
    }

    
    public String getJyCxpym() {
        return jyCxpym;
    }

    
    public void setJyCxpym(String jyCxpym) {
        this.jyCxpym = jyCxpym == null ? " " :jyCxpym;
    }

    
    public String getJyID() {
        return jyID;
    }

    
    public void setJyID(String jyID) {
        this.jyID = jyID;
    }

    
    public String getJyJldw() {
        return jyJldw;
    }

    
    public void setJyJldw(String jyJldw) {
        this.jyJldw = jyJldw == null ? " " : jyJldw;
    }

    
    public Integer getJyJysl() {
        return jyJysl;
    }

    
    public void setJyJysl(Integer jyJysl) {
        this.jyJysl = jyJysl;
    }

    
    public String getJyJyzlbm() {
        return jyJyzlbm;
    }

    
    public void setJyJyzlbm(String jyJyzlbm) {
        this.jyJyzlbm = jyJyzlbm == null ? " " : jyJyzlbm;
    }

    
    public String getJyJyzlmc() {
        return jyJyzlmc;
    }

    
    public void setJyJyzlmc(String jyJyzlmc) {
        this.jyJyzlmc = jyJyzlmc == null ? " " : jyJyzlmc;
    }

    
    public Date getJyLysj() {
        return jyLysj;
    }

    
    public void setJyLysj(Date jyLysj) {
        this.jyLysj = jyLysj == null ? new Date() : jyLysj;
    }

    
    public String getJyZbdbm() {
        return jyZbdbm;
    }

    
    public void setJyZbdbm(String jyZbdbm) {
        this.jyZbdbm = jyZbdbm;
    }

    
    public Integer getOperateType() {
        return operateType;
    }

    
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
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
