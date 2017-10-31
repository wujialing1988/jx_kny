/**
 * 
 */
package com.yunda.jx.wlgl.aboard.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.aboard.entity.MatAboardTrainAccount;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatAboardTrainAccount业务类, 大型消耗配件上车记录
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-9 上午11:45:37
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matAboardTrainAccountManager")
public class MatAboardTrainAccountManager extends JXBaseManager<MatAboardTrainAccount, MatAboardTrainAccount> {
	
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
	 * <li>说明：对编号字段的唯一性进行验证
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-10-09
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public String[] validateUpdate(MatAboardTrainAccount entity) {
		String matNo = entity.getMatNo();
		StringBuilder sb = new StringBuilder();
		sb.append("From MatAboardTrainAccount Where recordStatus = ").append(Constants.NO_DELETE);
		List<MatAboardTrainAccount> list = this.daoUtils.find(sb.toString());
		for (MatAboardTrainAccount account : list) {
			if (entity.getIdx().equals(account.getIdx())) {
				continue;
			}
			// 验证【功能编码】字段的唯一性
			if (matNo.equals(account.getMatNo())) {
				return new String[]{"编号[" + entity.getMatNo() + "]已经存在，请重新输入！"};
			}
		}
		return null;
	}
	
	/**
	 * <li>说明：物料退库 - 批量添加 - 分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体对象
	 * @return 
	 */		
	@Override
	public Page<MatAboardTrainAccount> findPageList(SearchEntity<MatAboardTrainAccount> searchEntity) {
		MatAboardTrainAccount entity = searchEntity.getEntity();
		StringBuilder sb = new StringBuilder();
		sb.append("From MatAboardTrainAccount Where recordStatus = 0");
		// 查询条件 - 车型
		if (!StringUtil.isNullOrBlank(entity.getTrainTypeIDX())) {
			sb.append(" And trainTypeIDX = '").append(entity.getTrainTypeIDX()).append("'");
		}
		// 查询条件 - 车号
		if (!StringUtil.isNullOrBlank(entity.getTrainNo())) {
			sb.append(" And trainNo = '").append(entity.getTrainNo()).append("'");
		}
		// 查询条件 - 修程
		if (!StringUtil.isNullOrBlank(entity.getXcId())) {
			sb.append(" And xcId = '").append(entity.getXcId()).append("'");
		}
		// 查询条件 - 修次
		if (!StringUtil.isNullOrBlank(entity.getRtId())) {
			sb.append(" And rtId = '").append(entity.getRtId()).append("'");
		}
		// 查询条件 - 领用人
		if (!StringUtil.isNullOrBlank(entity.getGetEmp())) {
			sb.append(" And getEmp Like'%").append(entity.getGetEmp()).append("%'");
		}
		// 查询条件 - 物料编号
		if (!StringUtil.isNullOrBlank(entity.getMatNo())) {
			sb.append(" And matNo Like'%").append(entity.getMatNo()).append("%'");
		}
		// 查询条件 - 物料类型
		if (!StringUtil.isNullOrBlank(entity.getMatClass())) {
			sb.append(" And matClass Like'%").append(entity.getMatClass()).append("%'");
		}
		SqlFilter.filterDate(sb, "aboardDate", entity.getStartDate(), entity.getEndDate());
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

}
