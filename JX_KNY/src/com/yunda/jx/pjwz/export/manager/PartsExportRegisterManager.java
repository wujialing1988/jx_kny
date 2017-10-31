package com.yunda.jx.pjwz.export.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.export.entity.PartsExportRegister;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsExportRegister业务类,配件调出登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsExportRegisterManager")
public class PartsExportRegisterManager extends JXBaseManager<PartsExportRegister, PartsExportRegister>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** 配件管理日志业务类*/
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param searchEntity 过滤条件
     * @return Page<PartsExportRegister> 分页对象
     * @throws BusinessException
     */
    public Page<PartsExportRegister> findPageList(SearchEntity<PartsExportRegister> searchEntity) throws BusinessException {
        StringBuilder sb = new StringBuilder("From PartsExportRegister t Where t.recordStatus=0 ");
        PartsExportRegister register = searchEntity.getEntity();
        String ic = register.getIdentificationCode();
        if (!StringUtil.isNullOrBlank(ic)) {
            sb.append(" And (t.partsNo like '%").append(ic).append("%' or t.partsName like '%").append(ic)
                .append("%' or t.specificationModel like '%").append(ic).append("%' or t.acceptDep like '%").append(
                    ic).append("%' or t.identificationCode like '%").append(ic).append("%' )");
        }
        Order[] orders = searchEntity.getOrders();
        if (null != orders && orders.length > 0) {
            sb.append(HqlUtil.getOrderHql(orders));
        } else {
            sb.append(" order by t.exportDate DESC");
        }
        String totalHql = "Select Count(*) As rowcount " + sb.substring(sb.indexOf("From"));
        return this.findPageList(totalHql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：保存配件调出登记信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 配件调出信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsExport(PartsExportRegister register) throws BusinessException, NoSuchFieldException {
        // 查询该配件周转台账信息
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); 
        if (null == account) {
            throw new BusinessException("配件编号为" + register.getPartsNo() + "的配件不存在，请重新添加！");
        }
        if (!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)) {
            throw new BusinessException("【" + account.getPartsStatusName() + "】的配件不能调出，请重新添加！");
        }
        //构造日志信息
        PartsManageLog log = initLog(register, account);
        // 更新配件周转台账
        updateAccount(register, account);
        if (null == register.getStatus()) {
            register.setStatus(PjwzConstants.STATUS_ED);    // 单据状态为已登帐
        }
        this.saveOrUpdate(register);
        log.setEventIdx(register.getIdx());
        // 保存配件周转日志信息
        partsManageLogManager.saveOrUpdate(log);// 新增配件管理日志
    }
    
    /**
     * <li>说明：构造日志信息
     * <li>创建人：何涛
     * <li>创建日期：2016-1-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件调出信息
     * @param account 配件信息（·配件周转台账）
     * @throws NoSuchFieldException
     */
    private PartsManageLog initLog(PartsExportRegister register, PartsAccount account) throws NoSuchFieldException {
        // 日志描述=接收单位
        String logDesc = register.getAcceptDep();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJDC, logDesc);
        log = partsManageLogManager.initLog(log, account);
        return log;
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 配件调出信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void updateAccount(PartsExportRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
        //设置责任部门信息和存放地点
        account.setManageDeptId(register.getAcceptDepCode());  //责任部门id为接收单位编码
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDept(register.getAcceptDep()); //责任部门名称为接收单位
        account.setManageDeptOrgseq("");//责任部门序列为空
        account.setLocation("");//存放地点为空
        account.setPartsStatusUpdateDate(register.getExportDate());//配件状态更新时间为调出日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_DC);//配件状态为调出
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DC, "调出"));
//        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
        this.partsAccountManager.saveOrUpdate(account);
    }
    
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2015-10-13
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param id 配件调出登记单idx主键
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsExportForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsExportRegister register = getModelById(id);
        if (null == register || StringUtil.isNullOrBlank(register.getPartsAccountIDX())) {
            throw new BusinessException("数据异常，请刷新后重试！");
        }
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); // 查询该配件周转台账信息
        if (null == account) {
            throw new BusinessException("数据异常，请刷新后重试！");
        }
        if (!PartsAccount.PARTS_STATUS_DC.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()) {
            throw new BusinessException("只有【已调出】状态的配件才能撤销！");
        }
        this.logicDelete(id); // 删除配件调出信息
        
        // 根据配件id和调出登记id查询配件日志信息
        PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id);
        // 配件状态回滚到调出前
        account = partsManageLogManager.getAccountFromLog(log, account);
        // 更新周转台账信息
        partsAccountManager.saveOrUpdate(account); 
        // 删除日志信息
        partsManageLogManager.deleteLogByEventIdx(id);
    }
    
    /**
     * <li>说明：修改配件调出登记信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 配件调出信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsExport(PartsExportRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        if(!PartsAccount.PARTS_STATUS_DC.equals(account.getPartsStatus())){
            throw new BusinessException("只有【已调出】状态的配件才能修改，请重新添加！");
        }
        //更新配件周转台账
        updateAccount(register , account);
        
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        this.saveOrUpdate(register);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【在册】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param account 查询实体
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getPartsAccount(PartsAccount account) {
//        account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC) ; //查询【在册】状态的周转信息
        PartsAccount entity = this.partsAccountManager.getAccount(account);
        if(null == entity){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode("");
            entity = this.partsAccountManager.getAccount(account);
        }
        if (null == entity) {
            throw new BusinessException("此配件未登记！");
        }
        //良好配件才能调出
        if (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)) {
            throw new BusinessException("良好配件才能调出，当前状态为【"+entity.getPartsStatusName()+"】，请重新添加！");
        }
        return entity;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【良好】配件周转台账信息列表
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
              if (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("【良好】配件才能调出，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
	/**
     * <li>说明：保存配件调出信息【web端】
     * <li>创建人：王利成
     * <li>创建日期：2015-11-10
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param exportParts 配件调出信息
     * @param details 配件信息
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchFieldException 
	 * @throws BusinessException 
     */
	public void savePartsExportRegister(PartsExportRegister exportParts, PartsAccount[] details) throws IllegalAccessException,
        InvocationTargetException, BusinessException, NoSuchFieldException {
        if (null == details || details.length <= 0) {
            throw new BusinessException("没有可以保存的数据！");
        }
        PartsExportRegister register = null;
        for (PartsAccount account : details) {
            register = new PartsExportRegister();
            BeanUtils.copyProperties(register, exportParts);
            register.setPartsName(account.getPartsName());
            register.setPartsNo(account.getPartsNo());
            register.setPartsTypeIDX(account.getPartsTypeIDX());
            register.setPartsAccountIDX(account.getIdx());
            register.setIdentificationCode(account.getIdentificationCode());
            register.setSpecificationModel(account.getSpecificationModel());
            register.setExportDate(exportParts.getExportDate());
            register.setAcceptDepCode(exportParts.getAcceptDepCode());
            register.setAcceptDep(exportParts.getAcceptDep());
            register.setExportOrder(exportParts.getExportOrder());
            register.setUnloadTrainTypeIdx(account.getUnloadTrainTypeIdx());
            register.setUnloadTrainType(account.getUnloadTrainType());
            register.setUnloadTrainNo(account.getUnloadTrainNo());
            register.setUnloadRepairClassIdx(account.getUnloadRepairClassIdx());
            register.setUnloadRepairClass(account.getUnloadRepairClass());
            register.setUnloadRepairTimeIdx(account.getUnloadRepairTimeIdx());
            register.setUnloadRepairTime(account.getUnloadRepairTime());
            register.setIsNewParts(account.getIsNewParts());
            this.savePartsExport(register);
        }
    }
    
    /**
     * <li>说明：配件调出登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param ids 配件调出登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsExportRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsExportRegister> entityList = new ArrayList<PartsExportRegister>();
        PartsExportRegister register;
        for (String id : ids) {
            register = getModelById(id);
            if (null == register) {
                continue;
            }
            register.setStatus(PjwzConstants.STATUS_CHECKED);// 配件调出登记状态为已确认
            entityList.add(register);
        }
        this.saveOrUpdate(entityList);
    }
    
}