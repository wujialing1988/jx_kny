package com.yunda.frame.baseapp.todojob;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.SearchEntity;
import com.yunda.sb.repair.process.entity.RepairTaskListBean;
import com.yunda.sb.repair.process.manager.RepairTaskListManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明: 工长确认待办项
 * <li>创建人：黄杨
 * <li>创建日期：2017年3月24日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备系统项目组
 * @version 1.0
 */
public class UntreatedJobForGzqr extends UnTreatedJobPermissionManager<TodoJob, TodoJob> implements IUntreatedJob {

	@Override
	public TodoJob getJob(String operatorid) {
		if (!super.checkPermission(Long.parseLong(operatorid), FUNC_GZQR_NAME)) {
			return null;
		}
		int count = getCount();
		if (count <= 0) {
			return null;
		}
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_GZQR_NAME);
		tdj.setJobText("待处理(" + count + ")");
		tdj.setJobNum(String.valueOf(count));
		return tdj;
	}

	/**
	 * <li>说明：获取待办项数量
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年3月24日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 待处理任务数量
	 */
	private int getCount() {
		RepairTaskListManager repairTaskListManager = (RepairTaskListManager) super.getBean("repairTaskListManager");
		String orgid = String.valueOf(SystemContext.getOmEmployee().getOrgid());
		SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(new RepairTaskListBean(), 0, 1, null);
		return repairTaskListManager.taskListForGzSign(searchEntity, orgid).getTotal();
	}

}
