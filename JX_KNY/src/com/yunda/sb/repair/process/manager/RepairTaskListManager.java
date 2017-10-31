package com.yunda.sb.repair.process.manager;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.entity.Attachment;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.baseapp.upload.manager.IAttachmentManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.repair.plan.entity.RepairPlanMonth;
import com.yunda.sb.repair.plan.entity.RepairPlanYear;
import com.yunda.sb.repair.plan.manager.RepairPlanMonthManager;
import com.yunda.sb.repair.process.entity.RepairScopeCase;
import com.yunda.sb.repair.process.entity.RepairTaskList;
import com.yunda.sb.repair.process.entity.RepairTaskListBean;
import com.yunda.sb.repair.process.entity.RepairTaskListTeam;
import com.yunda.sb.repair.process.entity.RepairWorkOrder;
import com.yunda.sb.repair.scope.entity.RepairScope;
import com.yunda.sb.repair.scope.manager.RepairScopeManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairTaskListManager，数据表：E_REPAIR_TASK_LIST
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service(value = "repairTaskListManager")
public class RepairTaskListManager extends JXBaseManager<RepairTaskList, RepairTaskList> implements IStateWatcher, IAttachmentManager {

	/** RepairTaskListTeam管理器，数据表：E_REPAIR_TASK_LIST_TEAM*/
	@Resource
	private RepairTaskListTeamManager repairTaskListTeamManager;

	/** RepairScopeCase管理器，数据表：E_REPAIR_SCOPE_CASE */
	@Resource
	private RepairScopeCaseManager repairScopeCaseManager;

	/** RepairPlanMonth管理器，数据表：SBJX_REPAIR_PLAN_MONTH */
	@Resource
	private RepairPlanMonthManager repairPlanMonthManager;

	/** RepairScope业务类，数据表：E_REPAIR_SCOPE */
	@Resource
	private RepairScopeManager repairScopeManager;

	/** 附件管理，数据表：JXPZ_Attachment_Manage */
	@Resource
	private AttachmentManager attachmentManager;

