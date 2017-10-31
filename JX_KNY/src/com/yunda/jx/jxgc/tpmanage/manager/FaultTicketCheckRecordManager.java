package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRecord;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRule;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车提票验证记录
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckRecordManager")
public class FaultTicketCheckRecordManager  extends JXBaseManager<FaultTicketCheckRecord, FaultTicketCheckRecord>  {
    
    
    /* 提票服务 */
    @Resource
    private FaultTicketManager faultTicketManager ;
    
    /* 提票确认规则服务 */
    @Resource
    private FaultTicketCheckRuleManager faultTicketCheckRuleManager ;
    
    /* 车检修作业计划查询业务类 */
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager ;
    
    /* 机车服务 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager ;
    
    /**
     * <li>说明：查询验收提票列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX
     * @return
     */
    public List<FaultTicket> queryFaultTicketCheck(String workPlanIDX) {
        FaultTicket bean = new FaultTicket();
        bean.setWorkPlanIDX(workPlanIDX);
        List<FaultTicketCheckRule> rules = faultTicketCheckRuleManager.findFaultTicketCheckRuleByParams(null,1);
        if(rules == null || rules.size() == 0){
            return null ;
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (FaultTicketCheckRule rule : rules) {
            map.put(rule.getFaultTicketType(), rule.getIsAffirm());
        }
        List<FaultTicket> lists = faultTicketManager.queryFaultTicketListForCheck(bean);
        for (FaultTicket ticket : lists) {
            ticket.setIsAffirm(map.get(ticket.getType())+"");
        }
        return lists;
    }
    
    /**
     * <li>说明：机车提票验收
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划ID
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveCheckNew(String workPlanIDX) throws BusinessException, NoSuchFieldException {
        List<FaultTicket> tickets = this.queryFaultTicketCheck(workPlanIDX);
        for (FaultTicket ticket : tickets) {
            if("1".equals(ticket.getIsAffirm()) && (null == ticket.getStatusAffirm() || ticket.getStatusAffirm() != 1)){
                 throw new BusinessException("类型【"+ticket.getType()+"】下的票需要全部确认！");
            }
            
            if(!"1".equals(ticket.getIsAffirm()) && (null == ticket.getStatus() || ticket.getStatus() != 30)){
                throw new BusinessException("类型【"+ticket.getType()+"】下的票需要全部处理！");
           }
        }
        FaultTicketCheckRecord record = this.getFaultTicketCheckRecord(workPlanIDX);
        if(record != null){
            throw new BusinessException("该机车已经验收，不能再验收！");
        }
        AcOperator ap = SystemContext.getAcOperator();
        Date checkTime = new Date();
        record = new FaultTicketCheckRecord();
        record.setWorkPlanIDX(workPlanIDX);
        record.setCheckEmpId(ap.getOperatorid());
        record.setCheckEmp(ap.getOperatorname());
        record.setCheckTime(checkTime);
        this.saveOrUpdate(record);
        
        // 更改机车状态为完成
        TrainWorkPlan workPlan = trainWorkPlanManager.getModelById(workPlanIDX);
        workPlan.setWorkPlanStatus(TrainWorkPlan.STATUS_HANDLED);
        trainWorkPlanManager.update(workPlan);
        
    }
    
    /**
     * <li>说明：获取验收记录
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIdx 作业计划ID
     * @return
     */
    public FaultTicketCheckRecord getFaultTicketCheckRecord(String workPlanIdx){
        StringBuffer sb = new StringBuffer(" from FaultTicketCheckRecord where recordStatus = 0");
        sb.append(" and workPlanIDX = ?");
        return (FaultTicketCheckRecord)this.daoUtils.findSingle(sb.toString(),new Object[]{workPlanIdx});
    }
    
    
    /**
     * <li>说明：判断是否可进行验收
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX
     * @return
     */
    public boolean isCanCheck(String workPlanIDX){
        boolean flag = true ;
        List<FaultTicket> tickets = this.queryFaultTicketCheck(workPlanIDX);
        if(tickets == null || tickets.size() == 0){
            flag = false ;
        }
        for (FaultTicket ticket : tickets) {
            if("1".equals(ticket.getIsAffirm()) && (null == ticket.getStatusAffirm() || ticket.getStatusAffirm() != 1)){
                 flag = false ;
                 break ;
            }
            if(!"1".equals(ticket.getIsAffirm()) && (null == ticket.getStatus() || ticket.getStatus() != 30)){
                flag = false ;
                break ;
           }
        }
        return flag ;
    }
    
    /**
     * <li>说明：获取可验收数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public Integer getCanCheckToatl(){
        Integer result = 0 ;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return 0 ;
        }
        for (TrainWorkPlan workPlan : workPlanList) {
            // 获取提票验收记录，如果已经验收，则不显示了
            FaultTicketCheckRecord record = this.getFaultTicketCheckRecord(workPlan.getIdx());
            if(record == null && this.isCanCheck(workPlan.getIdx())){
                result++ ;
            }
        }
        return result ;
    }
    
}
