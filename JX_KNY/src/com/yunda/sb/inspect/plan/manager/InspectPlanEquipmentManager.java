package com.yunda.sb.inspect.plan.manager;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.hibernate.criterion.Order;
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
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.PositionManager;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.inspect.plan.entity.InspectPlan;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipment;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentBean;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentBean2;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentBean3;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentEmp;
import com.yunda.sb.inspect.record.entity.InspectRecord;
import com.yunda.sb.inspect.record.manager.InspectRecordManager;
import com.yunda.sb.inspect.route.entity.InspectRouteDetails;
import com.yunda.sb.inspect.route.manager.InspectRouteDetailsManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlanEquipment业务类，数据表：E_INSPECT_PLAN_EQUIPMENT
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectPlanEquipmentManager")
public class InspectPlanEquipmentManager extends JXBaseManager<InspectPlanEquipment, InspectPlanEquipment> implements IAttachmentManager {

	/** InspectlRoute业务类，数据表：E_INSPECTL_ROUTE */
	@Resource
	private InspectRouteDetailsManager inspectRouteDetailsManager;

	/** InspectRecord业务类，数据表：E_INSPECT_RECORD */
	@Resource
	private InspectRecordManager inspectRecordManager;

	/** 附件管理，数据表：JXPZ_Attachment_Manage */
	@Resource
	private AttachmentManager attachmentManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** InspectPlanEquipmentEmp管理器，数据表：E_INSPECT_PLAN_EQUIPMENT_EMP */
	@Resource
	private InspectPlanEquipmentEmpManager inspectPlanEquipmentEmpManager;

	/** OmPosition管理器，数据表：OM_POSITION */
	@Resource
	private PositionManager positionManager;

	/**
	 * <li>说明：实例化周期巡检设备
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param plan 巡检周期计划实体对象
	 * @throws NoSuchFieldException 
	 */
	public void startUp(InspectPlan plan) throws NoSuchFieldException {
		List<InspectRouteDetails> details = inspectRouteDetailsManager.getModelsByRouteIdx(plan.getRouteIdx());
		if (null == details || details.isEmpty()) {
			return;
		}
		InspectPlanEquipment equipment = null;
		for (InspectRouteDetails t : details) {
			equipment = new InspectPlanEquipment();

			equipment.setPlanIdx(plan.getIdx());
			equipment.setEquipmentIdx(t.getEquipmentIdx());
			equipment.setSeqNo(t.getSeqNo());
			equipment.setCheckResult(InspectPlanEquipment.CHECK_RESULT_WXJ);

			EntityUtil.setNoDelete(equipment);
			this.saveOrUpdate(equipment);

			// 实例化周期设备巡检记录
			this.inspectRecordManager.startUp(equipment);
			// 实例化周期设备巡检人员
			this.inspectPlanEquipmentEmpManager.startUp(equipment, t);
		}
	}

