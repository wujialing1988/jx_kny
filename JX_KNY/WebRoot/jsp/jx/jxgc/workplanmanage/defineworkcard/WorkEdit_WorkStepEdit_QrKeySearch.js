/**
 * 工步编辑 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkStepEditSearch');                       //定义命名空间
	//定义组成对象
	WorkStepEditSearch.buildUpType = {} ;
	WorkStepEditSearch.labelWidth = 20;
	WorkStepEditSearch.fieldWidth = 100 ;
	WorkStepEditSearch.idx = "" ;  //定义工步主键
	WorkStepEditSearch.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	
	//定义质量控制编辑表单form
	WorkStepEditSearch.saveForm = null;
	//创建工步编辑页面质量控制表单
	WorkStepEditSearch.createSaveForm = function(){
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
	        editor.width = WorkStepEditSearch.fieldWidth ;
	        saveFields.push(editor);
	       
	    }
	    
	    var formItems = { xtype:'checkboxgroup', name: "checkItemCode", fieldLabel: '质量检查', items: saveFields, disabled: true};
	
	    //生成FormPanel
	    WorkStepEditSearch.saveForm = new Ext.form.FormPanel({
	        layout: "form",     border: false,      style: "padding-left:20px",      labelWidth: 100,
	        baseCls: "x-plain", align: "center",    defaults: { anchor: "98%" },
	        items: formItems
	    });
	}
	
	WorkStepEditSearch.clearQC = function(){
		
		for(var j = 0; j < objList.length; j++){
			var checkobj = Ext.getCmp("checkItemId" + j);
			if(!Ext.isEmpty(checkobj))	checkobj.setValue(false);  //设置该检验项为非选择
		}
	}
	//设置质量记录单的质量控制值选择情况
	WorkStepEditSearch.SetWorkSeqQC = function(workCardIDX, grid){
		var cfg = {
	        scope: grid, 
	        url: ctx + '/qCResultQuery!findCheckQC.action', 
	        params: {idx: workCardIDX },
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