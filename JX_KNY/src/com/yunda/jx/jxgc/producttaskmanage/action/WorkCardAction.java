package com.yunda.jx.jxgc.producttaskmanage.action; 


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardHandle;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.util.PerformanceMonitor;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCard控制器, 作业工单
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkCardAction extends JXBaseAction<WorkCard, WorkCard, WorkCardManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
    
    private static final String STATUS = "status";

	/**
	 * <li>方法名称：foremanDispatcher
	 * <li>方法说明：工长派工 
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-1-8 下午05:18:17
	 * <li>修改人：
	 * <li>修改内容：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void foremanDispatcher() throws IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            String empids = getRequest().getParameter("empids");
            String workCardIdx = getRequest().getParameter("workCardIdx");
            AcOperator ac = SystemContext.getAcOperator();
            this.manager.getDispatcher().updateForemanDispater(workCardIdx, empids, ac.getOperatorid()); 
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
	
	/**
	 * <li>方法说明：默认上次派工 
	 * <li>方法名称：defaultLastTimeWorker
	 * <li>@throws JsonMappingException
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-7-19 下午03:20:58
	 * <li>修改人：
	 * <li>修改内容：
	 */
	public void defaultLastTimeWorker() throws JsonMappingException, IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            String workCardIdx = getRequest().getParameter("workCardIdx");
            this.manager.getDispatcher().updateDefaultLastTimeWorker(workCardIdx);
            map.put(STATUS, Constants.SUCCESS);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
    
	/**
	 * <li>方法名称：findWorkCard
	 * <li>方法说明：查询作业卡 (工单)
	 * <li>@throws IOException
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-1-9 下午05:22:57
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
    public void findWorkCard() throws IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String search = req.getParameter("search");
            OmEmployee emp = SystemContext.getOmEmployee();
            map = this.manager.findWorkCardByWorkBySearch(req.getParameter(Constants.ENTITY_JSON),search, getStart(), getLimit(), getOrders(), emp.getEmpid()).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
    
	/**
	 * <li>说明：查询已处理的作业工单
	 * <li>创建人：何涛
	 * <li>创建日期：2016-5-6
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void findFinishedWorkCard() throws IOException{
	    Map<String, Object> map = new HashMap<String,Object>();
	    try {
            String entityJson = StringUtil.nvlTrim(getRequest().getParameter(Constants.ENTITY_JSON), "{}");
            String search = StringUtil.nvlTrim(getRequest().getParameter("search"), "");
            WorkCard entity = JSONUtil.read(entityJson, WorkCard.class);
            SearchEntity<WorkCard> searchEntity = new SearchEntity<WorkCard>(entity, getStart(), getLimit(), getOrders());
	        map = this.manager.findFinishedWorkCardBySearch(searchEntity,search).extjsStore();
	    } catch (Exception e) {
	        ExceptionUtil.process(e, logger, map);
	    } finally {
	        JSONUtil.write(this.getResponse(), map);
	    }
	}
	
	/**
     * <li>方法名称：queryWorkCard
     * <li>方法说明：查询工长派工分页列表
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-1-9 下午05:22:57
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public void queryWorkCard() throws IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String foreman = req.getParameter("foreman");
            map = this.manager.findWorkCard(req.getParameter(Constants.ENTITY_JSON), getStart(), getLimit(), getOrders(), foreman, null).extjsStore();
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：工长派工-全部批量派工
     * <li>创建人：程锐
     * <li>创建日期：2013-11-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
	public void foremanAllDispatcher() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String empids = req.getParameter("empids");
			AcOperator ac = SystemContext.getAcOperator();
			this.manager.getDispatcher().foremanAllDispatcher(req.getParameter(Constants.ENTITY_JSON), empids, ac.getOperatorid());
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
    
	/**
	 * <li>方法名称：complete
	 * <li>方法说明：完工 
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-2-4 下午05:06:18
	 * <li>修改人：
	 * <li>修改内容：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void complete() throws JsonMappingException, IOException{
		PerformanceMonitor.begin(logger, true, "WorkCardAction.complete");
		Map<String, Object> map = new HashMap<String,Object>();
	    try {
            String entityJson = StringUtil.nvlTrim(getRequest().getParameter(Constants.ENTITY_JSON), "{}");
            WorkCardHandle entity = JSONUtil.read(entityJson, WorkCardHandle.class);
            QCResultVO[] result = JSONUtil.read(getRequest(), QCResultVO[].class);
            Date realBeginTime = entity.getRealBeginTime();
            if (null == realBeginTime) {
                throw new NullPointerException("工单处理实际开始时间为空！");
            }
            // 验证实际结束时间不能为空
            Date realEndTime = entity.getRealEndTime();
            if (null == realEndTime) {
                throw new NullPointerException("工单处理实际结束时间为空！");
            }
	        if(!StringUtil.isNullOrBlank(entity.getIdx())) {
				OmEmployee emp = SystemContext.getOmEmployee();
				this.manager.updateCompleteWorkCard(entity, emp.getEmpid(), result);
				map.put(STATUS, Constants.SUCCESS);
	        } else {
				map.put(STATUS, "failure");
				map.put("ex", "请求参数不全");
			}
	    } catch (Exception ex) {
			ex.printStackTrace();
			logger.error("WorkCardAction.complete：异常:" + ex + "\n原因" + ex.getMessage());
			map.put(STATUS, "failure");
			map.put("ex", ex + ":" + ex.getMessage());
		} finally {
			PerformanceMonitor.end(logger, "【web端批量完工】", true, "WorkCardAction.complete");
			JSONUtil.write(this.getResponse(), map);
		}
	}
    
    /**
     * <li>说明：提交工单
     * <li>创建人：程锐
     * <li>创建日期：2015-7-7
     * <li>修改人：何涛
     * <li>修改日期：2016-04-12
     * <li>修改内容：重构
     * @throws JsonMappingException
     * @throws IOException
     */
    public void completeWorkCard() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 作业工单处理实体
            String entityJson = StringUtil.nvlTrim(getRequest().getParameter(Constants.ENTITY_JSON), "{}");
            WorkCardHandle entity = JSONUtil.read(entityJson, WorkCardHandle.class);
            
            // 质量检验结果值对象
            QCResultVO[] result = JSONUtil.read(getRequest(), QCResultVO[].class);

            // 作业任务及检测结果
            String workTaskAndDetect = StringUtil.nvl(getRequest().getParameter("workTaskAndDetect"), "[]");
            WorkTaskBean[] workTaskAndDetects = JSONUtil.read(workTaskAndDetect, WorkTaskBean[].class);
            
            // 提交工单
            this.manager.finishWorkCard(entity, result, workTaskAndDetects);
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存工单（暂存）（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2016-04-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveWorkCard() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 作业工单处理实体
            String entityJson = StringUtil.nvlTrim(getRequest().getParameter(Constants.ENTITY_JSON), "{}");
            WorkCardHandle entity = JSONUtil.read(entityJson, WorkCardHandle.class);
            
            // 质量检验结果值对象
            QCResultVO[] result = JSONUtil.read(getRequest(), QCResultVO[].class);
            
            // 作业任务及检测结果
            String workTaskAndDetect = StringUtil.nvl(getRequest().getParameter("workTaskAndDetect"), "[]");
            WorkTaskBean[] workTaskAndDetects = JSONUtil.read(workTaskAndDetect, WorkTaskBean[].class);
            
            // 提交工单
            this.manager.saveWorkCard(entity, result, workTaskAndDetects);
            map.put(Constants.SUCCESS, true);
        } catch (Exception ex) {
            ExceptionUtil.process(ex, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据流程实例id查询作业卡信息
     * <li>创建人：程梅
     * <li>创建日期：2013-3-18
     * <li>修改人： 张凡
     * <li>修改日期：2014/3/27
     * <li>修改内容：将接收参数从processInstID换成workCardIdx
     * @param 参数名：参数说明
     * @throws 抛出异常列表
     */
    public void getCardInfo() throws JsonMappingException, IOException {
        String workCardIdx = getRequest().getParameter("workcardIdx");
        if (StringUtils.isNotBlank(workCardIdx)) {
            WorkCard entity = this.manager.getEntityRdpInfo(workCardIdx);
            JSONUtil.write(getResponse(), entity);
        }
    }
    
    /**
     * <li>方法说明：删除作业工单（作业计划编辑） 
     * <li>方法名称：deleteByWorkPlan
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-9-23 上午09:35:55
     * <li>修改人：
     * <li>修改内容：
     */
    public void deleteByWorkPlan() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String idxs = req.getParameter("ids");
            this.manager.updateDeleteWork(idxs);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：新增编辑自定义工单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void saveDefineWork() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkCard[] w = (WorkCard[])JSONUtil.read(getRequest(), WorkCard[].class);
            QCResult[] qc = null;
            if (!StringUtil.isNullOrBlank(getRequest().getParameter("qualityControls"))) {
                qc = (QCResult[])JSONUtil.read(getRequest().getParameter("qualityControls"), QCResult[].class);
            }            
            this.manager.editDefineWork(w, qc);
            
            map.put(Constants.SUCCESS, true);
            map.put("entity", w[0]);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            map.put(Constants.ERRMSG, e.getMessage());
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据作业工单主键获取作业工单实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            WorkCard workCard = this.manager.getModelById(id);
            map.put("entity", workCard);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：更新作业工单所属流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void changeNode() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        String errMsg = "";
        try {            
            String oldNodeIDX = getRequest().getParameter("oldNodeIDX");
            errMsg = this.manager.changeNode(ids, id, oldNodeIDX);
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
    /**
     * <li>说明：更具作业工单IDX，动态查询QC_RESULT和jxgc_qc_participant的质检人员处理情况
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findQCNameByWorkCardIDX() throws Exception{
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        try {            
            String workCardIDX = getRequest().getParameter("workCardIDX");
            list = this.manager.findQCNameByWorkCardIDX(workCardIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(this.getResponse(), Page.extjsStore(list));
        }       
    }
    
    /**
     * <li>说明：批量完工（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2016-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void finishBatchWorkCards() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            // 作业工单处理实体
            String entityJson = StringUtil.nvlTrim(getRequest().getParameter(Constants.ENTITY_JSON), "{}");
            WorkCard entity = JSONUtil.read(entityJson, WorkCard.class);
            
            // 质量检验结果值对象
            QCResultVO[] result = JSONUtil.read(getRequest(), QCResultVO[].class);
            
            // 提交工单
            this.manager.finishBatchWorkCards(ids, entity, result);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：更具机车检修作业计划-检修活动IDX查询机车检修作业计划-检修活动下的所有检修记录卡详情
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人：张迪
     * <li>修改日期：2016-6-23
     * <li>修改内容：
     * @throws Exception
     */
    public void findWorkCardInfoByWorkPlanRepairActivityIDX() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        try {            
            String workPlanRepairActivityIDX = getRequest().getParameter("workPlanRepairActivityIDX");          
            List<WorkCardBean> workCardBeanList = this.manager.findWorkCardInfoByWorkPlanRepairActivityIDX(workPlanRepairActivityIDX);
            map.put("workCardBeanList", workCardBeanList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger,map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }
    
}