	/**
	 * <li>说明：巡检设备联合分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：何涛
	 * <li>修改内容：增加查询条件-设备编码
	 * <li>修改日期：2016-06-20
	 * @param searchEntity 查询对象实体
	 * @return 设备巡检明细查询对象分页集合
	 */
	public Page<InspectPlanEquipmentBean> queryPageList(SearchEntity<InspectPlanEquipment> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan_equipment:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		InspectPlanEquipment entity = searchEntity.getEntity();

		// 查询条件 - 设备巡检线路主键
		if (!StringUtil.isNullOrBlank(entity.getPlanIdx())) {
			sb.append(" and t.plan_idx = '").append(entity.getPlanIdx()).append("'");
		}

		// 查询条件 - 巡检结果
		if (!StringUtil.isNullOrBlank(entity.getCheckResult())) {
			sb.append(" and t.check_result ='").append(entity.getCheckResult()).append("'");
		}

		// 查询条件 - 设备编码或名称（特殊引用），如果有多个则以英文状态逗号（,）进行分隔
		if (!StringUtil.isNullOrBlank(entity.getEquipmentIdx())) {
			String param = entity.getEquipmentIdx().indexOf(",") <= 0 ? entity.getEquipmentIdx() : entity.getEquipmentIdx().replace(",", "', '");
			sb.append(" and t.equipment_idx in (");
			sb.append(" select idx from e_equipment_primary_info where record_status = 0 and (equipment_code in ('").append(param).append(
					"') or equipment_name like '%" + entity.getEquipmentIdx() + "%')");
			sb.append(" )");
		}

		// Modified by hetao on 2016-010-17 增加页面按字段内容排序查询的处理
		Order[] orders = searchEntity.getOrders();
		if (null != orders && orders.length > 0) {
			Field field0;
			Field field1;
			Field field = null;
			boolean ascending = false;
			String propertyName = null;
			try {
				field0 = Order.class.getDeclaredField("ascending");
				field0.setAccessible(true);
				field1 = Order.class.getDeclaredField("propertyName");
				field1.setAccessible(true);
				ascending = field0.getBoolean(orders[0]);
				propertyName = field1.get(orders[0]).toString();
				field = InspectPlanEquipmentBean.class.getDeclaredField(propertyName);
			} catch (Exception e) {
				throw new BusinessException(e);
			}

			Column annotation = field.getAnnotation(Column.class);
			if (null != annotation) {
				String name = annotation.name();
				if (null != name) {
					sb.append(" order by t.").append(name);
				}
			} else {
				sb.append(" order by t.").append(propertyName);
			}
			sb.append(ascending ? " asc" : " desc");
		}

		String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("from"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanEquipmentBean.class);
	}

	/**
	 * <li>说明：巡检设备联合分页查询，用于pda任务处理，如果操作人员是工长身份，则可以查询其所在班组所有巡检任务
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：何涛
	 * <li>修改内容：如果操作人员是工长身份，则可以查询其所在班组所有巡检任务
	 * <li>修改日期：2017年3月10日
	 * @param searchEntity 查询对象实体
	 * @param onlyMe 是否只查询自己的任务，此标识主要针对于身份为工长的系统操作员，如果为false，则工长可以查询其班组下所有巡检任务
	 * @return 设备巡检明细查询对象分页集合
	 */
	public Page<InspectPlanEquipmentBean2> queryPageList2(SearchEntity<InspectPlanEquipment> searchEntity, boolean onlyMe) {
		if (onlyMe) {
			return this.queryPageList2(searchEntity);
		}
		// 验证当前操作人员是否是工长
		boolean isGz = this.positionManager.validatePosi(SystemContext.getOmEmployee().getEmpid(), SystemContext.getOmEmployee().getOrgid(), "工长");
		if (isGz) {
			return this.queryPageList2GZ(searchEntity);
		}
		return this.queryPageList2(searchEntity);
	}

