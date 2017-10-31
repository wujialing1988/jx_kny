package com.yunda.sb.equipmentprimaryinfo.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.MixedUtils;
import com.yunda.sb.base.constant.BizConstant;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备主要信息业务类
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "equipmentPrimaryInfoManager")
public class EquipmentPrimaryInfoManager extends JXBaseManager<EquipmentPrimaryInfo, EquipmentPrimaryInfo> {

	/**
	 * <li>说明：数据唯一性验证
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月2日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 要存储的设备主要信息实体对象
	 * @return 验证信息
	 */
	@Override
	public String[] validateUpdate(EquipmentPrimaryInfo t) {
		String hql = "From EquipmentPrimaryInfo Where recordStatus = 0 And equipmentCode = ? And orgId = ?";
		if (StringUtil.isNullOrBlank(t.getFixedAssetNo())) {
			hql += " And (fixedAssetNo Is Null Or LENGTH(fixedAssetNo) <= 0)";
		} else {
			hql += " And (fixedAssetNo Is Not Null Or LENGTH(fixedAssetNo) > 0)";
		}
		EquipmentPrimaryInfo entity = (EquipmentPrimaryInfo) this.daoUtils.findSingle(hql, t.getEquipmentCode(), t.getOrgId());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { "设备编号：" + t.getEquipmentCode() + "已经存在，请重新输入！" };
		}
		return null;
	}

	/**
	 * <li>说明： 获取最大的设备编号
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-2
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@param classCode 设备类别编码
	 *@return 同类别下最大的设备编号
	 */
	public String findMaxEquipmentCode(String classCode) {
		String hql = "select max(equipmentCode) from EquipmentPrimaryInfo where classCode = ? AND recordStatus = 0";
		return (String) MixedUtils.maxCode((String) this.daoUtils.findSingle(hql, classCode), classCode);
	}

	/**
	 * <li>说明：根据设备编码查询具体设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月2日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备编码
	 * @return 设备实例对象
	 */
	public EquipmentPrimaryInfo getModelByEquipmentCode(String equipmentCode) {
		String hql = "From EquipmentPrimaryInfo where recordStatus = 0 And fixedAssetValue >= ? AND equipmentCode = ?";
		return (EquipmentPrimaryInfo) this.daoUtils.findSingle(hql, new Object[] { BizConstant.FIXED_ASSET_VALUE, equipmentCode });
	}

	/**
	 * <li>说明：根据设备名称查询具体设备，因为数据库中可能存在多个相同名称的设备信息，所以该方法返回的对象可能是不确定的
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月2日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentName 设备名称
	 * @return 设备实例对象
	 */
	public EquipmentPrimaryInfo getModelByEquipmentName(String equipmentName) {
		String hql = "From EquipmentPrimaryInfo where recordStatus = 0 AND equipmentName = ? Order By classCode Desc";
		return (EquipmentPrimaryInfo) this.daoUtils.findSingle(hql, new Object[] { equipmentName });
	}

}
