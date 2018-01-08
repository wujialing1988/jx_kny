<jsp:directive.page import="com.yunda.jczb.zbpz.workplaceorganization.manager.JczbWorkPlaceOrganizationManager"/>
<jsp:directive.page import="com.yunda.frame.util.JSONUtil"/>
<%@page import="com.yunda.frame.common.Constants" %>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %><%-- 组织机构选择控件 --%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消息服务配置-接收方定义-多站点</title>
<script>
/** 目标类型 人员 */
var EMP = <%=Constants.EMP%>;    
/** 目标类型 组织机构 */
var ORG = <%=Constants.ORG%>;
/** 目标类型 组 */
var GROUP = <%=Constants.GROUP%>;
/** 目标类型 岗位 */
var POSITION = <%=Constants.POSITION%>;
/** 目标类型 职务 */
var DUTY = <%=Constants.DUTY%>;
/** 目标类型 角色 */
var ROLE = <%=Constants.ROLE%>;

</script>
<script type="text/javascript">
var siteID = <%=JSONUtil.write(JczbWorkPlaceOrganizationManager.getSiteIDByCurrEmpByUID())%>
var siteName = <%=JSONUtil.write(JczbWorkPlaceOrganizationManager.getSiteNameByCurrEmpByUID())%>
var siteID = <%=JSONUtil.write(JczbWorkPlaceOrganizationManager.getSiteIDByCurrEmpByUID())%>
var orgName = <%=JSONUtil.write(JczbWorkPlaceOrganizationManager.getOrgIdByCurrEmpByUID("name"))%>
var orgID = <%=JSONUtil.write(JczbWorkPlaceOrganizationManager.getOrgIdByCurrEmpByUID("id"))%>
</script>
<script language="javascript" src="<%=ctx %>/frame/resources/i18n/<%=browserLang %>/bases/i18n-lang-TestComponen.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script><%-- 组织机构选择控件 --%>
<script language="javascript" src="<%=ctx %>/frame/baseapp/message/MsgCfgFunction.js"></script>
<script language="javascript" src="<%=ctx %>/frame/baseapp/message/MsgCfgReceiveZB.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>