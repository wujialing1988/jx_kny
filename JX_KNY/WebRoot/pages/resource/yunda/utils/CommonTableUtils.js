	/**
	 * 设置bootstrap-table需要的数据格式
	 */
 	function responseHandler(res){
 		var resultStr = res;
 		if(resultStr != null){
 			if(resultStr.totalProperty == null || resultStr.totalProperty == "" && resultStr.root != null){
 				return {
	 				"rows":resultStr.root,
	 				"total":0
 				};
 			}else if(resultStr.totalProperty != null && resultStr.totalProperty != "" && resultStr.root != null){
 				return {
	 				"rows":resultStr.root,
	 				"total":resultStr.totalProperty
 				};
 			}else{
 				return {
	 				"rows":[],
	 				"total":0
 				};
 			}
 		}else{
 			return {
 				"rows":[],
 				"total":0
 			};
 		}
 	}
 	
 	/**
	 * 删除table中的数据信息
	 */
 	function delTableData($table,requestURL){
 		//obj为选择的table row数据对象，获取对象中的idx
 		var selRow = $table.bootstrapTable('getSelections');
 		if (null == selRow || selRow.length == 0) {
 			toastr.info("请至少选取一条要删除的数据行!");
 			return;
 		}
 		//为datas赋值
 		var jsonArr = [];
 		if(selRow != null && "" != selRow){
 			for(var i = 0 ; i < selRow.length ; i ++){
 				jsonArr.push(selRow[i].idx);
 			}
 		}
 		Ewin.confirm({ message: "您真的确定要删除吗?" }).on(function (e) {
	        if (!e) {
	            return;
	        }
	        $.ajax({
		 			 type: "POST",
		             cache:false,
		             async : true,
		             dataType : "json",
		             url:  requestURL,
		             data: {"ids":JSON.stringify(jsonArr)},
		             success:function(data){
		             	toastr.success("删除成功");
		             	$table.bootstrapTable('refresh')
		            }
		 	});
	 });
 	}
 	
 	
 	/**
	 * bootstrap-table需要的公用的表格属性
	 * 1、提交方式默认为post提交方式
	 * 2、默认数据类型为json数据格式
	 */
 	function getTableCommonAttribute(){
 		/**
 		 * bootstrap-table需要的公用的表格属性设置
 		 */
 		var option ={
 			method:'post',
 			dataType: "json",
 			classes :"table-no-bordered",	//设置表的class，默认为由边框的table，无边框table可加table-no-bordered
 			pagination: true, 				//是否显示分页   
 			pageSize:30,					//
    		pageNumber:1,
    		silent:true,
    		//height:$(document).height()-100,
    		pageList:['10','30','50','100'],
    		sidePagination: "server", 		//服务端处理分页
    		queryParamsType:"limit",
    		contentType:"application/x-www-form-urlencoded",//适用post-form的形式提交数据
    		responseHandler:responseHandler,//返回值处理为table需要的格式
    		clickToSelect:true,				//点击时自动选择
    		paginationPreText:'上一页',		//指定上一页显示的文字或者图表
    		paginationNextText:'下一页',		//指定下一页显示的文字或者图表
    		singleSelect:false,				//是否禁止多选，默认false
    		idField:"idx"					//定义主键
 		}
 		return option;
 	}
 	
 	
 	var yundatable = $.extend({
 		method:'post',
		dataType: "json",
		classes :"table-no-bordered",	//设置表的class，默认为由边框的table，无边框table可加table-no-bordered
		pagination: true, 				//是否显示分页   
		pageSize:30,					//
		pageNumber:1,
		silent:true,
		//height:$(document).height()-100,
		pageList:['10','30','50','100'],
		sidePagination: "server", 		//服务端处理分页
		queryParamsType:"limit",
		contentType:"application/x-www-form-urlencoded",//适用post-form的形式提交数据
		responseHandler:responseHandler,//返回值处理为table需要的格式
		clickToSelect:true,				//点击时自动选择
		paginationPreText:'上一页',		//指定上一页显示的文字或者图表
		paginationNextText:'下一页',		//指定下一页显示的文字或者图表
		singleSelect:false,				//是否禁止多选，默认false
		idField:"idx"				//定义主键
 	})
 	

 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	