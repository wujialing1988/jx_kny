package com.yunda.frame.baseapp.message.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.message.entity.Message;
import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.baseapp.message.entity.MsgCfgFunction;
import com.yunda.frame.baseapp.message.entity.MsgCfgReceive;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Message业务类,系统消息记录表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="messageManager")
public class MessageManager extends JXBaseManager<Message, Message> implements IMessageManager{
	/** 消息标题 */
	private static final String TITLE = "系统消息";
	/** 消息记录接收者业务类 */
	private MessageReceiverManager messageReceiverManager;
	/** 依赖注入：人员组件实例对象  */
	@Resource
	private IOmEmployeeManager omEmployeeManager;
	/** 依赖注入：组织机构组件实例对象 */
	@Resource
	private IOmOrganizationManager omOrganizationManager;	
	/** 消息服务配置-功能点定义 业务类 */
	@Resource
	private MsgCfgFunctionManager msgCfgFunctionManager;
	/** 消息服务配置-接收方定义 业务类 */
	@Resource
	private MsgCfgReceiveManager msgCfgReceiveManager;
	/** 在线消息发送服务提供者 */
	@Resource
	private OnlineMessageProvider onlineMessageProvider;
	/**
	 * <li>说明：删除消息接收者记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * @param Serializable... ids 消息接收者表主键idx
	 * @return void
	 */
	public void deleteByIds(Serializable... ids){
		daoUtils.removeByIds(ids, MessageReceiver.class, getModelIdName(MessageReceiver.class));
	}	
	/**
	 * <li>说明：获取当前操作员尚未接收的消息记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return List<Message> 消息列表
	 * @throws 
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List<Message> getNoReceivedMsg(){
		Long receiveid = SystemContext.getOmEmployee().getEmpid();
		String hql = "from Message where receiveTime is null and receiver='" + receiveid + "' order by sendtime desc";
		List<Message> list = (List<Message>)find(hql);
		return list;
	}
	/**
	 * <li>说明：通知服务端已接收消息，设置消息接收时间
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@Transactional
	public void received(Serializable... idxs){
		Date now = new Date();
		for (Serializable idx : idxs) {
			MessageReceiver receiver = messageReceiverManager.getModelById(idx);
			receiver.setReceiveTime(now);
			messageReceiverManager.saveOrUpdate(receiver);
		}
	}
	
	/**
	 * <li>说明：向集合对象的所有人员发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param OmEmployee sender 发送者
	 * @param Collection<OmEmployee> receivers 接收者集合
	 * @param String content 消息内容
	 * @return void
	 */
	public void send(OmEmployee sender, Collection<OmEmployee> receivers, String content){
		MessageSender ms = new MessageSender();
		if(sender != null){
			ms.setSender(sender.getEmpid());
			ms.setSenderName(sender.getEmpname());
		}
		ms.setContent(content);
		List<MessageReceiver> receives = new ArrayList<MessageReceiver>();		
		for (OmEmployee receivEmp : receivers) {
			MessageReceiver r = new MessageReceiver();
			r.setReceiver(receivEmp.getEmpid().toString());
			r.setReceiverName(receivEmp.getEmpname());
			receives.add(r);
		}
		send(ms, receives);
	}	
	/**
	 * <li>说明：系统为发送者，向集合对象的所有人员发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Collection<OmEmployee> collEmp 接收者集合
	 * @param String content 消息内容
	 * @return void
	 */
	public void send(Collection<OmEmployee> receivers, String content){
		send(null, receivers, content);
	}	
	/**
	 * <li>说明：向指定的某位人员发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param OmEmployee emp 发送者
	 * @param OmEmployee receiver 接收者
	 * @param String content 消息内容
	 * @return void
	 */
	public void send(OmEmployee sender, OmEmployee receiver, String content){
		MessageSender s = new MessageSender();
		s.setSender(sender.getEmpid());
		s.setSenderName(sender.getEmpname());
		s.setContent(content);
		MessageReceiver r = new MessageReceiver();
		r.setReceiver(receiver.getEmpid().toString());
		r.setReceiverName(receiver.getEmpname());
		send(s, r);
	}	
	/**
	 * <li>说明：系统作为发送者，向指定的某位人员发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容： 
	 * @param OmEmployee receiver 接收者
	 * @param String content 消息内容
	 * @return void
	 */
	public void send(OmEmployee receiver, String content){
		MessageReceiver r = new MessageReceiver();
		r.setReceiver(receiver.getEmpid().toString());
		r.setReceiverName(receiver.getEmpname());
		MessageSender s = new MessageSender();
		s.setContent(content);
		send(s, r);
	}	
	/**
	 * <li>说明：向集合对象的所有人员发送消息
	 * *注意：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容： 
	 * @param MessageSender sender 消息发送者
	 * @param Collection<MessageReceiver> receivers 消息接收者集合
	 * @param String content 消息内容
	 * @return void
	 */
	public void send(MessageSender sender, Collection<MessageReceiver> receivers){
		sender = setSender(sender);
		List<MessageReceiver> list = new ArrayList<MessageReceiver>();
		for (MessageReceiver receiver : receivers) {
			receiver = setReceiver(receiver);
			list.add(receiver);
		}
		onlineMessageProvider.send(sender, list);	//发送在线消息
	}
	/**
	 * <li>说明：向指定的消息接收者发送消息
	 * *注意：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容： 
	 * @param MessageSender sender 消息发送者
	 * @param Collection<MessageReceiver> receivers 消息接收者集合
	 * @param String content 消息内容
	 * @return void
	 */
	public void send(MessageSender sender, MessageReceiver receiver){
		List<MessageReceiver> receivers = new ArrayList<MessageReceiver>();
		receivers.add(receiver);
		send(sender, receivers);
	}	
	
