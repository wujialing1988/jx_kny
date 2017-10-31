package com.yunda.sb.repair.process.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.sb.repair.material.manager.StuffClassManager;
import com.yunda.sb.repair.process.entity.RepairWorkOrderStuff;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairWorkOrderStuff管理器，数据表：E_REPAIR_WORK_ORDER_STUFF
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Service
public class RepairWorkOrderStuffManager extends JXBaseManager<RepairWorkOrderStuff, RepairWorkOrderStuff> {

	/** StuffClass管理器，数据表：WLGL_STUFF_CLASS */
	@Resource
	private StuffClassManager stuffClassManager;

	/* (non-Javadoc)
	 * @see com.yunda.frame.core.BaseManager#validateUpdate(java.lang.Object)
	 */
	@Override
	public String[] validateUpdate(RepairWorkOrderStuff t) {
		String hql = "From RepairWorkOrderStuff Where recordStatus = 0 And stuffName = ? And repairWorkOrderIdx = ?";
		RepairWorkOrderStuff entity = (RepairWorkOrderStuff) this.daoUtils.findSingle(hql, t.getStuffName(), t.getRepairWorkOrderIdx());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { String.format("物料：%s已经存在，请勿重新添加！", t.getStuffName()) };
		}
		return super.validateUpdate(t);
	}

	/* (non-Javadoc)
	 * @see com.yunda.frame.core.BaseManager#updateAfter(java.lang.Object, boolean)
	 */
	@Override
	protected void updateAfter(RepairWorkOrderStuff t, boolean isNew) {
		try {
			this.stuffClassManager.saveOrUpdateBySBJX(t);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <li>说明：根据设备检修工单idx主键获取设备检修用料实体对象集合
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月20日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairWorkOrderIdx 设备检修工单idx主键
	 * @return 设备检修用料实体对象集合
	 */
	@SuppressWarnings("unchecked")
	public List<RepairWorkOrderStuff> getModelsByRepairWorkOrderIdx(String repairWorkOrderIdx) {
		String hql = "From RepairWorkOrderStuff Where recordStatus = 0 And repairWorkOrderIdx = ?";
		return this.daoUtils.find(hql, repairWorkOrderIdx);
	}

}
