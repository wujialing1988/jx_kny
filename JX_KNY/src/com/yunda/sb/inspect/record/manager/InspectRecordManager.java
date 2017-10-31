package com.yunda.sb.inspect.record.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.upload.manager.AttachmentManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipment;
import com.yunda.sb.inspect.plan.manager.InspectPlanEquipmentEmpManager;
import com.yunda.sb.inspect.plan.manager.InspectPlanEquipmentManager;
import com.yunda.sb.inspect.record.entity.InspectRecord;
import com.yunda.sb.inspect.record.entity.InspectRecordBean;
import com.yunda.sb.inspect.scope.entity.InspectScope;
import com.yunda.sb.inspect.scope.manager.InspectScopeManager;
import com.yunda.sb.repair.scope.entity.RepairScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectRecord业务类，数据表：E_INSPECT_RECORD
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectRecordManager")
public class InspectRecordManager extends JXBaseManager<InspectRecord, InspectRecord> {

	/** InspectScope业务类，数据表：E_INSPECT_SCOPE */
	@Resource
	private InspectScopeManager inspectScopeManager;

	/** InspectPlanEquipment业务类，数据表：E_INSPECT_ROUTE_DETAILS */
	@Resource
	private InspectPlanEquipmentManager inspectPlanEquipmentManager;

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** InspectPlanEquipmentEmp管理器，数据表：E_INSPECT_PLAN_EQUIPMENT_EMP */
	@Resource
	private InspectPlanEquipmentEmpManager inspectPlanEquipmentEmpManager;

	/** 附件管理 */
	@Resource
	private AttachmentManager attachmentManager;

	/**
	 * <li>说明：实例化周期设备巡检记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：何涛
	 * <li>修改内容：修改巡检线路下添加的设备，在设备主要信息被删除时，会出现空指针异常的错误
	 * <li>修改日期：2016年9月8日
	 * @param t 巡检计划设备实体对象
	 * @throws NoSuchFieldException
	 */
	public void startUp(InspectPlanEquipment t) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
		if (null == epi) {
			return;
		}
		// 设备类别编码
		List<InspectScope> scopes = this.inspectScopeManager.getModelsByClassCode(epi.getEquipmentCode(), epi.getClassCode(), null);
		// 如果添加的设备未维护设备巡检项目，则终止操作
		if (null == scopes || scopes.isEmpty()) {
			throw new BusinessException(String.format("%s(%s)未维护设备巡检标准！", epi.getEquipmentName(), epi.getEquipmentCode()));
		}
		InspectRecord entity = null;
		List<InspectRecord> entityList = new ArrayList<InspectRecord>(scopes.size());
		for (InspectScope scope : scopes) {
			// 如果机械（电气）系数为零，则不生成机械（电气）巡检项目
			if (((null == epi.getMechanicalCoefficient() || epi.getMechanicalCoefficient() <= 0) && RepairScope.REPAIR_TYPE_JX == scope.getRepairType().intValue())
					|| ((null == epi.getElectricCoefficient() || epi.getElectricCoefficient() <= 0) && RepairScope.REPAIR_TYPE_DQ == scope.getRepairType().intValue())) {
				continue;
			}
			entity = new InspectRecord();
			entity.planEquipmentIdx(t.getIdx()) // 周期巡检设备idx主键 
					.repairType(scope.getRepairType()) // 检修类型（1：机械、2：电气、3：其它）
					.classCode(scope.getClassCode()) // 设备类别编码
					.className(scope.getClassName()) // 设备类别名称
					.checkItem(scope.getCheckItem()) // 检查项目
					.checkItemPY(scope.getCheckItemPY()) // 检查项目首拼
					.checkStandard(scope.getCheckStandard()) // 检查标准
					.seqNo(scope.getSeqNo()) // 顺序号
					.remarks(scope.getRemarks()); // 备注

			EntityUtil.setNoDelete(entity);
			entityList.add(entity);
		}

