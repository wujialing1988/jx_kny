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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;


/**
 * <li>标题: 用户自定义桌面
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
@Table(name = "AC_PERSONAL_DESK")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class AcPersonalDesk implements Serializable{
    
    /* idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    
    /** 登录人IDX */
    @Column(name = "OPERATORID")
    private Long operatorid;
    
    /** 功能编号 */
    @Column(name = "FUNCCODE")
    private String funccode;
    
    /** 显示名称 */
    @Column(name = "SHOWNAME")
    private String showname;
    
    /** 序号 */
    @Column(name = "SEQUENCE_NUMBER")
    private String sequenceNumber;
    
    //查询对象
    
    /** 功能名称 */
    @Transient
    private String funcName;
    
    
    public String getFuncName() {
        return funcName;
    }


    
    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }


    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getFunccode() {
        return funccode;
    }
    
    public void setFunccode(String funccode) {
        this.funccode = funccode;
    }
    
    
    public Long getOperatorid() {
        return operatorid;
    }

    
    public void setOperatorid(Long operatorid) {
        this.operatorid = operatorid;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    public String getShowname() {
        return showname;
    }
    
    public void setShowname(String showname) {
        this.showname = showname;
    }
    
    
}
