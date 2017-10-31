package com.yunda.passenger.traindemand.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;
import com.yunda.passenger.traindemand.entity.MarshallingTrainDemand;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列表需求车辆维护业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("marshallingTrainDemandManager")
public class MarshallingTrainDemandManager extends JXBaseManager<MarshallingTrainDemand, MarshallingTrainDemand>{

	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param trainDemandIdx
	 * @param trainList
	 * @param isAddOrEdit 是新增还是编辑 true 新增， false 编辑
	 */
	public void updateTrainListByDemandIDX(String trainDemandIdx, List<MarshallingTrain> trainList, boolean isAddOrEdit) throws Exception, InvocationTargetException {
		  if(!isAddOrEdit){
			  String sql = "DELETE  K_MARSHALLING_TRAIN_LOG WHERE RECORD_STATUS = 0 AND TRAIN_DEMAND_IDX = '" + trainDemandIdx + "'";
		      daoUtils.executeSql(sql);
		  }
		  if(null != trainList && trainList.size()>0){
			// 编组车辆日志
			  List<MarshallingTrainDemand>  marshallingTrainDemandList = new ArrayList<MarshallingTrainDemand>();
			  for(MarshallingTrain train : trainList){
					MarshallingTrainDemand marshallingTrainDemand = new MarshallingTrainDemand();
					BeanUtils.copyProperties(marshallingTrainDemand, train);
					marshallingTrainDemand.setIdx(null);
					marshallingTrainDemand.setTrainDemandIdx(trainDemandIdx);
					marshallingTrainDemand.setMarshallingTrainIdx(train.getIdx());
					marshallingTrainDemandList.add(marshallingTrainDemand);
			}
			this.saveOrUpdate(marshallingTrainDemandList);
		}
	}
    
    /**
     * <li>说明：通过列检任务获取车辆记录列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainDemandIdx
     * @return
     */
    public List<MarshallingTrainDemand> getMarshallingTrainDemandListByDemand(String trainDemandIdx){
        StringBuffer hql = new StringBuffer(" From MarshallingTrainDemand where recordStatus = 0 and trainDemandIdx = ? order by seqNo desc");
        return this.daoUtils.find(hql.toString(), new Object[]{trainDemandIdx});
    }
}
