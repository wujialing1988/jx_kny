package com.yunda.jxpz.smsnotify.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.jxpz.smsnotify.entity.SmsReceiver;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：SMSRECEIVER业务类,短信通知
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="smsReceiverManager")
public class SmsReceiverManager extends JXBaseManager<SmsReceiver, SmsReceiver>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>方法名：saveAndInsertNoticeIdx
	 * <li>@param receivers
	 * <li>@param noticeIdx
	 * <li>@throws BusinessException
	 * <li>返回类型：void
	 * <li>说明：派工短信通知记录时用于填入通知单主键信息
	 * <li>创建人：袁健
	 * <li>创建日期：2013-9-3
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void saveAndInsertNoticeIdx(List<SmsReceiver> receivers, String noticeIdx) throws BusinessException {
		for (SmsReceiver receiver : receivers) {
			receiver.setNoticeIdx(noticeIdx);
		}
		try {
			this.saveOrUpdate(receivers);
		} catch (NoSuchFieldException e) {
			ExceptionUtil.process(e,logger);
		}
	}
}