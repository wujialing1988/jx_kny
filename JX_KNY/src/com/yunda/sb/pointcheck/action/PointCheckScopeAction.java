package com.yunda.sb.pointcheck.action;

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
import com.yunda.sb.pointcheck.entity.PointCheckScope;
import com.yunda.sb.pointcheck.manager.PointCheckScopeManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: PointCheckScope控制器，数据表：SBJX_POINT_CHECK_SCOPE
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月15日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
public class PointCheckScopeAction extends BaseOrderAction<PointCheckScope, PointCheckScope, PointCheckScopeManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明：批量保存设备点检范围
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：     
	 * @throws IOException 
	 */
	public void save() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String classCode = req.getParameter("classCode");
			PointCheckScope[] scopes = JSONUtil.read(req.getParameter("jsonData"), PointCheckScope[].class);
			this.manager.save(scopes, classCode);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：点检范围选择添加的数据分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：     
	 * @param req http请求对象
	 * @param resp http返回对象
	 * @throws IOException
	 */
	public void pageSelectQuery() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			String checked = StringUtil.nvl(req.getParameter("checked"), "true");
			PointCheckScope entity = Fastjson.toObject(entityJson, PointCheckScope.class);
			SearchEntity<PointCheckScope> searchEntity = new SearchEntity<PointCheckScope>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.pageSelectQuery(searchEntity, Boolean.parseBoolean(checked)).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：初始化，设置设备类别名称首拼
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @throws IOException
	 */
	public void initClassNamePY() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			this.manager.initClassNamePY();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：导入点检项目
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void importCheckScope() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.manager.importPointCheckScope(file);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
			ServletActionContext.getResponse().setContentType("text/html");
			ServletActionContext.getResponse().getWriter().write(JSONUtil.write(map));
		}

	}

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
