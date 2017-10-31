package com.yunda.freight.zb.inspectrecord.manager;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.zb.inspectrecord.entity.TrainInspectRecord;


/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：货车客车巡检记录录入业务处理
 * <li>创建人：黄杨
 * <li>创建日期：2017-6-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service("trainInspectRecordManager")
public class TrainInspectRecordManager extends JXBaseManager<TrainInspectRecord, TrainInspectRecord>{
	
	/**
	 * <li>说明： 重写保存方法，增加录入人，录入时间保存
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-20
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	@Override
	public void saveOrUpdate(TrainInspectRecord t) throws BusinessException, NoSuchFieldException {
		t.setRecordTime(new Date());
		t.setRecordPersonId(SystemContext.getOmEmployee().getEmpid().toString());
		t.setRecordPerson(SystemContext.getOmEmployee().getEmpname());
		super.saveOrUpdate(t);
	}

}
