package com.yunda.jx.scdd.yearplan.manager;

import org.springframework.stereotype.Service;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlan业务类,机车年度检修计划
 * <li>创建人：郝凤
 * <li>创建日期：2016-10-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息事业部检修系统项目组
 * @version 1.0
 */ 
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.scdd.yearplan.entity.TrainYearPlan;
@Service(value="trainYearPlanManager")
public class TrainYearPlanManager extends JXBaseManager<TrainYearPlan, TrainYearPlan>{
	/**
	 * <li>说明：新增年计划操作前验证
	 * <li>创建人：郝凤
	 * <li>创建日期：2016-10-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(TrainYearPlan t){
		String trainTypeIdx=t.getTrainTypeIdx();
		String typeShortName=t.getTrainTypeShortName();
		String dateYear=t.getDateYear();
        String hql=" from TrainYearPlan where trainTypeIdx='" + trainTypeIdx + "' and dateYear='"+ dateYear + "' and recordStatus=0 and repairClassIdx='"
        + t.getRepairClassIdx() +"'";
        if(null != t.getIdx() && !"".equals(t.getIdx())){
            hql+=" and idx !='"+ t.getIdx() +"'";
        }
		if(find(hql).size()>0){
			return new String[]{"车型"+typeShortName+"在"+dateYear+"年已有年计划！"};
		}
		return null;
	}
}
