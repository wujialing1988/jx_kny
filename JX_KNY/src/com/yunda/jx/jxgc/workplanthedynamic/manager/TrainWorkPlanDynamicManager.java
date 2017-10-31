package com.yunda.jx.jxgc.workplanthedynamic.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.jxgc.workplanthedynamic.entity.TrainWorkPlanDynamic;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修当日动态业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainWorkPlanDynamicManager")
public class TrainWorkPlanDynamicManager  extends JXBaseManager<TrainWorkPlanDynamic, TrainWorkPlanDynamic> {
    /** 机车检修计划流程节点业务类 */
    @Resource
    private ExpandInformationManager expandInformationManager;
    @Resource
    private TrainInPlanManager trainInPlanManager;
    @Resource
    private WorkPlanRepairReportManager workPlanRepairReportManager;
    @Resource
    private WorkPlanWartoutTrainManager workPlanWartoutTrainManager;
    @Resource
    private JobProcessNodeManager jobProcessNodeManager;
    

    /**
     * <li>说明：生成所有数据 （当日检修动态，任务完成情况，入段计划，修峻待离段机车信息）
     * <li>创建人：张迪
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planGenerateDateStr 查询日期
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String insertTheDynamic(String planGenerateDateStr) throws Exception {
        String searchDate = DateUtil.yyyy_MM_dd.format(new Date());
        Long operaterId = SystemContext.getAcOperator().getOperatorid();
//      由于只更新当前数据，下面查询参数无效 
//        if (StringUtil.isNullOrBlank(planGenerateDateStr)) {
//            searchDate = DateUtil.yyyy_MM_dd.format(new Date());
//        }else {
//            searchDate = planGenerateDateStr.substring(0,10);
//        }  
        
        this.deleteTodayDynamic(searchDate);    // 删除当天的数据
        // 明天时间
        String tomorrowDate = DateUtil.yyyy_MM_dd.format((DateUtil.yyyy_MM_dd.parse(searchDate).getTime()+24*60*60*1000)); 
        String sql = SqlMapUtil.getSql("jxgc-rdp:generateTheDynamic")
                                .replace("#searchDate#", searchDate);
        List<Object[]> list =  daoUtils.executeSqlQuery(sql);
        if(null != list && 0< list.size()){          
            for (Object[] objects : list) {
                TrainWorkPlanDynamic dto = new TrainWorkPlanDynamic();
                dto.setPlanGenerateDate(searchDate);
                //  idx
                String idx = objects[0].toString();        
                dto.setIdx(objects[0] == null ? "" : idx);
                String trainWorkPlanIDX = objects[1].toString();  
                dto.setWorkPlanIDX(trainWorkPlanIDX);           
                dto.setTrainTypeIDX(objects[2].toString());
                dto.setTrainTypeShortName(objects[3].toString());
                dto.setTrainNo(objects[4].toString());
                dto.setRepairClassIDX(objects[5] == null ? "":objects[5].toString());
                dto.setRepairClassName(objects[6] == null ? "":objects[6].toString());
                dto.setRepairtimeIDX(objects[7] == null ? "":objects[7].toString());
                dto.setRepairtimeName(objects[8] == null ? "":objects[8].toString());
                dto.setDID(objects[9] == null ? "":objects[9].toString());
                dto.setDNAME(objects[10] == null ? "":objects[10].toString());
              
                dto.setDelegateDID(objects[11] == null ? "":objects[11].toString());
                dto.setDelegateDName(objects[12] == null ? "":objects[12].toString());
                dto.setBeginTime(objects[13] == null ? null:DateUtil.parse(objects[13].toString()));
                dto.setInTime(objects[14] == null ? null:DateUtil.parse(objects[14].toString()));
                dto.setPlanEndTime(objects[15] == null ? null:DateUtil.parse(objects[15].toString()));
                dto.setRemarks(objects[16] == null ? "":objects[16].toString()); 
                dto.setSiteID(objects[17] == null ? "":objects[17].toString()); 
           
                dto.setCreateTime(new Date());
                dto.setUpdateTime(new Date());
                dto.setUpdator(operaterId);
                dto.setCreator(operaterId);
                dto.setRecordStatus(0);
                dto.setSaveStatus(0);
       
                
                //根据检修计划ID查询今日计划
                String nodeName = jobProcessNodeManager.findTodayNodeNameByDate(trainWorkPlanIDX, searchDate);
                dto.setNodeNames(nodeName);
                
    //          根据检修计划ID查询当前工位
                String workStationName = jobProcessNodeManager.findTodayWorkStationNameByDate(trainWorkPlanIDX, searchDate);
                dto.setWorkStationName(workStationName);
    
                
    //          根据检修计划ID查询明日计划
                String tomorrowNodeNames = jobProcessNodeManager.findTodayNodeNameByDate(trainWorkPlanIDX,tomorrowDate);
                dto.setTomorrowNodeNames(tomorrowNodeNames);
                this.insert(dto);
            }
        }
        trainInPlanManager.insertTheTrainInPlan(searchDate);
        workPlanWartoutTrainManager.insertTheWartoutTrain(searchDate);
        workPlanRepairReportManager.insertWorkPlanRepairReport(searchDate);
        expandInformationManager.insertTheExpandInformation(searchDate);
        return null;
    
    }


    /**
     * <li>说明：删除当日检修动态
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 当前时间
     */
    private void deleteTodayDynamic(String searchDate) {
        String sql = "Delete JXGC_Train_Work_Plan_Dynamic Where Plan_Generate_Date ='"+ searchDate+ "'";
        daoUtils.executeSql(sql);       
    }
    
    /**
     * <li>说明：提交所有数据 （当日检修动态，任务完成情况，入段计划，修峻待离段机车信息）
     * <li>创建人：张迪
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void saveTheDynamic() throws Exception {
        String sql = "Update JXGC_Train_Work_Plan_Dynamic set save_status = 1 ";
        daoUtils.executeSql(sql);
        trainInPlanManager.updateTheTrainInPlan();
        workPlanWartoutTrainManager.updateTheWartoutTrain();
        workPlanRepairReportManager.updateWorkPlanRepairReport();
        expandInformationManager.updateTheExpandInformation();
    }
}
