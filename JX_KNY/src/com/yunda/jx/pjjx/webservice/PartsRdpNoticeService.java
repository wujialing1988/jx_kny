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
import com.yunda.Application;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.pjjx.partsrdp.IPartsRdpStatus;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.entity.PartsRdpNotice;
import com.yunda.jx.pjjx.partsrdp.rdpnotice.manager.PartsRdpNoticeManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.webservice.stationTerminal.base.entity.EosDictEntryBean;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修提票工单接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpNoticeService?wsdl
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-14 上午10:53:48
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partsRdpNoticeWS")
public class PartsRdpNoticeService implements IPartsRdpNoticeService {
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());
	
	/** PartsRdpNotice业务类,提票单 */
	@Resource
	private PartsRdpNoticeManager partsRdpNoticeManager;

	/**
	 * <li>说明：施修工单分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人: 何涛
	 * <li>修改日期：2016-04-11
	 * <li>修改内容：修改不规范的异常处理方式（测试通过）
	 * @param jsonObject {
			entityJson: {
				rdpIDX:"8a8283a85114b8ee0151187a80cf0003",
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
		PartsRdpNotice entity = JSONUtil.read(entityJson, PartsRdpNotice.class);
		
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
		Condition con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
		whereList.add(con);
		if(StringUtil.isNullOrBlank(entity.getStatus()) == false){
			con = new Condition("status", Condition.LLIKE, entity.getStatus());
			whereList.add(con);
		}
		// 当前登录用户只能处理自己领取的工单
		/*if (IPartsRdpStatus.CONST_STR_STATUS_DCL.equals(entity.getStatus())) {
			con = new Condition("handleEmpID", Condition.EQ, SystemContext.getOmEmployee().getEmpid());
			con.setStringLike(false);
			whereList.add(con);
		}*/
		
		QueryCriteria<PartsRdpNotice> query = new QueryCriteria<PartsRdpNotice>(PartsRdpNotice.class, whereList, orderList, start, limit);
		Page<PartsRdpNotice> page = this.partsRdpNoticeManager.findPageList(query);
		
		return JSONTools.toJSONList(page);
	}
	
	/**
     * TODO 待作废
	 * <li>说明：领活
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式
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
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 验证正在领取的工单的当前状态是否已经不是“待领取”
        String validateMsg = this.partsRdpNoticeManager.validateStatus(idx, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 领活
            this.partsRdpNoticeManager.startUpJob(idx);
        } catch (Exception e) {
            msg.setFaildFlag(e.getMessage());
            ExceptionUtil.process(e, logger);
        }
        return JSONUtil.write(msg);
    }
	
	/**
     * TODO 待作废
     * <li>说明：批量领活
     * <li>创建人：何涛
     * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式
     * @param jsonObject { ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "], operatorId: 800109 }
     * @return
     *            <li>"{'flag':'true','message':'操作成功！'}";
     *            <li>"{'flag':'false','message':'操作失败！'}"
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
        String[] validateMsg = this.partsRdpNoticeManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DLQ);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 领活
            this.partsRdpNoticeManager.startUpBatchJob(idArray);
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
     * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式
     * @param jsonObject { ids: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "], operatorId: 800109 }
     * @return
     *            <li>"{'flag':'true','message':'操作成功！'}";
     *            <li>"{'flag':'false','message':'操作失败！'}"
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
        String[] validateMsg = this.partsRdpNoticeManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 批量撤销领活
            this.partsRdpNoticeManager.giveUpBatchJob(idArray);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
     * <li>说明：暂存
     * <li>创建人：何涛
     * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式（测试通过）
     * @param jsonObject { entityJson: { "idx":"8a8283a85114b8ee015118a2e597000d", "solution":"test by hetao", "workStartTime":"2015-01-05 11:38",
     *            "workEndTime":"2015-01-05 12:00", "workEmpName":"张三;李四", "workEmpID":"101;102" }, operatorId: 800109 }
     * @return
     *            <li>"{'flag':'true','message':'操作成功！'}";
     *            <li>"{'flag':'false','message':'操作失败！'}"
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
        PartsRdpNotice tempEntity = JSONUtil.read(entityJson, PartsRdpNotice.class);
        
        // 验证工单的当前状态是否为“待处理”
        String[] validateMsg = this.partsRdpNoticeManager.validateStatus(new String[] { tempEntity.getIdx() }, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 暂存
            this.partsRdpNoticeManager.saveTemporary(tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
     * <li>说明：销活
     * <li>创建人：何涛
     * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式（测试通过）
     * @param jsonObject { entityJson: { "idx":"8a8283a85114b8ee015118a2e597000d", "solution":"test by hetao", "workStartTime":"2015-01-05 11:38",
     *            "workEndTime":"2015-01-05 12:00", "workEmpName":"张三;李四", "workEmpID":"101;102" }, operatorId: 800109 }
     * @return
     *            <li>"{'flag':'true','message':'操作成功！'}";
     *            <li>"{'flag':'false','message':'操作失败！'}"
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
        
        PartsRdpNotice tempEntity = JSONUtil.read(entityJson, PartsRdpNotice.class);
        
        // 验证工单的当前状态是否为“待处理”
        String[] validateMsg = this.partsRdpNoticeManager.validateStatus(new String[] { tempEntity.getIdx() }, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 销活
            this.partsRdpNoticeManager.finishJob(tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
     * <li>说明：批量销活
     * <li>创建人：何涛
     * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式
     * @param jsonObject { ids： ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023 "], entityJson：{ "workEmpName":"张三;李四",
     *            "workEmpID":"101;102" }, operatorId: 800109 }
     * @return
     *            <li>"{'flag':'true','message':'操作成功！'}";
     *            <li>"{'flag':'false','message':'操作失败！'}"
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
        
        PartsRdpNotice tempEntity = JSONUtil.read(entityJson, PartsRdpNotice.class);
        
        String[] validateMsg = this.partsRdpNoticeManager.validateStatus(idArray, IPartsRdpStatus.CONST_STR_STATUS_DCL);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 批量销活
            this.partsRdpNoticeManager.finishBatchJob(idArray, tempEntity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：提票
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理方式（测试通过）
	 * 
	 * @param jsonObject {
			entityJson:{
				rdpIDX:"8a8283a85114b8ee0151187a80cf0003",
				noticeNo: "TPD20150114028",
				noticeDesc: "施修过程处理不规范，请重新检修",
			},
			operatorId: 800109
		}
	 * @return 
	 * <li>"{'flag':'true','message':'操作成功！'}";
	 * <li>"{'flag':'false','message':'操作失败！'}"
	 * @throws IOException
	 */
	public String submitNotice(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 仅封装了“作业主键”和“作业节点主键”字段值的提票单对象数组
        PartsRdpNotice entity = JSONUtil.read(jo.getString(Constants.ENTITY_JSON), PartsRdpNotice.class);
        
        String[] validateMsg = this.partsRdpNoticeManager.validateUpdate(entity);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        
        // 提票
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpNoticeManager.submitNotice(entity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：获取提票类型
     * <li>创建人：张迪
     * <li>创建日期：2016-8-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票类型列表
     */
    public String getNoticeTypes() {
        try {
            List<EosDictEntry> list = getEosDictEntrySelectManager().findByDicTypeID("PJJX_Parts_Rdp_Notice_type");
            List<EosDictEntryBean> beanlist = new ArrayList<EosDictEntryBean>();
            for (EosDictEntry eos : list) {
                EosDictEntryBean bean = new EosDictEntryBean();
                bean.setDictid(eos.getId().getDictid());
                bean.setDictname(eos.getDictname());
                beanlist.add(bean);
            }
            return JSONUtil.write(beanlist);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    protected EosDictEntrySelectManager getEosDictEntrySelectManager() {
        return (EosDictEntrySelectManager) Application.getSpringApplicationContext().getBean("eosDictEntrySelectManager");
    }
}
