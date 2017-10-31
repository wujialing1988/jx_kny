package com.yunda.base.context;

import java.util.HashMap;
import java.util.Map;

/**
 * <li>标题: CoreFrame
 * <li>说明：该类用于保存用户与Web服务器会话过程的常用数据信息，如HttpSeesion中的用户、组织机构、权限等。
 * 便于在各层次进行调用（如业务层、数据访问层获取操作员、组织等）。
 * 不建议直接使用该类，在业务层、数据访问层等要获取该类的实例对象应该通过SystemContext的接口访问才能保证线程安全。
 * <li>版权: Copyright (c) 2008 运达科技公司
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-23
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * @author 运达科技公司
 * @version 1.0
 */
public final class UserSession {

	/** 该Map保存HttpSession中的对象以及其他系统上下文会话过程的相关信息 */
	private Map<Object,Object> webAttr = new HashMap<Object,Object>();
	
	/**
	 * <li>说明：获取web容器中的属性对象，如HttpSession中的对象
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param attrKey 保存对象的键值
	 * @return 要保存的实体类对象
	 */
	public Object getWebAttribute(String attrKey) {
		return this.webAttr.get(attrKey);
	}

	/**
	 * <li>说明：将对象保存到上下web容器中
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param attrKey 要保存对象的键值
	 * @param obj	要保存的对象
	 */
	public void setWebAttribute(String attrKey, Object obj) {
		this.webAttr.put(attrKey, obj);
	}
	/**
	 * <li>说明：移除Web容器所设置的信息
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param attrKey 要保存对象的键值
	 * @param obj	要保存的对象
	 */
	public void removeWebAttribute(String attrKey) {
		this.webAttr.remove(attrKey);
	}
	/**
	 * <li>说明：释放资源
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 */	
	public void release(){
		if(this.webAttr != null){
			this.webAttr.clear();
		}
	}
}
