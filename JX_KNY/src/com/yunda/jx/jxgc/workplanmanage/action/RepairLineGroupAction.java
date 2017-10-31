package com.yunda.jx.jxgc.workplanmanage.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.manager.RepairLineGroupManager;


/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 流水线排程控制器类
 * <li>创建人：程锐
 * <li>创建日期：2016-6-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
public class RepairLineGroupAction  extends JXBaseAction<JobProcessNode, JobProcessNode, RepairLineGroupManager>{

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());    

    /**
     * <li>说明：流水线排程列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void workStationOrderList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String parentIDX = req.getParameter("parentIDX");
            String type = req.getParameter("type");
            map = this.manager.getRepairInfoForChild(parentIDX, type).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：流水线排程确定方法
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateForDispatchByRepairLine() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workPlanIDX = getRequest().getParameter("workPlanIDX");
            String tecProcessCaseIDX = getRequest().getParameter("tecProcessCaseIDX");
            JobProcessNode[] nodeCases = (JobProcessNode[]) JSONUtil.read(getRequest(), JobProcessNode[].class);
            manager.updateForDispatch(nodeCases, workPlanIDX, tecProcessCaseIDX);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
