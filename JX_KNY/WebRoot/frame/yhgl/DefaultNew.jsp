<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/pages/jspf/frame.jspf" %>
<html>
<head>
	<meta charset="gbk">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<title>系统主框架页</title>
	<script language="javascript" src="<%=ctx%>/frame/yhgl/DefaultNew.js"></script>
</head>
<body class="hold-transition skin-blue layout-top-nav" ng-app="mainApp" ng-controller="mainCtrl">
	<div id="center" class="content-wrapper">
	    <section class="content" >
		   <div class="row">
		   	  <div class="col-md-3 col-sm-6 col-xs-12">
		          <div class="info-box bg-aqua">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-bookmark-o"></i></span>
		            <div class="info-box-content">
		              <span class="info-box-text" style="font-size: 20">安全生产</span>
		              <span class="info-box-number" style="padding-top: 10;font-size: 25" ng-cloak>{{ safeDays }}&nbsp;<span style="font-size: 10">天</span></span>
		            </div>
		            <!-- /.info-box-content -->
		          </div>
		          <!-- /.info-box -->
		        </div>
		        
		        
				<div class="col-md-3 col-sm-6 col-xs-12">
		          <div class="info-box bg-green">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-thumbs-o-up"></i></span>
		
		            <div class="info-box-content" ng-cloak>
		              <span class="info-box-text">{{ yearDate }}年修车统计(累计/计划/实际)</span>
		              <span class="info-box-number">{{trainStatistics.realAllcounts}}/{{trainStatistics.planCounts}}/{{trainStatistics.realCounts}}</span>
		
		              <div class="progress">
		                <div class="progress-bar" style="width: {{ trainStatistics.rate }}%"></div>
		              </div>
		                  <span class="progress-description">
		                     	兑现率：{{ trainStatistics.rate }}%
		                  </span>
		            </div>
		            <!-- /.info-box-content -->
		          </div>
		          <!-- /.info-box -->
		        </div> 
		        
		        <div class="col-md-3 col-sm-6 col-xs-12">
		          <div class="info-box bg-yellow">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-calendar"></i></span>
		
		            <div class="info-box-content">
		              <span class="info-box-text">生产动态</span>
		              <span class="info-box-number"><marquee id="marquee" direction="left"  scrollamount="3" onmouseover="this.stop()" onmouseout="this.start()"  >
		              		</marquee>
		              </span>
		            </div>
		            <!-- /.info-box-content -->
		          </div
		          <!-- /.info-box -->
		        </div>
		        
		      <div class="col-md-3 col-sm-6 col-xs-12">
		       <div class="info-box bg-red">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-exclamation-circle"></i></span>
		
		            <div class="info-box-content" ng-cloak>
		              <span class="info-box-text">预警提醒</span>
		              <span class="info-box-number">客车：{{ warning.kcCounts }} 辆</span>
		              <span class="info-box-number">货车：{{ warning.hcCounts }} 辆</span>
		            </div>
		            <!-- /.info-box-content -->
		          </div>
		          <!-- /.info-box -->
		        </div>
	      </div>
	      
	      <!-- 第二行 -->
	      <div class="row" style="">
	      	<div class="col-md-6">
				<div class="box box-solid">
            		<div class="box-header with-border">
		              <h3 class="box-title text-blue">故障分类统计</h3>
		              <div class="box-tools pull-right">
		                <button type="button" ng-click="refreshGrflReport();" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
			              <div class="chart">
			                <canvas id="grflReport" style="height:250px"></canvas>
			              </div>
			            </div>
          		</div>
	      	</div>
	      	
	      	<div class="col-md-6">
				<div class="box box-solid">
		            <div class="box-header with-border">
		              <h3 class="box-title text-blue">月计划兑现情况</h3>
		              <div class="box-tools pull-right">
		                <button type="button" ng-click="refreshMonthRateReport();" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		            	<div class="chart">
		             		 <canvas id="monthRateReport" style="height:250px"></canvas>
		              	</div>
		            </div>
		            <!-- /.box-body -->
		          </div>
	      	</div>
	      </div>
	      
	      	      <!-- 第二行 -->
	      <div class="row">
	      	<div class="col-md-12">
	      		<div class="box box-solid">
            		<div class="box-header with-border">
		              <h3 class="box-title text-blue">车辆检修情况</h3>
		              <div class="box-tools pull-right">
			              	<i class="fa fa-circle" style="font-size: 20;color: #d2d6de;"></i> <span>未开工</span>&nbsp;&nbsp;
	                		<i class="fa fa-circle" style="font-size: 20;color: #f39c12;"></i> <span>已开工</span>&nbsp;&nbsp;
	                		<i class="fa fa-circle" style="font-size: 20;color: #dd4b39;"></i> <span>延期</span>&nbsp;&nbsp;
	                		<i class="fa fa-circle" style="font-size: 20;color: #00a65a;"></i> <span>完工</span>&nbsp;&nbsp;
		              </div>
		            </div>
		            <div class="box-body">
		            	<div class="table-responsive">
		            		<table class="table no-margin">
			                  <tbody>
			                  <tr ng-cloak ng-repeat="obj in trainInfoList">
			                  		<td style="font-weight: bold;border: 0">
			                  			<i ng-class="setTrainClass(obj)" style="font-size: 20;color:#00A7D0;"></i>
			                  			{{ obj.trainTypeShortName }} {{ obj.trainNo }}
			                  		</td>
			                  		<td style="padding-top: 10;border: 0" ng-repeat="node in obj.jobProcessNodes"><small ng-class="setNodeClass(node)">{{ node.nodeName }}</small></td>
			                  </tr>
			                  </tbody>
                			</table>
             			 </div>
			        </div>
          		</div>
	      	</div>
	      </div>
	    </section>
	</div>
	
      <script>
      	
		window.onload = function(){
		
		}
		
	 </script>  
	
</body>

<script src="DefaultNew.js"></script>

</html>	