	/**
	 * <li>说明：巡检设备联合分页查询，用于pda任务处理
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @return 设备巡检明细查询对象分页集合
	 */
	private Page<InspectPlanEquipmentBean2> queryPageList2(SearchEntity<InspectPlanEquipment> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan_equipment:queryPageList2", File.separatorChar));
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (").append(sql).append(") t where 0 = 0");

		InspectPlanEquipment entity = searchEntity.getEntity();

		// 查询条件 - 设备巡检线路主键
		if (!StringUtil.isNullOrBlank(entity.getPlanIdx())) {
			sb.append(" and t.plan_idx = '").append(entity.getPlanIdx()).append("'");
		}
		// 查询条件 - 设备编码或名称（特殊引用），如果有多个则以英文状态逗号（,）进行分隔
		// 此处用逗号分隔多个值是考虑pda一次扫描到了多张RFID的情况
		if (!StringUtil.isNullOrBlank(entity.getEquipmentIdx())) {
			String param = entity.getEquipmentIdx().indexOf(",") <= 0 ? entity.getEquipmentIdx() : entity.getEquipmentIdx().replace(",", "', '");
			sb.append(" and t.equipment_idx in (");
			sb.append(" select idx from e_equipment_primary_info where record_status = 0 and (equipment_code in ('").append(param).append(
					"') or equipment_name like '%" + entity.getEquipmentIdx() + "%')");
			sb.append(" )");
		}

		// 巡检状态
		if (!StringUtil.isNullOrBlank(entity.getCheckResult())) {
			sb.append(" and t.check_result = '").append(entity.getCheckResult()).append("'");
		}

		sb.append(" order by t.equipment_code asc");

		// 根据是否还有未处理的巡检记录过滤机械（或：电气）巡检人员需要巡检的设备
		sql = "select * from (" + sb.toString().replace("?", SystemContext.getOmEmployee().getEmpid() + "") + ") t";

		String totalSql = "select count(*) as rowcount " + sql.substring(sql.indexOf("from"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanEquipmentBean2.class);
	}

	/**
	 * <li>说明：巡检设备联合分页查询，用于pda任务处理，如果操作人员是工长身份，则可以查询其所在班组所有巡检任务
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @return 设备巡检明细查询对象分页集合
	 */
	private Page<InspectPlanEquipmentBean2> queryPageList2GZ(SearchEntity<InspectPlanEquipment> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan_equipment:queryPageList2GZ", File.separatorChar));
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (").append(sql).append(") t where 0 = 0");

		InspectPlanEquipment entity = searchEntity.getEntity();

		// 查询条件 - 设备巡检线路主键
		if (!StringUtil.isNullOrBlank(entity.getPlanIdx())) {
			sb.append(" and t.plan_idx = '").append(entity.getPlanIdx()).append("'");
		}
		// 查询条件 - 设备编码或名称（特殊引用），如果有多个则以英文状态逗号（,）进行分隔
		// 此处用逗号分隔多个值是考虑pda一次扫描到了多张RFID的情况
		if (!StringUtil.isNullOrBlank(entity.getEquipmentIdx())) {
			String param = entity.getEquipmentIdx().indexOf(",") <= 0 ? entity.getEquipmentIdx() : entity.getEquipmentIdx().replace(",", "', '");
			sb.append(" and t.equipment_idx in (");
			sb.append(" select idx from e_equipment_primary_info where record_status = 0 and (equipment_code in ('").append(param).append(
					"') or equipment_name like '%" + entity.getEquipmentIdx() + "%')");
			sb.append(" )");
		}

		// 巡检状态
		if (!StringUtil.isNullOrBlank(entity.getCheckResult())) {
			sb.append(" and t.check_result = '").append(entity.getCheckResult()).append("'");
		}

		sb.append(" order by t.equipment_code asc");

		// 根据是否还有未处理的巡检记录过滤机械（或：电气）巡检人员需要巡检的设备
		sql = "select * from (" + sb.toString().replace("?", SystemContext.getOmEmployee().getOrgid() + "") + ") t";

		String totalSql = "select count(*) as rowcount " + sql.substring(sql.indexOf("from"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanEquipmentBean2.class);
	}

	/**
	 * <li>说明：设备巡检日志联合分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月2日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @return 设备巡检明细查询对象分页集合
	 */
	public Page<InspectPlanEquipmentBean3> queryPageList3(SearchEntity<InspectPlanEquipmentBean3> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_plan_equipment:queryPageList3", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);

		InspectPlanEquipmentBean3 entity = searchEntity.getEntity();

		// 查询条件 - 设备idx主键
		if (!StringUtil.isNullOrBlank(entity.getEquipmentIdx())) {
			sb.append(" AND EQUIPMENT_IDX = '").append(entity.getEquipmentIdx()).append("'");
		}

		// 查询条件 - 计划开始日期
		if (null != entity.getPlanStartDate()) {
			sb.append(" AND PLAN_START_DATE >= to_date('").append(DateUtil.yyyy_MM_dd.format(entity.getPlanStartDate())).append("', 'yyyy-mm-dd')");
		}

		// 查询条件 - 计划结束日期
		if (null != entity.getPlanEndDate()) {
			sb.append(" AND PLAN_START_DATE <= to_date('").append(DateUtil.yyyy_MM_dd.format(entity.getPlanEndDate())).append("', 'yyyy-mm-dd')");
		}

		this.processOrdersInSql(searchEntity, sb);
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, InspectPlanEquipmentBean3.class);
	}

	/**
	 * <li>说明：分页查询，查询需要使用人确认的巡检设备 - pda
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @return 设备巡检明细查询对象分页集合
	 */
	public Page<InspectPlanEquipment> queryPageList2User(SearchEntity<InspectPlanEquipment> searchEntity) {
		StringBuilder sb = new StringBuilder();
		// 查询语句
		sb.append("Select new InspectPlanEquipment(t.idx, i.equipmentName, i.equipmentCode, i.model, i.specification, i.usePlace)").append(
				"From InspectPlanEquipment t, EquipmentPrimaryInfo i Where t.recordStatus = 0 And i.recordStatus = 0 And t.equipmentIdx = i.idx")
		// 查询条件 - 已巡检的设备
				.append(" And t.checkResult = '").append(InspectPlanEquipment.CHECK_RESULT_YXJ).append("'")
				// 查询条件 - 还未被使用人确认的设备
				.append(" And t.useWorkerId Is Null");

		// 查询条件
		InspectPlanEquipment entity = searchEntity.getEntity();
		sb.append(" And t.planIdx = '").append(entity.getPlanIdx()).append("'");

		// 查询条件 - 设备编码、或者设备名称
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" And (");
			sb.append(" i.equipmentCode Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" Or");
			sb.append(" i.equipmentName Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" )");
		}

		String hql = sb.toString();
		String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
	}

