package com.yunda.sb.repair.plan.manager;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.repair.period.entity.RepairPeriod;
import com.yunda.sb.repair.period.manager.RepairPeriodManager;
import com.yunda.sb.repair.plan.entity.RepairPlanMonth;
import com.yunda.sb.repair.plan.entity.RepairPlanYear;
import com.yunda.sb.repair.plan.entity.RepairPlanYearBean;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanYear管理器，数据表：SBJX_REPAIR_PLAN_YEAR
 * <li>创建人： 黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service
public class RepairPlanYearManager extends JXBaseManager<RepairPlanYear, RepairPlanYear> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** RepairPlanMonth管理器，数据表：SBJX_REPAIR_PLAN_MONTH */
	@Resource
	private RepairPlanMonthManager repairPlanMonthManager;

	/** RepairPeriod管理器，数据表：SBJX_REPAIR_PERIOD */
	@Resource
	private RepairPeriodManager repairPeriodManager;

	/** 自动设置设备检修月计划时，查询历史年计划的最大年度数 */
	private int maxHistoryPlanNum = 3;

	/* (non-Javadoc)
	 * @see com.yunda.frame.core.BaseManager#validateUpdate(java.lang.Object)
	 */
	@Override
	public String[] validateUpdate(RepairPlanYear t) {
		String hql = "From RepairPlanYear Where recordStatus = 0 And equipmentIdx = ? And planYear = ?";
		RepairPlanYear entity = (RepairPlanYear) this.daoUtils.findSingle(hql, t.getEquipmentIdx(), t.getPlanYear());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { "重复添加的设备检修年计划！" };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @return 设备检修年计划查询封装实体对象分页集合
	 */
	public Page<RepairPlanYearBean> queryPageList(SearchEntity<RepairPlanYearBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("repair%crepair_plan_year:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		RepairPlanYearBean entity = searchEntity.getEntity();
		// 查询条件 - 单位名称
		if (!StringUtil.isNullOrBlank(entity.getOrgId())) {
			sb.append(" AND I.ORG_ID LIKE '").append(entity.getOrgId()).append("%'");
		}
		// 查询条件 - 计划年份
		if (null != entity.getPlanYear()) {
			sb.append(" AND T.PLAN_YEAR = '").append(entity.getPlanYear()).append("'");
		}
		// 查询条件 - 设备编号
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE LIKE '%").append(entity.getEquipmentCode()).append("%'");
		}
		// 查询条件 - 设备名称
		if (!StringUtil.isNullOrBlank(entity.getEquipmentName())) {
			sb.append(" AND I.EQUIPMENT_NAME LIKE '%").append(entity.getEquipmentName()).append("%'");
		}
		// Modified by hetao on 2017-02-23 修改按更新日期倒序排序，这样可以使最近一次插入（更新）的记录显示在首行
		//		sb.append(" ORDER BY T.UPDATE_TIME DESC");
		// Modified by hetao on 2017-02-24 修改以设备编号升序排列，修改原因：当年计划被批量下发时，如果按update_time排序，页面记录会显示的比较混乱
		sb.append(" ORDER BY I.EQUIPMENT_CODE ASC");
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairPlanYearBean.class);
	}

	/**
	 * <li>说明：保存设备检修年计划，保存已选择的设备到设备检修年计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月22日
	 * <li>修改人：何涛
	 * <li>修改内容：对于同类型不同设备存在不同检修范围的情况，查询检修范围时优先使用设备编号进行查询
	 * <li>修改日期：2017年2月16日
	 * <li>修改人：何涛
	 * <li>修改内容：取消添加设备年计划时的检修范围验证，方便用户使用该页面功能快速生成设备检修年度计划报表
	 * <li>修改日期：2017年2月23日
	 * @param planYear 计划年份
	 * @param equipmentIds 设备信息idx主键数组
	 * @throws NoSuchFieldException 
	 */
	public void save(Integer planYear, String[] equipmentIds) throws NoSuchFieldException {
		RepairPlanYear entity = null;
		for (String equipmentIdx : equipmentIds) {
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(equipmentIdx);

			// Modified by hetao on 2016-10-27  因为生成设备的检修计划时，会依据设备的机械、电气系数生成相应的检修范围，如果某一设备机械、电气系数均未设置，则生成的数据会成为异常数据
			// 验证设备的机械、电气系数是否已设置
			if ((null == epi.getElectricCoefficient() || 0 >= epi.getElectricCoefficient()) && (null == epi.getMechanicalCoefficient() || 0 >= epi.getMechanicalCoefficient())) {
				throw new BusinessException(String.format("%s(编号：%s)机械、电气系数未设置，请设置后重试！", epi.getEquipmentName(), epi.getEquipmentCode()));
			}

			entity = new RepairPlanYear();
			entity.init(); // 初始化每个月的修程为“未设置”
			entity.setPlanYear(planYear);
			entity.setEquipmentIdx(equipmentIdx);
			// 重置设备检修年计划中每个月的修程
			this.resetRepairClass(planYear, entity);
			// 实现根据检修周期自动生成月修程的功能
			this.initByRepairPeriod(entity);
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：以递归的方式获取设备的年检修计划实体对象（递归）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planYear 计划年度
	 * @param equipmentIdx 设备idx主键
	 * @param maxHistoryPlanNum 查询历史年计划的最大年度数，增加该字段是为了防止死循环
	 * @return 设备检修年计划实体对象
	 */
	private RepairPlanYear getHistory(int planYear, String equipmentIdx, int maxHistoryPlanNum) {
		String hql = "From RepairPlanYear Where recordStatus = 0 And planYear = ? And equipmentIdx = ?";
		RepairPlanYear o = (RepairPlanYear) this.daoUtils.findSingle(hql, planYear, equipmentIdx);
		if (null != o) {
			return o;
		}
		if (0 >= maxHistoryPlanNum) {
			return null;
		}
		return this.getHistory(--planYear, equipmentIdx, --maxHistoryPlanNum);
	}

	/**
	 * <li>说明：实现根据检修周期自动生成月修程的功能
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 设备检修年计划实体对象
	 * @throws NoSuchFieldException 
	 */
	private void initByRepairPeriod(RepairPlanYear entity) throws NoSuchFieldException {
		String equipmentIdx = entity.getEquipmentIdx();
		if (StringUtil.isNullOrBlank(equipmentIdx)) {
			throw new NullPointerException("设备检修年计划的设备idx主键未设置！");
		}
		// 通过设备idx主键获取设备维修周期实体对象
		RepairPeriod rp = repairPeriodManager.getModelByEquipmentIdx(equipmentIdx);
		if (null == rp) {
			return;
		}
		// 获取该设备最后一次的修程，1:小修、2：中修、3：项修
		RepairPlanYear history = this.getHistory(entity.getPlanYear() - 1, entity.getEquipmentIdx(), this.maxHistoryPlanNum);
		if (null == history) {
			return;
		}
		int i = 12;
		Short repairClass = (short) RepairPlanYear.REPAIR_CLASS_NOSET;
		// 从12月份开始，从后往前遍历设备月计划修程
		do {
			repairClass = (Short) BeanUtils.forceGetProperty(history, "month" + i);
			if (repairClass >= 10) {
				repairClass = (short) (repairClass / 10);
			}
			if (RepairPlanYear.REPAIR_CLASS_SMALL == repairClass || RepairPlanYear.REPAIR_CLASS_MEDIUM == repairClass) {
				break;
			}
			i--;
			if (0 == i) {
				history = this.getHistory(history.getPlanYear() - 1, history.getEquipmentIdx(), this.maxHistoryPlanNum);
				if (null == history) {
					break;
				}
				i = 12;
			}
		} while (true);
		// 如果历史没有月检修计划设置，则不能根据维修周期自动安排检修计划
		if (repairClass == RepairPlanYear.REPAIR_CLASS_NOSET) {
			return;
		}
		// 设备最近一次月计划月份
		int planMonth = i;
		int planYear = history.getPlanYear();
		int num = 0;
		// 如果最后一次是小修
		if (RepairPlanYear.REPAIR_CLASS_SMALL == repairClass) {
			// 查询上一次中修后，一共已经安排了几次小修
			num = 1;
			while (true) {
				i--;
				if (0 == i) {
					history = this.getHistory(history.getPlanYear() - 1, history.getEquipmentIdx(), this.maxHistoryPlanNum);
					if (null == history) {
						break;
					}
					i = 12;
				}
				// 获取修程
				repairClass = (Short) BeanUtils.forceGetProperty(history, "month" + i);
				if (repairClass >= 10) {
					repairClass = (short) (repairClass / 10);
				}
				// 如果查询到是小修，则小修次数加一
				if (RepairPlanYear.REPAIR_CLASS_SMALL == repairClass) {
					num++;
				}
				// 如果查询到是中修，则表明一个中修过程结束
				if (RepairPlanYear.REPAIR_CLASS_MEDIUM == repairClass) {
					break;
				}
			}
		}
		this.initByRepairPeriod(entity, planYear, planMonth, num);
	}

	/**
	 * 说明：实现根据检修周期自动生成月修程的功能
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 设备检修年计划实体对象
	 * @param planYear 设备最近一次月计划年度
	 * @param planMonth 设备最近一次月计划月份
	 * @param num 上一次中修后，一共已经安排的小修次数
	 * @throws NoSuchFieldException
	 */
	private void initByRepairPeriod(RepairPlanYear entity, int planYear, int planMonth, int num) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(entity.getEquipmentIdx());
		// 管理类别（A、B、C）
		String manageClass = epi.getManageClass();
		Integer xx = null; // 小修周期(月)
		Integer zx = null; // 中修间隔(小修次)
		RepairPeriod rp = repairPeriodManager.getModelByEquipmentIdx(entity.getEquipmentIdx());
		// 一般设备
		if ("A".equals(manageClass)) {
			xx = rp.getAxx();
			zx = rp.getAzx();
			// A类设备
		} else {
			xx = rp.getXx();
			zx = rp.getZx();
		}
		int currentYear = entity.getPlanYear(); // 当前正在添加的设备检修年计划实体对象
		int nextMonth = planMonth + xx; // 理论上，最近一次月计划后的计划月份
		if (12 < nextMonth) {
			nextMonth -= 12;
			planYear++;
		}
		// 如果安排的小修次数已经达到维修周期设定的“中修间隔（小修次）”，下一次计划就应该安排中修
		while (planYear <= currentYear) {
			if (zx <= num) {
				num = 0;
				if (planYear == entity.getPlanYear()) {
					BeanUtils.forceSetProperty(entity, "month" + nextMonth, (short) RepairPlanYear.REPAIR_CLASS_MEDIUM);
				}
			} else {
				num++;
				if (planYear == entity.getPlanYear()) {
					BeanUtils.forceSetProperty(entity, "month" + nextMonth, (short) RepairPlanYear.REPAIR_CLASS_SMALL);
				}
			}
			nextMonth += xx;
			if (12 < nextMonth) {
				nextMonth -= 12;
				planYear++;
			}
		}
	}

	/**
	 * <li>说明：查询是否存在已经生成了相应修程的月计划，如果有，则需对对应月进行修程设置
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planYear 年度
	 * @param entity 设备检修年计划
	 * @throws NoSuchFieldException
	 */
	private void resetRepairClass(Integer planYear, RepairPlanYear entity) throws NoSuchFieldException {
		String equipmentIdx = entity.getEquipmentIdx();
		if (null == equipmentIdx) {
			throw new NullPointerException("重置设备年检修修程错误，设备主键为空！");
		}
		// 查询是否存在已经生成、或者下发了相应修程的月计划，如果有，则需对对应月进行修程设置
		List<RepairPlanMonth> rpmList = this.repairPlanMonthManager.getModel(planYear, equipmentIdx);
		if (null == rpmList || rpmList.isEmpty()) {
			return;
		}
		// 修程
		Short repairClass = null;
		for (RepairPlanMonth rpm : rpmList) {
			repairClass = (short) ((short) 10 * rpm.getRepairClass());
			BeanUtils.forceSetProperty(entity, "month" + rpm.getPlanMonth(), repairClass);
		}
	}

	/**
	 * <li>说明：获取指定月份的年设备检修计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param month 月份，1, 2, 3, 4, 5, ..., 11, 12
	 * @param year 年份，2016, 2017, 2018, ...
	 * @return 指定月份的年设备检修计划
	 */
	@SuppressWarnings("unchecked")
	public List<RepairPlanYear> getModelsByMonth(int month, int year) {
		String filedName = "month" + month;
		String hql = "From RepairPlanYear Where recordStatus = 0 And &filedName > 0 And &filedName Not Like '%0' And planYear = ?".replace("&filedName", filedName);
		return this.daoUtils.find(hql, year);
	}

	/**
	 * <li>说明：获取设备的年检修计划
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planYear 年度
	 * @param equipmentIdx 设备idx主键
	 * @return 设备检修年计划实体对象
	 */
	public RepairPlanYear getModel(Integer planYear, String equipmentIdx) {
		String hql = "From RepairPlanYear Where recordStatus = 0 And planYear = ? And equipmentIdx = ?";
		return (RepairPlanYear) this.daoUtils.findSingle(hql, planYear, equipmentIdx);
	}

	public static void main(String[] args) {
		String filedName = "month" + 1;
		String hql = "From RepairPlanYear Where recordStatus = 0 And &filedName > 0 And &filedName Not Like '%0'".replace("&filedName", filedName);
		System.out.println(hql);
	}

}
