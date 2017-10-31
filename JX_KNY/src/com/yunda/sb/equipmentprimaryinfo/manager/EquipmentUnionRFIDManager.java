package com.yunda.sb.equipmentprimaryinfo.manager;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentUnionRFID;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentUnionRFIDBean;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentUnionRFID管理器，数据表：设备RFID关联
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "equipmentUnionRFIDManager")
public class EquipmentUnionRFIDManager extends JXBaseManager<EquipmentUnionRFID, EquipmentUnionRFID> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/**
	 * <li>说明：分页查询，查询进行RFID绑定的设备列表
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询条件封装实体
	 * @param isRegistered 是否只显示已登记设备，true：只查询已登记设备，false：查询所有固资设备
	 * @return 设备对象分页集合
	 */
	public Page<EquipmentUnionRFIDBean> queryPageList(SearchEntity<EquipmentUnionRFIDBean> searchEntity, boolean isRegistered) {
		String sql = SqlMapUtil.getSql(String.format("rfid%cequipment_union_rfid:queryPageList", File.separatorChar));
		StringBuilder sb = new StringBuilder(sql);
		EquipmentUnionRFIDBean entity = searchEntity.getEntity();

		// 查询条件 - 设备编码
		if (!StringUtil.isNullOrBlank(entity.getEquipmentCode())) {
			sb.append(" AND (EQUIPMENT_CODE LIKE '%").append(entity.getEquipmentCode()).append("%' OR EQUIPMENT_NAME LIKE '%").append(entity.getEquipmentCode()).append("%')");
		}

		// 只显示已登记设备
		if (isRegistered) {
			sb.append(" AND RFID_CODE IS NOT NULL");
		}

		// 排序
		this.processOrdersInSql(searchEntity, sb);

		sql = sb.toString();

		String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
		return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, EquipmentUnionRFIDBean.class);
	}

	/**
	 * <li>说明：验证扫描到的RFID识别码是否是一个设备编码，换句话说：该RFID是否已经和设备进行了绑定
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月28日
	 * <li>修改人：何东
	 * <li>修改内容：添加是否存在设备绑定记录的判断
	 * <li>修改日期：2016年11月25日
	 * @param equipmentCode 扫描到的RFID识别码，可能是一个设备编码
	 * @return 根据扫描到的RFID识别码查询设备主要信息，如果查到了，则说明该RFID是否已经和设备进行了绑定，否则则没有绑定
	 */
	public EquipmentPrimaryInfo checkIsBind(String equipmentCode) {
		// 如果表E_EQUIPMENT_UNION_RFID中没有该设备的设备编码记录，表示以前绑定的设备已被解除，只是RFID标签未擦除数据，此时表明，该RFID标签可以用，直接返回null
		EquipmentUnionRFID rfid = this.getModelById(equipmentCode);
		if (rfid == null) {
			return null;
		}

		// 当上面的判断不成立，表示此标签已于其他设备进行了关联，然后再次判断已关联设备是否存在，如果存在返回该设备信息，并表示该RFID标签不能被重写，反之则可以重写
		return this.equipmentPrimaryInfoManager.getModelByEquipmentCode(equipmentCode);
	}

}
