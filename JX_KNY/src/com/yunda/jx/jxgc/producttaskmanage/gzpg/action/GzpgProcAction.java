package com.yunda.jx.jxgc.producttaskmanage.gzpg.action;

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
import com.yunda.jx.jxgc.producttaskmanage.gzpg.manager.GzpgProcManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工长派工处理控制器类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class GzpgProcAction extends JXBaseAction<WorkCard, WorkCard, GzpgProcManager>{	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
    
    private static final String MODE = "mode";
    private static final String ID = "id";
	
	/**
	 * 
	 * <li>说明：查询工长派工列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryGzpgList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), "");
			String mode = getRequest().getParameter(MODE);
			map = this.getManager().queryGzpgList(searchJson, getStart(), getLimit(), mode, operatorid).extjsResult();
			map.put(ID, "idx");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}	
	/**
	 * 
	 * <li>说明：查询生产任务单
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryRdpList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String mode = getRequest().getParameter(MODE);
			String dispatcherStr = "";
			if ((JxgcConstants.GZPG_DISPATCHED + "").equals(mode)) {
				dispatcherStr = "have_default_person = '1'";
			} else if ((JxgcConstants.GZPG_NOTDISPATCHED + "").equals(mode)) {
				dispatcherStr = "have_default_person = '0' or have_default_person is null";
			}
			map = this.getManager().queryRdpList(operatorid, getStart(), getLimit(), dispatcherStr).extjsResult();
			map.put(ID, "idx");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询检修活动列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryRepairActivityList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String mode = getRequest().getParameter(MODE);
			String isDispatcher = "";
			if ((JxgcConstants.GZPG_DISPATCHED + "").equals(mode)) {
				isDispatcher = "y";
			} else if ((JxgcConstants.GZPG_NOTDISPATCHED + "").equals(mode)) {
				isDispatcher = "n";
			}
//			map = this.getManager().queryRepairActivityList(rdpIdx, isDispatcher, operatorid, getStart(), getLimit()).extjsResult();
			map = new HashMap<String, Object>();
			map.put(ID, "idx");
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
	 * <li>创建日期：2014-10-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryTecNodeList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String rdpIdx = getRequest().getParameter("rdpIdx");
			String parentIdx = StringUtil.nvlTrim(getRequest().getParameter("parentIdx"), "");
			String mode = getRequest().getParameter(MODE);
			String isDispatcher = "";
			if ((JxgcConstants.GZPG_DISPATCHED + "").equals(mode)) {
				isDispatcher = "y";
			} else if ((JxgcConstants.GZPG_NOTDISPATCHED + "").equals(mode)) {
				isDispatcher = "n";
			}
			list = this.getManager().queryTecNodeList(parentIdx, rdpIdx, operatorid, isDispatcher);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
	
	/**
	 * 
	 * <li>说明：查询处理状态列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryStatusList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.getManager().queryStatusList().extjsResult();
			map.put(ID, "status");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}

	/**
	 * 
	 * <li>说明：全部工长派工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveAllDispatch() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String searchJson = StringUtil.nvlTrim(getRequest().getParameter("searchJson"), "");
			String empids = getRequest().getParameter("empids");
			this.getManager().saveAllDispatch(searchJson, operatorid, empids);
			map = new Page().extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}


	/**
	 * 
	 * <li>说明：默认上次派工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveDefaultLastTimeDispatch() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String workCardIds = getRequest().getParameter("workCardIds");
			map = this.getManager().saveDefaultLastTimeDispatch(operatorid, workCardIds).extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	/**
	 * 
	 * <li>说明：工长派工、批量工长派工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveDispatch() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String workCardIds = getRequest().getParameter("workCardIds");
			String empids = getRequest().getParameter("empids");
			map = this.getManager().saveDispatch(operatorid, workCardIds, empids).extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
}