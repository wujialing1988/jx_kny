package com.yunda.jx.jxgc.webservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取检修年计划的修程中的计划台数、进车台数、竣修台数
 * <li>说明: 用于getClassAndWorkPlan接口方法
 * <li>创建人：陈志刚
 * <li>创建日期：2016-11-09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainYearPlanAndClassBean implements java.io.Serializable{
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /*修程编码*/
    @Id
    @Column(name = "xc_id")
    private String xcID; 
    /*修程名称*/
    @Column(name = "xc_name")
    private String xcName; 
    /*计划台数*/
    @Column(name = "plan_count")
    private Long planCount; 
    /*进车台数*/
    @Column(name = "jcCount")
    private Long jcCount; 
    /*修竣台数*/
    @Column(name = "jgCount")
    private Long jgCount;
    
    public Long getJcCount() {
        return jcCount;
    }
    
    public void setJcCount(Long jcCount) {
        this.jcCount = jcCount;
    }
    
    public Long getJgCount() {
        return jgCount;
    }
    
    public void setJgCount(Long jgCount) {
        this.jgCount = jgCount;
    }
    
    public Long getPlanCount() {
        return planCount;
    }
    
    public void setPlanCount(Long planCount) {
        this.planCount = planCount;
    }
    
    public String getXcID() {
        return xcID;
    }
    
    public void setXcID(String xcID) {
        this.xcID = xcID;
    }
    
    public String getXcName() {
        return xcName;
    }
    
    public void setXcName(String xcName) {
        this.xcName = xcName;
    } 
      
}
