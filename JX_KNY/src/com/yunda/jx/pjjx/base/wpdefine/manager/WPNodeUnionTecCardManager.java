package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNodeUnionTecCard;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPNodeUnionTecCard业务类,作业节点所挂工艺卡
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wPNodeUnionTecCardManager")
public class WPNodeUnionTecCardManager extends JXBaseManager<WPNodeUnionTecCard, WPNodeUnionTecCard>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：逻辑删除记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 作业节点所挂工艺卡主键
	 * @param wPNodeIDX 作业流程主键
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public void logicDelete(String[] ids, String wPNodeIDX) throws BusinessException, NoSuchFieldException {
		StringBuilder sb = new StringBuilder("From WPNodeUnionTecCard Where recordStatus = 0 And wPNodeIDX = ?");
		StringBuilder temp = new StringBuilder();
		for (String idx : ids) {
			temp.append(",").append("'").append(idx).append("'");
		}
		sb.append(" And tecCardIDX In (").append(temp.substring(1)).append(")");
		List<WPNodeUnionTecCard> entityList = this.daoUtils.find(sb.toString(), new Object[]{wPNodeIDX});
		for (WPNodeUnionTecCard tecCards : entityList) {
			tecCards = EntityUtil.setSysinfo(tecCards);
//			设置逻辑删除字段状态为已删除
			tecCards = EntityUtil.setDeleted(tecCards);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}
	
	/**
	 * <li>说明：批量保存实体时的验证方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tecCards 作业节点所挂工艺卡实体数组
	 * @return String[] 验证消息
	 */
	public String[] validateUpdate(WPNodeUnionTecCard[] tecCards) {
		for (WPNodeUnionTecCard entity : tecCards) {
			String[] errorMsg = this.validateUpdate(entity);
			if (null == errorMsg) {
				continue;
			}
			return errorMsg;
		}
		return null;
	}
	
	/**
	 * <li>说明：批量保存新增的【作业节点所挂工艺卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param tecCards 作业节点所挂工艺卡实体数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(WPNodeUnionTecCard[] tecCards) throws BusinessException, NoSuchFieldException {
		for (WPNodeUnionTecCard entity : tecCards) {
			this.saveOrUpdate(entity);
		}
	}
	
	/**
	 * <li>说明：根据“节点主键”获取【作业节点所挂工艺卡】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wPNodeIDX 节点主键
	 * @return List<WPNodeUnionTecCard> 作业节点所挂工艺卡实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPNodeUnionTecCard> getModelsByWPNodeIDX(String wPNodeIDX) {
		String hql = "From WPNodeUnionTecCard Where recordStatus = 0 And wPNodeIDX = ?";
		return this.daoUtils.find(hql, new Object[]{wPNodeIDX});
	}
	
}