	/**
	 * <li>说明：下发计划-发布检修计划
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planMonthIdxs 设备检修月计划idx主键数组
	 * @param team2Elc 电气班组封装实体，形如：{orgId: "381", orgName: "检修一组"}
	 * @param team2Mac 机械班组封装实体，形如：{orgId: "386", orgName: "检修二组"}
	 * @param isNeedAcceptance 是否需要验收，0：无需验收人验收、1：需要验收人验收
	 * @throws NoSuchFieldException
	 * @throws SQLException
	 */
	public void insert4Publish(String[] planMonthIdxs, RepairTaskListTeam team2Elc, RepairTaskListTeam team2Mac, boolean isNeedAcceptance) throws NoSuchFieldException,
			SQLException {
		if (null == team2Elc && null == team2Mac) {
			throw new BusinessException("数据异常，未指定任何检修作业班组！");
		}
		RepairTaskList task = null; // 检修任务单
		RepairPlanMonth m = null; // 设备检修月计划
		EquipmentPrimaryInfo epi = null; // 设备主要信息
		for (String planMonthIdx : planMonthIdxs) {
			m = repairPlanMonthManager.getModelById(planMonthIdx);
			// 验证月计划是否已下发
			if (RepairPlanMonth.PLAN_STATUS_YXF == m.getPlanStatus().intValue()) {
				epi = (EquipmentPrimaryInfo) this.daoUtils.get(m.getEquipmentIdx(), EquipmentPrimaryInfo.class);
				throw new BusinessException(String.format("%s(编号：%s)的设备月计划已经下发！", epi.getEquipmentName(), epi.getEquipmentCode()));
			}
			// 验证月计划是否正确设置了开工完工时间
			if (null == m.getBeginTime() || null == m.getEndTime()) {
				epi = (EquipmentPrimaryInfo) this.daoUtils.get(m.getEquipmentIdx(), EquipmentPrimaryInfo.class);
				throw new BusinessException(String.format("%s(编号：%s)的设备月计划未设置正确的开工或完工时间！", epi.getEquipmentName(), epi.getEquipmentCode()));
			}
			if (m.getBeginTime().after(m.getEndTime())) {
				epi = (EquipmentPrimaryInfo) this.daoUtils.get(m.getEquipmentIdx(), EquipmentPrimaryInfo.class);
				throw new BusinessException(String.format("%s(编号：%s)的设备月计划的开工时间不能晚于完工时间！", epi.getEquipmentName(), epi.getEquipmentCode()));
			}
			// 验证设备是否维护了对应修程设备检修范围
			List<RepairScope> scopeList = repairScopeManager.getModelsByEquipmentIdx(m.getEquipmentIdx(), m.getRepairClass());
			if (null == scopeList || scopeList.isEmpty()) {
				epi = (EquipmentPrimaryInfo) this.daoUtils.get(m.getEquipmentIdx(), EquipmentPrimaryInfo.class);
				throw new BusinessException(String.format("%s(编号：%s)未维护设备%s修范围！", epi.getEquipmentName(), epi.getEquipmentCode(), RepairPlanYear.getRepairClassName(m
						.getRepairClass())));
			}
			// 设置设备计划检修月计划的计划状态为：已下发
			m.setPlanStatus(RepairPlanMonth.PLAN_STATUS_YXF);
			this.repairPlanMonthManager.saveOrUpdate(m);

			task = new RepairTaskList();
			// 根据设备检修月计划保存设备检修任务单
			task.setEquipmentIdx(m.getEquipmentIdx()); // 设备idx主键
			// 是否需要验收，0：无需验收人验收、1：需要验收人验收
			task.setIsNeedAcceptance(isNeedAcceptance);
			String repairClassName = m.getRepairClassName();
			if (null == repairClassName) {
				throw new NullPointerException("数据异常，修程编码为空，Table_Name[SBJX_REPAIR_PLAN_MONTH] - idx[" + m.getIdx() + "]");
			}
			task.setPlanMonthIdx(m.getIdx()); // 月计划主键
			task.setRepairClassName(repairClassName); // 修程名称（小、中、项）
			// Modified by hetao on 2017-02-27 根据安康机务段需求，开工日期为主修人初次扫描施修时间
			//			// Modified by hetao on 2016-10-21 默认设置设备检修计划实际开工日期为计划下发日期
			//			task.setRealBeginTime(Calendar.getInstance().getTime());
			task.setBeginTime(m.getBeginTime()); // 开工时间
			task.setEndTime(m.getEndTime()); // 完工时间
			task.setState(RepairTaskList.STATE_WCL); // 初始化检修任务单状态为“未处理”
			this.saveOrUpdate(task);

			// 根据检修类型（1：机械、2：电气、3：其它）筛选相应班组（机械班组 or 电气班组）的检修范围活
			List<RepairScope> rsList2Mac = new ArrayList<RepairScope>();
			List<RepairScope> rsList2Elc = new ArrayList<RepairScope>();
			for (RepairScope scope : scopeList) {
				if (RepairScope.REPAIR_TYPE_JX == scope.getRepairType().intValue()) {
					rsList2Mac.add(scope);
				} else if (RepairScope.REPAIR_TYPE_DQ == scope.getRepairType().intValue()) {
					rsList2Elc.add(scope);
				}
			}

			// 保存检修任务单机械作业班组，生成设备检修范围实例
			if (null != team2Mac && !rsList2Mac.isEmpty()) {
				this.repairTaskListTeamManager.insert4Publish(team2Mac, task, RepairScope.REPAIR_TYPE_JX);
				this.repairScopeCaseManager.insert4Publish(task, rsList2Mac);
			}
			// 保存检修任务单电气作业班组，生成设备检修范围实例
			if (null != team2Elc && !rsList2Elc.isEmpty()) {
				this.repairTaskListTeamManager.insert4Publish(team2Elc, task, RepairScope.REPAIR_TYPE_DQ);
				this.repairScopeCaseManager.insert4Publish(task, rsList2Elc);
			}
		}
	}

