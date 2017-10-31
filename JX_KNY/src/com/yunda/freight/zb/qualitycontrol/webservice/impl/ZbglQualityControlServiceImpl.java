package com.yunda.freight.zb.qualitycontrol.webservice.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.zb.gztp.entity.Gztp;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControl;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlDTO;
import com.yunda.freight.zb.qualitycontrol.manager.ZbglQualityControlManager;
import com.yunda.freight.zb.qualitycontrol.webservice.IZbglQualityControlService;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 质量检验webservice接口
 * <li>创建人：何东
 * <li>创建日期：2017-04-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglQualityControlService")
public class ZbglQualityControlServiceImpl implements IZbglQualityControlService {

	Logger logger = Logger.getLogger(this.getClass());
	
	/** ZbglQualityControlManager业务类,质量检查 */
	@Autowired
	private ZbglQualityControlManager zbglQualityControlManager;

	/**
     * <li>说明：根据用户ID查询质量检查任务
     * <li>创建人：何东
     * <li>创建日期：2017-04-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》根据用户ID查询质量检查任务
     * @return 质量检验任务单列表
     */
	@Override
	public String queryQualityControl(String jsonObject) {
		try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            Gztp entity = JSONUtil.read(entityJson, Gztp.class);
            if (null == entity) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            // 分页页号
            int start = jo.getIntValue(Constants.START);
            
            // 分页大小
            int limit = jo.getIntValue(Constants.LIMIT);
            start = limit * (start - 1);
            
            Page<ZbglQualityControlDTO> page = zbglQualityControlManager.queryQualityControl(start, limit, entity);
            List<ZbglQualityControlDTO> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            
            return JSONTools.toJSONList(page.getTotal(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
	}
	
	/**
     * <li>说明：完成质量检查
     * <li>创建人：何东
     * <li>创建日期：2017-04-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》完成质量检查
     * @return 操作结果
     */
	@Override
	public String finishQualityControl(String jsonObject) {
		try {
			JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
			
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            ZbglQualityControl[] entitys = JSONObject.parseObject(entityJson, new TypeReference<ZbglQualityControl[]>(){});
            if (null == entitys || entitys.length <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            zbglQualityControlManager.finishQualityControl(entitys, jo.getString("checkResult"));
            
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
	}
}
