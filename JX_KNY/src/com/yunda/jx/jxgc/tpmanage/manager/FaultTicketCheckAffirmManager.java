package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.AcRoleManager;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckAffirm;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRole;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRule;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsNewVo;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsTotalVo;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsVo;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 提票检查确认
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckAffirmManager")
public class FaultTicketCheckAffirmManager extends JXBaseManager<FaultTicketCheckAffirm, FaultTicketCheckAffirm> {
    
    // 提票确认类型
    private static String JCZL_FAULT_TYPE_AFFIRM = "JCZL_FAULT_TYPE_AFFIRM";
    
    /* 提票服务 */
    @Resource
    private FaultTicketManager faultTicketManager ;
    
    /* 数据字典选择服务 */
    @Resource
    private EosDictEntrySelectManager eosDictEntrySelectManager ;
    
    /* 角色服务 */
    @Resource
    private AcRoleManager acRoleManager ;
    
    /* 提票确认规则服务 */
    @Resource
    private FaultTicketCheckRuleManager faultTicketCheckRuleManager ;
    
    /* 提票确认角色服务 */
    @Resource
    private FaultTicketCheckRoleManager faultTicketCheckRoleManager ;
    
    
    
    /**
     * <li>说明：查询提票确认统计列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 检修任务单ID
     * @param type 1代表确认查询 2代表验收查询
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicketStatisticsVo> queryCheckStatisticsList(String workPlanIDX,String type) {
        if(StringUtil.isNullOrBlank(workPlanIDX) || StringUtil.isNullOrBlank(type)){
            return null ;
        }
        String sql = SqlMapUtil.getSql("jxgc-tp:queryCheckStatisticsList");
        if("1".equals(type)){
            sql = sql.replace("#where1#", "a.filter1");
        }else if("2".equals(type)){
            sql = sql.replace("#where1#", "a.filter2");
        }
        sql = sql.replace("#workPlanIDX#", workPlanIDX);
        return this.daoUtils.executeSqlQueryEntity(sql, FaultTicketStatisticsVo.class);
    }

    /**
     * <li>说明：确认
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划ID
     * @param faultTicketType 提票类型
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveAffirm(String workPlanIDX, String faultTicketType) throws BusinessException, NoSuchFieldException {
        FaultTicket bean = new FaultTicket();
        bean.setWorkPlanIDX(workPlanIDX);
        bean.setType(faultTicketType);
        bean.setStatus(2);
        List<FaultTicket> entityList = faultTicketManager.queryFaultTicketList(bean);
        AcOperator ap = SystemContext.getAcOperator();
        Date affirmTime = new Date();
        for (FaultTicket entity : entityList) {
            // 保存数据状态为【已确认】
            entity.setStatusAffirm(FaultTicket.AFFIRM_STATUS_DONE);
            this.daoUtils.saveOrUpdate(entity);
            // 保存确认记录
            FaultTicketCheckAffirm affirm = this.getFaultTicketCheckAffirmByFault(entity.getIdx(),ap.getOperatorid(),1);
            if(affirm == null){
                affirm = new FaultTicketCheckAffirm();
                affirm.setWorkPlanIDX(workPlanIDX);
                affirm.setFaultTicketIDX(entity.getIdx());
                affirm.setFaultTicketType(faultTicketType);
                affirm.setAffirmEmpId(ap.getOperatorid());
                affirm.setAffirmEmp(ap.getOperatorname());
                affirm.setStatusAffirm(FaultTicket.AFFIRM_STATUS_DONE);
                affirm.setAffirmTime(affirmTime);
            }
            this.saveOrUpdate(affirm);
        }
    }
    
    /**
     * <li>说明：验收
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划ID
     * @param faultTicketType 提票类型
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveCheck(String workPlanIDX, String faultTicketType) throws BusinessException, NoSuchFieldException {
        FaultTicket bean = new FaultTicket();
        bean.setWorkPlanIDX(workPlanIDX);
        bean.setType(faultTicketType);
        List<FaultTicket> entityList = null ;
        // 验收前验证，查询该过程是否做确认，1、如果要做确认，则所有票数据为【已确认】才能验收；2、如果不做确认，则所有票数据为【已处理】才能验收
        EosDictEntry dictEntry = eosDictEntrySelectManager.getEosDictEntryByTypeIDAndName(JCZL_FAULT_TYPE_AFFIRM, faultTicketType);
        if(dictEntry == null){
            throw new BusinessException("类型【"+faultTicketType+"】未配置");
        }
        if("true".equals(dictEntry.getFilter1())){
            bean.setStatus(2);
            bean.setStatusAffirm(1);
            entityList = faultTicketManager.queryFaultTicketList(bean);
//            for (FaultTicket ticket : entityList) {
//                if(ticket.getStatusAffirm() == null || ticket.getStatusAffirm() != FaultTicket.AFFIRM_STATUS_DONE){
//                    throw new BusinessException("【"+ticket.getFaultName()+"】未确认");
//                }
//            }
        }else{
            bean.setStatus(2);
            entityList = faultTicketManager.queryFaultTicketList(bean);
//            for (FaultTicket ticket : entityList) {
//                if(ticket.getStatus() != null && ticket.getStatus() != FaultTicket.STATUS_OVER){
//                    throw new BusinessException("【"+ticket.getFaultName()+"】未处理");
//                }
//            }
        }
        AcOperator ap = SystemContext.getAcOperator();
        Date affirmTime = new Date();
        for (FaultTicket entity : entityList) {
            // 已验收的数据 不做操作
            if(entity.getStatusAffirm() != null && entity.getStatusAffirm() == 2){
                continue ;
            }
            // 保存数据状态为【已验收】
            entity.setStatusAffirm(FaultTicket.AFFIRM_STATUS_CHECK);
            this.daoUtils.saveOrUpdate(entity);
            // 保存确认记录
            FaultTicketCheckAffirm affirm = this.getFaultTicketCheckAffirmByFault(entity.getIdx(),ap.getOperatorid(),FaultTicket.AFFIRM_STATUS_CHECK);
            if(affirm == null){
                affirm = new FaultTicketCheckAffirm();
                affirm.setWorkPlanIDX(workPlanIDX);
                affirm.setFaultTicketIDX(entity.getIdx());
                affirm.setFaultTicketType(faultTicketType);
                affirm.setAffirmEmpId(ap.getOperatorid());
                affirm.setAffirmEmp(ap.getOperatorname());
                affirm.setStatusAffirm(FaultTicket.AFFIRM_STATUS_CHECK);
                affirm.setAffirmTime(affirmTime);
            }
            this.saveOrUpdate(affirm);
        }
    }
    
    /**
     * <li>说明：提票返修
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 提票列表
     */
    public void backCheckAffirm(FaultTicket[] entityList) {
       for (FaultTicket ticket : entityList) {
           FaultTicket entity = faultTicketManager.getModelById(ticket.getIdx());
           // 判断为空的数据
           if(entity == null){
               continue ;
           }
           entity.setStatus(FaultTicket.STATUS_OPEN); // 设置状态为【处理中】
           entity.setStatusAffirm(FaultTicket.AFFIRM_STATUS_UNDO); // 设置确认状态为【未确认】
           entity.setMethodID(null);
           entity.setMethodName(null);
           entity.setMethodDesc(null);
           entity.setRepairResult(null);
           entity.setRepairEmpID(null);
           entity.setRepairEmp(null);
           entity.setRepairTeam(null);
           entity.setRepairTeamName(null);
           entity.setRepairTeamOrgseq(null);
           entity.setCompleteEmp(null);
           entity.setCompleteEmpID(null);
           entity.setCompleteTime(null);
           entity.setResponseEmpID(null);
           entity.setResponseEmp(null);
           // 清除确认信息
           this.deleteCheckAffirmByFaultID(ticket.getIdx());
           this.daoUtils.update(entity);
       }
    }


