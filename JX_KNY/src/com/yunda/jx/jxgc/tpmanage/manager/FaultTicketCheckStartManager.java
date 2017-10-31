package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckStart;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 提票检查签到开始检查记录业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckStartManager")
public class FaultTicketCheckStartManager extends JXBaseManager<FaultTicketCheckStart, FaultTicketCheckStart> {
    
    
    /** 员工服务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /** 机车检修计划服务类 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;
    
    /**
     * <li>说明：获取开始检查记录实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 检修任务单
     * @param faultTicketType 提票类型
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public FaultTicketCheckStart getFaultTicketCheckStart(String workPlanIDX,String faultTicketType) throws BusinessException{
        // 如果参数为空，则返回空
        if(StringUtil.isNullOrBlank(workPlanIDX) || StringUtil.isNullOrBlank(faultTicketType)){
            return null ;
        }
        StringBuffer hql = new StringBuffer();
        hql.append(" From FaultTicketCheckStart s where recordStatus = 0 and workPlanIDX = ? and faultTicketType = ?");
        return (FaultTicketCheckStart)this.daoUtils.findSingle(hql.toString(), new Object[]{workPlanIDX,faultTicketType});
    }

    /**
     * <li>说明：保存开始检查记录
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param checkSign 记录实体
     * @throws NoSuchFieldException 
     */
    public void saveCheckStart(FaultTicketCheckStart checkStart) throws BusinessException, NoSuchFieldException {
        if(checkStart == null){
            throw new BusinessException("检查签到数据为空");
        }
        FaultTicketCheckStart start = this.getFaultTicketCheckStart(checkStart.getWorkPlanIDX(), checkStart.getFaultTicketType());
        if(null != start){
            throw new BusinessException("该过程已经确认");
        }
        TrainWorkPlan plan = trainWorkPlanManager.getModelById(checkStart.getWorkPlanIDX());
        start = new FaultTicketCheckStart();
        // 机车信息
        if(plan != null){
            start.setTrainTypeIDX(plan.getTrainTypeIDX());
            start.setTrainNo(plan.getTrainNo());
            start.setTrainTypeShortName(plan.getTrainTypeShortName());
        }
        start.setCheckSignEmpId(checkStart.getCheckSignEmpId());
        OmEmployee emp = omEmployeeManager.findOmEmployee(checkStart.getCheckSignEmpId());
        if(emp != null){
            start.setCheckSignEmp(emp.getEmpname());
        }
        start.setWorkPlanIDX(checkStart.getWorkPlanIDX());
        start.setFaultTicketType(checkStart.getFaultTicketType());
        start.setCheckSignTimeStart(new Date());
        // 保存
        this.saveOrUpdate(start); 
    }
}
