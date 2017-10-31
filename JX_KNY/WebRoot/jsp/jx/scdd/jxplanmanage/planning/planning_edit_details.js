Ext.onReady(function(){
	if(typeof(PlanningEdit) === 'undefined')
		Ext.ns("PlanningEdit");
	
	var form, grid, panel;
	
	function createForm(){
		form = defineFormPanel({
			labelWidth: 30,
			rows:[{
				cw: [150, 150, 60, 60],
				cols: [{
					xtype: "TrainType_combo",
					fieldLabel: "车型",
					hiddenName: "unloadTrainTypeIdx",
					displayField: "shortName",
					valueField: "typeID",
					pageSize: 20,
					minListWidth: 200,
					editable:false
				},{
					fieldLabel: "车号",
					name: "trainNo",
					maxLength: 4,
					vtype: "numberInt"
				},{
					xtype: "button",
					style: "margin-left: 10px",
					text: "查询",
					iconCls: "searchIcon",
					handler:function(){
						
					}
				},{
					xtype: "button",
					style: "margin-left: 10px",
					text: "重置",
					iconCls: "resetIcon",
					handler:function(){
						var form = this.findParentByType("form");
						form.getForm().reset();
					}
				}]
			}]
		}, {labelAlign: "right"});
	}
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx,
			saveURL: ctx,
			deleteURL: ctx,
			storeAutoLoad: false,
			tbar: ["add",{
				text: "根据走行生成",
				iconCls: "checkIcon",
				handler: function(){
					RKM.showWin();
				}
			}, "delete"],
			fields: [{
				dataIndex: "idx"
			},{
				header: "车型", dataIndex: ""
			},{
				header: "车型", dataIndex: ""
			},{
				header: "车型", dataIndex: ""
			},{
				header: "车型", dataIndex: ""
			},{
				header: "车型", dataIndex: ""
			},{
				header: "配属段", dataIndex: ""
			},{
				header: "委修单位", dataIndex: ""
			},{
				header: "走行公里", dataIndex: ""
			},{
				header: "备注", dataIndex: ""
			}],
			toEditFn: false,
			addButtonFn: function(){
				PlanningDetail.showWin();
			}
		});
	}
	
	function createPanel(){
		createForm();
		createGrid();
		
		return new Ext.Panel({
			layout: "border",
			items: [{
				region: "north",
				height: 35,
				layout: "fit",
				baseCls: "x-plain",
				items: form
			},{
				region: "center",
				layout: "fit",
				items: grid
			}]
		});
	}
	
	PlanningEdit.createPanel = createPanel;
});