/**
 * 批量调入 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('BatchMoveIn');                       //定义命名空间
BatchMoveIn.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTransferDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTransferDetail!saveOrUpdateList.action',             //保存数据的请求URL
    saveFormColNum: 2 ,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'调入类型', dataIndex:'type', editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'调拨类型', dataIndex:'transferType', editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'部命令号', dataIndex:'ministryOrder', editor:{  maxLength:20}
	},{
		header:'局命令号', dataIndex:'bureauOrder', editor:{  maxLength:20 }
	},{
		header:'部命令日期', dataIndex:'ministryOrderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'局命令日期', dataIndex:'bureauOrderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'配属局ID', dataIndex:'bId', editor:{
			id: "comboTree_bId_batch", xtype: "BureauSelect_comboTree",
			hiddenName: "bId", fieldLabel: "配属局", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "bName_batch", propertyName: "text"},
			  			  {widgetId: "bShortName_batch", propertyName: "orgname"}],
		    listeners : {
			  	"select" : function() {
			  		Ext.getCmp("comboTree_dId_batch").reset();
	                Ext.getCmp("comboTree_dId_batch").clearValue();
			  		Ext.getCmp("comboTree_dId_batch").orgid = this.getValue();
			  		Ext.getCmp("comboTree_dId_batch").orgname = this.lastSelectionText;
			  	}
			  }
		  }		
	},{
		header:'配属段ID', dataIndex:'dId', editor:{
			id: "comboTree_dId_batch", xtype: "DeportSelect_comboTree",
			hiddenName: "dId", fieldLabel: "配属段", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "dName_batch", propertyName: "text"},
			  			  {widgetId: "dShortName_batch", propertyName: "orgname"}],
		    listeners : {
				"beforequery" : function(){
					//选择段前先选局
					var comboTree_bId =  Ext.getCmp("comboTree_bId_batch").getValue();
					if(comboTree_bId == "" || comboTree_bId == null){
						MyExt.Msg.alert("请先选择配属局！");
						return false;
					}
				}
			}
		}
	},{
		header:'配属局名称', dataIndex:'bName', editor:{id:"bName_batch", xtype:'hidden' }
	},{
		header:'配属局简称', dataIndex:'bShortName', editor:{id:"bShortName_batch", xtype:'hidden', maxLength:300 }
	},{
		header:'配属段名称', dataIndex:'dName', editor:{id:"dName_batch", xtype:'hidden', maxLength:512 }
	},{
		header:'配属段简称', dataIndex:'dShortName', editor:{id:"dShortName_batch", xtype:'hidden', maxLength:300 }
	},{
		header:'配属单位', dataIndex:'holdOrgId', editor:{
			id:"holdOrgId_batch",xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "holdOrgId", fieldLabel: "配属单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea", //只能选段级单位
		    returnField: [{widgetId: "holdOrgNameId_batch", propertyName: "orgname"},
			  			  {widgetId: "holdOrgSeqId_batch", propertyName: "orgseq"}]
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', editor:{id:"holdOrgSeqId_batch", xtype:'hidden', maxLength:512 }
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', editor:{id:"holdOrgNameId_batch", xtype:'hidden', maxLength:300 }
	},{
		header:'支配单位ID', dataIndex:'usedOrgId', editor:{
			id:"usedOrgId_batch",xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "usedOrgId", fieldLabel: "支配单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea" //只能选段级单位
		}
	},{
		header:'调入日期', dataIndex:'transferDate', xtype:'datecolumn', editor:{ xtype:'my97date' , allowBlank: false }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	}]
});
//创建表单
BatchMoveIn.grid.createSaveForm();
//定义点击确定按钮的操作
BatchMoveIn.submit = function(){
	alert("请覆盖方法（BatchMoveIn.submit）！");
}
//定义选择窗口
BatchMoveIn.saveWin = new Ext.Window({
	title:"批量调入", width:550, height:360, closeAction:"hide", modal:true, layout:"fit", items:{
		id:'cardPanel', layout:'card', activeItem: 0,frame:true,
		items:[{
			title:'选择机车列表',layout:"fit",
			items: [Train.grid]
		},{
			title:'录入调入信息',layout:"fit",
			items: [BatchMoveIn.grid.saveForm]
		}],
		buttons:[{
			text: "保 存", iconCls:"saveIcon",hidden:true, handler:function(){
				var form = BatchMoveIn.grid.saveForm.getForm();
	            if (!form.isValid()) return;
	            var data = form.getValues();
	            var trainData = Train.grid.selModel.getSelections();
	            var detailList = [] ;
	            for (var i = 0; i < trainData.length; i++) {
	            	var obj = {} ;
	            	for(prop in data){
	            		obj[prop] = data[prop] ;
	            	}
	            	obj.trainTypeIDX = trainData[i].get("trainTypeIDX");
	            	obj.trainTypeName = trainData[i].get("trainTypeShortName");
	            	obj.trainNo = trainData[i].get("trainNo");
	            	obj.trainUseName = trainData[i].get("trainUse");
	            	obj.idx = trainData[i].get("idx");
	            	detailList.push(obj);
	            }
	            JczlTrain.grid.loadMask.show();
			    var cfg = {
			        scope: BatchMoveIn.grid, 
			        url: BatchMoveIn.grid.saveURL, 
			        jsonData: detailList, 
			        success: function(response, options){
			            JczlTrain.grid.loadMask.hide();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			            	BatchMoveIn.saveWin.hide();
			            	BatchMoveIn.grid.store.reload(); 
			            	Train.grid.store.reload(); 
			            	JczlTrain.grid.store.reload(); 
					    	BatchMoveIn.grid.afterSaveSuccessFn(result, response, options)
			            } else {
			                BatchMoveIn.grid.afterSaveFailFn(result, response, options);
			            }
			        }
			    };
			    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			}
		},{
			text: "关 闭", iconCls:"closeIcon", handler:function(){BatchMoveIn.saveWin.hide();}
		},{
			text:'上一步',iconCls:'page-first' ,disabled:true,handler: changPage
		},{
			text:'下一步',iconCls:'page-last' ,handler: changPage
		}]
	}
});

//store载入前查询，为了分页查询时不至于出差错
Train.grid.store.on("beforeload", function(){
	this.baseParams.flag = "moveIn";  //查询可调入机车信息
});

});
//定义点击按钮方法
function changPage(btn){
	var cardPanel = Ext.getCmp("cardPanel");
	if(btn.text == '上一步'){
		cardPanel.layout.setActiveItem(0);
		cardPanel.buttons[0].setVisible(false);
		cardPanel.buttons[2].disable();
		cardPanel.buttons[3].enable();
	}else{ //下一步
		if(!$yd.isSelectedRecord(Train.grid)) return;
		cardPanel.layout.setActiveItem(1);
		cardPanel.buttons[0].setVisible(true);
		cardPanel.buttons[3].disable();
		cardPanel.buttons[2].enable();
	}
}