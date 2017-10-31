/**
 * 作业工单编辑（质量控制配置在作业任务上） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QREdit');//定义命名空间
	QREdit.labelWidth = 20;
	QREdit.fieldWidth = 100 ;
	QREdit.width = 460 ;                //默认控件宽度
	QREdit.idx = "" ;  //定义工序卡主键
	QREdit.OperStatus = "add" ;     //定义操作状态	
	//保存方法
	QREdit.saveFn = function(grid, editTabs){
		//表单验证是否通过
	    var form = grid.saveForm.getForm(); 
	    if (!form.isValid()) return;
	    var data = form.getValues();
	    delete data["isVirtual"];	    
	    if(Ext.isEmpty(data.buildUpTypeIDX) || Ext.isEmpty(data.buildUpTypeCode)){
	    	if(data.workSeqType == "buildUp" || data.workSeqType == "disassembly"){
	    		MyExt.Msg.alert("零部件型号为空，检修类型不能为【组装】或【拆卸】！");
	    		return;
	    	}
	    }
	    data.workSeqCode = Ext.getCmp("workSeqCodeId").getValue();
	    data.chartNo = Ext.getCmp("chartNo_Id").getValue();
	    data.buildUpTypeName = Ext.getCmp("buildUpTypeName_Id").getValue();
	    data.buildUpType = buildUpType;
	    if(buildUpType == BUILD_TYPE_PARTS){
	    	data.pPartsName = Ext.getCmp("pPartsName_Id").getValue();
	    }
        
	    grid.loadMask.show();
	    var cfg = {
	        scope: grid, 
	        url: grid.saveURL, 
	        jsonData: data,
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
			    	grid.afterSaveSuccessFn(result, response, options)
	            } else {
	                grid.afterSaveFailFn(result, response, options);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	//机车配件质量记录单新增按钮操作方法
	QREdit.addFn = function(grid, editTabs, saveWin){
		Ext.Ajax.request({
	        url: ctx + "/codeRuleConfig!getConfigRule.action",
	        params: {ruleFunction : "JXGC_WORK_SEQ_WORK_SEQ_CODE"},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            Ext.getCmp("workSeqCodeId").setValue(result.rule) ;
	        }
		});
		grid.saveForm.getForm().reset();
		JX.clearValue(saveWin);		
		editTabs.activate(0);
		editTabs.get(1).disable();
	    saveWin.show();
	}
	//机车配件质量记录单编辑操作方法
	QREdit.toEditFn = function(grid, rowIndex, e, saveWin, editTabs){
		if(this.searchWin != null)  this.searchWin.hide();  
	        var record = grid.store.getAt(rowIndex);
	        QREdit.OperStatus = "edit";
	        QREdit.idx = record.get("idx") ;
	        
	        //显示编辑窗口
	        if(grid.isEdit){
	        	editTabs.activate(0);
		    	editTabs.get(1).enable();
		    	WorkStep.grid.store.load();  //刷新工步
		        saveWin.show();
		        grid.saveForm.getForm().reset();
		        grid.saveForm.getForm().loadRecord(record);
		        if(buildUpType == BUILD_TYPE_TRAIN){
		        	Ext.getCmp("trainType_comb").setDisplayValue(record.get("pTrainTypeIDX"),record.get("pTrainTypeShortName"));
		        }else if(buildUpType == BUILD_TYPE_PARTS){
		        	Ext.getCmp("partsType_comb").setDisplayValue(record.get("pPartsTypeIDX"),record.get("pSpecificationModel"));
		        }
		        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").setDisplayValue(record.get("buildUpTypeCode"),record.get("buildUpTypeCode"));
				
	        }
	}
});
