package com.yunda.jxpz.utils;

import com.yunda.Application;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.systemconfig.manager.SystemConfigManager;


public class SystemConfigUtil {
    /**
     * <li>方法说明：获取系统配置项值 
     * <li>方法名称：getValue
     * <li>@param key
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-7-24 下午03:37:03
     * <li>修改人：
     * <li>修改内容：
     */
    public static String getValue(String key){
        
        fillData();
        return StringUtil.nvl(SystemConfigManager.getValue(key)); //值为空返回空字符串
    }
    
    /**
     * <li>方法说明：填充数据 
     * <li>方法名称：fillData
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-25 下午04:29:57
     * <li>修改人：
     * <li>修改内容：
     */
    private static void fillData() {
        if(SystemConfigManager.configIsNull()){
            SystemConfigManager m = (SystemConfigManager)Application.getSpringApplicationContext().getBean("systemConfigManager");
            m.fillConfig();
        }
    }
    
    /**
     * <li>方法说明：根据键获取值，如果值为空返回第二个参数 
     * <li>方法名称：getValue
     * <li>@param key
     * <li>@param defaultValue
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-4-25 下午04:30:06
     * <li>修改人：
     * <li>修改内容：
     */
    public static String getValue(String key, String defaultValue){
        
        fillData();
        String val = SystemConfigManager.getValue(key);
        if(val == null){
            return defaultValue;
        }
        return val;
    }
    
    /**
     * <li>方法说明：匹配一个键值是否相等（值不区分大小写） 
     * <li>方法名称：getMattch
     * <li>@param key
     * <li>@param value (null返回false)
     * <li>@return
     * <li>return: boolean
     * <li>创建人：张凡
     * <li>创建时间：2014-4-8 下午03:31:43
     * <li>修改人：
     * <li>修改内容：
     */
    public static boolean getMattch(String key, String value){
        if(value == null){
            return false;
        }
        return value.equalsIgnoreCase(getValue(key));
    }
}
