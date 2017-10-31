package com.yunda.zb.pjdj.fixparts.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.fixparts.entity.PartsFixRegister;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.zb.pjdj.fixparts.entity.ZbPartsFixRegister;
import com.yunda.zb.pjdj.unloadparts.entity.ZbPartsUnloadRegister;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.zbglrdpmanage.entity.VZbglRdp;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbPartsFixRegister业务类,上车配件登记单
 * <li>创建人：黄杨
 * <li>创建日期：2016-9-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbPartsFixRegisterManager")
public class ZbPartsFixRegisterManager extends JXBaseManager<ZbPartsFixRegister, ZbPartsFixRegister>{
    
	//配件信息业务类
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** 配件管理日志业务类*/
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /** 整备单业务类,机车检修作业计划 */
    @Resource
    private ZbglRdpManager zbglRdpManager ;   
    
    /**
     * <li>说明：保存配件上车信息（范围内上车为更新数据，范围外上车为新增数据）【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param register 上车配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void savePartsFixRegister(ZbPartsFixRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
    	PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        // 2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        // 20160906 by wujl 此处添加配置项，神木配件上车时，配件不需要先进行出库操作
        boolean isPartsStatusConfig = "true".equalsIgnoreCase(SystemConfigUtil.getValue(PartsFixRegister.IS_PARTS_STATUS_CONFIG));
        if(isPartsStatusConfig){
            if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)){
                throw new BusinessException("只有【良好】的配件才能上车，请重新添加！");
            }
        }else{
            if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)){
                throw new BusinessException("只有【良好不在库】的配件才能上车，请重新添加！");
            }
        }
        //日志描述=上车车型+上车车号+修程
        String logDesc = register.getAboardTrainType() + register.getAboardTrainNo() + " " + register.getAboardRepairClass();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJSC, logDesc);
        log = partsManageLogManager.initLog(log, account);
        //更新配件周转台账
        updateAccount(register , account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        // id为空，表示是范围外上车
        if(StringUtil.isNullOrBlank(register.getIdx())){
            register.setIsInRange(ZbPartsFixRegister.IS_IN_RANGE_NO);// 是否范围内下车---否
        }
        register.setRdpType("机车") ;//检修任务单类型为【机车】
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) register.setCreatorName(emp.getEmpname());
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        // 如果rdpIdx为空则状态为戒备
        register.setJbStatus(!StringUtil.isNullOrBlank(register.getRdpIdx()) ? ZbPartsUnloadRegister.JB_STATUS_YES : ZbPartsUnloadRegister.JB_STATUS_NO);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(register);
        log.eventIdx(register.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 上车配件信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(ZbPartsFixRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
       //上车信息为空就从机车兑现单里查询
        if(!StringUtil.isNullOrBlank(register.getRdpIdx())){
            //机车兑现单信息
            ZbglRdp rdp = zbglRdpManager.getModelById(register.getRdpIdx());
            register.setAboardTrainTypeIdx(rdp.getTrainTypeIDX());
            register.setAboardTrainType(rdp.getTrainTypeShortName());
            register.setAboardTrainNo(rdp.getTrainNo());
        }
        account.setAboardTrainTypeIdx(register.getAboardTrainTypeIdx());
        account.setAboardTrainType(register.getAboardTrainType());
        account.setAboardTrainNo(register.getAboardTrainNo());
        account.setAboardRepairTimeIdx(register.getAboardRepairTimeIdx());
        account.setAboardRepairTime(register.getAboardRepairTime());
        account.setAboardRepairClassIdx(register.getAboardRepairClassIdx());
        account.setAboardRepairClass(register.getAboardRepairClass());
        account.setAboardPlace(register.getAboardPlace());
        account.setAboardDate(register.getAboardDate());
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        //清除责任部门信息
        account.setManageDeptId("");  //责任部门id
        account.setManageDept(""); //责任部门名称
        account.setManageDeptType(null);//责任部门类型
        account.setManageDeptOrgseq("");//责任部门序列为空
        account.setLocation(register.getAboardPlace());//存放地点为上车位置
        account.setPartsStatusUpdateDate(new Date());//配件状态更新时间为当前日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_YSC);//配件状态为已上车
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_YSC, "已上车"));
        account.setAboardRdpIDX(register.getRdpIdx());
        account.setAboadRdpType(register.getRdpType());
//        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
    }
    
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateFixRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
        ZbPartsFixRegister register = getModelById(id);
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
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
        
        if(!PartsAccount.PARTS_STATUS_YSC.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()){
            throw new BusinessException("只有【已上车】状态的配件才能撤销！");
        }else if(null != accountV){
            throw new BusinessException("已有相同的配件存在，不能撤销！");
        }else{
            //清除上车记录
            account.setAboardTrainTypeIdx("");
            account.setAboardTrainType("");
            account.setAboardTrainNo("");
            account.setAboardRepairTimeIdx("");
            account.setAboardRepairTime("");
            account.setAboardRepairClassIdx("");
            account.setAboardRepairClass("");
            account.setAboardPlace("");
            account.setAboardDate(null);
            //根据配件id和上车配件登记id查询配件日志信息
            PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
            //配件状态回滚到上车登记前
            account = partsManageLogManager.getAccountFromLog(log, account);
            partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
            //如果是范围内上车的数据，则清除上车配件信息，并清除配件周转台帐中的上车信息
            if(ZbPartsFixRegister.IS_IN_RANGE_YES.equals(register.getIsInRange())){
                //清除上车记录
                register.setAboardTrainTypeIdx("");
                register.setAboardTrainType("");
                register.setAboardTrainNo("");
                register.setAboardRepairTimeIdx("");
                register.setAboardRepairTime("");
                register.setAboardRepairClassIdx("");
                register.setAboardRepairClass("");
                register.setAboardPlace("");
                register.setAboardDate(null);
                register.setStatus(PjwzConstants.STATUS_WAIT);  //单据状态为【未登帐】
                this.saveOrUpdate(register);   //清除上车配件信息
                partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
              }else if(ZbPartsFixRegister.IS_IN_RANGE_NO.equals(register.getIsInRange())){
                  //如果是范围外下车的数据，则删除上车配件信息，并清除配件周转台帐的上车信息
                  this.logicDelete(id);    //删除配件上车登记信息
                  partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    }


    /**
     * <li>说明：根据所选机车查询其下的配件登记情况
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglRdp 整备清单实例对象
     * @return 登记实例分页集合
     */
	public List registerDetailsUnderOne(VZbglRdp zbglRdp) {
		String hql = "From ZbPartsFixRegister where rdpIdx = ? " + zbglRdp.getIdx();
		int count = daoUtils.getCount(hql);
		//如果是解备则按照下车车辆信息查询
		if(count == 0){
			String newHql = " From ZbPartsFixRegister where rdpIdx is null";
			return daoUtils.find(newHql);
		}
		return daoUtils.find(hql);
	}
	
