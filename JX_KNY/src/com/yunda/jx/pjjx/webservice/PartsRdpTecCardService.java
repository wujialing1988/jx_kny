package com.yunda.jx.pjjx.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardMat;
import com.yunda.jx.pjjx.base.tecdefine.manager.TecCardMatManager;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.manager.PartsRdpExpendMatManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecCard;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecCardManager;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecWSManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修工艺工单接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpTecCardService?wsdl
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-12 下午04:53:56
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partsRdpTecCardWS")
public class PartsRdpTecCardService implements IPartsRdpTecCardService {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());
	
	/** PartsRdpTecCard业务类,配件检修工艺工单 */
	@Resource
	private PartsRdpTecCardManager partsRdpTecCardManager;
	
	/** PartsRdpTecWS业务类,配件检修工序实例 */
	@Resource
	private PartsRdpTecWSManager partsRdpTecWSManager;
	
	/** TecCardMat业务类 */
	@Resource
	private TecCardMatManager tecCardMatManager;
	
	/** PartsRdpExpendMat业务类,物料消耗记录 */
	@Resource
	private PartsRdpExpendMatManager partsRdpExpendMatManager;
	
	/**
	 * <li>说明：施修工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
	 * <li>修改人: 何涛
	 * <li>修改日期：2016-04-11
	 * <li>修改内容：修改不规范的异常处理（测试通过）
	 * @param jsonObject {
			entityJson: {
				rdpIDX:"8a8284f25146a0d8015146a7670d0003",
                rdpNodeIDX:"3512EA13DFD342CEBD7484B17800F96B",
				status:"03"
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
	public String pageList(String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		// 获取查询条件实体对象
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		PartsRdpTecCard entity = JSONUtil.read(entityJson, PartsRdpTecCard.class);
		
		// 查询工艺开始索引
		int start = jo.getIntValue(Constants.START);
		
		// 查询工艺条数
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
        // 查询条件 - 配件检修作业主键
        if (!StringUtil.isNullOrBlank(entity.getRdpIDX())) {
            con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
            con.setStringLike(false);
            whereList.add(con);
        }
        // 查询条件 - 配件检修作业节点主键
        if(!StringUtil.isNullOrBlank(entity.getStatus())){ 
            con = new Condition("rdpNodeIDX", Condition.EQ, entity.getRdpNodeIDX());
            con.setStringLike(false);
            whereList.add(con);
        }
        // 查询条件 - 工单状态
		if(!StringUtil.isNullOrBlank(entity.getStatus())){
			con = new Condition("status", Condition.LLIKE, entity.getStatus());
			whereList.add(con);
		}
		// 当前登录用户只能处理自己领取的工单
		/*if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(entity.getStatus())) {
			con = new Condition("handleEmpID", Condition.EQ, SystemContext.getOmEmployee().getEmpid());
			con.setStringLike(false);
			whereList.add(con);
		}*/
		
		QueryCriteria<PartsRdpTecCard> query = new QueryCriteria<PartsRdpTecCard>(PartsRdpTecCard.class, whereList, orderList, start, limit);
		Page<PartsRdpTecCard> page = this.partsRdpTecCardManager.findPageList(query);
		List<PartsRdpTecCard> list = page.getList();
		if (null == list || list.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
		for (PartsRdpTecCard t : list) {
		    t.setStatus(PartsRdpRecordCardService.convertPartsRdpCardStatus(t.getStatus()));
		}
		return JSONTools.toJSONList(page.getTotal(), list);
	}
	
	/**
     * TODO 待作废
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
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
        
        OperateReturnMessage msg = new OperateReturnMessage();
        // 验证正在领取的工单的当前状态是否已经不是“待领取”
        String validateMsg = this.partsRdpTecCardManager.validateStatus(idx, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg));
        }
        try {
            // 领活
            this.partsRdpTecCardManager.startUpJob(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }

	/**
     * TODO 待作废
	 * <li>说明：批量领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-07
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
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
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        String[] idArray = JSONUtil.read(ids, String[].class);
        // 验证正在领取的工单的当前状态是否已经不是“带领取”
        String[] validateMsg = this.partsRdpTecCardManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 批量领活
            this.partsRdpTecCardManager.startUpBatchJob(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }

	/**
     * TODO 待作废
	 * <li>说明：批量撤销领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
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
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        String[] idArray = JSONUtil.read(ids, String[].class);
        // 验证正在领取的工单的当前状态是否已经不是“带领取”
        String[] validateMsg = this.partsRdpTecCardManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 批量撤销领活
            this.partsRdpTecCardManager.giveUpBatchJob(idArray);
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
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
			entityJson : {
				"idx":"025962890654441FAF66909501484778",
				"workStartTime":"2015-01-05 11:38",
				"workEndTime":"2015-01-05 12:00",
				"workEmpName":"张三;李四",
				"workEmpID":"101;102"
			},
			operatorId: 800109
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
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        PartsRdpTecCard tempEntity = JSONUtil.read(entityJson, PartsRdpTecCard.class);
        
        // 验证工单的当前状态是否为“待处理”
        String[] validateMsg = this.partsRdpTecCardManager.validateStatus(new String[] { tempEntity.getIdx() }, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 暂存
            this.partsRdpTecCardManager.saveTemporary(tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
			entityJson : {
				"idx":"025962890654441FAF66909501484778",
				"workStartTime":"2015-01-05 11:38",
				"workEndTime":"2015-01-05 12:00",
				"workEmpName":"张三;李四",
				"workEmpID":"101;102"
			},
			operatorId: 800109
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
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpTecCard tempEntity = JSONUtil.read(entityJson, PartsRdpTecCard.class);
        
        // String[] validateMsg = this.partsRdpTecCardManager.validateStatus(new String[]{tempEntity.getIdx()}, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        // if (null != validateMsg) {
        // return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        // } else {
        // // 销活
        // this.partsRdpTecCardManager.finishJob(tempEntity);
        // }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpTecCardManager.finishJob(tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：批量销活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
		ids： ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
		entityJson：{
			"workEmpName":"张三;李四",
			"workEmpID":"101;102"
		},
		operatorId: 800109
	}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String finishBatchJob(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 待销活的工单主键数组
        String ids = jo.getString(Constants.IDS);
        // 包含工单处理信息的JSON对象entityJson
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        String[] idArray = JSONUtil.read(ids, String[].class);
        
        PartsRdpTecCard tempEntity = JSONUtil.read(entityJson, PartsRdpTecCard.class);
        
        String[] validateMsg = this.partsRdpTecCardManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 批量销活
            this.partsRdpTecCardManager.finishBatchJob(idArray, tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：工艺工序分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param searchEnityJson {
			entityJson: {
				rdpTecCardIDX:"7C9131712E214B59B6F13A2E4271773F",
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
	public String pageListForWS(String searchEnityJson) throws IOException {
		JSONObject jo = JSONObject.parseObject(searchEnityJson);
		// 获取查询条件实体对象
		String entityJson = jo.getString(Constants.ENTITY_JSON);
		PartsRdpTecWS entity = JSONUtil.read(entityJson, PartsRdpTecWS.class);
		// 查询记录开始索引
		int start = jo.getIntValue(Constants.START);
		// 查询记录条数
		int limit = jo.getIntValue("limit");
		start = limit * (start - 1);
		// 排序字段
		JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
		Order[] orders = JSONTools.getOrders(jArray);
		SearchEntity<PartsRdpTecWS> searchEntity = new SearchEntity<PartsRdpTecWS>(entity, start, limit, orders);
		Page<PartsRdpTecWS> page = partsRdpTecWSManager.findPageList(searchEntity);
		return JSONTools.toJSONList(page);
	}

	/**
     * TODO 待作废
	 * <li>说明：配件检修工艺工序批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
			ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String finishBatchWS(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 待领取的工单主键数组
        String ids = jo.getString(Constants.IDS);
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        String[] idArray = JSONUtil.read(ids, String[].class);
        // 验证正在处理的工艺工序当前状态是否已经不是“未处理”
        String[] validateMsg = this.partsRdpTecWSManager.validateStatus(idArray, PartsRdpTecWS.CONST_STR_STATUS_WCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 领活
            this.partsRdpTecWSManager.finishBatchWS(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
     * TODO 待作废
	 * <li>说明：所需物料分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param searchEnityJson {
			entityJson: {
				tecCardIDX:" 8a8284f249a2054a0149a2074ed50002",
				matCode:"",
				matDesc:""
			},
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String pageListForMat(String searchEnityJson) throws IOException {
        JSONObject jo = JSONObject.parseObject(searchEnityJson);
        // 获取查询条件实体对象
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        TecCardMat entity = JSONUtil.read(entityJson, TecCardMat.class);
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        SearchEntity<TecCardMat> searchEntity = new SearchEntity<TecCardMat>(entity, start, limit, orders);
        Page<TecCardMat> page = this.tecCardMatManager.queryPageList(searchEntity);
        return JSONTools.toJSONList(page);
    }
	
	/**
	 * <li>说明：新增物料消耗
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
		 	partsRdpExpendMats: [{ 
				rdpIDX:"8a8284f24ae27846014ae27ca8340002",
				rdpTecCardIDX:"9E2273FF52EB49B790269A14017F7573",
				matCode:"20003",
				matDesc:"20003",
				qty:12,
				unit:"20003",
				price:999.9
			}],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String saveExpendMats(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpExpendMat[] partsRdpExpendMats = JSONUtil.read(jo.getString("partsRdpExpendMats"), PartsRdpExpendMat[].class);
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpExpendMatManager.saveExpendMats(partsRdpExpendMats);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：更新物料消耗数量，不能更新其他人员的物料消耗
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
		 	entityJson: [{ 
				idx:"8a8284f24ae61e67014ae6206b7d0001",
				qty:13
			}],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String updateExpendMat(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpExpendMat tempEntity = JSONUtil.read(jo.getString(Constants.ENTITY_JSON), PartsRdpExpendMat.class);
        
        PartsRdpExpendMat entity = this.partsRdpExpendMatManager.getModelById(tempEntity.getIdx());
        entity.setQty(tempEntity.getQty());
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp != null) {
            entity.setHandleEmpId(emp.getEmpid());
            entity.setHandleEmpName(emp.getEmpname());
        }
        OmOrganization org = SystemContext.getOmOrganization();
        if (org != null) {
            entity.setHandleOrgId(org.getOrgid());
            entity.setHandleOrgName(org.getOrgname());
            entity.setHandleOrgSeq(org.getOrgseq());
        }
        
        /*
         * String[] validateMsg = this.partsRdpExpendMatManager.validateUpdate(entity); if (null != validateMsg) { return
         * JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0])); } else {
         */
        // }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpExpendMatManager.saveOrUpdate(entity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：删除物料消耗
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-22
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param jsonObject {
		 	ids: [
		 		"8a8284f24b1054cf014b1058bc2a0002",
		 		"8a8284f24b1054cf014b1059764c0009"
	 		],
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String deleteExpendMats(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 删除的物料消耗主键数组
        String ids = jo.getString(Constants.IDS);
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        String[] idArray = JSONUtil.read(ids, String[].class);
        
        String[] validateMsg = this.partsRdpExpendMatManager.validateDelete(idArray);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpExpendMatManager.logicDelete(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：消耗物料分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-21
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param searchEnityJson {
			entityJson: {
				rdpTecCardIDX:"3E15424C5A0943BA9E195108949A188C"
			},
			start:1,
			limit:50, 
			orders:[{
				sort: "idx",
				dir: "ASC"
			}]
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 */
	public String pageListForExpendMat(String searchEnityJson) throws IOException {
        JSONObject jo = JSONObject.parseObject(searchEnityJson);
        // 获取查询条件实体对象
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        PartsRdpExpendMat entity = JSONUtil.read(entityJson, PartsRdpExpendMat.class);
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        // 默认按物料编码排序
        if (orders == null || orders.length < 1) {
            orders = new Order[1];
            orders[0] = Order.asc("matCode");
        }
        
        SearchEntity<PartsRdpExpendMat> searchEntity = new SearchEntity<PartsRdpExpendMat>(entity, start, limit, orders);
        Page<PartsRdpExpendMat> page = this.partsRdpExpendMatManager.findPageList(searchEntity);
        return JSONTools.toJSONList(page);
    }
	
    /**
     * <li>说明：修改已处理的检修工艺工单
     * <li>创建人：程锐
     * <li>创建日期：2015-11-11
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
     * @param jsonObject {
            entityJson : {
                "idx":"025962890654441FAF66909501484778",
                "workStartTime":"2015-01-05 11:38",
                "workEndTime":"2015-01-05 12:00",
                "workEmpName":"张三;李四",
                "workEmpID":"101;102"
            },
            operatorId: 800109
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
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
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpTecCard tempEntity = JSONUtil.read(entityJson, PartsRdpTecCard.class);
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpTecCardManager.updateJob(tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
}
