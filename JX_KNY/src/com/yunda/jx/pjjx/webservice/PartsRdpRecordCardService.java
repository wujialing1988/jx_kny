/**
 * 
 */
package com.yunda.jx.pjjx.webservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQCParticipant.QCEmp;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQCParticipantManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordCard;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRI;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordRIAndDI;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsCheckItemDataManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordDIManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordRIManager;
import com.yunda.jx.pjjx.util.HttpClientUtils;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：检修记录工单接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpRecordCardService?wsdl
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-7 下午02:59:44
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service("partsRdpRecordCardWS")
public class PartsRdpRecordCardService implements IPartsRdpRecordCardService {
	
//  通过资源文件获取url
    private static String url;
    
    private static final String PARTSCHECKITEMDATA = "partsCheckItemData";
    
    static {
        url = HttpClientUtils.instence.getProperties().getProperty(PARTSCHECKITEMDATA).trim();
        if (StringUtil.isNullOrBlank(url)) {
            throw new BusinessException("未读取到检测项数据配置项，请检查webservice.properties文件是否正确！");
        }
        
    }
    
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/** PartsRdpRecordCard业务类,配件检修记录卡实例 */
	@Resource
	private PartsRdpRecordCardManager partsRdpRecordCardManager;
	
	/** PartsRdpRecordRI业务类,配件检修检测项实例 */
	@Resource
	private PartsRdpRecordRIManager partsRdpRecordRIManager;
	
	/** PartsRdpRecordDI业务类,配件检测项 */
	@Resource
	private PartsRdpRecordDIManager partsRdpRecordDIManager;
	
	/** PartsRdpQCParticipant实体类, 数据表：质量可检查人员 */
	@Resource
	private PartsRdpQCParticipantManager partsRdpQCParticipantManager;
    
    /** PartsCheckItemData业务类,可视化数据采集结果 */
    @Resource
    private PartsCheckItemDataManager partsCheckItemDataManager;
    
    /** PartsRdp业务类,配件检修作业 */
    @Resource
    private PartsRdpManager partsRdpManager;
	
	/**
	 * <li>说明：施修工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 何涛
	 * <li>修改内容：修改不规范的异常处理，修改接口参数注释说明（测试通过）
	 * <li>修改日期：2016-04-01
	 * @param jsonObject {
            entityJson: {
                status:"03",
                rdpIDX:"8a8284f25146a0d8015146a7670d0003",
                rdpNodeIDX:"",
    		},
    		start:1,
    		limit:50, 
    		orders:[{
    			sort: "updateTime",
    			dir: "DESC"
    		}]
        }
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String pageList(String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		// 获取查询条件实体对象
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		PartsRdpRecordCard entity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
		
		// 查询开始索引
		int start = jo.getIntValue(Constants.START);
		// 查询条数
		int limit = jo.getIntValue(Constants.LIMIT);
		start = limit * (start - 1);
		
		// 排序字段
		JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
		List<Order> orderList = new ArrayList<Order>();
		if (null != jArray) {
		    Order[] orders = JSONTools.getOrders(jArray);
		    for (Order order : orders) {
		        orderList.add(order);
		    }
		}
		
		// 查询条件
		List<Condition> whereList = new ArrayList<Condition>();
		Condition con = null;
		if(!StringUtil.isNullOrBlank(entity.getStatus())){
		    con = new Condition("status", Condition.LLIKE, entity.getStatus());
		    whereList.add(con);
		}
		if(!StringUtil.isNullOrBlank(entity.getRdpIDX())){
		    con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
		    con.setStringLike(false);
		    whereList.add(con);
		}
		if (!StringUtil.isNullOrBlank(entity.getRdpNodeIDX())) {
		    con = new Condition("rdpNodeIDX", Condition.EQ, entity.getRdpNodeIDX());
		    whereList.add(con);
		}
		// 当前登录用户只能处理自己领取的工单
		/*if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(entity.getStatus())) {
            con = new Condition("handleEmpID", Condition.EQ, SystemContext.getOmEmployee().getEmpid());
            con.setStringLike(false);
            whereList.add(con);
        }*/
		
