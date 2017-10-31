<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>机车走行公里维护</title>
	<%@include file="/frame/jspf/header.jspf" %>
	<style type="text/css">
		.labelItem{
			width: 60px;
			text-align: right;
		}
		#ym_container label{
			padding-left: 2px;
			display:inline-block;
		}
		#ym_container  span{
			display:inline-block;
			width: 100px;
		}
		input[name=x]{
			vertical-align: middle;
		  	margin-left: 5px;
		}
		.undo{
			background: url(u786.png) no-repeat;
			background-size: 15px;
			background-position-x: 50%;
			width: 25px;
			height: 15px;
			cursor: pointer;
		}
		.clear{
			background: url(database_refresh.png) no-repeat;
			background-size: 15px;
			background-position-x: 50%;
			width: 25px;
			height: 15px;
			cursor: pointer;
		}
		#c1c6_clear, #fxdx_clear{
			width: 100px;
		    position: absolute;
		    left: -100px;
		    top: 0;
		    z-index: 10000;
		    background-color: white;
		}
		#c1c6_clear ul, #fxdx_clear  ul{
			list-style: none;
			border-top: solid 1px #BBB8B8;
		}
		#c1c6_clear li, #fxdx_clear li{
			cursor: pointer;
			border: solid 1px #BBB8B8;
			border-top: none;
			padding: 2px;
			padding-left: 10px;
			font-size: 13px;
		}
		#c1c6_clear li:hover, .clearLi, #fxdx_clear li:hover {
			background-color: #CCF0FF;
		}
	</style>
	<script type="text/javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
	<script type="text/javascript" src="<%=ctx %>/jsp/cmps/fastform.js"></script>
	<script type="text/javascript" src="running_km_common.js"></script>
	<script type="text/javascript" src="running_km_fxdx.js"></script>
	<script type="text/javascript" src="running_km_fxdx_history.js"></script>
	<script type="text/javascript" src="running_km_c1c6.js"></script>
	<script type="text/javascript" src="running_km_c1c6_history.js"></script>
	<script type="text/javascript" src="running_km_edit_form.js"></script>
	<script type="text/javascript" src="running_km.js"></script>
	<script type="text/javascript" src="template_reg.js"></script>
  </head>
  
  <body>
 	 <div style="display:none">
  		<div id="ym_container" class="x-window-mc">
  			<label class="labelItem">
  				<input type="checkbox" name='x' checked/>
  				年月
  			</label>
  			<div style="display:inline-block">
  				<span id="el_ym_id"></span>
  			</div>
  			<div style="display:none">
  				<label>
  					<span id="el_begin_id"></span>
  				</label>
  				<label>-<span id="el_end_id"></span>
  				</label>
  			</div>
  		</div>
  	</div>
  	<div id="c1c6_clear">
		<ul>
			<li>C1</li>
			<li>C2</li>
			<li>C3</li>
			<li>C4</li>
			<li>C5</li>
			<li>C6</li>
		</ul>
	</div>
  	<div id="fxdx_clear">
		<ul>
			<li>辅修</li>
			<li>小修</li>
			<li>中修</li>
			<li>大修</li>
		</ul>
	</div>
  </body>
</html>
