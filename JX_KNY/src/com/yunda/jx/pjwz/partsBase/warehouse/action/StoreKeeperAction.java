package com.yunda.jx.pjwz.partsBase.warehouse.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjwz.partsBase.warehouse.entity.StoreKeeper;
import com.yunda.jx.pjwz.partsBase.warehouse.manager.StoreKeeperManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：StoreKeeper控制器, 库管员
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class StoreKeeperAction extends JXBaseAction<StoreKeeper, StoreKeeper, StoreKeeperManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	/**
	 * /jsp/jx/pjwz/stockbase/StoreKeeper.js
	 * <li>说明：批量添加库管员
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */
	public void saveOrUpdateBatch() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			StoreKeeper[] keepers = (StoreKeeper[])JSONUtil.read(getRequest(), StoreKeeper[].class);
			this.manager.saveOrUpdateBatch(keepers);
			map.put("success", "false");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}