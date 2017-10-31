package com.yunda.sb.pointcheck.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.pointcheck.entity.PointCheck;
import com.yunda.sb.pointcheck.entity.PointCheckContent;
import com.yunda.sb.pointcheck.entity.PointCheckScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheckContent管理器
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "pointCheckContentManager")
public class PointCheckContentManager extends JXBaseManager<PointCheckContent, PointCheckContent> {

	/** PointCheckScope业务类，数据表：SBJX_POINT_CHECK_SCOPE */
	@Resource
	private PointCheckScopeManager pointCheckScopeManager;

	/**
	 * <li>说明：根据设备主键查询点检内容
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param pointCheckIdx 设备点检任务单idx主键
	 * @return 当前设备点检内容
	 */
	@SuppressWarnings("unchecked")
	public List<PointCheckContent> getModelsByPointCheckIdx(String pointCheckIdx) {
		String hql = "from PointCheckContent Where recordStatus = 0 And pointCheckIdx = ?";
		return this.daoUtils.find(hql, pointCheckIdx);
	}

	/**
	 * <li>说明：生成（初始化）设备点检内容
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月18日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentInfo 设备实例对象
	 * @param pointCheck 点检单实例对象
	 * @return 点检内容集合
	 * @throws NoSuchFieldException 
	 */
	public List<PointCheckContent> insert(EquipmentPrimaryInfo equipmentInfo, PointCheck pointCheck) throws NoSuchFieldException {
		if (StringUtil.isNullOrBlank(equipmentInfo.getClassCode())) {
			throw new BusinessException("未查询到设备的类别编码，请先维护设备类别后重试！");
		}
		// Modified by hetao on 2017-02-24
		// 针对同类型不同设备会存在不同点检范围的情况，优先使用设备编号查询点检范围，未找到结果时再根据设备类别编码进行查询
		List<PointCheckScope> scopeList = this.pointCheckScopeManager.getModelsByClassCode(equipmentInfo.getEquipmentCode(), equipmentInfo.getClassCode());
		if (null == scopeList || scopeList.isEmpty()) {
			throw new BusinessException(String.format("%s（%s）未维护点检范围，不能生成点检任务！", equipmentInfo.getEquipmentName(), equipmentInfo.getEquipmentCode()));
		}
		PointCheckContent entity = null;
		List<PointCheckContent> entityList = new ArrayList<PointCheckContent>(scopeList.size());
		for (PointCheckScope scope : scopeList) {
			entity = new PointCheckContent();
			// 设备点检idx主键
			entity.setPointCheckIdx(pointCheck.getIdx());
			// 点检内容
			entity.setCheckContent(scope.getCheckContent());
			// Modified by hetao on 2016-10-21
			// 增加点检内容的唯一性验证，防止因重复的基础数据配置而产生重复的点检内容
			if (entityList.contains(entity)) {
				continue;
			}
			entityList.add(entity);
		}
		this.saveOrUpdate(entityList);
		return entityList;
	}

	/**
	 * <li>说明：改变点检内容状态
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param idx 点检内容主键
	 * @param technologyStateFlag 技术状态标志：良好、不良、待修、修理
	 * @throws NoSuchFieldException
	 */
	public void updateTechnologyStateFlag(String idx, String technologyStateFlag) throws NoSuchFieldException {
		PointCheckContent entity = this.getModelById(idx);
		entity.setTechnologyStateFlag(StringUtil.nvl(technologyStateFlag, null));
		this.saveOrUpdate(entity);
	}

}
