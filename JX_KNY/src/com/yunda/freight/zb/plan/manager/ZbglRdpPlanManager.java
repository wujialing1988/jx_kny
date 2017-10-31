package com.yunda.freight.zb.plan.manager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlan;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecord;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.JczlTrainManager;
import com.yunda.jx.jczl.attachmanage.manager.TrainStatusChangeManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检计划业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpPlanManager")
public class ZbglRdpPlanManager extends JXBaseManager<ZbglRdpPlan, ZbglRdpPlan> {
    
    /**
     * 车辆计划业务
     */
    @Resource
    private ZbglRdpPlanRecordManager zbglRdpPlanRecordManager ;
    
    
    /**
     * 列检中断业务
     */
    @Resource
    private ZbglRdpPlanInterruptManager zbglRdpPlanInterruptManager ;
    
    /**
     * 车辆状态业务类
     */
    @Resource
    private TrainStatusChangeManager trainStatusChangeManager;
    
    /**
     * 车辆信息表
     */
    @Resource
    private JczlTrainManager jczlTrainManager ;
    

    /**
     * <li>说明：新建列检计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveZbglRdpPlan(ZbglRdpPlan plan) throws BusinessException, NoSuchFieldException {
        // 根据计检时间和计划开始时间 算出计划结束时间
        setPlanEndTime(plan);
        // 新建或者作业班组发生改变，直接全部重新添加
        boolean flag = false ;
        if(!StringUtil.isNullOrBlank(plan.getIdx())){
            int count = getZbglRdpPlanByTeam(plan.getWorkTeamID(), plan.getIdx());
            if(count == 0){
                flag = true ;
            }
        }else{
            flag = true ;
        }
        this.saveOrUpdate(plan);
        // 生成相应的列检车辆
        zbglRdpPlanRecordManager.generateRecords(plan,flag);
        // 20170620修改，人员默认派工给班组分队
        if(plan.getVehicleType().equals(TrainVehicleType.TYPE_FREIGHT)){
            zbglRdpPlanRecordManager.startRecordsForFreight(plan);
        }
        
    }
    
    /**
     * <li>说明：通过作业班组查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTeamID
     * @param planIdx
     * @return
     */
    public int getZbglRdpPlanByTeam(String workTeamID,String planIdx){
        StringBuffer hql = new StringBuffer(" select count(*) From ZbglRdpPlan where recordStatus = 0 and workTeamID = ? and idx = ? ");
        return this.daoUtils.getCount(hql.toString(), new Object[]{workTeamID,planIdx});
    }

    /**
     * <li>说明：根据计检时间和计划开始时间 算出计划结束时间
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param plan
     */
    private void setPlanEndTime(ZbglRdpPlan plan) {
        if(plan != null && plan.getCheckTime() != null && plan.getPlanStartTime() != null){
            plan.setPlanEndTime(new Date(plan.getPlanStartTime().getTime() + 60 * 1000 * plan.getCheckTime()));
        }
    }

    /**
     * <li>说明：启动计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 列检计划ID
     * @return
     * @throws Exception 
     */
    public String startPlan(String id) throws Exception {
        if(StringUtil.isNullOrBlank(id)){
           return "没有要启动的计划！" ; 
        }
        ZbglRdpPlan plan = this.getModelById(id);
        if(plan == null){
           return "没有要启动的计划！" ; 
        }
        plan.setRdpPlanStatus(ZbglRdpPlan.STATUS_HANDLING);
        plan.setRealStartTime(new Date()); // 实际开始时间
        this.saveOrUpdate(plan);
        if(plan.getVehicleType().equals(TrainVehicleType.TYPE_FREIGHT)){
            // 批量启动车辆计划（货车）
            zbglRdpPlanRecordManager.batchStartRecordsForHC(plan);
        }else{
            // 批量启动车辆计划（客车）
            zbglRdpPlanRecordManager.batchStartRecordsForKC(plan);
        }
        return null;
    }

    /**
     * <li>说明：中断计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id
     * @return
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public String interruptPlan(String id) throws BusinessException, NoSuchFieldException {
        if(StringUtil.isNullOrBlank(id)){
            return "没有要中断的计划！" ; 
         }
         ZbglRdpPlan plan = this.getModelById(id);
         if(plan == null){
            return "没有要中断的计划！" ; 
         }
         plan.setRdpPlanStatus(ZbglRdpPlan.STATUS_INTERRUPT); // 状态设置为中断
         zbglRdpPlanInterruptManager.interruptPlan(id);
         this.saveOrUpdate(plan);
         return null;
    }
    
    /**
     * <li>说明：恢复计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id
     * @return
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public String regainPlan(String id) throws BusinessException, NoSuchFieldException {
        if(StringUtil.isNullOrBlank(id)){
            return "没有要恢复的计划！" ; 
         }
         ZbglRdpPlan plan = this.getModelById(id);
         if(plan == null){
            return "没有要恢复的计划！" ; 
         }
         plan.setRdpPlanStatus(ZbglRdpPlan.STATUS_HANDLING); // 状态设置为启动
         zbglRdpPlanInterruptManager.regainPlan(id);
         this.saveOrUpdate(plan);
         return null;
    }

    /**
     * <li>说明：完成计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public String cmpPlan(String id) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if(StringUtil.isNullOrBlank(id)){
            return "没有要完成的计划！" ; 
         }
         ZbglRdpPlan plan = this.getModelById(id);
         if(plan == null){
            return "没有要完成的计划！" ; 
         }
         plan.setRdpPlanStatus(ZbglRdpPlan.STATUS_HANDLED); // 状态设置为启动
         plan.setRealEndTime(new Date());
         this.saveOrUpdate(plan);
         // 改车辆状态
         List<ZbglRdpPlanRecord> records = zbglRdpPlanRecordManager.getZbglRdpPlanRecordComListByPlan(id);
         for (ZbglRdpPlanRecord record : records) {
             JczlTrain train = jczlTrainManager.getJczlTrainByTypeAndNo(record.getTrainTypeIdx(), record.getTrainNo());
             if(train == null || train.getTrainState() == JczlTrain.TRAIN_STATE_LIEJIAN){
                 // 车辆状态改变
                 trainStatusChangeManager.saveChangeRecords(record.getTrainTypeIdx(), record.getTrainNo(), JczlTrain.TRAIN_STATE_USE, record.getIdx(), "列检计划完成");
             }
         }
         return null;
    }
    
    /**
     * <li>说明：终止计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void stopPlan(Serializable... ids) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        List<ZbglRdpPlan> entityList = new ArrayList<ZbglRdpPlan>();
        for (Serializable id : ids) {
            ZbglRdpPlan t = getModelById(id);
            t = EntityUtil.setSysinfo(t);
//          设置逻辑删除字段状态为已删除
            t = EntityUtil.setDeleted(t);
            entityList.add(t);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }

    public Page<Map<String, Object>> findKCLJLIst(String planIdx) {
        String sql = SqlMapUtil.getSql("kny-base:findKCLJLIst")
        .replace("#planIdx#", planIdx);
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageMap(totalSql, sql, 0, 10000, false);
    }
    
    
}