		QueryCriteria<PartsRdpRecordCard> query = new QueryCriteria<PartsRdpRecordCard>(PartsRdpRecordCard.class, whereList, orderList, start, limit);
		Page<PartsRdpRecordCard> page = this.partsRdpRecordCardManager.findPageList(query);
		
		List<PartsRdpRecordCard> list = page.getList();
		if (null == list || list.size() <= 0){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
		}
		for(PartsRdpRecordCard t : list){
		    t.setStatus(convertPartsRdpCardStatus(t.getStatus()));
		}
		return JSONTools.toJSONList(page.getTotal(), list);
	}
	
	/**
     * <li>方法说明：转换配件检修工单状态编码为状态名称
     * <li>创建人：张凡
     * <li>创建日期：2015-10-28
     * <li>修改人：何涛
     * <li>修改内容：重构，消除硬编码
     * <li>修改日期：2016-04-01
     * @param statusCode 配件检修作业工单状态编码
     * @return 作业工单状态名称
     */
    public static String convertPartsRdpCardStatus(String statusCode) {
        if (IPartsRdpStatus.CONST_STR_STATUS_XJ.equals(statusCode)) {
            return "已处理";
        }
        if (IPartsRdpStatus.CONST_STR_STATUS_ZLJYZ.equals(statusCode)) {
            return "质检中";
        }
        if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(statusCode)) {
            return "待处理";
        }
        if (IPartsRdpStatus.CONST_STR_STATUS_DLQ.equals(statusCode)) {
            return "待领取";
        }
        if (IPartsRdpStatus.CONST_STR_STATUS_WKF.equals(statusCode)) {
            return "未开放";
        }
        return "错误！未知状态";
    }

	/**
     * TODO 待作废
	 * <li>说明：待检验工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 何涛
	 * <li>修改内容：修改不规范的异常处理（测试通过）
	 * <li>修改日期：2015-04-01 
	 * @param jsonObject {
            entityJson:{
                rdpIDX: "772D7520EF5945B695CDB79F2E6CC1E7"
            },
            operatorId: "800109",
            checkWay: "1",
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String findPageListForQC(String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		// 查询记录开始索引
		int start = jo.getIntValue(Constants.START);
		// 查询记录条数
		int limit = jo.getIntValue(Constants.LIMIT);
		start = limit * (start - 1);
        
		// 排序字段
		JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
		Order[] orders = JSONTools.getOrders(jArray);
		
		// 质量检查人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		//检验方式“抽检[1]/必检[2]”
        String checkWay = jo.getString("checkWay");
        PartsRdpRecordCard entity = JSONUtil.read(jo.getString(Constants.ENTITY_JSON), PartsRdpRecordCard.class);
		
		SearchEntity<PartsRdpRecordCard> searchEntity = new SearchEntity<PartsRdpRecordCard>(entity, start, limit, orders);
		
		Page<PartsRdpRecordCard> page = this.partsRdpRecordCardManager.findPageListForQC(searchEntity, checkWay);
		return JSONTools.toJSONList(page);
	}

	/**
     * TODO 待作废：当前系统设计，已无领活操作
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 何涛
	 * <li>修改内容：修改不规范的异常处理 
	 * <li>修改日期：2016-04-01
	 * @param jsonObject {
		    idx: "8a8284f249abf9720149ac1f0f380005",
		    operatorId: 800109
        }
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String startUpJob(String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		// 待领取的工单主键
		String idx = jo.getString("idx");
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		// 验证正在领取的工单的当前状态是否已经不是“待领取”
		String validateMsg = this.partsRdpRecordCardManager.validateStatus(idx, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
		
		OperateReturnMessage msg = new OperateReturnMessage();
		if (null != validateMsg) {
			msg.setFaildFlag(validateMsg);
			return JSONUtil.write(msg);
        }
		try {
		    // 领活
            this.partsRdpRecordCardManager.startUpJob(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
	}

	/**
     * TODO 待作废：当前系统设计，已无领活操作
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理 
     * <li>修改日期：2016-04-01
	 * 
	 * @param jsonObject {
			ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String startUpBatchJob(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 待领取的工单主键数组
        String ids = jo.getString(Constants.IDS);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        String[] idArray = JSONUtil.read(ids, String[].class);
        // 验证正在领取的工单的当前状态是否已经不是“待领取”
        String[] validateMsg = this.partsRdpRecordCardManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
        
        OperateReturnMessage msg = new OperateReturnMessage();
        if (null != validateMsg) {
            msg.setFaildFlag(validateMsg);
            return JSONUtil.write(msg);
        }
        try {
            // 领活
            this.partsRdpRecordCardManager.startUpBatchJob(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }

	/**
     * TODO 待作废：当前系统设计，已无领活操作
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理 
     * <li>修改日期：2016-04-01
	 * 
	 * @param jsonObject {
            ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
			operatorId: 800109
        }
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String giveUpBatchJob(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 待领取的工单主键数组
        String ids = jo.getString(Constants.IDS);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 验证正在领取的工单的当前状态是否已经不是“带领取”
        String[] idArray = JSONUtil.read(ids, String[].class);
        String[] validateMsg = this.partsRdpRecordCardManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        
        OperateReturnMessage msg = new OperateReturnMessage();
        if (null != validateMsg) {
            msg.setFaildFlag(validateMsg);
            return JSONUtil.write(msg);
        }
        try {
            // 批量撤销领活
            this.partsRdpRecordCardManager.giveUpBatchJob(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
	}
	
	/**
	 * <li>说明：暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理 
     * <li>修改日期：2016-04-01
	 * 
	 * @param jsonObject {
    		entityJson : {
    			"idx":"025962890654441FAF66909501484778",
    			"workStartTime":"2015-01-05 11:38",
    			"workEndTime":"2015-01-05 12:00",
    			"workEmpName":"张三;李四",
    			"workEmpID":"101;102",
    			"remarks":"test",
    			"qcEmpID":"",
    			"qcEmpName":""
    		},
    		operatorId: 800109,
    		qcEmpJson: [{empName:'王谦', empId:109, qcItemNo:'HJ'}]
    	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String saveTemporary(String jsonObject) throws IOException {
        // 针对日期格式的特殊处理
        jsonObject = jsonObject.replaceAll(SPRIT_CHAR, SHORT_LINE_CHAR);
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 包含工单处理信息的JSON对象entityJson
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        
        // 指派的质量检查人员
        String qcEmpJson = jo.getString("qcEmpJson");
        PartsRdpQCParticipant.QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, PartsRdpQCParticipant.QCEmp[].class);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpRecordCard tempEntity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
        // 验证工单的当前状态是否为“待处理”
        String[] validateMsg = this.partsRdpRecordCardManager.validateStatus(new String[] { tempEntity.getIdx() }, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        
        OperateReturnMessage msg = new OperateReturnMessage();
        if (null != validateMsg) {
            msg.setFaildFlag(validateMsg);
            return JSONUtil.write(msg);
        }
        try {
            // 暂存
            this.partsRdpRecordCardManager.saveTemporary(tempEntity, qcEmps, null);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
	}

	/**
	 * TODO 待作废
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理 
     * <li>修改日期：2016-04-01
	 * 
	 * @param jsonObject {
            entityJson: {
    			"idx":"025962890654441FAF66909501484778",
    			"workStartTime":"2015-01-05 11:38",
    			"workEndTime":"2015-01-05 12:00",
    			"workEmpName":"张三;李四",
    			"workEmpID":"101;102",
    			"qcEmpName":"白宝强",
    			"qcEmpID":"35",
    			"remarks":"test"
    		},
    		operatorId: 800109,
    		qcEmpJson: [{empName:'王谦', empId:109, qcItemNo:'HJ'}]
        }
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String finishJob(String jsonObject) throws IOException {
		// 针对日期格式的特殊处理
		jsonObject = jsonObject.replaceAll(SPRIT_CHAR, SHORT_LINE_CHAR);
		JSONObject jo = JSONObject.parseObject(jsonObject);
        
		// 包含工单处理信息的JSON对象entityJson
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		// 指派的质量检查人员
		String qcEmpJson = jo.getString("qcEmpJson");
		PartsRdpQCParticipant.QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, PartsRdpQCParticipant.QCEmp[].class);
		
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		PartsRdpRecordCard tempEntity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
		String[] validateMsg = this.partsRdpRecordCardManager.validateStatus(new String[]{tempEntity.getIdx()}, IPartsRdpStatus.CONST_STR_STATUS_DCL);

        OperateReturnMessage msg = new OperateReturnMessage();
		if (null != validateMsg) {
			msg.setFaildFlag(validateMsg);
            return JSONUtil.write(msg);
		}
        try {
			// 销活
			this.partsRdpRecordCardManager.completeJob(tempEntity, qcEmps);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONUtil.write(msg);
	}
    
    /**
     * TODO 待作废
     * <li>说明：修改已处理的检修记录单实体
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理 
     * <li>修改日期：2016-04-01
     * @param jsonObject {
        entityJson : {
            "idx":"025962890654441FAF66909501484778",
            "workStartTime":"2015-01-05 11:38",
            "workEndTime":"2015-01-05 12:00",
            "workEmpName":"张三;李四",
            "workEmpID":"101;102"
            "remarks":"test"
        },
        operatorId: 800109
        }
     * @return 操作成功与否
     * @throws IOException
     */
    public String updateJob(String jsonObject) throws IOException {
        // 针对日期格式的特殊处理
        jsonObject = jsonObject.replaceAll(SPRIT_CHAR, SHORT_LINE_CHAR);
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 包含工单处理信息的JSON对象entityJson
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpRecordCard tempEntity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);

        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpRecordCardManager.updateJob(tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * TODO 待作废
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理 
     * <li>修改日期：2016-04-01
	 * 
	 * @param jsonObject {
		ids： ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
		entityJson：{
			"workEmpName":"张三;李四",
			"workEmpID":"101;102",
			"qcEmpName":"白宝强",
			"qcEmpID":"35"
		},
		operatorId: 800109,
		qcEmpJson: [{empName:'王谦', empId:109, qcItemNo:'HJ'}]
	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String finishBatchJob (String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		// 待销活的工单主键数组
		String ids = jo.getString(Constants.IDS);
		
		// 包含工单处理信息的JSON对象entityJson
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		// 指派的质量检查人员
		String qcEmpJson = jo.getString("qcEmpJson");
		PartsRdpQCParticipant.QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, PartsRdpQCParticipant.QCEmp[].class);
		
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		String[] idArray = JSONUtil.read(ids, String[].class);
		PartsRdpRecordCard tempEntity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
		
		String[] validateMsg = this.partsRdpRecordCardManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DCL);

        OperateReturnMessage msg = new OperateReturnMessage();
		if (null != validateMsg) {
			msg.setFaildFlag(validateMsg);
            return JSONUtil.write(msg);
		}
        try {
			// 批量销活
			this.partsRdpRecordCardManager.finishBatchJob(idArray, tempEntity, qcEmps);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONUtil.write(msg);
	}

	/**
	 * TODO 待作废
	 * <li>说明：检修检测项分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * <li>修改日期：2016-04-01
	 * @param jsonObject {
            entityJson: {
				rdpRecordCardIDX:"D4379430826048ABB688F2FC6CDC7B7B",
				status:"01"
			},
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String pageListForRI(String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
        
		// 获取查询条件实体对象
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		PartsRdpRecordRI entity = JSONUtil.read(entityJson, PartsRdpRecordRI.class);
		// 查询记录开始索引
		int start = jo.getIntValue(Constants.START);
		// 查询记录条数
		int limit = jo.getIntValue("limit");
		start = limit * (start - 1);
		// 排序字段
		JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
		Order[] orders = JSONTools.getOrders(jArray);
		SearchEntity<PartsRdpRecordRI> searchEntity = new SearchEntity<PartsRdpRecordRI>(entity, start, limit, orders);
        
		Page<PartsRdpRecordRI> page = partsRdpRecordRIManager.findPageList(searchEntity);
	    return JSONTools.toJSONList(page);
	}

	/**
	 * TODO 待作废
	 * <li>说明：检测项查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * <li>修改日期：2016-04-01
	 * @param jsonObject {
			rdpRecordRIIDX:"B720ED3E27BD4104B10919E9BCF9C675"
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String findListForDI(String jsonObject) throws IOException {
	    JSONObject jo = JSONObject.parseObject(jsonObject);
	    // 检修检测项实例主键
	    String rdpRecordRIIDX = jo.getString("rdpRecordRIIDX");
	    List list = this.partsRdpRecordDIManager.getModelByRdpRecordRIIDX(rdpRecordRIIDX);
	    return JSONTools.toJSONList(list.size(), list);
	}

	/**
	 * TODO 待作废
	 * <li>说明：检修检测项处理，包括“提交”和“暂存
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理
     * <li>修改日期：2016-04-01
	 * @param jsonObject {
			partsRdpRecordRI:{
				idx:"E1AC1965F5F647C3A98F1C261366F5F5",
				status:"01",
				repairResult:"合格",
				remarks:"备注"
			},
			partsRdpRecordDIs:[{
				idx:"9069B8786F7842B5BC9DC6E0023214CE",
				dataItemResult:"合格"
			}],
			isTemporary:true,			// 如果是暂存则设置为true,否则设置为false
      		empId:109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String savePartsRdpRecordRI (String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		// 配件检修检测项实例
		PartsRdpRecordRI partsRdpRecordRI = JSONUtil.read(jo.getString("partsRdpRecordRI"), PartsRdpRecordRI.class);
		
		// 配件检测项对象数组
		PartsRdpRecordDI[] partsRdpRecordDIs = JSONUtil.read(jo.getString("partsRdpRecordDIs"), PartsRdpRecordDI[].class);
		
		// 是否是暂存
		boolean isTemporary = Boolean.parseBoolean(jo.getString("isTemporary"));
		
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);

        OperateReturnMessage msg = new OperateReturnMessage();
        try {
			this.partsRdpRecordRIManager.saveTemporary(partsRdpRecordRI, partsRdpRecordDIs, isTemporary);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONUtil.write(msg);
	}
	
	/**
	 * <li>说明：获取记录工单指派的质量检查人员
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-20
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * <li>修改日期：2016-04-01
	 * @param jsonObject {
	 		rdpRecordCardIDX:"FE0E9308C7C24B9E9D69ADF088C52F77"
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String getQCEmpsForAssign(String jsonObject) throws IOException {
	    JSONObject jo = JSONObject.parseObject(jsonObject);
	    
	    // 记录工单主键
	    String rdpRecordCardIDX = jo.getString("rdpRecordCardIDX");
	    
	    List<QCEmp> list = this.partsRdpQCParticipantManager.getQCEmpsForAssign(rdpRecordCardIDX);
	    return JSONTools.toJSONList(list);
	}
    
    /**
     * TODO 待作废
     * <li>说明：获取检修检测结果的数据字典项
     * <li>创建人：何涛
     * <li>创建日期：2015-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return JSON数组 ["合格", "良好"]
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String getDicForRIRepairResult() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("from EosDictEntry where status = 1");
        sb.append("and id.dicttypeid='").append("PJJX_RECORD_RI_RESULT").append("'");
        List<EosDictEntry> list = this.partsRdpQCParticipantManager.getDaoUtils().find(sb.toString());
        List<String> tempList = new ArrayList<String>(list.size());
        for (EosDictEntry entry : list) {
            tempList.add(entry.getDictname());
        }
        return JSONUtil.write(tempList);
    }    

    /**
     * <li>说明：获取配件检修检测项及数据项列表
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理（测试通过）
     * <li>修改日期：2016-04-01
     * @param jsonObject {
            rdpRecordCardIDX:"FE0E9308C7C24B9E9D69ADF088C52F77"
        }
     * @return 配件检修检测项及数据项列表
     * @throws IOException
     */
    public String getListForRIAndDI(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String rdpRecordCardIDX = jo.getString("rdpRecordCardIDX");
        List<PartsRdpRecordRIAndDI> list = null;
        try {
            list = partsRdpRecordRIManager.getListForRIAndDI(rdpRecordCardIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
        return JSONTools.toJSONList(list);
	}
    
    /**
     * <li>说明：暂存或者销活配件检修记录工单
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理
     * <li>修改日期：2016-04-01
     * @param jsonObject {
            entityJson: {
                "idx":"025962890654441FAF66909501484778",
                "workStartTime":"2015-01-05 11:38",
                "workEndTime":"2015-01-05 12:00",
                "workEmpName":"张三;李四",
                "workEmpID":"101;102",
                "remarks":"test"
            },
            operatorId: 800109,
            qcEmpJson: [{empName: "王谦", empId:109, qcItemNo: "HJ"}],
            riAndDi:[{
                idx:"E1AC1965F5F647C3A98F1C261366F5F5",
                status:"01",
                repairResult:"合格",
                remarks:"备注",           
                diList:[{
                    idx:"9069B8786F7842B5BC9DC6E0023214CE",
                    dataItemResult:"合格"
                }]
            }],
            isTemporary:true
        }
     * @return 操作成功与否
     * @throws IOException
     */
    @SuppressWarnings("static-access")
    public String batchCompleteJob(String jsonObject) throws IOException {
		// 针对日期格式的特殊处理
		jsonObject = jsonObject.replaceAll(SPRIT_CHAR, SHORT_LINE_CHAR);
		JSONObject jo = JSONObject.parseObject(jsonObject);
        
		// 包含工单处理信息的JSON对象entityJson
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		// 指派的质量检查人员
		String qcEmpJson = jo.getString("qcEmpJson");
		PartsRdpQCParticipant.QCEmp[] qcEmps = JSONUtil.read(qcEmpJson, PartsRdpQCParticipant.QCEmp[].class);
		
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		PartsRdpRecordCard tempEntity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
		String riAndDi = jo.getString("riAndDi");
		PartsRdpRecordRIAndDI[] riAndDis = JSONUtil.read(riAndDi, PartsRdpRecordRIAndDI[].class);
		boolean isTemporary = Boolean.parseBoolean(jo.getString("isTemporary"));
        
		String[] validateMsg = this.partsRdpRecordCardManager.validateStatus(new String[]{tempEntity.getIdx()}, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        
		OperateReturnMessage msg = new OperateReturnMessage();
		if (null != validateMsg) {
			msg.setFaildFlag(validateMsg);
            return JSONUtil.write(msg);
        }
        try {
            if (isTemporary) {
                // 暂存
                this.partsRdpRecordCardManager.saveTemporary(tempEntity, qcEmps, riAndDis);
            } else {
                // 销活
                this.partsRdpRecordCardManager.completeJob(tempEntity, qcEmps, riAndDis);
            }
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONUtil.write(msg);
	}
    
    /**
     * <li>说明：修改已处理的配件记录单
     * <li>创建人：程锐
     * <li>创建日期：2016-2-1
     * <li>修改人: 何涛
     * <li>修改内容：修改不规范的异常处理
     * <li>修改日期：2016-04-01
     * @param jsonObject {
            entityJson: {
                "idx":"025962890654441FAF66909501484778",
                "workStartTime":"2015-01-05 11:38",
                "workEndTime":"2015-01-05 12:00",
                "workEmpName":"张三;李四",
                "workEmpID":"101;102",
                "remarks":"test"
            },
            operatorId: 800109,
            riAndDi:[{
                idx:"E1AC1965F5F647C3A98F1C261366F5F5",
                status:"01",
                repairResult:"合格",
                remarks:"备注",           
                diList:[{
                    idx:"9069B8786F7842B5BC9DC6E0023214CE",
                    dataItemResult:"合格"
                 }]
            }]
        }
     * @return 操作成功与否
     * @throws IOException
     */
    public String batchUpdateJob(String jsonObject) throws IOException {
		// 针对日期格式的特殊处理
		jsonObject = jsonObject.replaceAll(SPRIT_CHAR, SHORT_LINE_CHAR);
		JSONObject jo = JSONObject.parseObject(jsonObject);
        
		// 包含工单处理信息的JSON对象entityJson
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		PartsRdpRecordCard tempEntity = JSONUtil.read(entityJson, PartsRdpRecordCard.class);
		String riAndDi = jo.getString("riAndDi");
		PartsRdpRecordRIAndDI[] riAndDis = JSONUtil.read(riAndDi, PartsRdpRecordRIAndDI[].class);

        OperateReturnMessage msg = new OperateReturnMessage();
        try {
			this.partsRdpRecordCardManager.updateJob(tempEntity, riAndDis);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
			msg.setFaildFlag(e.getMessage());
		}
		return JSONUtil.write(msg);
	}

    /**
     * <li>说明：调用接口方法同步可视化获取到的数据，并更新对应的检测项结果值
     * <li>创建人：林欢
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 传入参数
     * {
     *  partsID:"9999",//配件二维码
     *  startTime:"2015-01-05 12:00 999",//配件检修实际开始时间，精确到毫秒
     *  endTime:"2015-01-05 12:00 999",//配件检修实际结束时间，精确到毫秒
     *  rdpRecordCardRdpIDX:"2323asdfsf"//获取配件检修单idx
     * }
     * @return 操作成功与否
     * @throws IOException
     */
    public String synPartsCheckItemDataAndSavePartsRdpRecordDI(String jsonObject) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        OperateReturnMessage msg = new OperateReturnMessage();
        
//      其他参数map
        Map<String,String> paramMap = new HashMap<String, String>();
        //postMap
        Map<String,Object> postMap = new HashMap<String, Object>();
        
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 包含工单处理信息的JSON对象entityJson
        
        String rdpRecordCardRdpIDX = jo.getString("rdpRecordCardRdpIDX");//获取配件检修单idx
        paramMap.put("rdpRecordCardRdpIDX", rdpRecordCardRdpIDX);
        
//      通过检修计划idx查询检修对象
        PartsRdp partsRdp = partsRdpManager.getModelById(rdpRecordCardRdpIDX);
        
        String nowTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(new Date());
        postMap.put("endTime",nowTime);
        postMap.put("partID",partsRdp.getIdentificationCode());
        
        //开始时间由于可视化需要，时间-8小时
        BigDecimal startTimeBigDecimal = new BigDecimal(partsRdp.getRealStartTime().getTime()).subtract(new BigDecimal(8*60*60*1000));
        String startTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(new Date(startTimeBigDecimal.longValue()));
        
        postMap.put("startTime",startTime);
        
        try {
            map = this.partsCheckItemDataManager.synPartsCheckItemDataAndSavePartsRdpRecordDI(url, postMap, paramMap, map);
            if ("false".equals(map.get(Constants.SUCCESS).toString())) {
                msg.setFaildFlag(map.get(Constants.ERRMSG).toString());
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
}
