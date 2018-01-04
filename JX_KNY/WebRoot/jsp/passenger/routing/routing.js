/**
 * 路线信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('routing');                       //定义命名空间
	routing.searchParam = {} ;
	routing.labelWidth = 100;
	routing.fieldWidth = 140;
	routing.isSaveAndAdd = false;			// 是否是保存并新增的标识
	
    /** ************** 定义节点编辑表单开始 ************** */
	routing.saveForm = new Ext.form.FormPanel({
		padding: "10px", frame: true, labelWidth: routing.labelWidth,
		layout:"column",
		defaults: {
			layout:"form",
			columnWidth:0.5
		},
		items:[{
			defaults: {xtype:"textfield", maxLength:50,allowBlank: false, anchor:"90%"},
			items:[{
//				name:"routingCode", fieldLabel:"线路编码"
//			}, {
				fieldLabel: i18n.routing.departureTime, xtype:"my97date", name: 'departureTime', width: 80, format: "H:i",
		        	my97cfg: {dateFmt:"HH:mm"}		
			}, {
				name:"startingStation", fieldLabel:i18n.routing.startingStation
			}, {
				xtype: 'compositefield', id:'duration_id',fieldLabel: i18n.routing.duration, combineErrors: false, 
				items: [{
					xtype: 'numberfield', id: 'duration_H', name: 'duration_h', width: 60, validator: function(value) {
						if (Ext.isEmpty(value)) {
							return null;
						}
						if (parseInt(value) < 0) {
							return i18n.routing.return1;
						}
						var mValue = Ext.getCmp('duration_M').getValue();
						if (Ext.isEmpty(value) && Ext.isEmpty(mValue)) {
							return i18n.routing.return2
						} else {
							if (value.length > 2) {
								return i18n.routing.return3;
							} else if (Ext.isEmpty(mValue) || parseInt(mValue) < 60){
								Ext.getCmp('duration_H').clearInvalid();
								Ext.getCmp('duration_M').clearInvalid();
							}
						}
					}
				}, {
					xtype: 'label',
					text: ':',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {
					xtype: 'numberfield', id: 'duration_M', name: 'duration_m', width: 60, validator: function(value) {
						if (Ext.isEmpty(value)) {
							return null;
						}
						if (parseInt(value) < 0) {
							return i18n.routing.return1;
						}
						var hValue = Ext.getCmp('duration_H').getValue();
						if (Ext.isEmpty(value) && Ext.isEmpty(hValue)) {
							return i18n.routing.return2
						} else {
							if (parseInt(value) >= 60) {
								return i18n.routing.return4;
							} else if (hValue.length <= 2){
								Ext.getCmp('duration_H').clearInvalid();
								Ext.getCmp('duration_M').clearInvalid();
							}
						}
					}
				}, {
					xtype: 'label',
					text: i18n.routing.minute,
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}]
			}, {
				fieldLabel: i18n.routing.departBackTime, xtype:"my97date", name: 'departureBackTime', width: 80, format: "H:i",
		        	my97cfg: {dateFmt:"HH:mm"}		
//			}, {
//				name:"strips", fieldLabel:"车次"
			}]
		}, {
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				fieldLabel: i18n.routing.arrivalTime, xtype:"my97date", allowBlank: false, name: 'arrivalTime', width: 80, format: "H:i",
			        	my97cfg: {dateFmt:"HH:mm"}		
			}, {
				name:"leaveOffStation", fieldLabel:i18n.routing.forwardStation
			}, {
				name:"kilometers", xtype: 'numberfield', fieldLabel:i18n.routing.kilometers
			}, {
				fieldLabel: i18n.routing.arrivalBackTime, xtype:"my97date", name: 'arrivalBackTime', width: 80, format: "H:i",
	        	my97cfg: {dateFmt:"HH:mm"}		
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea", name:"remark", fieldLabel:i18n.routing.remark, maxLength:500, anchor:"97%", height: 55
			}]
		}, {
			// 【作业节点】保存表单的隐藏字段
			columnWidth:1,
			defaultType:'hidden',
			items:[
				{ fieldLabel:i18n.routing.idx, name:"idx" }
			]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: i18n.routing.save, iconCls: 'saveIcon', handler: function() {
				routing.isSaveAndAdd = false;
				routing.grid.saveFn();
			}
		}, {
			text: i18n.routing.add, iconCls: 'addIcon', handler: function() {
				routing.isSaveAndAdd = true;
				routing.grid.saveFn();
			}
		}, {
			text: i18n.routing.close, iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	// 表格
	routing.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/routing!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/routing!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/routing!logicDelete.action',            //删除数据的请求URL
	    saveForm: routing.saveForm,
	    saveWinWidth: 650,        
    	saveWinHeight: 500,    
//    	viewConfig:null,   
		fields: [{
			header:i18n.routing.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:i18n.routing.routingCode, dataIndex:'routingCode', width: 60, editor:{ },
			searcher: {xtype: 'textfield'}
		},{
			header:i18n.routing.trainNumber, dataIndex:'strips', hidden:true, editor: { xtype:'hidden' }
		},{
			header:i18n.routing.startingStation, dataIndex:'startingStation', editor:{  }
		},{
			header:i18n.routing.forwardStation, dataIndex:'leaveOffStation'
		},{
			header:i18n.routing.duration, dataIndex:'duration', 
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
		},{
			header:i18n.routing.departureTime, dataIndex:'departureTime',
			searcher:{disabled: true}
		},{
			header:i18n.routing.arrivalTime, dataIndex:'arrivalTime' 
		},{
			header:i18n.routing.departBackTime, dataIndex:'departureBackTime',
			searcher:{disabled: true}
		},{
			header:i18n.routing.arrivalBackTime, dataIndex:'arrivalBackTime' 
			
		},{
			header:i18n.routing.kilometers, dataIndex:'kilometers', editor:{ maxLength:10,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:i18n.routing.remark, dataIndex:'remark',  width: 260, editor:{ xtype:'textarea', maxLength:1000 },
			searcher:{ disabled:true }
		}],
		// 保存成功后的函数处理
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
            // 回显字段值
            var entity = result.entity;
	    	if (routing.isSaveAndAdd) {
			    this.saveForm.getForm().reset();
		    	this.afterShowSaveWin();
	    	} else {
	            this.saveForm.find('name', 'idx')[0].setValue(entity.idx);
	    	}
	    },
	    afterShowEditWin: function(record, rowIndex){
	    	// 设置“工期”字段
	    	var duration = record.get('duration');
	    	if (!Ext.isEmpty(duration) && duration > 0) {
				var duration_h = Math.floor(duration/60);
				var duration_m = duration%60;
				// 设置 额定工期_时
				if (duration_h > 0)
					Ext.getCmp("duration_H").setValue(duration_h);
				// 设置 额定工期_分
				if (duration_m > 0)
					Ext.getCmp("duration_M").setValue(duration_m);
	    	}
	    },
	    // 重新保存方法，完善对“duration（分钟）”字段保存时的特殊处理
		beforeSaveFn: function(data){ 
			var duration_h = data.duration_h;
			var duration_m = data.duration_m;
			data.duration = parseInt(duration_h * 60);
			if (!Ext.isEmpty(duration_m)) {
				 data.duration += parseInt(duration_m);
			}
			delete data.duration_h;
			delete data.duration_m;
			return true; 
		},
	    createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
				width: 650, height: 400,
				closeAction: 'hide',
				layout: 'fit',
				modal: false,
				items: [routing.saveForm]
	       	 });
	    },
		searchFn: function(searchParam){ 
			routing.searchParam = searchParam ;
	        this.store.load();
		}
	});
	//查询前添加过滤条件
	routing.grid.store.on('beforeload' , function(){
		var searchParam = routing.searchParam;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:routing.grid });

});