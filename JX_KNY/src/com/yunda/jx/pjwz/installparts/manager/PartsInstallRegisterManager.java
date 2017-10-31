package com.yunda.jx.pjwz.installparts.manager;

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
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.installparts.entity.PartsInstallRegister;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsInstallRegister业务类,配件安装登记单
 * <li>创建人：程梅
 * <li>创建日期：2016-01-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsInstallRegisterManager")
public class PartsInstallRegisterManager extends JXBaseManager<PartsInstallRegister, PartsInstallRegister> {
    
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager;
    
    /** 配件管理日志业务类 */
    @Resource
    private PartsManageLogManager partsManageLogManager;
    
    /** 配件检修作业计划业务类 */
    @Resource
    private PartsRdpManager partsRdpManager;
    
    /**
     * <li>说明：根据过滤条件查询配件兑现单列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-2
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
    public Page<PartsRdp> findPartsRdpList(final SearchEntity<PartsRdp> searchEntity) throws BusinessException {
        PartsRdp rdp = searchEntity.getEntity();
        StringBuilder selectSql =
            new StringBuilder(
                "select t.*,(select count(r.idx) from PJWZ_Parts_Install_Register r where r.RECORD_STATUS=0 and r.rdp_idx = t.IDX and r.status = '20') ")
                .append("|| '/' || (select count(u.idx) from PJWZ_Parts_Install_Register u where u.RECORD_STATUS=0 and u.rdp_idx = t.IDX) as \"num\" ");
        StringBuilder fromSql =
            new StringBuilder(" from PJJX_Parts_Rdp t where t.RECORD_STATUS=0 and t.status in ('").append(PartsRdp.STATUS_JXZ).append("','").append(PartsRdp.STATUS_DYS).append("') ");
        StringBuilder awhere = new StringBuilder();
        String unloadTrainNo = rdp.getUnloadTrainNo();
        if (!StringUtil.isNullOrBlank(unloadTrainNo)) {
            awhere.append(" and (t.UNLOAD_TRAINTYPE like '%").append(unloadTrainNo).append("%' or t.UNLOAD_TRAINNO like '%").append(unloadTrainNo)
                .append("%' or t.UNLOAD_REPAIR_CLASS like '%").append(unloadTrainNo).append("%' or t.IDENTIFICATION_CODE like '%").append(
                    unloadTrainNo).append("%' or t.PARTS_NO like '%").append(unloadTrainNo).append("%' or t.PARTS_NAME like '%")
                .append(unloadTrainNo).append("%' or t.SPECIFICATION_MODEL like '%").append(unloadTrainNo).append("%' ) ");
        }
        Order[] orders = searchEntity.getOrders();
        if (orders != null && orders.length > 0) {
            awhere.append(HqlUtil.getOrderHql(orders));
        } else {
            awhere.append(" order by t.Real_StartTime DESC");
        }
        StringBuilder totalSql = new StringBuilder("select count(*) ").append(fromSql).append(awhere);
        StringBuilder sql = selectSql.append(fromSql).append(awhere);
        return partsRdpManager.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(), null, searchEntity.getOrders());
    }
    
    /**
     * <li>说明：分页查询
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return 分页对象
     * @throws BusinessException
     */
    public Page<PartsInstallRegister> findPageList(SearchEntity<PartsInstallRegister> searchEntity) throws BusinessException {
        String totalHql = "select count(*) from PartsInstallRegister t where t.recordStatus=0 ";
        String hql = " From PartsInstallRegister t where t.recordStatus=0 ";
        PartsInstallRegister register = searchEntity.getEntity();
        String identificationCode = register.getIdentificationCode();
        StringBuffer awhere = new StringBuffer();
        // 未登帐列表
        if (PjwzConstants.STATUS_WAIT.equals(register.getStatus())) {
            awhere.append(" and t.status='").append(PjwzConstants.STATUS_WAIT).append("' and t.rdpIdx='").append(register.getRdpIdx()).append("' ");
            if (!StringUtil.isNullOrBlank(identificationCode)) {
                awhere.append(" and (t.partsName like '%").append(identificationCode).append("%' or t.specificationModel like '%").append(
                    identificationCode).append("%' or t.aboardPlace like '%").append(identificationCode).append("%' )");
            }
        } else if (PjwzConstants.STATUS_ED.equals(register.getStatus())) { // 已登帐列表
            awhere.append(" and t.status='").append(PjwzConstants.STATUS_ED).append("' and t.rdpIdx='").append(register.getRdpIdx()).append("' ");
            if (!StringUtil.isNullOrBlank(identificationCode)) {
                awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
                    .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.aboardPlace like '%").append(
                        identificationCode).append("%' or t.identificationCode like '%").append(identificationCode).append("%' )");
            }
        }
        // if(!StringUtil.isNullOrBlank(acceptTime)){
        // awhere.append(" and substr(to_char(t.acceptTime,'yyyy-MM-dd'),0,7) = '" + acceptTime + "'");
        // }
        Order[] orders = searchEntity.getOrders();
        if (orders != null && orders.length > 0) {
            awhere.append(HqlUtil.getOrderHql(orders));
        } else {
            awhere.append(" order by t.updateTime DESC");
        }
        totalHql += awhere;
        hql += awhere;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：保存配件安装信息（范围内安装为更新数据，范围外安装为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件安装信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsInstallRegister(PartsInstallRegister register) throws BusinessException, NoSuchFieldException {
        // 查询该配件周转台账信息
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX());
        if (null == account) {
            throw new BusinessException("数据异常，目标配件不存在！");
        }
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)){
            throw new BusinessException("只有【" + PartsAccount.PARTS_STATUS_LH_CH + "】状态的配件才能安装！");
        }
        //保存新增配件管理日志，日志描述=安装位置
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJAZ, register.getAboardPlace());
        log = partsManageLogManager.initLog(log, account);
        // 更新配件周转台账
        updateAccount(register, account);
        
        // 保存配件安装登记单
        register.setStatus(PjwzConstants.STATUS_ED);// 单据状态为已登帐
        // idx为空，表示是范围外安装
        if (StringUtil.isNullOrBlank(register.getIdx())) {
            register.setIsInRange(PartsInstallRegister.IS_IN_RANGE_NO);// 是否范围内下车---否
        }
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null)
            register.setCreatorName(emp.getEmpname());
        this.saveOrUpdate(register);
        log.eventIdx(register.getIdx());
        partsManageLogManager.saveOrUpdate(log);
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件安装信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsInstallRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
        account.setAboardPlace(register.getAboardPlace());
        account.setAboardDate(register.getAboardDate());
        // 清除责任部门信息
        account.setManageDeptId(""); // 责任部门id
        account.setManageDept(""); // 责任部门名称
        account.setManageDeptType(null); // 责任部门类型
        account.setManageDeptOrgseq(""); // 责任部门序列为空
        account.setLocation(register.getAboardPlace()); // 存放地点为安装位置
        account.setPartsStatusUpdateDate(new Date()); // 配件状态更新时间为当前日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_YSPJ); // 配件状态为已上配件
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_YSPJ,
            PartsAccount.PARTS_STATUS_YSPJ_CH));
        this.partsAccountManager.saveOrUpdate(account);
    }
    
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsInstallRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsInstallRegister register = getModelById(id);
        if (null == register) {
            throw new BusinessException("数据异常！未查询到id(" + id + ")的配件安装登记单");
        }
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); // 查询该配件周转台账信息
        if (!PartsAccount.PARTS_STATUS_YSPJ.equals(account.getPartsStatus()) || Constants.DELETED == account.getRecordStatus()) {
            throw new BusinessException("只有【" + PartsAccount.PARTS_STATUS_YSPJ_CH + "】状态的配件才能撤销！");
        } 
        
        // 根据配件id和安装配件登记id查询配件日志信息
        PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id);
        if (null != log) {
            // 配件状态回滚到安装登记前
            account = partsManageLogManager.getAccountFromLog(log, account);
        }
        // 清除安装记录
        account.setAboardTrainTypeIdx(null);
        account.setAboardTrainType(null);
        account.setAboardTrainNo(null);
        account.setAboardRepairTimeIdx(null);
        account.setAboardRepairTime(null);
        account.setAboardRepairClassIdx(null);
        account.setAboardRepairClass(null);
        account.setAboardPlace(null);
        account.setAboardDate(null);
        partsAccountManager.saveOrUpdate(account); // 更新周转台账信息
        
        // 如果是范围内安装的数据，则清除安装配件信息，并清除配件周转台帐中的安装信息
        if (null == register.getIsInRange()) {
            throw new BusinessException("数据异常！IS_IN_RANGE为空");
        }
        if (PartsUnloadRegister.IS_IN_RANGE_YES.equals(register.getIsInRange())) {
            // 清除安装记录
            register.setAboardPlace("");
            register.setAboardDate(null);
            register.setStatus(PjwzConstants.STATUS_WAIT); // 单据状态为【未登帐】
            this.saveOrUpdate(register); // 清除安装配件信息
        } else if (PartsUnloadRegister.IS_IN_RANGE_NO.equals(register.getIsInRange())) {
            // 如果是范围外安装的数据，则删除安装配件信息，并清除配件周转台帐的安装信息
            this.logicDelete(id); // 删除配件安装登记信息
        }
        partsManageLogManager.deleteLogByEventIdx(id);  // 删除日志信息
            
    }
    
    /**
     * <li>说明：修改安装配件信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件安装信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsInstallRegister(PartsInstallRegister register) throws BusinessException, NoSuchFieldException {
        // 查询该配件周转台账信息
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX());
        if (null == account) {
            throw new BusinessException("数据异常，目标配件不存在！");
        }
        if (!PartsAccount.PARTS_STATUS_YSPJ.equals(account.getPartsStatus())) {
            throw new BusinessException("当前配件状态为：" + account.getPartsStatusName() + "， 不能进行修改！");
        }
        // 更新配件周转台账
        updateAccount(register, account);
        
        // 保存配件安装登记单
        PartsInstallRegister entity = this.getModelById(register.getIdx());
        entity.setStatus(PjwzConstants.STATUS_ED);// 单据状态为已登帐
        this.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【良好不在库】配件周转台账信息
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
        // 首先根据“配件识别码”查询配件信息
        PartsAccount accountV = this.partsAccountManager.getAccount(account);
        //如果根据“配件识别码”查询不到配件信息，则再根据“配件编号”进行二次查询
        if(null == accountV){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode("");
            accountV = this.partsAccountManager.getAccount(account);
        }
        if (null == accountV) {
            throw new BusinessException("此配件未登记！");
        }
        //良好不在库状态的配件能安装
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)){
            throw new BusinessException("只有【良好】状态的配件才能安装，请重新添加！");
        }
        return accountV ;
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
              throw new BusinessException("只有【良好】状态的配件才能安装，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * <li>说明：安装配件登记【web端】
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：2015-01-16
     * <li>修改内容：增加批量保存时对单个保存实体的验证
     * @param registers 安装配件登记信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsInstallRegisterBatch(PartsInstallRegister[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsInstallRegister register : registers) {
            String[] validateMsg = this.validateUpdate(register);
            if (null != validateMsg) {
                throw new BusinessException(validateMsg[0]);
            }
            savePartsInstallRegister(register);
        }
    }
    
    /**
     * <li>说明：配件安装登记确认
     * <li>创建人：程梅
     * <li>创建日期：2016-01-08
     * <li>修改人：何涛
     * <li>修改日期：增加异常处理
     * <li>修改内容：2016-01-16
     * @param ids 配件安装登记单id数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsInstallRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsInstallRegister> registerList = new ArrayList<PartsInstallRegister>(ids.length);
        PartsInstallRegister register;
        for (String id : ids) {
            register = getModelById(id);
            if (null == register) {
                throw new BusinessException("数据异常，未查询到配件安装登记单信息！idx:" + id);
            }
            if (PjwzConstants.STATUS_CHECKED.equals(register.getStatus())) {
                throw new BusinessException("配件安装登记单已被确认，请刷新后重试！idx:" + id);
            }
            // 安装配件登记状态为已确认
            register.setStatus(PjwzConstants.STATUS_CHECKED);
            registerList.add(register);
        }
        this.saveOrUpdate(registerList);
    }
    
}
