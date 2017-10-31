package com.yunda.jx.wlgl.outstock.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
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
import com.yunda.jx.wlgl.expend.manager.MatTrainExpendAccountManager;
import com.yunda.jx.wlgl.outstock.entity.MatOutWHTrain;
import com.yunda.jx.wlgl.outstock.entity.MatOutWHTrainDetail;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatOutWHTrain业务类,机车用料单
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 下午02:18:55
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matOutWHTrainManager")
public class MatOutWHTrainManager extends JXBaseManager<MatOutWHTrain, MatOutWHTrain>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** MatOutWHTrainDetail业务类,物料入库明细 */
	@Resource
	MatOutWHTrainDetailManager matOutWHTrainDetailManager;
	
	/** MATSTOCK业务类,物料库存台账 */
	@Resource
	MatStockManager matStockManager;
	
	/** 机构服务类 */
	@Resource
	OmOrganizationManager omOrganizationManager;
	
	/** MatTrainExpendAccount业务类,机车用料消耗记录 */
	@Resource
	MatTrainExpendAccountManager matTrainExpendAccountManager;
	
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
		for (int i = 0; i < ids.length; i++) {
			MatOutWHTrain entity = this.getModelById(ids[i]);
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的入库单已经登账，请刷新页面后重试" };
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
	public String[] validateUpdate(MatOutWHTrain entity) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2014-09-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param searchEntity 查询实体对象
	 * @param isQueryPage 检验是否是查询页面的查询请求
	 * @return
	 */
	public Page<MatOutWHTrain> findPageList(SearchEntity<MatOutWHTrain> searchEntity, boolean isQueryPage) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatOutWHTrain Where recordStatus = ").append(Constants.NO_DELETE);
		MatOutWHTrain entity = searchEntity.getEntity();
		// 查询条件 - 领用库房idx主键
		if (!StringUtil.isNullOrBlank(entity.getWhIdx())) {
			sb.append(" And whIdx = '").append(entity.getWhIdx()).append("'");
		}
		// 查询条件 - 领用班组
		if (!StringUtil.isNullOrBlank(entity.getGetOrg())) {
			sb.append(" And getOrg Like '%").append(entity.getGetOrg()).append("%'");
		}
		// 查询条件 - 领用人
		if (!StringUtil.isNullOrBlank(entity.getGetEmp())) {
			sb.append(" And getEmp Like '%").append(entity.getGetEmp()).append("%'");
		}
		// 查询条件 - 状态
		if (!IBillStatus.CONST_STR_STATUS_SY.equalsIgnoreCase(entity.getStatus()) && !StringUtil.isNullOrBlank(entity.getStatus())) {
			sb.append(" And status = '").append(entity.getStatus()).append("'");
		}
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
		if (!isQueryPage) {
			// 过滤库房信息
			SqlFilter.filterWhIdx(sb, SystemContext.getOmEmployee().getEmpid(), null);
		}
		// 查询条件 - 领用日期
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
//			sb.append(" Order By updateTime");
		}
		String hql = sb.toString();
		String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
		return findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}

	/**
	 * <li>说明：保存机车用料单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrain  MatOutWHTrain实体类, 数据表：机车用料单
	 * @param detailList MatOutWHTrainDetail实体类数组, 数据表：机车用料明细
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] saveMatOutAndDetail(MatOutWHTrain matOutWHTrain, MatOutWHTrainDetail[] detailList) throws IllegalAccessException,
			InvocationTargetException, BusinessException, NoSuchFieldException {
		matOutWHTrain.setBillSummary(this.formatbillSummary(matOutWHTrain, detailList));			// 设置入库单单据摘要信息
		// 在后台自动设置领用人所在机构的序列
		if (StringUtil.isNullOrBlank(matOutWHTrain.getGetOrgSeq())) {
			OmOrganization organization = omOrganizationManager.getModelById(matOutWHTrain.getGetOrgId());
			matOutWHTrain.setGetOrgSeq(organization.getOrgseq());
		}
		if (null != matOutWHTrain.getIdx()) {
			MatOutWHTrain entity = this.getModelById(matOutWHTrain.getIdx());
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的入库单已经登账，请刷新页面后重试" };
			}
			BeanUtils.copyProperties(entity, matOutWHTrain);
			this.saveOrUpdate(entity);
		} else {
			this.saveOrUpdate(matOutWHTrain);
		}
		String idx = matOutWHTrain.getIdx();
		List<MatOutWHTrainDetail> entityList = new ArrayList<MatOutWHTrainDetail>();
		// 根据机车用料单主键获得该机车用料单暂存的机车用料明细
		List<MatOutWHTrainDetail> oList = this.matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX(idx);
		// 检验已暂存的入库单名称是否被删除，如果页面进行了删除，则在后台同步更新
		for (MatOutWHTrainDetail oDetail : oList) {
			if (isDeleted(oDetail, detailList)) {
				oDetail = EntityUtil.setSysinfo(oDetail);
				oDetail = EntityUtil.setDeleted(oDetail);
				entityList.add(oDetail);
			}
		}
		for (MatOutWHTrainDetail detail : detailList) {
			MatOutWHTrainDetail entity = null;
			if (null != detail.getIdx()) {
				entity = this.matOutWHTrainDetailManager.getModelById(detail.getIdx());
				if (entity.getQty() != detail.getQty()) {
					entity.setQty(detail.getQty());
				}
			} else {
				entity = detail;
				if (null == entity.getMatOutWHTrainIDX()) {
					entity.setMatOutWHTrainIDX(idx);
				}
			}
			entity = EntityUtil.setSysinfo(entity);
			//设置逻辑删除字段状态为未删除
			entity = EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		// 如果是登账操作，则需同步更新物料的库存信息
		if (matOutWHTrain.getStatus().equalsIgnoreCase(IBillStatus.CONST_STR_STATUS_DZ)) {
			this.saveEntryAccount(matOutWHTrain);
			this.matTrainExpendAccountManager.save(matOutWHTrain);
		}
		return null;
	}
	
	/**
	 * <li>说明：格式化机车用料单摘要信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrain 入库单
	 * @param detailList 机车用料明细
	 * @return 单据摘要样式为【 入库库房： 物料编码（数量）；物料编码（数量）；】
	 */
	private String formatbillSummary(MatOutWHTrain matOutWHTrain, MatOutWHTrainDetail[] detailList) {
		StringBuilder sb = new StringBuilder();
		sb.append(matOutWHTrain.getWhName()).append("：");
		for (MatOutWHTrainDetail detail : detailList) {
			if (null != detail) {
				sb.append(detail.getMatCode()).append("(").append(detail.getQty()).append(")；");
			}
		}
		return sb.toString();
	}
	
	/**
	 * <li>说明：检验数据库中已存储的物料机车用料明细，是否已经在页面进行了删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param oDetail
	 * @param detailList
	 * @return
	 */
	private boolean isDeleted(MatOutWHTrainDetail oDetail, MatOutWHTrainDetail[] detailList) {
		for (MatOutWHTrainDetail detail : detailList) {
			if (oDetail.getIdx().equals(detail.getIdx())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：重写删除方法，对机车用料明细进行级联删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 *  @param ids 入库单idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除机车用料明细
		for (int i = 0; i < ids.length; i++) {
			List<MatOutWHTrainDetail> list = this.matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX((String)ids[i]);
			if (null != list && list.size() > 0) {
				this.matOutWHTrainDetailManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	

	/**
	 * <li>说明：根据【机车用料单】出库单检验消耗配件库存数量是否充足
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrain 机车用料单 实体
	 * @return
	 */
	private String[] checkMatStockQty(MatOutWHTrain matOutWHTrain) {
		List<String> errorMsg = new ArrayList<String>();
		List<MatOutWHTrainDetail> oList = this.matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX(matOutWHTrain.getIdx());
		MatStock matStock = null;
		for (MatOutWHTrainDetail detail : oList) {
			matStock = matStockManager.getModel(matOutWHTrain.getWhIdx(), detail.getMatCode());
			if (null == matStock) {
				errorMsg.add("物料：" + detail.getMatDesc() + "(" + detail.getMatCode() + ")库存不足！");
			} else {
				int num = matStock.getQty() - detail.getQty();			// 计算出库后的库存数量
				if (num < 0) {
					errorMsg.add("物料：" + detail.getMatDesc() + "(" + detail.getMatCode() + ")库存不足！");
				}
			}
		}
		if (errorMsg.size() > 0) {
			return errorMsg.toArray(new String[errorMsg.size()]);
		}
		return null;
	}

	/**
	 * <li>说明：根据【机车用料单】出库
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrain 机车外用料单实体
	 * @return
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveEntryAccount(MatOutWHTrain matOutWHTrain) throws BusinessException, NoSuchFieldException {
		// 检验库存信息是否满足出库要求
		String[] errorMsg = this.checkMatStockQty(matOutWHTrain);
		if (null != errorMsg) {
			throw new StockLackingException(errorMsg[0]);
		}
		
		// 库存信息
		MatStock matStock = null;
		List<MatStock> matStockList = new ArrayList<MatStock>();
		List<MatOutWHTrainDetail> oList = this.matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX(matOutWHTrain.getIdx());
		for (MatOutWHTrainDetail detail : oList) {
			matStock = matStockManager.getModel(matOutWHTrain.getWhIdx(), detail.getMatCode());
			// 更新库存数量
			matStock.setQty(matStock.getQty() - detail.getQty());
			matStockList.add(matStock);
		}
		matStockManager.saveOrUpdate(matStockList);
	}
	
	/**
	 * <li>说明：检验是否已经执行了回滚操作
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
			MatOutWHTrain entity = this.getModelById(idx);
			if (IBillStatus.CONST_STR_STATUS_ZC.equals(entity.getStatus())) {
				errorMsg.add("用料单【" + entity.getBillNo() + "】已经回滚，请刷新页面后重试！");
			}
			if (!SqlFilter.isInLastMonth(entity.getGetDate())) {
				errorMsg.add("领用日期超过一个月的单据不能回滚");
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
			MatOutWHTrain entity = this.getModelById(idx);
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
	private void updateRollBack(MatOutWHTrain entity) throws BusinessException, NoSuchFieldException {
		// 回滚机车用料消耗记录（逻辑删除）
		this.matTrainExpendAccountManager.updateRollBack(entity);
		// 库存信息
		MatStock matStock = null;
		List<MatStock> matStockList = new ArrayList<MatStock>();
		List<MatOutWHTrainDetail> oList = this.matOutWHTrainDetailManager.getModelsByMatOutWHTrainIDX(entity.getIdx());
		for (MatOutWHTrainDetail detail : oList) {
			matStock = matStockManager.getModel(entity.getWhIdx(), detail.getMatCode());
			// 更新库存数量
			matStock.setQty(matStock.getQty() + detail.getQty());
			matStockList.add(matStock);
		}
		matStockManager.saveOrUpdate(matStockList);
	}
	
}