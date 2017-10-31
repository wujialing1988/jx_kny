package com.yunda.jx.wlgl.backsupply.manager;

import java.io.Serializable;
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
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.SqlFilter;
import com.yunda.jx.wlgl.IBillStatus;
import com.yunda.jx.wlgl.backsupply.entity.MatBackSupplyStation;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackSupplyStation业务类,质量反馈单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matBackSupplyStationManager")
public class MatBackSupplyStationManager extends JXBaseManager<MatBackSupplyStation, MatBackSupplyStation>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	//	消耗配件库存信息
	@Resource
	MatStockManager matStockManager;
	/**
	 * 
	 * <li>说明：质量反馈单登帐
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public  String[] updateMatBackSupplyStation(String[] ids) throws BusinessException, NoSuchFieldException{
		MatBackSupplyStation matBackSupplyStation ;
		for(String id : ids){
			matBackSupplyStation = new MatBackSupplyStation();
			matBackSupplyStation = getModelById(id);
			if (!MatBackSupplyStation.STATUS_ZC.equals(matBackSupplyStation.getStatus())) {
				return new String[]{ "物料编码为：【" + matBackSupplyStation.getMatCode() + "】的反馈单已经登帐，请刷新页面后重试" };
			}else{
				matBackSupplyStation.setStatus(MatBackSupplyStation.STATUS_DZ);//状态为已登帐
				//更新库存信息
				this.saveEntryAccount(matBackSupplyStation);
				this.saveOrUpdate(matBackSupplyStation);
			}
		}
		return null;
	}
	/**
	 * 
	 * <li>说明：分页查询
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<MatBackSupplyStation> findPageList(SearchEntity<MatBackSupplyStation> searchEntity,String flag) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatBackSupplyStation Where recordStatus = ").append(Constants.NO_DELETE);
		MatBackSupplyStation entity = searchEntity.getEntity();
		// 查询条件 - 状态
		if (!StringUtil.isNullOrBlank(entity.getStatus()) ) {
			sb.append(" And status = '").append(entity.getStatus()).append("'");
		}
		// 查询条件 - 入库日期
		SqlFilter.filterDate(sb, "feedBackDate", entity.getStartDate(), entity.getEndDate());
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
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param sb
	 * @param status
	 */
	private void filterStatus(StringBuilder sb, String status) {
		if (status.contains(",")) {
			String[] array = status.split(",");
			sb.append(" And status in ('").append(array[0]).append("'");
			for (int i = 1; i < array.length; i++) {
				sb.append(",").append("'").append(array[i]).append("'");
			}
			sb.append(")");
		} else {
			sb.append(" And status = '").append(status).append("'");
		}
	}
	
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
	public String[] validateUpdate(MatBackSupplyStation entity) throws BusinessException {
		return null;
	}
	
	/**
	 * 
	 * <li>说明：根据【质量反馈单】出库时检查库存是否充足
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	private String checkMatStockQty(MatBackSupplyStation matBackSupplyStation) {
		// 库存信息
		MatStock matStock = matStockManager.getModel(matBackSupplyStation.getWhIdx(), matBackSupplyStation.getMatCode());
		if (null == matStock) {
			return "物料：" + matBackSupplyStation.getMatDesc() + "(" + matBackSupplyStation.getMatCode() + ")库存不足！";
		} else {
			int num = matStock.getQty() - matBackSupplyStation.getQty();			// 计算出库后的库存数量
			if (num < 0) {
				return "物料：" + matBackSupplyStation.getMatDesc() + "(" + matBackSupplyStation.getMatCode() + ")库存不足！";
			}
		}
		return null;
	}
	
	/**
	 * 
	 * <li>说明：根据【质量反馈单】出库
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveEntryAccount(MatBackSupplyStation matBackSupplyStation) throws BusinessException, NoSuchFieldException {
		// 检验库存信息是否满足出库要求
		String errorMsg = this.checkMatStockQty(matBackSupplyStation);
		if (null != errorMsg) {
			throw new StockLackingException(errorMsg);
		}
		
		// 库存信息
		MatStock matStock = matStockManager.getModel(matBackSupplyStation.getWhIdx(), matBackSupplyStation.getMatCode());
		// 更新库存数量
		matStock.setQty(matStock.getQty() - matBackSupplyStation.getQty());
		matStockManager.saveOrUpdate(matStock);
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
			MatBackSupplyStation entity = this.getModelById(idx);
			if (IBillStatus.CONST_STR_STATUS_ZC.equals(entity.getStatus())) {
				errorMsg.add("质量反馈单已经回滚，请刷新页面后重试！");
			}
			if (!SqlFilter.isInLastMonth(entity.getFeedBackDate())) {
				errorMsg.add("反馈日期超过一个月的单据不能回滚");
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
			MatBackSupplyStation entity = this.getModelById(idx);
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
	private void updateRollBack(MatBackSupplyStation entity) throws BusinessException, NoSuchFieldException {
		// 库存信息
		MatStock matStock = matStockManager.getModel(entity.getWhIdx(), entity.getMatCode());
		// 更新库存数量
		matStock.setQty(matStock.getQty() + entity.getQty());
		matStockManager.saveOrUpdate(matStock);
	}
	
}