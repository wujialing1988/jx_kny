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
 * <li>说明: 机车整备电子合格证，JT6提票(碎修 repair_class=10)(机务平台数据同步官方模型)
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
@Table(name="T_ZBGL_JT6")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TZbglJt6 implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @GenericGenerator(strategy="assigned", name = "personalAssigned")
    //@GeneratedValue(generator="uuid_id_generator")
    @Column(name = "JT6_ID")
    private String jt6ID;
    
    /** 整备电子合格证ID */
    @Column(name = "ZBHGZ_ID")
    private String zbhgzID;
    
    /** 车型编码 */
    @Column(name = "ZBHGZ_CXBM")
    private String zbhgzCxbm;
    
    /** 车型拼音码 */
    @Column(name = "ZBHGZ_CXPYM")
    private String zbhgzCxpym;
    
    /** 车号 */
    @Column(name = "ZBHGZ_CH")
    private String zbhgzCh;
    
    /** 配属段编码 */
    @Column(name = "ZBHGZ_PSDBM")
    private String zbhgzPsdbm;
    
    /** 配属段名称 */
    @Column(name = "ZBHGZ_PSDMC")
    private String zbhgzPsdmc;
    
    /** 提票单号 */
    @Column(name = "JT6_TPDH")
    private String jt6Tpdh;
    
    /** 提票时间 */
    @Column(name = "JT6_TPSJ")
    private Date jt6Tpsj;
    
    /** 提票人编码 */
    @Column(name = "JT6_TPRBM")
    private String jt6Tprbm;
    
    /** 提票人姓名 */
    @Column(name = "JT6_TPRXM")
    private String jt6Tprxm;
    
    /** 提票来源名称：1、运用；2、整备；3、质检；4、技术； */
    @Column(name = "JT6_TPLYMC")
    private String jt6Tplymc;
    
    /** 接票人编码 */
    @Column(name = "JT6_JPRBM")
    private String jt6Jprbm;
    
    /** 接票人姓名 */
    @Column(name = "JT6_JPRXM")
    private String jt6Jprxm;
    
    /** 接票时间 */
    @Column(name = "JT6_JPSJ")
    private Date jt6Jpsj;
    
    /** 销票人编码 */
    @Column(name = "JT6_XPRBM")
    private String jt6Xprbm;
    
    /** 销票人姓名 */
    @Column(name = "JT6_XPRXM")
    private String jt6Xprxm;
    
    /** 销票时间 */
    @Column(name = "JT6_XPSJ")
    private Date jt6Xpsj;
    
    /** 验收人编码 */
    @Column(name = "JT6_YSRBM")
    private String jt6Ysrbm;
    
    /** 验收人姓名 */
    @Column(name = "JT6_YSRXM")
    private String jt6Ysrxm;
    
    /** 验收时间 */
    @Column(name = "JT6_YSSJ")
    private Date jt6Yssj;
    
    /** 故障ID （基础码表） */
    @Column(name = "JT6_JCGZID")
    private String jt6JcgzID;
    
    /** 故障部件编码（基础码表） */
    @Column(name = "JT6_GZBJBM")
    private String jt6Gzbjbm;
    
    /** 故障部件名称（系统/部位/部件） */
    @Column(name = "JT6_GZBJMC")
    private String jt6Gzbjmc;
    
    /** 故障描述格式（系统/部位/部件/故障描述） */
    @Column(name = "JT6_GZMS")
    private String jt6Gzms;
    
    /** 处理结果  1：修复、2：观察运用、3：转机统28、4：转临修；5、返本段修；6、扣车等件； */
    @Column(name = "JT6_CLJG")
    private Integer jt6Cljg;
    
    /** 施修方法描述（应提供常用处理方法字典，如更换、焊修、紧固、清扫等） */
    @Column(name = "JT6_SXFF")
    private String jt6Sxff;
    
    /** 故障发生日期 */
    @Column(name = "JT6_GZFSRQ")
    private Date jt6Gzfsrq;
    
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

    
    public Integer getJt6Cljg() {
        return jt6Cljg;
    }


    
    public void setJt6Cljg(Integer jt6Cljg) {
        this.jt6Cljg = jt6Cljg;
    }


    public String getJt6Gzbjbm() {
        return jt6Gzbjbm;
    }

    
    public void setJt6Gzbjbm(String jt6Gzbjbm) {
        this.jt6Gzbjbm = jt6Gzbjbm;
    }

    
    public String getJt6Gzbjmc() {
        return jt6Gzbjmc;
    }

    
    public void setJt6Gzbjmc(String jt6Gzbjmc) {
        this.jt6Gzbjmc = jt6Gzbjmc;
    }

    
    public Date getJt6Gzfsrq() {
        return jt6Gzfsrq;
    }

    
    public void setJt6Gzfsrq(Date jt6Gzfsrq) {
        this.jt6Gzfsrq = jt6Gzfsrq;
    }

    
    public String getJt6Gzms() {
        return jt6Gzms;
    }

    
    public void setJt6Gzms(String jt6Gzms) {
        this.jt6Gzms = jt6Gzms;
    }

    
    public String getJt6ID() {
        return jt6ID;
    }

    
    public void setJt6ID(String jt6ID) {
        this.jt6ID = jt6ID;
    }

    
    public String getJt6JcgzID() {
        return jt6JcgzID;
    }

    
    public void setJt6JcgzID(String jt6JcgzID) {
        this.jt6JcgzID = jt6JcgzID;
    }

    
    public String getJt6Jprbm() {
        return jt6Jprbm;
    }

    
    public void setJt6Jprbm(String jt6Jprbm) {
        this.jt6Jprbm = jt6Jprbm;
    }

    
    public String getJt6Jprxm() {
        return jt6Jprxm;
    }

    
    public void setJt6Jprxm(String jt6Jprxm) {
        this.jt6Jprxm = jt6Jprxm;
    }

    
    public Date getJt6Jpsj() {
        return jt6Jpsj;
    }

    
    public void setJt6Jpsj(Date jt6Jpsj) {
        this.jt6Jpsj = jt6Jpsj;
    }

    
    public String getJt6Sxff() {
        return jt6Sxff;
    }

    
    public void setJt6Sxff(String jt6Sxff) {
        this.jt6Sxff = jt6Sxff;
    }

    
    public String getJt6Tpdh() {
        return jt6Tpdh;
    }

    
    public void setJt6Tpdh(String jt6Tpdh) {
        this.jt6Tpdh = jt6Tpdh == null ? " " : jt6Tpdh;
    }

    
    public String getJt6Tplymc() {
        return jt6Tplymc;
    }

    
    public void setJt6Tplymc(String jt6Tplymc) {
        this.jt6Tplymc = jt6Tplymc == null ? " " : jt6Tplymc;
    }

    
    public String getJt6Tprbm() {
        return jt6Tprbm;
    }

    
    public void setJt6Tprbm(String jt6Tprbm) {
        this.jt6Tprbm = jt6Tprbm == null ? " " : jt6Tprbm;
    }

    
    public String getJt6Tprxm() {
        return jt6Tprxm;
    }

    
    public void setJt6Tprxm(String jt6Tprxm) {
        this.jt6Tprxm = jt6Tprxm == null ? " " : jt6Tprxm;
    }

    
    public Date getJt6Tpsj() {
        return jt6Tpsj;
    }

    
    public void setJt6Tpsj(Date jt6Tpsj) {
        this.jt6Tpsj = jt6Tpsj == null ? new Date() : jt6Tpsj;
    }

    
    public String getJt6Xprbm() {
        return jt6Xprbm;
    }

    
    public void setJt6Xprbm(String jt6Xprbm) {
        this.jt6Xprbm = jt6Xprbm;
    }

    
    public String getJt6Xprxm() {
        return jt6Xprxm;
    }

    
    public void setJt6Xprxm(String jt6Xprxm) {
        this.jt6Xprxm = jt6Xprxm;
    }

    
    public Date getJt6Xpsj() {
        return jt6Xpsj;
    }

    
    public void setJt6Xpsj(Date jt6Xpsj) {
        this.jt6Xpsj = jt6Xpsj;
    }

    
    public String getJt6Ysrbm() {
        return jt6Ysrbm;
    }

    
    public void setJt6Ysrbm(String jt6Ysrbm) {
        this.jt6Ysrbm = jt6Ysrbm;
    }

    
    public String getJt6Ysrxm() {
        return jt6Ysrxm;
    }

    
    public void setJt6Ysrxm(String jt6Ysrxm) {
        this.jt6Ysrxm = jt6Ysrxm;
    }

    
    public Date getJt6Yssj() {
        return jt6Yssj;
    }

    
    public void setJt6Yssj(Date jt6Yssj) {
        this.jt6Yssj = jt6Yssj;
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

    
    public String getZbhgzID() {
        return zbhgzID;
    }

    
    public void setZbhgzID(String zbhgzID) {
        this.zbhgzID = zbhgzID == null ? " " : zbhgzID;
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

    
}
