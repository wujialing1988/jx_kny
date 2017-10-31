package com.yunda.sb.repair.material.manager;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.sb.repair.material.entity.StuffClass;
import com.yunda.sb.repair.process.entity.RepairWorkOrderStuff;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：StuffClass管理器，数据表：WLGL_STUFF_CLASS
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service
public class StuffClassManager extends JXBaseManager<StuffClass, StuffClass> {

	/*
	 * (non-Javadoc)
	 * @see com.yunda.frame.core.BaseManager#validateUpdate(java.lang.Object)
	 */
	@Override
	public String[] validateUpdate(StuffClass t) {
		String hql = "From StuffClass Where recordStatus = 0 And stuffName = ?";
		StuffClass entity = (StuffClass) this.daoUtils.findSingle(hql, t.getStuffName());
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { String.format("物料：%s已经存在，请勿重新添加！", t.getStuffName()) };
		}
		return super.validateUpdate(t);
	}

	/**
	 * <li>说明：根据设备检修用料初始化数据
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月9日
	 * <li>修改人：何涛
	 * <li>修改内容：如果在填写检修用料时修改了物料的历史单价，则更新物料的单价为最新单价
	 * <li>修改日期：2017年1月19日
	 * @param stuff 设备检修用料实体对象
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
	 */
	public void saveOrUpdateBySBJX(RepairWorkOrderStuff stuff) throws BusinessException, NoSuchFieldException {
		String hql = "From StuffClass Where recordStatus = 0 And stuffName = ?";
		StuffClass entity = (StuffClass) this.daoUtils.findSingle(hql, stuff.getStuffName());
		if (null == entity) {
			entity = new StuffClass(stuff.getStuffName(), stuff.getStuffNamePY(), stuff.getStuffUnit(), stuff.getStuffUnitPrice());
			this.saveOrUpdate(entity);
			return;
		}
		// Modified by hetao on 2017-01-19 如果在填写检修用料时修改了物料的历史单价，则更新物料的单价为最新单价
		if (entity.getStuffUnitPrice() != stuff.getStuffUnitPrice()) {
			entity.setStuffUnitPrice(stuff.getStuffUnitPrice());
			this.saveOrUpdate(entity);
		}
	}
}
