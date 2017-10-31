package com.yunda.sb.repair.process.manager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.repair.process.entity.RepairScopeCase;
import com.yunda.sb.repair.process.entity.RepairScopeCaseBean;
import com.yunda.sb.repair.process.entity.RepairTaskList;
import com.yunda.sb.repair.process.entity.RepairWorkOrder;
import com.yunda.sb.repair.scope.entity.RepairScope;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScopeCase管理器，数据表：E_REPAIR_SCOPE_CASE
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service(value = "repairScopeCaseManager")
public class RepairScopeCaseManager extends JXBaseManager<RepairScopeCase, RepairScopeCase> implements IStateWatched {

	/** RepairWorkOrder管理器，数据表：E_REPAIR_WORK_ORDER */
	@Resource
	private RepairWorkOrderManager repairWorkOrderManager;

	/** RepairTaskListManager，数据表：E_REPAIR_TASK_LIST */
	@Resource
	private RepairTaskListManager repairTaskListManager;

	/** RepairTaskListTeam管理器，数据表：E_REPAIR_TASK_LIST_TEAM */
	@Resource
	private RepairTaskListTeamManager repairTaskListTeamManager;

	/** 状态更新观察者集合 */
	private List<IStateWatcher> watchers;

	/**
	 * <li>说明：生成设备检修范围实例
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param task 检修任务单实体对象
	 * @param scopeList 设备检修范围集合，有且仅包含【一种】检修类型（1：机械、2：电气、3：其它）
	 * @throws NoSuchFieldException 
	 */
	public void insert4Publish(RepairTaskList task, List<RepairScope> scopeList) throws NoSuchFieldException {
		// 排序
		Collections.sort(scopeList);
		// 生成设备检修范围实例
		RepairScopeCase entity = null;
		int i = 0; // 顺序号临时变量
		for (RepairScope scope : scopeList) {
			// 根据修别生成相应修别的检修范围实例
			if (!scope.isValid(task.getRepairClassName())) {
				continue;
			}
			entity = new RepairScopeCase();
			entity.taskListIdx(task.getIdx()) // 检修任务单主键
					.scopeDefineIdx(scope.getIdx()) // 范围定义主键
					.seqNo(++i) // 序号
					.repairType(scope.getRepairType()) // 检修类型，1：机型、2：电气、3：其它
					.repairScopeName(scope.getRepairScopeName()) // 检修范围名称
					.remark(scope.getRemark()); // 备注
			entity.setState(RepairScopeCase.STATE_WCL);
			this.saveOrUpdate(entity);

			// 生成设备检修作业工单（来源设备检修范围维护的作业内如项）
			this.repairWorkOrderManager.insert4Publish(entity);
		}
	}

