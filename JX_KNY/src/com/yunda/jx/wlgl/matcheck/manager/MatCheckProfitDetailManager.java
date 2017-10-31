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
import com.yunda.jx.wlgl.matcheck.entity.MatCheckProfitDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatCheckProfitDetail业务类,物料盘盈明细
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matCheckProfitDetailManager")
public class MatCheckProfitDetailManager extends JXBaseManager<MatCheckProfitDetail, MatCheckProfitDetail>{
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
	public String[] validateUpdate(MatCheckProfitDetail entity) throws BusinessException {
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
	public Page<MatCheckProfitDetail> findPageList(SearchEntity<MatCheckProfitDetail> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatCheckProfitDetail Where recordStatus = 0");
		MatCheckProfitDetail entity = searchEntity.getEntity();
		// 查询条件 - 盘盈单主键
		if (!StringUtil.isNullOrBlank(entity.getMatCheckProfitIDX())) {
			sb.append(" And matCheckProfitIDX = '").append(entity.getMatCheckProfitIDX()).append("'");
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
	 * <li>说明：根据盘盈单主键查询盘盈明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matCheckProfitIDX 盘盈单主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MatCheckProfitDetail> getModelsByMatCheckProfitIDX(String matCheckProfitIDX) {
		String hql = "From MatCheckProfitDetail Where recordStatus = 0 And matCheckProfitIDX = ?";
		return this.daoUtils.find(hql, new Object[]{ matCheckProfitIDX });
	}
	
}