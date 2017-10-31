package com.yunda;

import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SystemInit extends HttpServlet implements ApplicationContextAware {

	private static final long serialVersionUID = -6636941827518900046L;

	private static Log log = LogFactory.getLog(SystemInit.class);

	private static Application app;

	private ApplicationContext ctx;

	/**
	 * 初始化selevet,启动相关的服务以及加载必要的系统全局参数
	 */
	@SuppressWarnings("static-access")
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// app = Application.getInstance();
		app.setSessionFactory((SessionFactory) (app
				.getSpringApplicationContext().getBean("sessionFactory")));

		// 开始初始化应用程序
//		startup(config);
		// 检查缓存配置
		checkCacheConfig();
		log.info("应用初始化成功。");
	}
	
	/**
	 * <li>说明：启动是检查并提示缓存开启信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("static-access")
	public void checkCacheConfig(){
		SessionFactoryImpl impl = (SessionFactoryImpl) app.getSessionFactory();
		boolean secondLevelCacheStatus = impl.getSettings().isSecondLevelCacheEnabled();
		boolean queryCacheStatus = impl.getSettings().isQueryCacheEnabled();
		//如果二级缓存和查询缓存并非同时为开启或者关闭
		if(secondLevelCacheStatus!=queryCacheStatus) {
			throw new RuntimeException("Hibernate缓存配置错误!");
		}
		else {
			System.out.println("********************************");
			System.out.println("Hibernate 二级缓存..."+(secondLevelCacheStatus?"已启用":"未启用")+"!");
			System.out.println("Hibernate 查询缓存..."+(queryCacheStatus?"已启用":"未启用")+"!");
			System.out.println("********************************");
		}
	}

	/***************************************************************************
	 * 加载应用列表，并初始化各个应用
	 * 
	 */
//	@SuppressWarnings("unused")
//	private void startup(ServletConfig config) {
//		// TODO and FIXME
//		// 第一步：这里需要加载各个应用模块
//		// 第二步：逐一为每个应用模块初始化其应用配置参数
//		// 第三步：逐一为每个应用模块初始化其应用配置参数
//		initDicPath(config);
//		//暂时注释
//		//initLog4j(config);
//		//initSysOptions(config);
//	}

//	/**
//	 * <li>方法名：initDicPath
//	 * <li>
//	 * 
//	 * @param config
//	 *            <li>返回类型：void
//	 *            <li>说明：初始化字典文件路径
//	 *            <li>创建人：曾锤鑫
//	 *            <li>创建日期：2011-4-13
//	 *            <li>修改人：
//	 *            <li>修改日期：
//	 */
//	private void initDicPath(ServletConfig config) {
//		String str_DicPath = config.getServletContext().getRealPath("dic");
//		DicCache.setDicPath(str_DicPath);
//	}

	/**
	 * 初始化应用程序的应用配置参数
	 * 
	 * @param config
	 */
	@SuppressWarnings("unused")
	private void initAppConfig(ServletConfig config) {

	}

	/**
	 * <li>方法名：initLog4j
	 * <li>
	 * 
	 * @param config
	 *            <li>返回类型：void
	 *            <li>说明：初始化log4j
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-4-2
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	@SuppressWarnings("unused")
	private void initLog4j(ServletConfig config) {
		String prefix = config.getServletContext().getRealPath("/");
		

		System.out.println("serverInfo : " + config.getServletContext().getServerInfo() );
		
		System.out.println("/////////////////////////////////////]]]]]]" +prefix );
		try {
			System.out.println("/////////////////////////////////////]]]]]]" +config.getServletContext().getResource("/"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String file = getInitParameter("log4j");
		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
			log.info("log4j初始化成功");
		}
	}

//	/**
//	 * 初始化应用程序的各种码表或选项值
//	 * 
//	 * @param config
//	 */
//	@SuppressWarnings("unused")
//	private void initSysOptions(ServletConfig config) {
//		try {
//			loadSysOptions(new FileInputStream(config.getServletContext()
//					.getRealPath("/WEB-INF/select-config/system.xml")));
//
//		} catch (FileNotFoundException e) {
//			log.fatal("文件System.xml无法找到！", new Error(e));
//			e.printStackTrace();
//		} catch (Exception e) {
//			log.fatal("初始化System.xml文件失败！", new Error(e));
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * 为每个应用模块加载其各种码表或系统选项。
//	 * 
//	 * @param is
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	private void loadSysOptions(InputStream is) throws Exception {
//		SAXReader reader = new SAXReader();
//		Document doc = reader.read(is);
//		is.close();
//
//		Map<String, SysParam> sysParamMap = new TreeMap<String, SysParam>();
//		List<Node> paramList = doc.selectNodes("//system-params/param");
//		if (paramList != null && paramList.size() > 0) {
//			for (Node n : paramList) {
//				SysParam param = new SysParam();
//				param.setName(n.valueOf("@name"));
//				param.setSource(n.valueOf("@source"));
//				param.setType(n.valueOf("@type"));
//				List<Node> data = n.selectNodes("./data");
//				LinkedHashMap<String, String> dataMap = new LinkedHashMap<String, String>();
//				if (data.size() > 0) {
//					for (Node b : data) {
//						dataMap.put(b.valueOf("@key"), b.getText());
//					}
//					param.setData(dataMap);
//				}
//				// //zmh增加Application的属性
//				String beanName = n.valueOf("@appBeanName");// 设置为Application的bean名称zmh
//				if (beanName != null && !"".equals(beanName)) {
//					getServletConfig().getServletContext().setAttribute(
//							beanName, dataMap);
//					if ("AppInstallDept".equals(beanName)) {// 安装本系统的部门相关信息都添加到Application中
//						setInstallDeptEntity(getServletConfig()
//								.getServletContext(), dataMap);
//					}
//				}
//
//				sysParamMap.put(param.getName(), param);
//			}
//		}
//		app.setSysOptions(sysParamMap);
//	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.ctx = arg0;
		app = Application.getInstance();
		app.setSpringApplicationContext(this.ctx);
	}

	/**
	 * 
	 * <li>方法名：setInstallDeptEntity
	 * <li>
	 * 
	 * @param context
	 *            <li>
	 * @param deptCode
	 *            <li>返回类型：void
	 *            <li>说明：
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-4
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
//	@SuppressWarnings("unchecked")
//	private void setInstallDeptEntity(ServletContext context,
//			Map<String, String> dataMap) {
//		/*
//		 * Dept dept = new Dept(); dept.setDeptCode(dataMap.get("deptCode"));
//		 * dept.setDeptType(dataMap.get("deptType")); try { BaseManager manager =
//		 * FindHelper.getManager(dept, context);
//		 * 
//		 * Collection list = manager.find(dept);
//		 * 
//		 * if(list.size()>0){ context.setAttribute("AppInstallDeptEntity",
//		 * list.toArray()[0]); } } catch (Exception e) { e.printStackTrace(); }
//		 */
//	}
}