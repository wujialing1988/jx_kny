package com.yunda.jx.pjwz.partsscrap.manager;

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
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.partsscrap.entity.PartsScrapRegister;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsScrapRegister业务类,配件报废登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsScrapRegisterManager")
public class PartsScrapRegisterManager extends JXBaseManager<PartsScrapRegister, PartsScrapRegister>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
	/** 组织机构查询接口 */
	@Resource(name="omOrganizationManager")
	private IOmOrganizationManager OmOrganizationManager;
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsScrapRegister> 分页对象
     * @throws BusinessException
     */
    public Page<PartsScrapRegister> findPageList(SearchEntity<PartsScrapRegister> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsScrapRegister t where t.recordStatus=0 ");
        PartsScrapRegister register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.identificationCode like '%").append(identificationCode).append("%' ) ") ;
        }
//        if(!StringUtil.isNullOrBlank(scrapDate)){
//            awhere.append(" and substr(to_char(t.scrapDate,'yyyy-MM-dd'),0,7) = '" + scrapDate + "'");
//        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.scrapDate desc,t.updateTime desc");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：保存配件报废登记信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件报废信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsScrap(PartsScrapRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF)){
            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能报废，请重新添加！");
        }
        //日志描述=报废理由
        String logDesc = register.getScrapReason();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJBF, logDesc);
        log = partsManageLogManager.initLog(log, account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) {
        	register.setScrapEmpId(Integer.parseInt(String.valueOf(emp.getEmpid())));
        	register.setScrapEmp(emp.getEmpname());
        }
        	
        OmOrganization org = SystemContext.getOmOrganization();
        if (org != null) {
        	register.setScrapOrgId(String.valueOf(org.getOrgid()));
        	register.setScrapOrg(org.getOrgname());
        }
        //更新配件周转台账
        updateAccount(register , account);
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
     * @param register 配件报废信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsScrapRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        //责任部门为报废部门信息
        account.setManageDeptId(register.getScrapOrgId());  //责任部门id为报废部门id
        account.setManageDept(register.getScrapOrg()); //责任部门名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDeptOrgseq("");//责任部门序列为空
        //清除存放地点
        account.setLocation("");//存放地点
        account.setPartsStatusUpdateDate(register.getScrapDate());//配件状态更新时间为报废日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_YBF);//配件状态为已报废
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_YBF, "已报废"));
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
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
    public void updatePartsScrapForCancel(String id) throws BusinessException, NoSuchFieldException {
            PartsScrapRegister register = getModelById(id);
            if(null != register){
                PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
                PartsAccount accountS = new PartsAccount();
                accountS.setIdentificationCode(account.getIdentificationCode());
                accountS.setPartsStatus(PartsAccount.PARTS_STATUS_ZC) ;
                PartsAccount accountV = this.partsAccountManager.getAccount(accountS);
                if(null == accountV){
                    accountS.setPartsNo(account.getPartsNo());
                    accountS.setSpecificationModel(account.getSpecificationModel());
                    accountS.setIdentificationCode("");
                    accountV = this.partsAccountManager.getAccount(accountS);
                }
                if(!PartsAccount.PARTS_STATUS_YBF.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()){
                    throw new BusinessException("只有【已报废】状态的配件才能撤销！");
                }else if(null != accountV){
                    throw new BusinessException("已有相同的配件存在，不能撤销！");
                }else{
                    //根据配件id和报废单id查询配件日志信息
                    PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                    //配件状态回滚到报废前
                    account = partsManageLogManager.getAccountFromLog(log, account);
                    this.logicDelete(id);    //删除配件报废信息
                    partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                    partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                }
                
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修改配件报废登记信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件报废信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsScrap(PartsScrapRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        if(!PartsAccount.PARTS_STATUS_YBF.equals(account.getPartsStatus())){
            throw new BusinessException("只有【已报废】状态的配件才能修改，请重新添加！");
        }
        //更新配件周转台账
        updateAccount(register , account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) {
        	register.setScrapEmpId(Integer.parseInt(String.valueOf(emp.getEmpid())));
        	register.setScrapEmp(emp.getEmpname());
        }
        	
        OmOrganization org = SystemContext.getOmOrganization();
        if (org != null) {
        	register.setScrapOrgId(String.valueOf(org.getOrgid()));
        	register.setScrapOrg(org.getOrgname());
        }
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
        //待修、待报废、检修中状态的配件能报废
        //Modified by hetao on 2016-03-24 由西安现场（JiangJ）试用提出，检修中的配件也有报废的可能
        if(null != accountV && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) 
            && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF) 
            && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_JXZ)){
            throw new BusinessException("只有待修、待报废、检修中的配件才能报废，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【待修、待报废、检修中】配件周转台账信息列表
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
              if (null != entity && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) 
                  && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF) 
                  && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_JXZ)) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只有待修、待报废、检修中的配件才能报废，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：web端保存配件报废记录
     * <li>创建人：王利成
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param scrap 报废记录保存实体
     * @param details 配件信息对象数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	@SuppressWarnings("unchecked")
	public void savePartdScrapRegister(PartsScrapRegister scrap,PartsAccount[] details) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
		OmOrganization omOrg = OmOrganizationManager.findByEmpId(Long.valueOf(scrap.getScrapEmpId()));
		if(details!=null && details.length>0){
			//遍历需要报废的配件信息
			for (PartsAccount account : details) {
				PartsScrapRegister scrapRegister = new PartsScrapRegister();
				BeanUtils.copyProperties(scrapRegister, scrap);
				scrapRegister.setPartsAccountIdx(account.getIdx());
				scrapRegister.setPartsName(account.getPartsName());
				scrapRegister.setPartsNo(account.getPartsNo());
				scrapRegister.setPartsTypeIDX(account.getPartsTypeIDX());
				scrapRegister.setIdentificationCode(account.getIdentificationCode());
				scrapRegister.setSpecificationModel(account.getSpecificationModel());
                scrapRegister.setUnloadTrainTypeIdx(account.getUnloadTrainTypeIdx());
                scrapRegister.setUnloadTrainType(account.getUnloadTrainType());
                scrapRegister.setUnloadTrainNo(account.getUnloadTrainNo());
				if(omOrg != null){
					scrapRegister.setScrapOrgId(String.valueOf(omOrg.getOrgid()));
					scrapRegister.setScrapOrg(omOrg.getOrgname());
				}
			    this.savePartsScrap(scrapRegister);
			}
		}
	}
    
    /**
     * <li>说明：配件报废登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 配件报废登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsScrapRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsScrapRegister> partsScrapRegisterList = new ArrayList<PartsScrapRegister>();
        PartsScrapRegister partsScrapRegister;
        for (String id : ids) {
            partsScrapRegister = new PartsScrapRegister();
            partsScrapRegister = getModelById(id);
            if(null != partsScrapRegister){
                partsScrapRegister.setStatus(PjwzConstants.STATUS_CHECKED);//配件报废登记状态为已确认
                partsScrapRegisterList.add(partsScrapRegister);
            }
        }
        this.saveOrUpdate(partsScrapRegisterList);
    }
    
}