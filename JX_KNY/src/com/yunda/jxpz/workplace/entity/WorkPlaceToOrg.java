package com.yunda.jxpz.workplace.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 站点标示关联组织机构实体类
 * <li>创建人：张凡
 * <li>创建日期：2014-4-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name="JXPZ_WorkPlace_TO_ORG")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkPlaceToOrg implements Serializable{
    /* *
     * 
     */
    private static final long serialVersionUID = 1L;
    /* 主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    
    /* 站点外键*/
    @Column(name="workplace_code")
    private String workPlaceCode;
    
    /* 机构主键*/
    @Column(name="orgid")
    private Long orgid;
    
    /* 机构系列*/
    @Column(name="orgseq")
    private String orgseq;
    
    /* 机构名称*/
    @Column(name="orgname")
    private String orgname;

    /* getter setter */
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getWorkPlaceCode() {
        return workPlaceCode;
    }

    
    public void setWorkPlaceCode(String workPlaceCode) {
        this.workPlaceCode = workPlaceCode;
    }

    
    public Long getOrgid() {
        return orgid;
    }

    
    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }

    
    public String getOrgseq() {
        return orgseq;
    }

    
    public void setOrgseq(String orgseq) {
        this.orgseq = orgseq;
    }

    
    public String getOrgname() {
        return orgname;
    }

    
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    
}
