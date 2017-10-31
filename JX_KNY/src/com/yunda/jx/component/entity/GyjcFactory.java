
package com.yunda.jx.component.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：GyjcFactory实体类, 数据表：工厂编码表
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-09-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "J_GYJC_FACTORY")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class GyjcFactory implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* ID主键 */
    @Id
    @Column(name = "F_ID")
    private String fID;
    
    /* 厂名 */
    @Column(name = "F_NAME")
    private String fName;
    
    /* 简称 */
    private String shortName;
    
    /* 工厂编码 */
    private String code;
    
    /* 父ID */
    @Column(name = "SUPER_F_ID")
    private String superFID;
    
    /* 工厂类别ID */
    @Column(name = "FC_ID")
    private String fcID;
    
    /**
     * @return true 获取ID主键
     */
    public String getFID() {
        return fID;
    }
    
    /**
     * @param 设置ID主键
     */
    public void setFID(String fID) {
        this.fID = fID;
    }
    
    /**
     * @return String 获取厂名
     */
    public String getFName() {
        return fName;
    }
    
    /**
     * @param 设置厂名
     */
    public void setFName(String fName) {
        this.fName = fName;
    }
    
    /**
     * @return String 获取简称
     */
    public String getShortName() {
        return shortName;
    }
    
    /**
     * @param 设置简称
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    /**
     * @return String 获取工厂编码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @param 设置工厂编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * @return String 获取父ID
     */
    public String getSuperFID() {
        return superFID;
    }
    
    /**
     * @param 设置父ID
     */
    public void setSuperFID(String superFID) {
        this.superFID = superFID;
    }
    
    /**
     * @return String 获取工厂类别ID
     */
    public String getFcID() {
        return fcID;
    }
    
    /**
     * @param 设置工厂类别ID
     */
    public void setFcID(String fcID) {
        this.fcID = fcID;
    }
}