    /**
     * <li>说明：获取确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fualtIdx 提票ID
     * @param operatorid 操作人员
     * @param statusAffirm 确认状态
     * @return
     */
    public FaultTicketCheckAffirm getFaultTicketCheckAffirmByFault(String fualtIdx, Long operatorid, Integer statusAffirm) {
        StringBuffer hql = new StringBuffer(" From FaultTicketCheckAffirm where recordStatus = 0 ");
        hql.append(" and faultTicketIDX = ? and affirmEmpId = ? and statusAffirm = ? ");
        return (FaultTicketCheckAffirm)this.daoUtils.findSingle(hql.toString(),new Object[]{fualtIdx,operatorid,statusAffirm});
    }
    
    /**
     * <li>说明：获取确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fualtIdx 提票ID
     * @param operatorid 操作人员
     * @param statusAffirm 确认状态
     * @param affirmRoleName 确认角色
     * @return
     */
    public FaultTicketCheckAffirm getFaultTicketCheckAffirmByFault(String fualtIdx, Long operatorid, Integer statusAffirm,String affirmRoleName) {
        StringBuffer hql = new StringBuffer(" From FaultTicketCheckAffirm where recordStatus = 0 ");
        hql.append(" and faultTicketIDX = ? and affirmEmpId = ? and statusAffirm = ? and affirmRoleName = ? ");
        return (FaultTicketCheckAffirm)this.daoUtils.findSingle(hql.toString(),new Object[]{fualtIdx,operatorid,statusAffirm,affirmRoleName});
    }
    
