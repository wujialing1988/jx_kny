package com.yunda.jx.jxgc.workplanmanage.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkCardQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 作业工单查询控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class WorkCardQueryAction extends JXBaseAction<WorkCard, WorkCard, WorkCardQueryManager> {
    
    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取流程节点关联的作业工单列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getWorkCardListByNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
            WorkCard objEntity = (WorkCard) JSONUtil.read(searchJson, WorkCard.class);
            SearchEntity<WorkCard> searchEntity = new SearchEntity<WorkCard>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.getWorkCardListByNode(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：流程节点是否有处理中的工单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void hasOnGoingWorkCardByNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String nodeCaseIDX = getRequest().getParameter("nodeCaseIDX");
            String workPlanIDX = getRequest().getParameter("workPlanIDX");
            List list = manager.getOnGoingCardByNode(nodeCaseIDX, workPlanIDX);
            if (list != null && list.size() > 0) {
                map.put("errMsg", "此节点已在处理，不能进行设置工位！");
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：获取其他作业人员和质检项JSON列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getWorkerAndQcByCard() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workCardIDX = getRequest().getParameter("workCardIDX");
            String empid = getRequest().getParameter("empid");
            List<Worker> workerList = manager.getOtherWorkerByWorkCard(workCardIDX, empid);
            if (workerList != null && workerList.size() > 0) {
                map.put("workerList", JSONUtil.write(workerList));
            }
            List<QCResult> qcList = manager.getIsAssignCheckItems(workCardIDX);
            if (qcList != null && qcList.size() > 0) {
                map.put("qcList", JSONUtil.write(qcList));
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
