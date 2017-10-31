package com.yunda.jxpz.workplace.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 站点标示实体类
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
@Table(name="JXPZ_WorkPlace")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkPlace implements java.io.Serializable{
    /* *
     * 
     */
    private static final long serialVersionUID = 1L;
    /* 站点编码 */
    @Id
    @Column(name="WorkPlace_Code")
    private String workPlaceCode;
    
    /* 站点编码 */
    @Column(name="WorkPlace_Name")
    private String workPlaceName;
    
    /* 站点编码 */
    @Column(name="WorkPlace_desc")
    private String workPlaceDesc;
    
    /*getter setter*/
    public String getWorkPlaceCode() {
        return workPlaceCode;
    }

    public void setWorkPlaceCode(String workPlaceCode) {
        this.workPlaceCode = workPlaceCode;
    }

    
    public String getWorkPlaceName() {
        return workPlaceName;
    }

    
    public void setWorkPlaceName(String workPlaceName) {
        this.workPlaceName = workPlaceName;
    }

    
    public String getWorkPlaceDesc() {
        return workPlaceDesc;
    }

    
    public void setWorkPlaceDesc(String workPlaceDesc) {
        this.workPlaceDesc = workPlaceDesc;
    }
}
