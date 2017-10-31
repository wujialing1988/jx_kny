package com.yunda.jx.pjjx.base.tecdefine.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCard;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardMat;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardWS;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: TecCard业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:23:18
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="tecCardManager")
public class TecCardManager extends AbstractOrderManager<TecCard, TecCard> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Resource
	TecCardWSManager tecCardWSManager;
	
	@Resource
	TecCardMatManager tecCardMatManager;
	
	/**
	 * <li>说明：验证“工艺卡编号”和“顺序号”字段的唯一性
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(TecCard t) {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
		List<TecCard> list = this.findAll(t);
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (TecCard entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getTecCardNo().equals(entity.getTecCardNo())) {
				return new String[]{"工艺卡编号：" + t.getTecCardNo() + "已经存在，不能重复添加！"};
			}
//			if (t.getSeqNo().equals(entity.getSeqNo())) {
//				return new String[]{"顺序号：" + t.getSeqNo() + "已经存在，不能重复添加！"};
//			}
		}
		return null;
	}
	
	/**
	 * <li>说明：根据“检修工艺主键”获取“配件检修工艺卡”列表
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tecIDX 检修工艺主键
	 * @return List<TecCard> 检修工艺卡集合
	 */
	@SuppressWarnings("unchecked")
	public List<TecCard> getModelsByTecIDX(String tecIDX) {
		String hql = "From TecCard Where recordStatus = 0 And tecIDX = ?";
		return this.daoUtils.find(hql, new Object[]{tecIDX});
	}
	
	/**
	 * <li>说明：级联删除【配件检修工序】和【配件检修所需物料】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 待删除的idx数组
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除【配件检修工序】
		for (int i = 0; i < ids.length; i++) {
			List<TecCardWS> list = this.tecCardWSManager.getModelsByTecCardIDX((String)ids[0]);
			if (null != list && 0 < list.size()) {
				this.tecCardWSManager.logicDelete(list);
			}
		}
		// 级联删除【配件检修所需物料】
		for (int i = 0; i < ids.length; i++) {
			List<TecCardMat> list = this.tecCardMatManager.getModelsByTecCardIDX((String)ids[0]);
			if (null != list && 0 < list.size()) {
				this.tecCardMatManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：级联删除【配件检修工序】和【配件检修所需物料】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 检修工艺卡集合
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public void logicDelete(List<TecCard> entityList) throws BusinessException, NoSuchFieldException {
		List list = null;
		for (TecCard tecCard : entityList) {
			// 级联删除【配件检修工序】
			String tecCardIDX = tecCard.getIdx();
			list = this.tecCardWSManager.getModelsByTecCardIDX(tecCardIDX);
			if (null != list && 0 < list.size()) {
				this.tecCardWSManager.logicDelete(list);
			}
			// 级联删除【配件检修所需物料】
			list = this.tecCardMatManager.getModelsByTecCardIDX(tecCardIDX);
			if (null != list && 0 < list.size()) {
				this.tecCardMatManager.logicDelete(list);
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
	 * @param t 检修工艺卡实体
	 * @return int 记录总数
	 */
	public int count(TecCard t) {
		String hql = "Select Count(*) From TecCard Where recordStatus = 0 And tecIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getTecIDX()});
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
		TecCard entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From TecCard Where recordStatus = 0 And seqNo > ? And tecIDX = ?";
		List<TecCard> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getTecIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<TecCard> entityList = new ArrayList<TecCard>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (TecCard tecCard : list) {
			tecCard.setSeqNo(tecCard.getSeqNo() - 1);
			entityList.add(tecCard);
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
		TecCard entity = this.getModelById(idx);
		String hql = "From TecCard Where recordStatus = 0 And seqNo = ? And tecIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		TecCard nextEntity = (TecCard)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getTecIDX()});
		List<TecCard> entityList = new ArrayList<TecCard>(2);
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
		TecCard entity = this.getModelById(idx);
		String hql = "From TecCard Where recordStatus = 0 And seqNo = ? And tecIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		TecCard nextEntity = (TecCard)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getTecIDX()});
		List<TecCard> entityList = new ArrayList<TecCard>(2);
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
		TecCard entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From TecCard Where recordStatus = 0 And seqNo < ? And tecIDX = ?";
		List<TecCard> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getTecIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<TecCard> entityList = new ArrayList<TecCard>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (TecCard tecCard : list) {
			tecCard.setSeqNo(tecCard.getSeqNo() + 1);
			entityList.add(tecCard);
		}
		this.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：查询作业流程节点所用工艺卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人：何涛
	 * <li>修改日期：2014-12-17
	 * <li>修改内容：同一个作业流程中，一个工艺卡只能与一个作业节点挂接
	 * 
	 * @param searchEntity 		查询实体
	 * @param wPNodeIDX			作业流程节点主键
	 * @param wPIDX				作业流程主键
	 * @param isInWPNode true:表示查询作业流程节点使用到的工艺卡 false:表示查询作业流程节点未使用到的工艺卡
	 * @return Page<TecCard> 实体集合
	 */
	private Page<TecCard> findPageListForWPNode(SearchEntity<TecCard> searchEntity, String wPNodeIDX, String wPIDX, boolean isInWPNode) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select new TecCard(a.idx, a.tecCardNo, a.tecCardDesc, a.seqNo, b.tecNo, b.tecName, b.tecDesc) From TecCard a, Tec b Where a.recordStatus = ").append(Constants.NO_DELETE);
		sb.append(" And a.recordStatus = ").append(Constants.NO_DELETE).append(" And a.tecIDX = b.idx");
		if (isInWPNode) {
			sb.append(" And a.idx In (Select tecCardIDX From WPNodeUnionTecCard Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPNodeIDX ='").append(wPNodeIDX).append("')");
		} else {
//			sb.append(" And a.idx Not In (Select tecCardIDX From WPNodeUnionTecCard Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPNodeIDX ='").append(wPNodeIDX).append("')");
			sb.append(" And a.idx Not In (Select tecCardIDX From WPNodeUnionTecCard Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPNodeIDX In (Select idx From WPNode Where recordStatus = ").append(Constants.NO_DELETE).append(" And wpIDX = '").append(wPIDX).append("'))");
		}
		// 工艺卡来源于检修需求中关联的检修工艺
		sb.append(" And a.tecIDX In (Select tecIDX From WPUnionTec Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPIDX = '").append(wPIDX).append("'").append(")");
		TecCard entity = searchEntity.getEntity();
		// 查询条件 - 工艺名称
		if (!StringUtil.isNullOrBlank(entity.getTecName())) {
			sb.append(" And b.tecName Like '%").append(entity.getTecName()).append("%'");
		}
		// 查询条件 - 编号
		if (!StringUtil.isNullOrBlank(entity.getTecCardNo())) {
			sb.append(" And a.tecCardNo Like '%").append(entity.getTecCardNo()).append("%'");
		}
		// 查询条件 - 描述
		if (!StringUtil.isNullOrBlank(entity.getTecCardDesc())) {
			sb.append(" And a.tecCardDesc Like '%").append(entity.getTecCardDesc()).append("%'");
		}
		// 排序字段
		if (null != searchEntity.getOrders()) {
			for (Order order : searchEntity.getOrders()) {
				String temp = order.toString();
				if (temp.contains("tecNo") || temp.contains("tecName") || temp.contains("tecDesc")) {
					sb.append(" order by ").append("b." + temp);
				} else if (temp.contains("seqNo") ) {
					sb.append(" order by ").append("b.tecName ASC, a." + temp);
				} else {
					sb.append(" order by ").append("a." + temp);
				}
			}
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：查询作业流程节点所用工艺卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 		查询实体
	 * @param wPNodeIDX			作业流程节点主键
	 * @param wPIDX				作业流程主键
	 * @return Page<TecCard> 实体集合
	 */
	public Page<TecCard> findPageListForWPNode(SearchEntity<TecCard> searchEntity, String wPNodeIDX, String wPIDX) {
		return this.findPageListForWPNode(searchEntity, wPNodeIDX, wPIDX, true);
	}
	
	
	/**
	 * <li>说明：查询作业流程节点所用工艺卡 - 候选工艺卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 		查询实体
	 * @param wPNodeIDX			作业流程节点主键
	 * @param wPIDX				作业流程主键
	 * @return Page<TecCard> 实体集合
	 */
	public Page<TecCard> findPageListForWPNodeSelect(SearchEntity<TecCard> searchEntity, String wPNodeIDX, String wPIDX) {
		return this.findPageListForWPNode(searchEntity, wPNodeIDX, wPIDX, false);
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
	public List<TecCard> findAll(TecCard t) {
		return this.getModelsByTecIDX(t.getTecIDX());
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
	 * @return List<TecCard> 工艺卡实体集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TecCard> findAllBySN(TecCard t) throws Exception {
		String hql = "From TecCard Where recordStatus = 0 And tecIDX = ? And seqNo >= ?";
		return this.daoUtils.find(hql, new Object[]{t.getTecIDX(), t.getSeqNo()});
	}

}