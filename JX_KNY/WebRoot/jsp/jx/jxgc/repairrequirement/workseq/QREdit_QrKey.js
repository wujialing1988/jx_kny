/**
 * 作业工单编辑（质量控制配置在作业工单上） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QREdit');//定义命名空间
	QREdit.labelWidth = 20;
	QREdit.fieldWidth = 100 ;
	QREdit.width = 460 ;                //默认控件宽度
	QREdit.recordIDX = "" ;  //定义工序单主键
	QREdit.idx = "" ;  //定义工序卡主键
	QREdit.OperStatus = "add" ;     //定义操作状态
	
	//保存方法
	QREdit.saveFn = function(grid, editTabs){
		//表单验证是否通过
	    var form = grid.saveForm.getForm();        
	    var qualityForm = QREdit.saveForm.getForm(); 
        
	    if (!form.isValid()) return;
	    Ext.getCmp("trainType_comb").enable();
	    var data = form.getValues();
	    delete data["isVirtual"];	    
	    if(Ext.isEmpty(data.buildUpTypeIDX) || Ext.isEmpty(data.buildUpTypeCode)){
	    	if(data.workSeqType == "buildUp" || data.workSeqType == "disassembly"){
	    		MyExt.Msg.alert("零部件型号为空，检修类型不能为【组装】或【拆卸】！");
	    		return;
	    	}
	    }
	    
	    
	    //data.workSeqCode = Ext.getCmp("workSeqCodeId").getValue();
	    
	    if(QREdit.recordIDX == ""){
			data.recordIDX = data.recordIDX;
	    }else{
			data.recordIDX = QREdit.recordIDX;
	    }
        
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
        
	    grid.loadMask.show();
	    var cfg = {
	        scope: grid, 
	        url: grid.saveURL, 
	        jsonData: data, params: {qualityControls: Ext.util.JSON.encode(objs)},
	        success: function(response, options){
	            grid.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	            	if(result.entity != null){
	            		QREdit.idx = result.entity.idx ;
		            	Ext.getCmp("entityID").setValue(result.entity.idx);
	            	}
	            	
	            	WorkStep.grid.store.load(); 
	            	editTabs.activate(1);
			    	editTabs.get(1).enable();
			    	if (editTabs.get(2))
			    		editTabs.get(2).enable();
			    	grid.afterSaveSuccessFn(result, response, options)
	            } else {
	                grid.afterSaveFailFn(result, response, options);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	
	//机车配件作业工单新增按钮操作方法
	QREdit.addFn = function(grid, editTabs, saveWin){
		
		grid.saveForm.getForm().reset();
        QREdit.clearQC();//清空质量选择项
		JX.clearValue(saveWin);
		
		editTabs.activate(0);
		editTabs.get(1).disable();
		if (editTabs.get(2))
			editTabs.get(2).disable();
	    saveWin.show();
	}
	//机车配件作业工单编辑操作方法
	QREdit.toEditFn = function(grid, rowIndex, e, saveWin, editTabs){
		if(this.searchWin != null)  this.searchWin.hide();  
	        var record = grid.store.getAt(rowIndex);
	        QREdit.OperStatus = "edit";
	        QREdit.idx = record.get("idx") ;
	        
	        //显示编辑窗口
	        if(grid.isEdit){
	        	editTabs.activate(0);
		    	editTabs.get(1).enable();
		    	if (editTabs.get(2))
		    		editTabs.get(2).enable();
		    	WorkStep.grid.store.load();  //刷新工步
		    	//PartsList.grid.store.load();//刷新配件清单
		        saveWin.show();
		        grid.saveForm.getForm().reset();
		        grid.saveForm.getForm().loadRecord(record);
		        Ext.getCmp("trainType_comb").setDisplayValue(record.get("pTrainTypeIDX"),record.get("pTrainTypeShortName"));
		        //Ext.getCmp("PartsTypeByBuild_SelectWin_Id").setDisplayValue(record.get("buildUpTypeCode"),record.get("buildUpTypeCode"));
				
	        }
	}
	
	//定义质量控制编辑表单form
	QREdit.saveForm = null;
	//创建工单编辑页面质量控制表单
	QREdit.createSaveForm = function(){
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
	        editor.width = QREdit.fieldWidth ;        
	        saveFields.push(editor);
	    }
	        
	    var formItems = { xtype:'checkboxgroup', id :'qcCheckGroup', name: "checkItemCode", fieldLabel: '质量检查', items: saveFields};
	    
	    //生成FormPanel
	    QREdit.saveForm = new Ext.form.FormPanel({
	        layout: "form",     border: false,      style: "padding-left:20px",      labelWidth: 120,
	        baseCls: "x-plain", align: "center",    defaults: { anchor: "98%" },
	        items: formItems
	    }); 
	}
	QREdit.clearQC = function(){
		for(var j = 0; j < objList.length; j++){
			var checkobj = Ext.getCmp("checkItemId" + j);
			if(!Ext.isEmpty(checkobj))	checkobj.setValue(false);  //设置该检验项为非选择
		}
	}
	//设置作业工单的质量控制值选择情况
	QREdit.SetWorkSeqQC = function(workSeqIDX, grid){
		var cfg = {
	        scope: grid, 
	        url: ctx + '/qualityControl!findWorkStepQC.action', 
	        params: {idx: workSeqIDX },
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
