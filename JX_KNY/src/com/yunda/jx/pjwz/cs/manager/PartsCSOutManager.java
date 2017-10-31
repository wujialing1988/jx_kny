package com.yunda.jx.pjwz.cs.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.cs.entity.PartsCSOut;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsCSOut业务类,配件售后出段业务类
 * <li>创建人：程梅
 * <li>创建日期：2016年6月20日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsCSOutManager")
public class PartsCSOutManager extends JXBaseManager<PartsCSOut, PartsCSOut>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<WellPartsExwh> 分页对象
     * @throws BusinessException
     */
    public Page<PartsCSOut> findPageList(SearchEntity<PartsCSOut> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsCSOut t where t.recordStatus=0 ");
        PartsCSOut register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.acceptDep like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.outTime DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：保存配件售后出段信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param out 配件售后出段信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsCSOut(PartsCSOut out) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(out.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+out.getPartsNo()+"的配件不存在，请重新添加！");
        }
        //除了在修的在册配件可售后出段
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
            throw new BusinessException("除了在修的在册配件才能售后出段！");
        }
        //日志描述=接收单位
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(out.getAcceptDep())) logDesc = out.getAcceptDep();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJSHCD, logDesc);
        log = partsManageLogManager.initLog(log, account);
        out.setOutTime(new Date());//出段日期为当前日期
        out.setPartsStatus(account.getPartsStatus());
        out.setPartsStatusName(account.getPartsStatusName());
        //更新配件周转台账
        updateAccount(out , account);
        super.saveOrUpdate(out);
        log.eventIdx(out.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param out 配件售后出段信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(PartsCSOut out, PartsAccount account) throws BusinessException, NoSuchFieldException {
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        account.setManageDeptId(out.getAcceptDepCode());  //责任部门id为接收单位编码
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDept(out.getAcceptDep()); //责任部门名称为接收单位
        account.setManageDeptOrgseq("");//责任部门序列为空
        account.setLocation("");//存放地点为空
        account.setPartsStatusUpdateDate(out.getOutTime());//配件状态更新时间为出段日期
        account.setPartsStatus(PartsAccount.PARTS_STATUS_SHCD);//配件状态为售后出段
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_SHCD, PartsAccount.PARTS_STATUS_SHCD_CH));
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
            
    }
    
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsCSOutForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsCSOut out = getModelById(id);
            if(null != out){
                PartsAccount account = partsAccountManager.getModelById(out.getPartsAccountIDX()); //查询该配件周转台账信息
                //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
                //if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG) || Constants.DELETED == account.getRecordStatus()){
                //V3.2.8版本，根据出库去向和配件状态验证是否可撤销
                if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_SHCD) || Constants.DELETED == account.getRecordStatus()){
                    throw new BusinessException("只有【售后出段】的配件才能撤销！");
                }else{
                    //根据配件id和出段单id查询配件日志信息
                    PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                    //配件状态回滚到出段前
                    account = partsManageLogManager.getAccountFromLog(log, account);
                    this.logicDelete(id);    //删除配件出库信息
                    partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                    partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
                }
              }else {
                  throw new BusinessException("数据有误！");
              }
        }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【在册、排除在修】配件周转台账信息
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
        PartsAccount accountV = this.partsAccountManager.getAccount(account);
        if(null == accountV){
            account.setPartsNo(account.getIdentificationCode());
            account.setIdentificationCode("");
            accountV = this.partsAccountManager.getAccount(account);
        }
        if (null == accountV) {
            throw new BusinessException("此配件未登记！");
        }
        //除了在修的在册配件可售后出段
        if(!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)){
            throw new BusinessException("除了在修的在册配件才能售后出段！");
        }   
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【在册、排除在修】配件周转台账信息列表
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
              if(!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZXZ)) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("除在修外的在册配件才能售后出段，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * <li>说明：配件售后初段登记【web端/手持终端批量出段】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月20日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 配件出段信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsCSOutBatch(PartsCSOut[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsCSOut register : registers) {
            savePartsCSOut(register);
        }
    }
}