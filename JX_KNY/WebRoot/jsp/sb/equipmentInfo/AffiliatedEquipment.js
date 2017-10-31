/**
 * 设备主要信息-附属设备 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.ns('AffiliatedEquipment');//定义命名空间
Ext.onReady(function(){

	var baseForm;
	var baseGrid;
	var baseWin;
	var equipmentIdx;
	
	
	function createForm(){
		//附属设备操作页面，主配件基本信息
		baseForm = new Ext.form.FormPanel({
			padding: 10,
			layout: "column",
			labelWidth:65,
			border: false,
			baseCls:"x-plain",
			style: "padding:5px",
			defaults:{
				layout:'form', 
				columnWidth: 0.25,
				defaultType:'displayfield',
				defaults:{
					anchor:'100%'
				}
			},
			items:[{
				baseCls:"x-plain",
	            items: [{
	            	name:"idx", xtype: "hidden"
	            },{
	            	fieldLabel:"主设备类别", style: "color:blue", name:"className"
	            },{
	            	fieldLabel:"主设备编号", style: "color:blue", name:"equipmentCode"
	            },{
	            	fieldLabel:"出厂编号", style: "color:blue", name:"leaveFactoryNo"
	            },{
	            	fieldLabel:"技术等级", style: "color:blue", name:"tecLevel"
	            }]
			},{
				baseCls:"x-plain",
	            items: [{
	            	fieldLabel:"固资编号", style: "color:blue", name:"fixedAssetNo"
	            },{
	            	fieldLabel:"主设备名称", style: "color:blue", name:"equipmentName"
	            },{
	            	fieldLabel:"机械系数", style: "color:blue", name:"mechanicalCoefficient", vtype: "nonNegativeFloat", maxLength: 5
	            },{
	            	fieldLabel:"固资原值", style: "color:blue", name:"fixedAssetValue"
	            }]
			}, {
				baseCls:"x-plain",
	            items: [{
	            	fieldLabel:"制造工厂", style: "color:blue", name:"makeFactory"
	            },{
	            	fieldLabel:"型号", style: "color:blue", name:"model"
	            },{
	            	fieldLabel:"电气系数", style: "color:blue", name:"electricCoefficient", vtype: "nonNegativeFloat", maxLength: 5
	            },{
	            	fieldLabel:"固资净值", style: "color:blue", name:""
	            }]
			},{
				baseCls:"x-plain",
	            items: [{
	            	fieldLabel:"制造年月", style: "color:blue", name:"makeDate"
	            },{
	            	fieldLabel:"规格", style: "color:blue", name:"specification"
	            },{
	            	fieldLabel:"电气总功率", style: "color:blue", name:"eletricTotalPower"
	            },{
	            	fieldLabel:"使用年月", style: "color:blue", name:"useDate"
	            }]
			}]
		});
	}

	function createGrid(){
		//附属配件表格
		baseGrid = new Ext.yunda.Grid({
		    loadURL: ctx + '/affiliatedEquipment!pageList.action',                 // 装载列表数据的请求URL
		    saveURL: ctx + '/affiliatedEquipment!saveOrUpdate.action',             // 保存数据的请求URL
		    deleteURL: ctx + '/affiliatedEquipment!logicDelete.action',            // 删除数据的请求URL
		    saveFormColNum:2, modalWindow:true, viewConfig:null,
		    tbar:['add','delete'],
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'主设备主键', dataIndex:'equipmentIdx', hidden:true, editor: {xtype: "hidden"}
			},{
				header:'设备编号', dataIndex:'equipmentNo', editor:{  maxLength:20, allowBlank: false }
			},{
				header:'设备名称', dataIndex:'equipmentName', editor:{  maxLength:15, allowBlank: false }
			},{
				header:'设备型号', dataIndex:'modal', editor:{  maxLength:10, allowBlank: false }
			},{
				header:'设备规格', dataIndex:'specification', editor: specification("aff_specification_id", "设备规格")
			},{
				header:'机械系数', dataIndex:'mechanicalcoefficient', editor:{ xtype:'numberfield',vtype: "nonNegativeInt", maxLength: 3 }
			},{
				header:'电气系数', dataIndex:'electriccoefficient', editor:{ xtype:'numberfield' ,vtype: "nonNegativeInt", maxLength: 3}
			},{
				header:'数量', dataIndex:'count', editor:{ xtype:'numberfield',vtype: "nonNegativeInt", maxLength: 3 }
			},{
				header:'计量单位', dataIndex:'unit', editor:{  maxLength:4}
			},{
				header:'功率', dataIndex:'power', editor:{ xtype:'numberfield' ,vtype: "nonNegativeInt", maxLength: 5}
			},{
				header:'单价', dataIndex:'price', editor:{ xtype:'numberfield', fieldLabel: "单价（元）" ,vtype: "positiveFloat", maxLength: 7}
			},{
				header:'生产厂家', dataIndex:'makeFactory', editor:{}
			},{
				header:'备注', dataIndex:'remark', editor:{ xtype:'textarea', maxLength:100 }
			}],
			afterShowSaveWin: function(){
				this.saveWin.setTitle('新增-附属设备');
				this.saveForm.find('name', 'equipmentIdx')[0].setValue(equipmentIdx);
			},
			afterShowEditWin: function(r){
				Ext.getCmp("aff_specification_id").setValue(r.get("specification"));
			}
		});
		// 表格数据加载前的参数设置
		baseGrid.store.on('beforeload', function(){
			this.baseParams.entityJson = Ext.util.JSON.encode({equipmentIdx: equipmentIdx});
		});
	}


	function createWin(){
		
		createForm();
		createGrid();
				
		//附属设备编辑Win
		baseWin = new Ext.Window({
		   	title: "附属设备",
		   	width:900, height:500, 
		   	
		   	maximizable:false, modal: true, maximized: false , plain: true,
		   	
			closeAction: "hide",
			layout: "border", 
			defaults: {
				  layout: "fit", border: false, region : 'center'
			},
			items:[{
		        region : 'north',  height:140,
		        baseCls:"x-plain",
		        items:[ baseForm ]
			 },{
		        items:[ baseGrid ]
			 }],
			 buttonAlign:"center",
			 buttons:[{
				 text: "关闭", iconCls: "closeIcon", handler: function(){
					 baseWin.hide();
				 }
			 }]
		});
	}

	function loadRecord(idx){
		var whereList = [{propName: 'idx', propValue: idx}];
		Ext.Ajax.request({
			url: ctx + '/equipmentPrimaryInfo!pageQuery.action',
			params: {
				whereListJson : Ext.encode(whereList)
			},
			success: function(resp, options){
				var entitys = Ext.util.JSON.decode(resp.responseText);
				var entity = entitys.root[0];
				var record = $yd.getRecord(entity);
				if(entity.useDate)
					record.set("useDate", new Date(entity.useDate).format("Y-m"));
				if(entity.makeDate)
					record.set("makeDate", new Date(entity.makeDate).format("Y-m"));
				baseForm.getForm().loadRecord(record);
			}
		});
	}
	function setButton(disable){
		baseGrid.topToolbar.items.items[0].setDisabled(disable);
		baseGrid.topToolbar.items.items[1].setDisabled(disable);
	}
	// 设备主要信息查看数据
	AffiliatedEquipment.showWin = function(idx, editable){		
		if(baseWin == undefined){
			createWin();
		}
		loadRecord(idx);
		equipmentIdx = idx;
		baseGrid.store.load();
		baseWin.show();
		setButton(!editable);
		
		if(editable == false){
			baseGrid.un("rowdblclick", baseGrid.toEditFn);
		}
		
	} 
});