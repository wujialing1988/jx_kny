package com.yunda.jx.wlgl.stockmanage.manager;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.stockmanage.entity.MatStock;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MATSTOCK业务类,物料库存台账
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-12
 * <li>修改人: 何涛
 * <li>修改日期：2014-10-13
 * <li>修改内容：将各个业务模块的对物料库存台账的更新方法重构到相应的业务管理器中，减少类间的耦合度
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="matStockManager")
public class MatStockManager extends JXBaseManager<MatStock, MatStock>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：列表信息【用于物料选择控件】
	 * <li>创建人：程梅
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<MatStock> findPageList(SearchEntity<MatStock> searchEntity, String matCode, String whIdx, String queryHql, String query)
	    throws BusinessException {
		    String totalHql = "select count(*) from MatStock  where 1=1";
		    String hql = "from MatStock where 1=1";
		    // 拼接查询sql，如queryHql配置项不为空则直接执行queryHql
		    if (!StringUtil.isNullOrBlank(queryHql)) {
		        hql = queryHql;
		        int beginPos = hql.toLowerCase().indexOf("from");
		        totalHql = " select count (*) " + hql.substring(beginPos);
		    }
		    StringBuilder awhere = new StringBuilder();
		    if (!"".equals(matCode)) {
		        awhere.append(" and matCode like '%").append(matCode).append("%'");
		    }
		    if (!StringUtil.isNullOrBlank(whIdx)) {
		        awhere.append(" and whIdx like '%").append(whIdx).append("%'");
		    }
		    // 关键字查询
		    if (!"".equals(query)) {
		        awhere.append(" and matCode like '%").append(query).append("%'");
		    }
		    Order[] orders = searchEntity.getOrders();
		    awhere.append(HqlUtil.getOrderHql(orders));
		    totalHql += awhere.toString();
		    hql += awhere.toString();
		    return super.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/**
	 * <li>说明：根据“库房idx主键”和“物料编码”查询库存信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param whIdx 库房idx主键
	 * @param matCode 物料编码
	 * @return MatStock 物料库存台账 实体
	 */
	public MatStock getModel(String whIdx, String matCode) {
		String hql = "From MatStock Where whIdx = ? And matCode = ?";
		List list = this.daoUtils.find(hql, new Object[]{ whIdx, matCode });
		if (null != list && list.size() > 0) {
			return (MatStock)list.get(0);
		}
		return null;
	}
	
	/**
	 * <li>说明：根据“库房idx主键”和“物料编码”检验是否已经有该物料的库存信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param whIdx 库房idx主键
	 * @param matCode 物料编码
	 * @return
	 */
	public boolean isExist(String whIdx, String matCode) {
		MatStock entity = this.getModel(whIdx, matCode);
		return null == entity ? false : true;
	}

	/**
	 * <li>说明：重写更新方法，增加业务处理的同步控制
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param entityList 更新的实体对象列表
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */	
	@Override
	public synchronized void saveOrUpdate(List<MatStock> entityList) throws BusinessException, NoSuchFieldException {
		super.saveOrUpdate(entityList);
	}
	@Override
	
	/**
	 * <li>说明：重写更新方法，增加业务处理的同步控制
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param t 更新的实体对象
	 * @throws BusinessException
	 * @throws NoSuchFieldException
	 */	
	public synchronized void saveOrUpdate(MatStock t) throws BusinessException, NoSuchFieldException {
		super.saveOrUpdate(t);
	}	
	
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
	public String[] validateUpdate(MatStock entity) throws BusinessException {
		return super.validateUpdate(entity);
	}
	

	/**
	 * <li>说明：增加库存时 - 检验库存信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 *
	 * @param whIdx 	库房idx主键
	 * @param matCode 	物料编码
	 * @return
	 */
	public String checkMatStockQtyForIncrease(String whIdx, String matCode) {
		MatStock matStock = this.getModel(whIdx, matCode);
		if (null == matStock) {
			throw new NullPointerException("数据异常：库房[" + whIdx + "]没有编码[" + matCode
					+ "]的库存信息.");
		}
		return null;
	}
	
	/**
	 * <li>说明：减少库存时 - 检验库存信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 *
	 * @param whIdx 	库房idx主键
	 * @param matCode 	物料编码
	 * @param qty		数量
	 * @return
	 */
	public String checkMatStockQtyForDecrease(String whIdx, String matCode,
			String matDesc, int qty) {
		MatStock matStock = this.getModel(whIdx, matCode);
		if (null == matStock) {
			throw new NullPointerException("数据异常：库房[" + whIdx + "]没有编码["
					+ matCode + "]的库存信息.");
		}
		if (matStock.getQty() < qty) {
			return "物料：" + matDesc + "(" + matCode + ")库存不足！";
		}
		return null;
	}
	
}