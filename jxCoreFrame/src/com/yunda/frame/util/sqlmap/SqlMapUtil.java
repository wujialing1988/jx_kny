package com.yunda.frame.util.sqlmap;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: sql文件映射及处理工具类，类似ibatis框架对sql语句进行文件管理
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-4-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class SqlMapUtil {
	/** 查找sql语句的id字符串分隔符  */
	public final static String SEPARATOR = ":";
	/** 日志工具类 */
	private static Logger logger = Logger.getLogger(SqlMapUtil.class.getName());
	/** sql文件类型 后缀名  */
	private final static String FILE_TYPE = ".xml";
	/** sql文件所在目录 */
	private static File DIR;
	/** sql映射集合对象 */
	private static Map<String, Map<String, String>> sqlMap;
//	静态初始化
	static{
		initCache();
	}
	/**
	 * <li>说明：获取sql映射集合对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return Map<String, Map<String, String>> sql映射集合对象
	 * @throws 抛出异常列表
	 */
	private static Map<String, Map<String, String>> getSqlMap(){
		if(sqlMap == null)	initCache();
		return sqlMap;
	}
	/**
	 * <li>说明：初始化缓存操作
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 */
	private static void initCache(){
		try {
			sqlMap = new HashMap<String, Map<String, String>>();
			List<File> list = getSqlFiles();
			if(list == null && list.size() < 1)	return;
			for (File file : list) {
				SqlMap map = JAXB.unmarshal(file, SqlMap.class);
				List<SqlTag> sqlList = map.getSql();
				if(sqlList == null || sqlList.size() < 1)	continue;
				
				Map<String, String> sqlTagMap = new HashMap<String, String>();
				for (SqlTag tag : sqlList) {
					sqlTagMap.put(tag.getId(), tag.getValue());
				}
				String fileName = file.getName();
				int idx = fileName.lastIndexOf(FILE_TYPE);
				sqlMap.put(fileName.substring(0, idx), sqlTagMap);
			}
		} catch (URISyntaxException e) {
			ExceptionUtil.process(e, logger);
		}		
	}
	/**
	 * <li>说明：根据pathid获取对应的sql语句字符串，未找到返回null
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String pathid 格式为文件名(不含后缀名)+id
	 * @return String sql sql语句
	 * @throws 抛出异常列表
	 */
	public static String getSql(String pathid){
		if(pathid == null || "".equals(StringUtil.nvlTrim(pathid)))	return null;
		try {
			if (JXConfig.getInstance().isSqlMapUseCache())	return getSqlFromCache(pathid);
			return getSqlFromFile(pathid);
		} catch (URISyntaxException e) {
			ExceptionUtil.process(e, logger);
		}
		return null;
	}
	/**
	 * <li>说明：根据pathid获取对应的sql语句字符串，未找到返回null
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String pathid 格式为文件名(不含后缀名)+id
	 * @param Map params 形参集合（key参数名，value参数值）
	 * @return String sql sql语句
	 * @throws 抛出异常列表
	 */
	public static String getSql(String pathid, Map params){
		String sql = getSql(pathid);
		if(params == null)	return sql;
		return StringUtil.replaceAll(sql, params);
	}	
	/**
	 * <li>说明：获取配置文件所在目录的File对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return File目录对象
	 * @throws URISyntaxException
	 */
	private static File getFileDir() throws URISyntaxException{
		if (DIR == null) {
			File clsdir = new File(SqlMapUtil.class.getResource("").toURI());
			String sepPath = File.separator + "file" + File.separator;
			DIR = new File(clsdir, sepPath);
		}
		return DIR;
	}
	/**
	 * <li>说明：获取SqlMap文件目录所有sql映射文件
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return List<File> sql文件列表
	 * @throws URISyntaxException 
	 * @throws 抛出异常列表
	 */
	private static List<File> getSqlFiles() throws URISyntaxException{
		File[] files = getFileDir().listFiles();
		List<File> list = new ArrayList<File>();
		for (File file : files) {
			if(file.getName().lastIndexOf(FILE_TYPE) != -1)	list.add(file);
		}
		return list;
	}
	/**
	 * <li>说明：使用缓存模式，从缓存中查找sql语句(客户生产环境中必须使用该模式，提高性能)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws URISyntaxException 
	 * @throws 抛出异常列表
	 */
	private static String getSqlFromCache(String pathid) throws URISyntaxException{
		String[] paths = pathid.split(SEPARATOR);
		Map<String, String> tag = getSqlMap().get(paths[ 0 ]);
		if(tag == null)	return null;
		String sql = tag.get(paths[ 1 ]);
		return sql == null ? null : sql.trim();
	}
	/**
	 * <li>说明：不使用缓存模式，从文件中查找sql语句(开发环境中可使用该模式，避免重启)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws URISyntaxException 
	 * @throws 抛出异常列表
	 */
	private static String getSqlFromFile(String pathid) throws URISyntaxException{
		String[] paths = pathid.split(SEPARATOR);
		File sqlFile = new File(getFileDir(), paths[ 0 ] + FILE_TYPE);
		SqlMap sqlmap = JAXB.unmarshal(sqlFile, SqlMap.class);
		List<SqlTag> sqlList = sqlmap.getSql();
		if (sqlList == null || sqlList.size() < 1)	return null;
		for (SqlTag tag : sqlList) {
			if(tag.getId().equals(paths[ 1 ]))	return tag.getValue().trim();
		}
		return null;
	}
	
}
