package com.yunda.zb.pjdj.unloadparts.manage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.util.BeanUtils;
import com.yunda.zb.pjdj.unloadparts.entity.ZbPartsUnloadRegister;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.zbglrdpmanage.entity.VZbglRdp;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbPartsUnloadRegister业务类,配件下车登记单
 * <li>创建人：黄杨
 * <li>创建日期：2016-09-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbPartsUnloadRegisterManager")
public class ZbPartsUnloadRegisterManager extends JXBaseManager<ZbPartsUnloadRegister, ZbPartsUnloadRegister>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** 整备单业务类,机车检修作业计划 */
    @Resource
    private ZbglRdpManager zbglRdpManager ;   
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
       
    /**
     * <li>说明：保存下车配件信息（范围内下车为更新数据，范围外下车为新增数据）【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param register 下车配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void savePartsUnloadRegister(ZbPartsUnloadRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        PartsAccount account = new PartsAccount();
        account.setPartsNo(register.getPartsNo());
        account.setSpecificationModel(register.getSpecificationModel());
        account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC);
        // 配件识别码唯一
        if (null != partsAccountManager.getAccountByIdCode(register.getIdentificationCode())) {
            throw new BusinessException("配件识别码【" + register.getIdentificationCode() + "】已存在，不能重复添加！");
        }
        // 配件编号+配件规格型号判断唯一性【在册状态中唯一】
        account = partsAccountManager.getAccount(account);
        if (null != account && !account.getIdx().equals(register.getPartsAccountIDX())) {
            throw new BusinessException("配件编号【" + register.getPartsNo() + "】,规格型号【" + register.getSpecificationModel() + "】已存在，不能重复添加！");
        }
        // 新增到配件周转台账表中
        account = saveAccount(register);
        if(null  == register.getTakeOverEmpId()){
            OmEmployee emp = SystemContext.getOmEmployee();
            if (null != emp) {
                register.setTakeOverEmpId(emp.getEmpid());
                register.setTakeOverEmp(emp.getEmpname());
            }
        }
        register.setPartsAccountIDX(account.getIdx());
        register.setStatus(PjwzConstants.STATUS_ED);// 单据状态为已登帐
        
        // id为空，表示是范围外下车
        if (StringUtil.isNullOrBlank(register.getIdx())) {
            register.setIsInRange(ZbPartsUnloadRegister.IS_IN_RANGE_NO); // 是否范围内下车---否
        } else {
            register.setIsInRange(ZbPartsUnloadRegister.IS_IN_RANGE_YES); // 是否范围内下车---是
        }
        // 如果rdpIdx为空则状态为戒备
        register.setJbStatus(!StringUtil.isNullOrBlank(register.getRdpIdx()) ? ZbPartsUnloadRegister.JB_STATUS_YES : ZbPartsUnloadRegister.JB_STATUS_NO);
        // 检修任务单类型为【机车】
        register.setRdpType("机车");              
        this.saveOrUpdate(register);
        
        // 保存配件周转信息日志
        saveLog(register);
    }
    
    /**
     * <li>说明：保存配件周转信息日志
     * <li>创建人：黄杨
     * <li>创建日期：2016-09-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param register 下车配件信息
     * @throws NoSuchFieldException
     */
    private void saveLog(ZbPartsUnloadRegister register) throws NoSuchFieldException {
        // 日志描述=下车车型+下车车号+修程+接收部门+存放位置
        StringBuilder sb = new StringBuilder();
        sb.append(register.getUnloadTrainType());
        sb.append(register.getUnloadTrainNo());
        sb.append(" ");
        sb.append(register.getUnloadRepairClass());
        if (!StringUtil.isNullOrBlank(register.getTakeOverDept())) {
            sb.append(" ").append(register.getTakeOverDept());
        }
        if (!StringUtil.isNullOrBlank(register.getLocation())) {
            sb.append(" ").append(register.getLocation());
        }
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_XCDJ, sb.toString());
        log.partsAccountIdx(register.getPartsAccountIDX());
        log.eventIdx(register.getIdx());
        // 新增配件管理日志
        partsManageLogManager.saveOrUpdate(log);
    }
    
    /**
     * <li>说明：新增数据到配件台账中【用于手持终端】
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * @param register 下车配件信息
     * @return 配件信息（·配件周转台账）
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PartsAccount saveAccount(ZbPartsUnloadRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException,
        InvocationTargetException {
        PartsAccount account = new PartsAccount();
        // 机车兑现单信息
        // 如果是解备提票，则传入rdpIdx为空，此时不需再拿车型车号信息
        if(!StringUtil.isNullOrBlank(register.getRdpIdx())){
            ZbglRdp rdp = zbglRdpManager.getModelById(register.getRdpIdx());
            register.setUnloadTrainTypeIdx(rdp.getTrainTypeIDX());
            register.setUnloadTrainType(rdp.getTrainTypeShortName());
            register.setUnloadTrainNo(rdp.getTrainNo());
        }
        // 将值赋给配件周转台账
        BeanUtils.copyProperties(account, register);
        if (null != register.getTakeOverDeptId()) {
            account.setManageDeptId(register.getTakeOverDeptId().toString());
        }
        account.setManageDept(register.getTakeOverDept());// 责任部门为接收部门
        account.setManageDeptType(register.getTakeOverDeptType());// 责任部门类型为机构 修改by wujl 责任部门为页面传过来的类型
        account.setManageDeptOrgseq(register.getTakeOverDeptOrgseq());// 责任部门序列为接收部门序列
        account.setPartsStatusUpdateDate(register.getTakeOverTime());// 配件状态更新时间为收件日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);// 配件状态为待修
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, "待修"));
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);// 是否新品---旧
        account.setSource("下车");
        account.setIdx(null);
        this.partsAccountManager.saveOrUpdate(account);
        return account;
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
    public void updateUnloadRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
        ZbPartsUnloadRegister register = getModelById(id);
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX());// 取得配件信息实体
        if (null == account || null == register.getIsInRange()) {
            throw new BusinessException("数据异常，请刷新后重试！");
        }
        if (!PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus()) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG) || Constants.DELETED == account.getRecordStatus()) {
            throw new BusinessException("只有【待修】且不在库的配件才能撤销！");
        }
        // 删除周转台账信息
        partsAccountManager.logicDelete(register.getPartsAccountIDX());     
        // 如果是范围内下车的数据，则清除下车配件信息，并删除配件周转台帐。
        if (ZbPartsUnloadRegister.IS_IN_RANGE_YES.equals(register.getIsInRange())) {
            register.setTakeOverDept("");
            register.setTakeOverDeptId(null);
            register.setTakeOverDeptOrgseq("");
            register.setTakeOverDeptType(null);// 接收部门类型
            register.setTakeOverEmp("");
            register.setTakeOverEmpId(null);
            register.setTakeOverTime(null);
            register.setHandOverEmp("");
            register.setHandOverEmpId(null);
            register.setPartsAccountIDX("");
            register.setMadeFactoryIdx("");
            register.setMadeFactoryName("");
            register.setUnloadDate(null);
            register.setUnloadReason("");
            register.setLocation("");
            register.setIdentificationCode("");
            register.setFactoryDate(null);
            register.setConfigDetail("");
            register.setStatus(PjwzConstants.STATUS_WAIT); // 单据状态为【未登帐】
            this.saveOrUpdate(register); // 清除下车配件信息
        }
        // 如果是范围外下车的数据，则删除下车配件信息，并删除配件周转台帐。
        if (ZbPartsUnloadRegister.IS_IN_RANGE_NO.equals(register.getIsInRange())) {
            this.logicDelete(id); // 删除下车配件信息
        }
        partsManageLogManager.deleteLogByEventIdx(id);// 删除日志信息
    }
    

    /**
     * <li>说明：重写整备下车登记查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询参数
     * @return Page<ZbPartsUnloadRegister> 整备下车登记列表
     * @throws BusinessException
     */
    public Page<ZbPartsUnloadRegister> findPageList(final SearchEntity<ZbPartsUnloadRegister> searchEntity) throws BusinessException{
        ZbPartsUnloadRegister register = searchEntity.getEntity() ;
        StringBuffer selectSql = null;
        StringBuffer fromSql = null;
        StringBuffer sql = null;
        String identificationCode = register.getIdentificationCode() ;
        selectSql = new StringBuffer("select t.*,a.Parts_Status,a.Parts_Status_Name ");
        fromSql = new StringBuffer(" from ZB_PARTS_UNLOAD_REGISTER t,PJWZ_PARTS_ACCOUNT a where t.PARTS_ACCOUNT_IDX=a.idx and t.record_Status=0 ");
        fromSql.append(" and t.jb_status = '"+register.getJbStatus()+"' ");
        if(!StringUtil.isNullOrBlank(register.getRdpIdx())){
            fromSql.append(" and t.RDP_IDX='").append(register.getRdpIdx()).append("' ");
        }
        if(!StringUtil.isNullOrBlank(identificationCode)){
            fromSql.append(" and (t.parts_no like '%").append(identificationCode).append("%' or t.parts_name like '%").append(identificationCode).append("%' or t.specification_model like '%")
            .append(identificationCode).append("%' or t.location like '%").append(identificationCode).append("%' or a.Parts_Status_Name like '%")
            .append(identificationCode).append("%' or t.UNLOAD_TRAINTYPE like '%").append(identificationCode).append("%' or t.UNLOAD_TRAINNO like '%")
            .append(identificationCode).append("%' or t.IDENTIFICATION_CODE like '%").append(identificationCode).append("%' )") ;
        }
        StringBuffer totalSql = new StringBuffer("select count(*) ").append(fromSql);
        sql = selectSql.append(fromSql).append(" order by t.UNLOAD_DATE desc,t.UPDATE_TIME desc ");
        return super.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }

    /**
     * <li>说明：根据所选机车查询其下的配件登记情况
     * <li>创建人：黄杨
     * <li>创建日期：2016-9-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param zbglRdp 整备清单实例对象
     * @return 登记实例集合
     */
	public List registerDetailsUnderOne(VZbglRdp zbglRdp) {
		String hql = "From ZbPartsUnloadRegister where rdpIdx = ? " + zbglRdp.getIdx();
		int count = daoUtils.getCount(hql);
		//如果是解备则按照下车车辆信息查询
		if(count == 0){
			String newHql = " From ZbPartsUnloadRegister where rdpIdx is null";
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
}