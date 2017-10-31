package com.yunda.jx.jxgc.producttaskmanage.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkTask控制器, 作业任务
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkTaskAction extends JXBaseAction<WorkTask, WorkTask, WorkTaskManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    private static final String SUCCESS = "success";
    	
	/**
	 * <li>方法名称：checkWorkTaskAllComplete
	 * <li>方法说明：检查是否还有作业任务需要人工完成并返回作业工单的质检指派项 
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-2 下午04:42:58
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void checkWorkTaskAllComplete() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String workCardIdx = getRequest().getParameter("workCardIdx");
            String canCompleteWorkCard = manager.canCompleteWorkCard(workCardIdx);
            Object[] obj = manager.getWorkCardBeginTime(workCardIdx, SystemContext.getAcOperator().getOperatorid());
            if (obj == null)
                throw new BusinessException("作业工单的作业人员已重新分配，请刷新页面！");            
            map.put("status", SUCCESS);
            map.put("canCompleteWorkCard", canCompleteWorkCard);
            map.put("qcList", manager.getIsAssignCheckItems(workCardIdx));
            map.put("realBeginTime", obj[0]);
            map.put("remarks", obj[1] == null ? "" : obj[1].toString().replaceAll("\n", ""));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
	
	/**
	 * <li>方法名称：getWorkTaskType
	 * <li>方法说明：获取作业任务检修状态字典 
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-7 下午01:40:16
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void getWorkTaskType() throws JsonMappingException, IOException{
	    String dictTypeId = getRequest().getParameter("dictTypeId");
	    JSONUtil.write(getResponse(), this.manager.getRepairTypeDict(dictTypeId));
	}
	
	/**
	 * 重写方法
	 */
	public void saveOrUpdate() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkTask t = (WorkTask)JSONUtil.read(getRequest(), entity.getClass());

            String[] errMsg = this.manager.validateUpdate(t);
            if (errMsg == null || errMsg.length < 1) {
            	QCResult[] qc = null;
                if(getRequest().getParameter("qualityControls") != null){
                    qc = (QCResult[])JSONUtil.read(getRequest().getParameter("qualityControls"), QCResult[].class);
                }
                this.manager.saveOfDefineWork(t, qc);
                
                //返回记录保存成功的实体对象
                map.put("entity", t);  
                map.put(SUCCESS, true);
            } else {
                map.put(SUCCESS, false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
	
	/**
	 * <li>方法说明：获取最大序号 
	 * <li>方法名称：getMaxSeq
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-10-11 下午03:47:33
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void getMaxSeq() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            String workCardIDX = getRequest().getParameter("workCardIDX");
            int seq = 1;
            if(!StringUtil.isNullOrBlank(workCardIDX)){
                seq = this.manager.getMaxSeq(workCardIDX);
            }
            map.put("seq", seq);  
            map.put(SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
	}
    
    
    /**
     * <li>说明：获取作业任务信息（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-22
     * <li>修改人：何涛
     * <li>修改日期：2016-04-14
     * <li>修改内容：优化代码，使用新增的pageQuery方法进行分页查询，由于更新后的作业工单处理方式（作业任务批量提交），pad端已经不再调用该方法
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void findWorkTask() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            // 机车检修作业工单主键
            String workCardIdx = req.getParameter("workCardIdx");
            // 默认查询“未处理”的作业任务
            String[] statusArray = new String[]{ WorkTask.STATUS_WAITINGFORGET, WorkTask.STATUS_INIT };;
            
            String mode = StringUtil.nvl(req.getParameter("mode"), "0");
            // 查询已处理
            if ("1".equals(mode)) {
                statusArray = new String[]{ WorkTask.STATUS_HANDLED };
            }
            map = this.manager.pageQuery(workCardIdx, statusArray, start, limit, getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：完成作业任务
     * <li>创建人：何涛
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveCompleteWorkTask() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            // 作业任务保存实体
            WorkTask workTask = JSONUtil.read(req.getParameter("workTask"), WorkTask.class);
            // 作业任务下属检测项保存数组
            DetectResult[] detectResultArray = JSONUtil.read(req.getParameter("detectResultArray"), DetectResult[].class);
            this.manager.saveCompleteWorkTask(workTask, detectResultArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：检修检测项查询，并将查询结果以HTML形式进行返回，以返回结果在页面进行显示
     * <li>创建人：何涛
     * <li>创建日期：2016-04-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void queryInHTML() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workCardIDX = this.getRequest().getParameter("workCardIDX");
            String html = this.manager.queryInHTML(workCardIDX);
            map.put("html", html);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：动态获取检修检测项的页面输入组件
     * <li>创建人：何涛
     * <li>创建日期：2016-04-07
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void createInHTML() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String workCardIDX = this.getRequest().getParameter("workCardIDX");
            map = this.manager.createInHTML(workCardIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}