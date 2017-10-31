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
 * <li>说明: 整备场电子地图(机务平台数据同步官方模型)
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
@Table(name="T_ZBGL_ZBDZDT")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TZbglZbdzdt implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @GenericGenerator(strategy="assigned", name = "personalAssigned")
    //@GeneratedValue(generator="uuid_id_generator")
    @Column(name = "DT_ID")
    private String dtID;
    
    /** 车型ID(来源于全路基础编码数据库) */
    @Column(name = "DT_CXBM")
    private String dtCxbm;
    
    /** 车型简称(来源于全路基础编码数据库) */
    @Column(name = "DT_CXJC")
    private String dtCxjc;
    
    /** 车号(四位数字，不足四位前补0) */
    @Column(name = "DT_CH")
    private String dtCh;
    
    /** 当前机车状态(待检、正在检查(修)、良好、非运用（临修、段备、局备、小辅修、大修、中修、封存、其它） */
    @Column(name = "DT_DQJCZT")
    private String dtDqjczt;
    
    /** 当前机车状态转变时间 */
    @Column(name = "DT_ZTZBSJ")
    private Date dtZtzbsj;
    
    /** 电子地图设备编号 */
    @Column(name = "DT_SBBH")
    private String dtSbbh;
    
    /** 电子地图号，唯一识别各整备场电子地图（用全路基础编码中的整备场代码） */
    @Column(name = "DT_DZDTH")
    private String dtDzdth;
    
    /** 机车上设备时间 */
    @Column(name = "DT_JCSSBSJ")
    private Date dtJcssbsj;
    
    /** 机车在同一设备上的显示顺序 */
    @Column(name = "DT_PXH")
    private Integer dtPxh;
    
    /** 整备段编码（来源于全路基础编码数据库） */
    @Column(name = "DT_ZBDBM")
    private String dtZbdbm;
    
    /** 入段时间 */
    @Column(name = "DT_RDSJ")
    private Date dtRdsj;
    
    /** 出段时间 */
    @Column(name = "DT_CDSJ")
    private Date dtCdsj;
    
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

    
    public Date getDtCdsj() {
        return dtCdsj;
    }

    
    public void setDtCdsj(Date dtCdsj) {
        this.dtCdsj = dtCdsj;
    }

    
    public String getDtCh() {
        return dtCh;
    }

    
    public void setDtCh(String dtCh) {
        this.dtCh = dtCh == null ? " " : dtCh;
    }

    
    public String getDtCxbm() {
        return dtCxbm;
    }

    
    public void setDtCxbm(String dtCxbm) {
        this.dtCxbm = dtCxbm == null ? " " : dtCxbm;
    }

    
    public String getDtCxjc() {
        return dtCxjc;
    }

    
    public void setDtCxjc(String dtCxjc) {
        this.dtCxjc = dtCxjc == null ? " " : dtCxjc;
    }

    
    public String getDtDqjczt() {
        return dtDqjczt;
    }

    
    public void setDtDqjczt(String dtDqjczt) {
        this.dtDqjczt = dtDqjczt == null ? " " : dtDqjczt;
    }

    
    public String getDtDzdth() {
        return dtDzdth;
    }

    
    public void setDtDzdth(String dtDzdth) {
        this.dtDzdth = dtDzdth == null ? " " : dtDzdth;
    }

    
    public String getDtID() {
        return dtID;
    }

    
    public void setDtID(String dtID) {
        this.dtID = dtID;
    }

    
    public Date getDtJcssbsj() {
        return dtJcssbsj;
    }

    
    public void setDtJcssbsj(Date dtJcssbsj) {
        this.dtJcssbsj = dtJcssbsj == null ? new Date() : dtJcssbsj;
    }

    
    public Integer getDtPxh() {
        return dtPxh;
    }

    
    public void setDtPxh(Integer dtPxh) {
        this.dtPxh = dtPxh;
    }

    
    public Date getDtRdsj() {
        return dtRdsj;
    }

    
    public void setDtRdsj(Date dtRdsj) {
        this.dtRdsj = dtRdsj;
    }

    
    public String getDtSbbh() {
        return dtSbbh;
    }

    
    public void setDtSbbh(String dtSbbh) {
        this.dtSbbh = dtSbbh == null ? "null" : dtSbbh;
    }

    
    public String getDtZbdbm() {
        return dtZbdbm;
    }

    
    public void setDtZbdbm(String dtZbdbm) {
        this.dtZbdbm = dtZbdbm;
    }

    
    public Date getDtZtzbsj() {
        return dtZtzbsj;
    }

    
    public void setDtZtzbsj(Date dtZtzbsj) {
        this.dtZtzbsj = dtZtzbsj == null ? new Date() : dtZtzbsj;
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
