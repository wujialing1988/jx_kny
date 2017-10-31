<%@page import="com.yunda.frame.util.StringUtil"%>
<%@page import="com.yunda.frame.util.DateUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<%@page import="com.yunda.frame.baseapp.upload.entity.Attachment"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>

<%String ctx = request.getContextPath(); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>看图</title>
		<link rel="stylesheet" href="<%=ctx %>/jsp/sb/base/css/lrtk.css" type="text/css" media="screen" />
		<script src="<%=ctx %>/jsp/sb/base/js/jquery.min.js"></script>
		<script src="<%=ctx %>/jsp/sb/base/js/jquery.flexslider-min.js"></script>
		<script type="text/javascript">
			var i = 0;
			var src = '<%=ctx %>/jsp/sb/base/images/404';
			var img_src = [src + '.jpg', src + '_1.jpg' ];
			function imgError(){
				var event = event || window.event;
				var img = event.srcElement || event.target;
				img.src = img_src[i++%2];
			}
		</script>
	</head>
	<body style="text-align:center;padding-top:10px;">
		<div id="container">
			<div class="flexslider">
			    <ul class="slides">
			<%
				List<Attachment> list = (List<Attachment>) request.getAttribute("list");
				if (list.size() == 0) {
			%>
				<li><img src="<%=request.getContextPath() %>/jsp/sb/base/images/noup.gif" width='800'/></li>
			<%			    
				} else {
				    String path = null;
				    String attachmentKeyName = null;
				    String year = null;
				    String month = null;
				    String attachmentSaveName = null;
				    Calendar c = Calendar.getInstance();
					for(Attachment e : list){
					    c.setTime(e.getUploadTime());
					    String uploadTime = DateUtil.yyyy_MM_dd_HH_mm_ss.format(e.getUploadTime());
					    String uploadPersonName = StringUtil.nvl(e.getUploadPersonName(), "");
					    String title = String.format("%s&nbsp;上传于%s", uploadPersonName, uploadTime);
					    attachmentKeyName = e.getAttachmentKeyName();
					    year = String.valueOf(c.get(Calendar.YEAR));
					    month = String.valueOf(c.get(Calendar.MONTH) + 1);
					    attachmentSaveName = e.getAttachmentSaveName();
			%>
						
						<li><div style="position:absolute;font-size:48px;color:#fff;font-family:微软雅黑;padding:10px;" title="<%= title %>"><%= uploadTime %>&nbsp;<%= uploadPersonName %></div><img title="<%= title %>" src="<%= ctx %>/attachment!image.action?attachmentKeyName=<%= attachmentKeyName %>&year=<%=year %>&month=<%=month%>&attachmentSaveName=<%=attachmentSaveName %>" onerror='imgError()'/></li>
			<%
					}
				}
			%>
			    </ul>
		  	</div>
		  	<div style="clear:both"></div>
		</div>
	</body>
	<script type="text/javascript">
	    //var slideToStartIdx = <%=request.getParameter("slideToStart") != null ? request.getParameter("slideToStart") : 0 %>;
		$('.flexslider').flexslider(); //{slideToStart : slideToStartIdx}
	</script>
</html>