package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeUnionEquipCard;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPNodeUnionEquipCard业务类,作业节点所挂机务设备工单
 * <li>创建人：程梅
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wPNodeUnionEquipCardManager")
public class WPNodeUnionEquipCardManager extends JXBaseManager<WPNodeUnionEquipCard, WPNodeUnionEquipCard>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据“节点主键”获取【作业节点所挂机务设备工单】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人：何涛
	 * <li>修改日期：2015-02-10
	 * <li>修改内容：修改方法声明，设备工单不与作业节点做关联，因为此次应该是获取作业上所属的设备工单
	 * 
	 * @param wPIDX 作业流程主键
	 * @return List<WPNodeUnionEquipCard> 机务设备工单实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNodeUnionEquipCard> getModelsByWPIDX(String wPIDX) {
		String hql = "From WPNodeUnionEquipCard Where recordStatus = 0 And wPIDX = ?";
		return this.daoUtils.find(hql, new Object[]{wPIDX});
	}
	
	/**
	 * 
	 * <li>说明：查询需求单对应机务设备作业工单列表信息
	 * <li>创建人：程梅
	 * <li>创建日期：2015-1-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wPIDX 作业主键
	 * @param start 开始索引
	 * @param limit 记录条数
	 * @return Page<WPNodeUnionEquipCard> 实体集合
	 * @throws BusinessException
	 */
	public Page<WPNodeUnionEquipCard> findPageQuery(String wPIDX,Integer start, Integer limit) throws BusinessException {
		StringBuilder sb = new StringBuilder();
		sb.append("Select new WPNodeUnionEquipCard(c.idx,e.equipCardNo,e.deviceTypeName,e.equipCardDesc) ");
		sb.append(" From EquipCard e,WPNodeUnionEquipCard c Where e.recordStatus=0 And c.recordStatus=0 And c.equipCardIDX=e.idx");
		sb.append(" and c.wPIDX='").append(wPIDX).append("' ");
		String hql = sb.toString();
		String totalHql = "select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return super.findPageList(totalHql, hql, start, limit);
	}
    
	/**
	 * <li>说明：批量保存实体时的验证方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param cards 设备工单实体数组
	 * @return String[] 验证消息
	 */
	public String[] validateUpdate(WPNodeUnionEquipCard[] cards) {
		for (WPNodeUnionEquipCard entity : cards) {
			String[] errorMsg = this.validateUpdate(entity);
			if (null == errorMsg) {
				continue;
			}
			return errorMsg;
		}
		return null;
	}
    
	/**
	 * <li>说明：批量保存新增的【作业流程所用设备作业工单】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param cards 设备工单实体数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(WPNodeUnionEquipCard[] cards) throws BusinessException, NoSuchFieldException {
		for (WPNodeUnionEquipCard entity : cards) {
			this.saveOrUpdate(entity);
		}
	}
}