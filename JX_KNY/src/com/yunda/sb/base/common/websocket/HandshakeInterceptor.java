//package com.yunda.sb.base.common.websocket;
//
//import java.util.Map;
//
//import org.springframework.stereotype.Component;
//
///**
// * <li>标题: 设备管理信息系统
// * <li>说明: HandshakeInterceptor，websocket握手协议
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
//public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
//
//	@Override
//	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//		// 用于解决低版本浏览器不支持websocket协议而产生的错误
//		if(request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
//			request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
//		}
//		System.out.println("Before Handshake......");
//		return super.beforeHandshake(request, response, wsHandler, attributes);
//	}
//
//	@Override
//	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
//		System.out.println("After Handshake......");
//		super.afterHandshake(request, response, wsHandler, ex);
//	}
//	
//
//}
