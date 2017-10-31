package com.yunda.jx.jxgc.workplanmanage.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeUpdateApply;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeUpdateApplyBean;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeUpdateApplyManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修计划流程节点调整申请
 * <li>创建人：张迪
 * <li>创建日期：2017-1-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class JobProcessNodeUpdateApplyAction extends JXBaseAction<JobProcessNodeUpdateApply, JobProcessNodeUpdateApply, JobProcessNodeUpdateApplyManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：根据节点主键获取节点实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByNodeIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JobProcessNodeUpdateApply node = this.manager.getEntityByNodeIDX(getRequest().getParameter("nodeIDX"));
            map.put("entity", node);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：申请更新子节点时间往后移
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void updateLeafNodeTimeApply() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobProcessNodeUpdateApply nodeApply = (JobProcessNodeUpdateApply) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.updateLeafNodeTimeApply(nodeApply);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                  
    }
    /**
     * <li>说明：根据父节点主键获取更新记录对象
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findChildrenNodeApplyList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<JobProcessNodeUpdateApply> nodes = this.manager.findChildrenNodeApplyList(getRequest().getParameter("parentIDX"),null);
            map = new Page<JobProcessNodeUpdateApply>(nodes.size(), nodes).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：查询指定节点对应的申请记录
    * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findNodeApplyList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            JobProcessNodeUpdateApply applyNode = (JobProcessNodeUpdateApply)JSONUtil.read(searchJson, entitySearch.getClass());
//            JobProcessNodeUpdateApply applyNode = (JobProcessNodeUpdateApply) JSONUtil.read(getRequest(), entity.getClass());
            List<JobProcessNodeUpdateApplyBean> applyNodes = this.manager.findNodeApplyList(applyNode);
            map = new Page<JobProcessNodeUpdateApplyBean>(applyNodes.size(), applyNodes).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：审核延期申请：确认或取消
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void updateConfirmApply() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobProcessNodeUpdateApply nodeApply = (JobProcessNodeUpdateApply) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.updateConfirmApply(nodeApply);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                  
    }
   
    /**
     * <li>说明：审核延期申请
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void saveApproveParentNode() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String idx = getRequest().getParameter("idx");
            String reason = getRequest().getParameter("reason");
            this.manager.saveApproveParentNode(idx, reason);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                  
    }
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findFirstNodeApplyList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workPlanIDX = getRequest().getParameter("workPlanIDX");
            List<JobProcessNodeUpdateApply> nodes = this.manager.findFirstNodeApplyList(workPlanIDX);
            map = new Page<JobProcessNodeUpdateApply>(nodes.size(), nodes).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
       /**
        * <li>说明：审核一级节点延期申请：确认或取消
        * <li>创建人：张迪
        * <li>创建日期：2017-1-13
        * <li>修改人： 
        * <li>修改日期：
        * <li>修改内容：
        * @param nodeIDX 节点IDX
        * @return 下级节点列表
        */
   public void updateFirstNodeApply() throws Exception {
       Map<String, Object> map = new HashMap<String,Object>();
       try {
           JobProcessNodeUpdateApply nodeApply = (JobProcessNodeUpdateApply) JSONUtil.read(getRequest(), entity.getClass());
           this.manager.updateFirstNodeApply(nodeApply);
           map.put(Constants.SUCCESS, true);
       } catch (Exception e) {
           ExceptionUtil.process(e, logger, map);
       } finally {
           JSONUtil.write(this.getResponse(), map);
       }                  
   }
}
