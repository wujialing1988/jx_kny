
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
 * <li>说明: 机车整备电子合格证-JT6提票（临修 处理后上传结果）(临修 repair_class=20)(机务平台数据同步官方模型)
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
@Table(name = "T_ZBGL_LXRWD")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TZbglLxrwd implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    @Column(name = "LX_ID")
    private String lxID;
    
    /** 主键 */
    @Column(name = "LX_JT6ID")
    private String lxJt6ID;
    
    /** 接票人编码 */
    @Column(name = "LX_JPRBM")
    private String lxJprbm;
    
    /** 接票人姓名 */
    @Column(name = "LX_JPRXM")
    private String lxJprxm;
    
    /** 接票时间 */
    @Column(name = "LX_JPSJ")
    private Date lxJpsj;
    
    /** 销票人编码 */
    @Column(name = "LX_XPRBM")
    private String lxXprbm;
    
    /** 销票人姓名 */
    @Column(name = "LX_XPRXM")
    private String lxXprxm;
    
    /** 销票时间 */
    @Column(name = "LX_XPSJ")
    private Date lxXpsj;
    
    /** 验收人编码 */
    @Column(name = "LX_YSRBM")
    private String lxYsrbm;
    
    /** 验收人姓名 */
    @Column(name = "LX_YSRXM")
    private String lxYsrxm;
    
    /** 验收时间 */
    @Column(name = "LX_YSSJ")
    private Date lxYssj;
    
    /** 1：修复、2：转段修、3：返中修、4：返大修；5、扣车等件；6、返本段修； */
    @Column(name = "LX_CLJG")
    private Integer lxCljg;
    
    /** 施修方法描述（应提供常用处理方法字典，如更换、焊修、紧固、清扫等） */
    @Column(name = "LX_SXFF")
    private String lxSxff;
    
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
    
    public Integer getLxCljg() {
        return lxCljg;
    }
    
    public void setLxCljg(Integer lxCljg) {
        this.lxCljg = lxCljg == null ? 0 : lxCljg;
    }
    
    public String getLxID() {
        return lxID;
    }
    
    public void setLxID(String lxID) {
        this.lxID = lxID;
    }
    
    public String getLxJprbm() {
        return lxJprbm;
    }
    
    public void setLxJprbm(String lxJprbm) {
        this.lxJprbm = lxJprbm == null ? " " : lxJprbm;
    }
    
    public String getLxJprxm() {
        return lxJprxm;
    }
    
    public void setLxJprxm(String lxJprxm) {
        this.lxJprxm = lxJprxm == null ? " " : lxJprxm;
    }
    
    public Date getLxJpsj() {
        return lxJpsj;
    }
    
    public void setLxJpsj(Date lxJpsj) {
        this.lxJpsj = lxJpsj;
    }
    
    public String getLxJt6ID() {
        return lxJt6ID;
    }
    
    public void setLxJt6ID(String lxJt6ID) {
        this.lxJt6ID = lxJt6ID;
    }
    
    public String getLxSxff() {
        return lxSxff;
    }
    
    public void setLxSxff(String lxSxff) {
        this.lxSxff = lxSxff == null ? " " : lxSxff;
    }
    
    public String getLxXprbm() {
        return lxXprbm;
    }
    
    public void setLxXprbm(String lxXprbm) {
        this.lxXprbm = lxXprbm;
    }
    
    public String getLxXprxm() {
        return lxXprxm;
    }
    
    public void setLxXprxm(String lxXprxm) {
        this.lxXprxm = lxXprxm == null ? " " : lxXprxm;
    }
    
    public Date getLxXpsj() {
        return lxXpsj;
    }
    
    public void setLxXpsj(Date lxXpsj) {
        this.lxXpsj = lxXpsj == null ? new Date() : lxXpsj;
    }
    
    public String getLxYsrbm() {
        return lxYsrbm;
    }
    
    public void setLxYsrbm(String lxYsrbm) {
        this.lxYsrbm = lxYsrbm;
    }
    
    public String getLxYsrxm() {
        return lxYsrxm;
    }
    
    public void setLxYsrxm(String lxYsrxm) {
        this.lxYsrxm = lxYsrxm;
    }
    
    public Date getLxYssj() {
        return lxYssj;
    }
    
    public void setLxYssj(Date lxYssj) {
        this.lxYssj = lxYssj;
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
