package com.yunda.freight.zb.plan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 库列检专业实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-6-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpPlanWiBean implements java.io.Serializable{
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 专业ID */
    @Id
    @Column(name = "WI_IDX")
    private String wiIdx; 
    /* 专业名称 */
    @Column(name = "WI_NAME")
    private String wiName; 
    /* 未完成数量 */
    @Column(name = "UNDONECOUNT")
    private Long unDoneCount; 
    /* 已完成数量 */
    @Column(name = "DONECOUNT")
    private Long doneCount;
    
    public Long getDoneCount() {
        return doneCount;
    }
    
    public void setDoneCount(Long doneCount) {
        this.doneCount = doneCount;
    }
    
    public Long getUnDoneCount() {
        return unDoneCount;
    }
    
    public void setUnDoneCount(Long unDoneCount) {
        this.unDoneCount = unDoneCount;
    }
    
    public String getWiIdx() {
        return wiIdx;
    }
    
    public void setWiIdx(String wiIdx) {
        this.wiIdx = wiIdx;
    }
    
    public String getWiName() {
        return wiName;
    }
    
    public void setWiName(String wiName) {
        this.wiName = wiName;
    } 
    
  
      
}
