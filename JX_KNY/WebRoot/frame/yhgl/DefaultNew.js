/**
 * 首页数据内容加载 伍佳灵 2017-11-24
 */
// 定义首页app
var app = angular.module('mainApp', []);
app.controller('mainCtrl', function($http,$scope,$filter) {
	
	/**
	 * 故障分类统计
	 */
	var grflReportChart ;
	var grflReportConfig = {
		    type: 'pie',
		    data: {
		        datasets: [],
		        labels: []
		    },
		    options: {
		        responsive: true,
		        legend: {
		            position: 'right'
		        }
		    }
		};

	var pieChartCanvas = $('#grflReport').get(0).getContext('2d');
	grflReportChart = new Chart(pieChartCanvas,grflReportConfig);
	
	// 刷新故障分类饼图
	$scope.refreshGrflReport = function () {
		$http({
		    method: 'POST',
		    params:{},
		    url: ctx + '/gztp!findGzFlStatistics.action'
		}).then(function successCallback(response) {
				var lists = response.data.root ;
				var newDataset = {
			            backgroundColor: [],
			            data: []
			    };
				var labels = [] ;
				if(lists){
					for(var i=0 ; i < lists.length ; i++){
						var obj = lists[i];
						newDataset.data.push(obj.counts);
						newDataset.backgroundColor.push(getGztpColor(obj.typeKey));
						labels.push(obj.typeValue);
					}
					grflReportChart.config.data.datasets = [] ;
					grflReportChart.config.data.datasets.push(newDataset);
					grflReportChart.config.data.labels = labels ;
					grflReportChart.update();
				}
		    }, function errorCallback(response) {
		});
	};
	
	/**
	 * 月计划兑现情况
	 */
	var monthRateReportChart ;
	var monthRateReportConfig = {
            type: 'line',
            data: {
                labels: ["一月", "二月", "三月", "四月", "五月", "六月", "七月","九月","十月","十一月","十二月"],
                datasets: []
            },
            options: {
                responsive: true,
                tooltips: {
                    mode: 'index',
                    intersect: false,
                },
                hover: {
                    mode: 'nearest',
                    intersect: true
                },
                scales: {
                    xAxes: [{
                        display: true
                    }],
                    yAxes: [{
                        display: true
                    }]
                }
            }
        };
	
		var monthRateReportCanvas = $('#monthRateReport').get(0).getContext('2d');
		monthRateReportChart = new Chart(monthRateReportCanvas,monthRateReportConfig);
		
		var yearDate = new Date().getFullYear();
		var monthDate = new Date().getMonth();
		$scope.yearDate = yearDate ;
		// 刷新月计划折线图
		$scope.refreshMonthRateReport = function () {
			$http({
			    method: 'POST',
			    params:{"year":yearDate},
			    url: ctx + '/trainEnforcePlanDetail!findMonthRateStatistics.action'
			}).then(function successCallback(response) {
					var lists = response.data.root ;
					if(lists){
						var planData = {
			                    label: "计划",
			                    backgroundColor: window.chartColors.red,
			                    borderColor: window.chartColors.red,
			                    data: [],
			                    fill: false,
			            };
						var realDate = {
			                    label: "实际",
			                    fill: false,
			                    backgroundColor: window.chartColors.blue,
			                    borderColor: window.chartColors.blue,
			                    data: [],
			            };
						for(var i=0 ; i < monthDate ; i++){
							var obj = lists[i];
							planData.data.push(obj.planCounts);
							realDate.data.push(obj.realCounts);
						}
						monthRateReportChart.config.data.datasets = [] ;
						monthRateReportChart.config.data.datasets.push(planData);
						monthRateReportChart.config.data.datasets.push(realDate);
						monthRateReportChart.update();
					}
			    }, function errorCallback(response) {
			});
		};
		
		// 上部分统计信息查询
		$scope.refresDefaultParts = function () {
			$http({
			    method: 'POST',
			    params:{"year":yearDate},
			    url: ctx + '/zbglRdpPlan!findDefaultStatistics.action'
			}).then(function successCallback(response) {
				var data = response.data ;
				if(data){
					// 安全生产天数
					$scope.safeDays = data.safeDays ;
					// 修车数量统计
					var trainStatisticsList = data.trainStatisticsList ;
					var trainStatistics = {} ;
					trainStatistics.realAllcounts = trainStatisticsList[0].counts ;
					trainStatistics.realCounts = trainStatisticsList[1].counts ;
					trainStatistics.planCounts = trainStatisticsList[2].counts ;
					var rate = 0 ;
					if(trainStatistics.planCounts != 0){
						rate = (trainStatistics.realCounts/trainStatistics.planCounts).toFixed(2) * 100;
					}
					trainStatistics.rate = rate ;
					$scope.trainStatistics = trainStatistics ;
					// 生产动态
					var jrdt = data.jrdt ;
					var zbglPlanList = jrdt.zbglPlanList ;
					var trainWorkPlans = jrdt.trainWorkPlans ;
					var kjInfos = "" ;
					if(zbglPlanList && zbglPlanList.length > 0){
						kjInfos = "库检("+zbglPlanList.length+"):"
						for(var i=0 ; i < zbglPlanList.length ; i++){
							var zbglPlan = zbglPlanList[i];
							kjInfos +=  zbglPlan.railwayTime + ";";
						}
					}
					
					var jxInfos = "" ;
					if(trainWorkPlans && trainWorkPlans.length > 0){
						jxInfos = "检修("+trainWorkPlans.length+"):"
						for(var i=0 ; i < trainWorkPlans.length ; i++){
							var trainWorkPlan = trainWorkPlans[i];
							jxInfos += ( trainWorkPlan.trainTypeShortName + "" + trainWorkPlan.trainNo + ";");
						}
					}
					$("#marquee").html(kjInfos + "<br/>" + jxInfos);
					
					
					// 预警提醒
					var warning = data.warning ;
					var warns = {} ;
					warns.hcCounts = warning.warningHC.length ;
					warns.kcCounts = warning.warningKC.length ;
					$scope.warning = warns ;
				}
			    }, function errorCallback(response) {
			});
		};
		
		// 检修作业情况
		$scope.refreshWorkPlanList = function () {
			var queryParams = {} ;
			queryParams.workPlanStatus = "ONGOING";
			$http({
			    method: 'POST',
			    params:{"entityJson":angular.toJson(queryParams),"start":0,"limit":10},
			    url: ctx + '/vTrainWorkPlan!queryWorkPlanList.action'
			}).then(function successCallback(response) {
					var workPlanList = response.data.root ;
					 $scope.trainInfoList = workPlanList ;
			    }, function errorCallback(response) {
			});
		};
		
		// 设置作业节点样式
		$scope.setNodeClass = function(node){
			var clazz = "label label-default" ; 
			if(node.status == "NOTSTARTED"){
				clazz = "label label-default" ; 
			}else if(node.status == "RUNNING"){
				clazz = "label label-warning" ; 
			}else if(node.status == "COMPLETED"){
				clazz = "label label-success" ; 
			}
			// 判断延期
			if(node.delayTime != "" && node.status == "RUNNING"){
				clazz = "label label-danger" ; 
			}
		    return clazz;
		};
		
		// 设置货车 客车 柴油发电机图片样式
		$scope.setTrainClass = function(node){
			var clazz = "fa fa-train" ; 
			if(node.vehicleType == "10"){
				clazz = "fa fa-train" ; 
			}else if(node.vehicleType == "20"){
				clazz = "fa fa-subway" ; 
			}else if(node.vehicleType == "30"){
				clazz = "fa fa-bolt" ; 
			}
		    return clazz;
		};
		
});

/**
 * 获取故障统计图表颜色
 */
var getGztpColor = function(key){
	var color = window.chartColors.grey ;
	if(key == 'FAULT_CLASSIFY'){
		color = window.chartColors.red ;
	}else if(key == 'FAULT_CLOSEDOOR'){
		color = window.chartColors.orange ;
	}else if(key == 'FAULT_WHEEL'){
		color = window.chartColors.yellow ;
	}else if(key == 'FAULT_OTHER'){
		color = window.chartColors.grey ;
	}
	return color ;
};

//初始化
$(document).ready(function(){
	var appElement = document.querySelector('[ng-controller=mainCtrl]');
	var $scope = angular.element(appElement).scope();
	$scope.refresDefaultParts();
	$scope.refreshGrflReport();
	$scope.refreshMonthRateReport();
	$scope.refreshWorkPlanList();
});

