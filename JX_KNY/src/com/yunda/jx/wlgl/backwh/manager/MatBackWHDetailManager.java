/**
 * 
 */
package com.yunda.jx.wlgl.backwh.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.backwh.entity.MatBackWHDetail;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackWHDetail业务类,退库单明细
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-08 上午10:31:52
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matBackWHDetailManager")
public class MatBackWHDetailManager extends JXBaseManager<MatBackWHDetail, MatBackWHDetail> {
	
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
	public String[] validateDelete(Serializable... ids) {
		// TODO Auto-generated method stub
		return super.validateDelete(ids);
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
	public String[] validateUpdate(MatBackWHDetail t) {
		// TODO Auto-generated method stub
		return super.validateUpdate(t);
	}
	
	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @return 
	 */
	public Page<MatBackWHDetail> findPageList(SearchEntity<MatBackWHDetail> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatBackWHDetail Where recordStatus = 0");
		MatBackWHDetail entity = searchEntity.getEntity();
		if (!StringUtil.isNullOrBlank(entity.getMatBackWhIdx())) {
			sb.append(" And matBackWhIdx = '").append(entity.getMatBackWhIdx()).append("'");
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
	 * <li>说明：根据退库单主键查询退库单明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matBackWhIdx 退库单主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MatBackWHDetail> getModelsByMatBackWhIdx (String matBackWhIdx) {
		String hql = "From MatBackWHDetail Where recordStatus = 0 And matBackWhIdx = ?";
		return this.daoUtils.find(hql, new Object[]{ matBackWhIdx });
	}
	
}
