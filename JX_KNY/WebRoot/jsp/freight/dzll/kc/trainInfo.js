/**
 * 车辆基本信息 伍佳灵 2017-12-06
 */
var app = angular.module('trianInfoApp', []);
app.controller('trianInfoCtrl', function($http,$scope,$filter) {
	
	// 刷新基本信息页面
	$scope.refreshTrainInfo = function () {
		$http({
		    method: 'POST',
		    params:{
		    	"trainTypeIDX":trainTypeIDX,
		    	"trainNo":trainNo
		    },
		    url: ctx + '/trainRecord!getTrainInfoForKC.action'
		}).then(function successCallback(response) {
			$scope.trainInfoObj = response.data[0];
		    }, function errorCallback(response) {
		});
	};
	
	
	// 设置作业节点样式
	$scope.setTrainStateClass = function(){
		if(!$scope.trainInfoObj){
			return "";
		}
		var trainState = $scope.trainInfoObj.trainState ;
		var clazz = "label label-success" ; 
    	if(trainState == 10){
    		clazz = "label label-warning" ;
		}else if(trainState == 20){
			clazz = "label label-success" ;
		}else if(trainState == 30){
			clazz = "label label-primary" ;
		}else if(trainState == 40){
			clazz = "label label-danger" ;				
		}
	    return clazz;
	};
	
});

// 过滤器
app.filter('getStatus', function(){
    var filter = function(trainState){
    	var reuslt = "" ;
    	if(trainState == 10){
    		reuslt = "检修" ;
		}else if(trainState == 20){
			reuslt = "运用" ;
		}else if(trainState == 30){
			reuslt = "列检" ;
		}else if(trainState == 40){
			reuslt = "扣车" ;				
		}
        return reuslt;
      };
      return filter;
});


//初始化
$(document).ready(function(){
	var appElement = document.querySelector('[ng-controller=trianInfoCtrl]');
	var $scope = angular.element(appElement).scope();
	$scope.refreshTrainInfo();
});