	/**
	 * <li>说明：分页查询-作业人员待处理的设备检修范围活
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @param orgId 班组id
	 * @return 分页列表
	 */
	public Page<RepairScopeCaseBean> queryPageList(SearchEntity<RepairScopeCase> searchEntity, String orgId) {
		RepairScopeCase entity = searchEntity.getEntity();
		String taskListIdx = entity.getTaskListIdx();
		if (StringUtil.isNullOrBlank(taskListIdx)) {
			throw new NullPointerException("设备检修范围活分页查询必须指定检修任务单主键（taskListIdx）");
		}
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_scope_case:query_page_list", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);

		// 查询条件 - 检修类型，1：机型、2：电气、3：其它
		if (null != entity.getRepairType()) {
			sb.append(" AND T.REPAIR_TYPE = '").append(entity.getRepairType()).append("'");
		}

		// 查询条件 - 检修任务单idx主键
		sb.append(" AND T.TASK_LIST_IDX = '").append(taskListIdx).append("'");

		// 查询条件 - 作业班组
		if (!StringUtil.isNullOrBlank(orgId)) {
			sb.append(" AND (',' || M.ORG_ID || ',') LIKE ',%").append(orgId).append("%,'");
		}

		sb.append(" ORDER BY T.TASK_LIST_IDX ASC, T.REPAIR_TYPE ASC, T.SORT_NO ASC");

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sql.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairScopeCaseBean.class);
	}

	/**
	 * <li>说明：更新检修范围实例状态为已处理
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeCaseIdx
	 * @throws NoSuchFieldException
	 */
	public void updateFinish(String idx) throws NoSuchFieldException {
		RepairScopeCase entity = this.getModelById(idx);
		entity.setState(RepairScopeCase.STATE_YCL);
		// 重写保存方法，在检修任务单所有作业范围均已处理完成后，自动更新检修任务单状态为已处理
		this.saveOrUpdate(entity);

		// 初始化更新观察者观察者
		if (null == this.watchers) {
			this.watchers = new ArrayList<IStateWatcher>();
			watchers.add(this.repairTaskListManager);
			watchers.add(this.repairTaskListTeamManager);
		}

		// 在检修任务单所有作业范围均已处理完成后，自动更新检修任务单状态为已处理
		// Modified by hetao on 2017-04-14 将机械和电气工长验收完全分开，即：只要某一类型的检修活项被处理完成，该类活项的处理班组工长就可以进行验收
		this.notifyWatchers(entity.getTaskListIdx(), entity.getRepairType());
	}

	/**
	 * <li>说明：根据检修任务单主键获取检修范围实例集合
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单主键
	 * @return 检修范围实例集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairScopeCase> getModelsByTaskListIdx(String taskListIdx) {
		String hql = "From RepairScopeCase Where recordStatus = 0 And taskListIdx = ?";
		return this.daoUtils.find(hql, taskListIdx);
	}

	/**
	 * <li>说明：根据检修任务单主键获取检修范围实例集合
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单主键
	 * @param state 处理状态（未处理、已处理）
	 * @param repairType 检修类型（可以为null)
	 * @return 检修范围实例集合
	 */
	@SuppressWarnings("unchecked")
	protected List<RepairScopeCase> getModelsByTaskListIdx(String taskListIdx, String state, Integer repairType) {
		String hql = "From RepairScopeCase Where recordStatus = 0 And taskListIdx = ? And state = ?";
		if (null != repairType) {
			hql += " And repairType = ?";
			return this.daoUtils.find(hql, taskListIdx, state, repairType);
		}
		return this.daoUtils.find(hql, taskListIdx, state);
	}

	/**
	 * <li>说明：获取检修作业处理情况饼图数据源
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单主键
	 * @return 检修作业处理情况饼图数据源
	 */
	public List<Map<String, Object>> queryChartData(String taskListIdx) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<RepairScopeCase> entityList = this.getModelsByTaskListIdx(taskListIdx, RepairScopeCase.STATE_YCL, null);
		map.put("K", "已处理");
		map.put("V", entityList.size());
		result.add(map);
		map = new HashMap<String, Object>();
		entityList = this.getModelsByTaskListIdx(taskListIdx, RepairScopeCase.STATE_WCL, null);
		// 未巡检记录数
		map.put("K", "未处理");
		map.put("V", entityList.size());
		result.add(map);
		return result;
	}

	/**
	 * <li>说明：级联删除设备检修工单
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 设备检修作业工单idx主键数组
	 */
	@Override
	public void deleteByIds(Serializable... ids) {
		Serializable[] array = null;
		List<RepairWorkOrder> list = null;
		for (Serializable idx : ids) {
			list = this.repairWorkOrderManager.getModelsByScopeCaseIdx((String) idx);
			if (null == list || list.isEmpty()) {
				continue;
			}
			array = new String[list.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list.get(i).getIdx();
			}
			this.repairWorkOrderManager.deleteByIds(array);
		}
		super.deleteByIds(ids);
	}

	/* (non-Javadoc)
	 * @see com.yunda.sbgl.repair.process.manager.IStateWatched#addWatcher(com.yunda.sbgl.repair.process.manager.IStateWatcher)
	 */
	@Override
	public void addWatcher(IStateWatcher watcher) {
		if (!watchers.contains(watcher)) {
			watchers.add(watcher);
		}
	}

	/* (non-Javadoc)
	 * @see com.yunda.sbgl.repair.process.manager.IStateWatched#removeWatcher(com.yunda.sbgl.repair.process.manager.IStateWatcher)
	 */
	@Override
	public void removeWatcher(IStateWatcher watcher) {
		if (watchers.contains(watcher)) {
			watchers.remove(watcher);
		}
	}

	/* (non-Javadoc)
	 * @see com.yunda.sbgl.repair.process.manager.IStateWatched#notifyWatchers(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void notifyWatchers(String taskListIdx, Integer repairType) {
		if (null == watchers || watchers.isEmpty()) {
			return;
		}
		for (IStateWatcher watcher : this.watchers) {
			try {
				watcher.updateState(taskListIdx, repairType);
			} catch (Exception e) {
				throw new BusinessException(e);
			}
		}
	}

}
