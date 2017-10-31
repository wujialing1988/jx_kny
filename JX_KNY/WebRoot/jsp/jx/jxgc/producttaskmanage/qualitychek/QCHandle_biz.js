Ext.onReady(function(){
	
	Ext.namespace("Biz");
	
	/**
	 * 当前操作
	 */
	Biz.Operating = 0;
	/**
	 * 已操作完毕
	 */
	Biz.Operated = true;
	
	/**
	 * 获取选中行记录
	 */
	Biz.getRecords = function(grid){
		return grid.selModel.getSelections();
	}
	
	
	
	/**
	 * 操作条件检查
	 */
	Biz.conditionCheck = function(grid, operating, tipText){
		var data = grid.selModel.getSelections();
		var len = data.length ;
		
		if(len == 0 && len == 0){					
			MyExt.Msg.alert("没有选择数据");
			return false;
		}
		return true;
	}
	
	
	/**
	 * 单个质检任务提交
	 */
	Biz.QCSubmit = function(){
		Ext.Msg.confirm("提示","确认提交", function(btn){
			if(btn == "yes"){
				Biz.Operated = false;
				var data = Base.checkForm.getForm().getValues();
				var r = QCHandle.currentRecord;
				var qcDatas = [];
				data.workCardIDX = r.get("sourceIdx");
				data.checkItemCode = r.get("checkItemCode");
				qcDatas.push(data);
				 
				Ext.Ajax.request({
			    	url: ctx +"/qCResult!updateFinishQCResult.action",
			        jsonData: qcDatas,
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.success) {
			            	alertSuccess();
			            	QCHandle.handlerWin.hide();
			            	Biz.reloadGrid();
			            } else {
			                alertFail(result.errMsg);
			            }
			            Biz.Operated = true;
			        },
			        failure: function(response, options){
			        	Biz.Operated = true;
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    	}
				});
			}
		});
	}
	
	
	
	/**
	 * 质检批量提交
	 */
	Biz.QCBatchSubmit = function(grid){
		if(Biz.conditionCheck(grid, true, '质检') == false){
			return;
		}
		Ext.Msg.confirm("提示","确认提交", function(btn){
			if(btn == "yes"){
				Biz.Operated = false;
				var recds = Biz.getRecords(grid);
				var qcDatas = [];
				for(var i = 0; i < recds.length; i++){
					qcDatas.push({						
						workCardIDX: recds[i].get("sourceIdx"),
						checkItemCode: recds[i].get("checkItemCode"),
						qcEmpID: empid,
						qcEmpName: uname,
						qcTime: new Date()						
					});
				}
				Ext.Ajax.request({
					url: ctx +"/qCResult!updateFinishQCResult.action",
			        jsonData: qcDatas,
			        success: function(response, options){
			        	var result = Ext.util.JSON.decode(response.responseText);
			            if (result.success) {
			            	alertSuccess();
			            	Biz.reloadGrid();
			            } else {
			                alertFail(result.errMsg);
			            }
			            Biz.Operated = true;
			        },
			        failure: function(response, options){
			        	Biz.Operated = true;
			        	MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    	}
				});
			}
		});
	}
	
	Biz.reloadGrid = function(){
		if(QCHandle.tabs.getActiveTab().title == "必检"){
    		QCHandle.grid.store.reload();
    	}else{
    		Async.grid.store.reload();			            		
    	}
	}
});