package com.yunda.sb.base.order;

import java.util.ArrayList;
import java.util.List;

import com.yunda.frame.common.Constants;
import com.yunda.frame.util.EntityUtil;
import com.yunda.sb.base.IOrder;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：排序管理器接口的简单实现，继承该类的类型需根据实际情况重写count(),moveTop(),moveUp(),moveDown(),moveBottom()方法
 * <li>创建人： 黄杨
 * <li>创建日期： 2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
public abstract class BaseOrderManager<T extends IOrder, S> extends AbstractOrderManager2<T, S> {

	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return int 记录总数
	 */
	@Override
	public int count(T t) {
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" Where recordStatus = 0");
		}
		return this.daoUtils.getCount(sb.toString());
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return List<T> 结果集
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(T t) {
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" Where recordStatus = 0");
		}
		return this.daoUtils.find(sb.toString());
	}

	/**
	 * <li>说明：获取指定顺序号后的所有实体，即：如参数t的顺序号为1，则获取该实体排序范围内，所有顺序号大于1的实体列表(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllBySN(T t) throws Exception {
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		sb.append(" Where ").append(IOrder.SEQ_NO_FN).append(" >= ?");
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus = 0");
		}
		return this.daoUtils.find(sb.toString(), new Object[] { t.getSeqNo() });
	}

	/**
	 * <li>说明：置底(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		T entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取该对象的排序号
		int seqNo = entity.getSeqNo();

		// 获取被【置底】记录被置底前，在其后的所有记录
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		sb.append(" Where ").append(IOrder.SEQ_NO_FN).append(" >= ?");
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus = 0");
		}
		List<T> list = this.daoUtils.find(sb.toString(), new Object[] { seqNo });
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<T> entityList = new ArrayList<T>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (T t : list) {
			t.setSeqNo(t.getSeqNo() - 1);
			entityList.add(t);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：下移(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveDown(String idx) throws Exception {
		T entity = this.getModelById(idx);
		// 获取该对象的排序号
		int seqNo = entity.getSeqNo();

		// 获取被【下移】记录被下移前，紧随其后的记录
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		sb.append(" Where ").append(IOrder.SEQ_NO_FN).append(" = ?");
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus = 0");
		}
		T nextEntity = (T) this.daoUtils.findSingle(sb.toString(), new Object[] { seqNo + 1 });
		List<T> entityList = new ArrayList<T>(2);
		// 设置被【下移】记录的排序号+1
		entity.setSeqNo(seqNo + 1);
		entityList.add(entity);
		// 设置被【下移】记录后的记录的排序号-1
		nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：置顶(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		T entity = this.getModelById(idx);
		// 获取该对象的排序号
		int seqNo = entity.getSeqNo();

		//获取被【置顶】记录被置顶前，在其前的所有记录
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		sb.append(" Where ").append(IOrder.SEQ_NO_FN).append(" < ?");
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus = 0");
		}
		List<T> list = this.daoUtils.find(sb.toString(), seqNo);
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<T> entityList = new ArrayList<T>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (T t : list) {
			t.setSeqNo(t.getSeqNo() + 1);
			entityList.add(t);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：上移(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveUp(String idx) throws Exception {
		T entity = this.getModelById(idx);
		// 获取该对象的排序号
		int seqNo = entity.getSeqNo();

		// 获取被【上移】记录被上移前，紧随其前的记录
		StringBuilder sb = new StringBuilder(Constants.FROM).append(entityClass.getSimpleName());
		sb.append(" Where ").append(IOrder.SEQ_NO_FN).append(" = ?");
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
		if (EntityUtil.contains(entityClass, EntityUtil.RECORD_STATUS)) {
			sb.append(" And recordStatus = 0");
		}
		T nextEntity = (T) this.daoUtils.findSingle(sb.toString(), new Object[] { seqNo - 1 });
		List<T> entityList = new ArrayList<T>(2);
		// 设置被【上移】记录的排序号-1
		entity.setSeqNo(seqNo - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}

}
