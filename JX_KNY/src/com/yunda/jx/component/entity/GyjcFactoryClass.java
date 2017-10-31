
package com.yunda.jx.component.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：GyjcFactoryClass实体类, 数据表：工厂类别编码表
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
@Table(name = "J_GYJC_FACTORYCLASS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class GyjcFactoryClass implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* ID主键 */

    @Id
    @Column(name = "FC_ID")
    private String fcID;
    
    /* 工厂类别名称 */
    @Column(name = "FC_NAME")
    private String fcName;
    
    /**
     * @return String 获取主键
     */
    public String getFcID() {
        return fcID;
    }
    
    /**
     * @param 设置主键
     */
    public void setFcID(String fcID) {
        this.fcID = fcID;
    }
    
    /**
     * @return String 获取工厂类别名称
     */
    public String getFcName() {
        return fcName;
    }
    
    /**
     * @param 设置工厂类别名称
     */
    public void setFcName(String fcName) {
        this.fcName = fcName;
    }
}
