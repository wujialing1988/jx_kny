<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<body>
	<!-- 模态窗口 -->
	<div class="modal fade in" id="trainEnforcePlanModle" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static">
          <div class="modal-dialog" style="width: 800px;">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span></button>
                  <h4 class="modal-title">{{ trainEnforcePlanModleTitle }}</h4>
              </div>
              <div class="modal-body">
              		<form id="trainEnforcePlanForm" class="form-horizontal" role="form">
              			<!-- 第一行 -->
                    	<div class="form-group">
                    		<label for="planName" class="col-md-2 control-label">*计划名称:</label>
							<div class="col-md-10">
							  	<input type="text" class="form-control" ng-model="plan.planName" placeholder="请输入计划名称" required>
							</div>				
                    	</div>
              			<!-- 第二行 -->
                    	<div class="form-group">
                     		<label for="planStartDate" class="col-md-2 control-label">*计划开始</label>
							<div class="col-md-4">
							    <div class='input-group date' id='planStartDate'>  
               						<input type='text' class="form-control"name="planStartDate"/>  
               						<span class="input-group-addon">  
                   						<span class="glyphicon glyphicon-calendar"></span>  
               						</span>  
	           					</div> 
							    
							</div>		
							
                     		<label for="planEndDate" class="col-md-2 control-label">*计划结束</label>
							<div class="col-md-4">
							    <div class='input-group date' id='planEndDate'>  
               						<input type='text' class="form-control"name="planEndDate"/>  
               						<span class="input-group-addon">  
                   						<span class="glyphicon glyphicon-calendar"></span>  
               						</span>  
	           					</div> 
							    
							</div>					
                    	</div>    
						<!-- 第三行 -->
                    	<div class="form-group">
                    		<label for="planPersonName" class="col-md-2 control-label">编制人:</label>
							<div class="col-md-4">
								<input type="hidden" class="form-control" ng-model="plan.planPerson">
							  	<input type="text" class="form-control" ng-model="plan.planPersonName" disabled>
							</div>
                    		<label for="planOrgName" class="col-md-2 control-label">编制单位:</label>
							<div class="col-md-4">
								<input type="hidden" class="form-control" ng-model="plan.planOrgId">
								<input type="hidden" class="form-control" ng-model="plan.planOrgSeq">
							  	<input type="text" class="form-control" ng-model="plan.planOrgName" disabled>
							</div>				
                    	</div>                    	                	
	                </form>                        
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default pull-left" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary pull-right" ng-click="savePlan()">保存</button>
              </div>
            </div>
            <!-- /.modal-content -->
          </div>
          <!-- /.modal-dialog -->
    </div>
		<!-- 模态窗口 end-->
	</body>
</html>