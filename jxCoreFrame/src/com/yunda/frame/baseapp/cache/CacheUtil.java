package com.yunda.frame.baseapp.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 系统缓存服务接口调用工具类
 * <br/><li>创建人：刘晓斌
 * <br/><li>创建日期：2013-9-10
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //将数据压入缓存容器
 * &nbsp; OmEmployee emp = new OmEmployee();
 * &nbsp; emp.setEmpname("张三");
 * &nbsp; CacheUtil.put("zhangsan", emp);
 * &nbsp; 
 * &nbsp; //从缓存容器取出数据
 * &nbsp; OmEmployee emp = (OmEmployee)CacheUtil.get("zhangsan");
 * &nbsp; //删除缓存容器的数据
 * &nbsp; CacheUtil.remove("zhangsan"); 
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class CacheUtil {
	/** 缓存容器名称 */
	private static final String CACHE_NAME = "JX_CACHE";
	/** 缓存容器 */
	private static Cache cache = null;
	
	/**
	 * <br/><li>说明：根据键（key），删除系统默认缓存容器中的对应的缓存数据
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-9-12
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param key 缓存对象的键
	 * @return true删除成功，false删除失败
	 */	
	public static boolean remove(Object key){
		return getCache().remove(key);
	}
	/**
	 * <br/><li>说明：删除系统默认缓存容器中的所有缓存数据
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-9-12
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return void
	 */
	public static void removeAll(){
		getCache().removeAll();
	}
	/**
	 * <br/><li>说明：将数据对象放入系统默认的缓存容器，采用（key/value）存放
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-9-11
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param key 缓存对象的键
	 * @param value 缓存对象
	 * @return void
	 */
	public static void put(Object key, Object value){
		if(key == null || value == null)	throw new RuntimeException("参数异常：key和value不能为null");
		getCache().put(new Element(key, value));
	}
	/**
	 * <br/><li>说明：根据键值（key）从系统默认的缓存容器中取出数据对象
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-9-11
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param key 缓存对象的键
	 * @return 缓存对象
	 */
	public static Object get(Object key){
		Element el = getCache().get(key);
		if(el == null)	return null;
		return el.getObjectValue();
	}
	/**
	 * <br/><li>说明：获取默认的缓存容器
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-9-10
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 缓存容器
	 */
	private static Cache getCache(){
		if(cache == null){
			CacheManager manager = CacheManager.getInstance();
			manager.addCache(CACHE_NAME);
			cache = manager.getCache(CACHE_NAME);
		}
		return cache;
	}
	
}