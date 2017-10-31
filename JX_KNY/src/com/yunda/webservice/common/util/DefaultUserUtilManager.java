package com.yunda.webservice.common.util;

import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.jxpz.utils.SystemConfigUtil;

/**
 * 
 * <li>标题: 检修管理系统默认用户管理工具类
 * <li>说明: 默认用户帮助类，用于获取系统默认用户，当系统没有登录却需要当前用户信息时可使用此类；
 * 			例如台位图自动进行机车入库时：台位图端通过调用web端入库接口进行机车入库，无法获取到当前用户时，可通过此帮助类进行当前默认用户设置；
 * <li>创建人：汪东良
 * <li>创建日期：2014-7-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="defaultUserUtilManager")
public class DefaultUserUtilManager {
	/**
	 * 
	 * <li>说明：设置默认操作者：
	 * 		通过SystemContext.getAcOperator()获取当前操作者currentOperactor
	 * 			if（currentOperactor为空）{
	 * 				从系统配置项（ck.system.defaultUserID）中获取默认defaultUserID；
	 * 				if(defaultUserID为空){
	 * 					设置defaultUserID为"sysadmin";
	 * 				}
	 * 				根据defaultUserID获取操作者信息；
	 * 				调用SystemContext.setAcOperator(AcOperator)设置用户操作者信息；
	 * 			}
	 * 		
	 * <li>创建人：汪东良
	 * <li>创建日期：2014-7-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 无	
	 * @return 无
	 */
	public static void setDefaultOperator(){
	    	AcOperator objAcOperator = SystemContext.getAcOperator();
	    	if(objAcOperator == null){
	    		String  strDefaultUserID = SystemConfigUtil.getValue("ck.system.defaultUserID");
	    		if(StringUtil.isNullOrBlank(strDefaultUserID)){
	    			strDefaultUserID = "sysadmin";
	    		}
	        	AcOperatorManager objAcOperatorManager =(AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
	        	if(objAcOperatorManager!=null){
	        		objAcOperator = objAcOperatorManager.findLoginAcOprator(strDefaultUserID);
	        		SystemContext.setAcOperator(objAcOperator);
	        	}
	    	}
	    }
}
