Ext.onReady(function(){
	
	if(typeof(NewPartsPlan) === 'undefined')
		Ext.ns("NewPartsPlan");
	
	var win, grid, form, regGrid, initialized = false;
	
	function createForm(){
		form = defineFormPanel({
			defaultType: "textfield",
			labelWidth: 70,
			rows: [{
				cw: .33,
				cols: [{
					id: "nprsw_trainType_id",
					xtype: "TrainType_combo",
					fieldLabel: "下车车型",
					hiddenName: "unloadTrainTypeIdx",
					displayField: "shortName", valueField: "typeID",
					pageSize: 20, minListWidth: 200,   //isCx:'no',
					editable:false  ,
					listeners : {   
						"select" : function() {   
							//重新加载修程下拉数据
							var rc_comb_s = Ext.getCmp("rc_comb_s");
							rc_comb_s.reset();
							rc_comb_s.clearValue();
							rc_comb_s.getStore().removeAll();
							rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue()};
					        rc_comb_s.cascadeStore();
						}   
					}
				},{
					fieldLabel:"下车车号",
					name: "unloadTrainNo"
				},{
					id:"nprsw_rs_id",
					xtype: "Base_combo",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					fieldLabel: "下车修程",
					hiddenName: "unloadRepairClassIdx",
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 20, minListWidth: 200,
					queryHql: 'from UndertakeRc',
					editable:false
				},{
					fieldLabel:"配件编号",
					name: "partsNo"
				},{
					fieldLabel:"配件状态"
				},{
					fieldLabel:"是否新品",
					name: "isNewParts"
				}]
			}]
		}, {
			labelAlign: "right",
		    buttonAlign:"center",
		    buttons: [{
	            text: "查询", iconCls: "searchIcon", handler: function(){
	            	grid.store.load();
	            }
	        },{
	            text: "重置", iconCls: "resetIcon", handler: function(){
	            	form.getForm().reset();
	            	Ext.getCmp("nprsw_trainType_id").clearValue();
	            	Ext.getCmp("nprsw_rs_id").clearValue();
	            }
	        },{
				text: "关闭", iconCls: "closeIcon", 
				handler: function(){
					win.hide();
		        }
		    }]
		});
	}
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsAccount!pageQuery.action",
			tbar: [{
				text: "添加",
				iconCls: "addIcon",
				handler: function(){
					var data = grid.selModel.getSelections();
					if(data.length == 0){
						MyExt.Msg.alert("尚未选择一条数据");
					}else{
						for(var i = 0; i < data.length; i++){
							NewPartsPlan.idxs.push(data[i].get("idx"));
						}
						regGrid.store.load();
						grid.store.reload();
					}
				}
			}],
			storeAutoLoad: false,
			fields:[{
				dataIndex: "idx", hidden:true
			},{
				sortable:true, header:"partsTypeIDX", dataIndex:"partsTypeIDX", hidden: true
			},{
				sortable:true, header:"specificationModel", dataIndex:"specificationModel", hidden: true
			},{
				sortable:true, header:"partsName", dataIndex:"partsName", hidden: true
			},{
				sortable:true, header:"配件编号", dataIndex:"partsNo", width:150
			},{
				sortable:true, header:"配件识别码", dataIndex:"identificationCode", width:150
			},{
				sortable:true, header:"是否新品", dataIndex:"isNewParts"
			},{
				header: "下车车型", dataIndex: "unloadTrainTypeIdx", hidden: true
			},{
				header: "下车车号", dataIndex: "unloadTrainNo"
			},{
				header: "下车修程", dataIndex: "unloadRepairClass"
			},{
				header: "下车修程", dataIndex: "unloadRepairClassIdx", hidden: true
			},{
				header: "下车修次", dataIndex: "unloadRepairTime"
			},{
				header: "下车修次", dataIndex: "unloadRepairTimeIdx", hidden: true
			},{
				sortable:true, header:"责任部门", dataIndex:"manageDept"
	    	}],
	    	toEditFn: notEidt
		});
		grid.store.on("beforeload", function(){
			var sp = MyJson.deleteBlankProp(form.getForm().getValues());
			var whereList = [];
			for(var i in sp){
				whereList.push({propName: i, propValue: sp[i]});
			}
						
			if(NewPartsPlan.idxs.length > 0){
				whereList.push({sql: "idx NOT IN('" + NewPartsPlan.idxs.join("','") + "')", compare: Condition.SQL});
			}
			whereList.push({propName: "partsStatus", propValue: partsStatus, stringLike: false});
			whereList.push({propName: "manageDeptType", propValue: manageDeptType, stringLike: false});
			whereList.push({propName: "partsTypeIDX", propValue: NewPartsPlan.partsType, stringLike: false});
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "选择待修配件",width:800, height:500, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			border: true, resizable: false,
			items: [{
				layout: "border",
				items: [{
					region: "north",
					items: form,
					frame: true,
					height: 100
				},{
					region: "center",
					layout:"fit",
					items: grid
				}]
			}]
		});
	}
	
	function initialize(){
		createGrid();
		createForm();
		createWin();
		initialized = true;
	}
	
	//partsAccount.PARTS_STATUS_DXZK;
	NewPartsPlan.showSelect = function(_grid){
		if(initialized == false) initialize();
		win.show();
		grid.store.load();
		regGrid = _grid;
	}
});