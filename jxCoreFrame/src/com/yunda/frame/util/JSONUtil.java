package com.yunda.frame.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 处理JSON的工具类
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-8-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public final class JSONUtil{
	
	/** 对象映射解析器，使用静态对象提高效率 */
	private final static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * <li>说明：获取json工具的对象映射解析器
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return ObjectMapper json工具的对象映射解析器
	 */
	public static ObjectMapper getObjectMapper(){
		return mapper;
	}
	/**
	 * <li>说明：解析JSON返回实体类对象，读取HTTP请求的输入流，根据Class<T>类型返回对象
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param <T> 泛型，指定入参及返回的对象类型
	 * @param request HTTP请求对象
	 * @param clazz	返回对象的类型
	 * @return 解析JSON后返回的对象实例
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T read(HttpServletRequest request, Class<T> clazz)
		throws JsonParseException, JsonMappingException, IOException{
		return mapper.readValue(request.getInputStream(), clazz);
	}
	/**
	 * <li>说明：解析JSON字符串，根据Class<T>类型返回对象
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param <T> 泛型，指定入参及返回的对象类型
	 * @param json json字符串
	 * @param clazz	返回对象的类型
	 * @return 解析JSON后返回的对象实例
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T read(String json, Class<T> clazz)
		throws JsonParseException, JsonMappingException, IOException{
		return mapper.readValue(json, clazz);
	}	
	/**
	 * <li>说明：根据实体类对象返回JSON字符串，并写入HttpServletResponse
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param response	HTTP响应对象
	 * @param entity	实体对象
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void write(HttpServletResponse response, Object entity)
		throws JsonMappingException, IOException{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		mapper.writeValue(response.getWriter(), entity);
//		response.flushBuffer();
	}
	/**
	 * <li>说明：根据实体类对象返回JSON字符串
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity	实体对象
	 * @return 解析JSON后返回的对象实例
	 * @throws IOException
	 */
	public static String write(Object entity)
		throws IOException{
		return mapper.writeValueAsString(entity);
	}	
}