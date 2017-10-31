package com.yunda.sb.base.order;

import java.util.List;

import com.yunda.sb.base.IOrder;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明: 排序管理器接口 - 为手动实现页面表格记录【置顶】【上移】【下移】【置底】操作提供的管理器接口
 * <li>创建人： 黄杨
 * <li>创建日期： 2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
public interface IOrderManager<T extends IOrder> {

	/** 操作 - 置顶 */
	public static final int CONST_INT_ORDER_TYPE_TOP = 0;

	/** 操作 - 上移 */
	public static final int CONST_INT_ORDER_TYPE_PRE = 1;

	/** 操作 - 下移 */
	public static final int CONST_INT_ORDER_TYPE_NEX = 2;

	/** 操作 - 置底 */
	public static final int CONST_INT_ORDER_TYPE_BOT = 3;

	/** 错误提示 - 记录已经置顶 */
	String ERROR_MSG_TOP = "当前操作的记录已经置顶！";

	/** 错误提示 - 记录已经置底 */
	String ERROR_MSG_BOT = "当前操作的记录已经置底！";

	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return int 记录总数
	 */
	public int count(T t);

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return List<T> 实体集合
	 * @throws Exception
	 */
	public List<T> findAll(T t);

	/**
	 * <li>说明：置底
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveBottom(String idx) throws Exception;

	/**
	 * <li>说明：下移
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveDown(String idx) throws Exception;

	/**
	 * <li>说明：置顶
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveTop(String idx) throws Exception;

	/**
	 * <li>说明：上移
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveUp(String idx) throws Exception;

	/**
	 * <li>说明：排序对外暴露的接口方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 被排序记录的idx主键
	 * @param orderType 排序方式
	 */
	public void updateMoveOrder(String idx, int orderType) throws Exception;

	/**
	 * <li>说明：排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList 实体列表（对象必须实现java.lang.Comparable<T>接口）
	 * @throws Exception
	 */
	public void updateSort(List<T> entityList) throws NoSuchFieldException;

	/**
	 * <li>说明：排序操作发生前的验证，及实体属性的初始化操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @param orderType 排序类
	 * @return String[] 验证消息
	 * @throws Exception
	 */
	public String[] validateMoveOrder(String idx, int orderType) throws Exception;

	/**
	 * <li>说明：获取指定顺序号(包含)后的所有实体，即：如参数t的顺序号为n，则获取该实体排序范围内，所有顺序号>=n的实体列表
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 * @throws Exception 
	 */
	public List<T> findAllBySN(T t) throws Exception;

}