	   /**
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getPartsAccount(PartsAccount account) {
        PartsAccount pa = this.partsAccountManager.getAccount(account);
        if(null == pa){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode(null);
            pa = this.partsAccountManager.getAccount(account);
        }
        if (null == pa) {
            throw new BusinessException("此配件未登记！");
        }
        // 2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        // 20160906 by wujl 此处添加配置项，神木配件上车时，配件不需要先进行出库操作
        boolean isPartsStatusConfig = "true".equalsIgnoreCase(SystemConfigUtil.getValue(PartsFixRegister.IS_PARTS_STATUS_CONFIG));
        if(isPartsStatusConfig){
            if (!pa.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)) {
                throw new BusinessException("只有【良好】的配件才能上车！");
            }
        }else{
            if (!pa.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !pa.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)) {
                throw new BusinessException("只有【良好不在库】的配件才能上车！");
            }
        }
        return pa;
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
      
      boolean isPartsStatusConfig = "true".equalsIgnoreCase(SystemConfigUtil.getValue(PartsFixRegister.IS_PARTS_STATUS_CONFIG));
      if(null != entityList && entityList.size() > 0){
          // 先将list进行复制 然后再遍历
          List<PartsAccount> entityListV = new ArrayList<PartsAccount>();
          entityListV.addAll(entityList);
          for(PartsAccount entity : entityListV){
              if(isPartsStatusConfig){
                  if (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)) {
                      entityList.remove(entity);
                  }
              }else{
                  if (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !entity.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)) {
                      entityList.remove(entity);
                  }
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              String errMsg = isPartsStatusConfig ? "只有【良好】的配件才能上车！" : "只有【良好不在库】的配件才能上车！";
              throw new BusinessException(errMsg);
          }
      }else throw new BusinessException("此配件未登记！");
  }
    
}