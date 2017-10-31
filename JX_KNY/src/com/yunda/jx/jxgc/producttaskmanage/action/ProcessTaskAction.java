package com.yunda.jx.jxgc.producttaskmanage.action; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.ProcessTask;
import com.yunda.jx.jxgc.producttaskmanage.manager.ProcessTaskManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ProcessTask控制器, 检修过程-流程任务单
 * <li>创建人：王治龙
 * <li>创建日期：2013-02-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
public class ProcessTaskAction extends JXBaseAction<ProcessTask, ProcessTask, ProcessTaskManager>{
	
    private static final long serialVersionUID = 1L;
    /** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	
	
	/**
	 * <li>方法名称：findProcessTaskForComplete
	 * <li>方法说明：已处理任务查询 
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-2-28 下午01:57:34
	 * <li>修改人：
	 * <li>修改内容：
	 */
    public void findProcessTaskForComplete() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            map = this.manager.findHandledTaskList(req.getParameter("entityJson"), getStart(), getLimit(), getOrders(),
                SystemContext.getAcOperator().getUserid()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
    
    /**
     * <li>方法说明：新增系统任务数据 
     * <li>方法名称：newSystem
     * <li>业务场景：目前无流程调用此方法
     * <li>@param processInstID
     * <li>@param rdpIdx
     * <li>@throws Exception
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 下午02:15:18
     * <li>修改人：
     * <li>修改内容：
     */
    public void newSystem(Long processInstID, String rdpIdx) throws Exception{
        ProcessTaskManager processTaskManager = (ProcessTaskManager)getManager("processTaskManager");
        processTaskManager.insertSystemTask(processInstID, rdpIdx);
    }
    /**
     * TODO 未发现有流程调用
     * <li>说明：更新流程任务状态为已完成
     * <li>创建人：程梅
     * <li>创建日期：2013-3-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void updateState(String taskid){
    	((ProcessTaskManager)(getManager("processTaskManager"))).updateState(taskid);
    }
    
    /**
     * <li>说明：根据工作项ID查询任务基本信息
     * <li>创建人：张凡
     * <li>创建日期：2013-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void getBeseInfo() throws JsonMappingException, IOException{
		String workitemid = getRequest().getParameter("workitemid");
		String cantFindRdp = getRequest().getParameter("cantQueryRdp");
		if(StringUtils.isNotBlank(workitemid)){
		    ProcessTask entity = null;
		    if(cantFindRdp!= null){
		        entity = this.manager.getEntityByWorkItemId(workitemid);
		    }else{
		        entity = this.manager.getProcessBasicInfo(workitemid);
		    }
			JSONUtil.write(getResponse(), entity);		
		}
	}    
    
    /**
     * <li>说明：当流程完成后, 根据流程实例ID, 更新相应的流程任务单状态
     * <li>业务场景：在多个流程完成后的触发事件中调用
     * <li>创建人：袁健
     * <li>创建日期：2013-3-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public void updateTaskStatusWhenProcessFinished(String processInstId) throws Exception {
    	ProcessTaskManager processTaskManager = (ProcessTaskManager)getManager("processTaskManager");
    	try {
			processTaskManager.updateTaskStatusWhenProcessFinished(processInstId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ProcessTaskAction.updateTaskStatusWhenProcessFinished:" + "异常：" + e + "\n原因：" + e.getCause());
		}
    }
    
    
    /**
     * <li>方法说明：根据传入条件查询流程工单 
     * <li>方法名称：getProcessTaskOfCondition
     * <li>业务场景：目前仅用于提票查询模块的查询流程签名业务
     * <li>@throws Exception
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-3-31 下午02:32:05
     * <li>修改人：
     * <li>修改内容：
     */
    public void getProcessTaskOfCondition() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String field = getRequest().getParameter("field");
            String value = getRequest().getParameter("value");
            String token = getRequest().getParameter("token");
            List<ProcessTask> list = manager.findTaskOfCondition(field, value, token);
            map.put("list", list);
            map.put("success", true);
         } catch (Exception e) {
             ExceptionUtil.process(e, logger, map);
         } finally {
             JSONUtil.write(this.getResponse(), map);
         }
    }
}