	/**
	 * <li>说明：根据功能点编码，发送消息，接收者为功能点编码->通知对象（组织机构）所包括的所有人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String funCode 功能点编码
	 * @param String content 消息内容
	 * @return void
	 */
	public void sendByFunCode(String funCode, String content){
		MessageSender sender = new MessageSender();
		sender.setContent(content);
		sendByFunCode(sender, funCode);
	}
	/**
	 * <li>说明：根据功能点编码发送消息，接收者为功能点编码->通知对象（组织机构）所包括的所有人员
	 * *注意：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param MessageSender sender 消息发送者
	 * @param String funCode 功能点编码
	 * @return void
	 */
	public void sendByFunCode(MessageSender sender, String funCode){
		Set<OmEmployee> empSet = msgCfgFunctionManager.getEmployeeByFunCode(funCode);
		if(empSet == null || empSet.size() < 1)	return;
		List<MessageReceiver> receivers = new ArrayList<MessageReceiver>();
		for (OmEmployee employee : empSet) {
			MessageReceiver r = new MessageReceiver();
			r.setReceiver(employee.getEmpid().toString());
			r.setReceiverName(employee.getEmpname());
			receivers.add(r);
		}		
		send(sender, receivers);
	}
	/**
	 * <li>说明：根据功能点编码发送消息，接收者为功能点编码->通知对象（组织机构）所包括的所有人员，此外有附加规则ruleFlags
	 * 附加规则：
	 * 1)ruleFlags 和 MsgCfgReceive中的ruleFlag进行匹配，若相同则发送。
	 * 2)若MsgCfgReceive中的ruleFlag为null也发送。
	 * 3)增加该附加规则用于工序延误时的消息发送。
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param funCode 功能点编码
	 * @param content 消息内容
	 * @param ruleFlags 接收在线消息的规则标志
	 * @return void
	 */
	public void sendByFunCode(String funCode, String content, String... ruleFlags){
		MsgCfgFunction fun = msgCfgFunctionManager.findByFunCode(funCode);
		List<MsgCfgReceive> list = msgCfgReceiveManager.findByFunidx(fun.getIdx());
		if(list == null || list.size() < 1)	return;
		if(ruleFlags == null || ruleFlags.length < 1){
			send(msgCfgReceiveManager.getEmployee(list), content);
			return;
		}
		
		List<MsgCfgReceive> receiveList = new ArrayList<MsgCfgReceive>();
		int size = list.size();
		for (int i = size - 1; i >= 0; i--) {
			MsgCfgReceive msgRec = list.get(i);
			String ruleflag = msgRec.getRuleFlag();
			if(ruleflag == null){
				receiveList.add(msgRec);
				continue;
			} 
			for (String flag : ruleFlags) {
				if(ruleflag.equals(flag))	receiveList.add(msgRec);
			}					
		}
		send(msgCfgReceiveManager.getEmployee(receiveList), content);
	}
	/**
	 * <li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员，发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param OmEmployee sender 发送人员
	 * @param Collection<Long> orgids 组织机构orgid集合
	 * @param String content 消息内容
	 * @return void
	 */
	public void sendToOrg(OmEmployee sender, Collection<Long> orgids, String content){
		MessageSender s = new MessageSender();
		if(sender != null){
			s.setSender(sender.getEmpid());
			s.setSenderName(sender.getEmpname());			
		}		
		s.setContent(content);
		sendToOrg(s, orgids);
	}
	/**
	 * <li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员，发送消息
	 * *注意：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param MessageSender sender 消息发送者
	 * @param Collection<Long> orgids 组织机构orgid集合
	 * @return void
	 */
	public void sendToOrg(MessageSender sender, Collection<Long> orgids){
		Long[] orgidAry = new Long[ orgids.size() ];
		orgids.toArray(orgidAry);
		List<OmEmployee> empList = omEmployeeManager.findByOrgIds(orgidAry);
		if(empList == null || empList.size() < 1)	return;
		List<MessageReceiver> receivers = new ArrayList<MessageReceiver>();
		for (OmEmployee employee : empList) {
			MessageReceiver r = new MessageReceiver();
			r.setReceiver(employee.getEmpid().toString());
			r.setReceiverName(employee.getEmpname());
			receivers.add(r);
		}		
		send(sender, receivers);		
	}
	/**
	 * <li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员，发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param OmEmployee sender 发送人员
	 * @param Long orgid 组织机构orgid
	 * @param String content 消息内容
	 * @return void
	 */
	public void sendToOrg(OmEmployee sender, Long orgid, String content){
		List<Long> orgids = new ArrayList<Long>();
		orgids.add(orgid);
		sendToOrg(sender, orgids, content);
	}	
	/**
	 * <li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员发送消息，发送人为当前登录人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Long orgid 组织机构orgid
	 * @param String content 消息内容
	 * @return void
	 */
	public void sendToOrg(Long orgid, String content){
		sendToOrg(null, orgid, content);
	}	
	/**
	 * <li>说明：向指定组织机构（包括下属及最末层级机构）所属人员，发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param OmEmployee sender 发送人员
	 * @param Long orgid 组织机构orgid
	 * @param String content 消息内容
	 * @return void
	 */
	public void sendToOrgAndSuborg(OmEmployee sender, Long orgid, String content){
		MessageSender s = new MessageSender();
		if(sender != null){
			s.setSender(sender.getEmpid());
			s.setSenderName(sender.getEmpname());			
		}
		s.setContent(content);
		sendToOrgAndSuborg(s, orgid);
	}	
	/**
	 * <li>说明：向指定组织机构（包括下属及最末层级机构）所属人员发送消息，发送人为当前登录人员
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Long orgid 组织机构orgid
	 * @param String content 消息内容
	 * @return void
	 */
	public void sendToOrgAndSuborg(Long orgid, String content){
		sendToOrgAndSuborg(null, orgid, content);
	}
	/**
	 * <li>说明：向指定组织机构（包括下属及最末层级机构）所属人员，发送消息
	 * *注意：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param MessageSender sender 消息发送者
	 * @param Long orgid 组织机构orgid
	 * @return void
	 */
	public void sendToOrgAndSuborg(MessageSender sender, Long orgid){
		List<OmOrganization> orgList = omOrganizationManager.findAllChilds(orgid, true);
		Long[] orgids = new Long[ orgList.size() ];
		for (int i = 0; i < orgids.length; i++) {
			orgids[ i ] = orgList.get(i).getOrgid();
		}
		List<OmEmployee> receivEmps = omEmployeeManager.findByOrgIds(orgids);
		if(receivEmps == null || receivEmps.size() < 1)	return;
		
		List<MessageReceiver> receivers = new ArrayList<MessageReceiver>();
		for (OmEmployee employee : receivEmps) {
			MessageReceiver r = new MessageReceiver();
			r.setReceiver(employee.getEmpid().toString());
			r.setReceiverName(employee.getEmpname());
			receivers.add(r);
		}
		send(sender, receivers);		
	}
	/**
	 * <li>说明：检查消息发送者接收对象，补充设置接收者姓名
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param MessageSender sender 消息发送者
	 * @return MessageSender
	 */
	private MessageReceiver setReceiver(MessageReceiver receiver){
		if(receiver.getReceiver() == null)	throw new RuntimeException("没有设置接收者的empid");
		if(receiver.getReceiverName() == null){
			OmEmployee receive = (OmEmployee)daoUtils.get(receiver.getReceiver(), OmEmployee.class);
			receiver.setReceiverName(receive.getEmpname());
		}
		return receiver;
	}
	/**
	 * <li>说明：如果没有设置消息发送者，则设置为当前登录用户或系统
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param MessageSender sender 消息发送者
	 * @return MessageSender
	 */
	private MessageSender setSender(MessageSender sender){
		if(sender.getSender() == null){
			OmEmployee emp = SystemContext.getOmEmployee();
			if(emp != null){
				sender.setSender(emp.getEmpid());
				sender.setSenderName(emp.getEmpname());				
			} else {
				sender.setSender(-1L);
				sender.setSenderName("系统通知");				
			}
		} else if(sender.getSenderName() == null) {
			OmEmployee send = (OmEmployee)daoUtils.get(sender.getSender(), OmEmployee.class);
			sender.setSenderName(send.getEmpname());		
		}
		sender.setSendTime(new Date());
		if(sender.getTitle() == null)	sender.setTitle(TITLE);
		if(sender.getSendMode() == null)	sender.setOnlineMode();
		if(sender.getShowPageMode() == null)	sender.setShowPageMode(MessageSender.OPEN_TAB);
		return sender;
	}	
	
	public MessageReceiverManager getMessageReceiverManager() {
		return messageReceiverManager;
	}
	public void setMessageReceiverManager(
			MessageReceiverManager messageReceiverManager) {
		this.messageReceiverManager = messageReceiverManager;
	}

}