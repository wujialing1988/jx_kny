package com.yunda.frame.sso.action;

import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.base.filter.LoginPurviewCheckFilter;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.sso.entity.SingleSign;
import com.yunda.frame.sso.manager.SingleSignManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 单点登录控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-8-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@SuppressWarnings(value = "serial")
public class SingleSignAction extends JXBaseAction<SingleSign, SingleSign, SingleSignManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Resource
    private AcOperatorManager acOperatorManager;
    
    /**
     * 员工
     */
    @Resource
    private OmEmployeeSelectManager omEmployeeSelectManager;
    
    /**
     * 机构
     */
    private OmOrganizationManager omOrganizationManager;
    
    /**
     * <li>说明：异步登录
     *     前台URL示例：http://10.2.3.40:8080/CoreFrame/frame/sso/AsynchronousLogin.jsp?queryParams={"userId":"wangqian","queryUrl":"/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.jsp"}
     * <li>创建人：程锐
     * <li>创建日期：2015-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void asynchronousLogin() throws Exception {
        try {
            HttpServletRequest request = getRequest();
            HttpServletResponse response = getResponse();
            String encodeQueryParams = request.getParameter("queryParams");
            if (encodeQueryParams == null) {
                getResponse().sendRedirect(JXConfig.getInstance().getAppURL().concat("error.jsp"));
                return;
            }
            String str = URLDecoder.decode(encodeQueryParams, "UTF-8");            
            if (!str.contains("\""))             	
            	str = buildUrl(str);
            SingleSign sign = JSONUtil.read(str, SingleSign.class);
//            String url = this.manager.getUrl(sign);
            // 执行系统登录
            AcOperator acOperator = acOperatorManager.findLoginAcOprator(sign.getUserId());
            if (acOperator != null) {
                SystemContext.setAcOperator(acOperator);
                getSession().setAttribute(LoginPurviewCheckFilter.USERS_SESSION_NAME, acOperator);
                OmEmployee omEmp = omEmployeeSelectManager.findEmpByOperator(acOperator.getOperatorid());
                if (omEmp == null) {
                    omEmp = new OmEmployee();
                    omEmp.setOperatorid(acOperator.getOperatorid());
                } else {
                    OmOrganization omOrg = (OmOrganization) this.omOrganizationManager.findSingle(" FROM OmOrganization WHERE orgid = (select id.orgid from OmEmporg where id.empid = '" + omEmp.getEmpid() + "')");
                    getSession().setAttribute("org", omOrg);
                }
                // 设置用户所属段信息(2 表示段)
                if (omEmp != null) {
                    if (omEmp.getOrgid() != null) {
                        List<OmOrganization> omOrgDeps = this.omOrganizationManager.findLoginOmOrgList(omEmp.getOrgid(), "oversea");
                        if (omOrgDeps != null && omOrgDeps.size() > 0) {
                            getSession().setAttribute("orgDep", omOrgDeps.get(0));
                        }
                        // 将人员说在单位的每一级都放在session，以便后面使用
                        List<Object[]> empAllOrg = this.omOrganizationManager.getEmpAllOrg(omEmp.getOrgid() + "");
                        for (int i = empAllOrg.size() - 1; i >= 0; i--) {
                            getSession().setAttribute(empAllOrg.get(i)[2] + "", empAllOrg.get(i)[0] + "");
                            getSession().setAttribute(empAllOrg.get(i)[2] + "Name", empAllOrg.get(i)[1] + "");
                        }
                    }
                }
                getSession().setAttribute("emp", omEmp);
            }
            SystemContext.setSessionInfo(request.getSession());
            // 执行页面跳转
//            getResponse().sendRedirect(url);
            request.getRequestDispatcher(sign.getQueryUrl()).forward(request, response);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            getResponse().sendRedirect(JXConfig.getInstance().getAppURL().concat("error.jsp"));
        }
    }
    
    /**
     * <li>说明：针对chrome浏览器打开时会将传递的"号去掉的问题
     *          chrome浏览器解析后的url为http://10.2.3.40:8080/CoreFrame/frame/sso/AsynchronousLogin.jsp?queryParams={userId:wangqian,queryUrl:/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.jsp}
     * <li>创建人：程锐
     * <li>创建日期：2015-9-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param str url字符串
     * @return 加上"后的url字符串
     */
    private String buildUrl(String str) {
        str = str.substring(1, str.length() - 1);
        String[] array = str.split(",");
        StringBuffer sb = new StringBuffer();
        for (String s : array) {
            String[] array1 = s.split(":");
            for (String s1 : array1) {
                sb.append("\"");
                sb.append(s1.substring(0, s1.length())).append("\"");
                sb.append(":");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return "{".concat(sb.toString()).concat("}");
    }
    
    public OmOrganizationManager getOmOrganizationManager() {
        return omOrganizationManager;
    }
    
    public void setOmOrganizationManager(OmOrganizationManager omOrganizationManager) {
        this.omOrganizationManager = omOrganizationManager;
    }
    
}
