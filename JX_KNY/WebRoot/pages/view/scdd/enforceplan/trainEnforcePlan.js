/**
 *  检修月计划编制
 */
var app = angular.module('trainEnforcePlanApp', []);

app.controller('trainEnforcePlanCtrl', function($http,$scope,$filter) {
	
	// 刷新计划列表
	$scope.refreshPlanList = function () {
		$http({
		    method: 'POST',
		    params:{"entityJson":"{}","start":0,"limit":10},
		    url: ctx + '/trainEnforcePlan!findPageList.action'
		}).then(function successCallback(response) {
				$scope.planList = response.data.root;
		    }, function errorCallback(response) {
		});
	};
	
	// 选中计划列表
	$scope.selectPlan = function($index){
		var selectObj = $scope.planList[$index];
		$scope.selectPlanObj = selectObj ;
		$scope.selectedPlanRow = $index ;
		// 刷新明细列表
		$("#detailTable").bootstrapTable('refresh',{
			query:{
				entityJson:angular.toJson({"trainEnforcePlanIDX":selectObj.idx}, true)
			}
		});
	};
	
	// 添加计划（弹出添加窗口）
	$scope.shwoAddPlan = function(){
		$scope.trainEnforcePlanModleTitle = "添加计划" ;
		$("#trainEnforcePlanForm")[0].reset();
		// 初始化表单
		var initData = {} ;
		initData.planTime = $filter('date')(new Date(), 'yyyy-MM-dd');
		initData.planStartDate = $filter('date')(new Date(), 'yyyy-MM-dd');
		initData.planEndDate = $filter('date')(new Date(), 'yyyy-MM-dd');
		initData.planPerson = empId ;
		initData.planPersonName = empname ;
		initData.planOrgId = orgId ;
		initData.planOrgSeq = orgseq ;
		initData.planOrgName = orgName ;
		$scope.plan = initData ;
		$('#trainEnforcePlanModle').modal('show');
	};
	
	// 双击修改计划
	$scope.showUpdatePlan = function($index){
		var selectObj = $scope.planList[$index];
		$scope.plan = selectObj ;
		// 时间格式化
		if($scope.plan.planTime){
			$scope.plan.planTime = $filter('date')($scope.plan.planTime, 'yyyy-MM-dd');
		}
		$scope.trainEnforcePlanModleTitle = "修改计划" ;
		$('#trainEnforcePlanModle').modal('show');
	};
	
	// 保存计划
	$scope.savePlan = function(){
		var data = $scope.plan ;
		debugger ;
		$('#trainEnforcePlanModle').modal('hide');
	};
	
});


// 初始化
$(document).ready(function(){
	var appElement = document.querySelector('[ng-controller=trainEnforcePlanCtrl]');
	var $scope = angular.element(appElement).scope();
	$scope.refreshPlanList();
	
	// 初始化时间控件
	$('#planTime').datetimepicker({  
        //format: 'yyyy-mm-dd hh:mm',  
		format: 'yyyy-mm-dd',  
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        todayBtn: true
      }); 
	
	$('#planStartDate').datetimepicker({  
		format: 'yyyy-mm-dd',  
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        todayBtn: true
      });
	
	$('#planEndDate').datetimepicker({  
		format: 'yyyy-mm-dd',  
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        todayBtn: true
      });
	
	// 明细列表初始化
	var entityJson = {"trainEnforcePlanIDX":"###"};
	
	$("#detailTable").bootstrapTable({
		method : 'get',
		url : ctx + '/trainEnforcePlanDetail!findPageList.action',
		dataType : "json",
		classes : "table-no-bordered", // 设置表的class，默认为由边框的table，无边框table可加table-no-bordered
		pagination : true, // 是否显示分页
		pageSize : 10, //
		pageNumber : 1,
		silent : true,
		height : 490,
		pageList : ['5', '10', '30', '50'],
		sidePagination : "server", // 服务端处理分页
		queryParamsType : "limit", //
		queryParams : function(params) {
			var temp = {
					start : params.offset,
					limit : params.limit,
					entityJson: angular.toJson(entityJson, true)
				}
			return temp;
		}, // 获取查询参数
		contentType : "application/x-www-form-urlencoded",// 适用post-form的形式提交数据
		responseHandler : responseHandler,// 返回值处理为table需要的格式
		paginationPreText : '上一页', // 指定上一页显示的文字或者图表
		paginationNextText : '下一页', // 指定下一页显示的文字或者图表
		singleSelect : false, // 是否禁止多选，默认false
		idField : "idx", // 定义主键
		columns : [{
					align : 'center',
					checkbox : true ,
					formatter: function (value,row,index) {
						return '';
					}
				}, {
					field : 'idx',
					visible : false
				}, {
					title : '车型',
					field : 'trainTypeShortName',
					align : 'center',
					formatter: function (value,row,index) {
						if (null == value || "" == value) {
							return "";
						}
						return "<span style='font-weight:bold;'>"+value+"</span>";
					}
				}, {
					title : '车号',
					field : 'trainNo',
					align : 'center'
				},{
					title : '配属段',
					field : 'dNAME',
					align : 'center'
				},{
					title : '修程',
					field : 'repairClassName',
					align : 'center'
				},{
					title : '计划入段',
					field : 'planStartDate',
					align : 'center',
					formatter: function (value,row,index) {
						if (null == value || "" == value) {
							return "";
						}
						return new Date(value).Format('yyyy-MM-dd');
					}
				},{
					title : '计划离段',
					field : 'planEndDate',
					align : 'center',
					formatter: function (value,row,index) {
						if (null == value || "" == value) {
							return "";
						}
						return new Date(value).Format('yyyy-MM-dd');
					}
				}],
			onDblClickRow: function (data) {
			}
	});
});