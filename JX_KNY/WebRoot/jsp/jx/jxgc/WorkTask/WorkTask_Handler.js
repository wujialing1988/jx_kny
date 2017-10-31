/*
 * 处理作业工单
 */
Ext.namespace("handler");

function checkBatch(val){
	var x = val.split('|');
	if(x[0] != '0'){	//有未完成的检修项目
		
		if(x[2] != x[0]){	//默认结果与未完成项目数不一致
			
			return false;
			
		}else if(x[1] !='0'){	//有录入数据项未填
			
			return false;
		}
	}
	return true;
}

handler.currentIdx;//标识唯一处理的作业任务

handler.allNot = function(status){
	return "请选择<span style='font-weight:bold'>" + status + "</span>的记录";
}
handler.partialNot = function(count, status){
	return count + "条记录非<span style='font-weight:bold'>" + status + "</span>状态，被自动过滤";
}
/*
 * 事件处理 
 */

//完工前处理的事件
handler.beforeComplete = function (){
	if(!$yd.isSelectedRecord(handler.grid,true)) return;
	var record = handler.grid.selModel.getSelections();
	var ids = [];
	var filter = 0;
	var f2 = 0;
	var removes = [];
	for(var i = 0; i < record.length; i++){	    			
		if(!checkBatch(record[i].get("batch"))){
			removes.push(handler.grid.store.indexOfId(record[i].get("idx")));
			f2++;
			continue;
		}		
		ids.push(record[i].get("idx"));
	}
	for(var i = 0; i < removes.length; i++){
		//取消被过滤数据的选择状态
		handler.grid.selModel.deselectRow(removes[i]);
	}	
	//提示不能批量完工的数据
	if(f2 != 0){
		if(filter + f2 == record.length){//当不能完工的和不能批量完工的加起来等到选择的记录数
			
			if(record.length == 1){ //只有一个不能批量的任务，弹到作业任务界面去。
				var r = handler.grid.store.getById(record[0].get("idx"));
				var index = handler.grid.store.indexOf(r) ;
				handler.grid.toEditFn(handler.grid, index);
				win.editTabs.activate(1);
				MyExt.Msg.alert("尚有未完成的检测/修项目");
				return;
			}else{//超过一条数据就给予提示
				MyExt.Msg.alert("所选数据尚不能批量完工");
				return;
			}
		}else if(f2 > 0 && ids.length > 0){
			MyExt.Msg.alert(f2 + "个任务不能批量，被自动过滤");			
		}
	}
	//处理完工
	handler.complete(ids+"");
}
//完工处理事件
handler.complete = function(ids, call){
	if(!ids){
		MyExt.Msg.alert("操作失败！");
		return;
	}
	handler.currentIdx = ids;
	var idx = ids.split(",");
	jQuery.ajax({
		url: ctx + "/workTask!checkWorkTaskAllComplete.action",
		type:"post",
		data:{workCardIdx: idx + ""},//取第一条作业卡的开工时间作为开工时间
		dataType:"json",
		success:function(data){
			if(data.status=='success'){
				if(data.canCompleteWorkCard == 'true'){					
					var qcList = data.qcList;
					form.createForm(qcList);
					win.createWin(qcList);
					win.completeConfirmWin.show();//显示提交窗口
					var completeConfirm = form.completeConfirm.getForm();
					completeConfirm.findField("realBeginTime").setValue("");
					completeConfirm.findField("remarks").setValue("");
					completeConfirm.findField("realBeginTime").setValue(data.realBeginTime);
					completeConfirm.findField("remarks").setValue(data.remarks.replace(/\\r\\n/g, '\r\n'));
				}else{
					if(idx.length == 1){
						var r = handler.grid.store.getById(idx[0]);
						var index = handler.grid.store.indexOf(r) ;
						handler.grid.toEditFn(handler.grid, index);
						win.editTabs.activate(1);
						MyExt.Msg.alert("尚有未完成的检测/修项目");
					}
				}
			}else{
				alertFail(data.ex);
			}
		}
	});
}

