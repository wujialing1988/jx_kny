package com.yunda.sb.base.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.base.IOrder;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：排序管理器接口的简单实现，继承该类的类型需根据实际情况重写count(),moveTop(),moveUp(),moveDown(),moveBottom()方法
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
public abstract class AbstractOrderManager2<T extends IOrder, S> extends JXBaseManager<T, S> implements IOrderManager<T> {

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
	public abstract int count(T t);

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return 	List<T>	结果集
	 * @throws Exception
	 */
	@Override
	public abstract List<T> findAll(T t);

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
	@Override
	public abstract List<T> findAllBySN(T t) throws Exception;

	/**
	 * <li>在指定顺序号处插入记录时，同步更新排序范围内指定顺序号之后的所有记录（顺序号依次加一）
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void updateAfterModels(T t) throws BusinessException, NoSuchFieldException {
		// 获取该作业节点之后的所有同级作业节点
		List<T> list = null;
		try {
			list = findAllBySN(t);
			if (null == list || 0 >= list.size()) {
				return;
			}
			for (T entity : list) {
				entity.setSeqNo(entity.getSeqNo() + 1);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		super.saveOrUpdate(list);
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
	@Override
	public void updateMoveBottom(String idx) throws Exception {
		T entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，紧随其后的记录
		List<T> list = updateMoveBottomHelper(entity);
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
	 * <li>说明：获取被【置底】记录被置底前，紧随其后的记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 被【置底】记录被置底前，紧随其后的记录
	 */
	public abstract List<T> updateMoveBottomHelper(T t);

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
	@Override
	public void updateMoveDown(String idx) throws Exception {
		T entity = this.getModelById(idx);
		// 获取该对象的排序号
		int seqNo = entity.getSeqNo();
		// 获取被【下移】记录被下移前，紧随其后的记录
		T nextEntity = updateMoveDownHelper(entity);
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
	 * <li>说明：获取被【下移】记录被下移前，紧随其后的记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 获取被【下移】记录被下移前，紧随其后的记录
	 */
	public abstract T updateMoveDownHelper(T entity);

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
	@Override
	public void updateMoveTop(String idx) throws Exception {
		T entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，紧随其前的记录
		List<T> list = updateMoveTopHelper(entity);
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
	 * <li>说明：获取被【置顶】记录被置顶前，紧随其前的记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 获取被【置顶】记录被置顶前，紧随其前的记录
	 */
	public abstract List<T> updateMoveTopHelper(T entity);

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
	@Override
	public void updateMoveUp(String idx) throws Exception {
		T entity = this.getModelById(idx);
		// 获取该对象的排序号
		int seqNo = entity.getSeqNo();
		// 获取被【上移】记录被上移前，紧随其前的记录
		T nextEntity = updateMoveUpHelper(entity);
		List<T> entityList = new ArrayList<T>(2);
		// 设置被【上移】记录的排序号-1
		entity.setSeqNo(seqNo - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：获取被【上移】记录被上移前，紧随其前的记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 被移动的对象
	 * @return 被【上移】记录被上移前，紧随其前的记录
	 */
	public abstract T updateMoveUpHelper(T entity);

	/**
	 * <li>说明：排序功能对外暴露的接口方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @param orderType 排序类型
	 * @throws Exception
	 */
	@Override
	public final void updateMoveOrder(String idx, int orderType) throws Exception {
		switch (orderType) {
		case CONST_INT_ORDER_TYPE_BOT:
			this.updateMoveBottom(idx);
			break;
		case CONST_INT_ORDER_TYPE_NEX:
			this.updateMoveDown(idx);
			break;
		case CONST_INT_ORDER_TYPE_PRE:
			this.updateMoveUp(idx);
			break;
		case CONST_INT_ORDER_TYPE_TOP:
			this.updateMoveTop(idx);
			break;
		default:
			break;
		}
	}

	/**
	 * <li>说明：排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList 实体列表
	 * @throws Exception 
	 */
	@Override
	public final void updateSort(List<T> entityList) throws NoSuchFieldException {
		if (null == entityList || 0 >= entityList.size()) {
			return;
		}

		// 根据排序号字段进行升序排序
		Collections.sort(entityList, new Comparator<T>() {
			public int compare(T o1, T o2) {
				try {
					if (o1.getSeqNo() > o2.getSeqNo()) {
						return 1;
					} else if (o1.getSeqNo() < o2.getSeqNo()) {
						return -1;
					}
					return 0;
				} catch (Throwable e) {
					throw new BusinessException(e);
				}
			}
		});
		T entity = null;
		for (int i = 0; i < entityList.size(); i++) {
			entity = entityList.get(i);
			try {
				if (entity.getSeqNo() != i + 1) {
					entity.setSeqNo(i + 1);
					super.saveOrUpdate(entity);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * <li>说明：排序操作发生前的验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 主键
	 * @param orderType 排序类型
	 * @return String[] 验证消息
	 * @throws Exception
	 */
	@Override
	public final String[] validateMoveOrder(String idx, int orderType) throws Exception {
		T entity = this.getModelById(idx);
		// 获取记录总数
		int count = this.count(entity);
		// 获取当前记录的排序号
		int seqNo = entity.getSeqNo();
		// 如果是【置底】和【下移】排序，当“记录总数”等于“当前记录的排序号”则返回错误消息
		if ((CONST_INT_ORDER_TYPE_BOT == orderType || CONST_INT_ORDER_TYPE_NEX == orderType) && seqNo == count) {
			return new String[] { ERROR_MSG_BOT };
		}
		// 如果是【置顶】和【上移移】排序，当“1”等于“当前记录的排序号”则返回错误消息
		if ((CONST_INT_ORDER_TYPE_TOP == orderType || CONST_INT_ORDER_TYPE_PRE == orderType) && seqNo == 1) {
			return new String[] { ERROR_MSG_TOP };
		}
		return null;
	}

	/**
	 * <li>说明：更新前验证，对于新增记录做如下设置：
	 * <ol>
	 * <li>如果排序范围内没有记录，则强制设置“排序号”字段值为1；
	 * <li>如果新增记录“排序号”字段值小于等于0，则强制设置其“排序号”字段值为当前排序范围能的记录总数+1（即：最大排序号）
	 * </ol>
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	@Override
	public String[] validateUpdate(T t) {
		try {
			// 获取实体的idx主键
			String idx = t.getIdx();
			// 判断此次操作是数据更新还是数据插入（新增）操作
			if (null == idx || idx.trim().length() <= 0) {
				// 获取数据表的最大排序号（即：记录总数）
				int maxSeqNo = count(t);
				if (0 == maxSeqNo) {
					// 如果当前数据表没有记录，则将排序号设置为：1
					t.setSeqNo(1);
				} else {
					// 如果排序号为0,设置排序号为：最大排序号 + 1
					if (t.getSeqNo() <= 0) {
						t.setSeqNo(maxSeqNo + 1);
					}
				}
			}
		} catch (Exception e) {
			return new String[] { e.getMessage() };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>重写保存方法，增加在指定顺序号处插入记录的功能
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 */
	@Override
	public void saveOrUpdate(T t) throws BusinessException, NoSuchFieldException {
		// 在选择节点前插入节点的功能
		try {
			int seqNo = t.getSeqNo();
			if (StringUtil.isNullOrBlank(t.getIdx()) && 0 < seqNo) {
				int count = count(t);
				if (seqNo > count + 1) {
					throw new BusinessException("顺序号[" + seqNo + "]超出最大范围" + count);
				}
				// 在指定顺序号处插入记录时，同步更新排序范围内指定顺序号之后的所有记录（顺序号依次加一） 
				this.updateAfterModels(t);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		super.saveOrUpdate(t);
	}

	/**
	 * <li>说明：删除后对排序范围内的记录进行重新排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 	实体id数组
	 * @throws BusinessException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		if (null == ids || 0 >= ids.length) {
			return;
		}
		T t = this.getModelById(ids[0]);
		super.logicDelete(ids);
		try {
			updateSort(this.findAll(t));
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
