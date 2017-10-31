package com.yunda.jxpz.trainshort.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jxpz.trainshort.entity.TrainShort;


@Service(value="trainShortManager")
public class TrainShortManager extends JXBaseManager<TrainShort, TrainShort> {
   
	/**
	 * <li>说明:根据车型短称查询车型对应表
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2016-07-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param shortName 车型短称
	 */
	public TrainShort getTrainShortByShort(String shortName){
		String hql = " From TrainShort where shortName = ? " ;
    	return (TrainShort)this.daoUtils.findSingle(hql, new Object[]{shortName});
    }
}