	/**
	 * <li>说明：提交巡检设备处理，更新巡检结果为：已巡检，并向上更新巡检计划状态，更新前不会验证巡检记录是否都已处理
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx idx主键
	 * @param checkResultDesc 巡检情况描述
	 * @throws NoSuchFieldException
	 */
	public void updateFinish(String idx, String checkResultDesc) throws NoSuchFieldException {
		InspectPlanEquipment entity = this.getModelById(idx);
		entity.setCheckResult(InspectPlanEquipment.CHECK_RESULT_YXJ); // 巡检结果 - 已巡检
		// Modified by hetao on 2017-03-07 增加设备巡检的实际开工、完工时间记录
		entity.setRealEndTime(Calendar.getInstance().getTime());
		entity.setCheckResultDesc(checkResultDesc); // 巡检情况描述
		this.saveOrUpdate(entity);

		// 如果该条巡检下属所有设备已巡检完成，则自动更新巡检计划为：已处理
		this.updateFinsih2InspectPlan(entity);
	}

	/**
	 * <li>说明：检查并更新巡检计划的状态为：已处理
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月11日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 巡检设备实体对象
	 * @throws NoSuchFieldException 
	 */
	private void updateFinsih2InspectPlan(InspectPlanEquipment entity) throws NoSuchFieldException {
		String hql = "From InspectPlanEquipment Where recordStatus = 0 And planIdx = ? And checkResult = ?";
		int count = this.daoUtils.getCount(hql, entity.getPlanIdx(), InspectPlanEquipment.CHECK_RESULT_WXJ);
		// 检验巡检计划下属的设备是否均已巡检
		if (0 < count) {
			return;
		}
		// 更新巡检计划为已处理
		InspectPlan plan = (InspectPlan) this.daoUtils.get(entity.getPlanIdx(), InspectPlan.class);
		plan.setState(InspectPlan.STATE_YCL); // 已处理
		// 设置实际结束时间
		plan.setRealEndDate(Calendar.getInstance().getTime());
		EntityUtil.setSysinfo(plan);
		this.daoUtils.saveOrUpdate(plan);
	}

	/**
	 * <li>说明：根据巡检计划或巡检设备集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planIdx 巡检计划idx主键
	 * @return 巡检设备集合
	 */
	@SuppressWarnings("unchecked")
	public List<InspectPlanEquipment> getModelsByPlanIdx(String planIdx) {
		String hql = "From InspectPlanEquipment Where recordStatus = 0 And planIdx = ?";
		return this.daoUtils.find(hql, planIdx);
	}

