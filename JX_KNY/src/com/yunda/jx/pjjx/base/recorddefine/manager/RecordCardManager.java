package com.yunda.jx.pjjx.base.recorddefine.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.qcitemdefine.entity.QCItem;
import com.yunda.jx.pjjx.base.qcitemdefine.manager.QCItemManager;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordCard;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordCardQC;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordCard业务类,记录卡
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="recordCardManager")
public class RecordCardManager extends AbstractOrderManager<RecordCard, RecordCard> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** RecordRI业务类,检修检测项 */
	@Resource
	RecordRIManager recordRIManager;
	
	/** RecordCardQC业务类,质量检查定义 */
	@Resource
	RecordCardQCManager recordCardQCManager;
	
	/** QCItem业务类 */
	@Resource
	QCItemManager qCItemManager;
	
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
	public String[] validateUpdate(RecordCard t) throws BusinessException {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
		List<RecordCard> list = this.findAll(t);
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (RecordCard entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getRecordCardNo().equals(entity.getRecordCardNo())) {
				return new String[]{"记录卡编号：" + t.getRecordCardNo() + "已经存在，不能重复添加！"};
			}
//			if (t.getSeqNo().equals(entity.getSeqNo())) {
//				return new String[]{"顺序号：" + t.getSeqNo() + "已经存在，不能重复添加！"};
//			}
		}
		return null;
	}
	
	/**
	 * <li>说明：同步更新【质量检查定义】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 记录卡实体
	 * @return
	 */
	@Override
	public void saveOrUpdate(RecordCard t) throws BusinessException, NoSuchFieldException {
		super.saveOrUpdate(t);
		// 同步更新【质量检查定义】
		// 获取“已存储”的【质量检查定义】记录
		List<RecordCardQC> list = recordCardQCManager.getModelsByRecordCardIDX(t.getIdx());
		// 1 删除已被页面删除的记录
		if (null != list && 0 < list.size()) {
			for (RecordCardQC rcQC: list) {
				if (this.recordCardQCManager.isDeleted(rcQC, t.getQCContent())) {
					this.recordCardQCManager.logicDelete(rcQC.getIdx());
				}
			}
		}
		// 2 存储新增加的记录
		RecordCardQC qc = null;
		List<RecordCardQC> entityList = new ArrayList<RecordCardQC>();
		// 获取“待存储”的【质量检查定义】条目
		List<QCItem> qCItems = this.qCItemManager.getModelsByQCContent(t.getQCContent());
		if (null != qCItems) {
			for (QCItem qcItem : qCItems) {
				// 根据质量检查项名称获取对象实体
				qc = recordCardQCManager.getModelByQCItemName(qcItem.getQCItemName(), t.getIdx());
				// 判断以该名称命名的质量检查项是否已经存在
				if (null != qc) {
					// 如果存在，并且该质量检查项的“检查项编码”未发生修改，则不做任何处理
					if (qc.getQCItemNo().equals(qcItem.getQCItemNo())) {
						continue;
					}
					qc.setQCItemNo(qcItem.getQCItemNo());
				} else {
					// 如果不存在，则进行新增
					qc = new RecordCardQC();
					qc.setRecordCardIDX(t.getIdx());
					qc.setQCItemNo(qcItem.getQCItemNo());
					qc.setQCItemName(qcItem.getQCItemName());
					qc.setSeqNo(qcItem.getSeqNo());
				}
				entityList.add(qc);
			}
		}
		recordCardQCManager.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：根据“记录单主键”获取【记录卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param recordIDX 记录单主键
	 * @return 记录卡集合
	 */
	@SuppressWarnings("unchecked")
	public List<RecordCard> getModelsByRecordIDX(String recordIDX) {
		String hql = "From RecordCard Where recordStatus = 0 And recordIDX = ?";
		return this.daoUtils.find(hql, new Object[]{recordIDX});
	}
	
	/**
	 * <li>说明：级联删除【检修检测项】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 待删除的idx数组
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		String idx = null;
		for (int i = 0; i < ids.length; i++) {
			// 记录卡idx主键
			idx = (String)ids[0];
			// 级联删除【检修检测项】
			List list = this.recordRIManager.getModelsByRecordCardIDX(idx);
			if (null != list && 0 < list.size()) {
				this.recordRIManager.logicDelete(list);
			}
			// 级联删除【质量检查定义】
			list = this.recordCardQCManager.getModelsByRecordCardIDX(idx);
			if (null != list && 0 < list.size()) {
				this.recordCardQCManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：级联删除【检修检测项】和
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 待删除的实体集合
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public void logicDelete(List<RecordCard> entityList) throws BusinessException, NoSuchFieldException {
		String idx = null;
		for (RecordCard entity : entityList) {
			// 记录卡idx主键
			idx = entity.getIdx();
			// 级联删除【检修检测项】
			List list = this.recordRIManager.getModelsByRecordCardIDX(idx);
			if (null != list && 0 < list.size()) {
				this.recordRIManager.logicDelete(list);
			}
			// 级联删除【质量检查定义】
			list = this.recordCardQCManager.getModelsByRecordCardIDX(idx);
			if (null != list && 0 < list.size()) {
				this.recordCardQCManager.logicDelete(list);
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
	 * @param t 记录卡实体
	 * @return 当前对象在数据库中的记录总数
	 */
	public int count(RecordCard t) {
		String hql = "Select Count(*) From RecordCard Where recordStatus = 0 And recordIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getRecordIDX()});
	}
	
	/**
	 * <li>说明：置底
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 记录卡主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		RecordCard entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From RecordCard Where recordStatus = 0 And seqNo > ? And recordIDX = ?";
		List<RecordCard> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getRecordIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<RecordCard> entityList = new ArrayList<RecordCard>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (RecordCard recordCard : list) {
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
	 * @param idx 记录卡主键
	 * @throws Exception
	 */
	public void updateMoveDown(String idx) throws Exception {
		RecordCard entity = this.getModelById(idx);
		String hql = "From RecordCard Where recordStatus = 0 And seqNo = ? And recordIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		RecordCard nextEntity = (RecordCard)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getRecordIDX()});
		List<RecordCard> entityList = new ArrayList<RecordCard>(2);
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
	 * @param idx 记录卡主键
	 * @throws Exception
	 */
	public void updateMoveUp(String idx) throws Exception {
		RecordCard entity = this.getModelById(idx);
		String hql = "From RecordCard Where recordStatus = 0 And seqNo = ? And recordIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		RecordCard nextEntity = (RecordCard)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getRecordIDX()});
		List<RecordCard> entityList = new ArrayList<RecordCard>(2);
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
	 * @param idx 记录卡主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		RecordCard entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From RecordCard Where recordStatus = 0 And seqNo < ? And recordIDX = ?";
		List<RecordCard> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getRecordIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<RecordCard> entityList = new ArrayList<RecordCard>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (RecordCard recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() + 1);
			entityList.add(recordCard);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：查询【质量检查维护】功能模块已配置的”质量检查项“
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人：何涛
	 * <li>修改日期：2014-12-04
	 * <li>修改内容：新增QCItemManager.getQCContent()方法用以替代该方法，因此不建议使用该方法
	 * 
	 * @return List<String>
	 */
	@Deprecated
	public static List<String> getQCContent() {
		QCItemManager qCItemManager = (QCItemManager) Application
				.getSpringApplicationContext().getBean("qCItemManager");
		List<QCItem> collect = qCItemManager.getAll();
		if (null == collect || collect.size() <= 0) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (QCItem entry : collect) {
			list.add(entry.getQCItemName());
		}
		return list;
	}
	
	/**
	 * <li>说明：查询作业流程节点所用记录卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人：何涛
	 * <li>修改日期：2014-12-17
	 * <li>修改内容：同一个作业流程中，一个记录卡只能与一个作业节点挂接
	 * 
	 * @param searchEntity 		查询实体
	 * @param wPNodeIDX			作业流程节点主键
	 * @param wPIDX				作业流程主键
	 * @param isInWPNode true:表示查询作业流程节点使用到的记录卡 false:表示查询作业流程节点未使用到的记录卡
	 * @return 记录卡分页查询结果集合
	 */
	private Page<RecordCard> findPageListForWPNode(SearchEntity<RecordCard> searchEntity, String wPNodeIDX, String wPIDX, boolean isInWPNode) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select new RecordCard(a.idx, a.recordCardNo, a.recordCardDesc, a.seqNo, b.recordNo, b.recordName, b.recordDesc) From RecordCard a, Record b Where a.recordStatus = ").append(Constants.NO_DELETE);
		sb.append(" And a.recordStatus = ").append(Constants.NO_DELETE).append(" And a.recordIDX = b.idx");
		if (isInWPNode) {
			sb.append(" And a.idx In (Select recordCardIDX From WPNodeUnionRecordCard Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPNodeIDX ='").append(wPNodeIDX).append("')");
		} else {
//			sb.append(" And a.idx Not In (Select recordCardIDX From WPNodeUnionRecordCard Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPNodeIDX ='").append(wPNodeIDX).append("')");
			sb.append(" And a.idx Not In (Select recordCardIDX From WPNodeUnionRecordCard Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPNodeIDX In (Select idx From WPNode Where recordStatus = ").append(Constants.NO_DELETE).append(" And wpIDX = '").append(wPIDX).append("'))");
		}
		// 记录卡来源于检修需求中关联的检修记录
		sb.append(" And a.recordIDX In (Select recordIDX From WPUnionRecord Where recordStatus = ").append(Constants.NO_DELETE).append(" And wPIDX = '").append(wPIDX).append("'").append(")");
		RecordCard entity = searchEntity.getEntity();
		// 查询条件 - 记录单名称
		if (!StringUtil.isNullOrBlank(entity.getRecordName())) {
			sb.append(" And b.recordName Like '%").append(entity.getRecordName()).append("%'");
		}
		// 查询条件 - 编号
		if (!StringUtil.isNullOrBlank(entity.getRecordCardNo())) {
			sb.append(" And a.recordCardNo Like '%").append(entity.getRecordCardNo()).append("%'");
		}
		// 查询条件 - 描述
		if (!StringUtil.isNullOrBlank(entity.getRecordCardDesc())) {
			sb.append(" And a.recordCardDesc Like '%").append(entity.getRecordCardDesc()).append("%'");
		}
		// 排序字段
		if (null != searchEntity.getOrders()) {
			for (Order order : searchEntity.getOrders()) {
				String temp = order.toString();
				if (temp.contains("recordNo") || temp.contains("recordName") || temp.contains("recordDesc")) {
					sb.append(" order by ").append("b." + temp);
				} else if (temp.contains("seqNo") ) {
					sb.append(" order by ").append("b.recordName ASC, a." + temp);
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
	 * <li>说明：查询作业流程节点所用记录卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 		查询实体
	 * @param wPNodeIDX			作业流程节点主键
	 * @param wPIDX				作业流程主键
	 * @return 作业流程节点下属记录卡集合
	 */
	public Page<RecordCard> findPageListForWPNode(SearchEntity<RecordCard> searchEntity, String wPNodeIDX, String wPIDX) {
		return this.findPageListForWPNode(searchEntity, wPNodeIDX, wPIDX, true);
	}
	
	
	/**
	 * <li>说明：查询作业流程节点所用记录卡 - 候选记录卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 		查询实体
	 * @param wPNodeIDX			作业流程节点主键
	 * @param wPIDX				作业流程主键
	 * @return 作业流程节点所用记录卡
	 */
	public Page<RecordCard> findPageListForWPNodeSelect(SearchEntity<RecordCard> searchEntity, String wPNodeIDX, String wPIDX) {
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
	public List<RecordCard> findAll(RecordCard t) {
		return this.getModelsByRecordIDX(t.getRecordIDX());
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
	 * @return List<RecordCard> 记录卡实体集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecordCard> findAllBySN(RecordCard t) throws Exception {
		String hql = "From RecordCard Where recordStatus = 0 And recordIDX = ? And seqNo >= ?";
		return this.daoUtils.find(hql, new Object[]{t.getRecordIDX(), t.getSeqNo()});
	}
	
}