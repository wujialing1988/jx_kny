package com.yunda.frame.yhgl.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.UserData;
import com.yunda.frame.yhgl.manager.EmployeeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class EmployeeAction  extends JXBaseAction<OmEmployee,OmEmployee,EmployeeManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存/更新人员信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-2
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			OmEmployee t = (OmEmployee)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t); //验证
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
//				返回记录保存成功的实体对象
				map.put("entity", t);  
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：根据查询条件，查询匹配的人员信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findEmpListByOrg() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
//			HttpServletRequest req = getRequest();
			String nodetype = getRequest().getParameter("nodetype");
			if(!StringUtil.isNullOrBlank(nodetype)){
				if("org".equals(nodetype)){
					//查询组织机构下直属人员
					map = this.manager.findEmployeeByOrg(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				} else if ("pos".equals(nodetype)){
					//查询岗位下直属人员
					map = this.manager.findEmployeeByPos(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				}
			}
			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：调用检查函数检查是否人员编号已存在
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void checkEmpCode() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String empcode = getRequest().getParameter("empCode");
			String empid  = getRequest().getParameter("empid");
			Boolean isExits = this.manager.checkEmpCode(empcode,empid);
			map.put("isExits", isExits);
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：根据人员ID删除人员信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void delete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.deleteByIds(ids);
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：人员列表选择控件
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void findEmpListToWidget() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
            String codeOrName = StringUtil.nvlTrim(req.getParameter("emp"), "");  //查询条件： 人员名称或代码 
            String empStatus = StringUtil.nvlTrim(req.getParameter("empstatus"), ""); //查询条件： 人员状态（默认为在岗）
            SearchEntity<OmEmployee> searchEntity = new SearchEntity<OmEmployee>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.findPageList(searchEntity, codeOrName, empStatus).extjsStore();
		} catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
	}
	
	/**
	 * <li>说明：根据nodeid获取对应的人员信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void findCurrentEmpInfo() throws Exception {
		String empid = getRequest().getParameter("nodeid");
		if(!StringUtil.isNullOrBlank(empid)){
			OmEmployee a = this.getManager().getModelById(empid);
			JSONUtil.write(this.getResponse(), a);
		}
	}
	
	/*******************************************/
	/*              工作组相关查询                  */
	/*******************************************/
	
	/**
	 * <li>说明：当用户点击工作组/岗位TreeNode时，调用该方法获取所选工作组/岗位的直属人员列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public void findEmpListByNode() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String nodetype = getRequest().getParameter("nodetype");
			if(!StringUtil.isNullOrBlank(nodetype)){
				//查询工作组下的人员列表信息
				if("gop".equals(nodetype)){
					map = this.manager.findEmpListByGroupNode(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				} 
				//查询工作组下辖岗位直属的人员信息
				else if ("pos".equals(nodetype)){
					map = this.manager.findEmpListByPosiNode(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				}
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：工作组管理中新增人员功能，读取未指派工作组的人员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public void findAddEmpList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String nodetype = getRequest().getParameter("nodetype");
			if(!StringUtil.isNullOrBlank(nodetype)){
//				//查询未指派工作组的人员信息
//				if("gop".equals(nodetype)){
//					map = this.manager.findAddEmpList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
//				} 
//				else 
				//查询未指派岗位的人员信息
				if ("pos".equals(nodetype)){
					map = this.manager.findAddEmpList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				}
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：工作组管理中新增人员功能，读取未指派工作组的人员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public void findAddEmpList2() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String nodetype = getRequest().getParameter("nodetype");
			if(!StringUtil.isNullOrBlank(nodetype)){
				//查询未指派工作组的人员信息
				if("gop".equals(nodetype)){
					map = this.manager.findAddEmpList(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				} 
				//查询未指派岗位的人员信息
				else if ("pos".equals(nodetype)){
					map = this.manager.findAddEmpList2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
				}
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：保存人员-工作组关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("all")
	public void addEmpGroupOrPosition() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				String groupid = getRequest().getParameter("groupid"); //工作组ID
				String positionid = getRequest().getParameter("positionid"); //岗位ID
				//工作组下执行人员新增
				if(!StringUtil.isNullOrBlank(groupid)&&StringUtil.isNullOrBlank(positionid)){
					this.manager.addEmpGroup(Long.valueOf(groupid),ids);
				}
				//岗位下执行人员新增
				else if (StringUtil.isNullOrBlank(groupid)&&!StringUtil.isNullOrBlank(positionid)){
					String ismain = getRequest().getParameter("ismain");
					if(!StringUtil.isNullOrBlank(ismain)&&"y".equals(ismain)){
						this.manager.addEmpPosition(Long.valueOf(positionid),true,ids);
					} else {
						this.manager.addEmpPosition(Long.valueOf(positionid),ids);
					}
				}
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}		
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * 
	 * <li>说明：删除工作组-人员关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("all")
	public void delEmpGroupOrPosition() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				String groupid = getRequest().getParameter("groupid"); //工作组ID
				String positionid = getRequest().getParameter("positionid"); //岗位ID
				//工作组下执行人员删除
				if(!StringUtil.isNullOrBlank(groupid)&&StringUtil.isNullOrBlank(positionid)){
					this.manager.delEmpGroup(Long.valueOf(groupid),ids);
				}
				//岗位下执行人员删除
				else if (StringUtil.isNullOrBlank(groupid)&&!StringUtil.isNullOrBlank(positionid)){
					this.manager.delEmpPosition(Long.valueOf(positionid),ids);
				}
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}		
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/*******************************************/
	/*              职务相关查询                    */
	/*******************************************/
	
	/**
	 * <li>说明：当用户点击职务TreeNode时，调用该方法获取所选职务的直属人员列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
	public void findEmpListByDuty() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String nodetype = getRequest().getParameter("nodetype");
			if(!StringUtil.isNullOrBlank(nodetype)){
				map = this.manager.findEmpListByDuty(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/*******************************************/
	/*              人员控制查询                    */
	/*******************************************/
	/**
	 * <li>说明：根据查询条件，查询匹配的人员信息(人员控制)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findAllEmployees() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = this.manager.findAllEmployees(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：更新人员-机构关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void updateEmpOrg() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String orgid = getRequest().getParameter("orgid");
			if(!StringUtil.isNullOrBlank(orgid)&& ids!=null){
				for(String _t : ids){
					this.manager.updateEmpOrg(Long.valueOf(orgid), Long.valueOf(_t));
				}
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：更新人员-工作组关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void updateEmpGroup() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String groupid = getRequest().getParameter("groupid");
			String empid = getRequest().getParameter("empid");
			String oldgroupid = getRequest().getParameter("oldgroupid");
			this.manager.updateEmpGroup(Long.valueOf(groupid), Long.valueOf(empid), Long.valueOf(oldgroupid));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：更新人员-岗位关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void updateEmpPosi() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String positionid = getRequest().getParameter("positionid");
			String isMain = getRequest().getParameter("ismain");
			if(!StringUtil.isNullOrBlank(isMain)&&"y".equals(isMain)){
				if(!StringUtil.isNullOrBlank(positionid)&& ids!=null){
					for(String _t : ids){
						this.manager.updateEmpPosi(Long.valueOf(positionid), Long.valueOf(_t), isMain);
					}
				}
			} else if (!StringUtil.isNullOrBlank(isMain)&&"n".equals(isMain)) {
				String empid = getRequest().getParameter("empid");
				String oldpositionid = getRequest().getParameter("oldpositionid");
				this.manager.updateEmpPosi(Long.valueOf(positionid), Long.valueOf(empid), Long.valueOf(oldpositionid),isMain);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	/**
	 * 
	 * <li>说明：根据组织机构获取人员列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryEmpListByTeam() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String orgId = getRequest().getParameter("orgId");
			map = this.manager.queryEmpListByTeam(orgId).extjsResult();
			map.put("id", "empid");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	* <li>说明： 根据用户所在部门ID查询该部门下的所有用户
	* <li>创建人： 黄杨
	* <li>创建日期：2017-5-26
	* <li>修改人：
	* <li>修改内容：
	* <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	*/
	public void getEmpListByOrgid() throws JsonMappingException, IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String orgId = req.getParameter("orgId");
			String empName = req.getParameter("empName");
			List<UserData> list = manager.getEmpListByOrgid(orgId, empName);
			map.put("success", true);
			map.put("userList", list);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}
}
