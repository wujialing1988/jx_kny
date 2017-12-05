package com.yunda.jx.pjwz.fixparts.manager;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.entity.EquipPart;
import com.yunda.jx.component.manager.EquipPartManager;
import com.yunda.jx.pjjx.workplan.entity.TrainWorkPlanView;
import com.yunda.jx.pjjx.workplan.manager.TrainWorkPlanViewManager;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.fixparts.entity.PartsAboardRegisterBean;
import com.yunda.jx.pjwz.fixparts.entity.PartsFixRegister;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.turnover.entity.OffPartList;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnRegisterVo;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister;
import com.yunda.jxpz.utils.SystemConfigUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsFixRegister业务类,上车配件登记单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsFixRegisterManager")
public class PartsFixRegisterManager extends JXBaseManager<PartsFixRegister, PartsFixRegister>{
    
	//配件信息业务类
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** 配件管理日志业务类*/
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /** 机车检修作业计划视图业务类 */
    @Resource
    private TrainWorkPlanViewManager trainWorkPlanViewManager ;
    
    /** EquipPart业务类,配件下车位置 */
    @Resource
    private EquipPartManager equipPartManager ;
    
    /** PartsTypeManager业务类,规格型号 */
    @Resource
    private PartsTypeManager partsTypeManager ;
    
