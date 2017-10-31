/**
 * <li>文件名：BPSServiceClientUtil.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-7-1
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.flow.util;

import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.api.IBPSServiceClient;
import com.primeton.workflow.api.WFServiceException;

/**
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-7-1 
 * <li>修改人： 
 * <li>修改日期：
 */
public class BPSServiceClientUtil {
	public static IBPSServiceClient client;
	/**
	 * <li>方法名：getServiceClient
	 * <li>@return
	 * <li>返回类型：IBPSServiceClient
	 * <li>说明：获取BPS服务
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-7-1
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public static IBPSServiceClient getServiceClient() {
		try {
			if(client == null){
				client =  BPSServiceClientFactory.getDefaultClient();
			}
			return client ;
		} catch (WFServiceException e) {
			e.printStackTrace();
			return null;
		}
	}
}