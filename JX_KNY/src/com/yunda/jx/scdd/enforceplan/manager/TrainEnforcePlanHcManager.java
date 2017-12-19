package com.yunda.jx.scdd.enforceplan.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanHc;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：trainEnforcePlanHc业务类,货车车辆检修计划
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainEnforcePlanHcManager")
public class TrainEnforcePlanHcManager extends JXBaseManager<TrainEnforcePlanHc, TrainEnforcePlanHc>{
    
	
	/**
	 * <li>说明：更新前验证
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	@Override
	public String[] validateUpdate(TrainEnforcePlanHc t) {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
        String hql = "From TrainEnforcePlanHc Where recordStatus = 0 And planYear = ? And planMorth = ? ";
        TrainEnforcePlanHc planHc = (TrainEnforcePlanHc)this.daoUtils.findSingle(hql, new Object[]{t.getPlanYear(),t.getPlanMorth()});
        if (null != planHc && !planHc.getIdx().equals(t.getIdx())) {
            return new String[]{t.getPlanYear() + "年" + t.getPlanMorth() + "月 检修计划已经存在!"};
        }
        return null;
	}
    
}