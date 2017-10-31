package com.yunda.jx.pjwz.cs.manager;

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
import com.yunda.jx.pjwz.cs.entity.PartsCSIn;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsCSIn业务类,配件售后入段业务类
 * <li>创建人：程梅
 * <li>创建日期：2016年6月20日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsCSInManager")
public class PartsCSInManager extends JXBaseManager<PartsCSIn, PartsCSIn>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /**
     * <li>说明：分页查询
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return 分页对象
     * @throws BusinessException
     */
    public Page<PartsCSIn> findPageList(SearchEntity<PartsCSIn> searchEntity) throws BusinessException{
        String totalHql = "select count(*) from PartsCSIn t where t.recordStatus=0 ";
        String hql = "From PartsCSIn t where t.recordStatus=0 ";
        String identificationCode = searchEntity.getEntity().getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.acceptDept like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
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
     * <li>说明：配件售后入段登记【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件售后入段信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsCSIn(PartsCSIn register) throws BusinessException, NoSuchFieldException {
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
        PartsCSIn csIn = saveAccount(register);
        csIn.setAcceptTime(new Date());
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null){
            csIn.setTakeOverEmpId(emp.getEmpid());
            csIn.setTakeOverEmp(emp.getEmpname());
        }
        this.saveOrUpdate(csIn);
        //日志描述=接收部门+存放位置
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(csIn.getAcceptDept())) logDesc = csIn.getAcceptDept();
        if(!StringUtil.isNullOrBlank(csIn.getLocationName())) logDesc = logDesc + " " + csIn.getLocationName();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJSHRD, logDesc);
        log.partsAccountIdx(csIn.getPartsAccountIdx());
        log.eventIdx(csIn.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：新增数据到配件台账中【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param register 配件售后入段信息
     * @return PartsCSIn
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public PartsCSIn saveAccount(PartsCSIn register) throws BusinessException, NoSuchFieldException {
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
        account.setPartsStatus(register.getPartsStatus());
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", register.getPartsStatus(), register.getPartsStatusName()));
    
        account.setPartsStatusUpdateDate(register.getAcceptTime());//配件状态更新时间为接收日期
        account.setIsNewParts(PartsAccount.IS_NEW_PARTS_NO);//旧品
        account.setLocation(register.getLocationName());//存放地址
        account.setIdx(null);
        account.setCreateTime(new Date());
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
        register.setPartsAccountIdx(account.getIdx());
        return register;
    }
    /**
     * <li>说明：撤销【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销的配件售后入段主键
     * @return String[] 错误提示
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsCSInForCancel(String id) throws BusinessException, NoSuchFieldException {
                PartsCSIn register = getModelById(id);
              if(null != register){
                  PartsAccount account = this.partsAccountManager.getModelById(register.getPartsAccountIdx());// 取得配件信息实体
                  List<PartsManageLog> logList = partsManageLogManager.getLogListByIdx(register.getPartsAccountIdx()) ;
                  PartsManageLog log = new PartsManageLog();
                  if(null != logList && logList.size() > 0){
                      log = logList.get(0); //获取最新的日志记录
                      //操作类型不为【配件售后入段】，则表示此配件已进行了下一步操作，不能撤销
                      if(!log.getEventType().equals(PartsManageLog.EVENT_TYPE_PJSHRD) || Constants.DELETED == account.getRecordStatus()){
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
     * <li>说明：根据配件编号和规格型号查询最新的【非在册】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
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
}