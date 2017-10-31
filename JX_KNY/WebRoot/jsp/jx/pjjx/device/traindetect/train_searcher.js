Ext.onReady(function(){
	
	Ext.ns("Searcher");
	var form;
	var searchers = [];
	
	function setInitStartTime(){
		var date = new Date();
		date.setDate(date.getDate() - 2);
		this.setValue(date);
	}
	
	function btnRender(count){
		if(count < 1) return;
		var el = document.querySelector("#buttons");
		for(var i = 0; i < el.children.length; i++){
			el.children[i].style.width = "";
		}
	}
	
	function createForm(){
		form = defineFormPanel({
			labelWidth: 65,
			rows:[{
				cw: [200, 200, 200, 200, 200],
				cols:[{
					fieldLabel: "开始时间",
					name: "startTime",
					id: "start_time_searcher_id",
					xtype: "my97date",
					format: "Y-m-d H:i",
					maxDate: '#F{$dp.$D(\'end_time_searcher_id\')}',
					my97cfg: {
						maxDate: '#F{$dp.$D(\'end_time_searcher_id\')||%y-%M-%d}',
						dateFmt: "yyyy-MM-dd HH:mm"
					},
					initNow: false,
					validator: function(){
						if(this.getValue() == ''){
							setInitStartTime.call(this);
						}
					},
					listeners:{
						render: function(){
							setInitStartTime.call(this);
						}
					}
				},{
					fieldLabel: "至",
					id: "end_time_searcher_id",
					minDate: '#F{$dp.$D(\'start_time_searcher_id\')}',
					name: "endTime",
					xtype: "my97date",
					format: "Y-m-d H:i",
					my97cfg: {
						minDate: '#F{$dp.$D(\'start_time_searcher_id\')}',
						maxDate: "%y-%M-%d",
						dateFmt: "yyyy-MM-dd HH:mm"
					}
				},{
					xtype: "Base_combo",
					business: 'deviceInfo',
					fields:['deviceInfoCode','deviceInfoDesc'],
					fieldLabel: "设备",
					hiddenName: "deviceInfoCode", 
					displayField: "deviceInfoDesc",
					editable: true,
					typeAhead : false,
					forceSelection: true,
					valueField: "deviceInfoCode",
					pageSize: 20, minListWidth: 230
				},{
					xtype: "Base_combo",
					business: 'trainRdpUnionDeviceSearch',
					fields:['trainTypeIDX','trainTypeShortName'],
					hiddenName: "trainType",
					displayField: "trainTypeShortName",
					queryHql: "2",
					editable: true,
					typeAhead : false,
					forceSelection: true,
					valueField: "trainTypeIDX",
					pageSize: 20, minListWidth: 200,
					fieldLabel: "车型"
				},{
					xtype: "Base_combo",
					business: 'trainRdpUnionDeviceSearch',
					fields:['unloadTrainNo'],
					hiddenName: "trainNo",
					displayField: "unloadTrainNo",
					queryHql: "3",
					editable: true,
					typeAhead : false,
					forceSelection: true,
					valueField: "unloadTrainNo",
					pageSize: 20, minListWidth: 200,
					fieldLabel: "车号"
				},{
					fieldLabel: "工单描述",
					name: "workOrderDesc"
				},{
					fieldLabel: "检测项名称",
					name: "detectItemName"
				},{
					fieldLabel: "检测值",
					name: "detectItemValue"
				}]
			}]
		},{
			labelAlign: "right",
			baseCls: "x-plain"
		});
		
		var rendered = 0;
		new Ext.Button({
			renderTo: "buttons",
			text: "查询",
			iconCls: "searchIcon",
			handler: search,
			listeners:{
				render: function(){
					btnRender(rendered++);
				}
			}
		});
		
		new Ext.Button({
			renderTo: "buttons",
			text: "重置",
			style: "padding-top: 5px",
			iconCls: "resetIcon",
			handler: reset,
			listeners:{
				render: function(){
					btnRender(rendered++);
					setTimeout(function(){
						btnRender(3);
					}, 500);
					setTimeout(function(){
						btnRender(3);
					}, 1000);
				}
			}
		});
		return form;
	}
	
	function reset(){
		form.getForm().reset();
		var cmps = form.findByType("Base_combo");
		for(var i = 0; i < cmps.length; i++){
			cmps[i].clearValue();
		}
		search();
	}
	
	
	function search(){
		for(var i = 0; i < searchers.length; i++){
			searchers[i](form.getForm().getValues());
		}
	}
	
	Searcher.reset = reset;
	Searcher.search = search;
	
	Searcher.regSearcher = function(callback){
		searchers.push(callback);
	}
	
	Searcher.createForm = createForm;
	Searcher.form = form;
});