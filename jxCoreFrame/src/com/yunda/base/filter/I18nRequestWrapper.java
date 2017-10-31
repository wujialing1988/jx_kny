package com.yunda.base.filter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.interceptor.I18nInterceptor;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: I18n拦截器
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-2-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class I18nRequestWrapper extends HttpServletRequestWrapper {

    private Locale locale = null ;
    
    public I18nRequestWrapper(HttpServletRequest request) {
        super(request);
        HttpSession session = request.getSession();
        locale = (Locale)session.getAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
    }
    
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if("Accept-Language".equals(name) && locale != null){
            value = locale.getLanguage() + "_" + locale.getCountry()
            + value.substring(6, value.length());
        }
        return value ;
    }

    @Override
    public Locale getLocale() {
        if(locale != null){
            return locale;
        }
        return super.getLocale();
    }
    
}
