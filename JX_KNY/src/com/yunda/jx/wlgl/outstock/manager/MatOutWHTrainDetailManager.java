package com.yunda.jx.wlgl.outstock.manager;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.backwh.entity.MatBackWHDetail;
import com.yunda.jx.wlgl.expend.manager.MatTrainExpendAccountManager;
import com.yunda.jx.wlgl.outstock.entity.MatOutWHTrainDetail;
import com.yunda.jx.wlgl.stockmanage.StockLackingException;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatOutWHTrainDetail业务类,机车用料明细
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 下午02:16:29
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="matOutWHTrainDetailManager")
public class MatOutWHTrainDetailManager extends JXBaseManager<MatOutWHTrainDetail, MatOutWHTrainDetail>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** MatTrainExpendAccount业务类,机车用料消耗记录 */
	@Resource
	private MatTrainExpendAccountManager matTrainExpendAccountManager;
	
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
	public String[] validateUpdate(MatOutWHTrainDetail entity) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param searchEntity 查询实体
	 * @return 
	 */
	public Page<MatOutWHTrainDetail> findPageList(SearchEntity<MatOutWHTrainDetail> searchEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("From MatOutWHTrainDetail Where recordStatus = 0");
		MatOutWHTrainDetail entity = searchEntity.getEntity();
		if (!StringUtil.isNullOrBlank(entity.getMatOutWHTrainIDX())) {
			sb.append(" And matOutWHTrainIDX = '").append(entity.getMatOutWHTrainIDX()).append("'");
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
	 * <li>说明：根据机车用料单主键查询机车用料明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-26
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param matOutWHTrainIDX 机车用料单主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MatOutWHTrainDetail> getModelsByMatOutWHTrainIDX (String matOutWHTrainIDX) {
		String hql = "From MatOutWHTrainDetail Where recordStatus = 0 And matOutWHTrainIDX = ?";
		return this.daoUtils.find(hql, new Object[]{ matOutWHTrainIDX });
	}
	
	/**
	 * <li>说明： 退库时，更新用料单的退库数量
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param detail 退库明细
	 * @return 
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public boolean saveBackWh(MatBackWHDetail detail) throws BusinessException, NoSuchFieldException {
		MatOutWHTrainDetail entity = this.getModelById(detail.getMatOutWhDatailIdx());
		if (null == entity) {
			return false;
		}
		// 设置“退库数量”
		if (null == entity.getBackQty()) {
			entity.setBackQty(detail.getQty());
		} else {
			entity.setBackQty(detail.getQty() + entity.getBackQty());
		}
		// 验证是否退库数量大于了出库数量
		if (entity.getQty() < entity.getBackQty()) {
			throw new StockLackingException("退库数量大于出库数量，请刷新页面后重试！");
		}
		this.saveOrUpdate(entity);
		// 修改“机车检修用料记录”的出库数量
		this.matTrainExpendAccountManager.saveBackWh(entity);
		return true;
	}
	
	/**
	 * <li>说明： 回滚时，更新用料单的退库数量
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param detail 退库明细
	 * @return 
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */
	public boolean updateRollBack(MatBackWHDetail detail) throws BusinessException, NoSuchFieldException {
		MatOutWHTrainDetail entity = this.getModelById(detail.getMatOutWhDatailIdx());
		if (null == entity) {
			return false;
		}
		// 退库数量 = 已退库数量 - 回滚数量
		entity.setBackQty(entity.getBackQty() - detail.getQty());
		// 验证是否退库数量大于了出库数量
		if (entity.getQty() < entity.getBackQty()) {
			throw new StockLackingException("数据异常：退库数量大于出库数量，请刷新页面后重试！");
		}
		this.saveOrUpdate(entity);
		return true;
	}
	
}