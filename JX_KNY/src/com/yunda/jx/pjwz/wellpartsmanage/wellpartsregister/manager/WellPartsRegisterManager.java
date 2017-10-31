package com.yunda.jx.pjwz.wellpartsmanage.wellpartsregister.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

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
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsregister.entity.WellPartsRegister;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.PartsStock;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：wellPartsRegister业务类,良好配件登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wellPartsRegisterManager")
public class WellPartsRegisterManager extends JXBaseManager<WellPartsRegister, WellPartsRegister>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /**
     * <li>说明：分页查询
     * <li>创建人：程梅
     * <li>创建日期：2015-9-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return 分页对象
     * @throws BusinessException
     */
    public Page<WellPartsRegister> findPageList(SearchEntity<WellPartsRegister> searchEntity) throws BusinessException{
        String totalHql = "select count(*) from WellPartsRegister t where t.recordStatus=0 ";
        String hql = "From WellPartsRegister t where t.recordStatus=0 ";
        String identificationCode = searchEntity.getEntity().getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.source like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
//        if(!StringUtil.isNullOrBlank(acceptTime)){
//            awhere.append(" and substr(to_char(t.acceptTime,'yyyy-MM-dd'),0,7) = '" + acceptTime + "'");
//        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.updateTime DESC");
        }
        totalHql += awhere;
        hql += awhere;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：良好配件登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 良好配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void saveWellParts(WellPartsRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        PartsAccount accountS = new PartsAccount();
        accountS.setPartsNo(register.getPartsNo());
        accountS.setSpecificationModel(register.getSpecificationModel());
        accountS.setPartsStatus(PartsAccount.PARTS_STATUS_ZC) ;
        //配件识别码唯一【在册状态中唯一】
        if(null != partsAccountManager.getAccountByIdCode(register.getIdentificationCode())){
            throw new BusinessException("配件识别码【"+register.getIdentificationCode()+"】已存在，不能重复添加！");
        }
        //配件编号+配件规格型号判断唯一性【在册状态中唯一】
        PartsAccount accountR = partsAccountManager.getAccount(accountS) ;
        if(null != accountR){
            throw new BusinessException("配件编号【"+register.getPartsNo()+"】,规格型号【" + register.getSpecificationModel() + "】已存在，不能重复添加！");
        }
        register.setAcceptTime(new Date());//接收日期为当前日期
        //新增到配件周转台账表中
        WellPartsRegister wellPartsRegister = saveAccount(register);
        //接收部门类型为【库房】，则新增良好配件库存信息
        if(WellPartsRegister.ACCEPT_DEPT_TYPE_WH == wellPartsRegister.getAcceptDeptType()){
            //新增良好配件库存信息
            savePartsStock(register);
        }
        wellPartsRegister.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        wellPartsRegister.setAcceptTime(new Date());
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null)
            wellPartsRegister.setCreatorName(emp.getEmpname());
        this.saveOrUpdate(wellPartsRegister);
        //日志描述=接收部门+存放位置
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(wellPartsRegister.getAcceptDept())) logDesc = wellPartsRegister.getAcceptDept();
        if(!StringUtil.isNullOrBlank(wellPartsRegister.getLocationName())) logDesc = logDesc + " " + wellPartsRegister.getLocationName();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_LHDJ, logDesc);
        log.partsAccountIdx(wellPartsRegister.getPartsAccountIdx());
        log.eventIdx(wellPartsRegister.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：良好配件登记【web端】
     * <li>创建人：程锐
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 良好配件信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void saveWellParts(WellPartsRegister[] registers) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        for (WellPartsRegister register : registers) {
            saveWellParts(register);
        }
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：新增数据到配件台账中【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 良好配件信息
     * @return WellPartsRegister
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public WellPartsRegister saveAccount(WellPartsRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = new PartsAccount();
        account.setPartsTypeIDX(register.getPartsTypeIdx());
        account.setSpecificationModel(register.getSpecificationModel());
        account.setPartsName(register.getPartsName());
        account.setPartsNo(register.getPartsNo());
        account.setNameplateNo(register.getNameplateNo());
        account.setIdentificationCode(register.getIdentificationCode());
        account.setMadeFactoryIdx(register.getMadeFactoryIdx());
        account.setMadeFactoryName(register.getMadeFactoryName());
        account.setFactoryDate(register.getFactoryDate());
        account.setConfigDetail(register.getConfigDetail());
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        account.setManageDeptId(register.getAcceptDeptId());
        account.setManageDept(register.getAcceptDept());//责任部门为接收部门
        account.setManageDeptType(register.getAcceptDeptType());//责任部门类型
        //责任部门类型为【机构】
        if(PartsAccount.MANAGE_DEPT_TYPE_ORG == register.getAcceptDeptType()){
            account.setManageDeptOrgseq(register.getAcceptDeptOrgSeq());//责任部门序列
        }else if(PartsAccount.MANAGE_DEPT_TYPE_WH == register.getAcceptDeptType()){  //责任部门类型为【库房】
            account.setManageDeptOrgseq(null);//责任部门序列为空
        }
        //配件状态为页面上选择的良好或者待交验
        if(StringUtil.isNullOrBlank(register.getPartsStatus())){
            account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, PartsAccount.PARTS_STATUS_LH_CH));
        }else{
            account.setPartsStatus(register.getPartsStatus());
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", register.getPartsStatus(), register.getPartsStatusName()));
        }
        account.setPartsStatusUpdateDate(register.getAcceptTime());//配件状态更新时间为接收日期
        
        //如果为新购，则为新品，否则是旧品
        if(WellPartsRegister.SOURCE_NEW.equals(register.getSource())){
            account.setIsNewParts(PartsAccount.IS_NEW_PARTS_YES);//是否新品---新
            account.setSource("新购");
        }else{
            account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
            account.setSource("调入");
        }
        account.setLocation(register.getLocationName());//存放地址
        account.setIdx(null);
        account.setCreateTime(new Date());
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
        register.setPartsAccountIdx(account.getIdx());
        return register;
    }
    
    /**
     * <li>说明：良好入库时，新增良好配件库存信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 良好配件入库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void savePartsStock(WellPartsRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
                PartsStock stock = new PartsStock();
                BeanUtils.copyProperties(stock, register);
                stock = EntityUtil.setSysinfo(stock);
                //设置逻辑删除字段状态为未删除
                stock = EntityUtil.setNoDelete(stock);
                stock.setWhLocationName(register.getLocationName());//库房
                stock.setPartsAccountIDX(register.getPartsAccountIdx());//配件信息主键
                stock.setWhDate(register.getAcceptTime());//接收时间
                stock.setIdx(null);
                stock.setCreateTime(new Date());
                this.daoUtils.getHibernateTemplate().saveOrUpdate(stock);
    }
    
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销的良好配件登记主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWellRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
              WellPartsRegister register = getModelById(id);
              if(null != register){
                  PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIdx());// 取得配件信息实体
                  List<PartsManageLog> logList = partsManageLogManager.getLogListByIdx(register.getPartsAccountIdx()) ;
                  PartsManageLog log = new PartsManageLog();
                  if(null != logList && logList.size() > 0){
                      log = logList.get(0); //获取最新的日志记录
                      //操作类型不为【良好登记】，则表示此配件已进行了下一步操作，不能撤销
                      if(!log.getEventType().equals(PartsManageLog.EVENT_TYPE_LHDJ) || Constants.DELETED == account.getRecordStatus()){
                          throw new BusinessException("配件已进入下一环节，不能撤销！");
                      }else{
                          this.logicDelete(id);    //删除良好配件登记信息
                          partsAccountManager.logicDelete(register.getPartsAccountIdx());    //删除周转台账信息
                          partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                      }
                  }
              }else {
                  throw new BusinessException("数据有误！");
            }
  }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修改良好配件登记信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 良好配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWellParts(WellPartsRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIdx());// 取得配件信息实体
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || Constants.DELETED == account.getRecordStatus()){
            throw new BusinessException("只有【良好】的配件才能修改！");
        }
        //修改配件周转台账
        updateAccount(register,account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(register);
    }
    
    /**
     * <li>说明：修改配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 良好配件信息
     * @param account 配件台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(WellPartsRegister register,PartsAccount account) throws BusinessException, NoSuchFieldException {
        account.setPartsTypeIDX(register.getPartsTypeIdx());
        account.setSpecificationModel(register.getSpecificationModel());
        account.setPartsName(register.getPartsName());
        account.setPartsNo(register.getPartsNo());
        account.setIdentificationCode(register.getIdentificationCode());
        account.setMadeFactoryIdx(register.getMadeFactoryIdx());
        account.setMadeFactoryName(register.getMadeFactoryName());
        account.setFactoryDate(register.getFactoryDate());
        account.setConfigDetail(register.getConfigDetail());
        if(null != register.getAcceptDeptId()){
            account.setManageDeptId(register.getAcceptDeptId());
        }
        account.setManageDept(register.getAcceptDept());//责任部门为接收部门
        account.setManageDeptType(register.getAcceptDeptType());//责任部门类型
        account.setManageDeptOrgseq(register.getAcceptDeptOrgSeq());//责任部门序列
        account.setPartsStatusUpdateDate(register.getAcceptTime());//配件状态更新时间为接收日期
        account.setLocation(register.getLocationName());//存放地址
        account.setCreateTime(new Date());
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
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
//        account.setPartsStatus(PartsAccount.PARTS_STATUS_FZC) ; //查询【非在册】状态的周转信息
        PartsAccount entity = this.partsAccountManager.getAccount(account);
        if(null == entity){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode("");
            entity = this.partsAccountManager.getAccount(account);
        }
        if (null != entity && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_FZC) ) {
            throw new BusinessException("此配件已登记，不能重复登记！");
        }else if(null == entity || entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_FZC)){
            return entity;
        }
        return null ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【非在册】配件周转台账信息列表
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
      if(null == entityList){
          account.setPartsNo(account.getIdentificationCode());
          account.setIdentificationCode("");
          entityList = this.partsAccountManager.getAccountList(account);
      }
      if(null != entityList && entityList.size() > 0){
          for(PartsAccount entity : entityList){
              if (null != entity && !entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_FZC) ) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("此配件已登记，不能重复登记！");
          }
      }
      return null ;
  }
    /**
     * <li>说明：良好配件登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 良好配件登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWellPartsRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<WellPartsRegister> wellPartsRegisterList = new ArrayList<WellPartsRegister>();
        WellPartsRegister wellPartsRegister;
        for (String id : ids) {
            wellPartsRegister = new WellPartsRegister();
            wellPartsRegister = getModelById(id);
            if(null != wellPartsRegister){
                wellPartsRegister.setStatus(PjwzConstants.STATUS_CHECKED);//良好配件登记状态为已确认
                wellPartsRegisterList.add(wellPartsRegister);
            }
        }
        this.saveOrUpdate(wellPartsRegisterList);
    }
    
}