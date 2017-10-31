package com.yunda.jx.pjjx.util;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.Order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.DateUtil;
import com.yunda.jx.pjjx.webservice.IService;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：JSON对象工具类
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-7 下午03:12:50
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class JSONTools {

	/**
	 * <li>说明：根据排序新增构造的JSON数据获取排序对象数组
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-01-07
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * 
	 * @param orders [{
			sort: "idx",
			dir: "ASC"
		}]
	 * @return Order[] 排序对象数组
	 */
	public static Order[] getOrders(String orders) {
		JSONArray jArray = JSONArray.parseArray(orders);
		return getOrders(jArray);
	}
	
	/**
	 * <li>说明：根据排序新增构造的JSON数据获取排序对象数组
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-01-07
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * 
	 * @param jArray JSON对象数组
	 * @return Order[] 排序对象数组
	 */
	public static Order[] getOrders(JSONArray jArray) {
		if (null == jArray) {
			return null;
		}
		List<Order> list = new ArrayList<Order>();
		String sort;				// 排序字段
		String dir;					// 排序类型
		for (int i = 0; i < jArray.size(); i ++) {
			sort = jArray.getJSONObject(i).getString("sort");
			dir = jArray.getJSONObject(i).getString("dir");
			if (dir.equalsIgnoreCase("ASC")) {
				list.add(Order.asc(sort));
			} else if (dir.equalsIgnoreCase("DESC")) {
				list.add(Order.desc(sort));
			}
		}
		return list.toArray(new Order[list.size()]);
	}
	
	/**
	 * <li>说明：将查询到的列表对象转换为JSON对象
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-01-13
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * 
	 * @param list 列表
	 * @return {
	 		count:1,
	 		list:[{},{}]
	   }
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String toJSONList(List list) throws JsonGenerationException, JsonMappingException, IOException {
		return toJSONList(list.size(), list);
	}
	
	/**
	 * <li>说明：将查询到的列表对象转换为JSON对象
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-01-22
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * 
	 * @param totalCount 记录总条数
	 * @param list 单次查询的记录列表
	 * @return {
	 		totalCount:2,
	 		list:[{}]
	   }
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String toJSONList(int totalCount, List list) throws JsonGenerationException, JsonMappingException, IOException {
        if (null == list || list.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_RESULT_IS_EMPTY));
        }
		// 设置日期的输出格式为 yyyy-MM-dd HH:mm:ss
		ObjectMapper om = new ObjectMapper();
		om.getSerializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
		om.getDeserializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
		
		JSONObject jObject = new JSONObject();
		jObject.put(IService.JSON_FILED_NAME_COUNT, totalCount);
		jObject.put(IService.JSON_FILED_NAME_LIST, om.writeValueAsString(list));
		
		return jObject.toString();
	}
    
	/**
	 * <li>说明：将查询到的列表对象转换为JSON对象
	 * <li>创建人： 何涛
	 * <li>创建日期： 2015-03-31
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * 
	 * @param totalCount 记录总条数
	 * @param list 单次查询的记录列表
     * @param dateFormat 日期格式
	 * @return {
	 		totalCount:2,
	 		list:[{}]
	   }
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String toJSONList(int totalCount, List list, DateFormat dateFormat) throws JsonGenerationException, JsonMappingException, IOException {
	    if (null == list || list.size() <= 0) {
	        return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_RESULT_IS_EMPTY));
	    }
	    // 设置日期的输出格式为 yyyy-MM-dd HH:mm:ss
	    ObjectMapper om = new ObjectMapper();
        if (null != dateFormat) {
            om.getSerializationConfig().setDateFormat(dateFormat);
            om.getDeserializationConfig().setDateFormat(dateFormat);
        }
	    
	    JSONObject jObject = new JSONObject();
	    jObject.put(IService.JSON_FILED_NAME_COUNT, totalCount);
	    jObject.put(IService.JSON_FILED_NAME_LIST, om.writeValueAsString(list));
	    
	    return jObject.toString();
	}
    
     /**
     * <li>说明：将查询到的Page对象转换为JSON对象
     * <li>创建人：何涛
     * <li>创建日期：2016-3-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param page 分页查询封装对象
     * @return {
            totalCount:2,
            list:[{}]
       }
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public static String toJSONList(Page<?> page) throws JsonGenerationException, JsonMappingException, IOException {
        if (null == page) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_RESULT_IS_EMPTY));
        }
        return toJSONList(page.getTotal(), page.getList());
    }
    
    /**
     * <li>说明：使用指定的日期格式转换JSON对象
     * <li>创建人：何涛
     * <li>创建日期：2016-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param object JSON编码对象
     * @param format 日期格式，如果为空，则默认格式为：yyyy-MM-dd HH:mm:ss
     * @return JSON对象
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String toJSON(Object object, DateFormat format) throws JsonGenerationException, JsonMappingException, IOException {
        if (null == format) {
            format = DateUtil.yyyy_MM_dd_HH_mm_ss;
        }
        ObjectMapper om = new ObjectMapper();
        om.getSerializationConfig().setDateFormat(format);
        om.getDeserializationConfig().setDateFormat(format);
        return om.writeValueAsString(object);
    }
	
}
