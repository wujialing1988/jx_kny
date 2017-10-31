package com.yunda.sb.inspect.route.manager;

import java.io.File;
import java.util.ArrayList;
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
import com.yunda.sb.inspect.route.entity.InspectRouteDetails;
import com.yunda.sb.inspect.route.entity.InspectRouteDetailsBean;
import com.yunda.sb.inspect.scope.entity.InspectScope;
import com.yunda.sb.inspect.scope.manager.InspectScopeManager;
import com.yunda.sb.repair.scope.entity.RepairScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectRouteDetails业务类，数据表：E_INSPECT_ROUTE_DETAILS
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectRouteDetailsManager")
public class InspectRouteDetailsManager extends JXBaseManager<InspectRouteDetails, InspectRouteDetails> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** InspectScope业务类，数据表：E_INSPECT_SCOPE */
	@Resource
	private InspectScopeManager inspectScopeManager;

	/**
	 * <li>说明：设备巡检明细的联合分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询对象实体
	 * @return 设备巡检明细查询对象分页集合
	 * @throws BusinessException
	 */
	public Page<InspectRouteDetailsBean> queryPageList(SearchEntity<InspectRouteDetailsBean> searchEntity) throws BusinessException {
		String sql = SqlMapUtil.getSql(String.format("inspect%cinspect_route:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		InspectRouteDetailsBean entity = searchEntity.getEntity();
		// 查询条件 - 设备巡检线路主键
		if (!StringUtil.isNullOrBlank(entity.getRouteIdx())) {
			sb.append(" and d.route_idx = '").append(entity.getRouteIdx()).append("'");
		}
		sb.append(" order by i.equipment_name asc");
		String totalSql = "select count(*) as rowcount " + sb.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, InspectRouteDetailsBean.class);
	}

	/**
	 * <li>说明：保存巡检线路明细，添加已选择的设备到巡检计划中
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：何涛
	 * <li>修改内容：添加设备到巡检计划时，验证该类型设备是否维护了巡检标准（项目）
	 * <li>修改日期：2016年10月12日
	 * <li>修改人：何涛
	 * <li>修改日期：2017年2月23日
	 * <li>修改内容：修改同类型不同设备存在不同巡检标准的问题，修改后巡检标准可以关联到具体的固资设备
	 * @param routeIdx 巡检线路idx主键
	 * @param equipmentIds 设备信息idx主键数组
	 * @throws NoSuchFieldException 
	 */
	public void save(String routeIdx, String[] equipmentIds) throws NoSuchFieldException {
		List<InspectRouteDetails> entityList = new ArrayList<InspectRouteDetails>(equipmentIds.length);
		InspectRouteDetails entity = null;
		for (String equipmentIdx : equipmentIds) {
			EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(equipmentIdx);
			if (StringUtil.isNullOrBlank(epi.getClassCode())) {
				throw new BusinessException("未查询到设备的类别编码，请先维护设备类别后重试！");
			}
			// Modified by hetao on 2016-10-27  因为生成设备的巡检计划时，会依据设备的机械、电气系数生成相应的巡检标准，如果某一设备机械、电气系数均未设置，则生成的数据会成为异常数据
			// 验证设备的机械、电气系数是否已设置
			if ((null == epi.getElectricCoefficient() || 0 >= epi.getElectricCoefficient()) && (null == epi.getMechanicalCoefficient() || 0 >= epi.getMechanicalCoefficient())) {
				throw new BusinessException(String.format("%s(编号：%s)机械、电气系数未设置，请设置后重试！", epi.getEquipmentName(), epi.getEquipmentCode()));
			}

			List<InspectScope> scopes = null;
			// 验证该类型设备是否维护了机械巡检标准（项目）
			if (null != epi.getMechanicalCoefficient() && 0 < epi.getMechanicalCoefficient()) {
				scopes = this.inspectScopeManager.getModelsByClassCode(epi.getEquipmentCode(), epi.getClassCode(), RepairScope.REPAIR_TYPE_JX);
				if (null == scopes || scopes.isEmpty()) {
					throw new BusinessException(String.format("%s(%s)未维护设备【机械】巡检标准！", epi.getEquipmentName(), epi.getEquipmentCode()));
				}
			}
			// 验证该类型设备是否维护了电气巡检标准（项目）
			if (null != epi.getElectricCoefficient() && 0 < epi.getElectricCoefficient()) {
				scopes = this.inspectScopeManager.getModelsByClassCode(epi.getEquipmentCode(), epi.getClassCode(), RepairScope.REPAIR_TYPE_DQ);
				if (null == scopes || scopes.isEmpty()) {
					throw new BusinessException(String.format("%s(%s)未维护设备【电气】巡检标准！", epi.getEquipmentName(), epi.getEquipmentCode()));
				}
			}

			entity = new InspectRouteDetails();
			entity.setRouteIdx(routeIdx);
			entity.setEquipmentIdx(equipmentIdx);

			// 设置设备包修人（机械维修人员、电气维修人员）为默认设备巡检人
			// 机械巡检人员
			entity.setMacInspectEmp(epi.getMechanicalRepairPerson());
			entity.setMacInspectEmpid(epi.getMechanicalRepairPersonId());
			// 电气巡检人员
			entity.setElcInspectEmp(epi.getElectricRepairPerson());
			entity.setElcInspectEmpid(epi.getElectricRepairPersonId());

			entityList.add(entity);
		}
		super.saveOrUpdate(entityList);
	}

	/**
	 * <li>说明：根据巡检线路idx主键获取周期巡检设备
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeIdx 巡检线路idx主键
	 * @return 周期巡检设备对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<InspectRouteDetails> getModelsByRouteIdx(String routeIdx) {
		String hql = "From InspectRouteDetails Where recordStatus = 0 And routeIdx = ?";
		return (List<InspectRouteDetails>) this.daoUtils.getHibernateTemplate().find(hql, routeIdx);
	}

	/**
	 * <li>说明：保存设备巡检人员
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 设备检修范围明细idx主键数组
	 * @param t 设备检修范围明细数据封装实体
	 * @throws NoSuchFieldException 
	 */
	public void saveInspectEmp(String[] ids, InspectRouteDetails t) throws NoSuchFieldException {
		List<InspectRouteDetails> entityList = new ArrayList<InspectRouteDetails>(ids.length);
		InspectRouteDetails entity = null;
		for (String idx : ids) {
			entity = this.getModelById(idx);

			// 机械巡检人员
			if (!StringUtil.isNullOrBlank(t.getMacInspectEmpid())) {
				entity.setMacInspectEmp(t.getMacInspectEmp());
				entity.setMacInspectEmpid(t.getMacInspectEmpid());
			}
			// 电气巡检人员
			if (!StringUtil.isNullOrBlank(t.getElcInspectEmpid())) {
				entity.setElcInspectEmp(t.getElcInspectEmp());
				entity.setElcInspectEmpid(t.getElcInspectEmpid());
			}

			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
	}

}
