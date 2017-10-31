package com.yunda.sb.pointcheck.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.pointcheck.entity.PointCheckCatalog;
import com.yunda.sb.pointcheck.entity.PointCheckCatalogBean;
import com.yunda.sb.pointcheck.manager.PointCheckCatalogManager;
import com.yunda.sb.pointcheck.manager.PointCheckCatalogManager.Sbdjtj;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheckCatalogAction
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PointCheckCatalogAction extends JXBaseAction<PointCheckCatalog, PointCheckCatalog, PointCheckCatalogManager> {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：查询设备点检目录
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPageList() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			PointCheckCatalogBean entity = JSONUtil.read(entityJson, PointCheckCatalogBean.class);
			SearchEntity<PointCheckCatalogBean> searchEntity = new SearchEntity<PointCheckCatalogBean>(entity, getStart(), getLimit(), null);
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：设备点检统计-日度
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void statisticsByDaily() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			Sbdjtj entity = JSONUtil.read(entityJson, Sbdjtj.class);
			SearchEntity<Sbdjtj> searchEntity = new SearchEntity<Sbdjtj>(entity, getStart(), getLimit(), null);
			map = this.manager.statisticsByDaily(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：保存点检目录明细，添加已选择的设备到点检目录中
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
			String[] equipmentIds = JSONUtil.read(req.getParameter("equipmentIds"), String[].class);
			this.manager.save(equipmentIds);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：通过设备点检任务单初始化设备点检目录数据
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void initDataByPointCheck() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			this.manager.initDataByPointCheck();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
