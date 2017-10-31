package com.yunda.jx.pjwz.partsmanage.manager;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsManageLog业务类,配件管理日志
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "partsManageLogManager")
public class PartsManageLogManager extends JXBaseManager<PartsManageLog, PartsManageLog> {
    /** PartsAccount业务类，配件周转台账---配件信息 */
    @Resource
    private PartsAccountManager partsAccountManager ;
    /**
     * <li>说明：根据配件id和业务模块id查询最新的一条配件日志信息
     * <li>创建人：程梅
     * <li>创建日期：2015-10-21
     * <li>修改人：何涛
     * <li>修改日期：2015-11-05
     * <li>修改内容：重构
     * @param partsAccountIdx 配件周转台账主键
     * @param eventIdx 业务模块主键
     * @return PartsManageLog 所查配件日志信息
     */
    public PartsManageLog getLogByIdx(String partsAccountIdx, String eventIdx) {
        String hql = "From PartsManageLog Where partsAccountIdx = ? And eventIdx = ? order by eventTime desc ";
        return (PartsManageLog) this.daoUtils.findSingle(hql, new Object[] { partsAccountIdx, eventIdx });
    }
    

    /**
     * <li>说明：重写保存方法，用于设置完善以下字段的信息
     * <ol>
     * <li>设置“操作时间”为当前系统时间；
     * <li>设置“操作人id”为当前系统操作人员id；
     * <li>设置“操作人名称”为当前系统操作人员名称；
     * </ol>
     * <li>创建人：何涛
     * <li>创建日期：2015-11-05
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 配件管理日志实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @Override
    public void saveOrUpdate(PartsManageLog t) throws BusinessException, NoSuchFieldException {
        if (null == t.getEventTime()) {
            t.setEventTime(Calendar.getInstance().getTime());
        }
        if (null == t.getOperator() || null == t.getOperatorId()) {
            AcOperator acOperator = SystemContext.getAcOperator();
            t.setOperator(acOperator.getOperatorname());
            t.setOperatorId(acOperator.getOperatorid() + "");
        }
        super.saveOrUpdate(t);
    }
    
    /**
     * <li>说明：根据配件周转台账id查询配件日志列表
     * <li>创建人：程梅
     * <li>创建日期：2015-11-5
     * <li>修改人：何涛
     * <li>修改日期：2015-11-06
     * <li>修改内容：重构
     * @param partsAccountIdx 配件信息主键
     * @return 配件管理日志实体
     */
    @SuppressWarnings("unchecked")
    public List<PartsManageLog> getLogListByIdx(String partsAccountIdx){
        String hql = "From PartsManageLog Where partsAccountIdx = ? order by eventTime desc";
        return this.daoUtils.find(hql, new Object[]{partsAccountIdx});
    }
    /**
     * 
     * <li>说明：根据业务操作模块主键删除日志信息
     * <li>创建人：程梅
     * <li>创建日期：2015-11-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param eventIdx 业务操作模块主键
     */
    public void deleteLogByEventIdx(String eventIdx) {
        String hql = "delete From PartsManageLog Where eventIdx = '"+eventIdx+"' ";
        this.getDaoUtils().executUpdateOrDelete(hql);
    }
    /**
     * 
     * <li>说明：构造日志信息，记录历史配件信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param log 日志信息
     * @param account 配件周转信息
     * @return 日志信息
     * @throws NoSuchFieldException
     */
    public PartsManageLog initLog(PartsManageLog log, PartsAccount account) throws NoSuchFieldException {
        log.setPartsAccountIdx(account.getIdx());
        log.setPartsStatusHis(account.getPartsStatus());
        log.setPartsStatusNameHis(account.getPartsStatusName());
        log.setManageDeptIdHis(account.getManageDeptId());
        log.setManageDeptHis(account.getManageDept());
        log.setManageDeptOrgseqHis(account.getManageDeptOrgseq());
        log.setManageDeptTypeHis(account.getManageDeptType());
        log.setLocationHis(account.getLocation());
        return log;
    }
    /**
     * 
     * <li>说明：从日志中获取历史配件信息构造配件信息
     * <li>创建人：程梅
     * <li>创建日期：2016-5-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param log 日志信息
     * @param account 配件周转信息
     * @return 配件周转信息
     * @throws NoSuchFieldException
     */
    public PartsAccount getAccountFromLog(PartsManageLog log, PartsAccount account) throws NoSuchFieldException {
        account.setPartsStatus(log.getPartsStatusHis());
        account.setPartsStatusName(partsAccountManager.getPartsStatusName("PJWZ_PARTS_ACCOUNT_STATUS", log.getPartsStatusHis(), log.getPartsStatusNameHis()));
        account.setManageDeptId(log.getManageDeptIdHis());
        account.setManageDept(log.getManageDeptHis());
        account.setManageDeptOrgseq(log.getManageDeptOrgseqHis());
        account.setManageDeptType(log.getManageDeptTypeHis());
        account.setLocation(log.getLocationHis());
        return account;
    }
}
