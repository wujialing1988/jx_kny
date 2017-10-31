package com.yunda.jx.wlgl.movestock.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.movestock.entity.MatMoveStock;
import com.yunda.jx.wlgl.movestock.entity.MatMoveStockDetail;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatMoveStock业务类,物料移库单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matMoveStockManager")
public class MatMoveStockManager extends JXBaseManager<MatMoveStock, MatMoveStock>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	//移库单明细业务类
	private MatMoveStockDetailManager matMoveStockDetailManager;
	//消耗配件库存信息
	private MatStockManager matStockManager;
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-09-28
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
			MatMoveStock entity = this.getModelById(ids[i]);
			if (!MatMoveStock.STATUS_ZC.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的移库单已移出，请刷新页面后重试" };
			}
		}
		return null;
	}
	/**
     * <li>说明：分页查询
     * <li>创建人：程梅
     * <li>创建日期：2014-09-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param searchEntity 查询实体对象
	 * @param fnCode 所属功能代码
	 * @return
	 */
	public Page<MatMoveStock> findPageList(SearchEntity<MatMoveStock> searchEntity,String empId) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatMoveStock Where recordStatus = ").append(Constants.NO_DELETE);
		MatMoveStock entity = searchEntity.getEntity();
		// 查询条件 - 状态
		if (!StringUtil.isNullOrBlank(entity.getStatus()) && !MatMoveStock.STATUS_ALL.equalsIgnoreCase(entity.getStatus()) ) {
			sb.append(" And status = '").append(entity.getStatus()).append("'");
		}
		if(!StringUtil.isNullOrBlank(empId)){
			sb.append(" and inWhIdx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='").append(empId).append("')");
		}
		if(!StringUtil.isNullOrBlank(entity.getExWhIdx())){
			sb.append(" and exWhIdx ='").append(entity.getExWhIdx()).append("'");
		}
		if(!StringUtil.isNullOrBlank(entity.getInWhIdx())){
			sb.append(" and inWhIdx ='").append(entity.getInWhIdx()).append("'");
		}
		// 查询条件 - 入库日期
		SqlFilter.filterDate(sb, "exWhDate", entity.getStartDate(), entity.getEndDate());
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
	 * 
	 * <li>说明：保存消耗配件移库单以及明细
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public String[] saveWhAndDetail(MatMoveStock matMoveStock, MatMoveStockDetail[] detailList) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
		matMoveStock.setBillSummary(this.formatbillSummary(matMoveStock, detailList));			// 设置入库单单据摘要信息
		if (null != matMoveStock.getIdx()) {
			MatMoveStock entity = this.getModelById(matMoveStock.getIdx());
			if (!MatMoveStock.STATUS_ZC.equals(entity.getStatus())) {
				return new String[]{ "单据编号为：【" + entity.getBillNo() + "】的移库单已经移出，请刷新页面后重试" };
			}
			BeanUtils.copyProperties(entity, matMoveStock);
			this.saveOrUpdate(entity);
		} else {
			this.saveOrUpdate(matMoveStock);
		}
		String matMoveStockIdx = matMoveStock.getIdx();
		List<MatMoveStockDetail> entityList = new ArrayList<MatMoveStockDetail>();
		// 根据移库单主键获得该入库单暂存的移库单明细
		List<MatMoveStockDetail> oList = this.matMoveStockDetailManager.getModelsByMatMoveStockIdx(matMoveStockIdx);
		// 检验以暂存的移库单名称是否被删除，如果页面进行了删除，则在后台同步更新
		for (MatMoveStockDetail oDetail : oList) {
			if (isDeleted(oDetail, detailList)) {
				oDetail = EntityUtil.setSysinfo(oDetail);
				oDetail = EntityUtil.setDeleted(oDetail);
				entityList.add(oDetail);
			}
		}
		for (MatMoveStockDetail detail : detailList) {
			MatMoveStockDetail entity = null;
			if (null != detail.getIdx()) {
				entity = this.matMoveStockDetailManager.getModelById(detail.getIdx());
				if (entity.getQty() != detail.getQty()) {
					entity.setQty(detail.getQty());
				}
			} else {
				entity = detail;
				if (null == entity.getMatMoveStockIdx()) {
					entity.setMatMoveStockIdx(matMoveStockIdx);
				}
			}
			entity = EntityUtil.setSysinfo(entity);
			//设置逻辑删除字段状态为未删除
			entity = EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
		// 如果是移出操作，则需同步更新物料的库存信息
		if (matMoveStock.getStatus().equalsIgnoreCase(MatMoveStock.STATUS_OUT)) {
			this.saveEntryAccount(matMoveStock);
		}
		return null;
	}
	/**
	 * <li>说明：格式化消耗配件移库单摘要信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matMoveStock 移库单
	 * @param detailList 移库单明细
	 * @return 单据摘要样式为【 移库库房移库到移入库房： 物料编码（数量）；物料编码（数量）；】
	 */
	private String formatbillSummary(MatMoveStock matMoveStock, MatMoveStockDetail[] detailList) {
		StringBuilder sb = new StringBuilder();
		sb.append(matMoveStock.getExWhName()).append("移库到").append(matMoveStock.getInWhName()).append("：");
		for (MatMoveStockDetail detail : detailList) {
			if (null != detail) {
				sb.append(detail.getMatCode()).append("(").append(detail.getQty()).append(")；");
			}
		}
		return sb.toString();
	}
	/**
	 * <li>说明：检验数据库中已存储的物料移库单明细，是否已经在页面进行了删除
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param oDetail
	 * @param detailList
	 * @return
	 */
	private boolean isDeleted(MatMoveStockDetail oDetail, MatMoveStockDetail[] detailList) {
		for (MatMoveStockDetail detail : detailList) {
			if (oDetail.getIdx().equals(detail.getIdx())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * <li>说明：确认移入
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public String[] updateMatMoveStock(String id) throws BusinessException, NoSuchFieldException {
		MatMoveStock matMoveStock = this.getModelById(id);
		if (!MatMoveStock.STATUS_OUT.equals(matMoveStock.getStatus())) {
			return new String[]{ "单据编号为：【" + matMoveStock.getBillNo() + "】的移库单已经移入，请刷新页面后重试" };
		}
		AcOperator acOperator = SystemContext.getAcOperator();
		matMoveStock.setInWhEmpId(acOperator.getOperatorid());
		matMoveStock.setInWhEmp(acOperator.getOperatorname());
		matMoveStock.setInWhDate(new Date());
		matMoveStock.setStatus(MatMoveStock.STATUS_IN);//移库单状态为【移入】
		//更新移库单信息
        this.saveOrUpdate(matMoveStock);
        //更新库存信息
        this.updateEntryAccount(matMoveStock);
        return null;
    }
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(MatMoveStock entity) throws BusinessException {
		return null;
	}
	public MatMoveStockDetailManager getMatMoveStockDetailManager() {
		return matMoveStockDetailManager;
	}
	public void setMatMoveStockDetailManager(
			MatMoveStockDetailManager matMoveStockDetailManager) {
		this.matMoveStockDetailManager = matMoveStockDetailManager;
	}
	public MatStockManager getMatStockManager() {
		return matStockManager;
	}
	public void setMatStockManager(MatStockManager matStockManager) {
		this.matStockManager = matStockManager;
	}
	
	/**
	 * 
	 * <li>说明：根据移库单检验消耗配件库存数量是否充足
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	private String[] checkMatStockQty(MatMoveStock matMoveStock) {
		List<String> errorMsg = new ArrayList<String>();
		List<MatMoveStockDetail> oList = matMoveStockDetailManager.getModelsByMatMoveStockIdx(matMoveStock.getIdx());
		MatStock matStock = null;
		for (MatMoveStockDetail detail : oList) {
			matStock = matStockManager.getModel(matMoveStock.getExWhIdx(), detail.getMatCode());
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
	 * <li>说明：根据【移库单】出库
	 * <li>创建人：程梅
	 * <li>创建日期：2014-09-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matMoveStock 移库单
	 * @return
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public void saveEntryAccount(MatMoveStock matMoveStock) throws BusinessException, NoSuchFieldException {
		// 检验库存信息是否满足出库要求
		String[] errorMsg = this.checkMatStockQty(matMoveStock);
		if (null != errorMsg) {
			throw new StockLackingException(errorMsg[0]);
		}
		
		// 库存信息
		MatStock matStock = null;
		List<MatStock> matStockList = new ArrayList<MatStock>();
		List<MatMoveStockDetail> oList = matMoveStockDetailManager.getModelsByMatMoveStockIdx(matMoveStock.getIdx());
		for (MatMoveStockDetail detail : oList) {
			matStock = matStockManager.getModel(matMoveStock.getExWhIdx(), detail.getMatCode());
			// 更新库存数量
			matStock.setQty(matStock.getQty() - detail.getQty());
			matStockList.add(matStock);
		}
		matStockManager.saveOrUpdate(matStockList);
	}
	
	/**
	 * 
	 * <li>说明：根据【消耗配件移出单】移入
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void updateEntryAccount(MatMoveStock matMoveStock) throws BusinessException, NoSuchFieldException {
		List<MatMoveStockDetail> oList = matMoveStockDetailManager.getModelsByMatMoveStockIdx(matMoveStock.getIdx());
		// 库存信息
		MatStock matStock = null;
		for (MatMoveStockDetail detail : oList) {
			matStock = matStockManager.getModel(matMoveStock.getInWhIdx(), detail.getMatCode());
			if (null == matStock) {
				// 如果数据库中查询不到该物料的库存信息，则判断为该物料是首次登账
				matStock = new MatStock();
				matStock.setMatCode(detail.getMatCode());			// 物料编码
				matStock.setMatDesc(detail.getMatDesc());			// 物料编码
				matStock.setQty(detail.getQty());					// 库存数量
				matStock.setUnit(detail.getUnit());					// 计量单位
				matStock.setWhIdx(matMoveStock.getInWhIdx());		// 库房idx主键
				matStock.setWhName(matMoveStock.getInWhName());		// 库房名称
			} else {
				// 更新数据库中同类物料的库存信息
				matStock.setQty(detail.getQty() + matStock.getQty());
			}
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
			MatMoveStock entity = this.getModelById(idx);
			// 确认移入的移库单不能回滚
			if (MatMoveStock.STATUS_IN.equals(entity.getStatus())) {
				errorMsg.add("移库单【" + entity.getBillNo() + "】已确认移入，不允许回滚！");
			}
			if (MatMoveStock.STATUS_ZC.equals(entity.getStatus())) {
				errorMsg.add("移库单【" + entity.getBillNo() + "】已经回滚，请刷新页面后重试！");
			}
			if (!SqlFilter.isInLastMonth(entity.getExWhDate())) {
				errorMsg.add("移出日期超过一个月的单据不能回滚");
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
			MatMoveStock entity = this.getModelById(idx);
			if (null != entity) {
				// 回滚库存记录
				this.updateRollBack(entity);
				// 设置单据状态为”暂存“
				entity.setStatus(MatMoveStock.STATUS_ZC);
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
	private void updateRollBack(MatMoveStock entity) throws BusinessException, NoSuchFieldException {
		// 库存信息
		MatStock matStock = null;
		List<MatStock> matStockList = new ArrayList<MatStock>();
		List<MatMoveStockDetail> oList = this.matMoveStockDetailManager.getModelsByMatMoveStockIdx(entity.getIdx());
		for (MatMoveStockDetail detail : oList) {
			matStock = matStockManager.getModel(entity.getExWhIdx(), detail.getMatCode());
			// 更新库存数量
			matStock.setQty(matStock.getQty() + detail.getQty());
			matStockList.add(matStock);
		}
		matStockManager.saveOrUpdate(matStockList);
	}
	
}