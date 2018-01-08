<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/pages/jspf/frame.jspf" %>
<%@include file="/pages/jspf/ext.jspf" %> 
<html>
<head>
	<meta charset="gbk">
  	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<title>系统主框架页</title>
	<script language="javascript" src="<%=ctx%>/frame/yhgl/DefaultNew.js"></script>
</head>
<body ng-cloak class="hold-transition skin-blue layout-top-nav" ng-app="mainApp" ng-controller="mainCtrl">
	<div id="center" class="content-wrapper">
	    <section class="content" >

	    	<div class="row" ng-show="false">
	    	<div class="col-md-12">
	    		<div class="box box-solid bg-light-blue-gradient">
	            <div class="box-header">
	              <i class="fa fa-commenting-o"></i>
	
	              09992 扣车 
	              <!-- /. tools -->
	            </div>
            
            </div>
	    	
	    	</div>
	    	</div>
	    	
	    	
	    	<!-- 第一行 -->
		   <div class="row">
		   	  <div class="col-md-3 col-sm-6 col-xs-12" ng-show="priviligeObj.isSafeDaysShow">
		          <div class="info-box bg-aqua">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-bookmark-o"></i></span>
		            <div class="info-box-content">
		              <span class="info-box-text" style="font-size: 20">安全生产</span>
		              <span class="info-box-number" style="padding-top: 10;font-size: 25">{{ safeDays }}&nbsp;<span style="font-size: 10">天</span></span>
		            </div>
		            <!-- /.info-box-content -->
		          </div>
		          <!-- /.info-box -->
		        </div>
		        
		        
				<div class="col-md-3 col-sm-6 col-xs-12" ng-show="priviligeObj.isTrainStatisticsKCShow">
		          <div class="info-box bg-green">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-thumbs-o-up"></i></span>
		
		            <div class="info-box-content" ng-cloak>
		              <span class="info-box-text">{{ yearDate }}年客车修车统计(累计/计划/实际)</span>
		              <span class="info-box-number"><div>{{trainStatistics.realAllcounts}}/{{trainStatistics.planCounts}}/{{trainStatistics.realCounts}}</div></span>
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
		        
		        <div class="col-md-3 col-sm-6 col-xs-12" ng-show="priviligeObj.isTrainStatisticsHCShow">
		          <div class="info-box bg-yellow">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-thumbs-o-up"></i></span>
		
		            <div class="info-box-content" ng-cloak>
		              <span class="info-box-text">{{ yearDate }}年货车修车统计(累计/计划/实际)</span>
		              <span class="info-box-number"><div>{{trainStatisticsHC.realAllcounts}}/{{trainStatisticsHC.planCounts}}/{{trainStatisticsHC.realCounts}}</div></span>
		              <div class="progress">
		                <div class="progress-bar" style="width: {{ trainStatistics.rate }}%"></div>
		              </div>
		                  <span class="progress-description">
		                     	兑现率：{{ trainStatisticsHC.rate }}%
		                  </span>
		            </div>
		            <!-- /.info-box-content -->
		          </div>
		          <!-- /.info-box -->
		        </div> 
		        
		        <!-- <div class="col-md-3 col-sm-6 col-xs-12">
		          <div class="info-box bg-yellow">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-calendar"></i></span>
		
		            <div class="info-box-content">
		              <span class="info-box-text">生产动态</span>
		              <span class="info-box-number"><marquee id="marquee" direction="left"  scrollamount="3" onmouseover="this.stop()" onmouseout="this.start()"  >
		              		</marquee>
		              </span>
		            </div>
		            /.info-box-content
		          </div
		          /.info-box
		        </div> -->
		        
		      <div class="col-md-3 col-sm-6 col-xs-12" ng-show="priviligeObj.isKctxShow">
		       <div class="info-box bg-red">
		            <span class="info-box-icon" style="padding-top: 20"><i class="fa fa-exclamation-circle"></i></span>
		
		            <div class="info-box-content" ng-cloak>
		              <span class="info-box-text">扣车提醒</span>
		              <span ng-show="priviligeObj.isKctxHCShow" class="info-box-number">客车：{{ warning.kcCounts }} 辆</span>
		              <span ng-show="priviligeObj.isKctxKCShow" class="info-box-number">货车：{{ warning.hcCounts }} 辆</span>
		            </div>
		            <!-- /.info-box-content -->
		          </div>
		          <!-- /.info-box -->
		        </div>
		        
	      </div>
	      
	      <!-- 第二行 -->
	      <div class="row">
	      	<div class="col-md-6" ng-show="priviligeObj.isGztpStatisticsHCShow">
				<div class="box box-solid">
            		<div class="box-header with-border">
		              <h3 class="box-title text-blue">货车故障分类统计</h3>
		              <div class="box-tools pull-right">
		                <button type="button" ng-click="refreshGrflReportHC();" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
			              <div class="chart">
			                <canvas id="grflReportHC" style="height:250px"></canvas>
			              </div>
			            </div>
          		</div>
	      	</div>
	      	
	      	<div class="col-md-6" ng-show="priviligeObj.isPlanMonthRateHCShow">
				<div class="box box-solid">
		            <div class="box-header with-border">
		              <h3 class="box-title text-blue">{{ yearDate }}年货车月计划兑现情况</h3>
		              <div class="box-tools pull-right">
		                <button type="button" ng-click="refreshMonthRateReportHC();" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		            	<div class="chart">
		             		 <canvas id="monthRateReportHC" style="height:250px"></canvas>
		              	</div>
		            </div>
		            <!-- /.box-body -->
		          </div>
	      	</div>
	      	
	      	<div class="col-md-6" ng-show="priviligeObj.isGztpStatisticsKCShow">
				<div class="box box-solid">
            		<div class="box-header with-border">
		              <h3 class="box-title text-blue">客车故障分类统计</h3>
		              <div class="box-tools pull-right">
		                <button type="button" ng-click="refreshGrflReportKC();" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
			              <div class="chart">
			                <canvas id="grflReportKC" style="height:250px"></canvas>
			              </div>
			            </div>
          		</div>
	      	</div>
	      	
	      	<div class="col-md-6" ng-show="priviligeObj.isPlanMonthRateKCShow">
				<div class="box box-solid">
		            <div class="box-header with-border">
		              <h3 class="box-title text-blue">{{ yearDate }}年客车月计划兑现情况</h3>
		              <div class="box-tools pull-right">
		                <button type="button" ng-click="refreshMonthRateReportKC();" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
		              </div>
		            </div>
		            <div class="box-body">
		            	<div class="chart">
		             		 <canvas id="monthRateReportKC" style="height:250px"></canvas>
		              	</div>
		            </div>
		            <!-- /.box-body -->
		          </div>
	      	</div>
	      	
	      </div>
	      
	      	      <!-- 第二行 -->
	      <div class="row" ng-show="priviligeObj.isJxStatisticsShow">
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