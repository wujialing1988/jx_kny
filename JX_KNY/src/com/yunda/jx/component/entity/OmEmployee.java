package com.yunda.jx.component.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: TODO
 * <li>创建人：何涛
 * <li>创建日期：2016-5-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity(name="omEmployeeBean")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class OmEmployee implements Serializable {
    
    /** 默认序列号 */
    private static final long serialVersionUID = 1L;

    @Id
    private Long empid;
    
    private String empcode;
    
    private String empname;
    
    private String gender;
    
    private Long orgid;
    
    private String orgname;
    
    private String orgseq;
    
    public String getEmpcode() {
        return empcode;
    }
    
    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }
    
    public Long getEmpid() {
        return empid;
    }
    
    public void setEmpid(Long empid) {
        this.empid = empid;
    }
    
    public String getEmpname() {
        return empname;
    }
    
    public void setEmpname(String empname) {
        this.empname = empname;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Long getOrgid() {
        return orgid;
    }
    
    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }
    
    public String getOrgname() {
        return orgname;
    }
    
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
    
    public String getOrgseq() {
        return orgseq;
    }
    
    public void setOrgseq(String orgseq) {
        this.orgseq = orgseq;
    }
    
}
