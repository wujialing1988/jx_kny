package com.yunda.jx.pjwz.check.manager;
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
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.pjwz.check.entity.PartsCheck;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsCheck业务层, 配件校验
 * <li>创建人：程梅
 * <li>创建日期：2016年5月31日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@Service(value="partsCheckManager")
public class PartsCheckManager extends JXBaseManager<PartsCheck, PartsCheck>{
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /** 组织机构业务类 */
    @Resource
    private IOmOrganizationManager omOrganizationManager;
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsCheck> 分页对象
     * @throws BusinessException
     */
    public Page<PartsCheck> findPageList(SearchEntity<PartsCheck> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsCheck t where t.recordStatus=0 ");
        PartsCheck register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.checkOrg like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.checkTime DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【待校验】配件周转台账信息
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
        //待校验的配件可校验
        if(null != accountV && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DJY) ){
            throw new BusinessException("只有【待校验】的配件才能校验，请重新添加！");
        }
        //获取当前登录者所在机构信息
        OmOrganization org = SystemContext.getOmOrganization();
        // 责任部门为库房则不限制 by wujl 2016-08-12
        if(PartsAccount.MANAGE_DEPT_TYPE_ORG == accountV.getManageDeptType() && !org.getOrgid().toString().equals(accountV.getManageDeptId())){
            throw new BusinessException("只能校验责任部门为当前班组的配件，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【责任部门为当前班组且状态为待校验】配件周转台账信息列表
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
              if (null != entity && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DJY)) {
                  entityList.remove(entity);
              }else if(PartsAccount.MANAGE_DEPT_TYPE_ORG == entity.getManageDeptType() && !org.getOrgid().toString().equals(entity.getManageDeptId())){
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只能校验责任部门为当前班组且状态为【待校验】的配件，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * <li>说明：保存校验单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param check 配件校验信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsCheck(PartsCheck check) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(check.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+check.getPartsNo()+"的配件不存在，请重新添加！");
        }
        // 【待校验】的配件可校验
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DJY)){
            throw new BusinessException("只有【待校验】的配件才能校验！");
        }
        //日志描述=校验部门
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(check.getCheckOrg())) logDesc = check.getCheckOrg();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJJY, logDesc);
        log = partsManageLogManager.initLog(log, account);
        if(null != check.getCheckEmpId()){
            //查询校验人所在部门
            OmOrganization org = omOrganizationManager.findByEmpId(Long.parseLong(check.getCheckEmpId().toString()));
            check.setCheckOrgId(Integer.parseInt(org.getOrgid().toString()));
            check.setCheckOrg(org.getOrgname());
            check.setCheckOrgSeq(org.getOrgseq());
        }
        //更新配件周转台账
        updateAccount(check , account);
        check.setCheckTime(new Date());//校验日期为当前日期
        super.saveOrUpdate(check);
        log.eventIdx(check.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param check 配件移库信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsCheck check, PartsAccount account) throws BusinessException, NoSuchFieldException {
        //设置责任部门信息和存放地点
        if(null != check.getCheckOrgId()) account.setManageDeptId(check.getCheckOrgId().toString());  //责任部门id为校验部门id
        account.setManageDept(check.getCheckOrg()); //责任部门名称为校验部门名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDeptOrgseq(check.getCheckOrgSeq());//责任部门序列为校验部门序列
        account.setLocation("");//存放地点为空
        account.setPartsStatusUpdateDate(check.getCheckTime());//配件状态更新时间为校验日期
        //校验结果为合格，则配件状态更新为良好；校验结构为不合格，则配件状态更新为待修
        if(PartsCheck.CHECK_RESULT_YES.equals(check.getCheckResult())){
            account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, PartsAccount.PARTS_STATUS_LH_CH)); 
        }else if(PartsCheck.CHECK_RESULT_NO.equals(check.getCheckResult())){
            account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);//配件状态为待修
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, PartsAccount.PARTS_STATUS_DX_CH)); 
        }
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
    }
    /**
     * <li>说明：配件校验登记【web端/手持终端批量校验】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 配件校验信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsCheckBatch(PartsCheck[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsCheck register : registers) {
            savePartsCheck(register);
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
    public void updatePartsCheckForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsCheck check = getModelById(id);
        if(null != check){
            PartsAccount account = partsAccountManager.getModelById(check.getPartsAccountIdx()); //查询该配件周转台账信息
            //根据校验结果和配件状态验证是否可撤销
            if(PartsCheck.CHECK_RESULT_YES.equals(check.getCheckResult()) && !PartsAccount.PARTS_STATUS_LH.equals(account.getPartsStatus())){
                throw new BusinessException("只有【良好】的配件才能撤销！");
            }else if(PartsCheck.CHECK_RESULT_NO.equals(check.getCheckResult()) && !PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus())){
                throw new BusinessException("只有【待修】的配件才能撤销！");
            }else{
                //根据配件id和校验单id查询配件日志信息
                PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                //配件状态回滚到校验前
                account = partsManageLogManager.getAccountFromLog(log, account);
                this.logicDelete(id);    //删除配件校验信息
                partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
            }
            
          }else {
              throw new BusinessException("数据有误！");
          }
        }
}