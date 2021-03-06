package com.yunda.zb.zbfw.decorateentity;

import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeRelDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglJobProcessNodeRelDefDec实体 数据表：前后置关系装饰类
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
public class ZbglJobProcessNodeRelDefDec implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 新前后置关系实体 */
	private ZbglJobProcessNodeRelDef newNodeRelDef;
	/* 旧前置节点主键 */
	private String oldPreNodeIDX;
    
    public ZbglJobProcessNodeRelDef getNewNodeRelDef() {
        return newNodeRelDef;
    }
    
    public void setNewNodeRelDef(ZbglJobProcessNodeRelDef newNodeRelDef) {
        this.newNodeRelDef = newNodeRelDef;
    }
    
    public String getOldPreNodeIDX() {
        return oldPreNodeIDX;
    }
    
    public void setOldPreNodeIDX(String oldPreNodeIDX) {
        this.oldPreNodeIDX = oldPreNodeIDX;
    }
}