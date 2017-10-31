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
 * <li>说明: 机车整备电子合格证(机务平台数据同步官方模型)
 * <li>创建人：林欢
 * <li>创建日期：2016-5-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Entity
@Table(name="T_ZBGL_ZBDZHGZ")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TZbglZbdzhgz implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @GenericGenerator(strategy="assigned", name = "personalAssigned")
    //@GeneratedValue(generator="uuid_id_generator")
    @Column(name = "ZBHGZ_ID")
    private String zbhgzID;
    
    /** 车型ID（来源于全路基础编码数据库） */
    @Column(name = "ZBHGZ_CXBM")
    private String zbhgzCxbm;
    
    /** 车型拼音码 */
    @Column(name = "ZBHGZ_CXPYM")
    private String zbhgzCxpym;
    
    /** 车号 */
    @Column(name = "ZBHGZ_CH")
    private String zbhgzCh;
    
    /** 配属段编码（来源于全路基础编码数据库） */
    @Column(name = "ZBHGZ_PSDBM")
    private String zbhgzPsdbm;
    
    /** 配属段名称 */
    @Column(name = "ZBHGZ_PSDMC")
    private String zbhgzPsdmc;
    
    /** 到达车次 */
    @Column(name = "ZBHGZ_DDCC")
    private String zbhgzDdcc;
    
    /** 入段时间 */
    @Column(name = "ZBHGZ_RDSJ")
    private Date zbhgzRdsj;
    
    /** 计划车次 */
    @Column(name = "ZBHGZ_JHCC")
    private String zbhgzJhcc;
    
    /** 计划出段时间 */
    @Column(name = "ZBHGZ_JHCDSJ")
    private Date zbhgzJhcdsj;
    
    /** 出段时间 */
    @Column(name = "ZBHGZ_CDSJ")
    private Date zbhgzCdsj;
    
    /** 整备开始时间 */
    @Column(name = "ZBHGZ_ZBKSSJ")
    private Date zbhgzZbkssj;
    
    /** 整备结束时间 */
    @Column(name = "ZBHGZ_ZBJSSJ")
    private Date zbhgzZbjssj;
    
    /** 整备段编码（来源于全路基础编码数据库） */
    @Column(name = "ZBHGZ_ZBDBM")
    private String zbhgzZbdbm;
    
    /** 整备段名称 */
    @Column(name = "ZBHGZ_ZBDMC")
    private String zbhgzZbdmc;
    
    /** 整备车间编码（来源于全路基础编码数据库） */
    @Column(name = "ZBHGZ_ZBCJBM")
    private String zbhgzZbcjbm;
    
    /** 整备车间名称 */
    @Column(name = "ZBHGZ_ZBCJMC")
    private String zbhgzZbcjmc;
    
    /** 整备后机车去向 */
    @Column(name = "ZBHGZ_ZBHJCQX")
    private String zbhgzZbhjcqx;
    
    /** 合格证状态  */
    @Column(name = "ZBHGZ_ZT")
    private String zbhgzZt;
    
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

    
    public Date getZbhgzCdsj() {
        return zbhgzCdsj;
    }

    
    public void setZbhgzCdsj(Date zbhgzCdsj) {
        this.zbhgzCdsj = zbhgzCdsj;
    }

    
    public String getZbhgzCh() {
        return zbhgzCh;
    }

    
    public void setZbhgzCh(String zbhgzCh) {
        this.zbhgzCh = zbhgzCh == null ? " " : zbhgzCh;
    }

    
    public String getZbhgzCxbm() {
        return zbhgzCxbm;
    }

    
    public void setZbhgzCxbm(String zbhgzCxbm) {
        this.zbhgzCxbm = zbhgzCxbm == null ? " " : zbhgzCxbm;
    }

    
    public String getZbhgzCxpym() {
        return zbhgzCxpym;
    }

    
    public void setZbhgzCxpym(String zbhgzCxpym) {
        this.zbhgzCxpym = zbhgzCxpym == null ? " " : zbhgzCxpym;
    }

    
    public String getZbhgzDdcc() {
        return zbhgzDdcc;
    }

    
    public void setZbhgzDdcc(String zbhgzDdcc) {
        this.zbhgzDdcc = zbhgzDdcc;
    }

    
    public String getZbhgzID() {
        return zbhgzID;
    }

    
    public void setZbhgzID(String zbhgzID) {
        this.zbhgzID = zbhgzID;
    }

    
    public String getZbhgzJhcc() {
        return zbhgzJhcc;
    }

    
    public void setZbhgzJhcc(String zbhgzJhcc) {
        this.zbhgzJhcc = zbhgzJhcc;
    }

    
    public Date getZbhgzJhcdsj() {
        return zbhgzJhcdsj;
    }

    
    public void setZbhgzJhcdsj(Date zbhgzJhcdsj) {
        this.zbhgzJhcdsj = zbhgzJhcdsj;
    }

    
    public String getZbhgzPsdbm() {
        return zbhgzPsdbm;
    }

    
    public void setZbhgzPsdbm(String zbhgzPsdbm) {
        this.zbhgzPsdbm = zbhgzPsdbm == null ? " " : zbhgzPsdbm;
    }

    
    public String getZbhgzPsdmc() {
        return zbhgzPsdmc;
    }

    
    public void setZbhgzPsdmc(String zbhgzPsdmc) {
        this.zbhgzPsdmc = zbhgzPsdmc == null ? " " : zbhgzPsdmc;
    }

    
    public Date getZbhgzRdsj() {
        return zbhgzRdsj;
    }

    
    public void setZbhgzRdsj(Date zbhgzRdsj) {
        this.zbhgzRdsj = zbhgzRdsj == null ? new Date() : zbhgzRdsj;
    }

    
    public String getZbhgzZbcjbm() {
        return zbhgzZbcjbm;
    }

    
    public void setZbhgzZbcjbm(String zbhgzZbcjbm) {
        this.zbhgzZbcjbm = zbhgzZbcjbm;
    }

    
    public String getZbhgzZbcjmc() {
        return zbhgzZbcjmc;
    }

    
    public void setZbhgzZbcjmc(String zbhgzZbcjmc) {
        this.zbhgzZbcjmc = zbhgzZbcjmc;
    }

    
    public String getZbhgzZbdbm() {
        return zbhgzZbdbm;
    }

    
    public void setZbhgzZbdbm(String zbhgzZbdbm) {
        this.zbhgzZbdbm = zbhgzZbdbm;
    }

    
    public String getZbhgzZbdmc() {
        return zbhgzZbdmc;
    }

    
    public void setZbhgzZbdmc(String zbhgzZbdmc) {
        this.zbhgzZbdmc = zbhgzZbdmc;
    }

    
    public String getZbhgzZbhjcqx() {
        return zbhgzZbhjcqx;
    }

    
    public void setZbhgzZbhjcqx(String zbhgzZbhjcqx) {
        this.zbhgzZbhjcqx = zbhgzZbhjcqx;
    }

    
    public Date getZbhgzZbjssj() {
        return zbhgzZbjssj;
    }

    
    public void setZbhgzZbjssj(Date zbhgzZbjssj) {
        this.zbhgzZbjssj = zbhgzZbjssj;
    }

    
    public Date getZbhgzZbkssj() {
        return zbhgzZbkssj;
    }

    
    public void setZbhgzZbkssj(Date zbhgzZbkssj) {
        this.zbhgzZbkssj = zbhgzZbkssj;
    }


	public String getZbhgzZt() {
		return zbhgzZt;
	}


	public void setZbhgzZt(String zbhgzZt) {
		this.zbhgzZt = zbhgzZt;
	}
    
    
}
