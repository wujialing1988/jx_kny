package com.yunda.jx.pjjx.repairline.manager;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.repairline.entity.PartsRepairLine;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRepairLine业务类,配件检修流水线
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsRepairLineManager")
public class PartsRepairLineManager extends JXBaseManager<PartsRepairLine, PartsRepairLine>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /** PartsWorkStation业务类,配件检修工位 */
    @Resource
    private PartsWorkStationManager partsWorkStationManager;
	
	/**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：程梅
	 * <li>创建日期：2015-10-16
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
	 * <li>创建人：程梅
	 * <li>创建日期：2015-10-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(PartsRepairLine entity) throws BusinessException {
		return null;
	}
    
    /**
     * <li>说明：级联删除配件检修工位
     * <li>创建人：何涛
     * <li>创建日期：2016-01-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */ 
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        // 级联删除配件检修工位
        this.partsWorkStationManager.deleteByRepairLineIds(ids);
        super.logicDelete(ids);
    }
}