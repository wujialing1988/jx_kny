package com.yunda.jx.pjwz.handover.manager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.pjwz.handover.entity.PartsHandOver;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsHandOver业务层, 配件交接
 * <li>创建人：程梅
 * <li>创建日期：2016年5月31日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@Service(value="partsHandOverManager")
public class PartsHandOverManager extends JXBaseManager<PartsHandOver, PartsHandOver>{
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsHandOver> 分页对象
     * @throws BusinessException
     */
    public Page<PartsHandOver> findPageList(SearchEntity<PartsHandOver> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsHandOver t where t.recordStatus=0 ");
        PartsHandOver register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.handOverOrg like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.handOverTime DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【不在库】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getPartsAccount(PartsAccount account) {
        PartsAccount accountV = this.partsAccountManager.getAccount(account);
        if(null == accountV){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode("");
            accountV = this.partsAccountManager.getAccount(account);
        }
        if (null == accountV) {
            throw new BusinessException("此配件未登记！");
        }
        //责任部门类型为机构的可交接
        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)) ){
            throw new BusinessException("只有【不在库】的配件才能交接，请重新添加！");
        }
        //获取当前登录者所在机构信息
        OmOrganization org = SystemContext.getOmOrganization();
        if(!org.getOrgid().toString().equals(accountV.getManageDeptId())){
            throw new BusinessException("只能交接责任部门为当前班组的的配件，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【责任部门为当前班组且状态为不在库】配件周转台账信息列表
     * <li>创建人：程梅
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return List<PartsAccount> 台账信息列表
     */
    public List<PartsAccount> getPartsAccountList(PartsAccount account) {
      List<PartsAccount> entityList = this.partsAccountManager.getAccountList(account);
      if(null == entityList || entityList.size() == 0){
          account.setPartsNo(account.getIdentificationCode());
          account.setIdentificationCode("");
          entityList = this.partsAccountManager.getAccountList(account);
      }
      //获取当前登录者所在机构信息
      OmOrganization org = SystemContext.getOmOrganization();
      if(null != entityList && entityList.size() > 0){
          // 先将list进行复制 然后再遍历
          List<PartsAccount> entityListV = new ArrayList<PartsAccount>();
          entityListV.addAll(entityList);
          for(PartsAccount entity : entityListV){
              if (null != entity && (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !entity.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG))) {
                  entityList.remove(entity);
              }else if(!org.getOrgid().toString().equals(entity.getManageDeptId())){
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只能交接责任部门为当前班组且状态为【不在库】的配件，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * <li>说明：保存交接单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param handover 配件交接信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsHandOver(PartsHandOver handover) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(handover.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+handover.getPartsNo()+"的配件不存在，请重新添加！");
        }
        //责任部门类型为机构的可交接
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)){
            throw new BusinessException("只有【不在库】的配件才能交接！");
        }
        //接收部门和交接部门不能相同
        if(account.getManageDeptId().equals(handover.getTakeOverOrgId())){
            throw new BusinessException("接收部门和交接部门不能相同！");
        }
        //交件班组为所选配件的责任部门信息
        if(null == handover.getHandOverOrgId()){
            handover.setHandOverOrgId(Integer.parseInt(account.getManageDeptId()));
            handover.setHandOverOrg(account.getManageDept());
            handover.setHandOverOrgSeq(account.getManageDeptOrgseq());
        }
        //日志描述=接收部门
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(handover.getTakeOverOrg())) logDesc = handover.getTakeOverOrg();
        if(!StringUtil.isNullOrBlank(handover.getTakeOverEmp())) logDesc = logDesc + " " + handover.getTakeOverEmp();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJJJ, logDesc);
        log = partsManageLogManager.initLog(log, account);
        //更新配件周转台账
        handover = updateAccount(handover , account);
        handover.setHandOverTime(new Date());//交接日期为当前日期
        super.saveOrUpdate(handover);
        log.eventIdx(handover.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param handover 配件交接信息
     * @param account 配件周转台账信息
     * @return PartsHandOver 实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public PartsHandOver updateAccount(PartsHandOver handover, PartsAccount account) throws BusinessException, NoSuchFieldException {
        //交件人为当前登录人
        OmEmployee emp = SystemContext.getOmEmployee();
        if(null != emp){
            handover.setHandOverEmpId(Integer.parseInt(emp.getEmpid().toString()));
            handover.setHandOverEmp(emp.getEmpname());
        }
        //配件交接中保存配件状态信息
        handover.setPartsStatus(account.getPartsStatus());
        handover.setPartsStatusName(account.getPartsStatusName());
        //设置责任部门信息和存放地点
        account.setManageDeptId(handover.getTakeOverOrgId().toString());  //责任部门id为接收部门id
        account.setManageDept(handover.getTakeOverOrg()); //责任部门名称为接收部门名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDeptOrgseq(handover.getTakeOverOrgSeq());//责任部门序列为接收部门序列
        account.setLocation("");//存放地点为空
        account.setPartsStatusUpdateDate(handover.getHandOverTime());//配件状态更新时间为交接日期
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
        return handover;
    }
    /**
     * <li>说明：配件交接登记【web端/手持终端批量交接】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 配件交接信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsHandOverBatch(PartsHandOver[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsHandOver register : registers) {
            savePartsHandOver(register);
        }
    }
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsHandOverForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsHandOver handover = getModelById(id);
        if(null != handover){
            PartsAccount account = partsAccountManager.getModelById(handover.getPartsAccountIdx()); //查询该配件周转台账信息
            //不在库的配件可撤销
            if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)){
                throw new BusinessException("只有【不在库】的配件才能撤销！");
            }else{
                //根据配件id和交接单id查询配件日志信息
                PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                //配件状态回滚到交接前
                account = partsManageLogManager.getAccountFromLog(log, account);
                this.logicDelete(id);    //删除配件交接信息
                partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
            }
            
          }else {
              throw new BusinessException("数据有误！");
          }
        }
}