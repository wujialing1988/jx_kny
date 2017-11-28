package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: TrainWorkPlan业务类, 机车检修作业计划Vis视图查询
 * <li>创建人：何涛
 * <li>创建日期：2015-4-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="vTrainWorkPlanManager")
public class VTrainWorkPlanManager extends JXBaseManager<TrainWorkPlan, TrainWorkPlan> {
    
    /** JobProcessNode业务类, 机车检修计划流程节点Vis视图查询 */
    @Resource
    private VJobProcessNodeManager vJobProcessNodeManager;
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    @Resource
    private  TrainAccessAccountManager trainAccessAccountManager;
    /**
     * <li>说明：分页查询
     * <li>创建人：何涛
     * <li>创建日期：2015-04-21
     * <li>修改人：何涛
     * <li>修改日期：2015-05-08
     * <li>修改内容：修改机车检修计划以“计划开始时间”升序排序
     * @param searchEntity 包装了实体类查询条件的对象
     * @return Page<TrainWorkPlan> 分页查询列表
     * @throws BusinessException
     */
    @Override
    public Page<TrainWorkPlan> findPageList(SearchEntity<TrainWorkPlan> searchEntity) throws BusinessException {
        TrainWorkPlan entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(entity.getWorkPlanStatus())) {
            sb.append(" And workPlanStatus In ('").append(entity.getWorkPlanStatus().replace(",", "','")).append("')");
        }
        // 根据“车型”“车号”“修程”“修次”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo || repairClassName || repairtimeName Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        // 以“计划开始日期”进行升序排序
        sb.append(" Order By planBeginTime ASC");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<TrainWorkPlan> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<TrainWorkPlan> list = page.getList();
        for (TrainWorkPlan plan : list) {
            // 查询机车检修作业计划下属第一层级的作业流程节点实体
            try {
                plan.setJobProcessNodes(vJobProcessNodeManager.getFirstLevelNode(plan.getIdx()));
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
        return page;
    }
    
    /**
     * <li>说明：机车检修作业计划编制（不按工期）查询检修作业节点，按节点顺序，依次显示检修作业节点
     * <li>创建人：何涛
     * <li>创建日期：2016-03-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return Page<TrainWorkPlan> 分页查询列表
     * @throws BusinessException
     */
    public Page<TrainWorkPlan> queryPageList(SearchEntity<TrainWorkPlan> searchEntity) throws BusinessException {
        TrainWorkPlan entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(entity.getWorkPlanStatus())) {
            sb.append(" And workPlanStatus In ('").append(entity.getWorkPlanStatus().replace(",", "','")).append("')");
        }
        // 根据“车型”“车号”“修程”“修次”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo || repairClassName || repairtimeName Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        // 以“计划开始日期”进行升序排序
        sb.append(" Order By planBeginTime ASC");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<TrainWorkPlan> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<TrainWorkPlan> list = page.getList();
        for (TrainWorkPlan plan : list) {
            // 查询机车检修作业计划下属第一层级的作业流程节点实体
            try {
                plan.setJobProcessNodes(vJobProcessNodeManager.getFirstPlanNode(plan.getIdx()));
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
        return page;
    }
    
   
    /**
     * <li>说明：机车检修作业计划编制查询检修作业节点，按节点顺序，依次显示检修作业节点(最新修改)
     * <li>创建人：张迪
     * <li>创建日期：2016-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 包装了实体类查询条件的对象
     * @return 分页查询列表
     * @throws BusinessException
     */
    public Page<TrainWorkPlan> queryWorkPlanList(SearchEntity<TrainWorkPlan> searchEntity) throws BusinessException {
        TrainWorkPlan entity = searchEntity.getEntity();
        StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
        if (!StringUtil.isNullOrBlank(entity.getWorkPlanStatus())) {
            sb.append(" And workPlanStatus In ('").append(entity.getWorkPlanStatus().replace(",", "','")).append("')");
        }
        // 根据“车型”“车号”“修程”“修次”模糊匹配查询
        String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainTypeShortName || trainNo || repairClassName || repairtimeName Like '%").append(trainNo.toUpperCase()).append("%'");
        }
        // 客货类型 10 货车 20 客车 30 柴油发电机组
        if (!StringUtil.isNullOrBlank(entity.getVehicleType())) {
        	sb.append(" And vehicleType = '"+entity.getVehicleType()+"'");
        }
        // 以“计划开始日期”进行升序排序
        sb.append(" Order By planBeginTime DESC");
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        Page<TrainWorkPlan> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
        List<TrainWorkPlan> list = page.getList();    
       
        for (TrainWorkPlan plan : list) {
            // 查询机车检修作业计划下属第一层级的作业流程节点实体
            try {
                plan.setJobProcessNodes(vJobProcessNodeManager.getFirstPlanNodeNew(plan.getIdx()));
                List<Object[]> timeList = jobProcessNodeQueryManager.getMinBeginAndMaxEndRealTime(plan.getIdx()); 
                for (Object[] obj : timeList) {
                    if (obj[0] == null || obj[1] == null)
                        continue;
                    try {
                    	plan.setMinRealTime(DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[0].toString()));
                        plan.setMaxRealTime(DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[1].toString()));
					} catch (Exception e) {
						System.err.println(122);
					}
                    
                }
              
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
        return page;
    }
    
}
