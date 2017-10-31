package com.yunda.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.api.IWFProcessInstManager;
import com.eos.workflow.api.IWFRelativeDataManager;
import com.eos.workflow.api.IWFWorkItemManager;
import com.eos.workflow.api.IWFWorklistQueryManager;
import com.eos.workflow.data.WFWorkItem;
import com.eos.workflow.omservice.WFParticipant;
import com.primeton.workflow.api.PageCond;
import com.primeton.workflow.api.WFReasonableException;
import com.primeton.workflow.api.WFServiceException;
import com.yunda.common.BusinessException;
import com.yunda.flow.util.BPSServiceClientUtil;
import com.yunda.util.YDStringUtil;

public abstract class FlowManager {
	
	private static final Logger log = Logger.getLogger(FlowManager.class);
	
	public FlowManager(){
        //获取缺省的ID为"default"的远程uddi服务器信息配置对应的BPS服务客户端实例
        //try {
			//client = BPSServiceClientFactory.getDefaultClient();
		//} catch (WFServiceException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//client = BPSServiceClientFactory.getClient("ServerA");
	}
	/**
	 * 
	 * <li>方法名：bpsServiceClient
	 * <li>@return
	 * <li>返回类型：IBPSServiceClient
	 * <li>说明：通过workItemId查找工作项
	 * <li>创建人：李恒飞
	 * <li>创建日期：2011-7-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * @throws WFServiceException 
	 * @throws WFServiceException 
	 *//*
	public WFWorkItem findWorkItemById(long workItemId,String userId, String userName) throws WFServiceException
	{
		if(JudgeAndSetCurrentUser(userId, userName))
		{
			IWFWorkItemManager mng = BPSServiceClientUtil.getServiceClient().getWorkItemManager();
			return mng.queryWorkItemDetail(workItemId);
		}
		else
		{
			return null;
		}
	}*/

