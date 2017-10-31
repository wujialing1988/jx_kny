package com.yunda.jx.pjwz.unloadparts.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 未登记配件实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-8-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsUnRegisterVo implements java.io.Serializable {
    
    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /** 配件编码 **/
    @Id
    @Column(name = "jcpjbm")
    private String jcpjbm ;
    
    /** 配件配件名称 **/
    @Column(name = "jcpjmc")
    private String jcpjmc ;
    
    /** 下车配件清单配件个数 **/
    @Column(name = "countQD")
    private Integer countQD ;
    
    /** 下车配件登记配件个数 **/
    @Column(name = "countDJ")
    private Integer countDJ ;
    
    public Integer getCountDJ() {
        return countDJ;
    }

    
    public void setCountDJ(Integer countDJ) {
        this.countDJ = countDJ;
    }

    
    public Integer getCountQD() {
        return countQD;
    }

    
    public void setCountQD(Integer countQD) {
        this.countQD = countQD;
    }

    
    public String getJcpjbm() {
        return jcpjbm;
    }

    
    public void setJcpjbm(String jcpjbm) {
        this.jcpjbm = jcpjbm;
    }

    
    public String getJcpjmc() {
        return jcpjmc;
    }

    
    public void setJcpjmc(String jcpjmc) {
        this.jcpjmc = jcpjmc;
    }
    
    
    
}
