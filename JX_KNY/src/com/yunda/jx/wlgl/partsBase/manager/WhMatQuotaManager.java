package com.yunda.jx.wlgl.partsBase.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.WarehouseManager;
import com.yunda.jx.wlgl.partsBase.entity.WhMatQuota;
import com.yunda.jx.wlgl.stockmanage.manager.MatStockManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WhMatQuota业务类,库房保有量
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-09-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="whMatQuotaManager")
public class WhMatQuotaManager extends JXBaseManager<WhMatQuota, WhMatQuota>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	//物料清单业务类
	private MatTypeListManager matTypeListManager;
	//库存台账业务类
	private MatStockManager matStockManager;
	
	/** Warehouse业务类,库房基本信息 */
	@Resource
	WarehouseManager warehouseManager;
	
	/**
	 * 
	 * <li>说明：批量保存保有量信息
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whIdx 库房id
     * @param quotaList 保有量信息
	 */
	public void saveWhMatQuotaBatch(String whIdx,WhMatQuota[] quotaList) throws BusinessException, NoSuchFieldException {
		Warehouse warehouse = warehouseManager.getModelById(whIdx);
		if (null == warehouse) {
			throw new NullPointerException("数据异常，系统没有查询到正在操作的库房信息！");
		}
		AcOperator ac = SystemContext.getAcOperator();
		List<WhMatQuota> list = new ArrayList<WhMatQuota>();
		for(WhMatQuota quota : quotaList){
			quota.setWhIdx(whIdx);
			quota.setWhName(warehouse.getWareHouseName());
			quota.setMaintainEmp(ac.getOperatorname());
			quota.setMaintainDate(new Date());
			list.add(quota);
		}
		this.saveOrUpdate(list);
	}
	/**
	 * 
	 * <li>说明：批量添加前验证
	 * <li>创建人：程梅
	 * <li>创建日期：2014-9-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
     * @param whIdx 库房id
     * @param quotaList 保有量信息
	 */
	public void validateBatch(String whIdx, WhMatQuota[] quotaList) {
		for(WhMatQuota quota : quotaList){
			StringBuffer hql = new StringBuffer("from WhMatQuota where whIdx='").append(whIdx).append("' and matCode='").append(quota.getMatCode()).append("'");
			List list = daoUtils.find(hql.toString());
			if(list != null && list.size() > 0){
                throw new BusinessException("物料【"+ quota.getMatCode() +"】在此库房中已维护！");
			}
		}
	}
    
    /**
     * <li>说明：添加前验证
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workplaceCode
     * @param quotaList
     */
    public void validateBatchForWorkPlace(String workplaceCode, WhMatQuota[] quotaList) {
        for(WhMatQuota quota : quotaList){
            StringBuffer hql = new StringBuffer("from WhMatQuota where workplaceCode='").append(workplaceCode).append("' and matCode='").append(quota.getMatCode()).append("'");
            List list = daoUtils.find(hql.toString());
            if(list != null && list.size() > 0){
                throw new BusinessException("物料【"+ quota.getMatCode() +"】在此站点中已维护！");
            }
        }
    }
    
	public MatTypeListManager getMatTypeListManager() {
		return matTypeListManager;
	}
	public void setMatTypeListManager(MatTypeListManager matTypeListManager) {
		this.matTypeListManager = matTypeListManager;
	}
	public MatStockManager getMatStockManager() {
		return matStockManager;
	}
	public void setMatStockManager(MatStockManager matStockManager) {
		this.matStockManager = matStockManager;
	}
    /**
     * 
     * <li>说明：根据库房id、物料编码、物料类型查询库房保有量信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param whIdx 库房id
     * @param matCode 物料编码
     * @param matType 物料类型
     * @return 保有量信息
     */
    public WhMatQuota getMatQuota(String whIdx, String matCode, String matType) {
        String hql = "From WhMatQuota Where whIdx = ? And matCode = ? And matType = ? ";
        List list = this.daoUtils.find(hql, new Object[]{ whIdx, matCode, matType });
        if (null != list && list.size() > 0) {
            return (WhMatQuota)list.get(0);
        }
        return null;
    }
    
    /**
     * <li>说明：保存物料
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workplaceCode
     * @param workplaceName
     * @param quotaList
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveWhMatQuotaBatchForWorkPlace(String workplaceCode, String workplaceName, WhMatQuota[] quotaList) throws BusinessException, NoSuchFieldException {
        AcOperator ac = SystemContext.getAcOperator();
        List<WhMatQuota> list = new ArrayList<WhMatQuota>();
        for(WhMatQuota quota : quotaList){
            quota.setWorkplaceCode(workplaceCode);
            quota.setWorkplaceName(workplaceName);
            quota.setMaintainEmp(ac.getOperatorname());
            quota.setMaintainDate(new Date());
            list.add(quota);
        }
        this.saveOrUpdate(list);
        
    }

}