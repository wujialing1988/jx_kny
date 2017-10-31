package com.yunda.frame.baseapp.message.manager;

import java.util.Collection;

import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.yhgl.entity.OmEmployee;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 消息管理业务接口
 * <br/><li>创建人：刘晓斌
 * <br/><li>创建日期：2013-7-24
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：消息功能组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IMessageManager messageManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据功能点编码发送消息，接收者为功能点编码->通知对象（组织机构）所包括的所有人员
 * &nbsp; messageManager.sendByFunCode("SCDD_DelayWarning", msg.toString());
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IMessageManager{
	
	/**
	 * <br/><li>说明：向集合对象的所有人员发送消息
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人：
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 发送者
	 * @param receivers 接收者集合
	 * @param content 消息内容
	 */
	public void send(OmEmployee sender, Collection<OmEmployee> receivers, String content);
	/**
	 * <br/><li>说明：系统为发送者，向集合对象的所有人员发送消息
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param receivers 接收者集合
	 * @param content 消息内容
	 */
	public void send(Collection<OmEmployee> receivers, String content);
	/**
	 * <br/><li>说明：向指定的某位人员发送消息
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 发送者
	 * @param receiver 接收者
	 * @param content 消息内容
	 */
	public void send(OmEmployee sender, OmEmployee receiver, String content);
	/**
	 * <br/><li>说明：系统作为发送者，向指定的某位人员发送消息
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param receiver 接收者
	 * @param content 消息内容
	 */
	public void send(OmEmployee receiver, String content);
	/**
	 * <br/><li>说明：向集合对象的所有人员发送消息
	 * <br/><li><font color=red>注*：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)</font>
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param sender 消息发送者
	 * @param receivers 消息接收者集合
	 */
	public void send(MessageSender sender, Collection<MessageReceiver> receivers);
	/**
	 * <br/><li>说明：向指定的消息接收者发送消息
	 * <br/><li><font color=red>注*：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)</font>
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param sender 消息发送者
	 * @param receiver 消息接收者
	 */
	public void send(MessageSender sender, MessageReceiver receiver);
	
	/**
	 * <br/><li>说明：根据功能点编码发送消息，接收者为功能点编码->通知对象（组织机构）所包括的所有人员
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param funCode 功能点编码
	 * @param content 消息内容
	 */
	public void sendByFunCode(String funCode, String content);
	/**
	 * <br/><li>说明：根据功能点编码发送消息，接收者为功能点编码->通知对象（组织机构）所包括的所有人员
	 * <br/><li><font color=red>注*：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)</font>
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 消息发送者
	 * @param funCode 功能点编码
	 */
	public void sendByFunCode(MessageSender sender, String funCode);
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
	public void sendByFunCode(String funCode, String content, String... ruleFlags);	
	/**
	 * <br/><li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员，发送消息
	 * <br/><li><font color=red>注*：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)</font>
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 发送人员
	 * @param orgids 组织机构orgid集合
	 * @param content 消息内容
	 */
	public void sendToOrg(OmEmployee sender, Collection<Long> orgids, String content);
	/**
	 * <br/><li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员，发送消息
	 * <br/><li><font color=red>注*：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)</font>
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 消息发送者
	 * @param orgids 组织机构orgid集合
	 */
	public void sendToOrg(MessageSender sender, Collection<Long> orgids);	
	/**
	 * <br/><li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员，发送消息
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 发送人员
	 * @param orgid 组织机构orgid
	 * @param content 消息内容
	 */
	public void sendToOrg(OmEmployee sender, Long orgid, String content);
	/**
	 * <br/><li>说明：向指定组织机构（不包括子机构及子机构以下）所属人员发送消息，发送人为当前登录人员
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgid 组织机构orgid
	 * @param content 消息内容
	 */
	public void sendToOrg(Long orgid, String content);
	/**
	 * <br/><li>说明：向指定组织机构（包括下属及最末层级机构）所属人员，发送消息
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 发送人员
	 * @param orgid 组织机构orgid
	 * @param content 消息内容
	 */
	public void sendToOrgAndSuborg(OmEmployee sender, Long orgid, String content);
	/**
	 * <br/><li>说明：向指定组织机构（包括下属及最末层级机构）所属人员，发送消息
	 * <br/><li><font color=red>注*：如果要实现用户点击在线消息后跳转到功能页面，必须设置MessageSender(pageTitle,url,showPageMode)</font>
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param sender 消息发送者
	 * @param orgid 组织机构orgid
	 */
	public void sendToOrgAndSuborg(MessageSender sender, Long orgid);	
	/**
	 * <br/><li>说明：向指定组织机构（包括下属及最末层级机构）所属人员发送消息，发送人为当前登录人员
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgid 组织机构orgid
	 * @param content 消息内容
	 */
	public void sendToOrgAndSuborg(Long orgid, String content);
}
