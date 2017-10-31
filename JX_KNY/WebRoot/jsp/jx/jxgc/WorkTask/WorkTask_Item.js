Ext.namespace("taskItem");
taskItem.currentIdx;//作业卡唯一处理标识
taskItem.texts = [];//特殊字符数组
//获取特殊字符集
var cfg = {
    url: ctx + '/systemChar!pageQuery.action', 
    success: function(response, options){
        var result = Ext.util.JSON.decode(response.responseText);
        if (result.root != null) {
            for(var i = 0; i < result.root.length;i++ ){
            	taskItem.texts.push(result.root[i].specialChar);           
            }
        } 
    }
};
Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
taskItem.removeTbar = function(cmp){		
	//剔除作业任务Grid的tbar
//	var dom =taskItem.grid.tbar.dom;
//	if(dom){
//		dom.parentNode.removeChild(dom);
//		taskItem.grid.doLayout(true);
//		
//		dom =taskItem.dataItemGrid.tbar.dom;
//		if(dom){
//			dom.parentNode.removeChild(dom);
//			taskItem.dataItemGrid.doLayout(true);
//		}
//	}
	if(taskItem.grid.selModel.getCount() == 0 && taskItem.grid.store.getCount() > 0){
		setTimeout("taskItem.grid.toEditFn(taskItem.grid, 0)",1000);
	}
	cmp.un("activate",taskItem.removeTbar,cmp);	
}

