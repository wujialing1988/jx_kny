/**
 * 机务设备工单定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('TrainAccessAccountEdit');
	
		/** ******************** 定义全局变量开始 ******************** */
	TrainAccessAccountEdit.labelWidth = 80;
	/** ******************** 定义全局变量结束 ******************** */
	

	/** **************** 定义机车入段编辑表单开始 **************** */
	TrainAccessAccountEdit.form = new Ext.form.FormPanel({
		title: '机车入段编辑', iconCls: 'edit1Icon',
		padding:10,  frame: true, labelWidth:TrainAccessAccountEdit.labelWidth,
		layout:'column', 
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
			defaults: { xtype:'textfield', anchor:'95%'}
		},
		items:[{
			items:[{
    			fieldLabel: "车型", name: "trainTypeShortName", allowBlank: false, disabled: true, value: trainTypeShortName
    		},{
    			xtype: 'Base_comboTree',hiddenName: 'toGo', 
				fieldLabel: '入段去向',
				allowBlank: false,
				treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
				queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
				selectNodeModel: 'leaf'
    		},{
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassIDX", 
				returnField: [{widgetId:"repairClassName_Id1",propertyName:"xcName"}],
				business: 'trainRC',
				fields:['xcID','xcName'],
				displayField: "xcName",
				valueField: "xcID",
				pageSize: 0, minListWidth: 200,
				listeners : {
	            	"beforequery" : function(){
	            		var trainTypeShortName =  TrainAccessAccountEdit.form.getForm().findField("trainTypeShortName").getValue();
						if(Ext.isEmpty(trainTypeShortName)){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
	            	}
	            }
	          
    		},{
    			fieldLabel: "入段时间", name: "inTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
    		},{
    			fieldLabel: "入段司机", name: "inDriver", maxLength:20
    		},{
    			fieldLabel: "计划车次", name: "planOrder", maxLength:20
    		},{
    			fieldLabel: "修程", id:'repairClassName_Id1', name: "repairClassName", xtype:"hidden"   
    		}]
		}, {
			items:[{
    			fieldLabel: "车号", name: "trainNo", allowBlank: false, disabled: true, value: trainNo
    		},{
    			id: "dName_edit", fieldLabel: "配属段", name: "dName", maxLength:50, disabled: true
    		},{    			
				xtype: 'combo',
				fieldLabel: '车头方向',
		        hiddenName:'ctfx',
		        store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
					data : [['左','左'],['右','右']]
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				editable: false		
    		},{ 
    			xtype: 'hidden', name: 'idx', value:idx
    		},{ id: 'did_edit', xtype: 'hidden', name: 'dID'},{
    			fieldLabel: "到达车次", name: "arriveOrder", maxLength:20
    		},{
    			fieldLabel: "计划出段时间", name: "planOutTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
    		}]
		}, {
			columnWidth:1,
			defaults: {
				xtype:'hidden', anchor:'95%'
			},
			items: [{fieldLabel: 'trainTypeIDX_Id', name: 'trainTypeIDX', value: trainTypeIDX}]
		}],
		buttonAlign: 'center',
		buttons: [{
        	text: "保存", iconCls: "saveIcon", handler: function() {  
			    var form = TrainAccessAccountEdit.form.getForm(); 
			    if (!form.isValid()) return;
			    form.findField("trainNo").enable();
				form.findField("trainTypeShortName").enable();
				form.findField("toGo").enable();
				form.findField("dName").enable();
			    var data = form.getValues();	
			    if (Ext.isEmpty(data.trainNo) && !Ext.isEmpty(Ext.get("trainNo_comb_Id").dom.value)) {
					data.trainNo = Ext.get("trainNo_comb_Id").dom.value;
				}
			    var cfg = {
			        scope: this, url: ctx + '/trainAccessAccount!updateIn.action', jsonData: data,
			        success: function(response, options){
//			            if(TrainAccessAccountEdit.loadMask)   TrainAccessAccountEdit.loadMask.hide();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                TrainAccessAccountEdit.store.reload();
			                form.findField("trainNo").disable();
			                form.findField("trainTypeShortName").disable();
			                form.findField("toGo").disable();
							form.findField("dName").disable();
						    alertSuccess();
						    this.hide();		
    						if (top.jDiaglog) top.jDiaglog.closeTime(1);
			            } else {
			                TrainAccessAccountEdit.store.reload();
	    					alertFail(result.errMsg);
			            }
			        }
			    };
			    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
        	}
    	}, {
        	text: "重置", iconCls: "closeIcon", handler: function(){ 
        	    var form = TrainAccessAccountEdit.form.getForm();
				form.reset();
			}
		}]
	});
	/** **************** 定义机车入段编辑表单结束 **************** */

	
	// 数据加载完成后的函数处理
	TrainAccessAccountEdit.storeLoadFn = function(store, records, options) {
		var count = this.getCount();
		var record = this.getAt(0);
		var form = TrainAccessAccountEdit.form.getForm();
		form.reset();
		form.loadRecord(record);
		form.findField("toGo").setDisplayValue(record.get("toGo"), EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",record.get("toGo")));	
		form.findField("repairClassIDX").setDisplayValue(record.get("repairClassIDX"), record.get("repairClassName"));	
		if (record.get("toGo") != TRAINTOGO_ZB) {
			form.findField("toGo").enable();
		} else {
			form.findField("toGo").disable();
		}
		form.findField("dName").disable();
		form.findField("inTime").setValue(new Date(record.get("inTime")).format('Y-m-d H:i:s'));
		if (record.get("planOutTime") != null)
			form.findField("planOutTime").setValue(new Date(record.get("planOutTime")).format('Y-m-d H:i:s'));
	}
		
	/*** 加载机车入段列表***/
	TrainAccessAccountEdit.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/trainAccessAccount!pageQuery.action',
		fields : [
			"idx", 
			"trainTypeIDX", "trainTypeShortName", 
			"trainNo", "dID", "dName", 
			"repairClassName", "trainAliasName", 
			"inTime", "planOutTime", 
			"toGo", "trainStatus", 
			"startTime", "arriveOrder", 
			"planOrder", "inDriver", 
			"repairClassName","repairClassIDX","ctfx"
		],
		sortInfo : {
			field : 'trainTypeShortName',
			direction : 'ASC'
		},
		listeners : {
			// 数据加载完成后的函数处理
			load : TrainAccessAccountEdit.storeLoadFn,
			
			// 数据加载异常处理
			exception: function() {
				var response = arguments[4];
	        	var result = Ext.util.JSON.decode(response.responseText);
				if (!Ext.isEmpty(result.errMsg)) {
					Ext.Msg.alert("数据错误", result.errMsg);
				}
			},		
			beforeload: function(store, options) {
				var searchParam = {};
				searchParam.idx = idx;
				searchParam.trainTypeIDX = trainTypeIDX
				searchParam.trainTypeShortName = trainTypeShortName;
				searchParam.trainNo = trainNo;
				var whereList = [];
				whereList.push({propName:'idx',propValue:searchParam['idx'],compare:Condition.EQ, stringLike: false}); 
				store.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
			}
		}
	});
	
	// 页面自适应布局
	new Ext.Viewport({
		layout:'fit',
		items: [TrainAccessAccountEdit.form]
	});

})