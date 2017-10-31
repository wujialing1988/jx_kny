package com.yunda.jx.pjjx.webservice;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpNodeMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.entity.PartsRdpExpendMat;
import com.yunda.jx.pjjx.partsrdp.expendmat.manager.PartsRdpExpendMatManager;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpNodeMatManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeList;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：配件检修物料消耗接口
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpNodeMatService?wsdl
 * <li>创建人： 张迪
 * <li>创建日期： 2016-9-14 
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partsRdpNodeMatWS")
public class PartsRdpNodeMatService implements IPartsRdpNodeMatService{

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
	/** PartsRdpExpendMat业务类,物料消耗记录 */
	@Resource
	private PartsRdpExpendMatManager partsRdpExpendMatManager;
    @Resource
    private PartsRdpNodeMatManager partsRdpNodeMatManager;
    @Resource
    private MatTypeListManager matTypeListManager;
	
	/**
	 * <li>说明：所需物料查询列表
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
     * <li>修改人: 何涛
     * <li>修改日期：2016-04-11
     * <li>修改内容：修改不规范的异常处理
	 * @param searchEnityJson 查询参数
	 * @return 所需物料查询列表
	 */
    public String findListForMat(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        PartsRdpNodeMat entity = JSONUtil.read(entityJson, PartsRdpNodeMat.class);      
        List<PartsRdpNodeMat> list = this.partsRdpNodeMatManager.findListForMat(entity);
        return JSONTools.toJSONList(list);
    }
	/**
	 * <li>说明：新增保存物料消耗
	 * <li>创建人：张迪
	 * <li>创建日期：2016-09-14
     * <li>修改人: 
     * <li>修改日期：1
     * <li>修改内容：
	 * @param jsonObject json对像
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
     * <li>说明：查询基础物料信息列表(用于自定义新增物料消耗记录)
     * <li>创建人：张迪
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson 查询参数
     * @return 物料信息列表
     * @throws IOException
     */
    public String pageListForMatList(String searchEnityJson) throws IOException {
        JSONObject jo = JSONObject.parseObject(searchEnityJson);
        // 获取查询条件实体对象
        String entityJson = jo.getString(Constants.ENTITY_JSON);
        MatTypeList entity = JSONUtil.read(entityJson, MatTypeList.class);
        // 节点
        String rdpNodeIDX = jo.getString("rdpNodeIDX");
        if(null != rdpNodeIDX && !"".equals(rdpNodeIDX)){
            String rdpNodeStr = " select matCode from PartsRdpNodeMat where rdpNodeIDX = '"+ rdpNodeIDX + "' ";
            entity.setRdpNodeStr(rdpNodeStr);
        }     
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        SearchEntity<MatTypeList> searchEntity = new SearchEntity<MatTypeList>(entity, start, limit, orders);
        Page<MatTypeList> page = this.matTypeListManager.findPageList(searchEntity);
        return JSONTools.toJSONList(page);
    }
    
    /**
     * <li>说明：新增物料信息
     * <li>创建人：张迪
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 返回提示信息
     * @throws IOException
     */
    public String saveNodeMatList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        PartsRdpNodeMat[] partsRdpNodeMat = JSONUtil.read(jo.getString("partsRdpNodeMat"), PartsRdpNodeMat[].class);
        
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpNodeMatManager.saveNodeMatList(partsRdpNodeMat);
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
 
	
	/**
	 * <li>说明：删除物料消耗
	 * <li>创建人：张迪
	 * <li>创建日期：2016-09-14
     * <li>修改人: 
     * <li>修改日期：
     * <li>修改内容：
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
	public String deleteNodeMats(String jsonObject) throws IOException {
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
        String[] validateMsg = this.partsRdpNodeMatManager.validateDelete(idArray);
        if (null != validateMsg) {
            return JSONUtil.write(OperateReturnMessage.newFailsInstance(validateMsg[0]));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            this.partsRdpNodeMatManager.logicDelete(idArray);
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
				rdpNodeIDX:"3E15424C5A0943BA9E195108949A188C"
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

}
