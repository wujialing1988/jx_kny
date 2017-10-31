package com.yunda.sb.repair.process.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.repair.process.entity.RepairScopeCase;
import com.yunda.sb.repair.process.entity.RepairTaskList;
import com.yunda.sb.repair.process.entity.RepairTaskListTeam;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairTaskListTeam管理器，数据表：E_REPAIR_TASK_LIST_TEAM
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service
public class RepairTaskListTeamManager extends JXBaseManager<RepairTaskListTeam, RepairTaskListTeam> implements IStateWatcher {

	/** RepairScopeCase管理器，数据表：E_REPAIR_SCOPE_CASE */
	@Resource
	private RepairScopeCaseManager repairScopeCaseManager;

	/**
	 * <li>说明：验证检修任务单是否被所有工长确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单idx主键
	 * @return true：所有工长已确认，false：还有工长未确认
	 */
	public boolean isAllChecked(String taskListIdx) {
		if (StringUtil.isNullOrBlank(taskListIdx)) {
			return false;
		}
		String hql = "From RepairTaskListTeam Where taskListIdx = ? And isConfirmed <> ?";
		int count = this.daoUtils.getCount(hql, taskListIdx, RepairTaskListTeam.IS_CONFIRMED_YES);
		return count <= 0;
	}

	/**
	 * <li>说明：根据检修任务单获取检修任务单处理班组集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：何涛
	 * <li>修改内容：2017年1月12日
	 * <li>修改日期：修改方法实现，增加方法参数orgId空值验证，如果为空则查询该检修任务单所有可处理班组信息
	 * @param taskListIdx 检修任务单idx主键
	 * @param orgId 班组id
	 * @return 检修任务单处理班组集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairTaskListTeam> getModels(String taskListIdx, String orgId) {
		String hql = "From RepairTaskListTeam Where taskListIdx = ?";
		if (null != orgId) {
			hql += " And concat(',', orgId, ',') Like ',%" + orgId + "%,'";
		}
		return this.daoUtils.find(hql, taskListIdx);
	}

	/**
	 * <li>说明：根据检修任务单获取检修任务单处理班组集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年2月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单idx主键
	 * @return 检修任务单处理班组
	 */
	@SuppressWarnings("unchecked")
	public List<RepairTaskListTeam> getModelsByTaskListIdx(String taskListIdx) {
		String hql = "From RepairTaskListTeam Where taskListIdx = ?";
		return this.daoUtils.find(hql, taskListIdx);
	}

	/**
	 * <li>说明：根据检修任务单获取检修任务单处理班组集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单idx主键
	 * @param repairType 检修类型
	 * @return 检修任务单处理班组
	 */
	public RepairTaskListTeam getModel(String taskListIdx, Integer repairType) {
		String hql = "From RepairTaskListTeam Where taskListIdx = ? And repairType = ?";
		return (RepairTaskListTeam) this.daoUtils.findSingle(hql, taskListIdx, repairType);
	}

	/**
	 * <li>说明：生成检修任务处理作业班组实例
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月27日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param team 检修任务单处理班组实例对象
	 * @param RepairTaskList 检修任务单实例对象
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @throws NoSuchFieldException
	 */
	public void insert4Publish(RepairTaskListTeam team, RepairTaskList task, int repairType) throws NoSuchFieldException {
		RepairTaskListTeam entity = new RepairTaskListTeam();
		entity.setOrgId(team.getOrgId());
		entity.setOrgName(team.getOrgName());
		entity.setRepairType(repairType);
		entity.setTaskListIdx(task.getIdx());
		entity.setIsConfirmed(RepairTaskListTeam.IS_CONFIRMED_NO);
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：维修班组开工，是施修人初次扫码施修时间
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单idx主键
	 * @param realBeginTime 开工日期
	 * @throws NoSuchFieldException 
	 */
	public void updateRealBeginTime(String taskListIdx, Date realBeginTime) throws NoSuchFieldException {
		String orgid = SystemContext.getOmEmployee().getOrgid().toString();
		String hql = "From RepairTaskListTeam Where taskListIdx = ? And orgId = ? And realBeginTime Is Null";
		@SuppressWarnings("unchecked")
		List<RepairTaskListTeam> entityList = this.daoUtils.find(hql, taskListIdx, orgid);
		if (null == entityList || entityList.isEmpty()) {
			return;
		}
		for (RepairTaskListTeam entity : entityList) {
			if (null != entity.getRealBeginTime()) {
				continue;
			}
			entity.setRealBeginTime(realBeginTime);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：更新检修任务单处理班组状态，设置状态为“工长确认 - 待确认【2】”
	 * <li>创建人：何涛
	 * <li>创建日期：2017年4月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单idx主键
	 * @param repairType 检修类型
	 */
	@Override
	public void updateState(Serializable taskListIdx, Integer repairType) {
		List<RepairScopeCase> entityList = this.repairScopeCaseManager.getModelsByTaskListIdx((String) taskListIdx, RepairScopeCase.STATE_WCL, repairType);
		if (null != entityList && !entityList.isEmpty()) {
			return;
		}
		// 由于机械和电气是分开处理检修任务的，那么维修工长也相应的要分开对机械或者电气的范围活进行确认，因此在范围活被处理完成后，设置标识表示该班组工长可以进行“工长”确认了
		RepairTaskListTeam entity = this.getModel((String) taskListIdx, repairType);
		entity.setIsConfirmed(RepairTaskListTeam.IS_CONFIRMED_TODO);
		try {
			this.saveOrUpdate(entity);
		} catch (NoSuchFieldException e) {
			throw new BusinessException(e);
		}
	}

}
