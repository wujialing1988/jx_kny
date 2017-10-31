package com.yunda.jx.wlgl.instock.manager;

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
import com.yunda.jx.wlgl.instock.entity.MatInWhDetail;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatInWhDetail业务类,物料入库明细
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matInWhDetailManager")
public class MatInWhDetailManager extends JXBaseManager<MatInWhDetail, MatInWhDetail>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-09-12
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
	 * <li>创建日期：2014-09-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(MatInWhDetail entity) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-16
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 
	 */
	public Page<MatInWhDetail> findPageList(SearchEntity<MatInWhDetail> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatInWhDetail Where recordStatus = 0");
		MatInWhDetail entity = searchEntity.getEntity();
		if (!StringUtil.isNullOrBlank(entity.getMatInWhIdx())) {
			sb.append(" And matInWhIdx = '").append(entity.getMatInWhIdx()).append("'");
		}
//		 排序字段
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
	 * <li>说明：根据入库单主键查询入库单明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-25
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matInWhIdx 入库单主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MatInWhDetail> getModelsByMatInWhIdx(String matInWhIdx) {
		String hql = "From MatInWhDetail Where recordStatus = 0 And matInWhIdx = ?";
		return this.daoUtils.find(hql, new Object[]{ matInWhIdx });
	}
	
}