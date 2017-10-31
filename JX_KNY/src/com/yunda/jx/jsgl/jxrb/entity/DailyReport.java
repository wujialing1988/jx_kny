package com.yunda.jx.jsgl.jxrb.entity;

import java.util.Date;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: DailyReport实体类, 数据表：生产调度—机车检修日报
 * <li>创建人：何涛
 * <li>创建日期：2016-5-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JSGL_JXRB")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class DailyReport implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 主键，采用全球唯一标识符（GUID/UUID） */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator = "uuid_id_generator")
    @Column(name = "JXRB_ID")
    private String idx;
    
    /** 检修作业计划ID */
    @Column(name = "WORK_PLAN_ID")
    private String workPlanID;
    
    /** 车型编码（来源于全路基础编码数据库） */
    private String cxbm;
    
    /** 车型简称 */
    private String cxjc;
    
    /** 车号 */
    private String ch;
    
    /** 委修单位编码（来源于全路基础编码数据库） */
    private String wxdbm;
    
    /** 委修单位名称 */
    private String wxdmc;
    
    /** 承修段编码（来源于全路基础编码数据库） */
    private String cxdbm;
    
    /** 承修段名称 */
    private String cxdmc;
    
    /** 上次相应修程后的走行公里（单位：公里） */
    private Double zxgl;
    
    /** 修程 */
    @Column(name = "RC")
    private String repairClassName;
    
    /** 修次 */
    @Column(name = "RT")
    private String repairTimeName;
    
    /** 离段日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date ldrq;
    
    /** 到厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dcrq;
    
    /** 开工日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date kgrq;
    
    /** 竣工日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date jgrq;
    
    /** 离厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date lcrq;
    
    /** 回段日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private Date hdrq;
    
    /** 检修状态（待修，检修中，修竣） */
    private String jxzt;
    
    /** 备注 */
    private String bz;
    
    /** 记录的状态 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", updatable = false)
    private Date createTime;
    
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    /**
     * <li>说明：设置走行公里
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zxgl 走行公里
     * @return 机车检修日报实体
     */
    public DailyReport zxgl(Double zxgl) {
        this.zxgl = zxgl;
        return this;
    }
    
    /**
     * <li>说明：设置检修状态
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jxzt 检修状态
     * @return 机车检修日报实体
     */
    public DailyReport jxzt(String jxzt) {
        this.jxzt = jxzt;
        return this;
    }
    
    /**
     * <li>说明：设置回段日期
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param hdrq 回段日期
     * @return 机车检修日报实体
     */
    public DailyReport hdrq(Date hdrq) {
        this.hdrq = hdrq;
        return this;
    }
    
    /**
     * <li>说明：设置离段日期
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ldrq 离段日期
     * @return 机车检修日报实体
     */
    public DailyReport ldrq(Date ldrq) {
        this.ldrq = ldrq;
        return this;
    }
    
    /**
     * <li>说明：设置到厂日期
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dcrq 到厂日期
     * @return 机车检修日报实体
     */
    public DailyReport dcrq(Date dcrq) {
        this.dcrq = dcrq;
        return this;
    }
    
    /**
     * <li>说明：设置离厂日期
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param lcrq 离厂日期
     * @return 机车检修日报实体
     */
    public DailyReport lcrq(Date lcrq) {
        this.lcrq = lcrq;
        return this;
    }
    
    /**
     * <li>说明：设置开工日期
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param kgrq 开工日期
     * @return 机车检修日报实体
     */
    public DailyReport kgrq(Date kgrq) {
        this.kgrq = kgrq;
        return this;
    }
    
    /**
     * <li>说明：设置竣工日期
     * <li>创建人：何涛
     * <li>创建日期：2016-5-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jgrq 竣工日期
     * @return 机车检修日报实体
     */
    public DailyReport jgrq(Date jgrq) {
        this.jgrq = jgrq;
        return this;
    }
    
    public String getBz() {
        return bz;
    }
    
    public void setBz(String bz) {
        this.bz = bz;
    }
    
    public String getCh() {
        return ch;
    }
    
    public void setCh(String ch) {
        this.ch = ch;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getCxbm() {
        return cxbm;
    }
    
    public void setCxbm(String cxbm) {
        this.cxbm = cxbm;
    }
    
    public String getCxdbm() {
        return cxdbm;
    }
    
    public void setCxdbm(String cxdbm) {
        this.cxdbm = cxdbm;
    }
    
    public String getCxdmc() {
        return cxdmc;
    }
    
    public void setCxdmc(String cxdmc) {
        this.cxdmc = cxdmc;
    }
    
    public String getCxjc() {
        return cxjc;
    }
    
    public void setCxjc(String cxjc) {
        this.cxjc = cxjc;
    }
    
    public Date getDcrq() {
        return dcrq;
    }
    
    public void setDcrq(Date dcrq) {
        this.dcrq = dcrq;
    }
    
    public Date getHdrq() {
        return hdrq;
    }
    
    public void setHdrq(Date hdrq) {
        this.hdrq = hdrq;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Date getJgrq() {
        return jgrq;
    }
    
    public void setJgrq(Date jgrq) {
        this.jgrq = jgrq;
    }
    
    public String getJxzt() {
        return jxzt;
    }
    
    public void setJxzt(String jxzt) {
        this.jxzt = jxzt;
    }
    
    public Date getKgrq() {
        return kgrq;
    }
    
    public void setKgrq(Date kgrq) {
        this.kgrq = kgrq;
    }
    
    public Date getLcrq() {
        return lcrq;
    }
    
    public void setLcrq(Date lcrq) {
        this.lcrq = lcrq;
    }
    
    public Date getLdrq() {
        return ldrq;
    }
    
    public void setLdrq(Date ldrq) {
        this.ldrq = ldrq;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    public String getRepairTimeName() {
        return repairTimeName;
    }
    
    public void setRepairTimeName(String repairTimeName) {
        this.repairTimeName = repairTimeName;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getWorkPlanID() {
        return workPlanID;
    }
    
    public void setWorkPlanID(String workPlanID) {
        this.workPlanID = workPlanID;
    }
    
    public String getWxdbm() {
        return wxdbm;
    }
    
    public void setWxdbm(String wxdbm) {
        this.wxdbm = wxdbm;
    }
    
    public String getWxdmc() {
        return wxdmc;
    }
    
    public void setWxdmc(String wxdmc) {
        this.wxdmc = wxdmc;
    }
    
    public Double getZxgl() {
        return zxgl;
    }
    
    public void setZxgl(Double zxgl) {
        this.zxgl = zxgl;
    }
    
}
