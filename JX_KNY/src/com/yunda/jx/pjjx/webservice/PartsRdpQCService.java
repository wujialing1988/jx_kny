package com.yunda.jx.pjjx.webservice;

import java.io.IOException;
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
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsQRBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpRecordZljcBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpZljyBean;
import com.yunda.jx.pjjx.partsrdp.qcinst.entity.PartsRdpQR.PartsRdpSearcher;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRManager;
import com.yunda.jx.pjjx.partsrdp.qcinst.manager.PartsRdpQRQueryManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修质量检验接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpQCService?wsdl
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-14 下午02:36:34
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partsRdpQCWS")
public class PartsRdpQCService implements IPartsRdpQCService {
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());
	
	/** PartsRdp业务类,配件检修作业 */
	@Resource
	private PartsRdpManager partsRdpManager;
	
	/** PartsRdpQR业务类,质量检查结果 */
	@Resource
	private PartsRdpQRManager partsRdpQRManager;
    
    /** PartsRdpQuery业务类,配件检修作业查询业务类 */
    @Resource
    private PartsRdpQueryManager partsRdpQueryManager;
    
    /** PartsRdpQRQueryManager业务类,配件质检查询业务类 */
    @Resource
    private PartsRdpQRQueryManager partsRdpQRQueryManager;
    
    @Resource
    private  PartsRdpRecordCardManager partsRdpRecordCardManager;
    
	/**
	 * <li>说明：待检验工单汇总分页查询
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人: 程锐
	 * <li>修改日期： 2015-10-10
	 * <li>修改内容：增加配件识别码
     * <li>修改人: 何涛
     * <li>修改日期：2016-03-21
     * <li>修改内容：1、增加查询字段：下车车型（unloadTrainType），组合匹配下车车型、下车车号，
     *             2、修改查询字段：名称规格型号（specificationModel），组合匹配配件规格型号、配件名称
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
	 * @param jsonObject {
			entityJson: {
				unloadTrainTypeIdx:"10026",
                unloadTrainType: "HXD10002",
				unloadTrainNo: "256",
				unloadRepairClassIdx: "8a8284f24ab80704014ab891375a0004",
				partsTypeIDX: "8a8284f24ab80704014ab891375a0004",
				specificationModel: "TGZS500.221.000\\SS4",
				partsNo: "25202",
				partsName: "转向架",
                identificationCode："25202"
			},
			start:0,
			limit:50,
			checkWay: "2",
			qcContent: "GZJ,YS,ZJ",
			operatorId: 800109
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String findPartRdpQCItems(String jsonObject) throws IOException {
		JSONObject jo = JSONObject.parseObject(jsonObject);
		
		// 当前作业处理人员ID
		Long operatorId = jo.getLong(Constants.OPERATOR_ID);
		if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
		}
		// 设置系统用户信息
		SystemContextUtil.setSystemInfoByOperatorId(operatorId);
		
		// 检验方式“抽检[1]/必检[2]”
		String checkWay = jo.getString("checkWay");
		
		// 质量检验项，多个质量检查项则已d逗号“,”进行分隔
		String qcContent = jo.getString("qcContent");
		// 查询开始索引
		int start = jo.getIntValue(Constants.START);
		// 查询条数
		int limit = jo.getIntValue("limit");
		start = limit * (start - 1);
		// 排序字段
		JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
		Order[] orders = JSONTools.getOrders(jArray);
		
		PartsRdp entity = JSONUtil.read(jo.getString(Constants.ENTITY_JSON), PartsRdp.class);
		SearchEntity<PartsRdp> searchEntity = new SearchEntity<PartsRdp>(entity, start, limit, orders);
		
		Page<PartsRdpSearcher> page = this.partsRdpManager.findPartRdpQCItems(searchEntity, checkWay, qcContent);
		return JSONTools.toJSONList(page);
	}
    
    /**
     * <li>说明：待检验工单分页查询
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
     * @param jsonObject { 
        entityJson:{
            rdpIDX: "772D7520EF5945B695CDB79F2E6CC1E7"
        },
        start:0,
        limit:50,
        operatorId: "10902",
        checkWay: "1"
       }
     * @return 待检验工单分页列表
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String findPageListForQC(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        
        
        // 质量检查人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        //检验方式“抽检[1]/必检[2]”
        String checkWay = jo.getString("checkWay");
        
        // 
        PartsQRBean entity = JSONUtil.read(jo.getString(Constants.ENTITY_JSON), PartsQRBean.class);
        
        SearchEntity<PartsQRBean> searchEntity = new SearchEntity<PartsQRBean>(entity, start, limit, null);
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        
        try {
            Page<PartsQRBean> page = partsRdpQRQueryManager.getQCPageList(omEmployee.getEmpid(), start, limit, checkWay, searchEntity);
            return JSONTools.toJSONList(page);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
	
	/**
	 * <li>说明：签名提交
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
	 * <li>修改人: 程锐
	 * <li>修改日期： 2015-10-12
	 * <li>修改内容：前台参数传递修改为传递配件质检单的idx
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
	 * @param jsonObject {
			idxs: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023"],
			qrResult:"检验合格"
			operatorId: 800109
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String signAndSubmit(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        String[] idxs = JSONUtil.read(jo.getString("idxs"), String[].class);
        
        // 获取质量检查结果
        String qrResult = jo.getString("qrResult");
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 签名提交
            this.partsRdpQRManager.signAndSubmitByIDX(idxs, qrResult);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
	/**
	 * <li>说明：返修
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-14
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
	 * @param jsonObject {
			rdpRecordCardIDXs: ["8a8284f249abf9720149ac1f0f380005", "8a8284f2493af21101493b17bb1d0023"],
			operatorId: 800109
		}
	 * @return JSON列表（数组）
	 * @throws IOException
	 */
	public String updateToBack(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 获取检修记录工单主键数组
        String[] rdpRecordCardIDXs = JSONUtil.read(jo.getString("rdpRecordCardIDXs"), String[].class);
        String qcItemNo = jo.getString("qcItemNo");
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 返修
            this.partsRdpQRManager.updateToBack(rdpRecordCardIDXs, qcItemNo);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
	
    /**
     * TODO 待确定是否有用
     * <li>说明：根据配件识别码获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
     * @param jsonObject 
     * {
     *   identityCode 配件识别码
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByIdentity(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String identityCode = jo.getString("identificationCode");
        PartsRdp partsRdp = new PartsRdp();
        partsRdp.setIdentificationCode(identityCode);
        return JSONObject.toJSONString(partsRdpQueryManager.getRdpForQC(partsRdp));
    }
    
    /**
     * TODO 待确定是否有用
     * <li>说明：根据配件编号获取配件作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-10-10
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
     * @param jsonObject 
     * {
     *   partsNo 配件编号
     * }  
     * @return 配件作业计划
     * @throws IOException
     */
    public String getPartsRdpByPartsNo(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String partsNo = jo.getString("partsNo");
        PartsRdp partsRdp = new PartsRdp();
        partsRdp.setPartsNo(partsNo);
        return JSONObject.toJSONString(partsRdpQueryManager.getRdpForQC(partsRdp));
    }
    
    /**
     * <li>说明：全部签名提交
     * <li>创建人：程锐
     * <li>创建日期：2015-10-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
     * 
     * @param jsonObject {
            entityJson:{
                rdpIDX: "772D7520EF5945B695CDB79F2E6CC1E7"
            },
            qrResult:"检验合格"
            operatorId: 800109,
            checkWay: "1"
        }
     * @return 操作成功与否
     * @throws IOException
     */
    public String allSignAndSubmit (String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 检验方式“抽检[1]/必检[2]”
        String checkWay = jo.getString("checkWay");                        
        // 获取质量检查结果
        String qrResult = jo.getString("qrResult");
        
        PartsQRBean entity = JSONUtil.read(jo.getString(Constants.ENTITY_JSON), PartsQRBean.class);
        SearchEntity<PartsQRBean> searchEntity = new SearchEntity<PartsQRBean>(entity, null, null, null);

        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 签名提交
            this.partsRdpQRManager.allSignAndSubmit(checkWay, searchEntity, qrResult);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：根据记录单IDX获取质检人信息列表
     * <li>创建人：程锐
     * <li>创建日期：2015-12-2
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容: 修改不规范的异常处理
     * @param jsonObject {
            rdpRecordCardIDX: "1"
        }
     * @return 质检人信息列表 [{
            "qcItemName":"互检",
            "qcEmpNames":"张三,李四"
        }]
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String getQREmpInfoByRecordCardIDX(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String rdpRecordCardIDX = jo.getString("rdpRecordCardIDX");
        try {
            List<PartsQRBean> list = partsRdpQRManager.getQREmpInfoByRecordCardIDX(rdpRecordCardIDX);
            return JSONTools.toJSONList(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }

    /**
     * <li>说明：（新）待检验工单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String findRdpPageList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        
        String entityJson = jo.getString("entityJson");
        PartsRdp rdp = JSONUtil.read(StringUtil.nvl(entityJson, Constants.EMPTY_JSON_OBJECT), PartsRdp.class);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        Page<PartsRdpZljyBean> page = partsRdpQRQueryManager.findRdpPageList(new SearchEntity<PartsRdp>(rdp, start, limit, orders));
        return JSONTools.toJSONList(page);
    }

    /**
     * <li>说明：（新）待检验检修记录卡汇总查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryCardList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);        
        String  rdpRecordIDX = jo.getString("rdpRecordIDX");
        String partsRdpRecordCardList  = partsRdpRecordCardManager.integrateQueryCardList(rdpRecordIDX);
        return partsRdpRecordCardList;
    }

    /**
     * <li>说明：（新）待检验检修记录单汇总
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-8-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryRecordPageList(String jsonObject) throws IOException {
      JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        
        // 是否查询全部
        boolean isAll = jo.getBooleanValue("isAll");
        
        String entityJson = jo.getString("entityJson");
        PartsRdpRecord entity = JSONUtil.read(StringUtil.nvl(entityJson, Constants.EMPTY_JSON_OBJECT), PartsRdpRecord.class);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        SearchEntity<PartsRdpRecord> searchEntity = new SearchEntity<PartsRdpRecord>(entity, start, limit, orders);
        try {
            Page<PartsRdpRecordZljcBean>  recordList = partsRdpQRQueryManager.queryRecordPageList(searchEntity,isAll);
            return JSONTools.toJSONList(recordList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
}
