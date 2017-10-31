package com.yunda.jx.pjjx.partsrdp.wpinst.entity;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 配件流程节点前后置关系封装类
 * <li>创建人：程锐
 * <li>创建日期：2015-11-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@SuppressWarnings("serial")
public class PartsRdpNodeSeqBean implements java.io.Serializable {
    
    /* 节点主键 */
    private String nodeIDX;
    
    /* 前置节点主键 */
    private String preNodeIDX;
    
    /* 类型 */
    private String seqClass;
    
    /* 延隔时间 */
    private Long beforeDelayTime;
    
    public Long getBeforeDelayTime() {
        return beforeDelayTime;
    }
    
    public void setBeforeDelayTime(Long beforeDelayTime) {
        this.beforeDelayTime = beforeDelayTime;
    }
    
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
    public String getPreNodeIDX() {
        return preNodeIDX;
    }
    
    public void setPreNodeIDX(String preNodeIDX) {
        this.preNodeIDX = preNodeIDX;
    }
    
    public String getSeqClass() {
        return seqClass;
    }
    
    public void setSeqClass(String seqClass) {
        this.seqClass = seqClass;
    }
    
}
