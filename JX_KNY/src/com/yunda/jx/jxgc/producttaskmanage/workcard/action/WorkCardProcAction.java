package com.yunda.jx.jxgc.producttaskmanage.workcard.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.jxgc.producttaskmanage.workcard.manager.WorkCardProcManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkCardBean;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 作业工单处理控制器类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class WorkCardProcAction extends JXBaseAction<WorkCard, WorkCard, WorkCardProcManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
    
    private static final String ID = "id";
    private static final String MODE = "mode";
    private static final String IDX = "idx";
    
	/**
	 * <li>说明：全部完工(最多执行100条)
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void completeAllWorkCard() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			QCResultVO[] result = JSONUtil.read(getRequest(), QCResultVO[].class);
			Page page = new Page();
            String otherWorkerIDS = StringUtil.nvlTrim(getRequest().getParameter("workerID"), "");
			page.setSuccess(this.getManager().completeAllWorkCard(operatorid, result, otherWorkerIDS));
			map = page.extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：完工、批量完工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void completeWorkCard() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String idxs = getRequest().getParameter("idxs");
			String remarks = StringUtil.nvlTrim(getRequest().getParameter("remarks"), "");
			QCResultVO[] result = JSONUtil.read(getRequest(), QCResultVO[].class);
			Page page = new Page();
            String otherWorkerIDS = StringUtil.nvlTrim(getRequest().getParameter("workerID"), "");
			page.setSuccess(this.getManager().completeWorkCard(operatorid, idxs, remarks, result, otherWorkerIDS));
			map = page.extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}

	}

	/**
	 * 
	 * <li>说明：查询检测项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryDetectResultList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String workTaskIdx = getRequest().getParameter("workTaskIdx");
			map = this.getManager().queryDetectResultList(workTaskIdx, getStart(), getLimit()).extjsResult();
			map.put(ID, IDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}

	/**
	 * <li>说明：查询已处理作业任务列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人：何涛
	 * <li>修改日期：2016-04-14
	 * <li>修改内容：删除无用的operatorId参数
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryHandledWorkTaskList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String workCardIdx = getRequest().getParameter("workCardIdx");
			map = this.getManager().queryHandledWorkTaskList(workCardIdx, getStart(), getLimit()).extjsResult();
			map.put(ID, IDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：查询生产任务单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryRdpList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String mode = getRequest().getParameter(MODE);
			if ((JxgcConstants.WORKCARDPROC_HANDLER + "").equals(mode)) {
				map = this.getManager().queryHandlerRdpList(operatorid, getStart(), getLimit()).extjsResult();
			} else if ((JxgcConstants.WORKCARDPROC_RECEIVE + "").equals(mode)) {
				map = this.getManager().queryReceiveRdpList(operatorid, getStart(), getLimit()).extjsResult();
			}			
			map.put(ID, IDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：查询检修活动列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryRepairActivityList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String rdpIdx = getRequest().getParameter("rdpIdx");
			String mode = getRequest().getParameter(MODE);
			if ((JxgcConstants.WORKCARDPROC_HANDLER + "").equals(mode)) {
				map = this.getManager().queryHandlerRepairActivityList(operatorid, rdpIdx, getStart(), getLimit());
			} else if ((JxgcConstants.WORKCARDPROC_RECEIVE + "").equals(mode)) {
				map = this.getManager().queryReceiveRepairActivityList(operatorid, rdpIdx, getStart(), getLimit());
			}	
			map.put(ID, IDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：查询工艺节点列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 王治龙
	 * <li>修改日期：2014-11-7
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryTecNodeList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<HashMap> list =  new ArrayList<HashMap>() ;
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String rdpIdx = getRequest().getParameter("rdpIdx");
			String parentIdx = StringUtil.nvlTrim(getRequest().getParameter("parentIdx"), "");
			String mode = getRequest().getParameter(MODE);
			list = new ArrayList<HashMap>();
			if ((JxgcConstants.WORKCARDPROC_HANDLER + "").equals(mode)) {
				list = this.getManager().queryHandlerTecNodeList(operatorid, rdpIdx, parentIdx);
			} else if ((JxgcConstants.WORKCARDPROC_RECEIVE + "").equals(mode)) {
				list = this.getManager().queryReceiveTecNodeList(operatorid, rdpIdx, parentIdx);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
	
	/**
	 * 
	 * <li>说明：查询作业工单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryWorkCardList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String searchString = StringUtil.nvlTrim(getRequest().getParameter("searchString"), "");
			String mode = getRequest().getParameter(MODE);
			Page page = new Page();
			if ((JxgcConstants.WORKCARDPROC_HANDLER + "").equals(mode)) {
				page = this.getManager().queryHandlerWorkCardList(searchString, operatorid, getStart(), getLimit());
			} /*else if ((JxgcConstants.WORKCARDPROC_RECEIVE + "").equals(mode)) {
				page = this.getManager().queryReceiveWorkCardList(searchString, operatorid, getStart(), getLimit());
			}*/			
			map = page.extjsResult();
			map.put(ID, IDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * 
	 * <li>说明：查询未处理作业任务列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
     * <li>修改人：何涛
     * <li>修改日期：2016-04-14
     * <li>修改内容：删除无用的operatorId参数
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryHandlerWorkTaskList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String workCardIdx = getRequest().getParameter("workCardIdx");
			map = this.getManager().queryHandlerWorkTaskList(workCardIdx, getStart(), getLimit()).extjsResult();
			map.put(ID, IDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}	
		
	/**
	 * 
	 * <li>说明：查询作业工单信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryWorkCardInfo() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String idx = getRequest().getParameter(IDX);			
			List<WorkCardBean> list = new ArrayList<WorkCardBean>();
			list.add(this.getManager().queryWorkCardInfo(idx));
			map = new Page(list.size(), list).extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}


	/**
	 * 
	 * <li>说明：保存检测项
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveDetectResult() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String idx = getRequest().getParameter(IDX);			
			String value = getRequest().getParameter("value");	
			this.getManager().saveDetectResult(operatorid, idx, value);
			map = new Page().extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：保存检测项并完成作业任务
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 王治龙
	 * <li>修改日期：2014-11-10
	 * <li>修改内容：data 数组的获取方式
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveDetectResultAndWorkTask() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
//			String dataArray = StringUtil.nvlTrim( getRequest().getParameter("data"), "[]" );
	        String[] data = (String[])JSONUtil.read(getRequest(), String[].class); 
			String taskIdx = getRequest().getParameter("taskIdx");	
			String result = getRequest().getParameter("result");	
			String remarks = getRequest().getParameter("remarks");	
			this.getManager().saveDetectResultAndWorkTask(operatorid, data, taskIdx, result, remarks);
			map = new Page().extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * V3.2.1删除
	 * <li>说明：领取全部作业工单
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public void updateReceiveAllWorkCard() throws Exception {
//		Map<String, Object> map = new HashMap<String,Object>();
//		try {
//			Long operatorid = SystemContext.getAcOperator().getOperatorid();
//			String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), "");
//			this.getManager().updateReceiveAllWorkCard(operatorid, searchJson);
//			map = new Page().extjsResult();
//		} catch (Exception e) {
//			ExceptionUtil.process(e, logger, map);
//		} finally {
//			JSONUtil.write(this.getResponse(), map);
//		}
//	}
	/**
	 * V3.2.1删除
	 * <li>说明：领取作业工单
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public void updateReceiveWorkCard() throws Exception {
//		Map<String, Object> map = new HashMap<String,Object>();
//		try {
//			Long operatorid = SystemContext.getAcOperator().getOperatorid();
//			String idxs = getRequest().getParameter("idxs");
//			this.getManager().updateReceiveWorkCard(operatorid, idxs);
//			map = new Page().extjsResult();
//		} catch (Exception e) {
//			ExceptionUtil.process(e, logger, map);
//		} finally {
//			JSONUtil.write(this.getResponse(), map);
//		}
//	}
	/**
	 * 
	 * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getIsAssignCheckItems() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<QCResult> list = new ArrayList<QCResult>();
		try {
			String workCardIDXS = getRequest().getParameter("workCardIDXS");			
			list = this.manager.getIsAssignCheckItems(workCardIDXS);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
	/**
	 * 
	 * <li>说明：获取当前操作者全部完工时需要指派的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getIsAssignCheckItemsForAllComplete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<QCResult> list = new ArrayList<QCResult>();
		try {		
			list = this.manager.getIsAssignCheckItemsForAllComplete(SystemContext.getAcOperator().getOperatorid());
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
	
}