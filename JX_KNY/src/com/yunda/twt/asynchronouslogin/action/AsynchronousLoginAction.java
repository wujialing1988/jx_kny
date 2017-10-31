
package com.yunda.twt.asynchronouslogin.action;

import java.net.URLDecoder;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.base.filter.LoginPurviewCheckFilter;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.twt.asynchronouslogin.entity.TWTAsynchronmousLongin;
import com.yunda.twt.asynchronouslogin.manager.AsynchronousLoginManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 异步登陆action
 * <li>创建人：程锐
 * <li>创建日期：2015-4-9
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class AsynchronousLoginAction extends JXBaseAction<Object, Object, AsynchronousLoginManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Resource
    private AcOperatorManager acOperatorManager;
       
    /**
     * <li>方法说明：异步登录
     * <li>方法名称：asynchronousLogin
     * <li>
     * <li>return: String
     * <li>创建人：王利成
     * <li>创建时间：2014-09-17 上午11:06:46
     * <li>修改人： 程锐
     * <li>修改内容： 2015-04-09 修改为跳转至配置了台位图页面权限的url
     * <li>修改人： 汪东良
     * <li>修改内容： 2015-05-25 增加查询参数为台位的功能，并对以往功能进行重构
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void asynchronousLogin() throws Exception {
        try {
            String encodeQueryParams = getRequest().getParameter("queryParams");
            if(encodeQueryParams==null){
                getResponse().sendRedirect(JXConfig.getInstance().getAppURL().concat("error.jsp"));
                return;
            }
            String str = URLDecoder.decode(encodeQueryParams, "UTF-8");
            TWTAsynchronmousLongin  twtAsynchronmousLongin=  JSONUtil.read(str, TWTAsynchronmousLongin.class);
            String url = this.manager.getUrl(twtAsynchronmousLongin);
            // 执行系统登录
            AcOperator acOperator = acOperatorManager.findLoginAcOprator(twtAsynchronmousLongin.getUserId());
            if (acOperator != null) {
                SystemContext.setAcOperator(acOperator);
                getSession().setAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME, acOperator);
            }
            //执行页面跳转
            getResponse().sendRedirect(url);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            getResponse().sendRedirect(JXConfig.getInstance().getAppURL().concat("error.jsp"));
        }
    }
}
