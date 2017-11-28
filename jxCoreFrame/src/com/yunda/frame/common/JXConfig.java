package com.yunda.frame.common;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类提供检修系统相关配置信息，对应配置文件JXConfig.xml
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-7-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlRootElement(name="JXConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public final class JXConfig {
	/** 静态实例化对象 */
	@XmlTransient
	private static JXConfig instance;
	/** 用于数据同步，设置每条数据记录的站点标识（哈尔滨检修基地HEBJD） */
	@XmlElement
	private String synSiteID;
	/** 用于设置当前系统应用单位（段）的机构代码（哈尔滨检修基地HEBJXJD），1105=重庆西机务段的orgcode */
	@XmlElement
	private String overseaOrgcode;
	/** 当前应用单位机构名称(Constants类常量引用此值，因神木和基地不一样，所以添加此配置项), (哈尔滨大功率机车检修基地,中国神华神朔铁路分公司机务段) */
	@XmlElement
	private String orgRootName;
	/** 顶级机构名称(Constants类常量引用此值，因神木和基地不一样，所以添加此配置项), (铁路总公司,中国神华集团) */
	@XmlElement
	private String orgTopRootName;
	
	/** 设置报表上下文路径名称，目前在客户生产环境中：报表是独立的web应用（建议统一配置为ydReport），开发环境中使用默认配置 */
	@XmlElement
	private String reportContext;
	/** 设置sql-map工具类参数，sql语句是否使用缓存，默认true开启缓存 */
	@XmlElement
	private boolean sqlMapUseCache = true;
	/** 设置当前系统附件上传后，存放文件的根目录 */
	@XmlElement
	private String uploadPath;
	/** 设置上传文件体积上限，单位MB(100MB) */
	@XmlElement
	private String uploadMaxSize;
	/** 设置允许上传的文件类型 */
	@XmlElement
	private String uploadFileType;
	/** 设置允许上传文件类型的描述（可以上传所有类型的文件） */
	@XmlElement
	private String uploadFileDescription;
	/** 设置本系统使用的台位图名称(天津电力机车有限公司) */
	@XmlElement
	private String stationMapName;
	/** 设置本系统调用的台位图服务器地址 */
	@XmlElement
	private String stationMapURL;
	/** 设置本系统调用消息推送服务器地址 edit by easy*/
	@XmlElement
	private String messageURL;
	/** 设置台位图中计算出入厂时间差参数(单位:分钟) 默认为一天 60*24 = 1440 */
	@XmlElement
	private String stationMapTimeDifference;
	/** 设置本机检修系统应用访问地址 */
	@XmlElement
	private String appURL;
	/** 设置检修系统名称（天津机车检修管理系统,神朔机车检修管理系统） */
	@XmlElement
	private String appName;	
	/** 当前应用图片文件名称(不要后缀),(神木为smjx,基地为jcjx) */
	@XmlElement
	private String appPicName;
	/** 设置版权信息（成都运达科技股份有限公司 保留所有权利.），（中国铁路总公司运输局 信息技术中心 成都运达公司 沈阳科研所.保留所有权利.） */
	@XmlElement
	private String appFooter;
	/** 判断机车入库和出库的时间*/
	@XmlElement
	private String checkIntoFactoryTimeByCurrentTime;
	/** 判断机车本次入库时间和上次出库时间*/
	@XmlElement
	private String checkIntoFactoryTimeByOnceTime;
	/** 判断机车进入检修库的时间和入库时间*/
	@XmlElement
	private String checkIntoJxByIntoTime;
	/** 基础管理模块是否启用查询缓存**/
	@XmlElement
	private String useJcglQueryCache;
	/** 是否启用短信接口. 默认false禁用 */
	@XmlElement	
	private boolean smsEnable = false;
	/** 短信后缀，在消息内容最后追加如【运达科技】后缀字符 */
	@XmlElement	
	private String smsPostfix;
	/**提票派工是否发送短信通知*/
	@XmlElement
	private boolean smsFaultEnable;
	/**工单派工是否发送短信通知*/
	@XmlElement
	private boolean smsWorkCardEnable;
	/**派工中的工长派工是否发送短信通知*/
	@XmlElement
	private boolean smsHeadManEnable;
	/** 是否开启页面在线消息，false关闭，默认true开启 */
	@XmlElement
	private boolean onlineMessageEnable;
	/** 同车同位置提票判断时间(当前时间向之前推算的天数) */
	@XmlElement
	private String faultCountDays;
	/** 子系统 */
	@XmlElement
	private SubSystem subSystem;
	/** web应用部署的绝对路径 */
	@XmlElement
	private String webappsPath;
    
    /** 系统类型：整备系统:zb,检修系统：jx或为空 */
    @XmlElement
    private String systemType;
    
    /** 本段配属段编码 - 来源于总公司基础码表t_jcbm_jwdzd或者t_jcbm_jxdzd */
    @XmlElement
    private String did;
    
    /** 本段配属段名称 - 来源于总公司基础码表t_jcbm_jwdzd或者t_jcbm_jxdzd */
    @XmlElement
    private String dname;
    
    /** ipad二维码解析规则 **/ 
    @XmlElement
    private String qRCodeRule;
    
    /** 列检所编码 **/ 
    @XmlElement
    private String inspectionCode;
    
    /** 列检所编码 **/ 
    @XmlElement
    private String inspectionName;
    
    /** 系统登录后跳转页面地址 默认/frame/yhgl/MainFrameNew.jsp **/ 
    @XmlElement
    private String mainFrameUrl;
	
	/*<smsFaultEnable>false</smsFaultEnable>			<!-- 提票派工是否发送短信通知 -->
	<smsWorkCardEnable>false</smsWorkCardEnable>	<!-- 工单派工是否发送短信通知 -->
	<smsHeadManEnable>false</smsHeadManEnable>		<!-- 派工中的工长派工是否发送短信通知 -->*/
	
	
    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }
    
    public String getDname() {
        return dname;
    }
    
    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getFaultCountDays() {
		return faultCountDays;
	}

	private JXConfig(){}
	
	public static JXConfig getInstance(){
		if(instance != null)	return instance;
		instance = JAXB.unmarshal(Thread.currentThread().getContextClassLoader().getResourceAsStream("/JXConfig.xml"), JXConfig.class);		
		return instance;
	}

	public String getAppFooter() {
		return appFooter;
	}
	public String getAppName() {
		return appName;
	}
	public String getAppPicName() {
		return appPicName;
	}
	public String getAppURL() {
		return appURL;
	}
	public String getOrgRootName() {
		return orgRootName;
	}
	
	public String getOrgTopRootName() {
		return orgTopRootName;
	}

	public String getOverseaOrgcode() {
		return overseaOrgcode;
	}
	public String getReportContext() {
		return reportContext;
	}
	public boolean isSqlMapUseCache() {
		return sqlMapUseCache;
	}
	public String getStationMapName() {
		return stationMapName;
	}
	public String getStationMapTimeDifference() {
		return stationMapTimeDifference;
	}
	public String getStationMapURL() {
		return stationMapURL;
	}
	public String getSynSiteID() {
		return synSiteID;
	}
	public String getUploadFileDescription() {
		return uploadFileDescription;
	}
	public String getUploadFileType() {
		return uploadFileType;
	}
	public String getUploadMaxSize() {
		return uploadMaxSize;
	}
	public String getUploadPath() {
		return uploadPath;
	}
	public String getMessageURL() {
		return messageURL;
	}
	public String getCheckIntoFactoryTimeByCurrentTime() {
		return checkIntoFactoryTimeByCurrentTime;
	}
	public String getUseJcglQueryCache() {
		return useJcglQueryCache;
	}
	public String getCheckIntoFactoryTimeByOnceTime() {
		return checkIntoFactoryTimeByOnceTime;
	}
	public String getCheckIntoJxByIntoTime() {
		return checkIntoJxByIntoTime;
	}
	public String getSmsPostfix() {
		return smsPostfix;
	}

	public boolean isSmsEnable() {
		return smsEnable;
	}

	public boolean isSmsFaultEnable() {
		return smsFaultEnable;
	}

	public boolean isSmsHeadManEnable() {
		return smsHeadManEnable;
	}

	public boolean isSmsWorkCardEnable() {
		return smsWorkCardEnable;
	}

	public boolean isOnlineMessageEnable() {
		return onlineMessageEnable;
	}

	public SubSystem getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}
    /**
     * @return 获取web应用部署的绝对路径
     */
    public String getWebappsPath() {
        return webappsPath;
    }
    /**
     * @param webappsPath 设置web应用部署的绝对路径
     */
    public void setWebappsPath(String webappsPath) {
        this.webappsPath = webappsPath;
    }
    
    public String getSystemType() {
        return systemType;
    }
    
    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }
    
    public String getQRCodeRule() {
        return qRCodeRule;
    }

    public void setQRCodeRule(String codeRule) {
        qRCodeRule = codeRule;
    }

    
    public String getInspectionCode() {
        return inspectionCode;
    }

    
    public void setInspectionCode(String inspectionCode) {
        this.inspectionCode = inspectionCode;
    }

    
    public String getInspectionName() {
        return inspectionName;
    }

    
    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

	public String getMainFrameUrl() {
		return mainFrameUrl;
	}

	public void setMainFrameUrl(String mainFrameUrl) {
		this.mainFrameUrl = mainFrameUrl;
	}
    
    
    
}