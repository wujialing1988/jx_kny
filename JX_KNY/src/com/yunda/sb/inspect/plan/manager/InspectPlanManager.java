package com.yunda.sb.inspect.plan.manager;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.manager.PositionManager;
import com.yunda.sb.inspect.plan.entity.InspectPlan;
import com.yunda.sb.inspect.plan.entity.InspectPlanBean;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipment;
import com.yunda.sb.inspect.route.entity.InspectRoute;
import com.yunda.sb.inspect.route.manager.InspectRouteManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlan业务类，数据表：E_INSPECTL_PLAN
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectPlanManager")
public class InspectPlanManager extends JXBaseManager<InspectPlan, InspectPlan> {

	/** InspectlRoute业务类，数据表：E_INSPECTL_ROUTE */
	@Resource
	private InspectRouteManager inspectRouteManager;

	/** InspectPlanEquipment业务类，数据表：E_INSPECT_ROUTE_DETAILS */
	@Resource
	private InspectPlanEquipmentManager inspectPlanEquipmentManager;

	/** OmPosition管理器，数据表：OM_POSITION */
	@Resource
	private PositionManager positionManager;

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：查询设备巡检线路树
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param periodTypes 巡检计划类型数组 [周检, 半月检, 月检, 季检]
	 * @param inspectPlan 查询条件封装实体对象
	 * @return 树集合
	 */
	public List<Map<String, Object>> tree(String[] periodTypes, InspectPlan inspectPlan) {
		StringBuilder sb = new StringBuilder(
				"SELECT T.*, (SELECT COUNT(*) FROM E_INSPECT_PLAN_EQUIPMENT WHERE RECORD_STATUS = 0 AND PLAN_IDX= T.IDX AND CHECK_RESULT = '未巡检') AS WXJ_COUNT, (SELECT COUNT(*) FROM E_INSPECT_PLAN_EQUIPMENT WHERE RECORD_STATUS = 0 AND PLAN_IDX= T.IDX AND CHECK_RESULT = '已巡检') AS YXJ_COUNT FROM E_INSPECT_PLAN T WHERE T.RECORD_STATUS = 0");
		// 根据巡检计划类型过滤
		if (null != periodTypes && periodTypes.length > 0) {
			sb.append(" AND T.PERIOD_TYPE IN(").append(this.filterByPeriodTypes(periodTypes)).append(")");
		}
		// 查询条件 - 计划名称
		if (!StringUtil.isNullOrBlank(inspectPlan.getRouteName())) {
			sb.append(" AND T.ROUTE_NAME LIKE '%").append(inspectPlan.getRouteName()).append("%'");
		}
		// 查询条件 - 计划编制人
		if (!StringUtil.isNullOrBlank(inspectPlan.getPartrolWorker())) {
			sb.append(" AND T.PARTROL_WORKER LIKE '%").append(inspectPlan.getPartrolWorker()).append("%'");
		}
		// 查询条件 - 处理状态：(未处理、已处理)
		if (!StringUtil.isNullOrBlank(inspectPlan.getState())) {
			sb.append(" AND T.STATE = '").append(inspectPlan.getState()).append("'");
		}
		// 根据日期过滤
		this.filterByDate(sb, inspectPlan.getPlanStartDate(), inspectPlan.getPlanEndDate());

		sb.append(" ORDER BY T.PLAN_START_DATE ASC");

		@SuppressWarnings("unchecked")
		List<InspectPlanBean> list = this.daoUtils.executeSqlQueryEntity(sb.toString(), InspectPlanBean.class);
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>(list.size());
		Map<String, Object> node = null;
		for (InspectPlanBean plan : list) {
			node = new HashMap<String, Object>(5);
			node.put("id", plan.getIdx());
			node.put("text", plan.toString());
			node.put("leaf", true);
			node.put("iconCls", "groupCheckedIcon");
			node.put("idx", plan.getIdx());
			node.put("routeName", plan.getRouteName());
			node.put("periodType", plan.getPeriodType());
			node.put("partrolWorker", plan.getPartrolWorker());
			node.put("partrolWorkerId", plan.getPartrolWorkerId());
			node.put("planStartDate", DateUtil.yyyy_MM_dd.format(plan.getPlanStartDate()));
			node.put("planEndDate", DateUtil.yyyy_MM_dd.format(plan.getPlanEndDate()));
			node.put("state", plan.convertState());
			node.put("yxjCount", plan.getYxjCount());
			node.put("wxjCount", plan.getWxjCount());
			nodeList.add(node);
		}
		return nodeList;
	}

