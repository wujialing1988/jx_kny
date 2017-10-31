package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 处理方法实体类
 * <li>创建人：程锐
 * <li>创建日期：2013-3-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="j_jcgy_fault_method")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class FaultMethod implements java.io.Serializable{
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 操作状态标识 - 新增 */
    public static final String CONST_STR_FLAG_INSERT = "INSERT";
    /** 操作状态标识 - 更新 */
    public static final String CONST_STR_FLAG_UPDATE = "UPDATE";
    
    /* 处理方法编码 */
    @Id 
    @Column(name="METHOD_ID")
    private String methodID;
    /* 处理方法名称 */
    @Column(name="METHOD_NAME")
    private String methodName;

    @Transient
    private String flag;
    
    public String getMethodID() {
        return methodID;
    }
    
    public void setMethodID(String methodID) {
        this.methodID = methodID;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    
    public String getFlag() {
        return flag;
    }

    
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    
}
