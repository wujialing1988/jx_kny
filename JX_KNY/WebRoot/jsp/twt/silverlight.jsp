<%@ page language="java"  pageEncoding="utf-8"%>
<%--<%@ taglib uri="http://www.yunda.com/yunda" prefix="yd"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.yunda.base.context.SystemContext"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  	<head>	
  		<style type="text/css">
		    html, body {
			    height: 100%;
			    overflow: auto;
		    }
		    body {
			    padding: 0;
			    margin: 0;
		    }
		    #silverlightControlHost {
			    height: 100%;
			    text-align:center;
		    }
	    </style>
		<script src="Silverlight.js" type="text/javascript"></script>
		<script type="text/javascript" src="lhgdialog/lhgcore.min.js"></script>
		<script type="text/javascript" src="lhgdialog/lhgdialog.min.js"></script>
		<link href="lhgcore.css" type="text/css" rel="stylesheet"/>
		<script type="text/javascript">
	        function onSilverlightError(sender, args) { 
	            var appSource = ""; 
	            if (sender != null && sender != 0) { 
	                appSource = sender.getHost().Source; 
	            } 
	
	            var errorType = args.ErrorType; 
	            var iErrorCode = args.ErrorCode; 
	
	            if (errorType == "ImageError" ||  
	                errorType == "MediaError") { 
	              return; 
	            } 
	
	            var errMsg = "Unhandled Error in Silverlight Application "  
	                + appSource + "\n"; 
	
	            errMsg += "Code: " + iErrorCode + "    \n"; 
	            errMsg += "Category: " + errorType + "       \n"; 
	            errMsg += "Message: " + args.ErrorMessage + "     \n"; 
	            if (errorType == "ParserError") { 
	                errMsg += "File: " + args.xamlFile + "     \n"; 
	                errMsg += "Line: " + args.lineNumber + "     \n"; 
	                errMsg += "Position: " + args.charPosition + "     \n"; 
	            } else if (errorType == "RuntimeError") { 
	                if (args.lineNumber != 0) { 
	                    errMsg += "Line: " + args.lineNumber + "     \n"; 
	                    errMsg += "Position: " + args.charPosition +  
	                        "     \n"; 
	                }
	                errMsg += "MethodName: " + args.methodName + "     \n"; 
	            }
	            throw new Error(errMsg);
	        } 
	        
	        function test1(url){
	        	return encodeURI(url);
	        }
	        function openPage(url){
		        window.open(url);
	        }
	        function showWin(url, title){
				var testDG = new J.dialog({ page:url, lockScroll:true, cover:true, width:800, height:500, rang:true, title:title, btnBar:false, iconTitle:false});
				testDG.ShowDialog();
				
				// 给top对象赋值窗口对象
				top.jDiaglog = testDG;
			}
	    </script>
		
		<!-- 修改鼠标滚动进行放大缩小 -->
		<script type="text/javascript">
			var scrollFunc = function (e)
			{
				e = e || window.event;
				if (e.wheelDelta)//IE/Opera/Chrome 
				{
					var a = e.wheelDelta + ":" + e.x + ":" + e.y;
					MouseWheel(a);
				}
				else if (e.detail)//Firefox 
				{
					var a = e.wheelDelta + ":" + e.x + ":" + e.y;
					MouseWheel(a);
				}
			}
			/* 注册事件 */
			try {
		        if (document.addEventListener && DOMMouseScroll) {
		        	document.addEventListener(DOMMouseScroll, scrollFunc, false)
		        }// W3C
			} catch (e) {
				console.log(e);
			} 
			window.onmousewheel = document.onmousewheel = scrollFunc;//IE/Opera/Chrome 
			
			function OnMouseWheel(sender) {
		    	slCtl = sender.getHost();
			}
			function MouseWheel(a) {
				if (slCtl != null) {
					slCtl.Content.OnMouseWheel.OnMouseWheel(a);
				}
			}
		</script>
		
	</head>
  	<body>
  		<div title="" region="center" autoScroll="true" border="false">
			<table style="height:100%;width:100%">
				<tr>
					<td align="center" valign="middle">
			   		    <object id="SilverlightPlugin1" width="100%" height="100%"  
					        data="data:application/x-silverlight-2,"  
					        type="application/x-silverlight-2" >
			            	<param name="onerror" value="onSilverlightError" />
				            <param name="background" value="white" />				            
				            <param name="minRuntimeVersion" value="4.0.50826.0" />
				            <param name="windowless" value="true" />
							
							<!-- 修改鼠标滚动进行放大缩小 -->
							<param name="onLoad" value="OnMouseWheel" />
							
				            <!-- <param name="autoUpgrade" value="true" /> -->
					        <param name="source" value="${pageContext.request.contextPath}/silverlight/Position.xap"/>
					        	<c:if test="${empty param.isview or param.isview == undefined}">
					        	<param name="initParams" value="IP=${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.request.contextPath}/,Name=重庆江北整备场台位图,UserId=${sessionScope.users.userid}" />  
							</c:if>
							<c:if test="${param.isview == true}">
				        	<param name="initParams" value="IP=${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.request.contextPath}/,Name=重庆江北整备场台位图,UserId=${sessionScope.users.userid}" />  
							</c:if>
							<table style="height:100%;width:100%">
								<tr>
									<td align="center" valign="middle">
								        <a href="${pageContext.request.contextPath}/frame/installfiles/silverlight.exe"  
								            style="text-decoration: none;">
								            <img src="${pageContext.request.contextPath}/frame/installfiles/images/installsilverlight.gif"  
								                alt="下载安装Silverlight" style="border-style: none"/>
								        </a>
										<br>
						                <br>
										<span class="sysmsgInfo" style="width:auto;font-weight:normal;text-align:left;">
						                    	温馨提示：该页面需要Silverlight插件的支持才能正常显示
						                		<br>
												请先点击【下载安装Silverlight】，安装完成后，重新打开该页面即可！
						                </span>
					        		</td>
					        	</tr>
					        </table>
					    </object>
	        		</td>
	        	</tr>
	        </table>
    	</div>
  	<body>&nbsp;</body>
</html>