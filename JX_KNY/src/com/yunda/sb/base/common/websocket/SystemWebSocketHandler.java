//package com.yunda.sb.base.common.websocket;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.jms.TextMessage;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Component;
////
///**
// * <li>标题: 设备管理信息系统
// * <li>说明: websocket消息推送管理器
// * <li>创建人：何涛
// * <li>创建日期：2016年9月14日
// * <li>修改人：
// * <li>修改日期：
// * <li>修改内容：
// * <li>版权: Copyright (c) 2008 运达科技公司
// * @author 信息系统事业部设备管理系统项目组
// * @version 3.0.1
// */
//@Component
//public class SystemWebSocketHandler implements WebSocketHandler {
//	
//    /** 
//     * 当前系统在线的用户session缓存
//     * key: empid
//     * value: List<WebSocketSession>
//     */
//    public static final Map<Long, List<WebSocketSession>> userMap = new HashMap<Long, List<WebSocketSession>>();
//	
//	/** 日志工具 */
//	private Logger logger = Logger.getLogger(getClass());
//	
//	@Override
//	public boolean supportsPartialMessages() {
//		return false;
//	}
//
//	/**
//	 * <li>说明：websocket连接创建后的事件监听
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月14日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param session WebSocketSession对象
//	 */
//	@Override
//	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//		logger.info("connect to the websocket success......");
////		session.sendMessage(new TextMessage("Server:connected OK!"));
//		
//		// 缓存在线用户的WebSocketSession对象，用于后期的消息推送
//		com.yunda.frame.core.UserData userData = (com.yunda.frame.core.UserData) session.getAttributes().get("users");
//		// 获取当前登录用户的empid
//		long empid = userData.getEmpid();
//		List<WebSocketSession> userList = userMap.get(empid);
//		if (null == userList) {
//			userList = new ArrayList<WebSocketSession>();
//			userMap.put(empid, userList);
//		}
//		if (!userList.contains(session)) {
//			userList.add(session);
//		}
//		logger.info(String.format("当前消息推送用户数：%d", userMap.size()));
//	}
//	
//	/**
//	 * <li>说明：websocket连接关闭后的事件监听
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月14日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param session WebSocketSession对象
//	 * @param cs CloseStatus对象
//	 */
//	@Override
//	public void afterConnectionClosed(WebSocketSession session, CloseStatus cs) throws Exception {
//		logger.info("websocket connection closed......");
//		com.yunda.frame.core.UserData userData = (com.yunda.frame.core.UserData) session.getAttributes().get("users");
//		long empid = userData.getEmpid();
//		List<WebSocketSession> userList = userMap.get(empid);
//		if (null != userList) {
//			if (userList.contains(session)) {
//				userList.remove(session);
//			}
//			if (userList.isEmpty()) {
//				userMap.remove(empid);
//			}
//		}
//			
//		logger.info(String.format("当前消息推送用户数：%d", userMap.size()));
//	}
//
//	/**
//	 * <li>说明：消息传输通道错误时的事件监听
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月14日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param session WebSocketSession对
//	 * @param thrwbl Throwable对象
//	 */
//	@Override
//	public void handleTransportError(WebSocketSession session, Throwable thrwbl) throws Exception {
//		if (session.isOpen()) {
//			session.close();
//		}
//		logger.info("websocket connection closed......");
//	}
//
//	/**
//	 * <li>说明：接收来自客户端的消息
//	 * <li>创建人：何涛
//	 * <li>创建日期：2016年9月14日
//	 * <li>修改人：
//	 * <li>修改内容：
//	 * <li>修改日期：
//	 * @param session WebSocketSession对象
//	 * @param wsm WebSocketMessage对象
//	 */
//	@Override
//	public void handleMessage(WebSocketSession session, WebSocketMessage<?> wsm) throws Exception {
//		TextMessage returnMessage = new TextMessage(wsm.getPayload() + " received at server");
//		session.sendMessage(returnMessage);
//	}
//	
//}
