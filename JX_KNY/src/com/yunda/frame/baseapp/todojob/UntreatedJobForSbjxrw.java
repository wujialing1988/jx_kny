package com.yunda.frame.baseapp.todojob;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.SearchEntity;
import com.yunda.sb.repair.process.entity.RepairTaskListBean;
import com.yunda.sb.repair.process.manager.RepairTaskListManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: 设备计划修待办项
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月22日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public class UntreatedJobForSbjxrw extends UnTreatedJobPermissionManager<TodoJob, TodoJob> implements IUntreatedJob {

	
	/** 设备计划处理url路径 */
	private final String url = "/jsp/sb/repair/task/dispose/RepairTaskList.jsp";

	/**
	 * <li>说明：获取待办事宜 
	 * <li>创建人：何涛
	 * <li>创建日期：2017年1月12日
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @return 待办事宜
	 */
	@Override
	public TodoJob getJob(String operatorid) {
		if (!super.checkPermission(Long.parseLong(operatorid), FUNC_SBJXRW_NAME)) {
			return null;
		}
		int count = getCount();
		if (count <= 0) {
			return null;
		}
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_SBJXRW_NAME);
		tdj.setJobText("待处理(" + count + ")");
		tdj.setJobUrl(url);
		tdj.setJobNum(String.valueOf(count));
		return tdj;
	}

	/**
	 * <li>说明：获取待处理任务数量
	 * <li>创建人：何涛
	 * <li>创建日期：2017年1月12日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 待处理任务数量 
	 */
	private int getCount() {
		RepairTaskListManager repairTaskListManager = (RepairTaskListManager) super.getBean("repairTaskListManager");
		Long orgid = SystemContext.getOmEmployee().getOrgid();
		SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(new RepairTaskListBean(), 0, 1, null);
		return repairTaskListManager.taskList(searchEntity, orgid.toString()).getTotal();
	}

}
