package com.yunda.frame.baseapp.todojob;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.SearchEntity;
import com.yunda.sb.fault.entity.FaultOrder;
import com.yunda.sb.fault.manager.FaultOrderManager;
import com.yunda.sb.inspect.plan.entity.InspectPlan;
import com.yunda.sb.inspect.plan.manager.InspectPlanManager;
import com.yunda.sb.repair.process.entity.RepairTaskListBean;
import com.yunda.sb.repair.process.manager.RepairTaskListManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: 使用人确认待办项
 * <li>创建人：何涛
 * <li>创建日期：2017年2月27日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public class UntreatedJobForSyrqr extends UnTreatedJobPermissionManager<TodoJob, TodoJob> implements IUntreatedJob {

	/** 使用人确认查询菜单url路径 */
	private final String url = "/jsp/sb/userconfirm/UserConfirm.jsp";

	/**
	 * <li>说明：获取待办事宜 
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月27日
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @return 待办事宜
	 */
	@Override
	public TodoJob getJob(String operatorid) {
		if (!super.checkPermission(Long.parseLong(operatorid), FUNC_SYRQR_NAME)) {
			return null;
		}
		int count = getCount();
		if (count <= 0) {
			return null;
		}
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_SYRQR_NAME);
		tdj.setJobText("待处理(" + count + ")");
		tdj.setJobUrl(url);
		tdj.setJobNum(String.valueOf(count));
		return tdj;
	}

	/**
	 * <li>说明：获取待处理任务数量
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 待处理任务数量 
	 */
	private int getCount() {
		// 设备检修
		RepairTaskListManager repairTaskListManager = (RepairTaskListManager) super.getBean("repairTaskListManager");
		SearchEntity<RepairTaskListBean> se0 = new SearchEntity<RepairTaskListBean>(new RepairTaskListBean(), 0, 1, null);
		Integer total = repairTaskListManager.taskListForUser(se0).getTotal();
		// 设备巡检
		InspectPlanManager inspectPlanManager = (InspectPlanManager) super.getBean("inspectPlanManager");
		SearchEntity<InspectPlan> s1 = new SearchEntity<InspectPlan>(new InspectPlan(), 0, 1, null);
		total += inspectPlanManager.queryPageList2User(s1).getTotal();
		// 故障提票
		FaultOrderManager faultOrderManager = (FaultOrderManager) super.getBean("faultOrderManager");
		SearchEntity<FaultOrder> s2 = new SearchEntity<FaultOrder>(new FaultOrder(), 0, 1, null);
		total += faultOrderManager.queryPageList2User(s2).getTotal();
		return total;
	}

}
