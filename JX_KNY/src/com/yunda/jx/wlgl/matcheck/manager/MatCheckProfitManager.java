package com.yunda.jx.wlgl.matcheck.manager;

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
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.IBillStatus;
import com.yunda.jx.wlgl.matcheck.entity.MatCheckProfit;
import com.yunda.jx.wlgl.matcheck.entity.MatCheckProfitDetail;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatCheckProfit业务类,物料盘盈处理
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matCheckProfitManager")
public class MatCheckProfitManager extends JXBaseManager<MatCheckProfit, MatCheckProfit>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** MatCheckProfitDetail业务类,物料盘盈明细 */
	@Resource
	MatCheckProfitDetailManager matCheckProfitDetailManager;
	
	/** MATSTOCK业务类,物料库存台账 */
	@Resource
	MatStockManager matStockManager;
	
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
			MatCheckProfit entity = this.getModelById(ids[i]);
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的盘盈单已经登账，请刷新页面后重试" };
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
	public String[] validateUpdate(MatCheckProfit entity) throws BusinessException {
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
	 * @param isQueryPage 检验是否是查询页面的查询请求
	 * @return
	 */
	public Page<MatCheckProfit> findPageList(SearchEntity<MatCheckProfit> searchEntity, boolean isQueryPage) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatCheckProfit Where recordStatus = ").append(Constants.NO_DELETE);
		MatCheckProfit entity = searchEntity.getEntity();
		// 查询条件 - 库房idx主键
		if (!StringUtil.isNullOrBlank(entity.getWhIdx())) {
			sb.append(" And whIdx = '").append(entity.getWhIdx()).append("'");
		}
		// 查询条件 - 盘盈人
		if (!StringUtil.isNullOrBlank(entity.getCheckEmp())) {
			sb.append(" And checkEmp = '").append(entity.getCheckEmp()).append("'");
		}
		// 查询条件 - 状态
		if (!IBillStatus.CONST_STR_STATUS_SY.equalsIgnoreCase(entity.getStatus()) && !StringUtil.isNullOrBlank(entity.getStatus())) {
			sb.append(" And status = '").append(entity.getStatus()).append("'");
		}
		if (!isQueryPage) {
			// 过滤库房信息
			SqlFilter.filterWhIdx(sb, SystemContext.getOmEmployee().getEmpid(), null);
		}
		// 查询条件 - 盘盈日期
		SqlFilter.filterDate(sb, "checkDate", entity.getStartDate(), entity.getEndDate());
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
	 * <li>说明：保存盘盈单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matCheckProfit  MatCheckProfit实体类, 数据表：盘盈单
	 * @param detailList MatCheckProfit实体类数组, 数据表：盘盈明细
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public String[] saveProfitAndDetail(MatCheckProfit matCheckProfit, MatCheckProfitDetail[] detailList) throws IllegalAccessException,
			InvocationTargetException, BusinessException, NoSuchFieldException {
		// 设置盘盈单单据摘要信息
		matCheckProfit.setBillSummary(this.formatbillSummary(matCheckProfit, detailList));
		if (null != matCheckProfit.getIdx()) {
			MatCheckProfit entity = this.getModelById(matCheckProfit.getIdx());
			if (IBillStatus.CONST_STR_STATUS_DZ.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的盘盈单已经登账，请刷新页面后重试" };
			}
			BeanUtils.copyProperties(entity, matCheckProfit);
			this.saveOrUpdate(entity);
		} else {
			this.saveOrUpdate(matCheckProfit);
		}
		String idx = matCheckProfit.getIdx();
		List<MatCheckProfitDetail> entityList = new ArrayList<MatCheckProfitDetail>();
		// 根据盘盈单主键获得该盘盈单暂存的盘盈明细
		List<MatCheckProfitDetail> oList = this.matCheckProfitDetailManager.getModelsByMatCheckProfitIDX(idx);
		// 检验已暂存的盘盈单名称是否被删除，如果页面进行了删除，则在后台同步更新
		for (MatCheckProfitDetail oDetail : oList) {
			if (isDeleted(oDetail, detailList)) {
				oDetail = EntityUtil.setSysinfo(oDetail);
				oDetail = EntityUtil.setDeleted(oDetail);
				entityList.add(oDetail);
			}
		}
		for (MatCheckProfitDetail detail : detailList) {
			MatCheckProfitDetail entity = null;
			if (null != detail.getIdx()) {
				entity = this.matCheckProfitDetailManager.getModelById(detail.getIdx());
				if (entity.getQty() != detail.getQty()) {
					entity.setQty(detail.getQty());
				}
			} else {
				entity = detail;
				if (null == entity.getMatCheckProfitIDX()) {
					entity.setMatCheckProfitIDX(idx);
				}
			}
			entity = EntityUtil.setSysinfo(entity);
			//设置逻辑删除字段状态为未删除
			entity = EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		// 如果是登账操作，则需同步更新物料的库存信息
		if (matCheckProfit.getStatus().equalsIgnoreCase(IBillStatus.CONST_STR_STATUS_DZ)) {
			this.saveEntryAccount(matCheckProfit);
		}
		return null;
	}
	
	/**
	 * <li>说明：格式化盘盈单摘要信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matCheckProfit 盘盈单
	 * @param detailList 盘盈明细
	 * @return 单据摘要样式为【 [盘盈库房]盘点： 物料编码（数量）；物料编码（数量）；】
	 */
	private String formatbillSummary(MatCheckProfit matCheckProfit, MatCheckProfitDetail[] detailList) {
		StringBuilder sb = new StringBuilder();
		sb.append(matCheckProfit.getWhName()).append("盘点：");
		for (MatCheckProfitDetail detail : detailList) {
			if (null != detail) {
				sb.append(detail.getMatCode()).append("(").append(detail.getQty()).append(")；");
			}
		}
		return sb.toString();
	}
	
	/**
	 * <li>说明：检验数据库中已存储的物料盘盈明细，是否已经在页面进行了删除
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
	private boolean isDeleted(MatCheckProfitDetail oDetail, MatCheckProfitDetail[] detailList) {
		for (MatCheckProfitDetail detail : detailList) {
			if (oDetail.getIdx().equals(detail.getIdx())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <li>说明：重写删除方法，对盘盈明细进行级联删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 *  @param ids 盘盈单idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		// 级联删除盘盈明细
		for (int i = 0; i < ids.length; i++) {
			List<MatCheckProfitDetail> list = this.matCheckProfitDetailManager.getModelsByMatCheckProfitIDX((String)ids[i]);
			if (null != list && list.size() > 0) {
				this.matCheckProfitDetailManager.logicDelete(list);
			}
		}
		super.logicDelete(ids);
	}
	
	/**
	 * <li>说明：根据【物料盘盈处理】入库
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matCheckProfit 盘盈单
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void saveEntryAccount(MatCheckProfit matCheckProfit) throws BusinessException, NoSuchFieldException {
		List<MatCheckProfitDetail> oList = this.matCheckProfitDetailManager.getModelsByMatCheckProfitIDX(matCheckProfit.getIdx());
		// 库存信息
		MatStock matStock = null;
		for (MatCheckProfitDetail detail : oList) {
			matStock = matStockManager.getModel(matCheckProfit.getWhIdx(), detail.getMatCode());
			if (null == matStock) {
				// 如果数据库中查询不到该物料的库存信息，则判断为该物料是首次登账
				matStock = new MatStock();
				matStock.setMatCode(detail.getMatCode());			// 物料编码
				matStock.setMatDesc(detail.getMatDesc());			// 物料编码
				matStock.setQty(detail.getQty());					// 库存数量
				matStock.setUnit(detail.getUnit());					// 计量单位
				matStock.setWhIdx(matCheckProfit.getWhIdx());		// 库房idx主键
				matStock.setWhName(matCheckProfit.getWhName());		// 库房名称
			} else {
				// 更新数据库中同类物料的库存信息
				matStock.setQty(detail.getQty() + matStock.getQty());
			}
			matStockManager.saveOrUpdate(matStock);
		}
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
			MatCheckProfit entity = this.getModelById(idx);
			if (IBillStatus.CONST_STR_STATUS_ZC.equals(entity.getStatus())) {
				errorMsg.add("盘盈单【" + entity.getBillNo() + "】已经回滚，请刷新页面后重试！");
			}
			if (!SqlFilter.isInLastMonth(entity.getCheckDate())) {
				errorMsg.add("盘盈日期超过一个月的单据不能回滚");
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
			MatCheckProfit entity = this.getModelById(idx);
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
	private void updateRollBack(MatCheckProfit entity) throws BusinessException, NoSuchFieldException {
		// 库存信息
		MatStock matStock = null;
		List<MatStock> matStockList = new ArrayList<MatStock>();
		List<MatCheckProfitDetail> oList = this.matCheckProfitDetailManager.getModelsByMatCheckProfitIDX(entity.getIdx());
		for (MatCheckProfitDetail detail : oList) {
			matStock = matStockManager.getModel(entity.getWhIdx(), detail.getMatCode());
			// 更新库存数量
			matStock.setQty(matStock.getQty() - detail.getQty());
			matStockList.add(matStock);
		}
		matStockManager.saveOrUpdate(matStockList);
	}
	
}