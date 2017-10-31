package com.yunda.frame.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.util.StdDateFormat;
import org.springframework.web.context.ContextLoader;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.util.systemsite.ISystemSiteManager;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该工具类提供检修系统实体类相关支持完善，如设置实体类非业务字段（创建人、创建时间、修改人、修改时间、站点标识）。
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-8-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class EntityUtil {
	/** 实体类主键字段属性名称 */
	public static final String IDX = "idx";
	/** 实体类属性：站点标识 */
	public static final String SITE_ID = "siteID";
	/** 实体类属性：创建人 */
	public static final String CREATOR = "creator";
	/** 实体类属性：创建时间 */
	public static final String CREATE_TIME = "createTime";
	/** 实体类属性：修改人 */
	public static final String UPDATOR = "updator";
	/** 实体类属性：修改时间 */
	public static final String UPDATE_TIME = "updateTime";
	/** 实体类属性：记录状态，逻辑删除字段 */
	public static final String RECORD_STATUS = "recordStatus";
	/** 实体类属性：站点标识名称 */
	public static final String SITE_NAME = "siteName";
	
	/**
	 * <li>说明：判断一个类是否包含指定名称的属性字段(不考虑继承的字段)，true：包含，false：不包含
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Class clazz：类描述
	 * @param String fieldname: 属性字段名称
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static boolean contains(Class clazz, String fieldname){
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null || fields.length < 1)	return false;
		for (Field field : fields) {
			if(fieldname.equals(field.getName()))	return true;
		}
		return false;
	}
	/**
	 * <li>说明：尝试将对象值转换成制定的简单对象类型（不支持复杂对象转换），如"12"转换成Integer(12)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Object src：需要转换的源对象
	 * @param Class<T> desClass:要转换成的目标类描述
	 * @return T : 制定的类型对象 
	 * @throws ParseException 
	 */
	public static Object convert(Object src, Class desClass) throws ParseException{
		String className = desClass.getName();
		Object ret = src;
		if("java.lang.String".equals(className)){
			ret = src.toString();
		} else if("java.lang.Boolean".equals(className)){
			ret = Boolean.parseBoolean(src.toString());
		} else if("java.lang.Character".equals(className)){
			ret = src.toString().charAt(0);
		} else if("java.lang.Double".equals(className)){
			ret = Double.parseDouble(src.toString());
		} else if("java.lang.Float".equals(className)){
			ret = Float.parseFloat(src.toString());
		} else if("java.lang.Integer".equals(className)){
			ret = Integer.parseInt(src.toString());
		} else if("java.lang.Long".equals(className)){
			ret = Long.parseLong(src.toString());
		} else if("java.lang.Short".equals(className)){
			ret = Short.parseShort(src.toString());
		} else if("java.math.BigDecimal.".equals(className)){
			ret = new BigDecimal(src.toString());
		} else if("java.math.BigInteger".equals(className)){
			ret = new BigInteger(src.toString());
		} else if("java.util.Date".equals(className)){
			ret = new StdDateFormat().parse(src.toString());
		}
		return ret;
	}	
	/**
	 * <li>说明：设置实体类中与业务无关的系统信息字段：siteID,creator,createTime,updator,updateTime
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-5
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T 实体对象
	 * @return 要保存的实体类对象
	 */
	public static <T> T setSysinfo(T t) throws NoSuchFieldException{
		Date now = new Date();
		return setSysinfo(t, now);
	}
	/**
	 * <li>说明：设置实体类中与业务无关的系统信息字段：siteID,creator,createTime,updator,updateTime
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-11-5
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T 实体对象
	 * @param Date now 当前日期对象
	 * @return 要保存的实体类对象
	 */	
	public static <T> T setSysinfo(T t, Date now) throws NoSuchFieldException{
		String idx = null;
		if(contains(t.getClass(), IDX)){
			//判断实体类idx主键，若为“”空白字符串，设置实体类主键idx为null
			idx = (String)BeanUtils.forceGetProperty(t, IDX);
			idx = StringUtil.nvlTrim(idx, null);
			BeanUtils.forceSetProperty(t, IDX, idx);			
		}
		// 黄杨2017/5/10修改 处理通过定时器拿不到登录员信息的空指针异常
		long operatorid;
		try{
			//获取当前登录操作员
			AcOperator acOperator = SystemContext.getAcOperator();
			operatorid = acOperator.getOperatorid();
		} catch(NullPointerException e) {
			operatorid = 0;
		}
		//如果是新增记录，设置创建人、创建时间、站点标识;
		if(idx == null){
			if(contains(t.getClass(), CREATOR))		BeanUtils.forceSetProperty(t, CREATOR, operatorid);
			if(contains(t.getClass(), CREATE_TIME))		BeanUtils.forceSetProperty(t, CREATE_TIME, now);
			
			if(contains(t.getClass(), SITE_ID)){
				//设置同步站点标识，若为“”空白字符串，设置当前系统所在站点的标识
				String siteID = (String)BeanUtils.forceGetProperty(t, SITE_ID);
				//2013-6-17修改：由于重庆整备单系统多站点数据过滤的要求，站点标示获取和当前登录人员相关
				siteID = StringUtil.nvlTrim(siteID,null);
				siteID = findSysSiteId(siteID);
				BeanUtils.forceSetProperty(t, SITE_ID, siteID);
                if (contains(t.getClass(), SITE_NAME)) {
                    //设置同步站点标识，若为“”空白字符串，设置当前系统所在站点的标识
                    String siteName = (String)BeanUtils.forceGetProperty(t, SITE_NAME);
                    siteName = StringUtil.nvlTrim(siteName,"");
                    ISystemSiteManager siteIdManager = EntityUtil.getSiteId();  //获取接口对象
                    if(StringUtil.isNullOrBlank(siteName) && siteIdManager != null){ 
                        siteName = siteIdManager.findSysSiteNameByLoginUser(siteID);
                    }
                    BeanUtils.forceSetProperty(t, SITE_NAME, siteName);
                }
			}
			
		}
		//设置修改人、修改时间
		if(contains(t.getClass(), UPDATOR))		BeanUtils.forceSetProperty(t, UPDATOR, operatorid);
		if(contains(t.getClass(), UPDATE_TIME))		BeanUtils.forceSetProperty(t, UPDATE_TIME, now);
		return t;
	}	
	
	/**
	 * 静态方法， 尝试获取siteid接口实现
	 * @return
	 */
	private static ISystemSiteManager getSiteId(){
		return (ISystemSiteManager)ContextLoader.getCurrentWebApplicationContext().getBean("systemSiteManager");
	}
	
	/**
	 * <li>说明：设置实体类中与业务无关的系统信息字段：siteID,creator,createTime,updator,updateTime
	 * <li>创建人：刘晓斌
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param List<T> 实体类对象集合
	 * @return 要保存的实体类对象
	 */
	public static <T> List<T> setSysinfo(List<T> entityList) throws NoSuchFieldException{
		for (T t : entityList) {
			t = setSysinfo(t);
		}
		return entityList;
	}	
	/**
	 * <li>说明：设置实体类逻辑删除字段状态为已删除，0未删除，1已删除；实体类逻辑删除属性名称必须为"recordStatus"
	 * <li>创建人：刘晓斌
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T 实体类对象
	 * @return 要保存的实体类对象
	 */
	public static <T> T setDeleted(T t) throws NoSuchFieldException{
		//设置逻辑删除字段状态为已删除，0未删除，1已删除
		if(contains(t.getClass(), RECORD_STATUS))	BeanUtils.forceSetProperty(t, RECORD_STATUS, Constants.DELETED);
		return t;
	}
	/**
	 * <li>说明：设置实体类逻辑删除字段状态为已删除，0未删除，1已删除；实体类逻辑删除属性名称必须为"recordStatus"
	 * <li>创建人：刘晓斌
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param List<T> 实体类对象集合
	 * @return 要保存的实体类对象
	 */
	public static <T> List<T> setDeleted(List<T> entityList) throws NoSuchFieldException{
		for (T t : entityList) {
			setDeleted(t);
		}
		return entityList;
	}
	/**
	 * <li>说明：设置实体类逻辑删除字段状态为未删除，0未删除，1已删除；实体类逻辑删除属性名称必须为"recordStatus"
	 * <li>创建人：刘晓斌
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T 实体类对象
	 * @return 要保存的实体类对象
	 */
	public static <T> T setNoDelete(T t) throws NoSuchFieldException{
		//设置逻辑删除字段状态为已删除，0未删除，1已删除
		if(contains(t.getClass(), RECORD_STATUS))	BeanUtils.forceSetProperty(t, RECORD_STATUS, Constants.NO_DELETE);
		return t;
	}
	/**
	 * <li>说明：设置实体类逻辑删除字段状态为未删除，0未删除，1已删除；实体类逻辑删除属性名称必须为"recordStatus"
	 * <li>创建人：刘晓斌
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param List<T> 实体类对象集合
	 * @return 要保存的实体类对象
	 */
	public static <T> List<T> setNoDelete(List<T> entityList) throws NoSuchFieldException{
		for (T t : entityList) {
			setNoDelete(t);
		}
		return entityList;
	}
	/**
	 * <li>说明：根据类的属性名称获取该属性字段的描述定义
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static Field getFieldByName(Class clazz, String name){
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(name)) return field;
		}		
		return null;
	}
	/**
	 * <li>说明：根据类的属性名称获取该属性字段的注解@Column的name值
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-5-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public static String getColumnName(Class clazz, String name){
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(name)){
				Column col = field.getAnnotation(Column.class);
				if(col == null)	return name;
				String colName = col.name();
				return colName == null ? name: colName;
			}
		}		
		return null;		
	}
	/**
	 * 
	 * <li>说明：根据登陆人或JXConfig.xml配置获取站场标示ID
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param siteID 站场标示ID
	 * @return 站场标示ID
	 */
	public static String findSysSiteId(String siteID) {
		ISystemSiteManager siteIdManager = getSiteId();  //获取接口对象
		if (StringUtil.isNullOrBlank(siteID) && siteIdManager != null) {
			siteID = siteIdManager.findSysSiteIdByLoginUser();// 已成功获取接口实现类对象，调用的siteid获取方法根据登录人获得siteid
		}
		siteID = StringUtil.nvlTrim(siteID, JXSystemProperties.SYN_SITEID);// 如果siteID为空则从JxConfig.xml配置中获取；
		return siteID;
	}
	/**
	 * 
	 * <li>说明：根据登陆人获取站场标示名称
	 * <li>创建人：程锐
	 * <li>创建日期：2015-1-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param siteID 站场标示ID
	 * @param siteName 站场标示名称
	 * @return 站场标示名称
	 */
	public static String findSysSiteName(String siteID, String siteName) {
		ISystemSiteManager siteIdManager = getSiteId();  //获取接口对象
		if (StringUtil.isNullOrBlank(siteName) && siteIdManager != null) {
			siteName = siteIdManager.findSysSiteNameByLoginUser(siteID);
		}
		return siteName;
	}
}
