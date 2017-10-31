package com.yunda.jx.pjwz.movewh.manager;
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
import com.yunda.jx.pjwz.movewh.entity.PartsMoveWh;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsMoveWh业务层, 配件移库
 * <li>创建人：程梅
 * <li>创建日期：2016年5月31日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@Service(value="partsMoveWhManager")
public class PartsMoveWhManager extends JXBaseManager<PartsMoveWh, PartsMoveWh>{
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    
    /** PartsManageLog业务类,配件管理日志 */
    @Resource
    private PartsManageLogManager partsManageLogManager ;
    
    /**
     * <li>说明：分页查询【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 过滤条件
     * @return Page<PartsMoveWh> 分页对象
     * @throws BusinessException
     */
    public Page<PartsMoveWh> findPageList(SearchEntity<PartsMoveWh> searchEntity) throws BusinessException{
        StringBuilder hql = new StringBuilder(" From PartsMoveWh t where t.recordStatus=0 ");
        PartsMoveWh register = searchEntity.getEntity() ;
        String identificationCode = register.getIdentificationCode() ;
        StringBuffer awhere =  new StringBuffer();
        if(!StringUtil.isNullOrBlank(identificationCode)){
            awhere.append(" and (t.partsNo like '%").append(identificationCode).append("%' or t.partsName like '%").append(identificationCode)
            .append("%' or t.specificationModel like '%").append(identificationCode).append("%' or t.whName like '%").append(identificationCode)
            .append("%' or t.inWhName like '%").append(identificationCode)
            .append("%' or t.identificationCode like '%").append(identificationCode).append("%' )") ;
        }
        Order[] orders = searchEntity.getOrders();
        if(orders != null && orders.length > 0){            
            awhere.append(HqlUtil.getOrderHql(orders));
        }else{
            awhere.append(" order by t.moveTime DESC");
        }
        hql.append(awhere);
        String totalHql = "select count(*) " + hql;
        return super.findPageList(totalHql, hql.toString(), searchEntity.getStart(), searchEntity.getLimit());
    }
    /**
     * <li>说明：根据配件编号和规格型号查询最新的【在库】配件周转台账信息
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
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
        //在库的都可移库
        if(null != accountV && (!accountV.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !accountV.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)) ){
            throw new BusinessException("只有【在库】的配件才能移库，请重新添加！");
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
      if(null == entityList){
          account.setPartsNo(account.getIdentificationCode());
          account.setIdentificationCode("");
          entityList = this.partsAccountManager.getAccountList(account);
      }
      if(null != entityList && entityList.size() > 0){
          for(PartsAccount entity : entityList){
              if (null != entity && (!entity.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !entity.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH))) {
                  entityList.remove(entity);
              }
          }
          if(null != entityList && entityList.size() > 0){
              return entityList ;
          }else{
              throw new BusinessException("只有【在库】的配件才能移库，请重新添加！");
          }
      }else throw new BusinessException("此配件未登记！");
  }
    /**
     * <li>说明：保存移库单信息【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param movewh 配件移库信息
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsMoveWh(PartsMoveWh movewh) throws BusinessException, NoSuchFieldException {
        PartsAccount account = partsAccountManager.getModelById(movewh.getPartsAccountIdx()); //查询该配件周转台账信息
        if(null == account){
            throw new BusinessException("配件编号为"+movewh.getPartsNo()+"的配件不存在，请重新添加！");
        }
        // 在库的都可移库
        if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
            throw new BusinessException("只有【在库】的配件才能移库！");
        }
        //移出库房和移入库房不能相同
        if(account.getManageDeptId().equals(movewh.getInWhIdx())){
            throw new BusinessException("移出库房和移入库房不能相同！");
        }
        //日志描述=移入库房
        String logDesc = "";
        if(!StringUtil.isNullOrBlank(movewh.getInWhName())) logDesc = movewh.getInWhName();
        PartsManageLog log = new PartsManageLog(PartsManageLog.EVENT_TYPE_PJYK, logDesc);
        log = partsManageLogManager.initLog(log, account);
        //更新配件周转台账
        movewh = updateAccount(movewh , account);
        movewh.setStatus(PjwzConstants.STATUS_ED);//单据状态为已登帐
        movewh.setMoveTime(new Date());//移库日期为当前日期
        super.saveOrUpdate(movewh);
        log.eventIdx(movewh.getIdx());
        partsManageLogManager.saveOrUpdate(log);//新增配件管理日志
    }
    
    /**
     * <li>说明：更新配件周转台账【用于手持终端】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param movewh 配件移库信息
     * @param account 配件周转台账信息
     * @return PartsMoveWh 实体对象
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public PartsMoveWh updateAccount(PartsMoveWh movewh, PartsAccount account) throws BusinessException, NoSuchFieldException {
        //移出库房为所选配件的责任部门信息
        if(StringUtil.isNullOrBlank(movewh.getWhIdx())){
            movewh.setWhIdx(account.getManageDeptId());
            movewh.setWhName(account.getManageDept());
        }
        movewh.setLocation(account.getLocation());//移出位置为所选配件的存放地址
        
        //设置责任部门信息和存放地点
        account.setManageDeptId(movewh.getInWhIdx());  //责任部门id为移入库房id
        account.setManageDept(movewh.getInWhName()); //责任部门名称为移入库房名称
        account.setManageDeptType(PartsAccount.MANAGE_DEPT_TYPE_WH);//责任部门类型为库房
        account.setManageDeptOrgseq("");//责任部门序列为空
        account.setLocation("");//存放地点为空
        account.setPartsStatusUpdateDate(movewh.getMoveTime());//配件状态更新时间为移库日期
        account = EntityUtil.setSysinfo(account);
        //设置逻辑删除字段状态为未删除
        account = EntityUtil.setNoDelete(account);
        this.daoUtils.getHibernateTemplate().saveOrUpdate(account);
        return movewh;
    }
    /**
     * <li>说明：配件移库登记【web端/手持终端批量移库】
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param registers 配件移库信息数组
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void savePartsMoveWhBatch(PartsMoveWh[] registers) throws BusinessException, NoSuchFieldException {
        for (PartsMoveWh register : registers) {
            savePartsMoveWh(register);
        }
    }
    /**
     * <li>说明：撤销
     * <li>创建人：程梅
     * <li>创建日期：2016年6月1日
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param id 需撤销数据id
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePartsMoveWhForCancel(String id) throws BusinessException, NoSuchFieldException {
        PartsMoveWh movewh = getModelById(id);
        if(null != movewh){
            PartsAccount account = partsAccountManager.getModelById(movewh.getPartsAccountIdx()); //查询该配件周转台账信息
            //在库的配件可撤销
            if(!account.getPartsStatus().startsWith(PartsAccount.PARTS_STATUS_ZC) || !account.getManageDeptType().equals(PartsAccount.MANAGE_DEPT_TYPE_WH)){
                throw new BusinessException("只有【在库】的配件才能撤销！");
            }else{
                //根据配件id和移库单id查询配件日志信息
                PartsManageLog log = partsManageLogManager.getLogByIdx(account.getIdx(), id) ;
                //配件状态回滚到移库前
                account = partsManageLogManager.getAccountFromLog(log, account);
                this.logicDelete(id);    //删除配件移库信息
                partsAccountManager.saveOrUpdate(account);    //更新周转台账信息
                partsManageLogManager.deleteLogByEventIdx(id);//删除日志信息
            }
            
          }else {
              throw new BusinessException("数据有误！");
          }
        }
}