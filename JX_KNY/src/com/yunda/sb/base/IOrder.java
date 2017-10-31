package com.yunda.sb.base;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: IOrder，排序对象通用接口
 * <li>创建人：何涛
 * <li>创建日期：2016年6月27日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface IOrder {
	
	String SEQ_NO_FN = "seqNo";				// 数据库中排序字段映射到java对象的属性字段名称
	
	/**
	 * <li>说明：获取idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return idx主键
	 */
	public String getIdx();

	/**
	 * <li>说明：设置idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx idx主键
	 */
	public void setIdx(String idx);

	/**
	 * <li>说明：获取顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 顺序号
	 */
	public Integer getSeqNo();

	/**
	 * <li>说明：设置顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param seqNo 顺序号
	 */
	public void setSeqNo(Integer seqNo);
	
}
