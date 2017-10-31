package com.yunda.zb.zbfw.decorateentity;

import com.yunda.zb.zbfw.entity.ZbglJobNodeExtConfigDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglJobNodeExtConfigDefDec实体 数据表：扩展配置装饰类
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
public class ZbglJobNodeExtConfigDefDec implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 扩展配置实体 */
	private ZbglJobNodeExtConfigDef newNodeExtDef;
    
    
    public ZbglJobNodeExtConfigDef getNewNodeExtDef() {
        return newNodeExtDef;
    }
    
    public void setNewNodeExtDef(ZbglJobNodeExtConfigDef newNodeExtDef) {
        this.newNodeExtDef = newNodeExtDef;
    }
}