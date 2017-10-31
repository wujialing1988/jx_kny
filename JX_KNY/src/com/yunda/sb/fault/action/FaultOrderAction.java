package com.yunda.sb.fault.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.fault.entity.FaultOrder;
import com.yunda.sb.fault.entity.FaultOrderBean;
import com.yunda.sb.fault.entity.FaultStatisM;
import com.yunda.sb.fault.manager.FaultOrderManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：FaultOrder控制器，数据表：E_FAULT_ORDER
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
public class FaultOrderAction extends JXBaseAction<FaultOrder, FaultOrder, FaultOrderManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：重写保存方法，因为父类的保存方法对时间格式为Y-m-d H:i的日期字段不能正常解析
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void saveOrUpdate() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			FaultOrder t = JSONUtil.read(req, FaultOrder.class);
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
				// 返回记录保存成功的实体对象
				map.put(Constants.ENTITY, t);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：增加保存方法，解决Android客户端不能使用req流传json对象的问题
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void saveOrUpdate2() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 故障提票保存（更新）实体
			FaultOrder t = JSONUtil.read(req.getParameter(Constants.ENTITY_JSON), FaultOrder.class);
			// 故障提票附件，保存到服务器磁盘的全路径数组：如：["F:\EquipmentUpload\e_fault_order\2016\8\20160810150140320.jpg"]
			String[] filePathArray = JSONUtil.read(StringUtil.nvl(req.getParameter("filePathArray"), "[]"), String[].class);
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate2(t, filePathArray);
				// 返回记录保存成功的实体对象
				map.put(Constants.ENTITY, t);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：自动生成提票单编号
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void createFaultOrderNo() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			String faultOrderNo = this.manager.createFaultOrderNo(null);
			map.put("faultOrderNo", faultOrderNo);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：根据设备编码获取设备主要信息
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void getEquipmentByCode() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String equipmentCode = req.getParameter("equipmentCode");
			EquipmentPrimaryInfo equipment = this.manager.getEquipmentByCode(equipmentCode);
			if (null != equipment) {
				map.put(Constants.SUCCESS, true);
				map.put("equipment", equipment);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：保存故障提票调度派工信息
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void saveRepairTeam() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = req.getParameter("ids");
			FaultOrder entity = JSONUtil.read(req.getParameter("jsonData"), FaultOrder.class);
			this.manager.saveRepairTeam(JSONUtil.read(ids, String[].class), entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：保存故障提票工长派工信息
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void saveRepairEmp() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = req.getParameter("ids");
			FaultOrder entity = JSONUtil.read(req, FaultOrder.class);
			this.manager.saveRepairEmp(JSONUtil.read(ids, String[].class), entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：保存故障提票工长派工信息（PDA）
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void saveRepairEmpForPDA() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = req.getParameter("ids");
			FaultOrder entity = JSONUtil.read(req.getParameter(Constants.ENTITY_JSON), FaultOrder.class);
			this.manager.saveRepairEmp(JSONUtil.read(ids, String[].class), entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：故障提票处理
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void updateFinish() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String idx = req.getParameter("idx");
			FaultOrder entity = JSONUtil.read(req.getParameter(Constants.ENTITY_JSON), FaultOrder.class);
			// 故障提票附件，保存到服务器磁盘的全路径数组：如：["F:\EquipmentUpload\e_fault_order\2016\8\20160810150140320.jpg"]
			String[] filePathArray = JSONUtil.read(StringUtil.nvl(req.getParameter("filePathArray"), "[]"), String[].class);
			this.manager.updateFinished(idx, entity, filePathArray);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：Web端故障提票处理,并且管理员可以通过web端对已处理的结果进行修改
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void updateFinishWeb() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			FaultOrder entity = JSONUtil.read(req, FaultOrder.class);
			String idx = entity.getIdx();
			this.manager.updateFinishedWeb(idx, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：设备使用人确认故障提票处理
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void confirm() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = req.getParameter("ids");
			// 设备使用人确认故障提票处理结果
			this.manager.confirm(JSONUtil.read(ids, String[].class));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：分页查询，查询需要使用人确认的故障提票 - pda
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPageList2User() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			FaultOrder entity = JSONUtil.read(entityJson, FaultOrder.class);
			SearchEntity<FaultOrder> searchEntity = new SearchEntity<FaultOrder>(entity, getStart(), getLimit(), getOrders());
			if (null == searchEntity.getOrders() || 0 >= searchEntity.getOrders().length) {
				searchEntity.setOrders(new Order[] { Order.asc("faultOccurTime") });
			}
			// 分页查询
			map = this.manager.queryPageList2User(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：设备故障提票单退回
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void updateBacked() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = req.getParameter("ids");
			// 退回原因
			String backReason = req.getParameter("backReason");
			this.manager.updateBacked(JSONUtil.read(ids, String[].class), backReason);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：分页查询，统计设备在一段时间内发生故障的次数
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPageStatistics() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			FaultOrderBean entity = JSONUtil.read(entityJson, FaultOrderBean.class);
			SearchEntity<FaultOrderBean> searchEntity = new SearchEntity<FaultOrderBean>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageStatistics(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：分页查询，按月度统计设备故障发生次数
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPageStatisticsByMonth() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			FaultStatisM entity = JSONUtil.read(entityJson, FaultStatisM.class);
			SearchEntity<FaultStatisM> searchEntity = new SearchEntity<FaultStatisM>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageStatisticsByMonth(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
