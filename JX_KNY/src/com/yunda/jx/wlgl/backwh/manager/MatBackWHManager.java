/**
 * 
 */
package com.yunda.jx.wlgl.backwh.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.IBillStatus;
import com.yunda.jx.wlgl.backwh.entity.MatBackWH;
import com.yunda.jx.wlgl.backwh.entity.MatBackWHDetail;
import com.yunda.jx.wlgl.outstock.manager.MatOutWHNoTrainDetailManager;
import com.yunda.jx.wlgl.outstock.manager.MatOutWHTrainDetailManager;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackWH业务类,退库单
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
@Service(value="matBackWHManager")
public class MatBackWHManager extends JXBaseManager<MatBackWH, MatBackWH> {
	
	/** MatBackWHDetail业务类,退库单明细 */
	@Resource
	MatBackWHDetailManager matBackWHDetailManager;
	
	/** 机构服务类 */
	@Resource
	OmOrganizationManager omOrganizationManager;
	
	/** MATSTOCK业务类,物料库存台账 */
	@Resource
	MatStockManager matStockManager;
	
	/** MatOutWHNoTrain业务类,机车外用料单 */
	@Resource
	MatOutWHNoTrainDetailManager matOutWHNoTrainDetailManager;
	
	/** MatOutWHTrain业务类,机车用料单 */
	@Resource
	MatOutWHTrainDetailManager matOutWHTrainDetailManager;

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
		for (int i = 0; i < ids.length; i++) {
			MatBackWH entity = this.getModelById(ids[i]);
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的退库单已经登账，请刷新页面后重试" };
			}
		}
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
	public String[] validateUpdate(MatBackWH t) {
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
	 * @param searchEntity 查询实体对象
	 * @param isQueryPage 检验是否是查询页面的查询请求
	 * @return
	 */
	public Page<MatBackWH> findPageList(SearchEntity<MatBackWH> searchEntity, boolean isQueryPage) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatBackWH Where recordStatus = ").append(Constants.NO_DELETE);
		MatBackWH entity = searchEntity.getEntity();
		// 查询条件 - 领用库房idx主键
		if (!StringUtil.isNullOrBlank(entity.getWhIdx())) {
			sb.append(" And whIdx = '").append(entity.getWhIdx()).append("'");
		}
		// 查询条件 - 退库班组
		if (!StringUtil.isNullOrBlank(entity.getBackOrg())) {
			sb.append(" And backOrg Like '%").append(entity.getBackOrg()).append("%'");
		}
		// 查询条件 - 退库人
		if (!StringUtil.isNullOrBlank(entity.getBackWhEmp())) {
			sb.append(" And backWhEmp Like '%").append(entity.getBackWhEmp()).append("%'");
		}
		// 查询条件 - 状态
		if (!IBillStatus.CONST_STR_STATUS_SY.equalsIgnoreCase(entity.getStatus()) && !StringUtil.isNullOrBlank(entity.getStatus())) {
			sb.append(" And status = '").append(entity.getStatus()).append("'");
		}
		if (!isQueryPage) {
			// 过滤库房信息
			SqlFilter.filterWhIdx(sb, SystemContext.getOmEmployee().getEmpid(), null);
		}
		// 查询条件 - 退库日期
		SqlFilter.filterDate(sb, "backDate", entity.getStartDate(), entity.getEndDate());
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
//			sb.append(" Order By updateTime");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：保存退库单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matBackWH  MatBackWH实体类, 数据表：退库单单
	 * @param detailList MatBackWHDetail实体类数组, 数据表：退库单明细
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] saveMatOutAndDetail(MatBackWH matBackWH, MatBackWHDetail[] detailList) throws IllegalAccessException,
			InvocationTargetException, BusinessException, NoSuchFieldException {
		matBackWH.setBillSummary(this.formatbillSummary(matBackWH, detailList));			// 设置入库单单据摘要信息
		// 在后台自动设置退库人所在机构的序列
		if (StringUtil.isNullOrBlank(matBackWH.getBackOrgSeq())) {
			OmOrganization organization = omOrganizationManager.getModelById(matBackWH.getBackOrgId());
			matBackWH.setBackOrgSeq(organization.getOrgseq());
		}
		if (null != matBackWH.getIdx()) {
			MatBackWH entity = this.getModelById(matBackWH.getIdx());
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的入库单已经登账，请刷新页面后重试" };
			}
			BeanUtils.copyProperties(entity, matBackWH);
			this.saveOrUpdate(entity);
		} else {
			this.saveOrUpdate(matBackWH);
		}
		String idx = matBackWH.getIdx();
		List<MatBackWHDetail> entityList = new ArrayList<MatBackWHDetail>();
		// 根据退库单单主键获得该退库单单暂存的退库单明细
		List<MatBackWHDetail> oList = this.matBackWHDetailManager.getModelsByMatBackWhIdx(idx);
		// 检验已暂存的入库单名称是否被删除，如果页面进行了删除，则在后台同步更新
		for (MatBackWHDetail oDetail : oList) {
			if (isDeleted(oDetail, detailList)) {
				oDetail = EntityUtil.setSysinfo(oDetail);
				oDetail = EntityUtil.setDeleted(oDetail);
				entityList.add(oDetail);
			}
		}
		for (MatBackWHDetail detail : detailList) {
			MatBackWHDetail entity = null;
			if (null != detail.getIdx()) {
				entity = this.matBackWHDetailManager.getModelById(detail.getIdx());
				if (entity.getQty() != detail.getQty()) {
					entity.setQty(detail.getQty());
				}
			} else {
				entity = detail;
				if (null == entity.getMatBackWhIdx()) {
					entity.setMatBackWhIdx(idx);
				}
			}
			entity = EntityUtil.setSysinfo(entity);
			//设置逻辑删除字段状态为未删除
			entity = EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		// 如果是登账操作，则需同步更新物料的库存信息及出库信息
		if (matBackWH.getStatus().equalsIgnoreCase(IBillStatus.CONST_STR_STATUS_DZ)) {
			for (MatBackWHDetail detail : detailList) {
				// 因为出库分“机车外用料出库”和“机车检修用料出库”， 因此退库时要判断退库的数据来源
				// 1 来源于“机车外用料出库”
				if (this.matOutWHNoTrainDetailManager.saveBackWh(detail)) {
					continue;
				}
				// 2 来源于“机车检修用料出库”
				this.matOutWHTrainDetailManager.saveBackWh(detail);
			}
			// 更新库存信息
			this.saveEntryAccount(matBackWH);
		}
		return null;
	}
	
	/**
	 * <li>说明：格式化退库单摘要信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matBackWH 退库单
	 * @param detailList 退库单明细
	 * @return 单据摘要样式为【 退库库房： 物料编码（数量）；物料编码（数量）；】
	 */
	private String formatbillSummary(MatBackWH matBackWH, MatBackWHDetail[] detailList) {
		StringBuilder sb = new StringBuilder();
		sb.append(matBackWH.getWhName()).append("：");
		for (MatBackWHDetail detail : detailList) {
			if (null != detail) {
				sb.append(detail.getMatCode()).append("(").append(detail.getQty()).append(")；");
			}
		}
		return sb.toString();
	}
	
	/**
	 * <li>说明：检验数据库中已存储的物料退库单明细，是否已经在页面进行了删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param oDetail
	 * @param detailList
	 * @return
	 */
	private boolean isDeleted(MatBackWHDetail oDetail, MatBackWHDetail[] detailList) {
		for (MatBackWHDetail detail : detailList) {
			if (oDetail.getIdx().equals(detail.getIdx())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：重写删除方法，对退库单明细进行级联删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 *  @param ids 入库单idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除退库单明细
		for (int i = 0; i < ids.length; i++) {
			List<MatBackWHDetail> list = this.matBackWHDetailManager.getModelsByMatBackWhIdx((String)ids[i]);
			if (null != list && list.size() > 0) {
				this.matBackWHDetailManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：根据【退库单】退库
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matBackWH 退库单记录实体
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void saveEntryAccount(MatBackWH matBackWH) throws BusinessException, NoSuchFieldException {
		List<MatBackWHDetail> oList = this.matBackWHDetailManager.getModelsByMatBackWhIdx(matBackWH.getIdx());
		// 库存信息
		MatStock matStock = null;
		for (MatBackWHDetail detail : oList) {
			matStock = matStockManager.getModel(matBackWH.getWhIdx(), detail.getMatCode());
			// 退库，即：增加库存数量
			matStock.setQty(matStock.getQty() + detail.getQty());
			matStockManager.saveOrUpdate(matStock);
		}
	}
	/**
	 * <li>说明：检验是否已经执行了回滚操作，或者已经被确认移入
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 实体类主键idx数组
	 * @return
	 */
	private String[] isRollBacked(String[] ids) {
		List<String> errorMsg = new ArrayList<String>();
		for (String idx: ids) {
			MatBackWH entity = this.getModelById(idx);
			if (IBillStatus.CONST_STR_STATUS_ZC.equals(entity.getStatus())) {
				errorMsg.add("退库单【" + entity.getBillNo() + "】已经回滚，请刷新页面后重试！");
			}
			if (!SqlFilter.isInLastMonth(entity.getBackDate())) {
				errorMsg.add("退库日期超过一个月的单据不能回滚");
			}
		}
		if (0 < errorMsg.size()) {
			return errorMsg.toArray(new String[errorMsg.size()]);
		}
		return null;
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param ids 实体类主键idx数组
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] updateRollBack(String[] ids) throws BusinessException, NoSuchFieldException {
		// 检验是否已经回滚
		String[] checkMsg = this.isRollBacked(ids);
		if (null != checkMsg) {
			return checkMsg;
		}
		for (String idx : ids) {
			MatBackWH entity = this.getModelById(idx);
			if (null != entity) {
				// 回滚库存记录
				this.updateRollBack(entity);
				// 设置单据状态为”暂存“
				entity.setStatus(IBillStatus.CONST_STR_STATUS_ZC);
				this.saveOrUpdate(entity);
			}
		}
		return null;
	}
	
	/**
	 * <li>说明：回滚操作
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entity 实体
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	private void updateRollBack(MatBackWH entity) throws BusinessException, NoSuchFieldException {
		// 库存信息
		MatStock matStock = null;
		List<MatStock> matStockList = new ArrayList<MatStock>();
		List<MatBackWHDetail> oList = this.matBackWHDetailManager.getModelsByMatBackWhIdx(entity.getIdx());
		for (MatBackWHDetail detail : oList) {
			matStock = matStockManager.getModel(entity.getWhIdx(), detail.getMatCode());
			// 更新库存数量
			matStock.setQty(matStock.getQty() - detail.getQty());
			matStockList.add(matStock);
			// 更新用料单明细的”退库数量“字段值
			// 因为出库分“机车外用料出库”和“机车检修用料出库”， 因此回滚时要判断退库的数据来源
			// 1 来源于“机车外用料出库”
			if (this.matOutWHNoTrainDetailManager.updateRollBack(detail)) {
				continue;
			}
			// 2 来源于“机车检修用料出库”
			this.matOutWHTrainDetailManager.updateRollBack(detail);
		}
		matStockManager.saveOrUpdate(matStockList);
	}
	
}
