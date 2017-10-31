package com.yunda.sb.repair.plan.manager;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.repair.plan.entity.RepairPlanMonth;
import com.yunda.sb.repair.plan.entity.RepairPlanMonthBean;
import com.yunda.sb.repair.plan.entity.RepairPlanYear;
import com.yunda.sb.repair.process.entity.RepairTaskListTeam;
import com.yunda.sb.repair.process.manager.RepairTaskListManager;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanMonth管理器，数据表：SBJX_REPAIR_PLAN_MONTH
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
public class RepairPlanMonthManager extends JXBaseManager<RepairPlanMonth, RepairPlanMonth> {

	/** RepairPlanYear管理器，数据表：SBJX_REPAIR_PLAN_YEAR */
	@Resource
	private RepairPlanYearManager repairPlanYearManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** RepairTaskListManager，数据表：E_REPAIR_TASK_LIST */
	@Resource
	private RepairTaskListManager repairTaskListManager;

	/**
	 * <li>说明：重写删除前的验证，对于已下发的月检修计划，不允许删除
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 设备检修月计划idx主键数组
	 * @return 设备检修年计划查询封装实体对象分页集合
	 */
	@Override
	public String[] validateDelete(Serializable... ids) {
		RepairPlanMonth t = null;
		EquipmentPrimaryInfo epi = null;
		for (Serializable idx : ids) {
			t = this.getModelById(idx);
			// 已下发的月检修计划，不允许删除
			if (RepairPlanMonth.PLAN_STATUS_YXF == t.getPlanStatus().intValue()) {
				epi = equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
				return new String[] { epi.getEquipmentName() + "(编号：" + epi.getEquipmentCode() + ")的月计划已经下发，不能删除！" };
			}
		}
		return super.validateDelete(ids);
	}

