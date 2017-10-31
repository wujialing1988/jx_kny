package com.yunda.jx.pjjx.base.tecdefine.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntryManager;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardWS;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: TecCardWS业务类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:24:14
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="tecCardWSManager")
public class TecCardWSManager extends AbstractOrderManager<TecCardWS, TecCardWS> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：验证“工序编号”和“顺序号”字段的唯一性
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
	public String[] validateUpdate(TecCardWS t) {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
		List<TecCardWS> list = this.getModelsByTecCardIDX(t.getTecCardIDX());
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (TecCardWS entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (entity.getWsNo().equals(t.getWsNo())) {
				return new String[]{"工序编号：" + t.getWsNo() + "已经存在，不能重复添加！"};
			}
//			if (entity.getSeqNo().equals(t.getSeqNo())) {
//				return new String[]{"顺序号：" + t.getSeqNo() + "已经存在，不能重复添加！"};
//			}
		}
		return null;
	}
	
	/**
	 * <li>说明：根据“检修工艺卡主键”获取“配件检修工序”列表
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tecCardIDX 检修工艺卡主键
	 * @return List<TecCardWS> 检修工序实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<TecCardWS> getModelsByTecCardIDX(String tecCardIDX) {
		String hql = "From TecCardWS Where recordStatus = 0 And tecCardIDX = ?";
		return this.daoUtils.find(hql, new Object[]{tecCardIDX});
	}
	
	/**
	 * <li>说明：查询是否为子节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param  parentIDX 上级工序idx主键
	 * @return boolean 如果是叶子节点则返回true，否则返回false
	 * @throws BusinessException
	 */
	public boolean isLeaf(String parentIDX) throws BusinessException {
		StringBuffer hql = new StringBuffer();
		hql.append("Select count(*) From TecCardWS Where recordStatus = " + Constants.NO_DELETE);
		if (!StringUtil.isNullOrBlank(parentIDX)) {
			hql.append(" And wsParentIDX = '" + parentIDX + "'");
		}
		int count = daoUtils.getInt(enableCache(), hql.toString());
		return count == 0 ? true : false;
	}

	/**
	 * <li>说明：查询配件检修工序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param parentIDX 上级工序idx主键
	 * @param tecCardIDX 检修工艺卡主键
	 * @return List<HashMap<String, Object>> 对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> tree(String parentIDX, String tecCardIDX) {
		String idx = "";
		if ("ROOT_0".equals(parentIDX)) {
			idx = parentIDX;
		} else {
			TecCardWS parentObj = this.getModelById(parentIDX); // 获取父类对象
			idx = parentObj.getIdx();
		}
		String hql = "from TecCardWS where wsParentIDX = ? And tecCardIDX = ? And recordStatus = "
				+ Constants.NO_DELETE + " order by seqNo";
		List<TecCardWS> list = (List<TecCardWS>) this.daoUtils.find(hql, new Object[] { idx, tecCardIDX });
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		for (TecCardWS t : list) {
			Boolean isLeaf = this.isLeaf(t.getIdx());
			HashMap<String, Object> nodeMap = new HashMap<String, Object>();
			nodeMap.put("id", t.getIdx());						// 工序idx主键
			nodeMap.put("text", formatDisplayInfo(t)); 				// 工序名称
			nodeMap.put("leaf", isLeaf);
			nodeMap.put("parentIDX", t.getWsParentIDX()); 		// 上级工序idx主键
			nodeMap.put("wPNo", t.getWsNo()); 					// 工序名称
			nodeMap.put("wPDesc", t.getWsDesc()); 				// 工序描述
			nodeMap.put("qCContent", t.getQCContent()); 		// 质量检验
			nodeMap.put("seqNo", t.getSeqNo());					// 顺序号
			nodeMap.put("tecCardIDX", t.getTecCardIDX());		// 检修工艺卡idx主键
			children.add(nodeMap);
		}
		return children;
	}
	
	/**
	 * <li>说明：格式化【工序】顺序号显示
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 当前节点的【工序】
	 * @return 工序显示名称
	 */
	private String formatDisplayInfo(TecCardWS entity) {
		List<Integer> list = new ArrayList<Integer>();
		this.getParentSeqNo(entity, list);
		int length = list.size();
		StringBuilder sb = new StringBuilder();
		for (int i = length - 1; i >=0; i--) {
			sb.append(list.get(i)).append(".");
		}
		// 工序名称
		sb.append(entity.getWsName());
		return sb.toString();
	}
	
	/**
	 * <li>说明：递归获取配件检修工序的顺序号列
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 当前节点的【工序】
	 * @param list 顺序号集合
	 */
	public void getParentSeqNo(TecCardWS entity, List<Integer> list) {
		list.add(entity.getSeqNo());
		if (!entity.getWsParentIDX().equals("ROOT_0")) {
			TecCardWS parentEntity = this.getModelById(entity.getWsParentIDX());
			getParentSeqNo(parentEntity, list);
		}
	}
	
	/**
	 * <li>说明：查询已配置的”配件检修 - 质量检查“数据字典项
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @return List<String> 质量检查名称集合
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getQCContent() {
		EosDictEntryManager eosDictEntryManager = (EosDictEntryManager) Application
				.getSpringApplicationContext().getBean("eosDictEntryManager");
		Collection<EosDictEntry> collection = eosDictEntryManager.find("From EosDictEntry Where id.dicttypeid Like 'PJJX_QC'");
		if (null == collection || collection.size() <= 0) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (EosDictEntry entry : collection) {
			list.add(entry.getDictname());
		}
		return list;
	}
	
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 工序实体
	 * @return int 记录总数
	 */
	public int count(TecCardWS t) {
		String hql = "Select Count(*) From TecCardWS Where recordStatus = 0 And tecCardIDX = ? And wsParentIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getTecCardIDX(), t.getWsParentIDX()});
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
		TecCardWS entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From TecCardWS Where recordStatus = 0 And seqNo > ? And tecCardIDX = ? And wsParentIDX = ?";
		List<TecCardWS> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getTecCardIDX(), entity.getWsParentIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<TecCardWS> entityList = new ArrayList<TecCardWS>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (TecCardWS tecCard : list) {
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
		TecCardWS entity = this.getModelById(idx);
		String hql = "From TecCardWS Where recordStatus = 0 And seqNo = ? And tecCardIDX = ? And wsParentIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		TecCardWS nextEntity = (TecCardWS)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getTecCardIDX(), entity.getWsParentIDX()});
		List<TecCardWS> entityList = new ArrayList<TecCardWS>(2);
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
		TecCardWS entity = this.getModelById(idx);
		String hql = "From TecCardWS Where recordStatus = 0 And seqNo = ? And tecCardIDX = ? And wsParentIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		TecCardWS nextEntity = (TecCardWS)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getTecCardIDX(), entity.getWsParentIDX()});
		List<TecCardWS> entityList = new ArrayList<TecCardWS>(2);
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
		TecCardWS entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From TecCardWS Where recordStatus = 0 And seqNo < ? And tecCardIDX = ? And wsParentIDX = ?";
		List<TecCardWS> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getTecCardIDX(), entity.getWsParentIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<TecCardWS> entityList = new ArrayList<TecCardWS>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (TecCardWS tecCard : list) {
			tecCard.setSeqNo(tecCard.getSeqNo() + 1);
			entityList.add(tecCard);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：获取排序范围内的同类型的所有记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-02
	 * <li>修改人： 何涛
	 * <li>修改日期：2015-01-07
	 * <li>修改内容：修改查询条件未限定父节点idx主键的错误
	 * 
	 * @param 	t		实体对象
	 * @return 	List<T>	结果集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TecCardWS> findAll(TecCardWS t) {
		String hql = "From TecCardWS Where recordStatus = 0 And tecCardIDX = ? And wsParentIDX = ?";
		return this.daoUtils.find(hql, new Object[]{t.getTecCardIDX(), t.getWsParentIDX()});
	}

	/**
	 * <li>说明：获取排序范围内的同类型的顺序号大于等于参数对象的顺序号的所有实体
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-02
	 * <li>修改人：何涛
	 * <li>修改日期：2015-01-07
	 * <li>修改内容：修改查询条件未限定父节点idx主键的错误
	 * 
	 * @param 	t		实体对象
	 * @return 	List<T>	结果集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TecCardWS> findAllBySN(TecCardWS t) throws Exception {
		String hql = "From TecCardWS Where recordStatus = 0 And tecCardIDX = ? And seqNo >= ? And wsParentIDX = ?";
		return this.daoUtils.find(hql, new Object[]{t.getTecCardIDX(), t.getSeqNo(), t.getWsParentIDX()});
	}

}