	/**
	 * <li>说明：设备使用人确认巡检设备处理结果
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年10月11日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids idx主键数组
	 * @throws NoSuchFieldException
	 */
	public void confirm(String[] ids) throws NoSuchFieldException {
		OmEmployee userData = SystemContext.getOmEmployee();
		for (Serializable idx : ids) {
			InspectPlanEquipment entity = this.getModelById(idx);
			if (null != entity.getUseWorkerId()) {
				EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(entity.getEquipmentIdx());
				throw new BusinessException(String.format("设备：%s(%s)已被【%s】确认，请刷新后重试！", epi.getEquipmentName(), epi.getEquipmentCode(), entity.getUseWorker()));
			}
			entity.setUseWorker(userData.getEmpname());
			entity.setUseWorkerId(userData.getEmpid());
			this.saveOrUpdate(entity);
		}
	}

	/**
	 * <li>说明：保存巡检设备
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planIdx 巡检计划idx主键
	 * @param equipmentIds 巡检设备idx主键
	 * @throws NoSuchFieldException 
	 */
	public void save(String planIdx, String[] equipmentIds) throws NoSuchFieldException {
		InspectPlanEquipment entity = null;
		EquipmentPrimaryInfo epi = null;
		for (String equipmentIdx : equipmentIds) {
			epi = this.equipmentPrimaryInfoManager.getModelById(equipmentIdx);
			if (null == epi) {
				throw new BusinessException("数据异常，未查询到idx为：" + equipmentIdx + "的设备信息！");
			}
			entity = new InspectPlanEquipment();

			entity.setPlanIdx(planIdx);
			entity.setEquipmentIdx(equipmentIdx);
			entity.setCheckResult(InspectPlanEquipment.CHECK_RESULT_WXJ);
			this.saveOrUpdate(entity);

			// 实例化周期设备巡检记录
			this.inspectRecordManager.startUp(entity);
			// 实例化周期设备巡检人员
			this.inspectPlanEquipmentEmpManager.startUp(entity);
		}
	}

	/**
	 * <li>说明：级联删除巡检记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 巡检设备idx主键数组
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void logicDelete(Serializable... ids) throws NoSuchFieldException {
		Serializable[] array = null;
		for (Serializable idx : ids) {
			// 逻辑删除设备巡检记录
			List<InspectRecord> list1 = this.inspectRecordManager.getModelsByPlanEquipmentIdx((String) idx);
			if (null != list1 && !list1.isEmpty()) {
				array = new String[list1.size()];
				for (int i = 0; i < array.length; i++) {
					array[i] = list1.get(i).getIdx();
				}
				this.inspectRecordManager.logicDelete(array);
			}
			// 逻辑删除设备巡检人员
			List<InspectPlanEquipmentEmp> list2 = this.inspectPlanEquipmentEmpManager.getModelsByPlanEquipmentIdx((String) idx);
			if (null != list2 && !list2.isEmpty()) {
				array = new String[list2.size()];
				for (int i = 0; i < array.length; i++) {
					array[i] = list2.get(i).getIdx();
				}
				this.inspectPlanEquipmentEmpManager.logicDelete(array);
			}
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明：保存设备巡检照片
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx 巡检设备idx主键
	 * @param filePathArray 附件（照片）在服务器上存储的文件全路径
	 * @throws NoSuchFieldException
	 */
	@Deprecated
	public void saveInspectPhoto(String idx, String[] filePathArray) throws NoSuchFieldException {
		// 保存附件信息
		if (null == filePathArray || 0 >= filePathArray.length) {
			throw new BusinessException("操作失败，没有可以保存的照片信息！");
		}
		this.attachmentManager.insert(idx, "e_inspect_plan_equipment", filePathArray);
	}

