/**
 * 
 */
package com.yunda.jx.wlgl.backwh.manager;

import java.io.Serializable;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.backwh.entity.MatBackWHSelect;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackWHSelect业务类,出库物料选择
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-8 上午10:31:52
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matBackWHSelectManager")
public class MatBackWHSelectManager extends JXBaseManager<MatBackWHSelect, MatBackWHSelect> {
	
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
	public String[] validateUpdate(MatBackWHSelect t) {
		// TODO Auto-generated method stub
		return super.validateUpdate(t);
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
	public Page<MatBackWHSelect> findPageList(SearchEntity<MatBackWHSelect> searchEntity) {
		MatBackWHSelect entity = searchEntity.getEntity();
		StringBuilder sb = new StringBuilder();
		sb.append("From MatBackWHSelect Where 0 = 0");
		// 查询条件 - 出库单编号
		if (!StringUtil.isNullOrBlank(entity.getBillNo())) {
			sb.append(" And billNo Like '%").append(entity.getBillNo()).append("%'");
		}
		// 查询条件 - 出库单摘要
		if (!StringUtil.isNullOrBlank(entity.getBillSummary())) {
			sb.append(" And billSummary LIke '%").append(entity.getBillSummary()).append("%'");
		}
		// 查询条件 - 退库库房idx主键
		if (!StringUtil.isNullOrBlank(entity.getWhIdx())) {
			sb.append(" And whIdx ='").append(entity.getWhIdx()).append("'");
		}
		// 查询条件 - 领用机构id
		if (!StringUtil.isNullOrBlank(entity.getGetOrgId())) {
			sb.append(" And getOrgId ='").append(entity.getGetOrgId()).append("'");
		}
		// 查询条件 - 物料编码
		if (!StringUtil.isNullOrBlank(entity.getMatCode())) {
			sb.append(" And matCode Like'%").append(entity.getMatCode()).append("%'");
		}
		// 查询条件 - 物料描述
		if (!StringUtil.isNullOrBlank(entity.getMatDesc())) {
			sb.append(" And matDesc Like'%").append(entity.getMatDesc()).append("%'");
		}
		// 查询条件 - 领用人
		if (!StringUtil.isNullOrBlank(entity.getGetEmp())) {
			sb.append(" And getEmp Like'%").append(entity.getGetEmp()).append("%'");
		}
		// 查询条件 - 出库用途
		if (!StringUtil.isNullOrBlank(entity.getPurpose())) {
			sb.append(" And purpose Like'%").append(entity.getPurpose()).append("%'");
		}
		SqlFilter.filterDate(sb, "getDate", entity.getStartDate(), entity.getEndDate());
		// 排序字段
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			sb.append(" Order By ");
			sb.append(orders[0].toString());
			for (int i = 1; i < orders.length; i++) {
				sb.append(", ");
				sb.append(orders[i].toString());
			}
		} else {
			sb.append(" Order By billNo");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}

}
