package com.yunda.sb.base.common.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.Constants;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.TodoJobFunction;
import com.yunda.sb.base.combo.ILogicDelete;
import com.yunda.sb.base.common.IEditorGridManager;
import com.yunda.util.BeanUtils;
import com.yunda.util.DaoUtils;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: BaseController，公用控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
public class SbCommonAction {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/** 框架中的数据库Hibernate访问操作工具，可供所有的业务服务对象都调用此对象完成数据库的操作 */
	@Resource
	private DaoUtils daoUtils;

	/**
	 * <li>说明：根据实体类名称获取其对应管理器的类名称
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月30日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param className 实体类全限定名称，例如：com.yunda.entity.TestBean
	 * @return 实体类对应的管理器的类名称
	 */
	public static String getBusinessName(String className) {
		String simpleName = null;
		if (-1 != className.indexOf(".")) {
			simpleName = className.substring(className.lastIndexOf(".") + 1);
		} else {
			simpleName = className;
		}
		char[] charArray = simpleName.toCharArray();
		charArray[0] = (char) (charArray[0] + 32);
		return new String(charArray) + "Manager";
	}

	/**
	 * <li>说明：该方法用于Ext.grid.EditorGridPanel组件（可编辑的表格）进行数据存储，主要参数实例如下：
	 * {
	 * 		"className": "com.yunda.sbgl.repair.base.entity.RepairScope",				// 必要
	 *      "fieldName": "repairClassSmall",											// 必要
	 *		"entityJson": {
	 *			idx: "8a8284f255c3759a0155c37b16120009",								// 必要
	 *			repairClassSmall: "1"													// 必要
	 *		}	
	 * }
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void saveChange() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		map.put(Constants.SUCCESS, false);
		try {
			// 实体类全限定名称，例如：com.yunda.entity.TestBean
			String className = req.getParameter("className");
			Class<?> clazz = Class.forName(className);
			// JSON对象 {idx: "", fieldName: ""}
			String entityJson = req.getParameter(Constants.ENTITY_JSON);
			Object entity = JSONUtil.read(entityJson, clazz);

			String idx = (String) BeanUtils.forceGetProperty(entity, EntityUtil.IDX);
			Object object = daoUtils.get(idx, clazz);
			// 类属性名称
			String fieldName = req.getParameter("fieldName");

			BeanUtils.forceSetProperty(object, fieldName, BeanUtils.forceGetProperty(entity, fieldName));

			// 更新前的验证方法
			String businessName = getBusinessName(className);
			Object manager = Application.getSpringApplicationContext().getBean(businessName);
			if (manager instanceof IEditorGridManager) {
				@SuppressWarnings( { "unchecked" })
				String validateMsg = ((IEditorGridManager) manager).validateSaveChange(fieldName, object);
				if (null != validateMsg) {
					map.put(Constants.ERRMSG, validateMsg);
					return;
				}
			}

			// 设置实体类中与业务无关的系统信息字段：siteID,creator,createTime,updator,updateTime
			EntityUtil.setSysinfo(object);
			// 保存
			this.daoUtils.saveOrUpdate(object);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：获取单个实体对象，根据一个实体的单个字段进行查询，只有返回一个实体对象，主要参数实例如下：
	 * {
	 * 		"className": "com.yunda.sbgl.repair.base.entity.RepairScope",				// 必要
	 *      "fieldName": "idx",															// 必要
	 *		"fieldValue": "8a8284c95773bc23015773dc495a0001"							// 必要
	 * }
	 * <li>创建人: 黄杨
	 * <li>创建日期：2016年10月19日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param resp http响应对象
	 * @throws IOException
	 */
	public void getSingleEntity() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		try {
			// 实体类全限定名
			String className = req.getParameter("className");
			// 要查询的字段名称
			String fieldName = req.getParameter("fieldName");
			// 要查询的字段值
			String fieldValue = req.getParameter("fieldValue");

			StringBuilder sb = new StringBuilder();
			// 构造查询hql
			sb.append("From ").append(className).append(" Where ").append(fieldName).append(" = '").append(fieldValue).append("'");

			// 处理实体类中是否含有recordStatus字段的情况
			Class<?> clazz = Class.forName(className);
			if (ILogicDelete.class.isAssignableFrom(clazz) || EntityUtil.contains(clazz, EntityUtil.RECORD_STATUS)) {
				sb.append(" And recordStatus = 0");
			}
			List<?> find = this.daoUtils.find(sb.toString());
			if (null == find) {
				map.put(Constants.ERRMSG, "没有对应的这条信息，请重试！");
				map.put(Constants.SUCCESS, false);
				return;
			}
			if (1 != find.size()) {
				map.put(Constants.ERRMSG, "查询到多条数据，请重试！");
				map.put(Constants.SUCCESS, false);
				return;
			}
			map.put(Constants.ENTITY, find.get(0));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：获取系统操作员相关任务代办项
	 * <li>创建人：何涛
	 * <li>创建日期：2017年1月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param resp http响应对象
	 * @throws Exception
	 */
	public void getToDoListContext() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(2);
		HttpServletResponse resp = ServletActionContext.getResponse();
		map.put(Constants.SUCCESS, false);
		try {
			OmEmployee userData = SystemContext.getOmEmployee();
			if (null == userData) {
				map.put(Constants.ERRMSG, "系统异常，请重新登录！");
				return;
			}
			// 获取系统操作员负责的代办项
			List<TodoJob> toDoListContext = TodoJobFunction.getInstance().getToDoListContext(userData.getOperatorid().toString());
			map.put(Constants.SUCCESS, true);
			map.put("todoJob", toDoListContext);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

	public static void main(String[] args) {
		System.out.println(getBusinessName("com.yunda.Hetao"));
	}

}
