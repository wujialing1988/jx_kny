package com.yunda.jx.common.homepage;

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
public class SynSite {
	private String synSiteID;
	private String synSiteName;
	private Links links;
	
	/**
	 * <li>说明：
	 * <li>返回值： the links
	 */
	public Links getLinks() {
		return links;
	}
	/**
	 * <li>说明：
	 * <li>参数： links
	 */
	public void setLinks(Links links) {
		this.links = links;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the synSiteID
	 */
	public String getSynSiteID() {
		return synSiteID;
	}
	/**
	 * <li>说明：
	 * <li>参数： synSiteID
	 */
	public void setSynSiteID(String synSiteID) {
		this.synSiteID = synSiteID;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the synSiteName
	 */
	public String getSynSiteName() {
		return synSiteName;
	}
	/**
	 * <li>说明：
	 * <li>参数： synSiteName
	 */
	public void setSynSiteName(String synSiteName) {
		this.synSiteName = synSiteName;
	}
	
	
}
