package com.yunda.jx.pjwz.unloadparts.manager;

import java.lang.reflect.InvocationTargetException;
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
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.component.entity.EquipPart;
import com.yunda.jx.component.manager.EquipPartManager;
import com.yunda.jx.pjjx.workplan.entity.TrainWorkPlanView;
import com.yunda.jx.pjjx.workplan.manager.TrainWorkPlanViewManager;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType;
import com.yunda.jx.pjwz.partsBase.partstype.manager.PartsTypeManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.turnover.entity.OffPartList;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnRegisterVo;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegister;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegisterNewBean;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsUnloadRegister业务类,下车配件登记单
 * <li>创建人：程梅
 * <li>创建日期：2015-04-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsUnloadRegisterManager")
public class PartsUnloadRegisterManager extends JXBaseManager<PartsUnloadRegister, PartsUnloadRegister>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** TrainWorkPlanView业务类,机车检修作业计划 */
    @Resource
    private TrainWorkPlanViewManager trainWorkPlanViewManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
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
        selectSql.append(" ( select * from PJWZ_Parts_Unload_Register ur ");
        selectSql.append(" left join PJWZ_Parts_Type pt on ur.parts_type_idx = pt.idx ");
        selectSql.append(" where ur.is_in_range = '是' and ur.record_status = 0 and ");
        selectSql.append("  (ur.Rdp_Idx = l.work_plan_idx AND  l.wzmc is not null and pt.jcpjbm = l.parts_idx and ur.Unload_Place = l.wzmc ) ");
        selectSql.append(" or ( ur.Rdp_Idx = l.work_plan_idx AND l.wzmc is null and ur.Unload_Place is null and pt.jcpjbm = l.parts_idx ) ");
        selectSql.append("  )) ");
        selectSql.append("  || '/' || (select count(u.idx) from PJWZ_Parts_Unload_Register u where u.RECORD_STATUS=0 and u.rdp_idx = t.IDX) as \"num\" ");
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
            awhere.append(" order by t.Train_Type_ShortName,t.Train_No,t.Work_Plan_Status ");
        }
        StringBuilder totalSql = new StringBuilder("select count(*) ").append(fromSql).append(awhere);
        StringBuilder sql = selectSql.append(fromSql).append(awhere);
        return trainWorkPlanViewManager.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }
    
    /**
     * <li>说明：根据过滤条件查询下车配件登记列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsUnloadRegister> 列表分页对象
     * @throws BusinessException
     */
    public Page<PartsUnloadRegister> findPageList(final SearchEntity<PartsUnloadRegister> searchEntity) throws BusinessException{
        PartsUnloadRegister register = searchEntity.getEntity() ;
        StringBuffer selectSql = null;
        StringBuffer fromSql = null;
        StringBuffer sql = null;
        String identificationCode = register.getIdentificationCode() ;
        //未登帐列表
        if(PjwzConstants.STATUS_WAIT.equals(register.getStatus())){
            selectSql = new StringBuffer("select t.* ");
            fromSql = new StringBuffer(" from PJWZ_Parts_Unload_Register t where t.record_Status=0 and t.status='")
            .append(PjwzConstants.STATUS_WAIT).append("' and t.RDP_IDX='").append(register.getRdpIdx()).append("' ");
            if(!StringUtil.isNullOrBlank(identificationCode)){
                fromSql.append(" and (t.parts_name like '%").append(identificationCode).append("%' or t.specification_model like '%")
                .append(identificationCode).append("%' or t.location like '%").append(identificationCode).append("%' )") ;
            }
        }else if(PjwzConstants.STATUS_ED.equals(register.getStatus())){   //已登帐列表
            selectSql = new StringBuffer("select t.*,a.Parts_Status,a.Parts_Status_Name ");
            fromSql = new StringBuffer(" from PJWZ_Parts_Unload_Register t,PJWZ_PARTS_ACCOUNT a where t.PARTS_ACCOUNT_IDX=a.idx and t.record_Status=0 and a.record_Status=0 and t.status='")
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
     * <li>说明：保存下车配件登记信息
     * <li>创建人：程梅
     * <li>创建日期：2015-4-28
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @param registerArray 下车配件明细
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveUnloadRegister(PartsUnloadRegister register, PartsUnloadRegister[] registerArray) throws BusinessException, NoSuchFieldException,
        IllegalAccessException, InvocationTargetException {
        for (PartsUnloadRegister reg : registerArray) {
            // 配件识别码唯一【在册状态中唯一】
            if (null != partsAccountManager.getAccountByIdCode(reg.getIdentificationCode())) {
                throw new BusinessException("配件识别码【" + reg.getIdentificationCode() + "】已存在，不能重复添加！ ");
            }
            PartsAccount account = new PartsAccount();
            account.setPartsNo(reg.getPartsNo());
            account.setSpecificationModel(reg.getSpecificationModel());
            account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC);
            // 配件编号+配件规格型号判断唯一性【在册状态中唯一】
            account = partsAccountManager.getAccount(account);
            if (null != account) {
                throw new BusinessException("配件编号【" + reg.getPartsNo() + "】,规格型号【" + reg.getSpecificationModel() + "】已存在，不能重复添加！");
            }
        }
        // 新增到配件周转台账表中
        List<PartsUnloadRegister> registerList = saveFromPartsUnloadRegister(register, registerArray);
        for (PartsUnloadRegister entity : registerList) {
            entity.setIdx(null);
            entity.setStatus(PjwzConstants.STATUS_ED);// 单据状态为已登帐
            entity.setIsInRange(PartsUnloadRegister.IS_IN_RANGE_NO);// 是否范围内下车---否
            entity.setRdpType("机车");// 检修任务单类型为【机车】
            this.saveOrUpdate(entity);
            
            // 保存配件周转信息日志
            this.saveLog(entity);
        }
    }
    
    
    /**
     * <li>说明：保存下车配件登记信息（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register
     * @param registerArray
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void saveUnloadRegisterNew(PartsUnloadRegister register, PartsUnloadRegister[] registerArray) throws BusinessException, NoSuchFieldException,
        IllegalAccessException, InvocationTargetException {
        for (PartsUnloadRegister reg : registerArray) {
            // 配件识别码唯一【在册状态中唯一】
            PartsAccount partsAccount = partsAccountManager.getAccountByIdCode(reg.getIdentificationCode());
            if (null != partsAccount && !reg.getPartsAccountIDX().equals(partsAccount.getIdx())) {
                throw new BusinessException("配件识别码【" + reg.getIdentificationCode() + "】已存在，不能重复添加！ ");
            }
            PartsAccount account = new PartsAccount();
            account.setPartsNo(reg.getPartsNo());
            account.setSpecificationModel(reg.getSpecificationModel());
            account.setPartsTypeIDX(reg.getPartsTypeIDX());
            account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC);
            // 配件编号+配件规格型号判断唯一性【在册状态中唯一】
            account = partsAccountManager.getAccount(account);
            if (null != account && !reg.getPartsAccountIDX().equals(account.getIdx())) {
                throw new BusinessException("配件编号【" + reg.getPartsNo() + "】,规格型号【" + reg.getSpecificationModel() + "】已存在，不能重复添加！");
            }
            if(!StringUtil.isNullOrBlank(reg.getPartsAccountIDX())){
                account = partsAccountManager.getModelById(reg.getPartsAccountIDX());
                try {
                    if(account != null && !PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus())){
                        // throw new BusinessException("配件编号【" + reg.getPartsNo() + "】,规格型号【" + reg.getSpecificationModel() + "】不能修改，只能修改【待修】的配件！"); 
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }

            }
        }
        // 新增到配件周转台账表中
        List<PartsUnloadRegister> registerList = saveFromPartsUnloadRegisterNew(register, registerArray);
        for (PartsUnloadRegister entity : registerList) {
            //entity.setIdx(null);
            entity.setStatus(PjwzConstants.STATUS_ED);// 单据状态为已登帐
            entity.setRdpType("车辆");// 检修任务单类型为【机车】
            this.saveOrUpdate(entity);
            // 保存配件周转信息日志
            this.saveLog(entity);
        }
    }
    
    
    /**
     * <li>说明：新增到配件周转台账表中（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register
     * @param registerArray
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<PartsUnloadRegister> saveFromPartsUnloadRegisterNew(PartsUnloadRegister register, PartsUnloadRegister[] registerArray)
    throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
    List<PartsUnloadRegister> entityList = new ArrayList<PartsUnloadRegister>(registerArray.length);
    for (PartsUnloadRegister entity : registerArray) {
        PartsAccount account = null;
        if(!StringUtil.isNullOrBlank(entity.getPartsAccountIDX())){
            account = partsAccountManager.getModelById(entity.getPartsAccountIDX());
            // 如果有非待修的配件 不进行处理
            if(account != null && !PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus())){
                continue ;
             }
        }
        if(account == null){
            account = new PartsAccount();
        }
        String partsAccountIdx = account.getIdx();
        entity.setTakeOverDept(register.getTakeOverDept());
        entity.setTakeOverDeptId(register.getTakeOverDeptId());
        entity.setTakeOverDeptOrgseq(register.getTakeOverDeptOrgseq());
        entity.setTakeOverDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);// 接收部门类型为机构
        entity.setTakeOverEmp(register.getTakeOverEmp());
        entity.setTakeOverEmpId(register.getTakeOverEmpId());
        entity.setTakeOverTime(register.getTakeOverTime());
        entity.setHandOverEmp(register.getHandOverEmp());
        entity.setHandOverEmpId(register.getHandOverEmpId());
        entity.setUnloadTrainTypeIdx(register.getUnloadTrainTypeIdx());
        entity.setUnloadTrainType(register.getUnloadTrainType());
        entity.setUnloadTrainNo(register.getUnloadTrainNo());
        entity.setUnloadRepairClass(register.getUnloadRepairClass());
        entity.setUnloadRepairClassIdx(register.getUnloadRepairClassIdx());
        entity.setUnloadRepairTime(register.getUnloadRepairTime());
        entity.setUnloadRepairTimeIdx(register.getUnloadRepairTimeIdx());
        entity.setRdpIdx(register.getRdpIdx());
        entity.setUnloadDate(new Date());// 下车日期为当前日期
        
        BeanUtils.copyProperties(account, entity);
        account.setIdx(partsAccountIdx);
        
        if (null != register.getTakeOverDeptId()) {
            account.setManageDeptId(register.getTakeOverDeptId().toString());
        }
        account.setManageDept(register.getTakeOverDept());// 责任部门为接收部门
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);// 责任部门类型为机构
        account.setManageDeptOrgseq(register.getTakeOverDeptOrgseq());// 责任部门序列为接收部门序列
        account.setPartsStatusUpdateDate(register.getTakeOverTime());// 配件状态更新时间为收件日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);// 配件状态为待修
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, "待修"));
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);// 是否新品---旧
        account.setSource("下车");
        account.setCreateTime(new Date());
        account.setUnloadRdpIDX(register.getRdpIdx());
        account.setUnloadRdpType(register.getRdpType());
        this.partsAccountManager.saveOrUpdate(account);
        entity.setPartsAccountIDX(account.getIdx());
        entityList.add(entity);
    }
    return entityList;
}
    
    

    /**
     * <li>说明：新增到配件周转台账表中
     * <li>创建人：程梅
     * <li>创建日期：2015-4-28
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @param registerArray 下车配件明细
     * @return List<PartsUnloadRegister> 登记单list
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public List<PartsUnloadRegister> saveFromPartsUnloadRegister(PartsUnloadRegister register, PartsUnloadRegister[] registerArray)
        throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        List<PartsUnloadRegister> entityList = new ArrayList<PartsUnloadRegister>(registerArray.length);
        PartsAccount account;
        for (PartsUnloadRegister entity : registerArray) {
            account = new PartsAccount();
            entity.setTakeOverDept(register.getTakeOverDept());
            entity.setTakeOverDeptId(register.getTakeOverDeptId());
            entity.setTakeOverDeptOrgseq(register.getTakeOverDeptOrgseq());
            entity.setTakeOverDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);// 接收部门类型为机构
            entity.setTakeOverEmp(register.getTakeOverEmp());
            entity.setTakeOverEmpId(register.getTakeOverEmpId());
            entity.setTakeOverTime(register.getTakeOverTime());
            entity.setHandOverEmp(register.getHandOverEmp());
            entity.setHandOverEmpId(register.getHandOverEmpId());
            entity.setUnloadTrainTypeIdx(register.getUnloadTrainTypeIdx());
            entity.setUnloadTrainType(register.getUnloadTrainType());
            entity.setUnloadTrainNo(register.getUnloadTrainNo());
            entity.setUnloadRepairClass(register.getUnloadRepairClass());
            entity.setUnloadRepairClassIdx(register.getUnloadRepairClassIdx());
            entity.setUnloadRepairTime(register.getUnloadRepairTime());
            entity.setUnloadRepairTimeIdx(register.getUnloadRepairTimeIdx());
            entity.setRdpIdx(register.getRdpIdx());
            entity.setUnloadDate(new Date());// 下车日期为当前日期
            BeanUtils.copyProperties(account, entity);
            if (null != register.getTakeOverDeptId()) {
                account.setManageDeptId(register.getTakeOverDeptId().toString());
            }
            account.setManageDept(register.getTakeOverDept());// 责任部门为接收部门
            account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);// 责任部门类型为机构
            account.setManageDeptOrgseq(register.getTakeOverDeptOrgseq());// 责任部门序列为接收部门序列
            account.setPartsStatusUpdateDate(register.getTakeOverTime());// 配件状态更新时间为收件日期
            account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);// 配件状态为待修
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, "待修"));
            account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);// 是否新品---旧
            account.setSource("下车");
            account.setIdx(null);
            account.setCreateTime(new Date());
            account.setUnloadRdpIDX(register.getRdpIdx());
            account.setUnloadRdpType(register.getRdpType());
            this.partsAccountManager.saveOrUpdate(account);
            entity.setPartsAccountIDX(account.getIdx());
            entityList.add(entity);
        }
        return entityList;
    }
    
    /**
     * <li>说明：保存下车配件信息（范围内下车为更新数据，范围外下车为新增数据）【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void savePartsUnloadRegister(PartsUnloadRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
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
        // 判断下车位置是否已经被选择，不能选择重复的下车位置，设置下车位置编码
        if(!StringUtil.isNullOrBlank(register.getUnloadPlace())){
            // 先把前后空格去掉
            register.setUnloadPlace(register.getUnloadPlace().trim());
            boolean flag = isExistUnloadPlace(register);
            if(flag){
                throw new BusinessException("下车位置【" + register.getUnloadPlace() + "】已存在，请重新选择！");
            }
            // 找到位置名称对应的位置编码
            EquipPart ep = equipPartManager.getEquipPartByName(register.getUnloadPlace());
            register.setUnloadPlaceCode(ep == null ? "" :ep.getPartId());
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
            register.setIsInRange(PartsUnloadRegister.IS_IN_RANGE_NO); // 是否范围内下车---否
        } else {
            register.setIsInRange(PartsUnloadRegister.IS_IN_RANGE_YES); // 是否范围内下车---是
        }
        // 检修任务单类型为【机车】
        register.setRdpType("机车");              
        this.saveOrUpdate(register);
        
        // 保存配件周转信息日志
        saveLog(register);
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
    private Boolean isExistUnloadPlace(PartsUnloadRegister entity){
        boolean flag = false ;
        String haveUploadPlaces = getHaveUnloadPlaces(entity.getPartsTypeIDX(), entity.getRdpIdx());
        if(!StringUtil.isNullOrBlank(haveUploadPlaces)){
            String[] hups = haveUploadPlaces.split(",");
            List<String> list = Arrays.asList(hups);
            if(list != null && list.contains("'"+entity.getUnloadPlace()+"'")){
                flag = true ;
            }
        }
        return flag ;
    }
    
    /**
     * <li>说明：保存配件周转信息日志
     * <li>创建人：何涛
     * <li>创建日期：2016-01-28
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @throws NoSuchFieldException
     */
    private void saveLog(PartsUnloadRegister register) throws NoSuchFieldException {
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
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @return 配件信息（·配件周转台账）
     * @throws BusinessException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public PartsAccount saveAccount(PartsUnloadRegister register) throws BusinessException, NoSuchFieldException, IllegalAccessException,
        InvocationTargetException {
        PartsAccount account = new PartsAccount();
        // 机车兑现单信息
        TrainWorkPlanView rdp = trainWorkPlanViewManager.getModelById(register.getRdpIdx());
        register.setUnloadTrainTypeIdx(rdp.getTrainTypeIdx());
        register.setUnloadTrainType(rdp.getTrainTypeShortName());
        register.setUnloadTrainNo(rdp.getTrainNo());
        register.setUnloadRepairClass(rdp.getRepairClassName());
        register.setUnloadRepairClassIdx(rdp.getRepairClassIdx());
        register.setUnloadRepairTime(rdp.getRepairtimeName());
        register.setUnloadRepairTimeIdx(rdp.getRepairtimeIdx());
        // 将值赋给配件周转台账
        BeanUtils.copyProperties(account, register);
        if (null != register.getTakeOverDeptId()) {
            account.setManageDeptId(register.getTakeOverDeptId().toString());
        }
        account.setManageDept(register.getTakeOverDept());// 责任部门为接收部门
        account.setManageDeptType(register.getTakeOverDeptType());// 责任部门类型为机构 by wujl 配件的存放位置类型从页面传过来
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
     * <li>说明：撤销（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateUnloadRegisterForCancelNew(String id) throws BusinessException, NoSuchFieldException {
        PartsUnloadRegister register = getModelById(id);
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX());// 取得配件信息实体
        if (null == account || null == register.getIsInRange()) {
            throw new BusinessException("数据异常，请刷新后重试！");
        }
        if (!PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus()) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG) || Constants.DELETED == account.getRecordStatus()) {
            throw new BusinessException("只有【待修】且不在库的配件才能撤销！");
        }
        // 删除周转台账信息
        partsAccountManager.logicDelete(register.getPartsAccountIDX());
        this.logicDelete(id); // 删除下车配件信息
        partsManageLogManager.deleteLogByEventIdx(id);// 删除日志信息
    }
    
    
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-13
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateUnloadRegisterForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsUnloadRegister register = getModelById(id);
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
        if (PartsUnloadRegister.IS_IN_RANGE_YES.equals(register.getIsInRange())) {
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
        if (PartsUnloadRegister.IS_IN_RANGE_NO.equals(register.getIsInRange())) {
            this.logicDelete(id); // 删除下车配件信息
        }
        partsManageLogManager.deleteLogByEventIdx(id);// 删除日志信息
    }
    
    /**
     * <li>说明：修改下车配件信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsUnloadRegister(PartsUnloadRegister register) throws BusinessException, NoSuchFieldException {
        // 取得配件信息实体
        PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX());
        if (null == account) {
            throw new BusinessException("配件编号为" + register.getPartsNo() + "的配件不存在，请重新添加！");
        }
        if (!PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus())) {
            throw new BusinessException("只有【待修】状态的配件才能修改！");
        }
        // 修改配件周转信息
        updateAccount(register, account);
        
        register.setStatus(PjwzConstants.STATUS_ED);    // 单据状态为已登帐
        this.saveOrUpdate(register);
    }
    
    /**
     * <li>说明：修改配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param register 下车配件信息
     * @param account 配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsUnloadRegister register, PartsAccount account) throws BusinessException, NoSuchFieldException {
        try {
            BeanUtils.copyProperties(account, register);
        } catch (Throwable e) {
            throw new BusinessException(e);
        }
        if (null != register.getTakeOverDeptId()) {
            account.setManageDeptId(register.getTakeOverDeptId().toString());
        }
        account.setManageDept(register.getTakeOverDept());// 责任部门为接收部门
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);// 责任部门类型为机构
        account.setManageDeptOrgseq(register.getTakeOverDeptOrgseq());// 责任部门序列为接收部门序列
        account.setPartsStatusUpdateDate(register.getTakeOverTime());// 配件状态更新时间为收件日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);// 配件状态为待修
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, "待修"));
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);// 是否新品---旧
        account.setSource("下车");
        account.setIdx(register.getPartsAccountIDX());
        this.partsAccountManager.saveOrUpdate(account);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
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
     * @param jcpjbm 大部件编码
     * @return List<PartsAccount> 台账信息列表
     */
    public List<PartsAccount> getPartsAccountList(PartsAccount account, String jcpjbm) {
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
              throw new BusinessException("此配件已登记，不能重复登记！");
          }
      }
      
      return null ;
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
     * <li>说明：下车配件登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人：何涛
     * <li>修改日期：2016-01-28
     * <li>修改内容：代码重构
     * @param ids 下车配件登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsUnloadRegisterForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<PartsUnloadRegister> entityList = new ArrayList<PartsUnloadRegister>(ids.length);
        PartsUnloadRegister register;
        for (String id : ids) {
            register = getModelById(id);
            if(null != register){
                register.setStatus(PjwzConstants.STATUS_CHECKED);//下车配件登记状态为已确认
                entityList.add(register);
            }
        }
        this.saveOrUpdate(entityList);
    }
    /**
     * 
     * <li>说明：根据过滤条件查询机车兑现单列表【web端下车配件登记】
     * <li>创建人：程梅
     * <li>创建日期：2016-3-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<TrainWorkPlanView> 列表分页对象
     * @throws BusinessException
     */
    public Page<TrainWorkPlanView> findWorkPlanListWeb(final SearchEntity<TrainWorkPlanView> searchEntity) throws BusinessException{
        TrainWorkPlanView plan = searchEntity.getEntity() ;
        StringBuilder selectSql = new StringBuilder("select t.*,(select count(r.idx) from PJWZ_Parts_Unload_Register r where r.RECORD_STATUS=0 and r.rdp_idx = t.IDX and r.status = '20') ")
        .append("|| '/' || (select count(u.idx) from PJWZ_Parts_Unload_Register u where u.RECORD_STATUS=0 and u.rdp_idx = t.IDX) as \"num\" ");
        StringBuffer fromSql = new StringBuffer(" from V_JXGC_TRAIN_WORK_PLAN t where t.RECORD_STATUS=0 and t.WORK_PLAN_STATUS != '").append(TrainWorkPlanView.STATUS_NULLIFY).append("' ");
        StringBuffer awhere =  new StringBuffer();
        //车型
        if(!StringUtil.isNullOrBlank(plan.getTrainTypeIdx())){
            awhere.append(" and t.Train_Type_IDX = '").append(plan.getTrainTypeIdx()).append("'");
        }
        //车号
        if(!StringUtil.isNullOrBlank(plan.getTrainNo())){
            awhere.append(" and t.Train_No like '%").append(plan.getTrainNo()).append("%'");
        }
        //修程
        if(!StringUtil.isNullOrBlank(plan.getRepairClassIdx())){
            awhere.append(" and t.Repair_Class_IDX = '").append(plan.getRepairClassIdx()).append("'");
        }
//        //实际开始时间【开始】
//        if(null != plan.getBeginTime()){
//            awhere.append(" and t.Begin_Time >= to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(plan.getBeginTime())).append("','yyyy-mm-dd hh24:mi:ss') ");
//        }
//        //实际开始时间【结束】
//        if(null != plan.getEndTime()){
//            awhere.append(" and t.Begin_Time <= to_date('").append(new SimpleDateFormat("yyyy-MM-dd").format(plan.getEndTime()) + " 23:59:59").append("','yyyy-mm-dd hh24:mi:ss') ");
//        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.Train_Type_ShortName,t.Train_No,t.Work_Plan_Status ");
        }
        StringBuilder totalSql = new StringBuilder("select count(*) ").append(fromSql).append(awhere);
        StringBuilder sql = selectSql.append(fromSql).append(awhere);
        return trainWorkPlanViewManager.findPageList(totalSql.toString(), sql.toString(), searchEntity.getStart(), searchEntity.getLimit(),null,searchEntity.getOrders());
    }
    /**
     * 
     * <li>说明：规格型号+配件编号唯一性验证
     * <li>创建人：程梅
     * <li>创建日期：2016-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 过滤条件
     * @return String[] 错误提示
     */
    public String[] validateUpdate(PartsUnloadRegister register){
        PartsAccount account = new PartsAccount();
        account.setPartsNo(register.getPartsNo());
        account.setSpecificationModel(register.getSpecificationModel());
        account.setPartsStatus(PartsAccount.PARTS_STATUS_ZC);
        // 配件编号+配件规格型号判断唯一性【在册状态中唯一】
        account = partsAccountManager.getAccount(account);
        if (null != account && !account.getIdx().equals(register.getPartsAccountIDX())) {
            throw new BusinessException("配件编号【" + register.getPartsNo() + "】,规格型号【" + register.getSpecificationModel() + "】已存在，不能重复添加！");
        }
        return null;
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
    public Page<EquipPart> findEquipPartList(SearchEntity<PartsUnloadRegister> searchEntity) {
        // 查询参数
        PartsUnloadRegister entity = searchEntity.getEntity();
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
            String haveUnloadPlaces = getHaveUnloadPlaces(entity.getPartsTypeIDX(), entity.getRdpIdx());
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
    private String getHaveUnloadPlaces(String partsTypeId,String rdpIdx){
        String result = "" ;
        String idxs = "";
        StringBuffer sb = new StringBuffer(" from PartsUnloadRegister pur where pur.recordStatus = 0 and pur.unloadPlace is not null ");
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
        List<PartsUnloadRegister> ur = (List<PartsUnloadRegister>) this.find(sb.toString());
        for (PartsUnloadRegister register : ur) {
            result += "'"+register.getUnloadPlace()+"',";
        }
        if(!StringUtil.isNullOrBlank(result)){
            result = result.substring(0, result.length()-1);
        }
        return result ;
    }

    /**
     * <li>说明：查询未登记列表集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数
     * @param limit 限制条数
     * @param workPlanId 计划ID
     * @param jcpjmc 
     * @return
     */
    public Page<PartsUnRegisterVo> findUnRegisterPartsList(Integer start, Integer limit, String workPlanId, String jcpjmc) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select jcpjbm ,jcpjmc , ");
        sb.append(" sum(case when dataSource = '01' then 1 else 0 end ) as countQD , ");
        sb.append(" sum(case when dataSource = '02' then 1 else 0 end ) as countDJ from ( ");
        sb.append(" select jj.jcpjbm ,jj.jcpjmc ,jj.pym,opl.work_plan_idx as workPlanId , '01' as dataSource from JXGC_Off_Parts_List opl ");
        sb.append(" inner join T_JCBM_JCPJZD jj on opl.parts_idx = jj.jcpjbm ");
        sb.append(" where opl.record_status = 0 ");
        sb.append("  union all ");
        sb.append(" select jj.jcpjbm ,jj.jcpjmc ,jj.pym, pur.rdp_idx as workPlanId , '02' as dataSource from PJWZ_PARTS_UNLOAD_REGISTER pur  ");
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
     * <li>说明：联合查询大部件与配件相关信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start
     * @param limit
     * @param workPlanId
     * @return
     */
    public Page<PartsUnloadRegisterNewBean> findPartsUnloadRegisterAll(Integer start, Integer limit, String workPlanId) {
        // 班组为当前系统操作员所在班组
        Long team = SystemContext.getOmOrganization().getOrgid();
        String sql = SqlMapUtil.getSql("pjwl-query:findPartsUnloadRegisterAll")
                    .replace("#RDP_IDX#", workPlanId);
        sql += "order by t.seq_no , jcpjbm nulls last";
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        return this.queryPageList(totalSql, sql, start, 1000, false, PartsUnloadRegisterNewBean.class);
    }
    
    /**
     * <li>说明：查询未登记的下车配件列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start
     * @param limit
     * @param workPlanId
     * @return
     */
    public Page<PartsUnloadRegisterNewBean> findPartsNotRegisterAll(Integer start, Integer limit, String workPlanId) {
        // 班组为当前系统操作员所在班组
        Long team = SystemContext.getOmOrganization().getOrgid();
        String sql = SqlMapUtil.getSql("pjwl-query:findPartsUnloadRegisterAll")
                    .replace("#RDP_IDX#", workPlanId);
        sql += " and t.idx is null ";
        sql += "order by t.seq_no , jcpjbm nulls last";
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        return this.queryPageList(totalSql, sql, start, 1000, false, PartsUnloadRegisterNewBean.class);
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 处理的数据
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateUnloadRegisterForCancelNew(String[] ids) throws BusinessException, NoSuchFieldException {
        
        // 先进行验证，然后再撤销操作
        for (String id : ids) {
            PartsUnloadRegister register = getModelById(id);
            try {
                
                PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIDX());// 取得配件信息实体
                if (null == account || null == register.getIsInRange()) {
                    throw new BusinessException("数据异常，请刷新后重试！");
                }
                if (!PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus()) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG) || Constants.DELETED == account.getRecordStatus()) {
                    throw new BusinessException("只有【待修】且不在库的配件才能撤销！");
                }
            } catch (Exception e) {
                    // TODO: handle exception
                e.printStackTrace();
            }

        }
        
        for (String id : ids) {
            updateUnloadRegisterForCancelNew(id);
        }
    }

 

    
