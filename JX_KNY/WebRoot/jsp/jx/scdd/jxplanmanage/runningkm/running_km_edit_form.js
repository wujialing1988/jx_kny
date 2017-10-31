
Ext.onReady(function(){
	Ext.ns("Edit");
		
	var trainTypeCmp, callback;
	
	
	var form = defineFormPanel({
		defaultType: "textfield",
		labelWidth: 60,
		rows:[{
			cw: .8,
			cols: [{
				xtype: "Base_combo",
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				business:"trainType",
				fields:['typeID','shortName'],
				hiddenName: "trainTypeIdx", 
				displayField: "shortName",
				forceSelection: true,
				editable:true,
				queryName:"shortName",
				valueField: "typeID",
				pageSize: 20, minListWidth: 200,
				returnField: [{widgetId:"rkef_trainType_id", propertyName:"shortName"}],
				queryParams: {repairType: "C1C6"},
				fieldLabel: "车型",
				allowBlank: false,
				validator: requestRKM,
				listeners:{
					select: function(){
						var tn = Ext.getCmp("trainNo_id");
						tn.queryParams
							= {trainTypeIDX: this.getValue()};
						tn.cascadeStore();
					}
				}
			},{
				id: "trainNo_id",
				xtype: "Base_combo",
				entity:'JczlTrain',
				fields:['trainNo'],
				name: "trainNo", 
				displayField: "trainNo",
				pageSize: 20, minListWidth: 200,
				fieldLabel: "车号",
				editable:true,
				queryName:"trainNo",
				allowBlank: false,
				typeAhead : false,
				forceSelection :false,
				vtype: "trainNo",
				listeners:{
					blur: requestRKM,
					focus: function(){
						this.bindFocus = true;
					}
				}
			},{
				fieldLabel: "走行公里",
				name: "recentlyRunningKm",
				vtype: "nonNegativeNumber",
				allowBlank: false,
				listeners:{
					blur: requestRKM,
					focus: function(){
						this.bindFocus = true;
					}
				}
			}]
		},{
			define: true,
			content:{
				xtype: "panel",
				border: false,
			 	contentEl: "ym_container"
			}
		},{
			cw: .8,
			cols: [{
				fieldLabel: "新造",
				xtype: "numberfield",
				vtype: "nonNegativeNumber",
				maxLength: 7,
				name: "newRunningKm"
			},{
				fieldLabel: "C1",
				id: "c1f_id",
				maxLength: 7,
				vtype: "nonNegativeNumber",
				xtype: "numberfield",
				name: "c1"
			},{
				fieldLabel: "C2",
				maxLength: 7,
				vtype: "nonNegativeNumber",
				xtype: "numberfield",
				name: "c2"
			},{
				fieldLabel: "C3",
				maxLength: 7,
				vtype: "nonNegativeNumber",
				xtype: "numberfield",
				name: "c3"
			},{
				fieldLabel: "C4",
				maxLength: 7,
				vtype: "nonNegativeNumber",
				xtype: "numberfield",
				name: "c4"
			},{
				fieldLabel: "C5",
				maxLength: 7,
				vtype: "nonNegativeNumber",
				xtype: "numberfield",
				name: "c5"
			},{
				fieldLabel: "C6",
				maxLength: 7,
				vtype: "nonNegativeNumber",
				xtype: "numberfield",
				name: "c6"
			},{
				xtype: "hidden",
				name: "idx"
			},{
				xtype: "hidden",
				name: "repairType"
			},{
				xtype: "hidden",
				name: "repairClass"
			},{
				xtype: "hidden",
				name: "repairClassName"
			},{
				xtype: "hidden",
				name: "repairOrder"
			},{
				xtype: "hidden",
				name: "repairOrderName"
			},{
				xtype: "hidden",
				name: "trainType",
				id: "rkef_trainType_id"
			}]
		}]
	}, {labelAlign: "right", border: true});
	
	function requestRKM(){
		if(!this.bindFocus){
			return;
		}else{
			this.bindFocus = false;
		}
		var idx = form.find("name", "idx")[0].getValue();
		var traintype = form.findByType("Base_combo")[0].getValue();
		var trainno = form.find("name", "trainNo")[0].getValue();
		var zx = form.find("name", "recentlyRunningKm")[0].getValue();
		if(traintype && trainno && zx){
			rkm(traintype, trainno, parseFloat(zx), idx);
		}
		return true;
	}
	
	function rkm(traintype, trainno, zx, idx){
		Ext.Ajax.request({
			url: ctx + "/runningKM!findRunningKM.action",
			params: {trainTypeIdx: traintype, trainNo: trainno},
			success: function(r){
				var rlt = Ext.util.JSON.decode(r.responseText);
				var original = 0;
				if(rlt.entity){
					if(idx){
						original = rlt.entity.recentlyRunningKm;
					}
					var val;
					for(var i = 1; i <= 6; i++){
						val = rlt.entity["c" + i] + zx - original;
						form.find("name", "c" + i)[0].setValue(val < 0 ? 0 : val);
					}
					val = rlt.entity.newRunningKm + zx - original;
					form.find("name", "newRunningKm")[0].setValue(val < 0 ? 0 : val);
				}else{
					for(var i = 1; i <= 6; i++){
						form.find("name", "c" + i)[0].setValue(zx);
					}
					form.find("name", "newRunningKm")[0].setValue(zx);
				}
			}
		});
	}
	
	
	function submit(data){
		var me = this;
		this.disable();
		delete data.idx;//
		Ext.Ajax.request({
			url: ctx + "/runningKM!saveOrUpdate.action",
			jsonData: data,
			success: function(r){
				var rlt = Ext.util.JSON.decode(r.responseText);
				if(rlt.success){
					alertSuccess();
					callback();
					win.hide();
				}else{
					alertFail(rlt.errMsg);
				}
				me.enable();
			},
			failure: function(){
				alertFail("请求超时");
				me.enable();
			}
			
		});
	}
	
	function validSubmit(){
		var basicForm = form.getForm();
		if (!basicForm.isValid()) return;
		var data = basicForm.getValues();
		
		if(data.x){
			if(data.ym){
				var date = new Date(data.ym.match(/^\d{4}/), data.ym.match(/\b\d{2}$/), 0);
				data.beginDate = data.ym + "-01";
				data.endDate = data.ym + "-" + date.getDate();
			}else{
				MyExt.Msg.alert("年月不能为空");
				return;
			}
			delete data.x;
		}else if(!data.beginDate || !data.endDate){
			MyExt.Msg.alert("走行日期不能为空");
			return;
		}
		delete data.ym;
		submit.call(this, data);
	}
	
	var win = new Ext.Window({
	   	title: "新增",width:310, height: 365, layout: "fit", 
		closeAction: "hide", modal: true , buttonAlign:"center",
		border: false, resizable: false, style: "padding:2px;",
		items: form,
		buttons: [{
			text: "保存",
			iconCls: "saveIcon",
			handler: function(){
				validSubmit.call(this);
			}
		},{
			text: "关闭",
			iconCls: "closeIcon",
			handler: function(){
				win.hide();
			}
		}]
	});
	
	document.querySelector("input[name='x']").onclick = function(){
		var d1 = document.querySelector("#ym_container>div");
		var d2 = document.querySelector("#ym_container>div:last-child");		
		d1.style.display = this.checked ? "inline-block" : "none";
		d2.style.display = this.checked ? "none" : "inline-block";
	};
	
	var ym = new Ext.yunda.My97DatePicker({
		format: "Y-m",
		name: "ym",
		renderTo: "el_ym_id",
		my97cfg: {dateFmt: "yyyy-MM", maxDate: '%y-%M'},
		width: 100
	});
	
	var ymd_begin = new Ext.yunda.My97DatePicker({
		renderTo: "el_begin_id",
		name: "beginDate",
		id: "my97_begin_id",
		width: 100,
		my97cfg: {maxDate: "#F{$dp.$D(\'my97_end_id\')}"}
	});
	var ymd_end = new Ext.yunda.My97DatePicker({
		renderTo: "el_end_id",
		name: "endDate",
		id: "my97_end_id",
		width: 100,
		my97cfg: {minDate: "#F{$dp.$D(\'my97_begin_id\')}", maxDate: '%y-%M-%d'}
	});
	
	var c1c6 = ["C1", "C2", "C3", "C4"];
	var fxdx = ["辅修", "小修", "中修", "大修"];
	
	
	function changeType(isC1C6){
		if(trainTypeCmp === undefined)
			trainTypeCmp = form.findByType("Base_combo")[0];
		if(isC1C6){
			iterator(c1c6);
			win.setHeight(365);
		}else{
			iterator(fxdx);
			win.setHeight(305);
		}
	}
	
	function initData(isC1C6){
		var repairType;
		if(isC1C6)
			repairType = c1c6_val;
		else
			repairType = fxdx_val;
		trainTypeCmp.queryParams.repairType = repairType;
		form.find("name", "repairType")[0].setValue(repairType);
		trainTypeCmp.cascadeStore();
	}
	
	function iterator(arr){
		for(var i = 0; i < arr.length; i++){
			form.find("name", "c" + (i + 1))[0].label.dom.innerHTML = arr[i] + ":";
		}
	}
	
	function isLastDay(d){
		var date = new Date(d.getFullYear(), d.getMonth() + 1, 0);
		return d.getDate() == date.getDate();
	}
	
	Edit.showWin = function(isC1C6, _callback, record){
		win.show();
		changeType(isC1C6);
		trainTypeCmp.clearValue();
		form.getForm().reset();
		if(record){
			
			var begin = record.get("beginDate");
			var end = record.get("endDate");
			var cbx = document.querySelector("input[name='x']");
			if(begin.getDate() == 1 && isLastDay(end)){
				ym.setValue(begin);
				if(cbx.checked == false)
					cbx.click();
			}else{
				ymd_begin.setValue(begin);
				ymd_end.setValue(end);
				if(cbx.checked)
					cbx.click();
			}
			form.getForm().loadRecord(record);
			trainTypeCmp.setDisplayValue(record.get("trainTypeIdx"), record.get("trainType"));
			win.setTitle("编辑");
		}else{
			win.setTitle("新增");
		}
		initData(isC1C6);
		callback = _callback
	}
});