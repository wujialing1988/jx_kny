/**
 * 工位选择控件
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/jxgc/WorkStationSelect.js"></script>
 * 2.在表单页面js添加下例标签代码 
 * { xtype:'WorkStation_SelectWin',editable:false,fieldLabel : 自定义,hiddenName:自定义,returnField:[{widgetId:"自定义",propertyName:"自定义"}]}
 * 3.配置项说明：
 * a.id：控件id，为回显值必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,选填
 * d.editable:是否可编辑，true可编辑，false不可编辑，默认为false
 * e.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * f.valueField配置项：本控件提交值，默认为"idx"，可自定义所需字段
 * g.displayField配置项：本控件显示值，默认为"partsName",可自定义所需字段
 * 回显字段名称列表：参见WorkStation实体类
 * h.queryHql配置项：自定义Hql，暂只接受WorkStation实体类对象查询，必须为带where子句的hql,如"from WorkStation where 1=1",可选
 * I.entity配置项：默认配置为"com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation"，可选 
 * 4.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(valueField,displayField);
 *   componentId 控件id，参数说明：valueField：该控件valueField值、displayField：该控件displayField值
 * 5.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id           
 */


//工位列表
WorkStation_List = Ext.extend(Ext.yunda.Grid, {
	parentObj:null,
	constructor : function() {
		WorkStation_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : false
			},	
			singleSelect: true,
			tbar:[{
				xtype:"combo", hiddenName:"queryType", displayField:"type",editable: false,
		        width: 80, valueField:"type", value:"工位编码", mode:"local",triggerAction: "all",
				store: new Ext.data.SimpleStore({
					fields: ["type"],
					data: [ ["工位编码"], ["工位名称"], ["所属流水线"] ]
				})
			},{	            
		        xtype:"textfield", width: 100
			},{
				text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
				handler : function(){
					var typeName = this.getTopToolbar().get(1).getValue();
					var querytype = this.getTopToolbar().get(0).getValue();					
					var searchParam = {};
					if(querytype == '工位编码'){
						searchParam.workStationCode = typeName;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
					}else if(querytype == '工位名称'){
						searchParam.workStationName = typeName;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
					}else{
						searchParam.repairLineName = typeName;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
					}
					this.store.load();
				},
				scope : this
			},{
				text : "重置",iconCls : "resetIcon",
				handler : function(){
					//清空搜索输入框
					this.getTopToolbar().get(1).setValue("");
					this.getTopToolbar().get(0).setValue("工位编码");
					//清空工位查询集合
					this.store.baseParams.entityJson = Ext.util.JSON.encode({});
					this.store.load();
				},
				scope : this
			}],
			loadURL: ctx + '/workStation!pageListForCmp.action',
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'工位编码', dataIndex:'workStationCode', editor:{ allowBlank:false, maxLength:50 }
			},{
				header:'工位名称', dataIndex:'workStationName', width: 150, editor:{ allowBlank:false, maxLength:100 }
			},{
				header:'流水线主键', dataIndex:'repairLineIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
			},{
				header:'所属流水线', dataIndex:'repairLineName', width: 150, editor:{ xtype:'hidden', maxLength:150 }
			},{
				header:'所属台位编码', dataIndex:'deskCode', hidden:true, editor:{  xtype:'hidden' }
			},{
				header:'所属台位', dataIndex:'deskName', editor:{  maxLength:100 }
			},{
				header:'状态', dataIndex:'status', hidden:true,editor:{  }
			},{
				header:'备注', dataIndex:'remarks',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
			}],
			/**
		     * 在双击表格中记录时（rowdblclick）触发该函数，获取当前选中记录，并将选中记录加载到编辑表单，显示编辑窗口
		     * 在加载数据显示窗口之前，将执行beforeShowEditWin函数，返回false将不显示编辑窗口，中止编辑动作
		     * @param {Ext.yunda.Grid} grid 当前表格对象
		     * @param {Number} rowIndex 选中行下标
		     * @param {Ext.EventObject} e Ext事件对象
		     */    
		    toEditFn: function(grid, rowIndex, e){
		        var r = grid.store.getAt(rowIndex);
        		this.parentObj.parentObj.setValue(r);
        		this.parentObj.parentObj.setReturnValue(r);
        		this.parentObj.close();
		    }
		});
	}
});

