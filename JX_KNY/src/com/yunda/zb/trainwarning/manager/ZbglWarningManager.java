package com.yunda.zb.trainwarning.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.zb.rdp.zbbill.entity.ZbglRdp;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.tp.entity.ZbglTp;
import com.yunda.zb.trainwarning.entity.ZbglWarning;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglWarning业务类,机车检测预警
 * <li>创建人：程锐
 * <li>创建日期：2015-03-04
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglWarningManager")
public class ZbglWarningManager extends JXBaseManager<ZbglWarning, ZbglWarning> {
    
    /** 机车整备单业务类 */
    @Resource
    ZbglRdpManager zbglRdpManager;
    
    /** 机车整备任务单业务类 */
    @Resource
    ZbglRdpWiManager zbglRdpWiManager;
    
    /** 机车出入段台账查询业务类 */
    @Resource
    TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    
    private static final String NULL_MSG = "的机车检测预警实体为null";
    
    private static final String ERROR_MSG = "的机车检测预警实体状态不为【未处理】,请重新刷新页面";
    
    /**
     * <li>说明：下发班组
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 机车检测预警IDX数组
     * @throws Exception
     */
    public void updateForXfbz(String[] ids) throws Exception {
        List<ZbglWarning> entityList = new ArrayList<ZbglWarning>();
        for (String id : ids) {
            ZbglWarning warning = getModelById(id);
            validateWarning(warning);
            ZbglRdp rdp = null;
            TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainName(warning.getTrainTypeShortName(), warning.getTrainNo());
            if (account != null) {
                rdp = zbglRdpManager.getRunningRdpByAccount(account.getIdx());
                warning.setTrainAccessAccountIDX(account.getIdx());
            }
            if (rdp == null) {
                warning.setWarningStatus(ZbglWarning.STATUS_RELEASE);
            } else {
                warning.setWarningStatus(ZbglWarning.STATUS_RELEASED);
                warning.setRdpIDX(rdp.getIdx());
            }
            warning = buildEntity(warning);
            entityList.add(warning);
        }
        
        saveOrUpdate(entityList);
        daoUtils.flush();
        entityList = zbglRdpWiManager.saveForWarningXfbz(entityList);
        daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：取消预警
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 机车检测预警IDX数组
     * @throws Exception
     */
    public void updateForCancel(String[] ids) throws Exception {
        List<ZbglWarning> entityList = new ArrayList<ZbglWarning>();
        for (String id : ids) {
            ZbglWarning warning = getModelById(id);
            validateWarning(warning);
            warning.setWarningStatus(ZbglWarning.STATUS_CANCEL);
            warning = buildEntity(warning);
            entityList.add(warning);
        }
        saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：机车检测预警转提票生成提票时，需更新机车检测预警活的状态和处理ID
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tp 提票实体
     * @throws Exception 
     */
    public void updateForTp(ZbglTp tp) throws Exception {
        if (StringUtil.isNullOrBlank(tp.getWarningIDX()))
            return;
        ZbglWarning warning = getModelById(tp.getWarningIDX());
        validateWarning(warning);
        TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainName(warning.getTrainTypeShortName(), warning.getTrainNo());
        if (account != null) {
            warning.setTrainAccessAccountIDX(account.getIdx());
        }
        warning.setWarningStatus(ZbglWarning.STATUS_NOTICE);
        warning.setRelIDX(tp.getIdx());
        warning = buildEntity(warning);
        saveOrUpdate(warning);
    }
    
    /**
     * <li>说明：构建机车检测预警实体
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param warning 机车检测预警实体
     * @return 机车检测预警实体
     */
    private ZbglWarning buildEntity(ZbglWarning warning) {
        DefaultUserUtilManager.setDefaultOperator();
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        warning.setHandlePersonID(emp != null ? emp.getEmpid() : null);
        warning.setHandlePersonName(emp != null ? emp.getEmpname() : "");
        warning.setHandleTime(new Date());
        warning.setSiteID(EntityUtil.findSysSiteId(warning.getSiteID()));
        warning.setSiteName(EntityUtil.findSysSiteName(warning.getSiteID(), warning.getSiteName()));
        return warning;
    }
    
    /**
     * <li>说明：验证
     * <li>创建人：程锐
     * <li>创建日期：2015-3-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param warning 机车检测预警实体
     * @throws BusinessException
     */
    private void validateWarning(ZbglWarning warning) throws BusinessException {
        if (warning == null)
            throw new BusinessException("id为" + warning.getIdx() + NULL_MSG);
        if (!warning.getWarningStatus().equals(ZbglWarning.STATUS_TODO))
            throw new BusinessException("id为" + warning.getIdx() + ERROR_MSG);
    }

    /**
     * <li>说明：通过整备单信息存储过程生成预警范围活
     * <li>创建人：林欢
     * <li>创建日期：2016-5-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param param 参数，整备单id
     */
    public void saveZbglWarningByProc(String[] param) {
        daoUtils.executeProc("sp_create_zb_wi_warning", param);
        daoUtils.flush();
    }
}
