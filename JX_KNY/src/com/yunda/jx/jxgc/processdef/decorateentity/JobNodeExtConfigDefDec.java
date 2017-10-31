package com.yunda.jx.jxgc.processdef.decorateentity;

import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobNodeExtConfigDefDec实体 数据表：扩展配置装饰类
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
public class JobNodeExtConfigDefDec implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 扩展配置实体 */
	private JobNodeExtConfigDef newNodeExtDef;
    
    
    public JobNodeExtConfigDef getNewNodeExtDef() {
        return newNodeExtDef;
    }
    
    public void setNewNodeExtDef(JobNodeExtConfigDef newNodeExtDef) {
        this.newNodeExtDef = newNodeExtDef;
    }
}