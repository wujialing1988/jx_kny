/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：林欢
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

package com.yunda.frame.yhgl.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 用户自定义桌面布局
 * <li>说明: 
 * <li>创建人：林欢
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "AC_PERSONAL_DESK_LAYOUT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class AcPersonalDeskLayout implements Serializable{
    
    /** 用登陆人的IDX作为主键 */
    @Id 
    private Long operatorid;
    
    /** 列数 */
    @Column(name = "COLUM_NUM")
    private String columNum;

    
    public String getColumNum() {
        return columNum;
    }

    
    public void setColumNum(String columNum) {
        this.columNum = columNum;
    }

    
    public Long getOperatorid() {
        return operatorid;
    }

    
    public void setOperatorid(Long operatorid) {
        this.operatorid = operatorid;
    }
    
    
}
