package com.yunda.sb.repair.scope.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.base.order.BaseOrderAction;
import com.yunda.sb.repair.scope.entity.RepairScope;
import com.yunda.sb.repair.scope.manager.RepairScopeManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：RepairScope控制器，数据表：E_REPAIR_SCOPE
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class RepairScopeAction extends BaseOrderAction<RepairScope, RepairScope, RepairScopeManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	// excel导入
	private File file;

	/**
	 * <li>说明：重写查询方法，增加按“设备类别名称”和“顺序号”多重排序
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param req http请求对象
	 * @param resp http返回对象
	 * @throws IOException 
	 */
	@Override
	public void pageQuery() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			List<Order> orderList = new ArrayList<Order>(2);
			orderList.add(Order.asc("classCode"));
			orderList.add(Order.asc("seqNo"));
			QueryCriteria<RepairScope> query = new QueryCriteria<RepairScope>(getQueryClass(), getWhereList(), orderList, getStart(), getLimit());
			map = this.manager.findPageList(query).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：批量保存巡检范围
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
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
			RepairScope[] scopes = Fastjson.toObject(req, RepairScope[].class);
			this.manager.save(scopes, classCode);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：导入设备检修范围
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void importRepairScope() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.manager.importRepairScope(file);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
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
