package com.yunda.sb.newbuyequipment.manager;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.newbuyequipment.entity.NewBuyEquipment;
import com.yunda.sb.newbuyequipment.entity.NewBuyEquipmentBean;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明： 新购设备业务类
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "newBuyEquipmentManager")
public class NewBuyEquipmentManager extends JXBaseManager<NewBuyEquipment, NewBuyEquipment> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/**
	 * <li>说明：重写逻辑删除方法，级联删除设备主要信息
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 新购设备级联idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws NoSuchFieldException {
		for (Serializable idx : ids) {
			NewBuyEquipment t = this.getModelById(idx);
			// 级联删除设备主要信息
			this.equipmentPrimaryInfoManager.logicDelete(t.getEquipmentIdx());
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明：新购设备联合分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @return 分页列表
	 */
	public Page<NewBuyEquipmentBean> queryPageList(SearchEntity<NewBuyEquipmentBean> searchEntity) {
		String sql = "SELECT T.idx, T.BUY_DATE, T.EQUIPMENT_IDX, T.RATIFY_ORGNAME, T.RATIFY_ORGID, T.AFFILIATED_ORGNAME, T.AFFILIATED_ORGID, T.BUY_BATCH_NUM, T.REMARK, T.responsible_person," +
				" T.responsible_person_id, T.record_status, T.creator, T.create_time, T.updator, T.update_time," +
				"I.org_name, I.org_id, I.class_code, I.class_name, I.equipment_name, I.equipment_code, I.fixed_asset_no, I.model, I.specification, I.mechanical_coefficient, I.electric_coefficient, I.make_factory, I.make_date," +
				"I.fixed_asset_value, I.use_date, I.use_place, I.manage_level, I.manage_class, I.max_repair_year, I.weight, I.leave_factory_no, I.eletric_total_power, I.tec_level," +
				"I.tec_status, I.shape_size, I.is_primary_device, I.is_dedicated, I.is_special_type, I.is_exactness, I.is_frock, I.dynamic, I.fc_state, I.xz_state, I.cz_state, I.runing_shifts, I.USE_WORKSHOP_ID," +
				"I.USE_WORKSHOP, I.USE_PERSON_ID, I.USE_PERSON, I.MECHANICAL_REPAIR_PERSON_ID, I.MECHANICAL_REPAIR_PERSON, I.ELECTRIC_REPAIR_PERSON_ID, I.ELECTRIC_REPAIR_PERSON, I.ELECTRIC_REPAIR_TEAM_ID," +
				"I.ELECTRIC_REPAIR_TEAM, I.MECHANICAL_REPAIR_TEAM_ID, I.MECHANICAL_REPAIR_TEAM FROM E_NEW_BUY_EQUIPMENT T, E_EQUIPMENT_PRIMARY_INFO I WHERE T.RECORD_STATUS = 0 AND I.RECORD_STATUS = 0 AND T.EQUIPMENT_IDX = I.IDX";
		StringBuilder sb = new StringBuilder(sql);
		NewBuyEquipmentBean entity = searchEntity.getEntity();
		// 查询条件 - 单位
		if (!StringUtil.isNullOrBlank(entity.getOrgId())) {
			sb.append(" AND I.ORG_ID Like '").append(entity.getOrgId()).append("%'");
		}
		// 查询条件 - 设备名称（编号）
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND (I.EQUIPMENT_CODE Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" OR I.EQUIPMENT_NAME Like '%").append(entity.getEquipmentCode()).append("%'");
			sb.append(" )");
		}
		// 查询条件 - 设备类别名称（编号）
		if (!StringUtil.isNullOrBlank(entity.getClassCode())) {
			sb.append(" AND (I.CLASS_CODE Like '%").append(entity.getClassCode()).append("%'");
			sb.append(" OR I.CLASS_CODE Like '%").append(entity.getClassCode()).append("%'");
			sb.append(" )");
		}
		// 查询条件 - 新购开始日期
		if (null != entity.getStartDate()) {
			sb.append(" AND T.BUY_DATE >= to_date('").append(DateUtil.yyyy_MM_dd.format(entity.getStartDate())).append("', 'yyyy-mm-dd')");
		}
		// 查询条件 - 新购结束日期
		if (null != entity.getEndDate()) {
			sb.append(" AND T.BUY_DATE <= to_date('").append(DateUtil.yyyy_MM_dd.format(entity.getEndDate())).append("', 'yyyy-mm-dd')");
		}
		sb.append(" ORDER BY I.CLASS_NAME ASC, I.BUY_DATE DESC");
		sql = sb.toString();
		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sb.indexOf("FROM"));
		return super.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, NewBuyEquipmentBean.class);
	}

	/**
	 * <li>说明：保存新购设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t NewBuyEquipment联合封装实体
	 * @throws NoSuchFieldException 
	 */
	public NewBuyEquipment save(NewBuyEquipmentBean t) throws NoSuchFieldException {
		// 新增
		NewBuyEquipment entity = null;
		EquipmentPrimaryInfo epi = null;
		if (StringUtil.isNullOrBlank(t.getIdx())) {
			entity = new NewBuyEquipment();
			// 保存设备主要信息台账
			epi = this.saveOrUpdateEpi(t);
			entity.setEquipmentIdx(epi.getIdx());
			// 更新
		} else {
			entity = this.getModelById(t.getIdx());
			epi = this.saveOrUpdateEpi(t);
		}
		// 所属单位
		entity.setAffiliatedOrgid(t.getAffiliatedOrgid());
		entity.setAffiliatedOrgname(t.getAffiliatedOrgname());
		// 经办人
		entity.setResponsiblePerson(t.getResponsiblePerson());
		entity.setResponsiblePersonId(t.getResponsiblePersonId());
		// 批准单位
		entity.setRatifyOrgid(t.getRatifyOrgid());
		entity.setRatifyOrgname(t.getRatifyOrgname());
		// 新购批号
		entity.setBuyBatchNum(t.getBuyBatchNum());
		// 新购日期
		entity.setBuyDate(t.getBuyDate());
		// 备注
		entity.setRemark(t.getRemark());
		this.saveOrUpdate(entity);
		return entity;
	}

	/**
	 * <li>说明：保存设备主要信息台账
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t NewBuyEquipment联合封装实体
	 * @return 设备主要信息实体台账
	 * @throws NoSuchFieldException
	 */
	private EquipmentPrimaryInfo saveOrUpdateEpi(NewBuyEquipmentBean t) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = null;
		if (StringUtil.isNullOrBlank(t.getEquipmentIdx())) {
			epi = new EquipmentPrimaryInfo();
		} else {
			epi = this.equipmentPrimaryInfoManager.getModelById(t.getEquipmentIdx());
		}
		epi.setOrgId(t.getOrgId());
		epi.setOrgName(t.getOrgName());
		epi.setClassCode(t.getClassCode());
		epi.setClassName(t.getClassName());
		epi.setEquipmentCode(t.getEquipmentCode());
		epi.setEquipmentName(t.getEquipmentName());
		// 使用人
		epi.setUsePerson(t.getUsePerson());
		epi.setUsePersonId(t.getUsePersonId());
		// 使用车间
		epi.setUseWorkshop(t.getUseWorkshop());
		epi.setUseWorkshopId(t.getUseWorkshopId());
		// 电气维修信息
		epi.setElectricCoefficient(t.getElectricCoefficient());
		epi.setElectricRepairTeam(t.getElectricRepairTeam());
		epi.setElectricRepairTeamId(t.getElectricRepairTeamId());
		epi.setElectricRepairPerson(t.getElectricRepairPerson());
		epi.setElectricRepairPersonId(t.getElectricRepairPersonId());
		// 电气总功率
		epi.setEletricTotalPower(t.getEletricTotalPower());
		// 机械维修信息
		epi.setMechanicalCoefficient(t.getMechanicalCoefficient());
		epi.setMechanicalRepairTeam(t.getMechanicalRepairTeam());
		epi.setMechanicalRepairTeamId(t.getMechanicalRepairTeamId());
		epi.setMechanicalRepairPerson(t.getMechanicalRepairPerson());
		epi.setMechanicalRepairPersonId(t.getMechanicalRepairPersonId());
		// 规格、型号
		epi.setModel(t.getModel());
		epi.setSpecification(t.getSpecification());
		// 制造信息
		epi.setMakeDate(t.getMakeDate());
		epi.setMakeFactory(t.getMakeFactory());
		epi.setLeaveFactoryNo(t.getLeaveFactoryNo());
		// 设置地点
		epi.setUsePlace(t.getUsePlace());
		// 运行班制
		epi.setRuningShifts(t.getRuningShifts());
		// 固资原值、故障编号
		epi.setFixedAssetValue(t.getFixedAssetValue());
		epi.setFixedAssetNo(t.getFixedAssetNo());
		// 外形尺寸、重量
		epi.setShapeSize(t.getShapeSize());
		epi.setWeight(t.getWeight());
		// 管理类别、管理级别、技术等级、大精
		epi.setManageClass(t.getManageClass());
		epi.setManageLevel(t.getManageLevel());
		epi.setTecLevel(t.getTecLevel());
		epi.setIsExactness(t.getIsExactness());
		// 是否工装、专用、特种
		epi.setIsFrock(t.getIsFrock());
		epi.setIsDedicated(t.getIsDedicated());
		epi.setIsSpecialType(t.getIsSpecialType());
		// 新购日期
		epi.setBuyDate(t.getBuyDate());
		// 使用年月
		epi.setUseDate(t.getUseDate());
		// 是否主设备
		epi.setIsPrimaryDevice(t.getIsPrimaryDevice());
		// 设备动态
		epi.setDynamic(t.getDynamic());
		// 闲置状态
		epi.setXzState(t.getXzState());
		// 出租状态
		epi.setCzState(t.getCzState());
		// 出租状态
		epi.setFcState(t.getFcState());
		String[] validateUpdate = this.equipmentPrimaryInfoManager.validateUpdate(epi);
		if (null != validateUpdate && validateUpdate.length > 0) {
			throw new BusinessException(validateUpdate[0]);
		}
		// 保存设备主要信息
		this.equipmentPrimaryInfoManager.saveOrUpdate(epi);
		return epi;
	}

}
