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
									<td style="width: 10%;font-weight: bold;">运行天数：</td>
									<td colspan="5">{{ trainInfoObj.totalday }}</td>
								</tr>
							</tbody>
						</table>
						
						<table class="table table-bordered">
							<thead>
								<tr>
									<td style="width: 25%;font-weight: bold;">修程</td>
									<td style="width: 25%;font-weight: bold;">检修</td>
									<td style="width: 25%;font-weight: bold;">到期</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="width: 25%;font-weight: bold;">辅修</td>
									<td style="width: 25%;">{{ trainInfoObj.beforeFxDate }}</td>
									<td style="width: 25%;">{{ trainInfoObj.nextFxDate }}</td>
								</tr>
								<tr>
									<td style="width: 25%;font-weight: bold;">段修</td>
									<td style="width: 25%;">{{ trainInfoObj.beforeDxDate }}</td>
									<td style="width: 25%;">{{ trainInfoObj.nextDxDate }}</td>
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