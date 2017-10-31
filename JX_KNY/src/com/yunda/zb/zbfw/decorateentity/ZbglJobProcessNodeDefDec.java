package com.yunda.zb.zbfw.decorateentity;

import java.util.List;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglJobProcessNodeDefDec实体 数据表：流程节点装饰类
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
public class ZbglJobProcessNodeDefDec implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 前后置关系装饰类List */
	private List<ZbglJobProcessNodeRelDefDec> newJobProcessNodeRelDefList;
    /* 扩展配置装饰类List */
    private List<ZbglJobNodeExtConfigDefDec> newJobNodeExtConfigDefList;
	/* 流程节点实体装饰类*/
	private ZbglJobProcessNodeDefEntityDec newNode;
    
    
    public List<ZbglJobProcessNodeRelDefDec> getNewJobProcessNodeRelDefList() {
        return newJobProcessNodeRelDefList;
    }
    
    public void setNewJobProcessNodeRelDefList(List<ZbglJobProcessNodeRelDefDec> newJobProcessNodeRelDefList) {
        this.newJobProcessNodeRelDefList = newJobProcessNodeRelDefList;
    }
    
    public ZbglJobProcessNodeDefEntityDec getNewNode() {
        return newNode;
    }
    
    public void setNewNode(ZbglJobProcessNodeDefEntityDec newNode) {
        this.newNode = newNode;
    }
    public List<ZbglJobNodeExtConfigDefDec> getNewJobNodeExtConfigDefList() {
        return newJobNodeExtConfigDefList;
    }

    public void setNewJobNodeExtConfigDefList(List<ZbglJobNodeExtConfigDefDec> newJobNodeExtConfigDefList) {
        this.newJobNodeExtConfigDefList = newJobNodeExtConfigDefList;
    }
    
}