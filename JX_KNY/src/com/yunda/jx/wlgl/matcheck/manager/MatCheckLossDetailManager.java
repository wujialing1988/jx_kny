package com.yunda.jx.wlgl.matcheck.manager;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.matcheck.entity.MatCheckLossDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatCheckLossDetail业务类,物料损耗明细
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matCheckLossDetailManager")
public class MatCheckLossDetailManager extends JXBaseManager<MatCheckLossDetail, MatCheckLossDetail>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@Override
	public String[] validateDelete(Serializable... ids) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(MatCheckLossDetail entity) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @return 
	 */
	public Page<MatCheckLossDetail> findPageList(SearchEntity<MatCheckLossDetail> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatCheckLossDetail Where recordStatus = 0");
		MatCheckLossDetail entity = searchEntity.getEntity();
		// 查询条件 - 损耗单主键
		if (!StringUtil.isNullOrBlank(entity.getMatCheckLossIDX())) {
			sb.append(" And matCheckLossIDX = '").append(entity.getMatCheckLossIDX()).append("'");
		}
		// 排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append(orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append(orders[i].toString());
			}
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：根据损耗单主键查询损耗明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matCheckLossIDX 损耗单主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MatCheckLossDetail> getModelsByMatCheckLossIDX(String matCheckLossIDX) {
		String hql = "From MatCheckLossDetail Where recordStatus = 0 And matCheckLossIDX = ?";
		return this.daoUtils.find(hql, new Object[]{ matCheckLossIDX });
	}
	
}