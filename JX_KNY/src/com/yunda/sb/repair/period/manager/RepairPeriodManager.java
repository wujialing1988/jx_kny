package com.yunda.sb.repair.period.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.classfication.entity.Classification;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.repair.period.entity.RepairPeriod;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：RepairPeriod管理器
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "repairPeriodManager")
public class RepairPeriodManager extends JXBaseManager<RepairPeriod, RepairPeriod> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	@Override
	public String[] validateUpdate(RepairPeriod t) {
		// 验证设备类别是否存在
		String hql = "From Classification Where recordStatus = ? And classCode = ? And className = ?";
		Object o = this.daoUtils.findSingle(hql, Constants.NO_DELETE, t.getClassCode(), t.getClassName());
		if (null == o) {
			return new String[] { String.format("设备类别：%s(编号：%s)不存在！", t.getClassName(), t.getClassCode()) };
		}
		// 验证数据唯一性
		RepairPeriod entity = this.getModelByClassCode(t.getClassCode());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { String.format("设备类别：%s(编号：%s)已经存在，不允许重复添加！", t.getClassName(), t.getClassCode()) };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：根据设备类别编码获取设备检修周期实体对象
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @return 设备检修周期实体对象
	 */
	private RepairPeriod getModelByClassCode(String classCode) {
		String hql = "From RepairPeriod Where recordStatus = ? And classCode = ?";
		return (RepairPeriod) this.daoUtils.findSingle(hql, Constants.NO_DELETE, classCode);
	}

	/**
	 * <li>说明：通过向上递归的方式获取设备的维修周期（递归）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @return 设备检修周期实体对象
	 */
	private RepairPeriod getModelByIterator(String classCode) {
		// 根据设备类别编码获取设备检修周期实体对象
		RepairPeriod entity = this.getModelByClassCode(classCode);
		if (null != entity) {
			return entity;
		}
		// 获取上级设备类别实体对象
		String hql = "From Classification Where recordStatus = 0 And idx = (Select parentIdx From Classification Where recordStatus = 0 And classCode = ?)";
		Classification parentClassification = (Classification) this.daoUtils.findSingle(hql, classCode);
		if (null == parentClassification) {
			return null;
		}
		// 递归
		return this.getModelByIterator(parentClassification.getClassCode());
	}

	/**
	 * <li>说明：通过设备idx主键获取设备维修周期实体对象
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月21日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @return 设备检修周期实体对象
	 */
	public RepairPeriod getModelByEquipmentIdx(String equipmentIdx) {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(equipmentIdx);
		String classCode = epi.getClassCode();
		if (StringUtil.isNullOrBlank(classCode)) {
			return null;
		}
		return this.getModelByIterator(epi.getClassCode());
	}

}