taskItem.grid = new Ext.yunda.Grid({
	loadURL: ctx + '/workTask!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/workTask!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/workTask!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:3, searchFormColNum:2,
    storeAutoLoad:false,
    tbar:[], singleSelect:true,  pageSize:500, page: false,
    fields: [{
		header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'作业卡主键', dataIndex:'workCardIDX', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'工步主键', dataIndex:'workStepIDX', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'作业项编码', dataIndex:'workTaskCode', editor: { xtype:"hidden"},sortable:false, hidden:true
	},{
		header:'检测/修项目', dataIndex:'workTaskName', editor: { xtype:"hidden"},sortable:false,
		renderer:function(v,x,r){
			if(r.get("status") == task_finished){
				return "<span style='color:green'>" + v + "</span>"
			}else {
				return "<span style='color:red'>" + v + "</span>";
			}
		}
	},{
		header:'检修内容', dataIndex:'repairContent', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'技术要求或标准规定', dataIndex:'repairStandard', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'检修结果', dataIndex:'repairResult', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'状态', dataIndex:'status', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'备注', dataIndex:'remarks', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'repairMethod', dataIndex:'repairMethod', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'repairResultIdx', dataIndex:'repairResultIdx', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'workTaskSeq', dataIndex:'workTaskSeq', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'repairResult', dataIndex:'repairResult', editor: { xtype:"hidden"}, hidden:true
	}],
	toEditFn:function(grid,index,e){
		var record = grid.store.getAt(index);
		form.taskForm.getForm().reset();
		var taskForm = form.taskForm.getForm();
    	taskForm.findField("repairResult").queryParams = {"workStepIDX":'xxx'};
		if(record){
			try{
				taskItem.grid.selModel.selectRow(index);
			}catch(e){}
			form.taskForm.getForm().findField("repairResult").enable();
			loadResult(record.get("workStepIDX"), record.get("repairResult"));			
			form.taskForm.getForm().loadRecord(record);
			
			taskItem.currentIdx = record.get("idx");
			taskItem.dataItemGrid.store.load();	
			if(null == record.get("workStepIDX")) {
				taskForm.findField("repairResult").queryParams = {"workStepIDX":'xxx'};
			}
			else {
				taskForm.findField("repairResult").queryParams = {"workStepIDX": record.get("workStepIDX"), "recordStatus":"0"};
			}
			taskForm.findField("repairResult").cascadeStore();
		}else{
			taskForm.findField("workTaskName").setReadOnly(false);
			taskForm.findField("workTaskName").setValue("");
			taskForm.findField("workTaskName").setReadOnly(true);
			taskForm.findField("repairStandard").setReadOnly(false);
			taskForm.findField("repairStandard").setValue("");
			taskForm.findField("repairStandard").setReadOnly(true);
			taskForm.findField("repairResult").clearValue();
			taskForm.findField("repairResult").disable();			
			taskItem.currentIdx = "-1xxx";
			taskItem.dataItemGrid.store.removeAll();
		}
		
	},
	toAfterLoadFn:function(){
		this.toEditFn(this,0);
	}
});
//检测项
taskItem.dataItemGrid = new Ext.yunda.RowEditorGrid({
	loadURL: ctx + '/detectResult!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/detectResult!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/detectResult!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:3, searchFormColNum:2,
    tbar:['search'], singleSelect:true, storeAutoLoad:false, pageSize:500, page: false, 
    fields: [{
		header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'workTaskIDX', dataIndex:'workTaskIDX', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'检测项编码', dataIndex:'detectItemCode', editor: { xtype:"textfield"}, hidden:true
	},{
		header:'检测内容', dataIndex:'detectItemContent', editor: { xtype:"textfield", readOnly:true, style:"color:gray" }
	},{
		header:'检测项标准', dataIndex:'detectItemStandard', editor: { xtype:"textfield", readOnly:true, style:"color:gray" }
	},{
		header:'数据类型', dataIndex:'detectResulttype', editor: { }, hidden:false
	},{
		header:'检测结果', dataIndex:'detectResult',  
		editor: {id:'detectResult_Id', xtype: 'insertCharField', texts:taskItem.texts, maxLength: 100}
	},{
		header:'是否必填', dataIndex:'isNotBlank',
		/*
		editor: {
            id:"isNotBlank_text", 
            xtype:"textfield", 
            readOnly:true, 
            style:"color:gray" 
        },
		renderer : function(v){if(v==isNotBlank_yes)return "必填";else if(v == isNotBlank_no) return "非必填"; else return ""}
		*/
		renderer : function(v){if(v==isNotBlank_yes)return "必填";else if(v == isNotBlank_no) return "非必填"; else return ""},
		editor:{
			id:"isNotBlank_combo",
        	xtype: 'combo',
            fieldLabel: '是否必填',
            hiddenName:'isNotBlank',
            store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[isNotBlank_yes,'必填'],[isNotBlank_no,'非必填']]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
			value:isNotBlank_yes,
			readOnly:true
		}
	}],
	//行编辑之前判断是否能操作
	beforeEditFn:function(rowEditor, rowIndex){		
		if (Ext.getCmp("saveWorkTask").hidden) return false;
		var record = rowEditor.grid.store.getAt(rowIndex);
		setTimeout(function(){ //延迟加载该方法体，实现赋值
			//数据类型为数字则只能输入数字不能选择特殊字符
			if(record.get("detectResulttype") == '数字' || record.get("detectResulttype") == 'Number'){
				Ext.getCmp("detectResult_Id").isNumber = true;
                Ext.getCmp("detectResult_Id").vtype = "nonNegativeFloat";
			}else{
				Ext.getCmp("detectResult_Id").isNumber = false;
				Ext.getCmp("detectResult_Id").vtype = null;
			}
			var detectResult_Id = Ext.getCmp("detectResult_Id");
			detectResult_Id.setValue(record.get("detectResult"));
		},100)
        return true;
	},
	/**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Ext.ux.grid.RowEditor} rowEditor This object
     * @param {Object} changes Object with changes made to the record.
     * @param {Ext.data.Record} record The Record that was edited.
     * @param {Number} rowIndex The rowIndex of the row just edited
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(rowEditor, changes, record, rowIndex){
    	var isNotBlank = record.get("isNotBlank");
    	if(!Ext.isEmpty(isNotBlank) && isNotBlank == isNotBlank_yes && Ext.isEmpty(record.get("detectResult"))){
    		MyExt.Msg.alert("请填写【检测项】的【检测结果】");
    		record.cancelEdit();
    		record.set("detectResult",record.json.detectResult);//解决在输入框中将数据清空后页面不显示但实际有值的bug
    		return false;
    	} 
        if(record.get("detectResulttype") == '数字' || record.get("detectResulttype") == 'Number'){
            var result = record.get("detectResult");
            if((!isNaN(result)) && result == 0){
                MyExt.Msg.alert("该【检测项】的【检测结果】值不能为0");
	            record.cancelEdit();
	            record.set("detectResult",record.json.detectResult);//解决在输入框中将数据清空后页面不显示但实际有值的bug
	            return false;
            }
        }
        return true;
    },
	saveFn: function(rowEditor, changes, record, rowIndex){
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(rowEditor, changes, record, rowIndex)) {
            rowEditor.stopEditing(false);
            return;
        }
        record.data.detectResult = Ext.getCmp("detectResult_Id").getRawValue();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: record.data,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    }
});
//加载默认检修结果
function loadResult(idx, result){
	
	var taskForm = form.taskForm.getForm();
    taskForm.findField("repairResult").clearValue();
	taskForm.findField("repairResult").clearInvalid();
	if(result){
		taskForm.findField("repairResult").setDisplayValue(result, result);
		return;
	}
	jQuery.ajax({
		url: ctx + "/workStepResult!getDefaultValue.action",
		data:{stepIdx:idx},
		type:"post",
		dataType:"json",
		success:function(data){
			if(data.value)
				taskForm.findField("repairResult").setDisplayValue(data.value, data.value);
		}
	});
}
/*
 * 界面UI
 */
taskItem.UI = new Ext.Panel({
	layout:"border",
	frame:true,
	buttonAlign:"center",
    items:[{
        title : '',
        collapsible : true,
        width : 300,
        minSize : 200,
        maxSize : 300,
        split : true,
        region : 'west',
        layout: "fit",
        bodyBorder: true,
        autoScroll : true,
        items : [ taskItem.grid ]
    },{
		layout: "border",
		region : 'center',
        bodyBorder: true,
        autoScroll : true,
        frame:true,
        items : [{
		    height:200,
		    region : 'north',
		    bodyBorder: true,
		    autoScroll : false,
		    items : [ form.taskForm ]
		},{
			title:"检测项",
			layout: "fit",
			region : 'center',
		    bodyBorder: true,
		    autoScroll : true,
		    items : [ taskItem.dataItemGrid ]
		}]
    }],
    buttons:[{
		text: "保存", iconCls:"saveIcon", id:"saveWorkTask",
		handler:function(){
    	
    		if(Ext.getCmp("status").getValue() == task_finished || taskItem.grid.store.getCount() == 0){
    			
    			var count = taskItem.grid.store.getCount()
    			var fcount = 0;//完成的数量
        		var record;
        		for(var i = 0; i < count; i++){
        			record = taskItem.grid.store.getAt(i);
        			if(record.get("status") == task_finished){
        				fcount++;
        			}
        		}
    			if(fcount == count)
    				handler.complete(handler.currentIdx, handler.btnSwitch);//弹出完工确认窗口
    			else
    				MyExt.Msg.alert("任务已完成");
    			
				return;
    		}
    		
    		
	    	taskItem.submit();
    	}
	},{
		id:"closeWorkItem",text: "关闭", iconCls:"closeIcon",
		handler:function(){
			win.handlerWin.hide();
		}
	}]
});
/*
 * Grid默认排序
 */
taskItem.grid.store.setDefaultSort("status","desc");
taskItem.dataItemGrid.store.setDefaultSort("sortSeq","asc");
/*
 * 作业任务加载前处理事件
 */
taskItem.grid.store.on("beforeload", function(){
	if(!handler.currentIdx){
		taskItem.currentIdx = "-1xxx";
		taskItem.dataItemGrid.store.removeAll();
		return false;//未获取到唯一操作作业卡，不加载数据
	}
	var whereList = [];
	whereList.push({propName:"workCardIDX",propValue: handler.currentIdx});	
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
/*
 * 数据项加载前处理事件
 */
taskItem.dataItemGrid.store.on("beforeload",function(){
	if(!taskItem.currentIdx){
		return false;
	}
	var searchParam = {};
	searchParam.workTaskIDX = taskItem.currentIdx;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});	
//绑定加载之后处理事件
taskItem.grid.store.on("load", taskItem.grid.toAfterLoadFn, taskItem.grid);
//解除双击绑定事件
taskItem.grid.un('rowdblclick', taskItem.grid.toEditFn, taskItem.grid);
//绑定单击事件
taskItem.grid.on('rowclick', taskItem.grid.toEditFn, taskItem.grid);
/*
 * 提交作业任务方法
 */
taskItem.submit = function(){
	if(!form.taskForm.getForm().isValid()){
		var taskForm = form.taskForm.getForm();
		taskForm.findField("repairResult").dom.focus();
		return;
	}
	showtip(win.handlerWin.getEl());
	jQuery.ajax({
		url: ctx +"/detectResult!checkRetectResultAllComplete.action",
		type:"post",
		data:{workTaskIdx:Ext.getCmp("workTaskIdx").getValue()},
		dataType:"json",
		success:function(data){
			if(data.count >= 1){
				hidetip();
				MyExt.Msg.alert("尚有检测项未录入，请填写【检测项】中必填的【检测结果】");
				return;
			}else{
				var datax = form.taskForm.getForm().getValues();
				datax.status = task_finished; //设置状态为完成
				Ext.Ajax.request({
			        url: ctx +"/workTask!saveOrUpdate.action",
			        jsonData: datax,
			        success: function(response, options){
						hidetip();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
							taskItem.grid.store.load();
							alertSuccess();	    								
							handler.grid.store.load();//完成任务时刷新Grid，更新能否批量	    								
						}else{
							alertFail(result.errMsg);
						}
			        },
			        failure: function(response, options){
			        	hidetip();
			            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			        }
			    });
			}
		},
		error:function(){
			hidetip();
			MyExt.Msg.alert("请求失败");
		}
	});
}
