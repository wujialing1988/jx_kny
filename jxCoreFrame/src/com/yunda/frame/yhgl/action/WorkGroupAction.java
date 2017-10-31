package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmGroup;
import com.yunda.frame.yhgl.manager.WorkGroupManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工作组Action
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class WorkGroupAction extends JXBaseAction <OmGroup,OmGroup,WorkGroupManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获得工作组树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void tree() throws Exception{
		String nodeid = getRequest().getParameter("nodeid");//当前orgid
		String nodetype = getRequest().getParameter("nodetype");//当前nodetype
		List<Map> childNodeList = this.manager.getChildNodes(
				StringUtil.isNullOrBlank(nodeid)||"ROOT_0".equals(nodeid)?null:Long.valueOf(nodeid),
						StringUtil.isNullOrBlank(nodetype)?"gop":nodetype); //调用业务函数获取子工作组列表
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * <li>说明：根据nodeid获取对应的工作组信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void findCurrentOrgInfo() throws Exception {
		String groupid = getRequest().getParameter("nodeid");
		if(!StringUtil.isNullOrBlank(groupid)){
			OmGroup a = this.getManager().getModelById(groupid);
			JSONUtil.write(this.getResponse(), a);
		}
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的工作组信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-19
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findGroupList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findGroupList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	
	/**
	 * 
	 * <li>说明：获得工作组树 (人员管理-工作组调动-工作组树)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void treeWidget() throws Exception{
		String nodeid = getRequest().getParameter("nodeid");//当前orgid
		String nodetype = getRequest().getParameter("nodetype");//当前nodetype
		String widgetType = getRequest().getParameter("widgetType");//当前控件的查询范围 1-工作组、2-工作组+岗位、3-工作组（暂未实现）
		String empid = getRequest().getParameter("empid");
		List<Map> childNodeList = this.manager.getChildNodesForWidget(
				StringUtil.isNullOrBlank(nodeid)||"ROOT_0".equals(nodeid)?null:Long.valueOf(nodeid),
						StringUtil.isNullOrBlank(nodetype)?"gop":nodetype, widgetType, empid); //调用业务函数获取子工作组列表
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的工作组信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-29
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findGroupListByEmployee() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findGroupListByEmployee(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：返回已配置/未配置为当前角色的工作组列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-17
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findGroupListByRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String isRel = getRequest().getParameter("relevance"); //接收表示， 根据该参数决定查询
			if(StringUtil.isNullOrBlank(isRel)){
				//查询已分配角色的工作组				
				map = this.manager.findGroupListByRole(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			} else {
				//查询未分配角色的工作组				
				map = this.manager.findGroupListByRole2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
}
