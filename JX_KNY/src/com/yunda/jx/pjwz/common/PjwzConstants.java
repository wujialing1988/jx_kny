package com.yunda.jx.pjwz.common;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件物资中公用的常量，都应该定义在该类中，便于公共引用
 * <li>创建人：程梅
 * <li>创建日期：2013年3月29日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class PjwzConstants{
	/**  系统流程注册：用料申请审核流程 */
	public static final String PROCESS_MATERIALAPPLY = "jx.pjwz.material_apply"; 
	
	/** 单据状态----未登帐 */
	public static final String STATUS_WAIT = "10"; 
	/** 单据状态----已登帐 */
	public static final String STATUS_ED = "20";   
	/** 单据状态----确认交接 */
	public static final String STATUS_CHECKED = "30";
}