	/**
	 * <li>说明：重写删除方法，删除前应将设备检修年计划对应月的修程状态取消为“已下发”状态，即：取消设置修程代码已“0”结尾
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 设备检修月计划idx主键数组
	 * @return 设备检修年计划查询封装实体对象分页集合
	 */
	@Override
	public void logicDelete(Serializable... ids) throws NoSuchFieldException {
		RepairPlanMonth rpm = null;
		RepairPlanYear rpy = null;
		for (Serializable idx : ids) {
			rpm = this.getModelById(idx);
			if (null == rpm) {
				throw new NullPointerException("数据异常，设备检修年计划删除失败！idx[" + idx + "]");
			}
			// 设备检修年计划
			rpy = this.repairPlanYearManager.getModel(rpm.getPlanYear(), rpm.getEquipmentIdx());
			if (null == rpy) {
				continue;
			}
			String filedName = "month" + rpm.getPlanMonth();
			BeanUtils.forceSetProperty(rpy, filedName, rpm.getRepairClass());
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明：一键删除，用于系统调试
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws NoSuchFieldException
	 */
	public void deleteByOneKey() throws NoSuchFieldException {
		String hql = "From RepairPlanMonth";
		@SuppressWarnings("unchecked")
		List<RepairPlanMonth> find = this.daoUtils.find(hql);
		if (null == find || find.isEmpty()) {
			return;
		}
		RepairPlanMonth rpm = null;
		RepairPlanYear rpy = null;
		for (int i = 0; i < find.size(); i++) {
			rpm = find.get(i);
			if (null == rpm) {
				continue;
			}
			// 设备检修年计划
			rpy = this.repairPlanYearManager.getModel(rpm.getPlanYear(), rpm.getEquipmentIdx());
			if (null == rpy) {
				continue;
			}
			String filedName = "month" + rpm.getPlanMonth();
			BeanUtils.forceSetProperty(rpy, filedName, rpm.getRepairClass());
		}
		// 清空数据表
		this.daoUtils.executeSql("DELETE FROM SBJX_REPAIR_PLAN_MONTH");
	}

	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @param planStatus 查询条件 - 计划状态，多个状态已逗号进行分隔，如：-1,0,1
	 * @return 设备检修年计划查询封装实体对象分页集合
	 */
	public Page<RepairPlanMonthBean> queryPageList(SearchEntity<RepairPlanMonthBean> searchEntity, String planStatus) {
		String sql = SqlMapUtil.getSql(String.format("repair%crepair_plan_month:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		RepairPlanMonthBean entity = searchEntity.getEntity();
		// 查询条件 - 单位名称
		if (!StringUtil.isNullOrBlank(entity.getOrgId())) {
			sb.append(" AND I.ORG_ID LIKE '").append(entity.getOrgId()).append("%'");
		}
		// 查询条件 - 计划月份
		if (null != entity.getPlanMonth()) {
			sb.append(" AND T.PLAN_MONTH = '").append(entity.getPlanMonth()).append("'");
		}
		// 查询条件 - 计划年份
		if (null != entity.getPlanYear()) {
			sb.append(" AND T.PLAN_YEAR = '").append(entity.getPlanYear()).append("'");
		}
		if (!StringUtil.isNullOrBlank(planStatus)) {
			sb.append(" AND T.PLAN_STATUS IN ('").append(planStatus.replace(",", "', '")).append("')");
		}
		// 查询条件 - 设备编号
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE LIKE '%").append(entity.getEquipmentCode()).append("%'");
		}
		// 查询条件 - 设备名称
		if (!StringUtil.isNullOrBlank(entity.getEquipmentName())) {
			sb.append(" AND I.EQUIPMENT_NAME LIKE '%").append(entity.getEquipmentName()).append("%'");
		}
		sb.append(" ORDER BY T.PLAN_YEAR ASC, T.PLAN_MONTH ASC, T.BEGIN_TIME ASC, I.EQUIPMENT_NAME ASC");
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairPlanMonthBean.class);
	}

	/**
	 * <li>说明：生成全年计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年8月22日
	 * <li>修改日期：修改方法签名，将年度作为方法参数传入，生成参数指定的年度的设备检修计划
	 * @param planYear 计划年度
	 * @throws NoSuchFieldException 
	 */
	public void insertWholeMonthPlan(int planYear) throws NoSuchFieldException {
		// 计数器，某个月份没有设备检修计划，则计数+1
		int num = 0;
		for (int i = 1; i <= 12; i++) {
			try {
				this.insertMonthPlan(i, planYear);
			} catch (BusinessException e) {
				num++;
			}
		}
		// 如果计数器值为12，即全年都没有设备检修计划，则抛业务异常，用于页面提示
		if (12 == num) {
			throw new BusinessException(planYear + "年，没有可以生成的设备检修月计划！");
		}
	}

	/**
	 * <li>说明：生成当前月的下月设备检修计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws NoSuchFieldException
	 */
	public void insertNextMonthPlan() throws NoSuchFieldException {
		Calendar cal = Calendar.getInstance();
		int currentMonth = cal.get(Calendar.MONTH) + 1; // 当前月份，java中1月的索引值为0，所以+1才能表示月的自然计算方式
		int currentYear = cal.get(Calendar.YEAR); // 当前年
		int nextMonth = currentMonth + 1; // 下一月
		int year = currentYear;
		if (12 < nextMonth) { // 如果下一月的自然计算大于12，则表示数次年的1月份
			nextMonth = 1;
			year++;
		}
		// 生成设备检修月计划
		this.insertMonthPlan(nextMonth, year);
	}

	/**
	 * <li>说明：生成设备检修月计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param month 月份，1, 2, 3, 4, 5, ..., 11, 12
	 * @param year 年份，2016, 2017, 2018, ...
	 * @throws NoSuchFieldException
	 */
	public void insertMonthPlan(int month, int year) throws NoSuchFieldException {
		List<RepairPlanYear> planYearList = this.repairPlanYearManager.getModelsByMonth(month, year);
		if (null == planYearList || planYearList.isEmpty()) {
			throw new BusinessException(year + "年" + month + "月份，没有可以生成的设备检修月计划！");
		}
		RepairPlanMonth entity = null;
		List<RepairPlanMonth> entityList = new ArrayList<RepairPlanMonth>(planYearList.size());
		String filedName = "month" + month;
		// 将一个月平均的分为指定的时间片段
		List<Date> defaultDate = RepairPlanMonthManager.carveOnMonth(month, planYearList.size());
		int i = 0;
		for (RepairPlanYear rpy : planYearList) {
			entity = this.getModel(month, year, rpy.getEquipmentIdx());
			if (null != entity) {
				EquipmentPrimaryInfo epi = equipmentPrimaryInfoManager.getModelById(rpy.getEquipmentIdx());
				throw new BusinessException(year + "年" + month + "月份，已经存在" + epi.getEquipmentName() + "(编号：" + epi.getEquipmentCode() + ")的月计划，不能重复添加！");
			}
			entity = new RepairPlanMonth();
			entity.setPlanYear(year); // 设备检修年计划主键
			entity.setEquipmentIdx(rpy.getEquipmentIdx()); // 设备主键
			entity.setPlanMonth(month); // 计划月份
			Short repairClass = (Short) BeanUtils.forceGetProperty(rpy, filedName);
			entity.setRepairClass(repairClass); // 修程，1:小修、2：中修、3：项修
			// 默认设置一个开始、结束时间
			entity.setBeginTime(defaultDate.get(i));
			entity.setEndTime(defaultDate.get(++i));
			entity.setPlanStatus(RepairPlanMonth.PLAN_STATUS_WXF); // 计划状态，0：未下发
			entityList.add(entity);

			// 设置设备间检修计划中，该月修程状态为“已下发”，即：设置修程代码已“0”结尾
			BeanUtils.forceSetProperty(rpy, filedName, (short) ((short) 10 * repairClass));
			this.repairPlanYearManager.saveOrUpdate(rpy);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：获取设备检修月计划，月计划应具有“月份、年度、设备主键”的联合唯一性
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param month 月份
	 * @param year 年度
	 * @param equipmentIdx 设备idx主键
	 * @return 设备检修月计划
	 */
	private RepairPlanMonth getModel(int month, int year, String equipmentIdx) {
		String hql = "From RepairPlanMonth Where recordStatus = 0 And planMonth = ? And planYear = ? And equipmentIdx = ?";
		return (RepairPlanMonth) this.daoUtils.findSingle(hql, month, year, equipmentIdx);
	}

	/**
	 * <li>说明：获取设备检修月计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param year 年度
	 * @param equipmentIdx 设备idx主键
	 * @return 设备检修月计划
	 */
	@SuppressWarnings("unchecked")
	public List<RepairPlanMonth> getModel(int year, String equipmentIdx) {
		String hql = "From RepairPlanMonth Where recordStatus = 0 And planYear = ? And equipmentIdx = ?";
		return this.daoUtils.find(hql, year, equipmentIdx);
	}

	/**
	 * <li>说明：将一个月平均的分为指定的时间片段
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年12月23日
	 * <li>修改日期：修改当一个月计划修理设备台数较多时，时间分隔精度有误的问题
	 * @param month 月份，1, 2, 3, 4, 5, ..., 11, 12
	 * @param num 时间段个数
	 * @return 日期集合
	 */
	private static List<Date> carveOnMonth(int month, int num) {
		if (1 > num) {
			return null;
		}
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1); // 一个月的第一天
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		list.add(cal.getTime());
		int step = 30 * 24 * 60 / num; // 时间间隔
		for (int i = 0; i < num; i++) {
			if (num - 1 == i) {
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 一个月的最后一天
				list.add(cal.getTime());
				break;
			}
			cal.setTime(list.get(i));
			cal.add(Calendar.MINUTE, step);
			list.add(cal.getTime());
		}
		return list;
	}

	/**
	 * <li>说明：更新设备检修月计划的开工、完工时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entityList 更新对象集合，[{
	 * 		"idx": "8a8283c94fb5eef7014fb5f460a70000",
	 * 		"beginTime": "2016-07-24",
	 * 		"endTime": "2016-07-25"
	 * }]
	 * @throws NoSuchFieldException 
	 */
	public void update(RepairPlanMonth[] entityList) throws NoSuchFieldException {
		List<RepairPlanMonth> list = new ArrayList<RepairPlanMonth>(entityList.length);
		RepairPlanMonth t = null;
		for (RepairPlanMonth rpm : entityList) {
			t = this.getModelById(rpm.getIdx());
			if (null == t) {
				throw new BusinessException("数据异常，请刷新后重试！");
			}
			if (RepairPlanMonth.PLAN_STATUS_YXF == t.getPlanStatus().intValue()) {
				EquipmentPrimaryInfo epi = equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
				throw new BusinessException(epi.getEquipmentName() + "(编号：" + epi.getEquipmentCode() + ")的月计划已经下发，不能更新！");
			}
			t.setBeginTime(rpm.getBeginTime()); // 开始时间
			t.setEndTime(rpm.getEndTime()); // 结束时间
			list.add(t);
		}
		this.saveOrUpdate(list);
	}

	public static void main(String[] args) {
		System.out.println(Calendar.getInstance().get(Calendar.MONTH));
		System.out.println(Calendar.getInstance().get(Calendar.YEAR));
		Short s = 2;
		System.out.println(s * 10);

		List<Date> carveOnMonth = carveOnMonth(7, 5);
		for (Date d : carveOnMonth) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(d));
		}
	}

	/**
	 * <li>说明：设置默认的设备检修班组，但对于批量下发的月计划，此默认值可能是不确定的
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月28日
	 * <li>修改人：何涛
	 * <li>修改内容：使用【两定三包】维护的检修班组信息，设置设备检修默认班组
	 * <li>修改日期：2016年8月11日
	 * @param ids 设备检修月计划idx主键数组
	 * @param map 页面返回实体
	 */
	public void defaultRepairRaskListTeam(String[] ids, Map<String, Object> map) {
		RepairPlanMonth entity = null; // 设备检修月计划
		//		RepairTaskList repairTaskList = null;			// 检修任务单
		RepairTaskListTeam teamMac = null;
		RepairTaskListTeam teamElc = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			if (null == entity) {
				continue;
			}
			// 修改 - 使用【两定三包】维护的检修班组信息，设置设备检修默认班组 - 开始
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(entity.getEquipmentIdx());
			if (null == epi) {
				continue;
			}
			if (null == teamMac && !StringUtil.isNullOrBlank(epi.getMechanicalRepairTeamId())) {
				teamMac = new RepairTaskListTeam();
				teamMac.setOrgId(epi.getMechanicalRepairTeamId());
				teamMac.setOrgName(epi.getMechanicalRepairTeam());
			}
			if (null == teamElc && !StringUtil.isNullOrBlank(epi.getElectricRepairTeamId())) {
				teamElc = new RepairTaskListTeam();
				teamElc.setOrgId(epi.getElectricRepairTeamId());
				teamElc.setOrgName(epi.getElectricRepairTeam());
			}
			// 修改 - 使用【两定三包】维护的检修班组信息，设置设备检修默认班组 - 结束

			if (null != teamMac && null != teamElc) {
				break;
			}
		}
		if (null == teamMac && null == teamElc) {
			map.put(Constants.SUCCESS, false);
		}
		if (null != teamMac) {
			map.put("teamMac", new Team(teamMac.getOrgId().toString(), teamMac.getOrgName()));
		}
		if (null != teamElc) {
			map.put("teamElc", new Team(teamElc.getOrgId().toString(), teamElc.getOrgName()));
		}
	}