		// 定时器不能获取登录用户信息，所以不能使用manager的saveOrUpdate()方法
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：巡检记录处理，支持批量处理巡检记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月16日
	 * <li>修改人：何涛
	 * <li>修改内容：为了响应安康机务段需要将“机械”和“电气”照片分开的需求，将拍照功能放到巡检记录工单上
	 * <li>修改日期：2017年4月24日
	 * @param ids 巡检记录idx主键数组
	 * @param checkResult 检查结果，默认为：合格
	 * @param filePathArray 附件（照片）在服务器磁盘上的存放路径数组，如果是批量数据处理，此对象应该为null
	 * @throws NoSuchFieldException 
	 */
	public void confirm(String[] ids, String checkResult, String[] filePathArray) throws NoSuchFieldException {
		if (null == ids || 0 >= ids.length) {
			return;
		}
		InspectRecord entity = null;
		OmEmployee userData = SystemContext.getOmEmployee();
		Date now = Calendar.getInstance().getTime();
		for (String idx : ids) {
			entity = this.getModelById(idx);
			entity.setCheckResult(StringUtil.isNullOrBlank(checkResult) ? InspectRecord.CHECK_RESULT_HG : checkResult);
			entity.setInspectWorker(userData.getEmpname());
			entity.setInspectWorkerId(userData.getEmpid());
			entity.setCheckTime(now);
			this.saveOrUpdate(entity);

			if (null == filePathArray || 0 >= filePathArray.length) {
				continue;
			}
			// 保存附件信息
			this.attachmentManager.insert(idx, "e_inspect_record", filePathArray);
		}

	}

	/**
	 * <li>说明：获取某一类型（机械、电气）下未处理的巡检记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键 
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 一类型（机械、电气）下未处理的巡检记录
	 */
	@SuppressWarnings("unchecked")
	public List<InspectRecord> getUndoModels(String planEquipmentIdx, int repairType) {
		String hql = "From InspectRecord Where recordStatus = 0 And planEquipmentIdx = ? And repairType = ? And checkResult Is Null";
		return this.daoUtils.find(hql, planEquipmentIdx, repairType);
	}

	/**
	 * <li>说明：根据巡检设备idx主键获取设备巡检记录实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键
	 * @return 设备巡检记录实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<InspectRecord> getModelsByPlanEquipmentIdx(String planEquipmentIdx) {
		String hql = "From InspectRecord Where recordStatus = 0 And planEquipmentIdx = ?";
		return (List<InspectRecord>) this.daoUtils.getHibernateTemplate().find(hql, planEquipmentIdx);
	}

	/**
	 * <li>说明：根据巡检设备idx主键和检修类型获取设备巡检记录实体集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月12日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 设备巡检记录实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<InspectRecord> getModels(String planEquipmentIdx, Integer repairType) {
		String hql = "From InspectRecord Where recordStatus = 0 And planEquipmentIdx = ? And repairType = ?";
		return (List<InspectRecord>) this.daoUtils.find(hql, planEquipmentIdx, repairType);
	}

	/**
	 * <li>说明：分页查询 - pda巡检任务处理
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @return 设备巡检记录分页集合
	 */
	@Override
	public Page<InspectRecord> findPageList(SearchEntity<InspectRecord> searchEntity) {
		long empid = SystemContext.getOmEmployee().getEmpid();
		StringBuilder sb = new StringBuilder("Select a From InspectRecord a, InspectPlanEquipmentEmp b Where a.recordStatus = 0 And b.recordStatus = 0");
		sb.append(" And a.planEquipmentIdx = b.planEquipmentIdx And a.repairType = b.repairType");
		sb.append(" And (Concat(',',b.inspectEmpid,',') like ',%" + empid + "%,' or Concat(',',b.entrustInspectEmpid,',') like ',%" + empid + "%,')");
		InspectRecord entity = searchEntity.getEntity();
		// 查询条件 - 巡检设备idx主键
		sb.append(" And a.planEquipmentIdx = '").append(entity.getPlanEquipmentIdx()).append("'");
		// 查询条件 - 检查结果（合格，不合格）
		if (!StringUtil.isNullOrBlank(entity.getCheckResult())) {
			sb.append(" And a.checkResult = '").append(entity.getCheckResult()).append("'");
		}
		sb.append(" Order By a.repairType ASC, a.seqNo ASC");
		String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
		return this.findPageList(totalHql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit());
	}