	/**
	 * <li>说明：更新检修任务单状态为已处理
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月8日
	 *  <li>修改人：何涛
	 * <li>修改内容：根据安康机务段需求，完工日期为包修工长签认时间
	 * <li>修改日期：2017年2月27日
	 * @param taskListIdx 检修任务单idx主键
	 * @param repairType 检修类型
	 */
	@Override
	public void updateState(Serializable taskListIdx, Integer repairType) {
		List<RepairScopeCase> entityList = this.repairScopeCaseManager.getModelsByTaskListIdx((String) taskListIdx, RepairScopeCase.STATE_WCL, null);
		if (null != entityList && !entityList.isEmpty()) {
			return;
		}
		RepairTaskList entity = this.getModelById(taskListIdx);
		entity.setState(RepairTaskList.STATE_YCL);
		// Modified by hetao on 2017-03-22 [检修任务单]上的“实际完工日期”为该检修任务下所有范围活处理完成时的记录日期，报表【设备检修记录单】上的“竣工日期”取[检修任务单处理班组]的“竣工日期”
		if (null == entity.getRealEndTime()) {
			entity.setRealEndTime(new Date());
		}
		this.daoUtils.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：分页查询-检修作业工单查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity  查询条件封装实体
	 * @param isHanding 是否只显示处理中（未验收）的检修作业工单，true：是，false：否
	 * @return 分页列表
	 */
	public Page<RepairTaskListBean> queryPageList(SearchEntity<RepairTaskListBean> searchEntity, boolean isHanding) {
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_task_list:query_page_list", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		RepairTaskListBean entity = searchEntity.getEntity();

		// 2017-02-09 hedong 首页统计下拽需要传递和统计一样的查询日期
		// 查询条件 beginTime和endTime不存在表示不是从首页跳转到统计页面
		if (null == entity.getBeginTime() || null == entity.getEndTime()) {
			// 查询条件 - 计划年度
			if (null != entity.getPlanYear()) {
				sb.append(" AND M.PLAN_YEAR ='").append(entity.getPlanYear()).append("'");
			}
			// 查询条件 - 计划月份
			if (null != entity.getPlanMonth()) {
				sb.append(" AND M.PLAN_MONTH ='").append(entity.getPlanMonth()).append("'");
			}
		} else {
			sb.append(" AND DATE_FORMAT(M.BEGIN_TIME, '%Y-%m-%d') BETWEEN '").append(DateUtil.yyyy_MM_dd.format(entity.getBeginTime())).append("' AND '").append(
					DateUtil.yyyy_MM_dd.format(entity.getEndTime())).append("'");
		}

		// 查询条件 - 单位名称
		if (!StringUtil.isNullOrBlank(entity.getOrgId())) {
			sb.append(" AND I.ORG_ID LIKE '%").append(entity.getOrgId()).append("%'");
		}
		// 查询条件 - 设备编号
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE LIKE '%").append(entity.getEquipmentCode()).append("%'");
		}
		// 查询条件 - 设备名称
		if (!StringUtil.isNullOrBlank(entity.getEquipmentName())) {
			sb.append(" AND I.EQUIPMENT_NAME LIKE '%").append(entity.getEquipmentName()).append("%'");
		}
		// 是否只显示处理中的检修作业工单
		if (isHanding) {
			sb.append(" AND L.STATE <> '").append(RepairTaskList.STATE_YYS).append("'");
		}
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		sb.append(" ORDER BY L.CREATE_TIME DESC");
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairTaskListBean.class);
	}

	/**
	 * <li>说明：分页查询-作业人员待处理的设备检修任务单pda
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月8日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年8月9日
	 * <li>修改日期：优化查询sql语句
	 * @param searchEntity 查询条件封装实体
	 * @param orgId 班组id
	 * @return 分页列表
	 */
	public Page<RepairTaskListBean> taskList(SearchEntity<RepairTaskListBean> searchEntity, String orgId) {
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_task_list:query_page_list", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		// 查询条件 - 作业班组（含电气、机械作业班组）
		if (StringUtil.isNullOrBlank(orgId)) {
			orgId = SystemContext.getOmEmployee().getOrgid() + "";
		}
		// 根据班组查询过滤
		sb.append(" AND L.IDX IN(");
		sb
				.append(" SELECT T.TASK_LIST_IDX FROM E_REPAIR_TASK_LIST_TEAM T, E_REPAIR_SCOPE_CASE C WHERE C.RECORD_STATUS = 0 AND T.TASK_LIST_IDX = C.TASK_LIST_IDX AND T.REPAIR_TYPE = C.REPAIR_TYPE AND C.STATE = '"
						+ RepairScopeCase.STATE_WCL + "' AND (',' || T.ORG_ID || ',') LIKE '%," + orgId + ",%'");
		sb.append(" )");
		RepairTaskListBean entity = searchEntity.getEntity();
		// 查询条件 - 设备编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE IN ('");
			sb.append(entity.getEquipmentCode().replace(",", "',' "));
			sb.append("')");
		}
		// 查询条件 - 查询未处理的检修任务单
		sb.append(" AND (L.STATE = '").append(RepairTaskList.STATE_WCL).append("'");
		sb.append(" OR");
		sb.append(" L.STATE = '").append(RepairTaskList.STATE_CLZ).append("'");
		sb.append(" )");

		// Modified by hetao on 2017-03-22 增加计划开工日期限制，不可以逾期进行检修任务处理
		sb.append(" AND TO_CHAR(L.BEGIN_TIME, 'YYYY-MM-DD') <= '").append(DateUtil.yyyy_MM_dd.format(new Date())).append("'");

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairTaskListBean.class);
	}

	/**
	 * <li>说明：检修任务单分页查询-工长确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月8日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @param orgId 班组id
	 * @return 分页列表
	 */
	public Page<RepairTaskListBean> taskListForGzSign(SearchEntity<RepairTaskListBean> searchEntity, String orgId) {
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_task_list:query_page_list", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		// 查询条件 - 作业班组（含电气、机械作业班组）
		if (StringUtil.isNullOrBlank(orgId)) {
			orgId = SystemContext.getOmEmployee().getOrgid() + "";
		}
		sb.append(" AND (");
		sb.append(" L.IDX IN (SELECT TASK_LIST_IDX FROM E_REPAIR_TASK_LIST_TEAM WHERE (',' || ORG_ID || ',') LIKE '%," + orgId + ",%' AND IS_CONFIRMED = '").append(
				RepairTaskListTeam.IS_CONFIRMED_TODO).append("')");
		sb.append(" OR");
		sb.append(" (");
		sb.append(" L.IDX IN (SELECT TASK_LIST_IDX FROM E_REPAIR_TASK_LIST_TEAM WHERE (',' || ORG_ID || ',') LIKE '%," + orgId + ",%' AND IS_CONFIRMED = '").append(
				RepairTaskListTeam.IS_CONFIRMED_NO).append("')");
		sb.append(" AND L.STATE ='").append(RepairTaskList.STATE_YCL).append("'");
		sb.append(" )");
		sb.append(" )");
		RepairTaskListBean entity = searchEntity.getEntity();
		// 查询条件 - 设备编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE IN ('");
			sb.append(entity.getEquipmentCode().replace(",", "',' "));
			sb.append("')");
		}
		// 查询条件 - 查询已处理的检修任务单

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairTaskListBean.class);
	}

	/**
	 * <li>说明：工长确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月8日
	 * <li>修改人：何涛
	 * <li>修改内容：根据安康机务段需求，完工日期为包修工长签认时间
	 * <li>修改日期：2017年2月27日
	 * @param ids 检修任务单idx主键数组
	 * @throws NoSuchFieldException
	 */
	public void updateGzSign(String[] ids) throws NoSuchFieldException {
		RepairTaskList entity = null;
		// 当前系统登录人员信息
		String empname = SystemContext.getOmEmployee().getOrgname();
		String orgid = SystemContext.getOmEmployee().getOrgid() + "";
		for (String idx : ids) {
			// 设备检修作业任务单
			entity = this.getModelById(idx);
			// 1、更新检修任务单处理班组已被工长确认
			List<RepairTaskListTeam> teams = this.repairTaskListTeamManager.getModels(idx, orgid);
			for (RepairTaskListTeam team : teams) {
				team.setIsConfirmed(RepairTaskListTeam.IS_CONFIRMED_YES);
				// Modified by hetao on 2017-03-22 竣工日期，是包修工长签认时间
				team.setRealEndTime(Calendar.getInstance().getTime());
				this.repairTaskListTeamManager.saveOrUpdate(team);

				// 2、更新电气工长签名；
				if (RepairScope.REPAIR_TYPE_DQ == team.getRepairType().intValue()) {
					entity.setGzSignElc(empname);
				}
				// 3、更新机械工长签名；
				if (RepairScope.REPAIR_TYPE_JX == team.getRepairType().intValue()) {
					entity.setGzSignMac(empname);
				}
			}
			// 验证检修任务单是否被所有工长确认
			if (this.repairTaskListTeamManager.isAllChecked(entity.getIdx())) {
				entity.setState(RepairTaskList.STATE_DYS);
				//				// Modified by hetao on 2017-02-27 根据安康机务段需求，完工日期为包修工长签认时间
				//				entity.setRealEndTime(Calendar.getInstance().getTime());
			}
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：检修任务单分页查询-验收人确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：何涛
	 * <li>修改内容：修改查询规则 - 工长确认后或者使用人确认后，验收才可以确认验收
	 * <li>修改日期：2016年10月25日
	 * @param searchEntity 查询条件封装实体
	 * @return 分页列表
	 */
	public Page<RepairTaskListBean> taskListForAcceptor(SearchEntity<RepairTaskListBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_task_list:query_page_list", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		RepairTaskListBean entity = searchEntity.getEntity();
		// 查询条件 - 设备编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE IN ('");
			sb.append(entity.getEquipmentCode().replace(",", "',' "));
			sb.append("')");
		}
		// 查询规则 - 工长确认后或者使用人确认后，验收才可以确认验收
		sb.append(" AND L.STATE ='").append(RepairTaskList.STATE_DYS).append("'");
		// 过滤掉对不需要进行验收的设备检修任务
		sb.append(" AND IS_NEED_ACCEPTANCE = 1 AND ACCEPTOR_ID IS NULL");

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairTaskListBean.class);
	}

	/**
	 * <li>说明：验收人确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：何涛
	 * <li>修改内容：设置“验收人ID”字段存储值为当前操作pda的操作员的人员id，设置“验收人名称”为pda端验收处理表单输入的值，具体修改需求详见方法内注释
	 * <li>修改日期：2017年1月9日
	 * @param ids 检修任务单idx主键数组
	 * @param acceptanceReviews 验收评语
	 * @param acceptorNames 验收人名称，可能为多个
	 * @throws NoSuchFieldException 
	 */
	public void updateAcceptor(String[] ids, String acceptanceReviews, String acceptorNames) throws NoSuchFieldException {
		OmEmployee userData = SystemContext.getOmEmployee();
		RepairTaskList entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			// Modified by hetao on 2017-02-27 根据安康机务段需求，验收时间为包机人签认时间
			//			entity.setAcceptanceTime(new Date()); // 验收时间
			entity.setAcceptanceReviews(acceptanceReviews); // 验收评语
			// Modified by hetao on 2017-01-09  因试用单位（安康机务段）提出设备检修验收要区分机械和电气，经沟通，修改方案为：在原设计基础上，让验收处理时可以输入多个验收人员名称
			entity.setAcceptorId(userData.getEmpid()); // 验收人id
			entity.setAcceptorName(StringUtil.isNullOrBlank(acceptorNames) ? userData.getEmpname() : acceptorNames); // 验收人名称，可能为多个
			if (entity.isAllChecked()) {
				entity.setState(RepairTaskList.STATE_YYS); // 状态：已验收
			}
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：检修任务单分页查询-使用人确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：何涛
	 * <li>修改内容：2016年8月9日
	 * <li>修改日期：优化查询sql语句
	 * @param searchEntity 查询条件封装实体
	 * @return 分页列表
	 */
	public Page<RepairTaskListBean> taskListForUser(SearchEntity<RepairTaskListBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("repair%cprocess%crepair_task_list:query_page_list", File.separatorChar, File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		RepairTaskListBean entity = searchEntity.getEntity();
		// 查询条件 - 设备编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND I.EQUIPMENT_CODE IN ('");
			sb.append(entity.getEquipmentCode().replace(",", "',' "));
			sb.append("')");
		}
		/**  Modified by hetao on 2016-09-08 工长验收后，使用人即可确认，不限制必须先验收确认 - 开始  */
		sb.append(" AND L.STATE ='").append(RepairTaskList.STATE_DYS).append("'");

		// 查询规则【old】
		/*
		 * sb.append(" AND ("); // 无需【验收人确认】的，需【工长确认】后才可以进行【使用人确认】
		 * sb.append(" (L.IS_NEED_ACCEPTANCE = 0 AND L.STATE ='"
		 * ).append(RepairTaskList.STATE_GZQR).append("')"); sb.append(" OR");
		 * // 需要【验收人确认】的，需【工长确认】-【验收人确认】后才可以进行【使用人确认】
		 * sb.append(" (L.IS_NEED_ACCEPTANCE = 1 AND L.STATE ='"
		 * ).append(RepairTaskList.STATE_YYS).append("')"); sb.append(" )");
		 */
		/**  Modified by hetao on 2016-09-08 工长验收后，使用人即可确认，不限制必须先验收确认 - 结束  */

		/** Modified by hetao on 2016-09-07 使用人所在班组的所有人，都可以确认，因此取消以下查询条件限制 - 开始 */
		// // 关联查询设备的使用人，特殊需求：工长也可以进行使用人确认（安康）
		// UserData userData = SystemContext.getUserData();
		// sb.append(" AND (I.USE_PERSON_ID = '").append(userData.getEmpid()).append("'");
		// sb.append(" OR GZ_SIGN_MAC = '").append(userData.getEmpname()).append("'");
		// sb.append(" OR GZ_SIGN_ELC = '").append(userData.getEmpname()).append("'");
		// sb.append(" )");
		sb.append(" AND L.USER_ID IS NULL");
		/** Modified by hetao on 2016-09-07 使用人所在班组的所有人，都可以确认，因此取消以下查询条件限制 - 结束 */

		// Modified by hetao on 2017-02-27 处理排序
		this.processOrdersInSql(searchEntity, sb);
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, RepairTaskListBean.class);
	}

	/**
	 * <li>说明：使用人确认
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：何涛
	 * <li>修改内容：根据安康机务段需求，验收时间为包机人签认时间
	 * <li>修改日期：2017年2月27日
	 * @param ids 检修任务单idx主键数组
	 * @param acceptanceReviews 验收评语
	 * @throws NoSuchFieldException 
	 */
	public void updateUser(String[] ids) throws NoSuchFieldException {
		OmEmployee userData = SystemContext.getOmEmployee();
		RepairTaskList entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);
			entity.setUserId(userData.getEmpid());
			entity.setUserName(userData.getEmpname());
			// Modified by hetao on 2017-02-27 根据安康机务段需求，验收时间为包机人签认时间
			entity.setAcceptanceTime(Calendar.getInstance().getTime());
			if (entity.isAllChecked()) {
				entity.setState(RepairTaskList.STATE_YYS); // 状态：已验收
			}
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：根据设备主键获取检修任务单，按更新日期倒序排序
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备主键
	 * @return 检修任务单集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairTaskList> getModelsByEquipmentIdx(String equipmentIdx) {
		String hql = "From RepairTaskList Where recordStatus = 0 And equipmentIdx = ? Order By updateTime Desc";
		return this.daoUtils.find(hql, equipmentIdx);
	}

	/**
	 * <li>说明：开工
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月29日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 检修任务单idx主键数组
	 * @throws NoSuchFieldException 
	 */
	public void updateRealBeginTime(String[] ids) throws NoSuchFieldException {
		RepairTaskList entity = null;
		List<RepairTaskList> entityList = new ArrayList<RepairTaskList>(ids.length);
		Date now = Calendar.getInstance().getTime();
		for (String idx : ids) {
			// Modified by hetao on 2017-03-22 维修班组开工
			this.repairTaskListTeamManager.updateRealBeginTime(idx, now);
			entity = this.getModelById(idx);
			if (null != entity.getRealBeginTime()) {
				continue;
			}
			entity.setRealBeginTime(now);
			entity.setState(RepairTaskList.STATE_CLZ);
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：完工
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月11日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 检修任务单idx主键数组
	 * @throws NoSuchFieldException 
	 */
	@Deprecated
	public void updateRealEndTime(String[] ids) throws NoSuchFieldException {
		RepairTaskList entity = null;
		List<RepairTaskList> entityList = new ArrayList<RepairTaskList>(ids.length);
		Date now = Calendar.getInstance().getTime();
		for (String idx : ids) {
			entity = this.getModelById(idx);
			entity.setRealEndTime(now);
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：验证设备检修任务单是否已经处理完成
	 * <li>创建人：何涛
	 * <li>创建日期：2017年1月12日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 检修任务单实体对象
	 * @return 检修任务单是否已经处理完成，true：是（已完成），false：否（未完成）
	 */
	@SuppressWarnings("unused")
	private boolean isDisposeCompleted(RepairTaskList entity) {
		// 验证使用人是否已确认
		if (StringUtil.isNullOrBlank(entity.getUserName())) {
			return false;
		}
		// 验证验收员是否已确认
		if (entity.getIsNeedAcceptance() && StringUtil.isNullOrBlank(entity.getAcceptorName())) {
			return false;
		}
		//  验证所有维修班组工长是否均已确认
		List<RepairTaskListTeam> teams = this.repairTaskListTeamManager.getModels(entity.getIdx(), null);
		for (RepairTaskListTeam team : teams) {
			if (RepairTaskListTeam.IS_CONFIRMED_NO.equals(team.getIsConfirmed())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <li>说明：级联删除设备检修范围活实例和检修任务处理班组
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 设备检修作业工单idx主键数组
	 */
	@Override
	public void deleteByIds(Serializable... ids) {
		Serializable[] array = null;
		List<RepairScopeCase> list0 = null;
		List<RepairTaskListTeam> list1 = null;
		for (Serializable idx : ids) {
			// 级联删除检修范围活实例
			list0 = this.repairScopeCaseManager.getModelsByTaskListIdx((String) idx);
			if (null == list0 || list0.isEmpty()) {
				continue;
			}
			array = new String[list0.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list0.get(i).getIdx();
			}
			this.repairScopeCaseManager.deleteByIds(array);
			// 级联删除检修任务单处理班组
			list1 = this.repairTaskListTeamManager.getModelsByTaskListIdx((String) idx);
			if (null == list1 || list1.isEmpty()) {
				continue;
			}
			array = new String[list1.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list1.get(i).getIdx();
			}
			this.repairTaskListTeamManager.deleteByIds(array);
		}
		super.deleteByIds(ids);
	}

	/**
	 * <li>说明：撤销设备检修月计划下发
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月20日
	 * <li>修改人：何涛
	 * <li>修改内容：取消已被处理的检修任务不可以被撤销的限制
	 * <li>修改日期：2017年4月18日
	 * @param entity 设备检修月计划实体对象
	 */
	public void updateForRescind(RepairPlanMonth rpm) {
		String hql = "From RepairTaskList Where recordStatus = 0 And planMonthIdx = ?";
		RepairTaskList entity = (RepairTaskList) this.daoUtils.findSingle(hql, rpm.getIdx());
		if (null == entity) {
			return;
		}
		//		// Modified by hetao on 2017-03-22 未开工的检修任务即可撤销
		//		if (!RepairTaskList.STATE_WCL.equals(entity.getState())
		//				&& null != entity.getRealBeginTime()) {
		//			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(entity.getEquipmentIdx());
		//			throw new BusinessException(String.format("%s（编号：%s）%d年%d月检修计划已被处理，不可以被撤销！", epi.getEquipmentName(), epi.getEquipmentCode(), rpm.getPlanYear(), rpm.getPlanMonth()));
		//		}
		this.deleteByIds(new Serializable[] { entity.getIdx() });
	}

	/* (non-Javadoc)
	 * @see com.yunda.frame.baseapp.upload.manager.IAttachmentManager#findImages(java.lang.String)
	 */
	@Override
	public List<Attachment> findImages(String idx) {
		List<String> attachmentKeyIds = new ArrayList<String>();
		attachmentKeyIds.add(idx);
		String hql = "From RepairWorkOrder Where recordStatus = 0 And scopeCaseIdx In (Select idx From RepairScopeCase Where recordStatus = 0 And taskListIdx = ?)";
		@SuppressWarnings("unchecked")
		List<RepairWorkOrder> list = this.daoUtils.find(hql, idx);
		for (RepairWorkOrder rwo : list) {
			attachmentKeyIds.add(rwo.getIdx());
		}
		return this.attachmentManager.findImages(attachmentKeyIds);
	}

}
