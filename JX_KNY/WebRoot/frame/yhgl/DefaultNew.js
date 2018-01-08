/**
 * 首页数据内容加载 伍佳灵 2017-11-24
 */
// 定义首页app
var app = angular.module('mainApp', []);
app.controller('mainCtrl', function($http,$scope,$filter) {
	
	/**
	 * 自定义桌面
	 */
	$scope.isKoucheShow = true ;
	
    $scope.showorhide = function () {
        return false ;
    }
    
	// 获取首页自定义菜单权限
	$scope.getDeskPriviligeObj = function () {
		$http({
		    method: 'POST',
		    params:{},
		    url: ctx + '/deskPrivilige!getDeskPriviligeObj.action'
		}).then(function successCallback(response) {
				$scope.priviligeObj = response.data ;
		    }, function errorCallback(response) {
		});
	};
	
	/**
	 * 故障分类统计货车
	 */
	var grflReportChartHC ;
	var grflReportConfigHC = {
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

	var pieChartCanvasHC = $('#grflReportHC').get(0).getContext('2d');
	grflReportChartHC = new Chart(pieChartCanvasHC,grflReportConfigHC);
	
	// 刷新故障分类饼图
	$scope.refreshGrflReportHC = function () {
		$http({
		    method: 'POST',
		    params:{},
		    url: ctx + '/gztp!findGzFlStatisticsHC.action'
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
					grflReportChartHC.config.data.datasets = [] ;
					grflReportChartHC.config.data.datasets.push(newDataset);
					grflReportChartHC.config.data.labels = labels ;
					grflReportChartHC.update();
				}
		    }, function errorCallback(response) {
		});
	};
	
	
	/**
	 * 故障分类统计客车
	 */
	var grflReportChartKC ;
	var grflReportConfigKC = {
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

	var pieChartCanvasKC = $('#grflReportKC').get(0).getContext('2d');
	grflReportChartKC = new Chart(pieChartCanvasKC,grflReportConfigKC);
	
	// 刷新故障分类饼图
	$scope.refreshGrflReportKC = function () {
		$http({
		    method: 'POST',
		    params:{},
		    url: ctx + '/gztp!findGzFlStatisticsKC.action'
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
					grflReportChartKC.config.data.datasets = [] ;
					grflReportChartKC.config.data.datasets.push(newDataset);
					grflReportChartKC.config.data.labels = labels ;
					grflReportChartKC.update();
				}
		    }, function errorCallback(response) {
		});
	};
	
	var yearDate = new Date().getFullYear();
	var monthDate = new Date().getMonth();
	$scope.yearDate = yearDate ;
	
	/**
	 * 货车月计划兑现情况
	 */
	var monthRateReportChartHC ;
	var monthRateReportConfigHC = {
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
	
		var monthRateReportCanvasHC = $('#monthRateReportHC').get(0).getContext('2d');
		monthRateReportChartHC = new Chart(monthRateReportCanvasHC,monthRateReportConfigHC);
		
		// 刷新月计划折线图
		$scope.refreshMonthRateReportHC = function () {
			$http({
			    method: 'POST',
			    params:{"year":yearDate},
			    url: ctx + '/trainEnforcePlanDetail!findMonthRateStatisticsHC.action'
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
						for(var i=0 ; i < (monthDate + 1) ; i++){
							var obj = lists[i];
							planData.data.push(obj.planCounts);
							realDate.data.push(obj.realCounts);
						}
						monthRateReportChartHC.config.data.datasets = [] ;
						monthRateReportChartHC.config.data.datasets.push(planData);
						monthRateReportChartHC.config.data.datasets.push(realDate);
						monthRateReportChartHC.update();
					}
			    }, function errorCallback(response) {
			});
		};
		
		
		/**
		 * 货车月计划兑现情况
		 */
		var monthRateReportChartKC ;
		var monthRateReportConfigKC = {
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
		
			var monthRateReportCanvasKC = $('#monthRateReportKC').get(0).getContext('2d');
			monthRateReportChartKC = new Chart(monthRateReportCanvasKC,monthRateReportConfigKC);
			
			// 刷新月计划折线图
			$scope.refreshMonthRateReportKC = function () {
				$http({
				    method: 'POST',
				    params:{"year":yearDate},
				    url: ctx + '/trainEnforcePlanDetail!findMonthRateStatisticsKC.action'
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
							for(var i=0 ; i < (monthDate + 1) ; i++){
								var obj = lists[i];
								planData.data.push(obj.planCounts);
								realDate.data.push(obj.realCounts);
							}
							monthRateReportChartKC.config.data.datasets = [] ;
							monthRateReportChartKC.config.data.datasets.push(planData);
							monthRateReportChartKC.config.data.datasets.push(realDate);
							monthRateReportChartKC.update();
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
					// 修车数量统计（客车）
					var trainStatisticsListKC = data.trainStatisticsListKC ;
					var trainStatistics = {} ;
					trainStatistics.realAllcounts = trainStatisticsListKC[0].counts ;
					trainStatistics.realCounts = trainStatisticsListKC[1].counts ;
					trainStatistics.planCounts = trainStatisticsListKC[2].counts ;
					var rate = 0 ;
					if(trainStatistics.planCounts != 0){
						rate = (trainStatistics.realCounts/trainStatistics.planCounts).toFixed(2) * 100;
					}
					trainStatistics.rate = rate ;
					$scope.trainStatistics = trainStatistics ;
					
					// 修车数量统计（货车）
					var trainStatisticsListHC = data.trainStatisticsListHC ;
					var trainStatisticsHC = {} ;
					trainStatisticsHC.realAllcounts = trainStatisticsListHC[0].counts ;
					trainStatisticsHC.realCounts = trainStatisticsListHC[1].counts ;
					trainStatisticsHC.planCounts = trainStatisticsListHC[2].counts ;
					var rate = 0 ;
					if(trainStatisticsHC.planCounts != 0){
						rate = (trainStatisticsHC.realCounts/trainStatisticsHC.planCounts).toFixed(2) * 100;
					}
					trainStatisticsHC.rate = rate ;
					$scope.trainStatisticsHC = trainStatisticsHC ;					
					
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
	$scope.getDeskPriviligeObj();
	$scope.refresDefaultParts();
	$scope.refreshGrflReportHC();
	$scope.refreshGrflReportKC();
	$scope.refreshMonthRateReportHC();
	$scope.refreshMonthRateReportKC();
	$scope.refreshWorkPlanList();
});

