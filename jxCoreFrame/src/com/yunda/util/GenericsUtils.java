package com.yunda.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <li>类型名称：
 * <li>说明：通用的范型参数类型获取工具。
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人： 
 * <li>修改日期：
 */
public class GenericsUtils {
	private static final Log log = LogFactory.getLog(GenericsUtils.class);
	
	/**
	 * 
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	private GenericsUtils() {
	}

	/**
	 * 
	 * <li>方法名：getSuperClassGenricType
	 * <li>@param clazz The class to introspect
	 * <li>@return the first generic declaration, or <code>Object.class</code> if cannot be determined
	 * <li>返回类型：Class
	 * <li>说明：通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 
	 * <li>方法名：getSuperClassGenricType
	 * <li>@param clazz clazz The class to introspect
	 * <li>@param index the Index of the generic ddeclaration,start from 0.
	 * <li>@return the index generic declaration, or <code>Object.class</code> if cannot be determined
	 * <li>返回类型：Class
	 * <li>说明：通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}
}