    /**
     * <li>说明：获取确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fualtIdx
     * @param operatorid
     * @param statusAffirm
     * @param affirmRoleName
     * @return
     */
    public List<FaultTicketCheckAffirm> getFaultTicketCheckAffirmByRole(String fualtIdx,Integer statusAffirm,String affirmRoleName) {
        StringBuffer hql = new StringBuffer(" From FaultTicketCheckAffirm where recordStatus = 0 ");
        hql.append(" and faultTicketIDX = ? and statusAffirm = ? and affirmRoleName = ? ");
        return this.daoUtils.find(hql.toString(),new Object[]{fualtIdx,statusAffirm,affirmRoleName});
    }
    
    
    /**
     * <li>说明：获取确认信息(票的确认信息)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fualtIdx 提票ID
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicketCheckAffirm> getFaultTicketCheckAffirmListByFault(String fualtIdx,Integer statusAffirm) {
        StringBuffer hql = new StringBuffer(" From FaultTicketCheckAffirm where recordStatus = 0 ");
        if(statusAffirm != null){
            hql.append(" and statusAffirm = "+statusAffirm);
        }
        hql.append(" and faultTicketIDX = ? ");
        return this.daoUtils.find(hql.toString(),new Object[]{fualtIdx});
    }

    
    /**
     * <li>说明：清除确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void deleteCheckAffirmByFaultID(String faultId){
        String hql = " update FaultTicketCheckAffirm set recordStatus = 1 where faultTicketIDX = ? ";
        this.daoUtils.execute(hql, new Object[]{faultId});
    }

    
    /****************** 以下为新版 *****************/
    
    /**
     * <li>说明：查询类型统计
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicketStatisticsNewVo> queryCheckStatisticsListNew(String workPlanIDX) {
        if(StringUtil.isNullOrBlank(workPlanIDX)){
            return null ;
        }
        AcOperator acOperator = SystemContext.getAcOperator();
        if(acOperator == null){
            return null ;
        }
        // 查询该用户所具备的类型,如果没有，直接返回空
        Map<String, List<String>> typeRoles = this.findFaultTypesByOperator(acOperator.getOperatorid(),1);
        if(typeRoles == null || typeRoles.size() == 0){
            return null ;
        }
        String typeIns = "" ;
        for (String type : typeRoles.keySet()) {
            typeIns += "'"+type+"',";
        }
        typeIns = typeIns.substring(0, typeIns.length() - 1);
        String sql = SqlMapUtil.getSql("jxgc-tp:queryCheckStatisticsListNew");
        sql = sql.replace("#workPlanIDX#", workPlanIDX);
        sql = sql + " and t.type in ("+typeIns+")" ;
        sql = sql + " order by t.sortno " ;
        List<FaultTicketStatisticsNewVo> results = this.daoUtils.executeSqlQueryEntity(sql, FaultTicketStatisticsNewVo.class);
        // 设置类型对应的角色
        for (FaultTicketStatisticsNewVo vo : results) {
            vo.setRoles(typeRoles.get(vo.getFaultTicketType()));
        }
        return results ;
    }
    
    /**
     * <li>说明：查询角色分类统计
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicketStatisticsTotalVo> queryCheckStatisticsListTotal(Long operatorid) {
        if(operatorid == null){
            return null ;
        }
        List<Object> roles = acRoleManager.findRoleNameByOperatorId(operatorid);
        if(roles == null){
            return null ;
        }
        String roleIns = "" ;
        for (Object role : roles) {
            roleIns += "'"+role+"',";
        }
        roleIns = roleIns.substring(0, roleIns.length() - 1);
        String sql = SqlMapUtil.getSql("jxgc-tp:queryCheckStatisticsListTotal");
        sql = sql.replace("#roleIns#", roleIns);
        List<FaultTicketStatisticsTotalVo> results = this.daoUtils.executeSqlQueryEntity(sql, FaultTicketStatisticsTotalVo.class);
        return results ;
    }
    
    

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 人员ID
     * @param type 类型1：确认，类型2：验收
     * @return HashMap<String, List<String>>
     */
    private HashMap<String, List<String>> findFaultTypesByOperator(Long operatorid, int type) {
        HashMap<String, List<String>> results = new HashMap<String, List<String>>();
        // 通过人员获取角色，获取提票确认规则配置项【是否确认==1】，遍历规格，找到对应的角色，插入类型到数据中
        List<Object> roles = acRoleManager.findRoleNameByOperatorId(operatorid);
        // 查询需要做确认的规则
        List<FaultTicketCheckRule> rules = faultTicketCheckRuleManager.findFaultTicketCheckRuleByParams(1, null);
        for (FaultTicketCheckRule rule : rules) {
            List<FaultTicketCheckRole> ruleRoles = faultTicketCheckRoleManager.findFaultTicketCheckRoleByType(rule.getFaultTicketType());
            List<String> roless = new ArrayList<String>();
            for (FaultTicketCheckRole role : ruleRoles) {
                if(roles.contains(role.getRoleName())){
                    roless.add(role.getRoleName());
                }
            }
            if(roless != null && roless.size() > 0){
                results.put(rule.getFaultTicketType(), roless);
            }
        }
        return results;
    }

