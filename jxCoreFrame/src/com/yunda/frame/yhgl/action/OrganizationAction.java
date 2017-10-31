package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.OrganizationManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：谭诚
 * <li>创建日期：2013-10-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class OrganizationAction extends JXBaseAction<OmOrganization,OmOrganization,OrganizationManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获得组织机构树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-30
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
						StringUtil.isNullOrBlank(nodetype)?"org":nodetype); //调用业务函数获取子机构列表
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * <li>说明：根据nodeid获取对应的机构信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findCurrentOrgInfo() throws Exception {
		String orgid = getRequest().getParameter("nodeid");
		if(!StringUtil.isNullOrBlank(orgid)){
			OmOrganization a = this.getManager().findCurrentOrgInfo(Long.valueOf(orgid));
			JSONUtil.write(this.getResponse(), a);
		}
	}
	
	/**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void overPageQuery() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findOrgList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
	
	/**
	 * 
	 * <li>说明：获得组织机构树(人员管理-机构调动-机构树)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void treeWidget() throws Exception{
		String nodeid = getRequest().getParameter("nodeid");//当前orgid
		String nodetype = getRequest().getParameter("nodetype");//当前nodetype
		String widgetType = getRequest().getParameter("widgetType");//当前控件的查询范围 1-机构、2-机构+岗位、3-岗位（暂未实现）
		List<Map> childNodeList = this.manager.getChildNodesForWidget(
				StringUtil.isNullOrBlank(nodeid)||"ROOT_0".equals(nodeid)?null:Long.valueOf(nodeid),
						StringUtil.isNullOrBlank(nodetype)?"org":nodetype, widgetType); //调用业务函数获取子机构列表
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * 
	 * <li>说明：获得组织机构树(机构调整)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void orgAdjust() throws Exception{
		String chkNodeid = getRequest().getParameter("chkNodeid");//当前orgid
		String [] orgids = StringUtil.isNullOrBlank(chkNodeid)?null:chkNodeid.split(",");
		List<Map> childNodeList = this.manager.getOrgTreeForAdjust(null,orgids); //调用业务函数获取子机构列表
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * 
	 * <li>说明：机构调整，更新机构的parentorgid，实现原机构->目标机构的移动
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void updateOrgAdjust() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		String _orgid = getRequest().getParameter("orgid");   //目标机构id
		try {
			Boolean success = false;
			Long orgid = StringUtil.isNullOrBlank(_orgid)?null:Long.valueOf(_orgid);
			success = this.manager.updateOrgAdjust(orgid,ids);
			map.put("success", success);
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：返回已配置/未配置为当前角色的机构列表
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
	public void findOrganizationListByRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String isRel = getRequest().getParameter("relevance"); //接收表示， 根据该参数决定查询
			if(StringUtil.isNullOrBlank(isRel)){
				//查询已配置角色的机构
				map = this.manager.findOrganizationListByRole(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			} else {
				//查询未配置角色的机构
				map = this.manager.findOrganizationListByRole2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
}