    /**
     * <li>说明：根据过滤条件查询机车兑现单列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<TrainWorkPlanView> 列表分页对象
     * @throws BusinessException
     */
    public Page<TrainWorkPlanView> findWorkPlanList(final SearchEntity<TrainWorkPlanView> searchEntity) throws BusinessException{
        TrainWorkPlanView plan = searchEntity.getEntity() ;
        StringBuilder selectSql = new StringBuilder("select t.*,(select count(*) from jxgc_off_parts_list l where l.work_plan_idx = t.IDX and not exists ");
        selectSql.append(" ( select * from pjwz_parts_fix_register ur ");
        selectSql.append(" left join PJWZ_Parts_Type pt on ur.parts_type_idx = pt.idx ");
        selectSql.append(" where ur.is_in_range = '是' and ur.record_status = 0 and ");
        selectSql.append("  (ur.Rdp_Idx = l.work_plan_idx AND  l.wzmc is not null and pt.jcpjbm = l.parts_idx and ur.aboard_place = l.wzmc ) ");
        selectSql.append(" or ( ur.Rdp_Idx = l.work_plan_idx AND l.wzmc is null and ur.aboard_place is null and pt.jcpjbm = l.parts_idx ) ");
        selectSql.append("  )) ");
        selectSql.append("  || '/' || (select count(u.idx) from pjwz_parts_fix_register u where u.RECORD_STATUS=0 and u.rdp_idx = t.IDX) as \"num\" ");
        StringBuffer fromSql = new StringBuffer(" from V_JXGC_TRAIN_WORK_PLAN t where t.RECORD_STATUS=0 and t.WORK_PLAN_STATUS != '").append(TrainWorkPlanView.STATUS_NULLIFY).append("' ");
        StringBuffer awhere =  new StringBuffer();
        String trainNo = plan.getTrainNo() ;
        if(!StringUtil.isNullOrBlank(trainNo)){
            awhere.append(" and (t.Train_Type_ShortName like '%").append(trainNo).append("%' or t.Train_No like '%").append(trainNo)
            .append("%' or t.Repair_Class_Name like '%").append(trainNo).append("%' ) ") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.Begin_Time DESC");
        }
        StringBuilder totalSql = new StringBuilder("select count(*) ").append(fromSql).append(awhere);
        StringBuilder sql = selectSql.append(fromSql).append(awhere);
        return trainWorkPlanViewManager.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }
    
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
    public Page<PartsFixRegister> findPageList(SearchEntity<PartsFixRegister> searchEntity) throws BusinessException{
        String totalHql = "select count(*) from PartsFixRegister t where t.recordStatus=0 ";
        String hql = " From PartsFixRegister t where t.recordStatus=0 ";
        PartsFixRegister register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        //未登帐列表
        if(PjwzConstants.STATUS_WAIT.equals(register.getStatus())){
            awhere.append(" and t.status='").append(PjwzConstants.STATUS_WAIT).append("' and t.rdpIdx='").append(register.getRdpIdx()).append("' ");
            if(!StringUtil.isNullOrBlank(identificationCode)){
                awhere.append(" and (t.partsName like '%").append(identificationCode)
                .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.aboardPlace like '%")
                .append(identificationCode).append("%' )") ;
            }
        }else if(PjwzConstants.STATUS_ED.equals(register.getStatus())){   //已登帐列表
            awhere.append(" and t.status='").append(PjwzConstants.STATUS_ED).append("' and t.rdpIdx='").append(register.getRdpIdx()).append("' ");
            if(!StringUtil.isNullOrBlank(identificationCode)){
                awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
                .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.aboardPlace like '%").append(identificationCode)
                .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
            }
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
     * <li>说明：上车配件登记-分支（通过配件规格型号【是否额外放行字段】判断，如果“是”，则转入新的保存方法；否则调用之前的方法）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsFixRegisterBranch(PartsFixRegister register) throws BusinessException, NoSuchFieldException {
        if(StringUtil.isNullOrBlank(register.getPartsTypeIDX())){
            throw new BusinessException("规格型号不能为空！");
        }
        PartsType partsType = partsTypeManager.getModelById(register.getPartsTypeIDX());
        if(partsType == null){
            throw new BusinessException("规格型号不存在！");
        }
        // 是否按照额外放行的方式进行保存
        if(partsType.getIsExtra() == Constants.YES){
            
        }else{
            this.savePartsFixRegister(register);
        }
    }
    
    /**
     * <li>说明：额外放行方式保存：先查看有没有PartsAccount
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsFixRegisterExtra(PartsFixRegister register) throws BusinessException, NoSuchFieldException {
        
    }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：保存上车配件信息（范围内上车为更新数据，范围外上车为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件上车信息
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsFixRegister(PartsFixRegister register) throws BusinessException, NoSuchFieldException {
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
        
        // 判断上车位置是否已经被选择，不能选择重复的上车位置，设置上车位置编码
        if(!StringUtil.isNullOrBlank(register.getAboardPlace())){
            // 先把前后空格去掉
            register.setAboardPlace(register.getAboardPlace().trim());
            boolean flag = isExistUnloadPlace(register);
            if(flag){
                throw new BusinessException("上车位置【" + register.getAboardPlace() + "】已存在，请重新选择！");
            }
            // 找到位置名称对应的位置编码
            EquipPart ep = equipPartManager.getEquipPartByName(register.getAboardPlace());
            register.setAboardPlaceCode(ep == null ? "" :ep.getPartId());
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
            register.setIsInRange(PartsFixRegister.IS_IN_RANGE_NO);// 是否范围内下车---否
        }
        register.setRdpType("机车") ;//检修任务单类型为【机车】
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) register.setCreatorName(emp.getEmpname());
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(register);
        log.eventIdx(register.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    
    /**
     * <li>说明：判断同位置的下车配件登记是否存在
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 查询参数
     * @return boolean true:存在 false:不存在
     */
    @SuppressWarnings("unchecked")
    private Boolean isExistUnloadPlace(PartsFixRegister entity){
        boolean flag = false ;
        String haveUploadPlaces = getHaveAboardPlaces(entity.getPartsTypeIDX(), entity.getRdpIdx());
        if(!StringUtil.isNullOrBlank(haveUploadPlaces)){
            String[] hups = haveUploadPlaces.split(",");
            List<String> list = Arrays.asList(hups);
            if(list != null && list.contains("'"+entity.getAboardPlace()+"'")){
                flag = true ;
            }
        }
        return flag ;
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 上车配件信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsFixRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
       //上车信息为空就从机车兑现单里查询
        if(StringUtil.isNullOrBlank(register.getAboardTrainTypeIdx())){
            //机车兑现单信息
            TrainWorkPlanView rdp = trainWorkPlanViewManager.getModelById(register.getRdpIdx());
            register.setAboardTrainTypeIdx(rdp.getTrainTypeIdx());
            register.setAboardTrainType(rdp.getTrainTypeShortName());
            register.setAboardTrainNo(rdp.getTrainNo());
            register.setAboardRepairClass(rdp.getRepairClassName());
            register.setAboardRepairClassIdx(rdp.getRepairClassIdx());
            register.setAboardRepairTime(rdp.getRepairtimeName());
            register.setAboardRepairTimeIdx(rdp.getRepairtimeIdx());           
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
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2015-10-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateFixRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
            PartsFixRegister register = getModelById(id);
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
                if(PartsFixRegister.IS_IN_RANGE_YES.equals(register.getIsInRange())){
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
                  }else if(PartsFixRegister.IS_IN_RANGE_NO.equals(register.getIsInRange())){
                      //如果是范围外下车的数据，则删除上车配件信息，并清除配件周转台帐的上车信息
                      this.logicDelete(id);    //删除配件上车登记信息
                      partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                  }else {
                      throw new BusinessException("数据有误！");
                  }
            }
        }
    
    /**
     * <li>说明：修改上车配件信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件上车信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsFixRegister(PartsFixRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        if(!PartsAccount.PARTS_STATUS_YSC.equals(account.getPartsStatus())){
            throw new BusinessException("只有【已上车】状态的配件才能修改！");
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
     * <li>说明：根据配件编号和规格型号查询最新的【良好不在库】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-27
     * <li>修改人：何涛
     * <li>修改日期：2016-04-08
     * <li>修改内容：审查规范：代码分层，action层、或者webservice接口方法，只用于接收参数，将业务逻辑处理放到manager。
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
    public List<PartsAccount> getPartsAccountList(PartsAccount account,String jcpjbm) {
      List<PartsAccount> entityList = this.partsAccountManager.getAccountList(account);
      if(null == entityList || entityList.size() == 0){
          account.setPartsNo(account.getIdentificationCode());
          account.setIdentificationCode("");
          entityList = this.partsAccountManager.getAccountList(account);
      }
      
      //boolean isPartsStatusConfig = "true".equalsIgnoreCase(SystemConfigUtil.getValue(PartsFixRegister.IS_PARTS_STATUS_CONFIG));
      if(null != entityList && entityList.size() > 0){
          // 先将list进行复制 然后再遍历
          List<PartsAccount> entityListV = new ArrayList<PartsAccount>();
          entityListV.addAll(entityList);
          for(PartsAccount entity : entityListV){
              if(entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
                  entityList.remove(entity);
              }
              
          }
          if(null != entityList && entityList.size() > 0){
              // 排除大部件范围内的配件
              if(!StringUtil.isNullOrBlank(jcpjbm)){
                  List<PartsAccount> accountsResults = getJcpjbmParts(entityList, jcpjbm);
                  if(null != accountsResults && accountsResults.size() > 0){
                      return accountsResults ;
                  }else{
                      throw new BusinessException("该配件未与当前大部件进行关联！");
                  }
              }              
              return entityList ;
          }else{
              throw new BusinessException("配件编号为"+account.getPartsNo()+"已经在做检修，不能登记！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    
    
    /**
     * <li>说明：获取大部件下过滤的配件
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList
     * @param jcpjbm
     * @return
     */
    public List<PartsAccount> getJcpjbmParts(List<PartsAccount> entityList,String jcpjbm) {
        List<PartsAccount> result = new ArrayList<PartsAccount>();
        result.addAll(entityList);
        List<PartsType> typeslist = partsTypeManager.getPartsTypeListByJcpjbm(jcpjbm);
        if(typeslist == null){
            return result ;
        }
        for (PartsAccount account : entityList) {
            if(!isContainType(account.getPartsTypeIDX(),typeslist)){
                result.remove(account);
            }
        }
        return result ;
    }
    
    /**
     * <li>说明：判断是否属于大部件下的配件
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsTypeIDX
     * @param typeslist
     * @return
     */
    private boolean isContainType(String partsTypeIDX, List<PartsType> typeslist) {
        boolean flag = false;
        for (PartsType type : typeslist) {
            if(type.getIdx().equals(partsTypeIDX)){
                flag = true ;
                break ;
            }
        }
        return flag;
    }
    
    /**
     * <li>说明：上车配件登记【web端】
     * <li>创建人：程梅
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 上车配件登记信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveFixRegisterBatch(PartsFixRegister[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsFixRegister register : registers) {
            savePartsFixRegister(register);
        }
    }
    
    /**
     * <li>说明：配件上车登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 配件上车登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateFixRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsFixRegister> partsFixRegisterList = new ArrayList<PartsFixRegister>();
        PartsFixRegister partsFixRegister;
        for (String id : ids) {
            partsFixRegister = new PartsFixRegister();
            partsFixRegister = getModelById(id);
            if(null != partsFixRegister){
                partsFixRegister.setStatus(PjwzConstants.STATUS_CHECKED);//上车配件登记状态为已确认
                partsFixRegisterList.add(partsFixRegister);
            }
        }
        this.saveOrUpdate(partsFixRegisterList);
    }
    
    /**
     * <li>说明：查询下车位置列表，通过规格型号以及机车检修作业计划进行过滤，同时需要过滤掉已经下车的配件位置
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询参数
     * @return Page<EquipPart>
     */
    @SuppressWarnings("unchecked")
    public Page<EquipPart> findEquipPartList(SearchEntity<PartsFixRegister> searchEntity) {
        // 查询参数
        PartsFixRegister entity = searchEntity.getEntity();
        // 组装下车配件清单的数据
        StringBuffer oplHql = new StringBuffer();
        oplHql.append(" select new OffPartList(opl.idx ,opl.wzdm,opl.wzmc) From OffPartList opl,JcpjzdBuild jb,PartsType pt,TrainWorkPlan twp where  ");
        oplHql.append(" opl.partsIDX = jb.jcpjbm and pt.jcpjbm = jb.jcpjbm and twp.idx = opl.workPlanIDX ");
        oplHql.append(" and opl.recordStatus = 0 and pt.recordStatus = 0 and twp.recordStatus = 0  ");
        // 规格型号ID
        if(!StringUtil.isNullOrBlank(entity.getPartsTypeIDX())){
            oplHql.append(" and pt.idx = '"+entity.getPartsTypeIDX()+"' ");
        }else{
            oplHql.append(" and 1=0 ");
        }
        
        // 检修作业计划ID
        if (!StringUtil.isNullOrBlank(entity.getRdpIdx())) {
            oplHql.append(" and twp.idx = '"+entity.getRdpIdx()+"' ");
        }else{
            oplHql.append(" and 1=0 ");
        }
        // 查询下车配件清单数据
        List<OffPartList> oplLists = this.daoUtils.find(oplHql.toString());
        // 下车配件清单位置代码
        String wzdms = "" ;
        for (OffPartList opl : oplLists) {
            // 判断位置编码是否为空 组装下车配件清单位置编码
            if(!StringUtil.isNullOrBlank(opl.getWzdm())){
                wzdms += "'"+opl.getWzdm()+"',";
            }
        }
        if(!StringUtil.isNullOrBlank(wzdms)){
            wzdms = wzdms.substring(0, wzdms.length()-1);
        }
        // 查询下车位置列表
        StringBuffer epHql = new StringBuffer();
        epHql.append(" From EquipPart eq where 1=1 ");
//        if(!StringUtil.isNullOrBlank(wzdms)){
//            epHql.append(" and eq.partId in ("+wzdms+")");
//        }
        // 排除掉已经下车登记的工单
        if (!StringUtil.isNullOrBlank(entity.getRdpIdx()) && !StringUtil.isNullOrBlank(entity.getPartsTypeIDX())) {
            String haveUnloadPlaces = getHaveAboardPlaces(entity.getPartsTypeIDX(), entity.getRdpIdx());
            if(!StringUtil.isNullOrBlank(haveUnloadPlaces)){
                epHql.append(" and eq.partName not in ("+haveUnloadPlaces+")");
            }
        }
        StringBuilder totalHql = new StringBuilder("select count(*) ").append(epHql);
        return equipPartManager.findPageList(totalHql.toString(), epHql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param partsTypeId 规格型号ID
     * @param rdpIdx 计划ID
     * @return
     */
    @SuppressWarnings("unchecked")
    private String getHaveAboardPlaces(String partsTypeId,String rdpIdx){
        String result = "" ;
        String idxs = "";
        StringBuffer sb = new StringBuffer(" from PartsFixRegister pur where pur.recordStatus = 0 and pur.aboardPlace is not null ");
        sb.append("and pur.rdpIdx = '"+rdpIdx+"'");
        PartsType pt = partsTypeManager.getModelById(partsTypeId);
        if(pt != null && !StringUtil.isNullOrBlank(pt.getJcpjbm())){
           List<PartsType> lists = partsTypeManager.getPartsTypeListByJcpjbm(pt.getJcpjbm());
           if(lists != null && lists.size() > 0){
               for (PartsType type : lists) {
                   idxs += "'"+type.getIdx()+"',";
               }
           }
        }
        if(!StringUtil.isNullOrBlank(idxs)){
            idxs = idxs.substring(0, idxs.length()-1);
        }else{
            idxs = "'"+partsTypeId+"'";
        }
        sb.append(" and pur.partsTypeIDX in ("+idxs+")");
        List<PartsFixRegister> ur = (List<PartsFixRegister>) this.find(sb.toString());
        for (PartsFixRegister register : ur) {
            result += "'"+register.getAboardPlace()+"',";
        }
        if(!StringUtil.isNullOrBlank(result)){
            result = result.substring(0, result.length()-1);
        }
        return result ;
    }

    /**
     * <li>说明：查询登记情况
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public Page<PartsUnRegisterVo> findFixRegisterPartsList(Integer start, Integer limit, String workPlanId, String jcpjmc) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select jcpjbm ,jcpjmc , ");
        sb.append(" sum(case when dataSource = '01' then 1 else 0 end ) as countQD , ");
        sb.append(" sum(case when dataSource = '02' then 1 else 0 end ) as countDJ from ( ");
        sb.append(" select jj.jcpjbm ,jj.jcpjmc ,jj.pym,opl.work_plan_idx as workPlanId , '01' as dataSource from JXGC_Off_Parts_List opl ");
        sb.append(" inner join T_JCBM_JCPJZD jj on opl.parts_idx = jj.jcpjbm ");
        sb.append(" where opl.record_status = 0 ");
        sb.append("  union all ");
        sb.append(" select jj.jcpjbm ,jj.jcpjmc ,jj.pym, pur.rdp_idx as workPlanId , '02' as dataSource from PJWZ_Parts_FIX_Register pur  ");
        sb.append(" inner join PJWZ_Parts_Type pt on pt.idx = pur.parts_type_idx ");
        sb.append(" inner join T_JCBM_JCPJZD jj on pt.jcpjbm = jj.jcpjbm ");
        sb.append(" where pur.record_status = 0 and pt.record_status = 0  ) ");
        sb.append(" where workPlanId = '"+workPlanId+"' ");
        if(!StringUtil.isNullOrBlank(jcpjmc)){
            sb.append(" and (jcpjmc like '%"+jcpjmc+"%' or pym like '%"+jcpjmc+"%') ");
        }
        sb.append("  group by jcpjbm ,jcpjmc ");
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT From (" + sb.toString() + ")";
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, PartsUnRegisterVo.class); 
    }

    /**
     * <li>说明：查询大部件与配件已下车登记的相关信息(上车配件登录（新）)
     * <li>创建人：张迪
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数    
     * @param limit 每页条数
     * @param workPlanIdx 作业计划idx
     * @return 查询集合
     */
    public Page<PartsAboardRegisterBean> findPartsAboardRegisterAll(Integer start, Integer limit, String workPlanIdx) {
        // 班组为当前系统操作员所在班组
        String sql = SqlMapUtil.getSql("pjwl-query:findPartsAboardRegisterAll")
        .replace("#RDP_IDX#", workPlanIdx);
        sql += " order by b.seq_no ,b.jcpjbm nulls last ";
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        Page<PartsAboardRegisterBean> sd = this.queryPageList(totalSql, sql, start, limit, false, PartsAboardRegisterBean.class);
        return sd;
    }
    
    /**
     * <li>说明：查询未登记的上车配件列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start
     * @param limit
     * @param workPlanIdx
     * @return
     */
    public Page<PartsAboardRegisterBean> findPartsNotRegisterAll(Integer start, Integer limit, String workPlanIdx) {
        // 班组为当前系统操作员所在班组
        String sql = SqlMapUtil.getSql("pjwl-query:findPartsAboardRegisterAll")
        .replace("#RDP_IDX#", workPlanIdx);
        sql += " and b.idx is null ";
        sql += " order by b.seq_no ,b.jcpjbm nulls last ";
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        Page<PartsAboardRegisterBean> sd = this.queryPageList(totalSql, sql, start, limit, false, PartsAboardRegisterBean.class);
        return sd;
    }
    
    
    /**
     * <li>说明：保存上车配件登录【web端】（新）
     * <li>创建人：张迪
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 上车登录实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsFixRegisterNew(PartsFixRegister register) throws BusinessException, NoSuchFieldException {
        PartsAccount account = new  PartsAccount();
        account.setPartsNo(register.getPartsNo());
        account.setSpecificationModel(register.getSpecificationModel());
        account.setIdentificationCode(register.getIdentificationCode());
        if(StringUtil.isNullOrBlank(register.getPartsAccountIDX())){ // 配件编号和规格型号查询该配件周转台账信息
            account = partsAccountManager.getAccountBySpeAndParts(account);
        }else{
           account = partsAccountManager.getModelById(register.getPartsAccountIDX()); //查询该配件周转台账信息
        }
       
        if(null == account){
            throw new BusinessException("配件编号为"+register.getPartsNo()+"的配件不存在，请重新添加！");
        }
        register.setPartsAccountIDX(account.getIdx());
        // 2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        // 20160906 by wujl 此处添加配置项，神木配件上车时，配件不需要先进行出库操作
//        boolean isPartsStatusConfig = "true".equalsIgnoreCase(SystemConfigUtil.getValue(PartsFixRegister.IS_PARTS_STATUS_CONFIG));
            if(account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
                throw new BusinessException("配件编号为"+register.getPartsNo()+"已经在做检修，不能登记！");
            }

        
        // 判断上车位置是否已经被选择，不能选择重复的上车位置，设置上车位置编码
        if(!StringUtil.isNullOrBlank(register.getAboardPlace())){
            // 先把前后空格去掉
            register.setAboardPlace(register.getAboardPlace().trim());
            boolean flag = isExistUnloadPlace(register);
            if(flag){
                throw new BusinessException("上车位置【" + register.getAboardPlace() + "】已存在，请重新选择！");
            }
            // 找到位置名称对应的位置编码
            EquipPart ep = equipPartManager.getEquipPartByName(register.getAboardPlace());
            register.setAboardPlaceCode(ep == null ? "" :ep.getPartId());
        }
        register.setAboardDate(new Date());
        //日志描述=上车车型+上车车号+修程
        String logDesc = register.getAboardTrainType() + register.getAboardTrainNo() + " " + register.getAboardRepairClass();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJSC, logDesc);
        log = partsManageLogManager.initLog(log, account);
        //更新配件周转台账
        updateAccount(register , account);
        register.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
//        // id为空，表示是范围外上车
//        if(StringUtil.isNullOrBlank(register.getIdx())){
//            register.setIsInRange(PartsFixRegister.IS_IN_RANGE_NO);// 是否范围内下车---否
//        }
        register.setRdpType("车辆") ;//检修任务单类型为【机车】
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) register.setCreatorName(emp.getEmpname());
        register = EntityUtil.setSysinfo(register);
        //设置逻辑删除字段状态为未删除
        register = EntityUtil.setNoDelete(register);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(register);
        log.eventIdx(register.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：保存上车配件登录【web端】（新）
     * <li>创建人：张迪
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveFixRegisterNew(PartsFixRegister[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsFixRegister register : registers) {
            savePartsFixRegisterNew(register);
        }
    }
    
    /**
     * <li>说明：通过作业计划ID查询上车配件清单
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-03
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIdx 作业计划ID
     */
    public List<PartsFixRegister> findPartsFixRegisterByWorkPlanIdx(String workPlanIdx){
    	StringBuffer hql = new StringBuffer(" From PartsFixRegister where recordStatus = 0 and rdpIdx = ? ");
    	return (List<PartsFixRegister>)this.daoUtils.find(hql.toString(), new Object[]{workPlanIdx});
    }
}