package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderManager;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPNode业务类,作业节点
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wPNodeManager")
public class WPNodeManager extends AbstractOrderManager<WPNode, WPNode>{
	/**
	 * <li>标题：机车检修管理信息系统
	 * <li>说明：用于关联查询节点前置节点主键和前置节点名称
	 * <li>创建人： 何涛
	 * <li>创建日期： 2014-11-25 上午11:48:00
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * 
	 * @author 测控部检修系统项目组
	 * @version 1.0
	 */
	@Entity
	private static class UnionWPNode{
		
		/* idx主键 */
		@Id
		private String idx;
		
		/* 作业流程主键 */
		@Column(name="WP_IDX")
		private String wpIDX;
		
		/* 上级作业节点主键 */
		@Column(name="PARENT_WP_NODE_IDX")
		private String parentWPNodeIDX;
		
		/* 节点名称 */
		@Column(name="WP_Node_Name")
		private String wpNodeName;
		
		/* 节点描述 */
		@Column(name="WP_Node_Desc")
		private String wpNodeDesc;
		
		/* 工期 */
		@Column(name="Rated_Period")
		private Double ratedPeriod;
		
		/* 顺序号 */
		@Column(name="Seq_No")
		private Integer seqNo;
		
		/* 是否叶子节点,0:否；1：是 */
		@Column(name="IS_LEAF")
		private Integer isLeaf;
		
		/* 前置节点主键 */
		@Column(name="Pre_WP_Node_IDX")
		private String preWPNodeIDX;
		
		/* 前置节点名称 */
		@Column(name="Pre_WP_Node_Name")
		private String preWPNodeName;
		
		/* 前置节点序号 */
		@Column(name="Pre_WP_Node_Seq_No")
		private String preWPNodeSeqNo;

		/**
		 * Default Constructor
		 */
		public UnionWPNode() {
			super();
		}

		public String getIdx() {
			return idx;
		}

		public Integer getIsLeaf() {
			return isLeaf;
		}

		public String getParentWPNodeIDX() {
			return parentWPNodeIDX;
		}

		public String getPreWPNodeIDX() {
			return preWPNodeIDX;
		}

		public String getPreWPNodeName() {
			return preWPNodeName;
		}

		public String getPreWPNodeSeqNo() {
			return preWPNodeSeqNo;
		}

		public void setPreWPNodeSeqNo(String preWPNodeSeqNo) {
			this.preWPNodeSeqNo = preWPNodeSeqNo;
		}

		public Double getRatedPeriod() {
			return ratedPeriod;
		}

		public Integer getSeqNo() {
			return seqNo;
		}

		public String getWpIDX() {
			return wpIDX;
		}

		public String getWpNodeDesc() {
			return wpNodeDesc;
		}

		public String getWpNodeName() {
			return wpNodeName;
		}

		public void setIdx(String idx) {
			this.idx = idx;
		}

		public void setIsLeaf(Integer isLeaf) {
			this.isLeaf = isLeaf;
		}

		public void setParentWPNodeIDX(String parentWPNodeIDX) {
			this.parentWPNodeIDX = parentWPNodeIDX;
		}

		public void setPreWPNodeIDX(String preWPNodeIDX) {
			this.preWPNodeIDX = preWPNodeIDX;
		}

		public void setPreWPNodeName(String preWPNodeName) {
			this.preWPNodeName = preWPNodeName;
		}

		public void setRatedPeriod(Double ratedPeriod) {
			this.ratedPeriod = ratedPeriod;
		}

		public void setSeqNo(Integer seqNo) {
			this.seqNo = seqNo;
		}

		public void setWpIDX(String wpIDX) {
			this.wpIDX = wpIDX;
		}

		public void setWpNodeDesc(String wpNodeDesc) {
			this.wpNodeDesc = wpNodeDesc;
		}

		public void setWpNodeName(String wpNodeName) {
			this.wpNodeName = wpNodeName;
		}

	}
	
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** WPNodeSeq业务类,节点前后置关系 */
	@Resource
	WPNodeSeqManager wPNodeSeqManager;
	
	/** WPNodeUnionEquipCard业务类,作业节点所挂机务设备工单 */
	@Resource
	WPNodeUnionEquipCardManager wPNodeUnionEquipCardManager;
	
	/** WPNodeUnionRecordCardManager业务类,作业节点所挂记录单 */
	@Resource
	WPNodeUnionRecordCardManager wPNodeUnionRecordCardManager;
	
	/** WPNodeUnionTecCard业务类,作业节点所挂工艺卡 */
	@Resource
	WPNodeUnionTecCardManager wPNodeUnionTecCardManager;
	
	
	/** CodeRuleConfig业务类,业务编码规则配置 */
	@Resource
	CodeRuleConfigManager codeRuleConfigManager;
	
