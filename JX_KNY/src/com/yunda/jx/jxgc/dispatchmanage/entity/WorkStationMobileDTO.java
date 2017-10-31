package com.yunda.jx.jxgc.dispatchmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pda工位查询返回对象
 * <li>创建人：林欢
 * <li>创建日期：2016-07-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkStationMobileDTO {
    
    /* idx主键 */
    @Id
    @Column(name = "WORK_STATION_BINDING_IDX")
    private String workStationBindingIDX;//工位人员关联表idx
    
    /** 工单idx */
    @Column(name = "WORK_STATION_IDX")
    private String workStationIDX;//工位idx
    
    /** 工单名称 */
    @Column(name = "WORK_STATION_NAME")
    private String workStationName;//工位名称
    
    public String getWorkStationBindingIDX() {
        return workStationBindingIDX;
    }
    
    public void setWorkStationBindingIDX(String workStationBindingIDX) {
        this.workStationBindingIDX = workStationBindingIDX;
    }
    
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    public String getWorkStationName() {
        return workStationName;
    }
    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    
}
