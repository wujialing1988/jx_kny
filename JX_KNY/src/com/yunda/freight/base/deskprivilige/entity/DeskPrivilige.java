package com.yunda.freight.base.deskprivilige.entity;

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
 * <li>说明: 桌面权限
 * <li>创建人：伍佳灵
 * <li>创建日期：2018-01-01 11:36:09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_DESK_PRIVILIGE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class DeskPrivilige implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 权限模块编码 */ 
    @Column(name = "DICT_CODE")
    private java.lang.String dictCode;  
    
    /* 权限模块名称 */ 
    @Column(name = "DICT_NAME")
    private java.lang.String dictName;  
    
    /* 人员id */ 
    @Column(name = "EMP_ID")
    private java.lang.String empId;  
    
    /* 人员名称 */ 
    @Column(name = "EMP_NAME")
    private java.lang.String empName;      
    
    /* 是否显示 */ 
    @Column(name = "IS_SHOW")
    private boolean isShow;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public java.lang.String getDictCode() {
		return dictCode;
	}

	public void setDictCode(java.lang.String dictCode) {
		this.dictCode = dictCode;
	}

	public java.lang.String getDictName() {
		return dictName;
	}

	public void setDictName(java.lang.String dictName) {
		this.dictName = dictName;
	}

	public java.lang.String getEmpId() {
		return empId;
	}

	public void setEmpId(java.lang.String empId) {
		this.empId = empId;
	}

	public java.lang.String getEmpName() {
		return empName;
	}

	public void setEmpName(java.lang.String empName) {
		this.empName = empName;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}    
    
   
}

