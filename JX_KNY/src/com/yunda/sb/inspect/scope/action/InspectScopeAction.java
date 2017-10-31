package com.yunda.sb.inspect.scope.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.base.order.BaseOrderAction;
import com.yunda.sb.inspect.scope.entity.InspectScope;
import com.yunda.sb.inspect.scope.manager.InspectScopeManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备巡检项目（标准）控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class InspectScopeAction extends BaseOrderAction<InspectScope, InspectScope, InspectScopeManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	private File file;

	/**
	 * <li>说明： 批量保存巡检范围
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void save() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String classCode = req.getParameter("classCode");
			String repairType = req.getParameter("repairType");
			InspectScope[] scopes = Fastjson.toObject(req, InspectScope[].class);
			this.manager.save(scopes, classCode, Integer.valueOf(repairType));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 巡检范围选择添加的数据分页查询
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void pageSelectQuery() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			String checked = StringUtil.nvl(req.getParameter("checked"), "true");
			InspectScope entity = Fastjson.toObject(entityJson, InspectScope.class);
			SearchEntity<InspectScope> searchEntity = new SearchEntity<InspectScope>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.pageSelectQuery(searchEntity, Boolean.parseBoolean(checked)).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 初始化，设置设备类别名称首拼
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void initClassNamePY() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.manager.initClassNamePY();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}

	/**
	 * <li>说明： 导入设备巡检标准
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void importInspectScope() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.manager.importInspectScope(file);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}finally{
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			ServletActionContext.getResponse().setContentType("text/html");
			ServletActionContext.getResponse().getWriter().write(JSONUtil.write(map));
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
