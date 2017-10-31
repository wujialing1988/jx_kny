package com.yunda.jx.pjjx.base.wpdefine.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPUnionTec;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPUnionTec业务类,作业流程所用工艺
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wPUnionTecManager")
public class WPUnionTecManager extends JXBaseManager<WPUnionTec, WPUnionTec>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：逻辑删除记录
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 配件型号主键
	 * @param wPIDX 作业流程主键
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public void logicDelete(String[] ids, String wPIDX) throws BusinessException, NoSuchFieldException {
		StringBuilder sb = new StringBuilder("From WPUnionTec Where recordStatus = 0 And wPIDX = ?");
		StringBuilder temp = new StringBuilder();
		for (String idx : ids) {
			temp.append(",").append("'").append(idx).append("'");
		}
		sb.append(" And tecIDX In (").append(temp.substring(1)).append(")");
		List<WPUnionTec> entityList = this.daoUtils.find(sb.toString(), new Object[]{wPIDX});
		for (WPUnionTec wpUnionTecs : entityList) {
			wpUnionTecs = EntityUtil.setSysinfo(wpUnionTecs);
//			设置逻辑删除字段状态为已删除
			wpUnionTecs = EntityUtil.setDeleted(wpUnionTecs);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}

	/**
	 * <li>说明：批量保存实体时的验证方法
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpUnionTecs 作业流程所用工艺实体数组
	 * @return String[] 验证消息
	 */
	public String[] validateUpdate(WPUnionTec[] wpUnionTecs) {
		for (WPUnionTec entity : wpUnionTecs) {
			String[] errorMsg = this.validateUpdate(entity);
			if (null == errorMsg) {
				continue;
			}
			return errorMsg;
		}
		return null;
	}
	
	/**
	 * <li>说明：批量保存新增的【作业流程所用工艺】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wpUnionTecs 作业流程所用工艺实体数组
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveOrUpdate(WPUnionTec[] wpUnionTecs) throws BusinessException, NoSuchFieldException {
		for (WPUnionTec entity : wpUnionTecs) {
			this.saveOrUpdate(entity);
		}
	}
	
	/**
	 * <li>说明：根据“作业流程主键”获取【作业流程所用工艺】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param wPIDX 作业流程主键
	 * @return List<WPUnionTec> 作业流程所用工艺实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<WPUnionTec> getModelsByWPIDX(String wPIDX) {
		String hql = "From WPUnionTec Where recordStatus = 0 And wPIDX = ?";
		return this.daoUtils.find(hql, new Object[]{wPIDX});
	}
	
}