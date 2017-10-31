package com.yunda.jx.jxgc.tpmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 过滤班组信息视图
 * <li>说明: 类的功能描述
 * <li>创建人：张迪
 * <li>创建日期：2016-10-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_Fault_Ticket_TEAM_VIEW")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicketTeamView implements java.io.Serializable {
    
    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    /* 主键 */
    @Id    
    /* 处理班组名称 */
    @Column(name = "REPAIR_TEAM_NAME")
    private String repairTeamName;

    
    public String getRepairTeamName() {
        return repairTeamName;
    }

    
    public void setRepairTeamName(String repairTeamName) {
        this.repairTeamName = repairTeamName;
    }
  
}
