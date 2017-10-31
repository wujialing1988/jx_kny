package com.yunda.frame.baseapp.externallink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.yunda.frame.common.JXConfig;
import com.yunda.frame.baseapp.externallink.SynSite;
import com.yunda.frame.baseapp.externallink.Link;
import com.yunda.frame.baseapp.externallink.SynSites;

import javax.xml.bind.JAXB;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 各基地、机务段的首页配置信息读取类
 * <li>创建人：谭诚
 * <li>创建日期：2013-9-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlRootElement(name="ExternalLink")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExternalLinkConfig {
	/** 静态实例化对象 */
	@XmlTransient
	private static ExternalLinkConfig instance = null;
	
	@XmlElement
	private SynSites synSites;
	
	private ExternalLinkConfig(){}
	
	public static ExternalLinkConfig getInstance(){
		if(instance == null)
			instance = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("/com/yunda/frame/baseapp/externallink/ExternalLink.xml"), ExternalLinkConfig.class);
		return instance;
	}
	/**
	 * <li>说明：获得与JXConfig.xml中站点配置参数相同的首页配置信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-9-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 无
	 * @return Map
	 * @throws 抛出异常列表
	 */
	public Map <String,Object> getExternalLinkContent(){
		Map <String,Object> map = null;
		Map <String,String> childMap = null;
		List <Map> list = null;
		try {
			String currentSynSiteID = JXConfig.getInstance().getSynSiteID(); //从JXConfig中获取当前配置文件中配置的站点ID
			for(SynSite site : getSynSites().getSynSite()){
				//在HomePage中查找与当前站点配置信息相同的站点主页信息
				if(currentSynSiteID.equals(site.getSynSiteID())){
					map = new HashMap <String,Object>();
					map.put("synSiteID", site.getSynSiteID());
					map.put("synSiteName", site.getSynSiteName());
					list = new ArrayList<Map>();
					if(site.getLinks()!=null&&site.getLinks().getLink()!=null){
						for(Link link : site.getLinks().getLink()){
							childMap = new HashMap<String,String>();
							childMap.put("imgUrl", link.getImgUrl());
							childMap.put("title", link.getTitle());
							childMap.put("openUrl", link.getOpenUrl());
							childMap.put("imgWidth", link.getImgWidth());
							childMap.put("imgHeight", link.getImgHeight());
							childMap.put("titleWidth", link.getTitleWidth());
							childMap.put("titleHeight", link.getTitleHeight());
							list.add(childMap);
						}
						map.put("linkList", list);
					}
				}
			}
			return map;
		} catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * <li>说明：
	 * <li>返回值： the synSites
	 */
	public SynSites getSynSites() {
		return synSites;
	}

	/**
	 * <li>说明：
	 * <li>参数： synSites
	 */
	public void setSynSites(SynSites synSites) {
		this.synSites = synSites;
	}
	
	
}