handler.beforeSubmit = function(call) {
	var baseForm = form.baseForm.getForm();
	var idx = baseForm.findField("idx").getValue();
	if (Ext.isEmpty(idx)) {
		MyExt.Msg.alert("作业工单主键为空，操作失败！");
		return;
	}
	var data = baseForm.getValues();
	if (!baseForm.isValid()) return;
	if(data.realBeginTime > data.realEndTime){
		MyExt.Msg.alert("开工时间不能大于等于完工时间");
		return;
	}
	
	var cfg = { 
        url: ctx + "/workTask!checkWorkTaskAllComplete.action",
        params: {
        	workCardIdx: idx
        },
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
			if(result.status == 'success'){
				if(result.canCompleteWorkCard == 'true'){	
					var qcList = result.qcList;
					handler.submit(qcList);
				} else {
//					if(idx.length == 1) {
//						win.editTabs.activate(1);
//						MyExt.Msg.alert("尚有未完成的检测/修项目");
//					}
					win.editTabs.activate(1);
					MyExt.Msg.alert("尚有未完成的检测/修项目");
				}
			}else{
				alertFail(result.ex);
			}
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}

handler.submit = function(qcList) {
	var baseForm = form.baseForm.getForm();	
	var qcDatas = [];
	if (qcList != null && qcList.length > 0) {
	    for (var i = 0; i < qcList.length; i++) {
	        var field = qcList[ i ];
	        var qcData = {};  //定义检验项
	        qcData.checkItemCode = field.checkItemCode;
	        if(!baseForm.findField(field.checkItemCode).getValue()) {
	        	MyExt.Msg.alert("请选择质量检查【" + field.checkItemName + "】指派人员！");
				return;
	        }
	        qcData.qcEmpID = baseForm.findField(field.checkItemCode).getValue();
	        qcDatas.push(qcData);			        
	    }  
	}
    var workerID = "";
    var workerValue = baseForm.getValues().workerID;
    if (Ext.isArray(workerValue)) {
		for (var i = 0; i < workerValue.length; i++) {
			workerID += workerValue[i] + ",";
		}
		workerID = workerID.substring(0, workerID.length - 1);
	} else {
		workerID = workerValue;
	}

	Ext.Msg.confirm("提示","确认提交工单", function(btn){
		if(btn == 'yes'){
			showtip(win.handlerWin.getEl());
			//批量则取批量的IDX，否则取作业卡唯一标识
			var entityJson = {};
			var data = baseForm.getValues();
			entityJson.idx = handler.currentIdx;
			entityJson.realBeginTime = data.realBeginTime;
			entityJson.realEndTime = data.realEndTime;
			entityJson.remarks = data.remarks;
			entityJson.workerID = workerID;
			
			var cfg = { 
		        url: ctx + "/workCard!completeWorkCard.action",
		        jsonData: qcDatas, 
		        params: {
		        	entityJson: Ext.util.JSON.encode(entityJson)
		        },
		        success: function(response, options){
		            hidetip();
		            var result = Ext.util.JSON.decode(response.responseText);
					if(result.success){
						alertSuccess("操作成功！");
					}else{
						alertFail(result.errMsg);
					}
					//重新加载数据
					handler.grid.store.load();
					handled.grid.store.load();
					//关闭窗口
					win.handlerWin.hide();
//					win.handlerWin = null;
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
	});
}

/*
 * 按钮切换
 */
handler.btnSwitch = function(){
	for(var i = 0; i < btns.length; i++){
		var isHidden = Ext.getCmp(btns[i]).hidden;
		Ext.getCmp(btns[i]).setVisible(false);//前面的按钮都隐藏掉
		if(!isHidden){
			if(i == btns.length -1){
				win.handlerWin.hide();
			}else{
				Ext.getCmp(btns[i+1]).setVisible(true);
				//隐藏显示作业任务的保存按钮
				Ext.getCmp("saveWorkTask").setVisible(i == 1);
			}
			if(handler.show2Btn ==2 || i == 1){//当开工之后
				handler.btnShowHide(status_handling);//该启用控件就启用控件，该 显示按钮就显示按钮
			}
			if(handler.show2Btn == 2){
				handler.show2Btn = 0;//如果为2则不跳出循环，因为点击了开工，需要循环两次显示出完工按钮
			}else{
				break;
			}
		}
	}
}

/*** 查询表单 start ***/
handler.searchLabelWidth = 90;
handler.searchAnchor = '95%';
handler.searchFieldWidth = 270;
handler.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: handler.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: handler.searchFieldWidth, labelWidth: handler.searchLabelWidth, defaults:{anchor:handler.searchAnchor},
			items:[{
				fieldLabel:'生产任务单',		
				id:"Baserdp_combo",
				xtype: 'Base_combo',
				hiddenName: 'rdpIDX',
				entity: 'x',
				action: ctx + "/trainWorkPlanQuery!findComboDataByWorkCardQuery.action",
				fields: ["rdpText", "idx"],
		    	minChars:50,
		    	maxLength:100,
		    	displayField:'rdpText',
		    	valueField:'idx',
				listeners : {
					"select" : function(cbo, rd, e) {						
						Ext.getCmp("tecprocessnode_node").queryParam = this.getValue();						
//						var position = Ext.getCmp("position");						
//						position.partsBuildUpTypeIdx = rd.get("buildUpTypeIDX");						
//						position.partsBuildUpTypeName = rd.get("buildupTypeName");
//						position.tree.root.setText(rd.get("buildupTypeName"));
//						if(position.expandx)
//							position.tree.getRootNode().reload();
					}
				}
			},{
				fieldLabel:'作业工单名称',
				xtype: "textfield",
				name: "workCardName"			
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
			fieldWidth: handler.searchFieldWidth, labelWidth: handler.searchLabelWidth, defaults:{anchor:handler.searchAnchor},
			items:[{
				fieldLabel:'流程节点',
				id: "tecprocessnode_node",
				xtype: "TecProcessNodeSelect_comboTree",
				dataurl: ctx + "/jobProcessNodeQuery!findNodeTree.action",
				hiddenName: "nodeCaseIDX",
				selectNodeModel: "all",
				listeners : {
					"beforequery" : function(){
						var baseRdp =  Ext.getCmp("Baserdp_combo").getValue();
						if(baseRdp == "" || baseRdp == null){
							MyExt.Msg.alert("请先选择生产任务单！");
							return false;
						}
					}
		  		}
			}
//			,{
//				id: 'position', xtype: 'BuildUpType_comboTree', fieldLabel: '安装位置',
//				hiddenName: 'fixPlaceFullName',				  
//				listeners:{
//					"beforequery" : function(){
//						var baseRdp =  Ext.getCmp("Baserdp_combo").getValue();
//						if(baseRdp == "" || baseRdp == null){
//							MyExt.Msg.alert("请先选择生产任务单！");
//							return false;
//						}
//					},
//					"expand": function(){
//						this.expandx = true;
//					}
//				}
			]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = handler.searchForm.getForm();
		        var searchParam = form.getValues();
                searchParam = MyJson.deleteBlankProp(searchParam);
				handler.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = handler.searchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo","TecProcessNodeSelect_comboTree","BuildUpType_comboTree"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
            	searchParam = {};
            	handler.grid.searchFn(searchParam);
            }
		}]
});
/*** 查询表单 end ***/
/*
 * GRID定义
 */
handler.grid = new Ext.yunda.Grid({
	loadURL: ctx + '/workCard!findWorkCard.action',             //装载列表数据的请求URL
    saveFormColNum:3, searchFormColNum:2,
    viewConfig: {forceFit: false, markDirty: false}, 
    tbar:[{
    	text: "完工",
    	iconCls: "pluginIcon",
    	handler: handler.beforeComplete//完工任务事件处理
    },'refresh'],
	fields: [
	    Attachment.createColModeJson({ attachmentKeyName:'JXGC_Work_Card' }),
	{
		header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'作业名称', dataIndex:'workCardName', width:300, editor: { }, searcher:{ anchor:'98%' }
	},{
		header:'作业编码', dataIndex:'workCardCode', editor: { }, searcher:{disabled:true}, hidden:true
	},
	/*{
		header:'位置', dataIndex:'fixPlaceFullName', width:250, editor: { }
	},
	*/{		
		header:'车型车号', dataIndex:'trainTypeAndNo', editor: { }
	},{
		header:'车型', dataIndex:'trainSortName', editor: { } ,hidden:true
	},{
		header:'车型', dataIndex:'trainTypeIdx', width:50, editor: { },hidden:true
	},{
		header:'车号', dataIndex:'trainNo', editor: { } ,hidden:true
	},{
		header:'修程修次', dataIndex:'repairClassRepairTime', editor: { }, width:85
	},{
		header:'修程', dataIndex:'repairClassName', editor: { }, width:50, hidden:true
	},{	
		header:'修次IDX', dataIndex:'repairTimeIdx', editor: { }, hidden:true
	},{
		header:'修程IDX', dataIndex:'repairClassIdx', editor: { }, hidden:true
	},{
		header:'作业内容', dataIndex:'workScope', width:250, editor: { }, searcher:{disabled:true}, hidden:true
	},{
		header:'批量', dataIndex:'batch', editor: { }, width:50, sortable:false,
		renderer:function(v,x,r){
			var y = '<span style="color:green">是</span>';
			var n = '<span style="color:red">否</span>';
			if(checkBatch(v)){
				return y;
			}else{
				return n;
			}
		}, searcher:{disabled:true}
	},{
		header:'流程节点', dataIndex:'nodeCaseName', editor: { }
	},{
		header:'检修活动', dataIndex:'repairActivityName', hidden: true, editor: { }
	},/*{
		header:'零部件名称', dataIndex:'partsName', hidden: true, editor: { }
	},{
		header:'零部件型号', dataIndex:'specificationModel', hidden: true, editor: { }
	},{
		header:'零部件编号', dataIndex:'nameplateNo', hidden: true, editor: { }
	},{
		header:'配件编号', dataIndex:'partsNo', editor: { }, hidden:true
	},*/{
		header:'状态', dataIndex:'status', hidden:true, editor: { },searcher:{disabled:true}
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor: { }, searcher:{disabled:true}
	},{
		header:'修程修次', dataIndex:'repairClassRepairTime', hidden:true, editor: { }
	},{
		header:'车型车号', dataIndex:'trainTypeTrainNo', hidden:true, editor: { }
	},{
		header:'转入时间', dataIndex:'transinTimeStr', hidden:true, editor: { }
	},{
		header:'计划交车时间', dataIndex:'planTrainTimeStr', hidden:true, editor: { }
	},{
		header:'工位名称', dataIndex:'workStationName', hidden:true, editor: { }
	},/*{
		header:'工位负责人名称', dataIndex:'dutyPersonName', hidden:true, editor: { }
	},{
		header:'额定工时', dataIndex:'ratedWorkHours', hidden:true, editor: { }
	},*/{
		header:'注意事项', dataIndex:'safeAnnouncements', hidden:true, editor: { }
	},/*{
		header:'互换配件信息主键', dataIndex:'partsAccountIDX', hidden:true, editor: { }
	},{
		header:'互换配件型号主键', dataIndex:'partsTypeIDX', hidden:true, editor: { }
	},{
		header:'计划开工时间', dataIndex:'planBeginTimeStr', hidden:true, editor: { }
	},{
		header:'计划完工时间', dataIndex:'planEndTimeStr', hidden:true, editor: { }
	},{
		header:'fixPlaceIDX', dataIndex:'fixPlaceIDX', hidden:true, editor: { }
	},{
		header:'buildUpTypeName', dataIndex:'buildUpTypeName', hidden:true, editor: { }
	},{
		header:'buildUpTypeIDX', dataIndex:'buildUpTypeIDX', hidden:true, editor: { }
	},{
		header:'fixPlaceFullCode', dataIndex:'fixPlaceFullCode', hidden:true, editor: { }
	},
	*/{
		header:'rdpIdx', dataIndex:'rdpIDX', hidden:true, editor: { }
	},{
		header:'status', dataIndex:'status', editor:{}, hidden:true
	},{
		header:'开工时间', dataIndex:'realBeginTimeStr', 
		editor:{ xtype:'hidden' }, hidden:true
	}],
	//查询处理事件
	searchFn:function(searchParam){
		handler.searchParam = searchParam ;
        this.store.load();
	},//查询按钮点击事件
    searchButtonFn: function(){                         //点击查询按钮触发的函数
        //判断查询窗体是否为null，如果为null则自动创建后显示
        if(this.searchWin == null)  this.createSearchWin();
        if(this.saveWin)    this.saveWin.hide();
        this.searchWin.setTitle("待处理作业工单查询");
        this.searchWin.show();
    },//双击处理事件
    toEditFn:function(grid, ri, e){    	
    	var record = grid.store.getAt(ri);
    	handler.currentIdx = record.get("idx");//设置唯一操作主键 
    	var cfg = {
	        url: ctx + '/workCardQuery!getWorkerAndQcByCard.action',
	        params: {workCardIDX: record.get("idx"), empid: empid},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
		            var workerList = null;
		            var qcList = null;
		            if (result.workerList)
		            	workerList = Ext.util.JSON.decode(result.workerList);
		            if (result.qcList)
		            	qcList = Ext.util.JSON.decode(result.qcList);
		            form.createWorkerForm(qcList, workerList);		            
		            win.createHandlerWin();
		            win.handlerWin.show();
			    	form.titleForm.getForm().reset();
			    	form.titleForm.getForm().loadRecord(record);
			    	
			    	form.baseForm.getForm().reset();
			    	form.baseForm.getForm().loadRecord(record);
			    	taskItem.grid.store.load();//刷新作业任务
    				Worker.grid.store.load();//刷新作业人员
    				
    				form.baseForm.getForm().findField("realBeginTime").setValue(record.get("realBeginTimeStr"));
    				form.baseForm.getForm().findField("realEndTime").setValue(new Date().format('Y-m-d H:i'));
    				
    				Ext.getCmp("saveWorkTask").setVisible(true);
					Ext.getCmp("saveParts").setVisible(true);
		        }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    }
});
/*
 * 数据过滤
 */
handler.grid.store.on("beforeload",function(){
	var status = [];
	var searchParam = MyJson.clone(handler.searchParam) || {};
	searchParam.status = "#[t]#"+ workCardHandling;	
	searchParam = MyJson.deleteBlankProp(searchParam);
	var json = Ext.util.JSON.encode(searchParam);
	this.baseParams.entityJson = json;
});