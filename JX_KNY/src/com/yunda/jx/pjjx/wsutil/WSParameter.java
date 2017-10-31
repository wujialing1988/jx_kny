package com.yunda.jx.pjjx.wsutil;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>说明：WS参数对应实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-14
 * <li>成都运达科技股份有限公司
 * @param <T> 查询返回类型
 */
public class WSParameter<T>{

	private int start;
	private int limit;
	private Order[] orders;
	private T entity;
	/**
	 * 构造WEB服务参数
	 * @param <T>
	 * @param jsonObject json字符串
	 * @param clazz 类型
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public WSParameter(String jsonObject, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(IService.MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        /*Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            throw new NullPointerException(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID);
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);*/
        this.limit = jo.getIntValue("limit");
        this.start = limit * (jo.getIntValue(Constants.START) - 1);
        
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        this.orders = JSONTools.getOrders(jArray);
        String entityJson = jo.getString("entityJson");
        this.entity = JSONUtil.read(StringUtil.isNullOrBlank(entityJson) ? "{}" : entityJson, clazz);
	}
	
	public int getStart() {
		return start;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public Order[] getOrders() {
		return orders;
	}

	public T getEntity() {
		return entity;
	}
}
