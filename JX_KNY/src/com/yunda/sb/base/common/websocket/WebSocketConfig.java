//package com.yunda.sb.base.common.websocket;
//
//import org.apache.log4j.Logger;
//
///**
// * <li>标题: 机车设备管理信息系统
// * <li>说明：WebSocketConfig，消息推送适配器配置
// * <li>创建人：黄杨
// * <li>创建日期：2017-5-15
// * <li>修改人: 
// * <li>修改日期：
// * <li>修改内容：
// * <li>版权: Copyright (c) 2008 运达科技公司
// * @author 系统集成事业部设备系统项目组
// * @version 1.0
// */
//@Configuration
//@EnableWebMvc
//@EnableWebSocket
//public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
//
//	/** 日志工具 */
//	private Logger logger = Logger.getLogger(getClass());
//
//	/** websocket连接URL */
//	public static final String webSocketURL = "/websck";
//
//	/** sockjs模拟连接URL */
//	public static final String sockJsURL = "/sockjs/websck/info";
//
//	@Override
//	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		// 适用 ws://localhost:8080/SBGL3/websck
//		registry.addHandler(systemWebSocketHandler(), webSocketURL).addInterceptors(new HandshakeInterceptor());
//		logger.info(String.format("注册消息推送服务请求URL[%s]成功！", webSocketURL));
//
//		// 适用 http://localhost:8080/SBGL3/sockjs/websck
//		registry.addHandler(systemWebSocketHandler(), sockJsURL).addInterceptors(new HandshakeInterceptor()).withSockJS();
//		logger.info(String.format("注册消息推送服务请求URL[%s]成功！", sockJsURL));
//	}
//
//	@Bean
//	public WebSocketHandler systemWebSocketHandler() {
//		return new SystemWebSocketHandler();
//	}
//
//}