/** ****************************************************************************************** */
//弹出窗口
WorkStation_SelectWin = Ext.extend(Ext.Window, {
	grid : new WorkStation_List(),
	parentObj:null,	
	entity:'com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation',
	queryHql:'',
	modal:true,
	// private
    beforeShow : function(){
    	this.grid.parentObj = this;
    	this.grid.getStore().load({
			params:{
				entity:this.entity,
				queryHql:this.queryHql
			}			
		});
		this.grid.getStore().baseParams.entity = this.entity;
		this.grid.getStore().baseParams.queryHql = this.queryHql;
    	//Ext.Window源代码
        delete this.el.lastXY;
        delete this.el.lastLT;
        if(this.x === undefined || this.y === undefined){
            var xy = this.el.getAlignToXY(this.container, 'c-c');
            var pos = this.el.translatePoints(xy[0], xy[1]);
            this.x = this.x === undefined? pos.left : this.x;
            this.y = this.y === undefined? pos.top : this.y;
        }
        this.el.setLeftTop(this.x, this.y);

        if(this.expandOnShow){
            this.expand(false);
        }

        if(this.modal){
            Ext.getBody().addClass('x-body-masked');
            this.mask.setSize(Ext.lib.Dom.getViewWidth(true), Ext.lib.Dom.getViewHeight(true));
            this.mask.show();
        }
    },
	constructor : function() {		
		WorkStation_SelectWin.superclass.constructor.call(this, {
			title : "工位选择",
			width : 550,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "fit",
			items : [this.grid]
		});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
WorkStation_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'idx',
	displayField : 'workStationName',
	hiddenName : 'WorkStation_SelectId',
	entity:'com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation',
	queryHql:'',
	editable :true,			
	win : new WorkStation_SelectWin(),
	triggerClass:'x-form-search-trigger',			
	returnField:[],
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;	
			this.win.entity = this.entity;
			this.win.queryHql = this.queryHql;
			this.win.show(this.el);
		}		
	},
	initComponent : function() {
		WorkStation_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		WorkStation_Select.superclass.onRender.call(this, ct, position);
		if (this.hiddenName) {
			this.hiddenField = this.el.insertSibling({
						tag : 'input',
						type : 'hidden',
						name : this.hiddenName,
						id : (this.hiddenId || Ext.id())
					}, 'before', true);
		}
		if(!this.editable){
            this.editable = true;
            this.setEditable(false);
        }
	},
	
	initValue : function() {
		WorkStation_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		WorkStation_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| WorkStation_Select.superclass.getName.call(this);
	},			
	//获取返回值			
	getValue : function() {				
		return typeof this.value != 'undefined'&& this.value != 'undefined'? this.value : '';
	},
	//设置值，r为Ext.data.Record类型的参数
	setValue : function(r) {				
		if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{					
			if (this.hiddenField && typeof(this.hiddenField) != 'undefined') {
				this.hiddenField.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);
			}
			WorkStation_Select.superclass.setValue.call(this, r.get(this.displayField));
			this.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);				
		}				
		return this;				
	},
	//设置自定义配置项的回显值
	setReturnValue : function(r) {		
		if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{
			var returnField = this.returnField;
	        if(returnField != null && returnField.length > 0){        	
	        	for(var i = 0;i < returnField.length;i++){
					var displaytext = returnField[i].propertyName;
					var fieldName = '';
					if( typeof(r) != 'undefined' && typeof(r.get(displaytext)) != 'undefined'){
						fieldName = r.get(displaytext);
					}							
					if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
						&& fieldName != '' && fieldName != null && fieldName != 'null'){
						Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
					}
					//针对html标签
					else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null 
						&& fieldName != '' && fieldName != null && fieldName != 'null'){
						Ext.get(returnField[i].widgetId).dom.value = fieldName;
					}	        		
	        	}        	
	        }
		}
	},
	//设置是否可编辑
	setEditable : function(value){
        if(value == this.editable){
            return;
        }
        this.editable = value;
        if(!value){
            this.getEl().dom.setAttribute('readOnly', true);
            this.el.on('mousedown', this.onTriggerClick,  this);
            this.el.addClass('x-combo-noedit');
        }else{
            this.el.dom.setAttribute('readOnly', false);
            this.el.un('mousedown', this.onTriggerClick,  this);
            this.el.removeClass('x-combo-noedit');
        }
    },    
    //回显值（new）,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
    setDisplayValue : function(valueField, displayField){
    	var p_record = new Ext.data.Record();
    	p_record.set(this.valueField,valueField);
        p_record.set(this.displayField,displayField);
        this.setValue(p_record);
    },
    //清空(new)
    clearValue :  function(){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		this.setValue(p_record);
    }
});
Ext.reg('WorkStation_SelectWin', WorkStation_Select);