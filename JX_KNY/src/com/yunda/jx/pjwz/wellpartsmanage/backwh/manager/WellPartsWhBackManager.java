package com.yunda.jx.pjwz.wellpartsmanage.backwh.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.wellpartsmanage.backwh.entity.WellPartsWhBack;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WellPartsWhBack业务类,良好配件退库单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wellPartsWhBackManager")
public class WellPartsWhBackManager extends JXBaseManager<WellPartsWhBack, WellPartsWhBack>{
    
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** 配件管理日志业务类 */
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
     * @return Page<WellPartsWhBack> 分页对象
     * @throws BusinessException
     */
    public Page<WellPartsWhBack> findPageList(SearchEntity<WellPartsWhBack> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From WellPartsWhBack t where t.recordStatus=0 ");
        WellPartsWhBack register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.whName like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.backTime DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【良好不在库】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
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
        //良好不在库状态的配件能退库
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG))){
            throw new BusinessException("只有【良好不在库】的配件才能退库，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【良好不在库】配件周转台账信息列表
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
      if(null != entityList && entityList.size() > 0){
          // 先将list进行复制 然后再遍历
          List<PartsAccount> entityListV = new ArrayList<PartsAccount>();
          entityListV.addAll(entityList);
          for(PartsAccount entity : entityListV){
              if (null != entity && (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !entity.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG))) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只有【良好不在库】的配件才能退库，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * <li>说明：保存配件退库单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-02-22
     * <li>修改内容：代码规范
     * @param register 配件退库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveWellPartsWhBack(WellPartsWhBack register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
        if (null == account) {
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)){
            throw new BusinessException("只有【良好不在库】的配件才能退库，请重新添加！");
        }
        //日志描述=库房
        String logDesc = register.getWhName() ;
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJTK, logDesc);
        log = partsManageLogManager.initLog(log, account);
        //更新配件周转台账
        updateAccount(register , account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        OmEmployee emp = SystemContext.getOmEmployee();
        if(null != emp){
            register.setWarehouseEmpId(emp.getEmpid());
            register.setWarehouseEmp(emp.getEmpname());
        }
        this.saveOrUpdate(register);
        log.eventIdx(register.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件退库信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(WellPartsWhBack register, PartsAccount account) throws BusinessException, NoSuchFieldException {
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        account.setPartsStatusUpdateDate(register.getBackTime());//配件状态更新时间为退库日期
        account.setManageDeptId(register.getWarehouseIdx());  //责任部门id为接收库房id
        account.setManageDept(register.getWhName()); //责任部门名称为接收库房名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_WH);//责任部门类型为库房
        account.setManageDeptOrgseq("");//责任部门序列为空
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好在库
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, PartsAccount.PARTS_STATUS_LH_CH));
//        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
    }
    
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsWhBackForCancel(String id) throws BusinessException, NoSuchFieldException {
        WellPartsWhBack register = getModelById(id);
        if (null == register) {
            throw new BusinessException("数据有误！");
        }
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); // 查询该配件周转台账信息
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if (!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH) || Constants.DELETED == account.getRecordStatus()) {
            throw new BusinessException("只有【良好在库】的配件才能撤销！");
        }
        // 根据配件id和销账登记id查询配件日志信息
        PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id);
        // 配件状态回滚到退库前
        account = partsManageLogManager.getAccountFromLog(log, account);
        this.logicDelete(id); // 删除配件退库信息
        partsAccountManager.saveOrUpdate(account); // 更新周转台账信息
        partsManageLogManager.deleteLogByEventIdx(id);// 删除日志信息
    }
    
    /**
     * <li>说明：修改配件退库单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件退库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWellPartsWhBack(WellPartsWhBack register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
            throw new BusinessException("只有【良好在库】的配件才能修改，请重新添加！");
        }
        //更新配件周转台账
        updateAccount(register , account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(register);
    }
    
    /**
     * <li>说明：保存配件退库信息【web端】
     * <li>创建人：王利成
     * <li>创建日期：2015-11-10
     * <li>修改人：何涛
     * <li>修改日期：2016-02-22
     * <li>修改内容：代码规范
     * @param wellParts 配件退库信息
     * @param details   配件信息
     * @throws IllegalAccessException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unused")
	public void saveWellWhPartsAccount(WellPartsWhBack wellParts, PartsAccount[] details) throws BusinessException, NoSuchFieldException  {
        if (null == details && details.length <= 0) {
            return;
        }
        for (PartsAccount account : details) {
            WellPartsWhBack wellPartsWhBack = new WellPartsWhBack();
            try {
                BeanUtils.copyProperties(wellPartsWhBack, wellParts);
            } catch (Exception e) {
                throw new BusinessException("操作失败，属性赋值异常！");
            }
            wellPartsWhBack.setPartsName(account.getPartsName());
            wellPartsWhBack.setPartsNo(account.getPartsNo());
            wellPartsWhBack.setPartsTypeIDX(account.getPartsTypeIDX());
            wellPartsWhBack.setIdentificationCode(account.getIdentificationCode());
            wellPartsWhBack.setSpecificationModel(account.getSpecificationModel());
            wellPartsWhBack.setBackTime(wellParts.getBackTime());
            wellPartsWhBack.setPartsAccountIDX(account.getIdx());
            wellPartsWhBack.setLocationName(account.getLocation());
            this.saveWellPartsWhBack(wellPartsWhBack);
        }
    }
    
    /**
     * <li>说明：配件退库登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 配件退库登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWellPartsWhBackForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<WellPartsWhBack> wellPartsWhBackList = new ArrayList<WellPartsWhBack>();
        WellPartsWhBack wellPartsWhBack;
        for (String id : ids) {
            wellPartsWhBack = new WellPartsWhBack();
            wellPartsWhBack = getModelById(id);
            if(null != wellPartsWhBack){
                wellPartsWhBack.setStatus(PjwzConstants.STATUS_CHECKED);//配件退库登记状态为已确认
                wellPartsWhBackList.add(wellPartsWhBack);
            }
        }
        this.saveOrUpdate(wellPartsWhBackList);
    }
    
}