	/**
	 * <li>说明：级联删除【作业节点所挂记录单】、【节点前后置关系】、【作业节点所挂工艺卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 何涛
	 * <li>修改日期：2015-02-10
	 * <li>修改内容：【作业节点所挂机务设备工单】未挂接在作业节点上，此处级联删除不对设备工单进行处理，设备工单的删除应放置在作业的删除部分
	 * 
	 * @param wPNodeIDX 节点主键
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	private void cascadeDelete(String wPNodeIDX) throws NoSuchFieldException {
		// 级联删除【作业节点所挂记录单】
		List list = this.wPNodeUnionRecordCardManager.getModelsByWPNodeIDX(wPNodeIDX);
		if (null != list && 0 < list.size()) {
			this.wPNodeUnionRecordCardManager.logicDelete(list);
		}
		// 级联删除【节点前后置关系】
		list = this.wPNodeSeqManager.getAllModelsByWPNodeIDX(wPNodeIDX);
		if (null != list && 0 < list.size()) {
			this.wPNodeSeqManager.logicDelete(list);
		}
		// 级联删除【作业节点所挂工艺卡】
		list = this.wPNodeUnionTecCardManager.getModelsByWPNodeIDX(wPNodeIDX);
		if (null != list && 0 < list.size()) {
			this.wPNodeUnionTecCardManager.logicDelete(list);
		}
	}
	
	/**
	 * <li>说明：查询当前对象在数据库中的记录总数
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 作业节点实体对象
	 * @return int 记录总数
	 */
	public int count(WPNode t) {
		String hql = "Select Count(*) From WPNode Where recordStatus = 0 And wpIDX = ? And parentWPNodeIDX = ?";
		return this.daoUtils.getCount(hql, new Object[]{t.getWpIDX(), t.getParentWPNodeIDX()});
	}
	
