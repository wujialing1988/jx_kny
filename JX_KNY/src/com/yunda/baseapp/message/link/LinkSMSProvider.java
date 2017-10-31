package com.yunda.baseapp.message.link;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.yunda.frame.baseapp.message.manager.ISMSProvider;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 发送短信管理实现类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-8-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class LinkSMSProvider implements ISMSProvider{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	/** 系统是否可以发送短信 */
	private static final boolean ENABLE_SMS = JXConfig.getInstance().isSmsEnable();
	/** 系统是否可以发送短信 */
	private static final String SMS_POSTFIX = StringUtil.nvlTrim(JXConfig.getInstance().getSmsPostfix(), "【运达科技】") ;	
//	/** 凌凯短信服务器数据库操作类  */
//	public static LinkSmsDao linkSmsDao;
//	
//	static{
//		Object obj = ContextLoader.getCurrentWebApplicationContext().getBean("linkSmsDao");
//		if(obj != null)	linkSmsDao = (LinkSmsDao)obj;
//	}
	private static LinkSmsDao getLinkSmsDao(){
		boolean isExist = ContextLoader.getCurrentWebApplicationContext().containsBean("linkSmsDao");
		if(isExist)	return (LinkSmsDao)ContextLoader.getCurrentWebApplicationContext().getBean("linkSmsDao");
		return null;
	}
	/**
	 * <li>说明：检查是否可以正常发送短信内容，true通过，false不通过
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-8-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param content 短信内容
	 * @return true通过，false不通过
	 */
	private boolean check(String content){
		if(!ENABLE_SMS)	return false;		//判断系统是否开启短信发送
		if(getLinkSmsDao() == null){
			String errInfo = "不能获取凌凯短信服务器数据访问对象，请检查applicationContext.xml文件中凌凯短信服务引擎配置是否正确。";
			logger.error(errInfo);
			throw new RuntimeException(errInfo);
		}
		if(StringUtil.nvlTrim(content, null) == null)	throw new RuntimeException("参数异常：content为空");
		return true;
	}
	/**
	 * <br/><li>说明：向集合对象的所有人员发送短信
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-8-16
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param content 短信内容，最大长度60
	 * @param employees 接收人员数组
	 */
	public void send(String content, OmEmployee... employees){
		if(employees == null)	throw new RuntimeException("参数异常：employees为空");
		if(!check(content))	return;
		
		List<String> phoneNumbers = new ArrayList<String>();
		for (OmEmployee employee : employees) {
			String phoneNum = StringUtil.nvlTrim(employee.getMobileno(), null);
			if(phoneNum == null)	continue;
			phoneNumbers.add(phoneNum);
		}
		if(phoneNumbers.size() > 0)	send(content, phoneNumbers.toArray(new String[ phoneNumbers.size() ]));
	}
	
	/**
	 * <br/><li>说明：向集合对象的所有人员发送短信
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-8-16
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param content 短信内容，最大长度60
	 * @param phoneNumbers 手机号码数组
	 */
	public void send(String content, String... phoneNumbers){
		if(phoneNumbers == null)	throw new RuntimeException("参数异常：content或phoneNumbers为空");
		if(!check(content))	return;
		
		content = content.trim() + SMS_POSTFIX;
		Date now = new Date();
		List<LinkMobileSend> list = new ArrayList<LinkMobileSend>();
		for (String phoneNum : phoneNumbers) {
			phoneNum = StringUtil.nvlTrim(phoneNum, null);
			if(phoneNum == null)	continue;
			LinkMobileSend lms = new LinkMobileSend();
			lms.setMobile(phoneNum);
			lms.setFlag(LinkMobileSend.NO_CONFIRM);
			lms.setContent(content);
			lms.setCreateDate(now);
			list.add(lms);
		}
		if(list.size() > 0)	getLinkSmsDao().getHibernateTemplate().saveOrUpdateAll(list);		
	}

}
