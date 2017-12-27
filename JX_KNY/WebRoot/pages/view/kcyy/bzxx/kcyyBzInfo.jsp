<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/pages/jspf/frame.jspf" %>
<%@include file="/pages/jspf/ext.jspf" %> 
<html>
<head>
	<meta charset="gbk">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<title>客车运用编组页面</title>
	<style type="text/css">
		.trainInfo{
			background-color:#4472C4;
			border:2px solid #4472C4;
			font-weight:bold;
			color:#FDFBFB;
			width:100%;
			text-align:center
		}
		
		.yxTrainInfo{
			background-color:#70AD47;
			border:2px solid #70AD47;
			font-weight:bold;
			color:#FDFBFB;
			width:100%;
			text-align:center
		}
		
		.nullTrainInfo{
			width:100%;
			text-align:center;
			border:2px solid #4472C4
		}
		
		.tooltip-inner{
			background-color:#C1B8AE;
			max-width: 400px;
			padding: 1px 1px;
		}
		
		.tooltip.bottom .tooltip-arrow {
			border-bottom-color: #C1B8AE;
		}
	</style>
	<script type="text/javascript">
		var vehicleType = "20" ;
	</script>
	<script language="javascript" src="<%=ctx%>/jsp/freight/dzll/kc/TrianWorkPlanRecord.js"></script>
</head>
<body class="hold-transition skin-blue layout-top-nav" ng-cloak ng-app="kcyyBzInfoApp" ng-controller="kcyyBzInfoCtrl">
	<div class="content-wrapper" >
		<section class="content" >	
		
		 <!-- 运用车 -->
		<div class="row">
	      	<div class="col-md-12">
	      		<div class="box box-primary">
            		<div class="box-header with-border">
            		  <i class="ion ion-clipboard"></i>
		              <h3 class="box-title text-blue">运用车</h3>
		             <div class="box-tools pull-right">
			              	<i class="fa fa-square" style="font-size: 20;color: #4472C4;"></i> <span>空闲编组</span>&nbsp;&nbsp;
	                		<i class="fa fa-square" style="font-size: 20;color: #70AD47;"></i> <span>运用编组</span>&nbsp;&nbsp;
	                		<i class="fa fa-square-o" style="font-size: 20;color: #4472C4;"></i> <span>预留车位</span>&nbsp;&nbsp;
		              </div>
		            </div>
		            <div class="box-body">
						<table class="table no-margin">
			                  <tbody ng-cloak ng-repeat="obj in marshallingList">
			                  <tr>
			                  		<td style="font-weight: bold;border: 0;width: 100px;text-align: center;">
			                  			{{ obj.marshallingName }}
			                  			
			                  			<div data-toggle="popover"> 
				                  			<a style="color:#4472C4;cursor:pointer;" data-placement="auto" data-toggle="tooltip" data-html=true title="{{ obj | getMarsTooltip }}">{{ obj | getStripsAll }}</a>
				                  		 </div>
			                  			
			                  		</td>
			                  		<td style="border: 0;width: 80px;float: left;" ng-repeat="train in obj.trains">
			                  			<div style="text-align: center;font-weight: bold;font-size:x-small;">{{ train.trainTypeShortname }}</div>
				                  		<div ng-class="setTrainClass(train)" data-toggle="popover"> 
				                  			<a ng-click="openTrianWorkPlanRecordWin(train)" style="color: white;cursor:pointer;" data-placement="auto" data-toggle="tooltip" data-html=true title="{{ train | getTooltip }}">{{ train.trainNo }}</a>
				                  		 </div>
			                  		</td>
			                  </tr>
			                  </tbody>
                		 </table>
			        </div>
          		</div>
	      	</div>
	      </div>
	      
	    <!-- 备用车 -->
		<div class="row">
	      	<div class="col-md-12">
	      		<div class="box box-primary">
            		<div class="box-header with-border">
            		  <i class="ion ion-clipboard"></i>
		              <h3 class="box-title text-blue">备用车</h3>
		            </div>
		            <div class="box-body">
						<table class="table no-margin">
			                  <tbody ng-cloak>
			                  <tr>
			                  		<td style="border: 0;width: 80px;float: left;" ng-repeat="train in notMarshallingList">
			                  			<div style="text-align: center;font-weight: bold;font-size:x-small;">{{ train.trainTypeShortname }}</div>
				                  		<div ng-class="setTrainClass(train)" data-toggle="popover"> 
				                  			<a ng-click="openTrianWorkPlanRecordWin(train)" style="color: white;cursor:pointer;" data-placement="auto" data-toggle="tooltip" data-html=true title="{{ train | getTooltip }}">{{ train.trainNo }}</a>
				                  		 </div>
			                  		</td>
			                  </tr>
			                  </tbody>
                		 </table>
			        </div>
          		</div>
	      	</div>
	      </div>
	      
	    <!-- 检修车 -->
		<div class="row">
	      	<div class="col-md-12">
	      		<div class="box box-primary">
            		<div class="box-header with-border">
            		  <i class="ion ion-clipboard"></i>
		              <h3 class="box-title text-blue">检修车</h3>
		            </div>
		            <div class="box-body">
						<table class="table no-margin">
			                  <tbody ng-cloak>
			                  <tr>
			                  		<td style="border: 0;width: 80px;float: left;" ng-repeat="train in jxMarshallingList">
			                  			<div style="text-align: center;font-weight: bold;font-size:x-small;">{{ train.trainTypeShortname }}</div>
				                  		<div ng-class="setTrainClass(train)" data-toggle="popover"> 
				                  			<a ng-click="openTrianWorkPlanRecordWin(train)" style="color: white;cursor:pointer;" data-placement="auto" data-toggle="tooltip" data-html=true title="{{ train | getTooltip }}">{{ train.trainNo }}</a>
				                  		 </div>
			                  		</td>
			                  </tr>
			                  </tbody>
                		 </table>
			        </div>
          		</div>
	      	</div>
	      </div>	      	      
			
		</section> 
	</div>
</body>

<script type="text/javascript">
	$("[data-toggle='tooltip']").tooltip(); 
</script>



<script src="kcyyBzInfo.js"></script>

</html>	