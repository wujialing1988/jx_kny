package com.yunda.jx.scdd.enforceplan.manager;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetailHc;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：trainEnforcePlanHc业务类,货车车辆检修计划详情
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainEnforcePlanDetailHcManager")
public class TrainEnforcePlanDetailHcManager extends JXBaseManager<TrainEnforcePlanDetailHc, TrainEnforcePlanDetailHc>{
    
	
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
	public String[] validateUpdate(TrainEnforcePlanDetailHc t) {
		String[] errorMsg = super.validateUpdate(t);
		if (null != errorMsg) {
			return errorMsg;
		}
        String hql = "From TrainEnforcePlanDetailHc Where recordStatus = 0 And trainTypeIDX = ? and planIdx = ? ";
        TrainEnforcePlanDetailHc planHc = (TrainEnforcePlanDetailHc)this.daoUtils.findSingle(hql, new Object[]{t.getTrainTypeIDX(),t.getPlanIdx()});
        if (null != planHc && !planHc.getIdx().equals(t.getIdx())) {
            return new String[]{"车型：" + t.getTrainTypeShortName() + "已经存在!"};
        }
        return null;
	}

	  /**
     * <li>说明：查询货车月计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数
     * @param limit 限制条数
     * @param planIdx 主表id
     * @return
     */  
	 public Page<Map<String, Object>> findTrainEnforcePlanDetailHcList(int start,int limit,String planIdx){
	        String sql = SqlMapUtil.getSql("kny-repairwarning:findTrainEnforcePlanDetailHcList");
	        // 段修年月
	        if(!StringUtil.isNullOrBlank(planIdx)){
	        	sql += " and d.plan_idx = '"+ planIdx + "'";
	        }
	        sql += " order by d.update_time ";
	        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
	        return this.queryPageMap(totalSql, sql, start, limit, false);
	    }
	
}