package com.yunda.frame.baseapp.externallink;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
public class Link {
	@XmlAttribute(name="imgUrl")
	private String imgUrl;
	@XmlAttribute(name="title")
	private String title;
	@XmlAttribute(name="openUrl")
	private String openUrl;
	@XmlAttribute(name="imgWidth")
	private String imgWidth;
	@XmlAttribute(name="imgHeight")
	private String imgHeight;
	@XmlAttribute(name="titleWidth")
	private String titleWidth;
	@XmlAttribute(name="titleHeight")
	private String titleHeight;
	
	public Link(){}
	
	/**
	 * <li>说明：
	 * <li>返回值： the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}
	/**
	 * <li>说明：
	 * <li>参数： imgUrl
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the openUrl
	 */
	public String getOpenUrl() {
		return openUrl;
	}
	/**
	 * <li>说明：
	 * <li>参数： openUrl
	 */
	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * <li>说明：
	 * <li>参数： title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <li>说明：
	 * <li>返回值： the imgHeight
	 */
	public String getImgHeight() {
		return imgHeight;
	}

	/**
	 * <li>说明：
	 * <li>参数： imgHeight
	 */
	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}

	/**
	 * <li>说明：
	 * <li>返回值： the imgWidth
	 */
	public String getImgWidth() {
		return imgWidth;
	}

	/**
	 * <li>说明：
	 * <li>参数： imgWidth
	 */
	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

	/**
	 * <li>说明：
	 * <li>返回值： the titleHeight
	 */
	public String getTitleHeight() {
		return titleHeight;
	}

	/**
	 * <li>说明：
	 * <li>参数： titleHeight
	 */
	public void setTitleHeight(String titleHeight) {
		this.titleHeight = titleHeight;
	}

	/**
	 * <li>说明：
	 * <li>返回值： the titleWidth
	 */
	public String getTitleWidth() {
		return titleWidth;
	}

	/**
	 * <li>说明：
	 * <li>参数： titleWidth
	 */
	public void setTitleWidth(String titleWidth) {
		this.titleWidth = titleWidth;
	}
	
	
}
