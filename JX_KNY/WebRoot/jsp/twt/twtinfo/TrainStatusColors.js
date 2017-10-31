 /**
  * 自定义颜色选择控件
  * @class
  * @extends Ext.form.TriggerField
  */
 var ColorSelector = Ext.extend(Ext.form.TriggerField, {

	constructor : function(config) {
		ColorSelector.superclass.constructor.call(this, config);
	},
	hiddenName : 'color',
	name : 'color',
//	editable : false,
	win : null,

	fieldLabel : '颜色',
	vtype: 'color',

	onTriggerClick : function() {
		if (!this.win)
			this.createWin()
		this.win.show();
	},
	
	createWin : function() {
		this.win = new Ext.Window({
			parentObj : this,
			
			title : '颜色选择',
			height : 198, width : 200, padding : 20,
			modal : true,
			closeAction : 'hide',
			buttonAlign : 'center',
			items : [{
				xtype : 'colorpalette'
			}],
			buttons : [{
				text : '确定',
				iconCls : 'yesIcon',
				handler : function() {
					var window = this.findParentByType('window');
					var color = "#" + window.items.items[0].value;
					// 设置回显值
					Ext.form.TriggerField.superclass.setValue.call(window.parentObj, color);
					// 隐藏窗口
					window.hide();
				}
			}, {
				text : '关闭',
				iconCls : 'closeIcon',
				handler : function() {
					this.findParentByType('window').hide();
				}
			}]
		});
	}
});
// 注册控件
Ext.reg('colorselector', ColorSelector);
 
/**
 * 机车状态颜色 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainStatusColors');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	TrainStatusColors.labelWidth = 100;
	TrainStatusColors.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	TrainStatusColors.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainStatusColors!pageList.action',                //装载列表数据的请求URL
	    saveURL: ctx + '/trainStatusColors!saveOrUpdate.action',            //保存数据的请求URL
	    deleteURL: ctx + '/trainStatusColors!delete.action',            	//删除数据的请求URL
	    
	    labelWidth: TrainStatusColors.labelWidth,  
    	fieldWidth: TrainStatusColors.fieldWidth,    
    	
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'状态名称', dataIndex:'status', editor:{  maxLength:50, allowBlank: false }
		},{
			header:'颜色(RGB值)', dataIndex:'color', editor:{  maxLength:50, allowBlank: false }, 
			editor: {
				xtype: 'colorselector',
				listeners: {
					valid: function(me) {
						var form = this.findParentByType('form');
						var previewField = form.find('name', 'preview')[0];
						if (previewField) previewField.el.setStyle('background', this.getValue());
					}
				}
			},
			renderer: function(value) {
				return [
					'<div style="height:18px;line-height:18px;float:left">',
					value,
					'</div><div style="box-shadow:2px 2px 2px gray;border-radius:10px;float:right;height:18px;width:100px;font-weight:bold;background-color:',
					value,
					';"></div>'
	
				].join("");
			}
		},{
			header:'排序号', align:'center', dataIndex:'seqNo', editor:{ xtype:'numberfield' }
		}],
		
		// 动态添加颜色预览区域
		afterShowEditWin: function(record, rowIndex){
			var previewField = this.saveForm.find('name', 'preview')[0];
			if (!previewField) {
				this.saveForm.insert(3, {
					readOnly: true,
					xtype: 'textfield', fieldLabel: '颜色预览', name: 'preview',
					style: 'box-shadow:2px 2px 2px gray;border-radius:10px;background:' + record.get('color') + ";border:" + record.get('color') + ";",
					width: TrainStatusColors.fieldWidth
				});
				this.saveForm.doLayout();
			}
		},
		
		// 删除颜色预览字段产生的冗余数据
		beforeSaveFn: function(data){ 
			delete data.preview;
			return true; 
		}
	});
	
	// 给每条行记录添加一个背景色
//	TrainStatusColors.grid.store.on("load",function(r){
//		var rowIndex = 0;
//		this.each(function(r){		
//			if (!Ext.isEmpty(r.get('color')))
//				TrainStatusColors.grid.getView().getRow(rowIndex).style.backgroundColor = r.get('color');
//				console.log(TrainStatusColors.grid.getView().getRow(rowIndex));
//			rowIndex++;
//		});								
//	});
	TrainStatusColors.grid.store.setDefaultSort('seqNo', 'ASC');
	//页面自适应布局
	new Ext.Viewport({ layout:'fit', items:TrainStatusColors.grid });
	
});