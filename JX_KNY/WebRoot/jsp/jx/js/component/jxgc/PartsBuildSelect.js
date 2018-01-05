Ext.ns('jx.jxgc.PartsBuildSelect');
jx.jxgc.PartsBuildSelect.buildUpTypeDesc = "";
jx.jxgc.PartsBuildSelect.buildUpTypeName = "";
jx.jxgc.PartsBuildSelect.chartNo = "";
//jx.jxgc.PartsBuildSelect.buildUpTypeIdx = "";
//jx.jxgc.PartsBuildSelect.fBuildUpTypeIDX = "";
//配件列表
PartsBuild_List = Ext.extend(Ext.yunda.Grid, {
	parentObj:null,
	constructor : function() {
		PartsBuild_List.superclass.constructor.call(this, {
			viewConfig : {
				forceFit : false
			},
			singleSelect: true,
			loadURL: ctx + '/partsBuildSelect!buildUpTypeList.action',
			tbar : [{
				xtype:"combo",
				hiddenName:"queryType",
				store: new Ext.data.SimpleStore({
					fields: ["type"],
					data: [ [i18n.PartsBuildSelect.byName], [i18n.PartsBuildSelect.byType],[i18n.PartsBuildSelect.byPicNo], [i18n.PartsBuildSelect.byProfessType] ]
				}),
				displayField:"type",
				width: 110,
				valueField:"type",
				value:i18n.PartsBuildSelect.byName,
//				id:"PartsTypeByBuild_queryTypeId",
				mode:"local",
				editable: false,
				triggerAction: "all"
			},{	            
                xtype:"textfield",								                
                name : "parts",
		        width: 200/*,
                id:"PartsTypeByBuild_partsId"*/
			},{
				text : i18n.PartsBuildSelect.search,
				iconCls : "searchIcon",
				handler : function(){
					var querytype = this.getTopToolbar().get(0).getValue();
					var searchText = this.getTopToolbar().get(1).getValue();
					if(querytype ==i18n.PartsBuildSelect.byType){
						var searchParam = {};
						searchParam.buildUpTypeName = searchText;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
						this.store.load();
					}else if(querytype == i18n.PartsBuildSelect.byName){
						var searchParam = {};
						searchParam.buildUpTypeDesc = searchText;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
						this.store.load();
					}else if(querytype == i18n.PartsBuildSelect.byPicNo){
						var searchParam = {};
						searchParam.chartNo = searchText;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
						this.store.load();
					}else if(querytype == i18n.PartsBuildSelect.byProfessType){
						var searchParam = {};
						searchParam.professionalTypeName = searchText;
						this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
						this.store.load();
					}
					
				},
				title : i18n.PartsBuildSelect.searchBy,
				scope : this
			},{
				text : i18n.PartsBuildSelect.reset,
				iconCls : "resetIcon",
				handler : function(){
					var searchParam = {};					
					this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
					this.store.load();
					this.getTopToolbar().get(1).setValue("");
					this.getTopToolbar().get(0).setValue(i18n.PartsBuildSelect.byName);
				},
				scope : this
			}],
			fields: [{
				header:i18n.PartsBuildSelect.id, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.professionalTypeIdx, dataIndex:'professionalTypeIdx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpTypeDesc, dataIndex:'buildUpTypeDesc', width: 100
			},{
				header:i18n.PartsBuildSelect.Specification, dataIndex:'buildUpTypeName', width: 130
			},{
				header:i18n.PartsBuildSelect.chartNo, dataIndex:'chartNo', width: 50
			},{
				header:i18n.PartsBuildSelect.professType, dataIndex:'professionalTypeName', width: 60
			},{
				header:i18n.PartsBuildSelect.assignPartsTIdKey, dataIndex:'partsClassIdx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.classificationName, dataIndex:'className', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.materialCoding, dataIndex:'matCode', hidden:true,editor:{ xtype:'hidden'}
			},{
				header:i18n.PartsBuildSelect.buildUpTypeDesc, dataIndex:'partsName', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.Specification, dataIndex:'specificationModel', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.SpecificationCoding, dataIndex:'specificationModelCode', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpTypeIDX, dataIndex:'buildUpTypeIDX', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpTypeCode, dataIndex:'buildUpTypeCode', hidden:true, editor:{  xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.partsTypeIDX, dataIndex:'partsTypeIDX', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.isVirtual, dataIndex:'isVirtual', width: 80
			},{
				header:'fBuildUpTypeIDX', dataIndex:'fBuildUpTypeIDX', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'buildUpClass', dataIndex:'buildUpClass', hidden:true, editor: { xtype:'hidden' }
			}],
			toEditFn: function(grid, rowIndex, e){},
			listeners:{
				//单击事件
				"rowclick": {
					fn: function(grid, rowIndex, e){
						var record = grid.store.getAt(rowIndex);
						jx.jxgc.PartsBuildSelect.buildUpTypeDesc = record.get("buildUpTypeDesc");
						jx.jxgc.PartsBuildSelect.buildUpTypeName = record.get("buildUpTypeName");
						jx.jxgc.PartsBuildSelect.chartNo = record.get("chartNo");
						jx.jxgc.PartsBuildSelect.isVirtual = record.get("isVirtual");
						jx.jxgc.PartsBuildSelect.partsName = record.get("partsName");
						jx.jxgc.PartsBuildSelect.specificationModel = record.get("specificationModel");
						if(!this.parentObj.placeGrid.bind){
							this.parentObj.placeGrid.store.on("beforeload", function(){	
								var searchParam = {};
								searchParam.buildUpTypeIdx = record.get("buildUpTypeIDX");
								searchParam.fBuildUpTypeIDX = record.get("fBuildUpTypeIDX");
								var whereList = [] ;
								for (prop in searchParam) {									
							        whereList.push({propName:prop, propValue: searchParam[prop]});
								}
								this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
							});
							this.parentObj.placeGrid.bind = true;
						}
//						jx.jxgc.PartsBuildSelect.buildUpTypeIdx = record.get("buildUpTypeIDX");
//						jx.jxgc.PartsBuildSelect.fBuildUpTypeIDX = record.get("fBuildUpTypeIDX");
						this.parentObj.placeGrid.store.load();
					}
				}				
			}
		});
	}
});
//配件位置列表
PartsPlace_List = Ext.extend(Ext.yunda.Grid, {
	parentObj:null,
	constructor : function() {
		PartsPlace_List.superclass.constructor.call(this, {
			loadURL: ctx + '/buildUpTypeCaseTest!pageQuery.action',
			storeAutoLoad: false,
			page: false,                                         
    		pageSize: 500, 
    		sortInfo : {field:'buildUpPlaceFullName',direction:'ASC'},
			tbar: [{
				text : i18n.PartsBuildSelect.confirm,
				iconCls : "saveIcon",
				handler : function(){
					var isSingSelect = this.parentObj.parentObj.isSingSelect;
					var grid = this;
	    			if(!$yd.isSelectedRecord(grid)) return;
	    			var data = grid.selModel.getSelections();
	    			var dataAry = [];
	    			//单选
					if(isSingSelect == true){
						if(data.length > 1){
		    				MyExt.Msg.alert(i18n.PartsBuildSelect.onlyOne);
							return;
		    			}
					}
					for(var i = 0; i < data.length;i++){
						var record = data[i];
	        			var newRecord = record.copy();
						newRecord.set("buildUpTypeDesc", jx.jxgc.PartsBuildSelect.buildUpTypeDesc);
						newRecord.set("buildUpTypeName", jx.jxgc.PartsBuildSelect.buildUpTypeName);
						newRecord.set("chartNo", jx.jxgc.PartsBuildSelect.chartNo);
						newRecord.set("isVirtual", jx.jxgc.PartsBuildSelect.isVirtual);
	        			dataAry.push(newRecord.data);
	        			data[i].set("buildUpTypeDesc", jx.jxgc.PartsBuildSelect.buildUpTypeDesc);
						data[i].set("buildUpTypeName", jx.jxgc.PartsBuildSelect.buildUpTypeName);
						data[i].set("chartNo", jx.jxgc.PartsBuildSelect.chartNo);
						data[i].set("isVirtual", jx.jxgc.PartsBuildSelect.isVirtual);
						data[i].set("partsName", jx.jxgc.PartsBuildSelect.partsName);					
						data[i].set("specificationModel", jx.jxgc.PartsBuildSelect.specificationModel);					
					}
					this.parentObj.parentObj.setValue(data[0]);
					this.parentObj.parentObj.setReturnValue(data[0]);
	            	this.parentObj.parentObj.fireEvent('select', dataAry);
	            	this.parentObj.close();
				},
				scope : this
			}],
			fields: [{
				header:i18n.PartsBuildSelect.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'fBuildUpTypeIDX', dataIndex:'fBuildUpTypeIDX', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'pBuildUpTypeIDX', dataIndex:'pBuildUpTypeIDX', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'buildUpPlaceIdx', dataIndex:'buildUpPlaceIdx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'buildUpTypeIdx', dataIndex:'buildUpTypeIdx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpPlaceFullCode, dataIndex:'buildUpPlaceFullCode', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpPlaceFullName, dataIndex:'buildUpPlaceFullName'
			},{
				header:'buildUpClass', dataIndex:'buildUpClass', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'isFirstLevel', dataIndex:'isFirstLevel', hidden:true,editor:{ xtype:'hidden'}
			},{
				header:'isLastVsersion', dataIndex:'isLastVsersion', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpTypeDesc, dataIndex:'buildUpTypeDesc', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.buildUpTypeName, dataIndex:'buildUpTypeName', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.PartsBuildSelect.chartNo, dataIndex:'chartNo', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'isVirtual', dataIndex:'isVirtual', hidden:true, editor: { xtype:'hidden' }
			}],
			toEditFn: function(grid, rowIndex, e){}/*,
			beforeload: function(){
				var searchParam = {};
				searchParam.buildUpTypeIdx = jx.jxgc.PartsBuildSelect.buildUpTypeIdx;
				searchParam.fBuildUpTypeIDX = jx.jxgc.PartsBuildSelect.fBuildUpTypeIDX;
				var whereList = [] ;
				for (prop in searchParam) {									
			        whereList.push({propName:prop, propValue: searchParam[prop]});
				}
				this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
			}*/
		});
	}
});
/** ****************************************************************************************** */
//弹出窗口
PartsBuild_SelectWin = Ext.extend(Ext.Window, {
	buildGrid : new PartsBuild_List(),
	placeGrid : new PartsPlace_List(),
	parentObj:null,
	modal:true,
	// private
    beforeShow : function(){
    	this.buildGrid.parentObj = this;
    	this.placeGrid.parentObj = this;
    	this.placeGrid.store.removeAll();
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
		PartsBuild_SelectWin.superclass.constructor.call(this, {
			title : i18n.PartsBuildSelect.SpecificationChoice,
			width : 850,
			height : 305,			
			plain : true,
			closeAction : "hide",
			layout : "border",
			items : [ new Ext.Panel({
							region : "west",
							width:465,
							layout : "fit",
							border : false,
							items : this.buildGrid
						}), new Ext.Panel({
							region : "center",
							layout : "fit",
							items : this.placeGrid
						})]

				});
		this.addEvents("submit");
	},
	close : function() {
		this.hide();
	}
	
});
/** ****************************************************************************************** */
PartsBuild_Select = Ext.extend(Ext.form.TriggerField, {
	valueField : 'buildUpTypeIdx',
	displayField : 'buildUpTypeDesc',
	hiddenName : 'PartsBuild_SelectId',
	queryHql:'',
	editable :false,
	isSingSelect: false,//是否单选，默认为多选
	win : new PartsBuild_SelectWin(),
	triggerClass:'x-form-search-trigger',			
	returnField:[],
	//点击控件触发事件
	onTriggerClick : function() {
		if(!this.disabled){
			this.win.parentObj = this;
			this.win.queryHql = this.queryHql;
			this.win.show(this.el);
		}		
	},
	initComponent : function() {
		PartsBuild_Select.superclass.initComponent.call(this);
	},
	submitValue : undefined,
	
	onRender : function(ct, position) {				
		if (this.hiddenName && typeof this.submitValue=='undefined') {
			this.submitValue = false;
		}
		PartsBuild_Select.superclass.onRender.call(this, ct, position);
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
		PartsBuild_Select.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = Ext.value(typeof this.hiddenValue!='undefined'
							? this.hiddenValue
							: this.value, '');
		}
	},
	constructor : function(options) {
		Ext.apply(this, options);
		PartsBuild_Select.superclass.constructor.call(this);
		this.win.on("submit", this.setOnSubmit, this);
	},
	
	setOnSubmit : function(_win, r) {
		this.setValue(r);
	},
	
	getName : function() {
		var hf = this.hiddenField;
		return hf && hf.name ? hf.name : this.hiddenName
				|| PartsBuild_Select.superclass.getName.call(this);
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
			PartsBuild_Select.superclass.setValue.call(this, r.get(this.displayField));
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
Ext.reg('PartsBuild_SelectWin', PartsBuild_Select);