package com.yunda.sb.inspect.record.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.inspect.record.entity.InspectRecord;
import com.yunda.sb.inspect.record.manager.InspectRecordManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectRecord控制器，数据表：E_INSPECT_RECORD
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
public class InspectRecordAction extends JXBaseAction<InspectRecord, InspectRecord, InspectRecordManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明： 分页查询 - pda巡检任务处理
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws JsonMappingException
	 *@throws IOException
	 */
	public void findPageList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
			InspectRecord entity = JSONUtil.read(searchJson, InspectRecord.class);
			SearchEntity<InspectRecord> searchEntity = new SearchEntity<InspectRecord>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 巡检记录处理，支持批量处理巡检记录
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws JsonMappingException
	 *@throws IOException
	 */
	public void confirm() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = StringUtil.nvlTrim(req.getParameter("ids"), "[]");
			String checkResult = StringUtil.nvlTrim(req.getParameter("checkResult"), InspectRecord.CHECK_RESULT_HG);

			// 附件（照片）在服务器磁盘上的存放路径数组，保存到服务器磁盘的全路径数组：如：["F:\EquipmentUpload\e_inspect_record\2017\4\20170424150140320.jpg"]
			String[] filePathArray = JSONUtil.read(StringUtil.nvl(req.getParameter("filePathArray"), "[]"), String[].class);

			// 巡检记录处理
			this.manager.confirm(JSONUtil.read(ids, String[].class), checkResult, filePathArray);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 设备巡检记录联合分页查询
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void queryPageList() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			InspectRecord entity = Fastjson.toObject(entityJson, InspectRecord.class);
			SearchEntity<InspectRecord> searchEntity = new SearchEntity<InspectRecord>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 获取巡检记录处理情况饼图数据源，获取指定巡检设备下已巡检、未巡检记录数
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void queryChartData() throws IOException {
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		// 巡检设备idx主键
		String planEquipmentIdx = req.getParameter("planEquipmentIdx");
		// 检修类型（1：机械、2：电气、3：其它）
		String repairType = req.getParameter("repairType");
		// 获取巡检记录处理情况饼图数据源，获取指定巡检设备下已巡检、未巡检记录数
		List<Map<String, Object>> result = this.manager.queryChartData(planEquipmentIdx, Integer.parseInt(repairType));
		// 返回结果集
		JSONUtil.write(resp, result);
	}

	/**
	 * <li>说明： 设备巡检记录处理，同一设备下的巡检记录处理完成后，反向更新巡检设备处理状态
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void updateFinished() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 设备巡检记录idx主键
			String idx = req.getParameter("idx");
			// 检查结果（合格，不合格） ，如果为null，则默认为：合格
			String checkResult = req.getParameter("checkResult");
			// 设备巡检记录处理，同一设备下的巡检记录处理完成后，反向更新巡检设备处理状态
			this.manager.updateFinished(idx, checkResult, null);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
