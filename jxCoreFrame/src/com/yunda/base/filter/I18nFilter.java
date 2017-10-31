package com.yunda.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: I18n国际化语言过滤器
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-2-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class I18nFilter implements Filter {

    public void destroy() {
        
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest)req;
        I18nRequestWrapper wrapper = new I18nRequestWrapper(r);
        filterChain.doFilter(wrapper, resp);
    }

    public void init(FilterConfig arg0) throws ServletException {
        
    }
    
}
