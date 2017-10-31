<%@ page import="com.yunda.frame.common.JXConfig" %>
<%@ page import="com.yunda.frame.common.SubSystem" %>
<%@ page import="com.yunda.frame.common.SubSysInfo" %>
<%@ page import="com.yunda.frame.util.StringUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//应用程序根目录
	String ctx = request.getContextPath();
	Map <String, Object> map = com.yunda.frame.baseapp.externallink.ExternalLinkConfig.getInstance().getExternalLinkContent();//根据配置文件, 动态生成页面
	
	/******根据请求参数中的appid（默认为JCJX），获取appid指定的子系统名称****/
	String appId = (String)session.getAttribute("appId");//目标子系统id
	String appname = JXConfig.getInstance().getAppName();
	String systemType = JXConfig.getInstance().getSystemType();
	String queryString = request.getQueryString(); //获得请求参数
	if(!StringUtil.isNullOrBlank(queryString)&&queryString.toUpperCase().startsWith("APPID")){
		SubSystem sys = JXConfig.getInstance().getSubSystem(); //读取JXConfig配置文件，获取子系统信息
		if(sys!=null){
			List<SubSysInfo> list2 = sys.getSubSysInfo();
			//遍历子系统
			for(SubSysInfo info : list2){
				if(appId.equals(info.getAppid().toUpperCase())){
					appId = info.getAppid().toUpperCase();
					appname = info.getAppname(); //当前URL参数中的APPID与配置文件中某个子系统匹配，则获取该子系统名称
				}
			}
		}
	}
	/**************************************************************/
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>系统主框架页</title>
	<link href="<%=ctx%>/frame/yhgl/css/welcome-page.css" rel="stylesheet" type="text/css" />
	<link href="<%= ctx %>/jsp/jx/jxgc/workplanmanage/VTrainAccessAccount.css" rel="stylesheet" type="text/css">
	<link href="<%=ctx%>/frame/yhgl/css/top.css" rel="stylesheet" type="text/css" />
	<link href="<%=ctx%>/frame/yhgl/css/main-page.css" rel="stylesheet" type="text/css" />
	
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-base.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/ext-all.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-zh_cn.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/js/MyJson.js"></script>
	<script language="javascript" src="<%=ctx%>/frame/resources/ext-3.4.0/Ext-yunda.js"></script>
	
	<script language="javascript">
	var ctx = "<%=ctx%>";
	var systemType = "<%=systemType%>";
	
	//MainFrame
	
	Ext.Ajax.request({
		url: ctx + "/todoJob!getInfo.action",
		success: function(response, options){
		       var result = Ext.util.JSON.decode(response.responseText);
		       if (result.errMsg == null && result.success == true) {
		       		document.getElementById("todoJobDiv").innerHTML = result.info;
		       } else {
		              alertFail(result.errMsg);
		       }
		},
		failure: function(response, options){
		       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		}
	});
	
	if (systemType == 'zb') {
		Ext.Ajax.request({
			url: ctx + '/trainAccessAccount!statistics.action',
			//请求成功后的回调函数
		    success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        
		        if (result.errMsg == null) {       //操作成功     
	        		var html = [];
		        	for(var prop in result) {
		        		html.push('<div class="infoPanel" style="box-shadow:2px 2px 2px ' + prop.split(',')[1] + ';border-color:'+ prop.split(',')[1] +';">');
		        		html.push('<div class="infoPanel_title " ' + 'style="background-color:'+ prop.split(',')[1] +';"' +'>');
		        		html.push('<span class="status">');
	        			html.push(prop.split(',')[0]);							// 状态名称
		        		html.push('</span>');
		        		html.push('<span class="number">');
		        		html.push(result[prop].length);				// 机车统计数量
		        		html.push('</span>');
		        		html.push('</div>');
			        	for (var i = 0; i < result[prop].length; i++) {
			        		html.push('<div class="train_pic_bg infoPanel_items">');
			        		var displayInfo = result[prop][i];
			        		// 车型车号
			        		var content = displayInfo.substring(displayInfo.indexOf(',') + 1);
			        		html.push(content);							        		
			        		html.push('</div>');
			        	}
			        	html.push('</div>');
		        	}
		        	if (html.length > 0) {
		        		document.getElementById("tranAccessAccountDiv").innerHTML = html.join('');
		        	}
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	</script>
	<script type="text/javascript" src="<%=ctx%>/frame/baseapp/message/OnlineMessage.js"></script>
</head>
<body>
<div id="center" class="body_index">
		<div class="mainContext">
			<!-- 问候&快捷方式部分开始 -->
			<div class="greetingAndLinkDiv">
				<!-- 标题部分 -->
				<div class="greetingAndLinkTitleDiv">
					<div class="greetingAndLinkTitleIco"></div>
					<div class="greetingAndLinkTitleText">
						${sessionScope.users.operatorname}，你好！欢迎使用<%=appname%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <a href="#" onClick="MainFrame.openNewTab('org_p_manage8_1','/frame/yhgl/NewPassWord.jsp','修改密码',true)" class="linkText">修改密码</a>  -->
						<a href="#" class="greetingAndLinkTitleLink" onClick="top.MainFrame.openNewTab('Message','/frame/baseapp/message/Message.jsp','消息管理',true)" class="linkText">查看消息</a>
						<a href="#" class="softwareDownloadLink" onClick="top.MainFrame.openNewTab('SoftwareDownload','/frame/installfiles/SoftwareDownload.jsp','软件下载',true)" class="linkText">软件下载</a>
					</div>
	
					<!-- 显示时间 -->
					<div class="greetingAndLinkTitleTime">
						<input id="localTime" class="localTimeInput showText" type="text"
							value="" />
						<script type="text/javascript">
							function showLocale(objD) {
								var str, colorhead, colorfoot;
								var yy = objD.getYear();
								if (yy < 1900)
									yy = yy + 1900;
								var MM = objD.getMonth() + 1;
								if (MM < 10)
									MM = '0' + MM;
								var dd = objD.getDate();
								if (dd < 10)
									dd = '0' + dd;
								var hh = objD.getHours();
								if (hh < 10)
									hh = '0' + hh;
								var mm = objD.getMinutes();
								if (mm < 10)
									mm = '0' + mm;
								var ss = objD.getSeconds();
								if (ss < 10)
									ss = '0' + ss;
								var ww = objD.getDay();
								if (ww == 0)
									colorhead = "";
								if (ww > 0 && ww < 6)
									colorhead = "";
								if (ww == 6)
									colorhead = "";
								if (ww == 0)
									ww = "星期日";
								if (ww == 1)
									ww = "星期一";
								if (ww == 2)
									ww = "星期二";
								if (ww == 3)
									ww = "星期三";
								if (ww == 4)
									ww = "星期四";
								if (ww == 5)
									ww = "星期五";
								if (ww == 6)
									ww = "星期六";
								str = colorhead + yy + "/" + MM + "/" + dd + " "
										+ hh + ":" + mm + ":" + ss + " " + ww;
								return (str);
							}
							function tick() {
								var today = new Date();
								document.getElementById("localTime").value = showLocale(today);
								window.setTimeout("tick()", 1000);
							}
							tick();
						</script>
	
					</div>
				</div>
			<!-- 1 start-->
				<!-- 待办事宜标题开始 -->
				<%--<img src="<%=ctx%>/frame/yhgl/images/mainpage/refresh.gif" style="cursor: hand;margin-top: 20px;" onclick='javascript:location.reload();'>
				--%><div class="tdlTitleRefreshDiv" style="cursor: hand;" onclick='javascript:location.reload();'>
					待办事宜：
				</div>
				<!-- 迭代待办事宜开始 -->
				<div id= "todoJobDiv"  style="width:100%;">
				</div>
				
				<!-- 迭代待办事宜结束 -->
				<!-- 待办事宜标题结束 -->
			<!-- 1 end -->
				<% if (systemType != null && "zb".equals(systemType)) { %>
					<div class="tdlTitleDiv">
						在修机车分类信息：
					</div>					
					<div id= "tranAccessAccountDiv"  style="width:100%;">
					</div>
				<% } %>
			</div>
		</div>			
		
		<% 
			//if(map==null) System.out.println("map is null"); 
		  // else {
		   		//List <Map> elList = (List)map.get("linkList");
		   		//if(elList == null) System.out.println("elList is null"); 
		  // }
		   if(map!=null&&(List)map.get("linkList")!=null){
		%>
		<!-- 外部链接开始 
		<div id="linkOut" class="body_index">
			<div class="mainContext">
					<div class="greetingAndLinkDiv">
						<div class="greetingAndLinkTitleDiv">
							<div class="greetingAndLinkTitleIco"></div>
							<div class="greetingAndLinkTitleText">
								外部链接
							</div>
						</div>
						<div style="margin-top: 25px;">
							<% 
								List <Map> elList = (List)map.get("linkList");
    							for(Map <String,String> childMap : elList){ 
    						%>
   								<div style="float: left;">
    								<div>
    									<img src="<%=ctx+childMap.get("imgUrl")%>"  onclick="window.open('<%=childMap.get("openUrl")%>')" style="width:'<%=childMap.get("imgWidth")%>';height:'<%=childMap.get("imgHeight")%>';cursor: hand;">
    								</div>
    								<div style="padding-left: 2px;padding-right: 2px;overflow: hidden; width:'<%=childMap.get("titleWidth")%>'; height:'<%=childMap.get("titleHeight")%>';line-height: <%=childMap.get("titleHeight")%>px; font-size:14px;"><center><%= childMap.get("title") %></center></div>
    							</div>
    							<div style="width: 37px; height:'<%=Integer.valueOf(childMap.get("imgHeight"))+Integer.valueOf(childMap.get("titleHeight"))%>'; float: left; ">&nbsp;</div>
							<% }%>
						</div>
					</div>
				</div>
			</div>
			-->
			<!-- 外部链接结束 -->	
			<%} %>
		</div>
		
</body>
</html>	