	/**
	 * <li>标题: 设备管理信息系统
	 * <li>说明: Team，数据表：维修班组临时封装实体
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月28日
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * @author 信息系统事业部设备管理系统项目组
	 * @version 3.0.1
	 */
	private class Team {

		/** 检修班组id */
		private String orgId;

		/** 检修班组名称 */
		private String orgName;

		/**
		 * <li>说明：构造方法
		 * <li>创建人：何涛
		 * <li>创建日期：2016年7月28日
		 * <li>修改人： 
		 * <li>修改内容：
		 * <li>修改日期：	 
		 * @param orgId 检修班组id
		 * @param orgName 检修班组名称
		 */
		public Team(String orgId, String orgName) {
			this.orgId = orgId;
			this.orgName = orgName;
		}

		@SuppressWarnings("unused")
		public String getOrgId() {
			return orgId;
		}

		@SuppressWarnings("unused")
		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}

		@SuppressWarnings("unused")
		public String getOrgName() {
			return orgName;
		}

		@SuppressWarnings("unused")
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
	}

	/**
	 * <li>说明：撤销设备检修月计划下发
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 设备检修月计划idx主键数组
	 * @throws NoSuchFieldException 
	 */
	public void updateForRescind(String[] ids) throws NoSuchFieldException {
		for (String idx : ids) {
			RepairPlanMonth entity = this.getModelById(idx);
			this.repairTaskListManager.updateForRescind(entity);

			// 更新设备检修月计划状态为：未下发
			entity.setPlanStatus(RepairPlanMonth.PLAN_STATUS_WXF);
			this.saveOrUpdate(entity);
		}
	}

}
