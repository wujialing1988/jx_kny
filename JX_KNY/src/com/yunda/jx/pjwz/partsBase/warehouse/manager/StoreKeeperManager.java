package com.yunda.jx.pjwz.partsBase.warehouse.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.StoreKeeper;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：StoreKeeper业务类,库管员
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="storeKeeperManager")
public class StoreKeeperManager extends JXBaseManager<StoreKeeper, StoreKeeper>{
	
	/**
	 * <li>说明：确定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务，单位+库房+人员应该唯一，不能重复
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(StoreKeeper entity) throws BusinessException {
		List<String> errMsg = new ArrayList<String>();
		StoreKeeper keeper = this.getModelById(entity.getIdx()); //获取当前对象在数据库中的值
		if("".equals(entity.getIdx()) || !keeper.getEmpID().equals(entity.getEmpID())){ //新增时、或者库房编码变更时验证
			String hql = "Select count(1) From StoreKeeper " +
					" where recordStatus = 0 and  empID='"+entity.getEmpID()+"'" +
//					" and siteID = '"+entity.getSiteID()+"'" + 
					" and warehouseIDX ='"+entity.getWarehouseIDX()+"'";
			int count = this.daoUtils.getCount(hql);
			if(count > 0){
				errMsg.add("库管员【"+ entity.getEmpName()+"】已存在！");
			}
		}
		if (errMsg.size() > 0) {
			String[] errArray = new String[errMsg.size()];
			errMsg.toArray(errArray);
			return errArray;
		}
		return null;
	}
	
	/**
	 * <li>说明：批量生成库管员
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return void
	 * @throws BusinessException
	 * @throws NoSuchFieldException 
	 */
	public void saveOrUpdateBatch(StoreKeeper[] keepers) throws BusinessException, NoSuchFieldException {
		for (StoreKeeper keeper : keepers) {
			this.saveOrUpdate(keeper);
		}
	}
}