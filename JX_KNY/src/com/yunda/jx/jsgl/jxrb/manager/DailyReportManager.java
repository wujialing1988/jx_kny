package com.yunda.jx.jsgl.jxrb.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jsgl.jxrb.entity.DailyReport;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: DailyReport业务类,生产调度—机车检修日报
 * <li>创建人：何涛
 * <li>创建日期：2016-5-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "dailyReportManager")
public class DailyReportManager extends JXBaseManager<DailyReport, DailyReport> {

    /** TrainWorkPlan业务类,机车检修作业计划 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;
    
    /**
     * <li>说明：保存机车检修日报
     * <li>创建人：何涛
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param report 机车检修日报实体对象
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveDailyReport(DailyReport report) throws BusinessException, NoSuchFieldException {
        DailyReport entity = this.getModelById(report.getIdx());
        entity.zxgl(report.getZxgl())                       // 走行公里
        .jxzt(report.getJxzt())                             // 检修状态
        .hdrq(report.getHdrq()).ldrq(report.getLdrq())      // 回段日期、离段日期
        .dcrq(report.getDcrq()).lcrq(report.getLcrq())      // 到厂日期、离厂日期
        .kgrq(report.getKgrq()).jgrq(report.getJgrq());     // 开工日期、竣工日期
        entity.setBz(report.getBz());                       // 备注
        this.saveOrUpdate(entity);
    }

    /**
     * <li>说明：插入机车检修日报
     * <li>创建人：何涛
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDs 机车检修作业计划idx主键数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void insertDailyReport(String[] workPlanIDs) throws BusinessException, NoSuchFieldException {
        List<DailyReport> entityList = new ArrayList<DailyReport>(workPlanIDs.length);
        DailyReport entity = null;
        for (String workPlanID : workPlanIDs) {
            TrainWorkPlan trainWorkPlan = trainWorkPlanManager.getModelById(workPlanID);
            if (null == trainWorkPlan) {
                throw new BusinessException("数据异常，未查询到对应机车的检修作业计划！");
            }
            entity  = new DailyReport();
            entity.setWorkPlanID(trainWorkPlan.getIdx());               // 检修作业计划ID
            entity.setCxbm(trainWorkPlan.getTrainTypeIDX());            // 车型编码
            entity.setCxjc(trainWorkPlan.getTrainTypeShortName());      // 车型简称
            entity.setCh(trainWorkPlan.getTrainNo());                   // 车号
            entity.setWxdbm(trainWorkPlan.getDelegateDID());            // 委修单位编码
            entity.setWxdmc(trainWorkPlan.getDelegateDName());          // 委修单位名称
            entity.setCxdbm(trainWorkPlan.getDID());                    // 承修段编码
            entity.setCxdmc(trainWorkPlan.getDNAME());                  // 承修段名称
            entity.setRepairClassName(trainWorkPlan.getRepairClassName());  // 修程
            entity.setRepairTimeName(trainWorkPlan.getRepairtimeName());    // 修次
            entityList.add(entity);
        }
        this.saveOrUpdate(entityList);
    }
    
}
