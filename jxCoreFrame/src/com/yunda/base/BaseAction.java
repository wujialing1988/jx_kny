package com.yunda.base;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.yunda.Application;
import com.yunda.base.filter.LoginPurviewCheckFilter;
//import com.yunda.component.tag.grid.server.GridServerHandler;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.AcRole;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.util.BeanUtils;
import com.yunda.util.FileUploadUtil;
import com.yunda.util.GenericsUtils;
import com.yunda.util.YDStringUtil;

/**
 * 
 * <li>类型名称：
 * <li>说明：普通的功能（即普通的增删改查功能）的action继承于此类，子类必须提供action操作的实体类型T，
 * <li>增、删、改功能使用entity T和action所依赖的manager对象；
 * <li>查询使用entitySearch S和action所依赖的manager对象；
 * <li>注：此类只提供对单个业务实体的增删改查的默认实现。
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人：
 * <li>修改日期：
 */
public abstract class BaseAction<T, S, M extends BaseManager<T, S>> extends
		BaseActionSupport {

//	protected GridServerHandler gridServerHandler; // GT-Grid工具类
	
	protected M manager; // action 所依赖的服务对象

	protected T entity; // action 所操作的主导业务实体对象

	protected S entitySearch; // 用于保存传递查询的JavaBean

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * 
	 * <li>说明：构造方法，创建entity实体，并获取entity的具体类型；设置本action方法返回的JSP页面的目录结构
	 * <li>设置默认的查询，编辑页面名称；并设置方法返回页面影射表的内容。
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public BaseAction() {

		serviceName = StringUtils.uncapitalize(GenericsUtils
				.getSuperClassGenricType(getClass(), 2).getSimpleName());
		
		actionClass = this.getClass().getPackage().getName();

		pageSubFolder = actionClass.toLowerCase().replace("com.yunda.", "")
				.replace(".action", "").replace(".", "/");

		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
		entityName = this.entityClass.getSimpleName();

		String actionClassName = getClass().getSimpleName().replace("Action",
				"");

		LISTPAGE = DEFAULTFOLDER + pageSubFolder + "/"
				+ YDStringUtil.createJspFileName(actionClassName) + LISTPAGE;
		EDITPAGE = DEFAULTFOLDER + pageSubFolder + "/"
				+ YDStringUtil.createJspFileName(actionClassName) + EDITPAGE;
		VIEWPAGE = DEFAULTFOLDER + pageSubFolder + "/"
				+ YDStringUtil.createJspFileName(actionClassName) + VIEWPAGE;
		EDITMASTERDETAILPAGE = DEFAULTFOLDER + pageSubFolder + "/"
				+ YDStringUtil.createJspFileName(actionClassName)
				+ EDITMASTERDETAILPAGE;
		EXPORTTOEXCELPAGE = DEFAULTFOLDER + pageSubFolder + "/"
				+ YDStringUtil.createJspFileName(actionClassName)
				+ EXPORTTOEXCELPAGE;
		try {
			this.entity = (T) GenericsUtils.getSuperClassGenricType(getClass())
					.newInstance();
			this.entitySearch = (S) GenericsUtils.getSuperClassGenricType(
					getClass(), 1).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// inint the navigationMap
		navigationMap.put(TOVIEW, VIEWPAGE);
		navigationMap.put(TOEDIT, EDITPAGE);
		navigationMap.put(SEARCH, LISTPAGE);
		navigationMap.put(SAVE, EDITPAGE);
		navigationMap.put(SAVETOLIST, EDITPAGE);
	}

	/**
	 * 
	 * <li>方法名：getEntity
	 * <li>
	 * 
	 * @return 范型指定类型的实体对象。
	 *         <li>返回类型：T
	 *         <li>说明：获取范型指定类型的实体对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * 
	 * <li>方法名：setEntity
	 * <li>
	 * 
	 * @param entity
	 *            范型指定类型的实体对象。
	 *            <li>返回类型：void
	 *            <li>说明：设置范型指定类型的实体对象，此方法可供程序员调用；struts框架值设置拦截器在填值的时候会自动调用本方法。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void setEntity(T entity) {
		this.entity = entity;
	}

	/**
	 * 
	 * <li>方法名：getEntitySearch
	 * <li>
	 * 
	 * @return 返回范型指定的查询bean对象。
	 *         <li>返回类型：S
	 *         <li>说明：获取范型指定的查询bean对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public S getEntitySearch() {
		return entitySearch;
	}

	/**
	 * 
	 * <li>方法名：setEntitySearch
	 * <li>
	 * 
	 * @param entitySearch
	 *            范型指定的查询bean对象。
	 *            <li>返回类型：void
	 *            <li>说明：设置范型指定的查询bean对象。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void setEntitySearch(S entitySearch) {
		this.entitySearch = entitySearch;
	}

	/**
	 * <li>方法名：getManager
	 * <li>@param serName
	 * <li>@return
	 * <li>返回类型：Object
	 * <li>说明：获取【当前】的manager业务对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-7-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public M getManager() {
		this.manager = (M) super.ctx.getBean(serviceName);
		if(this.manager == null){
			this.manager = (M)getManager(serviceName);
		}
		return manager;
	}
	
	/**
	 * <li>方法名：getManager
	 * <li>@param serName
	 * <li>@return
	 * <li>返回类型：Object
	 * <li>说明：获取指定的manager业务对象，serName为需要获取的manager对象名，首字母小写
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-7-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object getManager(String serName) {
		return Application.getSpringApplicationContext().getBean(serName);
	}

	/**
	 * 实现ApplicationContextWare接口，目的是设置上下文环境对象，并从Spring中获得业务管理类。
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void setApplicationContext(ApplicationContext cxt)
			throws BeansException {
		super.setApplicationContext(cxt);
		this.manager = (M) super.ctx.getBean(serviceName);
	}

	/**
	 * 
	 * <li>方法名：toAdd
	 * <li>
	 * 
	 * @return 导航到实体新增页面。
	 *         <li>
	 * @throws Exception
	 *             <li>返回类型：String
	 *             <li>说明：用户在页面请求新增时，调用此方法；如果有特殊的事情要做，请在
	 *             <li>子类中覆盖initCreatePage方法；然后系统导航到新增页面。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public String toAdd() throws Exception {
		return renderJSPPage(EDITPAGE);
	}

	/**
	 * 
	 * <li>方法名：toEdit
	 * <li>
	 * 
	 * @return 导航到实体编辑页面。
	 *         <li>
	 * @throws Exception
	 *             <li>返回类型：String
	 *             <li>说明：当用户在页面上浏览某业务实体，想查看该业务实体详细信息或修改
	 *             <li>该业务实体时，系统根据该业务实体的id从数据库获取该实体；对修改
	 *             <li>JSP页面做必要的初始化工作后，导航到修改页面。如果从数据库中获取
	 *             <li>实体之前还需要做别的事情，请在子类中覆盖initEditPage方法。然后系统导航到编辑页面。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public String toEdit() throws Exception {
		entity = this.getManager().getModelById(id);
		return renderJSPPage(EDITPAGE);
	}

	/**
	 * 
	 * <li>方法名：toView
	 * <li>
	 * 
	 * @return
	 * <li>
	 * @throws Exception
	 *             <li>返回类型：String
	 *             <li>说明：
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-5
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public String toView() throws Exception {
		entity = this.getManager().getModelById(id);
		return renderJSPPage(VIEWPAGE);
	}
	
//	/**
//	 * <li>方法名：save
//	 * <li>
//	 * @return 返回修改页面。
//	 *         <li>
//	 * @throws Exception
//	 *             <li>返回类型：String
//	 *             <li>说明：用户保存数据后，页面返回修改页面，用户可以继续修改
//	 *             <li>直到离开这个页面为止
//	 *             <li>创建人：曾锤鑫
//	 *             <li>创建日期：2011-01-14
//	 *             <li>修改人：
//	 *             <li>修改日期：
//	 */
//	@SuppressWarnings("unchecked")
//	public String save() throws Exception {
//		// System.out.println(getRequest().getParameter("txtXML")
//		// +"....................");
//
//		com.yunda.efs.base.ReturnDoc returndoc = new com.yunda.efs.base.ReturnDoc();
//		try {
//			if (isUpdate(entity)) {
//				entity = getManager().update(entity);
//				this.addActionMessage(MSG_UPDATE_SUCCESS);
//				returndoc.setFuncErrorInfo(MSG_UPDATE_SUCCESS);
//			} else {
//				entity = this.getManager().insert(entity);
//				this.addActionMessage(MSG_INSERT_SUCCESS);
//				returndoc.setFuncErrorInfo(MSG_INSERT_SUCCESS);
//			}
//			refreshFlag = "refresh";
//
//			// 执行成功，返回成功节点
//			returndoc.addErrorResult(com.yunda.efs.declare.Common.RT_SUCCESS);
//		} catch (Exception e) {
//
//			// 执行失败，返回异常描述
//			returndoc.addErrorResult(com.yunda.efs.declare.Common.RT_FUNCERROR);
//			returndoc.setFuncErrorInfo(e.getMessage());
//
//			this.addActionMessage(MSG_FAIL);
//			BeanUtils.forceSetProperty(entity, this.manager
//					.getModelIdName(entity), null);
//		}
//
//		this.ajaxMessage(returndoc.getXML(), "text/xml");
//		return null;
//	}


//	/**
//	 * 
//	 * <li>方法名：saveToList
//	 * <li>
//	 * 
//	 * @return 返回查询结果页面。
//	 *         <li>
//	 * @throws Exception
//	 *             <li>返回类型：String
//	 *             <li>说明：用户在页面修改或新增实体保存后后，页面返回在查询结果页面。此方法目前不推荐使用，框架中先保留此方法。
//	 *             <li>创建人：曾锤鑫
//	 *             <li>创建日期：2011-01-14
//	 *             <li>修改人：
//	 *             <li>修改日期：
//	 */
//	public String saveToList() throws Exception {
//		this.save();// 新增或修改
//		refreshFlag = "refresh";
//		this.addActionMessage(MSG_UPDATE_SUCCESS);
//		return renderJSPPage(LISTPAGE);
//	}

	/**
	 * 
	 * <li>方法名：deleteById
	 * <li>
	 * 
	 * @return 返回查询结果页面。
	 *         <li>返回类型：String
	 *         <li>说明：用ajax根据指定的单个id删除指定的业务实体，删除后返回查询结果页面。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public String deleteById() throws Exception {
		String message = MSG_DELETE_SUCCESS;
		this.getManager().deleteByIds(id);
		this.ajaxMessage(message);
		refreshFlag = "refresh";

		return null;
	}

	/**
	 * 
	 * <li>方法名：deleteByIds
	 * <li>
	 * 
	 * @return 返回查询结果页面。
	 *         <li>返回类型：String.
	 *         <li>说明：用ajax根据指定的id集合进行批量删除，删除后返回查询结果页面。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public String deleteByIds() throws Exception {
		try {
			if (ids.length == 1 && ids[0].indexOf(",") != -1) {
				ids = ids[0].split(",");
			}
			this.manager.deleteByIds(ids);
			refreshFlag = "refresh";
			this.ajaxMessage(MSG_DELETE_SUCCESS);
		} catch (Exception ex) {
			this.ajaxMessage(ex.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * <li>方法名：isAdd
	 * <li>
	 * 
	 * @param entity
	 *            <li>
	 * @return
	 *            <li>返回类型：boolean
	 *            <li>说明：通过对象是否有id判断是否是修改对象
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-22
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	protected boolean isUpdate(Object objEntity) {
        //修改 2015-02-02 汪东良 将entity 局部变量修改成obj开头，保证与属性变量命名不同。
        boolean isUpdate = true;
		if (objEntity == null) {
			return false;
		}

		BeanWrapper bw = new BeanWrapperImpl(objEntity);
        //修改 2015-02-02 汪东良 将id 局部变量修改成obj开头，保证与属性变量命名不同。
		Object objId = bw.getPropertyValue(this.manager.getModelIdName(objEntity));
		if (objId == null || "".equals(objId.toString().trim())) {// 没有id
			isUpdate = false;
		} else {
			isUpdate = true;
		}

		return isUpdate;
	}

	/**
	 * 
	 * <li>方法名：getLoginUsers
	 * <li>
	 * 
	 * @return
	 * <li>返回类型：Users
	 * <li>说明：获得登录操作员信息，如用户未登录则返回null
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-12-22
	 * <li>修改人：
	 * <li>修改日期：
	 */
	public AcOperator getLoginAcOperator() {
		Object obj = getRequest().getSession().getAttribute(
				LoginPurviewCheckFilter.USERS_SESSION_NAME);
		if (obj != null) {
			return (AcOperator) obj;
		} else {
			LOG.info("用户还没有登录,返回null");
			return null;
		}
	}
	
	/**
	 * <li>方法名：getLoginAcOperatorOmOrg
	 * <li>@return
	 * <li>返回类型：OmOrganization
	 * <li>说明：获取当前登录操作员的机构信息
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	protected OmOrganization getLoginAcOperatorOmOrg(){
		Object obj = getSession().getAttribute("org");
		if (obj != null) {
			return (OmOrganization) obj;
		} else {
			LOG.info("用户还没有等录,返回null");
			return null;
		}
	}
	
	/**
	 * <li>方法名：getLoginAcOperatorOmEmp
	 * <li>@return
	 * <li>返回类型：OmEmployee
	 * <li>说明：获取当前登录操作员的员工信息
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	protected OmEmployee getLoginAcOperatorOmEmp(){
		Object obj = getSession().getAttribute("emp");
		if (obj != null) {
			return (OmEmployee) obj;
		} else {
			LOG.info("用户还没有等录,返回null");
			return null;
		}
	}
	
	/**
	 * <li>方法名：getLoginAcOperatorAcRoles
	 * <li>@return
	 * <li>返回类型：List<AcRole>
	 * <li>说明：获取当前登录操作员所具有的角色集合
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	protected List<AcRole> getLoginAcOperatorAcRoles(){
		Object obj = getSession().getAttribute("roles");
		if (obj != null) {
			return (List<AcRole>) obj;
		} else {
			LOG.info("用户还没有等录,返回null");
			return null;
		}
	}
	
	/**
	 * <li>方法名：download
	 * <li>返回类型：void
	 * <li>说明：下载附件
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2012-06-04
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void download() throws Exception{
		try {
			String dirName = this.getRequest().getParameter("dirName"); //子目录名(upload/子目录)
			String fileName = "";
			if(StringUtils.isNotBlank(dirName)){
				fileName = dirName + "/";
			}
			fileName += this.getRequest().getParameter("fileName"); //文件名
			if(StringUtils.isNotBlank(fileName)){
				FileUploadUtil.download(URLDecoder.decode(fileName, "utf-8"), this.getRequest(), this.getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <li>方法名：loginBps
	 * <li>@param acOperator
	 * <li>返回类型：void
	 * <li>说明：登录BPS引擎
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-7-20
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public void loginBps(AcOperator acOperator){
		loginBps(acOperator ,this.getRequest());
	}
	
	@SuppressWarnings("unchecked")
	public void loginBps(AcOperator acOperator ,HttpServletRequest req){
		try{
			//登录BPS
			//BPSServiceClientFactory.getLoginManager().login(this.getRequest(), acOperator.getUserid(), acOperator.getOperatorname());
			//设置BPS当前活动用户
			//BPSServiceClientFactory.getLoginManager().setCurrentUser(acOperator.getUserid(), acOperator.getOperatorname());
			
			Object bpsSerobj = BeanUtils.invokePrivateMethod("com.eos.workflow.api.BPSServiceClientFactory", "getLoginManager", null);
			//登录BPS
			Class clazz = bpsSerobj.getClass();
			Method m = clazz.getDeclaredMethod("login",HttpServletRequest.class,String.class,String.class);
			m.invoke(bpsSerobj, new Object[]{req, acOperator.getUserid(), acOperator.getOperatorname()});
			//设置BPS当前活动用户
			BeanUtils.invokePrivateMethod(bpsSerobj, "setCurrentUser", new String[]{acOperator.getUserid(), acOperator.getOperatorname()});
		}catch(Exception ex){
			System.out.println("未使用BPS时，不用管该错误" + ex.getMessage());
		}
	}
	
	/**
	 * <li>方法名：logoutBps
	 * <li>返回类型：void
	 * <li>说明：退出BPS引擎
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-7-20
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void logoutBps(){
		try{
			//退出BPS
			//BPSServiceClientFactory.getLoginManager().logout(this.getRequest());
			Object bpsSerobj = BeanUtils.invokePrivateMethod("com.eos.workflow.api.BPSServiceClientFactory", "getLoginManager", null);
			Class clazz = bpsSerobj.getClass();
			Method m = clazz.getDeclaredMethod("logout",HttpServletRequest.class);
			m.invoke(bpsSerobj, new Object[]{this.getRequest()});
		}catch(Exception ex){
			System.out.println("未使用BPS时，不用管该错误" + ex.getMessage());
		}
	}
}