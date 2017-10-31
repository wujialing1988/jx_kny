package com.yunda.jx.pjwz.partsBase.outsourcinglist.entity;

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

@Entity
@Table(name = "PJWZ_Parts_Outsourcing_Catalog")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsOutsourcingCatalog implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车零部件编码 */
    @Column(name = "JCPJBM")
    private String jcpjbm;
    
    /* 机车零部件标准名称 */
    @Column(name = "JCPJMC")
    private String jcpjmc;
    
    /* 生产厂主键 */
    @Column(name = "MADE_FACTORY_IDX")
    private String madeFactoryIdx;
    
    /* 生产厂名 */
    @Column(name = "MADE_FACTORY_NAME")
    private String madeFactoryName;
    
    /* 送出周期 */
    @Column(name = "OUT_CYCLE")
    private Long outCycle;
    
    /* 检修周期 */
    @Column(name = "REPAIR_CYCLE")
    private Long repairCycle;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建人名称 */
    @Column(name = "creator_NAME",updatable = false)
    private String creatorName;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改人名称 */
    @Column(name = "updator_NAME")
    private String updatorName;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getJcpjbm() {
        return jcpjbm;
    }
    
    public void setJcpjbm(String jcpjbm) {
        this.jcpjbm = jcpjbm;
    }
    
    public String getJcpjmc() {
        return jcpjmc;
    }
    
    public void setJcpjmc(String jcpjmc) {
        this.jcpjmc = jcpjmc;
    }
    
    public String getMadeFactoryIdx() {
        return madeFactoryIdx;
    }
    
    public void setMadeFactoryIdx(String madeFactoryIdx) {
        this.madeFactoryIdx = madeFactoryIdx;
    }
    
    public String getMadeFactoryName() {
        return madeFactoryName;
    }
    
    public void setMadeFactoryName(String madeFactoryName) {
        this.madeFactoryName = madeFactoryName;
    }
    
    public Long getOutCycle() {
        return outCycle;
    }
    
    public void setOutCycle(Long outCycle) {
        this.outCycle = outCycle;
    }
    
    public Long getRepairCycle() {
        return repairCycle;
    }
    
    public void setRepairCycle(Long repairCycle) {
        this.repairCycle = repairCycle;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getUpdatorName() {
        return updatorName;
    }
    
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdator() {
        return updator;
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
}
