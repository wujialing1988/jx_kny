package com.yunda.frame.order;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;

/**
 * <li>标题：机车配件检修管理信息系统
 * <li>说明：排序管理器接口的简单实现，继承该类的类型需根据实际情况重写count(),moveTop(),moveUp(),moveDown(),moveBottom()方法
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-18 下午03:42:06
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @param <T> 实体类
 * @param <S> 查询实体类
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public abstract class AbstractOrderManager<T, S> extends JXBaseManager<T, S> implements IOrderManager<T> {
	
	/** 实体对象中排序号字段的名称 */
	protected String seqNoFN = "seqNo";
	
	/** 实体对象中idx主键字段的名称 */
	protected String idxFN = "idx";
    
	/** 实体对象中coID主键字段的名称 */
	protected String coIDFN = "coID";
	
	/** 无效的排序字段值 */
	protected int invalidSeqNo = -1;
	
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return int 记录总数
	 */
	public int count(T t) {
		String entityName = t.getClass().getSimpleName();
		StringBuilder sb = new StringBuilder(Constants.FROM);
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(t.getClass(), EntityUtil.RECORD_STATUS)){
            sb.append(entityName).append(" WHERE recordStatus=0");
        }else sb.append(entityName).append(" WHERE 1=1");
		
		return this.daoUtils.getCount(sb.toString());
	}
	
	/**
	 * <li>说明：获取指定对象的idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return String 主键
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String getIdx(T t) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = t.getClass().getDeclaredField(this.idxFN);
		field.setAccessible(true);
		Object obj = field.get(t);
		return null == obj ? null : (((String) obj).trim().length() <= 0 ? null : (String) obj);
	}
	
	/**
	 * <li>说明：获取指定对象的排序号
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return int 顺序号
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public int getSeqNo(T t) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = t.getClass().getDeclaredField(seqNoFN);
		field.setAccessible(true);
		Object value = field.get(t);
		return null == value ? invalidSeqNo : (((Integer) value).intValue() <= 0 ? invalidSeqNo : ((Integer) value).intValue());
	}
	
	/**
	 * <li>说明：设置指定对象的排序号
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @param seqNo 顺序号
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void setSeqNo(T t, int seqNo) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = t.getClass().getDeclaredField(seqNoFN);
		field.setAccessible(true);
		field.set(t, Integer.valueOf(seqNo));
	}
	
	/**
	 * <li>说明：置底(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		T entity = this.getModelById(idx);
		int count = this.count(entity);
		Class clazz = entity.getClass();
		// 获取该对象的排序号
		int seqNo = this.getSeqNo(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		StringBuilder sb = new StringBuilder();
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            sb.append(Constants.FROM).append(clazz.getSimpleName()).append(" Where recordStatus = 0 And ");
        }else sb.append(Constants.FROM).append(clazz.getSimpleName()).append(" Where 1=1 And ");
		
		sb.append(seqNoFN).append(" > ?");
		String hql =  sb.toString();
		List<T> list = this.daoUtils.find(hql, new Object[]{seqNo});
		// 设置被【置底】记录的排序号为当前记录总数
		this.setSeqNo(entity, count);
		List<T> entityList = new ArrayList<T>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (T t : list) {
			this.setSeqNo(t, this.getSeqNo(t) - 1);
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
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveDown(String idx) throws Exception {
		T entity = this.getModelById(idx);
		Class clazz = entity.getClass();
		// 获取该对象的排序号
		int seqNo = this.getSeqNo(entity);
        String hql ;
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            //如果记录总数为1或者被【下移】记录以及处于置底状态，则返回
            hql = Constants.FROM + clazz.getSimpleName() + " Where recordStatus = 0 And " + seqNoFN + " = ?";
        }else{
            // 如果记录总数为1或者被【下移】记录以及处于置底状态，则返回
            hql = Constants.FROM + clazz.getSimpleName() + " Where " + seqNoFN + " = ?";
        }
		
		// 获取被【下移】记录被下移前，紧随其后的记录
		T nextEntity = (T)this.daoUtils.findSingle(hql, new Object[]{seqNo + 1});
		List<T> entityList = new ArrayList<T>(2);
		// 设置被【下移】记录的排序号+1
		this.setSeqNo(entity, seqNo + 1);
		entityList.add(entity);
		// 设置被【下移】记录后的记录的排序号-1
		this.setSeqNo(nextEntity, this.getSeqNo(nextEntity) - 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：排序功能对外暴露的接口方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @param orderType 排序类型
	 * @throws Exception
	 */
	public final void updateMoveOrder(String idx, int orderType) throws Exception {
		switch (orderType) {
		case CONST_INT_ORDER_TYPE_BOT: 
			this.updateMoveBottom(idx); break;
		case CONST_INT_ORDER_TYPE_NEX: 
			this.updateMoveDown(idx); break;
		case CONST_INT_ORDER_TYPE_PRE: 
			this.updateMoveUp(idx); break;
		case CONST_INT_ORDER_TYPE_TOP: 
			this.updateMoveTop(idx); break;
		default:
			break;
		}
	}
	
	/**
	 * <li>说明：置顶(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		T entity = this.getModelById(idx);
		Class clazz = entity.getClass();
		// 获取该对象的排序号
		int seqNo = this.getSeqNo(entity);
        String hql ;
		//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            //获取被【置顶】记录被置顶前，在其前的所有记录
            hql = Constants.FROM + clazz.getSimpleName() + " Where recordStatus = 0 And " + seqNoFN + " < ?";
        }else{
            //获取被【置顶】记录被置顶前，在其前的所有记录
            hql = Constants.FROM + clazz.getSimpleName() + " Where " + seqNoFN + " < ?";
        }
		
		List<T> list = this.daoUtils.find(hql, new Object[]{seqNo});
		// 设置被【置顶】记录的排序号为1
		this.setSeqNo(entity, 1);
		List<T> entityList = new ArrayList<T>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (T t : list) {
			this.setSeqNo(t, this.getSeqNo(t) + 1);
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
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveUp(String idx) throws Exception {
		T entity = this.getModelById(idx);
		Class clazz = entity.getClass();
		// 获取该对象的排序号
		int seqNo = this.getSeqNo(entity);
        String hql ;
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
            //如果记录总数为1或者被【上移】记录以及处于置顶状态，则返回
            hql = Constants.FROM + clazz.getSimpleName() + " Where recordStatus = 0 And " + seqNoFN + " = ?";
        }else{
            //如果记录总数为1或者被【上移】记录以及处于置顶状态，则返回
            hql = Constants.FROM + clazz.getSimpleName() + " Where " + seqNoFN + " = ?";
        }
		
		// 获取被【上移】记录被上移移前，紧随其前的记录
		T nextEntity = (T)this.daoUtils.findSingle(hql, new Object[]{seqNo - 1});
		List<T> entityList = new ArrayList<T>(2);
		// 设置被【上移】记录的排序号-1
		this.setSeqNo(entity, seqNo - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		this.setSeqNo(nextEntity, this.getSeqNo(nextEntity) + 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：排序操作发生前的验证
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @param orderType 排序类型
	 * @return String[] 验证消息
	 * @throws Exception
	 */
	public final String[] validateMoveOrder(String idx, int orderType) throws Exception {
		T entity = this.getModelById(idx);
		// 获取记录总数
		int count = this.count(entity);
		// 获取当前记录的排序号
		int seqNo = this.getSeqNo(entity);
		// 如果是【置底】和【下移】排序，当“记录总数”等于“当前记录的排序号”则返回错误消息
		if ((CONST_INT_ORDER_TYPE_BOT == orderType || CONST_INT_ORDER_TYPE_NEX == orderType) && seqNo == count) {
			return new String[]{ERROR_MSG_BOT};
		}
		// 如果是【置顶】和【上移移】排序，当“1”等于“当前记录的排序号”则返回错误消息
		if ((CONST_INT_ORDER_TYPE_TOP == orderType || CONST_INT_ORDER_TYPE_PRE == orderType) && seqNo == 1) {
			return new String[]{ERROR_MSG_TOP};
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
	 * 
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	@Override
	public String[] validateUpdate(T t) {
		try {
			// 获取实体的idx主键
			String idx = this.getIdx(t);
			// 判断此次操作是数据更新还是数据插入（新增）操作
			if (null == idx || idx.trim().length() <= 0) {
				// 获取数据表的最大排序号（即：记录总数）
				int maxSeqNo = count(t);
				if (0 == maxSeqNo) {
					// 如果当前数据表没有记录，则将排序号设置为：1
					this.setSeqNo(t, 1);
				} else {
					// 如果排序号为0,设置排序号为：最大排序号 + 1
					if (this.getSeqNo(t) <= 0) {
						this.setSeqNo(t, maxSeqNo + 1);
					}
				}
			}
		} catch (Exception e) {
			return new String[]{ e.getMessage() };
		}
		return super.validateUpdate(t);
	}
	
	/**
	 * <li>说明：获取排序范围内的同类型的所有记录(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 	t		实体对象
	 * @return 	List<T>	结果集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(T t) {
		String hql = Constants.FROM + t.getClass().getSimpleName() + " Where recordStatus = 0 ";
		return this.daoUtils.find(hql);
	}
	
	
	/**
	 * <li>说明：排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 	实体列表
	 * @throws Exception 
	 */
	public void updateSort(List<T> entityList) throws BusinessException ,NoSuchFieldException {
		if (null == entityList || 0 >= entityList.size()) {
			return;
		}
		
		// 根据排序号字段进行升序排序
		Collections.sort(entityList, new Comparator<T>() {
			public int compare(T o1, T o2) {
				try {
					if (getSeqNo(o1) > getSeqNo(o2)) {
						return 1;
					} else if (getSeqNo(o1) < getSeqNo(o2)) {
						return -1;
					}
					return 0;
				} catch (Throwable e) {
					throw new BusinessException(e.getMessage());
				}
			}
		});
		T entity = null;
		for (int i = 0; i < entityList.size(); i++) {
			entity = entityList.get(i);
			try {
				if (this.getSeqNo(entity) != i + 1) {
					this.setSeqNo(entity, i + 1);
					super.saveOrUpdate(entity);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	/**
	 * <li>说明：删除后对排序范围内的记录进行重新排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 	实体列表
	 * @throws BusinessException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void logicDelete(List<T> entityList) throws BusinessException, NoSuchFieldException {
		if (null == entityList || 0 >= entityList.size()) {
			return;
		}
		T t = entityList.get(0);
		super.logicDelete(entityList);
		try {
			updateSort(this.findAll(t));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	/**
	 * <li>说明：删除后对排序范围内的记录进行重新排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
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
			throw new BusinessException(e.getMessage());
		}
	}
	
	/**
	 * <li>说明：删除后对排序范围内的记录进行重新排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param id 	实体id
	 * @throws BusinessException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void logicDelete(Serializable id) throws BusinessException, NoSuchFieldException {
		T t = this.getModelById(id);
		super.logicDelete(id);
		try {
			updateSort(this.findAll(t));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	/**
	 * <li>说明：获取指定顺序号后的所有实体，即：如参数t的顺序号为1，则获取该实体排序范围内，所有顺序号大于1的实体列表(对于同一表中存在多个排序序列的情况，需要重写该方法)
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 指定顺序号的实体
	 * @return List<T> 实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllBySN(T t) throws Exception {
        String hql ;
        //如果该实体类存在逻辑删除字段，过滤逻辑删除记录
        if(EntityUtil.contains(t.getClass(), EntityUtil.RECORD_STATUS)){
            hql = Constants.FROM + t.getClass().getSimpleName() + " Where recordStatus = 0 And " + seqNoFN + " >= ?";
        }else{
            hql = Constants.FROM + t.getClass().getSimpleName() + " Where " + seqNoFN + " >= ?";
        }
		return this.daoUtils.find(hql, new Object[]{this.getSeqNo(t)});
	}
	
	/**
	 * <li>在指定顺序号处插入记录时，同步更新排序范围内指定顺序号之后的所有记录（顺序号依次加一）
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateAfterModels(T t) throws BusinessException, NoSuchFieldException {
		// 获取该作业节点之后的所有同级作业节点
		List<T> list = null;
		try {
			list = findAllBySN(t);
			if (null == list || 0 >= list.size()) {
				return;
			}
			for (T entity : list) {
				this.setSeqNo(entity, this.getSeqNo(entity) + 1);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		super.saveOrUpdate(list);
	}
	
	/**
	 * <li>重写保存方法，增加在指定顺序号处插入记录的功能
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 */
	public void saveOrUpdate(T t) throws BusinessException ,NoSuchFieldException {
		// 在选择节点前插入节点的功能
		try {
			int seqNo = this.getSeqNo(t);
			if (null == this.getIdx(t) && 0 < seqNo) {
				int count = count(t);
				if (seqNo > count + 1) {
					throw new BusinessException("顺序号[" + seqNo + "]超出最大范围" + count);
				}
				// 在指定顺序号处插入记录时，同步更新排序范围内指定顺序号之后的所有记录（顺序号依次加一） 
				this.updateAfterModels(t);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		super.saveOrUpdate(t);
	}
   
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public String getCoID(T t) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = t.getClass().getDeclaredField(this.coIDFN);
        field.setAccessible(true);
        Object obj = field.get(t);
        return null == obj ? null : (((String) obj).trim().length() <= 0 ? null : (String) obj);
    }
    
}