    /**
     * <li>说明：提票确认（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX
     * @param faultTicketType
     * @param affirmReason
     * @param affirmRoleName
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveAffirmNew(String workPlanIDX, String faultTicketType, String affirmReason,String affirmRoleName) throws BusinessException, NoSuchFieldException {
        // 保存已处理数据
        FaultTicket bean = new FaultTicket();
        bean.setWorkPlanIDX(workPlanIDX);
        bean.setType(faultTicketType);
        bean.setStatus(2);
        List<FaultTicket> entityList = faultTicketManager.queryFaultTicketList(bean);
        for (FaultTicket entity : entityList) {
            saveFaultTicketAndAffirm(entity,workPlanIDX,faultTicketType,affirmReason,affirmRoleName);
        }
        
        // 通过配置查询未处理数据是否可以保存，如果可以，则保存未处理数据
        FaultTicketCheckRule rule = faultTicketCheckRuleManager.getFaultTicketCheckRuleByType(faultTicketType);
        if(rule != null && rule.getIsCheckControl() != 1){
            FaultTicket bean1 = new FaultTicket();
            bean1.setWorkPlanIDX(workPlanIDX);
            bean1.setType(faultTicketType);
            bean1.setStatus(1);
            List<FaultTicket> entityList1 = faultTicketManager.queryFaultTicketList(bean1);
            for (FaultTicket entity : entityList1) {
                saveFaultTicketAndAffirm(entity,workPlanIDX,faultTicketType,affirmReason,affirmRoleName);
            }
        }
    }

    /**
     * <li>说明：保存确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @param workPlanIDX
     * @param faultTicketType
     * @param affirmReason
     * @param affirmRoleName
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void saveFaultTicketAndAffirm(FaultTicket entity, String workPlanIDX, String faultTicketType, String affirmReason, String affirmRoleName) throws BusinessException, NoSuchFieldException {
        AcOperator ap = SystemContext.getAcOperator();
        Date affirmTime = new Date();
        // 保存确认记录
        FaultTicketCheckAffirm affirm = this.getFaultTicketCheckAffirmByFault(entity.getIdx(),ap.getOperatorid(),FaultTicket.AFFIRM_STATUS_DONE,affirmRoleName);
        if(affirm == null){
            affirm = new FaultTicketCheckAffirm();
            affirm.setWorkPlanIDX(workPlanIDX);
            affirm.setFaultTicketIDX(entity.getIdx());
            affirm.setFaultTicketType(faultTicketType);
            affirm.setAffirmEmpId(ap.getOperatorid());
            affirm.setAffirmEmp(ap.getOperatorname());
            affirm.setStatusAffirm(FaultTicket.AFFIRM_STATUS_DONE);
            affirm.setAffirmTime(affirmTime);
            affirm.setAffirmRoleName(affirmRoleName);
            // 未完成的数据需要存确认原因
            if(entity.getStatus() != FaultTicket.STATUS_OVER){
                affirm.setAffirmReason(affirmReason);
            }
        }
        this.saveOrUpdate(affirm);
        // 判断是否该类型的所有角色都已经确认
        boolean flag = true ;
        List<FaultTicketCheckRole> roles = faultTicketCheckRoleManager.findFaultTicketCheckRoleByType(faultTicketType);
        for (FaultTicketCheckRole role : roles) {
            if(affirmRoleName.equals(role.getRoleName())){
                continue ;
            }
            List<FaultTicketCheckAffirm> affirmlist = this.getFaultTicketCheckAffirmByRole(entity.getIdx(), FaultTicket.AFFIRM_STATUS_DONE, role.getRoleName());
            if(affirmlist == null || affirmlist.size() == 0){
                flag = false ;
                break ;
            }
        }
        if(flag){
            entity.setStatusAffirm(FaultTicket.AFFIRM_STATUS_DONE);
            this.daoUtils.saveOrUpdate(entity);
        }
    }
}
