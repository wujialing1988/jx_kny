<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/vis.jspf" %> 
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.vis.entity.PlatformTaskItem" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试VIS</title>
 <style>
    body, html {
      font-family: arial, sans-serif;
      font-size: 11pt;
    }

    #visualization {
      box-sizing: border-box;
      width: 100%;
      height: 300px;
    }
    
    a:HOVER {
    	font-weight: bold;
    }
	
}
  </style>
<style type="text/css">

	/** ******** 未开工显示样式定义开始 ******** */
	.vis.timeline .item.wkg {
		color: #FFF;
		background-color: #999999;
      	border-color: black;
    }
    //.vis.timeline .item.orange.selected {
    //  /* custom colors for selected orange items */
    //  background-color: orange;
    //  border-color: orangered;
    }
	/** ******** 未开工显示样式定义结束 ******** */
	
	/** ******** 已开工显示样式定义开始 ******** */
	.vis.timeline .item.ykg {
		color: #FFF;
		background-color: #fec80c;
      	border-color: black;
    }
	/** ******** 已开工显示样式定义结束 ******** */
	
	/** ******** 已延期显示样式定义开始 ******** */
	.vis.timeline .item.yyq {
		color: #FFF;
		background-color: #ff0000;
      	border-color: black;
    }
	/** ******** 已开工显示样式定义结束 ******** */
	
	/** ******** 已完工显示样式定义开始 ******** */
	.vis.timeline .item.ywg {
		color: #FFF;
		background-color: #008000;
      	border-color: black;
    }
	/** ******** 已完工显示样式定义结束 ******** */

    /* ******** 条目被选选择时的样式定义开始 ******** */
    .vis.timeline .item.selected {
      background-color: blue;
      border-color: black;
      color: black;
      box-shadow: 0 0 10px gray;
    }
    /* ******** 条目被选选择时的样式定义结束 ******** */
</style>
<style>
	/* 时间周期的样式定义 */
	.peroid {
		font-size: small;
		background-color: #000;
		margin-top:10px;
		color: #FFF;
	}
</style>
<style type="text/css">
    body, html {
      font-family: sans-serif;
    }

    /* alternating column backgrounds */
    .vis.timeline .timeaxis .grid.odd {
      background: #f5f5f5;
    }

    /* gray background in weekends, white text color */
    .vis.timeline .timeaxis .grid.saturday,
    .vis.timeline .timeaxis .grid.sunday {
      background: gray;
    }
    .vis.timeline .timeaxis .text.saturday,
    .vis.timeline .timeaxis .text.sunday {
      color: white;
    }
  </style>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>  
<script language="javascript" src="<%=ctx %>/vis_demo/PlatformTaskItem.js"></script>
<script language="javascript" src="<%=ctx %>/vis_demo/VisTest.js"></script>
<script type="text/javascript">
	var STATUS_WKG = '<%= PlatformTaskItem.STATUS_WKG %>'		// 未开工
	var STATUS_YKG = '<%= PlatformTaskItem.STATUS_YKG %>'		// 已开工
	var STATUS_YYQ = '<%= PlatformTaskItem.STATUS_YYQ %>'		// 已延期
	var STATUS_YWG = '<%= PlatformTaskItem.STATUS_YWG %>'		// 已完工
</script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>