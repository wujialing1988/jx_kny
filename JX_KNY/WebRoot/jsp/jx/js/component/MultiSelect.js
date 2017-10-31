Ext.ns("Ext.yunda.MultiSelect");

//Ext.yunda.MultiSelect.result = function(data, id){};

Ext.yunda.MultiSelect = Ext.extend(Ext.form.TriggerField, {
	/****************************
	 * TriggerField框的配置项 
	 ****************************/
	displayField : '',	 	//【必填】控件triggerField中的显示字段名称 ●
	hiddenField : '',		//控件triggerField中的隐藏域名称 ●
	valueField : '', 		//【必填】控件triggerField中的值字段名称 ●
	editable : false,		//控件triggerField的文本框是否可以接受输入 ●
	returnField : [], 		//需要联动其他控件或页面组件时，其控件ID的数组 ●
	singleSelect : false,	//单选/多选模式切换（后期完成）
	triggerClass : 'x-form-search-trigger', //控件triggerField的样式 ●
	
	/**************************** 
	 * MultiSelectWin的配置项 
	 ****************************/
	multiSelectWinTitle:'',			//选择器窗口Title ●
	multiSelectWin : null,			//选择器窗口对象 ●
	multiSelectWinWidth: 770,		//选择器窗口宽度 ●
	multiSelectWinHeight: 400,		//选择器窗口高度 ●
	
	/********************************** 
	 * SourceGrid与TargetGrid的配置项 
	 **********************************/
	/* soruceGrid部分 */
	sourceGrid:null,		//sourceGrid表格对象(左侧备选列表) ●
	sourceGridId : 'sourceGrid',
	sourceLoadURL: null,	//【必填】sourceGrid数据源源加载路径  ●
	sourceQueryHql: null,	//HQL查询条件
	sourceClassName: null,	//【必填】sourceGrid对应的实体类名称
	sourceFields: null,		//【必填】sourceGrid需要显示的字段
	sourceGridWidth: null,	//sourceGrid表格的宽度
	sourceId:'idx',
	searchParam:{},
	
	/* targetGrid部分 */
	targetGrid:null,		//targetGrid表格对象(右侧已选列表)
	targetGridId : 'targetGrid',
	targetFields: null,		//【必填】targetGrid需要显示的字段
	targetId:'idx',
//	targetInitRecord:null,
	
	/* 中部按钮栏的配置项  */
	centerBtnPanelId : 'btnAreaPanel',
	centerBtnAreaPanel: null,
	centerBtnAreaWidth: 70,
	
	/** 在targetGrid中回显已选项 **/
//	loadTargetGridRecord: function(){
//		if(this.targetInitRecord == null || this.targetInitRecord.length<1) return;
//		var r = null;
//		for(var i=0;i<this.targetInitRecord.length;i++){
//			r = new Ext.data.Record();
//			for(prop in this.targetInitRecord[i]){
//				r.set(prop, this.targetInitRecord[i][prop]);
//			}
//			this.targetGrid.store.add(r);
//		}
//	},
	
	loadTargetGridRecord: function(){
//		alert(this.getValue());
	},
	
	/** 1. 点击控件触发事件 ● */
	onTriggerClick : function() {
		if(this.disabled) return; //如果本控件本身被禁用，则不再触发后续事件 ●
		if(this.beforeOnClickTriggerFn() == false)   return;  //点击本控件触发事件前调用方法并判断是否继续 ●
		if(this.multiSelectWin == null)  this.createMultiSelectWin(); //如果多选窗口等于null，则调用方法自动创建 ●
		if(this.beforeShowMultiSelectWinFn() == false) return; //显示多选窗口前调用的方法， 如果返回false， 则窗口不再显示 ●
		this.multiSelectWin.setTitle(this.multiSelectWinTitle); //设置多选窗口左上角标题 ●
		this.multiSelectWin.show(); //显示多选窗口 ●
		this.loadTargetGridRecord();
		this.afterShowMultiSelectWinFn(); //多选窗口显示后调用的方法 ●
	},
	
	/** 2. 点击Trigger控件触发事件前调用的方法。● */
	beforeOnClickTriggerFn:function(){return true},
	
	/** 3. 创建多选器窗口 ● */
	createMultiSelectWin: function(){
		if(this.sourceGrid == null) this.createSourceGrid(); //创建待选Grid
		if(this.targetGrid == null) this.createTargetGrid(); //创建已选Grid
		if(this.centerBtnAreaPanel == null) this.createBtnAreaPanel(); //创建选择按钮栏Panel
		
		/* 计算窗体宽度开始 */
		//当左侧备选列表的宽度为null时，采用窗口宽度减去选择按钮栏面板宽度/2后计算得出
		if(this.sourceGridWidth == null) {
			this.sourceGridWidth = (this.multiSelectWinWidth - this.centerBtnAreaWidth)/2; 
		} 
		//右侧已选列表的宽度= 窗口宽度 - 选择按钮栏面板宽度 - 左侧备选列表宽度
		var targetGridWidth = this.multiSelectWinWidth - this.centerBtnAreaWidth - this.sourceGridWidth;
		/* 计算窗体宽度结束 */
		
		/* 创建多选器窗口开始*/
        var obj = this;
        this.multiSelectWin = new Ext.Window({
            title: this.multiSelectWinTitle, width:this.multiSelectWinWidth, height:this.multiSelectWinHeight+65, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, modal:true,
            items: new Ext.Panel({
            	layout:'border', height: this.multiSelectWinHeight,  frame: true, bodyBorder: false,  border:false,
            	items: [{region: 'west',  xtype: 'fieldset', title: '可选择',    layout:'fit', width: this.sourceGridWidth,	items:this.sourceGrid},
            	        {region: 'center',  bodyBorder: false, frame: false, layout:'fit', width: this.centerBtnAreaWidth, items: this.centerBtnAreaPanel},
            	        {region: 'east',  xtype: 'fieldset', title: '已选择',   layout:'fit', width: targetGridWidth, items:this.targetGrid}]
            }),  
            buttons: [{
                text: "确定", iconCls: "yesIcon", scope: this, handler: this.saveFn
            }, {
                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.multiSelectWin.hide(); }
            }],
            listeners : {
            	show : function (){
            		//obj.targetGrid.store.removeAll();
            		obj.sourceGrid.store.load();
            	},
            	/** 当窗口大小发生变化时， 动态重设图表绘图区域的尺寸，并调用方法重绘图形 */
    			resize:function(win, adjWidth, adjHeight, rawWidth, rawHeight){
    				var _a = adjWidth-obj.multiSelectWinWidth; //扩大了多少
    				win.items.items[0].items.items[0].setWidth((obj.sourceGridWidth+(_a/2)));
    				win.items.items[0].items.items[1].setWidth(70);
    				win.items.items[0].items.items[2].setWidth((adjWidth-70-(obj.sourceGridWidth+(_a/2))));
    				
//    				win.items.items[0].items.items[0].setWidth((adjWidth - 70)/2); //左表格宽度
//    				win.items.items[0].items.items[2].setWidth((adjWidth - 70)/2); //右表格宽度
//    				win.items.items[0].items.items[1].items.items[0].items.items[0].setHeight((adjHeight/2-100)); //按钮栏宽度
    				win.items.items[0].setHeight(adjHeight-63); //面板Panel高度
    				win.doLayout();
    			}
            }
        });
        /* 创建多选器窗口结束*/
    },
    
    /** 4. 创建SourceGrid ● */
    createSourceGrid: function(){
    	if(this.sourceLoadURL == null || this.sourceFields == null) return; //未配置数据源路径或页面显示字段，则不创建窗口
    	var obj = this;
    	this.sourceGrid = new Ext.yunda.Grid({
    		id : this.sourceGridId,
    		loadURL: this.sourceLoadURL,
       		storeAutoLoad: false,
    		singleSelect: obj.singleSelect,
    		border: false,
    		tbar: ['&nbsp;',
    		       {xtype:'label',text:'关键字：'},
    		       {xtype:'textfield', emptyText:'请输入搜索条件...' },'&nbsp;','-','&nbsp;',
    		       {text : "搜索", iconCls : "searchIcon", handler : function(){obj.sourceGrid.searchFn();}},
    		       {text : "重置", iconCls : "resetIcon", handler : function(){
    		    		   obj.sourceGrid.getTopToolbar().get(2).setValue(""); //清空查询条件
    		    		   obj.sourceGrid.searchFn();} //重新加载列表
    		       }],
    		fields: this.sourceFields,
    		searchFn : function(searchParam) {
    			obj.sourceGrid.store.load();
    		}
    	});
    	this.sourceGrid.un('rowdblclick', this.sourceGrid.toEditFn, this.sourceGrid); //对双击行编辑事件禁用
    	
    	/* 根据配置，对列表数据排序 */
    	for(var i=0;i<this.sourceFields.length;i++){
    		if(typeof(this.sourceFields[i].hidden)!='undefined' && this.sourceFields[i].hidden == true) continue; //隐藏列不参与排序
    		/* 当某个字段标识为升序排列时，以该字段排序 */
    		if(typeof(this.sourceFields[i].ascSort)!='undefined' && this.sourceFields[i].ascSort == true) {
    			this.sourceGrid.store.setDefaultSort(this.sourceFields[i].dataIndex, 'asc');
    			break;
    		}
    		/* 当某个字段标识为降序排列时，以该字段排序 */
    		if(typeof(this.sourceFields[i].descSort)!='undefined' && this.sourceFields[i].descSort == true) {
    			this.sourceGrid.store.setDefaultSort(this.sourceFields[i].dataIndex, 'desc');
    			break;
    		}
    	}
    	
    	var obj = this;
    	this.sourceGrid.store.on('beforeload', function() {
    		/* 获取查询框中用户输入的搜索条件，并对该Grid中所有非隐藏字段进行模糊匹配 */
    		var whereList = []; 
    		var clParam = new Array();
    		var searchFieldValue = obj.sourceGrid.getTopToolbar().get(2).getValue();//Ext.getCmp('_searchField').getValue();
    		/** 当输入了查询条件时， 将查询条件模糊匹配sourceGrid中显示出来的各列 */
    		if(searchFieldValue != null && searchFieldValue != '') {
	    		for(var i=0; i < obj.sourceFields.length; i++){
	    			if(typeof(obj.sourceFields[i].hidden)!='undefined' && obj.sourceFields[i].hidden == true) continue; //隐藏列不参与查询
	    			if(typeof(obj.sourceFields[i].tableAlias)!='undefined' && obj.sourceFields[i].tableAlias != null ){
	    				clParam.push({propName: obj.sourceFields[i].tableAlias + "." + obj.sourceFields[i].dataIndex, propValue: searchFieldValue, compare:Condition.LIKE});
	    			} else {
	    				clParam.push({propName: obj.sourceFields[i].dataIndex, propValue: searchFieldValue, compare:Condition.LIKE});
	    			}
	    		}
	    		whereList.push({compare:Condition.DISJUNCTION, conditionList:clParam}); //以or关联各查询条件
    		}
    		for(prop in obj.searchParam){
    			whereList.push({propName : prop, propValue : obj.searchParam[prop], compare:Condition.EQ, stringLike:false});
    		}
    		var params = {
    		    start : 0,
    			limit : obj.sourceGrid.pagingToolbar.pageSize,
    			queryClassName : obj.sourceClassName,
    			queryHql : obj.sourceQueryHql,
    		    whereListJson: Ext.util.JSON.encode(whereList)
    		};   
    		this.baseParams = params;
    	});
    },
    /** 5. 创建TargetGrid ● */
    createTargetGrid: function(){
    	if(this.targetFields == null) return;
    	this.targetGrid = new Ext.yunda.Grid({
    		id: this.targetGridId,
    		loadURL: null,
       		storeAutoLoad: false,
    		singleSelect: false,
    		store: new Ext.data.Store(),
    		page:false,
    		border: false,
    		tbar: [{}],
    		fields: this.targetFields
    	});
    },
    /** 7. 点击添加、移除等按钮时执行的方法 ● */
    clickSelectBtnFn: function(param){
    	var records; //用户所勾选的所有数据项数组
    	var _record; //临时存放records中的元素
    	var _source; //添加或移除的来源Grid
    	var _target; //添加或移除的目标Grid
    	if(param == 'addAll' || param == 'addFirst') {
    		_source = this.sourceGrid; //当前操作类型为添加时，_source是sourceGrid
    		_target = this.targetGrid; //当前操作类型为添加时，_target是targetGrid
    	}else if(param == 'removeAll' || param == 'removeFirst') {
    		_source = this.targetGrid; //当前操作类型为移除时，_source是targetGrid
    		_target = this.sourceGrid; //当前操作类型为移除时，_target是sourceGrid
    	}
    	records = _source.selModel.getSelections(); //获取所勾选的数据项列表
    	if(records == null || records.length<1){
    		MyExt.Msg.alert("尚未选择一条记录！");
    		return;
    	} //提示需勾选记录
    	
    	var isExist = false;
		for(var i=0;i<records.length;i++){
			_record = records[i]; //将records的元素放入_record中
			if(param == 'addAll' || param == 'addFirst') {
				isExist = false;
				if(this.sourceId!=null && typeof(this.sourceId) != undefined 
						&& this.targetId != null && typeof(this.targetId)!= undefined 
							&& this.sourceId != '' && this.targetId != '') {
					for(var j=0;j<_target.getStore().getCount();j++){
						if(_record.get(this.sourceId) == _target.getStore().getAt(j).get(this.targetId)) {
							isExist = true;
							break;
						}
					}
				}
				if(isExist) continue; //所选数据已经添加， 不再重复加入
				_target.store.add(_record); //将_record添加至_target所对应的Grid(添加操作时是targetGrid，移除时是sourceGrid)
				_source.getSelectionModel().clearSelections(); //清除已选项的勾
			} else {
				_source.store.remove(_record); //将_record从_source所对应的Grid中移除(添加操作时是sourceGrid，移除时是targetGrid)
			}
			if(param == 'addFirst' || param == 'removeFirst') break; //如果是单项添加动作，则只执行第一条数据
		}
		_source.getSelectionModel().clearSelections(); //清除已选项的勾
    },
    
    /** 6. 创建按钮栏Panel ● */
    createBtnAreaPanel: function(){
    	this.centerBtnAreaPanel = new Ext.Panel({
    		id: this.centerBtnPanelId,
    		layout: 'border',
    		baseCls: "x-plain",
    		frame: false, bodyBorder: false,  border:false,
    		items:[
    		    {region:'north', height:140, items: null},
    		    {region:'west', layout:'fit', width:9, items: null},
    		    {region:'center',
    			 items: [{
    				 xtype:'toolbar',border:false, width:26,items:[{iconCls : "page-last",	scope: this, handler: function(){this.clickSelectBtnFn('addAll')}}]
    			},{
    			/*	xtype:'toolbar',border:false, width:26,items:[{iconCls : "nextIcon",	scope: this, handler: function(){this.clickSelectBtnFn('addFirst')}}]
    			},{
    				xtype:'toolbar',border:false, width:26,items:[{iconCls : "prevIcon",	scope: this, handler: function(){this.clickSelectBtnFn('removeFirst')}}]
    			},{*/
    				xtype:'toolbar',border:false, width:26,items:[{iconCls : "page-first",scope: this, handler: function(){this.clickSelectBtnFn('removeAll')}}]}]
    			}
    		]
    	});
    },
    
	/** 进入多选控件窗口之前触发的函数，如果返回false将不显示该窗口 ● */
    beforeShowMultiSelectWinFn: function(){
    	return true;
    },
    /** 进入多选控件窗口之后触发的函数，如果返回false将不显示该窗口 ● */
    afterShowMultiSelectWinFn: function(){},
    
    /**
     * 保存按钮触发的函数，执行数据数据保存动作
     */
    saveFn: function(){
    	var dataAry = this.targetGrid.getStore().data.items; //获取targetGrid的所有数据项
    	if(dataAry.length<1){
    		MyExt.Msg.alert("请从左侧列表中选择数据！"); return; //未选择任何数据，给出提示
    	} 
    	var r = new Ext.data.Record();
    	var _ary;
    	for(var i=0;i<this.targetFields.length;i++){
    		_ary = new Array();
    		for(var j=0;j<dataAry.length;j++){
    			_ary.push(dataAry[j].get(this.targetFields[i].dataIndex));
    		}
    		r.set(this.targetFields[i].dataIndex,_ary); //构造record， 键为列名，值是该列名下的所有数据组织的数组
    	}
    	this.setValue(r);
    	this.setReturnValue(r);
    	this.multiSelectWin.hide();
    	this.complete(dataAry, this.id);
    },
	
    /************************************************************************************/
	initComponent : function() {
		Ext.yunda.MultiSelect.superclass.initComponent.call(this);
	},
	complete: function(dataAry, id){},
	submitValue : undefined,
	
	onRender : function(ct, position) {
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		Ext.yunda.MultiSelect.superclass.onRender.call(this, ct, position);
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
		Ext.yunda.MultiSelect.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	
	constructor : function(options) {
		Ext.apply(this, options);
		Ext.yunda.MultiSelect.superclass.constructor.call(this);
		//this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, _r) {
		this.setValue(_r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName || Ext.yunda.MultiSelect.superclass.getName.call(this);
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
			Ext.yunda.MultiSelect.superclass.setValue.call(this, r.get(this.displayField));
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
    
    /** 控件tiggerField回显值， 
     *  参数说明：
     *  	valueFields：该控件的隐藏提交值，通常为idx; 
     *  	displayFields：该控件的显示值, 
     *  	recordFields:在已选列表中显示的已选项 
     *  */
    setDisplayValue: function(valueFields, displayFields, recordFields){
    	/* 回显trigger框的值 */
    	var r = new Ext.data.Record();
    	r.set(this.valueField, valueFields);
    	r.set(this.displayField, displayFields);
    	this.setValue(r);
    	
    	/* 回显已选列表的值 */
    	if(this.targetGrid != null) this.targetGrid.store.removeAll();
//    	this.targetInitRecord = recordFields;
    	this.loadTargetGridRecord(recordFields);
    },
    
    //清空(new)
    clearValue :  function(){
    	var r = new Ext.data.Record();
    	r.set(this.valueField, "");
    	r.set(this.displayField, "");
//    	this.hiddenField = null;
    	r.set(this.hiddenField,"");
		this.setValue(r);
		this.targetInitRecord = null;
    }
});

Ext.reg('MultiSelect', Ext.yunda.MultiSelect);