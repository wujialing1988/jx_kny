package com.yunda.sb.pointcheck.manager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.fault.entity.FaultOrder;
import com.yunda.sb.pointcheck.entity.EquipmentShutdown;
import com.yunda.sb.pointcheck.entity.EquipmentShutdownBean;
import com.yunda.sb.repair.process.entity.RepairTaskList;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentShutdownManager，数据表：SBJX_EQUIPMENT_SHUTDOWN
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "equipmentShutdownManager")
public class EquipmentShutdownManager extends JXBaseManager<EquipmentShutdown, EquipmentShutdown> {

	private static final DateFormat df0 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

	private static final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/**
	 * <li>说明：根据设备idx主键获取设备停机记录
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param types 查询条件 - 停机类型，多个状态已逗号进行分隔，如：-1,临修,小修
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EquipmentShutdown> getModelsByEquipmentIdx(String equipmentIdx, Date startTime, Date endTime, String types) {
		String hql = "From EquipmentShutdown Where recordStatus = 0 And equipmentIdx = ?";
		StringBuilder sb = new StringBuilder(hql);
		sb.append(" And startTime >= '").append(df0.format(startTime)).append("'");
		sb.append(" And startTime <= '").append(df1.format(endTime)).append("'");
		sb.append(" And type In ('").append(types.replace(",", "','")).append("')");
		return this.daoUtils.find(sb.toString(), equipmentIdx);
	}

	/**
	 * <li>说明：更新设备停机记录的开始、结束时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entityList 更新对象集合，[{
	 * 		"idx": "8a8283c94fb5eef7014fb5f460a70000",
	 * 		"startTime": "2016-09-28 21:00",
	 * 		"endTime": "2016-09-28 22:00"
	 * }]
	 * @throws NoSuchFieldException 
	 */
	public void update(EquipmentShutdown[] entityList) throws NoSuchFieldException {
		List<EquipmentShutdown> list = new ArrayList<EquipmentShutdown>(entityList.length);
		EquipmentShutdown t = null;
		for (EquipmentShutdown es : entityList) {
			t = this.getModelById(es.getIdx());
			if (null == t) {
				throw new BusinessException("数据异常，请刷新后重试！");
			}
			t.setStartTime(es.getStartTime()); // 开始时间
			t.setEndTime(es.getEndTime()); // 结束时间
			list.add(t);
		}
		this.saveOrUpdate(list);
	}

	/**
	 * <li>说明：分页查询，统计设备停机总停时
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param  searchEntity查询条件实体
	 * @return 故障提票统计对象集合 
	 */
	public Page<EquipmentShutdownBean> queryPageStatistics(SearchEntity<EquipmentShutdownBean> searchEntity) {
		String sql = SqlMapUtil.getSql(String.format("check%cequipment_shutdown_vis:queryPageStatistics", File.separatorChar));

		EquipmentShutdownBean entity = searchEntity.getEntity();
		// 查询条件 - 设备类别
		if (!StringUtil.isNullOrBlank(entity.getClassName())) {
			sql = sql.replace("ORDER BY", "WHERE T.CLASS_NAME = '" + entity.getClassName() + "' ORDER BY");
		}
		// 查询条件 - 开始时间
		if (null != entity.getStartDate()) {
			sql = sql.replace("1988-02-10", DateUtil.yyyy_MM_dd.format(entity.getStartDate()));
		}
		// 查询条件 - 结束时间
		if (null != entity.getEndDate()) {
			sql = sql.replace("2099-01-01", DateUtil.yyyy_MM_dd.format(entity.getEndDate()));
		}

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT FROM (" + sql.toString() + ") T";
		return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, EquipmentShutdownBean.class);
	}

	/**
	 * <li>说明：更新设备类别信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws NoSuchFieldException 
	 */
	public void updatEquipmentClass() throws NoSuchFieldException {
		String hql = "From EquipmentShutdown Where recordStatus = 0";
		@SuppressWarnings("unchecked")
		List<EquipmentShutdown> list = this.daoUtils.find(hql);
		List<EquipmentShutdown> entityList = new ArrayList<EquipmentShutdown>();
		EquipmentPrimaryInfo epi = null;
		for (EquipmentShutdown t : list) {
			epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
			if (null == epi || null == epi.getClassCode()) {
				continue;
			}
			if (epi.getClassCode().equals(t.getClassCode())) {
				continue;
			}
			t.setClassCode(epi.getClassCode());
			t.setClassName(epi.getClassName());
			entityList.add(t);
		}
		this.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：更加设备idx主键创建设备停机登记实体对象，设置设备基本信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @return 已设置设备基本信息的实体停机实体对象
	 */
	private EquipmentShutdown createModelByEquipmentIdx(String equipmentIdx) {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(equipmentIdx);
		if (null == epi) {
			return null;
		}
		EquipmentShutdown entity = new EquipmentShutdown().equipmentIdx(epi.getIdx()) // 设备idx主键		
				.equipmentCode(epi.getEquipmentCode()) // 设备编码
				.equipmentName(epi.getEquipmentName()) // 设备名称
				.classCode(epi.getClassCode()) // 设备类别编码
				.className(epi.getClassName()); // 设备类别名称
		return entity;
	}

	/**
	 * <li>说明：设备故障提票处理完成后自动记录设备停机时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param faultOrder 故障提票实体对象
	 * @throws NoSuchFieldException
	 */
	public void insertByFaultOrder(FaultOrder faultOrder) throws NoSuchFieldException {
		// 设备故障处理完成后记录设备停机时间
		if (!FaultOrder.STATE_YCL.equals(faultOrder.getState())) {
			return;
		}
		EquipmentShutdown entity = this.createModelByEquipmentIdx(faultOrder.getEquipmentIdx());
		if (null == entity) {
			return;
		}
		entity.type(EquipmentShutdown.SHUTDOWN_TYPE_LX).startTime(faultOrder.getFaultOccurTime()).endTime(faultOrder.getUpdateTime());
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：设备检修使用人确认后自动记录设备停机时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairTaskList 检修任务单实体对象
	 * @throws NoSuchFieldException
	 */
	public void insertByRepairTaskList(RepairTaskList repairTaskList) throws NoSuchFieldException {
		// 使用人确认后记录设备停机时间
		if (!RepairTaskList.STATE_YYS.equals(repairTaskList.getState())) {
			return;
		}
		EquipmentShutdown entity = this.createModelByEquipmentIdx(repairTaskList.getEquipmentIdx());
		if (null == entity) {
			return;
		}
		entity.type(repairTaskList.getRepairClassName() + "修").startTime(repairTaskList.getRealBeginTime()).endTime(repairTaskList.getRealEndTime());
		this.saveOrUpdate(entity);
	}

}