	/**
	 * <li>说明：根据日期过滤
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param sb sql语句
	 * @param planStartDate 日期起
	 * @param planEndDate 日期止
	 */
	private void filterByDate(StringBuilder sb, Date planStartDate, Date planEndDate) {
		if (null != planStartDate && null != planEndDate) {
			sb.append(" AND T.PLAN_START_DATE >= to_date('").append(DateUtil.yyyy_MM_dd.format(planStartDate)).append("', 'yyyy-mm-dd')");
			sb.append(" AND T.PLAN_START_DATE <= to_date('").append(DateUtil.yyyy_MM_dd.format(planEndDate)).append("', 'yyyy-mm-dd')");
		}
		if (null == planStartDate && null != planEndDate) {
			sb.append(" AND T.PLAN_START_DATE <= to_date('").append(DateUtil.yyyy_MM_dd.format(planEndDate)).append("', 'yyyy-mm-dd')");
		}
		if (null != planStartDate && null == planEndDate) {
			sb.append(" AND T.PLAN_START_DATE >= to_date('").append(DateUtil.yyyy_MM_dd.format(planStartDate)).append("', 'yyyy-mm-dd')");
		}
	}

	/**
	 * <li>说明：根据巡检计划类型过滤
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param periodTypes 巡检计划类型数组 [周检, 半月检, 月检, 季检, 临时]
	 * @return sql语句in条件
	 */
	private String filterByPeriodTypes(String[] periodTypes) {
		StringBuilder sb = new StringBuilder();
		for (String periodType : periodTypes) {
			sb.append(",'").append(periodType).append("'");
		}
		return sb.substring(1);
	}

	/**
	 * <li>说明：根据巡检线路配置信息，定时生成巡检周期计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：何涛
	 * <li>修改内容：2016-06-21
	 * <li>修改日期：根据日期生成巡检计划名称
	 * @param routeIdx 巡检线路idx主键 
	 * @throws NoSuchFieldException 
	 */
	public void startUp(String routeIdx) throws NoSuchFieldException {
		InspectRoute ir = inspectRouteManager.getModelById(routeIdx);
		Calendar cal = Calendar.getInstance();
		// 未启用的巡检线路不能生成巡检周期计划
		if (null == ir || InspectRoute.STATE_QY != ir.getState()
		// 检验计划是否已经超出有效期
				|| (null != ir.getExpiryDate() && cal.getTime().after(ir.getExpiryDate()))
				// 检验计划是否已经到达发布日期
				|| (null != ir.getPlanPublishDate() && cal.getTime().before(ir.getPlanPublishDate()))
				// 验证该巡检线路是否有效，如果该线路下未维护任何巡检设备，则视为该线路是一条无效的巡检线路
				|| !this.inspectRouteManager.isValid(routeIdx)) {
			return;
		}

		Date planStartDate = null; // 计划开始日期
		Date planEndDate = null; // 计划结束日期
		String routeName = null;
		int periodType = ir.getPeriodType().intValue();

		// >>>>>>>>>>>>>>>>>> 周检 - 当前周生成下一周的巡检计划
		String periodTypeCH = null;
		if (InspectRoute.PERIOD_TYPE_ZJ == periodType) {
			periodTypeCH = InspectRoute.PERIOD_TYPE_ZJ_CH;
			// 开始时间为下周周一
			planStartDate = getDayOfWeek(2, 1);
			// 结束时间为下周周日
			planEndDate = getDayOfWeek(1, 2);
			// 根据日期生成巡检计划名称
			routeName = ir.getRouteName() + "[" + new SimpleDateFormat("yy年M月第W周").format(planStartDate) + "]";

			// >>>>>>>>>>>>>>>>>> 半月检 - 当半月生成下一半月的巡检计划
		} else if (InspectRoute.PERIOD_TYPE_BYJ == periodType) {
			periodTypeCH = InspectRoute.PERIOD_TYPE_BYJ_CH;
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 今天是几号
			// 如果是上半月，则生成下半月的巡检计划
			if (1 <= dayOfMonth && 15 >= dayOfMonth) {
				// 开始时间每个月的16号
				cal.set(Calendar.DAY_OF_MONTH, 16);
				planStartDate = cal.getTime();
				// 开始时间每个月的最后一天
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				planEndDate = cal.getTime();
				// 根据日期生成巡检计划名称
				routeName = ir.getRouteName() + "[" + new SimpleDateFormat("yy年M月").format(planStartDate) + "下半月]";
			} else {
				// 开始时间每个月的1号
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				planStartDate = cal.getTime();
				// 开始时间每个月的15号
				cal.set(Calendar.DAY_OF_MONTH, 15);
				planEndDate = cal.getTime();
				// 根据日期生成巡检计划名称
				routeName = ir.getRouteName() + "[" + new SimpleDateFormat("yy年M月").format(planStartDate) + "上半月]";
			}

			// >>>>>>>>>>>>>>>>>> 月检 - 当月生成下一月的巡检计划
		} else if (InspectRoute.PERIOD_TYPE_YJ == periodType) {
			periodTypeCH = InspectRoute.PERIOD_TYPE_YJ_CH;
			// 下一个月
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			planStartDate = cal.getTime();
			// 开始时间每个月的最后一天
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			planEndDate = cal.getTime();
			// 根据日期生成巡检计划名称
			routeName = ir.getRouteName() + "[" + new SimpleDateFormat("yy年M月").format(planStartDate) + "]";

			// >>>>>>>>>>>>>>>>>> 季检 - 当季生成下一季的巡检计划
		} else if (InspectRoute.PERIOD_TYPE_JJ == periodType) {
			periodTypeCH = InspectRoute.PERIOD_TYPE_JJ_CH;
			planStartDate = getNextSeasonStartDate();
			planEndDate = getNextSeasonEndDate();
			// 根据日期生成巡检计划名称
			Calendar c = Calendar.getInstance();
			c.setTime(planStartDate);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			String season = null;
			if (month >= 0 && month < 3) {
				season = "春季";
			} else if (month >= 3 && month < 6) {
				season = "夏季";
			} else if (month >= 6 && month < 9) {
				season = "秋季";
			} else if (month >= 9 && month < 12) {
				season = "冬季";
			}
			routeName = ir.getRouteName() + "[" + year + "年" + season + "]";
		}
		// 验证是否已经生成了下一周期的巡检计划，不包含临时的设备巡检计划
		InspectPlan plan = this.getNextInspectPlan(ir.getIdx(), planStartDate, planEndDate);
		if (null != plan) {
			return;
		}
		this.startUp(ir, planStartDate, planEndDate, periodTypeCH, routeName);
	}

