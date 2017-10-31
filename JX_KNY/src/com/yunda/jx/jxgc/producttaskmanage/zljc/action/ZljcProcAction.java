package com.yunda.jx.jxgc.producttaskmanage.zljc.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.jxgc.base.jcqcitemdefine.entity.JCQCItemDefine;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QualityControlCheckInfoVO;
import com.yunda.jx.jxgc.producttaskmanage.zljc.manager.ZljcProcManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.ProcessTaskListBean;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 质量检查处理控制器类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class ZljcProcAction extends JXBaseAction<Object, Object, ZljcProcManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：查询质量检查列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryZljcList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcOperator ac = SystemContext.getAcOperator();
			String uid = ac.getUserid();
			String uname = ac.getOperatorname();
			String queryString = StringUtil.nvlTrim(getRequest().getParameter("queryString"), "");
			String mode = StringUtil.nvl(getRequest().getParameter("mode"), JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "");
			map = this.getManager().queryZljcList(uid, uname, mode, queryString, getStart(), getLimit());
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * 
	 * <li>说明：单条、批量处理质量检查信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveQualityControlCheckInfo() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QualityControlCheckInfoVO checkInfo = JSONUtil.read(getRequest().getParameter("checkInfo"), QualityControlCheckInfoVO.class);
			ProcessTaskListBean[] listBean = JSONUtil.read(getRequest().getParameter("listBean"), ProcessTaskListBean[].class);
			this.getManager().saveQualityControlCheckInfo(checkInfo, listBean);
			map = new Page().extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}	
	
	/**
	 * 
	 * <li>说明：全部处理质量检查信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveAllQualityControlCheckInfo() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();
			String remarks = StringUtil.nvlTrim(getRequest().getParameter("remarks"), "");
			String queryString = StringUtil.nvlTrim(getRequest().getParameter("queryString"), "");
			this.getManager().saveAllQualityControlCheckInfo(operatorid, remarks, queryString);
			map = new Page().extjsResult();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：查询生产任务单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-13
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
			String mode = StringUtil.nvl(getRequest().getParameter("mode"), JCQCItemDefine.CONST_INT_CHECK_WAY_BJ + "");
			map = this.getManager().queryRdpList(operatorid, mode, getStart(), getLimit());
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
}
