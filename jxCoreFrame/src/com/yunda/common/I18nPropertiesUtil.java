package com.yunda.common;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: I18n国际化后台语言处理工具
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-2-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class I18nPropertiesUtil{
    
    /**
     * baseName名称，系统默认使用i18n
     */
    private static final String BASE_NAME = "i18n" ;
    
    /**
     * <li>说明：获取当前系统的语言环境
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return Locale
     */
    public static Locale getLocale(){
        Locale locale = (Locale)ServletActionContext.getRequest().getSession().getAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
        if(locale == null){
            locale = Locale.getDefault();
        }
        return locale ;
    }
    
    /**
     * <li>说明：通过key获取国际化资源中的value
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param key 关键值
     * @return 返回值
     */
    public static String getValue(String key){
        Locale currentLocal = getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, currentLocal);
        return bundle.getString(key);
    }
    
    /**
     * <li>说明：获取带有占位符的国际化资源 at {0} in {1}
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 国际化资源
     */
    public static String getMessageFormat(String key,Object ... arguments){
        String pattern = getValue(key);
        return MessageFormat.format(pattern, arguments);
    }
    
    
}