	/**
	 * <li>说明：提交巡检设备处理，更新巡检结果为：已巡检，并向上更新巡检计划状态，更前会验证巡检记录是否都已处理
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年12月6日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 巡检设备idx主键
	 * @throws NoSuchFieldException 
	 */
	public void updateFinishByIR(String idx) throws NoSuchFieldException {
		// 查询该设备下是否还有未处理的巡检记录
		String hql = "From InspectRecord Where recordStatus = 0 And planEquipmentIdx = ? And checkResult Is Null";
		int count = this.daoUtils.getCount(hql, idx);
		if (0 < count) {
			return;
		}
		// 提交巡检设备处理，更新巡检结果为：已巡检，并向上更新巡检计划状态，更新前不会验证巡检记录是否都已处理
		this.updateFinish(idx, null);
	}

	/**
	 * <li>说明：统一处理巡检设备下的所有巡检记录，更新巡检结果为：已巡检，并向上更新巡检计划状态【一键处理】
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年12月6日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 巡检设备idx主键
	 * @param checkResult 巡检记录的检查结果（合格，不合格） ，如果为null，则默认为：合格
	 * @throws NoSuchFieldException
	 */
	public void updateFinishedByOneKey(String idx, String checkResult) throws NoSuchFieldException {
		// 查询巡检设备下还未处理的巡检记录
		String hql = "From InspectRecord Where recordStatus = 0 And planEquipmentIdx = ? And checkResult Is Null";
		@SuppressWarnings("unchecked")
		List<InspectRecord> list = this.daoUtils.find(hql, idx);
		if (null == list || list.isEmpty()) {
			// 如果没有需要处理的巡检记录，则直接更新巡检设备状态为：已巡检
			this.updateFinish(idx, null);
			return;
		}
		// 处理巡检记录
		this.inspectRecordManager.updateFinished(list, checkResult);
	}

	/**
	 * <li>说明：统一处理巡检设备下的所有巡检记录，更新巡检结果为：已巡检，并向上更新巡检计划状态【批量处理】
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月6日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 巡检设备idx主键数组
	 * @param checkResult 巡检记录的检查结果（合格，不合格） ，如果为null，则默认为：合格
	 * @throws NoSuchFieldException
	 */
	public void updateFinished(String[] ids, String checkResult) throws NoSuchFieldException {
		if (null == ids || 0 >= ids.length) {
			return;
		}
		for (String idx : ids) {
			this.updateFinishedByOneKey(idx, checkResult);
		}
	}

	/**
	 * <li>说明：开工
	 * <li>创建人：何涛
	 * <li>创建日期：2016年3月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 检修任务单idx主键数组
	 * @throws NoSuchFieldException 
	 */
	public void updateRealBeginTime(String[] ids) throws NoSuchFieldException {
		InspectPlanEquipment entity = null;
		List<InspectPlanEquipment> entityList = new ArrayList<InspectPlanEquipment>(ids.length);
		Date now = Calendar.getInstance().getTime();
		for (String idx : ids) {
			entity = this.getModelById(idx);
			if (null != entity.getRealBeginTime()) {
				continue;
			}
			entity.setRealBeginTime(now);
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：根据巡检设备idx主键，获取该巡检设备及其下属所有设备巡检记录关联的照片信息集合
	 * <li>创建人：何涛
	 * <li>创建日期：2017年4月24日
	 * <li>修改人：何涛
	 * <li>修改内容：优化代码，减少一次请求中的数据库查询次数
	 * <li>修改日期：2017年4月24日
	 * @param idx 巡检设备idx主键
	 * @return 该业务已关联（包含直接关联和间接关联）的照片附件信息对象集合
	 */
	@Override
	public List<Attachment> findImages(String idx) {
		List<String> attachmentKeyIds = new ArrayList<String>();
		attachmentKeyIds.add(idx);
		List<InspectRecord> list = this.inspectRecordManager.getModelsByPlanEquipmentIdx(idx);
		for (InspectRecord inspectRecord : list) {
			attachmentKeyIds.add(inspectRecord.getIdx());
		}
		return this.attachmentManager.findImages(attachmentKeyIds);
	}
}
