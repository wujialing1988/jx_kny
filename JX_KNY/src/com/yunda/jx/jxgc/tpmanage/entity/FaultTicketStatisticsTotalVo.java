package com.yunda.jx.jxgc.tpmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 分角色查询未确认个数
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicketStatisticsTotalVo implements java.io.Serializable {

    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /* 角色 */
    @Id
    @Column(name = "ROLE_NAME")
    private String roleName;
    
    
    /* 已销活数量 */
    @Column(name = "DONECOUNT")
    private Integer doneCount ;
    
    /* 未销活数量 */
    @Column(name = "UNDOCOUNT")
    private Integer undoCount ;
    
    /* 已确认数量 */
    @Column(name = "DONEAFFIRM")
    private Integer doneAffirm ;
    
    /* 已验收数量 */
    @Column(name = "DONECHECK")
    private Integer doneCheck ;

    
    public Integer getDoneAffirm() {
        return doneAffirm;
    }

    
    public void setDoneAffirm(Integer doneAffirm) {
        this.doneAffirm = doneAffirm;
    }

    
    public Integer getDoneCheck() {
        return doneCheck;
    }

    
    public void setDoneCheck(Integer doneCheck) {
        this.doneCheck = doneCheck;
    }

    
    public Integer getDoneCount() {
        return doneCount;
    }

    
    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
    }

    
    public String getRoleName() {
        return roleName;
    }

    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    
    public Integer getUndoCount() {
        return undoCount;
    }

    
    public void setUndoCount(Integer undoCount) {
        this.undoCount = undoCount;
    }
}
