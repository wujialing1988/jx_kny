package com.yunda.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * <li>类型名称：
 * <li>说明：扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人：
 * <li>修改日期：
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	protected static final Log logger = LogFactory.getLog(BeanUtils.class);
    
    // 比较字段属性时排除不需要比较的字段
    protected static final String[] initialEliminates = {"idx","creator","createTime","updator","updateTime","recordStatus"};

	/**
	 * 
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	private BeanUtils() {
	}
	
	/**
	 * <li>方法名：getPropertyValue 
	 * <li>@param obj 实体对象
	 * <li>@param strPro 属性名，支持多级属性
	 * <li>@return 属性值
	 * <li>@throws Exception
	 * <li>返回类型：Object
	 * <li>说明：递归查找属性值
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-4-12
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static Object getPropertyValue(Object obj, String strProName) throws Exception {
		BeanWrapper bw = new BeanWrapperImpl(obj);
		if (strProName.indexOf(".") > 0) {
			int idx = strProName.indexOf(".");
			Object objPro = bw.getPropertyValue(strProName.substring(0, idx));
			return getProperty(objPro, strProName.substring(idx + 1));
		} else {
			return bw.getPropertyValue(strProName);
		}
	}

	/**
	 * 
	 * <li>方法名：getDeclaredField
	 * <li>
	 * 
	 * @param object
	 *            <li>
	 * @param propertyName
	 *            <li>
	 * @return
	 *            <li>
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 *             <li>返回类型：Field
	 *             <li>说明：循环向上转型,获取对象的DeclaredField.
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public static Field getDeclaredField(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);
		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 
	 * <li>方法名：getDeclaredField
	 * <li>
	 * 
	 * @param clazz
	 *            <li>
	 * @param propertyName
	 *            <li>
	 * @return
	 *            <li>
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 *             <li>返回类型：Field
	 *             <li>说明：循环向上转型,获取对象的DeclaredField.
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public static Field getDeclaredField(Class clazz, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(propertyName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName()
				+ '.' + propertyName);
	}

	/**
	 * 
	 * <li>方法名：forceGetProperty
	 * <li>
	 * 
	 * @param object
	 *            <li>
	 * @param propertyName
	 *            <li>
	 * @return
	 *            <li>
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 *             <li>返回类型：Object
	 *             <li>说明： 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public static Object forceGetProperty(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.info("error wont' happen");
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 
	 * <li>方法名：forceSetProperty
	 * <li>
	 * 
	 * @param object
	 *            <li>
	 * @param propertyName
	 *            <li>
	 * @param newValue
	 *            <li>
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 *             <li>返回类型：void
	 *             <li>说明：暴力设置对象变量值,忽略private,protected修饰符的限制.
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public static void forceSetProperty(Object object, String propertyName,
			Object newValue) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			logger.error("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * <li>方法名：invokePrivateMethod
	 * <li>@param className 类名
	 * <li>@param methodName 方法名
	 * <li>@param params 参数
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：Object
	 * <li>说明：利用反射调用方法
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-7-20
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public static Object invokePrivateMethod(String className, String methodName, Object... params) throws Exception {
		Class  c = Class.forName(className);
		Class[] types = null;
		if(params != null){
			types =new Class[params.length];
			for (int i = 0; i < params.length; i++) {
				types[i] = params[i].getClass();
			}
		}
		Method m = c.getMethod(methodName, types);
		return m.invoke(c, params);
		
		/*Class c = Class.forName(className);   
		return invokePrivateMethod(c , methodName,params);*/
	}
	
	/**
	 * 
	 * <li>方法名：invokePrivateMethod
	 * <li>
	 * 
	 * @param object
	 *            <li>
	 * @param methodName
	 *            <li>
	 * @param params
	 *            <li>
	 * @return
	 *            <li>
	 * @throws NoSuchMethodException
	 *             如果没有该Method时抛出.
	 *             <li>返回类型：Object
	 *             <li>说明：暴力调用对象函数,忽略private,protected修饰符的限制.
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public static Object invokePrivateMethod(Object object, String methodName,
			Object... params) throws NoSuchMethodException {
		Assert.notNull(object);
		Assert.hasText(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				logger.info("方法不在当前类定义,继续向上转型" + e.getMessage());
			}
		}

		if (method == null){
			logger.error("No Such Method:"
					+ clazz.getSimpleName() + methodName);
			throw new NoSuchMethodException("No Such Method:"
					+ clazz.getSimpleName() + methodName);
		}

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}
	
	

	/**
	 * 
	 * <li>方法名：getFieldsByType
	 * <li>
	 * 
	 * @param object
	 *            <li>
	 * @param type
	 *            <li>
	 * @return
	 *            <li>返回类型：List<Field>
	 *            <li>说明：按Filed的类型取得Field列表.
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public static List<Field> getFieldsByType(Object object, Class type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 
	 * <li>方法名：getPropertyType
	 * <li>
	 * 
	 * @param type
	 *            <li>
	 * @param name
	 *            <li>
	 * @return
	 *            <li>
	 * @throws NoSuchFieldException
	 *             <li>返回类型：Class
	 *             <li>说明：按FiledName获得Field的类型.
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public static Class getPropertyType(Class type, String name)
			throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}

	/**
	 * 
	 * <li>方法名：getGetterName
	 * <li>
	 * 
	 * @param type
	 *            <li>
	 * @param fieldName
	 *            <li>
	 * @return
	 *            <li>返回类型：String
	 *            <li>说明：获得field的getter函数名称.
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public static String getGetterName(Class type, String fieldName) {
		Assert.notNull(type, "Type required");
		Assert.hasText(fieldName, "FieldName required");

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}

	/**
	 * 
	 * <li>方法名：getGetterMethod
	 * <li>
	 * 
	 * @param type
	 *            <li>
	 * @param fieldName
	 *            <li>
	 * @return
	 *            <li>返回类型：Method
	 *            <li>说明：获得field的getter函数,如果找不到该方法,返回null.
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public static Method getGetterMethod(Class type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * <li>说明：匹配对象是否相等，如果相同返回true，否则false
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param field：属性对象
	 * @param arrays：属性对象排列集合
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private static boolean checkHas(Field field, Field[] arrays){
		if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
		    return false;
        }
		for(int i = 0; i < arrays.length; i++){
			if(field.getName() == arrays[i].getName()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <li>说明：比较两个属性对象，并获取其中相同的属性字符集合
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-12-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param  field1：属性对象1
	 * @param  field2：属性对象2
	 * @return String[]
	 */
	@SuppressWarnings("unused")
	private static String[] getMatchField(Field[] field1, Field[] field2){
		
		List<String> matcher = new ArrayList<String>(); 
		boolean f1Maxed = field1.length > field2.length; //比较标识
		int len =  f1Maxed ? field2.length : field1.length; 
		for (int i = 0; i < len; i++) {
			if(f1Maxed){ //标识为真，说明field1属性多，此时则用属性少的进行循环比较
				if(checkHas(field2[i], field1)){
					matcher.add(field2[i].getName());
				}
			}else{
				if(checkHas(field1[i], field2)){
					matcher.add(field1[i].getName());
				}
			}
		}
		return matcher.toArray(new String[matcher.size()]);
	}
	
	/**
	 * <li>说明：将fromList中的数据拷贝到toClass对象的List集合中
	 * <li>创建人：王治龙
	 * <li>创建日期：2013-12-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param toList：List目标源
	 * @param fromList：List数据来源
	 * @param clazz：实体对象Class
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List copyListToList(Class toClass, List<?> fromList) throws Exception {
		List toList = new ArrayList(); //定义操作的返回对象
		if(fromList != null && fromList.size() > 0){
			Class fromClass = fromList.get(0).getClass(); //获取List<?>泛型集合的泛型对象运行时CLASS
			Field[] toField = toClass.getDeclaredFields(); //获取要赋值对象的所有属性字段
			Field[] fromField = fromClass.getDeclaredFields(); //获取赋值来源的泛型对象的所有属性字段
			String[] matchField = getMatchField(toField, fromField);
			Field toClassField, fromListField;
			Object obj;
			for(int i = 0; i< fromList.size(); i++){ //循环对象赋值
				obj = toClass.newInstance(); //反射实例化对象
				for(int j = 0; j < matchField.length; j++){
					toClassField = toClass.getDeclaredField(matchField[j]); //获取toClass和fromClass中相同的属性X到toClassField中
					fromListField = fromClass.getDeclaredField(matchField[j]); //获取fromList和toClass中相同的属性XfromListField中
					fromListField.setAccessible(true); //设置属性为可访问
					toClassField.setAccessible(true);
					//获取该fromList.get(i)对象中fromListField的值，并设置到obj对象的toClassField属性中
					Object fromObj = fromListField.get(fromList.get(i)) ; //返回指定对象上此Field的值
					if(fromObj != null) toClassField.set(obj, fromObj); 
				}
				toList.add(obj);
			}
		}
		return toList;
	}
    
    /**
     * <li>说明：比较两个实体的字段值是否一致，并返回不一致的结果集
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param obj1 比较实体1
     * @param obj2 比较实体2
     * @param eliminates 排除字段集合
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static List compareObjectValue(Object obj1,Object obj2,String[] eliminates) {
        List toList = new ArrayList(); // 定义操作的返回对象
        try {
            Class clasz1 = obj1.getClass();
            Class clasz2 = obj2.getClass();
            Field[] fields1 = clasz1.getDeclaredFields();
            Field[] fields2 = clasz2.getDeclaredFields();
            String[] matchField = getMatchField(fields1, fields2); // 获取两个对象属性相同的所有字段集合
            Field compareField1, compareField2;
            List<String> eliminatesList = new ArrayList<String>(); // 排除的字段
            for (String eli : initialEliminates) {
                if(!eliminatesList.contains(eli)){
                    eliminatesList.add(eli);
                }
            }
            if(eliminates != null){
                for (String eli : eliminates) {
                    if(!eliminatesList.contains(eli)){
                        eliminatesList.add(eli);
                    }
                }
            }
            for (String fieldName : matchField) {
                if(!eliminatesList.contains(fieldName)){
                    compareField1 = clasz1.getDeclaredField(fieldName); // 获取clasz1对象的属性
                    compareField2 = clasz2.getDeclaredField(fieldName); // 获取clasz2对象的属性
                    String type1 = compareField1.getGenericType().toString(); // 获取属性的类型
                    String type2 = compareField2.getGenericType().toString(); // 获取属性的类型
                    // 如果类型相同，则进行比较
                    if(type1.equals(type2)){
                        String methodName = fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                        Method m1 = clasz1.getMethod("get" + methodName);
                        Method m2 = clasz2.getMethod("get" + methodName);
                        // 根据不同类型获取值，并比较
                        if (type1.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                            String value1 = (String) m1.invoke(obj1); // 调用getter方法获取属性值
                            String value2 = (String) m2.invoke(obj2); // 调用getter方法获取属性值
                            if (!value1.equals(value2)) {
                                toList.add(fieldName);
                                break ;
                            }
                        }else if(type1.equals("class java.lang.Integer")) { 
                            Integer value1 = (Integer) m1.invoke(obj1); 
                            Integer value2 = (Integer) m2.invoke(obj2); 
                            if (value1 != value2) {
                                toList.add(fieldName);
                                break ;
                            }
                        }else if(type1.equals("class java.lang.Boolean")) { 
                            Boolean value1 = (Boolean) m1.invoke(obj1); 
                            Boolean value2 = (Boolean) m2.invoke(obj2);
                            if (value1 != value2) {
                                toList.add(fieldName);
                                break ;
                            }
                        }else if(type1.equals("class java.util.Date")) {
                            Date value1 = (Date) m1.invoke(obj1); 
                            Date value2 = (Date) m2.invoke(obj2); 
                            if ((value1 != null && value2 != null && value1 != value2) 
                                || (value1 == null && value2 != null)
                                || (value1 != null && value2 == null)) {
                                toList.add(fieldName);
                                break ;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return toList ;
    } 
}