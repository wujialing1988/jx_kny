/**
 * 客车运用编组查看 伍佳灵 2017-12-20
 */
var app = angular.module('kcyyBzInfoApp', []);
app.controller('kcyyBzInfoCtrl', function($http,$scope,$filter,$interval,$timeout) {
	
	// 刷新编组信息列表
	$scope.findMarshallingList = function () {
		$http({
		    method: 'POST',
		    params:{
		    },
		    url: ctx + '/marshalling!findMarshallingList.action'
		}).then(function successCallback(response) {
				$scope.marshallingList = response.data.list ;
		    }, function errorCallback(response) {
		});
	};
	
	// 刷新未编组信息列表
	$scope.findNotMarshallingList = function () {
		$http({
		    method: 'POST',
		    params:{
		    },
		    url: ctx + '/marshalling!findNotMarshallingList.action'
		}).then(function successCallback(response) {
				$scope.notMarshallingList = response.data.list ;
		    }, function errorCallback(response) {
		});
	};	
	
	// 刷新检修车辆信息列表
	$scope.findJxMarshallingList = function () {
		$http({
		    method: 'POST',
		    params:{
		    },
		    url: ctx + '/marshalling!findJxMarshallingList.action'
		}).then(function successCallback(response) {
				$scope.jxMarshallingList = response.data.list ;
		    }, function errorCallback(response) {
		});
	};		
	
	/**
	 * 设置运用车样式
	 */
	$scope.setTrainClass = function (train) {
		var clazz = "trainInfo" ; 
		if(train.status == '20'){
			clazz = "nullTrainInfo" ; 
		}else if(train.status == '10'){
			clazz = "yxTrainInfo" ; 
		}
	    return clazz;
	}
	
	/**
	 * 打开车辆履历面板
	 * @param train 车辆实体
	 */
	$scope.openTrianWorkPlanRecordWin = function(train){
		if(train.trainTypeIdx && train.trainNo){
			TrianWorkPlanRecord.load(train.trainTypeIdx,train.trainNo);
			TrianWorkPlanRecord.win.show();
		}
	}
	
	/**
	 * 定时刷新 5s
	 */
	$scope.timer = $interval( function(){
		$scope.findMarshallingList();
		$scope.findNotMarshallingList();
		$scope.findJxMarshallingList();
	 }, 5000);
	
});

/**
 * 获取车次
 */
app.filter('getStripsAll', function(){
    var filter = function(obj){
    	if(obj.demandIdx){
    		return obj.stripsAll ;
    	}
        return ""; 
      };
      return filter;
});


/**
 * 编组信息面板展示
 */
app.filter('getMarsTooltip', function(){
    var filter = function(obj){
    	var reuslt = '<table class="table table-bordered" style="background-color:#C1B8AE;border:2px solid #1E1913">'+
    		'<tbody>'+
    		
    		'<tr>'+
			'<td>车次</td>'+
			'<td>'+ (obj.stripsAll == null ? '' : obj.stripsAll) +'</td>'+
			'</tr>'+
			
    		'<tr>'+
			'<td>地点</td>'+
			'<td>'+ (obj.stationAll == null ? '' : obj.stationAll) +'</td>'+
			'</tr>'+
    		
    		'<tr>'+
			'<td>发车日期</td>'+
			'<td>'+ (obj.runningDate == null ? '' : obj.runningDate) +'</td>'+
			'</tr>'+
			
    		'<tr>'+
			'<td>出发时间</td>'+
			'<td>'+ (obj.toTimeAll == null ? '' : obj.toTimeAll) +'</td>'+
			'</tr>'+
			
    		'<tr>'+
			'<td>返程时间</td>'+
			'<td>'+ (obj.arrivalTimeAll == null ? '' : obj.arrivalTimeAll) +'</td>'+
			'</tr>'+	
			
    		'<tr>'+
			'<td>乘务检车</td>'+
			'<td>'+ (obj.trainInspectorName == null ? '' : obj.trainInspectorName) +'</td>'+
			'</tr>'+			
			
			'</tbody></table>' ;
    	var dd = "<h2>'am Header2 </h2>" ;
        return reuslt; 
      };
      return filter;
});

/**
 * 车辆信息面板展示
 */
app.filter('getTooltip', function(){
    var filter = function(obj){
    	if(obj.status == '20'){
    		return "" ;
    	}
    	var reuslt = '<table class="table table-bordered" style="background-color:#C1B8AE;border:2px solid #1E1913">'+
    		'<thead><tr>'+
    		'<td>修程</td>'+
    		'<td>检修</td>'+
    		'<td>到期</td>'+
    		'<td>走行公里</td>'+
    		'</tr></thead>'+
    		'<tbody>'+
    		'<tr>'+
			'<td>A1</td>'+
			'<td>'+ (obj.beforeA1Date == null ? '' : obj.beforeA1Date) +'</td>'+
			'<td>'+ (obj.nextA1Date == null ? '' : obj.nextA1Date) +'</td>'+
			'<td>'+obj.a1km+'</td>'+
			'</tr>'+
			
    		'<tr>'+
			'<td>A2</td>'+
			'<td>'+ (obj.beforeA2Date == null ? '' : obj.beforeA2Date) +'</td>'+
			'<td>'+ (obj.nextA2Date == null ? '' : obj.nextA2Date) +'</td>'+
			'<td>'+obj.a2km+'</td>'+
			'</tr>'+
			
    		'<tr>'+
			'<td>A3</td>'+
			'<td>'+ (obj.beforeA3Date == null ? '' : obj.beforeA3Date) +'</td>'+
			'<td>'+ (obj.nextA3Date == null ? '' : obj.nextA3Date) +'</td>'+
			'<td>'+obj.a3km+'</td>'+
			'</tr>'+
			
    		'<tr>'+
			'<td>A4</td>'+
			'<td>'+ (obj.beforeA4Date == null ? '' : obj.beforeA4Date) +'</td>'+
			'<td>'+ (obj.nextA4Date == null ? '' : obj.nextA4Date) +'</td>'+
			'<td>'+obj.a4km+'</td>'+
			'</tr>'+	
			
    		'<tr>'+
			'<td>A5</td>'+
			'<td>'+ (obj.beforeA5Date == null ? '' : obj.beforeA5Date) +'</td>'+
			'<td>'+ (obj.nextA5Date == null ? '' : obj.nextA5Date) +'</td>'+
			'<td>'+obj.a5km+'</td>'+
			'</tr>'+			
			
			'</tbody></table>' ;
    	var dd = "<h2>'am Header2 </h2>" ;
        return reuslt; 
      };
      return filter;
});



//初始化
$(document).ready(function(){
	var appElement = document.querySelector('[ng-controller=kcyyBzInfoCtrl]');
	var $scope = angular.element(appElement).scope();
	$scope.findMarshallingList();
	$scope.findNotMarshallingList();
	$scope.findJxMarshallingList();
	
});

