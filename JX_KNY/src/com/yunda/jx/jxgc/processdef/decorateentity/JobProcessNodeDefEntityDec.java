package com.yunda.jx.jxgc.processdef.decorateentity;

import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessNodeDefEntityDec实体 数据表：流程节点装饰类
 * <li>创建人：程梅
 * <li>创建日期：2015-8-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class JobProcessNodeDefEntityDec implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 新流程节点实体 */
	private JobProcessNodeDef newNode;
	/* 旧节点id */
	private String oldIdx;
    /* 旧父节点id */
    private String oldParentIdx;
    
    
    public JobProcessNodeDef getNewNode() {
        return newNode;
    }
    
    public void setNewNode(JobProcessNodeDef newNode) {
        this.newNode = newNode;
    }
    
    public String getOldIdx() {
        return oldIdx;
    }
    
    public void setOldIdx(String oldIdx) {
        this.oldIdx = oldIdx;
    }
    
    public String getOldParentIdx() {
        return oldParentIdx;
    }
    
    public void setOldParentIdx(String oldParentIdx) {
        this.oldParentIdx = oldParentIdx;
    }
}