Ext.onReady(function(){
	
	if(typeof(NewPartsPlan) === 'undefined')
		Ext.ns("NewPartsPlan");
	
	var form, grid, win, initialized = storeBinded = false, regGrid;
	
	NewPartsPlan.idxs = null;
	NewPartsPlan.partsType = null;
	
	function setDefaultValue(func){
		return function(s, r){
			if(r.length > 0){
				func(r[0]);
			}
		}
	}
	
	function callSMReturnFn(node, e){
		form.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["text"]);
		form.find("name","specificationModel")[0].setValue(node.attributes["specificationModel"]);
		form.find("name","partsName")[0].setValue(node.attributes["partsName"]);
		form.find("name","matCode")[0].setValue(node.attributes["matCode"]);
		form.find("name","matCode")[0].setValue(node.attributes["matCode"]);
		Ext.getCmp("npp_ptidx_id").setValue(node.id);
		NewPartsPlan.partsType = node.id;
		var team = Ext.getCmp("repairTeam_id");
		var demand = Ext.getCmp("demand_id");
		team.queryName = node.id;
		demand.queryName = node.id;
		if(storeBinded === false){
			team.store.on("load", setDefaultValue(function(r){
				team.setDisplayValue(r.get('repairOrgID'),r.get('orgname'));
				Ext.getCmp("npp_orgseq_id").setValue(r.get("orgseq"));
			}));
			demand.store.on("load", setDefaultValue(function(r){
				demand.setDisplayValue(r.get('idx'),r.get('wPDesc'));
				Ext.getCmp("npp_wPNo_id").setValue(r.get("wPNo"));
			}));
			storeBinded = true;
		}
		team.cascadeStore();
		demand.cascadeStore();
	}
	
	function createForm(){
		form = defineFormPanel({
			defaultType: "textfield",
			labelWidth: 90,
			rows:[{
				cw: .33,
				cols: [{
					xtype:"PartsTypeTreeSelect",
					editable:false,
					fieldLabel: "规格型号",
					name: "tmp_name",
					returnFn: callSMReturnFn
				},{
					fieldLabel: "配件名称",
					name: "partsName",
					readOnly: true,
					style: "background-color:#EAE5E5;background-image:none;"
				},{
					fieldLabel: "物料编码",
					name: "matCode",
					readOnly: true,
					style: "background-color:#EAE5E5;background-image:none;"
				},{
					xtype:'hidden',name:"specificationModel"
				}]
			},{
				cw: [.33, .66],
				cols: [{
					fieldLabel: "承修班组",
					xtype: "Base_combo",
					id: "repairTeam_id",
					action: ctx + "/partsRepairList!repairOrg.action",
					fields:['orgname', 'repairOrgID', 'orgseq'],
					hiddenName: "repairOrgID", 
					returnField: [{widgetId:"npp_orgseq_id",propertyName:"orgseq"}],
					displayField: "orgname",
					valueField: "repairOrgID",
					pageSize: 20, minListWidth: 200,
					width: 140,
					editable:false,
					emptyText: '',
					listeners: {
						render: setTextName("repairOrgName")
					}
				},{
					fieldLabel: "检修需求单",
					xtype: "Base_combo",
					id: "demand_id",
					action: ctx + "/wP!findByPartsType.action",
					fields:['idx','wPNo', 'wPDesc'],
					hiddenName: "wpIDX", 
					returnField: [{widgetId:"npp_wPNo_id",propertyName:"wPNo"}],
					displayField: "wPDesc",
					valueField: "idx",
					pageSize: 20, minListWidth: 200,
					width: 140,
					editable:false,
					allowBlank: false,
					listeners: {
						render: setTextName("wpDesc")
					}
				}]
			},{
				cw: .33,
				cols: [{
					xtype: 'Base_combo',hiddenName: 'calendarIdx', fieldLabel: '日历',
					entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo',
					fields: ["idx","calendarName"],displayField:'calendarName',valueField:'idx',
					queryHql: 'from WorkCalendarInfo where recordStatus = 0',
					allowBlank: false,
					listeners: {
						render: function(){
							var me = this;
							this.store.on("load", setDefaultValue(function(r){
								me.setDisplayValue(r.get('idx'),r.get('calendarName'));
							}));
							this.cascadeStore();
							setTextName("calendarName").call(this);
						}
					}
				},{
					xtype: "hidden", name: "wpNo", id: "npp_wPNo_id"
				},{
					xtype: "hidden", name: "repairOrgSeq", id: "npp_orgseq_id"
				},{
					xtype: "hidden", name: "partsTypeIDX", id: "npp_ptidx_id"
				}]
			}]
		}, {labelAlign: "right"});
	}
	
	function addParts(partsNo, partsType){
		var me = this;
		this.disable();
		Ext.Ajax.request({
			url: ctx + "/partsAccount!findAwaitRepairParts.action",
			params: {partsNo: partsNo, partsType: partsType},
			success: function(r){
				var e = Ext.util.JSON.decode(r.responseText);
				if(e.success === false){
					alertFail(e.errMsg);
				}else if(e.list.length > 0){
					var add = true;
					var added = false;
					for(var i = 0; i < e.list.length; i++){
						for(var j = 0; j < NewPartsPlan.idxs.length; j++){
							if(e.list[i].idx == NewPartsPlan.idxs[j]){
								add = false;
								break;
							}
						}
						if(add){
							NewPartsPlan.idxs.push(e.list[i].idx);
							added = true;
						}
					}
					if(added){
						grid.store.reload();
						Ext.getCmp("npp_add_parts_id").setValue("");
					}else{
						MyExt.Msg.alert("配件已在列表中！");
					}
				}else{
					MyExt.Msg.alert("没有找到相关配件");
				}
				me.enable();
			},
			failure: function(){
				me.enable();
			}
		});
	}
	
	function createGrid(){
		grid = new Ext.yunda.EditGrid({
			loadURL: ctx + "/partsAccount!pageQuery.action",
			storeAutoLoad: false,
			autoSubmit: false,
			defaultTar: false,
			tbar: [{
				xtype:"textfield",
				id: "npp_add_parts_id"
			},{
				text: "添加明细",
				iconCls: "addIcon",
				handler:function(){
					var parts = Ext.getCmp("npp_add_parts_id");
					if(NewPartsPlan.partsType == undefined){
						MyExt.Msg.alert("尚未选择规格型号");
						return;
					}
					if(parts.getValue()){
						addParts.call(this, parts.getValue(), Ext.getCmp("npp_ptidx_id").getValue());
					}else{
						MyExt.Msg.alert("请输入配件编号");
						parts.focus();
					}
				}
			},{
				text: "选择待修配件",
				iconCls: "checkIcon",
				handler: function(){
					if(NewPartsPlan.partsType)
						NewPartsPlan.showSelect(grid);
					else
						MyExt.Msg.alert("尚未选择规格型号");
				}
			},{
				text:"删除明细",
				iconCls: "deleteIcon",
				handler: function(){
					var idxs = $yd.getSelectedIdx(grid);
					if(idxs.length == 0){
						MyExt.Msg.alert("尚未选择一条数据");
						return ;
					}
					for(var i = 0 ; i < idxs.length; i++){
						for(var j = 0; j < NewPartsPlan.idxs.length; j++){
							if(idxs[i] == NewPartsPlan.idxs[j]){
								var idx = NewPartsPlan.idxs.splice(j, 1);
								grid.store.removeAt(grid.store.indexOfId(idx[0]));
								break;
							}
						}
					}
				}
			}],
			fields: [{
				dataIndex: "idx", hidden: true
			},{
				header: "配件编号", dataIndex: "partsNo", width:150
			},{
				header: "配件识别码", dataIndex: "identificationCode", width:150
			},{
				header: "计划开始时间", dataIndex: "planStartTime", editor:{
					xtype:"my97date", my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i", allowBlank: false
				}, xtype: "datecolumn", width: 150
			}/*,{
				header: "计划完成时间", dataIndex: "planEndTime", editor:{
					xtype:"my97date", my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"}, format: "Y-m-d H:i", disabled:true
				}, xtype: "datecolumn", width: 150
			}*/,{
				header: "下车车型", dataIndex: "unloadTrainType"
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
				sortable:true, header:"partsTypeIDX", dataIndex:"partsTypeIDX", hidden: true
			},{
				sortable:true, header:"specificationModel", dataIndex:"specificationModel", hidden: true
			},{
				sortable:true, header:"partsName", dataIndex:"partsName", hidden: true
			},{
				sortable:true, header:"identificationCode", dataIndex:"identificationCode", hidden: true
			}]/*,
			afterEdit: function(meta){
				meta.record.set(meta.field, meta.value.format("Y-m-d H:i"));
				meta.record.set("unloadTrainType", "fkkk");
				meta.record.commit();
			}*/
		});
		
		grid.store.on("beforeload", function(){
			if(NewPartsPlan.idxs.length == 0){
				this.removeAll();
				return false;
			}
			var whereList = [];
			whereList.push({sql: "idx IN('" + NewPartsPlan.idxs.join("','") + "')", compare: Condition.SQL});
			whereList.push({propName: "partsStatus", propValue: partsStatus});
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		})
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "作业计划新增",width:900, height:500, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			border: false, resizable: false,
			items: {
				xtype: "panel",
				layout: "border",
				items:[{
			        layout: "fit",
			        region : 'north',
			        height: 95,
			        xtype: "panel",
			        baseCls:"x-plain",
			        items: form
				 },{
			        layout: "fit",
			        region : 'center',
			        items: grid
				 }]
			},
			buttons:[{
				text: "生成作业计划", iconCls: "saveIcon",
				handler: function(){
					var count = grid.store.getCount();
					if(count == 0){
						MyExt.Msg.alert("尚未添加明细");
						return;
					}
//					this.disable();
					submit.call(this, count);
				}
			}, {
				text: "关闭", iconCls: "closeIcon", 
				handler: function(){
					win.hide();
		        }
		    }]
		});
	}
	
	function submit(count){
		var thisForm = form.getForm(); 
        if (!thisForm.isValid()) return;
		var rdp = form.getForm().getValues();		
		delete rdp.tmp_name;
		delete rdp.partsTypeIDX;
		delete rdp.specificationModel;
		delete rdp.workCalendarIDX;
		delete rdp.partsName;
		var datas = [];
		for(var i = 0; i < count; i++){
			var r = MyJson.clone(grid.store.getAt(i).data);
			r.partsAccountIDX = r.idx;
			r.extendNo = r.extendNoJson;
			if(r.planStartTime)
				r.planStartTime = r.planStartTime.format("Y-m-d H:i:s");
			else {
				MyExt.Msg.alert("配件编号为" + r.partsNo + "的记录未选择计划开始时间，请重新选择");
				return;
			}
			if(r.planEndTime)
				r.planEndTime = r.planEndTime.format("Y-m-d H:i:s");
			delete r.extendNoJson;
			delete r.idx;
			delete r.isNewParts;
			delete r.manageDept;
			Ext.apply(r, rdp);
			datas.push(r);
		}
		var me = this;
		me.disable();
		Ext.Ajax.request({
			url: ctx + "/partsRdp!batchSavePartsRdp.action",
			jsonData: datas,
			timeout: 60000000,
			success: function(r){
				var retn = Ext.util.JSON.decode(r.responseText);
				if(retn.success){
					alertSuccess();
					win.hide();
					regGrid.store.reload();
				}else{
					alertFail(retn.errMsg);
				}
				me.enable();
			},
			failure: function(){
				alertFail("请求超时！");
				me.enable();
			}
		});
		
		
	}
	
	function initialize(){
		createForm();
		createGrid();
		createWin();
		initialized = true;
		initData();
	}
	
	function initForm(){
		form.getForm().reset();
		grid.store.removeAll();
		
		var bc = form.findByType("Base_combo");
		for(var i = 0; i < bc.length; i++){
			if(bc[i].fieldLabel != '日历')
				bc[i].clearValue();
		}
		form.findByType("PartsTypeTreeSelect")[0].setValue("");
		initData();
	}
	
	function initData(){
		NewPartsPlan.idxs = [];
		NewPartsPlan.partsType = null;
	}
	
	
	NewPartsPlan.showWin = function(_grid){
		regGrid = _grid;
		if(initialized == false)
			initialize();
		else{
			initForm();
		}
		win.show();
	};
});