	/**
	 * <li>说明：设备巡检记录联合分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询封装实体
	 * @return 设备巡检记录分页集合
	 */
	public Page<InspectRecordBean> queryPageList(SearchEntity<InspectRecord> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_record:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		InspectRecord entity = searchEntity.getEntity();

		// 查询条件 - 巡检设备idx主键
		sb.append(" AND T.PLAN_EQUIPMENT_IDX = '").append(entity.getPlanEquipmentIdx()).append("'");
		// 查询条件 - 检修类型（1：机械、2：电气、3：其它）
		if (null != entity.getRepairType()) {
			sb.append(" AND T.REPAIR_TYPE = '").append(entity.getRepairType()).append("'");
		}
		// 排序
		sb.append(" ORDER BY T.SEQ_NO");
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
		return this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, InspectRecordBean.class);
	}

	/**
	 * <li>说明：获取巡检记录处理情况饼图数据源，获取指定巡检设备下已巡检、未巡检记录数
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月12日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 巡检记录处理情况饼图数据源
	 */
	public List<Map<String, Object>> queryChartData(String planEquipmentIdx, Integer repairType) {
		List<InspectRecord> entityList = this.getModels(planEquipmentIdx, repairType);
		int yxjCount = 0, // 已巡检记录数
		wxjCount = 0; // 未巡检记录数
		for (InspectRecord entity : entityList) {
			if (StringUtil.isNullOrBlank(entity.getCheckResult())) {
				wxjCount++;
			} else {
				yxjCount++;
			}
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// 已巡检记录数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("K", "已巡检");
		map.put("V", yxjCount);
		result.add(map);
		map = new HashMap<String, Object>();
		// 未巡检记录数
		map.put("K", "未巡检");
		map.put("V", wxjCount);
		result.add(map);
		return result;
	}

	/**
	 * <li>说明：设备巡检记录处理，同一设备下的巡检记录处理完成后，反向更新巡检设备处理状态
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年12月6日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 设备巡检记录idx主键
	 * @param checkResult 检查结果（合格，不合格） ，如果为null，则默认为：合格
	 * @param checkTime 巡检时间，如果为null则默认为：服务器系统系统时间
	 * @throws NoSuchFieldException 
	 */
	public void updateFinished(String idx, String checkResult, Date checkTime) throws NoSuchFieldException {
		InspectRecord entity = this.getModelById(idx);
		// 巡检结果，默认为合格
		entity.setCheckResult(StringUtil.isNullOrBlank(checkResult) ? InspectRecord.CHECK_RESULT_HG : checkResult);
		// 巡检人取系统操作人员
		if (null == entity.getInspectWorkerId()) {
			entity.setInspectWorkerId(SystemContext.getOmEmployee().getEmpid());
			entity.setInspectWorker(SystemContext.getOmEmployee().getEmpname());
		}
		// 巡检时间
		if (null == entity.getCheckTime()) {
			entity.setCheckTime(null == checkTime ? Calendar.getInstance().getTime() : checkTime);
		}
		this.saveOrUpdate(entity);

		// 如果设备的某一类型（机械、电气）的巡检记录已处理完成，则更新同类型的“设备巡检人员”的巡检结果为：已巡检
		this.inspectPlanEquipmentEmpManager.updateFinishByIR(entity.getPlanEquipmentIdx(), entity.getRepairType());
		// 反向更新巡检设备处理状态
		this.inspectPlanEquipmentManager.updateFinishByIR(entity.getPlanEquipmentIdx());
	}

	/**
	 * <li>说明：设备巡检记录处理，同一设备下的巡检记录处理完成后，反向更新巡检设备处理状态【批量】
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年12月6日
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList 设备巡检记录实体集合
	 * @param checkResult 检查结果（合格，不合格） ，如果为null，则默认为：合格
	 * @throws NoSuchFieldException
	 */
	public void updateFinished(List<InspectRecord> entityList, String checkResult) throws NoSuchFieldException {
		if (null == entityList || entityList.isEmpty()) {
			return;
		}
		Date checkTime = Calendar.getInstance().getTime();
		for (InspectRecord ir : entityList) {
			this.updateFinished(ir.getIdx(), checkResult, checkTime);
		}
	}

}
