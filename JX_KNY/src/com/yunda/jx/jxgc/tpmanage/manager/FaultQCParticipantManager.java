package com.yunda.jx.jxgc.tpmanage.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.base.tpqcitemdefine.entity.TPQCItemDefine;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCParticipant;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultQCParticipant业务类,提票质量检查参与者
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value = "faultQCParticipantManager")
public class FaultQCParticipantManager extends JXBaseManager<FaultQCParticipant, FaultQCParticipant> {
    
    /**
     * <li>说明：保存指派的提票质量检查参与者
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIDX 提票单IDX
     * @param checkPersonID 指派人员ID
     * @param checkPersonName 指派人员姓名
     * @param checkItemCode 质检项编码
     */
    public void saveIsAssignParticiant(String tpIDX, 
                                       String checkPersonID, 
                                       String checkPersonName, 
                                       String checkItemCode) {
        String sql = SqlMapUtil.getSql("jxgc-tp:saveIsAssignParticiant")
                                .replace("#tpIDX#", tpIDX)
                                .replace("#CHECKPERSONID#", checkPersonID)
                                .replace("#CHECKPERSONNAME#", checkPersonName)
                                .replace("#CHECKITEMCODE#", checkItemCode);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：根据作业班组和质检项配置保存质检项参与者
     * <li>创建人：程锐
     * <li>创建日期：2014-11-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param tpIDX 提票单IDX
     * @param checkOrgID 作业班组
     */
    public void saveNotAssignParticiant(String tpIDX, String checkOrgID) {
        String sql = SqlMapUtil.getSql("jxgc-tp:saveNotAssignParticiant")
                                .replace("#tpIDX#", tpIDX)
                                .replace("#CONST_INT_IS_ASSIGN_N#", TPQCItemDefine.CONST_INT_IS_ASSIGN_N + "")
                                .replace("#STATUS_WKF#", FaultQCResult.STATUS_WKF + "")
                                .replace("#CHECKORGID#", checkOrgID);
        daoUtils.executeSql(sql);
    }
}
