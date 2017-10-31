package com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.manager;

import java.lang.reflect.InvocationTargetException;
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
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.wellpartsmanage.repairedpartswh.entity.PartsWHRegister;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.PartsStock;
import com.yunda.util.BeanUtils;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsWHRegister业务类，修竣配件入库单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value="partsWHRegisterManager")
public class PartsWHRegisterManager extends JXBaseManager<PartsWHRegister, PartsWHRegister>{
	
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager;
    
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
     * @return Page<PartsWHRegister> 分页对象
     * @throws BusinessException
     */
    public Page<PartsWHRegister> findPageList(SearchEntity<PartsWHRegister> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsWHRegister t where t.recordStatus=0 ");
        PartsWHRegister register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.whName like '%").append(identificationCode)
            .append("%' or t.handOverOrg like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
//        if(!StringUtil.isNullOrBlank(whTime)){
//            awhere.append(" and substr(to_char(t.whTime,'yyyy-MM-dd'),0,7) = '" + whTime + "'");
//        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.whTime desc,t.updateTime desc");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：修竣配件入库登记前验证配件编号+规格型号唯一性【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 需被验证的入库对象
     * @return String 错误提示
     */
    @SuppressWarnings("unused")
    public String validateByPartsNo(PartsWHRegister register) {
        if (null == register) {
            return new String("数据异常");
        }
        String error = "";
        String hql = "select a From PartsAccount a where a.recordStatus=0 and a.partsNo='"+register.getPartsNo()+"' and a.partsTypeIDX='" +register.getPartsTypeIDX()+
        "' and a.partsStatus like '"+PartsAccount.PARTS_STATUS_ZC+"%' and a.idx <> '"+register.getPartsAccountIdx()+"'";
        List list = daoUtils.find(hql);
        if(list != null && list.size()>0){
            error = "配件编号"+register.getPartsNo()+"已存在，不能重复添加！";
        }
        return error;
    }
    
    /**
     * <li>说明：修竣配件入库登记前验证配件识别码唯一性【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 需被验证的入库对象
     * @return String 错误提示
     */
    @SuppressWarnings("unused")
    public String validateByIdentificationCode(PartsWHRegister register) {
        if (null == register) {
            return new String("数据异常");
        }
        String error = "";
        String hql = "select a From PartsAccount a where a.recordStatus=0 and a.identificationCode='"+register.getIdentificationCode()+"' " +
        "and a.partsStatus like '"+PartsAccount.PARTS_STATUS_ZC+"%' and a.idx <> '"+register.getPartsAccountIdx()+"'";
        List list = daoUtils.find(hql);
        if(list != null && list.size()>0){
            error = "配件识别码"+register.getIdentificationCode()+"已存在，不能重复添加！";
        }
        return error;
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：新增修竣配件入库登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-12-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 入库信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @SuppressWarnings("unchecked")
    public void savePartsWHRegister(PartsWHRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException{
        // 检验数据是否异常，判断配件编号+规格型号唯一性
        String errorPartsNo = this.validateByPartsNo(register);
        if (!StringUtil.isNullOrBlank(errorPartsNo)) {
            throw new BusinessException(errorPartsNo);
        }
        //检验数据是否异常，判断配件识别码唯一性
        String errorIden = this.validateByIdentificationCode(register);
        if (!StringUtil.isNullOrBlank(errorIden)) {
            throw new BusinessException(errorIden);
        }
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIdx());// 取得配件信息实体
//        if(account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZK) || account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_FZC)){
        //良好不在库、委外、修竣状态的配件能修竣入库
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
//        if((!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_WWX) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_XJ)){
//            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能修竣入库，请重新添加！");
//        }
        //V3.2.8版本，在册（排除在修）、责任部门为机构的配件可以入库
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG) || account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能入库，请重新添加！");
        }
        //获取当前登录操作员
        OmEmployee emp = SystemContext.getOmEmployee();
        if (null != emp) {
            register.setCreatorName(emp.getEmpname());
            register.setTakeOverEmpId(Integer.parseInt(emp.getEmpid().toString()));
            register.setTakeOverEmp(emp.getEmpname());  
        }
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        register.setWhTime(new Date());//交件日期为当前日期
        this.saveOrUpdate(register);
        if(null != account){
            //日志描述=接收库房+存放位置
            String logDesc = "";
            if(!StringUtil.isNullOrBlank(register.getWhName())) logDesc = register.getWhName();
            if(!StringUtil.isNullOrBlank(register.getLocationName())) logDesc = logDesc + " " + register.getLocationName();
            PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_XJRK, logDesc);
            log = partsManageLogManager.initLog(log, account);
            
            account.setManageDeptId(register.getWhIdx());  //责任部门id为接收库房id
            account.setManageDept(register.getWhName()); //责任部门名称为接收库房名称
            account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_WH);//责任部门类型为库房
            account.setManageDeptOrgseq("");//责任部门序列为空
            account.setPartsStatusUpdateDate(register.getWhTime());//配件状态更新日期为交件日期【入库日期】
            //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
            if(StringUtil.isNullOrBlank(register.getPartsStatus())){
                account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好在库
                account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, "良好"));
            }else{
                account.setPartsStatus(register.getPartsStatus());
                account.setPartsStatusName(register.getPartsStatusName());
            }
            if(!register.getPartsNo().equals(account.getPartsNo())){
                account.setOldPartsNo(account.getPartsNo());
                account.setPartsNo(register.getPartsNo());
            }
            if(StringUtil.isNullOrBlank(register.getLocationName())){
                account.setLocation(register.getWhName());//存放地点=库房
            }else {
                account.setLocation(register.getWhName()+"("+register.getLocationName()+")");//存放地点=库房+库位
            }
            account.setConfigDetail(register.getConfigDetail());//详细配置
            account = EntityUtil.setSysinfo(account);
            this.partsAccountManager.saveOrUpdate(account);
            //新增良好配件库存信息
            savePartsStock(register);
            
            log.eventIdx(register.getIdx());
            partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
        }
    }
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修竣配件入库时，新增良好配件库存信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 修竣配件入库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void savePartsStock(PartsWHRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
            PartsStock stock = new PartsStock();
            BeanUtils.copyProperties(stock, register);
            stock = EntityUtil.setSysinfo(stock);
            //设置逻辑删除字段状态为未删除
            stock = EntityUtil.setNoDelete(stock);
            stock.setWhLocationName(register.getLocationName());//库房
            stock.setPartsAccountIDX(register.getPartsAccountIdx());//配件信息主键
            stock.setWhDate(register.getWhTime());//入库时间
            stock.setIdx(null);
            stock.setCreateTime(new Date());
            this.daoUtils.getHibernateTemplate().saveOrUpdate(stock);
    }
    
        
    
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：撤销修竣配件入库登记
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销的修竣配件入库登记主键
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWHRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
            PartsWHRegister register = getModelById(id);
            if(null != register){
                    PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIdx()); //查询该配件周转台账信息
                    //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
                    if((!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DX) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_DBF)) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
                        //throw new BusinessException("只有【良好】、【待修】、【待报废】的在库配件才能撤销！");
                        throw new BusinessException("不在库的配件不能撤销！");
                    }else{
                        //根据配件id和委外登记id查询配件日志信息
                        PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                        //配件状态回滚到修竣配件入库登记前
                        account = partsManageLogManager.getAccountFromLog(log, account);
                        partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                        this.logicDelete(id);    //删除修竣配件入库登记信息
                        partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                    }
                    
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修改修竣配件入库登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 需保存的入库信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public void updatePartsWHRegister(PartsWHRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIdx());// 取得配件信息实体
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
            throw new BusinessException("只有【良好在库】的配件才能修改，请重新添加！");
        }
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        //获取当前登录操作员
        OmEmployee emp = SystemContext.getOmEmployee();
        if (null != emp) {
            register.setCreatorName(emp.getEmpname());
            register.setTakeOverEmpId(Integer.parseInt(emp.getEmpid().toString()));
            register.setTakeOverEmp(emp.getEmpname());  
        }
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        this.daoUtils.getHibernateTemplate().saveOrUpdate(register);
        if(null != account){
            account.setManageDeptId(register.getWhIdx());  //责任部门id为接收库房id
            account.setManageDept(register.getWhName()); //责任部门名称为接收库房名称
            account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_WH);//责任部门类型为库房
            account.setManageDeptOrgseq("");//责任部门序列为空
            account.setPartsStatusUpdateDate(register.getWhTime());//配件状态更新日期为交件日期【入库日期】
            account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好在库
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, "良好"));
            if(!register.getPartsNo().equals(account.getPartsNo())){
                account.setOldPartsNo(account.getPartsNo());
                account.setPartsNo(register.getPartsNo());
            }
            if(StringUtil.isNullOrBlank(register.getLocationName())){
                account.setLocation(register.getWhName());//存放地点=库房
            }else {
                account.setLocation(register.getWhName()+"("+register.getLocationName()+")");//存放地点=库房+库位
            }
            account.setConfigDetail(register.getConfigDetail());//详细配置
            account = EntityUtil.setSysinfo(account);
            this.partsAccountManager.saveOrUpdate(account);
        }
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询最新的【除在库以外的在册】配件周转台账信息（除在库和非在册外）
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
        //良好不在库、修竣状态的配件能修竣入库
        //因有了委外回段模块，所以委外配件不能再修竣入库
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
//        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)) && !accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_XJ)){
//            throw new BusinessException("只有良好不在库、修竣状态的配件才能修竣入库，请重新添加！");
//        }
        //V3.2.8版本，在册（排除在修）、责任部门为机构的配件可以入库
        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG))){
            throw new BusinessException("在库的配件不能入库，请重新添加！");
        }else if(null != accountV && accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
            throw new BusinessException("在修的配件不能入库，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【除在库、在修以外的在册】配件周转台账信息列表
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
              if (null != entity && (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !entity.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG))) {
                  entityList.remove(entity);
              }else if(null != entity && entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("除【在库】和【在修】以外的在册配件才能入库，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修竣配件入库登记【web端/手持终端批量入库】
     * <li>创建人：程梅
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 修竣配件入库信息数组
     * @return 错误信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void savePartsWHRegisterBatch(PartsWHRegister[] registers) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        for (PartsWHRegister register : registers) {
            savePartsWHRegister(register);
        }
    }
    /**
     * <li>说明：修竣配件入库登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 修竣配件入库登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsWHRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsWHRegister> partsWHRegisterList = new ArrayList<PartsWHRegister>();
        PartsWHRegister partsWHRegister;
        for (String id : ids) {
            partsWHRegister = new PartsWHRegister();
            partsWHRegister = getModelById(id);
            if(null != partsWHRegister){
                partsWHRegister.setStatus(PjwzConstants.STATUS_CHECKED);//修竣配件入库登记状态为已确认
                partsWHRegisterList.add(partsWHRegister);
            }
        }
        this.saveOrUpdate(partsWHRegisterList);
    }
    
}

