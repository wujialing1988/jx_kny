/**
 * 工步编辑 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkStepEdit');                       //定义命名空间
	//定义组成对象
	WorkStepEdit.buildUpType = {} ;
	WorkStepEdit.labelWidth = 20;
	WorkStepEdit.fieldWidth = 100 ;
	WorkStepEdit.idx = "" ;  //定义工步主键
	WorkStepEdit.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	
	//定义质量控制编辑表单form
	WorkStepEdit.saveForm = null;
	//创建工步编辑页面质量控制表单
	WorkStepEdit.createSaveForm = function(){
	    //可现实的输入控件
	    var saveFields = [];
	    for (var i = 0; i < objList.length; i++) {
	        var field = objList[ i ];
	        var editor = {};  //定义检验项
	        editor.id = "checkItemId" + i;
	        editor.xtype = "checkbox";
	        editor.name = "checkItemCode"; //定义检验项名称规则
	        editor.boxLabel = field.checkItemName ;
	        editor.inputValue  = field.checkItemCode ;
	        editor.width = WorkStepEdit.fieldWidth ;
	        saveFields.push(editor);
	    }
	    var formItems = { xtype:'checkboxgroup', name: "checkItemCode", fieldLabel: '质量检查', items: saveFields};
	
	    //生成FormPanel
	    WorkStepEdit.saveForm = new Ext.form.FormPanel({
	        layout: "form",     border: false,      style: "padding-left:20px",      labelWidth: 100,
	        baseCls: "x-plain", align: "center",    defaults: { anchor: "98%" },
	        items: formItems
	    });
	}
	
	//定义获取工步编码的方法
	WorkStepEdit.saveFn = function(){
		//表单验证是否通过
		var form = WorkStep.stepForm.getForm();
	    var qualityForm = WorkStepEdit.saveForm.getForm(); 
	    if (!form.isValid()) return;
	    var data = form.getValues();
	    data.workCardIDX = TrainWork.idx ; //检修工序卡主键   
	    //获取质量控制选择值
	    var qualityData = qualityForm.getValues();
		    var objs = []; //质量控制对象
		    var length = objList.length ;
		    for (var i = 0; i < length; i++) {
		    	var obj = {} ;
		    	var checkobj = Ext.getCmp("checkItemId" + i);//获取检验项checkbox对象
		    	if(!Ext.isEmpty(checkobj) && checkobj.checked){
		    		obj.checkItemName = checkobj.boxLabel;
			    	obj.checkItemCode = checkobj.inputValue;
			    	objs.push(obj);
		    	}
		    }
		 //调用保存前触发函数，如果返回fasle将不保存记录
	    if(!WorkStep.grid.beforeSaveFn(data)) return;
	    WorkStepEdit.loadMask.show();
	    var cfg = {
	        scope: WorkStep.grid, 
	        url: WorkStep.grid.saveURL, 
	        jsonData: data, params: {qualityControls: Ext.util.JSON.encode(objs)},
	        success: function(response, options){
	            WorkStepEdit.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	            	if(result.entity != null){
	            		WorkStepEdit.idx = result.entity.idx ;
		            	Ext.getCmp("workTask_Id").setValue(result.entity.idx);
	            	}
			    	WorkStep.grid.afterSaveSuccessFn(result, response, options)
	            } else {
	                WorkStep.grid.afterSaveFailFn(result, response, options);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	WorkStepEdit.clearQC = function(){
		
		for(var j = 0; j < objList.length; j++){
			var checkobj = Ext.getCmp("checkItemId" + j);
			if(!Ext.isEmpty(checkobj))	checkobj.setValue(false);  //设置该检验项为非选择
		}
	}
	//设置质量记录单的质量控制值选择情况
	WorkStepEdit.SetWorkSeqQC = function(workStepIDX, grid){
		var cfg = {
	        scope: grid, 
	        url: ctx + '/qCResultQuery!findCheckQC.action', 
	        params: {idx: workStepIDX },
	        success: function(response, options){
	            if(grid.loadMask)   grid.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entityList != null) {
	            	for (var i = 0; i < result.entityList.length; i++) {
	            		var obj = result.entityList[i];        		
	            		if(obj.checkItemCode != null){ 
	            			for(var j = 0; j < objList.length; j++){
	            				var checkobj = Ext.getCmp("checkItemId" + j);
		            			if(checkobj.inputValue == obj.checkItemCode){
		            				checkobj.setValue(true);  //设置该检验项为选择
		            			}
	            			}
	            		}
	            	}
	            } else {
	                grid.afterSaveFailFn(result, response, options);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
});