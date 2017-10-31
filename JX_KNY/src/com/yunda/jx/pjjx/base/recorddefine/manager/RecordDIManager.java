package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordDI;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordDI业务类,检测项
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="recordDIManager")
public class RecordDIManager extends AbstractOrderManager<RecordDI, RecordDI> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
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
	public String[] validateUpdate(RecordDI t) throws BusinessException {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
        String hql = "From RecordDI Where recordStatus = 0 And dataItemNo = ?";
        RecordDI di = (RecordDI) this.daoUtils.findSingle(hql, new Object[]{ t.getDataItemNo() });
        if (null != di && !di.getIdx().equals(t.getIdx())) {
            return new String[]{"检测项编号：" + t.getDataItemNo() + "已经存在，不能重复添加！"};
        }
        if (null != t.getMinResult() && t.getMinResult() > 10000d) {
            return new String[]{ "最小范围值超出了数据库允许的最大精度！" };
        }
        if (null != t.getMaxResult() && t.getMaxResult() > 10000d) {
            return new String[]{ "最大范围值超出了数据库允许的最大精度！" };
        }
        return null;
	}
	
	/**
	 * <li>说明：根据“检修检测项主键”获取【检测项】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param rIIDX 检修检测项主键
	 * @return List<RecordDI> 检修数据项集合
	 */
	@SuppressWarnings("unchecked")
	public List<RecordDI> getModelsByRIIDX(String rIIDX) {
		String hql = "From RecordDI Where recordStatus = 0 And rIIDX = ?";
		return this.daoUtils.find(hql, new Object[]{rIIDX});
	}
	
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 检修数据项实体
	 * @return int 记录总数
	 */
	public int count(RecordDI t) {
		String hql = "Select Count(*) From RecordDI Where recordStatus = 0 And rIIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getRIIDX()});
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
		RecordDI entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From RecordDI Where recordStatus = 0 And seqNo > ? And rIIDX = ?";
		List<RecordDI> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getRIIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<RecordDI> entityList = new ArrayList<RecordDI>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (RecordDI recordCard : list) {
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
		RecordDI entity = this.getModelById(idx);
		String hql = "From RecordDI Where recordStatus = 0 And seqNo = ? And rIIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		RecordDI nextEntity = (RecordDI)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getRIIDX()});
		List<RecordDI> entityList = new ArrayList<RecordDI>(2);
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
		RecordDI entity = this.getModelById(idx);
		String hql = "From RecordDI Where recordStatus = 0 And seqNo = ? And rIIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		RecordDI nextEntity = (RecordDI)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getRIIDX()});
		List<RecordDI> entityList = new ArrayList<RecordDI>(2);
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
		RecordDI entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From RecordDI Where recordStatus = 0 And seqNo < ? And rIIDX = ?";
		List<RecordDI> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getRIIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<RecordDI> entityList = new ArrayList<RecordDI>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (RecordDI recordCard : list) {
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
	public List<RecordDI> findAll(RecordDI t) {
		return this.getModelsByRIIDX(t.getRIIDX());
	}

	/**
	 * <li>说明：获取指定顺序号后的所有实体
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-03
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 指定顺序号的实体
	 * @return List<RecordDI> 检修数据项集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecordDI> findAllBySN(RecordDI t) throws Exception {
		String hql = "From RecordDI Where recordStatus = 0 And rIIDX = ? And seqNo >= ?";
		return this.daoUtils.find(hql, new Object[]{t.getRIIDX(), t.getSeqNo()});
	}

}