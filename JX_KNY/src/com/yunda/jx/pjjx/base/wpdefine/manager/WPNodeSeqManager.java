package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeSeq;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPNodeSeq业务类,节点前后置关系
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wPNodeSeqManager")
public class WPNodeSeqManager extends JXBaseManager<WPNodeSeq, WPNodeSeq>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** WPNode业务类,作业节点 */
	@Resource
	private WPNodeManager wPNodeManager;
	
	/**
	 * <li>说明：唯一性验证，一个作业节点不能添加多个重复的前置作业节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-14-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(WPNodeSeq entity) throws BusinessException {
		List<WPNodeSeq> models = getModelsByWPNodeIDX(entity.getWpNodeIDX());
		for(WPNodeSeq t: models) {
			if (t.getIdx().equals(entity.getIdx()) && t.getPreWPNodeIDX().equals(entity.getPreWPNodeIDX())) {
				return null;
			}
			if (t.getPreWPNodeIDX().equals(entity.getPreWPNodeIDX())) {
				String preWPNodeName = wPNodeManager.getModelById(t.getPreWPNodeIDX()).getWpNodeName();
				String wpNodeName = wPNodeManager.getModelById(t.getWpNodeIDX()).getWpNodeName();
				return new String[]{"作业节点：" + wpNodeName + "已经配置了前置节点[" + preWPNodeName + "]，不能重复添加！"};
			}
		}
		
		// 验证是否有循环依赖（即：如有作业节点A、B、C，C的前置节点有A和B，则A或者B的前置节点不能有C）
		// 获取所有已该节点设置了前置节点的节点信息
		List<WPNode> list = new ArrayList<WPNode>();
		this.wPNodeManager.listAfterNodes(entity.getWpNodeIDX(), list);
		if (null != list && 0 < list.size()) {
			for (WPNode wpNode : list) {
				if (wpNode.getIdx().equals(entity.getPreWPNodeIDX())) {
					return new String[]{"前置节点设置存在循环依赖，请检查！"};
				}
			}
		}
		
		// 验证是否有重复的前置节点关系（即：如有作业节点A、B、C，B的前置节点为A，C的前置节点为B，则C不能设置前置节点为A）
		list = new ArrayList<WPNode>();
		this.wPNodeManager.listBeforeNodes(entity.getWpNodeIDX(), list);
		if (null != list && 0 < list.size()) {
			for (WPNode wpNode : list) {
				if (wpNode.getIdx().equals(entity.getPreWPNodeIDX())) {
					return new String[]{"不能设置重叠的前置节点关系，请检查！"};
				}
                // 验证新增的节点是否是前置节点的后置节点（即：如有作业节点A、B、C，B、C的前置节点均为A，则C(B)不能设置前置节点为B(C)）
                List<WPNode> tempList = new ArrayList<WPNode>();
                this.wPNodeManager.listAfterNodes(wpNode.getIdx(), tempList);
                for (WPNode node : tempList) {
                    if (node.getIdx().equals(entity.getPreWPNodeIDX())) {
                        return new String[]{"前置节点设置存在循环依赖，请检查！"};
                    }
                }
			}
		}
		
		return super.validateUpdate(entity);
	}
	
	/**
	 * <li>说明：分页查询，因为数据表只存储了前置节点的主键，为了在节目显示节点名称，所以对前置节点的名称进行联合查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询对象实体
	 * @return Page<WPNodeSeq> 实体集合
	 */
	@Override
	public Page<WPNodeSeq> findPageList(SearchEntity<WPNodeSeq> searchEntity) throws BusinessException {
		StringBuilder sb =  new StringBuilder();
		sb.append("Select new WPNodeSeq(a.idx, a.wpNodeIDX, a.preWPNodeIDX, b.wpNodeName, a.seqClass, a.beforeDelayTime) From WPNodeSeq a, WPNode b Where a.preWPNodeIDX = b.idx And a.recordStatus = 0 And b.recordStatus = 0");
		WPNodeSeq entity = searchEntity.getEntity();
		// 查询条件 - 节点主键
		if (!StringUtil.isNullOrBlank(entity.getWpNodeIDX())) {
			sb.append(" And a.wpNodeIDX = '").append(entity.getWpNodeIDX()).append("'");
		}
		if (null != searchEntity.getOrders()) {
			Order order = searchEntity.getOrders()[0];
			if (order.toString().contains("preWPNodeName")) {
				sb.append(" Order By ").append(order.toString().replace("preWPNodeName", "b.wpNodeName"));
			} else {
				sb.append(" Order By ").append("a." + order);
			}
		}
		String hql = sb.toString();
		String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
		return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：根据“节点主键”获取【节点前后置关系】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpNodeIDX 节点主键
	 * @return List<WPNodeSeq>实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNodeSeq> getModelsByWPNodeIDX(String wpNodeIDX) {
		String hql = "From WPNodeSeq Where recordStatus = 0 And wpNodeIDX = ? ";
		return this.daoUtils.find(hql, new Object[]{wpNodeIDX});
	}

	/**
	 * <li>说明：根据“节点主键”获取其所有的【节点前后置关系】
	 * <li>创建人：程锐
	 * <li>创建日期：2016-6-1
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：	 * 
	 * @param wpNodeIDX 节点主键
	 * @return List<WPNodeSeq>实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNodeSeq> getAllModelsByWPNodeIDX(String wpNodeIDX) {
		String hql = "From WPNodeSeq Where recordStatus = 0 And (wpNodeIDX = ? or preWPNodeIDX = ?)";
		return this.daoUtils.find(hql, new Object[]{wpNodeIDX, wpNodeIDX});
	}
	
	/**
	 * <li>说明：根据“节点主键”和“前置节点主键”获取【节点前后置关系】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wPNodeIDX 节点主键
	 * @param preWPNodeIDX 前置节点主键
	 * @return 节点前后置关系实体对象
	 */
	public WPNodeSeq getModel(String wPNodeIDX, String preWPNodeIDX) {
		String hql = "From WPNodeSeq Where recordStatus = 0 And wpNodeIDX = ? And preWPNodeIDX =?";
		return (WPNodeSeq)this.daoUtils.findSingle(hql, new Object[]{wPNodeIDX, preWPNodeIDX});
	}
    
    /**
     * <li>说明：获取节点的前置节点顺序号
     * <li>创建人：何涛
     * <li>创建日期：2015-6-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点主键
     * @return 节点的前置节点顺序号，例如（1,2）
     */
    @SuppressWarnings("unchecked")
    public String getPreNodeSeqNo(String nodeIDX) {
        StringBuilder sb = new StringBuilder("select a.seq_no from pjjx_wp_node a, pjjx_wp_node_seq b where a.record_status = 0 and b.record_status = 0 and a.idx = b.pre_wp_node_idx");
        sb.append(" and b.wp_node_idx = '").append(nodeIDX).append("' order by a.seq_no");
        List<BigDecimal> list = this.daoUtils.executeSqlQuery(sb.toString());
        if (null == list || list.size() <= 0) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (BigDecimal seqNo : list) {
            sBuilder.append(",").append(seqNo.intValue());
        }
        return sBuilder.substring(1);
    }
	
}