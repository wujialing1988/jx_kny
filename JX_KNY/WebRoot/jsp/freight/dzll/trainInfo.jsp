<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/pages/jspf/frame.jspf" %>
<html>
<head>
	<meta charset="gbk">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<title>车辆信息页面</title>
	<script type="text/javascript">
		var vehicleType="<%=(null==request.getParameter("vehicleType"))?"":request.getParameter("vehicleType")%>"; // 10 货车 20 客车
		var trainTypeIDX="<%=(null==request.getParameter("trainTypeIDX"))?"":request.getParameter("trainTypeIDX")%>"; //车型ID
		var trainNo="<%=(null==request.getParameter("trainNo"))?"":request.getParameter("trainNo")%>"; // 车号
	</script>
</head>
<body class="hold-transition skin-blue layout-top-nav" ng-cloak ng-app="trianInfoApp" ng-controller="trianInfoCtrl">
	<div class="content-wrapper" >
		<section class="content" >	
		
		<div class="row">
	      	<div class="col-md-12">
	      		<div class="box box-primary">
            		<div class="box-header with-border">
            		  <i class="ion ion-clipboard"></i>
		              <h3 class="box-title text-blue">车辆基本信息</h3>
		            </div>
		            <div class="box-body">
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td style="width: 10%;font-weight: bold;">车型：</td>
									<td>{{ trainInfoObj.trainTypeShortname }}</td>
									<td style="width: 10%;font-weight: bold;">车号：</td>
									<td>{{ trainInfoObj.trainNo }}</td>
									<td style="width: 10%;font-weight: bold;">车辆状态：</td>
									<td><span ng-class="setTrainStateClass()">{{ trainInfoObj.trainState | getStatus }}</span></td>
								</tr>
								<tr>
									<td style="width: 10%;font-weight: bold;">制造厂家：</td>
									<td>{{ trainInfoObj.makeFactoryName }}</td>
									<td style="width: 10%;font-weight: bold;">出厂日期：</td>
									<td>{{ trainInfoObj.leaveDate }}</td>
									<td style="width: 10%;font-weight: bold;">登记人员：</td>
									<td>{{ trainInfoObj.registerPersonName }}</td>
								</tr>
								
								<tr>
									<td style="width: 10%;font-weight: bold;">最近库检：</td>
									<td>{{ trainInfoObj.yyRailwayTime }}</td>
									<td style="width: 10%;font-weight: bold;">库检日期：</td>
									<td>{{ trainInfoObj.yyCreateTime }}</td>
									<td style="width: 10%;font-weight: bold;">运行天数：</td>
									<td>{{ trainInfoObj.totalday }}</td>
								</tr>
								
								<tr>
									<td style="width: 10%;font-weight: bold;">最近检修：</td>
									<td>{{ trainInfoObj.jxRepairClassName }}</td>
									<td style="width: 10%;font-weight: bold;">检修日期：</td>
									<td>{{ trainInfoObj.jxPlanBeginTime }}</td>
									<td style="width: 10%;font-weight: bold;"></td>
									<td></td>									
								</tr>
								
								<tr>
									<td style="width: 10%;font-weight: bold;">下次检修：</td>
									<td>{{ trainInfoObj.hcNextRepairClassName }}</td>
									<td style="width: 10%;font-weight: bold;">预计时间：</td>
									<td>{{ trainInfoObj.hcNextDate }}</td>
									<td style="width: 10%;font-weight: bold;"></td>
									<td></td>									
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
<script src="trainInfo.js"></script>

</html>	