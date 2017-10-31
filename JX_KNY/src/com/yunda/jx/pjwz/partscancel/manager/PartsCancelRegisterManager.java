package com.yunda.jx.pjwz.partscancel.manager;

import java.lang.reflect.InvocationTargetException;
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
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partscancel.entity.PartsCancelRegister;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsCancelRegister业务类,配件销账单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsCancelRegisterManager")
public class PartsCancelRegisterManager extends JXBaseManager<PartsCancelRegister, PartsCancelRegister>{
    
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
     * @return Page<PartsCancelRegister> 分页对象
     * @throws BusinessException
     */
    public Page<PartsCancelRegister> findPageList(SearchEntity<PartsCancelRegister> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsCancelRegister t where t.recordStatus=0 ");
        PartsCancelRegister register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.identificationCode like '%").append(identificationCode).append("%' ) ") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.canceledDate DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：保存配件销账单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件销账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsCancel(PartsCancelRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)){
            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能销账，请重新添加！");
        }
        //日志描述=销账部门+销账理由
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(register.getCanceledOrg())) logDesc = register.getCanceledOrg() ;
        if(!StringUtil.isNullOrBlank(register.getCanceledReason())) logDesc = logDesc + register.getCanceledReason() ;
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJXZ, logDesc);
        log = partsManageLogManager.initLog(log, account);
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) {
            register.setCanceledEmpId(Integer.parseInt(String.valueOf(emp.getEmpid())));
            register.setCanceledEmp(emp.getEmpname());
        }
        OmOrganization org = SystemContext.getOmOrganization();
        if (org != null) {
            register.setCanceledOrgId(org.getOrgid());
            register.setCanceledOrg(org.getOrgname());
        }
        //更新配件周转台账
        updateAccount(register , account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
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
     * @param register 配件销账信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsCancelRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        account.setPartsStatusUpdateDate(register.getCanceledDate());//配件状态更新时间为销账日期
        //责任部门为销账部门信息
        account.setManageDeptId(String.valueOf(register.getCanceledOrgId()));  //责任部门id为销账部门id
        account.setManageDept(register.getCanceledOrg()); //责任部门名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDeptOrgseq("");//责任部门序列为空
        
        account.setPartsStatus(PartsAccount.PARTS_STATUS_YXZ);//配件状态为已销账
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_YXZ, "已销账"));
//        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
            
    }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
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
    public void updatePartsCancelForCancel(String id) throws BusinessException, NoSuchFieldException {
            PartsCancelRegister register = getModelById(id);
            if(null != register){
                PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
                if(!PartsAccount.PARTS_STATUS_YXZ.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()){
                    throw new BusinessException("只有【已销账】状态的配件才能撤销！");
                }else{
                    //根据配件id和销账登记id查询配件日志信息
                    PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                    //配件状态回滚到销账前
                    account = partsManageLogManager.getAccountFromLog(log, account);
                    this.logicDelete(id);    //删除配件销账信息
                    partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                    partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                }
                
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修改配件销账单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件销账信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsCancel(PartsCancelRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        if(!PartsAccount.PARTS_STATUS_YXZ.equals(account.getPartsStatus())){
            throw new BusinessException("只有【已销账】状态的配件才能修改，请重新添加！");
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
     * <li>说明：根据配件编号和规格型号查询最新的【在册】配件周转台账信息
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
//        account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC) ; //查询【在册】状态的周转信息
        PartsAccount accountV = this.partsAccountManager.getAccount(account);
        if(null == accountV){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode("");
            accountV = this.partsAccountManager.getAccount(account);
        }
        if (null == accountV) {
            throw new BusinessException("此配件未登记！");
        }
        //待修、良好状态的配件能销账
        if(null != accountV && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)){
            throw new BusinessException("只有待修、良好的配件才能销账，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【待修、良好】配件周转台账信息列表
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
              if (null != entity && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只有待修、良好的配件才能销账，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
	/**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：保存配件销账信息【web端】
     * <li>创建人：王利成
     * <li>创建日期：2015-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param cancelParts 配件销账信息
     * @param detailList   配件信息
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
     * @throws IllegalAccessException
     * @throws InvocationTargetException
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
	public void savePartsCancelRegister(PartsCancelRegister cancelParts, PartsAccount[] detailList) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
		OmOrganization om = SystemContext.getOmOrganization();
	    if(detailList!=null && detailList.length>0){
			for(PartsAccount account : detailList) {
				PartsCancelRegister partsCancelRegister = new PartsCancelRegister();
	    		BeanUtils.copyProperties(partsCancelRegister, cancelParts);
	    		partsCancelRegister.setPartsName(account.getPartsName());
	    		partsCancelRegister.setPartsNo(account.getPartsNo());
	    		partsCancelRegister.setPartsTypeIdx(account.getPartsTypeIDX());
	    		partsCancelRegister.setPartsAccountIdx(account.getIdx());
	    		partsCancelRegister.setIdentificationCode(account.getIdentificationCode());
	    		partsCancelRegister.setSpecificationModel(account.getSpecificationModel());
	    		partsCancelRegister.setCanceledOrgId(om.getOrgid());
	    		partsCancelRegister.setCanceledOrg(om.getOrgname());
	    		partsCancelRegister.setCanceledEmpId(cancelParts.getCanceledEmpId());
	    		partsCancelRegister.setCanceledEmp(cancelParts.getCanceledEmp());
	    		partsCancelRegister.setCanceledDate(cancelParts.getCanceledDate());
	    		partsCancelRegister.setCanceledReason(cancelParts.getCanceledReason());
	    		partsCancelRegister.setCanceledType(cancelParts.getCanceledType());
	    		this.savePartsCancel(partsCancelRegister);
	    	}
	    }
	}

    /**
     * <li>说明：配件销账登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 配件销账登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsCancelRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsCancelRegister> partsCancelRegisterList = new ArrayList<PartsCancelRegister>();
        PartsCancelRegister partsCancelRegister;
        for (String id : ids) {
            partsCancelRegister = new PartsCancelRegister();
            partsCancelRegister = getModelById(id);
            if(null != partsCancelRegister){
                partsCancelRegister.setStatus(PjwzConstants.STATUS_CHECKED);//下车配件登记状态为已确认
                partsCancelRegisterList.add(partsCancelRegister);
            }
        }
        this.saveOrUpdate(partsCancelRegisterList);
    }
    
}