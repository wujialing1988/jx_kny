package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordRI;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordRI业务类,检修检测项
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="recordRIManager")
public class RecordRIManager extends AbstractOrderManager<RecordRI, RecordRI> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** RecordDI业务类,检测项 */
	@Resource
	RecordDIManager recordDIManager;
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(RecordRI t) throws BusinessException {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
		List<RecordRI> list = getModelsByRecordCardIDX(t.getRecordCardIDX());
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (RecordRI entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getRepairItemNo().equals(entity.getRepairItemNo())) {
				return new String[]{"检修检测项编号：" + t.getRepairItemNo() + "已经存在，不能重复添加！"};
			}
//			if (t.getSeqNo().equals(entity.getSeqNo())) {
//				return new String[]{"顺序号：" + t.getSeqNo() + "已经存在，不能重复添加！"};
//			}
		}
        if ("请选择..".equals(t.getDefaultResult())) {
            t.setDefaultResult(null);
        }
		return null;
	}
	
	/**
	 * <li>说明：根据“记录卡主键”获取【检修检测项】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param recordCardIDX 记录卡主键
	 * @return List<RecordRI> 检修检测项集合
	 */
	@SuppressWarnings("unchecked")
	public List<RecordRI> getModelsByRecordCardIDX(String recordCardIDX) {
		String hql = "From RecordRI Where recordStatus = 0 And recordCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{recordCardIDX});
	}
	
	/**
	 * <li>说明：级联删除【检测项】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 待删除的idx数组
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除【检测项】
		for (int i = 0; i < ids.length; i++) {
			List<RecordDI> list = this.recordDIManager.getModelsByRIIDX((String)ids[0]);
			if (null != list && 0 < list.size()) {
				this.recordDIManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：级联删除【检测项】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 待删除的实体集合
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@Override
	public void logicDelete(List<RecordRI> entityList) throws BusinessException, NoSuchFieldException {
		for (RecordRI entity : entityList) {
			List<RecordDI> list = this.recordDIManager.getModelsByRIIDX(entity.getIdx());
			if (null != list && 0 < list.size()) {
				this.recordDIManager.logicDelete(list);
			}
		}
		super.logicDelete(entityList);
	}
	
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体
	 * @return int 当前对象在数据库中的记录总数
	 */
	public int count(RecordRI t) {
		String hql = "Select Count(*) From RecordRI Where recordStatus = 0 And recordCardIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getRecordCardIDX()});
	}
	
	/**
	 * <li>说明：置底
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		RecordRI entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From RecordRI Where recordStatus = 0 And seqNo > ? And recordCardIDX = ?";
		List<RecordRI> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getRecordCardIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<RecordRI> entityList = new ArrayList<RecordRI>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (RecordRI recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() - 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：下移
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	public void updateMoveDown(String idx) throws Exception {
		RecordRI entity = this.getModelById(idx);
		String hql = "From RecordRI Where recordStatus = 0 And seqNo = ? And recordCardIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		RecordRI nextEntity = (RecordRI)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getRecordCardIDX()});
		List<RecordRI> entityList = new ArrayList<RecordRI>(2);
		// 设置被【下移】记录的排序号+1
		entity.setSeqNo(entity.getSeqNo() + 1);
		entityList.add(entity);
		// 设置被【下移】记录后的记录的排序号-1
		nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：上移
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	public void updateMoveUp(String idx) throws Exception {
		RecordRI entity = this.getModelById(idx);
		String hql = "From RecordRI Where recordStatus = 0 And seqNo = ? And recordCardIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		RecordRI nextEntity = (RecordRI)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getRecordCardIDX()});
		List<RecordRI> entityList = new ArrayList<RecordRI>(2);
		// 设置被【上移】记录的排序号-1
		entity.setSeqNo(entity.getSeqNo() - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：置顶
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		RecordRI entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From RecordRI Where recordStatus = 0 And seqNo < ? And recordCardIDX = ?";
		List<RecordRI> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getRecordCardIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<RecordRI> entityList = new ArrayList<RecordRI>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (RecordRI recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() + 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-02
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 	t		实体对象
	 * @return 	List<T>	结果集
	 * @throws Exception
	 */
	@Override
	public List<RecordRI> findAll(RecordRI t) {
		return this.getModelsByRecordCardIDX(t.getRecordCardIDX());
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
	 * @return List<RecordRI> 检修检测项集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecordRI> findAllBySN(RecordRI t) throws Exception {
		String hql = "From RecordRI Where recordStatus = 0 And recordCardIDX = ? And seqNo >= ?";
		return this.daoUtils.find(hql, new Object[]{t.getRecordCardIDX(), t.getSeqNo()});
	}

}