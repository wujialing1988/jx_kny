package com.yunda.passenger.traindemand.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.scdd.repairplan.manager.RepairWarningKCManager;
import com.yunda.passenger.marshalling.entity.MarshallingTrain;
import com.yunda.passenger.marshalling.manager.MarshallingTrainManager;
import com.yunda.passenger.traindemand.entity.TrainDemand;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列表需求维护业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Service("trainDemandManager")
public class TrainDemandManager extends JXBaseManager<TrainDemand, TrainDemand> implements IbaseCombo {
	 /** CodeRuleConfig业务类,业务编码规则配置 */
    @Resource
    private MarshallingTrainManager marshallingTrainManager;
    @Resource
    private MarshallingTrainDemandManager marshallingTrainDemandManager;
	@Resource
	private TrainInspectorDemandManager trainInspectorDemandManager;
	
	@Resource
	private RepairWarningKCManager repairWarningKCManager ;
	/**
	 * <li>说明：通过idx查询实体
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<TrainDemand> getModelByIdx(String idx) throws BusinessException {
		String sql = " SELECT * from K_TRAIN_DEMAND where RECORD_STATUS = 0 and IDX ='"+ idx +"'";
		return daoUtils.executeSqlQueryEntity(sql, TrainDemand.class);
	}
	/**
	 * <li>说明：保存时添加业务编码规则
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 编组基础信息实体类
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	public void saveOrUpdateDemand(TrainDemand entity) throws Exception, InvocationTargetException {
		List<MarshallingTrain>  trainList= marshallingTrainManager.findTrainListByCode(entity.getMarshallingCode());
		int trainCount =  CommonUtil.getListSize(trainList);
		entity.setTrainCount(trainCount);
		// 新增
	    if(StringUtil.isNullOrBlank(entity.getIdx())){
			this.saveOrUpdate(entity);
			daoUtils.flush();
			marshallingTrainDemandManager.updateTrainListByDemandIDX(entity.getIdx(), trainList, true);
			trainInspectorDemandManager.updateInspectorListByDemand(entity, true);
			repairWarningKCManager.updateKm(trainList, entity.getKilometers());
	    }else{  // 编辑
	    	TrainDemand  oldEntity = this.getModelById(entity.getIdx());
	    	if(null != oldEntity && !oldEntity.getMarshallingCode().equals(entity.getMarshallingCode())){
	    		marshallingTrainDemandManager.updateTrainListByDemandIDX(entity.getIdx(), trainList, false);
	    		trainInspectorDemandManager.updateInspectorListByDemand(entity,false);
	    	}
	    	BeanUtils.copyProperties(oldEntity, entity);
	    	this.saveOrUpdate(oldEntity);
	    }
	 }
    
    /**
     * <li>说明：通过车型车号获取客车编组任务
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型
     * @param trainNo 车号
     * @return
     */
    public TrainDemand findTrainDemandByTrain(String trainTypeIDX,String trainNo){
        StringBuffer hql = new StringBuffer("select t From TrainDemand t,MarshallingTrainDemand d where t.recordStatus = 0 and d.recordStatus = 0 and d.trainDemandIdx = t.idx ");
        hql.append(" and d.trainTypeIDX = ? and d.trainNo = ? order by t.runningDate desc ");
        return (TrainDemand)this.daoUtils.findSingle(hql.toString(), new Object[]{trainTypeIDX,trainNo});
    }
    
	/**
	 * <li>说明：编组下拉控件
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param req 
	 * @param start
	 * @param limit
	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
//	    String queryParams = req.getParameter("queryParams");
//	    Map queryParamsMap = new HashMap();
//        if (!StringUtil.isNullOrBlank(queryParams)) {
//            queryParamsMap = JSONUtil.read(queryParams, Map.class);
//        }
//        StringBuffer hql = new StringBuffer(" select t from Marshalling t where t.recordStatus = 0") ;
//        // 查询参数
//        String routingIdx = String.valueOf(queryParamsMap.get("str"));
//	    return null;
//	}
}
