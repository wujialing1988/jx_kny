package com.yunda.frame.yhgl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.OperatorManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 操作员action
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class OperatorAction extends JXBaseAction<AcOperator,AcOperator,OperatorManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：根据操作员id查询其实体
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void findOperatorInfo() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcOperator ope = this.manager.getModelById(getRequest().getParameter("operatorid"));
			map.put("acOperator", ope);
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：根据操作员id查询其实体
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	public void findOperatorToStore() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Long operatorid = SystemContext.getAcOperator().getOperatorid();//获取当前登录用户的ID
			AcOperator ope = this.manager.getModelById(operatorid);
			List <AcOperator> list = new ArrayList<AcOperator> ();
			list.add(ope);
			map.put("id","OPE");
			map.put("root", list);
			map.put("totalProperty",1);
		} catch (Exception e){
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	/**
	 * <li>说明：接受保存或更新记录请求，向客户端返回操作结果（JSON格式），实体类对象必须符合检修系统表设计，主键名必须为idx（字符串uuid）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-11
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
			AcOperator t = (AcOperator)JSONUtil.read(getRequest(), entity.getClass());
			String empid = getRequest().getParameter("empid");
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t,empid);
				//返回记录保存成功的实体对象
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
	 * <li>说明：接受保存或更新记录请求，向客户端返回操作结果（JSON格式），用于操作员维护
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-16
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void updateOperatorInfo() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcOperator t = (AcOperator)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.updateOperatorInfo(t);
				//返回记录保存成功的实体对象
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
	 * <li>说明：提交更新操作员密码
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-16
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void updateOperatorPWD() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcOperator t = (AcOperator)JSONUtil.read(getRequest(), entity.getClass());
			String newPWD = getRequest().getParameter("newPwd"); //新密码
			String[] errMsg = this.manager.validatePWD(t); //验证原始密码输入是否正确
			if (errMsg == null || errMsg.length < 1) {
				this.manager.updateOperatorPWD(t,newPWD);
				//返回记录保存成功的实体对象
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
	 * <li>说明：返回已配置/未配置为当前角色的操作员列表
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
	public void findOperatorListByRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String isRel = getRequest().getParameter("relevance"); //接收表示， 根据该参数决定查询
			if(StringUtil.isNullOrBlank(isRel)){
				//查询已分配角色的操作员
				map = this.manager.findOperatorListByRole(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			} else {
				//查询未分配角色的操作员
				map = this.manager.findOperatorListByRole2(getWhereList(), getOrderList(), getStart(), getLimit()).extjsStore();
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
}
