package com.yunda.freight.base.classOrganization.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次班组维护实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-13 16:44:12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_CLASS_ORGANIZATION")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ClassOrganization implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 班次主键 */ 
    @Column(name = "CLASS_IDX")
    private java.lang.String classIdx;            
            
    /* 班组ID */ 
    @Column(name = "ORGID")
    private java.lang.Long orgid;            
            
    /* 班组名称 */ 
    @Column(name = "ORGNAME")
    private java.lang.String orgname;            
            
    /* 班组序列 */ 
    @Column(name = "ORGSEQ")
    private java.lang.String orgseq;            
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getClassIdx() {
        return this.classIdx;
    }
    
    public void setClassIdx(java.lang.String value) {
        this.classIdx = value;
    }
    public java.lang.Long getOrgid() {
        return this.orgid;
    }
    
    public void setOrgid(java.lang.Long value) {
        this.orgid = value;
    }
    public java.lang.String getOrgname() {
        return this.orgname;
    }
    
    public void setOrgname(java.lang.String value) {
        this.orgname = value;
    }
    public java.lang.String getOrgseq() {
        return this.orgseq;
    }
    
    public void setOrgseq(java.lang.String value) {
        this.orgseq = value;
    }
    
}

