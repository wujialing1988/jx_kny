package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WP;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WP业务类,检修作业流程
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wPManager")
public class WPManager extends JXBaseManager<WP, WP>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** WPNode业务类,作业节点 */
	@Resource
	WPNodeManager wPNodeManager;
	
	/** WPUnionParts业务类,作业流程适用配件 */
	@Resource
	WPUnionPartsManager wPUnionPartsManager;
	
	/** WPUnionRecord业务类,作业流程所用记录单 */
	@Resource
	WPUnionRecordManager wPUnionRecordManager;
	
	/** WPUnionTec业务类,作业流程所用工艺 */
	@Resource
	WPUnionTecManager wPUnionTecManager;
    
	/** WPNodeUnionEquipCard业务类,作业节点所挂机务设备工单 */
	@Resource
    WPNodeUnionEquipCardManager wPNodeUnionEquipCardManager;
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(WP t) throws BusinessException {
		List<WP> list = this.daoUtils.find("From WP Where recordStatus = 0");
		if (null == list || list.size() <= 0) {
			return null;
		}
		for (WP entity : list) {
			if (entity.getIdx().equals(t.getIdx())) {
				continue;
			}
			if (t.getWPNo().equals(entity.getWPNo())) {
				return new String[]{"检修需求单编号：" + t.getWPNo() + "已经存在，不能重复添加！"};
			}
		}
		return null;
	}
	
	/**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2014-11-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param searchEntity 查询实体对象
	 * @return Page<WP> 实体集合
	 */
	@Override
	public Page<WP> findPageList(SearchEntity<WP> searchEntity) throws BusinessException {
		StringBuilder sb = new StringBuilder();
		sb.append("From WP Where recordStatus = ").append(Constants.NO_DELETE);
		WP entity = searchEntity.getEntity();
		// 查询条件 - 编号
		if (!StringUtil.isNullOrBlank(entity.getWPNo())) {
			sb.append(" And wPNo Like '%").append(entity.getWPNo()).append("%'");
		}
		// 查询条件 - 描述
		if (!StringUtil.isNullOrBlank(entity.getWPDesc())) {
			sb.append(" And wPDesc = '").append(entity.getWPDesc()).append("'");
		}
		// 查询条件 - 额定工期
		this.filterRatedPeriod(sb, "ratedPeriod", entity.getRatedPeriodFrom(), entity.getRatedPeriodTo());
		// 排序字段
		sb.append(HqlUtil.getOrderHql(searchEntity.getOrders()));
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
     * <li>说明：对额定工期进行过滤
     * <li>创建人：何涛
     * <li>创建日期：2014-11-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * <li>@param sb 形参StringBuilder
	 * <li>@param columnName 数据表日期字段名称
	 * @param from 下限
	 * @param to 上限
	 */
	private void filterRatedPeriod(StringBuilder sb, String columnName, Long from, Long to) {
		if (null != from && null != to) {
			sb.append(Constants.AND + columnName + " >= " + from);
			sb.append(Constants.AND + columnName + " <= " + to);
		} else if (null != from && null == to) {
			sb.append(Constants.AND + columnName + " >= "+ from);
		} else if (null == from && null != to) {
			sb.append(Constants.AND + columnName + " <= "+ to);
		}
	}
	
	/**
	 * <li>说明：级联删除【作业流程适用配件】、【作业流程所用工艺】、【作业流程所用记录单】、【作业节点】
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
		for (int i = 0; i < ids.length; i++) {
			// 级联删除【作业流程适用配件】、【作业流程所用工艺】、【作业流程所用记录单】、【作业节点】
			cascadeDelete((String)ids[0]);
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明：级联删除【作业流程适用配件】、【作业流程所用工艺】、【作业流程所用记录单】、【作业节点】、【作业节点所挂机务设备工单】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-14
	 * <li>修改人： 何涛
	 * <li>修改日期：2015-02-10
	 * <li>修改内容：级联删除【作业节点所挂机务设备工单】
	 * 
	 * @param wPIDX 作业流程主键
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	private void cascadeDelete(String wPIDX) throws NoSuchFieldException {
		// 级联删除【作业流程适用配件】
		List list = this.wPUnionPartsManager.getModelsByWPIDX(wPIDX);
		if (null != list && 0 < list.size()) {
			this.wPUnionPartsManager.logicDelete(list);
		}
		// 级联删除【作业流程所用工艺】
		list = this.wPUnionTecManager.getModelsByWPIDX(wPIDX);
		if (null != list && 0 < list.size()) {
			this.wPUnionTecManager.logicDelete(list);
		}
		// 级联删除【作业流程所用记录单】
		list = this.wPUnionRecordManager.getModelsByWPIDX(wPIDX);
		if (null != list && 0 < list.size()) {
			this.wPUnionRecordManager.logicDelete(list);
		}
		// 级联删除【作业所挂机务设备工单】
		list = this.wPNodeUnionEquipCardManager.getModelsByWPIDX(wPIDX);
		if (null != list && 0 < list.size()) {
		    this.wPNodeUnionEquipCardManager.logicDelete(list);
		}
		// 级联删除【作业节点】
		list = this.wPNodeManager.getModelsByWPIDX(wPIDX);
		if (null != list && 0 < list.size()) {
			this.wPNodeManager.logicDelete(list);
		}
	}
    
    /**
     * <li>说明：查询某配件规格型号对应的检修作业流程列表
     * <li>创建人：程梅
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsTypeIdx 配件型号主键
     * @param start 分页开始
     * @param limit 分页大小
     * @param orders 排序对象
     * @return Page分页列表
     * @throws Exception
     */
    public Page<WP> getListForPartsRdp(String partsTypeIdx, int start, int limit, Order[] orders) throws Exception {   
        if (StringUtil.isNullOrBlank(partsTypeIdx)) {
            throw new NullPointerException("配件型号主键不能为空！");
        }
        StringBuffer sb = new StringBuffer("SELECT T.* FROM PJJX_WP T WHERE T.RECORD_STATUS=0 AND T.IDX IN (SELECT WP_IDX FROM PJJX_WP_UNION_PARTS WHERE RECORD_STATUS = 0");
        sb.append(" AND PARTS_TYPE_IDX = '").append(partsTypeIdx).append("')");
        String totalSql = "SELECT COUNT(1) " + sb.substring(sb.indexOf("FROM"));
        return findPageList(totalSql, sb.toString(), start, limit , null ,orders);
    }
    
}