package com.yunda.jx.jczl.attachmanage.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.entity.TrainStatusChange;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车辆状态流转业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-18 15:23:13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings({"unchecked"})
@Service("trainStatusChangeManager")
public class TrainStatusChangeManager extends JXBaseManager<TrainStatusChange, TrainStatusChange> {
    
    /**
     * 车辆信息表
     */
    @Resource
    private JczlTrainManager jczlTrainManager ;
    
    /**
     * <li>说明：保存车辆状态历史（需要改变状态的时候直接调用）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX
     * @param trainNo
     * @param trainState
     * @param businessIdx
     * @param businessName
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveChangeRecords(String trainTypeIDX,String trainNo,Integer trainState,String businessIdx,String businessName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException{
        JczlTrain train = jczlTrainManager.saveOrUpdateNewTrainNew(trainTypeIDX, trainNo);
        if(train != null){
            TrainStatusChange change = new TrainStatusChange();
            buildTrainToChange(train,change);
            change.setBusinessIdx(businessIdx);
            change.setBusinessName(businessName);
            change.setTrainState(trainState);
            train.setTrainState(trainState);
            jczlTrainManager.update(train);
            this.save(change);
        }
    }

    /**
     * <li>说明：改变历史赋值
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param train
     * @param change
     */
    private void buildTrainToChange(JczlTrain train, TrainStatusChange change) {
        change.setTrainIdx(train.getIdx());
        change.setTrainTypeIdx(train.getTrainTypeIDX());
        change.setTrainTypeShortname(train.getTrainTypeShortName());
        change.setTrainNo(train.getTrainNo());
        change.setVehicleType(train.getVehicleType());
        change.setRecordTime(new Date());
    }
    
    /**
     * <li>说明：状态回滚
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX
     * @param trainNo
     * @param businessIdx
     * @param businessName
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NoSuchFieldException 
     */
    public void robackChangeRecords(String trainTypeIDX,String trainNo,String businessIdx,String businessName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException{
        TrainStatusChange change = this.getLastTrainStatusChange(trainTypeIDX,trainNo);
        if(change != null){
            this.saveChangeRecords(trainTypeIDX, trainNo, change.getTrainState(), businessIdx, businessName);
        }else{
            // 如果返回空，说明是新加数据，状态为运用
            this.saveChangeRecords(trainTypeIDX, trainNo, JczlTrain.TRAIN_STATE_USE, businessIdx, businessName);
        }
    }

    /**
     * <li>说明：获取最近一次的状态数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX
     * @param trainNo
     * @return
     */
    private TrainStatusChange getLastTrainStatusChange(String trainTypeIDX, String trainNo) {
        StringBuffer hql = new StringBuffer(" From TrainStatusChange where trainTypeIdx = ? and trainNo = ? order by recordTime desc ");
        List<TrainStatusChange> list = this.daoUtils.find(hql.toString(), new Object[]{trainTypeIDX,trainNo});
        if(list != null && list.size() >= 2){
            return list.get(1);
        }
        return null ;
    }
    
    /**
     * <li>说明：验证各种操作
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX  车型
     * @param trainNo       车号
     * @param trainStates   不能操作的状态组    
     * @param operationName 当前操作名称
     * @return
     */
    public String verificationOperation(String trainTypeIDX, String trainNo,Integer[] trainStates,String operationName){
        String msg = "" ;
        JczlTrain train = jczlTrainManager.getJczlTrainByTypeAndNo(trainTypeIDX, trainNo);
        if(train != null){
            for (Integer trainState : trainStates) {
                if(trainState.intValue() == train.getTrainState().intValue()){
                    msg = "该车辆状态为【"+ JczlTrain.getTrainStatusName(train.getTrainState()) + "】，不能进行"+operationName+"操作！";
                    break ;
                }
            }
        }
        return msg ;
    }
    
    
}
