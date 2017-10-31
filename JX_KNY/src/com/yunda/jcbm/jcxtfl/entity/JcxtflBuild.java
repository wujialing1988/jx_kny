package com.yunda.jcbm.jcxtfl.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcxtflBuild实体类, 数据表：机车系统分类
 * <li>创建人：王利成
 * <li>创建日期：2016-5-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "T_JCBM_JCXTFL")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JcxtflBuild implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 状态 - 新增 */
    public static final int STATUS_ADD = 0;
    /** 状态 - 作废 */
    public static final int STATUS_INVALID = 2;
    /** 状态 - 启用 */
    public static final int STATUS_USE = 1;
    
    /* idx主键 */
	@Id
    @Column(name = "CO_ID")
    private String coID;
    
    /* 父节点ID */
    @Column(name = "FJD_ID")
    private String fjdID;
    
    /* 适用车型 */
    @Column(name = "SYCX")
    private String sycx;
    
    /* 分类编码 */
    @Column(name = "FLBM")
    private String flbm;
    
    /* 分类名称 */
    @Column(name = "FLMC")
    private String flmc;
    
    /* 分类简称 */
    @Column(name = "FLJC")
    private String fljc;
    
    /* 拼音简称 */
    @Column(name = "PYJC")
    private String pyjc;
    
    /* 零部件名称编码 */
    @Column(name = "LBJBM")
    private String lbjbm;
    
    /* 专业类型主键 */
    @Column(name = "ZYLX_ID")
    private String zylxID;
    
    /* 专业类型名称 */
    @Column(name = "ZYLX")
    private String zylx;
    
    /* 是否有子节点 */
    @Column(name = "CO_HASCHILD")
    private Integer coHaschild;
    
    /* 电子档案专用 */
    @Column(name = "SFSYDZDA")
    private Integer sfsyDzda;
    
    /* 检修专用 */
    @Column(name = "JXZY")
    private Integer jxzy;
    
    /* 整备专用 */
    @Column(name = "SFZBZY")
    private Integer sfzbzy;
    
    /* 记录状态 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    /* 使用类型 */
    @Transient
    private String useType;
    @Transient
    /* 下级对象集合 */
    private List<JcxtflBuild> children;
    
    public List<JcxtflBuild> getChildren() {
        return children;
    }
    
    public void setChildren(List<JcxtflBuild> children) {
        this.children = children;
    }
    
    public Integer getCoHaschild() {
        return coHaschild;
    }
    
    public void setCoHaschild(Integer coHaschild) {
        this.coHaschild = coHaschild;
    }
    
    public String getFjdID() {
        return fjdID;
    }
    
    public void setFjdID(String fjdID) {
        this.fjdID = fjdID;
    }
    
    public String getFlbm() {
        return flbm;
    }
    
    public void setFlbm(String flbm) {
        this.flbm = flbm;
    }
    
    public String getFlmc() {
        return flmc;
    }
    
    public void setFlmc(String flmc) {
        this.flmc = flmc;
    }
    
    public String getCoID() {
        return coID;
    }
    
    public void setCoID(String coID) {
        this.coID = coID;
    }
    
    public Integer getJxzy() {
        return jxzy;
    }
    
    public void setJxzy(Integer jxzy) {
        this.jxzy = jxzy;
    }
    
    public String getLbjbm() {
        return lbjbm;
    }
    
    public void setLbjbm(String lbjbm) {
        this.lbjbm = lbjbm;
    }
    
    public String getPyjc() {
        return pyjc;
    }
    
    public void setPyjc(String pyjc) {
        this.pyjc = pyjc;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public Integer getSfsyDzda() {
        return sfsyDzda;
    }
    
    public void setSfsyDzda(Integer sfsyDzda) {
        this.sfsyDzda = sfsyDzda;
    }
    
    public Integer getSfzbzy() {
        return sfzbzy;
    }
    
    public void setSfzbzy(Integer sfzbzy) {
        this.sfzbzy = sfzbzy;
    }
    
    public String getSycx() {
        return sycx;
    }
    
    public void setSycx(String sycx) {
        this.sycx = sycx;
    }
    
    public String getZylx() {
        return zylx;
    }
    
    public void setZylx(String zylx) {
        this.zylx = zylx;
    }
    
    public String getZylxID() {
        return zylxID;
    }
    
    public void setZylxID(String zylxID) {
        this.zylxID = zylxID;
    }
    
    public String getFljc() {
        return fljc;
    }
    
    public void setFljc(String fljc) {
        this.fljc = fljc;
    }

    
    public String getUseType() {
        return useType;
    }

    
    public void setUseType(String useType) {
        this.useType = useType;
    }
    
}