	/**
	 * <li>方法名：startAndFinishFirstWorkItem
	 * <li>@param processInstID 流程实例ID
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：启动并结束第一个工作项
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void startAndFinishFirstWorkItem(long processInstID) throws BusinessException{
		//获取流程实例管理器接口
		IWFProcessInstManager mng = BPSServiceClientUtil.getServiceClient().getProcessInstManager();
		try {
			//启动流程实例，并完成第一个工作项
			mng.startProcessInstAndFinishFirstWorkItem(processInstID, false, new Object[0]);
			log.info("启动流程并结束第一个结点成功....processInstID：" + processInstID);
		} catch (WFServiceException e) {
			log.error("启动流程并结束第一个结点错误", e);
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}
	
	/**
	 * 
	 * <li>方法名：startProcessInst
	 * <li>@param processInstID 流程实例ID
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：启动流程
	 * <li>创建人：李恒飞
	 * <li>创建日期：2011-6-29
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void startProcessInst(long processInstID) throws Exception {
		//获取流程实例管理器接口
		IWFProcessInstManager mng = BPSServiceClientUtil.getServiceClient().getProcessInstManager();
		try {
			//启动流程实例，并完成第一个工作项
			mng.startProcessInstance(processInstID);
			log.info("启动流程成功....processInstID：" + processInstID);
		} catch (WFServiceException e) {
			log.error("启动流程错误", e);
			e.printStackTrace();
			throw new Exception(e);
		}
	}	
	
	/**
	 * <li>方法名：createProcess
	 * <li>@param processDefName  流程定义名
	 * <li>@param processInstName 流程实例名
	 * <li>@param processInstDesc 流程实例描述
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：long
	 * <li>说明：创建流程
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public synchronized long createProcess(String processDefName, String processInstName, String processInstDesc) throws BusinessException {
		//获取流程实例管理器接口
		IWFProcessInstManager mng = BPSServiceClientUtil.getServiceClient().getProcessInstManager();
		try {
			//创建流程实例，获取流程实例ID
			long processInstID = mng.createProcessInstance(processDefName, processInstName, processInstDesc);
			log.info("创建流程成功....processInstID：" + processInstID);
			return processInstID;
		} catch (WFServiceException e) {
			log.error("创建流程错误", e);
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	/**
	 * <li>方法名：finishWorkItem
	 * <li>@param workItemID 工作项ID
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：boolean
	 * <li>说明：结束工作项
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean finishWorkItem(long workItemID) throws BusinessException {
		//获取工作项管理接口
		IWFWorkItemManager mng = BPSServiceClientUtil.getServiceClient().getWorkItemManager();
		try {
			mng.finishWorkItem(workItemID, false);
			log.info("结束节点成功....workItemID：" + workItemID);
			return true;
		} catch (WFServiceException e1) {
			log.error("结束节点错误", e1);
			e1.printStackTrace();
			throw new BusinessException(e1);
		} catch (WFReasonableException e2) {
			log.error("结束节点错误", e2);
			e2.printStackTrace();
			throw new BusinessException(e2);
		}
	}
	
	/**
	 * <li>方法名：finishWorkItem
	 * <li>@param workItemID 工作项ID
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：boolean
	 * <li>说明：结束工作项
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean finishWorkItem(String workItemID) throws BusinessException {
		if (workItemID == null || "".equals(workItemID)) {
			workItemID = "0";
		}
		long workItemId = Long.parseLong(workItemID);
		return this.finishWorkItem(workItemId);
	}

	/**
	 * <li>方法名：setRelativeData
	 * <li>@param processInstID 流程实例ID
	 * <li>@param xpath 数据存放路径
	 * <li>@param value 对应值
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：boolean
	 * <li>说明：设置相关数据
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean setRelativeData(long processInstID, String xpath, Object value) throws BusinessException {
		//获取相关数据管理接口
		IWFRelativeDataManager mng = BPSServiceClientUtil.getServiceClient().getRelativeDataManager();
		try {
			mng.setRelativeData(processInstID, xpath, value);
			return true;
		} catch (WFServiceException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}
	/**
	 * 
	 * <li>方法名：getRelativeData
	 * <li>@param processInstID 流程实例ID
	 * <li>@param xpath 数据存放路径
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：Object
	 * <li>说明：获取相关数据
	 * <li>创建人：李恒飞
	 * <li>创建日期：2011-7-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object getRelativeData(long processInstID, String xpath) throws BusinessException {
		//获取相关数据管理接口
		IWFRelativeDataManager mng = BPSServiceClientUtil.getServiceClient().getRelativeDataManager();
		try {
			return mng.getRelativeData(processInstID, xpath);
		} catch (WFServiceException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}
	
	/**
	 * <li>方法名：setRelativeDataBatch
	 * <li>@param processInstID 流程实例ID
	 * <li>@param relaDataMap 需要设置的相关数据
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：boolean
	 * <li>说明：批量设置相关数据
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean setRelativeDataBatch(long processInstID, Map<Object, Object> relaDataMap) throws BusinessException {
		IWFRelativeDataManager mng = BPSServiceClientUtil.getServiceClient().getRelativeDataManager();
		try {
			mng.setRelativeDataBatch(processInstID, relaDataMap);
			return true;
		} catch (WFServiceException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}
	
	/**
	 * <li>方法名：getProbableParticipants
	 * <li>@param processInstID 流程实例ID
	 * <li>@param activityID 活动ID
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：List<WFParticipant>
	 * <li>说明：获取参与者
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public List<WFParticipant> getProbableParticipants(long processInstID, String activityID) throws BusinessException {
		//获取相关数据管理接口
		IWFProcessInstManager mng = BPSServiceClientUtil.getServiceClient().getProcessInstManager();
		try {
			return mng.getProbableParticipants(processInstID, activityID);
		} catch (WFServiceException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	/**
	 * <li>方法名：getUserTasks
	 * <li>@param userId 用户ID
	 * <li>@param pageCond 分页对象
	 * <li>@return
	 * <li>@throws WFServiceException
	 * <li>返回类型：List<WFWorkItem>
	 * <li>说明：获取用户对应的任务列表
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public List<WFWorkItem> getUserTasks(String userId, PageCond pageCond)	 throws BusinessException {
		try {
			//获取工作列表查询器接口
			IWFWorklistQueryManager workListMng = BPSServiceClientUtil.getServiceClient().getWorklistQueryManager();
			return workListMng.queryPersonWorkItems(userId, "ALL", "ALL", pageCond);
		} catch (WFServiceException e) {
			throw new BusinessException(e);
		}
	}
	
	/**
	 * <li>方法名：getUserFinishedTasks
	 * <li>@param userId 用户ID
	 * <li>@param pageCond 分页对象
	 * <li>@return
	 * <li>@throws WFServiceException
	 * <li>返回类型：List<WFWorkItem>
	 * <li>说明：获取用户对应的已完成的任务列表
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public List<WFWorkItem> getUserFinishedTasks(String userId, PageCond pageCond) throws BusinessException {
		try {
	        //获取工作列表查询器接口
			IWFWorklistQueryManager workListMng = BPSServiceClientUtil.getServiceClient().getWorklistQueryManager();
			return workListMng.queryPersonFinishedWorkItems(userId,	"ALL", false, pageCond);
		} catch (WFServiceException e) {
			throw new BusinessException(e);
		}
	}
	
	/**
	 * <li>方法名：queryNextWorkItem
	 * <li>@param processInstID 流程实例ID
	 * <li>@return
	 * <li>@throws WFServiceException
	 * <li>返回类型：List<WFWorkItem>
	 * <li>说明：获取下一个工作项
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public List<WFWorkItem> queryNextWorkItem (long processInstID) throws BusinessException {
		try {
			IWFWorkItemManager mng = BPSServiceClientUtil.getServiceClient().getWorkItemManager();
			List<WFWorkItem> list = mng.queryNextWorkItemsByProcessInstID(processInstID, false);
			return list;
		} catch (WFServiceException e) {
			throw new BusinessException(e);
		}
	}
	
	/**
	 * <li>方法名：getUserTasks4SDO
	 * <li>@param userId
	 * <li>@param pageCond
	 * <li>@param params
	 * <li>@return
	 * <li>@throws WFServiceException
	 * <li>返回类型：List<WFWorkItem>
	 * <li>说明：根据用户ID，结合业务表查询待办工作项列表.<br />底层类必须类实现该方法
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public List<WFWorkItem> getUserTasks4SDO(String userId, PageCond pageCond, Map<Object, Object> params) throws BusinessException {
		return null;
	}
	
	public List<WFWorkItem> getUserFinishedTasks4SDO(String userId, PageCond pageCond, Map<Object, Object> params) throws BusinessException {
		return null;
	}
	
	public WFWorkItem getWorkItem(long workItemID) throws BusinessException {
		return null;
	}
	
	/**
	 * 
	 * <li>方法名：getExtendAttributeMap
	 * <li>@param workItem
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：Collection
	 * <li>说明：返回流程节点的扩展数据的map
	 * <li>创建人：赵宏波
	 * <li>创建日期：2011-8-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Map<String, String> getExtendAttributeMap(WFWorkItem workItem) throws Exception {
		return getExtentAttributeMap(workItem.getProcessDefID(), workItem.getActivityDefID());
	}
	/**
	 * <li>方法名称：getExtentAttributeMap
	 * <li>方法说明：获取流程节点的扩展数据Map 
	 * <li>@param processDefID
	 * <li>@param activityDefID
	 * <li>@return
	 * <li>@throws Exception
	 * <li>return: Map<String,String>
	 * <li>创建人：张凡
	 * <li>创建时间：2013-5-7 下午08:44:20
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
    public Map<String, String> getExtentAttributeMap(Long processDefID, String activityDefID) throws Exception{
	    BPSServiceClientFactory.getLoginManager().setCurrentUser("0", "0");
	    String extendxml = BPSServiceClientUtil.getServiceClient().getDefinitionQueryManager().getExtendAttribute(processDefID, activityDefID);
        HashMap<String, String> map = new HashMap<String, String>();
        map = YDStringUtil.xmlToMap(extendxml, "extendNode", "key", "value");
        return map;
	}
    
}
