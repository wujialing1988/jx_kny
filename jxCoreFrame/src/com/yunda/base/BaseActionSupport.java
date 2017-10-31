package com.yunda.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：框架封装的最顶层的Action基类，具体功能的Action一般不直接继承于此类
 * <li>本类继承于struts 的ActionSupport类，并实现了Spring的ApplicationContextAware.
 * <li>提供从spring容器中获取具体功能Action注入的范型Manager对象。实现了struts零配置。
 * <li>定义了一些和前台交互的公用变量。
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部
 * @version 1.0
 */
public class BaseActionSupport extends ActionSupport implements ApplicationContextAware{
	
	private static final long serialVersionUID = 2878835572666434734L;
	//  0 configuration for struts2
	protected final static String DEFAULTFOLDER = "/jsp/";
    protected final static String JSPPAGE = "jsppage"; 
    
    protected static final String TOEDIT = "toEdit";
    protected static final String GETLIST = "getList";
    protected static final String SEARCH = "search";
    protected static final String TOVIEW = "toView";
    protected static final String TREE = "tree";
    
    protected static final String TOADD = "toAdd";
    protected static final String DELETEBYID = "deleteById";
    protected static final String DELETEBYIDS = "deleteByIds";
    protected static final String SAVE = "save";
    protected static final String SAVETOLIST = "saveToList";

    protected static final String MSG_SAVE_SUCCESS = "【保存】成功！";
    protected static final String MSG_DELETE_SUCCESS = "【删除】成功！";
    protected static final String MSG_INSERT_SUCCESS = "【新增】成功！";
    protected static final String MSG_UPDATE_SUCCESS = "【修改】成功！";
    protected static final String MSG_GIVEUP_SUCCESS = "【弃用】成功！";
    protected static final String MSG_STARTUP_SUCCESS = "【启用】成功！";
    protected static final String MSG_FAIL = "操作失败，请重试！";
    
	//	 JSP 页面与 action 交互及导航常量
    protected String LISTPAGE = "_list.jsp";	// 查询
    protected String EDITPAGE = "_edit.jsp";	// 单表编辑
    protected String VIEWPAGE = "_view.jsp";	// 查询
    protected String EDITMASTERDETAILPAGE = "_editmasterdetail.jsp"; // 主从表编辑    
    protected String SUCCESSPAGE = "_success.jsp"; 
    protected String EXPORTTOEXCELPAGE = "_exporttoexcel.jsp"; //导出excel
    
    protected String pageSubFolder = "common";
    
    protected Map<String,String> navigationMap = new HashMap<String,String>();
    
    protected String target; 
    public static ApplicationContext ctx;	   // spring bean 容器上下文
    
    protected Class entityClass;	           // 实体类型名
    protected String actionClass;	           // 具体action类型名
    
    protected String serviceName;	           // 具体action类型依赖的服务类型名
    protected String entityName;  
    
    protected String id;                       // 实体的id,主要用于根据id获取实体
    protected String[] ids;                    //用于保存多个业务实体的ID数组    
    protected Collection dataList; 
    protected String createOrEditMethod;
    protected String[] filePath;//文件路径数组
    
    protected String refreshFlag = "not refresh"; // 该变量用来设置在树型功能页面中是刷新父节点，还子节点，还是不刷新。
    
    /**
     * <li>说明：如果程序员中的程序需要访问request对象，本基类通过本方法提供
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return request对象
     */
    public HttpServletRequest getRequest(){	
		return ServletActionContext.getRequest();
	}
    
	/**
     * <li>说明：如果程序员中的程序需要访问response对象，本基类通过本方法提供
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return response对象
	 */
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
    /**
     * <li>说明：获取URL参数对应的值（若存在参数重名，则返回第一个匹配的参数值）
     * <li>创建人：温俊
     * <li>创建日期：2012-6-18
     * <li>修改人：何涛
     * <li>修改日期：2015-08-19
     * <li>修改内容：重构
     * @param param 参数名称
     * @return 参数值
     */
    @SuppressWarnings("deprecation")
	public String getParameter(String param) {
    	return getParameter(param, null);
    }
    
