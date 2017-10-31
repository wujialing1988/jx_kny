package com.yunda.jx.wlgl.planmanage.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.IBillStatus;
import com.yunda.jx.wlgl.planmanage.entity.MatPlan;
import com.yunda.jx.wlgl.planmanage.entity.MatPlanDetail;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatPlan业务类,用料计划单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matPlanManager")
public class MatPlanManager extends JXBaseManager<MatPlan, MatPlan>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** MatPlanDetail业务类,用料计划明细 */
	@Resource
	MatPlanDetailManager matPlanDetailManager;
	
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
		for (int i = 0; i < ids.length; i++) {
			MatPlan entity = this.getModelById(ids[i]);
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的用料计划单已经提交，请刷新页面后重试" };
			}
		}
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
	public String[] validateUpdate(MatPlan entity) throws BusinessException {
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
	 * @param searchEntity 查询实体对象
	 * @return
	 */
	public Page<MatPlan> findPageList(SearchEntity<MatPlan> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatPlan Where recordStatus = ").append(Constants.NO_DELETE);
		MatPlan entity = searchEntity.getEntity();
		// 查询条件 - 使用班组
		if (!StringUtil.isNullOrBlank(entity.getUseTeam())) {
			sb.append(" And useTeam = '").append(entity.getUseTeam()).append("'");
		}
		// 查询条件 - 申请人
		if (!StringUtil.isNullOrBlank(entity.getApplyEmp())) {
			sb.append(" And applyEmp = '").append(entity.getApplyEmp()).append("'");
		}
		// 查询条件 - 状态
		if (!IBillStatus.CONST_STR_STATUS_SY.equalsIgnoreCase(entity.getStatus()) && !StringUtil.isNullOrBlank(entity.getStatus())) {
			sb.append(" And status = '").append(entity.getStatus()).append("'");
		}
		// 查询条件 - 申请日期
		SqlFilter.filterDate(sb, "applyDate", entity.getStartDate(), entity.getEndDate());
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
			// sb.append(" Order By updateTime");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：保存用料计划单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matPlan  MatPlan实体类, 数据表：用料计划单
	 * @param detailList MatPlan实体类数组, 数据表：用料计划单明细
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] savePlanAndDetail(MatPlan matPlan, MatPlanDetail[] detailList) throws IllegalAccessException,
			InvocationTargetException, BusinessException, NoSuchFieldException {
		// 设置用料计划单单据摘要信息
		if (null != matPlan.getIdx()) {
			MatPlan entity = this.getModelById(matPlan.getIdx());
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的用料计划单已经提交，请刷新页面后重试" };
			}
			BeanUtils.copyProperties(entity, matPlan);
			this.saveOrUpdate(entity);
		} else {
			this.saveOrUpdate(matPlan);
		}
		String idx = matPlan.getIdx();
		List<MatPlanDetail> entityList = new ArrayList<MatPlanDetail>();
		// 根据用料计划单主键获得该用料计划单暂存的用料计划单明细
		List<MatPlanDetail> oList = this.matPlanDetailManager.getModelsByMatPlanIdx(idx);
		// 检验已暂存的用料计划单名称是否被删除，如果页面进行了删除，则在后台同步更新
		for (MatPlanDetail oDetail : oList) {
			if (isDeleted(oDetail, detailList)) {
				oDetail = EntityUtil.setSysinfo(oDetail);
				oDetail = EntityUtil.setDeleted(oDetail);
				entityList.add(oDetail);
			}
		}
		for (MatPlanDetail detail : detailList) {
			MatPlanDetail entity = null;
			if (null != detail.getIdx()) {
				entity = this.matPlanDetailManager.getModelById(detail.getIdx());
				if (entity.getQty() != detail.getQty()) {
					entity.setQty(detail.getQty());
				}
			} else {
				entity = detail;
				if (null == entity.getMatPlanIdx()) {
					entity.setMatPlanIdx(idx);
				}
			}
			entity = EntityUtil.setSysinfo(entity);
			//设置逻辑删除字段状态为未删除
			entity = EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		return null;
	}
	
	/**
	 * <li>说明：检验数据库中已存储的用料计划单明细，是否已经在页面进行了删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param oDetail
	 * @param detailList
	 * @return
	 */
	private boolean isDeleted(MatPlanDetail oDetail, MatPlanDetail[] detailList) {
		for (MatPlanDetail detail : detailList) {
			if (oDetail.getIdx().equals(detail.getIdx())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：重写删除方法，对用料计划单明细进行级联删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 *  @param ids 用料计划单idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除用料计划单明细
		for (int i = 0; i < ids.length; i++) {
			List<MatPlanDetail> list = this.matPlanDetailManager.getModelsByMatPlanIdx((String)ids[i]);
			if (null != list && list.size() > 0) {
				this.matPlanDetailManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
}