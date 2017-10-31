package com.yunda.jx.jcll.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jcll.entity.TrainRecord;
import com.yunda.jx.jcll.entity.TrainRecordDefinition;
import com.yunda.jx.jcll.entity.TrainRecordInstance;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车履历主要部件实例业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="trainRecordInstanceManager")
public class TrainRecordInstanceManager extends JXBaseManager<TrainRecordInstance, TrainRecordInstance> {
    
    /** 机车履历主要部件定义业务类 */
    @Resource
    private TrainRecordDefinitionManager trainRecordDefinitionManager ;
    
    /**
     * <li>说明：保存机车实例
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-10-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws BusinessException
     * @throws NoSuchFieldException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @SuppressWarnings("unchecked")
    public void generateInstances(TrainRecord record) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
//        if(record != null && !StringUtil.isNullOrBlank(record.getIdx())){
//            List<TrainRecordInstance> inslists = new ArrayList<TrainRecordInstance>();
//            // 通过车型主键查询主要部件定义列表
//            List<TrainRecordDefinition> delLists = trainRecordDefinitionManager.getTrainRecordDefinitionByTrainTypeIdx(record.getTrainTypeIDX());
//            if(delLists != null && delLists.size()>0){
//                for (TrainRecordDefinition definition : delLists) {
//                    TrainRecordInstance instance = new TrainRecordInstance();
//                    BeanUtils.copyProperties(instance, definition);
//                    instance.setIdx("");
//                    instance.setTrainRecordIdx(record.getIdx());
//                    inslists.add(instance);
//                }
//            }
//            if(inslists != null && inslists.size() > 0){
//                this.saveOrUpdate(inslists);
//            }
//        }
    }
    
}
