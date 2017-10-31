package com.yunda.jx.common.homepage;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 首页配置信息属性实体
 * <li>创建人：谭诚
 * <li>创建日期：2013-9-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SynSites {

	private List<SynSite> synSite;

	/**
	 * <li>说明：
	 * <li>返回值： the synSite
	 */
	public List<SynSite> getSynSite() {
		return synSite;
	}

	/**
	 * <li>说明：
	 * <li>参数： synSite
	 */
	public void setSynSite(List<SynSite> synSite) {
		this.synSite = synSite;
	}

	
	
	
}
