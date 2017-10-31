package com.yunda.frame.baseapp.todojob;

import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.sb.fault.manager.FaultOrderManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明: 设备故障处理待办项
 * <li>创建人：黄杨
 * <li>创建日期：2017年3月9日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备系统项目组
 * @version 1.0
 */
public class UntreatedJobForGzcl extends UnTreatedJobPermissionManager<TodoJob, TodoJob> implements IUntreatedJob {

	/**
	 * <li>说明：获取待办事宜
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年3月9日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @return 待办事宜
	 */
	@Override
	public TodoJob getJob(String operatorid) {
		if (!super.checkPermission(Long.parseLong(operatorid), FUNC_GZCL_NAME)) {
			return null;
		}
		int count = getCount();
		if (count <= 0) {
			return null;
		}
		TodoJob tdj = new TodoJob();
		tdj.setJobType(FUNC_GZCL_NAME);
		tdj.setJobText("待处理(" + count + ")");
		tdj.setJobUrl(null);
		tdj.setJobNum(String.valueOf(count));
		return tdj;
	}

	/**
	 * <li>说明：获取待处理任务数量
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年3月9日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 待处理任务数量
	 */
	private int getCount() {
		FaultOrderManager faultOrderManager = (FaultOrderManager) super.getBean("faultOrderManager");
		return faultOrderManager.getGzclCount();
	}

}
