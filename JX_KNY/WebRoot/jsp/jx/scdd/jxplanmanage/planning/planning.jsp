<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>检修计划编制</title>
    <%@include file="/frame/jspf/header.jspf" %>
    <script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/cmps/fastform.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/BureauSelect.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/enforceplan/DominateSectionWidget.js"></script>
    <script type="text/javascript" src="planning_grid.js"></script>
    <script type="text/javascript" src="planning_searchform.js"></script>
    <script type="text/javascript" src="planning_edit.js"></script>
    <script type="text/javascript" src="planning_edit_form.js"></script>
    <script type="text/javascript" src="planning_edit_details.js"></script>
    <script type="text/javascript" src="planning_edit_details_add.js"></script>
    <script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics_common.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics_fxdx.js"></script>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics_c1c6.js"></script>
    <script type="text/javascript" src="planning_edit_details_rkm.js"></script>
    <script type="text/javascript" src="planning.js"></script>
  </head>
  <body>
  </body>
</html>
