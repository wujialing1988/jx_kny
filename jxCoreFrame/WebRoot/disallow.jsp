<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--<%@ taglib uri="/struts-tags" prefix="s"%> --%>
<% 
	String ctx = request.getContextPath(); 	
%>
<!-- 未登录 -->
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-base-debug.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-all-debug.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-zh_cn.js"></script>
<script type="text/javascript">
	<%--var url = '<s:property value="@com.yunda.base.filter.LoginPurviewCheckFilter@RELOGIN_JSP"/>'; //注释原取值方式，多用户使用不同登录页面登录时，会造成登录页面不正确--%>
	var url = '${sessionScope.reloginJsp}';<%--从Session中获取登录页面URL--%>
	var currentLogin = eval('${sessionScope.currentLogin}');<%-- 接收参数，确定是否在当前页面范围转向到登录页面 --%>
	var current = currentLogin ? this : top;

	Ext.onReady(function(){
	<%-- top.Ext.Msg.alert('提示','${param.message}',function(){
	 		current.maskDocAll(Ext.getBody());
	 		if(current.prevUrl)
				current.document.location.href = current.prevUrl;
			else
				current.document.location.href = '${pageContext.request.contextPath}' + url.toLocaleLowerCase();
		 	//closeWin();
	 })--%>
	 	//Ext.Msg.confirm('警告','您试图访问某个被权限控制的网页，点击【确定】将会返回到登录页面，是否继续？,function(btn){if(btn=='yes'){document.location.replace('${pageContext.request.contextPath}/login!relogin.action');}}' 
	 	current.document.location.href = '${pageContext.request.contextPath}'+url.toLocaleLowerCase();
	 	
	});
</script>