	/**
	 * <li>说明：生成临时设备巡检计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeIdx 巡检线路idx主键
	 * @param planStartDate 计划开始日期
	 * @param planEndDate 计划结束日期
	 * @param routeName 巡检计划名称，在生成临时巡检计划时用到
	 * @throws NoSuchFieldException
	 */
	public void startUp(String routeIdx, Date planStartDate, Date planEndDate, String routeName) throws NoSuchFieldException {
		// 获取设备巡检线路实体对象
		InspectRoute ir = this.inspectRouteManager.getModelById(routeIdx);
		if (null == ir) {
			return;
		}
		//		if (!this.inspectRouteManager.isValid(ir.getIdx())) {
		//			throw new RuntimeException("该巡检线路下没有任何巡检设备信息，不能生成巡检计划！");
		//		}
		this.startUp(ir, planStartDate, planEndDate, null, routeName);
	}

	/**
	 * <li>说明：生成巡检周期计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ir 设备巡检线路实体对象
	 * @param planStartDate 计划开始日期
	 * @param planEndDate 计划结束日期
	 * @param periodType 巡检周期（周检，半月检，月检，季检，临时）
	 * @param routeName 巡检计划名称，在生成临时巡检计划时用到
	 * @throws NoSuchFieldException
	 */
	private void startUp(InspectRoute ir, Date planStartDate, Date planEndDate, String periodType, String routeName) throws NoSuchFieldException {
		InspectPlan plan = new InspectPlan();
		plan.setPeriodType(null != periodType ? periodType : InspectRoute.PERIOD_TYPE_TEMP); // 巡检周期
		plan.planStartDate(planStartDate).planEndDate(planEndDate);
		plan.routeIdx(ir.getIdx()).routeName(null == routeName ? ir.getRouteName() : routeName);
		plan.setPartrolWorker(ir.getPartrolWorker());
		plan.setPartrolWorkerId(ir.getPartrolWorkerId());
		plan.setState(InspectPlan.STATE_WCL);
		// Modified by hetao on 2016-06-22 默认设置实际开始时间即为计划开始时间
		plan.setRealStartDate(planStartDate);

		EntityUtil.setNoDelete(plan);
		this.saveOrUpdate(plan);

		// 实例化周期巡检设备
		this.inspectPlanEquipmentManager.startUp(plan);
	}

