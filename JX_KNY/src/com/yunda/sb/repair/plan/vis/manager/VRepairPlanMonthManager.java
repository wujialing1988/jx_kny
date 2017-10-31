package com.yunda.sb.repair.plan.vis.manager;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.repair.plan.vis.entity.VRepairPlanMonth;
import com.yunda.sb.repair.plan.vis.entity.VRepairPlanMonthItems;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: VRepairPlanMonth控制器，设备检修月计划的VIS实现
 * <li>创建人：何涛
 * <li>创建日期：2016年7月24日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service(value = "vRepairPlanMonthManager")
public class VRepairPlanMonthManager extends JXBaseManager<VRepairPlanMonth, VRepairPlanMonth> {

	/**
	 * <li>说明：分页查询 - 设备检修月计划编制VIS方式
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象封装实体
	 * @param planStatus 查询条件 - 计划状态，多个状态已逗号进行分隔，如：-1,0,1
	 * @return 分页列表
	 */
	public Page<VRepairPlanMonth> queryPageList(SearchEntity<VRepairPlanMonth> searchEntity, String planStatus) {
		String sql = SqlMapUtil.getSql(String.format("repair%crepair_plan_month_vis:queryPageList", File.separatorChar));
		VRepairPlanMonth entity = searchEntity.getEntity();

		// 查询条件 - 年度
		if (null != entity.getPlanYear()) {
			sql = sql.replace("$Y", entity.getPlanYear().toString());
		} else {
			sql = sql.replace("AND T.PLAN_YEAR = '$Y'", "");
		}
		// 查询条件 - 月份
		if (null != entity.getPlanMonth()) {
			sql = sql.replace("$M", entity.getPlanMonth().toString());
		} else {
			sql = sql.replace("AND T.PLAN_MONTH = '$M'", "");
		}

		StringBuilder sb = new StringBuilder(sql);
		// 查询条件 - 单位名称
		if (!StringUtil.isNullOrBlank(entity.getOrgId())) {
			sb.append(" AND I.ORG_ID LIKE '").append(entity.getOrgId()).append("%'");
		}
		// 查询条件 - 设备编号
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE LIKE '%").append(entity.getEquipmentCode()).append("%'");
		}
		// 查询条件 - 设备名称
		if (!StringUtil.isNullOrBlank(entity.getEquipmentName())) {
			sb.append(" AND I.EQUIPMENT_NAME LIKE '%").append(entity.getEquipmentName()).append("%'");
		}
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		Page<VRepairPlanMonth> page = this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, VRepairPlanMonth.class);
		List<VRepairPlanMonth> list = page.getList();
		for (VRepairPlanMonth t : list) {
			// Modified by hetao on 2016-10-24 增加按照时间轴范围查询过滤月计划，防止依次加载过多数据致使页面响应时间较长
			t.setItems(this.queryItems(t, planStatus, entity.getStartTime(), entity.getEndTime()));
		}
		return page;
	}

	/**
	 * <li>说明：获取设备的月检修计划明细
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月8日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 月检修计划VIS分组对象
	 * @param planStatus 查询条件 - 计划状态，多个状态已逗号进行分隔，如：-1,0,1
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @return 设备的月检修计划明细
	 */
	@SuppressWarnings("unchecked")
	private List<VRepairPlanMonthItems> queryItems(VRepairPlanMonth t, String planStatus, Date startTime, Date endTime) {
		String sql = SqlMapUtil.getSql(String.format("repair%crepair_plan_month_vis:queryItems", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" AND T.EQUIPMENT_IDX = '").append(t.getEquipmentIdx()).append("'");
		sb.append(" And t.begin_time >= to_date('").append(DateUtil.yyyy_MM_dd_HH_mm_ss.format(startTime)).append("', 'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" And t.begin_time <= to_date('").append(DateUtil.yyyy_MM_dd_HH_mm_ss.format(endTime)).append("', 'yyyy-mm-dd hh24:mi:ss')");
		sb.append(" And t.plan_status in ('").append(planStatus.replace(",", "', '")).append("')");
		return this.daoUtils.executeSqlQueryEntity(sb.toString(), VRepairPlanMonthItems.class);
	}

}
