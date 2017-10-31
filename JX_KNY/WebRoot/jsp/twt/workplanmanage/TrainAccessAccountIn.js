/**
 * 机务设备工单定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('TrainAccessAccountIn');
	
	/** ******************** 定义全局变量开始 ******************** */
	TrainAccessAccountIn.labelWidth = 80;
	TrainAccessAccountIn.fieldWidth = 150;
	
	TrainAccessAccountIn.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	TrainAccessAccountIn.isSaveAndAdd = false;				// 是否是保存并新增的标识
	/** ******************** 定义全局变量结束 ******************** */
	
	/** **************** 定义机车入段保存表单开始 **************** */
	// 机车入段保存表单
	TrainAccessAccountIn.form = new Ext.form.FormPanel({
		title: '机车入段', iconCls: 'edit1Icon',
		padding:10,  frame: true, labelWidth:TrainAccessAccountIn.labelWidth,
		layout:'column', 
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
			defaults: { xtype:'textfield', anchor:'95%'}
		},
		items:[{
			items:[{
				id:"trainType_comb_Id",	
				fieldLabel: "车型",
				hiddenName: "trainTypeShortName", 
				returnField: [{widgetId:"trainTypeIDX_Id",propertyName:"typeID"}],
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				editable:true,
				allowBlank: false,
				forceSelection: true,
				xtype: "Base_combo",
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = Ext.getCmp("trainNo_comb_Id");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
    		}, {
    			xtype: 'EosDictEntry_combo',hiddenName: 'toGo',
				displayField:'dictname', valueField:'dictid',
				fieldLabel: '入段去向', dicttypeid: 'TWT_TRAIN_ACCESS_ACCOUNT_TOGO',
				allowBlank: false, 
				listeners: {
					render: function(cmp){
						this.store.on('load', function(){
							if (this.getCount() > 1) {
								var record = this.getAt(1);
								if(!Ext.isEmpty(record)) {
									cmp.setDisplayValue(record.get('dictid'), record.get('dictname'));
								}
							}
						});
					}
				}
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
	            		var trainTypeShortName =  TrainAccessAccountIn.form.getForm().findField("trainTypeShortName").getValue();
						if(Ext.isEmpty(trainTypeShortName)){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
	            	}
	            }
			}, {
    			fieldLabel: "入段司机", name: "inDriver", maxLength:20
    		}, {
    			fieldLabel: "计划车次", name: "planOrder", maxLength:20
    		},{
    			fieldLabel: "修程", id:'repairClassName_Id1', name: "repairClassName", xtype:"hidden"    			
    		}]
		}, {
			items:[{
				id:"trainNo_comb_Id",	
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				minLength : 4, 
				maxLength : 5,
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'true'},
				isAll: 'yes',
				returnField: [
		              {widgetId:"did_edit", propertyName:"dId"},//配属段ID
		              {widgetId:"dName_edit", propertyName:"dName"}//配属段名称 -
				],
				editable:true,
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeShortName =  TrainAccessAccountIn.form.getForm().findField("trainTypeShortName").getValue();
						if(Ext.isEmpty(trainTypeShortName)){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					}
				}
		
    		}, {
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
    		}, {
    			fieldLabel: "到达车次", name: "arriveOrder", maxLength:20
    		}, {
    			fieldLabel: "计划出段时间", name: "planOutTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
    		}]
		}, {
			columnWidth:1,
			defaults: {
				xtype:'hidden', anchor:'95%'
			},
			items: [{
				fieldLabel: 'idx主键', name: 'idx'
			}, {
    			fieldLabel: '车型主键', id:'trainTypeIDX_Id', name:'trainTypeIDX'		
    		},{
				fieldLabel: '配属段ID', id: 'did_edit', name: 'dID'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: "确认入段", iconCls: "saveIcon", id:'id_btn_save', hidden: true, handler: function() {
				TrainAccessAccountIn.isSaveAndAdd = false;
				TrainAccessAccountIn.saveFn();
			}
		}, {
			text: "入段并新增", iconCls: "addIcon", handler: function() {
				TrainAccessAccountIn.isSaveAndAdd = true;
				TrainAccessAccountIn.saveFn();
			}
		}]
	});
	/** **************** 定义机车入段保存表单结束 **************** */
	
	/** ******************** 定义全局函数开始 ******************** */
	// 保存成功后的函数处理
	TrainAccessAccountIn.afterSaveSuccessFn = function(entity){
		if (TrainAccessAccountIn.isSaveAndAdd) {
			// 重置保存表单
			TrainAccessAccountIn.form.getForm().reset();
			// 重置车型字段
			Ext.getCmp('trainType_comb_Id').clearValue();
			// 重置车号字段
			Ext.getCmp('trainNo_comb_Id').clearValue();
		} else {
			TrainAccessAccountIn.form.find('name', 'idx')[0].setValue(entity.idx);
        	// 禁止多次点击保存按钮
        	Ext.getCmp('id_btn_save').disable();
        	// 500ms后关闭父容器窗口
    		if (top.jDiaglog) top.jDiaglog.closeTime(1);
		}
	}
	
	// 保存函数
	TrainAccessAccountIn.saveFn = function(){
		var form = TrainAccessAccountIn.form.getForm();
		if (!form.isValid()) {
			return;
		}
		// 取值前启用“配属段”字段
		Ext.getCmp('dName_edit').enable();
		var data = form.getValues();
		// 取值后禁用“配属段”字段
		Ext.getCmp('dName_edit').disable();
		
		TrainAccessAccountIn.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/trainAccessAccount!saveOrUpdateIn.action',
			jsonData: Ext.util.JSON.encode(data),
	   	 	//请求成功后的回调函数
			success: function(response, options){
		        TrainAccessAccountIn.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            // 保存成功后的函数处理
		            TrainAccessAccountIn.afterSaveSuccessFn(result.entity);
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
			},
		    //请求失败后的回调函数
		    failure: function(response, options){
		        TrainAccessAccountIn.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	
	/** ******************** 定义全局函数结束 ******************** */
	// 页面自适应布局
	new Ext.Viewport({
		layout:'fit',
		items: [TrainAccessAccountIn.form]
	});
	
//	new Ext.Window({
//		title:'机车入段',
//		width:600, height: 200,
//		layout: 'fit',
//		items: [TrainAccessAccountIn.form],
//		buttonAlign: 'center',
//		buttons: [{
//			text: "保存", iconCls: "saveIcon", handler: function() {
//				TrainAccessAccountIn.isSaveAndAdd = false;
//				TrainAccessAccountIn.saveFn();
//			}
//		}, {
//			text: "保存并新增", iconCls: "addIcon", handler: function() {
//				TrainAccessAccountIn.isSaveAndAdd = true;
//				TrainAccessAccountIn.saveFn();
//			}
//		}]
//	}).show();
})