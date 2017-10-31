<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/frame/jspf/TreeFilter.jspf" %>
<%@include file="/frame/jspf/FileUploadField.jspf" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yunda.frame.report.entity.FileCatalog" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表模板管理</title>
<style type="text/css">	
	<%-- 修改网页链接<a>访问后、鼠标移上时的字体样式 --%>
	a:VISITED { color:blue; }
	a:HOVER { color:green; font-weight:bold; }
</style>
<script type="text/javascript">

	var PATH_ROOT = '<%= FileCatalog.CONST_STR_PATH_ROOT%>';		// 报表部署根目录标识
	
	var CONST_STR_T = '<%=com.yunda.frame.report.entity.ReportConstants.CONST_STR_T%>';		// 是否可编辑 - 是
	var CONST_STR_F = '<%=com.yunda.frame.report.entity.ReportConstants.CONST_STR_F%>';		// 是否可编辑 - 否
	
	var UNEDITABLE_COLOR = '<%=com.yunda.frame.report.entity.ReportConstants.CONST_STR_UNEDITABLE_COLOR%>';		// 不可编辑记录的字体颜色
	
	<%-- 图片虚拟目录 --%>
	var imgpath = '<%=ctx %>/frame/resources/images/toolbar';
	<%-- 下载图标虚拟路径 --%>
	var downloadImg = imgpath + '/download.png';
	var img = imgpath + '/pjgl.gif';<%-- 图片虚拟路径 --%>
	
	// 自定义vtype，确保报表部署名称字段必须以.cpt结尾
	Ext.applyIf(Ext.form.VTypes, {
		fineReport: function(_v) {
			return /[\d\w\s\.]+.cpt$/.test(_v);
		},
		fineReportText: "报表部署名称格式为*.cpt！\n例如：report.cpt",
		fineReportMask: /[\w\d\s\.]/
	});
	
	// 自定义vtype，验证日期范围
	Ext.applyIf(Ext.form.VTypes, {
		dateRange: function(_v, field) {
			if (field.dateRange) {
				// 开始日期
				var startId = field.dateRange.startDate;
				this.startField = Ext.getCmp(startId);
				var startDate = this.startField.getValue();
				// 结束日期
				var endId = field.dateRange.endDate;
				this.endField = Ext.getCmp(endId);
				var endDate = this.endField.getValue();
				if (Ext.isEmpty(startDate) || Ext.isEmpty(endDate)) {
					return true;
				}
				return startDate <= endDate ? true : false
			}
		},
		// 验证失败信息
		dateRangeText: "开始日期不能大于结束日期"
	});
</script>

<script language="javascript" src="<%= ctx%>/jsp/jx/js/component/BaseComboTree.js"></script> 
<script language="javascript" src="<%= ctx%>/frame/report/FileCatalog.js"></script>
<script language="javascript" src="<%= ctx%>/frame/report/FileObject.js"></script>
<script language="javascript" src="<%= ctx%>/frame/report/PrinterModule.js"></script>
<script language="javascript" src="<%= ctx%>/frame/report/ReportManage.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>