    /**
     * <li>说明：根据指定编码方式获取URL参数对应的值并解码，一般用于二次编码（若存在参数重名，则返回第一个匹配的参数值）
     * <li>创建人：温俊
     * <li>创建日期：2012-6-18
     * <li>修改人：何涛
     * <li>修改日期：2015-8-19
     * <li>修改内容：重构
     * @param paramName 参数名称
     * @param encoding 编码格式
     * @return  参数值
     */
    @SuppressWarnings("deprecation")
    public String getParameter(String paramName, String encoding) {
        if (StringUtils.isNotBlank(paramName)) {
            try {
                String val = this.getRequest().getParameter(paramName);
                if (StringUtils.isNotBlank(val)) {
                    return StringUtils.isBlank(encoding) ? 
                        URLDecoder.decode(val) : URLDecoder.decode(val, encoding);
                }
                return "";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	/**
     * <li>说明：如果程序员中的程序需要访问session对象, 本基类通过本方法提供
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return session对象
	 */
	public HttpSession getSession(){
		return getRequest().getSession();
	}
    
    /**
     * <li>说明：参数为要返回的JSP页面名，系统自动导航到指定的页面。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param target 系统返回的JSP文件名
     * @return 导航到jsp页面，返回JSPPAGE
     */
    public String renderJSPPage(String target){   
        setTarget(target);   
        return JSPPAGE;   
    }
    
    /**
     * <li>说明：这个函数一般不会手工调用到，框架中在配置文件中获取要导航到的文件名时，自动调用这个方法。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 返回要导航到的页面
     */
    public String getTarget() {   
        return target;   
    }
    
    /**
     * <li>说明：程序要明确导航到某个页面，必须在代码中调用此函数进行设置要导航到的页面名称。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param target 需要导航到的页面名称
     */
    public void setTarget(String target) {   
        this.target = target;   
    }
    
	/**
     * <li>说明：程序的JSP页面根据命名规范，文件组织规范存放，此方法可以获取到某个页面的文件组织结构路径。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 页面的文件结构
	 */
	public String getPageSubFolder() {
		return pageSubFolder;
	}
    
	/**
     * <li>说明：程序的JSP页面根据命名规范，文件组织规范存放，此方法可以设置某个Action基类所调用jsp页面的文件组织结构路径。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param pageSubFolder 页面的文件结构，一般在构造函数中调用
	 */
	public void setPageSubFolder(String pageSubFolder) {
		this.pageSubFolder = pageSubFolder;
	}
    
	/**
     * <li>说明：获取方法返回页面映射表。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 该映射表用于记录某个方法执行完毕返回到哪个页面，当页面发生异常后，框架可以根据映射表中的设置返回到指定页面，并显示提示信息 
	 */
	public Map<String, String> getNavigationMap() {
		return navigationMap;
	}
    
	/**
     * <li>说明：设置方法返回页面映射表。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param navigationMap 映射表Map对象，框架可以根据映射表中的设置返回到指定页面
	 */
	public void setNavigationMap(Map<String, String> navigationMap) {
		this.navigationMap = navigationMap;
	}
    
	/**
     * <li>说明：获取用于和前台进行数据交换的id变量
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 用于和前台进行数据交换的id变量
	 */
	public String getId() {
		return this.id;
	}
    
	/**
	 * <li>说明：设置用于和前台进行数据交换的id变量，该方法在struts拦截器中自动调用
	 * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id 用于和前台进行数据交换的id变量
	 */
	public void setId(String id) {
		this.id = id;
	}
    
	/**
     * <li>说明：获取用于和前台进行数据交换的ids变量,该变量保存的是id的一个数组
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 用于和前台进行数据交换的ids变量,该变量保存的是id的一个数组
	 */
	public String[] getIds(){
		return this.ids;
	}
    
	/**
     * <li>说明：设置用于和前台进行数据交换的ids变量,该变量保存的是id的一个数组，该方法在struts拦截器中自动调用
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 用于和前台进行数据交换的ids变量,该变量保存的是id的一个数组
	 */
	public void setIds(String[] ids) {
		this.ids = ids;
	}
    
	/**
     * <li>说明：获取用于action和页面之间进行查询结果集合传递的变量
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 用于action和页面之间进行查询结果集合传递的变量
	 */
	public Collection getDataList() {
		return dataList;
	}
    
	/**
	 * <li>说明：设置用于action和页面之间进行查询结果集合传递的变量
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param dataList 用于action和页面之间进行查询结果集合传递的变量
	 */
	public void setDataList(Collection dataList) {
		this.dataList = dataList;
	}
    
	/**
     * <li>说明：获取应用上下文环境，从应用上下文中可以获取需要的业务服务对象
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 应用上下文环境，从应用上下文中可以获取需要的业务服务对象
	 */
	public ApplicationContext getCtx() {
		return ctx;
	}

	/**
	 * <li>说明：设置应用上下文环境，从应用上下文中可以获取需要的业务服务对象
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ctx 应用上下文环境，从应用上下文中可以获取需要的业务服务对象
	 */
	@SuppressWarnings("static-access")
	public void setCtx(ApplicationContext ctx) {
		if(this.ctx == null){
			this.ctx = ctx;
		}
	}
	
    /**
     * <li>说明：该方法是实现ApplicationContextWare接口必须实现的一个方法，目的是将上下文环境对象设置到本对象中。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ctx 应用上下文环境，从应用上下文中可以获取需要的业务服务对象
     */
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		if(this.ctx == null){
			this.ctx = ctx;
		}
	}
	
	/**
     * <li>说明：获取本Action 所操作的实体类的类型
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 本Action 所操作的实体类的类型
	 */
	protected Class getEntityClass() {
		return entityClass;
	}
	
	/**
	 * <li>说明：获取本Action的类型
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 本Action的类型
	 */
	public String getActionClass() {
		return actionClass;
	}
	
	/**
	 * <li>说明：设置本Action的类型
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param actionClass 本Action的类型
	 */
	public void setActionClass(String actionClass) {
		this.actionClass = actionClass;
	}
	
	/**
	 * <li>说明：因为新增实体和编辑实体共用edit页面，该变量在edit页面中用于标示某次请求是新建实体还是修改实体
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 标示某次请求是新建实体还是修改实体
	 */
	public String getCreateOrEditMethod() {
		return createOrEditMethod;
	}
	
	/**
	 * <li>说明：因为新增实体和编辑实体共用edit页面，该变量在edit页面中用于标示某次请求是新建实体还是修改实体
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param createOrEditMethod 请求是新建实体还是修改实体
	 */
	public void setCreateOrEditMethod(String createOrEditMethod) {
		this.createOrEditMethod = createOrEditMethod;
	}
	
	/**
     * <li>说明：获取刷新标识
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 刷新标识
	 */
	public String getRefreshFlag() {
		return refreshFlag;
	}

	/**
     * <li>说明：设置刷新标识
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param refreshFlag 刷新标识
	 */
	public void setRefreshFlag(String refreshFlag) {
		this.refreshFlag = refreshFlag;
	}

	/**
     * <li>说明：添加ajax消息处理方法；使用ajax请求，向客户端发送消息
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-6
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param message 消息内容
	 */
	public void ajaxMessage(String message){
		this.ajaxMessage(message, "text/html");
	}
	
	/**
     * <li>说明：添加ajax消息处理方法；使用ajax请求，向客户端发送消息。
     * <li>创建人：曾锤鑫
     * <li>创建日期：2011-01-6
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param message  消息内容
	 * @param contentType 编码格式
	 */
	public void ajaxMessage(String message, String contentType) {
        if (StringUtils.isBlank(contentType)) {
            getResponse().setContentType("text/html; charset=UTF-8");
        } else {
            getResponse().setContentType(contentType + "; charset=UTF-8");
        }
        PrintWriter out = null;
        try {
            out = getResponse().getWriter();
            out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    public String[] getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String[] filePath) {
        this.filePath = filePath;
    }
}