//    /**
//     * <li>说明：查询配件下车列表
//     * <li>创建人：张迪
//     * <li>创建日期：2016-6-22
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param searchEntity 查询条件
//     * @return 列表分页对象
//     * @throws BusinessException
//     */
//    public Page<PartsUnloadRegisterBean> queryPageList(final SearchEntity<PartsUnloadRegister> searchEntity) throws BusinessException{
//        PartsUnloadRegister entity = searchEntity.getEntity();
//        StringBuilder sb = new StringBuilder();
//        sb.append("select t.*, a.Parts_Status_Name, a.Parts_Status From PJWZ_Parts_Unload_Register t, PJWZ_PARTS_ACCOUNT a Where t.PARTS_ACCOUNT_IDX = a.idx ");
//        // 查询条件 -  检修任务单主键
//        if (!StringUtil.isNullOrBlank(entity.getRdpIdx())) {
//            sb.append(" And t.RDP_IDX ='").append(entity.getRdpIdx()).append("'");
//        }     
//         // 查询条件 - 配件名称
//        if (!StringUtil.isNullOrBlank(entity.getPartsName())) {
//            sb.append(" And t.PARTS_NAME like'%").append(entity.getPartsName()).append("%'");
//        }   
//        sb.append(" order by t.PARTS_NAME");
//        String hql = sb.toString();
//        String totalHql = "Select count(*) as rowcount " + hql.substring(hql.indexOf("From"));
//        
//        return this.queryPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit(), false, PartsUnloadRegisterBean.class);
//    }
//    
}