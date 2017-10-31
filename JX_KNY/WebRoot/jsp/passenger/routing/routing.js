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
				fieldLabel: '出发时间', xtype:"my97date", name: 'departureTime', width: 80, format: "H:i",
		        	my97cfg: {dateFmt:"HH:mm"}		
			}, {
				name:"startingStation", fieldLabel:"出发地"
			}, {
				xtype: 'compositefield', id:'duration_id',fieldLabel: '历时', combineErrors: false, 
				items: [{
					xtype: 'numberfield', id: 'duration_H', name: 'duration_h', width: 60, validator: function(value) {
						if (Ext.isEmpty(value)) {
							return null;
						}
						if (parseInt(value) < 0) {
							return "请输入正整数";
						}
						var mValue = Ext.getCmp('duration_M').getValue();
						if (Ext.isEmpty(value) && Ext.isEmpty(mValue)) {
							return '工期不能为空'
						} else {
							if (value.length > 2) {
								return "该输入项最大长度为2";
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
							return "请输入正整数";
						}
						var hValue = Ext.getCmp('duration_H').getValue();
						if (Ext.isEmpty(value) && Ext.isEmpty(hValue)) {
							return '工期不能为空'
						} else {
							if (parseInt(value) >= 60) {
								return "不能超过60分钟";
							} else if (hValue.length <= 2){
								Ext.getCmp('duration_H').clearInvalid();
								Ext.getCmp('duration_M').clearInvalid();
							}
						}
					}
				}, {
					xtype: 'label',
					text: '分',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}]
			}, {
				fieldLabel: '返程发车时间', xtype:"my97date", name: 'departureBackTime', width: 80, format: "H:i",
		        	my97cfg: {dateFmt:"HH:mm"}		
//			}, {
//				name:"strips", fieldLabel:"车次"
			}]
		}, {
			defaults: {xtype:"textfield", maxLength:50, allowBlank: false, anchor:"90%"},
			items:[{
				fieldLabel: '到达时间', xtype:"my97date", allowBlank: false, name: 'arrivalTime', width: 80, format: "H:i",
			        	my97cfg: {dateFmt:"HH:mm"}		
			}, {
				name:"leaveOffStation", fieldLabel:"前往地"
			}, {
				name:"kilometers", xtype: 'numberfield', fieldLabel:"往返行程（KM）"
			}, {
				fieldLabel: '返程到达时间', xtype:"my97date", name: 'arrivalBackTime', width: 80, format: "H:i",
	        	my97cfg: {dateFmt:"HH:mm"}		
			}]
		}, {
			columnWidth:1,
			items:[{
				xtype:"textarea", name:"remark", fieldLabel:"备注", maxLength:500, anchor:"97%", height: 55
			}]
		}, {
			// 【作业节点】保存表单的隐藏字段
			columnWidth:1,
			defaultType:'hidden',
			items:[
				{ fieldLabel:"idx主键", name:"idx" }
			]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: function() {
				routing.isSaveAndAdd = false;
				routing.grid.saveFn();
			}
		}, {
			text: '保存并新增', iconCls: 'addIcon', handler: function() {
				routing.isSaveAndAdd = true;
				routing.grid.saveFn();
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
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
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'routingCode', width: 60, editor:{ },
			searcher: {xtype: 'textfield'}
		},{
			header:'车次', dataIndex:'strips', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'出发地', dataIndex:'startingStation', editor:{  }
		},{
			header:'前往地', dataIndex:'leaveOffStation'
		},{
			header:'历时', dataIndex:'duration', 
			renderer: function(value){
				if (!Ext.isEmpty(value)) {
					return formatTimeForHours(value, 'm');
				}
			}
		},{
			header:'出发时间', dataIndex:'departureTime',
			searcher:{disabled: true}
		},{
			header:'到达时间', dataIndex:'arrivalTime' 
		},{
			header:'返程发车时间', dataIndex:'departureBackTime',
			searcher:{disabled: true}
		},{
			header:'返程到达时间', dataIndex:'arrivalBackTime' 
			
		},{
			header:'往返行程（KM）', dataIndex:'kilometers', editor:{ maxLength:10,allowBlank: true },
			searcher: {disabled: true}
		},{
			header:'备注', dataIndex:'remark',  width: 260, editor:{ xtype:'textarea', maxLength:1000 },
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