	/**
	 * <li>说明：查询指定时间范围内的巡检计划，不包含临时的设备巡检计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeIdx 巡检线路idx主键
	 * @param planStartDate 计划开始日期
	 * @param planEndDate 计划结束
	 * @return 巡检周期计划实体对象
	 */
	private InspectPlan getNextInspectPlan(String routeIdx, Date planStartDate, Date planEndDate) {
		String hql = "From InspectPlan Where recordStatus = 0 And routeIdx = ? And planStartDate = ? And planEndDate = ? And periodType <> '临时'";
		return (InspectPlan) this.daoUtils.findSingle(hql, new Object[] { routeIdx, planStartDate, planEndDate });
	}

	/**
	 * <li>说明：获取下一季度的开始日期
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 下一季度的开始日期
	 */
	private static Date getNextSeasonStartDate() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (0 <= month && 2 >= month) {
			cal.set(Calendar.MONTH, 3);
		} else if (3 <= month && 5 >= month) {
			cal.set(Calendar.MONTH, 6);
		} else if (6 <= month && 8 >= month) {
			cal.set(Calendar.MONTH, 9);
		} else if (9 <= month && 11 >= month) {
			cal.set(Calendar.MONTH, 0);
			cal.add(Calendar.YEAR, 1);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * <li>说明：获取下一季度的开始日期
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 下一季度的开始日期
	 */
	private static Date getNextSeasonEndDate() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (0 <= month && 2 >= month) {
			cal.set(Calendar.MONTH, 5);
		} else if (3 <= month && 5 >= month) {
			cal.set(Calendar.MONTH, 8);
		} else if (6 <= month && 8 >= month) {
			cal.set(Calendar.MONTH, 11);
		} else if (9 <= month && 11 >= month) {
			cal.set(Calendar.MONTH, 2);
			cal.add(Calendar.YEAR, 1);
		}
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * <li>说明：获取指定日期是星期几
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param date 指定日期，null表示当前日期
	 * @return 
	 * <li>1 - 星期天
	 * <li>2 - 星期一
	 * <li>3 - 星期二
	 * <li>4 - 星期三
	 * <li>5 - 星期四
	 * <li>6 - 星期五
	 * <li>7 - 星期六
	 */
	private static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		if (null != date) {
			cal.setTime(date);
		}
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * <li>说明：获取某一周的星期几的日期
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param dayOfWeek 
	 * <li>1 - 星期天
	 * <li>2 - 星期一
	 * <li>3 - 星期二
	 * <li>4 - 星期三
	 * <li>5 - 星期四
	 * <li>6 - 星期五
	 * <li>7 - 星期六
	 * @param count
	 * <li>0 - 表示获取本的日期
	 * <li>1 - 表示获取下一周的日期，以此类推
	 * <li>-1 - 表示上一周的日期，以此类推
	 * @return 日期对象
	 */
	public static Date getDayOfWeek(int dayOfWeek, int count) {
		if (dayOfWeek < 1 || dayOfWeek > 7) {
			throw new RuntimeException("传入参数非法，参数[dayOfWeek]有效区间为[1, 7]");
		}
		// 获取今天是星期几
		int temp = getDayOfWeek(null);
		if (1 == temp) {
			count--;
		}
		int day = dayOfWeek - temp + 7;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_WEEK, day + --count * 7);
		return cal.getTime();
	}

	/**
	 * <li>说明：分页查询，查询巡检人或者委托巡检人为当前系统操作人的巡检计划 - pda，如果操作人员是工长身份，则可以查询其所在班组所有巡检任务
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件实体
	 * @param onlyMe 是否只查询自己的任务，此标识主要针对于身份为工长的系统操作员，如果为false，则工长可以查询其班组下所有巡检任务
	 * @return 设备巡检计划集合
	 */
	public Page<InspectPlanBean> queryPageList(SearchEntity<InspectPlan> searchEntity, boolean onlyMe) {
		if (onlyMe) {
			return this.queryPageList(searchEntity);
		}
		// 验证当前操作人员是否是工长
		boolean isGz = this.positionManager.validatePosi(SystemContext.getOmEmployee().getEmpid(), SystemContext.getOmEmployee().getOrgid(), "工长");
		if (isGz) {
			return this.queryPageListGZ(searchEntity);
		}
		return this.queryPageList(searchEntity);
	}

