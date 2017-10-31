package com.yunda.sb.repair.process.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.repair.process.entity.RepairWorkOrder;
import com.yunda.sb.repair.process.entity.RepairWorkOrderBean;
import com.yunda.sb.repair.process.manager.RepairWorkOrderManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairWorkOrder控制器，数据表：E_REPAIR_WORK_ORDER
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings(value = "serial")
public class RepairWorkOrderAction extends JXBaseAction<RepairWorkOrder, RepairWorkOrder, RepairWorkOrderManager> {

	/** 日志工具 */
    Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * <li>说明：重写排序规则，增加默认按顺序号排序
     * <li>创建人：何涛
     * <li>创建日期：2016年7月8日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @param req http请求对象
     * @throws Exception 
     */
    @Override
    protected Order[] getOrders() {
    	List<Order> orderList = getOrderList();
    	Order order = Order.asc("sortNo");
    	if (!orderList.contains(order)) {
    		orderList.add(0, order);
    	}
    	return orderList.toArray(new Order[orderList.size()]);
    }
    
    /**
	 * <li>说明：设备检修作业工单联合分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
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
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			RepairWorkOrderBean entity = Fastjson.toObject(entityJson, RepairWorkOrderBean.class);
			SearchEntity<RepairWorkOrderBean> searchEntity = new SearchEntity<RepairWorkOrderBean>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}
    
    /**
     * <li>说明：完成设备检修作业工单
     * <li>创建人：黄杨
     * <li>创建日期：2017年5月5日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void updateFinish() throws JsonMappingException, IOException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
    	try {
    		// 设备检修作业工单idx主键
    		String idx = req.getParameter("idx");
    		// 其他作业人员
    		String otherWorkerName = req.getParameter("otherWorkerName");
    		// 实修记录
    		String repairRecord = req.getParameter("repairRecord");
    		
    		// 附件（照片）在服务器磁盘上的存放路径数组，保存到服务器磁盘的全路径数组：如：["F:\EquipmentUpload\e_fault_order\2016\8\20160810150140320.jpg"]
			String[] filePathArray = JSONUtil.read(StringUtil.nvl(req.getParameter("filePathArray"), "[]"), String[].class);
    		
    		this.manager.updateFinish(idx, otherWorkerName, repairRecord, filePathArray);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
    }
    
    /**
     * <li>说明：获取设备检修作业工单历史实修记录
     * <li>创建人：黄杨
     * <li>创建日期：2017年5月5日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void findHistoryRepairRecords() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
    	try {
    		// 设备检修作业范围作业项定义主键
    		String defineIdx = req.getParameter("defineIdx");
    		
    		List<Object> historyRepairRecords = this.manager.findHistoryRepairRecords(defineIdx);
			map.put("historyRepairRecords", historyRepairRecords);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
    }
    
    /**
     * <li>说明：批量完成设备检修作业工单
     * <li>创建人：黄杨
     * <li>创建日期：2017年5月5日
     * <li>修改人：
     * <li>修改内容：
     * <li>修改日期：
     * @throws IOException 
     * @throws JsonMappingException 
     */
	public void updateFinishBatch() throws JsonMappingException, IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
    	try {
    		String jsonData = req.getParameter("jsonData");
    		Map<String, List<RepairWorkOrder>> workOrders = JSON.parseObject(jsonData, new TypeReference<Map<String, List<RepairWorkOrder>>>(){});
    		this.manager.updateFinishBatch(workOrders, null);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
    }
    
	/**
	 * <li>说明：设备检修作业工单处理 - web
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void updateFinished() throws IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
    	try {
    		String idx = req.getParameter("idx");
    		String otherWorkerName = req.getParameter("otherWorkerName");
    		String repairRecord = req.getParameter("repairRecord");
    		RepairWorkOrder entity = this.manager.updateFinished(idx, otherWorkerName, repairRecord);
			map.put(Constants.ENTITY, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}
	
}
