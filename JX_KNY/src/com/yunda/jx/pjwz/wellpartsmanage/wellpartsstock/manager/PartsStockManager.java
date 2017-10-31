package com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.PartsStock;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明:PartsStock业务类，良好配件库存信息
 * <li>创建人：程梅
 * <li>创建日期：2014-5-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partsStockManager")
public class PartsStockManager extends JXBaseManager<PartsStock, PartsStock>{
    
	/**
	 * <li>说明：根据配件id查询库存信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-5-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public PartsStock getByPartsAccountIDX(String partsAccountIDX){
		String hql = "From PartsStock where recordStatus=0 and partsAccountIDX='"+partsAccountIDX+"'";
		PartsStock stock = (PartsStock)daoUtils.findSingle(hql);
		return stock;
	}
}