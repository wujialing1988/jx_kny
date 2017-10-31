//package com.yunda.sb.base.common.manager;
//
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import java.util.Map.Entry;
//
//import javax.annotation.Resource;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Service;
//
//import com.yunda.base.context.SystemContext;
//import com.yunda.common.BusinessException;
//import com.yunda.frame.common.Constants;
//import com.yunda.frame.common.JXBaseManager;
//import com.yunda.frame.util.ExceptionUtil;
//import com.yunda.frame.util.JSONUtil;
//import com.yunda.frame.util.StringUtil;
//import com.yunda.frame.yhgl.entity.AcMenu;
//import com.yunda.frame.yhgl.entity.OmEmployee;
//import com.yunda.frame.yhgl.entity.OmOrganization;
//import com.yunda.frame.yhgl.manager.EmployeeManager;
//import com.yunda.frame.yhgl.manager.OrganizationManager;
//import com.yunda.sb.base.common.entity.Menu;
//import com.yunda.sb.base.common.entity.SystemMessage;
//import com.yunda.sb.base.common.websocket.SystemWebSocketHandler;
//
///**
// * <li>标题: 设备管理信息系统
// * <li>说明: SystemMessage管理器，数据表：T_SYSTEM_MESSAGE
// * <li>创建人：何涛
// * <li>创建日期：2016年9月18日
// * <li>修改人：
// * <li>修改日期：
// * <li>修改内容：
// * <li>版权: Copyright (c) 2008 运达科技公司
// * @author 信息系统事业部设备管理系统项目组
// * @version 3.0.1
// */
//@Service(value = "systemMessageManager")
//public class SystemMessageManager extends JXBaseManager<SystemMessage, SystemMessage> {
//	
//	/** 日志工具 */
//	private Logger logger = Logger.getLogger(getClass());
//	
//	/** websocket消息推送管理器 */
//	@Resource
//	private SystemWebSocketHandler systemWebSocketHandler;
//	
//	@Resource
//	private EmployeeManager employeeManager;
//	
//	/** 机构业务处理类 */
//	@Resource
//	private OrganizationManager organizationManager;
//	
//	/**
//	 * <li>说明：保存并推送消息
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月18日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param t 系统消息实体对象
//	 * @throws NoSuchFieldException 
//	 * @throws BusinessException 
//	 */
//	public void saveAndPushMessage(SystemMessage t) throws BusinessException, NoSuchFieldException {
//		// 验证相同的业务记录只产生一条系统消息
//		SystemMessage entity = this.getModel(t.getBusinessIdx(), t.getContent());
//		try {
//			if (null == entity) {
//				// Modified by hetao on 2016-11-11 增加查询条件isleaf，保证获取的菜单为叶子菜单
//				String hql = "From AcMenu Where menuname = ? And isleaf = 'y'";
//				AcMenu menu = (AcMenu) this.daoUtils.findSingle(hql, t.getTitle());
//				if (null != menu) {
//					t.menuId(menu.getMenuid()) 				// 功能菜单id
//					.menuSeq(menu.getMenuseq()) 		// 功能菜单序列
//					.url(menu.getMenuaction()); 		// 功能菜单URL
//				}
//				t.pushTimes(1);
//				t.setCreateTime(Calendar.getInstance().getTime());
//				this.saveOrUpdate(t);
//				this.pushMessage(t);
//			} else {
//				entity.adaptFrom(t);
//				entity.setPushTimes(entity.getPushTimes() + 1);
//				this.saveOrUpdate(entity);
//				this.pushMessage(entity);
//			}
//		} catch (IOException e) {
//			ExceptionUtil.process(e, logger);
//			throw new BusinessException(e);
//		}
//	}
//	
//	/**
//	 * <li>说明：根据业务idx主键获取系统消息实体对象
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月18日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param businessIdx 业务idx主键
//	 * @param content 消息内容
//	 * @return 系统消息实体对象
//	 */
//	private SystemMessage getModel(String businessIdx, String content) {
//		String hql = "From SystemMessage Where businessIdx = ? And content = ? And recordStatus = 0";
//		return (SystemMessage) this.daoUtils.findSingle(hql, businessIdx, content);
//	}
//	
//	/**
//	 * <li>说明：给所有在线（除自己外）用户推送系统消息
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月14日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param t 系统消息实体对象
//	 * @throws IOException
//	 */
//	public void pushMessage(SystemMessage t) throws IOException {
//		if (SystemWebSocketHandler.userMap.isEmpty()) {
//			return;
//		}
//		TextMessage msg = new TextMessage(JSONUtil.write(t));
//		Set<Entry<Long, List<WebSocketSession>>> entrySet = SystemWebSocketHandler.userMap.entrySet();
//		for (Iterator<Entry<Long, List<WebSocketSession>>> i = entrySet.iterator(); i.hasNext();) {
//			Entry<Long, List<WebSocketSession>> next = i.next();
//			List<WebSocketSession> userList = next.getValue();
//			if (null == userList || userList.isEmpty()) {
//				continue;
//			}
//			Long empid = next.getKey();
//			
//			// 不给自己推送消息
//			if (SystemContext.getUserData().getEmpid().equals(empid)) {
//				continue;
//			}
//			
//			OmEmployee emp = this.employeeManager.getModelById(empid);
//			if (null != t.getAuthVerifyMode()) {
//				// 操作者权限控制，控制依据等同于“用户权限菜单”
//				// 验证消息推送的对象班组
//				if (!hasMenuAuthority(emp, t) && !isReceivedOrg(emp, t)) {
//					continue;
//				}
//			}
//
//			// 推送消息
//			for (Iterator<WebSocketSession> j = userList.iterator(); j.hasNext();) {
//				WebSocketSession session = j.next();
//				if (session.isOpen()) {
//					session.sendMessage(msg);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * <li>说明：验证系统操作者是否有菜单权限获取系统推送的消息
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月19日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param empid 人员id
//	 * @param t 系统消息实体对象
//	 * @return true：有权限获取系统推送消息，false：无权限获取系统推送消息
//	 */
//	private boolean hasMenuAuthority(OmEmployee emp, SystemMessage t) {
//		if (null == emp || (SystemMessage.AUTH_VERIFY_MODE_MENUSEQ & t.getAuthVerifyMode().intValue()) < 0) {
//			return false;
//		}
//		Long operatorid = emp.getOperatorid();
//		// 查询操作者所有的权限菜单序列
//		List<?> result = this.daoUtils.executeSqlQuery("SELECT getAllMenuSeqs(" + operatorid + ")");
//		if (null == result || result.isEmpty()) {
//			return false;
//		}
//		String[] split = result.get(0).toString().split(Constants.COMMA);
//		if (0 >= split.length) {
//			return false;
//		}
//		for (String s : split) {
//			if (s.equals(t.getMenuSeq())) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	/**
//	 * <li>说明：验证消息推送的对象班组
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月19日
//	 * <li>修改人：何涛
//	 * <li>修改内容：2016年10月17日
//	 * <li>修改日期：修改接收班组的验证规则，只有人所在班组隶属月消息推送的目标班组，该人即有权限接收到该消息
//	 * @param empid 人员id
//	 * @param t 系统消息实体对象
//	 * @return true：有权限获取系统推送消息，false：无权限获取系统推送消息
//	 */
//	private boolean isReceivedOrg(OmEmployee emp, SystemMessage t) {
//		if (StringUtil.isNullOrBlank(t.getReceiveOrgIds()) || (SystemMessage.AUTH_VERIFY_MODE_ORGID & t.getAuthVerifyMode().intValue()) < 0) {
//			return false;
//		}
//		// 消息接收班组
//		String[] split = t.getReceiveOrgIds().split(Constants.COMMA);
//		if (0 >= split.length) {
//			return false;
//		}
//		for (String receiveOrgId : split) {
//			// 人员所在班组
//			OmOrganization org = organizationManager.getModelById(emp.getOrgid());
//			OmOrganization receiveOrg = organizationManager.getModelById(receiveOrgId);
//			if (null == org || null == receiveOrg) {
//				continue;
//			}
//			if (org.getOrgseq().startsWith(receiveOrg.getOrgseq())) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	public static void main(String[] args) {
//		System.out.println(Menu.提票工长派工.toString());
//	}
//	
//}
