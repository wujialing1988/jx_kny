package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckSign;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 提票检查签到业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckSignManager")
public class FaultTicketCheckSignManager extends JXBaseManager<FaultTicketCheckSign, FaultTicketCheckSign> {
    
    /** 员工服务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /** 机构 */
    @Resource
    private OmOrganizationManager omOrganizationManager;
    
    /** 机车检修计划服务类 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;
    
    /**
     * <li>说明：查询提票检查签到信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @param faultTicketType 提票类型
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicketCheckSign> queryCheckSignList(String workPlanIDX,String faultTicketType) throws BusinessException{
        // 如果参数为空，则返回空
        if(StringUtil.isNullOrBlank(workPlanIDX) || StringUtil.isNullOrBlank(faultTicketType)){
            return null ;
        }
        StringBuffer hql = new StringBuffer();
        hql.append(" From FaultTicketCheckSign s where recordStatus = 0 and workPlanIDX = ? and faultTicketType = ?");
        hql.append(" order by checkSignTime desc ");
        return this.daoUtils.find(hql.toString(), new Object[]{workPlanIDX,faultTicketType});
    }

    /**
     * <li>说明：保存提票检查签到信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param checkSign
     * @throws NoSuchFieldException 
     */
    public void saveCheckSign(FaultTicketCheckSign checkSign) throws BusinessException, NoSuchFieldException {
        if(checkSign == null){
            throw new BusinessException("检查签到数据为空");
        }
        // 取人员，并通过人员查询到部门
        Long checkSignEmpId = checkSign.getCheckSignEmpId();
        if(checkSignEmpId == null){
            throw new BusinessException("检查签到人员为空");
        }
        OmEmployee emp = omEmployeeManager.findOmEmployee(checkSignEmpId);
        if(null == emp){
            throw new BusinessException("系统未找到相应人员");
        }
        // 验证是否已经签到过，如果已经签到，提示已经提示
        FaultTicketCheckSign sign = this.getFaultTicketCheckSign(checkSign.getWorkPlanIDX(), checkSign.getFaultTicketType(), checkSignEmpId);
        if(null != sign){
            throw new BusinessException("该用户已签到");   
        }
        TrainWorkPlan plan = trainWorkPlanManager.getModelById(checkSign.getWorkPlanIDX());
        sign = new FaultTicketCheckSign();
        // 机车信息
        if(plan != null){
            sign.setTrainTypeIDX(plan.getTrainTypeIDX());
            sign.setTrainNo(plan.getTrainNo());
            sign.setTrainTypeShortName(plan.getTrainTypeShortName());
        }
        sign.setCheckSignEmpId(checkSignEmpId);
        sign.setWorkPlanIDX(checkSign.getWorkPlanIDX());
        sign.setFaultTicketType(checkSign.getFaultTicketType());
        sign.setCheckSignEmp(emp.getEmpname());                // 签到人姓名
        OmOrganization org = omOrganizationManager.findByEmpId(emp.getEmpid());
        if(null != org){
            sign.setCheckSignOrgID(org.getOrgid());
            sign.setCheckSignOrgName(org.getOrgname());
        }
        sign.setCheckSignTime(new Date());
        // 保存
        this.saveOrUpdate(sign); 
    }
    
    /**
     * <li>说明：查询签到信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @param faultTicketType 提票类型
     * @param empId 人员
     * @return
     */
    public FaultTicketCheckSign getFaultTicketCheckSign(String workPlanIDX,String faultTicketType,Long empId){
        StringBuffer hql = new StringBuffer(" From FaultTicketCheckSign s where recordStatus = 0 and workPlanIDX = ? and faultTicketType = ? and checkSignEmpId = ? ");
        return (FaultTicketCheckSign)this.daoUtils.findSingle(hql.toString(), new Object[]{workPlanIDX,faultTicketType,empId});
    }
    
}