	/**
	 * <li>说明：关联查询节点前置节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-25
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询对象实体
	 * @return Page<UnionWPNode> 实体集合
	 */
	@SuppressWarnings("unchecked")
	public Page<UnionWPNode> findList(SearchEntity<WPNode> searchEntity) {
		String sql = SqlMapUtil.getSql("pjjx-wpNode:findUnionWPNode");
		StringBuilder sb = new StringBuilder(sql);
		WPNode entity = searchEntity.getEntity();
		if (!StringUtil.isNullOrBlank(entity.getWpIDX())) {
			sb.append(" AND WP_IDX ='").append(entity.getWpIDX()).append("'");
		}
		if (!StringUtil.isNullOrBlank(entity.getParentWPNodeIDX())) {
			sb.append(" AND PARENT_WP_NODE_IDX ='").append(entity.getParentWPNodeIDX()).append("'");
		}
		sb.append(" ORDER BY SEQ_NO ASC");
		List list = this.daoUtils.executeSqlQueryEntity(sb.toString(), UnionWPNode.class);
		return new Page<UnionWPNode>(list);
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
	 * @return 实体界面显示文本
	 */
	private String formatDisplayInfo(WPNode entity) {
		List<Integer> list = new ArrayList<Integer>();
		this.listParentsSeqNo(entity, list);
		int length = list.size();
		StringBuilder sb = new StringBuilder();
		for (int i = length - 1; i >= 0; i--) {
			sb.append(list.get(i)).append(".");
		}
		// 节点名称
		sb.append(entity.getWpNodeName());
		return sb.toString();
	}

	/**
	 * <li>说明：获取同一个作业流程、同级的作业节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-02
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param parentWPNodeIDX	父作业节点主键
	 * @param wpIDX				作业流程idx主键
	 * @return List<WPNode>		作业流程节点集
	 */
	@SuppressWarnings("unchecked")
	public List<WPNode> getModels(String parentWPNodeIDX, String wpIDX) {
		String hql = "From WPNode Where recordStatus = 0 And parentWPNodeIDX = ? And wpIDX = ?";
		return this.daoUtils.find(hql, new Object[]{parentWPNodeIDX, wpIDX});
	}

	/**
	 * <li>说明：根据“作业流程主键”获取【作业节点】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpIDX 作业流程主键
	 * @return 实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNode> getModelsByWPIDX(String wpIDX) {
		String hql = "From WPNode Where recordStatus = 0 And wpIDX = ?";
		return this.daoUtils.find(hql, new Object[]{wpIDX});
	}

	/**
	 * <li>获取已知作业节点之后的所有同级作业节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 作业节点实体对象
	 * @return 实体集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WPNode> findAllBySN(WPNode t) throws Exception {
		String hql = "From WPNode Where recordStatus = 0 And seqNo >= ? And wpIDX = ? And parentWPNodeIDX = ?";
		return this.daoUtils.find(hql, new Object[]{t.getSeqNo(), t.getWpIDX(), t.getParentWPNodeIDX()});
	}
	
	/**
	 * <li>说明：递归获取作业节点的顺序号列
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 当前节点的【作业节点】
	 * @param list 顺序号集合
	 */
	public void listParentsSeqNo(WPNode entity, List<Integer> list) {
		list.add(entity.getSeqNo());
		if (!entity.getParentWPNodeIDX().equals("ROOT_0")) {
			listParentsSeqNo(this.getModelById(entity.getParentWPNodeIDX()), list);
		}
	}
	
	/**
	 * <li>说明：查询作业节点的所有父节点，并以从叶子到根的顺序存入list
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param parentWPNodeIDX 上级作业节点主键
	 * @param list 作业节点实体集合
	 */
	public void listParentsWPNodes(String parentWPNodeIDX, List<WPNode> list) {
		WPNode wpNode = this.getModelById(parentWPNodeIDX);
		if (null != wpNode) {
			list.add(wpNode);
			listParentsWPNodes(wpNode.getParentWPNodeIDX(), list);
		}
	}
	
	/**
	 * <li>说明：获取设置指定节点主键的直接后置节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-01
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpNodeIDX 作业节点主键
	 * @return List<WPNode> 作业节点实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNode> getAfterWPNodes(String wpNodeIDX) {
		String hql = "From WPNode Where idx In (Select wpNodeIDX From WPNodeSeq Where preWPNodeIDX = ? And recordStatus = 0) And recordStatus = 0";
		return this.daoUtils.find(hql, new Object[]{wpNodeIDX});
	}
	
	/**
	 * <li>说明：获取设置指定节点主键的直接前置节点
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpNodeIDX 作业节点主键
	 * @return List<WPNode> 作业节点实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNode> getBeforeWPNodes(String wpNodeIDX) {
		String hql = "From WPNode Where idx In (Select preWPNodeIDX From WPNodeSeq Where wpNodeIDX = ? And recordStatus = 0) And recordStatus = 0";
		return this.daoUtils.find(hql, new Object[]{wpNodeIDX});
	}
	
	
	
	/**
	 * <li>说明：递归获取设置指定节点主键的所有直接和间接后置节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-01
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpNodeIDX 作业节点主键
	 * @param entityList 作业节点实体集合
	 */
	public void listAfterNodes(String wpNodeIDX, List<WPNode> entityList) {
		List<WPNode> afterWPNodes = this.getAfterWPNodes(wpNodeIDX);
		if (null != afterWPNodes && 0 < afterWPNodes.size()) {
			for (WPNode wpNode : afterWPNodes) {
				if (!entityList.contains(wpNode)) {
					entityList.add(wpNode);
				}
				listAfterNodes(wpNode.getIdx(), entityList);
			}
		}
	}
	
	/**
	 * <li>说明：递归获取设置指定节点主键的所有直接和间接前置节点
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpNodeIDX 作业节点主键
	 * @param entityList 作业节点实体集合
	 */
	public void listBeforeNodes(String wpNodeIDX, List<WPNode> entityList) {
		List<WPNode> beforeWPNodes = this.getBeforeWPNodes(wpNodeIDX);
		if (null != beforeWPNodes && 0 < beforeWPNodes.size()) {
			for (WPNode wpNode : beforeWPNodes) {
				if (!entityList.contains(wpNode)) {
					entityList.add(wpNode);
				}
				listBeforeNodes(wpNode.getIdx(), entityList);
			}
		}
	}
	
	/**
	 * <li>说明：级联删除【作业节点所挂记录单】、【作业节点所挂机务设备工单】、【节点前后置关系】、【作业节点所挂工艺卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 作业节点实体集合
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@Override
	public void logicDelete(java.util.List<WPNode> entityList) throws BusinessException ,NoSuchFieldException {
		for (WPNode node : entityList) {
            this.logicDelete(node);
		}
	}

	/**
	 * <li>说明：级联删除【作业节点所挂记录单】、【作业节点所挂机务设备工单】、【节点前后置关系】、【作业节点所挂工艺卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 待删除的idx数组
	 * @throws BusinessException,NoSuchFieldException
	 */		
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (int i = 0; i < ids.length; i++) {
            this.logicDelete(this.getModelById(ids[i]));
		}
	}
    
