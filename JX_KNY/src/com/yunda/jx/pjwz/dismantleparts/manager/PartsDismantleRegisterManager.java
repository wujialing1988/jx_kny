package com.yunda.jx.pjwz.dismantleparts.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.dismantleparts.entity.PartsDismantleRegister;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsDismantleRegister业务类,配件拆卸登记
 * <li>创建人：程梅
 * <li>创建日期：2016-01-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsDismantleRegisterManager")
public class PartsDismantleRegisterManager extends JXBaseManager<PartsDismantleRegister, PartsDismantleRegister>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsRdpManager业务类,配件检修作业计划 */
    @Resource
    private PartsRdpManager partsRdpManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /**
     * <li>说明：根据过滤条件查询配件兑现单列表
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2016-01-15
     * <li>修改内容：主配件的状态应为“检修中”和“待验收”的配件可以进行配件上下（安装、拆卸）配件。
     * <li>修改人：程梅
     * <li>修改日期：2016-03-01
     * <li>修改内容：主配件的状态应为“检修中”的配件可以进行配件上下（安装、拆卸）配件。
     * <li>修改人：程梅
     * <li>修改日期：2016-04-20
     * <li>修改内容：主配件的状态应为“检修中”和“待验收”的配件可以进行配件上下（安装、拆卸）配件。
     * @param searchEntity 过滤条件
     * @return Page<PartsRdp> 列表分页对象
     * @throws BusinessException
     */
    public Page<PartsRdp> findPartsRdpList(final SearchEntity<PartsRdp> searchEntity) throws BusinessException{
        PartsRdp rdp = searchEntity.getEntity() ;
        StringBuilder selectSql = new StringBuilder("select t.*,(select count(r.idx) from PJWZ_PARTS_DISMANTLE_REGISTER r where r.RECORD_STATUS=0 and r.rdp_idx = t.IDX and r.status = '20') ")
        .append("|| '/' || (select count(u.idx) from PJWZ_PARTS_DISMANTLE_REGISTER u where u.RECORD_STATUS=0 and u.rdp_idx = t.IDX) as \"num\" ");
        StringBuilder fromSql = new StringBuilder(" from PJJX_Parts_Rdp t where t.RECORD_STATUS=0 and t.status in ('").append(PartsRdp.STATUS_JXZ).append("','").append(PartsRdp.STATUS_DYS).append("') ");
        StringBuilder awhere =  new StringBuilder();
        String unloadTrainNo = rdp.getUnloadTrainNo() ;
        if(!StringUtil.isNullOrBlank(unloadTrainNo)){
            awhere.append(" and (t.UNLOAD_TRAINTYPE like '%").append(unloadTrainNo).append("%' or t.UNLOAD_TRAINNO like '%").append(unloadTrainNo)
            .append("%' or t.UNLOAD_REPAIR_CLASS like '%").append(unloadTrainNo).append("%' or t.IDENTIFICATION_CODE like '%").append(unloadTrainNo)
            .append("%' or t.PARTS_NO like '%").append(unloadTrainNo).append("%' or t.PARTS_NAME like '%").append(unloadTrainNo)
            .append("%' or t.SPECIFICATION_MODEL like '%").append(unloadTrainNo).append("%' ) ") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.Real_StartTime DESC");
        }
        StringBuilder totalSql = new StringBuilder("select count(*) ").append(fromSql).append(awhere);
        StringBuilder sql = selectSql.append(fromSql).append(awhere);
        return partsRdpManager.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }
    
    /**
     * <li>说明：根据过滤条件查询配件拆卸登记列表
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsDismantleRegister> 列表分页对象
     * @throws BusinessException
     */
    public Page<PartsDismantleRegister> findPageList(final SearchEntity<PartsDismantleRegister> searchEntity) throws BusinessException{
        PartsDismantleRegister register = searchEntity.getEntity() ;
        StringBuffer selectSql = null;
        StringBuffer fromSql = null;
        StringBuffer sql = null;
        String identificationCode = register.getIdentificationCode() ;
        //未登帐列表
        if(PjwzConstants.STATUS_WAIT.equals(register.getStatus())){
            selectSql = new StringBuffer("select t.* ");
            fromSql = new StringBuffer(" from PJWZ_PARTS_DISMANTLE_REGISTER t where t.record_Status=0 and t.status='")
            .append(PjwzConstants.STATUS_WAIT).append("' and t.RDP_IDX='").append(register.getRdpIdx()).append("' ");
            if(!StringUtil.isNullOrBlank(identificationCode)){
                fromSql.append(" and (t.parts_name like '%").append(identificationCode).append("%' or t.specification_model like '%")
                .append(identificationCode).append("%' or t.location like '%").append(identificationCode).append("%' )") ;
            }
        }else if(PjwzConstants.STATUS_ED.equals(register.getStatus())){   //已登帐列表
            selectSql = new StringBuffer("select t.*,a.Parts_Status,a.Parts_Status_Name ");
            fromSql = new StringBuffer(" from PJWZ_PARTS_DISMANTLE_REGISTER t,PJWZ_PARTS_ACCOUNT a where t.PARTS_ACCOUNT_IDX=a.idx and t.record_Status=0 and a.record_Status=0 and t.status='")
            .append(PjwzConstants.STATUS_ED).append("' and t.RDP_IDX='").append(register.getRdpIdx()).append("' ");
            if(!StringUtil.isNullOrBlank(identificationCode)){
                fromSql.append(" and (t.parts_no like '%").append(identificationCode).append("%' or t.parts_name like '%").append(identificationCode).append("%' or t.specification_model like '%")
                .append(identificationCode).append("%' or t.location like '%").append(identificationCode).append("%' or a.Parts_Status_Name like '%")
                .append(identificationCode).append("%' or t.IDENTIFICATION_CODE like '%").append(identificationCode).append("%' )") ;
            }
        }
        StringBuffer totalSql = new StringBuffer("select count(*) ").append(fromSql);
        sql = selectSql.append(fromSql).append(" order by t.UNLOAD_DATE desc,t.UPDATE_TIME desc ");
        return super.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }
    
    /**
     * <li>说明：保存配件拆卸信息（范围内拆卸为更新数据，范围外拆卸为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 配件拆卸信息
     * @throws BusinessException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public void savePartsDismantleRegister(PartsDismantleRegister register) throws BusinessException, IllegalAccessException, InvocationTargetException, NoSuchFieldException  {
        // 配件识别码唯一验证
        if(null != partsAccountManager.getAccountByIdCode(register.getIdentificationCode())){
            throw new BusinessException("配件识别码【"+register.getIdentificationCode()+"】已存在，不能重复添加！");
        }
        // 配件编号+配件规格型号判断唯一性【在册状态中唯一】验证
        PartsAccount account = new PartsAccount();
        account.setPartsNo(register.getPartsNo());
        account.setSpecificationModel(register.getSpecificationModel());
        account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC) ;
        account = partsAccountManager.getAccount(account);
        if(null != account && account.getIdx().equalsIgnoreCase(register.getPartsAccountIDX())){
            throw new BusinessException("配件编号【"+register.getPartsNo()+"】,规格型号【" + register.getSpecificationModel() + "】已存在，不能重复添加！");
        }
        
        // 新增到配件周转台账表中
        account = saveAccount(register);
        register.setPartsAccountIDX(account.getIdx());
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        // id为空，表示是范围外下车
        if(StringUtil.isNullOrBlank(register.getIdx())){
            register.setIsInRange(PartsDismantleRegister.IS_IN_RANGE_NO);   // 是否范围内下车 - 否
        } else {
            register.setIsInRange(PartsDismantleRegister.IS_IN_RANGE_YES);  // 是否范围内下车 - 是
        }
        this.saveOrUpdate(register);
        
        //日志描述=下车车型+下车车号+修程+接收部门+存放位置
        String logDesc = register.getUnloadTrainType() + register.getUnloadTrainNo() + " " + register.getUnloadRepairClass();
        if(!StringUtil.isNullOrBlank(register.getTakeOverDept())) {
            logDesc += " " + register.getTakeOverDept();
        }
        if(!StringUtil.isNullOrBlank(register.getLocation())) {
            logDesc += " " + register.getLocation();
        }
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJCX, logDesc);
        log.partsAccountIdx(register.getPartsAccountIDX());
        log.eventIdx(register.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：新增数据到配件台账中【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 配件拆卸信息 
     * @return 配件信息（·配件周转台账）实体
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public PartsAccount saveAccount(PartsDismantleRegister register) throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        PartsAccount account = new PartsAccount();
        // 配件兑现单信息
        PartsRdp rdp = partsRdpManager.getModelById(register.getRdpIdx());
        register.setParentPartsAccountIDX(rdp.getPartsAccountIDX());
        register.setUnloadTrainTypeIdx(rdp.getUnloadTrainTypeIdx());
        register.setUnloadTrainType(rdp.getUnloadTrainType());
        register.setUnloadTrainNo(rdp.getUnloadTrainNo());
        register.setUnloadRepairClass(rdp.getUnloadRepairClass());
        register.setUnloadRepairClassIdx(rdp.getUnloadRepairClassIdx());
        register.setUnloadRepairTime(rdp.getUnloadRepairTime());
        register.setUnloadRepairTimeIdx(rdp.getUnloadRepairTimeIdx());
        // 将值赋给配件周转台账
        BeanUtils.copyProperties(account, register);
        if(null != register.getTakeOverDeptId()){
            account.setManageDeptId(register.getTakeOverDeptId().toString());
        }
        account.setManageDept(register.getTakeOverDept());                  // 责任部门为接收部门
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);       // 责任部门类型为机构
        account.setManageDeptOrgseq(register.getTakeOverDeptOrgseq());      // 责任部门序列为接收部门序列
        account.setPartsStatusUpdateDate(register.getTakeOverTime());       // 配件状态更新时间为收件日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);               // 配件状态为待修
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, "待修"));
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);                // 是否新品---旧
        account.setSource("拆卸");
        account.setIdx(null);
        this.partsAccountManager.saveOrUpdate(account);
        return account;
    }
    
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsDismantleRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsDismantleRegister register = getModelById(id);
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX()); // 取得配件信息实体
        if (null == account || StringUtil.isNullOrBlank(register.getIsInRange())) {
            throw new BusinessException("数据异常，请刷新后重试！");
        }
        if (!PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()) {
            throw new BusinessException("只有【待修】状态的配件才能撤销！");
        }
        partsAccountManager.logicDelete(register.getPartsAccountIDX()); // 删除周转台账信息
        // 如果是范围内拆卸的数据，则清除拆卸配件信息，并删除配件周转台帐。
        if (PartsDismantleRegister.IS_IN_RANGE_YES.equals(register.getIsInRange())) {
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
            this.saveOrUpdate(register); // 清除配件拆卸信息
        }
        // 如果是范围外拆卸的数据，则删除配件拆卸信息，并删除配件周转台帐。
        if (PartsDismantleRegister.IS_IN_RANGE_NO.equals(register.getIsInRange())) {
            this.logicDelete(id); // 删除配件拆卸信息
        }
        partsManageLogManager.deleteLogByEventIdx(id);                  // 删除日志信息
    }
    
    /**
     * <li>说明：修改配件拆卸信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 配件拆卸信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsDismantleRegister(PartsDismantleRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX());    // 取得配件信息实体
        if (null == account) {
           throw new BusinessException("数据异常，请刷新后重试！");
        }
        if (!PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus())) {
            throw new BusinessException("只有【待修】状态的配件才能修改！");
        }
        // 修改配件周转信息
        updateAccount(register, account);
        register.setStatus(PjwzConstants.STATUS_ED);// 单据状态为已登帐
        this.saveOrUpdate(register);
    }
    
    /**
     * <li>说明：修改配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @param account 配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsDismantleRegister register,PartsAccount account) throws BusinessException, NoSuchFieldException {
        try {
            BeanUtils.copyProperties(account, register);
        } catch (Throwable e) {
            throw new BusinessException(e);
        }
        if(null != register.getTakeOverDeptId()){
            account.setManageDeptId(register.getTakeOverDeptId().toString());
        }
        account.setManageDept(register.getTakeOverDept());//责任部门为接收部门
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDeptOrgseq(register.getTakeOverDeptOrgseq());//责任部门序列为接收部门序列
        account.setPartsStatusUpdateDate(register.getTakeOverTime());//配件状态更新时间为收件日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);//配件状态为待修
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, "待修"));
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//是否新品---旧
        account.setSource("拆卸");
        account.setIdx(register.getPartsAccountIDX());
        this.partsAccountManager.saveOrUpdate(account);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param account 查询实体
     * @return PartsAccount 实体对象
     */
    @SuppressWarnings("unchecked")
    public PartsAccount getPartsAccount(PartsAccount account) {
//        account.setPartsStatus(PartsAccount.PARTS_STATUS_FZC) ; //查询【非在册】状态的周转信息
        return this.partsAccountManager.getAccount(account);
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
    /**
     * <li>说明：根据上级配件主键查询已登帐的拆卸信息列表
     * <li>创建人：程梅
     * <li>创建日期：2016-4-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentPartsAccountIDX 作业节点主键
     * @return List<PartsDismantleRegister> 拆卸list
     */
    @SuppressWarnings({ "unused", "unchecked" })
    public List<PartsDismantleRegister> getListByParentAccountIdx(String parentPartsAccountIDX) {
        String hql =
            "From PartsDismantleRegister Where parentPartsAccountIDX = ? And recordStatus = 0 and status = ? ";
        return this.daoUtils.find(hql, new Object[] { parentPartsAccountIDX, PjwzConstants.STATUS_ED});
    }
}