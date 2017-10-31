package com.yunda.jx.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
/**
 * 阿里巴巴Fastjson操作工具类
 * @author PEAK-CHEUNG
 *
 */
public final class Fastjson
{
    /**
     * 
     */
	private Fastjson() {}
	
	/**
	 * 编码格式
	 */
	public final static String ENCODING = "UTF-8";
	
	/**
	 * 将JSON字符串解析成实体对象
	 * @param <T> 类型
	 * @param text 文本
	 * @param clazz class
	 * @return 实体对象
	 */
	public static <T> T toObject(String text, Class<T> clazz)
	{
		return JSONObject.parseObject(text, clazz);
	}
	
	/**
	 * 从输入流中读取JSON字符串并解析成实体对象
	 * @param <T> 类型
	 * @param req request
	 * @param clazz class
	 * @return T
	 * @throws IOException
	 */	
	public static <T> T toObject(HttpServletRequest req, Class<T> clazz) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = -1;
		while((len = req.getInputStream().read(buff)) != -1)
		{
			out.write(buff, 0, len);
		}
		out.close();
		return JSONObject.parseObject(out.toByteArray(), clazz);
	}
	
	/**
	 * 将对象解析成json字符串 
	 * @param object 对象
	 * @return String
	 */
	public static String toJSON(Object object)
	{
		return JSONObject.toJSONString(object);
	}
	
	/**
	 * 将对象转换成JSON输出
	 * @param resp response
	 * @param object 对象
	 * @throws IOException
	 */
	public static void writer(HttpServletResponse resp, Object object) throws IOException
	{
		setDefaultContentType(resp);
		resp.getOutputStream().write(toJSON(object).getBytes(ENCODING));
	}
	
	/**
	 * <li>方法说明：输出文本
	 * <li>方法名：writeText
	 * @param resp response
	 * @param text 文本
	 * @throws IOException
	 */
	public static void writeText(HttpServletResponse resp, String text) throws IOException{
	    setDefaultContentType(resp);
	    resp.getOutputStream().write(text.getBytes(ENCODING));
	}
	
	/**
	 * 压缩数据输出
	 * @param resp response
	 * @param object 对象
	 * @throws IOException
	 */
	public static void gzipWriter(HttpServletResponse resp, Object object) throws IOException
	{
		setDefaultContentType(resp);
		resp.setHeader("Content-encoding", "gzip");
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gzipData(object, out);
		
		resp.setContentLength(out.size());
		resp.getOutputStream().write(out.toByteArray());
	}
	
	/**
	 * GZIP压缩数据
	 * @param object 压缩对象
	 * @param out 输出存储对象
	 * @throws IOException
	 */
	private static void gzipData(Object object, ByteArrayOutputStream out) throws IOException
	{
		GZIPOutputStream gout = new GZIPOutputStream(out);
		gout.write(toJSON(object).getBytes(ENCODING));
		gout.close();
	}
	/**
	 * 设置Response默认ContentType
	 * @param resp response
	 */
	private static void setDefaultContentType(HttpServletResponse resp)
	{
		resp.setCharacterEncoding(ENCODING);
		resp.setContentType("text/json;charset=" + ENCODING);
	}
}