	/**
	 * <li>说明：分页查询，查询巡检人或者委托巡检人为当前系统操作人的巡检计划 - pda
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件实体
	 * @return 设备巡检计划集合
	 */
	private Page<InspectPlanBean> queryPageList(SearchEntity<InspectPlan> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (").append(sql).append(") t").append(" where t.wxj_count > 0");

		// Modified by hetao on 2016-08-29 设备巡检处理时，只查询在当前日期已经计划开始的巡检计划
		sb.append(" and to_char(t.plan_start_date,'yyyy-mm-dd hh24:ii:ss') <= '").append(DateUtil.yyyy_MM_dd.format(new Date())).append(" 23:59:59'");

		InspectPlan entity = searchEntity.getEntity();
		// 查询条件 - 巡检计划名称
		if (!StringUtil.isNullOrBlank(entity.getRouteName())) {
			sb.append(" and t.route_name like '%").append(entity.getRouteName()).append("%'");
		}

		// 以计划开始日期升序排序
		sb.append(" order by t.plan_start_date asc");
		sql = sb.toString().replace("?", SystemContext.getOmEmployee().getEmpid() + "");
		String totalSql = "select count(*) as rowcount " + sql.substring(sql.indexOf("from"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanBean.class);
	}

	/**
	 * <li>说明：分页查询，查询巡检人或者委托巡检人为当前系统操作人的巡检计划 - pda，如果操作人员是工长身份，则可以查询其所在班组所有巡检任务
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件实体
	 * @return 设备巡检计划集合
	 */
	private Page<InspectPlanBean> queryPageListGZ(SearchEntity<InspectPlan> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan:queryPageListGZ", File.separatorChar));
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (").append(sql).append(") t").append(" where t.wxj_count > 0");

		// Modified by hetao on 2016-08-29 设备巡检处理时，只查询在当前日期已经计划开始的巡检计划
		sb.append(" and t.plan_start_date <= '").append(DateUtil.yyyy_MM_dd.format(new Date())).append(" 23:59:59'");

		InspectPlan entity = searchEntity.getEntity();
		// 查询条件 - 巡检计划名称
		if (!StringUtil.isNullOrBlank(entity.getRouteName())) {
			sb.append(" and t.route_name like '%").append(entity.getRouteName()).append("%'");
		}

		// 以计划开始日期升序排序
		sb.append(" order by t.plan_start_date asc");
		sql = sb.toString().replace("?", SystemContext.getOmEmployee().getOrgid() + "");
		String totalSql = "select count(*) as rowcount " + sql.substring(sql.indexOf("from"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanBean.class);
	}

	/**
	 * <li>说明：分页查询，查询需要使用人确认的巡检计划 - pda
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity查询条件实体
	 * @return 设备巡检计划集合
	 */
	public Page<InspectPlanBean> queryPageList2User(SearchEntity<InspectPlan> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan:queryPageList2User", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);

		InspectPlan entity = searchEntity.getEntity();
		// 查询条件 - 巡检线路（范围）名称
		if (!StringUtil.isNullOrBlank(entity.getRouteName())) {
			sb.append(" and t.route_name like '%").append(entity.getRouteName()).append("%'");
		}

		// Modified by hetao on 2017-02-27 处理排序
		this.processOrdersInSql(searchEntity, sb);

		sql = sb.toString();

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
		return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanBean.class);
	}

	/**
	 * <li>说明：级联删除巡检设备
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 巡检计划idx主键数组
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void logicDelete(Serializable... ids) throws NoSuchFieldException {
		Serializable[] array = null;
		for (Serializable idx : ids) {
			List<InspectPlanEquipment> list = this.inspectPlanEquipmentManager.getModelsByPlanIdx((String) idx);
			if (null == list || list.isEmpty()) {
				continue;
			}
			array = new String[list.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list.get(i).getIdx();
			}
			this.inspectPlanEquipmentManager.logicDelete(array);
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明： 定时生成巡检任务
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-10
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public void doTimerTask() {
		InspectRoute route = new InspectRoute();
		route.setRecordStatus(0);
		route.setState(InspectRoute.STATE_QY);
		// 获取所有已启用的周期巡检线路
		List<InspectRoute> all = daoUtils.getHibernateTemplate().findByExample(route);
		logger.info("**** **** **** **** **** **** **** **** **** **** **** **** **** ****");
		logger.info(String.format("巡检计划生成定时器启动，定时器执行时间：%s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
		logger.info("**** **** **** **** **** **** **** **** **** **** **** **** **** ****");
		for (InspectRoute ir : all) {
			try {
				// 生成巡检周期计划
				this.startUp(ir.getIdx());
			} catch (Exception e) {
				logger.error(e);
				continue;
			}
		}
	}

}
