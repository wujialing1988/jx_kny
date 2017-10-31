package com.yunda.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 * <li>类型名称：
 * <li>说明：读取properties文件工具类
 * <li>创建人： 温俊
 * <li>创建日期：2012-7-17
 * <li>修改人： 
 * <li>修改日期：
 */
public class PropertiesUtil {

	private static PropertiesUtil instance = null;

	private static Hashtable<String, Properties> htmlfileHash = null;

	private static Hashtable<String, Long> htmlfileTime = null;

	private PropertiesUtil() {
		htmlfileHash = new Hashtable<String, Properties>();
		htmlfileTime = new Hashtable<String, Long>();
	}

	public static PropertiesUtil getInstance() {
		if (instance == null) {
			instance = new PropertiesUtil();
		}
		return instance;
	}

	public synchronized Properties loadTemplateProp(String fileName) {
		try {
			File file = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath()
					+ fileName + ((fileName.indexOf(".properties") > -1) ? "" : ".properties"));
			if (!file.exists()) {
				return null;
			}

			long lLastModified = file.lastModified();
			Long lFileTime = htmlfileTime.get(fileName);

			if (lFileTime == null || lFileTime.longValue() != lLastModified) {
				Properties properties = new Properties();
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				properties.load(bis);
				bis.close();
				htmlfileHash.put(fileName, properties);
				htmlfileTime.put(fileName, new Long(lLastModified));
			}

			return htmlfileHash.get(fileName);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	/**
	 * <li>方法名：getPropValue
	 * <li>@param fileName 文件名，不包含“.properties”
	 * <li>@param key 属性名
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：读取属性值
	 * <li>创建人：温俊
	 * <li>创建日期：2012-7-17
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String getPropValue(String fileName, String key) {
		Properties properties = loadTemplateProp(fileName);

		if (properties != null && properties.getProperty(key) != null) {
			return properties.getProperty(key).trim();
		}
		return "";
	}
}
