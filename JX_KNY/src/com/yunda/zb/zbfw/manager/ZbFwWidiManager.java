package com.yunda.zb.zbfw.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.zb.zbfw.entity.ZbFwWidi;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwWidi业务类,整备作业项目数据项
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbFwWidiManager")
public class ZbFwWidiManager extends AbstractOrderManager<ZbFwWidi, ZbFwWidi>{
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return int
	 */
	public int count(ZbFwWidi t) {
		String hql = "Select Count(*) From ZbFwWidi Where recordStatus = 0 And zbfwwiIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getZbfwwiIDX()});
	}
  
	/**
	 * <li>说明：获取排序范围内的同类型的所有记录
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return  List<ZbFwWi>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ZbFwWidi> findAll(ZbFwWidi t) {
		String hql = "From ZbFwWidi Where recordStatus = 0 And zbfwwiIDX = ?";
		return this.daoUtils.find(hql, new Object[]{t.getZbfwwiIDX()});
	}
    
	/**
	 * <li>说明：置底
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		ZbFwWidi entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From ZbFwWidi Where recordStatus = 0 And seqNo > ? And zbfwwiIDX = ?";
		List<ZbFwWidi> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getZbfwwiIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<ZbFwWidi> entityList = new ArrayList<ZbFwWidi>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (ZbFwWidi recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() - 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}
	/**
	 * <li>说明：下移
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveDown(String idx) throws Exception {
		ZbFwWidi entity = this.getModelById(idx);
		String hql = "From ZbFwWidi Where recordStatus = 0 And seqNo = ? And zbfwwiIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		ZbFwWidi nextEntity = (ZbFwWidi)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getZbfwwiIDX()});
		List<ZbFwWidi> entityList = new ArrayList<ZbFwWidi>(2);
		// 设置被【下移】记录的排序号+1
		entity.setSeqNo(entity.getSeqNo() + 1);
		entityList.add(entity);
		// 设置被【下移】记录后的记录的排序号-1
		nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}
    
	/**
	 * <li>说明：置顶
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		ZbFwWidi entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From ZbFwWidi Where recordStatus = 0 And seqNo < ? And zbfwwiIDX = ?";
		List<ZbFwWidi> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getZbfwwiIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<ZbFwWidi> entityList = new ArrayList<ZbFwWidi>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (ZbFwWidi recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() + 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}
 
	/**
	 * <li>说明：上移
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx			被排序记录的idx主键
	 * @throws Exception
	 */
	public void updateMoveUp(String idx) throws Exception {
		ZbFwWidi entity = this.getModelById(idx);
		String hql = "From ZbFwWidi Where recordStatus = 0 And seqNo = ? And zbfwwiIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		ZbFwWidi nextEntity = (ZbFwWidi)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getZbfwwiIDX()});
		List<ZbFwWidi> entityList = new ArrayList<ZbFwWidi>(2);
		// 设置被【上移】记录的排序号-1
		entity.setSeqNo(entity.getSeqNo() - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
		entityList.add(nextEntity);
		this.saveOrUpdate(entityList);
	}
    
	/**
	 * <li>说明：获取指定顺序号(包含)后的所有实体，即：如参数t的顺序号为n，则获取该实体排序范围内，所有顺序号>=n的实体列表
	 * <li>创建人：王利成
	 * <li>创建日期：2015-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 指定顺序号的实体
	 * @return List<ZbFwWi>
	 * @throws Exception TODO
	 */
	@SuppressWarnings("unchecked")
	public List<ZbFwWidi> findAllBySN(ZbFwWidi t) throws Exception {
		String hql = "From ZbFwWidi Where recordStatus = 0 And zbfwwiIDX = ? And seqNo >= ?";
		return this.daoUtils.find(hql, new Object[]{t.getZbfwwiIDX(), t.getSeqNo()});
	}
    /**
     * <li>说明：根据作业项目主键获取数据项实体对象集
     * <li>创建人：程梅
     * <li>创建日期：2016-4-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param zbfwwiIDX 作业项目主键
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public List<ZbFwWidi> getModelByZbfwwiIDX(Serializable zbfwwiIDX) {
        String hql = "From ZbFwWidi Where recordStatus = 0 And zbfwwiIDX = ?";
        return this.daoUtils.find(hql, new Object[]{zbfwwiIDX});
    }
}