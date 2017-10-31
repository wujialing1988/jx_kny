package com.yunda.jx.jxgc.tpmanage.manager;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRule;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 提票过程规则业务类
 * <li>创建人：张迪
 * <li>创建日期：2016-12-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckRuleManager")
public class FaultTicketCheckRuleManager extends JXBaseManager<FaultTicketCheckRule, FaultTicketCheckRule> {
    /**
     * <li>说明： 分页查询
     * <li>创建人：张迪
     * <li>创建日期：2016-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param searchEntity 查询实体对象
     * @return 综合查询集合
     */     
    @Override
    public Page<FaultTicketCheckRule> findPageList(SearchEntity<FaultTicketCheckRule> searchEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT A.DICTNAME AS FAULT_TICKET_TYPE, NVL(B.IDX, SYS_GUID()) AS IDX, B.IS_AFFIRM, B.IS_CHECK,B.IS_DONE_CONTROL, B.SEQ_NO, B.RECORD_STATUS, B.CREATOR, B.CREATE_TIME, B.UPDATOR, B.UPDATE_TIME From EOS_DICT_ENTRY A, JXGC_FAULT_TICKET_RULE B Where  A.DICTNAME = B.FAULT_TICKET_TYPE(+) AND A.DICTTYPEID = 'JCZL_FAULT_TYPE' ");
       
        String hql = sql.toString();
        String totalSql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
        return super.queryPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, FaultTicketCheckRule.class);
    }
    
    
    /**
     * <li>说明：通过类型获取规则
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param type
     * @return
     */
    public FaultTicketCheckRule getFaultTicketCheckRuleByType(String type){
        StringBuffer sb = new StringBuffer(" From FaultTicketCheckRule where recordStatus = 0 ");
        sb.append(" and faultTicketType = ? ");
        return (FaultTicketCheckRule)this.daoUtils.findSingle(sb.toString(),new Object[]{type});
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param isAffirm 是否做确认
     * @param isCheck 是否做验收
     * @return
     */
    public List<FaultTicketCheckRule> findFaultTicketCheckRuleByParams(Integer isAffirm,Integer isCheck){
        StringBuffer sb = new StringBuffer(" From FaultTicketCheckRule where recordStatus = 0 ");
        // 是否做确认
        if(isAffirm != null){
            sb.append(" and isAffirm = "+isAffirm);
        }
        // 是否做验收
        if(isCheck != null){
            sb.append(" and isCheck = "+isCheck);
        }
        return this.daoUtils.find(sb.toString());
    }
    /**
     * <li>说明： 保存和更新提票规则方法
     * <li>创建日期：2016-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容： 
     * @param searchEntity 查询实体对象
     */     
    @Override
    public void saveOrUpdate(FaultTicketCheckRule t) throws BusinessException, NoSuchFieldException {
        t = EntityUtil.setSysinfo(t);
        //设置逻辑删除字段状态为未删除
        t = EntityUtil.setNoDelete(t);
        FaultTicketCheckRule oldEntity = this.getModelById(t.getIdx());
        if(null == oldEntity){
//          获取当前登录操作员
            AcOperator acOperator = SystemContext.getAcOperator();  
            t.setCreator(acOperator.getOperatorid());
            t.setCreateTime(new Date());
            this.daoUtils.insert(t);
        }
        else {
            oldEntity.setIsAffirm(t.getIsAffirm());
            oldEntity.setIsCheck(t.getIsCheck());
            oldEntity.setIsCheckControl(t.getIsCheckControl());
            oldEntity.setSeqNo(t.getSeqNo()); 
            this.daoUtils.update(oldEntity);
        }
    }
}
