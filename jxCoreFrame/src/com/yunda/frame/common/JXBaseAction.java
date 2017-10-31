package com.yunda.frame.common; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.api.IBPSServiceClient;
import com.yunda.base.BaseAction;
import com.yunda.base.context.SystemContext;
import com.yunda.flow.util.BPSServiceClientUtil;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：该类继承BaseAction，封装检修系统相关的功能如逻辑删除等，扩展实现对AJAX、JSON的支持。
 * <li>版权: Copyright (c) 2008 运达科技公司
 * <li>创建日期：2012-08-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public abstract class JXBaseAction<T, S, M extends JXBaseManager<T, S>> 
	extends BaseAction<T, S, M>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/** 分页查询参数开始行 */
	protected Integer start;
	/** 该页最大记录总数 */
	protected Integer limit;
	/** 分页查询实体类完整名称（包.类） */
	protected String queryClassName;
	/** 分页查询条件JSON字符串 */
	protected String whereListJson;
	/**
	 * <li>说明：接受保存或更新记录请求，向客户端返回操作结果（JSON格式），实体类对象必须符合检修系统表设计，主键名必须为idx（字符串uuid）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
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
			T t = (T)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
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
	 * <li>说明：接受逻辑删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void logicDelete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.logicDelete(ids);
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
	 * <li>说明：接受物理删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
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
	 * <li>说明：单表查询，返回单表查询记录的json
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void list() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String searchJson = StringUtil.nvlTrim( getRequest().getParameter("entityJson"), "{}" );
            //修改 2015-02-02 汪东良 将entity 局部变量修改成obj开头，保证与属性变量命名不同
			T objEntity = (T)JSONUtil.read(searchJson, entitySearch.getClass());
			List<T> entityList = this.manager.findList(objEntity, getOrders());
			map = new Page<T>(entityList).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void pageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			getRequest().getParameter("idx");
			String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            //修改 2015-02-02 汪东良 将entity 局部变量修改成obj开头，保证与属性变量命名不同
			T objEntity = (T)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<T> searchEntity = new SearchEntity<T>(objEntity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	/**
	 * <li>说明：单表分页查询，返回单表分页查询记录的json
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-13
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void pageQuery() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			QueryCriteria<T> query = new QueryCriteria<T>(getQueryClass(),getWhereList(), getOrderList(), getStart(), getLimit());
			map = this.manager.findPageList(query).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
	
	/**
	 * <li>说明：该私有方法，该方法用于获取HTTP请求中的排序字段信息，返回设置排序规则数组
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-9-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return Order[] 排序规则数组
	 * @throws 
	 */
	protected Order[] getOrders(){
		List<Order> orderList = getOrderList();
		if(orderList == null || orderList.size() < 1)	return null;
		return orderList.toArray(new Order[ orderList.size() ]);
	}
	/**
	 * <li>说明：该私有方法，该方法用于获取HTTP请求中的排序字段信息，返回设置排序规则列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return List<Order> 排序规则列表
	 * @throws 
	 */
	protected List<Order> getOrderList(){
		//根据请求参数设置排序规则
		HttpServletRequest req = getRequest();
		String sort = StringUtil.nvlTrim(req.getParameter("sort"), null);
		String dir = StringUtil.nvlTrim(req.getParameter("dir"), null);
		List<Order> orderList = new ArrayList<Order>();
		if (sort != null && dir != null) {
			if ("ASC".equalsIgnoreCase(dir)) {
				orderList.add(Order.asc(sort));
			} else if("DESC".equalsIgnoreCase(dir)){
				orderList.add(Order.desc(sort));
			}
		}
		return orderList;
	}	
	/**
	 * <li>说明：解析客户端请求参数：whereListJson分页查询条件JSON字符串，返回List<Condition>查询条件列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	protected List<Condition> getWhereList() throws JsonParseException, JsonMappingException, IOException{
		Condition[] conditions = JSONUtil.read(whereListJson == null ? "[]" : whereListJson, Condition[].class);
		return Arrays.asList(conditions);
	}
	/**
	 * <li>说明：从客户端的请求中获取分页查询对应的实体类，若请求参数queryClassName为空，返回entity.getClass()
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	protected Class getQueryClass() throws ClassNotFoundException{
		return queryClassName == null ? entity.getClass() : Class.forName(queryClassName);
	}
	
	/**
	 * <li>方法名称：BPSLogin
	 * <li>方法说明：BPS设置前当登陆人 
	 * <li>@param userId
	 * <li>@param userName
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-20 上午11:44:36
	 * <li>修改人：
	 * <li>修改内容：
	 */
	protected void loginBPS(String userId, String userName){
	    BPSServiceClientFactory.getLoginManager().setCurrentUser(userId, userName);
	}
	/**
	 * <li>方法名称：BPSLogin
	 * <li>方法说明：BPS设置当前登陆人 
	 * <li>
	 * <li>return: void
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-20 上午11:44:48
	 * <li>修改人：
	 * <li>修改内容：
	 */
	protected void loginBPS(){
	    AcOperator ac = SystemContext.getAcOperator();
        BPSServiceClientFactory.getLoginManager().setCurrentUser(ac.getUserid(), ac.getOperatorname());
	}
	
	/**
	 * <li>方法名称：getBPSService
	 * <li>方法说明：获取IBPSServiceClient ,使用前请先调用BPSLogin
	 * <li>@return
	 * <li>return: IBPSServiceClient
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-20 上午11:46:42
	 * <li>修改人：
	 * <li>修改内容：
	 */
	protected IBPSServiceClient  getBPSService(){
	     return BPSServiceClientUtil.getServiceClient();
	}
	
	public Integer getLimit() {
		return limit == null ? Page.PAGE_SIZE : limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getQueryClassName() {
		return queryClassName;
	}

	public void setQueryClassName(String queryClassName) {
		this.queryClassName = queryClassName;
	}

	public Integer getStart() {
		return start == null ? 0 : start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getWhereListJson() {
		return whereListJson;
	}

	public void setWhereListJson(String whereListJson) {
		this.whereListJson = whereListJson;
	}
}