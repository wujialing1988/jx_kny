/**
 * 
 */
package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmDuty;
import com.yunda.frame.yhgl.manager.WorkDutyManager;

/**
 * @author Administrator
 *
 */
@SuppressWarnings(value="serial")
public class WorkDutyAction extends JXBaseAction <OmDuty, OmDuty, WorkDutyManager>{
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
				StringUtil.isNullOrBlank(nodeid)||"ROOT_0".equals(nodeid)?null:nodeid,
						StringUtil.isNullOrBlank(nodetype)?"dict":nodetype); //调用业务函数获取子机构列表
		JSONUtil.write(getResponse(), childNodeList);
	}
	
	/**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
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
			map = this.manager.findDutyList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
	
	/**
	 * <li>说明：根据nodeid获取对应的职务信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void findCurrentDutyInfo() throws Exception {
		String dutyid = getRequest().getParameter("nodeid");
		if(!StringUtil.isNullOrBlank(dutyid)){
			OmDuty a = this.getManager().findCurrentDutyInfo(Long.valueOf(dutyid));
			JSONUtil.write(this.getResponse(), a);
		}
	}
	
	/**
	 * <li>说明：返回已配置/未配置为当前角色的职务列表
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
	public void findDutyListByRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String isRel = getRequest().getParameter("relevance"); //接收表示， 根据该参数决定查询
			if(StringUtil.isNullOrBlank(isRel)){
				//查询已分配角色的职务			
				map = this.manager.findDutyListByRole(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			} else {
				//查询未分配角色的职务				
				map = this.manager.findDutyListByRole2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
}
