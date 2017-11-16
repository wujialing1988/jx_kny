<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/pages/jspf/frame.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>检修月计划编制</title>
		
		<script language="javascript">
			//获取当前用户
			var empId = '${sessionScope.emp.empid}';
			var empname = '${sessionScope.emp.empname}';
			var orgId = '${sessionScope.org.orgid}';
			var orgName = '${sessionScope.org.orgname}';
			var orgseq = '${sessionScope.org.orgseq}';
		</script>
		
		<style type="text/css">
		</style>
	</head>
	<body ng-app="trainEnforcePlanApp"  ng-controller="trainEnforcePlanCtrl" style="background: #ecf0f5;">
		
		<section class="invoice">
			<!-- 添加计划窗口 -->
			<%@include file="/pages/view/scdd/enforceplan/trainEnforcePlanModle.jsp" %>
			<div class="row">
		        <div class="col-xs-12">
		          <div class="box-header">
	              <h3 class="box-title">检修计划编制</h3>
	              <div class="box-tools">
	                <div class="input-group input-group-sm" style="width: 200px;">
	                  <input type="text" name="table_search" class="form-control pull-right" placeholder="搜索...">
	
	                  <div class="input-group-btn">
	                    <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
	                  </div>
	                </div>
	              </div>
	            </div>
		          
		        </div>
		        <!-- /.col -->
		    </div>
		
			<div class="row">
		       <div class="col-md-3" style="padding-right: 0px;">
		          <div class="box">
		            <div class="box-header with-border">
		              <div class="pull-right">
			          	<button type="button" class="btn btn-default" ng-click="shwoAddPlan()" style="color: #55AFF2;font-size: 22px;">
							  <span class="fa fa-plus-circle"></span>
						 </button>
						 <button type="button" class="btn btn-default" style="color: #55AFF2;font-size: 22px;">
							  <span class="fa fa-minus-circle"></span>
						 </button>						 
						 <button type="button" class="btn btn-default" style="color: #55AFF2;font-size: 22px;">
							  <span class="fa fa-print"></span>
						 </button>
		              </div>
		            </div>
		            
		            <div class="box-header with-border">
		            	<label class="radio-inline">
							<input type="radio" name="optionsRadiosinline" id="optionsRadios3" value="option1" checked> 未发布
						</label>
						<label class="radio-inline">
							<input type="radio" name="optionsRadiosinline" id="optionsRadios4"  value="option2"> 已发布
						</label>
		            </div>
		            
		             <div class="box-body no-padding" style="min-height: 450px;">
					   	<ul class="nav nav-pills nav-stacked">
					   		<li ng-repeat="obj in planList" ng-class='{active:$index==selectedPlanRow}'><a style="cursor: pointer;"  ng-click="selectPlan($index)" ng-dblclick="showUpdatePlan($index)"> {{ obj.planName }}</a></li>
			            </ul>
		            </div>
		            <!-- /.box-body -->
		            
		            <!-- box-footer -->
		            <div class="box-footer" style="text-align: center;">
			            	<button type="button" class="btn btn-default" style="color: #55AFF2">
			              		<span class="glyphicon glyphicon-save-file"></span> 会签
						  	</button>
						  	<button type="button" class="btn btn-default" style="color: #55AFF2">
						  		<span class="glyphicon glyphicon-open-file"></span> 发布
						  	</button>
           			</div>
           			<!-- /.box-footer -->
		          </div>
		        </div>
		
				<div class="col-md-9" style="padding-left: 5px;">
		          <!-- BAR CHART -->
		          <div class="box">
		          
		          	<div class="box-header with-border" style="background: #F7F7F7;margin:10 10 10 10 ">
		              <h3 class="box-title">
		              	{{ selectPlanObj.planPersonName }}（{{ selectPlanObj.planOrgName }} | {{ selectPlanObj.planTime | date:'yyyy-MM-dd' }}）：{{ selectPlanObj.planStartDate | date:'yyyy-MM-dd' }} ~ {{ selectPlanObj.planEndDate | date:'yyyy-MM-dd' }}
		              </h3>
		            </div>
		          	
		            <div class="box-header with-border">
		              <button type="button" class="btn btn-default" style="color: #55AFF2;">
						  <span class="glyphicon glyphicon-plus"></span> 新增
					  </button>
		              <button type="button" class="btn btn-default" style="color: #55AFF2;">
						  <span class="glyphicon glyphicon-minus"></span> 删除
					  </button>	
					  				  
		              <div class="pull-right">
		              	<button type="button" class="btn btn-default" style="color: #55AFF2">
		              		<span class="glyphicon glyphicon-save-file"></span> 导入
					  	</button>
					  	<button type="button" class="btn btn-default" style="color: #55AFF2">
					  		<span class="glyphicon glyphicon-open-file"></span> 导出
					  	</button>
		              </div>
		            </div>
		            
		            <div class="box-body no-padding" style="min-height: 490px;">
		            	<table id="detailTable" class="table table-striped"></table>
		            </div>
		            <!-- /.box-body -->
		          </div>
		          <!-- /.box -->
				</div>
		      </div>
		</section>
	</body>
	<script src="trainEnforcePlan.js"></script>
</html>
