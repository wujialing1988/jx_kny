package com.yunda.jx.pjwz.wellpartsmanage.exwh.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
import com.yunda.jx.pjwz.wellpartsmanage.exwh.entity.WellPartsExwh;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity.PartsStock;
import com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.manager.PartsStockManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WellPartsExwh业务类,良好配件出库单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="wellPartsExwhManager")
public class WellPartsExwhManager extends JXBaseManager<WellPartsExwh, WellPartsExwh>{
    
	/** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    @Resource
    /** PartsStock业务类，良好配件库存信息 */
    private PartsStockManager partsStockManager ;
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<WellPartsExwh> 分页对象
     * @throws BusinessException
     */
    public Page<WellPartsExwh> findPageList(SearchEntity<WellPartsExwh> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From WellPartsExwh t where t.recordStatus=0 ");
        WellPartsExwh register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.whName like '%").append(identificationCode)
            .append("%' or t.acceptOrg like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
//        if(!StringUtil.isNullOrBlank(whTime)){
//            awhere.append(" and substr(to_char(t.whTime,'yyyy-MM-dd'),0,7) = '" + whTime + "'");
//        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.whTime DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：保存良好配件出库单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param exwh 配件出库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsExwh(WellPartsExwh exwh) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(exwh.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+exwh.getPartsNo()+"的配件不存在，请重新添加！");
        }
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
//        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
//            throw new BusinessException("只有【良好在库】的配件才能出库！");
//        }
        // V3.2.8版本，在库的都可出库
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
            throw new BusinessException("只有【在库】的配件才能出库！");
        }
        //如果出库去向为上车。则需判断配件状态是否为良好，只有良好配件才能上车
        if(WellPartsExwh.TO_GO_SC.equals(exwh.getToGo()) && !account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH)){
            throw new BusinessException("只有【良好】的配件才能上车出库！");
        }
        //日志描述=接收部门+领件人
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(exwh.getAcceptOrg())) logDesc = exwh.getAcceptOrg();
        if(!StringUtil.isNullOrBlank(exwh.getAcceptEmp())) logDesc = logDesc + " " + exwh.getAcceptEmp();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJCK, logDesc);
        log = partsManageLogManager.initLog(log, account);
        exwh.setWhTime(new Date());//领件日期为当前日期
        //更新配件周转台账
        updateAccount(exwh , account);
        //更新良好配件库存信息
        updatePartsStock(exwh);
        exwh.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        super.saveOrUpdate(exwh);
        log.eventIdx(exwh.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param exwh 配件出库信息
     * @param account 配件周转台账信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateAccount(WellPartsExwh exwh, PartsAccount account) throws BusinessException, NoSuchFieldException {
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        //设置责任部门信息和存放地点
        if(null != exwh.getAcceptOrgID()){
            account.setManageDeptId(exwh.getAcceptOrgID().toString());  //责任部门id为接收部门id
        }else {
            account.setManageDeptId("");  //责任部门id为空
        }
        account.setManageDept(exwh.getAcceptOrg()); //责任部门名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_ORG);//责任部门类型为机构
        account.setManageDeptOrgseq("");//责任部门序列为空
        account.setLocation(exwh.getDeliverLocation());//存放地点为配送地址
        account.setPartsStatusUpdateDate(exwh.getWhTime());//配件状态更新时间为领件日期
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
//        account.setPartsStatus(PartsAccount.PARTS_STATUS_LH);//配件状态为良好不在库
//        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_LH, "良好"));
        //V3.2.8版本，出库去向为校验，则配件状态更新为待校验；出库去向为检修，则配件状态更新为待修
        //去向为上车，不更改配件状态
//        if(WellPartsExwh.TO_GO_SC.equals(exwh.getToGo())){
//            account.setPartsStatus(PartsAccount.PARTS_STATUS_YSC);//配件状态为已上车
//            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_YSC, PartsAccount.PARTS_STATUS_YSC_CH)); 
//        }
        if(WellPartsExwh.TO_GO_JY.equals(exwh.getToGo())){
            account.setPartsStatus(PartsAccount.PARTS_STATUS_DJY);//配件状态为待校验
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DJY, PartsAccount.PARTS_STATUS_DJY_CH)); 
        }else if(WellPartsExwh.TO_GO_JX.equals(exwh.getToGo())){
            account.setPartsStatus(PartsAccount.PARTS_STATUS_DX);//配件状态为待修
            account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", PartsAccount.PARTS_STATUS_DX, PartsAccount.PARTS_STATUS_DX_CH)); 
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
            
    }
    
    /**
     * <li>说明：更新良好配件库存信息
     * <li>创建人：程梅
     * <li>创建日期：2015-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param exwh 良好配件信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsStock(WellPartsExwh exwh) throws BusinessException, NoSuchFieldException {
        PartsStock stock = new PartsStock();
        stock = partsStockManager.getByPartsAccountIDX(exwh.getPartsAccountIDX());
        if(stock != null){
            stock = EntityUtil.setSysinfo(stock);
            stock = EntityUtil.setNoDelete(stock);
            stock.setGetEmpId(exwh.getAcceptEmpId()); 
            stock.setGetEmp(exwh.getAcceptEmp());//出库领件人
            stock.setGetOrgId(exwh.getAcceptOrgID());
            stock.setGetOrg(exwh.getAcceptOrg());//领件部门
            stock.setExWhEmpId(exwh.getHandOverEmpId());
            stock.setExWhEmp(exwh.getHandOverEmp());//出库人
            stock.setExWhDate(exwh.getWhTime());//出库日期
            this.daoUtils.getHibernateTemplate().saveOrUpdate(stock);
        }
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
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsExwhForCancel(String id) throws BusinessException, NoSuchFieldException {
            WellPartsExwh exwh = getModelById(id);
            if(null != exwh){
                PartsAccount account = partsAccountManager.getModelById(exwh.getPartsAccountIDX()); //查询该配件周转台账信息
                //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
                //if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG) || Constants.DELETED == account.getRecordStatus()){
                //V3.2.8版本，根据出库去向和配件状态验证是否可撤销
                if(WellPartsExwh.TO_GO_SC.equals(exwh.getToGo()) && (!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG))){
                    throw new BusinessException("只有【不在库】、【待校验】、【待修】的配件才能撤销！");
                }else if(WellPartsExwh.TO_GO_JY.equals(exwh.getToGo()) && !PartsAccount.PARTS_STATUS_DJY.equals(account.getPartsStatus())){
                    throw new BusinessException("只有【不在库】、【待校验】、【待修】的配件才能撤销！");
                }else if(WellPartsExwh.TO_GO_JX.equals(exwh.getToGo()) && !PartsAccount.PARTS_STATUS_DX.equals(account.getPartsStatus())){
                    throw new BusinessException("只有【不在库】、【待校验】、【待修】的配件才能撤销！");
                }else{
                    //根据配件id和出库单id查询配件日志信息
                    PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                    //配件状态回滚到出库前
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
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：修改良好配件出库单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param exwh 配件出库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsExwh(WellPartsExwh exwh) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(exwh.getPartsAccountIDX()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+exwh.getPartsNo()+"的配件不存在，请重新添加！");
        }
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_ORG)){
            throw new BusinessException("只有【良好不在库】的配件才能修改，请重新添加！");
        }
        //更新配件周转台账
        updateAccount(exwh , account);
        exwh.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        exwh = EntityUtil.setSysinfo(exwh);
        //设置逻辑删除字段状态为未删除
        exwh = EntityUtil.setNoDelete(exwh);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(exwh);
    }
    
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【良好在库】配件周转台账信息
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
        //良好在库状态的配件能出库
        //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好
//        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_LH) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)) ){
//            throw new BusinessException("只有【良好在库】的配件才能出库，请重新添加！");
//        }
        //V3.2.8版本，在库的都可出库
        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)) ){
            throw new BusinessException("只有【在库】的配件才能出库，请重新添加！");
        }
        return accountV ;
    }
    /**
     * 
     * <li>说明：根据配件编号和规格型号查询【在库】配件周转台账信息列表
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
              if (null != entity && (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !entity.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH))) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只有【在库】的配件才能出库，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * 【已处理】FIXME 代码审查[何涛2016-04-08]：不规范的异常处理方式
     * <li>说明：配件出库登记【web端/手持终端批量出库】
     * <li>创建人：程梅
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 配件出库信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveWellPartsExwhBatch(WellPartsExwh[] registers) throws BusinessException, NoSuchFieldException {
        for (WellPartsExwh register : registers) {
            savePartsExwh(register);
        }
    }
    
    /**
     * <li>说明：配件出库登记确认
     * <li>创建人：程梅
     * <li>创建日期：2015-12-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 配件出库登记单id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateWellPartsExwhForCheck(String[] ids) throws BusinessException, NoSuchFieldException {
        List<WellPartsExwh> wellPartsExwhList = new ArrayList<WellPartsExwh>();
        WellPartsExwh wellPartsExwh;
        for (String id : ids) {
            wellPartsExwh = new WellPartsExwh();
            wellPartsExwh = getModelById(id);
            if(null != wellPartsExwh){
                wellPartsExwh.setStatus(PjwzConstants.STATUS_CHECKED);//配件出库登记状态为已确认
                wellPartsExwhList.add(wellPartsExwh);
            }
        }
        this.saveOrUpdate(wellPartsExwhList);
    }
    
}