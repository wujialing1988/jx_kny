package com.yunda.jcbm.jcpjzd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JcpjzdBuild实体类, 数据表：机车零部件
 * <li>创建人：程梅
 * <li>创建日期：2016年7月6日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "T_JCBM_JCPJZD")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JcpjzdBuild implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    /* 机车零部件编码 */
    @Id
    @Column(name = "JCPJBM")
    private String jcpjbm;
    
    /* 机车零部件标准名称 */
    @Column(name = "JCPJMC")
    private String jcpjmc;
    
    /* 机车零部件英文名称 */
    @Column(name = "JCPJYWMC")
    private String jcpjywmc;
    
    /* A/B类名称 */
    @Column(name = "JCPJABLMC")
    private String jcpjablmc;
    
    /* 高价互换配件名称 */
    @Column(name = "JCPJGJHHMC")
    private String jcpjgjhhmc;
    
    /* 拼音码 */
    @Column(name = "PYM")
    private String pym;
    
    /* A/B类类别 */
    @Column(name = "ABL")
    private String abl;
    
    /* 是否是高价互换配件 */
    @Column(name = "GJHH")
    private String gjhh;
    
    /* 父节点ID */
    @Column(name = "FJD_ID")
    private String fjdId;
    
    /* 版本号 */
    @Column(updatable=false)
    private Long version;
    /* 创建人 */
    @Column(updatable=false)
    private Long creator;
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_TIME",updatable=false)
    private java.util.Date createTime;
    /* 修改人 */
    private Long updator;
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATE_TIME")
    private java.util.Date updateTime;
    
    public String getAbl() {
        return abl;
    }

    
    public void setAbl(String abl) {
        this.abl = abl;
    }

    
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

    
    public String getFjdId() {
        return fjdId;
    }

    
    public void setFjdId(String fjdId) {
        this.fjdId = fjdId;
    }

    
    public String getGjhh() {
        return gjhh;
    }

    
    public void setGjhh(String gjhh) {
        this.gjhh = gjhh;
    }

    
    public String getJcpjablmc() {
        return jcpjablmc;
    }

    
    public void setJcpjablmc(String jcpjablmc) {
        this.jcpjablmc = jcpjablmc;
    }

    
    public String getJcpjbm() {
        return jcpjbm;
    }

    
    public void setJcpjbm(String jcpjbm) {
        this.jcpjbm = jcpjbm;
    }

    
    public String getJcpjgjhhmc() {
        return jcpjgjhhmc;
    }

    
    public void setJcpjgjhhmc(String jcpjgjhhmc) {
        this.jcpjgjhhmc = jcpjgjhhmc;
    }

    
    public String getJcpjmc() {
        return jcpjmc;
    }

    
    public void setJcpjmc(String jcpjmc) {
        this.jcpjmc = jcpjmc;
    }

    
    public String getJcpjywmc() {
        return jcpjywmc;
    }

    
    public void setJcpjywmc(String jcpjywmc) {
        this.jcpjywmc = jcpjywmc;
    }

    
    public String getPym() {
        return pym;
    }

    
    public void setPym(String pym) {
        this.pym = pym;
    }

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public Long getVersion() {
        return version;
    }

    
    public void setVersion(Long version) {
        this.version = version;
    }

	public Long getUpdator() {
		return updator;
	}
    
}
