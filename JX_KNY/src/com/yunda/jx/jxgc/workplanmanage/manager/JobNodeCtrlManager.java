package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 流程节点卡控业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-5-7
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "jobNodeCtrlManager")
public class JobNodeCtrlManager extends JXBaseManager<JobProcessNode, JobProcessNode> {
    
    /** 质检查询业务类 */
    @Resource
    private QCResultQueryManager qCResultQueryManager;
    
    /** 提票业务类 */
    @Resource
    private FaultTicketManager faultTicketManager;
    
    /**
     * <li>说明：检查能否完成节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：张迪
     * <li>修改日期：2016-10-11
     * <li>修改内容：添加提票质量检查卡控
     * @param controlMap 质控map
     * @return 成功或卡控信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> finishNodeCtrl(Map controlMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        String ticketControl = CommonUtil.getMapValue(controlMap, JobNodeExtConfigDef.EXT_CHECK_TICKET);
        String qcControl = CommonUtil.getMapValue(controlMap, JobNodeExtConfigDef.EXT_CHECK_CONTROL);
        if (!StringUtil.isNullOrBlank(qcControl) && !JobNodeExtConfigDef.EXT_CHECK_CONTROL_NONE.equals(qcControl)) {
            String nodeIDXS = CommonUtil.getMapValue(controlMap, qcControl);
            if (hasWCLQCByNode(nodeIDXS)) {
                map.put(Constants.ERRMSG, "有未完成的质检项，不能完成节点");
                return map;
            }
        }
        if (!StringUtil.isNullOrBlank(ticketControl) && !JobNodeExtConfigDef.EXT_CHECK_TICKET_NONE.equals(ticketControl)) {
            String WorkPlanIDX = CommonUtil.getMapValue(controlMap, ticketControl);
            if (hasWCLTicketByRdIdx(WorkPlanIDX)) {
                map.put(Constants.ERRMSG, "有未完成的票活，不能完成节点");
                return map;
            }
        }
        map.put(Constants.SUCCESS, "true");
        return map;
    }
    
    
    /**
     * <li>说明：节点是否有未完成的必检质检项
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXS 节点IDXS
     * @return true 有未完成的质检项 false 无
     */
    private boolean hasWCLQCByNode(String nodeIDXS) {
        int qcCount = qCResultQueryManager.getWClQCCountByNode(nodeIDXS);
        return qcCount > 0;
    }
    /**
     * <li>说明：节点是否有未完成的票活
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param WorkPlanIDX 作业计划idx
     * @return true 有未完成的票活 false 无
     */
    private boolean hasWCLTicketByRdIdx(String WorkPlanIDX) {
        int ticketCount = faultTicketManager.getWClTicketCountByRdpIdx(WorkPlanIDX);
        return ticketCount > 0;
    }
}