    /**
     * <li>说明：递归删除作业节点及其下属所有子节点
     * <li>创建人：何涛
     * <li>创建日期：2015-11-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 作业节点实体
     * @throws NoSuchFieldException
     */
    private void logicDelete(WPNode entity) throws NoSuchFieldException {
        // 如果是叶子节点则级联删除【作业节点所挂记录单】、【作业节点所挂机务设备工单】、【节点前后置关系】、【作业节点所挂工艺卡】
        if (WPNode.CONST_INT_IS_LEAF_YES == entity.getIsLeaf().intValue()) {
            this.cascadeDelete(entity.getIdx());

        // 如果非叶子节点则级联删除下级所有节点
        } else {
            List<WPNode> entityList = this.getModels(entity.getIdx(), entity.getWpIDX());
            for (WPNode node : entityList) {
                this.logicDelete(node);
            }
        }
        super.logicDelete(entity.getIdx());
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
	@SuppressWarnings("unchecked")
	@Override
	public List<WPNode> findAll(WPNode t) {
		return getModels(t.getParentWPNodeIDX(), t.getWpIDX());
	}
	
	/**
	 * <li>说明：重写保存方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 作业节点实体
	 * @throws BusinessException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void saveOrUpdate(WPNode t) throws BusinessException, NoSuchFieldException {
		// 在保存叶子节点时，同步更新所有父节点的“工期（分钟）”字段值
		if (t.getIsLeaf() == WPNode.CONST_INT_IS_LEAF_YES && null != t.getIdx()) {
			WPNode existedWPNode = this.getModelById(t.getIdx());

			Double rp = existedWPNode.getRatedPeriod() == null ? 0 : existedWPNode.getRatedPeriod();
			// 获取更新后的【作业节点】与原【作业节点】的“工期”差值
			Double dissimilarity = t.getRatedPeriod() - rp;
			
			if (dissimilarity != 0) {
				updateParentWPNodes(t, dissimilarity);
			}
			try {
				BeanUtils.copyProperties(existedWPNode, t);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			super.saveOrUpdate(existedWPNode);
		} else {
			super.saveOrUpdate(t);
		}
	}
	
	/**
	 * <li>说明：作业节点树
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param parentIDX 上级节点idx主键
	 * @param wpIDX 作业主键
	 * @return List<HashMap<String, Object>> 实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> tree(String parentIDX, String wpIDX) {
		String idx = "";
		if ("ROOT_0".equals(parentIDX)) {
			idx = parentIDX;
		} else {
			WPNode parentObj = this.getModelById(parentIDX); // 获取父类对象
			idx = parentObj.getIdx();
		}
		String hql = "from WPNode where parentWPNodeIDX = ? And wpIDX = ? And recordStatus = "
				+ Constants.NO_DELETE + " order by seqNo";
		List<WPNode> list = (List<WPNode>) this.daoUtils.find(hql, new Object[] { idx, wpIDX });
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		for (WPNode t : list) {
			HashMap<String, Object> nodeMap = new HashMap<String, Object>();
			nodeMap.put("id", t.getIdx());						// 节点idx主键
			nodeMap.put("text", formatDisplayInfo(t)); 			// 树节点显示名称
			nodeMap.put("leaf", t.getIsLeaf() == 0 ? false : true);						// 是否是叶子节点 0:否；1：是
			nodeMap.put("wPNodeName", t.getWpNodeName()); 		// 节点名称
			nodeMap.put("wPNodeDesc", t.getWpNodeDesc()); 		// 节点描述
			nodeMap.put("seqNo", t.getSeqNo());					// 顺序号
			children.add(nodeMap);
		}
		return children;
	}

	/**
	 * <li>在保存叶子节点时，同步更新所有父节点的“工期（分钟）”字段值
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 作业节点实体
	 * @param dissimilarity 工期
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void updateParentWPNodes(WPNode t, double dissimilarity) throws BusinessException, NoSuchFieldException {
		List<WPNode> entityList = new ArrayList<WPNode>();
		this.listParentsWPNodes(t.getParentWPNodeIDX(), entityList);
		
		for (int i = 0; i < entityList.size(); i++) {
			Double ratedPeriod = entityList.get(i).getRatedPeriod();
			if (null == ratedPeriod) {
				ratedPeriod = dissimilarity;
			} else {
				ratedPeriod += dissimilarity;
			}
			entityList.get(i).setRatedPeriod(ratedPeriod);
		}
		
		// 批量保存新增的【作业节点】及其所有的父节点
		super.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：置底
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveBottom(String idx) throws Exception {
		WPNode entity = this.getModelById(idx);
		int count = this.count(entity);
		// 获取被【置底】记录被置底前，在其后的所有记录
		String hql = "From WPNode Where recordStatus = 0 And seqNo > ? And wpIDX = ? And parentWPNodeIDX = ?";
		List<WPNode> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getWpIDX(), entity.getParentWPNodeIDX()});
		// 设置被【置底】记录的排序号为当前记录总数
		entity.setSeqNo(count);
		List<WPNode> entityList = new ArrayList<WPNode>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后依次减一
		for (WPNode recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() - 1);
			entityList.add(recordCard);
		}
		super.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：下移
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	public void updateMoveDown(String idx) throws Exception {
		WPNode entity = this.getModelById(idx);
		String hql = "From WPNode Where recordStatus = 0 And seqNo = ? And wpIDX = ? And parentWPNodeIDX = ?";
		// 获取被【下移】记录被下移前，紧随其后的记录
		WPNode nextEntity = (WPNode)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() + 1, entity.getWpIDX(), entity.getParentWPNodeIDX()});
		List<WPNode> entityList = new ArrayList<WPNode>(2);
		// 设置被【下移】记录的排序号+1
		entity.setSeqNo(entity.getSeqNo() + 1);
		entityList.add(entity);
		// 设置被【下移】记录后的记录的排序号-1
		nextEntity.setSeqNo(nextEntity.getSeqNo() - 1);
		entityList.add(nextEntity);
		super.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：置顶
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateMoveTop(String idx) throws Exception {
		WPNode entity = this.getModelById(idx);
		// 获取被【置顶】记录被置顶前，在其前的所有记录
		String hql = "From WPNode Where recordStatus = 0 And seqNo < ? And wpIDX = ? And parentWPNodeIDX = ?";
		List<WPNode> list = this.daoUtils.find(hql, new Object[]{entity.getSeqNo(), entity.getWpIDX(), entity.getParentWPNodeIDX()});
		// 设置被【置顶】记录的排序号为1
		entity.setSeqNo(1);
		List<WPNode> entityList = new ArrayList<WPNode>();
		entityList.add(entity);
		// 设置其后的所有记录的排序后一次加一
		for (WPNode recordCard : list) {
			recordCard.setSeqNo(recordCard.getSeqNo() + 1);
			entityList.add(recordCard);
		}
		super.saveOrUpdate(entityList);
	}
	
	/**
	 * <li>说明：上移
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param idx 主键
	 * @throws Exception
	 */
	public void updateMoveUp(String idx) throws Exception {
		WPNode entity = this.getModelById(idx);
		String hql = "From WPNode Where recordStatus = 0 And seqNo = ? And wpIDX = ? And parentWPNodeIDX = ?";
		// 获取被【上移】记录被上移移前，紧随其前的记录
		WPNode nextEntity = (WPNode)this.daoUtils.findSingle(hql, new Object[]{entity.getSeqNo() - 1, entity.getWpIDX(), entity.getParentWPNodeIDX()});
		List<WPNode> entityList = new ArrayList<WPNode>(2);
		// 设置被【上移】记录的排序号-1
		entity.setSeqNo(entity.getSeqNo() - 1);
		entityList.add(entity);
		// 设置被【上移】记录前的记录的排序号+1
		nextEntity.setSeqNo(nextEntity.getSeqNo() + 1);
		entityList.add(nextEntity);
		super.saveOrUpdate(entityList);
	}
    
    /**
     * <li>说明：分页查询，因为unix系统下的oracle不支持WM_CONCAT函数，所以单独处理了对前置节点的查询
     * <li>创建人：何涛
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询实体
     * @return Page 
     */
    @Override
    public Page<WPNode> findPageList(SearchEntity<WPNode> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder("From WPNode Where recordStatus = 0");
        WPNode entity = searchEntity.getEntity();
        // 查询条件 - 流程主键
        sb.append(" And wpIDX = '").append(entity.getWpIDX()).append("'");
        // 查询条件 - 上级主键
        sb.append(" And parentWPNodeIDX = '").append(entity.getParentWPNodeIDX()).append("'");
        // 排序
        if (null != searchEntity.getOrders()) {
            String order = HqlUtil.getOrderHql(searchEntity.getOrders());
            if (!order.contains("preWPNodeSeqNo")) {
                sb.append(order);
            }
        } else {
            sb.append(" Order By seqNo ASC");
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<WPNode> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<WPNode> list = page.getList();
        if (null != list || list.size() > 0) {
            for (WPNode wpNode : list) {
                wpNode.setPreWPNodeSeqNo(this.wPNodeSeqManager.getPreNodeSeqNo(wpNode.getIdx()));
            }
        }
        return page;
    }
	
}