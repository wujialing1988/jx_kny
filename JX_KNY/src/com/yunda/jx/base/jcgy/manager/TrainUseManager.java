package com.yunda.jx.base.jcgy.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.base.jcgy.entity.TrainUse;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainUse业务类,机车用途代码
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainUseManager")
public class TrainUseManager extends JXBaseManager<TrainUse, TrainUse>{
	/** 所有机车用途代码记录集合 */
	private static List<TrainUse> allTrainUse = null;
	
	/**
	 * <li>说明：所有机车用途代码记录集合列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-04-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return List<TrainUse> 所有机车用途代码记录
	 * @throws 抛出异常列表
	 */
	public List<TrainUse> getAll(){
		if (allTrainUse == null) {
			allTrainUse = super.getAll();
		}
		return allTrainUse;
	}
	/**
	 * <li>说明：根据“机车用途代码”返回“机车用途名称”
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String useID 机车用途代码
	 * @return 未找到返回null
	 * @throws 抛出异常列表
	 */
	public String toUseName(String useID){
		List<TrainUse> allList = getAll();
		for (TrainUse use : allList) {
			if(use.getUseID().equals(useID))	return use.getUseName();
		}
		return null;
	}
		
}