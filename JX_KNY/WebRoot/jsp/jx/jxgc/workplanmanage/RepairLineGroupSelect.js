Ext.onReady(function(){
	Ext.namespace('RepairLineGroupSelect');
	Ext.Ajax.timeout = 1000000;
	RepairLineGroupSelect.loadMask = {msg: "正在处理，请稍侯..."};
	RepairLineGroupSelect.tecProcessCaseIDX = '';
	RepairLineGroupSelect.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	RepairLineGroupSelect.store = new Ext.data.JsonStore({
		id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: false,
	    url: ctx + "/repairLineGroup!workStationOrderList.action",
	    fields: [ "idx","groupId", "repairLineName","nodeName","repairLineIDX","idxs" ],
	    sortInfo: {field: "groupId", direction: "ASC"}
	});
	RepairLineGroupSelect.reloadParent = function(nodeIdx) {
		WorkPlanGantt.loadFn("expanded", nodeIdx);
	}
	RepairLineGroupSelect.submit = function(){
		Ext.Msg.confirm("提示  ", "该操作将对列表中所有节点进行派工操作，是否继续？  ", function(btn){
	        if(btn != 'yes')    return;
	        //获取所有记录
			var datas = new Array();
			for (var i = 0; i < RepairLineGroupSelect.store.getCount(); i++) {
				var data = {} ;
				data = RepairLineGroupSelect.store.getAt(i).data;
				if(Ext.isEmpty(data.repairLineIDX)){
					MyExt.Msg.alert("请选择检修流水线！");
					return;
				}
				datas.push(data);
			}
			if(RepairLineGroupSelect.grid.loadMask ) RepairLineGroupSelect.grid.loadMask.show();
	        Ext.Ajax.request({
	            url: ctx + '/repairLineGroup!updateForDispatchByRepairLine.action',
	            jsonData: datas,
	            params: {workPlanIDX: workPlanIDX, tecProcessCaseIDX: RepairLineGroupSelect.tecProcessCaseIDX},
	            success: function(response, options){
	              	RepairLineGroupSelect.grid.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.success == true) {
	                    alertSuccess();
	                    RepairLineGroupSelect.win.hide();
	                    if(typeof(WorkPlanGantt)!= 'undefined'){
	                    	RepairLineGroupSelect.reloadParent();
	                    }						
						if(typeof(TrainWorkPlanForLeafNode)!= 'undefined'){
							TrainWorkPlanForLeafNode.store.reload();
						}
	                } else {
	                    alertFail(result.errMsg);
	                }
	            },
	            failure: function(response, options){
	                RepairLineGroupSelect.grid.loadMask.hide();
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });    	
	    });
	}
	RepairLineGroupSelect.filterBy = function(id,grid){
		var nodeCase_Name = Ext.get(id);		
		var searchArray = [];
		if(!Ext.isEmpty(nodeCase_Name) && !Ext.isEmpty(nodeCase_Name.getValue())){
			var searchParam = {};
			searchParam.property = "nodeName";
			searchParam.value = nodeCase_Name.getValue();
			searchParam.anyMatch = true;
			searchParam.caseSensitive = false;
			searchArray.push(searchParam);
		}
		grid.store.filter(searchArray);
	}
	RepairLineGroupSelect.nodeGrid = new Ext.grid.GridPanel({
		store: new Ext.data.JsonStore({
			data:[],
		    fields: [  "nodeName"],
		    sortInfo: {field: "nodeName", direction: "ASC"}
		}),
	    border: false, enableColumnMove: true, stripeRows: true,loadMask:true, 
	    viewConfig: {forceFit: true}, 	    
	    tbar:[
	    	  '流程节点:',{xtype:'textfield',id:'nodeCase_Name',width:100},
	    	 {
		        text:"查询", iconCls:"searchIcon", handler: function(){
		        	RepairLineGroupSelect.filterBy('nodeCase_Name',RepairLineGroupSelect.nodeGrid);
		       	}
	    },{
	        text: "关闭", iconCls: "closeIcon", handler: function(){
	        	RepairLineGroupSelect.nodeWin.hide();
	        }
	    }],
		colModel: new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	        { sortable: true, header: "流程节点", dataIndex: "nodeName" }
		])
	});
	RepairLineGroupSelect.nodeGrid.un('rowdblclick', RepairLineGroupSelect.nodeGrid.toEditFn, RepairLineGroupSelect.nodeGrid);
	RepairLineGroupSelect.nodeWin = new Ext.Window({
		title:"查看影响流程节点", width:350, height:300, closeAction:"hide", layout:"fit",
		items:[RepairLineGroupSelect.nodeGrid]
	});
	
	RepairLineGroupSelect.grid =  new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: true, stripeRows: true, 
	    viewConfig: {forceFit: true}, selModel: RepairLineGroupSelect.sm,clicksToEdit: 2,
	    store: RepairLineGroupSelect.store, loadMask: RepairLineGroupSelect.loadMask,
	    tbar: [
	    	'影响流程节点:',{xtype:'textfield',id:'nodeCase_Names',width:100},
	    	 {
		        text:"查询", iconCls:"searchIcon", handler: function(){
		        	RepairLineGroupSelect.filterBy('nodeCase_Names',RepairLineGroupSelect.grid);
		       	}
	    	},{
	        text: "查看影响流程节点", iconCls: "queryIcon", handler: function(){
	        	RepairLineGroupSelect.nodeWin.hide();
	        	if(!$yd.isSelectedRecord(RepairLineGroupSelect.grid, true)) return;
		    	var record = RepairLineGroupSelect.grid.selModel.getSelections();
		    	RepairLineGroupSelect.nodeWin.show();
		    	var nodeCaseNames = record[0].get("nodeName");
		    	RepairLineGroupSelect.nodeGrid.store.removeAll();
		    	if(!Ext.isEmpty(nodeCaseNames)){
		    		var array = nodeCaseNames.split(",");
		    		for(var i = 0; i < array.length;i++){
		    			var record = new Ext.data.Record();
		    			record.set("nodeName",array[i]);
		    			RepairLineGroupSelect.nodeGrid.store.add(record);
		    		}
		    	}
	        }
	    },{
	        text: "确定", iconCls: "addIcon", handler: function(){ RepairLineGroupSelect.submit(); }
	    },{
	        text: "关闭", iconCls: "closeIcon", handler: function(){
	        	RepairLineGroupSelect.nodeWin.hide();
	        	RepairLineGroupSelect.win.hide(); 
	        }
	    }],
	    colModel: new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),         
	        { sortable:true, header:"检修流水线", dataIndex:"repairLineName" , width:50,
	          editor:{
					id:"BaseRepairLine_combo",
					xtype: 'Base_combo',
					entity: 'x',
					hiddenName: 'repairLineIDX',
					fields: ["idx", 'repairLineName'],
			    	displayField:'repairLineName',
			    	valueField:'repairLineName',
			    	business:'repairLineGroup',
			    	pageSize: 0,
			    	returnField:[{widgetId:"repairLineIDX_Id",propertyName:"idx"}]
				}
	        },
	        { sortable: true, header: "影响流程节点",  dataIndex: "nodeName" },
	        { sortable: true, header: "groupId", hidden: true, dataIndex: "groupId" },
	        { sortable: true, header: "流水线主键", hidden: true, dataIndex: "repairLineIDX", editor:{ id: "repairLineIDX_Id"}},
	        { sortable: true, header: "idxs", hidden: true, dataIndex: "idxs" }
		]),
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
			    var record = grid.getStore().getAt(rowIndex);  // 返回当前行Record对象
			    var fieldName = grid.getColumnModel().getDataIndex(3); // 返回groupId字段名称 
			    var data = record.get(fieldName);
			    var repairLineCombo =  Ext.getCmp("BaseRepairLine_combo");
			    repairLineCombo.queryParams = {"groupId": data};
			    repairLineCombo.store.proxy = new Ext.data.HttpProxy( {   
		            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(repairLineCombo.queryParams)+'&manager=repairLineGroup' 
		        });
			    repairLineCombo.getStore().on("load",function(store, records){ 
					if(records.length > 0){
				    	repairLineCombo.setDisplayValue(records[0].get('repairLineName'),records[0].get('repairLineName'));				    	
					}
				});
				repairLineCombo.getStore().load();
			},
			afteredit:function(e){
				e.record.data.repairLineName = Ext.getCmp("BaseRepairLine_combo").getValue();
				e.record.data.repairLineIDX = Ext.getCmp("repairLineIDX_Id").getValue();
			}
		}
	});
	//流水线分组列表窗口
	RepairLineGroupSelect.win = new Ext.Window({
		title:"流水线选择", width:850, height:360, closeAction:"hide", modal:true, layout:"fit",
		items:[RepairLineGroupSelect.grid]
	});
});