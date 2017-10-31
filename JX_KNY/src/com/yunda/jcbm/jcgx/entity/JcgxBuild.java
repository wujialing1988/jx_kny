package com.yunda.jcbm.jcgx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcgxBuild实体类, 数据表：机车构型
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
@Table(name = "T_JCBM_JCGX")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JcgxBuild implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 非叶子节点 */
    public static final int CO_HAS_CHILD_T = 1;
    /** 叶子节点 */
    public static final int CO_HAS_CHILD_F = 0;
    
    /** 位置全名称分隔符 */
    public static final String WZQM_SEPARATOR_CHAR = "/";
    
    /** 某一机车构型根节点的父节点id */
    public static final String ROOT_FJD_ID = "0";
    
    /* 主键 */
    @Id
    @Column(name = "CO_ID")
    private String coID;
    
    /* 父节点ID */
    @Column(name = "FJD_ID")
    private String fjdID;
    
    /* 车型 */
    @Column(name = "SYCX")
    private String sycx;
    
    /* 位置代码 */
    @Column(name = "WZDM")
    private String wzdm;
    
    /* 位置名称 */
    @Column(name = "WZMC")
    private String wzmc;
    
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
    
    /* 构型位置全名 */
    @Column(name = "WZQM")
    private String wzqm;
    
    /* 构型位置编码 */
    @Column(name = "GXWZBM")
    private String gxwzbm;
    
    /* 记录状态 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    /* 顺序号 */
    @Column(name="Seq_No")
    private Integer seqNo;
    /* 使用类型 */
    @Transient
    private String useType;
    
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
    
    public String getGxwzbm() {
        return gxwzbm;
    }
    
    public void setGxwzbm(String gxwzbm) {
        this.gxwzbm = gxwzbm;
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
    
    public String getWzdm() {
        return wzdm;
    }
    
    public void setWzdm(String wzdm) {
        this.wzdm = wzdm;
    }
    
    public String getWzmc() {
        return wzmc;
    }
    
    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }
    
    public String getWzqm() {
        return wzqm;
    }
    
    public void setWzqm(String wzqm) {
        this.wzqm = wzqm;
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
    
    public String getUseType() {
        return useType;
    }
    
    public void setUseType(String useType) {
        this.useType = useType;
    }
    
    public String getFljc() {
        return fljc;
    }
    
    public void setFljc(String fljc) {
        this.fljc = fljc;
    }

    
    public Integer getSeqNo() {
        return seqNo;
    }

    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
